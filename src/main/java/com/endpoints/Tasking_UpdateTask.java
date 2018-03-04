package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Tasking_UpdateTask 
{

	public static Boolean tasking_UpdateTask_Test(HashMap<String, String> parameters, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strSQLQuery = "select datediff(second,create_timestamp,modify_timestamp) as diff,modified_by as mb  "
					+ "from user_todo_list "
					+ "WHERE task_id = '"+parameters.get("taskId")+"'" ;
					
			
			Object objIntDateDiff = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "diff");

			Object objIntModifiedBy = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "mb");
			if(Integer.valueOf(objIntDateDiff.toString()) > 0)
			{
				if(Objects.equals(objIntModifiedBy.toString(), System.getProperty("LoggedInUserId")))
				{
					String strQuery = "SELECT U.task_id AS id, U.enterprise_id AS EnterpriseId, U.practice_id AS PracticeId, "
							+ " CASE U.task_priority WHEN 1 THEN 'High' WHEN 2 THEN 'Normal' WHEN 3 THEN 'Low' "
							+ "END AS Priority, U.task_due_date AS DueDate, U.task_desc Description, "
							+ "CASE U.read_flag WHEN 'Y' THEN 'true' WHEN 'N' THEN 'false' ELSE 'false' END AS IsRead, CASE U.task_completed WHEN 1 "
							+ "THEN 'true' WHEN 0 THEN 'false' END AS IsCompleted, CASE U.task_assgn WHEN ' ' THEN 'false' ELSE 'true' END AS IsAssigned, "
							+ "CASE U.rejected_ind WHEN 'Y' THEN 'true' WHEN 'N' THEN 'false' ELSE 'false' END AS IsRejected,  "
							+ "u.create_timestamp as createTimestamp, u.modified_by as modifiedBy, "
							+ "CASE WHEN (U.rejected_ind = 'Y') THEN 'Rejected' WHEN (U.task_assgn = '' AND U.task_completed = '0') THEN 'Pending' "
							+ "WHEN (U.task_completed = '1') THEN 'Completed' WHEN (U.task_completed = '0' AND (U.task_assgn = 'G' OR U.task_assgn = 'Y')) "
							+ "THEN 'Assigned' WHEN (U.task_completed = '0' AND U.task_assgn = 'A') THEN 'Accepted' END AS Status "
							+ " FROM (user_todo_list U WITH (NOLOCK) "
							+ "LEFT OUTER JOIN person P WITH (NOLOCK) ON U.pat_acct_id = P.person_id) LEFT OUTER JOIN patient PA WITH (NOLOCK) ON (PA.person_id = P.person_id "
							+ "AND PA.enterprise_id = '"+System.getProperty("LoggedInEnterpriseId")+"' AND PA.practice_id = '"+System.getProperty("LoggedInPracticeId")+"')  WHERE (user_id = "+System.getProperty("LoggedInUserId")+") and U.task_id = '"+parameters.get("taskId")+"'";
					ArrayList<Object> listTestData  = new ArrayList<>();
					
					listTestData.add(Objects.equals(parameters.get("taskId"), "") ? null: parameters.get("taskId").toString().toUpperCase());
					listTestData.add(Objects.equals(parameters.get("enterpriseId"), "") ? null: parameters.get("enterpriseId"));
					listTestData.add(Objects.equals(parameters.get("practiceId"), "") ? null: parameters.get("practiceId"));
					listTestData.add(Objects.equals(parameters.get("priority"), "") ? null: parameters.get("priority"));
					listTestData.add(Objects.equals(parameters.get("dueDate"), "") ? null: parameters.get("dueDate").toString().replace("T", " "));
					// check this > listTestData.add(Objects.equals(parameters.get("subject"), "") ? null: parameters.get("subject"));
					listTestData.add(Objects.equals(parameters.get("description"), "") ? null: parameters.get("description"));
					listTestData.add(Objects.equals(parameters.get("isRead"), "") ? null: parameters.get("isRead").toString().toLowerCase());
					listTestData.add(Objects.equals(parameters.get("isCompleted"), "") ? null: parameters.get("isCompleted").toString().toLowerCase());
					listTestData.add(Objects.equals(parameters.get("isAssigned"), "") ? null: parameters.get("isAssigned").toString().toLowerCase());
					listTestData.add(Objects.equals(parameters.get("isRejected"), "") ? null: parameters.get("isRejected").toString().toLowerCase());
					listTestData.add(Objects.equals(parameters.get("createTimestamp"), "") ? null: parameters.get("createTimestamp").toString().replace("T", " "));
					listTestData.add(Objects.equals(parameters.get("modifiedBy"), "") ? null: parameters.get("modifiedBy"));
					listTestData.add(Objects.equals(parameters.get("status"), "") ? null: parameters.get("status"));
					
					
				    Log.info("Parameters:"+parameters);
					Log.info("TestData:\n"+listTestData);
					status = ValidateResults.resultsvalidation(listTestData, DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery));
				}
				else
				{
					Log.error("tasks with taskId="+parameters.get("taskId")+", was not updated correctly-Elements are not updated with logged in user's credentials..");
					Log.error("Expected:"+System.getProperty("LoggedInUserId")+", Actual:"+objIntModifiedBy.toString());
					softAssert.fail("Person with Person_id="+parameters.get("personId")+", Elements are not updated with logged in user's credentials..");
				}
			}
			else{
				Log.error("Person with Person_id="+parameters.get("personId")+", modify_timestamp is not updated with latest timesatmp..");
				softAssert.fail("Request with request_id="+parameters.get("requestId")+", modify_timestamp is not updated with latest timesatmp..");
			}
		

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}



}
