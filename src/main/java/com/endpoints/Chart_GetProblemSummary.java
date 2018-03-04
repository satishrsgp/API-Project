package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetProblemSummary
{

	public static Boolean chart_GetProblemSummary_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = null;
				if(strResponse.contains("$skip=25"))
				{
					strQuery = "select top 25 UPPER(CAST(pp.uniq_id AS CHAR(36))) as id,UPPER(CAST(pp.person_id AS CHAR(36))) as personId,pp.description as description,pp.fully_specified_name as fullySpecifiedName, "
							+"case when pp.chronic_ind='N' then 'false' else 'true' end as isChronic,case when pp.secondary_condition_ind='N' then 'false' else 'true' end as hasSecondaryCondition, "
							+"pp.problem_status_id as problemStatusId,UPPER(CAST(pp.recent_note_id AS CHAR(36))) as recentNoteId,pp.last_addressed_date as lastAddressedDate,pp.onset_date as onsetDate,pp.resolved_date as resolvedDate, "
							+"pp.resolved_reason as resolvedReason,pp.concept_id as conceptId,case when pp.comorbid_condition_ind='N' then 'false' else 'true' end as isComorbid, "
							+"case when pp.delete_ind='N' then 'false' else 'true' end as isDeleted "
							+"from patient_problems pp where pp.person_id='"+parameters.get("personId")+"' and pp.delete_ind='N' order by pp.uniq_id";
				}
				else
				{
					strQuery = "select UPPER(CAST(pp.uniq_id AS CHAR(36))) as id,UPPER(CAST(pp.person_id AS CHAR(36))) as personId,pp.description as description,pp.fully_specified_name as fullySpecifiedName, "
							+"case when pp.chronic_ind='N' then 'false' else 'true' end as isChronic,case when pp.secondary_condition_ind='N' then 'false' else 'true' end as hasSecondaryCondition, "
							+"pp.problem_status_id as problemStatusId,UPPER(CAST(pp.recent_note_id AS CHAR(36))) as recentNoteId,pp.last_addressed_date as lastAddressedDate,pp.onset_date as onsetDate,pp.resolved_date as resolvedDate, "
							+"pp.resolved_reason as resolvedReason,pp.concept_id as conceptId,case when pp.comorbid_condition_ind='N' then 'false' else 'true' end as isComorbid, "
							+"case when pp.delete_ind='N' then 'false' else 'true' end as isDeleted "
							+"from patient_problems pp where pp.person_id='"+parameters.get("personId")+"' and pp.delete_ind='N' order by pp.uniq_id";
				}
				
				String[] jsonMetaData = {"id","personId","description","fullySpecifiedName","isChronic","hasSecondaryCondition","problemStatusId","recentNoteId","lastAddressedDate","onsetDate","resolvedDate","resolvedReason","conceptId","isComorbid","isDeleted"};

				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..id", jsonList);
				} 
				catch (Exception e)
				{
					e.printStackTrace();
					Log.info("Error in Test Class and the detail are : \n" + e.getMessage());
					softAssert.fail("Error in Test Script please look at logs");
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