package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetVitals
{

	public static Boolean chart_GetVitals(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "select seq_no as id, person_id as personId, practice_id as practiceId, enterprise_id as enterpriseId, "
						+ "enc_id as encounterId, vitalSignsDate as vitalSignsDate, vitalSignsTime as vitalSignsTime "
						+ "From vital_signs_ where person_id='"+parameters.get("personId")+"' ";
				
				//providerName ,"whoReviewed": null,"whenReviewed": null,

				
				String[] jsonMetaData = {"id","personId","practiceId","enterpriseId","encounterId","vitalSignsDate;Date_YYYYMMDD", "vitalSignsTime"};
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..personId", jsonList);
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
