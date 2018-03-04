package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Persons_GetPersonLookup 
{


	public static Boolean persons_GetPersonLookup_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert,Map<String, String> mapQuery)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "select DISTINCT pr.person_id as id,"
						+ "pr.first_name as firstName, "
						+ "pr.last_name as lastName, "
						+ "pr.middle_name as middleName, "
						+ "pr.nickname as nickname,"
						+ "pr.address_line_1 as addressLine1, "
						+ "pr.city as city,"
						+ "pr.state as state,"
						+ "pr.zip as zip,"
						+ "pr.country as country,"
						+ "pr.home_phone as homePhone,"
						+ "pr.date_of_birth as dateOfBirth,"
						+ "pr.sex as sex,"
						+ "pr.ssn as socialSecurityNumber,"
						+ "pr.person_nbr as personNumber,"
						+ "pt.med_rec_nbr as medicalRecordNumber,"
						+ "pr.other_id_number as otherIdNumber, "
						+ "CASE WHEN pec.enterprise_chart_ind = 'Y'  THEN 'true' ELSE 'false' END as isEnterpriseChart, "
						+ "CASE WHEN pt.person_id = null THEN 'false' ELSE 'true'  END as isPatient "
						+ "from person pr INNER JOIN patient pt ON pr.person_id = pt.person_id "
						+ "INNER JOIN patient_enterprise_chart pec ON pec.enterprise_id = pt.enterprise_id AND pec.person_id = pt.person_id "
						+ "where pt.practice_id='"+System.getProperty("LoggedInPracticeId")+"' and pt. enterprise_id='"+System.getProperty("LoggedInEnterpriseId")+"' AND "+ mapQuery.get("Query")
						+ "order by pr.last_name asc,pr.first_name asc,pr.middle_name asc";
						
				//providerName ,"whoReviewed": null,"whenReviewed": null,

				
				String[] jsonMetaData = {"id","firstName","lastName","middleName","nickname","addressLine1","city","state","zip","country","homePhone",
						"dateOfBirth;Date_YYYYMMDD","sex","socialSecurityNumber","personNumber","medicalRecordNumber","otherIdNumber",
						"isEnterpriseChart","isPatient"};
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..id", jsonList);
				} 
				catch (Exception e)
				{
					e.printStackTrace();
					Log.info("Error in Test Class and the detail are : \n" + e.getMessage());
					softAssert.fail("Error in Test Script please look at logs");
					//objHandler.setStrRequestStatus("Fail");
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
