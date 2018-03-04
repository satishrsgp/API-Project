package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_UpdateDiagnosis 
{

	public static Boolean chart_UpdateDiagnosis_Test(HashMap<String, String> parameters, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strSQLQuery = "select datediff(second,create_timestamp,modify_timestamp) as diff,modified_by as mb  "
					+ "from patient_diagnosis "
					+ "WHERE person_id = '"+parameters.get("personId")+"' AND "
					+ "enc_id = '"+parameters.get("encounterId")+"'";
					
			Object objIntModifiedBy = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "mb");
		
				if(Objects.equals(objIntModifiedBy.toString(), System.getProperty("LoggedInUserId")))
				{
					String strQuery1 = 
							" SELECT pd.date_onset_sympt AS onsetDate, pd.date_diagnosed AS diagnosisDate, "
							+ "pd.date_resolved AS resolvedDate,pd.severity_id, CASE WHEN pd.recorded_elsewhere_ind = 'Y' THEN 'true' ELSE 'false' END AS wasRecordedElsewhere,"
							+ "pd.source_product_id AS sourceProductId ,pd.status_id AS statusId,  pd.provider_id AS providerId,pd.location_id AS locationId, "
							+ "pd.note AS note  "
							+ " FROM patient_diagnosis pd INNER JOIN diagnosis_code_mstr dcm ON "
							+ "pd.diagnosis_code_id = dcm.diagnosis_code_id INNER JOIN practice p ON pd.practice_id = p.practice_id "
							+ "INNER JOIN patient_encounter pe ON pd.enc_id = pe.enc_id INNER JOIN icd9cm_code_mstr icd ON pd.diagnosis_code_id = icd.icd9cm_code_id "
							+ "AND pd.diagnosis_code_lib_id = dcm.diagnosis_code_lib_id WHERE pd.enterprise_id = '"+System.getProperty("LoggedInEnterpriseId")+"' AND pd.practice_id = '"+System.getProperty("LoggedInPracticeId")+"' "
							+ "AND pd.person_id = '"+parameters.get("personId")+"' and pd.severity_id='"+parameters.get("SeverityId")+"'"
						;
					
					
					
					ArrayList<Object> listTestData  = new ArrayList<>();
					listTestData.add(Objects.equals(parameters.get("OnsetDate"), "") ? null: parameters.get("OnsetDate").toString().substring(0, 10).replace("T", "").replace("-", ""));
					listTestData.add(Objects.equals(parameters.get("DiagnosisDate"), "") ? null: parameters.get("DiagnosisDate").substring(0, 10).replace("T", "").replace("-", ""));
					listTestData.add(Objects.equals(parameters.get("ResolvedDate"), "") ? null: parameters.get("ResolvedDate").substring(0, 10).replace("T", "").replace("-", ""));
					listTestData.add(Objects.equals(parameters.get("SeverityId"), "") ? null: parameters.get("SeverityId").toString().toUpperCase());
					listTestData.add(Objects.equals(parameters.get("WasRecordedElsewhere"), "") ? null: parameters.get("WasRecordedElsewhere").toString().toLowerCase());
					listTestData.add(Objects.equals(parameters.get("SourceProductId"), "") ? null: parameters.get("SourceProductId").toString().toUpperCase());
					listTestData.add(Objects.equals(parameters.get("StatusId"), "") ? null: parameters.get("StatusId").toString().toUpperCase());
					listTestData.add(Objects.equals(parameters.get("ProviderId"), "") ? null: parameters.get("ProviderId").toString().toUpperCase());
					listTestData.add(Objects.equals(parameters.get("LocationId"), "") ? null: parameters.get("LocationId").toString().toUpperCase());
					listTestData.add(Objects.equals(parameters.get("Note"), "") ? null: parameters.get("Note"));

					
					
					
				    Log.info("Parameters:"+parameters);
					Log.info("TestData:\n"+listTestData);
					status = ValidateResults.resultsvalidation(listTestData, DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
				}
				else
				{
					Log.error("Person with Person_id="+parameters.get("personId")+", was not updated correctly-modified_by is not updated with logged in user's credentials.");
					Log.error("Expected:"+System.getProperty("LoggedInUserId")+", Actual:"+objIntModifiedBy.toString());
					softAssert.fail("Person with Person_id="+parameters.get("personId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				}
		

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}



}
