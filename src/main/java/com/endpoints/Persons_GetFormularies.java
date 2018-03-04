package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Persons_GetFormularies {

	public static Boolean persons_GetFormularies_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{

				String strQuery="SELECT top 25 [Project2].[C3] AS medicationId,[Project2].[routed_med_id] AS routedMedId, [Project2].[C2] AS medicationName, "
						+ " case when [Project2].[C10] = 1 "  //case when [Project2].[generic_ind] = 'N' then 'false' else 'true' end as isGeneric,
						+ "then 'true' else 'false' end as isSingleSource, case when [Project2].[C8] = 1 then 'true' else 'false' "
						+ "end as isOTC,  [Project2].[generic_name] AS genericName, [Project2].[generic_routed_med_id] AS genericRoutedMedId, [Project2].[hic3] AS hic3, "
						+ " [Project2].[routed_dosage_form_med_id] AS routedDosageFormMedId,"
						+ " [Project2].[gcn_seqno] AS gcnSequenceNumber, [Project2].[generic_medid] AS genericMedid, [Project2].[med_strength] "
						+ "AS medicationStrength, [Project2].[med_strength_uom] AS medicationStrengthUnitOfMeasure, [Project2].[med_route_abbrv] "
						+ "AS medicationRouteAbbreviation, [Project2].[med_dosage_form_abbrv] AS medicationDosageFormAbbreviation, [Project2].[C5] "
						+ "AS medicationDescription, [Project2].[med_ref_dea_class_code] AS deaClassCode , [Project2].[person_id] AS personId, "
						+ "[Project2].[pbm_id] AS pbmId, [Project2].[pbm_name] AS pbmName, [Project2].[plan_id] AS planId, [Project2].[plan_name] "
						+ "AS planName, [Project2].[formulary_id] AS formularyId, [Project2].[alt_formulary_id] AS altFormularyId, "
						+ "[Project2].[coverage_id] AS coverageId, [Project2].[copay_id] AS copayId, case when [Project2].[hidden_flag] = 'N' "
						+ "then 'true' else 'false' end as isHidden FROM "  //, [Project2].[formulary_status] AS formularyStatusCode
						+ "( SELECT [Distinct1].[routed_med_id] AS [routed_med_id], [Distinct1].[generic_name] AS [generic_name], "
						+ "[Distinct1].[avail_generic_ind] AS [avail_generic_ind], [Distinct1].[med_type_ind] AS [med_type_ind], "
						+ "[Distinct1].[hic3] AS [hic3], [Distinct1].[generic_ind] AS [generic_ind], [Distinct1].[generic_routed_med_id] "
						+ "AS [generic_routed_med_id], [Distinct1].[person_id] AS [person_id], [Distinct1].[pbm_id] AS [pbm_id], [Distinct1].[pbm_name] "
						+ "AS [pbm_name], [Distinct1].[plan_id] AS [plan_id], [Distinct1].[plan_name] AS [plan_name], [Distinct1].[formulary_id]"
						+ " AS [formulary_id], [Distinct1].[alt_formulary_id] AS [alt_formulary_id], [Distinct1].[coverage_id] AS [coverage_id], "
						+ "[Distinct1].[copay_id] AS [copay_id], [Distinct1].[routed_dosage_form_med_id] AS [routed_dosage_form_med_id], "
						+ "[Distinct1].[med_strength] AS [med_strength], [Distinct1].[med_strength_uom] AS [med_strength_uom], [Distinct1].[gcn_seqno] "
						+ "AS [gcn_seqno], [Distinct1].[generic_medid] AS [generic_medid], [Distinct1].[med_ref_generic_name_code] AS "
						+ "[med_ref_generic_name_code], [Distinct1].[med_ref_dea_class_code] AS [med_ref_dea_class_code], [Distinct1].[med_fed_legend] "
						+ "AS [med_fed_legend], [Distinct1].[med_dosage_form_abbrv] AS [med_dosage_form_abbrv], [Distinct1].[med_route_abbrv] AS "
						+ "[med_route_abbrv], [Distinct1].[med_name_type_code] AS [med_name_type_code], [Distinct1].[C1] AS [C1], [Distinct1].[C2] AS [C2], "
						+ "[Distinct1].[C3] AS [C3], [Distinct1].[C4] AS [C4], [Distinct1].[C5] AS [C5], [Distinct1].[C6] AS [C6], [Distinct1].[C7] AS [C7], "
						+ "[Distinct1].[C8] AS [C8], [Distinct1].[C9] AS [C9], [Distinct1].[C10] AS [C10], [Distinct1].[hidden_flag] AS [hidden_flag], "
						+ "[Distinct1].[formulary_status] AS [formulary_status], [Distinct1].[note] AS [note] FROM ( SELECT DISTINCT [Extent1].[routed_med_id] AS [routed_med_id], "
						+ "[Extent1].[generic_name] AS [generic_name], [Extent1].[avail_generic_ind] AS [avail_generic_ind], [Extent1].[med_type_ind] AS [med_type_ind], "
						+ "[Extent1].[hic3] AS [hic3], [Extent1].[generic_ind] AS [generic_ind], [Extent1].[generic_routed_med_id] AS [generic_routed_med_id], "
						+ "[Extent2].[person_id] AS [person_id], [Extent2].[pbm_id] AS [pbm_id], [Extent2].[pbm_name] AS [pbm_name], [Extent2].[plan_id] AS [plan_id], "
						+ "[Extent2].[plan_name] AS [plan_name], [Extent2].[formulary_id] AS [formulary_id], [Extent2].[alt_formulary_id] AS [alt_formulary_id], "
						+ "[Extent2].[coverage_id] AS [coverage_id], [Extent2].[copay_id] AS [copay_id], [Extent3].[routed_dosage_form_med_id] AS [routed_dosage_form_med_id], [Extent3]."
						+ "[med_strength] AS [med_strength], [Extent3].[med_strength_uom] AS [med_strength_uom], [Extent3].[gcn_seqno] AS [gcn_seqno], [Extent3].[generic_medid] AS [generic_medid], "
						+ "[Extent3].[med_ref_generic_name_code] AS [med_ref_generic_name_code], [Extent3].[med_ref_dea_class_code] AS [med_ref_dea_class_code],"
						+ " [Extent3].[med_fed_legend] AS [med_fed_legend], [Extent3].[med_dosage_form_abbrv] AS [med_dosage_form_abbrv], [Extent3].[med_route_abbrv] AS [med_route_abbrv], "
						+ "[Extent3].[med_name_type_code] AS [med_name_type_code], 1 AS [C1], CASE WHEN ( ([Extent4].[tm_alt_med_name_desc] IS NOT NULL) AND "
						+ "( NOT (((LTRIM(RTRIM([Extent4].[tm_alt_med_name_desc]))) = '') AND ((CASE WHEN (LTRIM(RTRIM([Extent4].[tm_alt_med_name_desc])) IS NULL) "
						+ "THEN cast(1 as bit) ELSE cast(0 as bit) END) = (CASE WHEN ('' IS NULL) THEN cast(1 as bit) ELSE cast(0 as bit) END))))) THEN "
						+ "LTRIM(RTRIM([Extent4].[tm_alt_med_name_desc])) ELSE [Extent1].[med_name] END AS [C2], CAST( [Extent3].[medid] AS int) AS [C3], "
						+ "CASE WHEN ([Extent3].[med_strength] IS NULL) THEN N'0' WHEN (1 = (ISNUMERIC([Extent3].[med_strength]))) THEN [Extent3].[med_strength] "
						+ "ELSE N'0' END AS [C4], CASE WHEN (([Extent4].[tm_alt_medid_desc] IS NOT NULL) AND ( NOT (((LTRIM(RTRIM([Extent4].[tm_alt_medid_desc]))) = '') "
						+ "AND ((CASE WHEN (LTRIM(RTRIM([Extent4].[tm_alt_medid_desc])) IS NULL) THEN cast(1 as bit) ELSE cast(0 as bit) END) = (CASE WHEN ('' IS NULL) "
						+ "THEN cast(1 as bit) ELSE cast(0 as bit) END))))) THEN LTRIM(RTRIM([Extent4].[tm_alt_medid_desc])) ELSE [Extent3].[medid_desc] END AS [C5], CASE "
						+ "WHEN ([Extent6].[formulary_status] IS NULL) THEN cast(1 as bit) ELSE cast(0 as bit) END AS [C6], CASE WHEN (N'2' = [Extent3].[med_name_type_code]) "
						+ "THEN cast(1 as bit) WHEN (N'2' <> [Extent3].[med_name_type_code]) THEN cast(0 as bit) END AS [C7], CASE WHEN (N'2' = [Extent3].[med_fed_legend]) "
						+ "THEN cast(1 as bit) WHEN (N'2' <> [Extent3].[med_fed_legend]) THEN cast(0 as bit) END AS [C8], CASE WHEN (N'0' = [Extent1].[med_type_ind]) THEN "
						+ "cast(1 as bit) WHEN (N'0' <> [Extent1].[med_type_ind]) THEN cast(0 as bit) END AS [C9], CASE WHEN (N'2' = [Extent1].[avail_generic_ind]) THEN cast(1 as bit) "
						+ "WHEN (N'2' <> [Extent1].[avail_generic_ind]) THEN cast(0 as bit) END AS [C10], [Extent5].[hidden_flag] AS [hidden_flag], [Extent6].[formulary_status] "
						+ "AS [formulary_status], [Extent6].[note] AS [note] FROM [dbo].[formu_med_mstr] AS [Extent1] CROSS JOIN [dbo].[patient_eligibility] AS [Extent2] "
						+ "INNER JOIN [dbo].[ng_med_tables_mstr] AS [Extent3] ON [Extent1].[routed_med_id] = [Extent3].[routed_med_id] LEFT OUTER JOIN [dbo].[fdb_tall_man_name_mstr] "
						+ "AS [Extent4] ON [Extent3].[medid] = [Extent4].[medid] LEFT OUTER JOIN [dbo].[medication_hidden] AS [Extent5] ON [Extent3].[medid] = [Extent5].[medid] "
						+ "LEFT OUTER JOIN [dbo].[ng_rxh_formulary_med_data] AS [Extent6] ON ([Extent3].[medid] = CAST( [Extent6].[medid] AS decimal(19,0))) AND ([Extent2].[formulary_id] = "
						+ "[Extent6].[formulary_id]) AND ([Extent2].[pbm_id] = [Extent6].[sourceid]) WHERE ((N'2' <> [Extent3].[med_ref_generic_name_code]) OR ( NOT ((N'2' = [Extent1].[generic_ind]) "
						+ "AND ([Extent1].[generic_ind] IS NOT NULL)))) AND ([Extent2].[person_id] = '"+parameters.get("personId")+"') ) AS [Distinct1] ) AS [Project2] "
						+ "ORDER BY [Project2].[C3] " ;
						 
				//-  "isGeneric", -[Project2].[generic_routed_med_id] asc , [Project2].[med_strength] asc,[Project2].[formulary_status] asc, [Project2].[pbm_id] asc ,+ "[Project2].[plan_id] asc,"  
				String[] jsonMetaData = {"medicationId","routedMedId","medicationName", "isSingleSource","isOTC","genericName","genericRoutedMedId","hic3","routedDosageFormMedId",
						"gcnSequenceNumber","genericMedid","medicationStrength","medicationStrengthUnitOfMeasure","medicationRouteAbbreviation","medicationDosageFormAbbreviation","medicationDescription","deaClassCode",
						"personId","pbmId","pbmName","planId","planName","formularyId","altFormularyId","coverageId","copayId","isHidden"};

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
