package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Audit_GetMedicationDetail
{



	public static Boolean audit_GetMedicationDetail_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				
				
				String event_unique_id ="select unique_id from event_chart_summary where event_id = '" + parameters.get("eventId")+"'";
String unique_id = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGAAudit, event_unique_id, "unique_id").toString();
				
				String strQuery=" select pm.uniq_id as id, lm.location_name as locationName, prm.first_name as providerFirstName, "
						+ "prm.degree as providerDegree, prm.last_name as providerLastName, fm.brand_name as brandName, "
						+ "fm.generic_name as genericName, pm.start_date as startDate, "
						+ "pm.date_stopped as dateStopped, pm.sig_desc as sigDescription, pm.rx_quanity as rxQuantity, "
						+ "pm.rx_refills as rxRefills, pm.rx_units as rxUnits, pm.rx_comment as rxComment from patient_medication pm "
						+ "inner join location_mstr lm on lm.location_id = pm.location_id inner join provider_mstr prm on "
						+ "prm.provider_id = pm.provider_id inner join fdb_medication fm on fm.ndc_id = pm.ndc_id where "
						+ "pm.uniq_id = '"+unique_id+"'" ;
												

						
				
				String[] jsonMetaData = {"id","locationName","providerFirstName","providerDegree","providerLastName","brandName","genericName","startDate",
					     "dateStopped","sigDescription","rxQuantity","rxRefills","rxUnits","rxComment"  };
				
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
