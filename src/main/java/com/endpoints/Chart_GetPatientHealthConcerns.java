package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetPatientHealthConcerns 
{




	public static Boolean chart_GetPatientHealthConcerns_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{

				String strQuery = "select top 25 "
						+ "health_concern_num as healthConcernId,person_id as personId ,encounterID as encounterId,enterprise_id as enterpriseId,practice_id as practiceId, "
						+ "category as category,"
						+ "comments as comments,health_concern as healthConcern, "
						+ "case when child_record_exists = 'N' then 'false'  "
						+ "when isnull(child_record_exists,'1') = '1' then 'false' "
						+ "else 'true' end  as childRecordExists, "
						+ "case when del_ind = 'N' then 'false' else 'true' end as isDeleted, "
						+ "parent_health_concern_nbr as secondaryTo,identified_date as identifiedDate,resolved_date as resolvedDate, "
						+ "code_description as codeDescription,code_system as codeSystem,code_value as codeValue,status as status "
						+ "from health_concern_ext_ "
						+ "Where person_id = '"+parameters.get("personId")+"' and (del_ind is null OR del_ind = 'N')"
						+ "order by category asc,child_record_exists asc,code_description asc,code_system asc,code_value asc,comments asc,encounterID asc,enterprise_id asc,health_concern asc, "
						+ "health_concern_num asc,identified_date asc,del_ind asc,person_id asc,practice_id asc,resolved_date asc,parent_health_concern_nbr asc,status asc ";

				String[] jsonMetaData = {"id","personId","encounterId","enterpriseId","practiceId","category","comments","description","childRecordExists","isDeleted","secondaryTo","identifiedDate","resolvedDate","codeDescription","codeSystem","codeValue","status"};

				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					/*Object obj = JsonPath.read(strResponse, "$.."+strUniqueJsonToken);
					ArrayList<Object> jsonList = new ArrayList<>();

					if(obj instanceof JSONArray)
					{
						for(int i = 0; i< ((JSONArray)obj).size(); i++)
						{
							
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].id"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].id").toString());
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].personId"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].personId").toString().toUpperCase());
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].encounterId"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].encounterId").toString().toUpperCase());
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].enterpriseId"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].enterpriseId").toString());
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].practiceId"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].practiceId").toString());
							
							String Category = JsonPath.read(strResponse, "$.items["+i+"].category");
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
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].comments"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].comments").toString());
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].description"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].description").toString());
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].childRecordExists"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].childRecordExists").toString());
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].isDeleted"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].isDeleted").toString());
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].secondaryTo"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].secondaryTo").toString());
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].identifiedDate"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].identifiedDate").toString().substring(0,10).replace(" T", " ").replace("-", ""));
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].resolvedDate"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].resolvedDate").toString().substring(0,10).replace(" T", " ").replace("-", ""));
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].codeDescription"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].codeDescription").toString());
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].codeSystem"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].codeSystem").toString());
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].codeValue"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].codeValue").toString());
							jsonList.add(Objects.equals(JsonPath.read(strResponse, "$.items["+i+"].status"), null) ? null: JsonPath.read(strResponse, "$.items["+i+"].status").toString());
							
						}
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
