package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetObservationPanelSummary
{
	public static Boolean Chart_GetObservationPanelSummary_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		System.out.println("Chart_GetObservationPanelSummary_Test Parameters for this run is:"+parameters);
		Boolean status=false;
		try
		{
			if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
			{
				String strQuery = "";
				if(strResponse.contains("$skip=25"))
				{
					strQuery = "select top 25 unique_obr_num as id,enterprise_id as enterpriseId,practice_id as practiceId,person_id as personId,ngn_order_num as orderId,coll_date_time as collectionDateTime, "
							+"coll_date_time_tz as collectionDateTimeTimezone,ord_phys_code as orderingProviderId,CASE WHEN order_result_stat='C' THEN 'Corrected' WHEN order_result_stat='ER' THEN 'ErrorNoOrder' WHEN order_result_stat='F' THEN 'Final' "
							+"WHEN order_result_stat='O' THEN 'IncompleteNoSpecimen' WHEN order_result_stat='SC' THEN 'InProcessScheduled' WHEN order_result_stat='IP' THEN 'InProcessUnspecified' WHEN order_result_stat='X' THEN 'LabCancelled' " 
							+"WHEN order_result_stat='CA' THEN 'OrderCancelled' WHEN order_result_stat='CM' THEN 'OrderCompleted' WHEN order_result_stat='DC' THEN 'OrderDiscontinued' WHEN order_result_stat='HD' THEN 'OrderHold' "
							+"WHEN order_result_stat='RP' THEN 'OrderReplaced' WHEN order_result_stat='A' Or order_result_stat='M' THEN 'Partial' WHEN order_result_stat IN ('I' ,'S') THEN 'PendingIncomplete' WHEN order_result_stat='D' THEN 'PracticeCancelled' " 
							+"WHEN order_result_stat='P' OR order_result_stat='R' THEN 'Preliminary' ELSE 'Unknown' END AS resultStatus,test_desc as testDescription, "
							+"case when delete_ind='Y' then 'true' else 'false' end as isDeleted,create_timestamp as createTimestamp,create_timestamp_tz as createTimestampTimezone,location_id as locationId, "
							+"obr_comment as obrComment,loinc_code as loincCode,case when confidential_ind='Y' then 'true' else 'false' end as isConfidential,order_test_id as orderTestId "
							+"from lab_results_obr_p where person_id='"+parameters.get("personId")+"' and delete_ind='N' order by unique_obr_num";
				}
				else
				{
					strQuery = "select unique_obr_num as id,enterprise_id as enterpriseId,practice_id as practiceId,person_id as personId,ngn_order_num as orderId,coll_date_time as collectionDateTime, "
							+"coll_date_time_tz as collectionDateTimeTimezone,ord_phys_code as orderingProviderId,CASE WHEN order_result_stat='C' THEN 'Corrected' WHEN order_result_stat='ER' THEN 'ErrorNoOrder' WHEN order_result_stat='F' THEN 'Final' "
							+"WHEN order_result_stat='O' THEN 'IncompleteNoSpecimen' WHEN order_result_stat='SC' THEN 'InProcessScheduled' WHEN order_result_stat='IP' THEN 'InProcessUnspecified' WHEN order_result_stat='X' THEN 'LabCancelled' " 
							+"WHEN order_result_stat='CA' THEN 'OrderCancelled' WHEN order_result_stat='CM' THEN 'OrderCompleted' WHEN order_result_stat='DC' THEN 'OrderDiscontinued' WHEN order_result_stat='HD' THEN 'OrderHold' "
							+"WHEN order_result_stat='RP' THEN 'OrderReplaced' WHEN order_result_stat='A' Or order_result_stat='M' THEN 'Partial' WHEN order_result_stat IN ('I' ,'S') THEN 'PendingIncomplete' WHEN order_result_stat='D' THEN 'PracticeCancelled' " 
							+"WHEN order_result_stat='P' OR order_result_stat='R' THEN 'Preliminary' ELSE 'Unknown' END AS resultStatus,test_desc as testDescription, "
							+"case when delete_ind='Y' then 'true' else 'false' end as isDeleted,create_timestamp as createTimestamp,create_timestamp_tz as createTimestampTimezone,location_id as locationId, "
							+"obr_comment as obrComment,loinc_code as loincCode,case when confidential_ind='Y' then 'true' else 'false' end as isConfidential,order_test_id as orderTestId "
							+"from lab_results_obr_p where person_id='"+parameters.get("personId")+"'  and delete_ind='N' order by unique_obr_num";
				}

				String[] jsonMetaData = {"id;GUID","enterpriseId;Normal","practiceId;Normal","personId;GUID","orderId;GUID","collectionDateTime;Date_YYYYMMDD HH:MM:SS","collectionDateTimeTimezone;Normal","orderingProviderId;GUID","resultStatus;Normal","testDescription;Normal","isDeleted;Boolean","createTimestamp;Date_YYYYMMDD HH:MM:SS","createTimestampTimezone;Normal","locationId;GUID","obrComment;Normal","loincCode;Normal","isConfidential;Boolean","orderTestId;GUID"};
				//String[] jsonSchema = {"GUID","Normal","Normal","GUID","GUID","Date_YYYYMMDD HH:MM:SS","Normal","GUID","Normal","Normal","Boolean","Date_YYYYMMDD HH:MM:SS","Normal","GUID","Normal","Normal","Boolean","GUID"};
				ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
				//ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, jsonSchema, strResponse, strUniqueJsonToken);
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