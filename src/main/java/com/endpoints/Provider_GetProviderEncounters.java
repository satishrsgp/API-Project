/**
 * API 1.x route_Abhishek
 */

package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Provider_GetProviderEncounters {

	public static Boolean provider_GetProviderEncounters(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery="SELECT top 25 pe.enc_id, p.practice_id,pe.enc_timestamp,p.person_id, p.last_name, p.first_name, p.middle_name, p.date_of_birth,p.sex,pe.rendering_provider_id " 
						+ "FROM patient_encounter AS pe "
						+ "INNER JOIN person AS p ON pe.person_id=p.person_id "
						+ "WHERE(pe.rendering_provider_id='"+parameters.get("providerId")+"') order by pe.enc_id asc";

				String[] jsonMetaData = {"id","practiceId","timestamp;Date_YYYYMMDD HH:MM:SS","personId","patientLastName","patientFirstName","patientMiddleName","patientDateOfBirth","patientSex","providerId"};

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
