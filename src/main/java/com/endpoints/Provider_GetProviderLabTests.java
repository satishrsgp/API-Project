package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Provider_GetProviderLabTests {


	public static Boolean Provider_GetProviderLabTests_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery="SELECT TOP (25) "
						+ "[Project4].[C4] AS id, "
						+ "[Project4].[C2] AS externalSystemName, "
						+ "[Project4].[C3] AS systemId,     "
						+ "[Project4].[C5] AS labTestDescription, "
						+ "[Project4].[C7] AS codeSystem, "
						+ "[Project4].[C8] AS cost, "
						+ "[Project4].[C9] AS loincCode, "
						+ "case when [Project4].[C11] = 'N' then 'false' else 'true' end AS isDeleted, "
						+ "[Project4].[C12] AS orderType, "
						+ "case when [Project4].[C15] = 'N' then 'false' else 'true' end AS isSimGroup, "
						+ "[Project4].[C16] AS simCode,"
						+ "[Project4].[C17] AS expectedInDays,  "
						+ "[Project4].[C10] AS departmentId,"
						+ "[Project4].[C6] AS listItemDescription, "
						+ "case when [Project4].[C22] = 'N' then 'false' else 'true' end AS isCds,  "
						+ "[Project4].[C23] AS cdsCptCodeId "
						+ "FROM ( SELECT  "
						+ "[Distinct1].[C1] AS [C1], "
						+ "[Distinct1].[C2] AS [C2], "
						+ "[Distinct1].[C3] AS [C3], "
						+ "[Distinct1].[C4] AS [C4], "
						+ "[Distinct1].[C5] AS [C5], "
						+ "[Distinct1].[C6] AS [C6], "
						+ "[Distinct1].[C7] AS [C7], "
						+ "[Distinct1].[C8] AS [C8], "
						+ "[Distinct1].[C9] AS [C9], "
						+ "[Distinct1].[C10] AS [C10], "
						+ "[Distinct1].[C11] AS [C11], "
						+ "[Distinct1].[C12] AS [C12], "
						+ "[Distinct1].[C13] AS [C13], "
						+ "[Distinct1].[C14] AS [C14], "
						+ "[Distinct1].[C15] AS [C15], "
						+ "[Distinct1].[C16] AS [C16], "
						+ "[Distinct1].[C17] AS [C17], "
						+ "[Distinct1].[C18] AS [C18], "
						+ "[Distinct1].[C19] AS [C19], "
						+ "[Distinct1].[C20] AS [C20], "
						+ "[Distinct1].[C21] AS [C21], "
						+ "[Distinct1].[C22] AS [C22], "
						+ "[Distinct1].[C23] AS [C23] "
						+ "FROM ( SELECT DISTINCT  "
						+ "    [UnionAll1].[C1] AS [C1], "
						+ "[UnionAll1].[C2] AS [C2], "
						+ "[UnionAll1].[C3] AS [C3], "
						+ "[UnionAll1].[ng_labtest_key] AS [C4], "
						+ "[UnionAll1].[ng_labtest_desc] AS [C5], "
						+ "[UnionAll1].[mstr_list_item_desc] AS [C6], "
						+ "[UnionAll1].[code_system] AS [C7], "
						+ "[UnionAll1].[C4] AS [C8], "
						+ "[UnionAll1].[C5] AS [C9], "
						+ "[UnionAll1].[department] AS [C10], "
						+ "[UnionAll1].[delete_ind] AS [C11], "
						+ "[UnionAll1].[order_type] AS [C12], "
						+ "[UnionAll1].[user_desc] AS [C13], "
						+ "[UnionAll1].[confidential_ind] AS [C14], "
						+ "[UnionAll1].[C6] AS [C15], "
						+ "[UnionAll1].[C7] AS [C16], "
						+ "[UnionAll1].[C8] AS [C17], "
						+ "[UnionAll1].[specialty_code] AS [C18], "
						+ "[UnionAll1].[C9] AS [C19], "
						+ "[UnionAll1].[practice_id] AS [C20], "
						+ "[UnionAll1].[provider_id] AS [C21], "
						+ "[UnionAll1].[ng_cds_ind] AS [C22], "
						+ "[UnionAll1].[ng_cds_cpt_code_id] AS [C23] "
						+ "FROM  (SELECT "
						+ "1 AS [C1],  "
						+ "CAST(NULL AS varchar(1)) AS [C2], "
						+ "CAST(NULL AS int) AS [C3], "
						+ "[Filter1].[ng_labtest_key] AS [ng_labtest_key], "
						+ "[Filter1].[ng_labtest_desc] AS [ng_labtest_desc], "
						+ "[Extent4].[mstr_list_item_desc] AS [mstr_list_item_desc], "
						+ "[Filter1].[code_system] AS [code_system], "
						+ "CAST(NULL AS decimal(18,0)) AS [C4], "
						+ "CAST(NULL AS varchar(1)) AS [C5], "
						+ "[Filter1].[department] AS [department], "
						+ "[Filter1].[delete_ind1] AS [delete_ind],"
						+ "[Filter1].[order_type1] AS [order_type], "
						+ "[Filter1].[user_desc] AS [user_desc], "
						+ "[Filter1].[confidential_ind] AS [confidential_ind], "
						+ "CAST(NULL AS varchar(1)) AS [C6], "
						+ "CAST(NULL AS varchar(1)) AS [C7], "
						+ "CAST(NULL AS int) AS [C8], "
						+ "[Extent5].[specialty_code] AS [specialty_code], "
						+ "N'Y' AS [C9], "
						+ "[Extent5].[practice_id] AS [practice_id], "
						+ "[Filter1].[provider_id] AS [provider_id], "
						+ "[Filter1].[ng_cds_ind] AS [ng_cds_ind], "
						+ "[Filter1].[ng_cds_cpt_code_id] AS [ng_cds_cpt_code_id] "
						+ "FROM    (SELECT [Extent1].[lab_favorite_id] AS [lab_favorite_id1], [Extent2].[provider_id] AS [provider_id], [Extent3].[ng_labtest_key] AS [ng_labtest_key], [Extent3].[ng_labtest_desc] AS [ng_labtest_desc], [Extent3].[code_system] AS [code_system], [Extent3].[department] AS [department], [Extent3].[delete_ind] AS [delete_ind1], [Extent3].[order_type] AS [order_type1], [Extent3].[user_desc] AS [user_desc], [Extent3].[confidential_ind] AS [confidential_ind], [Extent3].[ng_cds_ind] AS [ng_cds_ind], [Extent3].[ng_cds_cpt_code_id] AS [ng_cds_cpt_code_id] "
						+ "FROM   [dbo].[lab_favorite_tests] AS [Extent1] "
						+ "INNER JOIN [dbo].[lab_favorite_mstr] AS [Extent2] ON [Extent1].[lab_favorite_id] = [Extent2].[lab_favorite_id] "
						+ "INNER JOIN [dbo].[lab_tests_mstr] AS [Extent3] ON [Extent1].[labtest_key] = [Extent3].[ng_labtest_key] WHERE (N'Y' <> [Extent2].[delete_ind]) AND (N'Y' = N'Y') AND ((N'0' = (LTRIM(RTRIM([Extent3].[delete_ind])))) OR (N'n' = (LOWER(LTRIM(RTRIM([Extent3].[delete_ind])))))) ) AS [Filter1] "
						+ "LEFT OUTER JOIN [dbo].[mstr_lists] AS [Extent4] ON [Filter1].[department] = [Extent4].[mstr_list_item_id] "
						+ "LEFT OUTER JOIN [dbo].[lab_favorite_xref] AS [Extent5] ON [Filter1].[lab_favorite_id1] = [Extent5].[lab_favorite_id] "
						+ "UNION ALL "
						+ "SELECT  "
						+ "1 AS [C1],  "
						+ "[Extent8].[external_system_name] AS [external_system_name],  "
						+ "[Extent9].[system_id] AS [system_id],  "
						+ "[Extent9].[labtest_key] AS [labtest_key],  "
						+ "[Extent9].[labtest_desc] AS [labtest_desc],  "
						+ "[Extent11].[mstr_list_item_desc] AS [mstr_list_item_desc], "
						+ "[Extent9].[code_system] AS [code_system], "
						+ "[Extent9].[cost] AS [cost], "
						+ "[Extent9].[loinc_code] AS [loinc_code],  "
						+ "[Extent10].[ng_dept] AS [ng_dept],  "
						+ "[Extent9].[delete_ind] AS [delete_ind],  "
						+ "[Extent9].[order_type] AS [order_type], "
						+ "CAST(NULL AS varchar(1)) AS [C2], "
						+ "CAST(NULL AS varchar(1)) AS [C3], "
						+ "[Extent9].[sim_group_ind] AS [sim_group_ind],  "
						+ "[Extent9].[sim_code] AS [sim_code],  "
						+ "[Extent9].[expected_in_days] AS [expected_in_days],  "
						+ "[Extent12].[specialty_code] AS [specialty_code], "
						+ "[Extent9].[orderable_ind] AS [orderable_ind],  "
						+ "[Extent12].[practice_id] AS [practice_id],  "
						+ "[Extent7].[provider_id] AS [provider_id], "
						+ "[Extent9].[cds_ind] AS [cds_ind], "
						+ "[Extent9].[cds_cpt_code_id] AS [cds_cpt_code_id] "
						+ "FROM       [dbo].[lab_favorite_tests] AS [Extent6] "
						+ "INNER JOIN [dbo].[lab_favorite_mstr] AS [Extent7] ON [Extent6].[lab_favorite_id] = [Extent7].[lab_favorite_id] "
						+ "INNER JOIN [dbo].[external_system] AS [Extent8] ON [Extent7].[external_system_id] = [Extent8].[external_system_id] "
						+ "INNER JOIN [dbo].[lab_tests_xref] AS [Extent9] ON ([Extent6].[labtest_key] = [Extent9].[labtest_key]) AND ([Extent8].[external_system_id] = [Extent9].[system_id]) "
						+ "LEFT OUTER JOIN [dbo].[lab_dept_xref] AS [Extent10] ON [Extent9].[labdept] = [Extent10].[lab_dept] "
						+ "LEFT OUTER JOIN [dbo].[mstr_lists] AS [Extent11] ON [Extent10].[ng_dept] = [Extent11].[mstr_list_item_id]"
						+ "LEFT OUTER JOIN [dbo].[lab_favorite_xref] AS [Extent12] ON [Extent6].[lab_favorite_id] = [Extent12].[lab_favorite_id] "
						+ "WHERE ((N'0' = (LTRIM(RTRIM([Extent9].[delete_ind])))) OR (N'n' = (LOWER(LTRIM(RTRIM([Extent9].[delete_ind])))))) AND (N'Y' = [Extent9].[orderable_ind])) AS [UnionAll1] "
						+ ")  AS [Distinct1] "
						+ "WHERE [Distinct1].[C21] = '"+parameters.get("providerId")+"'"
						+ ")  AS [Project4]"
						+ "ORDER BY [Project4].[C4] ASC";
								
				String[] jsonMetaData = {"id","externalSystemName","systemId","labTestDescription","codeSystem","cost",
						"loincCode","isDeleted","orderType","isSimGroup","simCode","expectedInDays","departmentId","listItemDescription",
						"isCds","cdsCptCodeId"};

				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$.."+strUniqueJsonToken, jsonList);
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
