package com.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.testscripts.BaseTest;

import java.util.Objects;

public class DatabaseConnection 
{

	public static Connection makeDBConnection()
	{
		//Reading the Database
		String dbServerIP = ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), "DatabaseDetails", 1, 0);
		String dbInstance = ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), "DatabaseDetails", 1, 1);
		String dbName = ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), "DatabaseDetails", 1, 2);
		String dbUserName = ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), "DatabaseDetails", 1, 3);
		String dbPassword = ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), "DatabaseDetails", 1, 4);

		//composing the URL through which SQL Server connection can be established
		final String strDBURL = "jdbc:sqlserver://" + dbServerIP + ";databaseName=" + dbName + ";instanceName=" + dbInstance + ";user=" + dbUserName + ";password=" + dbPassword;

		Connection conn = null;
		try {
			//STEP 2: Register JDBC driver
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			//STEP 3: Open a connection
			//System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(strDBURL);
			//System.out.println("Connection is successful");
			//return conn;
		} catch (Exception e) {
			//System.out.println("connection failed");
			e.printStackTrace();
		}
		return conn;
	}

	public static Connection makeDBConnection(String dbServerIP, String dbInstance, String dbName, String dbUserName, String dbPassword)
	{
		//composing the URL through which SQL Server connection can be established
		final String strDBURL = "jdbc:sqlserver://" + dbServerIP + ";databaseName=" + dbName + ";instanceName=" + dbInstance + ";user=" + dbUserName + ";password=" + dbPassword;

		Connection conn = null;
		try {
			//STEP 2: Register JDBC driver
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			//STEP 3: Open a connection
			//System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(strDBURL);
			//System.out.println("Connection is successful");
			//return conn;
		} catch (Exception e) {
			//System.out.println("connection failed");
			e.printStackTrace();
		}
		return conn;
	}

	public static ResultSet statementExecution(Connection conn, String strSQLQuery)
	{
		ResultSet rs = null;
		try {
			//System.out.println("SQL Query:\n" + strSQLQuery);
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//Statement stmt = conn.createStatement();
			//System.out.println("Running Query");
			Log.info("Query that will be executed: \n" + strSQLQuery);
			//System.out.println("Start exec stmt:"+GeneralUtils.getDateTime());
			rs = stmt.executeQuery(strSQLQuery);
			//System.out.println("End exec stmt:"+GeneralUtils.getDateTime());
			//System.out.println("Query Exectuted Successful");
		} catch (Exception e) {
			//sSystem.out.println("Query failed to execute");
			e.printStackTrace();
		}
		return rs;
	}

	public static void closeDBResultSet(ResultSet rs, Statement stmt)
	{
		try {
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void closeDBConnection (Connection conn)
	{
		try {
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*public static ArrayList<String> addResultSettoArrayList(ResultSet rs)
	{

		ArrayList<String> al = new ArrayList<String>();
		try {
			while (rs.next()) 
			{ 
				ResultSetMetaData metadata = rs.getMetaData(); 
				int columnCount = metadata.getColumnCount(); 
				String row = ""; 
				for (int i = 1; i <= columnCount; i++) 
				{ 
					row = rs.getString(i); 
					al.add(row);
				}
			}
			Log.info("The SQL Response in a list: \n" + al);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return al;
	}*/

	public static ArrayList<Object> addResultSettoArrayList(ResultSet rs) 
	{ 
		ArrayList<Object> al = new ArrayList<Object>(); 
		try 
		{ 
			while (rs.next()) 
			{ 
				ResultSetMetaData metadata = rs.getMetaData(); 
				int columnCount = metadata.getColumnCount(); 
				Object row = ""; 
				for (int i = 1; i <= columnCount; i++) 
				{ 
					row = rs.getObject(i);
					al.add(row); 
				} 
			}
			Log.info("The SQL Response in a list: \n" + al); 
		} catch (SQLException e) 
		{ 
			e.printStackTrace(); 
		} 
		return al; 
	}
	
	//added newly for Tasks_Favorites_GET
	public static Object fetchColumnAsObject(Connection conn, String strSQLQuery,String strColName)
	{
		Object retObj = null;
		try
		{
			ResultSet rs = DatabaseConnection.statementExecution(conn, strSQLQuery);
			while (rs.next()) 
			{ 
				@SuppressWarnings("unused")
				ResultSetMetaData metadata = rs.getMetaData(); 
				retObj = rs.getObject(strColName);
			}
			Log.info("Column:"+strColName+",Data:"+(retObj == null ? null : retObj.toString()));
			System.out.println("Column:"+strColName+",Data:"+(retObj == null ? null : retObj.toString()));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return retObj;
	}

	public static Map<String , Object> addResultswithMetaData(ResultSet rs)
	{
		Map<String , Object> mapResultwithMetaData = new HashMap<String , Object>();
		try 
		{ 
			while (rs.next()) 
			{ 
				ResultSetMetaData metadata = rs.getMetaData(); 
				int columnCount = metadata.getColumnCount(); 
				for (int i = 1; i <= columnCount; i++) 
					mapResultwithMetaData.put(metadata.getColumnLabel(i), rs.getObject(i));
			}
			Log.info("The SQL Response in a list: \n" + mapResultwithMetaData); 
		} catch (SQLException e) 
		{ 
			e.printStackTrace(); 
		} 
		return mapResultwithMetaData;
	}
	
	public static int returnNumberOfRows(ResultSet rs)
	{
		int intRows = 0;
		try
		{
			rs.last();
			intRows = rs.getRow();
			rs.beforeFirst();
			/*while(rs.next())
			{
				intRows++;
			}*/
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return intRows;
	}

	public static ResultSet spExecution(Connection conn, String strStmt, LinkedHashMap <String, Object> strSPParam)
	{
		ResultSet rs = null;
		try
		{
			CallableStatement callableStmt = conn.prepareCall(strStmt);

			for(Entry<String, Object> entry : strSPParam.entrySet())
			{
				callableStmt.setObject(entry.getKey(), entry.getValue());
				//System.out.println(entry.getKey() + " " +entry.getValue());
			}
			boolean results = callableStmt.execute();
			//System.out.println("Results is: "+results);
			int rowsAffected = 0;
			while(results || rowsAffected != -1)
			{
				if(results)
				{
					rs = callableStmt.getResultSet();
					break;
				}
				else
				{
					rowsAffected = callableStmt.getUpdateCount();
				}
				results = callableStmt.getMoreResults();
			}
			while(results)
			{
				rs = callableStmt.getResultSet();
				//ValidateResults.resultsvalidation(jsonList, rs, objHandler, strColumns, intPGNumber, intPGSize);
				results = callableStmt.getMoreResults();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return rs;
	}

	/*public static Map<String , ArrayList<Object>> addSPResultsToArrayList(Connection conn, String strStmt, LinkedHashMap <String, Object> strSPParam, List<String> strColumns, int intPGNumber, int intPGSize, ArrayList<Object> jsonList)
	{
		ArrayList<Object> alSelected = new ArrayList<Object>();
		int intStartRow = intPGNumber * intPGSize;
		@SuppressWarnings("unused")
		int intRSRowCount = 0;
		Object row=null;
		Map<String, ArrayList<Object>> mList = new HashMap<String , ArrayList<Object>>();

		ResultSet rs = null;
		try
		{
			CallableStatement callableStmt = conn.prepareCall(strStmt);

			for(Entry<String, Object> entry : strSPParam.entrySet())
			{
				callableStmt.setObject(entry.getKey(), entry.getValue());
				//System.out.println(entry.getKey() + " " +entry.getValue());
			}
			boolean results = callableStmt.execute();
			//System.out.println("Results is: "+results);
			int rowsAffected = 0;
			while(results || rowsAffected != -1)
			{
				if(results)
				{
					rs = callableStmt.getResultSet();
					break;
				}
				else
				{
					rowsAffected = callableStmt.getUpdateCount();
					//System.out.println(rowsAffected);
				}
				results = callableStmt.getMoreResults();
				//System.out.println(results);
			}
			int intNoOfRows = 0;
			while(results)
			{
				rs = callableStmt.getResultSet();
				while(rs.next())
				{
					//ResultSetMetaData metadata = rs.getMetaData();
					intRSRowCount++;
					ResultSetMetaData rsmd = rs.getMetaData();
					if(intStartRow < rs.getRow() && intNoOfRows < intPGSize )
					{
						//System.out.println("rs.getrow() is: " + rs.getRow());
						intNoOfRows++;
						for (int i=0; i< strColumns.size(); i++)
						{
							row = rs.getObject(strColumns.get(i));
							int type = rsmd.getColumnType(i+1);

							if(type == Types.DECIMAL && jsonList.get(i) != null && row!= null)
							{	
								if(row.toString().length() == jsonList.get(i).toString().length())
								{
									jsonList.set(i, (jsonList.get(i).toString()));
									row = rs.getString(strColumns.get(i));
								}
								else if(jsonList.get(i).toString().contains("."))
								{
									int t = (row.toString().length() - row.toString().indexOf(".")) - (jsonList.get(i).toString().length() - jsonList.get(i).toString().indexOf("."));
									if (t==1)
									{
										jsonList.set(i, (jsonList.get(i).toString() + "0"));
									}
									else if(t==2){
										jsonList.set(i, (jsonList.get(i).toString() + "00"));
									}
									row = rs.getString(strColumns.get(i));
								}

								else
								{
									int t = (row.toString().length() - row.toString().indexOf("."));
									if (t==1)
									{
										jsonList.set(i, (jsonList.get(i).toString() + "."));
									}
									else if(t==2){
										jsonList.set(i, (jsonList.get(i).toString() + ".0"));
									}
									else if(t==3){
										jsonList.set(i, (jsonList.get(i).toString() + ".00"));
									}
									else if(t==4){
										jsonList.set(i, (jsonList.get(i).toString() + ".000"));
									}
									row = rs.getString(strColumns.get(i));
								}
							}
							else
							{
								if(type == Types.DECIMAL && !Objects.equals(jsonList.get(i), null))
								{
									if((double)jsonList.get(i) == 0.0 && Objects.equals(rs.getObject(strColumns.get(i)), null))
									{
										jsonList.set(i, null);
									}
								}		
							}
							if(type == Types.INTEGER && !Objects.equals(jsonList.get(i), null))
							{
								if((int)jsonList.get(i) == 0 && Objects.equals(rs.getObject(strColumns.get(i)), null))
								{
									jsonList.set(i, null);
								}
							}
							else if((type == Types.VARCHAR ||type == Types.CHAR) && !Objects.equals(jsonList.get(i), null))
							{
								if(jsonList.get(i).toString().equals("") && Objects.equals(rs.getObject(strColumns.get(i)), null))
								{
									jsonList.set(i, null);
								}
								else if(row != null)
								{
									row = rs.getString(strColumns.get(i)).toString().replaceAll("\\s+$","");
								}
							}

							if(type == Types.TIMESTAMP)
							{
								//jsonList.set(i-1, (Timestamp) jsonList.get(i-1).toString());
								row = rs.getString(strColumns.get(i));
							}

							alSelected.add(rs.getObject(strColumns.get(i)));
						}
					}
				}

				results = callableStmt.getMoreResults();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		if(!jsonList.isEmpty() && !alSelected.isEmpty())
		{
			mList.put("jsonList", jsonList);
			mList.put("dbList", alSelected);
		}

		return mList;
	}*/
	
	
	public static Map<String , ArrayList<Object>> addSPResultsToArrayList(Connection conn, String strStmt, LinkedHashMap <String, Object> strSPParam, List<String> strColumns, int intPGNumber, int intPGSize, ArrayList<Object> jsonList)
	{
		ArrayList<Object> alSelected = new ArrayList<Object>();
		int intStartRow = intPGNumber * intPGSize;
		@SuppressWarnings("unused")
		int intRSRowCount = 0;
		Object row=null;
		Map<String, ArrayList<Object>> mList = new HashMap<String , ArrayList<Object>>();

		ResultSet rs = null;
		try
		{
			CallableStatement callableStmt = conn.prepareCall(strStmt);

			for(Entry<String, Object> entry : strSPParam.entrySet())
			{
				callableStmt.setObject(entry.getKey(), entry.getValue());
				//System.out.println(entry.getKey() + " " +entry.getValue());
			}
			boolean results = callableStmt.execute();
			//System.out.println("Results is: "+results);
			int rowsAffected = 0;
			while(results || rowsAffected != -1)
			{
				if(results)
				{
					rs = callableStmt.getResultSet();
					break;
				}
				else
				{
					rowsAffected = callableStmt.getUpdateCount();
					//System.out.println(rowsAffected);
				}
				results = callableStmt.getMoreResults();
				//System.out.println(results);
			}
			int intNoOfRows = 0;
			while(results)
			{
				rs = callableStmt.getResultSet();
				int intJsonIndex = 0;
				while(rs.next())
				{
					//ResultSetMetaData metadata = rs.getMetaData();
					intRSRowCount++;
					ResultSetMetaData rsmd = rs.getMetaData();
					if(intStartRow < rs.getRow() && intNoOfRows < intPGSize )
					{
						//System.out.println("rs.getrow() is: " + rs.getRow());
						intNoOfRows++;
						for (int i=1; i<= rsmd.getColumnCount(); i++)
						{
							for(int reqColumns =0; reqColumns< strColumns.size();reqColumns++)
							{
								if(Objects.equals(rsmd.getColumnName(i), strColumns.get(reqColumns)))
								{

									row = rs.getObject(strColumns.get(reqColumns));
									int type = rsmd.getColumnType(i);

									if(type == Types.DECIMAL && jsonList.get(intJsonIndex) != null && row!= null)
									{	
										if(row.toString().length() == jsonList.get(intJsonIndex).toString().length())
										{
											jsonList.set(intJsonIndex, (jsonList.get(intJsonIndex).toString()));
											row = rs.getString(strColumns.get(reqColumns));
										}
										else if(jsonList.get(intJsonIndex).toString().contains("."))
										{
											int t = (row.toString().length() - row.toString().indexOf(".")) - (jsonList.get(intJsonIndex).toString().length() - jsonList.get(intJsonIndex).toString().indexOf("."));
											if (t==1)
											{
												jsonList.set(intJsonIndex, (jsonList.get(intJsonIndex).toString() + "0"));
											}
											else if(t==2){
												jsonList.set(intJsonIndex, (jsonList.get(intJsonIndex).toString() + "00"));
											}
											row = rs.getString(strColumns.get(reqColumns));
										}

										else
										{
											int t = (row.toString().length() - row.toString().indexOf("."));
											if (t==1)
											{
												jsonList.set(intJsonIndex, (jsonList.get(intJsonIndex).toString() + "."));
											}
											else if(t==2){
												jsonList.set(intJsonIndex, (jsonList.get(intJsonIndex).toString() + ".0"));
											}
											else if(t==3){
												jsonList.set(intJsonIndex, (jsonList.get(intJsonIndex).toString() + ".00"));
											}
											else if(t==4){
												jsonList.set(intJsonIndex, (jsonList.get(intJsonIndex).toString() + ".000"));
											}
											row = rs.getString(strColumns.get(reqColumns));
										}
									}
									else
									{
										if(type == Types.DECIMAL && !Objects.equals(jsonList.get(intJsonIndex), null))
										{
											if((double)jsonList.get(intJsonIndex) == 0.0 && Objects.equals(rs.getObject(strColumns.get(reqColumns)), null))
											{
												jsonList.set(intJsonIndex, null);
											}
										}		
									}
									if(type == Types.INTEGER && !Objects.equals(jsonList.get(intJsonIndex), null))
									{
										if((int)jsonList.get(intJsonIndex) == 0 && Objects.equals(rs.getObject(strColumns.get(reqColumns)), null))
										{
											jsonList.set(intJsonIndex, null);
										}
									}
									else if((type == Types.VARCHAR ||type == Types.CHAR) && !Objects.equals(jsonList.get(intJsonIndex), null))
									{
										if(jsonList.get(intJsonIndex).toString().equals("") && Objects.equals(rs.getObject(strColumns.get(reqColumns)), null))
										{
											jsonList.set(intJsonIndex, null);
										}
										else if(row != null)
										{
											row = rs.getString(strColumns.get(reqColumns)).toString().replaceAll("\\s+$","");
										}
									}

									if(type == Types.TIMESTAMP)
									{
										//jsonList.set(i-1, (Timestamp) jsonList.get(i-1).toString());
										row = rs.getString(strColumns.get(reqColumns));
									}
									
									if(type == Types.BIGINT && jsonList.get(intJsonIndex).getClass() == java.lang.String.class)
									{
										row = rs.getString(strColumns.get(reqColumns));
									}

									//alSelected.add(rs.getObject(strColumns.get(reqColumns)));
									alSelected.add(row);
									intJsonIndex++;
								}
								
							}
						}
						/*for (int i=0; i< strColumns.size(); i++)
						{
							row = rs.getObject(strColumns.get(i));
							int type = rsmd.getColumnType(i+1);

							if(type == Types.DECIMAL && jsonList.get(i) != null && row!= null)
							{	
								if(row.toString().length() == jsonList.get(i).toString().length())
								{
									jsonList.set(i, (jsonList.get(i).toString()));
									row = rs.getString(strColumns.get(i));
								}
								else if(jsonList.get(i).toString().contains("."))
								{
									int t = (row.toString().length() - row.toString().indexOf(".")) - (jsonList.get(i).toString().length() - jsonList.get(i).toString().indexOf("."));
									if (t==1)
									{
										jsonList.set(i, (jsonList.get(i).toString() + "0"));
									}
									else if(t==2){
										jsonList.set(i, (jsonList.get(i).toString() + "00"));
									}
									row = rs.getString(strColumns.get(i));
								}

								else
								{
									int t = (row.toString().length() - row.toString().indexOf("."));
									if (t==1)
									{
										jsonList.set(i, (jsonList.get(i).toString() + "."));
									}
									else if(t==2){
										jsonList.set(i, (jsonList.get(i).toString() + ".0"));
									}
									else if(t==3){
										jsonList.set(i, (jsonList.get(i).toString() + ".00"));
									}
									else if(t==4){
										jsonList.set(i, (jsonList.get(i).toString() + ".000"));
									}
									row = rs.getString(strColumns.get(i));
								}
							}
							else
							{
								if(type == Types.DECIMAL && !Objects.equals(jsonList.get(i), null))
								{
									if((double)jsonList.get(i) == 0.0 && Objects.equals(rs.getObject(strColumns.get(i)), null))
									{
										jsonList.set(i, null);
									}
								}		
							}
							if(type == Types.INTEGER && !Objects.equals(jsonList.get(i), null))
							{
								if((int)jsonList.get(i) == 0 && Objects.equals(rs.getObject(strColumns.get(i)), null))
								{
									jsonList.set(i, null);
								}
							}
							else if((type == Types.VARCHAR ||type == Types.CHAR) && !Objects.equals(jsonList.get(i), null))
							{
								if(jsonList.get(i).toString().equals("") && Objects.equals(rs.getObject(strColumns.get(i)), null))
								{
									jsonList.set(i, null);
								}
								else if(row != null)
								{
									row = rs.getString(strColumns.get(i)).toString().replaceAll("\\s+$","");
								}
							}

							if(type == Types.TIMESTAMP)
							{
								//jsonList.set(i-1, (Timestamp) jsonList.get(i-1).toString());
								row = rs.getString(strColumns.get(i));
							}

							alSelected.add(rs.getObject(strColumns.get(i)));
						}*/	
				}
				}

				results = callableStmt.getMoreResults();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		if(!jsonList.isEmpty() && !alSelected.isEmpty())
		{
			mList.put("jsonList", jsonList);
			mList.put("dbList", alSelected);
		}

		return mList;
	}

	/*public static void main(String[] args) {
		Connection conn = DatabaseConnection.makeDBConnection();
		String query = "select appt_id,event_id,appt_date,begintime,endtime,duration,details,workflow_room,workflow_status,person_id,last_name,first_name,middle_name,rendering_provider_id,location_id from appointments where rendering_provider_id = 'ECA14F45-85F4-47B0-B818-002FDD7EB6D8' and appt_date between '20140510'and '20160410' order by create_timestamp asc";
		ArrayList<String> alappt = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(conn, query));
		System.out.println(alappt.size());
		LinkedHashMap <String, Object> strSPParam = new LinkedHashMap<String, Object>();
		strSPParam.put("SDBPpi_enterprise_id", "00001");
		strSPParam.put("SDBPpi_practice_id", "0002");
		strSPParam.put("SDBPpi_user_id", 189);
		strSPParam.put("SDBPpi_view_by", 0);
		strSPParam.put("SDBPpi_expired_ind", false);
		strSPParam.put("SDBPpi_return_patients_only", false);
		strSPParam.put("SDBPpi_enterprise_chart", false);
		strSPParam.put("SDBPpi_nextMD", false);
		strSPParam.put("SDBPpi_EPM_for_EHR_sched_src", true);
		strSPParam.put("SDBPpi_scoping_level", 0);
		strSPParam.put("SDBPpi_privacy_level", 0);
		strSPParam.put("SDBPpi_add_to_inclusion", false);
		strSPParam.put("SDBPpi_EMPI_ext_ind", false);
		strSPParam.put("SDBPpi_secure_lookup", 0);
		strSPParam.put("SDBPpi_secure_lookup_enabled", true);
		strSPParam.put("SDBPpi_patient_share_type", 0);
		strSPParam.put("SDBPpi_top_limit", 1000);
		strSPParam.put("SDBPpi_last_name", "Test");
		ArrayList<String> str = new ArrayList<String>();
		str.add("person_id");
		str.add("first_name");
		//fetchSelectedResultsToArray(DatabaseConnection.spExecution(conn, "{call ng_get_person_lookup_result (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}", strSPParam),str, 0,30);
		//fetchSPResultsToArray(Connection conn, String strStmt, LinkedHashMap <String, Object> strSPParam, List<String> strColumns, int intPGNumber, int intPGSize)
		ArrayList<String> str1 = addSPResultsToArrayList(conn,"{call ng_get_person_lookup_result (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}", strSPParam,str, 2,10);
		System.out.println(str1.toString());
		}*/
}