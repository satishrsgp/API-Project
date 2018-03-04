package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_UpdateCPHealthConcern {



	public static Boolean Chart_UpdateCPHealthConcern_Test(HashMap<String, String> parameters, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strSQLQuery = "select datediff(second,create_timestamp,modify_timestamp) as diff,modified_by as mb  "
					+ "from health_concern_ext_ "
					+ "where person_id = '"+parameters.get("personId")+"'"
					+ " AND encounterID = '"+parameters.get("encounterId")+"'"
					+ " AND health_concern_num  = '"+parameters.get("healthConcernId")+"'";
					
			Object objIntDateDiff = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "diff");
			Object objIntModifiedBy = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "mb");
			if(Integer.valueOf(objIntDateDiff.toString()) > 0)
			{
				if(Objects.equals(objIntModifiedBy.toString(), System.getProperty("LoggedInUserId")))
				{
					String strQuery1 = "select "
							+ "comments as comments,"
							+ "category as category, "
							+ "health_concern as description, " 
							+ "parent_health_concern_nbr as secondaryTo, "
							+ "identified_date as identifiedDate, "
							+ "resolved_date as resolvedDate,"
							+ "status as status "
							+ "from health_concern_ext_ "
							+ "where person_id = '"+parameters.get("personId")+"'"
							+ " AND encounterID = '"+parameters.get("encounterId")+"'"
							+ " AND health_concern_num  = '"+parameters.get("healthConcernId")+"'"
							+ " AND del_ind = 'N'";
					
					//ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
					ArrayList<Object> listTestData  = new ArrayList<>();
					listTestData.add(Objects.equals(parameters.get("comments"), "") ? null: parameters.get("comments").toString());
					switch (parameters.get("category")) {
					case "Allergy" :
						listTestData.add("Allergy Intolerance Observation");
						break;
					case "AssessmentScale" :
						listTestData.add("Assessment Scale Observation");
						break;
					case "ProblemObservation" :
						listTestData.add("Problem Observation");
						break;
					case "EncounterDiagnosis" :
						listTestData.add("Encounter Diagnosis");
						break;
					case "FamilyHistoryOrganizer" :
						listTestData.add("Family History Organizer");
						break;
					case "SmokingStatus" :
						listTestData.add("Smoking Status - Meaningful Use");
						break;
					case "TobaccoUseStatus" :
						listTestData.add("Tobacco Use");
						break;
					case "SocialHistoryTobaccoUseAndExposure" :
						listTestData.add("Social History Observation");
						break;
					case "SocialHistoryExercise" :
						listTestData.add("Social History Observation");
						break;
					case "SocialHistoryAlcoholIntake" :
						listTestData.add("Social History Observation");
						break;
					case "SocialHistoryEmploymentDetails" :
						listTestData.add("Social History Observation");
						break;
					case "SocialHistoryDrugMisuseBehavior" :
						listTestData.add("Social History Observation");
						break;
					case "SocialHistoryEducationalAchievement" :
						listTestData.add("Social History Observation");
						break;
					case "VitalsBloodPressure" :
						listTestData.add("Vitals - Blood Pressure");
						break;
					case "VitalsHeadCircumference" :
						listTestData.add("Vitals - Head Circumference");
						break;
					case "VitalsHeartRate" :
						listTestData.add("Vitals - Heart Rate");
						break;
					case "VitalsHeight" :
						listTestData.add("Vitals - Height");
						break;
					case "VitalsWeight" :
						listTestData.add("Vitals - Weight");
						break;
					case "VitalsBmi" :
						listTestData.add("Vitals - BMI");
						break;
					case "VitalsO2BldCOximetry" :
						listTestData.add("Vitals - O2 % BldC Oximetry");
						break;
					case "VitalsRespiratoryRate" :
						listTestData.add("Vitals - Respiratory Rate");
						break;
					case "VitalsBodyTemperature" :
						listTestData.add("Vitals - Body Temperature");
						break;
					case "Other" :
						listTestData.add("Other");
						break;

					default:
						listTestData.add(parameters.get("category").toString());
						break;
					}
					
					listTestData.add(Objects.equals(parameters.get("description"), "") ? null: parameters.get("description").toString());
					listTestData.add(Objects.equals(parameters.get("secondaryTo"), "null") ? null: parameters.get("secondaryTo"));
					listTestData.add(Objects.equals(parameters.get("identifiedDate"), "") ? null : (parameters.get("identifiedDate").toString().substring(0, 10).replace("T", "").replace("-", "")));
					listTestData.add(Objects.equals(parameters.get("resolvedDate"), "") ? null : (parameters.get("resolvedDate").toString().substring(0, 10).replace("T", "").replace("-", "")));
					listTestData.add(Objects.equals(parameters.get("status"), "") ? null: parameters.get("status").toString());

					Log.info("Parameters:"+parameters);
					Log.info("TestData:\n"+listTestData);
					//Log.info("DBData:\n"+listDatabase);
					status = ValidateResults.resultsvalidation(listTestData, DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
				}
				else
				{
					Log.error("Health concern with HeathConcernID ="+parameters.get("healthConcernId")+" for a personID ="+parameters.get("personId")+" , was not updated correctly-modified_by is not updated with logged in user's credentials.");
					Log.error("Expected:"+System.getProperty("LoggedInUserId")+", Actual:"+objIntModifiedBy.toString());
					softAssert.fail("Health concern with HeathConcernID ="+parameters.get("healthConcernId")+" for a personID ="+parameters.get("personId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				}
			}
			else
			{
				Log.error("Health concern with HeathConcernID ="+parameters.get("healthConcernId")+" for a personID ="+parameters.get("personId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				softAssert.fail("Health concern with HeathConcernID ="+parameters.get("healthConcernId")+" for a personID ="+parameters.get("personId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}




	
}
