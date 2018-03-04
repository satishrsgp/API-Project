package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetPatientEncDiagnosis 
{

	public static Boolean chart_GetPatientEncDiagnosis_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery ="SELECT pd.uniq_id AS id, pd.person_id AS personId, pd.enterprise_id AS"
                        + " enterpriseId, pd.practice_id AS practiceId, p.practice_name AS practiceName, pd.enc_id AS"
                        + " encounterId, pe.enc_timestamp AS encounterTimestamp, CASE WHEN dbo.fn_is_encounter_locked (pe.enc_id) = 'Y' THEN 'true' ELSE 'false' END AS"
                        + " isEncounterLocked, pd.icd9cm_code_id AS icdCode, icd9.icd_type AS icdCodeSystem, pd.diagnosis_code_lib_id AS"
                        + " diagnosisCodeLibId, pd.diagnosis_code_id AS diagnosisCodeId, pd.description AS description, dcm.user_description AS"
                        + " userDescription, dcm.description AS billingDescription, pd.date_onset_sympt AS onsetDate, pd.date_diagnosed AS"
                        + " diagnosisDate, pd.date_resolved AS resolvedDate, pd.location_id AS location_id, lm.location_name AS"
                        + " locationName, pd.provider_id AS providerId,  CONCAT(pm.first_name,' ',pm.last_name) AS providerName, pd.severity_id AS severityId, dsm.description AS"
                        + " severityDescription, pd.status_id AS statusId, dstatusm.description AS statusDescription, CASE WHEN"
                        + " pd.chronic_ind = 'Y' THEN 'true' ELSE 'false' END AS isChronic, pd.note AS note, CASE WHEN pd.recorded_elsewhere_ind = 'Y' "
                        + "THEN 'true' ELSE 'false' END AS wasRecordedElsewhere, pd.source_product_id AS sourceProductId, icd9.eff_date AS"
                        + " icdEffectiveDate, icd9.exp_date AS icdExpirationDate, pd.snomed_concept_id AS"
                        + " snomedConceptId, pd.snomed_fully_specified_name AS snomedFullySpecifiedName, CASE"
                        + " WHEN (EXISTS (SELECT * FROM lab_nor AS ln LEFT JOIN lab_order_diag AS lod ON ln.order_num = lod.order_num WHERE NOT ln.delete_ind = 'N' "
                        + "AND ln.person_id = pd.person_id AND ln.practice_id = pd.practice_id AND lod.diagnosis_code_id = pd.diagnosis_code_id)) THEN"
                        + " 'true' ELSE 'false' END AS hasOrders, ehm.hcc_id AS hccId, ehm.hcc_description AS"
                        + " hccDescription, ehm.comm_coefficient AS hccCommunityCoefficient, ehm.inst_coefficient AS"
                        + " hccInstitutionalCoefficient, erm.rxhcc_id AS rxHccId, erm.rxhcc_description AS"
                        + " rxHccDescription, erm.inst_coefficient AS rxHccInstitutionalCoefficient, erm.LowIncome_under65 AS"
                        + " rxHccCoefficientLowIncomeUnder65, erm.LowIncome_over65 AS rxHccCoefficientLowIncomeOver65, erm.under65 AS"
                        + " rxHccCoefficientNonLowIncomeUnder65, erm.over65 AS rxHccCoefficientNonLowIncomeOver65, pd.created_by AS"
                        + " createdById, um1.login_id AS createdByLoginId, pd.create_timestamp AS createTimestamp, pd.create_timestamp_tz AS"
                        + " createTimestampTimezone, pd.modified_by AS modifiedById, um2.login_id AS modifiedByLoginId, pd.modify_timestamp AS"
                        + " modifyTimestamp, pd.modify_timestamp_tz AS modifyTimestampTimezone FROM patient_diagnosis AS pd LEFT JOIN practice AS"
                        + " p ON pd.practice_id = p.practice_id LEFT JOIN patient_encounter AS"
                        + " pe ON pd.enc_id = pe.enc_id LEFT JOIN icd9cm_code_mstr AS icd9 ON pd.icd9cm_code_id = icd9.icd9cm_code_id "
                        + "LEFT JOIN diagnosis_code_mstr AS dcm ON (pd.diagnosis_code_id = dcm.diagnosis_code_id AND pd.diagnosis_code_lib_id = dcm.diagnosis_code_lib_id) "
                        + "LEFT JOIN location_mstr AS lm ON pd.location_id = lm.location_id LEFT JOIN provider_mstr AS pm ON pd.provider_id = pm.provider_id "
                        + "LEFT JOIN diagnosis_severity_mstr AS dsm ON pd.severity_id = dsm.severity_id LEFT JOIN diagnosis_status_mstr AS dstatusm ON pd.status_id = dstatusm.status_id "
                        + "LEFT JOIN user_mstr AS um1 ON pd.created_by = um1.user_id LEFT JOIN user_mstr AS um2 ON pd.modified_by = um2.user_id LEFT JOIN extract_icd_hcc_xref AS eihx ON pd.nghcc_id = eihx.nghcc_id "
                        + "LEFT JOIN extract_hcc_mstr AS ehm ON eihx.hcc_id = ehm.hcc_id AND eihx.effdate = ehm.effdate AND eihx.expdate = ehm.expdate "
                        + "LEFT JOIN extract_icd_rxhcc_xref AS eirx ON pd.ngrxhcc_id = eirx.ngrxhcc_id "
                        + "LEFT JOIN extract_rxhcc_mstr AS erm ON eirx.rxhcc_id = erm.rxhcc_id AND eirx.effdate = erm.effdate AND eirx.expdate = erm.expdate "
                        + "WHERE pd.person_id = '"+parameters.get("personId")+"' AND pd.enc_id = '"+parameters.get("encounterId")+"' AND pd.uniq_id = '"+parameters.get("diagnosisId")+"'";
				
				//da.event_text whoReviewed,da.create_timestamp as whenReviewed   --------"whoReviewed": null,"whenReviewed": null,

				
				String[] jsonMetaData = {"id","personId","enterpriseId","practiceId","practiceName","encounterId",
						"encounterTimestamp;Date_YYYYMMDD HH:MM:SS","isEncounterLocked","icdCode","icdCodeSystem","diagnosisCodeLibId","diagnosisCodeId",
						"description","userDescription","billingDescription","onsetDate","diagnosisDate","resolvedDate","locationId","locationName",
						"providerId","providerName","severityId","severityDescription","statusId","statusDescription","isChronic","note","wasRecordedElsewhere","sourceProductId",
						"icdEffectiveDate","icdExpirationDate","snomedConceptId","snomedFullySpecifiedName","hasOrders","hccId",
						"hccDescription","hccCommunityCoefficient","hccInstitutionalCoefficient","rxHccId","rxHccDescription",
						"rxHccInstitutionalCoefficient","rxHccCoefficientLowIncomeUnder65","rxHccCoefficientLowIncomeOver65","rxHccCoefficientNonLowIncomeUnder65",
						"rxHccCoefficientNonLowIncomeOver65","createdById","createdByLoginId","createTimestamp;Date_YYYYMMDD HH:MM:SS","createTimestampTimezone",
						"modifiedById","modifiedByLoginId","modifyTimestamp;Date_YYYYMMDD HH:MM:SS","modifyTimestampTimezone"};
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
