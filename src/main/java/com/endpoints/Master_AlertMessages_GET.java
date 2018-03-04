package com.endpoints;

import java.util.ArrayList;
import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;


public class Master_AlertMessages_GET
{
	public static Boolean master_AlertMessages_GET_Test(String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strQuery =null;
			if(strResponse.contains("$skip=25"))
			{
				strQuery= "select top 25 message_id as id, description as messageDescription,alerttype_id as alertTypeId,case when delete_ind='N' then 'false' else 'true' end as isDeleted from ehr_alerts_mstr order by message_id";
			}
			else
			{
				strQuery= "select message_id as id, description as messageDescription,alerttype_id as alertTypeId,case when delete_ind='N' then 'false' else 'true' end as isDeleted from ehr_alerts_mstr order by message_id";
			}

			String[] jsonMetaData = {"id","messageDescription","alertTypeId","isDeleted"};
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