package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.ExcelReaderWriter;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_AddPatient {

	public static Boolean Chart_AddPatient_Test(HashMap<String, String> parameters, String strResponse,
			HashMap<String, Object> mapPostDetails, SoftAssert softAssert, String strQuery) {
		Boolean status = false;

		try {
			int countAfterPOST = DatabaseConnection
					.returnNumberOfRows(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery));
			if (Math.abs(countAfterPOST - ((int) mapPostDetails.get("countBeforePOST"))) == 1) {
				try {

					String strSQLQuery = "SELECT FORMAT(CAST(first_office_enc_date AS datetime),'yyyy-MM-dd'), FORMAT(CAST(last_office_enc_date AS datetime),'yyyy-MM-dd'), FORMAT(CAST(next_office_enc_date AS datetime),'yyyy-MM-dd'), pharmacy_code_1_id, pharmacy_code_2_id, default_location_id, privacy_level, CASE WHEN suppress_billing_ind = 'Y' THEN 'true' ELSE 'false' END , preferred_provider_id, financial_class, med_rec_nbr, guar_id, guar_type, contact_person_id, user_defined1, user_defined2, user_defined3, user_defined4, CASE WHEN print_stmt_ind = 'Y' THEN 'true' ELSE 'false' END , FORMAT(CAST(last_letter_date AS datetime),'yyyy-MM-dd'), rendering_prov_id, CASE WHEN co_managed_ind = 'Y' THEN 'true' ELSE 'false' END , co_managed_prov_id, mrkt_plan_id, mrkt_source_id, CAST(mrkt_source_type AS VARCHAR), mrkt_comments, mrkt_details, referring_prov_id, FORMAT(CAST(last_print_statement_date AS datetime),'yyyy-MM-dd'), privacy_notice_code_id, FORMAT(CAST(privacy_notice_issued AS datetime),'yyyy-MM-dd'), FORMAT(CAST(privacy_notice_received AS datetime),'yyyy-MM-dd'), privacy_notice_notes, user_defined5, user_defined6, user_defined7, user_defined8, head_of_household_id, CASE WHEN no_unresolved_allergies_ind = 'Y' THEN 'true' ELSE 'false' END , CASE WHEN no_active_medications_ind = 'Y' THEN 'true' ELSE 'false' END , CASE WHEN no_active_problems = 'Y' THEN 'true' ELSE 'false' END FROM patient WHERE person_id='"+parameters.get("personId")+"'";

					ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(
							DatabaseConnection.statementExecution(BaseTest.connNGA, strSQLQuery));
					System.out.println("listDatabase size :" + listDatabase.size());
					ArrayList<Object> jsonList = new ArrayList<Object>();

					for (int i = 1; i < ExcelReaderWriter.getColumnCount(
							BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), "Chart_AddPatient") - 2; i++) {
						String key = ExcelReaderWriter.getCellValue(
								BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), "Chart_AddPatient", 0, i);
						
						
						if(key.equals("DefaultLocationId") || key.equals("PreferredProviderId") )
						{
							
							
							if (parameters.get(key) == "null")
								jsonList.add("00000000-0000-0000-0000-000000000000");
							else
								jsonList.add(parameters.get(key));
							continue;
						}
							
					if (parameters.get(key) == "null")
							jsonList.add(null);
						else
							jsonList.add(parameters.get(key));

					}

					status = ValidateResults.resultValidation(jsonList, listDatabase);
					// status = ValidateResults.resultsvalidation(jsonList,
					// DatabaseConnection.statementExecution(BaseTest.connNGA, strSQLQuery));
				} catch (Exception e) {
					e.printStackTrace();
					Log.info("Error in Test Class and the detail are : \n" + e.getMessage());
					softAssert.fail("Error in Test Script please look at logs");
				}

			} else {
				Log.info(
						"The Difference between records count before and after post is more than one, please reexcute once more");
				softAssert.fail(
						"The Difference between records count before and after post is more than one. Please reexecute once more");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

}
