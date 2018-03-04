package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetPatientHealthConcernDetails 
{




	public static Boolean chart_GetPatientHealthConcernDetails_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				
				String strQuery = "Select "
						+ "health_concern_num as healthConcernId,encounterID as encounterId,person_Id as personId,enterprise_id as enterpriseId,practice_id as practiceId, "
						+ "category as category,comments as comments,health_concern as healthConcern, "
						+ "case when child_record_exists = 1 then 'true' else 'false' end as childRecordExists, " 
						+ "case when del_ind = 'N' then 'false' else 'true' end as isDeleted, "
						+ "parent_health_concern_nbr as secondaryTo,identified_date as identifiedDate,resolved_date as resolvedDate, "
						+ "code_description as codeDescription,code_system as codeSystem,code_value as codeValue,status as status "
						+ "FROM health_concern_ext_  "
						+ "WHERE person_id = '"+parameters.get("personId")+"' AND  "
						+ "encounterID = '"+parameters.get("encounterId")+"' AND  "
						+ "health_concern_num='"+parameters.get("healthConcernId")+"' ";
						
				
				String[] jsonMetaData = {"id","encounterId","personId","enterpriseId","practiceId","category","comments","description","childRecordExists",
						"isDeleted","secondaryTo","identifiedDate","resolvedDate","codeDescription","codeSystem","codeValue","status"};
				
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					/*Object obj = JsonPath.read(strResponse, "$.."+strUniqueJsonToken);
					ArrayList<Object> jsonList = new ArrayList<>();

					if(obj instanceof JSONArray)
					{
						
							
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.id"), null) ? null: JsonPath.read(strResponse, "$.id").toString());
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.encounterId"), null) ? null: JsonPath.read(strResponse, "$.encounterId").toString().toUpperCase());
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.personId"), null) ? null: JsonPath.read(strResponse, "$.personId").toString().toUpperCase());							
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.enterpriseId"), null) ? null: JsonPath.read(strResponse, "$.enterpriseId").toString());
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.practiceId"), null) ? null: JsonPath.read(strResponse, "$.practiceId").toString());
							
							String Category = JsonPath.read(strResponse, "$.category");
							switch (Category) {
							case "Allergy" :
								jsonList.add("Allergy Intolerance Observation");
								break;
							case "AssessmentScale" :
								jsonList.add("Assessment Scale Observation");
								break;
							case "ProblemObservation" :
								jsonList.add("Problem Observation");
								break;
							case "EncounterDiagnosis" :
								jsonList.add("Encounter Diagnosis");
								break;
							case "FamilyHistoryOrganizer" :
								jsonList.add("Family History Organizer");
								break;
							case "SmokingStatus" :
								jsonList.add("Smoking Status - Meaningful Use");
								break;
							case "TobaccoUseStatus" :
								jsonList.add("Tobacco Use");
								break;
							case "SocialHistoryTobaccoUseAndExposure" :
								jsonList.add("Social History Observation");
								break;
							case "SocialHistoryExercise" :
								jsonList.add("Social History Observation");
								break;
							case "SocialHistoryAlcoholIntake" :
								jsonList.add("Social History Observation");
								break;
							case "SocialHistoryEmploymentDetails" :
								jsonList.add("Social History Observation");
								break;
							case "SocialHistoryDrugMisuseBehavior" :
								jsonList.add("Social History Observation");
								break;
							case "SocialHistoryEducationalAchievement" :
								jsonList.add("Social History Observation");
								break;
							case "VitalsBloodPressure" :
								jsonList.add("Vitals - Blood Pressure");
								break;
							case "VitalsHeadCircumference" :
								jsonList.add("Vitals - Head Circumference");
								break;
							case "VitalsHeartRate" :
								jsonList.add("Vitals - Heart Rate");
								break;
							case "VitalsHeight" :
								jsonList.add("Vitals - Height");
								break;
							case "VitalsWeight" :
								jsonList.add("Vitals - Weight");
								break;
							case "VitalsBmi" :
								jsonList.add("Vitals - BMI");
								break;
							case "VitalsO2BldCOximetry" :
								jsonList.add("Vitals - O2 % BldC Oximetry");
								break;
							case "VitalsRespiratoryRate" :
								jsonList.add("Vitals - Respiratory Rate");
								break;
							case "VitalsBodyTemperature" :
								jsonList.add("Vitals - Body Temperature");
								break;
							case "Other" :
								jsonList.add("Other");
								break;

							default:
								jsonList.add(parameters.get("category").toString());
								break;
							}
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.comments"), null) ? null: JsonPath.read(strResponse, "$.comments").toString());
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.description"), null) ? null: JsonPath.read(strResponse, "$.description").toString());
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.childRecordExists"), null) ? null: JsonPath.read(strResponse, "$.childRecordExists").toString());
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.isDeleted"), null) ? null: JsonPath.read(strResponse, "$.isDeleted").toString());
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.secondaryTo"), null) ? null: JsonPath.read(strResponse, "$.secondaryTo").toString());
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.identifiedDate"), null) ? null: JsonPath.read(strResponse, "$.identifiedDate").toString().substring(0,10).replace(" T", " ").replace("-", ""));
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.resolvedDate"), null) ? null: JsonPath.read(strResponse, "$.resolvedDate").toString().substring(0,10).replace(" T", " ").replace("-", ""));
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.codeDescription"), null) ? null: JsonPath.read(strResponse, "$.codeDescription").toString());
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.codeSystem"), null) ? null: JsonPath.read(strResponse, "$.codeSystem").toString());
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.codeValue"), null) ? null: JsonPath.read(strResponse, "$.codeValue").toString());
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.status"), null) ? null: JsonPath.read(strResponse, "$.status").toString());
							
						}*/
					
					
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
