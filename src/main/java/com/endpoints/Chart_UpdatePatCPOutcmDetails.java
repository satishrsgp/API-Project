
package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import org.testng.asserts.SoftAssert;
import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_UpdatePatCPOutcmDetails
{
	public static Boolean updatePatCPOutcmDetails(HashMap<String, String> parameters, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strSQLQuery = "select datediff(second,create_timestamp,modify_timestamp) as diff,modified_by as mb "
									+ "from care_plan_outcomes_ext_ " 
									+ "Where person_id = '"+parameters.get("personId")+"' and enc_id ='"+parameters.get("encounterId")+"' and health_concern_num = '"+parameters.get("healthConcernId")+"' and goal_num = '"+parameters.get("goalId")+"' and  intervention_num = '"+parameters.get("interventionId")+"' and  outcome_num = '"+parameters.get("outcomeId")+"'";
			
			Object objIntDateDiff = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "diff");
			Object objIntModifiedBy = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "mb");
			if(Integer.valueOf(objIntDateDiff.toString()) > 0)
			{
				if(Objects.equals(objIntModifiedBy.toString(), System.getProperty("LoggedInUserId")))
				{
					String strQuery1 = "select "
	                        + "outcome,case when goal_achieved='Yes' then 'true' else 'false' end as goal_achieved,goal_achieved_details,outcome_date "
	                        + "From care_plan_outcomes_ext_ " 
	                        + "Where person_id = '"+parameters.get("personId")+"' and enc_id ='"+parameters.get("encounterId")+"' and health_concern_num = '"+parameters.get("healthConcernId")+"' and goal_num = '"+parameters.get("goalId")+"' and  intervention_num = '"+parameters.get("interventionId")+"' and  outcome_num = '"+parameters.get("outcomeId")+"'";
					
					//ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
					ArrayList<Object> jsonList  = new ArrayList<>();
					 jsonList.add(parameters.get("outcomepayload").toString());
		                jsonList.add(parameters.get("goalAchieved").toString());
		                jsonList.add(parameters.get("goalAchievedDetails").toString());
		                jsonList.add(parameters.get("outcomeDate").toString().replace("-", "").split("T")[0]);
		                
					Log.info("Parameters:"+parameters);
					Log.info("TestData:\n"+jsonList);
					//Log.info("DBData:\n"+listDatabase);
					
					status = ValidateResults.resultsvalidation(jsonList,DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
				}
				else
				{
					Log.error("Outcome with person_id = '"+parameters.get("personId")+"' and enc_id ='"+parameters.get("encounterId")+"' and health_concern_num = '"+parameters.get("healthConcernId")+"' and goal_num = '"+parameters.get("goalId")+"' and  intervention_num = '"+parameters.get("interventionId")+"' and  outcome_num = '"+parameters.get("outcomeId")+", was not updated correctly-modified_by is not updated with logged in user's credentials.");
			//		Log.error("Expected:"+System.getProperty("LoggedInUserId")+", Actual:"+objIntModifiedBy.toString());
					softAssert.fail("Outcome with person_id = '"+parameters.get("personId")+"' and enc_id ='"+parameters.get("encounterId")+"' and health_concern_num = '"+parameters.get("healthConcernId")+"' and goal_num = '"+parameters.get("goalId")+"' and  intervention_num = '"+parameters.get("interventionId")+"' and  outcome_num = '"+parameters.get("outcomeId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				}
			}
			else
			{
				Log.error("Outcome with person_id = '"+parameters.get("personId")+"' and enc_id ='"+parameters.get("encounterId")+"' and health_concern_num = '"+parameters.get("healthConcernId")+"' and goal_num = '"+parameters.get("goalId")+"' and  intervention_num = '"+parameters.get("interventionId")+"' and  outcome_num = '"+parameters.get("outcomeId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				softAssert.fail("Outcome with person_id = '"+parameters.get("personId")+"' and enc_id ='"+parameters.get("encounterId")+"' and health_concern_num = '"+parameters.get("healthConcernId")+"' and goal_num = '"+parameters.get("goalId")+"' and  intervention_num = '"+parameters.get("interventionId")+"' and  outcome_num = '"+parameters.get("outcomeId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}


}

