package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetPatientAllergyReactions 
{

	public static Boolean chart_GetPatientAllergyReactions_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "SELECT pa.uniq_id AS patientAllergyId, par.reaction_desc AS ReactionDescription, par.reaction_code AS snomedCode, par.severity_code AS severityCode, "
						+ "par.rank AS RANK FROM patient_allergy pa LEFT JOIN patient_allergy_reactions par ON pa.uniq_id = par.patient_allergy_id WHERE "
						+ "pa.person_id = '"+parameters.get("personId")+"' AND par.patient_allergy_id = '"+parameters.get("allergyId")+"' AND "
						+ "pa.enc_id = '"+parameters.get("encounterId")+"'";
				

				
				String[] jsonMetaData = {"patientAllergyId","ReactionDescription","snomedCode","severityCode","RANK"};
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
