package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Master_GetVaccineManufacturers 
{

	public static Boolean master_GetVaccineManufacturers_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "SELECT top 25 manufacturer_nbr AS manufacturerNumber, manufacturer_name AS manufacturerName, "
						+ "manufacturer_cdc_code AS manufacturerCdcCode, CASE WHEN delete_ind = 'N' THEN 'false' ELSE 'True' "
						+ "END AS isDeleted FROM vaccine_manufacturer_mstr WHERE delete_ind = 'N' order by manufacturer_cdc_code" ;
				
				//providerName ,"whoReviewed": null,"whenReviewed": null,
				
				
				String[] jsonMetaData = {"manufacturerNumber","manufacturerName","manufacturerCdcCode","isDeleted"};
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..manufacturerNumber", jsonList);
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
