package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class GetHealthConcernsProblemObservations 
{

	public static Boolean getHealthConcernsProblemObservations_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "SELECT uniq_id AS id, last_addressed_date AS lastAddressedDate, onset_date AS onsetDate, "
						+ "description AS description, CASE WHEN chronic_ind = 'N' THEN 'false' ELSE 'true' END AS isChronic, "
						+ "enterprise_id AS enterpriseId, practice_id AS practiceId, person_id AS personId, "
						+ "CASE WHEN description <> '' THEN 'ProblemObservation' ELSE '' END AS category, concept_id AS snomedId, "
						+ "CASE WHEN delete_ind = 'N' THEN 'false' ELSE 'true' END AS isDeleted FROM patient_problems "
						+ "WHERE person_id = '"+parameters.get("personId")+"' AND   delete_ind = 'N'";
				
				//providerName ,"whoReviewed": null,"whenReviewed": null,
				
				
				String[] jsonMetaData = {"id","lastAddressedDate","onsetDate","description","isChronic","enterpriseId","practiceId",
						"personId","category","snomedId","isDeleted"};
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
