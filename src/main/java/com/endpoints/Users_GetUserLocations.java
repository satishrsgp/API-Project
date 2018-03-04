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
 * Class Name						: Users_GetUserLocations 
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
public class Users_GetUserLocations 
{

	public static Boolean users_GetUserLocations_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "SELECT TOP 25 lm.location_id AS id, p.practice_id AS practiceId, lm.location_name AS name, "
						+ "lm.address_line_1 AS addressLine1, lm.address_line_2 AS addressLine2, lm.city AS city, lm.state AS state, "
						+ "lm.zip AS zip, lm.phone AS phone, CASE WHEN lm.delete_ind = 'Y' THEN 'true' ELSE 'false' END AS isDeleted, "
						+ "um.user_id AS userId FROM user_mstr um INNER JOIN user_group_xref ugx ON (um.user_id = ugx.user_id AND um.delete_ind = 'N') "
						+ "INNER JOIN security_groups sg ON sg.group_id = ugx.group_id "
						+ "INNER JOIN practice p ON (sg.practice_id = p.practice_id AND p.delete_ind = 'N') "
						+ "INNER JOIN practice_location_xref plx ON (plx.practice_id = p.practice_id) "
						+ "INNER JOIN location_mstr lm ON (lm.location_id = plx.location_id AND lm.delete_ind = 'N') "
						+ "WHERE um.user_id = '"+parameters.get("userId")+"' "
				        + "and sg.enterprise_id='"+System.getProperty("LoggedInEnterpriseId")+"' "
				        	+ "and sg.practice_id='"+System.getProperty("LoggedInPracticeId")+"' ";
				
				String[] jsonMetaData = {"id", "practiceId", "name", "addressLine1", "addressLine2",
						"city", "state", "zip", "phone", "isDeleted", "userId"};
				
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
