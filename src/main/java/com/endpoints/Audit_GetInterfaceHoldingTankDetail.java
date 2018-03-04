package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Audit_GetInterfaceHoldingTankDetail 
{

	public static Boolean audit_GetInterfaceHoldingTankDetail_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{//
			try
			{				
				String strQuery = "SELECT event_id AS id,queue_entry_id AS queueEntryId,match_type AS"
						+ " matchType,message_type AS messageType,holding_tank_reason AS"
						+ " holdingTankReason FROM event_interface_holding_tank where event_id='"+parameters.get("eventId")+"'";
				
			
				String[] jsonMetaData = {"id","queueEntryId","matchType","messageType","holdingTankReason"};
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGAAudit, strQuery), strResponse, "$.."+strUniqueJsonToken, jsonList);
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
