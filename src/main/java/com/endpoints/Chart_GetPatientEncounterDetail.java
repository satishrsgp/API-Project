package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetPatientEncounterDetail
{

	/*
	 * ********************************************************************************
	 * Class Name						:Chart_GetPatientEncounters
	 * Created By						: 
	 * Created Date						: 
	 * Modified Date					:
	 * Modified By						:
	 * Parameters						: 
	 * Test Case ID						:
	 * Test Case Author					: 
	 * Test Case Modified By			:
	 * Test Case Reviewed By			:
	 * Purpose							: Route will fetch the allergy chart details
	 ***********************************************************************************
	 */

	public static Boolean chart_GetPatientEncounterDetail_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		//System.out.println(strResponse);
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{ 
				String strQuery = "select" 
						+" UPPER(CAST(pe.enc_id AS CHAR(36))) as id,pe.enterprise_id as enterpriseId,pe.practice_id as practiceId,UPPER(CAST(pe.person_id AS CHAR(36))) as personId,"
						+" UPPER(CAST(pe.case_id AS CHAR(36))) as caseId,pe.casemgt_case_id as caseManagementCaseId,UPPER(CAST(pe.parent_enc_id AS CHAR(36))) as parentEncounterId,"
						+" UPPER(CAST(pe.group_enc_id AS CHAR(36))) as groupEncounterId,pe.enc_timestamp as timestamp,case when dbo.fn_is_encounter_Locked(pe.enc_id)='Y' then 'true' else 'false' end as isLocked,"
						+" pe.enc_timestamp_tz as timestarendermpTimezone,UPPER(CAST(pe.rendering_provider_id AS CHAR(36))) as renderingProviderId,pm.description as renderingProviderName,pe.locked_by as lockedBy,pe.lock_timestamp as lockTimestamp,"
						+" pe.lock_timestamp_tz as lockTimestampTimezone,pe.billable_timestamp as billableTimestamp,pe.billable_timestamp_tz as billableTimestampTimezone,"
						+" case when pe.clinical_ind='Y' then 'true' else 'false' end as isClinical,case when pe.billable_ind='Y' then 'true' else 'false' end as isBillable,"
						+" case when pe.optical_ind='Y' then 'true' else 'false' end as isOptical,UPPER(CAST(pe.location_id AS CHAR(36))) as locationId,pe.remarks as remarks,pe.enc_status as status,"
						+" pe.source_product_id as sourceProductId,pe.site_id as siteId,"
						+"pe.enc_nbr as encounterNumber, "
						+" UPPER(CAST(pe.guar_id AS CHAR(36))) as guarantorId,pe.guar_type as guarantorType,"
						+" UPPER(CAST(pe.cob1_payer_id AS CHAR(36))) as cob1PayerId,UPPER(CAST(pe.cob1_insured_person_id AS CHAR(36))) as cob1InsuredPersonId,"
						+" UPPER(CAST(pe.cob1_person_payer_id AS CHAR(36))) as cob1PersonPayerId,pe.onset_date as onsetDate,pe.onset_time as onsetTime,pe.admit_date as admitDate,pe.discharge_date as dischargeDate,"
					+" UPPER(CAST(pe.refer_provider_id AS CHAR(36))) as referProviderId,pm1.description as referingProviderName"
						+" from patient_encounter as pe left join provider_mstr pm on pe.rendering_provider_id=pm.provider_id"
						+" left join provider_mstr pm1 on pe.refer_provider_id=pm1.provider_id"
						+" where "
						+" pe.enterprise_id='"+System.getProperty("LoggedInEnterpriseId")+"' and pe.practice_id='"+System.getProperty("LoggedInPracticeId")+"' and pe.person_id = '"+parameters.get("personId")+"' and pe.enc_id = '"+parameters.get("encounterId")+"'" 
							//	+ "pe.person_id='"+personId+"' and pe.enc_id='"+encounterId+"' and"
						
						+" and (pe.clinical_ind='Y' or pe.billable_ind='Y' or pe.optical_ind='Y' )"
						+" order by pe.enc_timestamp desc,pe.parent_enc_id";
				
						
		
				//
				String[] jsonMetaData = {"id;GUID","enterpriseId","practiceId","personId;GUID","caseId;GUID","caseManagementCaseId:GUID", "parentEncounterId","groupEncounterId","timestamp;Date_YYYYMMDD HH:MM:SS","isLocked","timestampTimezone","renderingProviderId;GUID","renderingProviderName","lockedBy","lockTimestamp;Date_YYYYMMDD HH:MM:SS","lockTimestampTimezone","billableTimestamp;Date_YYYYMMDD HH:MM:SS","billableTimestampTimezone","isClinical","isBillable","isOptical","locationId","remarks","status","sourceProductId","siteId","encounterNumber","guarantorId","guarantorType","cob1PayerId","cob1InsuredPersonId","cob1PersonPayerId","onsetDate","onsetTime","admitDate","dischargeDate","referringProviderId","referringProviderName"};

				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..id", jsonList);
					//objHandler = FinalAssertions.assertResponseCount(jsonMetaData, objHandler, DatabaseConnection.statementExecution(connNGA, strQuery), "id");
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
