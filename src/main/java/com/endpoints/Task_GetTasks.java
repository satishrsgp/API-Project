package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Task_GetTasks
{

	public static Boolean Task_GetTasks_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			String strQuery = "SELECT TOP 25 * FROM (SELECT U.task_id AS UniqueId,U.enterprise_id AS EnterpriseId,U.practice_id AS PracticeId,U.user_id AS UserId, "
					+"U.task_priority AS PriorityId,CASE U.task_priority WHEN 1 THEN 'High' WHEN 2 THEN 'Normal' WHEN 3 THEN 'Low' END AS Priority, "
					+"U.task_subj AS Subject,U.task_desc AS Description,"
					+"CASE WHEN (U.rejected_ind = 'Y') THEN 'Rejected' WHEN (U.task_assgn = '' AND U.task_completed = '0') THEN 'Pending' "
					+"WHEN (U.task_completed = '1') THEN 'Completed' WHEN (U.task_completed = '0' AND (U.task_assgn = 'G' OR U.task_assgn = 'Y')) THEN 'Assigned'"
					+"WHEN (U.task_completed = '0' AND U.task_assgn = 'A') THEN 'Accepted' END AS Status,"
					//+"U.todo_task_type AS TYPE," type is being returned as None always. Hence we are not going to evaluate the same.
					+"U.task_owner AS OwnerId,U.task_due_date AS DueDate, "
					+"CASE U.read_flag WHEN 'Y' THEN 'true' WHEN 'N' THEN 'false' ELSE 'false' END AS IsRead, "
					+"CASE U.task_assgn WHEN ' ' THEN 'false' ELSE 'true' END AS IsAssigned, "
					+"CASE U.rejected_ind WHEN 'Y' THEN 'true' WHEN 'N' THEN 'false' ELSE 'false' END AS IsRejected,"
					+"CASE U.task_completed WHEN 1 THEN 'true' WHEN 0 THEN 'false'  END AS IsCompleted, "
					+"CASE WHEN U.task_deleted IS NULL THEN 'false' WHEN U.task_deleted = 'Y' THEN 'true' ELSE 'false' END AS IsDeleted, "
					+"U.pat_acct_id AS AttachedPersonId,P.first_name AS PersonFirstName,P.middle_name AS PersonMiddleName, "
					+"P.last_name AS PersonLastName,U.pat_enc_id AS EncounterId,U.created_by AS CreatedBy, "
					+"U.create_timestamp as CreateTimestamp,U.modified_by as ModifiedBy,U.modify_timestamp as ModifyTimestamp "      
					+"FROM (user_todo_list U WITH (NOLOCK) "
					+"LEFT OUTER JOIN person P WITH (NOLOCK) ON U.pat_acct_id = P.person_id) "
					+"LEFT OUTER JOIN patient PA WITH (NOLOCK) ON (PA.person_id = P.person_id AND PA.enterprise_id = '"+System.getProperty("LoggedInEnterpriseId")+"' AND PA.practice_id = '"+System.getProperty("LoggedInPracticeId")+"') "
					+"WHERE (user_id = "+System.getProperty("LoggedInUserId")+") AND   (U.task_due_date >= '1753-01-01 00:00:00' AND U.task_due_date <= '9999-12-31 23:59:59') "
					+"UNION "
					+"SELECT U.task_id AS UniqueId,U.enterprise_id AS EnterpriseId,U.practice_id AS PracticeId,U.user_id AS UserId, "
					+"U.task_priority AS PriorityId,CASE U.task_priority WHEN 1 THEN 'High' WHEN 2 THEN 'Normal' WHEN 3 THEN 'Low' END AS Priority, "
					+"U.task_subj AS Subject,U.task_desc AS Description,"
					+"CASE WHEN (U.rejected_ind = 'Y') THEN 'Rejected' WHEN (U.task_assgn = '' AND U.task_completed = '0') THEN 'Pending' "
					+"WHEN (U.task_completed = '1') THEN 'Completed' WHEN (U.task_completed = '0' AND (U.task_assgn = 'G' OR U.task_assgn = 'Y')) THEN 'Assigned'"
					+"WHEN (U.task_completed = '0' AND U.task_assgn = 'A') THEN 'Accepted' END AS Status,"
					//+"U.todo_task_type AS TYPE,"
					+"U.task_owner AS OwnerId,U.task_due_date AS DueDate, "
					+"CASE U.read_flag WHEN 'Y' THEN 'true' WHEN 'N' THEN 'false' ELSE 'false' END AS IsRead, "
					+"CASE U.task_assgn WHEN ' ' THEN 'false' ELSE 'true' END AS IsAssigned, "
					+"CASE U.rejected_ind WHEN 'Y' THEN 'true' WHEN 'N' THEN 'false' ELSE 'false' END AS IsRejected,"
					+"CASE U.task_completed WHEN 1 THEN 'true' WHEN 0 THEN 'false'  END AS IsCompleted, "
					+"CASE WHEN U.task_deleted IS NULL THEN 'false' WHEN U.task_deleted = 'Y' THEN 'true' ELSE 'false' END AS IsDeleted, "
					+"U.pat_acct_id AS AttachedPersonId,P.first_name AS PersonFirstName,P.middle_name AS PersonMiddleName, "
					+"P.last_name AS PersonLastName,U.pat_enc_id AS EncounterId,U.created_by AS CreatedBy, "
					+"U.create_timestamp as CreateTimestamp,U.modified_by as ModifiedBy,U.modify_timestamp as ModifyTimestamp " 
					+"FROM (user_todo_list U WITH (NOLOCK) "
					+"LEFT OUTER JOIN person P WITH (NOLOCK) ON U.pat_acct_id = P.person_id) "
					+"LEFT OUTER JOIN patient PA WITH (NOLOCK) ON (PA.person_id = P.person_id AND PA.enterprise_id = '"+System.getProperty("LoggedInEnterpriseId")+"' AND PA.practice_id = '"+System.getProperty("LoggedInPracticeId")+"') "
					+"WHERE U.task_id IN (SELECT task_id FROM todo_assign WHERE assigned_to = "+System.getProperty("LoggedInUserId")+" AND   (responce_flag IS NULL OR responce_flag = 'A')) "
					+"AND  (U.task_due_date >= '1753-01-01 00:00:00' AND U.task_due_date <= '9999-12-31 23:59:59') "
					+"UNION "
					+"SELECT U.task_id AS UniqueId,U.enterprise_id AS EnterpriseId,U.practice_id AS PracticeId,U.user_id AS UserId, "
					+"U.task_priority AS PriorityId,CASE U.task_priority WHEN 1 THEN 'High' WHEN 2 THEN 'Normal' WHEN 3 THEN 'Low' END AS Priority, "
					+"U.task_subj AS Subject,U.task_desc AS Description,"
					+"CASE WHEN (U.rejected_ind = 'Y') THEN 'Rejected' WHEN (U.task_assgn = '' AND U.task_completed = '0') THEN 'Pending' "
					+"WHEN (U.task_completed = '1') THEN 'Completed' WHEN (U.task_completed = '0' AND (U.task_assgn = 'G' OR U.task_assgn = 'Y')) THEN 'Assigned'"
					+"WHEN (U.task_completed = '0' AND U.task_assgn = 'A') THEN 'Accepted' END AS Status, "
					//+"U.todo_task_type AS TYPE,"
					+"U.task_owner AS OwnerId,U.task_due_date AS DueDate, "
					+"CASE U.read_flag WHEN 'Y' THEN 'true' WHEN 'N' THEN 'false' ELSE 'false' END AS IsRead, "
					+"CASE U.task_assgn WHEN ' ' THEN 'false' ELSE 'true' END AS IsAssigned, "
					+"CASE U.rejected_ind WHEN 'Y' THEN 'true' WHEN 'N' THEN 'false' ELSE 'false' END AS IsRejected,"
					+"CASE U.task_completed WHEN 1 THEN 'true' WHEN 0 THEN 'false'  END AS IsCompleted, "
					+"CASE WHEN U.task_deleted IS NULL THEN 'false' WHEN U.task_deleted = 'Y' THEN 'true' ELSE 'false' END AS IsDeleted, "
					+"U.pat_acct_id AS AttachedPersonId,P.first_name AS PersonFirstName,P.middle_name AS PersonMiddleName, "
					+"P.last_name AS PersonLastName,U.pat_enc_id AS EncounterId,U.created_by AS CreatedBy, "
					+"U.create_timestamp as CreateTimestamp,U.modified_by as ModifiedBy,U.modify_timestamp as ModifyTimestamp " 
					+"FROM (user_todo_list U WITH (NOLOCK) "
					+"LEFT OUTER JOIN person P WITH (NOLOCK) ON U.pat_acct_id = P.person_id) "
					+"LEFT OUTER JOIN patient PA WITH (NOLOCK) ON (PA.person_id = P.person_id AND PA.enterprise_id = '"+System.getProperty("LoggedInEnterpriseId")+"' AND PA.practice_id = '"+System.getProperty("LoggedInPracticeId")+"') "
					+"WHERE U.task_id IN ((SELECT task_id FROM group_assign WHERE group_id IN (SELECT workgroup_id FROM task_workgroup_mstr "
					+"WHERE workgroup_id IN (SELECT workgroup_id FROM task_workgroup_user_xref WHERE user_id = "+System.getProperty("LoggedInUserId")+") AND   delete_ind = 'N')) "
					+"EXCEPT (SELECT task_id FROM user_response WHERE user_id = 429 AND   response = 'D') "
					+"EXCEPT (SELECT task_id FROM user_response WHERE user_id IN (SELECT DISTINCT user_id FROM task_workgroup_user_xref "
					+"WHERE workgroup_id IN (SELECT workgroup_id FROM task_workgroup_user_xref WHERE user_id = "+System.getProperty("LoggedInUserId")+")) AND   response = 'A' AND   user_id != "+System.getProperty("LoggedInUserId")+")) "
					+"AND   (U.task_due_date >= '1753-01-01 00:00:00' AND U.task_due_date <= '9999-12-31 23:59:59')) AS TEMPTABLE "
					+"ORDER BY DueDate DESC, IsCompleted ASC, Priority ASC, CreateTimestamp ASC, UniqueId ASC";

			String[] jsonMetaData = {"id;GUID","enterpriseId;Normal","practiceId;Normal","userId;Normal","priorityId;Normal","priority;Normal","subject;Normal","description;Normal","status;Normal","ownerId;Normal","dueDate;Date_YYYYMMDD HH:MM:SS","isRead;Boolean","isAssigned;Boolean","isRejected;Boolean","isCompleted;Boolean","isDeleted;Boolean","personId;GUID","personFirstName;Normal","personMiddleName;Normal","personLastName;Normal","encounterId;GUID","createdBy;Normal","createTimestamp;Date_YYYYMMDD HH:MM:SS","modifiedBy;Normal","modifyTimestamp;Date_YYYYMMDD HH:MM:SS"};

			try
			{
				ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
				Log.info("JSON LIST is :\n" +jsonList);
				status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..id", jsonList);
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
			status = true;
		}
		return status;
	}

}
