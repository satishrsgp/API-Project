package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Master_GetDiagnoses 
{
	public static Boolean master_GetDiagnoses_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{

				String strQuery = "select top 25 "
						+"DCM.diagnosis_code_id as diagnosisCodeId,DCM.diagnosis_code_lib_id as diagnosisCodeLibraryId,DCM.icd9cm_code_id as icd9cmCodeId,DCM.description as description, "
						+"case when DCM.delete_ind = 'N' then 'false' else 'true' end as isDeleted, "
						+"DCM.user_description as userDescription ,I9CM.icd_type as codeSystem , I9CM.eff_date as effectiveDate,I9CM.exp_date as expirationDate "
						+"from  diagnosis_code_mstr DCM,icd9cm_code_mstr I9CM "
						+"where DCM.icd9cm_code_id  = I9CM.icd9cm_code_id "
						+"ORDER BY I9CM.icd_type ASC, DCM.description  ASC, DCM.diagnosis_code_id ASC, DCM.diagnosis_code_lib_id ASC, "
						+"I9CM.eff_date ASC, I9CM.exp_date ASC, DCM.icd9cm_code_id  ASC, DCM.delete_ind ASC, DCM.user_description ASC ";

				String[] jsonMetaData = {"diagnosisCodeId;Normal","diagnosisCodeLibraryId;GUID","icd9cmCodeId;Normal","description;Normal","isDeleted;Boolean","userDescription;Normal","codeSystem;Normal","effectiveDate;Date_YYYYMMDD","expirationDate;Date_YYYYMMDD"};

				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..diagnosisCodeId", jsonList);
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