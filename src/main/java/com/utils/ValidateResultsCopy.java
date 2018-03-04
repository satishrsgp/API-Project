package com.utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class ValidateResultsCopy 
{
	public enum Magenta
	{
		A,AA,B,W,D,U,R,I
	};

	public enum Black
	{
		S,MS,VS,N
	};

	public enum Red
	{
		H,HH
	}

	public enum Blue
	{
		L,LL
	}

	public static void resultsvalidation(ArrayList<Object> jsonList, ResultSet rs, RequestResponseHandler objHandler)
	{
		try
		{	
			if (rs != null && !(jsonList.size() == 0))
			{
				Comparision:
					while (rs.next())
					{
						int intUindex = 0;
						boolean bol = false;
						Object row = ""; 
						ResultSetMetaData rsmd = rs.getMetaData();
						for (int i = 1; i <= rsmd.getColumnCount(); i++)
						{
							int type = rsmd.getColumnType(i);
							if(!Objects.equals(rs.getObject(i), null)){
								row = rs.getObject(i).toString().replace(".jp2", "");	
							}
							else
							{
								row = rs.getObject(i);	

							}
							if(!bol)
							{
								Unique_ID:
									for (int l=0; l < jsonList.size(); l++)
									{
										if ( Objects.equals(row, jsonList.get(l)))
										{
											intUindex = l;
											//System.out.println("intUindex= "+l);
											bol = true;
											break Unique_ID;
										}
									}
							}

							//System.out.println("The Json Type" + jsonList.get(intUindex).getClass());
							//System.out.println("The SQL Type" + rsmd.getColumnTypeName(i));

							if(type == Types.DECIMAL && jsonList.get(intUindex) != null && row!= null)
							{	
								if(row.toString().length() == jsonList.get(intUindex).toString().length())
								{
									jsonList.set(intUindex, (jsonList.get(intUindex).toString()));
									row = rs.getString(i);
								}
								else if(jsonList.get(intUindex).toString().contains("."))
								{
									int t = (row.toString().length() - row.toString().indexOf(".")) - (jsonList.get(intUindex).toString().length() - jsonList.get(intUindex).toString().indexOf("."));
									if (t==1)
									{
										jsonList.set(intUindex, (jsonList.get(intUindex).toString() + "0"));
									}
									else if(t==2){
										jsonList.set(intUindex, (jsonList.get(intUindex).toString() + "00"));
									}
									row = rs.getString(i);
								}

								else
								{
									int t = (row.toString().length() - row.toString().indexOf("."));
									if (t==1)
									{
										jsonList.set(intUindex, (jsonList.get(intUindex).toString() + "."));
									}
									else if(t==2){
										jsonList.set(intUindex, (jsonList.get(intUindex).toString() + ".0"));
									}
									else if(t==3){
										jsonList.set(intUindex, (jsonList.get(intUindex).toString() + ".00"));
									}
									else if(t==4){
										jsonList.set(intUindex, (jsonList.get(intUindex).toString() + ".000"));
									}
									row = rs.getString(i);
								}
							}
							else
							{
								if(type == Types.DECIMAL && !Objects.equals(jsonList.get(intUindex), null))
								{
									if((double)jsonList.get(intUindex) == 0.0 && Objects.equals(rs.getObject(i), null))
									{
										jsonList.set(intUindex, null);
									}
								}		
							}
							if(type == Types.INTEGER && !Objects.equals(jsonList.get(intUindex), null) && jsonList.get(intUindex).getClass() != java.lang.String.class)
							{
								if((int)jsonList.get(intUindex) == 0 && Objects.equals(rs.getObject(i), null))
								{
									jsonList.set(intUindex, null);
								}
								else if(!Objects.equals(jsonList.get(intUindex), row) && row != null)
								{
									row = rs.getString(i).toString().replaceAll("\\s+$","");
									jsonList.set(intUindex, String.valueOf(jsonList.get(intUindex)));
								}
								else if(type == Types.INTEGER && !Objects.equals(jsonList.get(intUindex), null) && jsonList.get(intUindex).getClass() == java.lang.String.class)
								{
									row = rs.getString(i);
								}
							}
							if(type == Types.SMALLINT && !Objects.equals(jsonList.get(intUindex), null))
							{
								//System.out.println(jsonList.get(intUindex).getClass());
								if(jsonList.get(intUindex) == "" && Objects.equals(rs.getString(i), null))
								{
									jsonList.set(intUindex, null);
								}
								else if(!Objects.equals(jsonList.get(intUindex), rs.getObject(i)) && row != null)
								{
									row = rs.getString(i).toString().replaceAll("\\s+$","");
									jsonList.set(intUindex, String.valueOf(jsonList.get(intUindex)));
								}
								else
								{
									row = rs.getString(i);
								}
							}

							if((type == Types.VARCHAR ||type == Types.CHAR) && !Objects.equals(jsonList.get(intUindex), null))
							{
								if(jsonList.get(intUindex).toString().equals("") && Objects.equals(rs.getObject(i), null))
								{
									jsonList.set(intUindex, null);
								}
								else if(!Objects.equals(jsonList.get(intUindex), rs.getString(i)) && row != null)
								{
									row = rs.getString(i).toString().replaceAll("\\s+$","");
								}
								else//added new 
								{ 
									//System.out.println(jsonList.get(intUindex)); 
									row = rs.getString(i); 
									//System.out.println(row); }
								}
								if(!Objects.equals(rs, null) && !Objects.equals(jsonList.get(intUindex), null))
								{
									if((rs.getString(i).endsWith("AM") || rs.getString(i).endsWith("PM")))
									{
										if( rs.getString(i).contains(":") &&(jsonList.get(intUindex).toString().length() != rs.getString(i).length()))
										{
											if(rs.getString(i).toString().startsWith("0"))
											{
												if(Objects.equals(rs.getString(i).substring(1, rs.getString(i).length()), jsonList.get(intUindex)))
												{
													jsonList.set(intUindex, rs.getString(i));
												}
											}
										}
									}
								}
							}
							else if(Objects.equals(row, "") && Objects.equals(jsonList.get(intUindex), null))
							{
								jsonList.set(intUindex, "");
							}

							//added new to handle panel comment in lab order results get route
							if(type == Types.LONGVARCHAR && !Objects.equals(jsonList.get(intUindex), null))
							{
								if(jsonList.get(intUindex).toString().equals("") && Objects.equals(rs.getObject(i), null))
								{
									jsonList.set(intUindex, null);
								}
								else if(!Objects.equals(jsonList.get(intUindex), rs.getString(i)) && row != null)
								{
									row = rs.getString(i).toString().replaceAll("\\s+$","");
								}
								else
								{
									row = rs.getString(i);
								}
							}
							/*if(type == Types.NUMERIC && !Objects.equals(jsonList.get(intUindex), null) )
							{
								//jsonList.set(intUindex, (BigDecimal)jsonList.get(intUindex));
								row = rs.getString(i);
							}*/
							if(type == Types.NUMERIC && jsonList.get(intUindex) != null && row != null)
							{
								if(jsonList.get(intUindex).getClass() ==  java.lang.String.class)
								{
									row = rs.getString(i);
								}
								else if (jsonList.get(intUindex).getClass() ==  java.lang.Integer.class)
								{
									row = rs.getInt(i);
								}
							}
							if(type == Types.TIMESTAMP)
							{

								//System.out.println("I am in tiemstamp for SQL value:"+row.toString()+" and jsonlist value:"+jsonList.get(intUindex).toString());
								//jsonList.set(i-1, (Timestamp) jsonList.get(i-1).toString());
								/*row = rs.getString(i);
								int t = (row.toString().length() - row.toString().indexOf("."));
								if(row.toString().endsWith(".0") || row.toString().endsWith(".00") ||  row.toString().endsWith(".000"))
								{
									if (t==1)
									{
										jsonList.set(intUindex, (jsonList.get(intUindex).toString() + "."));
									}
									else if(t==2){
										jsonList.set(intUindex, (jsonList.get(intUindex).toString() + ".0"));
									}
									else if(t==3){
										jsonList.set(intUindex, (jsonList.get(intUindex).toString() + ".00"));
									}
									else if(t==4){
										jsonList.set(intUindex, (jsonList.get(intUindex).toString() + ".000"));
									}
								}
								row = rs.getString(i);*/

								if(row.toString().length() == jsonList.get(intUindex).toString().length())
								{
									jsonList.set(intUindex, (jsonList.get(intUindex).toString()));
									row = rs.getString(i);
								}
								else if(jsonList.get(intUindex).toString().contains("."))
								{
									int t = (row.toString().length() - row.toString().indexOf(".")) - (jsonList.get(intUindex).toString().length() - jsonList.get(intUindex).toString().indexOf("."));
									if (t==1)
									{
										jsonList.set(intUindex, (jsonList.get(intUindex).toString() + "0"));
									}
									else if(t==2){
										jsonList.set(intUindex, (jsonList.get(intUindex).toString() + "00"));
									}
									row = rs.getString(i);
								}

								else
								{
									int t = (row.toString().length() - row.toString().indexOf("."));
									if (t==1)
									{
										jsonList.set(intUindex, (jsonList.get(intUindex).toString() + "."));
									}
									else if(t==2){
										jsonList.set(intUindex, (jsonList.get(intUindex).toString() + ".0"));
									}
									else if(t==3){
										jsonList.set(intUindex, (jsonList.get(intUindex).toString() + ".00"));
									}
									else if(t==4){
										jsonList.set(intUindex, (jsonList.get(intUindex).toString() + ".000"));
									}
									row = rs.getString(i);
								}

							}


							//System.out.println("Column Name: "+rsmd.getColumnName(i)+", Column Data Type: "+rsmd.getColumnTypeName(i)+", Column Data: "+row);
							//System.out.println("jsonList: " +jsonList.get(intUindex).getClass() +"jsonList.get(" + intUindex +")="+jsonList.get(intUindex));
							//System.out.println("jsonList.get(" + intUindex +")="+jsonList.get(intUindex));
							if ( Objects.equals(row, jsonList.get(intUindex)))
							{
								Log.info("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Pass");
								objHandler.setStrRequestStatus("Pass");
								System.out.println("Pass :::: " + "SQL Column Name: "+rsmd.getColumnName(i)+ " , SQL Column Data: "+row+" , jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i));
								//System.out.println("Pass");
							}
							else if ((jsonList.get(intUindex)).equals("00010101"))
							{
								//System.out.println("inside");
								if (row == " " || row.toString().isEmpty())
								{
									Log.info("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Pass");
									objHandler.setStrRequestStatus("Pass");
									System.out.println("Pass :::: " + "SQL Column Name: "+rsmd.getColumnName(i)+ " , SQL Column Data: "+row+" , jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i));
									//System.out.println("Pass");
								}
								//added new on 29-06-2016
								else
								{
									Log.info("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Fail");
									objHandler.setStrRequestStatus("Fail");
									System.out.println("Fail :::: " + "SQL Column Name: "+rsmd.getColumnName(i)+ " , SQL Column Data: "+row+" , jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i));
									break Comparision;
								}
							}
							else
							{
								Log.info("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Fail");
								objHandler.setStrRequestStatus("Fail");
								System.out.println("Fail :::: " + "SQL Column Name: "+rsmd.getColumnName(i)+ " , SQL Column Data: "+row+" , jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i));
								//System.out.println("SQL Column Name: "+rsmd.getColumnName(i)+ " , SQL Column Data: "+row+" , jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+",  Comparision: Fail");
								break Comparision;
							}
							intUindex = intUindex + 1;
						}
					}
			}
		}

		catch(Exception e)
		{
			e.printStackTrace();
		}

	}


	/*public static void resultsvalidation(ArrayList<Object> jsonList, ResultSet rs, RequestResponseHandler objHandler,ArrayList<String> strColumns, int intPGNumber, int intPGSize)
	{

		int intStartRow = intPGNumber * intPGSize;
		int intRSRowCount = 0;
		int intNoOfRows = 0;

		try
		{	
			if (rs != null)
			{
				Comparision:
					while(rs.next())
					{
						intRSRowCount++;

						if(intStartRow < rs.getRow() && intNoOfRows < intPGSize )
						{
							int intUindex = 0;
							boolean bol = false;
							Object row = ""; 
							ResultSetMetaData rsmd = rs.getMetaData();
							for (int i = 1; i <= rsmd.getColumnCount(); i++)
							{
								for(int j=0; j< strColumns.size(); j++)
								{
									if(rsmd.getColumnLabel(i).equalsIgnoreCase(strColumns.get(j)))
									{
										int type = rsmd.getColumnType(i);
										row = rs.getObject(strColumns.get(j));
										if(!bol)
										{
											Unique_ID:
												for (int l=0; l < jsonList.size(); l++)
												{
													if ( Objects.equals(row, jsonList.get(l)))
													{
														intUindex = l;
														System.out.println("intUindex= "+l);
														bol = true;
														break Unique_ID;
													}
												}
										}

										if(type == Types.DECIMAL && jsonList.get(intUindex) != null && row!= null)
										{	
											if(row.toString().length() == jsonList.get(intUindex).toString().length())
											{
												jsonList.set(intUindex, (jsonList.get(intUindex).toString()));
												row = rs.getString(i);
											}
											else if(jsonList.get(intUindex).toString().contains("."))
											{
												int t = (row.toString().length() - row.toString().indexOf(".")) - (jsonList.get(intUindex).toString().length() - jsonList.get(intUindex).toString().indexOf("."));
												if (t==1)
												{
													jsonList.set(intUindex, (jsonList.get(intUindex).toString() + "0"));
												}
												else if(t==2){
													jsonList.set(intUindex, (jsonList.get(intUindex).toString() + "00"));
												}
												row = rs.getString(i);
											}

											else
											{
												int t = (row.toString().length() - row.toString().indexOf("."));
												if (t==1)
												{
													jsonList.set(intUindex, (jsonList.get(intUindex).toString() + "."));
												}
												else if(t==2){
													jsonList.set(intUindex, (jsonList.get(intUindex).toString() + ".0"));
												}
												else if(t==3){
													jsonList.set(intUindex, (jsonList.get(intUindex).toString() + ".00"));
												}
												else if(t==4){
													jsonList.set(intUindex, (jsonList.get(intUindex).toString() + ".000"));
												}
												row = rs.getString(i);
											}
										}
										else
										{
											if(type == Types.DECIMAL && !Objects.equals(jsonList.get(intUindex), null))
											{
												if((double)jsonList.get(intUindex) == 0.0 && Objects.equals(rs.getObject(i), null))
												{
													jsonList.set(intUindex, null);
												}
											}		
										}
										if(type == Types.INTEGER && !Objects.equals(jsonList.get(intUindex), null))
										{
											if((int)jsonList.get(intUindex) == 0 && Objects.equals(rs.getObject(i), null))
											{
												jsonList.set(intUindex, null);
											}
										}
										else if((type == Types.VARCHAR ||type == Types.CHAR) && !Objects.equals(jsonList.get(intUindex), null))
										{
											if(jsonList.get(intUindex).toString().equals("") && Objects.equals(rs.getObject(i), null))
											{
												jsonList.set(intUindex, null);
											}
											if(row != null)
											{
												row = rs.getString(i).toString().trim();
											}
										}

										if(type == Types.TIMESTAMP)
										{
											//jsonList.set(i-1, (Timestamp) jsonList.get(i-1).toString());
											row = rs.getString(i);
										}

										System.out.println("Column Name: "+rsmd.getColumnName(i)+", Column Data Type: "+rsmd.getColumnTypeName(i)+", Column Data: "+row);
										System.out.println("jsonList.get(" + intUindex +")="+jsonList.get(intUindex));
										if ( Objects.equals(row, jsonList.get(intUindex)))
										{
											objHandler.setStrRequestStatus("Pass");
											System.out.println(" Pass");
										}
										else if ((jsonList.get(intUindex)).equals("00010101"))
										{
											System.out.println("inside");
											if (row == " ")
											{
												objHandler.setStrRequestStatus("Pass");
												System.out.println(" Pass");
											}
										}
										else
										{
											objHandler.setStrRequestStatus("Fail");
											System.out.println(" Fails");
											break Comparision;
										}
										intUindex = intUindex + 1;
									}
								}	
							}
						}
					}
			}
		}

		catch(Exception e)
		{
			e.printStackTrace();
		}
	}*/

	public static void resultsvalidation(Map<String , ArrayList<Object>> mList, RequestResponseHandler objHandler)
	{
		try
		{
			if(mList != null)
			{
				ArrayList<Object> jsonList = mList.get("jsonList");
				ArrayList<Object> dbList = mList.get("dbList");
				System.out.println("jsonList is: " + jsonList);
				System.out.println("dbList is: " + dbList);
				if(dbList != null)
				{
					if (jsonList != null)
					{
						Comparison:
							for (int i = 0 ; i< jsonList.size() ; i++)
							{
								if(!Objects.equals(dbList.get(i), null)) 
								{ 
									if(!dbList.get(i).getClass().equals(jsonList.get(i).getClass())) 
									{ 
										jsonList.set(i, jsonList.get(i).toString()); 
										dbList.set(i, dbList.get(i).toString()); 
									} 
								}
								if(Objects.equals(jsonList.get(i), dbList.get(i)))
								{
									Log.info("dbList("+i+"): "+dbList.get(i)+" ,jsonList("+i+"): "+jsonList.get(i)+" ,Comparison: Pass");
									objHandler.setStrRequestStatus("Pass");
									//System.out.println("Pass");
								}
								else if ((jsonList.get(i)).equals("00010101"))
								{
									System.out.println("inside");
									if (dbList.get(i) == " ")
									{
										Log.info("dbList("+i+"): "+dbList.get(i)+" ,jsonList("+i+"): "+jsonList.get(i)+" ,Comparison: Pass");
										objHandler.setStrRequestStatus("Pass");
										//System.out.println("Pass");
									}
								}
								else
								{
									Log.info("dbList("+i+"): "+dbList.get(i)+" ,jsonList("+i+"): "+jsonList.get(i)+" ,Comparison: Fail");
									objHandler.setStrRequestStatus("Fail");
									System.out.println("Fail");
									break Comparison;
								}
							}
					}
					else
					{
						Log.info("dbList has data and the returned Json does not have data. Please investigate");
						objHandler.setStrRequestStatus("Fail");
						System.out.println("Fail");
					}
				}
				else
				{
					if(Objects.equals(jsonList, null))
					{
						Log.info("Both dbList and jsonList are null, Comparison: Pass");
						objHandler.setStrRequestStatus("Pass");
						//System.out.println("Pass");
					}
					else
						Log.info("Please correct the Query and reexcute the things");
				}	
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	public static void resultValidation(ArrayList<Object> jsonList,ArrayList<Object> dbList,RequestResponseHandler objHandler)
	{
		try
		{
			if( Objects.equals(jsonList, null) && Objects.equals(dbList, null))
			{
				objHandler.setStrRequestStatus("Pass");
				Log.info("Both jsonList and dbList are null!!!");
			}
			else if(Objects.equals(jsonList, null))
			{
				if(Objects.equals(dbList, null))
				{
					objHandler.setStrRequestStatus("Pass");
					Log.info("Both jsonList and dbList are null!!!");
				}
				else
				{
					objHandler.setStrRequestStatus("Fail");
					Log.error("jsonList is null but dbList is not null!!!");
				}
			}	
			else if(!jsonList.isEmpty() && !dbList.isEmpty())
			{
				if(jsonList.size() == dbList.size())
				{

					for(int i = 0; i< jsonList.size(); i++)
					{
						//System.out.println("jsonList("+i+")= "+jsonList.get(i).toString());
						//System.out.println("dbList("+i+")= "+dbList.get(i).toString());
						//Log.info("dbList("+i+")= "+dbList.get(i).toString());
						if(jsonList.get(i).toString().equalsIgnoreCase("Magenta"))
						{
							for(Magenta mg : Magenta.values())
							{
								//System.out.println("mg is: "+mg.toString());
								if(mg.toString().equalsIgnoreCase(dbList.get(i).toString()))
								{
									Log.info("jsonList("+i+")= "+jsonList.get(i).toString()+", dbList("+i+")= "+dbList.get(i).toString()+", jsonList matches with "+mg.toString()+", Comparison: Pass");
									objHandler.setStrRequestStatus("Pass");
									//System.out.println("Pass in magenta");
									break;
								}
								//objHandler.setStrRequestStatus("Fail");
							}
						}
						else if(jsonList.get(i).toString().equalsIgnoreCase("Black"))
						{
							for(Black blk : Black.values())
							{
								//System.out.println("blk is: "+blk.toString());
								if(blk.toString().equalsIgnoreCase(dbList.get(i).toString()))
								{
									Log.info("jsonList("+i+")= "+jsonList.get(i).toString()+", dbList("+i+")= "+dbList.get(i).toString()+", jsonList matches with "+blk.toString()+", Comparison: Pass");
									objHandler.setStrRequestStatus("Pass");
									//System.out.println("Pass in black");
									break;
								}
								//objHandler.setStrRequestStatus("Fail");
							}
						}
						else if(jsonList.get(i).toString().equalsIgnoreCase("Red"))
						{
							for(Red rd : Red.values())
							{
								//System.out.println("rd is: "+rd.toString());
								if(rd.toString().equalsIgnoreCase(dbList.get(i).toString()))
								{
									Log.info("jsonList("+i+")= "+jsonList.get(i).toString()+", dbList("+i+")= "+dbList.get(i).toString()+", jsonList matches with "+rd.toString()+", Comparison: Pass");
									objHandler.setStrRequestStatus("Pass");
									//System.out.println("Pass in red");
									break;
								}
								//objHandler.setStrRequestStatus("Fail");
							}
						}
						else if(jsonList.get(i).toString().equalsIgnoreCase("Blue"))
						{
							if(Objects.equals(dbList.get(i), null))
							{
								Log.info("jsonList("+i+")= "+jsonList.get(i).toString()+", dbList("+i+")= null, Comparison: Pass");
								objHandler.setStrRequestStatus("Pass");
								//System.out.println("Pass in blue when dbList is null");
								break;
							}
							else
							{
								for(Blue bl : Blue.values())
								{
									//System.out.println("bl is: "+bl.toString());
									if(bl.toString().equalsIgnoreCase(dbList.get(i).toString()))
									{
										Log.info("jsonList("+i+")= "+jsonList.get(i).toString()+", dbList("+i+")= "+dbList.get(i).toString()+", jsonList matches with "+bl.toString()+", Comparison: Pass");
										objHandler.setStrRequestStatus("Pass");
										//System.out.println("Pass in blue");
										break;
									}
									//objHandler.setStrRequestStatus("Fail");
								}
							}
						}
						else
						{
							objHandler.setStrRequestStatus("Fail");
							Log.error("Value of jsonList is: "+jsonList.get(i)+", value of dbList is: "+dbList.get(i)+", Comparison: Fails");
						}
					}
				}
				else
				{
					objHandler.setStrRequestStatus("Fail");
					Log.error("jsonList is: "+jsonList+", size is: "+jsonList.size());
					Log.error("dbList is: "+dbList+", size is: "+dbList.size());
					Log.info("jsonList and dbList sizes vary, Comparison: Fails");
				}
			}
			else
			{
				objHandler.setStrRequestStatus("Pass");
				Log.error("Both jsonList or dbList is empty!!! Please check");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void resultsvalidation(Connection conn, String strStmt, LinkedHashMap <String, Object> strSPParam, List<String> strColumns,ArrayList<Object> jsonList, RequestResponseHandler objHandler,String strText)
	{
		try
		{
			if(!(jsonList.size() == 0))
			{
				@SuppressWarnings("unused")
				int intRSRowCount = 0;

				ResultSet rs = null;
				try
				{
					CallableStatement callableStmt = conn.prepareCall(strStmt);
					//Log.info("SP executed is: "+callableStmt.toString());
					for(Entry<String, Object> entry : strSPParam.entrySet())
					{
						callableStmt.setObject(entry.getKey(), entry.getValue());
					}
					boolean results = callableStmt.execute();
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
						if (rs != null)
						{
							//if(rs.getString("pharmacy_model").equalsIgnoreCase(strText) )
							Comparision:
								while (rs.next())
								{
									if(rs.getString("pharmacy_model").equalsIgnoreCase(strText))
									{
										//if(rs.getString("pharmacy_model").equalsIgnoreCase(strText) )
										//strColumns.indexOf(rsmd.getColumnName(i)) >=0 
										int intUindex = 0;
										boolean bol = false;
										Object row = ""; 
										ResultSetMetaData rsmd = rs.getMetaData();
										//Log.info("ResultSet has "+rsmd.getColumnCount()+" Columns");
										for (int i = 1; i <= rsmd.getColumnCount(); i++)
										{
											//System.out.println("rsmd.getColumnName(i) is "+rsmd.getColumnName(i));
											if(strColumns.indexOf(rsmd.getColumnName(i)) >=0)
											{
												int type = rsmd.getColumnType(i);
												row = rs.getObject(rsmd.getColumnName(i));
												if(!bol)
												{
													Unique_ID:
														for (int l=0; l < jsonList.size(); l++)
														{
															if ( Objects.equals(row, jsonList.get(l)))
															{
																intUindex = l;
																//System.out.println("intUindex= "+l);
																bol = true;
																break Unique_ID;
															}
														}
												}

												if(type == Types.DECIMAL && jsonList.get(intUindex) != null && row!= null)
												{	
													if(row.toString().length() == jsonList.get(intUindex).toString().length())
													{
														jsonList.set(intUindex, (jsonList.get(intUindex).toString()));
														row = rs.getString(rsmd.getColumnName(i));
													}
													else if(jsonList.get(intUindex).toString().contains("."))
													{
														int t = (row.toString().length() - row.toString().indexOf(".")) - (jsonList.get(intUindex).toString().length() - jsonList.get(intUindex).toString().indexOf("."));
														if (t==1)
														{
															jsonList.set(intUindex, (jsonList.get(intUindex).toString() + "0"));
														}
														else if(t==2){
															jsonList.set(intUindex, (jsonList.get(intUindex).toString() + "00"));
														}
														row = rs.getString(rsmd.getColumnName(i));
													}

													else
													{
														int t = (row.toString().length() - row.toString().indexOf("."));
														if (t==1)
														{
															jsonList.set(intUindex, (jsonList.get(intUindex).toString() + "."));
														}
														else if(t==2){
															jsonList.set(intUindex, (jsonList.get(intUindex).toString() + ".0"));
														}
														else if(t==3){
															jsonList.set(intUindex, (jsonList.get(intUindex).toString() + ".00"));
														}
														else if(t==4){
															jsonList.set(intUindex, (jsonList.get(intUindex).toString() + ".000"));
														}
														row = rs.getString(rsmd.getColumnName(i));
													}
												}
												else
												{
													if(type == Types.DECIMAL && !Objects.equals(jsonList.get(intUindex), null))
													{
														if((double)jsonList.get(intUindex) == 0.0 && Objects.equals(rs.getObject(rsmd.getColumnName(i)), null))
														{
															jsonList.set(intUindex, null);
														}
													}		
												}
												if(type == Types.NUMERIC && jsonList.get(intUindex) != null && row != null)
												{
													//System.out.println("Numeric for index: "+i);

													if(row.toString().length() == jsonList.get(intUindex).toString().length())
													{
														jsonList.set(intUindex, (jsonList.get(intUindex).toString()));
														row = rs.getString(rsmd.getColumnName(i));
													}
													else if(jsonList.get(intUindex).toString().contains("."))
													{
														int t = (row.toString().length() - row.toString().indexOf(".")) - (jsonList.get(intUindex).toString().length() - jsonList.get(intUindex).toString().indexOf("."));
														if (t==1)
														{
															jsonList.set(intUindex, (jsonList.get(intUindex).toString() + "0"));
														}
														else if(t==2){
															jsonList.set(intUindex, (jsonList.get(intUindex).toString() + "00"));
														}
														row = rs.getString(rsmd.getColumnName(i));
													}
													else
													{
														int t = (row.toString().length() - row.toString().indexOf("."));
														if (t==1)
														{
															jsonList.set(intUindex, (jsonList.get(intUindex).toString() + "."));
														}
														else if(t==2){
															jsonList.set(intUindex, (jsonList.get(intUindex).toString() + ".0"));
														}
														else if(t==3){
															jsonList.set(intUindex, (jsonList.get(intUindex).toString() + ".00"));
														}
														else if(t==4){
															jsonList.set(intUindex, (jsonList.get(intUindex).toString() + ".000"));
														}
														row = rs.getString(rsmd.getColumnName(i));
													}
												}
												else
												{
													if(type == Types.NUMERIC && !Objects.equals(jsonList.get(intUindex), null))
													{
														if((double)jsonList.get(intUindex) == 0.0 && Objects.equals(rs.getObject(strColumns.get(i)), null))
														{
															jsonList.set(intUindex, null);
														}
													}
												}
												if(type == Types.INTEGER && !Objects.equals(jsonList.get(intUindex), null))
												{
													if((int)jsonList.get(intUindex) == 0 && Objects.equals(rs.getObject(rsmd.getColumnName(i)), null))
													{
														jsonList.set(intUindex, null);
													}
												}
												//added new
												if(type == Types.SMALLINT && !Objects.equals(jsonList.get(intUindex), null))
												{
													//System.out.println(jsonList.get(intUindex).getClass());
													if(jsonList.get(intUindex) == "" && Objects.equals(rs.getString(rsmd.getColumnName(i)), null))
													{
														jsonList.set(intUindex, null);
													}
													if(row != null)
													{
														row = rs.getString(rsmd.getColumnName(i));
													}
												}

												else if((type == Types.VARCHAR ||type == Types.CHAR) && !Objects.equals(jsonList.get(intUindex), null))
												{
													if(jsonList.get(intUindex).equals("Retail"))
													{
														if(Objects.equals(row, "RT") || Objects.equals(row, null))
														{
															jsonList.set(intUindex, "RT");
															row = rs.getString(rsmd.getColumnName(i));
														}
													}
													if(jsonList.get(intUindex).equals("MailOrder"))
													{
														if(Objects.equals(row, "MO"))
														{
															jsonList.set(intUindex, "MO");
															row = rs.getString(rsmd.getColumnName(i));
														}
														else if (Objects.equals(row, "MR"))
														{
															jsonList.set(intUindex, "MR");
															row = rs.getString(rsmd.getColumnName(i));
														}
													}

													if((jsonList.get(intUindex).getClass() == java.lang.Double.class) && !Objects.equals(row, null))
													{
														if(row.toString().length() == jsonList.get(intUindex).toString().length())
														{
															jsonList.set(intUindex, (jsonList.get(intUindex).toString()));
															row = rs.getString(rsmd.getColumnName(i));
														}
														else if(jsonList.get(intUindex).toString().contains("."))
														{
															int t = (row.toString().length() - row.toString().indexOf(".")) - (jsonList.get(intUindex).toString().length() - jsonList.get(intUindex).toString().indexOf("."));
															if (t==1)
															{
																jsonList.set(intUindex, (jsonList.get(intUindex).toString() + "0"));
															}
															else if(t==2){
																jsonList.set(intUindex, (jsonList.get(intUindex).toString() + "00"));
															}
															row = rs.getString(rsmd.getColumnName(i));
														}
														else
														{
															int t = (row.toString().length() - row.toString().indexOf("."));
															if (t==1)
															{
																jsonList.set(intUindex, (jsonList.get(intUindex).toString() + "."));
															}
															else if(t==2){
																jsonList.set(intUindex, (jsonList.get(intUindex).toString() + ".0"));
															}
															else if(t==3){
																jsonList.set(intUindex, (jsonList.get(intUindex).toString() + ".00"));
															}
															else if(t==4){
																jsonList.set(intUindex, (jsonList.get(intUindex).toString() + ".000"));
															}
															row = rs.getString(rsmd.getColumnName(i));
														}
													}
													else if(jsonList.get(intUindex).toString().equals("") && Objects.equals(rs.getObject(rsmd.getColumnName(i)), null))
													{
														jsonList.set(intUindex, null);
													}
													else if(!Objects.equals(jsonList.get(intUindex), rs.getString(rsmd.getColumnName(i))) && row != null)//added new
													{
														row = rs.getString(rsmd.getColumnName(i)).toString().replaceAll("\\s+$","");
														//String ltrim = s.replaceAll("^\\s+","");
														//String rtrim = s.replaceAll("\\s+$","");
													}
													else//added new
													{
														//System.out.println(jsonList.get(intUindex));
														row = rs.getString(rsmd.getColumnName(i)).replaceAll("\\s+$","");
														//System.out.println(row);
													}
												}


												if(type == Types.TIMESTAMP)
												{

													//jsonList.set(i-1, (Timestamp) jsonList.get(i-1).toString());
													row = rs.getString(i);
													int t = (row.toString().length() - row.toString().indexOf("."));
													if(row.toString().endsWith(".0") || row.toString().endsWith(".00") ||  row.toString().endsWith(".000"))
													{
														if (t==1)
														{
															jsonList.set(intUindex, (jsonList.get(intUindex).toString() + "."));
														}
														else if(t==2){
															jsonList.set(intUindex, (jsonList.get(intUindex).toString() + ".0"));
														}
														else if(t==3){
															jsonList.set(intUindex, (jsonList.get(intUindex).toString() + ".00"));
														}
														else if(t==4){
															jsonList.set(intUindex, (jsonList.get(intUindex).toString() + ".000"));
														}
													}
													row = rs.getString(i);

												}
												/*Log.info("Column Name: "+rsmd.getColumnName(i)+", Column Data Type: "+rsmd.getColumnTypeName(i)+", Column Data: "+row);
												Log.info("jsonList.get(" + intUindex +")="+jsonList.get(intUindex));*/
												//System.out.println("Column Name: "+rsmd.getColumnName(i)+", Column Data Type: "+rsmd.getColumnTypeName(i)+", Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex));
												//System.out.println("jsonList.get(" + intUindex +")="+jsonList.get(intUindex));
												if ( Objects.equals(row, jsonList.get(intUindex)))
												{
													Log.info("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Pass");
													//System.out.println("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Pass");
													objHandler.setStrRequestStatus("Pass");
													//System.out.println("Pass");
												}
												else if ((jsonList.get(intUindex)).equals("00010101"))
												{
													//System.out.println("inside");
													if (row == " ")
													{
														Log.info("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Pass");
														//System.out.println("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Pass");objHandler.setStrRequestStatus("Pass");
														//System.out.println("Pass");
													}
													else
													{
														Log.info("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Fail");
														System.out.println("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Fail");objHandler.setStrRequestStatus("Fail");
														objHandler.setStrRequestStatus("Fail");
														//System.out.println("Fails");
														break Comparision;
													}
												}
												else
												{
													Log.info("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Fail");
													System.out.println("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Fail");objHandler.setStrRequestStatus("Fail");
													objHandler.setStrRequestStatus("Fail");
													//System.out.println("Fails");
													break Comparision;
												}
												intUindex = intUindex + 1;

											}
										}


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
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
