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
public class Chart_GetGoals 
{

	public static Boolean Chart_GetGoals_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{

				String strQuery = "select top 25 concat(goal_num,health_concern_num) as primKey, "
						+ "goal_num as goalId,person_id as personId,encounterID as encounterId,health_concern_num as healthConcernId, "
						+ "code_description as codeDescription,code_system as codeSystem,code_value as codeValue,comments as comments, "
						+ "case when del_ind = 'N' then 'false' else 'true' end as isDeleted,goal as goal, "
						+ "case when goal_achieved = 'No' then 'false' else 'true' end as isGoalAchieved, "
						+ "goal_achieved_details as goalAchievedDetails,goal_completion_date as goalCompletionDate, "
						+ "goal_start_date as goalStartDate, "
						+ "case when patient_goal_ind = 0 then 'false' else 'true' end as isPatientGoal,"
						//+ "case when patient_priority = 'High priority' then 'HighPriority'  "
						//+ "when patient_priority = 'Normal priority' then 'Normal`Priority'  "
						//+ "when patient_priority = 'Delayed priority' then 'DelayedPriority'  "
						//+ "end as patientPriority, "
						+ "patient_priority as patientPriority,"
						+ "case when provider_goal_ind = 0 then 'false'else 'true' end as isProviderGoal, "
						//+ "case when provider_priority = 'High priority' then 'HighPriority' "
						//+ "when provider_priority = 'Normal priority' then 'NormalPriority' "
						//+ "when provider_priority = 'Delayed priority' then 'DelayedPriority' "
						//+ "end as providerPriority   "
						+ " provider_priority as providerPriority"
						+ "from care_plan_goals_ext_ "
						+ "Where person_id = '"+parameters.get("personId")+"' and encounterID = '"+parameters.get("encounterId")+"' and health_concern_num = '"+parameters.get("healthConcernId")+"' and del_ind = 'N'"
						+ "Order by goal_num asc,person_id asc,encounterID asc,health_concern_num asc,code_description asc,code_system asc,code_value asc,comments asc,del_ind asc, "
						+ "goal asc,goal_achieved asc,goal_achieved_details asc,goal_completion_date asc,goal_start_date asc,patient_goal_ind asc,patient_priority asc, "
						+ "provider_goal_ind asc,provider_priority asc ";

				Object obj = JsonPath.read(strResponse, "$.."+strUniqueJsonToken);
				ArrayList<Object> jsonList = new ArrayList<>();

				if(obj instanceof JSONArray)
				{
					for(int i = 0; i< ((JSONArray)obj).size(); i++)
					{
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].goalId").toString() + JsonPath.read(strResponse, "$.items["+i+"].healthConcernId").toString());
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].goalId"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].personId").toString().toUpperCase());
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].encounterId").toString().toUpperCase());
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].healthConcernId"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].codeDescription"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].codeSystem"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].codeValue"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].comments"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].isDeleted").toString());
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].description"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].isGoalAchieved").toString());
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].goalAchievedDetails"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].goalCompletionDate"),null) ? null :JsonPath.read(strResponse, "$.items["+i+"].goalCompletionDate").toString().substring(0,10).replace(" T", " ").replace("-", ""));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, " $.items["+i+"].goalStartDate"),null) ? null :JsonPath.read(strResponse, " $.items["+i+"].goalStartDate").toString().substring(0,10).replace(" T", " ").replace("-", ""));
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].isPatientGoal").toString());
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].patientPriority"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].isProviderGoal").toString());
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].providerPriority"));
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
