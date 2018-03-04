package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;
/*
 * ********************************************************************
 * Class Name						: Users_GetUserEnterprises
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
public class Users_GetUserEnterprises 
{

	public static Boolean users_GetUserEnterprises_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "SELECT DISTINCT sg.enterprise_id AS id, e.enterprise_name AS enterpriseName, "
						+ "CASE WHEN e.delete_ind = 'Y' THEN 'true' ELSE 'false' END AS isDeleted FROM user_mstr um "
						+ "INNER JOIN user_group_xref ugx ON (um.user_id = ugx.user_id AND um.delete_ind = 'N') "
						+ "INNER JOIN security_groups sg ON sg.group_id = ugx.group_id "
						+ "INNER JOIN enterprise e ON (sg.enterprise_id = e.enterprise_id AND e.delete_ind = 'N') "
						+ "WHERE um.user_id = '"+parameters.get("userId")+"'";
				
				String[] jsonMetaData = {"id","name","isDeleted"};
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
