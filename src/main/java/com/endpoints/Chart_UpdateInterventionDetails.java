package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_UpdateInterventionDetails 
{


	public static Boolean Chart_UpdateInterventionDetails_Test(HashMap<String, String> parameters, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strSQLQuery = "select datediff(second,create_timestamp,modify_timestamp) as diff,modified_by as mb  "
					+ "from care_plan_intervention_ext_ "
					+ "where person_id = '"+parameters.get("personId")+"' "
					+ "And goal_num = '"+parameters.get("goalId")+"'  "
					+ "And encounterID ='"+parameters.get("encounterId")+"' "
					+ "And health_concern_num = '"+parameters.get("healthConcernId")+"'  "
					+ "And intervention_num = '"+parameters.get("interventionId")+"'";
					
			Object objIntDateDiff = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "diff");
			Object objIntModifiedBy = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "mb");
			if(Integer.valueOf(objIntDateDiff.toString()) > 0)
			{
				if(Objects.equals(objIntModifiedBy.toString(), System.getProperty("LoggedInUserId")))
				{
					String strQuery1 = "Select top 1"
							+ "intervention as name,"
							+ "case when intervention_category = 'Planned Procedure' then 'PlannedProcedure' when intervention_category = 'Medication Activity' then 'MedicationActivity' when intervention_category = 'Immunization Activity' then 'ImmunizationActivity' when intervention_category = 'Nutrition Recommendation' then 'NutritionRecommendation' else intervention_category end  as category,"
							+ "case when pointer_value is null then null else upper(pointer_value) end as categoryId,"
							+ "intervention_status as status,"
							+ "intervention_start_date as startDate,"
							+ "intervention_frequency as frequency,"
							+ "intervention_review_date as nextReviewDate,"
							+ "intervention_progress as interventionProgress,"
							+ "code_system as codeSystem,"
							+ "code_value as codeValue,"
							+ "code_description as codeDescription "
							+ "from care_plan_intervention_ext_ "
							+ "where person_id = '"+parameters.get("personId")+"' "
							+ "And goal_num = '"+parameters.get("goalId")+"'  "
							+ "And encounterID ='"+parameters.get("encounterId")+"' "
							+ "And health_concern_num = '"+parameters.get("healthConcernId")+"'  "
							+ "And intervention_num = '"+parameters.get("interventionId")+"'  "
							+ "order by create_timestamp desc";
					
					//ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
					ArrayList<Object> listTestData  = new ArrayList<>();
					listTestData.add(Objects.equals(parameters.get("name"), "") ? null: parameters.get("name").toString());
					listTestData.add(Objects.equals(parameters.get("category"), "") ? null : (parameters.get("category").toString()));
					listTestData.add(Objects.equals(parameters.get("categoryId"), "") ? null: parameters.get("categoryId").toString().toUpperCase());
					listTestData.add(Objects.equals(parameters.get("status"), "") ? null: parameters.get("status").toString());
					listTestData.add(Objects.equals(parameters.get("startDate"), "") ? null : (parameters.get("startDate").toString().substring(0, 10).replace("T", "").replace("-", "")));
					listTestData.add(Objects.equals(parameters.get("frequency"), "") ? null: parameters.get("frequency"));
					listTestData.add(Objects.equals(parameters.get("nextReviewDate"), "") ? null : (parameters.get("nextReviewDate").toString().substring(0, 10).replace("T", "").replace("-", "")));
					listTestData.add(Objects.equals(parameters.get("interventionProgress"), "") ? null : (parameters.get("interventionProgress").toString()));
					listTestData.add(Objects.equals(parameters.get("codeSystem"), "null") ? null: parameters.get("codeSystem").toString());
					listTestData.add(Objects.equals(parameters.get("codeValue"), "null") ? null: parameters.get("codeValue").toString());
					listTestData.add(Objects.equals(parameters.get("codeDescription"), "null") ? null: parameters.get("codeDescription").toString());
					Log.info("Parameters:"+parameters);
					Log.info("TestData:\n"+listTestData);
					//Log.info("DBData:\n"+listDatabase);
					status = ValidateResults.resultsvalidation(listTestData, DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
				}
				else
				{
					Log.error("Encounter with EncounterId="+parameters.get("encounterId")+", was not updated correctly-modified_by is not updated with logged in user's credentials.");
					Log.error("Expected:"+System.getProperty("LoggedInUserId")+", Actual:"+objIntModifiedBy.toString());
					softAssert.fail("Encounter with EncounterId="+parameters.get("encounterId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				}
			}
			else
			{
				Log.error("Encounter with EncounterId="+parameters.get("encounterId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				softAssert.fail("Encounter with EncounterId="+parameters.get("encounterId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}




}
