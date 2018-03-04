package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Users_UpdateFavoriteDiagnosesGr
{
	public static Boolean Users_UpdateFavoriteDiagnosesGr_Test(HashMap<String, String> parameters, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strSQLQuery = "select datediff(second,create_timestamp,modify_timestamp) as diff,modified_by as mb "
					+ "from diagnosis_favorite_mstr WHERE user_id = '"+parameters.get("userId")+"' and diag_favorite_id='"+parameters.get("groupId")+"'";
			Object objIntDateDiff = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "diff");
			Object objIntModifiedBy = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "mb");
			if(Integer.valueOf(objIntDateDiff.toString()) > 0)
			{
				if(Objects.equals(objIntModifiedBy.toString(), System.getProperty("LoggedInUserId")))
				{
					String strQuery1 =   "select "
						+"  user_id as userId ,diag_favorite_id as id,diag_favorite_name as groupName,seq_no as sequenceNumber "
						+" from diagnosis_favorite_mstr "
						+ "where user_id='"+parameters.get("userId")+"' and diag_favorite_id='"+parameters.get("groupId")+"'";
				
					
					//ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
					ArrayList<Object> listTestData  = new ArrayList<>();
					listTestData.add(parameters.get("userId").toString());
					listTestData.add(parameters.get("groupId").toString());
					//listTestData.add(Objects.equals(parameters.get("userId"), null) ? null : (parameters.get("userId").toString()));		
				
						//listTestData.add(Objects.equals(parameters.get("id"), "") ? null : (parameters.get("id").toString()));
				
						listTestData.add(Objects.equals(parameters.get("groupName"), null) ? null : (parameters.get("groupName").toString()));	
						listTestData.add(Objects.equals(parameters.get("sequenceNumber"), null) ? null : (parameters.get("sequenceNumber").toString()));	
						//listTestData.add(Objects.equals(parameters.get("Cpt4Code"), null) ? null : (parameters.get("Cpt4Code").toString()));	
						//listTestData.add(parameters.get("RecallPlanId").toString());
					
						//listTestData.add(parameters.get("BaseUnit"));
						
						
				
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
