
package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import org.testng.asserts.SoftAssert;
import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_AddPatCareplanOutDetails
{
	public static Boolean Chart_AddPatientCareplanOutcomeDetails_Test(HashMap<String, String> parameters,String strResponse, HashMap<String, Object> mapPostDetails, SoftAssert softAssert, String strQuery)
	{
		Boolean status=false;
		try
		{
			int countAfterPOST = DatabaseConnection.returnNumberOfRows(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery));
			if(countAfterPOST - ((int)mapPostDetails.get("countBeforePOST")) == 1)
			{
				String strQuery1 = "select "
						+ "outcome,goal_achieved,goal_achieved_details,outcome_date "
                        + "From care_plan_outcomes_ext_ " 
                        + "Where person_id = '"+parameters.get("personId")+"' and enc_id ='"+parameters.get("encounterId")+"' and health_concern_num = '"+parameters.get("healthConcernId")+"' and goal_num = '"+parameters.get("goalId")+"' and  intervention_num = '"+parameters.get("interventionId")+"'";
				
				ArrayList<Object> jsonList = new ArrayList<Object>();
				
                jsonList.add(parameters.get("outcomepayload").toString());
                jsonList.add(parameters.get("goalAchieved").toString());
                jsonList.add(parameters.get("goalAchievedDetails").toString().toUpperCase());
                jsonList.add(parameters.get("outcomeDate").toString());
                
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