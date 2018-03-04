package com.utils;

import java.sql.ResultSet;
import java.util.ArrayList;

import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

public class FinalAssertions 
{
	public static RequestResponseHandler assertResponseCount(String[] jsonMetaData, RequestResponseHandler objHandler, ResultSet rs, String strUniqueJsonToken)
	{
		try
		{
			ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, objHandler.getStrResponse(), strUniqueJsonToken);
			System.out.println("The API Call executed is : \n" + objHandler.getStrRequestURI() + "\n");
			System.out.println("The JSON Response in a List: \n" + jsonList + "\n");

			//ResultSet rs = DatabaseConnection.statementExecution(connNGA, strQuery);

			int intNumberOfRows = DatabaseConnection.returnNumberOfRows(rs);

			//Check the number of rows and if it is same as the number of ID's in jsonList then only proceed to validation
			Object obj = JsonPath.read(objHandler.getStrResponse(), "$.." + strUniqueJsonToken);

			if( intNumberOfRows == ((JSONArray) obj).size())
			{
				ValidateResults.resultsvalidation(jsonList, rs, objHandler);
			}
			else
			{

				System.out.println("The JSON List returned does not match with the SQL Result Set. Please Review the API Call");
				System.out.println("The JSON List size is :" + ((JSONArray) obj).size() + " and the SQL Result Set Row Count is : " + intNumberOfRows);
				Log.info("The JSON List returned does not match with the SQL Result Set. Please Review the API Call");
				Log.info("The JSON List size is :" + ((JSONArray) obj).size() + " and the SQL Result Set Row Count is : " + intNumberOfRows);
				objHandler.setStrRequestStatus("Fail");
			}

		} catch(Exception e)
		{
			objHandler.setStrRequestStatus("Fail");
			System.out.println("Java Error Encountered while executing assertResponseCount");
			Log.info("Java Error Encountered while executing assertResponseCount");
			e.printStackTrace();
		}

		return objHandler;
	}
	
	public static boolean assertResponseCount(ResultSet rs, String strResponse, String strJsonPath, ArrayList<Object> jsonList)
	{
		int intNumberOfRows = DatabaseConnection.returnNumberOfRows(rs);

		//Check the number of rows and if it is same as the number of ID's in jsonList then only proceed to validation
		Object obj = JsonPath.read(strResponse, strJsonPath);
		if( intNumberOfRows == ((JSONArray) obj).size())
		{
			if(intNumberOfRows != 0)
				return ValidateResults.resultsvalidation(jsonList, rs);
			else
			{
				System.out.println("The JSON List size is :" + ((JSONArray) obj).size() + " and the SQL Result Set Row Count is : " + intNumberOfRows + "\n");
				System.out.println("We don't require to go for a comparison");
				Log.info("The JSON List size is :" + ((JSONArray) obj).size() + " and the SQL Result Set Row Count is : " + intNumberOfRows);
				Log.info("We don't require to go for a comparison");
				return true;
			}
		}
		else
		{
			System.out.println("The JSON List returned does not match with the SQL Result Set. Please Review the API Call");
			System.out.println("The JSON List size is :" + ((JSONArray) obj).size() + " and the SQL Result Set Row Count is : " + intNumberOfRows);
			Log.info("The JSON List returned does not match with the SQL Result Set. Please Review the API Call");
			Log.info("The JSON List size is :" + ((JSONArray) obj).size() + " and the SQL Result Set Row Count is : " + intNumberOfRows);
			return false;
		}
	}
}
