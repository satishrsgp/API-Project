package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Master_GetRecallPlans
{

	/*
	 * ********************************************************************************
	 * Class Name						:Master_GetRecallPlans
	 * Created By						: 
	 * Created Date						: 
	 * Modified Date					:
	 * Modified By						:
	 * Parameters						: 
	 * Test Case ID						:
	 * Test Case Author					: 
	 * Test Case Modified By			:
	 * Test Case Reviewed By			:
	 * Purpose							: Route will fetch the allergy chart details
	 ***********************************************************************************
	 */

	public static Boolean master_GetRecallPlans_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		//System.out.println(strResponse);
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{ 
				String strQuery = "SELECT top 25 recall_plan_id as id,"
						+ " description as description, "
						+ " practice_id as practiceId, "
						+ " start_event_id as eventId, "
						+ " case when  event_prompt_ind ='N' then 'false' else 'true' end as allowEventOverride," 
						+ " resource_id as resourceId, "   
						+ " case when  resource_prompt_ind ='N' then 'false' else 'true' end as allowResourceOverride, "
						+  " location_id as locationId,"
						+ " case when  location_prompt_ind ='N' then 'false' else 'true' end as allowLocationOverride,"
						+ " event_sched_days as eventScheduleDays,"
						+ " case when  cancel_plan_status ='K' then 'Kept'"
						+ "when cancel_plan_status ='S' then 'Scheduled' " 
						+ " else 'None' end as cancelPlanStatus, "
						+ " first_form_id as firstFormId, "
						+ "first_form_interval as firstFormInterval, "
						+ " case when first_label_ind ='N' then 'false' else 'true' end as firstLabelIndicator, "
						+ "second_form_id as secondFormId, "
						+ "second_form_interval as secondFormInterval, "
						+ " case when second_label_ind ='N' then 'false' else 'true' end as secondLabelIndicator, "
						+ " third_form_id as thirdFormId, "
						+ " third_form_interval as thirdFormInterval, "
						+ " case when third_label_ind ='N' then 'false' else 'true' end as thirdLabelIndicator, "
						+ " note as note, "
						+ " case when delete_ind ='N' then 'false' else 'true' end as isDeleted "
						+ " FROM  recall_plan_mstr  "
					+ " ORDER BY [description] , [recall_plan_id] ASC" ;
						
				
						
		
				//
				String[] jsonMetaData = {"id", "description","practiceId","eventId","allowEventOverride","resourceId","allowResourceOverride","locationId", "allowLocationOverride","eventScheduleDays", "cancelPlanStatus" ,"firstFormId", "firstFormInterval" ,"firstLabelIndicator", "secondFormId" , "secondFormInterval","secondLabelIndicator","thirdFormId","thirdFormInterval","thirdLabelIndicator", "note", "isDeleted" };

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
