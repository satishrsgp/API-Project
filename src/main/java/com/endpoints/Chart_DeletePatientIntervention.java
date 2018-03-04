package com.endpoints;

import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;

public class Chart_DeletePatientIntervention  
{

	public static Boolean chart_DeletePatientIntervention(HashMap<String, String> parameters, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strSQLQuery = "Select datediff(second,create_timestamp,modify_timestamp) as diff,modified_by as mb "
					+ "from care_plan_outcomes_ext_ "
					+ "WHERE person_id = '"+parameters.get("personId")+"' AND "
					+ "enc_id = '"+parameters.get("encounterId")+"' AND  "
					+ "goal_num = '"+parameters.get("goalId")+"' AND "
					+ "health_concern_num = '"+parameters.get("healthConcernId")+"' AND "
					+ "intervention_num = '"+parameters.get("interventionId")+"'";
				
					
					
			Object objIntDateDiff = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "diff");
			Object objIntModifiedBy = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "mb");
			if(Integer.valueOf(objIntDateDiff.toString()) > 0)
			{
				if(Objects.equals(objIntModifiedBy.toString(), System.getProperty("LoggedInUserId")))
				{
					String strQuery1 = "Select del_ind from care_plan_outcomes_ext_ "
							+ "WHERE person_id = '"+parameters.get("personId")+"' AND "
							+ "enc_id = '"+parameters.get("encounterId")+"' AND  "
							+ "goal_num = '"+parameters.get("goalId")+"' AND "
							+ "health_concern_num = '"+parameters.get("healthConcernId")+"' AND "
							+ "intervention_num = '"+parameters.get("interventionId")+"'";
						
							
					String DeleteInd = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strQuery1, "del_ind").toString();
					if (DeleteInd.equals("Y"))
					{
						status = true;
						Log.info("care plan intervention deleted for patient with personId : " +parameters.get("personId") + "EncounterID: "+ parameters.get("encounterId"));
					}
					else
					{
						status = false;
						Log.error("care plan intervention not deleted for patient with personId : " +parameters.get("personId") + "EncounterID: "+ parameters.get("encounterId"));
						softAssert.fail("care plan  not deleted for patient with personId : " +parameters.get("personId") + "EncounterID: "+ parameters.get("encounterId"));
					}
					
				}
				else
				{
					Log.error("care plan intervention not deleted for patient with personId :="+parameters.get("personId")+", was not Updated correctly-modified_by is not updated with logged in user's credentials.");
					Log.error("Expected:"+System.getProperty("LoggedInUserId")+", Actual:"+objIntModifiedBy.toString());
					softAssert.fail("care plan intervention not deleted for patient with personId :="+parameters.get("personId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				}
			}
			else
			{
				Log.error("care plan intervention not deleted for patient with personId :="+parameters.get("personId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				softAssert.fail("care plan intervention not deleted for patient with personId :="+parameters.get("personId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}




}
