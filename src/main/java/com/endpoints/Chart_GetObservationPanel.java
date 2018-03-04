package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetObservationPanel
{
	public static Boolean Chart_GetObservationPanel_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		System.out.println("Chart_GetObservationPanel_Test Parameters for this run is:"+parameters);
		Boolean status=false;
		try
		{
			if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
			{
				String strQuery = "select unique_obr_num as id,enterprise_id as enterpriseId,practice_id as practiceId,person_id as personId,ngn_order_num as orderId,coll_date_time as collectionDateTime, "
						+"coll_date_time_tz as collectionDateTimeTimezone,ord_phys_code as orderingProviderId,CASE WHEN order_result_stat='C' THEN 'Corrected' WHEN order_result_stat='ER' THEN 'ErrorNoOrder' WHEN order_result_stat='F' THEN 'Final' "
						+"WHEN order_result_stat='O' THEN 'IncompleteNoSpecimen' WHEN order_result_stat='SC' THEN 'InProcessScheduled' WHEN order_result_stat='IP' THEN 'InProcessUnspecified' WHEN order_result_stat='X' THEN 'LabCancelled' " 
						+"WHEN order_result_stat='CA' THEN 'OrderCancelled' WHEN order_result_stat='CM' THEN 'OrderCompleted' WHEN order_result_stat='DC' THEN 'OrderDiscontinued' WHEN order_result_stat='HD' THEN 'OrderHold' "
						+"WHEN order_result_stat='RP' THEN 'OrderReplaced' WHEN order_result_stat='A' Or order_result_stat='M' THEN 'Partial' WHEN order_result_stat IN ('I' ,'S') THEN 'PendingIncomplete' WHEN order_result_stat='D' THEN 'PracticeCancelled' " 
						+"WHEN order_result_stat='P' OR order_result_stat='R' THEN 'Preliminary' ELSE 'Unknown' END AS resultStatus,test_desc as testDescription, "
						+"case when delete_ind='Y' then 'true' else 'false' end as isDeleted,create_timestamp as createTimestamp,create_timestamp_tz as createTimestampTimezone,location_id as locationId, "
						+"obr_comment as obrComment,loinc_code as loincCode,case when confidential_ind='Y' then 'true' else 'false' end as isConfidential,order_test_id as orderTestId,case when microbiology_ind='Y' then 'true' else 'false' end as isMicrobiology "
						+"from lab_results_obr_p where person_id='"+parameters.get("personId")+"' and unique_obr_num='"+parameters.get("panelId")+"'";

				String[] jsonMetaData = {"id","enterpriseId","practiceId","personId","orderId","collectionDateTime","collectionDateTimeTimezone","orderingProviderId","resultStatus","testDescription","isDeleted","createTimestamp","createTimestampTimezone","locationId","obrComment","loincCode","isConfidential","orderTestId","isMicrobiology"};
				ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
				Log.info("JSON LIST is :\n" + jsonList);
				status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..id", jsonList);
			}
			else
			{
				status = true;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.info("Error in Test Class and the detail are : \n" + e.getMessage());
			softAssert.fail("Error in Test Script please look at logs");
		}
		return status;
	}
}