package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Master_GetMedications
{
	public static Boolean master_GetMedications_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{

				String strQuery = "SELECT TOP (25) [Project4].[C4] AS medicationId, [Project4].[C2] AS brandId, [Project4].[C3] AS medicationName, LTRIM([Project4].[C5]) AS medicationIdDescription,[Project4].[C9] AS gcnSequenceNumber, "
						+ "CASE[Project4].[C6] WHEN 0 THEN 'false' ELSE 'true' END AS isGeneric, CASE [Project4].[C7] WHEN ' ' THEN 'true' ELSE 'false' END AS isHidden "
						+ "FROM ( SELECT [Distinct1].[C1] AS [C1], [Distinct1].[C2] AS [C2], [Distinct1].[C3] AS [C3], [Distinct1].[C4] AS [C4], [Distinct1].[C5] AS [C5], [Distinct1].[C6] AS [C6], "
						+ "[Distinct1].[C7] AS [C7], [Distinct1].[C8] AS [C8], [Distinct1].[C9] AS [C9] FROM ( SELECT DISTINCT [UnionAll1].[C1] AS [C1], [UnionAll1].[med_name_id] AS [C2], "
						+ "[UnionAll1].[C2] AS [C3], [UnionAll1].[C3] AS [C4], [UnionAll1].[medid_desc] AS [C5], [UnionAll1].[C4] AS [C6], [UnionAll1].[hidden_flag] AS [C7], "
						+ "[UnionAll1].[ndc_id] AS [C8], [UnionAll1].[gcn_seqno] AS [C9] FROM (SELECT 1 AS [C1], [Extent1].[med_name_id] AS [med_name_id], CASE WHEN ((1 = 1) "
						+ "AND ([Extent3].[tm_alt_med_name_desc] IS NOT NULL) AND ( NOT (((LTRIM(RTRIM([Extent3].[tm_alt_med_name_desc]))) = '') AND ((CASE WHEN (LTRIM(RTRIM([Extent3].[tm_alt_med_name_desc])) IS NULL) "
						+ "THEN cast(1 as bit) ELSE cast(0 as bit) END) = (CASE WHEN ('' IS NULL) THEN cast(1 as bit) ELSE cast(0 as bit) END))))) THEN LTRIM(RTRIM([Extent3].[tm_alt_med_name_desc])) WHEN ((1 = 1) "
						+ "AND ([Extent3].[tm_alt_medid_desc] IS NOT NULL) AND ( NOT (((LTRIM(RTRIM([Extent3].[tm_alt_medid_desc]))) = '') AND ((CASE WHEN (LTRIM(RTRIM([Extent3].[tm_alt_medid_desc])) IS NULL) "
						+ "THEN cast(1 as bit) ELSE cast(0 as bit) END) = (CASE WHEN ('' IS NULL) THEN cast(1 as bit) ELSE cast(0 as bit) END))))) THEN LTRIM(RTRIM([Extent3].[tm_alt_medid_desc])) ELSE [Extent1].[med_name] "
						+ "END AS [C2], CAST( CAST( [Extent1].[medid] AS int) AS decimal(19,0)) AS [C3], [Extent1].[medid_desc] AS [medid_desc], CASE WHEN (cast(0 as decimal(18)) = [Extent1].[generic_medid]) "
						+ "THEN cast(1 as bit) WHEN (cast(0 as decimal(18)) <> [Extent1].[generic_medid]) THEN cast(0 as bit) END AS [C4], [Extent2].[hidden_flag] AS [hidden_flag], [Extent4].[ndc_id] AS [ndc_id], [Extent1].[gcn_seqno] "
						+ "AS [gcn_seqno] FROM [dbo].[fdb_med_name_search_mstr] AS [Extent1] LEFT OUTER JOIN [dbo].[medication_hidden] AS [Extent2] ON [Extent1].[medid] = [Extent2].[medid] LEFT OUTER JOIN [dbo].[fdb_tall_man_name_mstr] "
						+ "AS [Extent3] ON [Extent1].[medid] = [Extent3].[medid] LEFT OUTER JOIN [dbo].[fdb_med_single] AS [Extent4] ON [Extent1].[medid] = [Extent4].[medid] WHERE ([Extent2].[hidden_flag] IS NULL) "
						+ "OR (N'0' = (LTRIM(RTRIM([Extent2].[hidden_flag])))) OR (N'n' = (LOWER(LTRIM(RTRIM([Extent2].[hidden_flag]))))) UNION ALL SELECT 1 AS [C1], cast(-1 as decimal(18)) AS [C2], CASE WHEN ((1 = 1) "
						+ "AND ([Extent6].[tm_alt_med_name_desc] IS NOT NULL) AND ( NOT (((LTRIM(RTRIM([Extent6].[tm_alt_med_name_desc]))) = '') AND ((CASE WHEN (LTRIM(RTRIM([Extent6].[tm_alt_med_name_desc])) IS NULL) "
						+ "THEN cast(1 as bit) ELSE cast(0 as bit) END) = (CASE WHEN ('' IS NULL) THEN cast(1 as bit) ELSE cast(0 as bit) END))))) THEN LTRIM(RTRIM([Extent6].[tm_alt_med_name_desc])) WHEN ((1 = 1) "
						+ "AND ([Extent6].[tm_alt_medid_desc] IS NOT NULL) AND ( NOT (((LTRIM(RTRIM([Extent6].[tm_alt_medid_desc]))) = '') AND ((CASE WHEN (LTRIM(RTRIM([Extent6].[tm_alt_medid_desc])) IS NULL) "
						+ "THEN cast(1 as bit) ELSE cast(0 as bit) END) = (CASE WHEN ('' IS NULL) THEN cast(1 as bit) ELSE cast(0 as bit) END))))) THEN LTRIM(RTRIM([Extent6].[tm_alt_medid_desc])) ELSE [Extent5].[brand_name] "
						+ "END AS [C3], CASE WHEN ([Extent5].[medid] IS NULL) THEN cast(0 as decimal(18)) ELSE [Extent5].[medid] END AS [C4], CASE WHEN (LTRIM(RTRIM(CASE WHEN (CASE WHEN (N'2' = [Extent5].[generic_indicator]) "
						+ "THEN [Extent5].[brand_name] ELSE [Extent5].[generic_name] END IS NULL) THEN '' WHEN (N'2' = [Extent5].[generic_indicator]) THEN [Extent5].[brand_name] ELSE [Extent5].[generic_name] END)) IS NULL) "
						+ "THEN N'' ELSE LTRIM(RTRIM(CASE WHEN (CASE WHEN (N'2' = [Extent5].[generic_indicator]) THEN [Extent5].[brand_name] ELSE [Extent5].[generic_name] END IS NULL) THEN '' WHEN (N'2' = [Extent5].[generic_indicator]) "
						+ "THEN [Extent5].[brand_name] ELSE [Extent5].[generic_name] END)) END + CASE WHEN (CASE WHEN (([Extent5].[dose] IS NOT NULL) AND ( NOT (((LTRIM(RTRIM([Extent5].[dose]))) = '') "
						+ "AND ((CASE WHEN (LTRIM(RTRIM([Extent5].[dose])) IS NULL) THEN cast(1 as bit) ELSE cast(0 as bit) END) = (CASE WHEN ('' IS NULL) THEN cast(1 as bit) ELSE cast(0 as bit) END))))) "
						+ "THEN N' ' + CASE WHEN (LTRIM(RTRIM([Extent5].[dose])) IS NULL) THEN N'' ELSE LTRIM(RTRIM([Extent5].[dose])) END ELSE '' END IS NULL) THEN N'' WHEN (([Extent5].[dose] IS NOT NULL) "
						+ "AND ( NOT (((LTRIM(RTRIM([Extent5].[dose]))) = '') AND ((CASE WHEN (LTRIM(RTRIM([Extent5].[dose])) IS NULL) THEN cast(1 as bit) ELSE cast(0 as bit) END) = (CASE WHEN ('' IS NULL) "
						+ "THEN cast(1 as bit) ELSE cast(0 as bit) END))))) THEN N' ' + CASE WHEN (LTRIM(RTRIM([Extent5].[dose])) IS NULL) THEN N'' ELSE LTRIM(RTRIM([Extent5].[dose])) END ELSE '' END + CASE "
						+ "WHEN (CASE WHEN (([Extent5].[route_desc] IS NOT NULL) AND ( NOT (((LTRIM(RTRIM([Extent5].[route_desc]))) = '') AND ((CASE WHEN (LTRIM(RTRIM([Extent5].[route_desc])) IS NULL) "
						+ "THEN cast(1 as bit) ELSE cast(0 as bit) END) = (CASE WHEN ('' IS NULL) THEN cast(1 as bit) ELSE cast(0 as bit) END))))) THEN N' ' + CASE WHEN (LTRIM(RTRIM([Extent5].[route_desc])) IS NULL) "
						+ "THEN N'' ELSE LTRIM(RTRIM([Extent5].[route_desc])) END ELSE '' END IS NULL) THEN N'' WHEN (([Extent5].[route_desc] IS NOT NULL) AND ( NOT (((LTRIM(RTRIM([Extent5].[route_desc]))) = '') "
						+ "AND ((CASE WHEN (LTRIM(RTRIM([Extent5].[route_desc])) IS NULL) THEN cast(1 as bit) ELSE cast(0 as bit) END) = (CASE WHEN ('' IS NULL) THEN cast(1 as bit) ELSE cast(0 as bit) END))))) "
						+ "THEN N' ' + CASE WHEN (LTRIM(RTRIM([Extent5].[route_desc])) IS NULL) THEN N'' ELSE LTRIM(RTRIM([Extent5].[route_desc])) END ELSE '' END + CASE WHEN (CASE WHEN (([Extent5].[dose_form_desc] IS NOT NULL) "
						+ "AND ( NOT (((LTRIM(RTRIM([Extent5].[dose_form_desc]))) = '') AND ((CASE WHEN (LTRIM(RTRIM([Extent5].[dose_form_desc])) IS NULL) THEN cast(1 as bit) ELSE cast(0 as bit) END) = (CASE WHEN ('' IS NULL) "
						+ "THEN cast(1 as bit) ELSE cast(0 as bit) END))))) THEN N' ' + CASE WHEN (LTRIM(RTRIM([Extent5].[dose_form_desc])) IS NULL) THEN N'' ELSE LTRIM(RTRIM([Extent5].[dose_form_desc])) END ELSE '' END IS NULL) THEN N'' "
						+ "WHEN (([Extent5].[dose_form_desc] IS NOT NULL) AND ( NOT (((LTRIM(RTRIM([Extent5].[dose_form_desc]))) = '') AND ((CASE WHEN (LTRIM(RTRIM([Extent5].[dose_form_desc])) IS NULL) THEN cast(1 as bit) "
						+ "ELSE cast(0 as bit) END) = (CASE WHEN ('' IS NULL) THEN cast(1 as bit) ELSE cast(0 as bit) END))))) THEN N' ' + CASE WHEN (LTRIM(RTRIM([Extent5].[dose_form_desc])) IS NULL) THEN N'' ELSE LTRIM(RTRIM([Extent5].[dose_form_desc])) "
						+ "END ELSE '' END AS [C5], CASE WHEN ( NOT ((N'2' = [Extent5].[generic_indicator]) AND ([Extent5].[generic_indicator] IS NOT NULL))) THEN cast(1 as bit) WHEN (N'2' = [Extent5].[generic_indicator]) THEN cast(0 as bit) "
						+ "END AS [C6], CASE WHEN ((N'Y' = [Extent5].[delete_ind]) OR (N'Y' <> [Extent5].[available_ind])) THEN N'Y' ELSE N'N' END AS [C7], [Extent5].[ndc_id] AS [ndc_id], CAST( [Extent5].[gcn_seqno] AS decimal(19,0)) "
						+ "AS [C8] FROM [dbo].[fdb_medication] AS [Extent5] LEFT OUTER JOIN [dbo].[fdb_tall_man_name_mstr] AS [Extent6] ON [Extent5].[medid] = [Extent6].[medid] WHERE ([Extent5].[ndc_id] LIKE N'UDM%') "
						+ "AND ((CASE WHEN ((N'Y' = [Extent5].[delete_ind]) OR (N'Y' <> [Extent5].[available_ind])) THEN N'Y' ELSE N'N' END IS NULL) OR (N'0' = (LTRIM(RTRIM(CASE WHEN ((N'Y' = [Extent5].[delete_ind]) "
						+ "OR (N'Y' <> [Extent5].[available_ind])) THEN N'Y' ELSE N'N' END)))) OR (N'n' = (LOWER(LTRIM(RTRIM(CASE WHEN ((N'Y' = [Extent5].[delete_ind]) OR (N'Y' <> [Extent5].[available_ind])) "
						+ "THEN N'Y' ELSE N'N' END))))))) AS [UnionAll1] ) AS [Distinct1] ) AS [Project4] ORDER BY [Project4].[C2] ASC, [Project4].[C9] ASC, [Project4].[C6] ASC, [Project4].[C7] ASC, [Project4].[C4] ASC, "
						+ "[Project4].[C5] ASC, [Project4].[C3] ASC";

				String[] jsonMetaData = {"medicationId","brandId", "medicationName",  "medicationIdDescription", "gcnSequenceNumber", "isGeneric", "isHidden" };

				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..medicationId", jsonList);
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