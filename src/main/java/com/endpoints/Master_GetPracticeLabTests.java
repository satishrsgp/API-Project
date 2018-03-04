package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Master_GetPracticeLabTests 
{

	public static Boolean master_GetPracticeLabTests_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{   //[Distinct1].[C8] AS cost,
				String strQuery = "SELECT top 25 [Distinct1].[C4] AS id, [Distinct1].[C2] AS externalSystemName, [Distinct1].[C3] AS system_id, [Distinct1].[C6] AS labTestDescription,"
						+ " [Distinct1].[C7] AS codeSystem,  [Distinct1].[C9] AS loincCode, case when  [Distinct1].[C11] = 'N' then 'false' else 'true' end  as isDeleted, "
						+ "[Distinct1].[C12] AS orderType, case when   [Distinct1].[C15] = 'N' then 'false' else 'true' end as  isSimGroup, [Distinct1].[C16] AS simCode, [Distinct1].[C17] AS expectedInDays, "
						+ "[Distinct1].[C5] AS listItemDescription, case when    [Distinct1].[C22]= 'N' then 'false' else 'true' end as  isCds, [Distinct1].[C23] AS cdsCptCodeId FROM ( SELECT DISTINCT [UnionAll1].[C1] AS [C1],"
						+ " [UnionAll1].[external_system_name] AS [C2], [UnionAll1].[system_id] AS [C3], [UnionAll1].[labtest_key] AS [C4], [UnionAll1].[mstr_list_item_desc] AS [C5], "
						+ "[UnionAll1].[labtest_desc] AS [C6], [UnionAll1].[code_system] AS [C7], [UnionAll1].[cost] AS [c8], [UnionAll1].[loinc_code] AS [C9],"
						+ " [UnionAll1].[ng_dept] AS [C10], [UnionAll1].[delete_ind] AS [C11], [UnionAll1].[order_type] AS [C12], [UnionAll1].[C2] AS [C13], [UnionAll1].[C3] AS [C14],"
						+ " [UnionAll1].[sim_group_ind] AS [C15], [UnionAll1].[sim_code] AS [C16], [UnionAll1].[expected_in_days] AS [C17], [UnionAll1].[specialty_code] AS [C18],"
						+ " [UnionAll1].[orderable_ind] AS [C19], [UnionAll1].[practice_id] AS [C20], [UnionAll1].[provider_id] AS [C21], [UnionAll1].[cds_ind] AS [C22],"
						+ " [UnionAll1].[cds_cpt_code_id] AS [C23] FROM (SELECT 1 AS [C1], [Extent3].[external_system_name] AS [external_system_name], [Extent5].[system_id] AS [system_id],"
						+ " [Extent5].[labtest_key] AS [labtest_key], [Extent7].[mstr_list_item_desc] AS [mstr_list_item_desc], [Extent5].[labtest_desc] AS [labtest_desc],"
						+ " [Extent5].[code_system] AS [code_system], [Extent5].[cost] AS [cost], [Extent5].[loinc_code] AS [loinc_code], [Extent6].[ng_dept] AS [ng_dept],"
						+ " [Extent5].[delete_ind] AS [delete_ind], [Extent5].[order_type] AS [order_type], CAST(NULL AS varchar(1)) AS [C2], CAST(NULL AS varchar(1)) AS [C3],"
						+ " [Extent5].[sim_group_ind] AS [sim_group_ind], [Extent5].[sim_code] AS [sim_code], [Extent5].[expected_in_days] AS [expected_in_days], "
						+ "[Extent1].[specialty_code] AS [specialty_code], [Extent5].[orderable_ind] AS [orderable_ind], [Extent1].[practice_id] AS [practice_id], "
						+ "[Extent2].[provider_id] AS [provider_id], [Extent5].[cds_ind] AS [cds_ind], [Extent5].[cds_cpt_code_id] AS [cds_cpt_code_id]"
						+ " FROM [dbo].[lab_favorite_xref] AS [Extent1] INNER JOIN [dbo].[lab_favorite_mstr] AS [Extent2] ON [Extent1].[lab_favorite_id] = [Extent2].[lab_favorite_id] "
						+ "INNER JOIN [dbo].[external_system] AS [Extent3] ON [Extent2].[external_system_id] = [Extent3].[external_system_id] "
						+ "INNER JOIN [dbo].[lab_favorite_tests] AS [Extent4] ON [Extent2].[lab_favorite_id] = [Extent4].[lab_favorite_id] "
						+ "INNER JOIN [dbo].[lab_tests_xref] AS [Extent5] ON ([Extent4].[labtest_key] = [Extent5].[labtest_key]) "
						+ "AND ([Extent3].[external_system_id] = [Extent5].[system_id]) AND ([Extent2].[delete_ind] = [Extent5].[delete_ind]) "
						+ "LEFT OUTER JOIN [dbo].[lab_dept_xref] AS [Extent6] ON [Extent5].[labdept] = [Extent6].[lab_dept] LEFT OUTER JOIN [dbo].[mstr_lists] AS [Extent7] "
						+ "ON [Extent6].[ng_dept] = [Extent7].[mstr_list_item_id] WHERE ((N'0' = (LTRIM(RTRIM([Extent5].[delete_ind])))) "
						+ "OR (N'n' = (LOWER(LTRIM(RTRIM([Extent5].[delete_ind])))))) AND (N'Y' = [Extent5].[orderable_ind]) UNION ALL SELECT 1 AS [C1], "
						+ "CAST(NULL AS varchar(1)) AS [C2], CAST(NULL AS int) AS [C3], [Extent11].[ng_labtest_key] AS [ng_labtest_key], "
						+ "[Extent12].[mstr_list_item_desc] AS [mstr_list_item_desc], [Extent11].[ng_labtest_desc] AS [ng_labtest_desc], [Extent11].[code_system] AS [code_system], "
						+ "CAST(NULL AS decimal(18,0)) AS [C4], CAST(NULL AS varchar(1)) AS [C5], [Extent11].[department] AS [department], "
						+ "[Extent11].[delete_ind] AS [delete_ind], [Extent11].[order_type] AS [order_type], [Extent11].[user_desc] AS [user_desc], [Extent11].[confidential_ind]"
						+ " AS [confidential_ind], CAST(NULL AS varchar(1)) AS [C6], CAST(NULL AS varchar(1)) AS [C7], CAST(NULL AS int) AS [C8], "
						+ "[Extent8].[specialty_code] AS [specialty_code], N'Y' AS [C9], [Extent8].[practice_id] AS [practice_id], [Extent9].[provider_id] AS [provider_id], "
						+ "[Extent11].[ng_cds_ind] AS [ng_cds_ind], [Extent11].[ng_cds_cpt_code_id] AS [ng_cds_cpt_code_id] FROM [dbo].[lab_favorite_xref] AS [Extent8] "
						+ "INNER JOIN [dbo].[lab_favorite_mstr] AS [Extent9] ON [Extent8].[lab_favorite_id] = [Extent9].[lab_favorite_id] INNER JOIN [dbo].[lab_favorite_tests] AS [Extent10] "
						+ "ON [Extent9].[lab_favorite_id] = [Extent10].[lab_favorite_id] INNER JOIN [dbo].[lab_tests_mstr] AS [Extent11] "
						+ "ON ([Extent10].[labtest_key] = [Extent11].[ng_labtest_key]) AND ([Extent9].[delete_ind] = [Extent11].[delete_ind]) "
						+ "LEFT OUTER JOIN [dbo].[mstr_lists] AS [Extent12] ON [Extent11].[department] = [Extent12].[mstr_list_item_id] WHERE (N'Y' = N'Y') "
						+ "AND ((N'0' = (LTRIM(RTRIM([Extent11].[delete_ind])))) OR (N'n' = (LOWER(LTRIM(RTRIM([Extent11].[delete_ind]))))))) AS [UnionAll1] ) "
						+ "AS [Distinct1] WHERE [Distinct1].[C20] = '"+parameters.get("practiceId")+"' order by [Distinct1].[C4]";
						;
				
				String[] jsonMetaData = {"id","externalSystemName","systemId","labTestDescription","codeSystem",/*"cost",*/"loincCode",
				"isDeleted","orderType","isSimGroup","simCode","expectedInDays","listItemDescription","isCds","cdsCptCodeId"};
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
