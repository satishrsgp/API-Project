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

public class Chart_AddNote
{
	public static Boolean Chart_AddNote_Test(HashMap<String, String> parameters, HashMap<String, Object> mapPostDetails, SoftAssert softAssert, String strQuery)
	{/*
		Boolean status=false;
		try
		{
			String strSQLQuery = "select top 1 UPPER(CAST(pp.person_id AS CHAR(36))) as personId,UPPER(CAST(pnx.problem_id AS CHAR(36))) as problemId,pnx.note as note "
					+"from patient_problems pp inner join problem_note_xref pnx on pp.uniq_id=pnx.problem_id " 
					+"where pp.person_id='"+parameters.get("personId")+"' and pp.uniq_id='"+parameters.get("problemId")+"' order by pnx.create_timestamp desc";
			ArrayList<Object> jsonList = new ArrayList<Object>();
			jsonList.add(parameters.get("personId").toString().toUpperCase());
			jsonList.add(parameters.get("problemId").toString().toUpperCase());
			jsonList.add(parameters.get("noteBody").toString());
			status = ValidateResults.resultsvalidation(jsonList, DatabaseConnection.statementExecution(BaseTest.connNGA, strSQLQuery));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.info("Error in Test Class and the detail are : \n" + e.getMessage());
			softAssert.fail("Error in Test Script please look at logs");
		}
		return status;
	 */


		Boolean status=false;
		try
		{
			int countAfterPOST = DatabaseConnection.returnNumberOfRows(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery));
			if(countAfterPOST - ((int)mapPostDetails.get("countBeforePOST")) == 1)
			{
				String strQuery1 = "select top 1 pnx.note as note from patient_problems pp inner join problem_note_xref pnx on pp.uniq_id=pnx.problem_id where pp.uniq_id='"+parameters.get("problemId")+"' order by pnx.create_timestamp desc";
				ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
				ArrayList<Object> listTestData  = new ArrayList<>();
				for(Map.Entry<String, Object> entry1 : mapPostDetails.entrySet())
				{
					for(Map.Entry<String, String> entry2 : parameters.entrySet())
					{
						if(Objects.equals(entry2.getKey(), entry1.getKey()))
							listTestData.add(entry1.getValue());
					}	
				}
				status = ValidateResults.resultValidation(listTestData, listDatabase);
			}
			else
			{
				Log.info("The Difference between records count before and after post is more than one, please reexcute once more");
				softAssert.fail("The Difference between records count before and after post is more than one. Please reexecute once more");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}
}