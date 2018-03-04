package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Tasking_GetTaskSets
{

	public static Boolean tasking_GetTaskSets(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				
				String strQuery = "SELECT t.tab_id as id,t.user_id as userId,t.tab_name as name,t.tab_order as ordr,"
						+ "LOWER(x.categ_id) as assignedCategoryIds "
						+ "FROM todo_tabs t LEFT OUTER JOIN todo_tab_xref x ON t.tab_id=x.tab_id LEFT OUTER JOIN todo_categ_list c ON x.categ_id=c.categ_id "
						+ "WHERE t.user_id='"+parameters.get("userId")+"' ORDER BY t.tab_order,c.categ_name";
						
				String[] jsonMetaData = {"id","userId","name","order","assignedCategoryIds"};
				
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..id", jsonList);
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
