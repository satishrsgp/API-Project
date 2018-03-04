/**
 * API 1.x route_Abhishek
 */

package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Tasking_GetAssigneeDetails {

	public static Boolean tasking_GetAssigneeDetails(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{

				String strQuery="SELECT [Distinct2].[C2] AS taskId, [Distinct2].[C3] AS userId, [Distinct2].[C5] AS firstName, [Distinct2].[C6] AS middleName, "
						+ "[Distinct2].[C7] AS lastName, [Distinct2].[C8] AS response, [Distinct2].[C9] AS responseDate, case when [c4] IS NULL then 'true' else 'false' end as isAssignedIndividual,"
						+ " [Distinct2].[C4] AS [C4] FROM ( SELECT DISTINCT [UnionAll2].[C1] AS [C1], [UnionAll2].[task_id] AS [C2], [UnionAll2].[assigned_to] AS [C3],"
						+ " [UnionAll2].[C2] AS [C4], [UnionAll2].[first_name] AS [C5], [UnionAll2].[mi] AS [C6], [UnionAll2].[last_name] AS [C7], "
						+ "[UnionAll2].[responce_flag] AS [C8], [UnionAll2].[responce_date] AS [C9] FROM (SELECT 1 AS [C1], [Extent1].[task_id] AS [task_id], [Extent2].[assigned_to] AS [assigned_to], "
						+ "CAST(NULL AS int) AS [C2], [Extent3].[first_name] AS [first_name], [Extent3].[mi] AS [mi], [Extent3].[last_name] AS [last_name], "
						+ "[Extent2].[responce_flag] AS [responce_flag], [Extent2].[responce_date] AS [responce_date] FROM [dbo].[user_todo_list] AS [Extent1] "
						+ "INNER JOIN [dbo].[todo_assign] AS [Extent2] ON [Extent1].[task_id] = [Extent2].[task_id] INNER JOIN [dbo].[user_mstr] AS [Extent3] ON [Extent2].[assigned_to] = [Extent3].[user_id]"
						+ " UNION ALL SELECT 1 AS [C1], [Distinct1].[C1] AS [C2], [Distinct1].[C3] AS [C3], [Distinct1].[C2] AS [C4], [Distinct1].[C4] AS [C5], [Distinct1].[C5] AS [C6], [Distinct1].[C6] AS [C7], "
						+ "[Distinct1].[C7] AS [C8], [Distinct1].[C8] AS [C9] FROM ( SELECT DISTINCT [UnionAll1].[task_id] AS [C1], [UnionAll1].[group_id] AS [C2], [UnionAll1].[user_id] AS [C3],"
						+ " [UnionAll1].[first_name] AS [C4], [UnionAll1].[mi] AS [C5], [UnionAll1].[last_name] AS [C6], [UnionAll1].[response] AS [C7], [UnionAll1].[C1] AS [C8] "
						+ "FROM (SELECT [Extent4].[task_id] AS [task_id], [Extent7].[group_id] AS [group_id], [Extent4].[user_id] AS [user_id], [Extent6].[first_name] AS [first_name], "
						+ "[Extent6].[mi] AS [mi], [Extent6].[last_name] AS [last_name], [Extent4].[response] AS [response], CAST( [Extent4].[create_timestamp] AS datetime2) AS [C1] "
						+ "FROM [dbo].[user_response] AS [Extent4] INNER JOIN [dbo].[user_todo_list] AS [Extent5] ON [Extent4].[task_id] = [Extent5].[task_id]"
						+ " INNER JOIN [dbo].[user_mstr] AS [Extent6] ON [Extent4].[user_id] = [Extent6].[user_id] INNER JOIN [dbo].[group_assign] AS [Extent7]"
						+ " ON ([Extent4].[task_id] = [Extent7].[task_id]) AND (([Extent4].[response] = [Extent7].[response]) OR (([Extent4].[response] IS NULL) "
						+ "AND ([Extent7].[response] IS NULL))) INNER JOIN [dbo].[task_workgroup_mstr] AS [Extent8] ON [Extent7].[group_id] = [Extent8].[workgroup_id] "
						+ "WHERE (N'A' = [Extent4].[response]) AND ( NOT EXISTS (SELECT 1 AS [C1] FROM [dbo].[user_todo_list] AS [Extent9] INNER JOIN [dbo].[group_assign]"
						+ " AS [Extent10] ON [Extent9].[task_id] = [Extent10].[task_id] INNER JOIN [dbo].[task_workgroup_mstr] AS [Extent11] ON [Extent10].[group_id] = [Extent11].[workgroup_id] "
						+ "INNER JOIN [dbo].[task_workgroup_user_xref] AS [Extent12] ON [Extent11].[workgroup_id] = [Extent12].[workgroup_id] WHERE ([Extent9].[task_id] = [Extent4].[task_id])"
						+ " AND ([Extent10].[group_id] = [Extent7].[group_id]) AND ([Extent12].[user_id] = [Extent4].[user_id]) )) UNION ALL SELECT [Extent13].[task_id] AS [task_id], "
						+ "[Extent14].[group_id] AS [group_id], [Extent16].[user_id] AS [user_id], [Extent17].[first_name] AS [first_name], [Extent17].[mi] AS [mi], "
						+ "[Extent17].[last_name] AS [last_name], [Extent18].[response] AS [response], CAST( [Extent18].[create_timestamp] AS datetime2) AS [C1] "
						+ "FROM [dbo].[user_todo_list] AS [Extent13] INNER JOIN [dbo].[group_assign] AS [Extent14] ON [Extent13].[task_id] = [Extent14].[task_id] "
						+ "INNER JOIN [dbo].[task_workgroup_mstr] AS [Extent15] ON [Extent14].[group_id] = [Extent15].[workgroup_id] INNER JOIN [dbo].[task_workgroup_user_xref] "
						+ "AS [Extent16] ON [Extent15].[workgroup_id] = [Extent16].[workgroup_id] INNER JOIN [dbo].[user_mstr] AS [Extent17] ON [Extent16].[user_id] = [Extent17].[user_id] "
						+ "LEFT OUTER JOIN [dbo].[user_response] AS [Extent18] ON ([Extent13].[task_id] = [Extent18].[task_id]) AND ([Extent16].[user_id] = [Extent18].[user_id])) AS [UnionAll1] ) "
						+ "AS [Distinct1] WHERE [Distinct1].[C3] <> '"+parameters.get("userId")+"') AS [UnionAll2] ) AS [Distinct2] WHERE ([Distinct2].[C2] = '"+parameters.get("taskId")+"') "
						+ "AND ([Distinct2].[C3] = '"+parameters.get("assigneeId")+"')";

				String[] jsonMetaData = {"taskId","userId","firstName","middleName","lastName","response","responseDate","isAssignedAsIndividual","groupIds"};

				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..taskId", jsonList);
					//objHandler = FinalAssertions.assertResponseCount(jsonMetaData, objHandler, DatabaseConnection.statementExecution(connNGA, strQuery), "id");
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
