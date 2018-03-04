package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_UpdatePrescriptionNote {

	public static Boolean chart_UpdatePrescriptionNote(HashMap<String, String> parameters, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strSQLQuery = "select datediff(second,create_timestamp,modify_timestamp) as diff,modified_by as mb "
					+ "from med_rx_notes where medication_id='" + parameters.get("medicationId")+ "' and note_id='" + parameters.get("noteId") + "'";
			Object objIntDateDiff = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "diff");
			Object objIntModifiedBy = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "mb");
			if(Integer.valueOf(objIntDateDiff.toString()) > 0)
			{
				if(Objects.equals(objIntModifiedBy.toString(), System.getProperty("LoggedInUserId")))
				{
					String strQuery1 = "select top 1 note_type noteType, note "
							+ "from med_rx_notes where medication_id='" + parameters.get("medicationId") + "' and note_id='" + parameters.get("noteId") + "'";

					ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
					ArrayList<Object> listTestData  = new ArrayList<>();
					
					listTestData.add(Objects.equals(parameters.get("Type"), "") ? null: parameters.get("Type"));
					listTestData.add(Objects.equals(parameters.get("Note"), "") ? null: parameters.get("Note"));
					
					Map<String , ArrayList<Object>> mList = new HashMap<String , ArrayList<Object>>();
					mList.put("jsonList", listTestData);
					mList.put("dbList", listDatabase);
					
					Log.info("Parameters:"+parameters);
					Log.info("TestData:\n"+listTestData);
					Log.info("DBData:\n"+listDatabase);
					status = ValidateResults.resultsvalidation(mList);
				}
				else
				{
					Log.error("Medication note with medicationId="+parameters.get("medicationId")+", was not updated correctly-modified_by is not updated with logged in user's credentials.");
					Log.error("Expected:"+System.getProperty("LoggedInUserId")+", Actual:"+objIntModifiedBy.toString());
					softAssert.fail("Medication note with medicationId="+parameters.get("medicationId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				}
			}
			else
			{
				Log.error("Medication with medicationId="+parameters.get("medicationId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				softAssert.fail("Medication note with problemId="+parameters.get("medicationId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}
}
