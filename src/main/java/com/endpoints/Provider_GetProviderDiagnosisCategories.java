package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Provider_GetProviderDiagnosisCategories {
	
	public static Boolean provider_GetProviderDiagnosisCategories(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{


				String strQuery="select pm.provider_id as providerId, icm.icd_category_id as id, icm.description as description, icm.short_description as shortDescription, picm.seq_nbr as sequenceNumber, "
						+ "CASE WHEN icm.delete_ind = 'Y' then 'true' else 'false' end as isDeleted, "
						+ "CASE WHEN icm.kbm_ind = 'Y' then 'true' else 'false' end as fromKbm, "
						+ "CASE WHEN icm.show_in_epm_ind = 'Y' then 'true' else 'false' end as showInEpm, "
						+ "CASE WHEN icm.show_in_ehr_ind = 'Y' then 'true' else 'false' end as showInEhr "
						+ "from provider_mstr pm inner join provider_icd_category_mstr picm on pm.provider_id = picm.provider_id "
						+ "inner join icd_category_mstr icm on picm.icd_category_id = icm.icd_category_id "
						+ "where pm.provider_id='" + parameters.get("providerId") + "'";

				String[] jsonMetaData = {"providerId","id","description","description","shortDescription","sequenceNumber","isDeleted","fromKbm","showInEpm","showInEhr"};

				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..providerId", jsonList);
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
