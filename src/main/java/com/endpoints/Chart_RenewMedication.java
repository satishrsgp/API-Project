
package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;
import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_RenewMedication
{
	public static Boolean chart_RenewMedication(HashMap<String, String> parameters,String strResponse, HashMap<String, Object> mapPostDetails, SoftAssert softAssert, String strQuery)
	{
		Boolean status=false;
		try
		{
			int countAfterPOST = DatabaseConnection.returnNumberOfRows(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery));
			if(countAfterPOST - ((int)mapPostDetails.get("countBeforePOST")) == 1)
			{
				
				String strQuery1="select top 1 rx_quanity,rx_refills,case when dispense_as_writtn_ind='Y' then 'true' else 'false' end as dispense_as_writtn_ind ,"
						+ "case when prescribed_else_ind='Y' then 'true' else 'false' end as prescribed_else_ind ,case when privacy_ind='Y' then 'true' else 'false' end as privacy_ind,prescribed_else_location,"
						+ "rx_comment,rx_special_instruction,start_date,date_stopped,provider_id,location_id, diagnosis_code_id,diagnosis_code_id2,diagnosis_code_id3,case when prn_ind='Y' then 'true' else 'false' end as prn_ind,prn_reason ,rx_units,poeostrid,custom_dosage_order_id,"
						+ "ped_order_id,sig_desc,supervising_provider_id,case when prior_auth_ind='Y' then 'true' else 'false' end as prior_auth_ind ,prior_auth_date,"
						+ "case when samples_ind='Y' then 'true' else 'false' end as samples_ind,lot_number,expiration_date from patient_medication "
						+ "where person_id='"+parameters.get("personId")+"' and enc_id='"+parameters.get("encounterId")+"' and renew_parent_id='"+parameters.get("medicationId")+"'"
						+ "order by create_timestamp desc";
				
				ArrayList<Object> jsonList = new ArrayList<Object>();
			
				jsonList.add(parameters.get("RxQuantity").toString());
                jsonList.add(parameters.get("RxRefills"));
                jsonList.add(parameters.get("DispenseAsWritten"));
                jsonList.add(parameters.get("IsPrescribedElsewhere").toString());
                jsonList.add(parameters.get("PrivacyIndicator").toString());
                jsonList.add(parameters.get("PrescribedElsewhereLocation"));
                jsonList.add(parameters.get("RxComment").toString());
                jsonList.add(parameters.get("RxSpecialInstruction").toString());
            
                jsonList.add(Objects.equals(parameters.get("StartDate"), "") ? null : (parameters.get("StartDate").toString().split("T")[0]).replace("-", ""));
                jsonList.add(Objects.equals(parameters.get("StopDate"), "") ? null : (parameters.get("StopDate").toString().split("T")[0]).replace("-", ""));
                jsonList.add(parameters.get("ProviderId").toString().toUpperCase());
                jsonList.add(parameters.get("LocationId").toString().toUpperCase());
                jsonList.add(parameters.get("DiagnosisCode").toString());
                jsonList.add(parameters.get("SecondaryDiagnosisCode").toString());
                jsonList.add(parameters.get("TertiaryDiagnosisCode"));
                jsonList.add(parameters.get("IsPrn").toString());
                jsonList.add(parameters.get("PrnReason").toString());
                jsonList.add(parameters.get("RxUnits").toString());
                
                jsonList.add(parameters.get("FdbDosageId").toString().toUpperCase());
                jsonList.add(parameters.get("CustomDosageId").toString().toUpperCase());
                jsonList.add(Objects.equals(parameters.get("PedOrderId"), "null") ? null : (parameters.get("PedOrderId")));
                jsonList.add(parameters.get("SigDescription").toString());
                jsonList.add(parameters.get("SupervisingProviderId").toString().toString().toUpperCase());
                jsonList.add(parameters.get("PriorAuthorizationIndicator"));
                jsonList.add(Objects.equals(parameters.get("PriorAuthorizationDate"), "") ? null : (parameters.get("PriorAuthorizationDate").toString().split("T")[0]).replace("-", ""));
                jsonList.add(parameters.get("SamplesIndicator").toString());
                jsonList.add(parameters.get("SampleLotNumber").toString());
                jsonList.add(Objects.equals(parameters.get("SampleExpirationDate"), null) ? null : (parameters.get("SampleExpirationDate").toString().split("T")[0]).replace("-", ""));
                
                
				status = ValidateResults.resultsvalidation(jsonList, DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
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