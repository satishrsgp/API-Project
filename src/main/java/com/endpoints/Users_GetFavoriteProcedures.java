package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Users_GetFavoriteProcedures 
{

	public static Boolean Users_GetFavoriteProcedures_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "select userId as userId, user_name as userName, phrase_type as type, phrase_summary as summary, "
								+ "	phrase as text, kbm_ind as kbmIndicator from ngkbm_my_phrases_ where userId='"+parameters.get("userId")+"' "
				;
					
				

				
				String[] jsonMetaData = {"userId","userName","type","summary","text","kbmIndicator"};
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..userId", jsonList);
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

		return status;
	}
}
