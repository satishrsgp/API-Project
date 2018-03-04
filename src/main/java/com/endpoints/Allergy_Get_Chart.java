package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Allergy_Get_Chart
{

	/*
	 * ********************************************************************************
	 * Class Name						: Allergy_Get_Chart
	 * Created By						: 
	 * Created Date						: 
	 * Modified Date					:
	 * Modified By						:
	 * Parameters						: 
	 * Test Case ID						:
	 * Test Case Author					: 
	 * Test Case Modified By			:
	 * Test Case Reviewed By			:
	 * Purpose							: Route will fetch the allergy chart details
	 ***********************************************************************************
	 */

	public static Boolean Allergy_Get_Chart_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		//System.out.println(strResponse);
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "SELECT pa.uniq_id,pa.enterprise_id,pa.practice_id, "
						+"pa.person_id, "
						+"pa.enc_id, "
						+"pe.enc_timestamp, "
						+"pe.enc_timestamp_tz, "
						+"pa.allergy_id, "
						+"pa.allergy_type_id, "
						+"am.description, "
						+"pa.allergy_type, "
						+"pa.location_id, "
						+"pa.provider_id, "
						+"pa.date_onset, "
						+"pa.date_resolved, "
						+"pa.allergy_comment, "
						+"pa.rxn_desc, "
						+ " case  when dbo.fn_is_encounter_Locked(pa.enc_id) = 'Y' then 'true' else 'false'  end as lock_ind,"
						+ " case when dbo.fn_is_encounter_Locked(pa.enc_id) = 'Y' then 'true' else 'false' end as isReadOnly "      
						+"FROM patient_allergy pa left JOIN patient_encounter pe ON pa.enc_id = pe.enc_id  "
						+ "left JOIN allergy_mstr am ON pa.allergy_id = am.allergy_id and pa.allergy_type_id = am.allergy_type_id "
						+ "WHERE pa.person_id = '"+parameters.get("personId")+"' ORDER BY pa.rxn_desc DESC";
				//
				String[] jsonMetaData = {"id","enterpriseId","practiceId","personId","encounterId","encounterTimestamp","encounterTimestampTimezone","allergyId","allergyTypeId","description","allergyType","locationId","providerId","onsetDate","resolvedDate","comment","reactionDescription","isLocked","isReadOnly"};

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
