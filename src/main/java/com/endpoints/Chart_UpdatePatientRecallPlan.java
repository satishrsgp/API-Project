package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_UpdatePatientRecallPlan {


	public static Boolean chart_UpdatePatientRecallPlan_Test(HashMap<String, String> parameters, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strSQLQuery = "select datediff(second,create_timestamp,modify_timestamp) as diff,modified_by as mb "
					+ "from patient_recall_plans where person_id='"+parameters.get("personId")+"' and  recall_plan_id= '"+parameters.get("recallPlanId")+"'";
			Object objIntModifiedBy = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "mb");
			
				if(Objects.equals(objIntModifiedBy.toString(), System.getProperty("LoggedInUserId")))
				{
	
					//when stop is updated to false below then active_plan_ind will be true
				String strQuery1 = "";
			    strQuery1 =  "SELECT recall_plan_id,CASE WHEN active_plan_ind = 'N' THEN 'TRUE' else 'FALSE' END as stop,last_date,event_id,resource_id,location_id,note "
			    		+ "FROM patient_recall_plans WHERE person_id = '"+parameters.get("personId")+"' AND recall_plan_id = '"+parameters.get("recallPlanId")+"'" ;

				
			        Log.info("strQuery1:"+strQuery1);
					ArrayList<Object> listTestData  = new ArrayList<>();
					listTestData.add(Objects.equals(parameters.get("RecallPlanId"), "") ? null: parameters.get("recallPlanId").toString());
					listTestData.add(Objects.equals(parameters.get("Stop"), "") ? null: parameters.get("Stop").toString());
			    	//listTestData.add(Objects.equals(parameters.get("StopReasonId"), "") ? null: parameters.get("StopReasonId").toString());
					listTestData.add(Objects.equals(parameters.get("LastDate"), "") ? null: parameters.get("LastDate").substring(0, 10).replace("T", "").replace("-", ""));
					listTestData.add(Objects.equals(parameters.get("EventId"), "") ? null: parameters.get("EventId").toString().toUpperCase());
					listTestData.add(Objects.equals(parameters.get("ResourceId"), "") ? null: parameters.get("ResourceId").toString().toUpperCase());
					listTestData.add(Objects.equals(parameters.get("LocationId"), "") ? null: parameters.get("LocationId").toString().toUpperCase());
					listTestData.add(Objects.equals(parameters.get("Note"), "") ? null: parameters.get("Note").toString());


					Log.info("Parameters:"+parameters);
					Log.info("TestData:\n"+listTestData);
					status = ValidateResults.resultsvalidation(listTestData, DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
				
				

		}
				else{
					
					
				}
			}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	
		
		return status;
	}
}
