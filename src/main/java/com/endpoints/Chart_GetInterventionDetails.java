package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetInterventionDetails
{



	public static Boolean Chart_GetInterventionDetails_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				
				String strQuery = "Select "
						+ "intervention_num as id,health_concern_num as healthConcernId,goal_num as goalId,intervention as detail,intervention_category as category, "
						+ "intervention_frequency as frequency,intervention_progress as progress,intervention_review_date as reviewDate,intervention_start_date as startDate, " 
						+ "intervention_status as status,code_description as codeDescription,code_system as codeSystem,code_value as codeValue,"
						+ "Case when del_ind = 'Y' then 'true' else 'false' end  as isDeleted, "
						+ "person_id as personId,encounterid as encounterId,created_by as createdBy,create_timestamp as createTimestamp,modified_by as modifiedBy, "
						+ "modify_timestamp as modifyTimestamp,create_timestamp_tz as createTimestampTimezone,modify_timestamp_tz as modifyTimestampTimezone "
						+ "From care_plan_intervention_ext_ "
						+ "WHERE person_id = '"+parameters.get("personId")+"'  "
						+ "AND encounterID = '"+parameters.get("encounterId")+"'  "
						+ "AND health_concern_num = '"+parameters.get("healthConcernId")+"'  "
						+ "AND goal_num ='"+parameters.get("goalId")+"'  " 
						+ "AND intervention_num = '"+parameters.get("interventionId")+"'"; 
												

						
				
				String[] jsonMetaData = {"id","healthConcernId","goalId","detail","category","frequency","progress","reviewDate","startDate",
						"status","codeDescription","codeSystem","codeValue","isDeleted","personId","encounterId","createdBy","createTimestamp;Date_YYYYMMDD HH:MM:SS",
						"modifiedBy","modifyTimestamp;Date_YYYYMMDD HH:MM:SS","createTimestampTimezone","modifyTimestampTimezone"};
				
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
