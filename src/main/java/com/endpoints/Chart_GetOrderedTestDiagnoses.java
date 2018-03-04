package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetOrderedTestDiagnoses
{
	public static Boolean Chart_GetOrderedTestDiagnoses_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "select UPPER(CAST(lod.unique_diag_num AS CHAR(36))) AS id,UPPER(CAST(lod.order_num AS CHAR(36))) AS orderNumber,UPPER(CAST(ln.person_id AS CHAR(36))) AS personId,ln.enterprise_id as enterpriseId, "
						+"ln.practice_id as practiceId,UPPER(CAST(lod.order_test_id AS CHAR(36))) AS orderTestId,UPPER(CAST(lod.diagnosis_code_lib_id AS CHAR(36))) AS diagnosisCodeLibraryId, "
						+"lod.diagnosis_code_id as diagnosisCodeId,dcm.description as description,dcm.icd9cm_code_id as icd9cmCodeId,dcm.user_description as userDescription "
						+"from lab_order_diag lod inner join lab_nor ln on lod.order_num=ln.order_num "
						+"inner join diagnosis_code_mstr dcm on (lod.diagnosis_code_lib_id=dcm.diagnosis_code_lib_id and lod.diagnosis_code_id=dcm.diagnosis_code_id) "
						+"where ln.person_id='"+parameters.get("personId")+"' and ln.order_num='"+parameters.get("orderId")+"' and lod.order_test_id='"+parameters.get("testId")+"' "
						+"order by lod.create_timestamp desc";

				String[] jsonMetaData = {"id","orderNumber","personId","enterpriseId","practiceId","orderTestId","diagnosisCodeLibraryId","diagnosisCodeId","description","icd9cmCodeId","userDescription"};

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