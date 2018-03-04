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

public class Chart_GetInterventions
{
	public static Boolean Chart_GetInterventions_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "select top 25 concat(intervention_num,health_concern_num,goal_num) as primKey,"
						+ "intervention_num as id,"
						+ "health_concern_num as healthConcernId,"
						+ "goal_num as goalId,"
						+ "intervention as detail,"
						+ "intervention_category as category,"
						+ "intervention_frequency as frequency,"
						+ "intervention_progress as progress,"
						+ "intervention_review_date as reviewDate,"
						+ "intervention_start_date as startDate,"
						+ "intervention_status as status,"
						+ "code_description as codeDescription,"
						+ "code_system as codeSystem,"
						+ "code_value as codeValue,"
						+ "case when del_ind = 'N' then 'false' else 'true' end as isDeleted,"
						+ "person_id as personId,"
						+ "encounterid as encounterId,"
						+ "created_by as createdBy,"
						+ "create_timestamp as createTimestamp,"
						+ "modified_by as modifiedBy,"
						+ "modify_timestamp as modifyTimestamp,"
						+ "create_timestamp_tz as createTimestampTimezone,"
						+ "modify_timestamp_tz as modifyTimestampTimezone " 
						+"From care_plan_intervention_ext_ " 
						+"Where person_id = '"+parameters.get("personId")+"' and encounterid ='"+parameters.get("encounterId")+"' and health_concern_num = '"+parameters.get("healthConcernId")+"' and goal_num = '"+parameters.get("goalId")+"' and " 
						+"(del_ind is NULL or del_ind='N') " 
						+"Order by intervention_num asc,health_concern_num asc,goal_num asc,intervention asc,intervention_category asc,intervention_frequency asc,intervention_progress asc,intervention_review_date asc, "
						+"intervention_start_date asc, intervention_status asc,code_description asc,code_system asc,code_value asc";

				Object obj = JsonPath.read(strResponse, "$.."+strUniqueJsonToken);
				ArrayList<Object> jsonList = new ArrayList<>();

				if(obj instanceof JSONArray)
				{
					for(int i = 0; i< ((JSONArray)obj).size(); i++)
					{
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].id").toString() + JsonPath.read(strResponse, "$.items["+i+"].healthConcernId").toString() + JsonPath.read(strResponse, "$.items["+i+"].goalId").toString());
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].id"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].id"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].healthConcernId"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].healthConcernId"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].goalId"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].goalId"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].detail"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].detail"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].category"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].category"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].frequency"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].frequency"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].progress"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].progress"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].reviewDate"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].reviewDate").toString().substring(0,10).replace("T", "").replace("-", ""));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].startDate"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].startDate").toString().substring(0,10).replace("T", "").replace("-", ""));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].status"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].status"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].codeDescription"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].codeDescription"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].codeSystem"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].hccDescription"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].codeValue"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].codeSystem"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].isDeleted"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].isDeleted").toString());
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].personId"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].personId").toString().toUpperCase());
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].encounterId"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].encounterId").toString().toUpperCase());
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].createdBy"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].createdBy"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].createTimestamp"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].createTimestamp").toString().replaceAll("T", " "));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].modifiedBy"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].modifiedBy"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].modifyTimestamp"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].modifyTimestamp").toString().replaceAll("T", " "));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].createTimestampTimezone"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].createTimestampTimezone").toString().split("\\.")[0]);
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].modifyTimestampTimezone"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].modifyTimestampTimezone").toString().split("\\.")[0]);
						
						//jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].id"));
						//jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].healthConcernId"));
						//jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].goalId"));
						//jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].detail"));
						//jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].category"));
						//jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].frequency"));
						//jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].progress"));
						//jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].reviewDate").toString().substring(0,10).replace("T", "").replace("-", ""));
						//jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].startDate").toString().substring(0,10).replace("T", "").replace("-", ""));
						//jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].status"));
						//jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].codeDescription"));
						//jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].codeSystem"));
						//jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].codeValue"));
						//jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].isDeleted").toString());
						//jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].personId").toString().toUpperCase());
						//jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].encounterId").toString().toUpperCase());
						//jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].createdBy"));
						//jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].createTimestamp").toString().replaceAll("T", " "));
						//jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].modifiedBy"));
						//jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].modifyTimestamp").toString().replaceAll("T", " "));
						//jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].createTimestampTimezone").toString().split("\\.")[0]);
						//jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].modifyTimestampTimezone").toString().split("\\.")[0]);
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
