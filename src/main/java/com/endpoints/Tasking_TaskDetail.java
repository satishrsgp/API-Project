package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Tasking_TaskDetail 
{

	public static Boolean tasking_TaskDetail_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "SELECT U.task_id AS id, U.enterprise_id AS EnterpriseId, U.practice_id AS PracticeId, U.user_id AS UserId, "
						+ "U.task_priority as priorityId, CASE U.task_priority WHEN 1 THEN 'High' WHEN 2 THEN 'Normal' WHEN 3 THEN 'Low' "
						+ "END AS Priority, U.task_due_date AS DueDate, U.task_subj AS Subject, U.task_desc Description, U.task_owner AS OwnerId, "
						+ "CASE U.read_flag WHEN 'Y' THEN 'true' WHEN 'N' THEN 'false' ELSE 'false' END AS IsRead, CASE U.task_completed WHEN 1 "
						+ "THEN 'true' WHEN 0 THEN 'false' END AS IsCompleted, CASE U.task_assgn WHEN ' ' THEN 'false' ELSE 'true' END AS IsAssigned, "
						+ "CASE U.rejected_ind WHEN 'Y' THEN 'true' WHEN 'N' THEN 'false' ELSE 'false' END AS IsRejected, U.pat_acct_id AS personId, "
						+ "p.person_nbr as personNumber, P.first_name AS personFirstName, P.middle_name AS personMiddleName, P.last_name AS personLastName, "
						+ "p.sex as sex, p.date_of_birth as dateOfBirth,  FLOOR((DATEDIFF(day,date_of_birth,getdate ())) / 365.25) AS Age,U.pat_enc_id AS encounterId, u.pat_item_id as itemId, "
						+ "u.pat_item_type as itemType, u.pat_item_desc as itemDescription,u.created_by as createdBy, "
						+ "u.create_timestamp as createTimestamp, u.modified_by as modifiedBy, u.modify_timestamp as modifyTimestamp, "
						+ "CASE WHEN (U.rejected_ind = 'Y') THEN 'Rejected' WHEN (U.task_assgn = '' AND U.task_completed = '0') THEN 'Pending' "
						+ "WHEN (U.task_completed = '1') THEN 'Completed' WHEN (U.task_completed = '0' AND (U.task_assgn = 'G' OR U.task_assgn = 'Y')) "
						+ "THEN 'Assigned' WHEN (U.task_completed = '0' AND U.task_assgn = 'A') THEN 'Accepted' END AS Status "
						+ " FROM (user_todo_list U WITH (NOLOCK) "
						+ "LEFT OUTER JOIN person P WITH (NOLOCK) ON U.pat_acct_id = P.person_id) LEFT OUTER JOIN patient PA WITH (NOLOCK) ON (PA.person_id = P.person_id "
						+ "AND PA.enterprise_id = '"+System.getProperty("LoggedInEnterpriseId")+"' AND PA.practice_id = '"+System.getProperty("LoggedInPracticeId")+"')  WHERE (user_id = "+System.getProperty("LoggedInUserId")+") and U.task_id = '"+parameters.get("taskId")+"'";
				
				
				String[] jsonMetaData = {"id","enterpriseId","practiceId","userId","priority","dueDate;Date_YYYYMMDD HH:MM:SS",
				"description","ownerId","isCompleted","personId","personNumber","personFirstName",
				"personMiddleName","personLastName","sex","dateOfBirth","age","encounterId","itemId","itemType","itemDescription","createdBy","createTimestamp;Date_YYYYMMDD HH:MM:SS"
				,"modifiedBy","modifyTimestamp;Date_YYYYMMDD HH:MM:SS","status"};
				
				
				
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..id", jsonList);
				} 
				catch (Exception e)
				{
					e.printStackTrace();
					Log.info("Error in Test Class and the detail are : \n" + e.getMessage());
					softAssert.fail("Error in Test Script please look at logs");
					//objHandler.setStrRequestStatus("Fail");
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			status = true;
		}
		return status;
	}
}
