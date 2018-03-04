package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Audit_GetTemplateDetail 
{

	public static Boolean audit_GetTemplateDetail_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{				
				String strQuery = "SELECT event_id AS id, template_id AS templateId, encounter_id AS encounterId, case_id AS"
						+ " caseId, group_id AS groupId, group_encounter_id AS groupEncounterId, extended_popup_id AS"
						+ " extendedPopupId, sequence_id AS sequenceId, practice_id AS"
						+ " practiceId FROM event_template WHERE event_id = '"+parameters.get("eventId")+"'";
				
				//CONVERT(uniqueidentifier,'0C6A36BA-10E4-438F-BA86-0D5B68A2BB15'
				
				
				//da.event_text whoReviewed,da.create_timestamp as whenReviewed   --------"whoReviewed": null,"whenReviewed": null, parameters.get("personId")

				
				String[] jsonMetaData = {"id","templateId","encounterId","caseId","groupId","groupEncounterId","extendedPopupId","sequenceId","practiceId"};
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGAAudit, strQuery), strResponse, "$.."+strUniqueJsonToken, jsonList);
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
