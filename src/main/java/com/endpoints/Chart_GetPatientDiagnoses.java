package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

/*
 * ********************************************************************************
 * Class Name						: Chart_GetPatientDiagnosis
 * Created By						: Anil Kumar Veluru
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: PersonID
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							: Route will fetch the diagnosis chart details
 ***********************************************************************************
 */

public class Chart_GetPatientDiagnoses 
{
	public static Boolean chart_GetPatientDiagnoses_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				/*String strQuery = "select pd.uniq_id as id,pd. enterprise_id as enterpriseId, pd.practice_id as practiceId, pd.person_id as personId, "
						+ "pd.diagnosis_code_id as diagnosisCodeId, pd.diagnosis_code_lib_id as diagnosisCodeLibId, pd.description as description, pd.date_diagnosed as dateDiagnosed, "
						+ "pd.date_resolved as dateResolved, pd.severity_id as severityId, CASE WHEN pd.chronic_ind = 'Y' THEN 'true' ELSE 'false' END as isChronic"
						+ ", pd.create_timestamp as createTimestamp, "
						+ " pd.create_timestamp_tz as createTimestampTimezone, pd.modify_timestamp as modifyTimeStamp, "
						+ "pd.modify_timestamp_tz as modifyTimestampTimezone, "
						+ "UPPER(pd.icd9cm_code_id) as icd9cmCodeId, dcm.user_description as userDescription"
						+ " FROM patient_diagnosis pd INNER JOIN diagnosis_code_mstr dcm ON pd.diagnosis_code_id = dcm.diagnosis_code_id AND "
						+ "pd.diagnosis_code_lib_id = dcm.diagnosis_code_lib_id WHERE pd.enterprise_id = '" + System.getProperty("LoggedInEnterpriseId") + "' "
						+ "AND pd.practice_id = '" + System.getProperty("LoggedInPracticeId") + "' "
						+ "AND pd.person_id  = '" + parameters.get("personId") + "'";
				
				String[] jsonMetaData = {"id","enterpriseId","practiceId","personId","diagnosisCodeId","diagnosisCodeLibId","description","dateDiagnosed","dateResolved","severityId","isChronic","createTimestamp","createTimestampTimezone","modifyTimestamp","modifyTimestampTimezone","icd9cmCodeId","userDescription"};*/

				String strQuery = "select pd.uniq_id as id, pd.person_id as personId, pd. enterprise_id as enterpriseId, pd.practice_id as practiceId, "
						+ "p.practice_name as practiceName, pd.enc_id as encounterId, pe.enc_timestamp as encounterTimestamp, "
						+ "CASE WHEN pe.lock_ind  = 'Y' THEN 'true' ELSE 'false' END as isEncounterLocked, pd.icd9cm_code_id as icdCode, "
						+ "icd.icd_type as icdCodeSystem, pd.diagnosis_code_lib_id as diagnosisCodeLibId, pd.diagnosis_code_id as diagnosisCodeId, "
						+ "pd.description as description, dcm.user_description  as userDescription, pd.date_onset_sympt as onsetDate, pd.date_diagnosed as diagnosisDate, "
						+ "pd.date_resolved as resolvedDate, pd.location_id as locationId, pd.provider_id as providerId, pd.severity_id as severityId, pd.status_id as statusId, "
						+ "CASE WHEN pd.chronic_ind = 'Y' THEN 'true' ELSE 'false' END as isChronic,  pd.note as note, "
						+ "CASE WHEN pd.recorded_elsewhere_ind = 'Y' THEN 'true' ELSE 'false' END as wasRecordedElsewhere, pd.source_product_id as sourceProductId "
						+ "FROM patient_diagnosis pd INNER JOIN diagnosis_code_mstr dcm ON pd.diagnosis_code_id = dcm.diagnosis_code_id "
						+ "INNER JOIN practice p ON pd.practice_id = p.practice_id "
						+ "INNER JOIN patient_encounter pe ON pd.enc_id = pe.enc_id "
						+ "INNER JOIN icd9cm_code_mstr icd ON pd.diagnosis_code_id= icd.icd9cm_code_id AND pd.diagnosis_code_lib_id = dcm.diagnosis_code_lib_id  "
						+ "WHERE pd.enterprise_id = '" + System.getProperty("LoggedInEnterpriseId") + "' "
						+ "AND pd.practice_id = '" + System.getProperty("LoggedInPracticeId") + "' "
						+ "AND pd.person_id  = '" + parameters.get("personId") + "'";
				
				String[] jsonMetaData = {"id","personId","enterpriseId","practiceId","practiceName","encounterId","encounterTimestamp;Date_YYYYMMDD HH:MM:SS","isEncounterLocked","icdCode","icdCodeSystem",
						"diagnosisCodeLibId","diagnosisCodeId","description","userDescription","onsetDate","diagnosisDate","resolvedDate","locationId","providerId","severityId",
						"statusId","isChronic","note","wasRecordedElsewhere","sourceProductId"};
				
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
