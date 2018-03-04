package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_UpdateEncounter 
{

	public static Boolean chart_UpdateEncounter_Test(HashMap<String, String> parameters, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strSQLQuery = "select datediff(second,create_timestamp,modify_timestamp) as diff,modified_by as mb  "
					+ "from patient_encounter "
					+ "WHERE person_id = '"+parameters.get("personId")+"' AND "
					+ "enc_id = '"+parameters.get("encounterId")+"'";
					
			Object objIntDateDiff = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "diff");
			Object objIntModifiedBy = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "mb");
			if(Integer.valueOf(objIntDateDiff.toString()) > 0)
			{
				if(Objects.equals(objIntModifiedBy.toString(), System.getProperty("LoggedInUserId")))
				{
					String strQuery1 = "select "
							+ "rendering_provider_id as RenderingProviderId, "
							+ "billable_timestamp as BillableTimestamp, "
							+ "billable_timestamp_tz as BillableTimestampTimezone, " 
							+ "clinical_ind as IsClinical, "
							+ "billable_ind as IsBillable, "
							+ "optical_ind as IsOptical, "
							+ "location_id as LocationId, "
							+ "remarks as Remarks, "
							+ "onset_date as OnsetDate, "
							+ "onset_time as OnsetTime, "
							+ "admit_date as AdmitDate, "
							+ "discharge_date as DischargeDate, "
							+ "refer_provider_id as ReferringProviderId "
							+ "from patient_encounter "
							+ "WHERE person_id = '"+parameters.get("personId")+"' AND "
							+ "enc_id = '"+parameters.get("encounterId")+"'";
					//ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
					ArrayList<Object> listTestData  = new ArrayList<>();
					listTestData.add(Objects.equals(parameters.get("RenderingProviderId"), "") ? null: parameters.get("RenderingProviderId").toString().toUpperCase());
					listTestData.add(Objects.equals(parameters.get("BillableTimestamp"), "") ? null : (parameters.get("BillableTimestamp").toString().replace("T", " ")));
					listTestData.add(Objects.equals(parameters.get("BillableTimestampTimezone"), "") ? null: parameters.get("BillableTimestampTimezone"));
					listTestData.add(Objects.equals(parameters.get("IsClinical"), "false") ? "N" : "Y");
					listTestData.add(Objects.equals(parameters.get("IsBillable"), "false") ? "N" : "Y");
					listTestData.add(Objects.equals(parameters.get("IsOptical"), "false") ? "N" : "Y");
					listTestData.add(Objects.equals(parameters.get("LocationId"), "") ? null: parameters.get("LocationId").toString().toUpperCase());
					listTestData.add(Objects.equals(parameters.get("Remarks"), "") ? null: parameters.get("Remarks"));
					listTestData.add(Objects.equals(parameters.get("OnsetDate"), "") ? null : (parameters.get("OnsetDate").toString().substring(0, 10).replace("T", "").replace("-", "")));
					listTestData.add(Objects.equals(parameters.get("OnsetTime"), "") ? null: parameters.get("OnsetTime"));
					listTestData.add(Objects.equals(parameters.get("AdmitDate"), "") ? null : (parameters.get("AdmitDate").toString().substring(0, 10).replace("T", "").replace("-", "")));
					listTestData.add(Objects.equals(parameters.get("DischargeDate"), "") ? null : (parameters.get("DischargeDate").toString().substring(0, 10).replace("T", "").replace("-", "")));
					listTestData.add(Objects.equals(parameters.get("ReferringProviderId"), "") ? null: parameters.get("ReferringProviderId").toString().toUpperCase());
					Log.info("Parameters:"+parameters);
					Log.info("TestData:\n"+listTestData);
					//Log.info("DBData:\n"+listDatabase);
					status = ValidateResults.resultsvalidation(listTestData, DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
				}
				else
				{
					Log.error("Encounter with EncounterId="+parameters.get("encounterId")+", was not updated correctly-modified_by is not updated with logged in user's credentials.");
					Log.error("Expected:"+System.getProperty("LoggedInUserId")+", Actual:"+objIntModifiedBy.toString());
					softAssert.fail("Encounter with EncounterId="+parameters.get("encounterId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				}
			}
			else
			{
				Log.error("Encounter with EncounterId="+parameters.get("encounterId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				softAssert.fail("Encounter with EncounterId="+parameters.get("encounterId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}



}
