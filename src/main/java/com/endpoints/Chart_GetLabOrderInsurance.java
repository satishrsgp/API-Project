package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetLabOrderInsurance
{
	public static Boolean Chart_GetLabOrderInsurance_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery= null;
				if(strResponse.contains("$skip="))
				{
					strQuery = "SELECT top 25 loi.unique_ins_num AS id, loi.person_payer_id AS personPayerId,loi.payer_id AS payerId,loi.insured_person_id AS insuredPersonId,loi.cob AS cob, ln.order_num AS orderNumber, "
							+"ln.person_id AS personId,p.last_name as lastName,p.first_name as firstName,ln.enterprise_id as enterpriseId,ln.practice_id as practiceId,pp.payer_name AS payerName "
							+"FROM    dbo.lab_order_ins AS loi "
							+"INNER JOIN dbo.person_payer AS pp ON loi.person_payer_id = pp.person_payer_id "
							+"INNER JOIN dbo.person AS p ON loi.insured_person_id = p.person_id "
							+"INNER JOIN dbo.lab_nor AS ln ON loi.order_num = ln.order_num "
							+"WHERE (ln.person_id = '"+parameters.get("personId")+"') AND (ln.order_num = '"+parameters.get("orderId")+"') AND (ln.enterprise_id = '"+System.getProperty("LoggedInEnterpriseId")+"') AND (ln.practice_id='"+System.getProperty("LoggedInPracticeId")+"')";
				}
				else
				{
					strQuery = "SELECT loi.unique_ins_num AS id, loi.person_payer_id AS personPayerId,loi.payer_id AS payerId,loi.insured_person_id AS insuredPersonId,loi.cob AS cob, ln.order_num AS orderNumber, "
							+"ln.person_id AS personId,p.last_name as lastName,p.first_name as firstName,ln.enterprise_id as enterpriseId,ln.practice_id as practiceId,pp.payer_name AS payerName "
							+"FROM    dbo.lab_order_ins AS loi "
							+"INNER JOIN dbo.person_payer AS pp ON loi.person_payer_id = pp.person_payer_id "
							+"INNER JOIN dbo.person AS p ON loi.insured_person_id = p.person_id "
							+"INNER JOIN dbo.lab_nor AS ln ON loi.order_num = ln.order_num "
							+"WHERE (ln.person_id = '"+parameters.get("personId")+"') AND (ln.order_num = '"+parameters.get("orderId")+"') AND (ln.enterprise_id = '"+System.getProperty("LoggedInEnterpriseId")+"') AND (ln.practice_id='"+System.getProperty("LoggedInPracticeId")+"')";
				}
				String[] jsonMetaData = {"id","personPayerId","payerId","insuredPersonId","cob","orderNumber","personId","lastName","firstName","enterpriseId","practiceId","payerName"};

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