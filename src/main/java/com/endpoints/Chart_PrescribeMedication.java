package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_PrescribeMedication 
{

	
	public static Boolean Chart_PrescribeMedication_Test(HashMap<String, String> parameters, String strResponse,HashMap<String, Object> mapPostDetails, SoftAssert softAssert, String strQuery)
	{
		Boolean status=false;
		try
		{
			int countAfterPOST = DatabaseConnection.returnNumberOfRows(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery));
			if(Math.abs(countAfterPOST - ((int)mapPostDetails.get("countBeforePOST")))  == 1)
			{
				try
				{
					String strSQLQuery = "Select top 1 "
							+ "rx_quanity as RxQuantity, "
							+ "rx_refills as RxRefills, "
							+ "case when dispense_as_writtn_ind = 'N' then 'false' else 'true' end as DispenseAsWritten, "
							+ "case when prescribed_else_ind  = 'N' then 'false' else 'true' end as IsPrescribedElsewhere, "
							+ "case when privacy_ind = 'N' then 'false' else 'true' end  as PrivacyIndicator, "
							+ "prescribed_else_location as PrescribedElsewhereLocation, "
							+ "rx_comment as RxComment, "
							+ "rx_special_instruction as RxSpecialInstruction, "
							+ "start_date as StartDate, "
							+ "date_stopped as StopDate, "
							+ "provider_id as ProviderId, "
							+ "location_id as LocationId, "
							+ "diagnosis_code_id as DiagnosisCode, "
							+ "diagnosis_code_id2 as SecondaryDiagnosisCode, "
							+ "diagnosis_code_id3 as TertiaryDiagnosisCode, "
							+ "case when prn_ind = 'N' then 'false' else 'true' end as IsPrn, "
							+ "prn_reason as PrnReason, "
							+ "rx_units as RxUnits, "
							//+ "--FdbDosageId "
							+ "custom_dosage_order_id as CustomDosageId, "
							+ "case when ped_order_id is null then '1' else ped_order_id end as PedOrderId, "
							+ "medid as MedicationId, "
							+ "case when reprndc_ind = 'N' then 'false' else 'true' end as IsRepresentativeNdc, "
							+ "sig_codes as SigCode, "
							+ "sig_desc as SigDescription, "
							+ "supervising_provider_id as SupervisingProviderId, "
							+ "case when prior_auth_ind = 'N' then 'false' else 'true' end as PriorAuthorizationIndicator, "
							+ "prior_auth_date as PriorAuthorizationDate "
							//+ "--ByPassDeaRefillLimitCheck, "
							//+ ",case when samples_ind = 'N' then 'false' else 'true' end as SamplesIndicator, "
							//+ "lot_number as SampleLotNumber, "
							//+ "expiration_date as SampleExpirationDate "
							+ "from patient_medication "
							+ "where person_id = '"+parameters.get("personId")+"' "
							+ " AND enc_id = '"+parameters.get("encounterId")+"'  "
							+ "order by create_timestamp desc";
					ArrayList<Object> jsonList = new ArrayList<Object>();
					jsonList.add(parameters.get("RxQuantity"));
					jsonList.add(parameters.get("RxRefills"));
					jsonList.add(parameters.get("DispenseAsWritten"));
					jsonList.add(parameters.get("IsPrescribedElsewhere"));
					jsonList.add(parameters.get("PrivacyIndicator"));
					jsonList.add(parameters.get("PrescribedElsewhereLocation"));
					jsonList.add(parameters.get("RxComment"));
					jsonList.add(parameters.get("RxSpecialInstruction"));
					jsonList.add(parameters.get("StartDate").toString().substring(0,10).replace(" T", " ").replace("-", ""));
					jsonList.add(parameters.get("StopDate").toString().substring(0,10).replace(" T", " ").replace("-", ""));
					jsonList.add(parameters.get("ProviderId").toString().toUpperCase());
					jsonList.add(parameters.get("LocationId").toString().toUpperCase());
					jsonList.add(parameters.get("DiagnosisCode"));
					jsonList.add(parameters.get("SecondaryDiagnosisCode"));
					jsonList.add(parameters.get("TertiaryDiagnosisCode"));
					jsonList.add(parameters.get("IsPrn"));
					jsonList.add(parameters.get("PrnReason"));
					jsonList.add(parameters.get("RxUnits"));
					jsonList.add(parameters.get("CustomDosageId").toString().toUpperCase());
					jsonList.add(parameters.get("PedOrderId"));
					jsonList.add(parameters.get("MedicationId"));
					jsonList.add(parameters.get("IsRepresentativeNdc"));
					jsonList.add(parameters.get("SigCode"));
					jsonList.add(parameters.get("SigDescription"));
					jsonList.add(parameters.get("SupervisingProviderId"));
					jsonList.add(parameters.get("PriorAuthorizationIndicator"));
					jsonList.add(parameters.get("PriorAuthorizationDate").toString().substring(0,10).replace(" T", " ").replace("-", ""));
										
					status = ValidateResults.resultsvalidation(jsonList, DatabaseConnection.statementExecution(BaseTest.connNGA, strSQLQuery));
				}
				catch(Exception e)
				{
					e.printStackTrace();
					Log.info("Error in Test Class and the detail are : \n" + e.getMessage());
					softAssert.fail("Error in Test Script please look at logs");
				}
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
