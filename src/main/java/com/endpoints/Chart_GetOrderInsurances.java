package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetOrderInsurances 
{
	public static Boolean Chart_GetOrderInsurances_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "SELECT loi.unique_ins_num AS id, imn.person_id AS personId, loi.person_payer_id AS personPayerId, loi.payer_id AS payerId, "
						+ "loi.insured_person_id AS insuredPersonId, loi.cob AS cob, imn.order_num AS orderNumber, pp.payer_name AS payerName, "
						+ "p.last_name + ', ' + p.first_name AS insuredPersonName "
						+ "FROM    dbo.lab_order_ins AS loi "
						+ " INNER JOIN dbo.person_payer AS pp ON loi.person_payer_id = pp.person_payer_id "
						+ " INNER JOIN dbo.person AS p ON loi.insured_person_id = p.person_id "
						+ " INNER JOIN dbo.imm_nor AS imn ON loi.order_num = imn.order_num "
						+ " WHERE (imn.person_id = '" + parameters.get("personId") + "') AND (imn.order_num = '" + parameters.get("orderId") + "') "
						+ " AND (imn.enterprise_id = '" + System.getProperty("LoggedInEnterpriseId") + "') AND (imn.practice_id='" + System.getProperty("LoggedInPracticeId") + "')";
				
				String[] jsonMetaData = {"id", "personId", "personPayerId", "payerId", "insuredPersonId", "cob", "orderNumber", "payerName", "insuredPersonName"};

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
