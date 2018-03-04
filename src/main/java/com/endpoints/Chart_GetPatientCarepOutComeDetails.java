package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetPatientCarepOutComeDetails
{
	public static Boolean chart_GetPatientCarepOutComeDetails_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				
				String strQuery = "Select "
						+ "person_id as personId,enc_id as encounterId,enterprise_id as enterpriseId,practice_id as practiceId, "
						+ "Case when del_ind = 'Y' then 'true' else 'false' end as isDeleted,"
						+ "case when goal_achieved = 'Yes' then 'true' else 'false' end  as goalAchieved, " 
						+ "goal_achieved_details as goalAchievedDetails,goal_num as goalId,health_concern_num as healthConcernId, "
						+ "intervention_num as interventionId,outcome as description,outcome_date as outcomeDate,outcome_num as OutcomeId, "
						+ "created_by as createdBy ,create_timestamp as createTimestamp,create_timestamp_tz as createTimestampTimeZone, "
						+ "modified_by as modifiedBy,modify_timestamp as modifyTimestamp,modify_timestamp_tz as modifyTimestampTimeZone "
						+ "FROM care_plan_outcomes_ext_ "
						+ "WHERE person_id = '"+parameters.get("personId")+"'  "
						+ "AND enc_id = '"+parameters.get("encounterId")+"'  "
						+ "AND health_concern_num = '"+parameters.get("healthConcernId")+"'  "
						+ "AND goal_num ='"+parameters.get("goalId")+"'  " 
						+ "AND intervention_num = '"+parameters.get("interventionId")+"'"
						+ "AND outcome_num = '"+parameters.get("outcomeId")+"'"
						+ "and del_ind != 'Y'"; 
												

				String[] jsonMetaData = {"personId","encounterId","enterpriseId","practiceId","isDeleted","goalAchieved","goalAchievedDetails","goalId",
						"healthConcernId","interventionId","description","outcomeDate","outcomeId","createdBy","createTimestamp;Date_YYYYMMDD HH:MM:SS","createTimestampTimeZone",
						"modifiedBy","modifyTimestamp;Date_YYYYMMDD HH:MM:SS",	"modifyTimestampTimeZone"};
				
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..personId", jsonList);
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
