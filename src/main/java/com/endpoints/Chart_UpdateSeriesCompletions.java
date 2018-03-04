package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_UpdateSeriesCompletions
{
	public static Boolean chart_UpdateSeriesCompletions_Test(HashMap<String, String> parameters, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strSQLQuery = "select datediff(second,create_timestamp,modify_timestamp) as diff,modified_by as mb "
					+ "from imm_series_complete where person_id='"+parameters.get("personId")+"' and  series_complete_id= '"+parameters.get("seriesId")+"'";
			Object objIntDateDiff = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "diff");
			Object objIntModifiedBy = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "mb");
			if(Integer.valueOf(objIntDateDiff.toString()) > 0)
			{
				if(Objects.equals(objIntModifiedBy.toString(), System.getProperty("LoggedInUserId")))
				{
					String strQuery1 =  " SELECT top 1 series_complete_id AS id, "			   
							+ " person_id AS personId,"
							+  "   enterprise_id AS enterpriseId, "
							+ "    practice_id AS practiceId, "
							+ "  series_name AS seriesName, "		
							//+ "CASE WHEN series_complete_ind = 'false' THEN 'N' "
							//  + "ELSE 'Y' END AS isComplete, "
							+ "  series_complete_ind AS isComplete, "		
							+ "  comment AS comment "
							+ " FROM imm_series_complete  "
							+ "  WHERE person_id = '"+parameters.get("personId")+"' and series_complete_id= '"+parameters.get("seriesId")+"'  order by create_timestamp desc";
					//ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
					ArrayList<Object> listTestData  = new ArrayList<>();
					listTestData.add(Objects.equals(parameters.get("seriesId"), null) ? null : (parameters.get("seriesId").toString()));		

					listTestData.add(Objects.equals(parameters.get("personId"), "") ? null : (parameters.get("personId").toString()));

					listTestData.add(Objects.equals(parameters.get("enterpriseId"), null) ? null : (parameters.get("enterpriseId").toString()));	
					listTestData.add(Objects.equals(parameters.get("practiceId"), null) ? null : (parameters.get("practiceId").toString()));	
					listTestData.add(Objects.equals(parameters.get("seriesName"), null) ? null : (parameters.get("seriesName").toString()));	
					//listTestData.add(parameters.get("RecallPlanId").toString());
					listTestData.add(Objects.equals(parameters.get("isComplete"), "false") ? "N" : "Y");
					listTestData.add(Objects.equals(parameters.get("comment"), null) ? null : (parameters.get("comment").toString()));	


					//  listTestData.add(Objects.equals(parameters.get("Teeth"), "") ? null : (parameters.get("Teeth").toString()));	
					Log.info("Parameters:"+parameters);
					Log.info("TestData:\n"+listTestData);
					//Log.info("DBData:\n"+listDatabase);
					//status = ValidateResults.resultValidation(listTestData, listDatabase);
					status = ValidateResults.resultsvalidation(listTestData, DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
				}
				else
				{
					Log.error("Procedure note with problemId="+parameters.get("procedureId")+", was not updated correctly-modified_by is not updated with logged in user's credentials.");
					Log.error("Expected:"+System.getProperty("LoggedInUserId")+", Actual:"+objIntModifiedBy.toString());
					softAssert.fail("Problem note with problemId="+parameters.get("problemId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				}
			}
			else
			{
				Log.error("Procedure with problemId="+parameters.get("procedureId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				softAssert.fail("Problem note with problemId="+parameters.get("procedureId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}
}
