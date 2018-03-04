package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Provider_GetProviderErxDetails 
{

	public static Boolean provider_GetProviderErxDetails_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{				
				String strQuery = "SELECT pm.provider_id AS id, ppm.enterprise_id AS enterpriseId, ppm.practice_id AS"
						+ " practice_id, epm.addr_line_1 AS addressLine1, epm.addr_line_2 AS addressLine2, epm.city AS"
						+ " city, epm.state AS state, epm.zip AS zip, epm.phone AS phone, epm.fax AS fax, epm.spi_nbr AS"
						+ " spiNumber, epm.enroll_date AS enrollDate, epm.unenroll_date AS unenrollDate, epm.rx_hub_provider_id AS"
						+ " rxHubProviderId, epm.rx_hub_enroll_date AS"
						+ " rxHubEnrollDate, CASE WHEN epm.require_supervisor_ind = 'Y' THEN 'true' ELSE 'false' END AS"
						+ " requiresSupervisor, epm.supervising_provider_id AS supervisingProviderId, epm.ss_service_level AS"
						+ " surescriptsServiceLevel, epm.rx_hub_service_level AS"
						+ " rx_hub_service_level, CASE WHEN (epm.unenroll_date IS NOT NULL AND epm.unenroll_date > getdate ())"
						+ " THEN 'true' ELSE 'false' END AS isSurescriptsActive FROM provider_mstr AS pm LEFT JOIN"
						+ " provider_practice_mstr AS ppm ON pm.provider_id = ppm.provider_id AND ppm.practice_id = '"+System.getProperty("LoggedInPracticeId")
						+ "' LEFT JOIN erx_provider_mstr AS epm ON ppm.provider_id = epm.provider_id AND ppm.practice_id = '"+System.getProperty("LoggedInPracticeId")
						+ "' WHERE pm.provider_id = '"+parameters.get("providerId")+"'";
				
				//CONVERT(uniqueidentifier,'0C6A36BA-10E4-438F-BA86-0D5B68A2BB15'
				//da.event_text whoReviewed,da.create_timestamp as whenReviewed   --------"whoReviewed": null,"whenReviewed": null, parameters.get("personId")

				String[] jsonMetaData = {"id","enterpriseId","practiceId","addressLine1","addressLine2","city","state","zip","phone","fax",
						"spiNumber","enrollDate;Date_YYYYMMDD HH:MM:SS","unenrollDate;Date_YYYYMMDD HH:MM:SS","rxHubProviderId","rxHubEnrollDate;Date_YYYYMMDD HH:MM:SS","requiresSupervisor",
						"supervisingProviderId","surescriptsServiceLevel","rxHubServiceLevel","isSurescriptsActive"};
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$.."+strUniqueJsonToken, jsonList);
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
