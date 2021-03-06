package com.endpoints;

import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.google.common.base.Objects;
import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;

public class Chart_DeleteNote
{

	public static Boolean Chart_DeleteNote_Test(HashMap<String, String> parameters, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{	
			String strSQLQuery = "select datediff(second,pnx.create_timestamp,pnx.modify_timestamp) as diff,pnx.modified_by as modifiedBy from patient_problems pp inner join problem_note_xref pnx on pnx.problem_id=pp.uniq_id where pp.person_id='"+parameters.get("personId") + "' and pp.uniq_id='"+ parameters.get("problemId") +"' and pnx.note_id='"+parameters.get("noteId")+"'";
			Object objDateDiff = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "diff");
			Object objModifiedBy = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "modifiedBy");
			if(Integer.valueOf(objDateDiff.toString()) > 0)
			{
				if(Objects.equal(objModifiedBy.toString(), System.getProperty("LoggedInUserId")))
				{
					status = true;
				}
				else
				{
					Log.error("The modified_by column in SQL is not updated with correct data");
					softAssert.fail("The modified_by column in SQL is not updated with correct data");
					Log.error("Actual:"+objModifiedBy.toString()+",Expected:"+System.getProperty("LoggedInUserId"));
				}
			}
			else
			{
				Log.error("The modify_timestamp is same as create_timestamp");
				softAssert.fail("The modify_timestamp is same as create_timestamp");
				Log.error("create_timestamp:"+DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, "select create_timestamp as diff from patient_problems where person_id='"+parameters.get("personId") + "' and uniq_id='"+ parameters.get("problemId") +"'", "diff").toString()+",modify_timestamp:"+DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, "select modify_timestamp as diff from patient_problems where person_id='"+parameters.get("personId") + "' and uniq_id='"+ parameters.get("problemId") +"'", "diff").toString());
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			Log.info("Error in Test Class and the detail are : \n" + e.getMessage());
			softAssert.fail("Error in Test Script please look at logs");
		}
		return status;
	}
}