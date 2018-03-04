package com.endpoints;

import java.util.ArrayList;
import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;


public class Master_GetEvents
{
	public static Boolean master_GetEvents(String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
		//	String strQuery =null;
			
			String strQuery= "select top 25  event_id,practice_id,event, case when delete_ind like 'N' then 'false' else 'true' end as delete_ind from events  where delete_ind='N' order by practice_id,event asc";
			

			String[] jsonMetaData = {"id","practiceId","name","isDeleted"};
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
		return status;
	}
}