package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Master_GetSpecialtyLabTests 
{

	public static Boolean master_GetSpecialtyLabTests_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "select top 25 labtest_key AS id, external_system_name AS externalSystemName, system_id AS systemId, labtest_desc AS labTestDescription, "
						+ " code_system AS codeSystem, cost AS cost, loinc_code AS loincCode, CASE WHEN delete_ind = 'N' THEN 'false' ELSE 'true' END AS isDeleted, "
						+ " order_type AS orderType, CASE WHEN sim_group_ind = 'N' THEN 'false' ELSE 'true' END AS isSimGroup, sim_code AS simCode, expected_in_days AS expectedInDays, "
						+ " ng_dept AS departmentId, mstr_list_item_desc AS listItemDescription, CASE WHEN cds_ind = 'N' THEN 'false' ELSE 'true' END AS isCds, cds_cpt_code_id AS cdsCptCodeId"
						+ " FROM ( SELECT e.external_system_name, l.system_id, l.labtest_key, m.mstr_list_item_desc, l.labtest_desc, "
						+ " l.code_system, l.cost, l.loinc_code, d.ng_dept, l.delete_ind, l.order_type, null AS user_desc, "
						+ " null AS confidential_ind, l.sim_group_ind, l.sim_code, l.expected_in_days, x.specialty_code, l.orderable_ind, "
						+ " x.practice_id, lm.provider_id, l.cds_ind, l.cds_cpt_code_id FROM lab_favorite_xref x JOIN lab_favorite_mstr lm "
						+ " ON x.lab_favorite_id = lm.lab_favorite_id JOIN external_system e ON lm.external_system_id = e.external_system_id "
						+ " JOIN lab_favorite_tests f ON lm.lab_favorite_id = f.lab_favorite_id JOIN lab_tests_xref l ON f.labtest_key = l.labtest_key "
						+ " AND e.external_system_id = l.system_id AND lm.delete_ind = l.delete_ind LEFT JOIN lab_dept_xref d ON l.labdept = d.lab_dept "
						+ " LEFT JOIN mstr_lists m ON d.ng_dept = m.mstr_list_item_id UNION SELECT null AS external_system_name, null AS system_id, "
						+ " l.ng_labtest_key AS labtest_key, m.mstr_list_item_desc, l.ng_labtest_desc, l.code_system, null AS cost, null AS loinc_code, "
						+ " l.department AS ng_dept, l.delete_ind, l.order_type, l.user_desc, l.confidential_ind, null AS sim_group_ind, null AS sim_code, "
						+ " null AS expected_in_days, x.specialty_code, 'Y' AS orderable_ind, x.practice_id, lm.provider_id, l.ng_cds_ind, l.ng_cds_cpt_code_id "
						+ " FROM lab_favorite_xref x JOIN lab_favorite_mstr lm ON x.lab_favorite_id = lm.lab_favorite_id JOIN lab_favorite_tests f "
						+ " ON lm.lab_favorite_id = f.lab_favorite_id JOIN lab_tests_mstr l ON f.labtest_key = l.ng_labtest_key AND lm.delete_ind = l.delete_ind "
						+ " LEFT JOIN lab_dept_xref d ON l.department = d.lab_dept LEFT JOIN mstr_lists m ON d.ng_dept = m.mstr_list_item_id ) z "
						+ " WHERE z.delete_ind <> 'Y' AND z.specialty_code = '"+parameters.get("specialtyId")+"' AND z.orderable_ind = 'Y' ORDER BY z.labtest_key asc";
				
				//providerName ,"whoReviewed": null,"whenReviewed": null,

				
				String[] jsonMetaData = {"id","externalSystemName","systemId","labTestDescription","codeSystem","cost","loincCode","isDeleted",
						"orderType","isSimGroup","simCode","expectedInDays","departmentId","listItemDescription","isCds","cdsCptCodeId"};
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
