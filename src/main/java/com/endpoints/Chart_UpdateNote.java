package com.endpoints;

import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;

public class Chart_UpdateNote
{

	public static Boolean chart_UpdateNote_Test(HashMap<String, String> parameters, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strSQLQuery = "select pnx.note as note,datediff(second,pnx.create_timestamp,pnx.modify_timestamp) as diff,pnx.modified_by as mb "
					+ "from patient_problems pp inner join problem_note_xref pnx on pp.uniq_id=pnx.problem_id "
					+ "where pnx.delete_ind='N' and pnx.note_id='"+parameters.get("noteId")+"'";
			Object objIntDateDiff = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "diff");
			Object objStringNote = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "note");
			Object objIntModifiedBy = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "mb");
			if(Objects.equals(objStringNote, parameters.get("Note")))
			{
				if(Integer.valueOf(objIntDateDiff.toString()) > 0)
				{
					if(Objects.equals(objIntModifiedBy.toString(), System.getProperty("LoggedInUserId")))
					{
						status = true;
					}
					else
					{
						Log.error("Problem note with noteId="+parameters.get("noteId")+", was updated correctly but the modified_by is not updated with logged in user's credentials.");
						Log.error("Expected:"+System.getProperty("LoggedInUserId")+", Actual:"+objIntModifiedBy.toString());
						softAssert.fail("Problem note with noteId="+parameters.get("noteId")+", was updated correctly but the modify_timestamp is not updated with latest timesatmp");
					}
				}
				else
				{
					Log.error("Problem note with noteId="+parameters.get("noteId")+", was updated correctly but the modify_timestamp is not updated with latest timesatmp");
					softAssert.fail("Problem note with noteId="+parameters.get("noteId")+", was updated correctly but the modify_timestamp is not updated with latest timesatmp");
				}
			}
			else
			{
				Log.error("Problem note with noteId="+parameters.get("noteId")+" was not updated in DB please investigate");
				Log.error("Expected:"+parameters.get("Note")+", Actual:"+objStringNote.toString());
				softAssert.fail("Problem note with noteId="+parameters.get("noteId")+" was not updated in DB please investigate");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}

}