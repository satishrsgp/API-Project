package com.endpoints;



import java.util.ArrayList;
import java.util.HashMap;

import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_AddDiagnosis {
	public static Boolean chart_AddDiagnosis_Test(HashMap<String, String> parameters, String strResponse,HashMap<String, Object> mapPostDetails, SoftAssert softAssert, String strQuery)
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
				
							+ " icd9cm_code_id as IcdCode, description as DiagnosisDescription "
							+ " from patient_diagnosis  "
							+ "  WHERE person_id = '"+parameters.get("personId")+"' and  enc_id= '"+parameters.get("encounterId")+"' order by create_timestamp desc";
					
					//ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strSQLQuery));

					ArrayList<Object> jsonList = new ArrayList<Object>();
					
					jsonList.add(Objects.equals(parameters.get("IcdCode"), null) ? null : (parameters.get("IcdCode").toString()));	
				//	jsonList.add(Objects.equals(parameters.get("DiagnosisDescription"), null) ? null : (parameters.get("serviceDate").toString().split("T")[0]).replace("-", ""));
					jsonList.add(Objects.equals(parameters.get("DiagnosisDescription"), "null") ? "" : (parameters.get("DiagnosisDescription").toString()));	
				

	
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
