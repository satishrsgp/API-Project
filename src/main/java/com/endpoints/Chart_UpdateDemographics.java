package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_UpdateDemographics
{
	public static Boolean chart_UpdateDemographics_Test(HashMap<String, String> parameters, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strSQLQuery = "select datediff(second,create_timestamp,modify_timestamp) as diff,modified_by as mb "
					+ "from patient "
					+ " where person_id='"+parameters.get("personId")+"' "
					+ "and enterprise_id='" + System.getProperty("LoggedInEnterpriseId") + "'"
					+ "and practice_id = '" + System.getProperty("LoggedInPracticeId")+"'";
			Object objIntDateDiff = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "diff");
			Object objIntModifiedBy = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "mb");
			if(Integer.valueOf(objIntDateDiff.toString()) > 0)
			{
				if(Objects.equals(objIntModifiedBy.toString(), System.getProperty("LoggedInUserId")))
				{
					String strQuery1 = "Select no_known_imm_history_ind "
							+ "from patient"
							+ " where person_id='"+parameters.get("personId")+"' "
							+ "and enterprise_id='" + System.getProperty("LoggedInEnterpriseId") + "'"
							+ "and practice_id = '" + System.getProperty("LoggedInPracticeId")+"'";
					Log.info("strQuery1:"+strQuery1);
					ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
					ArrayList<Object> listTestData  = new ArrayList<>();					
					listTestData.add(Objects.equals(parameters.get("HasNoKnownImmunizationHistory"), "false") ? "N" : "Y");
					Log.info("Parameters:"+parameters);
					Log.info("TestData:\n"+listTestData);
					Log.info("DBData:\n"+listDatabase);
					status = ValidateResults.resultValidation(listTestData, listDatabase);
				}
				else
				{
					Log.error("HasNoKnownImmunizationHistory for patient with person_id ="+parameters.get("personId")+", was not updated correctly-modified_by is not updated with logged in user's credentials.");
					Log.error("Expected:"+System.getProperty("LoggedInUserId")+", Actual:"+objIntModifiedBy.toString());
					softAssert.fail("HasNoKnownImmunizationHistory for patient with person_id ="+parameters.get("personId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				}
			}
			else
			{
				Log.error("HasNoKnownImmunizationHistory for patient with person_id ="+parameters.get("personId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				softAssert.fail("HasNoKnownImmunizationHistory for patient with person_id ="+parameters.get("personId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}




}
