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

public class Provider_GetProviders {

	public static Boolean provider_GetProviders(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery="select distinct top 25 (pm.provider_id),pm.last_name,pm.first_name,pm.middle_name,pm.description,pm.address_line_1,pm.address_line_2,pm.city,pm.state,pm.zip,pm.phone, "
								+ "pm.phone_ext,case pm.delete_ind when 'n' then 'false' end as delete_ind,pm.degree,ppm.practice_id,ppm.enterprise_id from  "
								+ "provider_mstr pm "
								+ "inner join provider_practice_mstr ppm on ppm.provider_id = pm.provider_id "
								+ "left join provider_location_xref l on  pm.provider_id = l.provider_id "
								+ "where (enterprise_id="+System.getProperty("LoggedInEnterpriseId")+" and practice_id=" +System.getProperty("LoggedInPracticeId")+" and pm.delete_ind != 'Y' and ppm.delete_ind != 'Y') "
								+ "order by pm.provider_id";

				String[] jsonMetaData = {"id","lastName","firstName","middleName","description","providerAddressLine1","providerAddressLine2","providerCity",
						"providerState","providerZip","providerPhone","providerPhoneExtension","isDeleted","degree","practiceId","enterpriseId"};

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
