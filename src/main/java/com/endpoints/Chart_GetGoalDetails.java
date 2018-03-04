package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetGoalDetails {


	public static Boolean Chart_GetGoalDetails_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "select "
						+ " goal_num as goalId,"
						+ "person_id as personId,"
						+ "encounterID as encounterId,"
						+ "health_concern_num as healthConcernId,"
						+ "code_description as codeDescription,"
						+ "code_system as codeSystem,"
						+ "code_value as codeValue,"
						+ "comments as comments,"
						+ "Case when del_ind = 'N' then 'false' else 'true' end as isDeleted,"
						+ "goal as description,"
						+ "case when goal_achieved = 'Yes' then 'true' when goal_achieved = 'No' then 'false' else goal_achieved end  as isGoalAchieved,"
						+ "goal_achieved_details as goalAchievedDetails,"
						+ "goal_completion_date as goalCompletionDate,"
						+ "goal_start_date as goalStartDate,"
						+ "case when patient_goal_ind = '1' then 'true' else 'false' end as isPatientGoal,"
						+ "case when patient_priority = 'High priority' then 'HighPriority' when  patient_priority = 'Delayed priority' then 'DelayedPriority'  when  patient_priority = 'Normal priority' then 'NormalPriority'end as patientPriority,"
						+ "case when provider_goal_ind = '1' then 'true' else 'false' end as isProviderGoal,"
						+ "case when provider_priority = 'High priority' then 'HighPriority' when  patient_priority = 'Delayed priority' then 'DelayedPriority'  when  patient_priority = 'Normal priority' then 'NormalPriority'end as providerPriority,"
						+ "documented_by as documentedBy,"
						+ "encounterDate as encounterDate,"
						+ "seq_no as sequenceNumber,"
						+ "created_by as createdBy,"
						+ "create_timestamp as createTimestamp,"
						+ "modified_by as modifiedBy,"
						+ "modify_timestamp as modifyTimestamp,"
						+ "create_timestamp_tz as createTimestampTimezone,"
						+ "modify_timestamp_tz as modifyTimestampTimezone"
						+ " From care_plan_goals_ext_ "
						+ "where person_id = '"+parameters.get("personId")+"'"
						+ " AND encounterID = '"+parameters.get("encounterId")+"'"
						+ " AND health_concern_num  = '"+parameters.get("healthConcernId")+"'"
						+ " AND goal_num = '"+parameters.get("goalId")+"'"
						+ " AND del_ind = 'N'";
						
				
							
				String[] jsonMetaData = {"goalId","personId","encounterId","healthConcernId","codeDescription",
						"codeSystem","codeValue","comments","isDeleted","description","isGoalAchieved","goalAchievedDetails",
						"goalCompletionDate;Date_YYYYMMDD","goalStartDate;Date_YYYYMMDD","isPatientGoal","patientPriority","isProviderGoal","providerPriority",
						"documentedBy","encounterDate;Date_YYYYMMDD","sequenceNumber","createdBy","createTimestamp;Date_YYYYMMDD HH:MM:SS","modifiedBy","modifyTimestamp;Date_YYYYMMDD HH:MM:SS",
						"createTimestampTimezone","modifyTimestampTimezone"};
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
