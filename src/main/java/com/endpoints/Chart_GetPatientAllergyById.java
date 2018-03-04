package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetPatientAllergyById
{
	public static Boolean chart_GetPatientAllergyById_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "SELECT top 1 pa.uniq_id as id, pa.enterprise_id as enterpriseId, pa.practice_id as practiceId, "
						+ "pa.person_id as personId, pa.enc_id as encounterId, pe.enc_timestamp as encounterTimestamp, "
						+ "pe.enc_timestamp_tz as encounterTimestampTimezone, pa.allergy_id as allergyId, pa.allergy_type_id as allergyTypeId, "
						+ "am.description as description, pa.allergy_type as allergyType, pa.location_id as locationId, "
						+ "pa.provider_id as providerId, pa.date_onset as onsetDate, pa.date_resolved as resolvedDate, "
						+ "pa.allergy_comment as comment, pa.rxn_desc as reactionDescription, CASE WHEN dbo.fn_is_encounter_Locked (pa.enc_id) = 'Y' "
						+ "THEN 'true' ELSE 'false' END AS isLocked, CASE WHEN dbo.fn_is_encounter_Locked (pa.enc_id) = 'Y' THEN 'true' ELSE 'false' E"
						+ "ND AS isReadOnly FROM patient_allergy pa LEFT JOIN patient_encounter pe ON pa.enc_id = pe.enc_id "
						+ "LEFT JOIN allergy_mstr am ON pa.allergy_id = am.allergy_id AND pa.allergy_type_id = am.allergy_type_id "
						+ " INNER JOIN rimkfae0 AS ri ON pa.allergy_id = ri.imk_fdb_vocab_no_id AND ri.evd_fdb_vocab_type_id = CASE WHEN "
						+ "pa.allergy_type_id = 1 THEN 104 WHEN pa.allergy_type_id = 3  THEN 110 END "
						+ "WHERE pa.person_id = '"+parameters.get("personId")+"'"+ " and pa.uniq_id = '"+parameters.get("allergyId")+"'" ;
				
				String[] jsonMetaData = {"id","enterpriseId","practiceId","personId","encounterId","encounterTimestamp;Date_YYYYMMDD HH:MM:SS","encounterTimestampTimezone","allergyId","allergyTypeId","description","allergyType","locationId",
						"providerId","onsetDate","resolvedDate","comment","reactionDescription","isLocked","isReadOnly","rxNormCode","rxNormDescription"};

				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..id", jsonList);
				} 
				catch (Exception e)
				{
					e.printStackTrace();
					Log.info("Error in Test Class and the detail are : \n" + e.getMessage());
					softAssert.fail("Error in Test Script please look at logs");
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