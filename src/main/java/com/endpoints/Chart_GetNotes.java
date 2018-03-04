package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetNotes
{
	public static Boolean Chart_GetProblemNote_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery= null;
				if(strResponse.contains("$skip="))
				{
					strQuery = "select top 25 UPPER(CAST(pnx.note_id AS CHAR(36))) as id,UPPER(CAST(pnx.problem_id AS CHAR(36))) as problemId,UPPER(CAST(pp.person_id AS CHAR(36))) as personId,pnx.note as note, "
							+"pnx.created_by as createdBy,pnx.create_timestamp as createTimestamp,pnx.modified_by as modifiedBy,pnx.modify_timestamp as modifyTimestamp,case when pnx.delete_ind ='N' then 'false' else 'true' end as isDeleted, "
							+"pnx.create_timestamp_tz as createTimestampTimezone,pnx.modify_timestamp_tz as modifyTimestampTimezone "
							+"from patient_problems pp inner join problem_note_xref pnx on pp.uniq_id=pnx.problem_id "
							+"where pp.person_id='"+parameters.get("personId")+"' and pp.uniq_id='"+parameters.get("problemId")+"' order by pnx.note_id";
				}
				else
				{
					strQuery = "select UPPER(CAST(pnx.note_id AS CHAR(36))) as id,UPPER(CAST(pnx.problem_id AS CHAR(36))) as problemId,UPPER(CAST(pp.person_id AS CHAR(36))) as personId,pnx.note as note, "
							+"pnx.created_by as createdBy,pnx.create_timestamp as createTimestamp,pnx.modified_by as modifiedBy,pnx.modify_timestamp as modifyTimestamp,case when pnx.delete_ind ='N' then 'false' else 'true' end as isDeleted, "
							+"pnx.create_timestamp_tz as createTimestampTimezone,pnx.modify_timestamp_tz as modifyTimestampTimezone "
							+"from patient_problems pp inner join problem_note_xref pnx on pp.uniq_id=pnx.problem_id "
							+"where pp.person_id='"+parameters.get("personId")+"' and pp.uniq_id='"+parameters.get("problemId")+"' order by pnx.note_id";
				}
				String[] jsonMetaData = {"id","problemId","personId","note","createdBy","createTimestamp","modifiedBy","modifyTimestamp","isDeleted","createTimestampTimezone","modifyTimestampTimezone"};

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