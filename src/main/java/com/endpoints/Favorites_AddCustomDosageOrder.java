package com.endpoints;



import java.util.ArrayList;
import java.util.HashMap;

import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Favorites_AddCustomDosageOrder {
	public static Boolean favorites_AddCustomDosageOrder_Test(HashMap<String, String> parameters, String strResponse,HashMap<String, Object> mapPostDetails, SoftAssert softAssert, String strQuery)
	{
		Boolean status=false;


		try
		{
			int countAfterPOST = DatabaseConnection.returnNumberOfRows(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery));
			if(Math.abs(countAfterPOST - ((int)mapPostDetails.get("countBeforePOST")))  == 1)
			{
				try
				{
					String strSQLQuery = " SELECT top 1 "
			
							+  " additional_text as AdditionalText,special_instruction as SpecialInstruction,admin_direction as AdminDirection,admin_direction2 as AdminDirection2,route_description_code as RouteDescriptionCode, "
						
							+ " route as Route,dosage_form_amount_unit_low as DosageFormAmountUnitLow,dosage_form_amount_unit_high as DosageFormAmountUnitHigh, "
							+ " dosage_form_amount_low as DosageFormAmountLow,dosage_form_amount_high as DosageFormAmountHigh,dosage_unit_low as DosageUnitLow,dosage_unit_high as DosageUnitHigh, "
							+ " dosage_low as dosageLow,dosage_high as dosageHigh, "
							+ " frequency_low as frequencyLow,frequency_high as frequencyHigh,frequency_low2 as frequencyLow2,frequency_high2 as frequencyHigh2,interval_unit_low as intervalUnitLow,interval_unit_high as intervalUnitHigh, "
							+ " interval_unit_low2 as intervalUnitLow2,interval_unit_high2 as intervalUnitHigh2,interval_low as intervalLow, "
							+ " interval_high as intervalHigh,interval_low2 as intervalLow2,interval_high2 as intervalHigh2,duration_unit_low as durationUnitLow,duration_unit_high as durationUnitHigh, "
							+ " duration_low as durationLow,duration_high as durationHigh  "
							
							+ " from fdb_med_dosage_order_custom  "
							+ "  WHERE gcn_seqno = '"+parameters.get("gcnSeqNo")+"' order by create_timestamp desc" ;
					
					//ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strSQLQuery));

					ArrayList<Object> jsonList = new ArrayList<Object>();
					
					//jsonList.add(Objects.equals(parameters.get("ServiceItemGroupName"), null) ? null : (parameters.get("ServiceItemGroupName").toString()));		
				//	jsonList.add(parameters.get("serviceItemGroupName").toString());
					//jsonList.add(Objects.equals(parameters.get("ServiceItemGroupSequenceNumber"), "") ? null : (parameters.get("ServiceItemGroupSequenceNumber").toString()));
				//jsonList.add(parameters.get("ServiceItemGroupSequenceNumber"), null) ? null : (parameters.get("ServiceItemGroupSequenceNumber")));
					jsonList.add(Objects.equals(parameters.get("AdditionalText"), null) ? null : (parameters.get("AdditionalText").toString()));	
					jsonList.add(Objects.equals(parameters.get("SpecialInstruction"), null) ? null : (parameters.get("SpecialInstruction").toString()));
					jsonList.add(Objects.equals(parameters.get("AdminDirection"), "null") ? "" : (parameters.get("AdminDirection").toString()));	
					jsonList.add(Objects.equals(parameters.get("AdminDirection2"), "null") ? "" : (parameters.get("AdminDirection2").toString()));
					jsonList.add(parameters.get("RouteDescriptionCode"));
					jsonList.add(Objects.equals(parameters.get("Route"), null) ? null : (parameters.get("Route").toString()));	
					//jsonList.add(parameters.get("units").concat(".00"));
					//jsonList.add(Objects.equals(parameters.get("rxOnFileIndicator"), "false") ? "N" : "Y");
					//jsonList.add(Objects.equals(parameters.get("suppressBillingIndicator"), "false") ? "N" : "Y");
					jsonList.add(Objects.equals(parameters.get("DosageFormAmountUnitLow"), null) ? null : (parameters.get("DosageFormAmountUnitLow").toString()));
					jsonList.add(Objects.equals(parameters.get("DosageFormAmountUnitHigh"), null) ? null : (parameters.get("DosageFormAmountUnitHigh").toString()));	
					jsonList.add(Objects.equals(parameters.get("DosageFormAmountLow"), null) ? null : (parameters.get("DosageFormAmountLow")));	
					jsonList.add(Objects.equals(parameters.get("DosageFormAmountHigh"), null) ? null : (parameters.get("DosageFormAmountHigh")));	
					jsonList.add(Objects.equals(parameters.get("DosageUnitLow"), null) ? null : (parameters.get("DosageUnitLow").toString()));	
					jsonList.add(Objects.equals(parameters.get("DosageUnitHigh"), null) ? null : (parameters.get("DosageUnitHigh").toString()));
					jsonList.add(Objects.equals(parameters.get("DosageLow"), null) ? null : (parameters.get("DosageLow")));	
					jsonList.add(Objects.equals(parameters.get("DosageHigh"), null) ? null : (parameters.get("DosageHigh")));	
					jsonList.add(Objects.equals(parameters.get("FrequencyLow"), null) ? null : (parameters.get("FrequencyLow")));	
					jsonList.add(Objects.equals(parameters.get("FrequencyHigh"), null) ? null : (parameters.get("FrequencyHigh")));	
					jsonList.add(Objects.equals(parameters.get("FrequencyLow2"), null) ? null : (parameters.get("FrequencyLow2")));	
					jsonList.add(Objects.equals(parameters.get("FrequencyHigh2"), null) ? null : (parameters.get("FrequencyHigh2")));	
					jsonList.add(Objects.equals(parameters.get("IntervalUnitLow"), null) ? null : (parameters.get("IntervalUnitLow").toString()));	
					jsonList.add(Objects.equals(parameters.get("IntervalUnitHigh"), null) ? null : (parameters.get("IntervalUnitHigh").toString()));	
					jsonList.add(Objects.equals(parameters.get("IntervalUnitLow2"), null) ? null : (parameters.get("IntervalUnitLow2").toString()));	
					jsonList.add(Objects.equals(parameters.get("IntervalUnitHigh2"), null) ? null : (parameters.get("IntervalUnitHigh2").toString()));	
					jsonList.add(Objects.equals(parameters.get("IntervalLow"), null) ? null : (parameters.get("IntervalLow")));	
					jsonList.add(Objects.equals(parameters.get("IntervalHigh"), null) ? null : (parameters.get("IntervalHigh")));	
					jsonList.add(Objects.equals(parameters.get("IntervalLow2"), null) ? null : (parameters.get("IntervalLow2")));	
					jsonList.add(Objects.equals(parameters.get("IntervalHigh2"), null) ? null : (parameters.get("IntervalHigh2")));	
					jsonList.add(Objects.equals(parameters.get("DurationUnitLow"), null) ? null : (parameters.get("DurationUnitLow").toString()));
					jsonList.add(Objects.equals(parameters.get("DurationUnitHigh"), null) ? null : (parameters.get("DurationUnitHigh").toString()));	
					jsonList.add(Objects.equals(parameters.get("DurationLow"), null) ? null : (parameters.get("DurationLow").toString()));	
					jsonList.add(Objects.equals(parameters.get("DurationHigh"), null) ? null : (parameters.get("DurationHigh").toString()));	
				//	jsonList.add(Objects.equals(parameters.get("ServiceItemDescription"), null) ? null : (parameters.get("ServiceItemDescription").toString()));	
					//jsonList.add(Objects.equals(parameters.get("Cpt4Code"), null) ? null : (parameters.get("Cpt4Code").toString()));	
					//jsonList.add(parameters.get("RecallPlanId").toString());
				//	jsonList.add(Objects.equals(parameters.get("ServiceDate"), null) ? null : (parameters.get("ServiceDate").toString().split("T")[0]).replace("-", ""));
				
				  //jsonList.add(parameters.get("StopReasonId").toString());
				
					//jsonList.add(Objects.equals(parameters.get("fax"), "") ? null : (parameters.get("fax").toString().toUpperCase()));	
				//	jsonList.add(Objects.equals(parameters.get("homePhone"), "") ? null : (parameters.get("homePhone").toString().toUpperCase()));	
				
					
					
					//jsonList.add(parameters.get("zip").toString());
					//jsonList.add(Objects.equals(parameters.get("VerbalOrderIndicator"), "false") ? "N" : "Y");
					//jsonList.add(parameters.get("AllergiesReviewed").toString());
					//if (get.(AllergiesReviewed) =="egg")
			


					status = ValidateResults.resultsvalidation(jsonList, DatabaseConnection.statementExecution(BaseTest.connNGA, strSQLQuery));
					//status = ValidateResults.resultValidation(jsonList, listDatabase);
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
