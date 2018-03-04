package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_AddPrescriptionNote {	
	public static Boolean chart_AddPrescriptionNote(HashMap<String, String> parameters, HashMap<String, Object> mapPostDetails, SoftAssert softAssert, String strQuery)
	{
		Boolean status=false;
		try
		{
			int countAfterPOST = DatabaseConnection.returnNumberOfRows(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery));
			if(countAfterPOST - ((int)mapPostDetails.get("countBeforePOST")) == 1)
			{
				String strQuery1 = "select top 1 case when note_type ='1' then 'General' when note_type ='2'  then 'Prescribe' when note_type ='3' then 'Patient' "
						+ "when note_type = '4' then 'Stop' else 'None' end as noteType, note "
						+ "from med_rx_notes where medication_id='" + parameters.get("medicationId") + "' order by create_timestamp desc";
				
				ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
				
				ArrayList<Object> listTestData  = new ArrayList<>();
				
				listTestData.add(Objects.equals(parameters.get("note_type"), "") ? null: parameters.get("note_type"));
				listTestData.add(Objects.equals(parameters.get("note"), "") ? null: parameters.get("note"));
				
				Log.info("Parameters:"+parameters);
				Log.info("TestData:\n"+listTestData);
				Log.info("DBData:\n"+listDatabase);
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
