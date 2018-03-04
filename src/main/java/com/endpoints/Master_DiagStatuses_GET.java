package com.endpoints;

import java.util.ArrayList;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Master_DiagStatuses_GET
{
	public static Boolean master_DiagStatuses_GET_Test(String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strQuery =null;
			if(strResponse.contains("$skip=25"))
			{
				strQuery= "select top 25 status_id as id,description as description,case when delete_ind='N' then 'false' else 'true' end as isDeleted,note as note from diagnosis_status_mstr order by status_id";
			}
			else
			{
				strQuery= "select status_id as id,description as description,case when delete_ind='N' then 'false' else 'true' end as isDeleted,note as note from diagnosis_status_mstr order by status_id";
			}
			String[] jsonMetaData = {"id","description","isDeleted","note"};
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