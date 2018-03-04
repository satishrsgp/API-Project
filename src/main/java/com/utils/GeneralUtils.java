package com.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.TimeZone;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.testscripts.BaseTest;

public class GeneralUtils {

	public static String getDateTime()
	{
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HHmmss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static String getXNGDate()
	{
		final Date currentTime = new Date();
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

		//final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");

		//final SimpleDateFormat sdf1 = new SimpleDateFormat();

		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		//System.out.println("UTC time: " + sdf.format(currentTime));
		return sdf.format(currentTime).toString();
	}

	public static String createFolder()
	{
		String strFolderPath = "TestResults/" + (GeneralUtils.getDateTime()).substring(0, 10);
		File file = new File(strFolderPath);
		if (!file.exists()) 
		{
			if (file.mkdir()) 
			{
				System.out.println("Directory is created! \n");
			}else 
			{
				System.out.println("Failed to create directory! \n");
			}
		}else 
		{
			System.out.println("Test Results Folder Already exists for the Current Date \n");
		}
		return strFolderPath;
	}


	public static String prettyprintJsonResponse(String strResponse)
	{
		String response = null;
		try
		{
			com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
			Object json = mapper.readValue(strResponse, Object.class);
			//System.out.println("This is deprecated"+mapper.defaultPrettyPrintingWriter().writeValueAsString(json));
			//response = mapper.enable(com.fasterxml.jackson.databind.SerializationConfig. .INDENT_OUTPUT).writeValueAsString(json);
			response = mapper.enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(json);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return response;
	}

	public static String formatDateasYYYYMMDDWhenDateDoesNotHaveT(String strDate)
	{
		String strCustomDate= null;//changed to null
		//strCustomDate = "";
		if(Objects.equals(strDate, null))//changed
		{
			return null;
		}
		if(strDate != null && !strDate.isEmpty())
		{
			try
			{
				//String line= "03/28/1970 00:00:00-05:00";
				//String strDBDate = strDate.split(" ")[0];
				strCustomDate = strDate.substring(0, (strDate.length() - strDate.indexOf("T")+1)).replace("-", "");
				/*strCustomDate.append(strDBDate.substring(6, 10));
				strCustomDate.append(strDBDate.substring(0, 2));
				strCustomDate.append(strDBDate.substring(3, 5));*/
				//System.out.println(strCustomDate);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/*else if (!strDate.contains(""))//changed
		{
			return "";
		}*/
		else//changed
		{
			return "";
		}
		return strCustomDate;
	}

	public static String printTime(String str)
	{
		String strFinal = null;
		try
		{
			if(Integer.parseInt((str.substring(11,19)).substring(0, 2)) > 12)
			{
				//System.out.println("Greater");
				int i = Integer.parseInt((str.substring(11,19)).substring(0, 2)) - 12;
				//System.out.println(i);
				strFinal = i + (str.substring(11,19)).substring(2, 5) + " PM";
				//System.out.println(strFinal);
			}
			else
			{
				//System.out.println("Lesser");
				int i = Integer.parseInt((str.substring(11,19)).substring(0, 2));
				//System.out.println(i);
				strFinal = i + (str.substring(11,19)).substring(2, 5) + " AM";
				//System.out.println(strFinal);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return strFinal;
	}

	/*public static String formatDateasYYYYMMDD(String strDate)
	{
		String strCustomDate= new String("");
		//strCustomDate = "";
		if(!strDate.isEmpty() && strDate != null)
		{
			try
			{
				//String line= "03/28/1970 00:00:00-05:00";
				String strDBDate = strDate.split(" ")[0];
				strCustomDate = strDBDate.substring(6, 10) + strDBDate.substring(0, 2) + strDBDate.substring(3, 5);
				strCustomDate.append(strDBDate.substring(6, 10));
				strCustomDate.append(strDBDate.substring(0, 2));
				strCustomDate.append(strDBDate.substring(3, 5));
				//System.out.println(strCustomDate);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return strCustomDate;
	}*/

	public static String formatDateasYYYYMMDD(String strDate)
	{
		String strCustomDate= null;//changed to null
		//strCustomDate = "";
		if(Objects.equals(strDate, null))//changed
		{
			return null;
		}
		if(strDate != null && !strDate.isEmpty())
		{
			try
			{
				//String line= "03/28/1970 00:00:00-05:00";
				String strDBDate = strDate.split(" ")[0];
				strCustomDate = strDBDate.substring(6, 10) + strDBDate.substring(0, 2) + strDBDate.substring(3, 5);
				/*strCustomDate.append(strDBDate.substring(6, 10));
                 strCustomDate.append(strDBDate.substring(0, 2));
                 strCustomDate.append(strDBDate.substring(3, 5));*/
				//System.out.println(strCustomDate);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/*else if (!strDate.contains(""))//changed
         {
             return "";
         }*/
		else//changed
		{
			return "";
		}
		return strCustomDate;
	}

	public static int ToAbnormalFlagIndex(String strAbnormalFlag)
	{
		int intAbnormalFlagIndex = 0;
		try
		{
			if(strAbnormalFlag.isEmpty() || Objects.equals(strAbnormalFlag, null))
			{
				intAbnormalFlagIndex = 1;
			}
			if(strAbnormalFlag.equals("H"))
			{
				intAbnormalFlagIndex = 4;
			}
			else if(strAbnormalFlag.equals("HH"))
			{
				intAbnormalFlagIndex = 5;
			}
			else if(strAbnormalFlag.equals(">"))
			{
				intAbnormalFlagIndex = 5;
			}
			else if(strAbnormalFlag.equals("L"))
			{
				intAbnormalFlagIndex = 3;
			}
			else if(strAbnormalFlag.equals("LL"))
			{
				intAbnormalFlagIndex = 0;
			}
			else if(strAbnormalFlag.equals("<"))
			{
				intAbnormalFlagIndex = 3;
			}
			else if(strAbnormalFlag.equals("A"))
			{
				intAbnormalFlagIndex = 2;
			}
			else if(strAbnormalFlag.equals("AA"))
			{
				intAbnormalFlagIndex = 2;
			}
			else if(strAbnormalFlag.equals("U"))
			{
				intAbnormalFlagIndex = 2;
			}
			else if(strAbnormalFlag.equals("D"))
			{
				intAbnormalFlagIndex = 2;
			}
			else if(strAbnormalFlag.equals("B"))
			{
				intAbnormalFlagIndex = 2;
			}
			else if(strAbnormalFlag.equals("W"))
			{
				intAbnormalFlagIndex = 2;
			}
			else if(strAbnormalFlag.equals("R"))
			{
				intAbnormalFlagIndex = 2;
			}
			else if(strAbnormalFlag.equals("I"))
			{
				intAbnormalFlagIndex = 2;
			}
			else if(strAbnormalFlag.equals("S"))
			{
				intAbnormalFlagIndex = 0;
			}
			else if(strAbnormalFlag.equals("MS"))
			{
				intAbnormalFlagIndex = 0;
			}
			else if(strAbnormalFlag.equals("VS"))
			{
				intAbnormalFlagIndex = 0;
			}
			else if(strAbnormalFlag.equals("N"))
			{
				intAbnormalFlagIndex = 1;
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return intAbnormalFlagIndex;
	}


	public static String TostrResultStatus(int intResultStatus)
	{
		String strRetVal = "U";
		try
		{
			switch (intResultStatus)
			{
			case 0:		{ strRetVal = "U";	break;}
			case 1:		{ strRetVal = "P";	break;}
			case 2:		{ strRetVal = "A";	break;}
			case 3:		{ strRetVal = "C";	break;}
			case 4:		{ strRetVal = "I";	break;}
			case 5:		{ strRetVal = "O";	break;}
			case 6:		{ strRetVal = "D";	break;}
			case 7:		{ strRetVal = "X";	break;}
			case 8:		{ strRetVal = "CA";	break;}
			case 9:		{ strRetVal = "DC";	break;}
			case 10:	{ strRetVal = "ER";	break;}
			case 11:	{ strRetVal = "HD";	break;}
			case 12:	{ strRetVal = "IP";	break;}
			case 13:	{ strRetVal = "SC";	break;}
			case 14:	{ strRetVal = "RP";	break;}
			case 15:	{ strRetVal = "CM";	break;}
			case 16:	{ strRetVal = "F";	break;}
			case 17:	{ strRetVal = "U";	break;}
			default : 	{ strRetVal = "U"; break;}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return strRetVal;
	}

	public static String returnDateAsddmmyyyy(String strDate)
	{
		String strCustomDate= null;//changed to null
		if(Objects.equals(strDate, null))//changed
		{
			return null;
		}
		if(strDate != null && !strDate.isEmpty())
		{
			try
			{

				strCustomDate = strDate.substring(5, 7)+"-"+strDate.substring(8, 10)+"-"+strDate.substring(0, 4);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else//changed
		{
			return "";
		}
		return strCustomDate;

	}



	//Added new on 11-10-2016
	//  Read the data from Properties file and builds the map
	public static Map<String,String> readPropsToMap(String strPropFilePath,String strDelimiter)
	{
		Map<String,String> retMapOfProps = new HashMap<String, String>();
		try
		{
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(strPropFilePath));
			String line;
			while((line = br.readLine())!= null)
			{
				if(!(line.trim().length()==0))
				{
					if(!(line.startsWith("#")))
					{
						int intPos = line.indexOf(strDelimiter);
						retMapOfProps.put(line.substring(0, intPos).trim(), line.substring(intPos+1).trim());
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			retMapOfProps = null;
		}
		return retMapOfProps;
	}

	public static String readValueFromProps(String strPropFilePath, String strKey) throws IOException
	{
		Properties prop = new Properties();
		InputStream inputStream = null;
		try
		{
			inputStream = new FileInputStream(strPropFilePath);

			if (inputStream != null) {
				prop.load(inputStream);
			} 
		} catch (Exception e)
		{
			Log.info("Exception: " + e);
			System.out.println("Exception: " + e);
		} finally
		{
			inputStream.close();
		}
		return prop.getProperty("strKey");
	}


	public static Map<String, String> generateQueryString(String strSheetName, int intRowNum) 
	{
		Map<String, Object> mapQueryString = new HashMap<String, Object>(); //Map that can used to frame the Query String parameters
		Map<String, Object> mapSqlQueryString = new HashMap<String, Object>(); //Map that can used to frame the SQL Query Where Condition
		Map<String, String> maptoReturn = new HashMap<String, String>();
		String strExcelPath = BaseTest.mapExecutionConfigs.get("TestDataWorkBook");
		//String strExcelPath = "./TestData/TestData_UD3.xlsx";
		StringBuffer strQueryString = new StringBuffer();
		StringBuffer strQuery = new StringBuffer();

		//int intRowCount = ExcelReaderWriter.getRowCount(strExcelPath, strSheetName);
		int intColumnCount = ExcelReaderWriter.getColumnCount(strExcelPath, strSheetName);
		// we need to read the Test Data sheet column by column of that particular row and frame Query String
		for(int i=0; i<intColumnCount; i++)
		{
			mapQueryString.put(ExcelReaderWriter.getCellValue(strExcelPath, strSheetName, 0, i), ExcelReaderWriter.getCellValue(strExcelPath, strSheetName, intRowNum, i));
			mapSqlQueryString.put(ExcelReaderWriter.getCellValue(strExcelPath, strSheetName, 1, i), ExcelReaderWriter.getCellValue(strExcelPath, strSheetName, intRowNum, i));
		}
		/*            System.out.println(mapQueryString);
                    System.out.println(mapSqlQueryString);*/

		 Map<String, Object> treeMap = new TreeMap<String, Object>(mapQueryString);
		for(String key : treeMap.keySet())
		{
			if(!key.equalsIgnoreCase("ExpectedResponseCode") && !key.equalsIgnoreCase("Comment") )
			{
				if(!Objects.equals((mapQueryString.get(key)),null) && !mapQueryString.get(key).toString().isEmpty())
				{
					if(strQueryString.length()==0)
						strQueryString = strQueryString.append("?" + key + "=" + mapQueryString.get(key).toString());
					else
						strQueryString = strQueryString.append("&" + key + "=" + mapQueryString.get(key).toString());          
				}
			}
		}


		for(String key : mapSqlQueryString.keySet())
		{
			if(!Objects.equals(key, null) && !key.toString().isEmpty())
			{
				if(!Objects.equals((mapSqlQueryString.get(key)),null) && !mapSqlQueryString.get(key).toString().isEmpty())
				{
					if(strQuery.length()==0)
					{
						if(!key.equalsIgnoreCase("date_Of_Birth"))
							strQuery = strQuery.append(key + " like " + "\'%" + mapSqlQueryString.get(key).toString() + "%\' ");
						else
							strQuery = strQuery.append(key + " like " + "\'%" + mapSqlQueryString.get(key).toString().replace("-", "") + "%\' ");
					}
					else
					{
						if(!key.equalsIgnoreCase("date_Of_Birth"))
							strQuery = strQuery.append("and " + key + " like " + "\'%" + mapSqlQueryString.get(key).toString() + "%\' ");
						else
							strQuery = strQuery.append("and " + key + " like " + "\'%" + mapSqlQueryString.get(key).toString().replace("-", "") + "%\' ");
					}              
				}
			}
		}
		maptoReturn.put("QueryString", strQueryString.toString());
		maptoReturn.put("Query", strQuery.toString());

		System.out.println(strQueryString);
		System.out.println(strQuery);
		return maptoReturn;
	}
	
	
	public static String getSimpleName(String strClassName)
    {
        String strSimpleClassName = strClassName.substring(0, strClassName.lastIndexOf("_"));
        System.out.println("Running test case " + strClassName+ " with Thread Id:- " + Thread.currentThread().getId());
        Log.info("Running test case " + strClassName+ " with Thread Id:- " + Thread.currentThread().getId());
        return strSimpleClassName;
    }

    public static synchronized String sheetName(String strSimpleClassName)
    {
        if(strSimpleClassName.length()>=31)
            strSimpleClassName = strSimpleClassName.substring(0, 31);
        return strSimpleClassName;
    }
    
    
    public static String AddDate(int datenumber,String format)
    {
    	String Mydate = null;
    	DateFormat dateFormat = new SimpleDateFormat(format);
    	Calendar c = Calendar.getInstance(); 
    	c.setTime(new Date());
    	c.add(Calendar.DATE, datenumber);
    	System.out.println(dateFormat.format(c.getTime()));
    	Mydate = dateFormat.format(c.getTime());	
		return Mydate;
    	    	    	
		
    	
    }
}
