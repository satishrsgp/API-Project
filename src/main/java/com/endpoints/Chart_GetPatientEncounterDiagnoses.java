package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetPatientEncounterDiagnoses 
{

	public static Boolean chart_GetPatientEncounterDiagnoses_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery ="SELECT pd.uniq_id AS id, pd.person_id AS personId, pd.enterprise_id AS enterpriseId, pd.practice_id AS"
						+ " practiceId, p.practice_name AS practiceName, pd.enc_id AS encounterId, pe.enc_timestamp AS"
						+ " encounterTimestamp, CASE WHEN pe.lock_ind = 'Y' THEN 'true' ELSE 'false' END AS"
						+ " isEncounterLocked, pd.icd9cm_code_id AS icdCode, icd9.icd_type AS"
						+ " icdCodeSystem, pd.diagnosis_code_lib_id AS diagnosisCodeLibId, pd.diagnosis_code_id AS"
						+ " diagnosisCodeId, pd.description AS description, dcm.user_description AS"
						+ " userDescription,CASE WHEN (dcm.description IS NULL) THEN icd9.description ELSE dcm.description END AS billingDescription, pd.date_onset_sympt AS"
						+ " onsetDate, pd.date_diagnosed AS diagnosisDate, pd.date_resolved AS resolvedDate, pd.location_id AS"
						+ " location_id, pd.provider_id AS providerId, pd.severity_id AS severityId, pd.status_id AS"
						+ " statusId, CASE WHEN pd.chronic_ind = 'Y' THEN 'true' ELSE 'false' END AS isChronic, pd.note AS"
						+ " note, CASE WHEN pd.recorded_elsewhere_ind = 'Y' THEN 'true' ELSE 'false' END AS"
						+ " wasRecordedElsewhere, pd.source_product_id AS sourceProductId FROM patient_diagnosis AS pd "
						+ "LEFT JOIN practice AS p ON pd.practice_id = p.practice_id "
						+ "LEFT JOIN patient_encounter AS pe ON pd.enc_id = pe.enc_id "
						+ "LEFT JOIN icd9cm_code_mstr AS icd9 ON pd.icd9cm_code_id = icd9.icd9cm_code_id "
						+ "LEFT JOIN diagnosis_code_mstr AS dcm ON (pd.diagnosis_code_id = dcm.diagnosis_code_id AND pd.diagnosis_code_lib_id = dcm.diagnosis_code_lib_id) "
						+ "WHERE pd.person_id = '"+parameters.get("personId")+"' AND pd.enc_id = '"+parameters.get("encounterId")+"'";
				
				//da.event_text whoReviewed,da.create_timestamp as whenReviewed   --------"whoReviewed": null,"whenReviewed": null,

				
				String[] jsonMetaData = {"id","personId","enterpriseId","practiceId","practiceName","encounterId",
						"encounterTimestamp;Date_YYYYMMDD HH:MM:SS","isEncounterLocked","icdCode","icdCodeSystem","diagnosisCodeLibId","diagnosisCodeId",
						"description","userDescription","billingDescription","onsetDate","diagnosisDate","resolvedDate","locationId",
						"providerId","severityId","statusId","isChronic","note","wasRecordedElsewhere","sourceProductId"};
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