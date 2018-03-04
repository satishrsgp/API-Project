package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetHealthConcernsEncDiag {

	public static Boolean chart_GetHealthConcernsEncounterDiagnosis(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{


				String strQuery="select pd.uniq_id,CONVERT(char(10), pe.enc_timestamp,126) +'T'+ convert(varchar(8), pe.enc_timestamp, 108) 'enc_Timestamp', "
						+ "pd.description,pd.icd9cm_code_id,case when pd.chronic_ind like 'N' "
						+ "then 'false' else 'true' end as chronic_ind,pd.enterprise_id,pd.practice_id,pd.person_id  "
						+ "from patient_diagnosis pd inner join patient_encounter pe on pd.person_id=pe.person_id where pd.person_id='"+parameters.get("personId")+"'";

				String[] jsonMetaData = {"id","encounterDate","description","icdCode","isChronic","enterpriseId","practiceId","personId"};

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
