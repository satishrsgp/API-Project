package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetPatientRecallPlans 
{

	public static Boolean chart_GetPatientRecallPlans_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "select prp.recall_plan_id as recallPlanId,prp.person_id as personId, prp.practice_id as practiceId,  "
						+ "prp.seq_nbr as id, rpm.description as description, case when prp.active_plan_ind = 'Y' then 'true' else 'false' "
						+ "end as isActive, prp.note as note, prp.plan_start_date as startDate, prp.plan_end_date as endDate, "
						+ "prp.last_date as lastDate, prp.expected_return_date as expectedReturnDate, "
						+ "prp.letter1_sched_date as letter1ScheduledDate, prp.letter1_sent_date as letter1SentDate, "
						+ "prp.letter2_sched_date as letter2ScheduledDate, prp.letter2_sent_date as letter2SentDate, "
						+ "prp.letter3_sched_date as letter3ScheduledDate, prp.letter3_sent_date as letter3SentDate, "
						+ "prp.manual_stop_reason_id as stopReasonId, prp.stop_reason as stopReason, prp.event_id as eventId, "
						+ "ev.event as eventDescription, prp.resource_id as resourceId, rs.description as resourceDescription, "
						+ "prp.location_id as locationId, lm.location_name as locationName, prp.created_by as createdByUserId, "
						+ "concat(um.last_name,', ',um.first_name, um.mi) as createdByName, prp.create_timestamp as createTimestamp, "
						+ "prp.modified_by as modifiedByUserId, "
						+ "prp.modify_timestamp as modifyTimestamp from patient_recall_plans prp inner join user_mstr um "
						+ "on prp.created_by = um.user_id left join events ev on ev.event_id = prp.event_id left join resources rs "
						+ "on rs.resource_id = prp.resource_id left join location_mstr lm on lm.location_id = prp.location_id "
						+ "left join recall_plan_mstr rpm on rpm.recall_plan_id = prp.recall_plan_id "
						+ "where prp.person_id ='"+parameters.get("personId")+"'";
						
				
				
				
				String[] jsonMetaData = {"recallPlanId","personId","practiceId","id","description","isActive","note","startDate","endDate",
						"lastDate","expectedReturnDate","letter1ScheduledDate","letter1SentDate","letter2ScheduledDate",
				"letter2SentDate","letter3ScheduledDate","letter3SentDate",	"stopReasonId",	"stopReason","eventId","eventDescription","resourceId",
				"resourceDescription","locationId","locationName","createdByUserId","createdByName","createTimestamp;Date_YYYYMMDD HH:MM:SS","modifiedByUserId","modifyTimestamp;Date_YYYYMMDD HH:MM:SS"
				};
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..recallPlanId", jsonList);
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
