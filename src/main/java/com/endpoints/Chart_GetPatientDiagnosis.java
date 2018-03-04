package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetPatientDiagnosis 
{
	public static Boolean chart_GetPatientDiagnosis_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				/*String strQuery = "select pd.uniq_id as id,  pd.diagnosis_code_id as diagnosisCodeId, pd.diagnosis_code_lib_id as diagnosisCodeLibId, "
						+ "pd.description as description,  pd.create_timestamp as createTimestamp,  pd.create_timestamp_tz  as createTimestampTimezone, "
						+ "CASE WHEN pd.chronic_ind = 'Y' THEN 'true' ELSE 'false' END as isChronic, pd. enterprise_id as enterpriseId, pd.practice_id as practiceId, "
						+ "pd.person_id as personId, pd.provider_id as providerId, pd.location_id, pd.date_resolved as dateResolved, pd.date_onset_sympt as dateOnsetSymptom, "
						+ "pd.date_diagnosed as dateDiagnosed, pd.severity_id as severityId, dsvm.description as severity,  pd.note, pd.modify_timestamp as modifyTimeStamp, "
						+ "pd.modify_timestamp_tz as modifyTimestampTimezone, UPPER(pd.icd9cm_code_id) as icd9cmCodeId, pd.status_id as statusId, "
						+ "dcm.user_description as userDescription,icd.eff_date as effectiveDate, icd.exp_date as expirationDate "
						+ "FROM patient_diagnosis pd INNER JOIN diagnosis_code_mstr dcm ON pd.diagnosis_code_id = dcm.diagnosis_code_id "
						+ "INNER JOIN diagnosis_status_mstr dsm ON pd.status_id = dsm.status_id "
						+ "INNER JOIN diagnosis_severity_mstr dsvm ON pd.severity_id = dsvm.severity_id "
						+ "INNER JOIN icd9cm_code_mstr icd ON pd.diagnosis_code_id= icd.icd9cm_code_id AND pd.diagnosis_code_lib_id = dcm.diagnosis_code_lib_id "
						+ "WHERE pd.enterprise_id = '" + System.getProperty("LoggedInEnterpriseId") + "' "
						+ "AND pd.practice_id = '" + System.getProperty("LoggedInPracticeId") + "' "
						+ "AND pd.person_id  = '" + parameters.get("personId") + "'" 
						+ "AND pd.uniq_id = '" + parameters.get("uniqueId") + "'";*/
				
				String strQuery = "select pd.uniq_id as id,  pd.person_id as personId, pd. enterprise_id as enterpriseId, pd.practice_id as practiceId, "
						+ "p.practice_name as practiceName, pd.enc_id as encounterId, pe.enc_timestamp as encounterTimestamp, "
						+ "CASE WHEN pe.lock_ind  = 'Y' THEN 'true' ELSE 'false' END as isEncounterLocked, pd.icd9cm_code_id as icdCode, icd.icd_type as icdCodeSystem, "
						+ "pd.diagnosis_code_lib_id as diagnosisCodeLibId, pd.diagnosis_code_id as diagnosisCodeId, pd.description as description, "
						+ "dcm.user_description  as userDescription, pd.date_onset_sympt as onsetDate, pd.date_diagnosed as diagnosisDate, pd.date_resolved as resolvedDate, "
						+ "pd.location_id as locationId, lm.location_name as locationName, pd.provider_id as providerId, pm.first_name + ' ' +  pm.last_name as providerName, "
						+ "pd.severity_id as severityId, dsvm.description as severityDescription, pd.status_id as statusId, dsm.description as statusDescription,  "
						+ "CASE WHEN pd.chronic_ind = 'Y' THEN 'true' ELSE 'false' END as isChronic, pd.note as note, "
						+ "CASE WHEN pd.recorded_elsewhere_ind = 'Y' THEN 'true' ELSE 'false' END as wasRecordedElsewhere, pd.source_product_id as sourceProductId, "
						+ "icd.eff_date as icdEffectiveDate, icd.exp_date as icdExpirationDate, pd.snomed_concept_id as snomedConceptId, "
						+ "pd.snomed_fully_specified_name as snomedFullySpecifiedName, pd.created_by as createdById, um.login_id as createdByLoginId, "
						+ "pd.create_timestamp as createTimestamp,  pd.create_timestamp_tz  as createTimestampTimezone, pd.modified_by as modifiedById, "
						+ "um.login_id as modifiedByLoginId, pd.modify_timestamp as modifyTimeStamp, pd.modify_timestamp_tz as modifyTimestampTimezone "
						+ "FROM patient_diagnosis pd INNER JOIN diagnosis_code_mstr dcm ON pd.diagnosis_code_id = dcm.diagnosis_code_id "
						+ "INNER JOIN practice p ON pd.practice_id = p.practice_id "
						+ "INNER JOIN patient_encounter pe ON pd.enc_id = pe.enc_id "
						+ "INNER JOIN location_mstr lm ON pd.location_id = lm.location_id "
						+ "INNER JOIN provider_mstr pm on pd.provider_id = pm.provider_id "
						+ "INNER JOIN diagnosis_status_mstr dsm ON pd.status_id = dsm.status_id "
						+ "INNER JOIN diagnosis_severity_mstr dsvm ON pd.severity_id = dsvm.severity_id "
						+ "INNER JOIN icd9cm_code_mstr icd ON pd.diagnosis_code_id= icd.icd9cm_code_id AND pd.diagnosis_code_lib_id = dcm.diagnosis_code_lib_id  "
						+ "INNER JOIN user_mstr um ON pd.created_by = um.user_id  "
						+ "WHERE pd.enterprise_id = '" + System.getProperty("LoggedInEnterpriseId") + "' "
						+ "AND pd.practice_id = '" + System.getProperty("LoggedInPracticeId") + "' "
						+ "AND pd.person_id  = '" + parameters.get("personId") + "' " 
						+ "AND pd.uniq_id = '" + parameters.get("uniqueId") + "'";

				//String[] jsonMetaData = {"id", "diagnosisCodeId", "diagnosisCodeLibId", "description", "createTimestamp;Date_YYYYMMDD HH:MM:SS", "createTimestampTimezone", "isChronic", "enterpriseId", "practiceId", "personId", "providerId", "locationId", "dateResolved", "dateOnsetSymptom", "dateDiagnosed", "severityId", "severity", "note", "modifyTimestamp", "modifyTimestampTimezone",  "icd9cmCodeId", "statusId", "userDescription", "effectiveDate", "expirationDate"};
				String[] jsonMetaData = {"id","personId","enterpriseId","practiceId","practiceName","encounterId","encounterTimestamp;Date_YYYYMMDD HH:MM:SS","isEncounterLocked","icdCode","icdCodeSystem",
						"diagnosisCodeLibId","diagnosisCodeId","description","userDescription","onsetDate","diagnosisDate","resolvedDate","locationId","locationName","providerId",
						"providerName","severityId","severityDescription","statusId","statusDescription","isChronic","note","wasRecordedElsewhere","sourceProductId","icdEffectiveDate",
						"icdExpirationDate","snomedConceptId","snomedFullySpecifiedName","createdById","createdByLoginId","createTimestamp;Date_YYYYMMDD HH:MM:SS","createTimestampTimezone","modifiedById",
						"modifiedByLoginId","modifyTimestamp;Date_YYYYMMDD HH:MM:SS","modifyTimestampTimezone"};
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
