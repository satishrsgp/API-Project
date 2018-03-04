package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class GetProviderMedicationRefills {

	public static Boolean getProviderMedicationRefills_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{

				String strQuery="select request_id as requestId,rx_request_name as rxRequestName,  requesting_ncpdp_id as requestingNcpdpId, "
						+ "workflow_task_id as workflowTaskId, sig as sig, requested_amount as requestedAmount, last_fill_date as lastFillDate, "
						+ "ndc_id as ndcId, days_supply as daysSupply, written_date as writtenDate, notes as notes, req_refill_qty as "
						+ "requestRefillQuantity, request_err as requestError, request_med as requestMedication, provider_id as providerId, "
						+ "dea_class as deaClass, matched_person_id as matchedPersonId, case when unmatched_request_ind = 'N' then 'false' else 'true' "
						+ "end as isUnmatchedRequest, create_timestamp as createTimestamp from rx_refill_request where provider_id = "
						+ "'"+parameters.get("providerId")+"'" ;

				String[] jsonMetaData = {"requestId","rxRequestName","requestingNcpdpId","workflowTaskId","sig","requestedAmount","lastFillDate;Date_YYYYMMDD HH:MM:SS","ndcId","daysSupply"
						,"writtenDate;Date_YYYYMMDD HH:MM:SS","notes","requestRefillQuantity","requestError","requestMedication","providerId","deaClass","matchedPersonId","isUnmatchedRequest","createTimestamp;Date_YYYYMMDD HH:MM:SS"};

				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..requestId", jsonList);
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
