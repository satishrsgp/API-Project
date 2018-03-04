package com.endpoints;



import java.util.ArrayList;
import java.util.HashMap;

import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_AddImmunizationOrders {
	public static Boolean Chart_AddImmunizationOrders_Test(HashMap<String, String> parameters, String strResponse,HashMap<String, Object> mapPostDetails, SoftAssert softAssert, String strQuery)
	{
		Boolean status=false;


		try
		{
			int countAfterPOST = DatabaseConnection.returnNumberOfRows(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery));
			if(Math.abs(countAfterPOST - ((int)mapPostDetails.get("countBeforePOST")))  == 1)
			{
				try
				{
					String strSQLQuery = "select top 1 generated_by as GeneratedBy, "
							+"location_id as LocationId, "
							+"ordering_provider as OrderingProviderId, "
							+"supervisor_provider as SupervisorProviderId, "
							+"registry_id as RegistryId, "   
							+"order_comment as Comment,  "
							+ "verbal_order_ind as VerbalOrderIndicator, "
							+ "allergy_review as [AllergiesReviewed] "
							+ "FROM  imm_nor "
							+ " WHERE person_id = '"+parameters.get("personId")+"' and enc_id = '"+parameters.get("encounterId")+"' order by create_timestamp desc";

					ArrayList<Object> jsonList = new ArrayList<Object>();

					//jsonList.add(parameters.get("EncounterId").toString());
					jsonList.add(parameters.get("GeneratedBy").toString());
					jsonList.add(parameters.get("LocationId").toString());
					jsonList.add(parameters.get("OrderingProviderId").toString());
					jsonList.add(parameters.get("SupervisorProviderId").toString());
					jsonList.add(parameters.get("RegistryId").toString());
					jsonList.add(parameters.get("Comment").toString());
					jsonList.add(Objects.equals(parameters.get("VerbalOrderIndicator"), "false") ? "N" : "Y");
					//jsonList.add(parameters.get("AllergiesReviewed").toString());
					//if (get.(AllergiesReviewed) =="egg")
					String allergyReviewed = parameters.get("AllergiesReviewed");
				
					//jsonList.add(allergyReviewed.contains("egg")? "1":allergyReviewed.contains("neomycin")?2:allergyReviewed.contains("latex")?4:allergyReviewed.contains("gelatin")?8:"");				
					if(allergyReviewed.contains(","))
					{
						int i=0;
						String allergyReviewed1[] = allergyReviewed.split(",");

						for(int j=0;j<allergyReviewed1.length;j++)
						{
							//allergyReviewed1[j]= "\"" +allergyReviewed1[j]+ "\"";
							
								if(allergyReviewed1[j].contains("egg"))
								i=i+1;
							else if((allergyReviewed1[j].trim().contains("latex")))
								i=i+4;
							else if((allergyReviewed1[j].trim().contains("gelatin")))
								i=i+8;
							else if((allergyReviewed1[j].trim().contains("neomycin")))
								i=i+2;
						}
						System.out.println(i);
						jsonList.add(i);
						//jsonList.add(allergyReviewed1);
					}
					// String line ="egg ,latex,getain,neomycin" ;
					else
					{
						int i=0;
						String allergyReviewed1[]={allergyReviewed};
						for(int j=0;j<allergyReviewed1.length;j++)
						{
							if(allergyReviewed1[j].trim().contains("egg"))
								i=i+1;
							else if((allergyReviewed1[j].trim().contains("latex")))
								i=i+4;
							else if((allergyReviewed1[j].trim().contains("gelatin")))
								i=i+8;
							else if((allergyReviewed1[j].trim().contains("neomycin")))
								i=i+2;
						}
						System.out.println(i);
						jsonList.add(i);
					}



					status = ValidateResults.resultsvalidation(jsonList, DatabaseConnection.statementExecution(BaseTest.connNGA, strSQLQuery));
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
