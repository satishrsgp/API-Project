package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import org.testng.asserts.SoftAssert;

import com.google.common.base.Objects;
import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_AddPatientProblem
{	
	public static Boolean Chart_AddPatientProblem_Test(HashMap<String, String> parameters, HashMap<String, Object> mapPostDetails, SoftAssert softAssert, String strQuery)
	{
		Boolean status=false;
		try
		{
			int countAfterPOST = DatabaseConnection.returnNumberOfRows(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery));
			if(countAfterPOST - ((int)mapPostDetails.get("countBeforePOST")) == 1)
			{
				/*String strQuery1 = "select top 1 concept_id,side_id,site,onset_date,last_addressed_date,resolved_date,resolved_reason,case when isnull(resolved_by,1)=1 then null else resolved_by end as resolved_by, "
						+ "problem_status_id,UPPER(CAST(clinical_status_id AS CHAR(36))),case when chronic_ind='Y' then 'true' else 'false' end as chronic_ind, "
						+ "case when secondary_condition_ind='Y' then 'true' else 'false' end as secondary_condition_ind, "
						+ "case when comorbid_condition_ind='Y' then 'true' else 'false' end as comorbid_condition_ind, "
						+ "UPPER(CAST(location_id AS CHAR(36))),UPPER(CAST(provider_id AS CHAR(36))),case when recorded_elsewhere_ind='Y' then 'true' else 'false' end as recorded_elsewhere_ind, "
						+ "recorded_elsewhere_source,UPPER(CAST(responsible_provider_id AS CHAR(36))) as responsible_provider_id "
						+ "from patient_problems where person_id='" + parameters.get("personId") + "' order by create_timestamp desc";*/
				String strQuery1 = "select top 1 concept_id,side_id,site,onset_date,last_addressed_date,resolved_date, "
						+ "problem_status_id,UPPER(CAST(clinical_status_id AS CHAR(36))),case when chronic_ind='Y' then 'true' else 'false' end as chronic_ind, "
						+ "case when secondary_condition_ind='Y' then 'true' else 'false' end as secondary_condition_ind, "
						+ "case when comorbid_condition_ind='Y' then 'true' else 'false' end as comorbid_condition_ind, "
						+ "UPPER(CAST(location_id AS CHAR(36))),UPPER(CAST(provider_id AS CHAR(36))),case when recorded_elsewhere_ind='Y' then 'true' else 'false' end as recorded_elsewhere_ind, "
						+ "UPPER(CAST(responsible_provider_id AS CHAR(36))) as responsible_provider_id "
						+ "from patient_problems where person_id='" + parameters.get("personId") + "' order by create_timestamp desc";
				ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
				ArrayList<Object> listTestData  = new ArrayList<>();
				listTestData.add(Objects.equal(parameters.get("ConceptId"), "") ? null: parameters.get("ConceptId"));
				listTestData.add(Objects.equal(parameters.get("SideId"), "") ? null: parameters.get("SideId"));
				listTestData.add(Objects.equal(parameters.get("Site"), "") ? null: parameters.get("Site"));
				listTestData.add(Objects.equal(parameters.get("OnsetDate"), "") ? null : (parameters.get("OnsetDate").toString().split("T")[0]).replace("-", ""));
				listTestData.add(Objects.equal(parameters.get("LastAddressedDate"), "") ? null : (parameters.get("LastAddressedDate").toString().split("T")[0]).replace("-", ""));
				listTestData.add(Objects.equal(parameters.get("ResolvedDate"), "") ? null : (parameters.get("ResolvedDate").toString().split("T")[0]).replace("-", ""));
				//listTestData.add(Objects.equal(parameters.get("ResolvedReason"), "") ? null : parameters.get("ResolvedReason"));
				//listTestData.add(Objects.equal(parameters.get("ResolvedBy"), "") ? null : parameters.get("ResolvedBy"));
				listTestData.add(Objects.equal(parameters.get("ProblemStatusId"), "") ? null : parameters.get("ProblemStatusId"));
				listTestData.add(Objects.equal(parameters.get("ClinicalStatusId"), "") ? null : parameters.get("ClinicalStatusId").toString().toUpperCase());
				listTestData.add(Objects.equal(parameters.get("IsChronic"), "") ? null : parameters.get("IsChronic").toString());
				listTestData.add(Objects.equal(parameters.get("HasSecondaryCondition"), "") ? null : parameters.get("HasSecondaryCondition").toString());
				listTestData.add(Objects.equal(parameters.get("IsComorbid"), "") ? null : parameters.get("IsComorbid").toString());
				listTestData.add(Objects.equal(parameters.get("LocationId"), "") ? null : parameters.get("LocationId").toString().toUpperCase());
				listTestData.add(Objects.equal(parameters.get("ProviderId"), "") ? null : parameters.get("ProviderId").toString().toUpperCase());
				listTestData.add(Objects.equal(parameters.get("IsRecordedElsewhere"), "") ? null : parameters.get("IsRecordedElsewhere").toString());
				//listTestData.add(Objects.equal(parameters.get("RecordedElsewhereSource"), "") ? null : parameters.get("RecordedElsewhereSource"));
				listTestData.add(Objects.equal(parameters.get("ResponsibleProviderId"), "") ? null : parameters.get("ResponsibleProviderId").toString().toUpperCase());
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