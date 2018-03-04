package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Master_GetMasterFileProviders 
{

	public static Boolean master_GetMasterFileProviders_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "SELECT DISTINCT top 25 (pm.provider_id) as id, pm.last_name as lastName, pm.first_name as firstName, "
						+ "pm.middle_name as middleName, pm.description as description, pm.address_line_1 as providerAddressLine1, "
						+ "pm.address_line_2 as providerAddressLine2, pm.city as providerCity, pm.state as providerState, pm.zip as providerZip, "
						+ "pm.phone as providerPhone, pm.phone_ext as providerPhoneExtension, "
						+ "CASE pm.delete_ind WHEN 'n' THEN 'false' END AS isDeleted, pm.degree as degree, ppm.practice_id as practiceId, "
						+ "ppm.enterprise_id as enterpriseId FROM provider_mstr pm "
						+ "INNER JOIN provider_practice_mstr ppm ON ppm.provider_id = pm.provider_id "
						+ "LEFT JOIN provider_location_xref l ON pm.provider_id = l.provider_id "
						+ "WHERE (pm.delete_ind != 'Y' AND ppm.delete_ind != 'Y')";
				
				String[] jsonMetaData = {"id","lastName","firstName","middleName","description","providerAddressLine1","providerAddressLine2",
				"providerCity","providerState","providerZip","providerPhone","providerPhoneExtension","isDeleted","degree", "practiceId", "enterpriseId"};
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
