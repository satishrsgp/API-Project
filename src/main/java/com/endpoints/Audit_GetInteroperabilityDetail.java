package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Audit_GetInteroperabilityDetail 
{

	public static Boolean audit_GetInteroperabilityDetail_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{//
			try
			{				
				String strQuery = "SELECT event_id AS id,cqm_provider_id AS cqpmProviderId,external_system_id AS"
						+ " externalSystemId,external_enterprise_id AS externalEnterpriseId,external_practice_id AS"
						+ " externalPracticeId,external_user_id AS externalUserId,external_patient_id AS externalPatientId,message AS"
						+ " message FROM event_interoperability WHERE event_id='"+parameters.get("eventId")+"'";
				
				
				
				//CONVERT(uniqueidentifier,'0C6A36BA-10E4-438F-BA86-0D5B68A2BB15'
				
				
				//da.event_text whoReviewed,da.create_timestamp as whenReviewed   --------"whoReviewed": null,"whenReviewed": null, parameters.get("personId")

				
				String[] jsonMetaData = {"id","cqpmProviderId","externalSystemId","externalEnterpriseId","externalPracticeId","externalUserId","externalPatientId","message"};
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
