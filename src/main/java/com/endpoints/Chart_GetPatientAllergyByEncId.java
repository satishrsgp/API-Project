package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetPatientAllergyByEncId 
{

	public static Boolean chart_GetPatientAllergyByEncId_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "SELECT top 1 pa.uniq_id as id,pa.enterprise_id as enterpriseId, pa.practice_id as practiceId, pa.person_id as personId"
						+ ", pa.enc_id as encounterId,  pe.enc_timestamp_tz as encounterTimestampTimezone, "
						+ "pa.allergy_id as allergyId, pa.allergy_type as allergyType,pa.allergy_type_id as allergyTypeId,  pa.location_id as locationId,  "
						+ "lm.location_name as locationName,pa.provider_id as providerId, pm.description as providerName,pa.date_onset as onsetDate, pa.date_resolved as resolvedDate, pa.allergy_comment as comment"
						+ ",pa.rxn_desc as reactionDescription, pa.source_product_id as sourceProductId , pa.audit_id as auditId,pa.causative_agent as causativeAgent, "
						+ " CASE WHEN dbo.fn_is_encounter_Locked (pa.enc_id) = 'Y' THEN 'true' ELSE 'false' END AS lock_ind, "
						+ "pa.severity_code, CASE WHEN pa.intolerance_ind = 'N' THEN 'false' ELSE 'true' END AS isIntolerant, "
						+ "am.description as description, pa.created_by as createdBy, pa.create_timestamp as createTimestamp, pa.create_timestamp_tz as createTimestampTimezone, pa.modified_by as modifiedBy, "
						+ "pa.modify_timestamp as modifyTimestamp, pa.modify_timestamp_tz as modifyTimestampTimezone, CASE WHEN dbo.fn_is_encounter_Locked (pa.enc_id) = 'Y' "
						+ "THEN 'true' ELSE 'false' END AS isReadOnly ,ri.IMK_EXT_VOCAB_ID AS rxNormCode, ri.IMK_EXT_VOCAB_DESC AS rxNormDescription FROM patient_allergy pa RIGHT JOIN patient_encounter pe "
						+ "ON pa.person_id = pe.person_id INNER JOIN allergy_mstr am ON pa.allergy_id = am.allergy_id "
						+ "INNER JOIN provider_mstr pm ON pm.provider_id=pa.provider_id  INNER JOIN location_mstr lm ON pa.location_id = lm.location_id "
						+ "INNER JOIN rimkfae0 AS ri ON pa.allergy_id = ri.imk_fdb_vocab_no_id AND ri.evd_fdb_vocab_type_id = "
						+ "CASE WHEN pa.allergy_type_id = 1 THEN 104 WHEN pa.allergy_type_id = 3 THEN 110 END "
						+ "AND pe.person_id = '"+parameters.get("personId")+"' AND pa.uniq_id = '"
						+parameters.get("allergyId")+"'"  ;
				
				//da.event_text whoReviewed,da.create_timestamp as whenReviewed   --------"whoReviewed": null,"whenReviewed": null,
          
				
				String[] jsonMetaData = {"id","enterpriseId","practiceId","personId","encounterId","encounterTimestampTimezone",
						"allergyId","allergyType","allergyTypeId","locationId","locationName","providerId","providerName","onsetDate;Date_YYYYMMDD HH:MM:SS",
						"resolvedDate","comment","reactionDescription","sourceProductId","auditId","causativeAgent","isLocked","severityCode","isIntolerant",
						"description","createdBy","createTimestamp;Date_YYYYMMDD HH:MM:SS","createTimestampTimezone","modifiedBy","modifyTimestamp;Date_YYYYMMDD HH:MM:SS","modifyTimestampTimezone","isReadOnly","rxNormCode","rxNormDescription"};
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

