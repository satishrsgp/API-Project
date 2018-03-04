package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Tasking_AddTask
{
	public static Boolean tasking_AddTask_Test(HashMap<String, String> parameters, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strSQLQuery = "select datediff(second,create_timestamp,modify_timestamp) as diff,modified_by as mb "
					+ "from user_todo_list where user_id = '"+parameters.get("userId")+"' and pat_enc_id = '"+parameters.get("EncounterId")+"'";
			//Object objIntDateDiff = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "diff");
			Object objIntModifiedBy = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "mb");
		
				if(Objects.equals(objIntModifiedBy.toString(), System.getProperty("LoggedInUserId")))
				{
					String strQuery1 =  "select top 1 case when ut.task_priority = '1' then 'high' else 'Normal' end as Priority, "
							+ "ut.task_subj as Subject, ut.task_desc as Description,ut.task_due_date as DueDate, ut.pat_item_id as ItemId, "
							+ "ut.pat_item_desc as ItemDescription, ut.pat_item_type as ItemType, ut.pat_enc_id as EncounterId, "
							+ "ut.pat_acct_id as PersonId  from user_todo_list ut  "
							+ " where ut.user_id = '"+System.getProperty("LoggedInUserId")+"' and ut.pat_enc_id = '"+parameters.get("EncounterId")+"'  ORDER BY create_timestamp DESC" ;
					//ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
					ArrayList<Object> listTestData  = new ArrayList<>();
					listTestData.add(Objects.equals(parameters.get("Priority"), null) ? null : (parameters.get("Priority").toString()));		

					listTestData.add(Objects.equals(parameters.get("Subject"), "") ? null : (parameters.get("Subject").toString()));

					listTestData.add(Objects.equals(parameters.get("Description"), null) ? null : (parameters.get("Description").toString()));	
					listTestData.add(Objects.equals(parameters.get("DueDate"), null) ? null : (parameters.get("DueDate").toString()));	
					listTestData.add(Objects.equals(parameters.get("ItemId"), null) ? null : (parameters.get("ItemId").toString()));	
					listTestData.add(Objects.equals(parameters.get("ItemDescription"), null) ? null : (parameters.get("ItemDescription").toString()));	
					listTestData.add(Objects.equals(parameters.get("ItemType"), null) ? null : (parameters.get("ItemType").toString()));	
					listTestData.add(Objects.equals(parameters.get("EncounterId"), null) ? null : (parameters.get("EncounterId").toString()));	
					listTestData.add(Objects.equals(parameters.get("PersonId"), null) ? null : (parameters.get("PersonId").toString()));	
					listTestData.add(Objects.equals(parameters.get("AssignedCategoryIds"), null) ? null : (parameters.get("AssignedCategoryIds").toString()));	

					
					//  listTestData.add(Objects.equals(parameters.get("Teeth"), "") ? null : (parameters.get("Teeth").toString()));	
					Log.info("Parameters:"+parameters);
					Log.info("TestData:\n"+listTestData);
					//Log.info("DBData:\n"+listDatabase);
					//status = ValidateResults.resultValidation(listTestData, listDatabase);
					status = ValidateResults.resultsvalidation(listTestData, DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
				}
				else
				{
					Log.error("Tasking for the UserId  ="+parameters.get("userId")+", was not updated correctly and modified_by is not updated with logged in user's credentials.");
					Log.error("Expected:"+System.getProperty("LoggedInUserId")+", Actual:"+objIntModifiedBy.toString());
					softAssert.fail("Tasking for the UserId="+parameters.get("userId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				}
			
		

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}
}
