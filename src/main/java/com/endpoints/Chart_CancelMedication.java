package com.endpoints;




import java.util.ArrayList;
import java.util.HashMap;


import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;


public class Chart_CancelMedication {
	public static Boolean chart_CancelMedication_Test(HashMap<String, String> parameters, String strResponse,HashMap<String, Object> mapPostDetails, SoftAssert softAssert, String strQuery)
	{
		Boolean status=false;


		try
		{
			int countAfterPOST = DatabaseConnection.returnNumberOfRows(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery));
			if(Math.abs(countAfterPOST - ((int)mapPostDetails.get("countBeforePOST")))  == 1)
			{
				try
				{
					 
						String strSQLQuery = "select top 1 request_type, response_type  "
								+"from erx_message_history  "
								+"where person_id='"+parameters.get("personId")+"' and "
								+"medication_id = '"+parameters.get("medicationId")+"' and "
								+"request_type= '"+parameters.get("request_type")+"' and "
								+"response_type= '"+parameters.get("response_type")+"' "
								+"order by create_timestamp desc ";
						
						ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strSQLQuery));

						ArrayList<Object> jsonList = new ArrayList<Object>();
						
						
						//jsonList.add(Objects.equals(parameters.get("DiagnosisCodeLibraryId"), "") ? null : (parameters.get("DiagnosisCodeLibraryId").toString()));
						//jsonList.add(Objects.equals(parameters.get("DiagnosisCodeId"), null) ? null : (parameters.get("DiagnosisCodeId").toString()));	
						//jsonList.add(parameters.get("diagnosisCodeLibraryId").toString());
						//jsonList.add(Objects.equals(parameters.get("LastDate"), null) ? null : (parameters.get("LastDate").toString().split("T")[0]).replace("-", ""));
						//jsonList.add(Objects.equals(parameters.get("ReturnDate"), null) ? null : (parameters.get("ReturnDate").toString().split("T")[0]).replace("-", ""));
						jsonList.add(parameters.get("request_type").toString());
					jsonList.add(parameters.get("response_type").toString());
						//jsonList.add(parameters.get("LocationId").toString());
						//jsonList.add(parameters.get("diagnosisCodeId").toString());
					
						//jsonList.add(Objects.equals(parameters.get("fax"), "") ? null : (parameters.get("fax").toString().toUpperCase()));	
					//	jsonList.add(Objects.equals(parameters.get("homePhone"), "") ? null : (parameters.get("homePhone").toString().toUpperCase()));	
					
						
						
						//jsonList.add(parameters.get("zip").toString());
						//jsonList.add(Objects.equals(parameters.get("VerbalOrderIndicator"), "false") ? "N" : "Y");
						//jsonList.add(parameters.get("AllergiesReviewed").toString());
						//if (get.(AllergiesReviewed) =="egg")
				


						//status = ValidateResults.resultsvalidation(jsonList, DatabaseConnection.statementExecution(BaseTest.connNGA, strSQLQuery));
						status = ValidateResults.resultValidation(jsonList, listDatabase);
					
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
