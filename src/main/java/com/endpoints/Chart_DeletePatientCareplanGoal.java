package com.endpoints;

import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;

public class Chart_DeletePatientCareplanGoal 
{

	public static Boolean Chart_DeletePatientCareplanGoal_Test(HashMap<String, String> parameters, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strSQLQuery = "Select datediff(second,create_timestamp,modify_timestamp) as diff,modified_by as mb "
					+ "from care_plan_goals_ext_ "
					+ "WHERE person_id = '"+parameters.get("personId")+"' AND "
					+ "encounterID = '"+parameters.get("encounterId")+"' AND  "
					+ "health_concern_num = '"+parameters.get("healthConcernId")+"'  "
					+ " AND goal_num = '"+parameters.get("goalId")+"'  ";
					
					
			Object objIntDateDiff = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "diff");
			Object objIntModifiedBy = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "mb");
			if(Integer.valueOf(objIntDateDiff.toString()) > 0)
			{
				if(Objects.equals(objIntModifiedBy.toString(), System.getProperty("LoggedInUserId")))
				{
					String strQuery1 = "Select del_ind from care_plan_goals_ext_ "
							+ "WHERE person_id = '"+parameters.get("personId")+"'  "
							+ "AND encounterID = '"+parameters.get("encounterId")+"' AND  "
							+ "health_concern_num = '"+parameters.get("healthConcernId")+"'  "
							+ "AND goal_num = '"+parameters.get("goalId")+"' ";
							
					String DeleteInd = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strQuery1, "del_ind").toString();
					if (DeleteInd.equals("Y"))
					{
						status = true;
						Log.info("care plan goal deleted for patient with personId : " +parameters.get("personId") + "EncounterID: "+ parameters.get("encounterId"));
					}
					else
					{
						status = false;
						Log.error("care plan goal not deleted for patient with personId : " +parameters.get("personId") + "EncounterID: "+ parameters.get("encounterId"));
						softAssert.fail("care plan goal not deleted for patient with personId : " +parameters.get("personId") + "EncounterID: "+ parameters.get("encounterId"));
					}
					
				}
				else
				{
					Log.error("care plan goal not deleted for patient with personId :="+parameters.get("personId")+", was not Updated correctly-modified_by is not updated with logged in user's credentials.");
					Log.error("Expected:"+System.getProperty("LoggedInUserId")+", Actual:"+objIntModifiedBy.toString());
					softAssert.fail("care plan goal not deleted for patient with personId :="+parameters.get("personId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				}
			}
			else
			{
				Log.error("care plan goal not deleted for patient with personId :="+parameters.get("personId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				softAssert.fail("care plan goal not deleted for patient with personId :="+parameters.get("personId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}




}
