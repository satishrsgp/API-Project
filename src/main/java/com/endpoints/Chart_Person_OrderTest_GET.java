package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_Person_OrderTest_GET
{
	public static Boolean chart_Person_OrderTest_GET_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = null;
				if(strResponse.contains("$skip=25"))
				{
					strQuery = "select top 25 UPPER(CAST(lot.order_test_id AS CHAR(36))) as id,UPPER(CAST(lot.order_num AS CHAR(36))) as orderNumber,ln.enterprise_id as enterpriseId,ln.practice_id as practiceId,"
							+" UPPER(CAST(ln.person_id AS CHAR(36))) as personId,lot.test_code_id as testCodeId,lot.test_code_text as testCodeText,lot.test_comment as testComment,lot.expected_rslt_date as expectedResultDate,"
							+" lot.scheduled_time as scheduledTime,lot.scheduled_time_tz as scheduledTimezone,UPPER(CAST(lot.charge_id AS CHAR(36))) as chargeId,lot.ordering_reason as orderingReason"
							+" from lab_order_tests lot inner join lab_nor ln on (lot.order_num = ln.order_num)"
							+" where (ln.person_id = '"+parameters.get("personId")+"' AND ln.order_num = '"+parameters.get("orderId")+"')";
				}
				else
				{
					strQuery = "select UPPER(CAST(lot.order_test_id AS CHAR(36))) as id,UPPER(CAST(lot.order_num AS CHAR(36))) as orderNumber,ln.enterprise_id as enterpriseId,ln.practice_id as practiceId,"
							+" UPPER(CAST(ln.person_id AS CHAR(36))) as personId,lot.test_code_id as testCodeId,lot.test_code_text as testCodeText,lot.test_comment as testComment,lot.expected_rslt_date as expectedResultDate,"
							+" lot.scheduled_time as scheduledTime,lot.scheduled_time_tz as scheduledTimezone,UPPER(CAST(lot.charge_id AS CHAR(36))) as chargeId,lot.ordering_reason as orderingReason"
							+" from lab_order_tests lot inner join lab_nor ln on (lot.order_num = ln.order_num)"
							+" where (ln.person_id = '"+parameters.get("personId")+"' AND ln.order_num = '"+parameters.get("orderId")+"')";
				}
				String[] jsonMetaData = {"id","orderNumber","enterpriseId","practiceId","personId","testCodeId","testCodeText","testComment","expectedResultDate","scheduledTime","scheduledTimezone","chargeId","orderingReason"};

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