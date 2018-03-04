package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Master_GetLabTests 
{

	public static Boolean master_GetLabTests_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{//
			try
			{				
				String strQuery = "SELECT TOP 25 ltx.labtest_key AS id, es.external_system_name AS externalSystemName, ltx.system_id AS"
						+ " systemId, ltx.labtest_desc AS labTestDescription, ltx.code_system AS codeSystem, ltx.cost AS"
						+ " cost, ltx.loinc_code AS loincCode, CASE WHEN ltx.delete_ind = 'Y' THEN 'true' ELSE 'false' END AS"
						+ " isDeleted, ltx.order_type AS orderType, CASE WHEN ltx.sim_group_ind = 'Y' THEN 'true' ELSE 'false' END AS"
						+ " isSimGroup, ltx.sim_code AS simCode, ltx.expected_in_days AS expectedInDays, ldx.ng_dept AS"
						+ " departmentId, CASE WHEN ltm.ng_cds_ind = 'Y' THEN 'true' ELSE 'false' END AS"
						+ " isCds, ltm.ng_cds_cpt_code_id AS cdsCptCodeId FROM lab_tests_xref AS ltx LEFT JOIN external_system AS"
						+ " es ON ltx.system_id = es.external_system_id LEFT JOIN lab_tests_mstr AS"
						+ " ltm ON ltx.labtest_key = ltm.ng_labtest_key LEFT JOIN lab_dept_xref AS ldx ON ltx.system_id = ldx.system_id ORDER BY ltx.labtest_key";
				
				
				//CONVERT(uniqueidentifier,'0C6A36BA-10E4-438F-BA86-0D5B68A2BB15'
				//da.event_text whoReviewed,da.create_timestamp as whenReviewed   --------"whoReviewed": null,"whenReviewed": null, parameters.get("personId")
				
				String[] jsonMetaData = {"id","externalSystemName","systemId","labTestDescription","codeSystem","cost","loincCode","isDeleted","orderType","isSimGroup","simCode","expectedInDays","departmentId","isCds","cdsCptCodeId",""};
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$.."+strUniqueJsonToken, jsonList);
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
