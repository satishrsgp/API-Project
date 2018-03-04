package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.JsonPath;
import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

import net.minidev.json.JSONArray;

public class Chart_GetPatientCareTeamMembers {
	
	public static Boolean chart_GetPatientCareTeamMembers_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
// 07/26/2017 -- encounterId and isReadOnly  - we can not validate these two token since it does not have any specific column name in the stored procedure
// according to Rishi encounterId will always be Null since they are not populating any data from stored procedure
// whereas isReadOnly will be set to false if Seq_no is present for the row or else it will be true if seq_no is Null		
				String strStmt = "{call ngkbm_udg_care_team_members (?,?,?,?,?,?)}";
				LinkedHashMap <String, Object> strSPParam = new LinkedHashMap<String, Object>();
				strSPParam.put("Design",parameters.get("Design") );
				strSPParam.put("pi_enterprise_id",parameters.get("enterpriseId") );
				strSPParam.put("pi_practice_id",parameters.get("practiceId") );
				strSPParam.put("pi_person_id",parameters.get("personId") );
				strSPParam.put("pi_sort_by",parameters.get("sortBy") );
				strSPParam.put("pi_filter_by",parameters.get("filterBy") );
				
				ArrayList<String> strColumns = new ArrayList<>();
				strColumns.add("seq_no");
				strColumns.add("enterprise_id");
				strColumns.add("practice_id");
				strColumns.add("person_id");
				strColumns.add("provider_id");
				strColumns.add("first_name");
				strColumns.add("last_name");
				strColumns.add("email");
				strColumns.add("active_date");
				strColumns.add("inactive_date");
				strColumns.add("address");
				strColumns.add("city");
				strColumns.add("state");
				strColumns.add("zip");
				strColumns.add("home_phone");
				strColumns.add("cell_phone");
				strColumns.add("fax");
				strColumns.add("agency_name");
				strColumns.add("agency_type");
				strColumns.add("patient_relationship");
				strColumns.add("support_role");
				strColumns.add("provider_specialty");
				
				Object obj = JsonPath.read(strResponse, "$.."+strUniqueJsonToken);
				ArrayList<Object> jsonList = new ArrayList<>();
					
				if(obj instanceof JSONArray)
				{
					for(int i = 0; i< ((JSONArray)obj).size(); i++)
					{
						/*jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].id"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].id").toString().toUpperCase());
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].enterpriseId"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].practiceId"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].personId").toString().toUpperCase());
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].providerId"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].providerId").toString().toUpperCase());
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].firstName"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].firstName"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].lastName"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].lastName"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].email"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].email"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].activeDate"), null) ? "": JsonPath.read(strResponse, "$.items["+i+"].activeDate").toString().substring(0,10).replace(" T", " ").replace("-", ""));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].inactiveDate"), null) ? "": JsonPath.read(strResponse, "$.items["+i+"].inactiveDate").toString().substring(0,10).replace(" T", " ").replace("-", ""));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].address"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].address"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].city"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].city"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].state"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].state"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].zip"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].zip"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].homePhone"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].homePhone"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].cellPhone"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].cellPhone"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].fax"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].fax"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].agencyName"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].agencyName"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].agencyType"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].agencyType"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].patientRelationship"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].patientRelationship"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].supportRole"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].supportRole"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].specialty"), null) ? null :Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].specialty"), "") ? "" : JsonPath.read(strResponse, "$.items["+i+"].specialty"));
						*/
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].enterpriseId"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].practiceId"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].firstName"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].firstName").toString().toLowerCase());
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].lastName"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].lastName"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].agencyName"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].agencyName"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].agencyType"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].agencyType"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].supportRole"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].supportRole"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].patientRelationship"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].patientRelationship"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].specialty"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].specialty"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].address"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].address"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].city"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].city"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].state"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].state"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].zip"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].zip"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].homePhone"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].homePhone"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].cellPhone"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].cellPhone"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].fax"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].fax"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].email"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].email"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].activeDate"), null) ? "": JsonPath.read(strResponse, "$.items["+i+"].activeDate").toString().substring(0,10).replace(" T", " ").replace("-", ""));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].inactiveDate"), null) ? "": JsonPath.read(strResponse, "$.items["+i+"].inactiveDate").toString().substring(0,10).replace(" T", " ").replace("-", ""));
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].personId").toString().toUpperCase());
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].id"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].id").toString().toUpperCase());
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].providerId"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].providerId").toString().toUpperCase());
						//jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].id"), null) ? true: false);
					}
				}
				Log.info("JSON LIST is :\n" + jsonList);
				Map<String , ArrayList<Object>> mList = DatabaseConnection.addSPResultsToArrayList(BaseTest.connNGA, strStmt, strSPParam, strColumns, 0, 25, jsonList);
				status = ValidateResults.resultValidation(mList.get("jsonList"),mList.get("dbList") );
				
				//status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$.."+strUniqueJsonToken, jsonList);
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
