package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Master_GetMasterListItems 
{

	public static Boolean master_GetMasterListItems_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "SELECT TOP 25 mstr_list_item_id AS id, mstr_list_type AS TYPE, mstr_list_item_desc AS name, "
						+ "list_order AS list_order, CASE WHEN show_in_emr_ind = 'N' THEN 'false' ELSE 'true' END AS showInEmr, "
						+ "CASE WHEN delete_ind = 'N' THEN 'false' ELSE 'true' END AS isDeleted FROM mstr_lists "
						+ "WHERE delete_ind = 'N' ORDER BY TYPE, list_order";
				
				//providerName ,"whoReviewed": null,"whenReviewed": null,

				
				String[] jsonMetaData = {"id","type","name","order","showInEmr","isDeleted"};
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
