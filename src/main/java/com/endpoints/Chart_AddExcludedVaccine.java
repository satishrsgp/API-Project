package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_AddExcludedVaccine {


	
	public static Boolean Chart_AddExcludedVaccine_Test(HashMap<String, String> parameters, String strResponse,HashMap<String, Object> mapPostDetails, SoftAssert softAssert, String strQuery)
	{
		Boolean status=false;
		
		
		try
		{
			int countAfterPOST = DatabaseConnection.returnNumberOfRows(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery));
			if(Math.abs(countAfterPOST - ((int)mapPostDetails.get("countBeforePOST")))  == 1)
			{
				try
				{
					String strSQLQuery = "Select top 1"
							+ "cvx,reason,start_date,end_date,comment "
							+ "from imm_vaccine_exclusion "
							+ "where person_id = '"+parameters.get("personId")+"' order by create_timestamp desc";
					
					//ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strSQLQuery));
					
					ArrayList<Object> jsonList = new ArrayList<Object>();
					
					jsonList.add(parameters.get("CvxCode"));
					jsonList.add(parameters.get("Reasons"));
					jsonList.add(parameters.get("StartDate").toString().replace("T", " "));
					jsonList.add(parameters.get("EndDate").toString().replace("T", " "));
					jsonList.add(parameters.get("Comment"));
					
					//status = ValidateResults.resultValidation(jsonList, listDatabase);
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
