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
 * Class Name						: Users_GetUserProviders
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

public class Users_GetUserProviders {


	public static Boolean users_GetUserProviders_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				
				String strQuery = "SELECT TOP 25 pm.provider_id AS providerId, pm.last_name AS lastName, pm.first_name AS firstName, "
						+ "pm.middle_name AS middleName, CASE WHEN pm.delete_ind = 'Y' THEN 'true' ELSE 'false' END AS isDeleted, "
						+ "pm.degree AS degree, sg.practice_id AS practiceId, sg.enterprise_id AS enterpriseId, "
						+ "um.user_id AS userId FROM user_mstr um INNER JOIN user_group_xref ugx ON um.user_id = ugx.user_id "
						+ "INNER JOIN security_groups sg ON sg.group_id = ugx.group_id "
						+ "INNER JOIN provider_practice_mstr ppm ON (sg.enterprise_id = ppm.enterprise_id AND sg.practice_id = ppm.practice_id) "
						+ "INNER JOIN provider_mstr pm ON pm.provider_id = ppm.provider_id "
						+ "WHERE um.user_id = '"+parameters.get("userId")+"' "
				        + "and sg.enterprise_id='"+System.getProperty("LoggedInEnterpriseId")+"' "
				        	+ "and sg.practice_id='"+System.getProperty("LoggedInPracticeId")+"' "
						+ "order by pm.degree asc, sg.enterprise_id asc, pm.first_name asc, pm.delete_ind asc,pm.last_name asc, "
						+ "pm.middle_name asc, sg.practice_id asc, pm.provider_id asc, um.user_id";
						
						
				String[] jsonMetaData = {"providerId","lastName","firstName","middleName","isDeleted","degree","practiceId","enterpriseId", "userId"};
				
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
