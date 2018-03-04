package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.JsonPath;
import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

import net.minidev.json.JSONArray;

public class Master_GetCodeTypes 
{

	public static Boolean master_GetCodeTypes_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{//
			try
			{				
				String strQuery = "select distinct code_type from code_tables order by code_type";
				try
				{	
					Object obj = JsonPath.read(strResponse, "$");
					ArrayList<Object> jsonList = new ArrayList<>();       
					if (obj instanceof JSONArray)
					{
						for(int j = 0; j< ((JSONArray) obj).size(); j++)
						{
							jsonList.add(JsonPath.read(strResponse,"$.["+j+"]"));
						}
					}
					Log.info("JSON LIST is :\n" + jsonList);
					status = ValidateResults.resultsvalidation(jsonList, DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery));
				} 
				catch (Exception e)
				{
					e.printStackTrace();
					Log.info("Error in Test Class and the detail are : \n" + e.getMessage());
					softAssert.fail("Error in Test Script please look at logs");
					//objHandler.setStrRequestStatus("Fail");
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			status = true;
		}
		return status;
	}
}
