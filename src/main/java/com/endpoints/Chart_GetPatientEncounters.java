package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetPatientEncounters 
{
	public static Boolean chart_GetPatientEncounters_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				
				String strQuery = "select pe.enc_id as id, pe.person_id as personId, pe.enc_timestamp as timestamp, "
						+ "pe.enc_timestamp_tz as enc_timestamp_tz, pe.location_id as locationId, CASE "
						+ "WHEN dbo.fn_is_encounter_Locked (pe.enc_id) = 'Y' THEN 'true' ELSE 'false' END AS isLocked, "
						+ "pe.rendering_provider_id as renderingProviderId, pm.description as renderingProviderName "
						+ "from patient_encounter pe inner join provider_mstr pm on pe.rendering_provider_id = pm.provider_id "
						+ "where pe.person_id = '"+parameters.get("personId")+"'";					
				
				String[] jsonMetaData = {"id","personId","timestamp;Date_YYYYMMDD HH:MM:SS","timestampTimezone","locationId","isLocked","renderingProviderId","renderingProviderName"};
				
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..id", jsonList);
					//objHandler = FinalAssertions.assertResponseCount(jsonMetaData, objHandler, DatabaseConnection.statementExecution(connNGA, strQuery), "id");
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
