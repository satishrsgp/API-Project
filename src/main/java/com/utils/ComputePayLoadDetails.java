package com.utils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.testscripts.BaseTest;

public class ComputePayLoadDetails {

	/*public static HashMap<String, Object> computePayLoadMap(HashMap<String, String> parameters, String strQuery, String jsonFilePath)
	{
		HashMap<String, Object> mapPostDetails = new HashMap<>();
		try
		{
			String strTrackingComment = "";
			String strPayLoad = new String(Files.readAllBytes(Paths.get("./TestData/" + jsonFilePath + ".json")));
			if(!Objects.equals(parameters.get("trackingComment"), null))
				strTrackingComment = parameters.get("trackingComment") + GeneralUtils.getDateTime();
			parameters.put("trackingComment",strTrackingComment);
			mapPostDetails.put("trackingComment", strTrackingComment);

			mapPostDetails.put("PayLoad", strPayLoad.replace("#Comment#", parameters.get("trackingComment")));
			if(!Objects.equals(parameters.get("trackingComment"), null) && (parameters.get("trackingComment").toString().length() == 36) && (parameters.get("trackingComment").toString().substring(8, 9).equals("-") && parameters.get("trackingComment").toString().substring(13, 14).equals("-") && parameters.get("trackingComment").toString().substring(18, 19).equals("-") && parameters.get("trackingComment").toString().substring(23,24).equals("-")))
			{
				mapPostDetails.put("countBeforePOST", DatabaseConnection.returnNumberOfRows(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery)));
			}
			else
				mapPostDetails.put("countBeforePOST", 0);

		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return mapPostDetails;
	}*/

	public static HashMap<String, Object> computePayLoadMap(HashMap<String, String> parameters, String strQuery, String jsonFilePath,String strTokenToBeReplaced,String strUniqueSQLColumn)
	{
		HashMap<String, Object> mapPostDetails = new HashMap<>();
		try
		{
			String strTrackingComment = "";
			String strPayLoad = new String(Files.readAllBytes(Paths.get("./TestData/" + jsonFilePath + ".json")));
			if(!Objects.equals(parameters.get(strTokenToBeReplaced), null))
				strTrackingComment = parameters.get(strTokenToBeReplaced) + GeneralUtils.getDateTime();
			parameters.put(strTokenToBeReplaced,strTrackingComment);
			mapPostDetails.put(strTokenToBeReplaced, strTrackingComment);

			mapPostDetails.put("PayLoad", strPayLoad.replace("#"+strTokenToBeReplaced+"#", parameters.get(strTokenToBeReplaced)));

			if(!Objects.equals(strUniqueSQLColumn, "null") && (strUniqueSQLColumn.length() == 36) && (strUniqueSQLColumn.substring(8, 9).equals("-") && strUniqueSQLColumn.substring(13, 14).equals("-") && strUniqueSQLColumn.substring(18, 19).equals("-") && strUniqueSQLColumn.substring(23,24).equals("-")))
			{
				mapPostDetails.put("countBeforePOST", DatabaseConnection.returnNumberOfRows(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery)));
			}
			else
				mapPostDetails.put("countBeforePOST", 0);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return mapPostDetails;
	}

	public static HashMap<String, Object> computePayLoadMap(HashMap<String, String> parameters, String strQuery, String jsonFilePath,String strTokenToBeReplaced)
	{
		HashMap<String, Object> mapPostDetails = new HashMap<>();
		try
		{
			String strTrackingComment = "";
			String strPayLoad = new String(Files.readAllBytes(Paths.get("./TestData/" + jsonFilePath + ".json")));
			if(!Objects.equals(parameters.get(strTokenToBeReplaced), null))
				strTrackingComment = parameters.get(strTokenToBeReplaced) + GeneralUtils.getDateTime();
			parameters.put(strTokenToBeReplaced,strTrackingComment);
			mapPostDetails.put(strTokenToBeReplaced, strTrackingComment);

			mapPostDetails.put("PayLoad", strPayLoad.replace("#"+strTokenToBeReplaced+"#", parameters.get(strTokenToBeReplaced)));

			boolean blnGUIDStatus = true;
			//Check if the parameters map has got invalid GUIDs
			for(String strKey : parameters.keySet())
			{
				//Check if the current parameter is a GUID or not
				if(strKey.toLowerCase().endsWith("id"))
				{
					//Check if the parameter is a valid GUID or not 
					if(!Objects.equals(parameters.get(strKey), "null") && (parameters.get(strKey).length() == 36) && (parameters.get(strKey).substring(8, 9).equals("-") && parameters.get(strKey).substring(13, 14).equals("-") && parameters.get(strKey).substring(18, 19).equals("-") && parameters.get(strKey).substring(23,24).equals("-")))
					{
						blnGUIDStatus = blnGUIDStatus && true;
					}
					else
						blnGUIDStatus = blnGUIDStatus && false;
				}
			}

			if(blnGUIDStatus)
			{
				mapPostDetails.put("countBeforePOST", DatabaseConnection.returnNumberOfRows(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery)));
			}
			else
				mapPostDetails.put("countBeforePOST", 0);

		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return mapPostDetails;
	}

	public static HashMap<String,Object> computePUTPayloadMap(HashMap<String,String> parameters, String jsonFilePath)
	{
		HashMap<String, Object> mapPutDetails = new HashMap<>();
		try
		{

			String strPayLoad = null;
			if(!jsonFilePath.isEmpty())
			{
				strPayLoad = new String(Files.readAllBytes(Paths.get("./TestData/" + jsonFilePath + ".json")));
				ArrayList<String> lstStrTokenToBeReplaced = computeJSONTokensToBeReplaced(parameters);
				//Edit the payload with the test data from excel file
				for(String strTokenData : lstStrTokenToBeReplaced)
				{
					strPayLoad = strPayLoad.replace("#"+strTokenData+"#", parameters.get(strTokenData));
				}
				//Add the final payload to the map that has to be returned
				mapPutDetails.put("PayLoad", strPayLoad);
			}
			else
			{
				mapPutDetails.put("PayLoad", "");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return mapPutDetails;
	}
	
	
	public static HashMap<String, Object> computePayLoadMap(HashMap<String, String> parameters, String strQuery, String jsonFilePath)
	{
		HashMap<String, Object> mapPostDetails = new HashMap<>();
		try
		{
			String strPayLoad = null;
			if(!jsonFilePath.isEmpty())
			{
				strPayLoad = new String(Files.readAllBytes(Paths.get("./TestData/" + jsonFilePath + ".json")));
				ArrayList<String> lstStrTokenToBeReplaced = computeJSONTokensToBeReplaced(parameters);
				//Edit the payload with the test data from excel file
				for(String strTokenData : lstStrTokenToBeReplaced)
				{
					strPayLoad = strPayLoad.replace("#"+strTokenData+"#", parameters.get(strTokenData));
				}
				//Add the final payload to the map that has to be returned
				mapPostDetails.put("PayLoad", strPayLoad);
			}
			else
			{
				mapPostDetails.put("PayLoad", "");
			}

			//Boolean variable to hold value if SQL has to be executed or not
			boolean blnGUIDStatus = false;
			for(String strKey : parameters.keySet())
			{
				//Check if the current parameter is a GUID or not
				if(strKey.endsWith("Id") || strKey.equals("gcnSeqNo"))
				{
					//Check if the parameter is a valid GUID or not 
					if(!Objects.equals(parameters.get(strKey), "null"))
					{
						if(strKey.equals("userId"))
						{
							blnGUIDStatus = true;
						}
						// satish addded on 2018/01/19
						if(strKey.equals("healthConcernId"))
						{
							blnGUIDStatus = true;
						}
						// Pavana added for Favorites_AddCustomDosageOrder route
						if(strKey.equals("gcnSeqNo"))
						{
							blnGUIDStatus = true;
						}
						if((parameters.get(strKey).length() == 36) && (parameters.get(strKey).substring(8, 9).equals("-") && parameters.get(strKey).substring(13, 14).equals("-") && parameters.get(strKey).substring(18, 19).equals("-") && parameters.get(strKey).substring(23,24).equals("-")))
						{
							blnGUIDStatus = true;
						}
						else
							break;
					}
				}
			}

			//If the parameters included only valid GUIDs, then execute SQL else do not execute SQL
			if(blnGUIDStatus)
			{
				mapPostDetails.put("countBeforePOST", DatabaseConnection.returnNumberOfRows(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery)));
			}
			else
				mapPostDetails.put("countBeforePOST", 0);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return mapPostDetails;
	}

	public static ArrayList<String> computeJSONTokensToBeReplaced(HashMap<String, String> parameters)
	{
		ArrayList<String> arList = new ArrayList<String>();
		try
		{
			for(Map.Entry<String,String> map : parameters.entrySet())
			{
				//System.out.println("Key:"+map.getKey());
				//System.out.println("Value:"+map.getValue());
				arList.add(map.getKey());
			}
			//System.out.println(arList);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			arList = null;
		}
		return arList;
	}

}