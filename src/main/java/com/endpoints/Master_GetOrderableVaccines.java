package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Master_GetOrderableVaccines 
{

	public static Boolean master_GetOrderableVaccines_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "SELECT TOP 5 [Project3].[hicl_seqno] AS hiclSequenceNumber,[Project3].[cpt] AS cptCode, [Project3].[cvx] AS cvxCode, [Project3].[vaccine_name] AS name, "
						+ "[Project3].[category_name] AS categoryName, [Project3].[practice_id] AS practiceId, "
						+ " [Project3].[gcn_seqno] AS gcnSequenceNumber, [Project3].[diagnosis_code_id] AS diagnosisCodeId, [Project3].[diagnosis_code_lib_id] "
						+ "AS diagnosisCodeLibraryId, [Project3].[icd9cm_code_id] AS icd9cmCodeId, [Project3].[description] AS diagnosisDescription, "
						+ "[Project3].[user_description] AS diagnosisUserDescription, [Project3].[display_name] AS displayName, case when [Project3].[latex_ind]= 'Y' then 'true' else 'false' END AS containsLatex, "
						+ "case when [Project3].[egg_ind]= 'Y' then 'true' else 'false' END AS containsEgg, case when [Project3].[neomycin_ind]= 'Y' then 'true' else 'false' END AS containsNeomycin, "
						+ "case when [Project3].[gelatin_ind]= 'Y' then 'true' else 'false' END AS containsGelatin "
						+ " FROM (SELECT [Distinct2].[cpt] AS [cpt], [Distinct2].[vaccine_name] AS [vaccine_name], "
						+ "[Distinct2].[cvx] AS [cvx], [Distinct2].[hicl_seqno] AS [hicl_seqno], [Distinct2].[gcn_seqno] AS [gcn_seqno], [Distinct2].[category_name] "
						+ "AS [category_name], [Distinct2].[practice_id] AS [practice_id], [Distinct2].[C1] AS [C1], [Distinct2].[C2] AS [C2], [Distinct2].[diagnosis_code_lib_id] "
						+ "AS [diagnosis_code_lib_id], [Distinct2].[diagnosis_code_id] AS [diagnosis_code_id], [Distinct2].[icd9cm_code_id] AS [icd9cm_code_id], [Distinct2].[description]  "
						+ "AS [description], [Distinct2].[user_description] AS [user_description], [Distinct2].[display_name] AS [display_name], [Distinct2].[latex_ind]  "
						+ "AS [latex_ind], [Distinct2].[egg_ind] AS [egg_ind], [Distinct2].[neomycin_ind] AS [neomycin_ind], [Distinct2].[gelatin_ind] AS [gelatin_ind] FROM (SELECT DISTINCT [Extent1].[cpt] AS [cpt], "
						+ "[Extent1].[vaccine_name] AS [vaccine_name], [Extent1].[cvx] AS [cvx], [Extent1].[hicl_seqno] AS [hicl_seqno], [Extent1].[gcn_seqno] AS [gcn_seqno], "
						+ "[Extent1].[category_name] AS [category_name], [Extent1].[practice_id] AS [practice_id], 1 AS [C1], CASE WHEN (([Distinct1].[C7] IS NULL) OR "
						+ "(N'active' = (LOWER(LTRIM (RTRIM ([Distinct1].[C7])))))) THEN CAST(1 AS BIT) WHEN (NOT (([Distinct1].[C7] IS NULL) OR ((N'active' = (LOWER(LTRIM (RTRIM ([Distinct1].[C7]))))) "
						+ "AND (LOWER(LTRIM (RTRIM ([Distinct1].[C7]))) IS NOT NULL)))) THEN CAST(0 AS BIT) END AS [C2], [Extent3].[diagnosis_code_lib_id] AS [diagnosis_code_lib_id], "
						+ "[Extent3].[diagnosis_code_id] AS [diagnosis_code_id], [Extent3].[icd9cm_code_id] AS [icd9cm_code_id], [Extent3].[description] AS [description], [Extent3].[user_description] "
						+ "AS [user_description], [Distinct1].[display_name] AS [display_name], [Distinct1].[latex_ind] AS [latex_ind], [Distinct1].[egg_ind] AS [egg_ind], [Distinct1].[neomycin_ind] "
						+ "AS [neomycin_ind], [Distinct1].[gelatin_ind] AS [gelatin_ind] FROM [dbo].[imm_vaccine_favorites] AS [Extent1] LEFT OUTER JOIN [dbo].[practice_misc] "
						+ "AS [Extent2] ON [Extent1].[practice_id] = [Extent2].[practice_id] LEFT OUTER JOIN [dbo].[diagnosis_code_mstr] AS [Extent3] ON ( (CASE WHEN ( ([Extent1].[user_icd] IS NULL) "
						+ "OR ([Extent1].[user_icd] = 0) OR ( ([Extent1].[user_icd] IS NULL) AND ([Extent1].[user_icd] IS NULL))) THEN [Extent1].[ng_icd] ELSE [Extent1].[user_icd] END) = "
						+ "[Extent3].[diagnosis_code_id]) AND ([Extent2].[diagnosis_code_lib_id] = [Extent3].[diagnosis_code_lib_id]) LEFT OUTER JOIN (SELECT DISTINCT [Extent4].[medid] AS [medid], "
						+ "[Extent4].[bn] AS [bn], [Extent7].[cvx_friendly_name] AS [cvx_friendly_name], [Extent7].[display_name] AS [display_name], [Extent7].[hide_on_chart_ind] "
						+ "AS [hide_on_chart_ind], [Extent7].[ng_icd] AS [ng_icd], [Extent7].[total_components] AS [total_components], [Extent7].[latex_ind] AS [latex_ind], [Extent7].[egg_ind] "
						+ "AS [egg_ind], [Extent7].[neomycin_ind] AS [neomycin_ind], [Extent7].[gelatin_ind] AS [gelatin_ind], [Extent7].[live_ind] AS [live_ind], LTRIM(RTRIM ([Extent4].[ndc])) "
						+ "AS [C1], LTRIM(RTRIM ([Extent4].[med_medid_desc])) AS [C2], CAST(CAST([Extent4].[gcn_seqno] AS INT) AS DECIMAL(19,0)) AS [C3], CAST([Extent5].[hicl_seqno] AS INT) AS [C4], "
						+ "REPLACE(LTRIM (REPLACE(LTRIM (RTRIM ([Extent4].[evd_cvx_cd])),N'0',N' ')),N' ',N'0') AS [C5], LTRIM(RTRIM ([Extent4].[evd_cvx_cd_desc_long])) AS [C6], CASE "
						+ "WHEN ([Extent4].[evd_cvx_cd_status] IS NULL) THEN CAST(NULL AS VARCHAR(1)) ELSE LTRIM (RTRIM ([Extent4].[evd_cvx_cd_status])) END AS [C7], LTRIM(RTRIM "
						+ "([Extent4].[evd_mvx_cd])) AS [C8], LTRIM(RTRIM ([Extent4].[evd_mvx_cd_desc])) AS [C9], CASE WHEN (([Extent6].[evd_cpt_cd] IS NOT NULL) AND "
						+ "(NOT (((LTRIM (RTRIM ([Extent6].[evd_cpt_cd]))) = 8) AND (( CASE WHEN (LTRIM (RTRIM ([Extent6].[evd_cpt_cd])) IS NULL) THEN CAST(1 AS BIT) "
						+ "ELSE CAST(0 AS BIT) END ) = ( CASE WHEN (1 IS NULL) THEN CAST(1 AS BIT) ELSE CAST(0 AS BIT) END ))))) THEN LTRIM (RTRIM ([Extent6].[evd_cpt_cd])) ELSE [Extent7].[cpt] "
						+ "END AS [C10], CASE WHEN ([Extent6].[evd_cpt_cd_desc] IS NULL) THEN CAST(NULL AS VARCHAR(1)) ELSE LTRIM (RTRIM ([Extent6].[evd_cpt_cd_desc])) END AS [C11], "
						+ "CASE WHEN ([Extent6].[evd_cpt_status] IS NULL) THEN CAST(NULL AS VARCHAR(1)) ELSE LTRIM (RTRIM ([Extent6].[evd_cpt_status])) END AS [C12], CASE WHEN (([Extent4].[obsdtec] "
						+ "IS NULL) OR (CAST(0 AS DECIMAL(18)) = [Extent4].[obsdtec]) OR (([Extent4].[obsdtec] + CAST(20000 AS DECIMAL(18))) > 2)) THEN N'Y' ELSE N'N' END AS [C13] "
						+ "FROM [dbo].[REVDNCM0] AS [Extent4] INNER JOIN [dbo].[gcnseqnotbl] AS [Extent5] ON [Extent4].[gcn_seqno] = [Extent5].[gcn_seqno] LEFT OUTER JOIN [dbo].[revdcpt0] "
						+ "AS [Extent6] ON [Extent4].[evd_cvx_cd] = [Extent6].[evd_cvx_cd] LEFT OUTER JOIN [dbo].[ng_vaccines_xref] AS [Extent7] ON ( (REPLACE (LTRIM (REPLACE (LTRIM (RTRIM "
						+ "([Extent4].[evd_cvx_cd])),N'0',N' ')),N' ',N'0')) = CAST ([Extent7].[cvx] AS NVARCHAR (MAX))) AND ([Extent6].[evd_cpt_cd] = [Extent7].[cpt])) AS [Distinct1] ON CAST ([Extent1].[cvx] AS "
						+ "NVARCHAR (MAX)) = [Distinct1].[C5]) AS [Distinct2]) AS [Project3] ORDER BY [Project3].[practice_id] ASC, [Project3].[category_name] ASC, [Project3].[vaccine_name] ASC, [Project3].[cpt] ASC, [Project3].[egg_ind] ASC, "
						+ "[Project3].[gelatin_ind] ASC, [Project3].[latex_ind] ASC, [Project3].[neomycin_ind] ASC, [Project3].[cvx] ASC, [Project3].[diagnosis_code_id] ASC, [Project3].[diagnosis_code_lib_id] ASC, [Project3].[description] ASC, "
						+ "[Project3].[user_description] ASC, [Project3].[display_name] ASC, [Project3].[gcn_seqno] ASC, [Project3].[hicl_seqno] ASC, [Project3].[icd9cm_code_id] ASC, [Project3].[C2] ASC";
				
				//providerName ,"whoReviewed": null,"whenReviewed": null,

				String[] jsonMetaData = {"hiclSequenceNumber","cptCode","cvxCode","name","categoryName","practiceId","gcnSequenceNumber","diagnosisCodeId"
						,"diagnosisCodeLibraryId","icd9cmCodeId","diagnosisDescription","diagnosisUserDescription","displayName","containsLatex","containsEgg","containsNeomycin",
						"containsGelatin"};
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..cptCode", jsonList);
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
