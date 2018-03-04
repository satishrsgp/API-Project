package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_AddOrderedVaccine 
{


	
	public static Boolean Chart_AddOrderedVaccine_Test(HashMap<String, String> parameters, String strResponse,HashMap<String, Object> mapPostDetails, SoftAssert softAssert, String strQuery)
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
							+ "administer_year as AdministerYear,"
							+ "administer_month as AdministerMonth,"
							+ "administer_day as AdministerDay,"
							+ "cvx_code as CvxCode, "
							+ "vaccine_desc as VaccineDescription,"
							+ "record_source as RecordSource,"
							+ "cpt4_code as Cpt4Code,"
							+ "seq_nbr  as SequenceNumber, "
							+ "vaccine_status as Status,"
							+ "brand_name as BrandName,"
							+ "site_code as SiteCode,"
							+ "site as Site,"
							+ "route_code as RouteCode, "
							+ "route as Route,"
							+ "units_code as UnitsCode,"
							+ "units as Units,"
							+ "lot_num as LotNumber,"
							+ "expiration_date as ExpirationDate, "
							+ "dose as Dose,"
							+ "manufacturer_name as ManufacturerName,"
							+ "not_administered_reason as NotAdministeredReason, "
							+ "not_administered_code as NotAdministeredCode,"
							+ "vaccine_comment as Comment,"
							+ "manufacturer_nbr as ManufacturerNumber, "
							+ "strength as Strength,"
							+ "created_by as AdministerBy,"
							+ "audit_id as AuditId,"
							+ "exception_ind as IsException, "
							+ "counsel_ind as IsCounselled,"
							+ "concent_from as ConcentFrom,"
							+ "ndc_id as NdcId,"
							+ "error_ind as IsError, "
							+ "snomed_immunity as SnomedImmunityCode,"
							+ "administer_time as AdministerTime,"
							+ "vfc_code as VfcCode, "
							+ "funding_src_code as FundingSourceCode,"
							+ "vfc_ind as IsVfc,"
							+ "override_invalid_dose_ind as OverrideInvalidDose "
							+ "from imm_order_vaccines "
							+ "where order_num='"+parameters.get("orderId")+"' order by create_timestamp desc";
					ArrayList<Object> jsonList = new ArrayList<Object>();
					jsonList.add(Objects.equals(parameters.get("AdministerYear"),"\"\"") ? null : parameters.get("AdministerYear").toString());
					jsonList.add(Objects.equals(parameters.get("AdministerMonth"), "\"\"") ? null : parameters.get("AdministerMonth").toString());
					jsonList.add(Objects.equals(parameters.get("AdministerDay"), "\"\"") ? null : parameters.get("AdministerDay").toString());
					/*jsonList.add(parameters.get("AdministerYear").toString());
					jsonList.add(parameters.get("AdministerMonth").toString());
					jsonList.add(parameters.get("AdministerDay").toString());*/
					jsonList.add(parameters.get("CvxCode").toString());
					jsonList.add(parameters.get("VaccineDescription").toString());
					jsonList.add(parameters.get("RecordSource").toString());
					jsonList.add(parameters.get("Cpt4Code").toString());
					jsonList.add(parameters.get("SequenceNumber").toString());
					jsonList.add(parameters.get("Status").toString());
					jsonList.add(parameters.get("BrandName").toString());
					jsonList.add(parameters.get("SiteCode").toString());
					jsonList.add(parameters.get("Site").toString());
					jsonList.add(parameters.get("RouteCode").toString());
					jsonList.add(parameters.get("Route").toString());
					jsonList.add(parameters.get("UnitsCode").toString());
					jsonList.add(parameters.get("Units").toString());
					jsonList.add(parameters.get("LotNumber").toString());
					jsonList.add(Objects.equals(parameters.get("ExpirationDate"), "") ? null : parameters.get("ExpirationDate").toString().toUpperCase());
					jsonList.add(Objects.equals(parameters.get("Dose"), "") ? null : parameters.get("Dose").toString()+".000");
					jsonList.add(parameters.get("ManufacturerName").toString());
					jsonList.add(parameters.get("NotAdministeredReason").toString());
					jsonList.add(parameters.get("NotAdministeredCode").toString());
					jsonList.add(parameters.get("Comment").toString());
					jsonList.add(parameters.get("ManufacturerNumber").toString());
					jsonList.add(parameters.get("Strength").toString());
					jsonList.add(parameters.get("AdministerBy").toString());
					jsonList.add(Objects.equals(parameters.get("AuditId"), null) ? null : parameters.get("AuditId").toString().toUpperCase());
					jsonList.add(Objects.equals(parameters.get("IsException"), "false") ? "N" : "Y");
					jsonList.add(Objects.equals(parameters.get("IsCounselled"), "false") ? "N" : "Y");
					jsonList.add(parameters.get("ConcentFrom").toString());
					jsonList.add(parameters.get("NdcId").toString());
					jsonList.add(Objects.equals(parameters.get("IsError"), "false") ? "N" : "Y");
					jsonList.add(parameters.get("SnomedImmunityCode").toString());
					jsonList.add(Objects.equals(parameters.get("AdministerTime"), "") ? null : parameters.get("AdministerTime").toString().toUpperCase());
					jsonList.add(parameters.get("VfcCode").toString());
					jsonList.add(parameters.get("FundingSourceCode").toString());
					jsonList.add(Objects.equals(parameters.get("IsVfc"), "false") ? "N" : "Y");
					jsonList.add(Objects.equals(parameters.get("OverrideInvalidDose"), "false") ? "N" : "Y");					
					
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
