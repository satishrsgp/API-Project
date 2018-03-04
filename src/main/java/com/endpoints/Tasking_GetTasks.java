package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Tasking_GetTasks
{

	public static Boolean tasking_GetTasks_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				/*String strQuery = "select "
						+ "pex.person_id,pex.ethnicity_item_id,ml.mstr_list_item_desc'description',pex.ethnicity_order,ecm.code'categoryId',ml.mstr_list_item_desc 'CategoryName',ecm.code'cdcEthnicityCode'"
						+ "from person_ethnicity_xref pex "
						+ "inner join ethnicity_category_mapping ecm on pex.ethnicity_item_id=ecm.mstr_list_item_id "
						+ "inner join mstr_lists ml on ml.mstr_list_item_id=pex.ethnicity_item_id "
						+ "where pex.person_id='"+parameters.get("personId")+"'";*/
				
				String strQuery = "SELECT "
						   + "UTL.task_id as id, "
					       + "UTL.enterprise_id as enterpriseId, "
					       + "UTL.practice_id as practiceId, "
					       + "UTL.user_id as userId, "
					       + "UTL.task_priority as priorityId, "
					       + "Case when task_priority = '1' then 'High' when task_priority = '2' then 'Normal' when task_priority = '3' then 'Low' else 'Invalid' end as priority, "
					       + "UTL.task_subj as subject,"
					       + "UTL.task_desc as description,"
					       + "case when UTL.task_assgn is null then 'Pending' when UTL.rejected_ind = 'Y' then 'Rejected' when UTL.task_completed = 'Y' then 'Completed' when UTL.task_assgn = 'A' then 'Accepted' when UTL.task_assgn = 'G' then 'Assigned' when UTL.task_assgn = 'Y' then 'Assigned' else 'Pending' end as status,"
					       + "case when UTL.todo_task_type is null then 'None' when UTL.todo_task_type = '' then 'None' else UTL.todo_task_type end as type,"
					       + "UTL.task_owner as ownerId,"
					       + "UTL.task_due_date as dueDate,"
					       + "case when UTL.read_flag = 'Y' then 'true' else 'false' end as isRead,"
					       + "case when UTL.task_assgn = 'Y' then 'true' when UTL.task_assgn = 'Y' then 'true' else 'false' end as isAssigned,"
					       + "case when UTL.task_assgn = 'A' then 'true' else 'false' end  as isAccepted,"
					       + "case when UTL.rejected_ind = 'Y' then 'true' else 'false' end as isRejected,"
					       + "case when UTL.task_completed = '0' then 'false' else 'true' end  as isCompleted,"
					       + "case when UTL.task_deleted = 'Y' then 'true' else 'false' end  as isDeleted,"
					       + "UTL.pat_acct_id as personId,"
					       + "P.first_name as personFirstName,"
					       + "P.middle_name as personMiddleName,"
					       + "P.last_name as personLastName,"
					      // + "case when UTL.pat_acct_id = null then 'false' else 'true' end as   patientHasChartInCurrentPractice,"
					       + "UTL.pat_enc_id as encounterId,"
					       + "UTL.created_by as createdBy,"
					       + "UTL.create_timestamp as createTimestamp,"
					       + "UTL.modified_by as modifiedBy,"
					       + "UTL.modify_timestamp as modifyTimestamp "
					       + "from "
					       + "user_todo_list UTL "
					       + "Left outer join person P "
					       + "on UTL.[pat_acct_id] = P.[person_id]"
					       + "where task_id in ("
					       + "Select task_id from "
					       + "todo_categ_xref where  "
					       + "categ_id in (Select categ_id from dbo.todo_tab_xref where tab_id in "
					       + "(Select tab_id from  "
					       + "todo_tabs where user_id = '"+parameters.get("userId")+"' And tab_id = '"+parameters.get("setId")+"'))) "
					       + "AND UTL.user_id = '"+parameters.get("userId")+"' "
					       + "AND UTL.enterprise_id = '"+System.getProperty("LoggedInEnterpriseId")+"' "
					       + "AND UTL.practice_id = '"+System.getProperty("LoggedInPracticeId")+"'  ";
					        
				String[] jsonMetaData = {"id","enterpriseId","practiceId","userId","priorityId","priority","subject",
						"description","status","type","ownerId","dueDate;Date_YYYYMMDD HH:MM:SS","isRead","isAssigned","isAccepted","isRejected",
						"isCompleted","isDeleted","personId","personFirstName","personMiddleName","personLastName",
						//"patientHasChartInCurrentPractice",
						"encounterId","createdBy","createTimestamp;Date_YYYYMMDD HH:MM:SS","modifiedBy","modifyTimestamp;Date_YYYYMMDD HH:MM:SS"};
				
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..id", jsonList);
					//objHandler = FinalAssertions.assertResponseCount(jsonMetaData, objHandler, DatabaseConnection.statementExecution(connNGA, strQuery), "id");
				} 
				catch (Exception e)
				{
					e.printStackTrace();
					Log.info("Error in Test Class and the detail are : \n" + e.getMessage());
					softAssert.fail("Error in Test Script please look at logs");
					//objHandler.setStrRequestStatus("Fail");
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			status = true;
		}
		
		return status;
	}
}
