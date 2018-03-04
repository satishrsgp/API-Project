package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Master_GetRelatedMedications
{

	public static Boolean master_GetRelatedMedications_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{

				String strQuery = "Select "
						+ "[Distinct1].[medid1] AS medicationId,[Distinct1].[gcn_seqno] AS gcnSequenceNumber,[Distinct1].[med_name_id] AS brandId, [Distinct1].[etc_id] AS etcId, "
						+ "[Distinct1].[generic_medid] AS genericMedicationId, [Distinct1].[generic_name] AS genericName, [Distinct1].hic3 as hic3, "
						+ "[Distinct1].[C4] AS hic3SequenceNumber,  " 
						+ "Case when [Distinct1].[C3] = '0' then 'false' else 'true' end as isGeneric,  "
						+ "case when [Distinct1].[C5] = '0' then 'false' else 'true' end  AS isHidden, "
						+ "[Distinct1].[med_dosage_form_desc] AS medicationDosageFormDescription, [Distinct1].[med_dosage_form_id] AS medicationDosageFormId,  "
						+ "[Distinct1].[medid_desc] AS medicationIdDescription,    [Distinct1].[C2] AS medicationName,  "
						+ "[Distinct1].[med_ref_dea_class_code] AS medicationRefDeaClassCode, [Distinct1].[med_route_desc] AS medicationRouteDescription,  "
						+ "[Distinct1].[med_route_id] AS medicationRouteId, [Distinct1].[med_strength] AS medicationStrength, [Distinct1].[med_strength_numeric] AS medicationStrengthNumeric, "
						+ "[Distinct1].[med_strength_uom] AS medicationStrengthUom, [Distinct1].[ndc_id] AS ndcId  "
						+ "FROM ( SELECT DISTINCT  " 
						+ "[Extent1].[medid] AS [medid], [Extent2].[medid] AS [medid1], [Extent2].[generic_medid] AS [generic_medid],  " 
						+ "[Extent2].[medid_desc] AS [medid_desc], [Extent2].[med_name_id] AS [med_name_id], [Extent2].[gcn_seqno] AS [gcn_seqno],  "
						+ "[Extent2].[med_route_id] AS [med_route_id], [Extent2].[med_strength] AS [med_strength], [Extent2].[med_strength_uom] AS [med_strength_uom],  "
						+ "[Extent2].[med_dosage_form_desc] AS [med_dosage_form_desc], [Extent2].[med_ref_dea_class_code] AS [med_ref_dea_class_code],  "
						+ "[Extent2].[med_dosage_form_id] AS [med_dosage_form_id], [Extent2].[med_strength_numeric] AS [med_strength_numeric],  "
						+ "[Extent3].[ndc_id] AS [ndc_id], [Extent4].[generic_name] AS [generic_name], [Extent6].[med_route_desc] AS [med_route_desc],  "
						+ "[Extent7].[hic3] AS [hic3], [Extent7].hic3_seqn as hic3_seqn,1 AS [C1],  "
						+ "CASE WHEN ([Extent5].[tm_alt_med_name_desc] IS NOT NULL) AND ( NOT (((LTRIM(RTRIM([Extent5].[tm_alt_med_name_desc]))) = '') AND ((CASE WHEN (LTRIM(RTRIM([Extent5].[tm_alt_med_name_desc])) IS NULL) THEN cast(1 as bit) ELSE cast(0 as bit) END) = (CASE WHEN ('' IS NULL) THEN cast(1 as bit) ELSE cast(0 as bit) END)))) THEN LTRIM(RTRIM([Extent5].[tm_alt_med_name_desc])) WHEN ( ([Extent5].[tm_alt_medid_desc] IS NOT NULL) AND ( NOT (((LTRIM(RTRIM([Extent5].[tm_alt_medid_desc]))) = '') AND ((CASE WHEN (LTRIM(RTRIM([Extent5].[tm_alt_medid_desc])) IS NULL) THEN cast(1 as bit) ELSE cast(0 as bit) END) = (CASE WHEN ('' IS NULL) THEN cast(1 as bit) ELSE cast(0 as bit) END))))) THEN LTRIM(RTRIM([Extent5].[tm_alt_medid_desc])) ELSE [Extent2].[med_name] END AS [C2],  "
						+ "CASE WHEN (cast(0 as decimal(18)) = [Extent2].[generic_medid]) THEN cast(1 as bit) WHEN (cast(0 as decimal(18)) <> [Extent2].[generic_medid]) THEN cast(0 as bit) END AS [C3],  "
						+ "CASE WHEN ([Extent7].[hicl_seqno] IS NULL) THEN N'' ELSE  CAST( [Extent7].[hicl_seqno] AS nvarchar(max)) END AS [C4],  "
						+ "CASE WHEN (N'Y' = [Extent9].[hidden_flag]) THEN cast(1 as bit) WHEN ( NOT ((N'Y' = [Extent9].[hidden_flag]) AND ([Extent9].[hidden_flag] IS NOT NULL))) THEN cast(0 as bit) END AS [C5],  "
						+ "[Extent8].[etc_id] AS [etc_id] "
						+ "FROM  [dbo].[fdb_med_name_search_mstr] AS [Extent1] "
						+ "INNER JOIN [dbo].[fdb_med_name_search_mstr] AS [Extent2] ON ([Extent1].[med_name_id] = [Extent2].[med_name_id]) AND ([Extent1].[med_dosage_form_id] = [Extent2].[med_dosage_form_id]) AND ([Extent1].[med_route_id] = [Extent2].[med_route_id]) "
						+ "INNER JOIN [dbo].[fdb_med_single] AS [Extent3] ON [Extent2].[medid] = [Extent3].[medid] "
						+ "INNER JOIN [dbo].[fdb_medication] AS [Extent4] ON [Extent2].[medid] = [Extent4].[medid] "
						+ "LEFT OUTER JOIN [dbo].[fdb_tall_man_name_mstr] AS [Extent5] ON [Extent2].[medid] = [Extent5].[medid] "
						+ "INNER JOIN [dbo].[fdb_med_route_mstr] AS [Extent6] ON [Extent2].[med_route_id] = [Extent6].[med_route_id] "
						+ "INNER JOIN [dbo].[gcnseqnotbl] AS [Extent7] ON [Extent2].[gcn_seqno] = [Extent7].[gcn_seqno] "
						+ "LEFT OUTER JOIN [dbo].[RETCMED0] AS [Extent8] ON (N'1' = [Extent8].[etc_default_use_flag]) AND ([Extent2].[medid] = [Extent8].[medid]) "
						+ "LEFT OUTER JOIN [dbo].[medication_hidden] AS [Extent9] ON [Extent2].[medid] = [Extent9].[medid] "
						+ "WHERE [Extent1].[medid] = '"+parameters.get("medId")+"'"
						+ ")  AS [Distinct1]";




				String[] jsonMetaData = {"medicationId","gcnSequenceNumber","brandId","etcId","genericMedicationId","genericName","hic3","hiclSequenceNumber",
						"isGeneric","isHidden","medicationDosageFormDescription","medicationDosageFormId","medicationIdDescription",
						"medicationName","medicationRefDeaClassCode","medicationRouteDescription","medicationRouteId","medicationStrength",
						"medicationStrengthNumeric","medicationStrengthUom","ndcId"};

				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..medicationId", jsonList);
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
