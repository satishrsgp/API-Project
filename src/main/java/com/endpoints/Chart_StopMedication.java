package com.endpoints;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Date;
import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;
public class Chart_StopMedication {
	

	public static Boolean chart_Stop_Test(HashMap<String, String> parameters, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strSQLQuery = "select datediff(second,create_timestamp,modify_timestamp) as diff,modified_by as mb "
					+ "from patient_medication where person_id='"+parameters.get("personId")+"' and uniq_id='"+parameters.get("MedicationId")+"'";
			Object objIntDateDiff = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "diff");
			Object objIntModifiedBy = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "mb");
			if(Integer.valueOf(objIntDateDiff.toString()) > 0)
			{
				if(Objects.equals(objIntModifiedBy.toString(), System.getProperty("LoggedInUserId")))
				{
					String strQuery1 = "Select top 1 date_stopped "
							+ "from patient_medication where person_id='"+parameters.get("personId")+"' and uniq_id='"+parameters.get("MedicationId")+"'";
					Log.info("strQuery1:"+strQuery1);
					ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
					ArrayList<Object> listTestData  = new ArrayList<>();
					DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
					Date date = new Date();
					listTestData.add(dateFormat.format(date));
					Log.info("Parameters:"+parameters);
					Log.info("TestData:\n"+listTestData);
					Log.info("DBData:\n"+listDatabase);
					status = ValidateResults.resultValidation(listTestData, listDatabase);
				}
				else
				{
					Log.error("Stop date for medication with medicationID ="+parameters.get("MedicationId")+", was not updated correctly-modified_by is not updated with logged in user's credentials.");
					Log.error("Expected:"+System.getProperty("LoggedInUserId")+", Actual:"+objIntModifiedBy.toString());
					softAssert.fail("Stop date for medication with medicationID ="+parameters.get("MedicationId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				}
			}
			else
			{
				Log.error("Stop date for medication with medicationID ="+parameters.get("MedicationId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				softAssert.fail("Stop date for medication with medicationID ="+parameters.get("MedicationId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}


}
