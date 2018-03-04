package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_UpdateCareTeamMember 
{

	public static Boolean chart_UpdateCareTeamMember_Test(HashMap<String, String> parameters, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strSQLQuery = "select datediff(second,create_timestamp,modify_timestamp) as diff,modified_by as mb "
					+ "from patient_care_team_memb_ext_ "
					+ " where person_id='"+parameters.get("personId")+"' "
					+ "and seq_no='" + parameters.get("careTeamMemberId") + "'";
					
			Object objIntDateDiff = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "diff");
			Object objIntModifiedBy = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "mb");
			if(Integer.valueOf(objIntDateDiff.toString()) > 0)
			{
				if(Objects.equals(objIntModifiedBy.toString(), System.getProperty("LoggedInUserId")))
				{
					String strQuery1 = "Select "
							+ "txt_provider_id as providerId, "
							+ "txt_first_name as firstName, "
							+ "txt_last_name as lastName, "
							+ "txt_email as email, "
							+ "txt_active_date as activeDate, "
							+ "txt_address as address, "
							+ "txt_agency_name as agencyName, "
							+ "txt_agency_type as agencyType, "
							+ "txt_cell_phone as cellPhone, "
							+ "txt_city as city, "
							+ "txt_fax as fax, "
							+ "txt_home_phone as homePhone, "
							+ "txt_inactive_date as inactiveDate, "
							+ "txt_patient_relationship as patientRelationship, "
							+ "txt_state as state, "
							+ "txt_support_role as supportRole, "
							+ "txt_zip as zip "
							+ "from patient_care_team_memb_ext_"
							+ " where person_id='"+parameters.get("personId")+"' "
							+ "and seq_no='" + parameters.get("careTeamMemberId") + "'";
							
					Log.info("strQuery1:"+strQuery1);
					//ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
					ArrayList<Object> listTestData  = new ArrayList<>();
					listTestData.add(Objects.equals(parameters.get("providerId"), "") ? null: parameters.get("providerId").toString().toLowerCase());
					listTestData.add(Objects.equals(parameters.get("firstName"), "") ? null : (parameters.get("firstName").toString()));
					listTestData.add(Objects.equals(parameters.get("lastName"), "") ? null: parameters.get("lastName").toString());
					listTestData.add(Objects.equals(parameters.get("email"), "") ? null: parameters.get("email").toString());
					listTestData.add(Objects.equals(parameters.get("activeDate"), "") ? null : (parameters.get("activeDate").toString().split("T")[0]).replace("-", ""));
					listTestData.add(Objects.equals(parameters.get("address"), "") ? null: parameters.get("address").toString());
					listTestData.add(Objects.equals(parameters.get("agencyName"), "") ? null: parameters.get("agencyName").toString());
					listTestData.add(Objects.equals(parameters.get("agencyType"), "") ? null: parameters.get("agencyType").toString());
					listTestData.add(Objects.equals(parameters.get("cellPhone"), "") ? null: parameters.get("cellPhone").toString());
					listTestData.add(Objects.equals(parameters.get("city"), "") ? null: parameters.get("city").toString());
					listTestData.add(Objects.equals(parameters.get("fax"), "") ? null: parameters.get("fax").toString());
					listTestData.add(Objects.equals(parameters.get("homePhone"), "") ? null: parameters.get("homePhone").toString());
					listTestData.add(Objects.equals(parameters.get("inactiveDate"), "") ? null : (parameters.get("inactiveDate").toString().split("T")[0]).replace("-", ""));
					listTestData.add(Objects.equals(parameters.get("patientRelationship"), "") ? null: parameters.get("patientRelationship").toString());
					listTestData.add(Objects.equals(parameters.get("state"), "") ? null: parameters.get("state").toString());
					listTestData.add(Objects.equals(parameters.get("supportRole"), "") ? null: parameters.get("supportRole").toString());
					listTestData.add(Objects.equals(parameters.get("zip"), "") ? null: parameters.get("zip").toString());
					
					Log.info("Parameters:"+parameters);
					Log.info("TestData:\n"+listTestData);
					//Log.info("DBData:\n"+listDatabase);
					status = ValidateResults.resultsvalidation(listTestData, DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
				}
				else
				{
					Log.error("Care team member for patient with person_id ="+parameters.get("personId")+", was not updated correctly-modified_by is not updated with logged in user's credentials.");
					Log.error("Expected:"+System.getProperty("LoggedInUserId")+", Actual:"+objIntModifiedBy.toString());
					softAssert.fail("seq_no for patient with person_id ="+parameters.get("personId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				}
			}
			else
			{
				Log.error("Care team member for patient with person_id ="+parameters.get("personId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				softAssert.fail("seq_no for patient with person_id ="+parameters.get("personId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}
}
