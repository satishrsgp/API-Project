package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetOrderSchedule
{

	public static Boolean Chart_GetOrderSchedule_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "select os.schedule_id as id,ln.person_id as personId,ln.enterprise_id as enterpriseId,ln.practice_id as practiceId,os.order_id as orderId,os.order_type as orderType, "
						+"os.start_date as startDate,os.end_date as endDate,os.interval as interval,os.timespan as timespan,os.created_by as createdBy,os.create_timestamp as createTimestamp, "
						+"os.nextdue_date as nextDueDate,os.modified_by as modifiedBy,case when os.auto_release_ind='Y' then 'true' else 'false' end as isAutoRelease,os.max_occurrence as maxOccurrence, "
						+"os.stop_date as stopDate,os.stop_reason as stopReason,case when os.future_ind='F' then 'true' else 'false' end as isFutureOrder "
						+"from order_schedule os inner join lab_nor ln on os.order_id=ln.order_num "
						+"where os.order_id='"+parameters.get("orderId")+"' and ln.person_id='"+parameters.get("personId")+"'";

				String[] jsonMetaData = {"id","personId","enterpriseId","practiceId","orderId","orderType","startDate","endDate","interval","timespan","createdBy","createTimestamp","nextDueDate","modifiedBy","isAutoRelease","maxOccurrence","stopDate","stopReason","isFutureOrder"};

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