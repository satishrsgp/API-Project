package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_AddEncounter 
{

	
	public static Boolean Chart_AddEncounter_Test(HashMap<String, String> parameters, String strResponse,HashMap<String, Object> mapPostDetails, SoftAssert softAssert, String strQuery)
	{
		Boolean status=false;
		
		
		try
		{
			int countAfterPOST = DatabaseConnection.returnNumberOfRows(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery));
			if(Math.abs(countAfterPOST - ((int)mapPostDetails.get("countBeforePOST")))  == 1)
			{
				try
				{
					/*String strSQLQuery = "select "
							+ "enterprise_id as enterpriseId,practice_id as practiceId,person_id as personId,case_id as caseId,casemgt_case_id as caseManagementCaseId, "
							+ "parent_enc_id as parentEncounterId,group_enc_id as groupEncounterId,enc_timestamp as timestamp,"
							+ "case when Lock_ind = 'N' then 'false' else 'true' end as isLocked, "
							+ "enc_timestamp_tz as timestampTimezone,rendering_provider_id as renderingProviderId,locked_by as lockedBy,lock_timestamp as lockTimestamp, "
							+ "lock_timestamp_tz as lockTimestampTimezone,billable_timestamp as billableTimestamp,billable_timestamp_tz as billableTimestampTimezone, "
							+ "Case when clinical_ind = 'N' then 'false' else 'true' end as isClinical, "
							+ "case when billable_ind = 'N' then 'false' else 'true' end as isBillable, "
							+ "case when optical_ind = 'N' then 'false' else 'true' end  as isOptical, "
							+ "location_id as locationId,remarks as remarks, "
							+ "enc_status as status,source_product_id as sourceProductId,site_id as siteId,guar_id as guarantorId,guar_type as guarantorType, "
							+ "cob1_payer_id as cob1PayerId,cob1_insured_person_id as cob1InsuredPersonId,cob1_person_payer_id as cob1PersonPayerId,onset_date as onsetDate, "
							+ "onset_time as onsetTime,admit_date as admitDate,discharge_date as dischargeDate,refer_provider_id as referringProviderId,refer_provider_name as referringProviderName "
							+ "From Patient_encounter "
							+ "where person_id='"+parameters.get("personId")+"' order by create_timestamp desc";*/
					String strSQLQuery = "Select top 1"
							+ "rendering_provider_id,location_id,cast (cast(enc_timestamp as date) as varchar ) as enc_timestamp  "
							+ "from patient_encounter "
							+ "where person_id = '"+parameters.get("personId")+"' order by create_timestamp desc";
					
					ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strSQLQuery));
					
					ArrayList<Object> jsonList = new ArrayList<Object>();
					/*jsonList.add(parameters.get("enterpriseId").toString());
					jsonList.add(parameters.get("practiceId").toString());
					jsonList.add(parameters.get("personId").toString().toUpperCase());
					jsonList.add(parameters.get("caseId").toString());
					jsonList.add(parameters.get("caseManagementCaseId").toString());
					jsonList.add(parameters.get("parentEncounterId").toString());
					jsonList.add(parameters.get("groupEncounterId").toString());
					jsonList.add(parameters.get("timestamp").toString().split("\\.")[0]);
					jsonList.add(parameters.get("isLocked").toString());
					jsonList.add(parameters.get("timestampTimezone").toString());
					jsonList.add(parameters.get("renderingProviderId").toString().toUpperCase());
					jsonList.add(parameters.get("lockedBy").toString());
					jsonList.add(parameters.get("lockTimestamp").toString().split("\\.")[0]);
					jsonList.add(parameters.get("lockTimestampTimezone").toString());
					jsonList.add(parameters.get("billableTimestamp").toString().split("\\.")[0]);
					jsonList.add(parameters.get("billableTimestampTimezone").toString());
					jsonList.add(parameters.get("isClinical").toString());
					jsonList.add(parameters.get("isBillable").toString());
					jsonList.add(parameters.get("isOptical").toString());
					jsonList.add(parameters.get("locationId").toString());
					jsonList.add(parameters.get("remarks").toString());
					jsonList.add(parameters.get("sourceProductId").toString());
					jsonList.add(parameters.get("siteId").toString());
					jsonList.add(parameters.get("guarantorId").toString().toUpperCase());
					jsonList.add(parameters.get("guarantorType").toString());
					jsonList.add(parameters.get("cob1PayerId").toString());
					jsonList.add(parameters.get("cob1InsuredPersonId").toString());
					jsonList.add(parameters.get("cob1PersonPayerId").toString());
					jsonList.add(parameters.get("onsetDate").toString());
					jsonList.add(parameters.get("admitDate").toString());
					jsonList.add(parameters.get("dischargeDate").toString());
					jsonList.add(parameters.get("referringProviderId").toString());
					jsonList.add(parameters.get("referringProviderName").toString());*/
					jsonList.add(parameters.get("RenderingProviderId").toString().toUpperCase());
					jsonList.add(parameters.get("LocationId").toString().toUpperCase());
					jsonList.add(parameters.get("EncounterTimestamp").toString().substring(0, 10));
					
					status = ValidateResults.resultValidation(jsonList, listDatabase);
					//status = ValidateResults.resultsvalidation(jsonList, DatabaseConnection.statementExecution(BaseTest.connNGA, strSQLQuery));
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
