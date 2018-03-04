package com.utils;

import java.util.HashMap;

public class RequestParameters 
{
	
	//Use this method if Route has any Parameters (Test Data)
	public static HashMap<String, Object> generateRequestParamsHashMap(String strRequestType, String strPayLoad, String strQueryString)
	{
		HashMap<String, Object> mapRequestParrameters = new HashMap<String, Object>();
		try
		{
			//Preparing the Map with all the Request details
			mapRequestParrameters.put("strRequestType", strRequestType);
			mapRequestParrameters.put("strPayLoad", strPayLoad);
			mapRequestParrameters.put("strQueryString", strQueryString);
		}catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return mapRequestParrameters;
		
	}
	
	//Use this method if Route has no Parameters (Test Data)
	public static HashMap<String, Object> generateRequestParamsHashMap(String strRequestType, String strPayLoad, String strQueryString, String strURI)
	{
		HashMap<String, Object> mapRequestParrameters = new HashMap<String, Object>();
		try
		{
			//Preparing the Map with all the Request details
			mapRequestParrameters.put("strRequestType", strRequestType);
			mapRequestParrameters.put("strPayLoad", strPayLoad);
			mapRequestParrameters.put("strQueryString", strQueryString);
			mapRequestParrameters.put("strURI", strURI);
		}catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return mapRequestParrameters;
		
	}
	
	
	
}
