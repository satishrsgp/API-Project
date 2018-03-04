package com.utils;

import java.util.ArrayList;
import java.util.Objects;

import com.jayway.jsonpath.JsonPath;

public class Json_Parser 
{

	//It will take JsonTokenName, JsonResponse and an integer value to extract the value of 
	public static ArrayList<Object> jsonTokenValue(String strTokenName, String strResponse, int j, ArrayList<Object> jsonList)
	{
		if(Objects.equals(JsonPath.read(strResponse,"$.."+strTokenName+"["+j+"]"), null))
		{
			jsonList.add(null);
		}
		else
		{
			jsonList.add(JsonPath.read(strResponse,"$.."+strTokenName+"["+j+"]"));
		}
		return jsonList;
		
	}
}
