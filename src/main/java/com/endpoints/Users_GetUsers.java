package com.endpoints;

/*
 * ********************************************************************
 * Class Name						: Users_GetUsers
 * Created By						: Seema Jaffary
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: 
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							:
 * ******************************************************************
 */
import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Users_GetUsers {


	public static Boolean users_GetUsers_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				
				String strQuery = "SELECT TOP (25) user_id AS id, first_name AS firstName, last_name AS lastName, mi AS mi, "
						+ "employee_num AS employeeNum, enterprise_id AS enterpriseId, practice_id AS practiceId, "
						+ "login_id AS loginId, CASE WHEN delete_ind = 'Y' THEN 'true' ELSE 'false' END AS isDeleted "
						+ "FROM [dbo].[user_mstr] AS [Extent1] WHERE (N'0' = (LTRIM(RTRIM([Extent1].[delete_ind])))) OR "
						+ "(N'n' = (LOWER(LTRIM(RTRIM([Extent1].[delete_ind]))))) "
						+ "ORDER BY [Extent1].[first_name] ASC, [Extent1].[last_name] ASC, [Extent1].[user_id] ASC";
						
				String[] jsonMetaData = {"id","firstName","lastName","mi","employeeNum","enterpriseId","practiceId","loginId", "isDeleted"};
				
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$.."+strUniqueJsonToken, jsonList);
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
