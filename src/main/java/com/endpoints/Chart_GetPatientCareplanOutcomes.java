package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.JsonPath;
import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.Log;

import net.minidev.json.JSONArray;

public class Chart_GetPatientCareplanOutcomes
{
	public static Boolean chart_GetPatientCareplanOutcomes_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "select top 25 "
						+ "concat(outcome_num,intervention_num,health_concern_num,goal_num) as primKey, "
						+"person_id as personId,enc_id as encounterId,enterprise_id as enterpriseId,practice_id as practiceId, "
						+"Case When del_ind = 'Y' then 'true' else 'false' end as isDeleted, "
						+ "case when goal_achieved = 'Yes' then 'true' else 'false' end as goalAchieved, "
						+"goal_achieved_details as goalAchievedDetails,goal_num as goalId,health_concern_num as healthConcernId, "
						+ "intervention_num as interventionId,outcome as description,outcome_date as outcomeDate,outcome_num as outcomeId, "
						+ "created_by as createdBy,create_timestamp as createTimestamp,create_timestamp_tz as createTimestampTimeZone, "
						+ "modified_by as modifiedBy,modify_timestamp as modifyTimestamp,modify_timestamp_tz as modifyTimestampTimeZone " 
						+"From care_plan_outcomes_ext_ " 
						+"Where person_id = '"+parameters.get("personId")+"' and enc_id ='"+parameters.get("encounterId")+"' and health_concern_num = '"+parameters.get("healthConcernId")+"' and goal_num = '"+parameters.get("goalId")+"' and  intervention_num = '"+parameters.get("interventionId")+"'"
						+ "and del_ind != 'Y'"; 

				Object obj = JsonPath.read(strResponse, "$.."+strUniqueJsonToken);
				ArrayList<Object> jsonList = new ArrayList<>();

				if(obj instanceof JSONArray)
				{
					for(int i = 0; i< ((JSONArray)obj).size(); i++)
					{
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].outcomeId").toString()+JsonPath.read(strResponse, "$.items["+i+"].interventionId").toString() + JsonPath.read(strResponse, "$.items["+i+"].healthConcernId").toString() + JsonPath.read(strResponse, "$.items["+i+"].goalId").toString());
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].personId").toString().toUpperCase());
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].encounterId").toString().toUpperCase());
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].enterpriseId"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].practiceId"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].isDeleted").toString());
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].goalAchieved").toString());
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].goalAchievedDetails"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].goalId"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].healthConcernId"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].interventionId"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].description"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, " $.items["+i+"].outcomeDate"),null) ? null :JsonPath.read(strResponse, " $.items["+i+"].outcomeDate").toString().substring(0,10).replace("T", " ").replace("-", ""));
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].outcomeId"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].createdBy"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, " $.items["+i+"].createTimestamp"),null) ? null :JsonPath.read(strResponse, " $.items["+i+"].createTimestamp").toString().replace("T", " "));
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].createTimestampTimeZone"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].modifiedBy"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, " $.items["+i+"].modifyTimestamp"),null) ? null :JsonPath.read(strResponse, " $.items["+i+"].modifyTimestamp").toString().replace("T", " "));
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].modifyTimestampTimeZone"));
						
					}
				}
				Log.info("JSON LIST is :\n" + jsonList);
				status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$.."+strUniqueJsonToken, jsonList);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				Log.info("Error in Test Class and the detail are : \n" + e.getMessage());
				softAssert.fail("Error in Test Script please look at logs");
			}
		}
		else
		{
			status = true;
		}
		return status;
	}

}
