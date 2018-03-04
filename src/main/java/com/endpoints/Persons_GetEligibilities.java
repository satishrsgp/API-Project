package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Persons_GetEligibilities 
{

	public static Boolean persons_GetEligibilities_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "select pe.erx_id as erxId, pe.erx_type as erxType, pe.eligibility_id eligibilityId, pe.person_id personId, "
						+ " pe.person_code as personCode, pe.request_timestamp as requestTimestamp, pe.eligible_ind as eligibleStatusCode, "
						+ " pe.pbm_id as pbmId, pe.pbm_name as pbmName, pe.provider_last_name as providerLastName, "
						+ " pe.provider_first_name as providerFirstName, pe.provider_middle_name as providerMiddleName, "
						+ " pe.plan_id as planId, pe.plan_name as planName, pe.group_id as groupId, pe.group_name as groupName, "
						+ " pe.pbm_member_id as pbmMemberId, pe.cardholder_id as cardholderId, pe.cardholder_first_name as cardholderFirstName, "
						+ " pe.cardholder_middle_name as cardholderMiddleName, pe.cardholder_last_name as cardholderLastName, "
						+ " pe.cardholder_suffix as cardholdersuffix, pe.cardholder_member_id as cardholderMemberId, "
						+ " pe.patient_first_name as patientFirstName, pe.patient_middle_name as patientMiddleName, "
						+ " pe.patient_last_name as patientLastName, pe.patient_suffix as patientSuffix, pe.patient_adders_1 as patientAddressLine1, "
						+ " pe.patient_adders_2 as patientAddressLine2, pe.patient_city as patientCity, pe.patient_state as patientState, "
						+ " pe.patient_zip as patientZip,  pe.patient_gender as patientGender, "//pe.patient_dob as patientDateOfBirth,
						+ " pe.formulary_id as formularyId, pe.alt_formulary_id as alternativeFormularyId, pe.trans_id as transactionId, "
						+ " pe.bin_location_number as binLocationNumber, case when pe.error_message_in_response_ind = 'N' then 'false' else 'true' "
						+ " end as isErrorMessageInResponse, pe.created_by as createdBy, pe.modified_by as modifiedBy, "
						+ " pe.create_timestamp as createTimestamp, pe.modify_timestamp as modifyTimestamp, pe.service_type_code as serviceTypeCode, "
						+ " case when pe.mail_order_authorized_ind = 'N' then 'false' else 'true' end as isMailOrderAuthorized, case when pe.retail_authorized_ind = 'N' then 'false' else 'true' "
						+ " end as isRetailAuthorized, pe.coverage_id as coverageId, pe.copay_id as copayId, pe.error_description as errorDescription, "
						+ " case when pe.demographic_change_ind = 'N' then 'false' else 'true' end as isDemographicChange, "
						+ " case when pe.health_authorized_ind = 'N' then 'false' else 'true' end as isHealthAuthorized, "
						+ " case when pe.specialty_authorized_ind = 'N' then 'false' else 'true' end as isSpecialtyAuthorized, "
						+ " pe.specialty_description as specialtyDescription, pe.health_description as healthDescription, "
						+ " case when pe.ltc_authorized_ind = 'N' then 'false' else 'true' end as isLtcAuthorized, pe.ltc_description as ltcDescription, "
						+ " case when pe.allow_resend_ind = 'N' then 'false' else 'true' end as isAllowResend, "
						+ " pe.interchange_control_number as interchangeControlNumber, pe.processor_control_number as processorControlNumber "
						+ " from patient_eligibility pe where pe.person_id = '"+parameters.get("personId")+"'"
						;
				
				//providerName ,"whoReviewed": null,"whenReviewed": null,

				
				String[] jsonMetaData = {"erxId","erxType","eligibilityId","personId","personCode","requestTimestamp;Date_YYYYMMDD HH:MM:SS","eligibleStatusCode",
						"pbmId","pbmName","providerLastName","providerFirstName","providerMiddleName","planId","planName","groupId","groupName"
						,"pbmMemberId","cardholderId","cardholderFirstName","cardholderMiddleName","cardholderLastName","cardholderSuffix",
						"cardholderMemberId","patientFirstName","patientMiddleName","patientLastName","patientSuffix","patientAddressLine1",
						"patientAddressLine2","patientCity","patientState","patientZip","patientGender","formularyId",    //"patientDateOfBirth",
						"alternativeFormularyId","transactionId","binLocationNumber","isErrorMessageInResponse","createdBy","modifiedBy"
						,"createTimestamp;Date_YYYYMMDD HH:MM:SS","modifyTimestamp;Date_YYYYMMDD HH:MM:SS","serviceTypeCode","isMailOrderAuthorized","isRetailAuthorized","coverageId",
						"copayId","errorDescription","isDemographicChange","isHealthAuthorized","isSpecialtyAuthorized","specialtyDescription"
						,"healthDescription","isLtcAuthorized","ltcDescription","isAllowResend","interchangeControlNumber","processorControlNumber"};
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..personId", jsonList);
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
