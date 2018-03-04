package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Master_GetLabComponents 
{

	public static Boolean master_GetLabComponents_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "SELECT ltcx.lab_component_key AS id, ltcx.system_id AS systemId, ltcx.labtest_key AS testId, lcm.lab_component_desc AS labComponentDescription, "
						+ "lcm.code_system AS codeSystem, lcm.units AS units, lcm.ref_range AS refRange, lcm.loinc_code AS loincCode, "
						+ "lcm.snomed_code AS snomedCode, lcm.data_type AS dataType, CASE WHEN lcm.delete_ind = 'N' THEN 'false' ELSE 'true' "
						+ "END AS isDeleted, ltcx.sequence_num AS sequenceNumber FROM lab_components_mstr lcm INNER JOIN lab_test_components_xref ltcx "
						+ "ON lcm.lab_component_key = ltcx.lab_component_key WHERE ltcx.labtest_key = '"+parameters.get("testId")+"'";
				
				String[] jsonMetaData = {"id","systemId","testId","labComponentDescription","codeSystem","units","refRange",
				"loincCode","snomedCode","dataType"	,"isDeleted","sequenceNumber"};
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..testId", jsonList);
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
