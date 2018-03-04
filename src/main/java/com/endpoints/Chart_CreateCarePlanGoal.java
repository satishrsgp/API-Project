package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_CreateCarePlanGoal {


	
	public static Boolean Chart_CreateCarePlanGoal_Test(HashMap<String, String> parameters, String strResponse,HashMap<String, Object> mapPostDetails, SoftAssert softAssert, String strQuery)
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
							+ "code_description as codeDescription, "
							+ "code_system as codeSystem, "
							+ "code_value as codeValue,"
							+ "comments as comments,"
							+ "goal as description,"
							+ "case when goal_achieved = 'No' then 'false' when goal_achieved = 'Yes' then 'true' else goal_achieved end as isGoalAchieved,"
							+ "goal_achieved_details as goalAchievedDetails,"
							+ "goal_completion_date as goalCompletionDate,"
							+ "goal_start_date as goalStartDate,"
							+ "case when patient_goal_ind = '0' then 'false' else 'true' end as isPatientGoal,"
							+ "case when patient_priority = 'High priority' then 'HighPriority' when  patient_priority = 'Delayed priority' then 'Delayedpriority'  when  patient_priority = 'Normal priority' then 'NormalPriority'end as patientPriority,"
							+ "case when provider_goal_ind = '0' then 'false' else 'true' end as isProviderGoal,"
							+ "case when provider_priority = 'High priority' then 'HighPriority' when  patient_priority = 'Delayed priority' then 'Delayedpriority'  when  patient_priority = 'Normal priority' then 'NormalPriority'end as providerPriority"
							+ " from care_plan_goals_ext_   "
							+ "where person_id = '"+parameters.get("personId")+"' "
							+ "And encounterID ='"+parameters.get("encounterId")+"' "
							+ "And health_concern_num = '"+parameters.get("healthConcernId")+"'  "
							+ "order by create_timestamp desc";
					
					ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strSQLQuery));
					
					ArrayList<Object> jsonList = new ArrayList<Object>();
					jsonList.add(Objects.equals(parameters.get("codeDescription"), "null") ? "" : (parameters.get("codeDescription").toString()));
					jsonList.add(Objects.equals(parameters.get("codeSystem"), "null") ? null : (parameters.get("codeSystem").toString()));
					jsonList.add(Objects.equals(parameters.get("codeValue"), "null") ? null : (parameters.get("codeValue").toString()));
					jsonList.add(Objects.equals(parameters.get("comments"), "") ? null : (parameters.get("comments").toString()));	
					jsonList.add(Objects.equals(parameters.get("description"), "") ? null : (parameters.get("description").toString()));	
					jsonList.add(Objects.equals(parameters.get("isGoalAchieved"), "") ? null : (parameters.get("isGoalAchieved").toString()));
					jsonList.add(Objects.equals(parameters.get("goalAchievedDetails"), "null") ? null : (parameters.get("goalAchievedDetails").toString()));		
					jsonList.add(Objects.equals(parameters.get("goalCompletionDate"), null) ? null : (parameters.get("goalCompletionDate").toString().split("T")[0]).replace("-", ""));
					jsonList.add(Objects.equals(parameters.get("goalStartDate"), null) ? null : (parameters.get("goalStartDate").toString().split("T")[0]).replace("-", ""));			
					jsonList.add(Objects.equals(parameters.get("isPatientGoal"), "") ? null : (parameters.get("isPatientGoal").toString()));	
					jsonList.add(Objects.equals(parameters.get("patientPriority"), "") ? null : (parameters.get("patientPriority").toString()));	
					jsonList.add(Objects.equals(parameters.get("isProviderGoal"), "") ? null : (parameters.get("isProviderGoal").toString()));	
					jsonList.add(Objects.equals(parameters.get("providerPriority"), "") ? null : (parameters.get("providerPriority").toString()));	
						
			
					
					status = ValidateResults.resultValidation(jsonList, listDatabase);
					//status = ValidateResults.resultsvalidation(jsonList, DatabaseConnection.statementExecution(BaseTest.connNGA, strSQLQuery));
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
