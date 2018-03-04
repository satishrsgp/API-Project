package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_UpdateExcludedVaccine {


	public static Boolean Chart_UpdateExcludedVaccine_Test(HashMap<String, String> parameters, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strSQLQuery = "select datediff(second,create_timestamp,modify_timestamp) as diff,modified_by as mb  "
					+ "from imm_vaccine_exclusion "
					+ "WHERE person_id = '"+parameters.get("personId")+"' AND "
					+ "exclusion_id = '"+parameters.get("exclusionId")+"'";
					
			Object objIntDateDiff = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "diff");
			Object objIntModifiedBy = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "mb");
			if(Integer.valueOf(objIntDateDiff.toString()) > 0)
			{
				if(Objects.equals(objIntModifiedBy.toString(), System.getProperty("LoggedInUserId")))
				{
					String strQuery1 = "select top 1 "
							+ "exclusion_id as id, "
							+ "person_id as personId, "
							+ "enterprise_id as enterpriseId, " 
							+ "practice_id as practiceId, "
							+ "cvx as cvxCode, "
							+ "vaccine_name as name, "
							+ "reason as reasons, "
							+ "start_date as startDate, "
							+ "end_date as endDate, "
							+ "comment as comment "
							+ "from imm_vaccine_exclusion "
							+ "WHERE person_id = '"+parameters.get("personId")+"' AND "
							+ "exclusion_id = '"+parameters.get("exclusionId")+"' "
							+ "Order by modify_timestamp desc";
					//ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
					ArrayList<Object> listTestData  = new ArrayList<>();
					listTestData.add(Objects.equals(parameters.get("exclusionId"), "") ? null: parameters.get("exclusionId").toString().toUpperCase());
					listTestData.add(Objects.equals(parameters.get("personId"), "") ? null : (parameters.get("personId").toString().toUpperCase()));
					listTestData.add(Objects.equals(parameters.get("enterpriseId"), "") ? null: parameters.get("enterpriseId"));
					listTestData.add(Objects.equals(parameters.get("practiceId"), "") ? null: parameters.get("practiceId"));
					listTestData.add(Objects.equals(parameters.get("CvxCode"), "") ? null: parameters.get("CvxCode"));
					listTestData.add(Objects.equals(parameters.get("name"), "") ? null: parameters.get("name"));
					listTestData.add(Objects.equals(parameters.get("Reasons"), "") ? null : (parameters.get("Reasons")));
					listTestData.add(Objects.equals(parameters.get("StartDate"), "") ? null: parameters.get("StartDate"));
					listTestData.add(Objects.equals(parameters.get("EndDate"), "") ? null : (parameters.get("EndDate")));
					listTestData.add(Objects.equals(parameters.get("Comment"), "") ? null : (parameters.get("Comment")));
					
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
