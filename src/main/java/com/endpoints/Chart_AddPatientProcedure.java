package com.endpoints;



import java.util.ArrayList;
import java.util.HashMap;

import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_AddPatientProcedure {
	public static Boolean chart_AddPatientProcedure_Test(HashMap<String, String> parameters, String strResponse,HashMap<String, Object> mapPostDetails, SoftAssert softAssert, String strQuery)
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
					
							+  " service_item_id as serviceItemId, "
				
							+ " service_date as serviceDate, "
							+ " start_time as startTime, "
							+ " stop_time as stopTime, "
							+ " amount as amount, "
							+ " place_of_service as placeOfService, "
							+ " units as units, "
							+ " rx_on_file_ind as rxOnFileIndicator, "
							+ " suppress_billing_ind as suppressBillingIndicator, "
							+ " national_drug_code as nationalDrugCode, "
							+ " location_id as locationId, "
							+ " provider_id as providerId, " 
							+ " tooth as tooth, "
							+ " surface as surface, "
							+ " quadrant as quadrant, "
							+ " diagnosis_code_id_1 as diagnosisCodeId1, "
							+ " diagnosis_code_id_2 as diagnosisCodeId2, "
							+ " diagnosis_code_id_3 as diagnosisCodeId3, "
							+ " diagnosis_code_id_4 as diagnosisCodeId4, "
							+ " diagnosis_code_id_5 as diagnosisCodeId5, "
							+ " diagnosis_code_id_6 as diagnosisCodeId6, "
							+ " diagnosis_code_id_7 as diagnosisCodeId7, "
							+ " diagnosis_code_id_8 as diagnosisCodeId8, "
							+ " diagnosis_code_id_9 as diagnosisCodeId9, "
							+ " diagnosis_code_id_10 as diagnosisCodeId10, "
							+ " diagnosis_code_id_11 as diagnosisCodeId11, "
							+ " diagnosis_code_id_12 as diagnosisCodeId12, "
				
							+ "modifier_id_1 as modifierId1, "
							+ "modifier_id_2 as modifierId2, "
							+ " modifier_id_3 as modifierId3, "
							+ " modifier_id_4 as modifierId4, "
							+ " note as note "
							+ " from patient_procedure  "
							+ "  WHERE person_id = '"+parameters.get("personId")+"' and  enc_id= '"+parameters.get("encounterId")+"' order by create_timestamp desc";
					
					//ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strSQLQuery));

					ArrayList<Object> jsonList = new ArrayList<Object>();
					
					jsonList.add(Objects.equals(parameters.get("serviceItemId"), null) ? null : (parameters.get("serviceItemId").toString()));	
					jsonList.add(Objects.equals(parameters.get("serviceDate"), null) ? null : (parameters.get("serviceDate").toString().split("T")[0]).replace("-", ""));
					jsonList.add(Objects.equals(parameters.get("startTime"), "null") ? "" : (parameters.get("startTime").toString()));	
					jsonList.add(Objects.equals(parameters.get("stopTime"), "null") ? "" : (parameters.get("stopTime").toString()));
					jsonList.add(parameters.get("amount").concat(".00"));
					jsonList.add(Objects.equals(parameters.get("placeOfService"), null) ? null : (parameters.get("placeOfService").toString()));	
					jsonList.add(parameters.get("units").concat(".00"));
					jsonList.add(Objects.equals(parameters.get("rxOnFileIndicator"), "false") ? "N" : "Y");
					jsonList.add(Objects.equals(parameters.get("suppressBillingIndicator"), "false") ? "N" : "Y");
					jsonList.add(Objects.equals(parameters.get("nationalDrugCode"), null) ? null : (parameters.get("nationalDrugCode").toString()));
					jsonList.add(Objects.equals(parameters.get("locationId"), null) ? null : (parameters.get("locationId").toString()));	
					jsonList.add(Objects.equals(parameters.get("providerId"), null) ? null : (parameters.get("providerId").toString()));	
					jsonList.add(Objects.equals(parameters.get("tooth"), null) ? null : (parameters.get("tooth").toString()));	
					jsonList.add(Objects.equals(parameters.get("surface"), null) ? null : (parameters.get("surface").toString()));	
					jsonList.add(Objects.equals(parameters.get("quadrant"), null) ? null : (parameters.get("quadrant").toString()));
					jsonList.add(Objects.equals(parameters.get("diagnosisCodeId1"), null) ? null : (parameters.get("diagnosisCodeId1").toString()));	
					jsonList.add(Objects.equals(parameters.get("diagnosisCodeId2"), null) ? null : (parameters.get("diagnosisCodeId2").toString()));	
					jsonList.add(Objects.equals(parameters.get("diagnosisCodeId3"), null) ? null : (parameters.get("diagnosisCodeId3").toString()));	
					jsonList.add(Objects.equals(parameters.get("diagnosisCodeId4"), null) ? null : (parameters.get("diagnosisCodeId4").toString()));	
					jsonList.add(Objects.equals(parameters.get("diagnosisCodeId5"), null) ? null : (parameters.get("diagnosisCodeId5").toString()));	
					jsonList.add(Objects.equals(parameters.get("diagnosisCodeId6"), null) ? null : (parameters.get("diagnosisCodeId6").toString()));	
					jsonList.add(Objects.equals(parameters.get("diagnosisCodeId7"), null) ? null : (parameters.get("diagnosisCodeId7").toString()));	
					jsonList.add(Objects.equals(parameters.get("diagnosisCodeId8"), null) ? null : (parameters.get("diagnosisCodeId8").toString()));	
					jsonList.add(Objects.equals(parameters.get("diagnosisCodeId9"), null) ? null : (parameters.get("diagnosisCodeId9").toString()));	
					jsonList.add(Objects.equals(parameters.get("diagnosisCodeId10"), null) ? null : (parameters.get("diagnosisCodeId10").toString()));	
					jsonList.add(Objects.equals(parameters.get("diagnosisCodeId11"), null) ? null : (parameters.get("diagnosisCodeId11").toString()));	
					jsonList.add(Objects.equals(parameters.get("diagnosisCodeId12"), null) ? null : (parameters.get("diagnosisCodeId12").toString()));	
					jsonList.add(Objects.equals(parameters.get("modifierId1"), null) ? null : (parameters.get("modifierId1").toString()));	
					jsonList.add(Objects.equals(parameters.get("modifierId2"), null) ? null : (parameters.get("modifierId2").toString()));	
					jsonList.add(Objects.equals(parameters.get("modifierId3"), null) ? null : (parameters.get("modifierId3").toString()));
					jsonList.add(Objects.equals(parameters.get("modifierId4"), null) ? null : (parameters.get("modifierId4").toString()));	
					jsonList.add(Objects.equals(parameters.get("note"), null) ? null : (parameters.get("note").toString()));	

	
					status = ValidateResults.resultsvalidation(jsonList, DatabaseConnection.statementExecution(BaseTest.connNGA, strSQLQuery));
					//status = ValidateResults.resultValidation(jsonList, listDatabase);
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
