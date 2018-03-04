package com.endpoints;



import java.util.ArrayList;
import java.util.HashMap;

import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_AddCareTeamMember {
	public static Boolean chart_AddCareTeamMember_Test(HashMap<String, String> parameters, String strResponse,HashMap<String, Object> mapPostDetails, SoftAssert softAssert, String strQuery)
	{
		Boolean status=false;


		try
		{
			int countAfterPOST = DatabaseConnection.returnNumberOfRows(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery));
			if(Math.abs(countAfterPOST - ((int)mapPostDetails.get("countBeforePOST")))  == 1)
			{
				try
				{
					String strSQLQuery = "select top 1 txt_provider_id as providerId, "
							+"txt_first_name as firstName, "
							+"txt_last_name as lastName, "
							+"txt_email as email, "
							+"txt_active_date as activeDate, "   
							+"txt_address as address,  "
							+ "txt_agency_name as agencyName, "
							+ "txt_agency_type as agencyType, "
							+ "txt_cell_phone as cellPhone, "
							+ "txt_city as city, "
							+ "txt_fax as fax, "
							+ "txt_home_phone as homePhone, "
							+ "txt_inactive_date as inativeDate, "
							+ "txt_patient_relationship as patientRelationship, "
							+ "txt_state as state, "
							+ "txt_support_role as supportRole, "
							+  "txt_zip as zip "
							+ "FROM  patient_care_team_memb_ext_ "
							+ " WHERE person_id = '"+parameters.get("personId")+"' order by create_timestamp desc";
					
					ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strSQLQuery));

					ArrayList<Object> jsonList = new ArrayList<Object>();
					
					jsonList.add(Objects.equals(parameters.get("providerId"), "") ? null : (parameters.get("providerId").toString().toLowerCase()));		
					jsonList.add(Objects.equals(parameters.get("firstName"), "") ? null : (parameters.get("firstName").toString()));	
					jsonList.add(Objects.equals(parameters.get("lastName"), "") ? null : (parameters.get("lastName").toString()));	
					jsonList.add(Objects.equals(parameters.get("email"), "") ? null : (parameters.get("email").toString()));	
					//jsonList.add(parameters.get("firstName").toString());
					//jsonList.add(parameters.get("lastName").toString());
					//jsonList.add(parameters.get("email").toString());
					jsonList.add(Objects.equals(parameters.get("activeDate"), null) ? null : (parameters.get("activeDate").toString().split("T")[0]).replace("-", ""));
			
					jsonList.add(Objects.equals(parameters.get("address"), "") ? null : (parameters.get("address").toString()));	
					jsonList.add(Objects.equals(parameters.get("agencyName"), "") ? null : (parameters.get("agencyName").toString()));	
					jsonList.add(Objects.equals(parameters.get("agencyType"), "") ? null : (parameters.get("agencyType").toString()));	
					jsonList.add(Objects.equals(parameters.get("cellPhone"), "") ? null : (parameters.get("cellPhone").toString().toUpperCase()));	
					jsonList.add(Objects.equals(parameters.get("city"), "") ? null : (parameters.get("city").toString()));	
					jsonList.add(Objects.equals(parameters.get("fax"), "") ? null : (parameters.get("fax").toString().toUpperCase()));	
					jsonList.add(Objects.equals(parameters.get("homePhone"), "") ? null : (parameters.get("homePhone").toString().toUpperCase()));	
				
					jsonList.add(Objects.equals(parameters.get("inativeDate"), null) ? null : (parameters.get("inativeDate").toString().split("T")[0]).replace("-", ""));
					jsonList.add(Objects.equals(parameters.get("patientRelationship"), "") ? null : (parameters.get("patientRelationship").toString()));	
					//jsonList.add(parameters.get("patientRelationship").toString());
					jsonList.add(Objects.equals(parameters.get("state"), "") ? null : (parameters.get("state").toString()));	
					jsonList.add(parameters.get("supportRole").toString());
					//jsonList.add(Objects.equals(parameters.get("fax"), "") ? null : (parameters.get("fax").toString().toLowerCase()));	
					jsonList.add(Objects.equals(parameters.get("zip"), "") ? null : (parameters.get("zip").toString().toUpperCase()));	
					//jsonList.add(parameters.get("state").toString());
					
					//jsonList.add(parameters.get("zip").toString());
					//jsonList.add(Objects.equals(parameters.get("VerbalOrderIndicator"), "false") ? "N" : "Y");
					//jsonList.add(parameters.get("AllergiesReviewed").toString());
					//if (get.(AllergiesReviewed) =="egg")
					/*String allergyReviewed = parameters.get("AllergiesReviewed");
					//jsonList.add(allergyReviewed.contains("egg")? "1":allergyReviewed.contains("neomycin")?2:allergyReviewed.contains("latex")?4:allergyReviewed.contains("gelatin")?8:"");				
					if(allergyReviewed.contains(","))
					{
						int i=0;
						String allergyReviewed1[] = allergyReviewed.split(",");
						for(int j=0;j<allergyReviewed1.length;j++)
						{
							if(allergyReviewed1[j].trim().equals("egg"))
								i=i+1
							else if((allergyReviewed1[j].trim().equals("latex")))
								i=i+4;
							else if((allergyReviewed1[j].trim().equals("gelatin")))
								i=i+8;
							else if((allergyReviewed1[j].trim().equals("neomycin")))
								i=i+2;
						}
						System.out.println(i);
						jsonList.add(i);
					}
					// String line ="egg ,latex,getain,neomycin" ;
					else
					{
						int i=0;
						String allergyReviewed1[]={allergyReviewed};
						for(int j=0;j<allergyReviewed1.length;j++)
						{
							if(allergyReviewed1[j].trim().equals("egg"))
								i=i+1;
							else if((allergyReviewed1[j].trim().equals("latex")))
								i=i+4;
							else if((allergyReviewed1[j].trim().equals("gelatin")))
								i=i+8;
							else if((allergyReviewed1[j].trim().equals("neomycin")))
								i=i+2;
						}
						System.out.println(i);
						jsonList.add(i);
					}*/



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
