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
 * Class Name						: Task_Categories_GET
 * Created By						: Seema Jaffary
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: 
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							: Route will fetch the details of the task categories that have been configured
 * ******************************************************************
 */

public class Master_GetTaskCategories
{
	public static Boolean master_GetTaskCategories_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = null;
				if(strResponse.contains("?$skip=25"))
				{
					strQuery = "select top 25 UPPER(CAST(categ_id AS CHAR(36))) as id,categ_name as name,case when delete_ind='Y' then 'true' else 'false' end as isDeleted from todo_categ_list where delete_ind not like 'Y' order by categ_name";
				}
				else
				{
					strQuery = "select UPPER(CAST(categ_id AS CHAR(36))) as id,categ_name as name,case when delete_ind='Y' then 'true' else 'false' end as isDeleted from todo_categ_list where delete_ind not like 'Y' order by categ_name";
				}

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
