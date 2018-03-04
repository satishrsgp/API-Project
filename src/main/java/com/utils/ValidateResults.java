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

import org.apache.http.HttpStatus;

public class ValidateResults 
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
						//System.out.println("The SQL resultset has "+rsmd.);
						for (int i = 1; i <= rsmd.getColumnCount(); i++)
						{
							int type = rsmd.getColumnType(i);
							if(!Objects.equals(rs.getObject(i), null))
							{
								if(rs.getObject(i).toString().endsWith(".jp2"))
								{
									row = rs.getObject(i).toString().replace(".jp2", "").replaceAll("\\s+$", "");
								}
								else
								{
									row = rs.getObject(i);
								}
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
											if (Objects.equals(row.toString().replaceAll("\\s+$","").replaceAll("^\\s+", ""), jsonList.get(l).toString().replaceAll("\\s+$","").replaceAll("^\\s+", "")))
											{
												intUindex = l;
												////System.out.println("intUindex= "+l);
												bol = true;
												break Unique_ID;
											}

										}
									}
							}

							////System.out.println("The Json Type" + jsonList.get(intUindex).getClass());
							////System.out.println("The SQL Type" + rsmd.getColumnTypeName(i));

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

							//added newly on 20-07-2016
							if(type == Types.INTEGER )//&& !Objects.equals(jsonList.get(intUindex), null) && jsonList.get(intUindex).getClass() == java.lang.String.class)
							{
								if(!Objects.equals(jsonList.get(intUindex), null))
								{
									if(jsonList.get(intUindex).getClass() == java.lang.String.class)
									{
										////System.out.println("I came inside newly added code");
										if(Objects.equals(String.valueOf(rs.getInt(i)), jsonList.get(intUindex)))
										{
											row = rs.getString(i);
										}
									}
								}
							}

							if(type == Types.SMALLINT && !Objects.equals(jsonList.get(intUindex), null))
							{
								////System.out.println(jsonList.get(intUindex).getClass());
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
									row = rs.getString(i).toString().replaceAll("\\s+$","");
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
									row = rs.getString(i).toString().replaceAll("\\s+$","").replaceAll("^\\s+", "");
								}
								else if(!Objects.equals(rs.getString(i), null))//added new 
								{ 
									////System.out.println(jsonList.get(intUindex)); 
									//added on 16-08-2016
									//deleting the spaces at the starting and ending of the SQL value and jsonList
									row = rs.getString(i).toString().replaceAll("\\s+$","").replaceAll("^\\s+", "");
									jsonList.set(intUindex, jsonList.get(intUindex).toString().replaceAll("\\s+$","").replaceAll("^\\s+", ""));
									////System.out.println(row); }
								}
								//added newly on 01-08-2016
								else if(Objects.equals(rs.getString(i), null) && Objects.equals(jsonList.get(intUindex), "0"))
								{
									jsonList.set(intUindex, null);
								}
								if(!Objects.equals(row, null) && !Objects.equals(jsonList.get(intUindex), null))
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
								//added newly on 20-07-2016
								if(!Objects.equals(row, null))
								{
									if(!Objects.equals(jsonList.get(intUindex), null))
									{
										if(jsonList.get(intUindex).getClass() == java.lang.Integer.class)
										{
											////System.out.println("I came inside newly added code");
											if(Objects.equals(rs.getString(i), String.valueOf(jsonList.get(intUindex))))
											{
												jsonList.set(intUindex, rs.getString(i));
											}
										}
									}
								}
								/*if((jsonList.get(intUindex).getClass() == java.lang.Integer.class) && !Objects.equals(rs, null) && !Objects.equals(jsonList.get(intUindex), null))
								{
									////System.out.println("I came inside newly added code");
									if(Objects.equals(rs.getString(i), String.valueOf(jsonList.get(intUindex))))
									{
										jsonList.set(intUindex, rs.getString(i));
									}
								}*/
							}
							else if(Objects.equals(row, "") && Objects.equals(jsonList.get(intUindex), null))
							{
								jsonList.set(intUindex, "");
							}

							if(Objects.equals(row, "00000000") && Objects.equals(jsonList.get(intUindex), null)){
								jsonList.set(intUindex, rs.getString(i));
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
									row = rs.getString(i).toString().replaceAll("\\s+$","").replaceAll("^\\s+", "");
									//added on 16-08-2016
									jsonList.set(intUindex, jsonList.get(intUindex).toString().replaceAll("\\s+$","").replaceAll("^\\s+", ""));
								}
								else
								{
									row = rs.getString(i).toString().replaceAll("\\s+$","").replaceAll("^\\s+", "");
									//added on 16-08-2016
									jsonList.set(intUindex, jsonList.get(intUindex).toString().replaceAll("\\s+$","").replaceAll("^\\s+", ""));
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

							if(type==Types.NUMERIC && Objects.equals(row, null))
							{
								Log.info("I came inside first if in numeric");
								if(Objects.equals(jsonList.get(intUindex), 0))
								{
									jsonList.set(intUindex, null);
									Log.info("I came inside second if in numeric");
								}
							}

							if(type == Types.TIMESTAMP)
							{
								//Try to fetch the values of the row and jsonList and modify only if both are not null
								if(!Objects.equals(row, null) && !Objects.equals(jsonList.get(intUindex), null))
								{


									////System.out.println("I am in tiemstamp for SQL value:"+row.toString()+" and jsonlist value:"+jsonList.get(intUindex).toString());
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
							}


							////System.out.println("Column Name: "+rsmd.getColumnName(i)+", Column Data Type: "+rsmd.getColumnTypeName(i)+", Column Data: "+row);
							////System.out.println("jsonList: " + (jsonList.get(intUindex) == null ? null:jsonList.get(intUindex).getClass())+"jsonList.get(" + intUindex +")="+jsonList.get(intUindex));
							////System.out.println("jsonList.get(" + intUindex +")="+jsonList.get(intUindex));
							if ( Objects.equals(row, jsonList.get(intUindex)))
							{
								Log.info("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" Jsonlist Data Type:"+ (jsonList.get(intUindex) == null ? null:jsonList.get(intUindex).getClass())+",jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Pass");
								objHandler.setStrRequestStatus("Pass");
								//System.out.println("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" Jsonlist Data Type:"+ (jsonList.get(intUindex) == null ? null:jsonList.get(intUindex).getClass())+",jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Pass");
								////System.out.println("Pass");
							}
							else if ((jsonList.get(intUindex)).equals("00010101"))
							{
								////System.out.println("inside");
								if (row == " " || Objects.equals(row, null) || Objects.equals(row, "00000000")|| Objects.equals(row, ""))//changed from if (row == " " || row.toString().isEmpty())
								{
									Log.info("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" Jsonlist Data Type:"+ (jsonList.get(intUindex) == null ? null:jsonList.get(intUindex).getClass())+",jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Pass");
									objHandler.setStrRequestStatus("Pass");
									//System.out.println("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" Jsonlist Data Type:"+ (jsonList.get(intUindex) == null ? null:jsonList.get(intUindex).getClass())+",jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Pass");
									////System.out.println("Pass");
								}
								//added new on 29-06-2016
								else
								{
									Log.info("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" Jsonlist Data Type:"+ (jsonList.get(intUindex) == null ? null:jsonList.get(intUindex).getClass())+",jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Fail");
									objHandler.setStrRequestStatus("Fail");
									//System.out.println("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" Jsonlist Data Type:"+ (jsonList.get(intUindex) == null ? null:jsonList.get(intUindex).getClass())+",jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Fail");
									break Comparision;
								}
							}
							else
							{
								Log.info("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" Jsonlist Data Type:"+ (jsonList.get(intUindex) == null ? null:jsonList.get(intUindex).getClass())+",jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Fail");
								objHandler.setStrRequestStatus("Fail");
								//System.out.println("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" Jsonlist Data Type:"+ (jsonList.get(intUindex) == null ? null:jsonList.get(intUindex).getClass())+",jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Fail");
								////System.out.println("SQL Column Name: "+rsmd.getColumnName(i)+ " , SQL Column Data: "+row+" , jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+",  Comparision: Fail");
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


	public static boolean resultsvalidation(ArrayList<Object> jsonList, ResultSet rs)
	{
		Boolean status=true;
		try
		{	
			if (rs != null && !(jsonList.size() == 0))
			{
				while (rs.next())
				{
					int intUindex = 0;
					boolean bol = false;
					Object row = ""; 
					ResultSetMetaData rsmd = rs.getMetaData();
					//System.out.println("The SQL resultset has "+rsmd.);
					for (int i = 1; i <= rsmd.getColumnCount(); i++)
					{
						int type = rsmd.getColumnType(i);
						if(!Objects.equals(rs.getObject(i), null))
						{
							/*if(rs.getObject(i).toString().endsWith(".jp2"))
							{
								row = rs.getObject(i).toString().replace(".jp2", "").replaceAll("\\s+$", "");
							}
							// 07/11/2017 --  added to make CreateTime stamp as a unique for Chart_GetSocialHistory_TC1 
							else*/ if (type == Types.TIMESTAMP) 
							{
								row = rs.getObject(i).toString();

							}
							// 08/17/2017 
							// Added the below else if statement to convert numeric data type to double in order to make this as a unique key
							//for ex: -  Before - Json list having 1740.0 but row was having 1740 hence not able to find unique key
							//			 After - Json list having 1740.0 and row is having 1740.0 hence we are able to find unique key
							else if (type == Types.NUMERIC) 
							{
								if(jsonList.get(intUindex).getClass() == java.lang.Double.class)
								{
									System.out.println("Its double");
									row = rs.getDouble(i);
								}
								else
								{
									row = rs.getObject(i);
								}

							}
							else
							{
								row = rs.getObject(i);
							}

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
									// 08/08/2017 - satish
									// row is of type bigDecimal and json type was integer with space
									// hence converted to string and removed the space
									if(!Objects.equals(row,null) && !Objects.equals(jsonList.get(l),null) )
									{
										if( Objects.equals(row.toString(), jsonList.get(l).toString().replaceAll("\\s+$","").replaceAll("^\\s+", "")))
										{
											if (Objects.equals(row.toString().replaceAll("\\s+$","").replaceAll("^\\s+", ""), jsonList.get(l).toString().replaceAll("\\s+$","").replaceAll("^\\s+", "")))
											{
												intUindex = l;
												////System.out.println("intUindex= "+l);
												bol = true;
												break Unique_ID;
											}
										}

									}

									//-- satish commented the below code
									/*if ( Objects.equals(row, jsonList.get(l)))
									{
										if (Objects.equals(row.toString().replaceAll("\\s+$","").replaceAll("^\\s+", ""), jsonList.get(l).toString().replaceAll("\\s+$","").replaceAll("^\\s+", "")))
										{
											intUindex = l;
											////System.out.println("intUindex= "+l);
											bol = true;
											break Unique_ID;
										}

									}*/
								}
						}

						////System.out.println("The Json Type" + jsonList.get(intUindex).getClass());
						////System.out.println("The SQL Type" + rsmd.getColumnTypeName(i));

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

						//added newly on 20-07-2016
						if(type == Types.INTEGER )//&& !Objects.equals(jsonList.get(intUindex), null) && jsonList.get(intUindex).getClass() == java.lang.String.class)
						{
							if(!Objects.equals(jsonList.get(intUindex), null))
							{
								if(jsonList.get(intUindex).getClass() == java.lang.String.class)
								{
									////System.out.println("I came inside newly added code");
									if(Objects.equals(String.valueOf(rs.getInt(i)), jsonList.get(intUindex)))
									{
										row = rs.getString(i);
									}
								}
							}
						}

						if(type == Types.SMALLINT && !Objects.equals(jsonList.get(intUindex), null))
						{
							////System.out.println(jsonList.get(intUindex).getClass());
							if(jsonList.get(intUindex) == "" && Objects.equals(rs.getString(i), null))
							{
								jsonList.set(intUindex, null);
							}
							else if(!Objects.equals(jsonList.get(intUindex), rs.getObject(i)) && row != null)
							{
								if(Objects.equals(jsonList.get(intUindex), "0.0"))
								{
									row = Integer.valueOf(rs.getString(i));
									jsonList.set(intUindex, 0);
								}
								else
								{
									row = rs.getString(i).toString().replaceAll("\\s+$","");
									jsonList.set(intUindex, String.valueOf(jsonList.get(intUindex)));
								}

							}
							else
							{
								row = rs.getString(i).toString().replaceAll("\\s+$","");
							}
						}

						if((type == Types.VARCHAR ||type == Types.CHAR) && !Objects.equals(jsonList.get(intUindex), null))
						{
							if(jsonList.get(intUindex).toString().equals("") && Objects.equals(rs.getObject(i), null))
							{
								jsonList.set(intUindex, null);
							}
							else if(!Objects.equals(jsonList.get(intUindex), rs.getString(i)) && row != null )
							{
								if(!row.toString().contains("-") && jsonList.get(intUindex).toString().contains("-") )
								{
									jsonList.set(intUindex, jsonList.get(intUindex).toString().replace("-", "").substring(0, jsonList.get(intUindex).toString().indexOf(" ")-1).replaceAll("\\s+$","").replaceAll("^\\s+", ""));
								}
								row = rs.getString(i).toString().replaceAll("\\s+$","").replaceAll("^\\s+", "");
							}
							else if(!Objects.equals(rs.getString(i), null))//added new 
							{ 
								////System.out.println(jsonList.get(intUindex)); 
								//added on 16-08-2016
								//deleting the spaces at the starting and ending of the SQL value and jsonList
								row = rs.getString(i).toString().replaceAll("\\s+$","").replaceAll("^\\s+", "");
								jsonList.set(intUindex, jsonList.get(intUindex).toString().replaceAll("\\s+$","").replaceAll("^\\s+", ""));
								////System.out.println(row); }
							}
							//added newly on 01-08-2016
							else if(Objects.equals(rs.getString(i), null) && Objects.equals(jsonList.get(intUindex), "0"))
							{
								jsonList.set(intUindex, null);
							}
							if(!Objects.equals(row, null) && !Objects.equals(jsonList.get(intUindex), null))
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
							//added newly on 20-07-2016
							if(!Objects.equals(row, null))
							{
								if(!Objects.equals(jsonList.get(intUindex), null))
								{
									if(jsonList.get(intUindex).getClass() == java.lang.Integer.class)
									{
										////System.out.println("I came inside newly added code");
										if(Objects.equals(rs.getString(i), String.valueOf(jsonList.get(intUindex))))
										{
											jsonList.set(intUindex, rs.getString(i));
										}
									}
								}
							}
							/*if((jsonList.get(intUindex).getClass() == java.lang.Integer.class) && !Objects.equals(rs, null) && !Objects.equals(jsonList.get(intUindex), null))
							{
								////System.out.println("I came inside newly added code");
								if(Objects.equals(rs.getString(i), String.valueOf(jsonList.get(intUindex))))
								{
									jsonList.set(intUindex, rs.getString(i));
								}
							}*/
						}
						else if(Objects.equals(row, "") && Objects.equals(jsonList.get(intUindex), null))
						{
							jsonList.set(intUindex, "");
						}

						if(Objects.equals(row, "00000000") && Objects.equals(jsonList.get(intUindex), null)){
							jsonList.set(intUindex, rs.getString(i));
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
								row = rs.getString(i).toString().replaceAll("\\s+$","").replaceAll("^\\s+", "");
								//added on 16-08-2016
								jsonList.set(intUindex, jsonList.get(intUindex).toString().replaceAll("\\s+$","").replaceAll("^\\s+", ""));
							}
							else
							{
								row = rs.getString(i).toString().replaceAll("\\s+$","").replaceAll("^\\s+", "");
								//added on 16-08-2016
								jsonList.set(intUindex, jsonList.get(intUindex).toString().replaceAll("\\s+$","").replaceAll("^\\s+", ""));
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
							//07/18/2017   added for the script Chart_AddOrderedVaccine_TC1 -- satish
							else if(jsonList.get(intUindex).getClass() == java.lang.Double.class)
							{
								System.out.println("Came inside!!!");
								row = rs.getDouble(i);
							}
						}

						if(type==Types.NUMERIC && Objects.equals(row, null))
						{
							Log.info("I came inside first if in numeric");
							if(Objects.equals(jsonList.get(intUindex), 0))
							{
								jsonList.set(intUindex, null);
								Log.info("I came inside second if in numeric");
							}
						}

						// 07/12/2017-  added to handle administeredTimestamp for the script Chart_GetOrderedVaccines_TC1 - Satish added
						if(type == Types.TIME)
						{
							if(!Objects.equals(jsonList.get(intUindex), null))
							{
								if(jsonList.get(intUindex).getClass() == java.lang.String.class)
								{
									////System.out.println("I came inside newly added code");
									if(Objects.equals(String.valueOf(rs.getTime(i)), jsonList.get(intUindex)))
									{
										row = rs.getTime(i).toString();
										//row = rs.getString(i);
									}
								}
							}
						}
						
						// 07/12/2017-  added to handle administeredTimestamp for the script Chart_GetOrderedVaccines_TC1 - Satish added
						if(type == Types.BIT)
						{
							if(!Objects.equals(jsonList.get(intUindex), null))
							{
								if(jsonList.get(intUindex).getClass() == java.lang.String.class)
								{
									
										row = rs.getObject(i).toString();
									
								}
							}
						}
						

						if(type == Types.TIMESTAMP)
						{
							//Try to fetch the values of the row and jsonList and modify only if both are not null
							if(!Objects.equals(row, null) && !Objects.equals(jsonList.get(intUindex), null))
							{


								////System.out.println("I am in tiemstamp for SQL value:"+row.toString()+" and jsonlist value:"+jsonList.get(intUindex).toString());
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
									//row = rs.getString(i);
									row = rs.getObject(i).toString();
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
									row = rs.getObject(i).toString();
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
									row = rs.getObject(i).toString();
								}


							}
							//  -satish added the else part
							else
							{
								row = rs.getString(i);


							}
						}


						////System.out.println("Column Name: "+rsmd.getColumnName(i)+", Column Data Type: "+rsmd.getColumnTypeName(i)+", Column Data: "+row);
						////System.out.println("jsonList: " + (jsonList.get(intUindex) == null ? null:jsonList.get(intUindex).getClass())+"jsonList.get(" + intUindex +")="+jsonList.get(intUindex));
						////System.out.println("jsonList.get(" + intUindex +")="+jsonList.get(intUindex));

						if ( Objects.equals(row, jsonList.get(intUindex)))
						{
							Log.info("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" Jsonlist Data Type:"+ (jsonList.get(intUindex) == null ? null:jsonList.get(intUindex).getClass())+",jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Pass");
							//objHandler.setStrRequestStatus("Pass");
							//System.out.println("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" Jsonlist Data Type:"+ (jsonList.get(intUindex) == null ? null:jsonList.get(intUindex).getClass())+",jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Pass");
							////System.out.println("Pass");
						}
						else if ((Objects.equals(jsonList.get(intUindex),"00010101"))) // ---- changed due ti null pointer exception
						{
							////System.out.println("inside");
							if (row == " " || Objects.equals(row, null) || Objects.equals(row, "00000000")|| Objects.equals(row, ""))//changed from if (row == " " || row.toString().isEmpty())
							{
								Log.info("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" Jsonlist Data Type:"+ (jsonList.get(intUindex) == null ? null:jsonList.get(intUindex).getClass())+",jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Pass");
								//objHandler.setStrRequestStatus("Pass");
								//System.out.println("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" Jsonlist Data Type:"+ (jsonList.get(intUindex) == null ? null:jsonList.get(intUindex).getClass())+",jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Pass");
								////System.out.println("Pass");
							}
							//added new on 29-06-2016
							else
							{
								Log.info("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" Jsonlist Data Type:"+ (jsonList.get(intUindex) == null ? null:jsonList.get(intUindex).getClass())+",jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Fail");
								status = false;
								//objHandler.setStrRequestStatus("Fail");
								//System.out.println("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" Jsonlist Data Type:"+ (jsonList.get(intUindex) == null ? null:jsonList.get(intUindex).getClass())+",jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Fail");

								return status;
								//break Comparision;
							}
						}
						else
						{
							Log.info("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" Jsonlist Data Type:"+ (jsonList.get(intUindex) == null ? null:jsonList.get(intUindex).getClass())+",jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Fail");
							status = false;
							//objHandler.setStrRequestStatus("Fail");
							//System.out.println("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" Jsonlist Data Type:"+ (jsonList.get(intUindex) == null ? null:jsonList.get(intUindex).getClass())+",jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Fail");
							////System.out.println("SQL Column Name: "+rsmd.getColumnName(i)+ " , SQL Column Data: "+row+" , jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+",  Comparision: Fail");

							return status;
							//break Comparision;
						}
						intUindex = intUindex + 1;
					}
					int count=rsmd.getColumnCount();

					while(count!=0)
					{
						jsonList.remove(intUindex-rsmd.getColumnCount());
						count--;
					}
				}
				//return status;
			}
		}

		catch(Exception e)
		{
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return status;
	}
	public static void resultsvalidation(Map<String , ArrayList<Object>> mList, RequestResponseHandler objHandler)
	{
		try
		{
			if(mList != null)
			{
				ArrayList<Object> jsonList = mList.get("jsonList");
				ArrayList<Object> dbList = mList.get("dbList");
				//System.out.println("jsonList is: " + jsonList);
				//System.out.println("dbList is: " + dbList);
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
									////System.out.println("Pass");
								}
								else if ((jsonList.get(i)).equals("00010101"))
								{
									//System.out.println("inside");
									if (dbList.get(i) == " ")
									{
										Log.info("dbList("+i+"): "+dbList.get(i)+" ,jsonList("+i+"): "+jsonList.get(i)+" ,Comparison: Pass");
										objHandler.setStrRequestStatus("Pass");
										////System.out.println("Pass");
									}
								}
								else
								{
									Log.info("dbList("+i+"): "+dbList.get(i)+" ,jsonList("+i+"): "+jsonList.get(i)+" ,Comparison: Fail");
									objHandler.setStrRequestStatus("Fail");
									//System.out.println("Fail");
									break Comparison;
								}
							}
					}
					else
					{
						Log.info("dbList has data and the returned Json does not have data. Please investigate");
						objHandler.setStrRequestStatus("Fail");
						//System.out.println("Fail");
					}
				}
				else
				{
					if(Objects.equals(jsonList, null))
					{
						Log.info("Both dbList and jsonList are null, Comparison: Pass");
						objHandler.setStrRequestStatus("Pass");
						////System.out.println("Pass");
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

	//Modified it to return boolean for POST
	public static boolean resultValidation(ArrayList<Object> jsonList,ArrayList<Object> dbList)
	{
		boolean status = true;
		try
		{
			if( Objects.equals(jsonList, null) && Objects.equals(dbList, null))
			{
				Log.info("Both jsonList and dbList are null!!!");
				return true;
			}
			else if(Objects.equals(jsonList, null))
			{
				if(Objects.equals(dbList, null))
				{
					Log.info("Both jsonList and dbList are null!!!");
					return true;
				}
				else
				{
					Log.error("jsonList is null but dbList is not null!!!");
					return false;
				}
			}	
			else if(!jsonList.isEmpty() && !dbList.isEmpty())
			{
				if(jsonList.size() == dbList.size())
				{

					for(int i = 0; i< jsonList.size(); i++)
					{

						if(!Objects.equals(jsonList.get(i), dbList.get(i)))
						{
							status = false;
							Log.error("Value of jsonList is: " + jsonList.get(i) + " , value of dbList is: " + dbList.get(i) + ", Comparison: Fails");
							break;
						}
						Log.info("Value of jsonList is: " + jsonList.get(i) + " , value of dbList is: " + dbList.get(i) + ", Comparison: Pass");
					}
				}
				else
				{
					//objHandler.setStrRequestStatus("Fail");
					status = false;
					Log.error("jsonList is: "+jsonList+", size is: "+jsonList.size());
					Log.error("dbList is: "+dbList+", size is: "+dbList.size());
					Log.info("jsonList and dbList sizes vary, Comparison: Fails");
				}
			}
			else
			{
				//objHandler.setStrRequestStatus("Pass");
				status = false;
				Log.error("Both jsonList or dbList is empty!!! Please check");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
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
											////System.out.println("rsmd.getColumnName(i) is "+rsmd.getColumnName(i));
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
																////System.out.println("intUindex= "+l);
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
													////System.out.println("Numeric for index: "+i);

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
													////System.out.println(jsonList.get(intUindex).getClass());
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
														////System.out.println(jsonList.get(intUindex));
														row = rs.getString(rsmd.getColumnName(i)).replaceAll("\\s+$","");
														////System.out.println(row);
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
												////System.out.println("Column Name: "+rsmd.getColumnName(i)+", Column Data Type: "+rsmd.getColumnTypeName(i)+", Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex));
												////System.out.println("jsonList.get(" + intUindex +")="+jsonList.get(intUindex));
												if ( Objects.equals(row, jsonList.get(intUindex)))
												{
													Log.info("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Pass");
													////System.out.println("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Pass");
													objHandler.setStrRequestStatus("Pass");
													////System.out.println("Pass");
												}
												else if ((jsonList.get(intUindex)).equals("00010101"))
												{
													////System.out.println("inside");
													if (row == " ")
													{
														Log.info("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Pass");
														////System.out.println("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Pass");objHandler.setStrRequestStatus("Pass");
														////System.out.println("Pass");
													}
													else
													{
														Log.info("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Fail");
														//System.out.println("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Fail");objHandler.setStrRequestStatus("Fail");
														objHandler.setStrRequestStatus("Fail");
														////System.out.println("Fails");
														break Comparision;
													}
												}
												else
												{
													Log.info("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Fail");
													//System.out.println("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Fail");objHandler.setStrRequestStatus("Fail");
													objHandler.setStrRequestStatus("Fail");
													////System.out.println("Fails");
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

	public static void resultsvalidation(Connection conn, String strStmt, LinkedHashMap <String, Object> strSPParam, List<String> strColumns,ArrayList<Object> jsonList, RequestResponseHandler objHandler,String strColumnName,String strText)
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
									if(rs.getString(strColumnName).equalsIgnoreCase(strText))
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
											////System.out.println("rsmd.getColumnName(i) is "+rsmd.getColumnName(i));
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
																////System.out.println("intUindex= "+l);
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
													////System.out.println("Numeric for index: "+i);

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
													////System.out.println(jsonList.get(intUindex).getClass());
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
														////System.out.println(jsonList.get(intUindex));
														row = rs.getString(rsmd.getColumnName(i)).replaceAll("\\s+$","");
														////System.out.println(row);
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
												////System.out.println("Column Name: "+rsmd.getColumnName(i)+", Column Data Type: "+rsmd.getColumnTypeName(i)+", Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex));
												////System.out.println("jsonList.get(" + intUindex +")="+jsonList.get(intUindex));
												if ( Objects.equals(row, jsonList.get(intUindex)))
												{
													Log.info("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Pass");
													////System.out.println("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Pass");
													objHandler.setStrRequestStatus("Pass");
													////System.out.println("Pass");
												}
												else if ((jsonList.get(intUindex)).equals("00010101"))
												{
													////System.out.println("inside");
													if (row == " ")
													{
														Log.info("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Pass");
														////System.out.println("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Pass");objHandler.setStrRequestStatus("Pass");
														////System.out.println("Pass");
													}
													else
													{
														Log.info("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Fail");
														//System.out.println("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Fail");objHandler.setStrRequestStatus("Fail");
														objHandler.setStrRequestStatus("Fail");
														////System.out.println("Fails");
														break Comparision;
													}
												}
												else
												{
													Log.info("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Fail");
													//System.out.println("SQL Column Name: "+rsmd.getColumnName(i)+", SQL Column Data Type: "+rsmd.getColumnTypeName(i)+", SQL Column Data: "+row+" ,jsonList.get(" + intUindex +")="+jsonList.get(intUindex)+", Comparision: Fail");objHandler.setStrRequestStatus("Fail");
													objHandler.setStrRequestStatus("Fail");
													////System.out.println("Fails");
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

	public static void validateStatusCode(RequestResponseHandler objHandler, int responseCodeExpected)
	{

		if(objHandler.getIntResponseCode() == responseCodeExpected)
		{
			System.out.println("The Status Code from the Response is " + objHandler.getIntResponseCode() + " and expected Status Code " + responseCodeExpected + " did match");
			Log.info("The Status Code from the Response " + objHandler.getIntResponseCode() + " and expected Status Code " + responseCodeExpected + " matched");
			objHandler.setStrRequestStatus("Pass");
		}
		else if(objHandler.getIntResponseCode() == HttpStatus.SC_NOT_FOUND)
		{
			System.out.println("Please check the Request URI");
			Log.info("The Request URI: " + objHandler.getStrRequestURI() +  " could not be found on the Server");
			objHandler.setStrRequestStatus("Fail");
		}
		else
		{
			System.out.println("The Status Code from the Response is " + objHandler.getIntResponseCode() + " and expected Status Code " + responseCodeExpected + " didn't match");
			Log.info("The Status Code from the Response " + objHandler.getIntResponseCode() + " and expected Status Code " + responseCodeExpected + " didn't match");
			objHandler.setStrRequestStatus("Fail");
		}
	}

	public static boolean resultsvalidation(Map<String , ArrayList<Object>> mList)
	{
		boolean bStatus = true;
		try
		{
			if(mList != null)
			{
				ArrayList<Object> jsonList = mList.get("jsonList");
				ArrayList<Object> dbList = mList.get("dbList");
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
								}
								else if ((jsonList.get(i)).equals("00010101"))
								{
									if (dbList.get(i) == " ")
									{
										Log.info("dbList("+i+"): "+dbList.get(i)+" ,jsonList("+i+"): "+jsonList.get(i)+" ,Comparison: Pass");
									}
								}
								else
								{
									Log.info("dbList("+i+"): "+dbList.get(i)+" ,jsonList("+i+"): "+jsonList.get(i)+" ,Comparison: Fail");
									bStatus = false;
									break Comparison;
								}
							}
					}
					else
					{
						Log.info("dbList has data and the returned Json does not have data. Please investigate");
						bStatus = false;
					}
				}
				else
				{
					if(Objects.equals(jsonList, null))
					{
						Log.info("Both dbList and jsonList are null, Comparison: Pass");
					}
					else
					{
						Log.info("Please correct the Query and reexcute the things");
						bStatus = false;
					}

				}	
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return bStatus;
	}
}
