package com.endpoints;

import java.util.ArrayList;

import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.JsonPath;
import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Audit_GetPaqDetail
{
	public static Boolean audit_GetPaqDetail(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				Object objId = JsonPath.read(strResponse, "$.id");

				String strQuery = "select psh.unique_id,psh.provider_id,p.first_name,p.last_name,psh.item_type,psh.item_name,psh.signoff_desc,psh.create_timestamp "
									+ " from provider_mstr p inner join paq_signoff_history psh on p.provider_id=psh.provider_id where psh.unique_id='"+objId.toString()+"'";

				String[] jsonMetaData = {"id", "providerId", "providerFirstName","providerLastName","type","itemName","signoffDescription","timestamp;Date_YYYYMMDD HH:MM:SS" };

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