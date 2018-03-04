package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Provider_MatchMedicationRefills 
{

	public static Boolean provider_MatchMedicationRefills_Test(HashMap<String, String> parameters, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strSQLQuery = "select datediff(second,create_timestamp,modify_timestamp) as diff,modified_by as mb  "
					+ "from rx_refill_request "
					+ "WHERE request_id = '"+parameters.get("requestId")+"'" ;
					
			
			Object objIntDateDiff = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "diff");

			Object objIntModifiedBy = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "mb");
			if(Integer.valueOf(objIntDateDiff.toString()) > 0)
			{
				if(Objects.equals(objIntModifiedBy.toString(), System.getProperty("LoggedInUserId")))
				{
					String strQuery1 = 
							"select request_id as requestId,matched_person_id as MatchedPersonId from  rx_refill_request WHERE request_id = '"+parameters.get("requestId")+"'" ;
					
					
					
					ArrayList<Object> listTestData  = new ArrayList<>();
					listTestData.add(Objects.equals(parameters.get("requestId"), "") ? null: parameters.get("requestId").toString().toUpperCase());
					listTestData.add(Objects.equals(parameters.get("MatchedPersonId"), "") ? null: parameters.get("MatchedPersonId").toString().toUpperCase());
					

					
					
					
				    Log.info("Parameters:"+parameters);
					Log.info("TestData:\n"+listTestData);
					status = ValidateResults.resultsvalidation(listTestData, DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
				}
				else
				{
					Log.error("Person with Person_id="+parameters.get("personId")+", was not updated correctly-Elements are not updated with logged in user's credentials..");
					Log.error("Expected:"+System.getProperty("LoggedInUserId")+", Actual:"+objIntModifiedBy.toString());
					softAssert.fail("Person with Person_id="+parameters.get("personId")+", Elements are not updated with logged in user's credentials..");
				}
			}
			else{
				Log.error("Person with Person_id="+parameters.get("personId")+", modify_timestamp is not updated with latest timesatmp..");
				softAssert.fail("Request with request_id="+parameters.get("requestId")+", modify_timestamp is not updated with latest timesatmp..");

			}
		

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}



}
