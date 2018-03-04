package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetOrderResults 
{
	public static Boolean Chart_GetOrderResults_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				
				String strQuery = " select "
						+ "lro.obx_seq_num as obxSequenceNumber,"
						+ "ln.order_num as orderId,"
						+ "lrop.unique_obr_num as resultId,"
						+ "ln.create_timestamp as orderDate,"
						+ "lro.create_timestamp as resultDate,"
						+ "lro.create_timestamp_tz as resultDateTimezone,"
						+ "lrop.obs_batt_id as observationBatteryId,"
						+ "lrop.test_desc as testDescription,"
						+ "lro.obs_id as observationId,"
						+ "lro.units as units,"
						+ "lro.ref_range as referenceRange,"						
						+ "ln.enterprise_id as enterpriseId,"
						+ "ln.practice_id as practiceId,"
						+ "ln.person_id as personId,"
						+ "case when lrop.delete_ind = 'N' then 'false' else 'true' end as isDeleted "
						+ "FROM   dbo.lab_nor AS ln "
						+ "INNER JOIN dbo.lab_results_obr_p AS lrop ON ln.order_num = lrop.ngn_order_num "
						+ "LEFT OUTER JOIN dbo.lab_results_obx AS lro ON lrop.unique_obr_num = lro.unique_obr_num "
						+ "where lro.person_id = '"+parameters.get("personId")+"' and ln.order_num = '"+parameters.get("orderId")+"' "
						+ "AND ln.enterprise_id = '" + System.getProperty("LoggedInEnterpriseId") + "' "
						+ "AND ln.practice_id = '" + System.getProperty("LoggedInPracticeId") + "' "
						+ "And ln.order_num = lrop.ngn_order_num and lrop.unique_obr_num = lro.unique_obr_num "
						+ "order by lro.obs_id,lro.obx_seq_num,ln.create_timestamp";
								
				
				String[] jsonMetaData = {"obxSequenceNumber","orderId","resultId","orderDate;Date_YYYYMMDD HH:MM:SS","resultDate;Date_YYYYMMDD HH:MM:SS","resultDateTimezone","observationBatteryId","testDescription",
						"observationId","units","referenceRange","enterpriseId","practiceId","personId","isDeleted"};
						
				
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..obxSequenceNumber", jsonList);
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
