package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_UpdatePatientProblem
{
	public static Boolean chart_UpdatePatientProblem_Test(HashMap<String, String> parameters, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strSQLQuery = "select datediff(second,create_timestamp,modify_timestamp) as diff,modified_by as mb "
					+ "from patient_problems where person_id='"+parameters.get("personId")+"' and uniq_id='"+parameters.get("problemId")+"'";
			Object objIntDateDiff = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "diff");
			Object objIntModifiedBy = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "mb");
			if(Integer.valueOf(objIntDateDiff.toString()) > 0)
			{
				if(Objects.equals(objIntModifiedBy.toString(), System.getProperty("LoggedInUserId")))
				{
					String strQuery1 = "select top 1 concept_id,side_id,site,onset_date,last_addressed_date,resolved_date, "
							+ "problem_status_id,UPPER(CAST(clinical_status_id AS CHAR(36))),case when chronic_ind='Y' then 'true' else 'false' end as chronic_ind, "
							+ "case when secondary_condition_ind='Y' then 'true' else 'false' end as secondary_condition_ind, "
							+ "case when comorbid_condition_ind='Y' then 'true' else 'false' end as comorbid_condition_ind, "
							+ "UPPER(CAST(location_id AS CHAR(36))),UPPER(CAST(provider_id AS CHAR(36))),case when recorded_elsewhere_ind='Y' then 'true' else 'false' end as recorded_elsewhere_ind, "
							+ "UPPER(CAST(responsible_provider_id AS CHAR(36))) as responsible_provider_id "
							+ "from patient_problems where person_id='" + parameters.get("personId") + "' order by modify_timestamp desc";
					ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
					ArrayList<Object> listTestData  = new ArrayList<>();
					listTestData.add(Objects.equals(parameters.get("ConceptId"), "") ? null: parameters.get("ConceptId"));
					listTestData.add(Objects.equals(parameters.get("SideId"), "") ? null: parameters.get("SideId"));
					listTestData.add(Objects.equals(parameters.get("Site"), "") ? null: parameters.get("Site"));
					listTestData.add(Objects.equals(parameters.get("OnsetDate"), "") ? null : (parameters.get("OnsetDate").toString().split("T")[0]).replace("-", ""));
					listTestData.add(Objects.equals(parameters.get("LastAddressedDate"), "") ? null : (parameters.get("LastAddressedDate").toString().split("T")[0]).replace("-", ""));
					listTestData.add(Objects.equals(parameters.get("ResolvedDate"), "") ? null : (parameters.get("ResolvedDate").toString().split("T")[0]).replace("-", ""));
					//listTestData.add(Objects.equals(parameters.get("ResolvedReason"), "") ? null : parameters.get("ResolvedReason"));
					//listTestData.add(Objects.equals(parameters.get("ResolvedBy"), "") ? null : parameters.get("ResolvedBy"));
					listTestData.add(Objects.equals(parameters.get("ProblemStatusId"), "") ? null : parameters.get("ProblemStatusId"));
					listTestData.add(Objects.equals(parameters.get("ClinicalStatusId"), "") ? null : parameters.get("ClinicalStatusId").toString().toUpperCase());
					listTestData.add(Objects.equals(parameters.get("IsChronic"), "") ? null : parameters.get("IsChronic").toString());
					listTestData.add(Objects.equals(parameters.get("HasSecondaryCondition"), "") ? null : parameters.get("HasSecondaryCondition").toString());
					listTestData.add(Objects.equals(parameters.get("IsComorbid"), "") ? null : parameters.get("IsComorbid").toString());
					listTestData.add(Objects.equals(parameters.get("LocationId"), "") ? null : parameters.get("LocationId").toString().toUpperCase());
					listTestData.add(Objects.equals(parameters.get("ProviderId"), "") ? null : parameters.get("ProviderId").toString().toUpperCase());
					listTestData.add(Objects.equals(parameters.get("IsRecordedElsewhere"), "") ? null : parameters.get("IsRecordedElsewhere").toString());
					//listTestData.add(Objects.equal(parameters.get("RecordedElsewhereSource"), "") ? null : parameters.get("RecordedElsewhereSource"));
					listTestData.add(Objects.equals(parameters.get("ResponsibleProviderId"), "") ? null : parameters.get("ResponsibleProviderId").toString().toUpperCase());
					Log.info("Parameters:"+parameters);
					Log.info("TestData:\n"+listTestData);
					Log.info("DBData:\n"+listDatabase);
					status = ValidateResults.resultValidation(listTestData, listDatabase);
				}
				else
				{
					Log.error("Problem note with problemId="+parameters.get("problemId")+", was not updated correctly-modified_by is not updated with logged in user's credentials.");
					Log.error("Expected:"+System.getProperty("LoggedInUserId")+", Actual:"+objIntModifiedBy.toString());
					softAssert.fail("Problem note with problemId="+parameters.get("problemId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				}
			}
			else
			{
				Log.error("Problem with problemId="+parameters.get("problemId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				softAssert.fail("Problem note with problemId="+parameters.get("problemId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}


}
