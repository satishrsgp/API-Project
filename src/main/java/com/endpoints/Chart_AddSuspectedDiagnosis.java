package com.endpoints;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_AddSuspectedDiagnosis {
	public static Boolean chart_AddSuspectedDiagnosis_Test(HashMap<String, String> parameters, String strResponse,HashMap<String, Object> mapPostDetails, SoftAssert softAssert, String strQuery)
	{
		Boolean status=false;


		try
		{
			int countAfterPOST = DatabaseConnection.returnNumberOfRows(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery));
			if(Math.abs(countAfterPOST - ((int)mapPostDetails.get("countBeforePOST")))  == 1)
			{
				try
				{
					String strSQLQuery = "select top 1  "
							+"UPPER(CAST(lod.diagnosis_code_lib_id AS CHAR(36))) AS diagnosisCodeLibraryId, "
							+"lod.diagnosis_code_id as diagnosisCodeId "
							+"from lab_order_diag lod inner join lab_nor ln on lod.order_num=ln.order_num "
							+"inner join diagnosis_code_mstr dcm on (lod.diagnosis_code_lib_id=dcm.diagnosis_code_lib_id and lod.diagnosis_code_id=dcm.diagnosis_code_id) "
							+"where ln.person_id='"+parameters.get("personId")+"' and ln.order_num='"+parameters.get("orderId")+"' and lod.order_test_id='"+parameters.get("testId")+"' "
							+"order by lod.create_timestamp desc";
					
					ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strSQLQuery));

					ArrayList<Object> jsonList = new ArrayList<Object>();
					
					//jsonList.add(Objects.equals(parameters.get("RecallPlanId"), "") ? null : (parameters.get("RecallPlanId").toString().toLowerCase()));		
					//jsonList.add(Objects.equals(parameters.get("LastDate"), "") ? null : (parameters.get("LastDate").toString()));	
					//jsonList.add(Objects.equals(parameters.get("ReturnDate"), "") ? null : (parameters.get("ReturnDate").toString()));	
					//jsonList.add(Objects.equals(parameters.get("email"), "") ? null : (parameters.get("email").toString()));	
					jsonList.add(Objects.equals(parameters.get("DiagnosisCodeLibraryId"), "") ? null : (parameters.get("DiagnosisCodeLibraryId").toString()));
					jsonList.add(Objects.equals(parameters.get("DiagnosisCodeId"), null) ? null : (parameters.get("DiagnosisCodeId").toString()));	
					//jsonList.add(parameters.get("diagnosisCodeLibraryId").toString());
					//jsonList.add(Objects.equals(parameters.get("LastDate"), null) ? null : (parameters.get("LastDate").toString().split("T")[0]).replace("-", ""));
					//jsonList.add(Objects.equals(parameters.get("ReturnDate"), null) ? null : (parameters.get("ReturnDate").toString().split("T")[0]).replace("-", ""));
					//jsonList.add(parameters.get("EventId").toString());
					//jsonList.add(parameters.get("ResourceId").toString());
					//jsonList.add(parameters.get("LocationId").toString());
					//jsonList.add(parameters.get("diagnosisCodeId").toString());
				
					//jsonList.add(Objects.equals(parameters.get("fax"), "") ? null : (parameters.get("fax").toString().toUpperCase()));	
				//	jsonList.add(Objects.equals(parameters.get("homePhone"), "") ? null : (parameters.get("homePhone").toString().toUpperCase()));	
				
					
					
					//jsonList.add(parameters.get("zip").toString());
					//jsonList.add(Objects.equals(parameters.get("VerbalOrderIndicator"), "false") ? "N" : "Y");
					//jsonList.add(parameters.get("AllergiesReviewed").toString());
					//if (get.(AllergiesReviewed) =="egg")
			


					//status = ValidateResults.resultsvalidation(jsonList, DatabaseConnection.statementExecution(BaseTest.connNGA, strSQLQuery));
					status = ValidateResults.resultValidation(jsonList, listDatabase);
				}
				catch(Exception e)
				{
					e.printStackTrace();
					Log.info("Error in Test Class and the detail are : \n" + e.getMessage());
					softAssert.fail("Error in Test Script please look at logs");
				}
				/*String strQuery1 = "select "
                                                                                    + "enterprise_id as enterpriseId,practice_id as practiceId,person_id as personId,case_id as caseId,casemgt_case_id as caseManagementCaseId, "
                                                                                    + "parent_enc_id as parentEncounterId,group_enc_id as groupEncounterId,enc_timestamp as timestamp,Lock_ind as isLocked, "
                                                                                    + "enc_timestamp_tz as timestampTimezone,rendering_provider_id as renderingProviderId,locked_by as lockedBy,lock_timestamp as lockTimestamp, "
                                                                                    + "lock_timestamp_tz as lockTimestampTimezone,billable_timestamp as billableTimestamp,billable_timestamp_tz as billableTimestampTimezone, "
                                                                                    + "clinical_ind as isClinical,billable_ind as isBillable,optical_ind as isOptical,location_id as locationId,remarks as remarks, "
                                                                                    + "enc_status as status,source_product_id as sourceProductId,site_id as siteId,guar_id as guarantorId,guar_type as guarantorType, "
                                                                                    + "cob1_payer_id as cob1PayerId,cob1_insured_person_id as cob1InsuredPersonId,cob1_person_payer_id as cob1PersonPayerId,onset_date as onsetDate, "
                                                                                    + "onset_time as onsetTime,admit_date as admitDate,discharge_date as dischargeDate,refer_provider_id as referringProviderId,refer_provider_name as referringProviderName "
                                                                                    + "From Patient_encounter "
                                                                                    + "where person_id='"+parameters.get("personId")+"' order by create_timestamp desc";
                                                    ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
                                                    ArrayList<Object> listTestData  = new ArrayList<>();
                                                    for(Map.Entry<String, Object> entry1 : mapPostDetails.entrySet())
                                                    {
                                                                    for(Map.Entry<String, String> entry2 : parameters.entrySet())
                                                                    {
                                                                                    if(Objects.equals(entry2.getKey(), entry1.getKey()))
                                                                                                    listTestData.add(entry1.getValue());
                                                                    }              
                                                    }
                                                    status = ValidateResults.resultValidation(listTestData, listDatabase);
				 */          

			}
			else
			{
				Log.info("The Difference between records count before and after post is more than one, please reexcute once more");
				softAssert.fail("The Difference between records count before and after post is more than one. Please reexecute once more");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}

}
