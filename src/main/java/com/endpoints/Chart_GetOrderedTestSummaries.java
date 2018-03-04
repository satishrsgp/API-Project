package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetOrderedTestSummaries {

	public static Boolean chart_GetOrderedTestSummaries(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{


				String strQuery="select lot.order_test_id,lot.order_num,ln.enterprise_id,ln.practice_id,ln.person_id,lot.test_code_id,lot.test_code_text,lot.test_code_system,"
						+ "lot.test_comment,lot.collection_time, lot.collection_time_tz,lot.expected_rslt_date,lot.scheduled_time,lot.scheduled_time_tz,lot.loinc_code,lot.charge_id,"
						+ "lot.ordering_reason from lab_order_tests lot inner join lab_nor ln on lot.order_num = ln.order_num where lot.order_num ='"+parameters.get("orderId")+"'";

				String[] jsonMetaData = {"id","orderId","enterpriseId","practiceId","personId","testCodeId","testCodeText","testCodeSystem","testComment","collectionTime","collectionTimezone","expectedResultDate;Date_YYYYMMDD HH:MM:SS","scheduledTime","scheduledTimezone",
						"loincCode","chargeId","orderingReason"};

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
