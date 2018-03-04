package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetHealthConcernsAllergy 
{

	public static Boolean chart_GetHealthConcernsAllergy_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "select pe.enterprise_id enterpriseId, pe.practice_id as practiceId, pe.enc_id as encounterId, pe.person_id as personId, "
						+ " am.snomed_concept_id as snomedConceptId, pa.date_onset as dateOnset, pa.date_resolved as dateResolved, "
						+ " pa.allergy_type as allergyType, pe.enc_timestamp as encounterTimestamp, pa.allergy_comment as allergyComment, "
						+ " pa.uniq_id as id, CONCAT('Allergic to', ' ',am.description) as healthConcern,am.description as description FROM patient_allergy pa inner JOIN patient_encounter pe "
						+ " ON pa.enc_id = pe.enc_id inner JOIN allergy_mstr am ON pa.allergy_id = am.allergy_id where "
						+ " pe.person_id = '"+parameters.get("personId")+"'";
				
				//providerName ,"whoReviewed": null,"whenReviewed": null,

				
				String[] jsonMetaData = {"enterpriseId","practiceId","encounterId","personId","snomedConceptId",
						"dateOnset","dateResolved","allergyType","encounterTimestamp;Date_YYYYMMDD HH:MM:SS","allergyComment","id","healthConcern","description"};
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..personId", jsonList);
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
