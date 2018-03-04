package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_UpdateCareplanGoal 
{

	public static Boolean Chart_UpdateCareplanGoal_Test(HashMap<String, String> parameters, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strSQLQuery = "select datediff(second,create_timestamp,modify_timestamp) as diff,modified_by as mb  "
					+ "from care_plan_goals_ext_ "
					+ "where person_id = '"+parameters.get("personId")+"'"
					+ " AND encounterID = '"+parameters.get("encounterId")+"'"
					+ " AND health_concern_num  = '"+parameters.get("healthConcernId")+"'"
					+ " AND goal_num = '"+parameters.get("goalId")+"'"
					+ " AND del_ind = 'N'";
					
			Object objIntDateDiff = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "diff");
			Object objIntModifiedBy = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "mb");
			if(Integer.valueOf(objIntDateDiff.toString()) > 0)
			{
				if(Objects.equals(objIntModifiedBy.toString(), System.getProperty("LoggedInUserId")))
				{
					String strQuery1 = "select "
							+ "code_description as codeDescription, "
							+ "code_system as codeSystem, "
							+ "code_value as codeValue, " 
							+ "comments as comments, "
							+ "goal as description, "
							+ "case when goal_achieved = 'No' then 'false' when goal_achieved = 'Yes' then 'true' else goal_achieved end as isGoalAchieved, "
							+ "goal_achieved_details as goalAchievedDetails, "
							+ "goal_completion_date as goalCompletionDate, "
							+ "goal_start_date as goalStartDate, "
							+ "case when patient_goal_ind = '0' then 'false' else 'true' end as isPatientGoal, "
							+ "case when patient_priority = 'High priority' then 'HighPriority' when  patient_priority = 'Delayed priority' then 'DelayedPriority'  when  patient_priority = 'Normal priority' then 'NormalPriority'end as patientPriority, "
							+ "case when provider_goal_ind = '1' then 'true' else 'false' end as isProviderGoal, "
							+ "case when provider_priority = 'High priority' then 'HighPriority' when  patient_priority = 'Delayed priority' then 'DelayedPriority'  when  patient_priority = 'Normal priority' then 'NormalPriority'end as providerPriority  "
							+ "from care_plan_goals_ext_ "
							+ "where person_id = '"+parameters.get("personId")+"'"
							+ " AND encounterID = '"+parameters.get("encounterId")+"'"
							+ " AND health_concern_num  = '"+parameters.get("healthConcernId")+"'"
							+ " AND goal_num = '"+parameters.get("goalId")+"'"
							+ " AND del_ind = 'N'";
					//ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
					ArrayList<Object> listTestData  = new ArrayList<>();
					listTestData.add(Objects.equals(parameters.get("codeDescription"), "null") ? null: parameters.get("codeDescription").toString());
					listTestData.add(Objects.equals(parameters.get("codeSystem"), "null") ? null : (parameters.get("codeSystem").toString()));
					listTestData.add(Objects.equals(parameters.get("codeValue"), "null") ? null: parameters.get("codeValue").toString());
					listTestData.add(Objects.equals(parameters.get("comments"), "") ? null: parameters.get("comments"));
					listTestData.add(Objects.equals(parameters.get("description"), "") ? null: parameters.get("description"));
					listTestData.add(Objects.equals(parameters.get("isGoalAchieved"), "") ? null: parameters.get("isGoalAchieved"));
					listTestData.add(Objects.equals(parameters.get("goalAchievedDetails"), "") ? null: parameters.get("goalAchievedDetails"));
					listTestData.add(Objects.equals(parameters.get("goalCompletionDate"), "") ? null : (parameters.get("goalCompletionDate").toString().substring(0, 10).replace("T", "").replace("-", "")));
					listTestData.add(Objects.equals(parameters.get("goalStartDate"), "") ? null : (parameters.get("goalStartDate").toString().substring(0, 10).replace("T", "").replace("-", "")));
					listTestData.add(Objects.equals(parameters.get("isPatientGoal"), "") ? null: parameters.get("isPatientGoal").toString());
					listTestData.add(Objects.equals(parameters.get("patientPriority"), "") ? null : (parameters.get("patientPriority")));
					listTestData.add(Objects.equals(parameters.get("isProviderGoal"), "") ? null : (parameters.get("isProviderGoal")));
					listTestData.add(Objects.equals(parameters.get("providerPriority"), "") ? null: parameters.get("providerPriority"));
					Log.info("Parameters:"+parameters);
					Log.info("TestData:\n"+listTestData);
					//Log.info("DBData:\n"+listDatabase);
					status = ValidateResults.resultsvalidation(listTestData, DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
				}
				else
				{
					Log.error("Gaol with Goal ID ="+parameters.get("goalId")+" for a personID ="+parameters.get("personId")+" , was not updated correctly-modified_by is not updated with logged in user's credentials.");
					Log.error("Expected:"+System.getProperty("LoggedInUserId")+", Actual:"+objIntModifiedBy.toString());
					softAssert.fail("Gaol with Goal ID ="+parameters.get("goalId")+" for a personID ="+parameters.get("personId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				}
			}
			else
			{
				Log.error("Gaol with Goal ID ="+parameters.get("goalId")+" for a personID ="+parameters.get("personId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				softAssert.fail("Gaol with Goal ID ="+parameters.get("goalId")+" for a personID ="+parameters.get("personId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}



}
