/**
 * API 1.x route_Abhishek
 */

package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Persons_GetInsurance {

	public static Boolean persons_GetInsurance(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{


				String strQuery="select payer_id as payerId, payer_name as payerName,person_payer_id,def_cob as defaultCob, policy_eff_date as policyEffectiveDate, policy_exp_date as policyExpirationDate, "
						+ "CASE WHEN active_ind = 'Y' THEN 'true' ELSE 'false' END as isActive from person_payer where person_id='"+parameters.get("personId")+"'and (policy_exp_date > getdate() or policy_exp_date = '') "
						+ " and active_ind = 'Y'";

				String[] jsonMetaData = {"payerId","payerName","personPayerId","defaultCob","policyEffectiveDate","policyExpirationDate","isActive"};

				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..payerId", jsonList);
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
