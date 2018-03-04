package com.endpoints;

/*
 * ********************************************************************
 * Class Name						: TaskPriorities_GET
 * Created By						: Seema Jaffary
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: 
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							: Route will fetch the details of the task priorities that have been configured
 * ******************************************************************
 */



import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.utils.JsonListArray;
import com.utils.Log;
import com.utils.ValidateResults;

public class Master_GetTaskPriorities
{
	public static Boolean master_GetTaskPriorities_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				
				String[] jsonMetaData = {"id","name"};

				ArrayList<Object> dbList = new ArrayList<>();
				dbList.add(1);
				dbList.add("High");
				dbList.add(2);
				dbList.add("Normal");
				dbList.add(3);
				dbList.add("Low");
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status=ValidateResults.resultValidation(jsonList, dbList);
						
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