package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Tasking_GetAssignees
{
	public static Boolean tasking_GetAssignees_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{

				String strQuery = "SELECT [Distinct2].[C3] AS userId, [Distinct2].[C2] AS taskId, [Distinct2].[C5] AS firstName, [Distinct2].[C6] AS middleName, "
						+ "[Distinct2].[C7] AS lastName, [Distinct2].[C8] AS response, [Distinct2].[C9] AS responseDate, case when [c4] IS NULL then 'true' else 'false' end as isAssignedIndividual, "
						+ "[Distinct2].[C4] AS groupIds FROM ( SELECT DISTINCT [UnionAll2].[C1] AS [C1], [UnionAll2].[task_id] AS [C2], [UnionAll2].[assigned_to] AS [C3], [UnionAll2].[C2] AS [C4], "
						+ "[UnionAll2].[first_name] AS [C5], [UnionAll2].[mi] AS [C6], [UnionAll2].[last_name] AS [C7], [UnionAll2].[responce_flag] AS [C8], "
						+ "[UnionAll2].[responce_date] AS [C9] FROM (SELECT 1 AS [C1], [tid].[task_id] AS [task_id], [Ato].[assigned_to] AS [assigned_to], "
						+ "CAST(NULL AS int) AS [C2], [name].[first_name] AS [first_name], [name].[mi] AS [mi], [name].[last_name] AS [last_name], "
						+ "[Ato].[responce_flag] AS [responce_flag], [Ato].[responce_date] AS [responce_date] FROM [dbo].[user_todo_list] AS [tid] "
						+ "INNER JOIN [dbo].[todo_assign] AS [Ato] ON [tid].[task_id] = [Ato].[task_id] INNER JOIN [dbo].[user_mstr] AS [name] "
						+ "ON [Ato].[assigned_to] = [name].[user_id] UNION ALL SELECT 1 AS [C1], [Distinct1].[C1] AS [C2], [Distinct1].[C3] AS [C3], [Distinct1].[C2] AS [C4],"
						+ " [Distinct1].[C4] AS [C5], [Distinct1].[C5] AS [C6], [Distinct1].[C6] AS [C7], [Distinct1].[C7] AS [C8], [Distinct1].[C8] AS [C9] "
						+ "FROM ( SELECT DISTINCT [UnionAll1].[task_id] AS [C1], [UnionAll1].[group_id] AS [C2], [UnionAll1].[user_id] AS [C3], [UnionAll1].[first_name] "
						+ "AS [C4], [UnionAll1].[mi] AS [C5], [UnionAll1].[last_name] AS [C6], [UnionAll1].[response] AS [C7], [UnionAll1].[C1] AS [C8] "
						+ "FROM (SELECT [tid1].[task_id] AS [task_id], [gid].[group_id] AS [group_id], [tid1].[user_id] AS [user_id], [name2].[first_name] AS [first_name], "
						+ "[name2].[mi] AS [mi], [name2].[last_name] AS [last_name], [tid1].[response] AS [response], CAST( [tid1].[create_timestamp] "
						+ "AS datetime2) AS [C1] FROM [dbo].[user_response] AS [tid1] INNER JOIN [dbo].[user_todo_list] AS [tid3] ON [tid1].[task_id] = [tid3].[task_id] "
						+ "INNER JOIN [dbo].[user_mstr] AS [name2] ON [tid1].[user_id] = [name2].[user_id] INNER JOIN [dbo].[group_assign] AS [gid] ON ([tid1].[task_id] = [gid].[task_id]) "
						+ "AND (([tid1].[response] = [gid].[response]) OR (([tid1].[response] IS NULL) AND ([gid].[response] IS NULL))) "
						+ "INNER JOIN [dbo].[task_workgroup_mstr] AS [wid] ON [gid].[group_id] = [wid].[workgroup_id] WHERE (N'A' = [tid1].[response]) "
						+ "AND ( NOT EXISTS (SELECT 1 AS [C1] FROM [dbo].[user_todo_list] AS [tid2] INNER JOIN [dbo].[group_assign] AS [gid3] "
						+ "ON [tid2].[task_id] = [gid3].[task_id] INNER JOIN [dbo].[task_workgroup_mstr] AS [wId2] ON [gid3].[group_id] = [wId2].[workgroup_id] "
						+ "INNER JOIN [dbo].[task_workgroup_user_xref] AS [wId3] ON [wId2].[workgroup_id] = [wId3].[workgroup_id] WHERE ([tid2].[task_id] = [tid1].[task_id]) "
						+ "AND ([gid3].[group_id] = [gid].[group_id]) AND ([wId3].[user_id] = [tid1].[user_id]) )) UNION ALL SELECT [task1].[task_id] AS [task_id], "
						+ "[group].[group_id] AS [group_id], [Uid].[user_id] AS [user_id], [name4].[first_name] AS [first_name], [name4].[mi] AS [mi], [name4].[last_name] "
						+ "AS [last_name], [res].[response] AS [response], CAST( [res].[create_timestamp] AS datetime2) AS [C1] FROM [dbo].[user_todo_list] AS [task1] "
						+ "INNER JOIN [dbo].[group_assign] AS [group] ON [task1].[task_id] = [group].[task_id] INNER JOIN [dbo].[task_workgroup_mstr] AS [workId] "
						+ "ON [group].[group_id] = [workId].[workgroup_id] INNER JOIN [dbo].[task_workgroup_user_xref] AS [Uid] ON [workId].[workgroup_id] = [Uid].[workgroup_id] "
						+ "INNER JOIN [dbo].[user_mstr] AS [name4] ON [Uid].[user_id] = [name4].[user_id] LEFT OUTER JOIN [dbo].[user_response] AS [res] "
						+ "ON ([task1].[task_id] = [res].[task_id]) AND ([Uid].[user_id] = [res].[user_id])) AS [UnionAll1] ) AS [Distinct1] WHERE [Distinct1].[C3] <> '"+parameters.get("userId")+"') "
						+ "AS [UnionAll2] ) AS [Distinct2] WHERE [Distinct2].[C2] ='"+parameters.get("taskId")+"'";
				
						
				String[] jsonMetaData = {"userId", "taskId", "firstName", "middleName", "lastName", "response", "responseDate", "isAssignedAsIndividual", "groupIds" };

				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$.."+strUniqueJsonToken, jsonList);
				} 
				catch (Exception e)
				{
					e.printStackTrace();
					Log.info("Error in Test Class and the detail are : \n" + e.getMessage());
					softAssert.fail("Error in Test Script please look at logs");
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