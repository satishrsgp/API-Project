package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Audit_GetDiagnosisDetail {

	public static Boolean audit_GetDiagnosisDetail_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{

				String event_unique_id ="select unique_id from event_chart_summary where event_id = '" + parameters.get("eventId")+"'";
		
				String unique_id =DatabaseConnection.fetchColumnAsObject(BaseTest.connNGAAudit, event_unique_id, "unique_id").toString();
				
				String strQuery="select pd.uniq_id as id , pd.icd9cm_code_id as diagnosisCodeId , pd.modify_timestamp as timestamp, "
						+ "lm.location_name as locationName, pm.first_name as providerFirstName, pm.last_name as providerLastName, "
						+ " st.description as status, pd.date_onset_sympt as dateOnset, "
						+ "pd.date_diagnosed as dateDiagnosed, pd.date_resolved as dateResolved, pd.note as notes "
						+ "from patient_diagnosis pd inner join location_mstr lm on pd.location_id = lm.location_id "
						+ "inner join provider_mstr pm on pm.provider_id =pd.provider_id inner join diagnosis_severity_mstr sev "
						+ "on pd.severity_id = sev.severity_id inner join diagnosis_status_mstr st on pd.status_id =st.status_id "
						+ "where pd.uniq_id = '"+unique_id+"'" ;

				String[] jsonMetaData = {"id","diagnosisCodeId","timestamp;Date_YYYYMMDD HH:MM:SS","locationName","providerFirstName","providerLastName"
						,"status","dateOnset","dateDiagnosed","dateResolved","notes"};

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
