package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetProblemDetails
{

	public static Boolean Chart_GetProblemDetails_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "SELECT UPPER(CAST(pp.uniq_id AS CHAR(36))) as id,pp.enterprise_id as enterpriseId,pp.practice_id as racticeId,UPPER(CAST(pp.person_id AS CHAR(36))) as personId,pp.description, "
						+"pp.concept_id as conceptId,pp.side_id as iteId,pp.site as site ,pp.onset_date as onsetDate,pp.last_addressed_date as lastAddressedDate,pp.resolved_date as resolvedDate, "
						+"pp.resolved_reason as resolvedReason,pp.resolved_by as resolvedBy,pp.problem_status_id as problemStatusId,pp.clinical_status_id as clinicalStatusId, "
						+"case when pp.chronic_ind='N' then 'false' else 'true' end as isChronic,case when pp.secondary_condition_ind='N' then 'false' else 'true' end as hasSecondaryCondition, "
						+"case when pp.delete_ind='N' then 'false' else 'true' end as isDeleted,UPPER(CAST(pp.location_id AS CHAR(36))) as locationId,UPPER(CAST(pp.provider_id AS CHAR(36))) as providerId, "
						+"case when pp.recorded_elsewhere_ind='N' then 'false' else 'true' end as isRecordedElsewhere,pp.recorded_elsewhere_source as recordedElsewhereSource, "
						+"UPPER(CAST(pp.responsible_provider_id AS CHAR(36))) as responsibleProviderId,case when pp.comorbid_condition_ind='N' then 'false' else 'true' end as isComorbid,pp.created_by as createdBy, "
						+"pp.create_timestamp as createTimestamp,pp.modified_by as modifiedBy,pp.modify_timestamp as modifyTimestamp,pp.create_timestamp_tz as createTimestampTimezone,pp.modify_timestamp_tz as modifyTimestampTimezone "
						+"FROM patient_problems pp where pp.person_id='"+parameters.get("personId")+"' and pp.uniq_id='"+parameters.get("problemId")+"'";

				String[] jsonMetaData = {"id","enterpriseId","practiceId","personId","description","conceptId","sideId","site","onsetDate","lastAddressedDate","resolvedDate","resolvedReason","resolvedBy","problemStatusId","clinicalStatusId","isChronic","hasSecondaryCondition","isDeleted","locationId","providerId","isRecordedElsewhere","recordedElsewhereSource","responsibleProviderId","isComorbid","createdBy","createTimestamp","modifiedBy","modifyTimestamp","createTimestampTimezone","modifyTimestampTimezone"};

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