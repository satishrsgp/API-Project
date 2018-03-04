package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetPatienMedSumCollective 
{
	public static Boolean chart_GetPatienMedSumCollective_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				
				String strQuery = "Select "
						+ "[Project3].[uniq_id] AS id, "
						+ "CASE WHEN ((N'Y' = [Project3].[selected_generic_ind]) AND ([Project3].[generic_name] IS NOT NULL) AND ( NOT (((LTRIM(RTRIM([Project3].[generic_name]))) = '') AND ((CASE WHEN (LTRIM(RTRIM([Project3].[generic_name])) IS NULL) THEN cast(1 as bit) ELSE cast(0 as bit) END) = (CASE WHEN ('' IS NULL) THEN cast(1 as bit) ELSE cast(0 as bit) END))))) THEN LTRIM(RTRIM([Project3].[generic_name])) WHEN (N'Y' = [Project3].[selected_generic_ind]) THEN [Project3].[brand_name] WHEN ((N'Y' <> [Project3].[selected_generic_ind]) AND ([Project3].[brand_name] IS NOT NULL) AND ( NOT (((LTRIM(RTRIM([Project3].[brand_name]))) = '') AND ((CASE WHEN (LTRIM(RTRIM([Project3].[brand_name])) IS NULL) THEN cast(1 as bit) ELSE cast(0 as bit) END) = (CASE WHEN ('' IS NULL) THEN cast(1 as bit) ELSE cast(0 as bit) END))))) THEN LTRIM(RTRIM([Project3].[brand_name])) ELSE [Project3].[generic_name] END AS fdbName,  "
						+ "CASE WHEN ( (N'Y' = [Project3].[reprndc_ind]) AND ([Project3].[tm_alt_med_name_desc] IS NOT NULL) AND ( NOT (((LTRIM(RTRIM([Project3].[tm_alt_med_name_desc]))) = '') AND ((CASE WHEN (LTRIM(RTRIM([Project3].[tm_alt_med_name_desc])) IS NULL) THEN cast(1 as bit) ELSE cast(0 as bit) END) = (CASE WHEN ('' IS NULL) THEN cast(1 as bit) ELSE cast(0 as bit) END))))) THEN LTRIM(RTRIM([Project3].[tm_alt_med_name_desc])) WHEN ((N'Y' <> [Project3].[reprndc_ind]) AND ([Project3].[tm_alt_medid_desc] IS NOT NULL) AND ( NOT (((LTRIM(RTRIM([Project3].[tm_alt_medid_desc]))) = '') AND ((CASE WHEN (LTRIM(RTRIM([Project3].[tm_alt_medid_desc])) IS NULL) THEN cast(1 as bit) ELSE cast(0 as bit) END) = (CASE WHEN ('' IS NULL) THEN cast(1 as bit) ELSE cast(0 as bit) END))))) THEN LTRIM(RTRIM([Project3].[tm_alt_medid_desc])) ELSE [Project3].[medication_name] END AS medicationName,  "
						+ "[Project3].[generic_name] AS genericName,  "
						+ "[Project3].[brand_name] AS brandName, "
						+ "[Project3].[dose] AS dose, "
						+ "[Project3].[dose_form_desc] AS doseForm, "
						+ "[Project3].[ndc_id] AS ndcId, "
						+ "[Project3].[gcn] AS gcn, "
						+ "[Project3].[route_desc] AS route, "
						+ "[Project3].[hicl_seqno] AS hiclSequenceNumber, "
						+ "[Project3].[gcn_seqno] AS gcnSequenceNumber, "
						+ "[Project3].[dea_id] AS deaId, "
						+ "[Project3].[medid] AS medicationId, "
						+ "Case when [Project3].[available_ind] = 'N' then 'false' else 'true' end AS isAvailable, "
						+ "[Project3].[created_by] AS createdBy, "
						+ "[Project3].[create_timestamp] AS createTimestamp, "
						+ "[Project3].[create_timestamp_tz] AS createTimestampTimezone, "
						+ "[Project3].[modified_by] AS modifiedBy, "
						+ "[Project3].[modify_timestamp] AS modifyTimestamp, "
						+ "[Project3].[modify_timestamp_tz] AS modifyTimestampTimezone, "
						+ "[Project3].[person_id] AS personId, "
						+ "[Project3].[enc_id] AS encounterId, "
						+ "[Project3].[rendering_provider_id] AS encounterProviderId, "
						+ "[Project3].[enc_timestamp] AS encounterTimestamp, "
						+ "[Project3].[enc_timestamp_tz] AS encounterTimestampTimezone, "
						+ "[Project3].[provider_id] AS providerId, "
						+ "[Project3].[location_id] AS locationId, "
						+ "[Project3].[practice_id] AS practiceId, "
						+ "[Project3].[practice_name] AS practiceName, "
						+ "[Project3].[enterprise_id] AS enterpriseId, "
						+ "[Project3].[enterprise_name] AS enterpriseName, "
						+ "[Project3].[start_date] AS startDate, "
						+ "[Project3].[start_date1] AS originalStartDate, "
						+ "[Project3].[date_stopped] AS stopDate, "
						+ "[Project3].[sig_codes] AS sigCodes, "
						+ "[Project3].[rx_quanity] AS rxQuantity, "
						+ "CASE WHEN (([Project3].[rx_refills] IS NULL) OR ((LTRIM(RTRIM([Project3].[rx_refills]))) = '') OR ((LTRIM(RTRIM([Project3].[rx_refills])) IS NULL) AND ('' IS NULL))) THEN N'0' ELSE [Project3].[rx_refills] END AS rxRefills, "
						+ "case when [Project3].[generic_ok_ind] = 'N' then 'false' else 'true' end AS isGenericAllowed, "
						+ "Case when [Project3].[samples_ind] = 'N' then 'false' else 'true' end  AS isSample, "
						+ "case when [Project3].[dispense_as_writtn_ind] = 'N' then 'false' else 'true' end AS dispenseAsWritten, "
						+ "case when [Project3].[org_refills] = '' then 0 else [Project3].[org_refills] end  AS orgRefills, "
						+ "case when [Project3].[date_last_refilled] = '' then null else [Project3].[date_last_refilled] end  AS lastRefillDate, "
						+ "[Project3].[sig_desc] AS sigDescription, "
						+ "Case when [Project3].[prescribed_else_ind] = 'N' then 'false' else 'true' end AS isPrescribedElsewhere, "
						+ "[Project3].[rx_units] AS rxUnits, "
						+ "[Project3].[rx_comment] AS rxComment, "
						+ "[Project3].[rx_special_instruction] AS rxSpecialInstruction, "
						+ "case when [Project3].[print_spanish_ind] = 'Y' then 'true' else 'false' end AS printSpanishIndicator, "
						+ "case when [Project3].[selected_generic_ind] = 'Y' then 'true' else 'false' end AS isGenericSelected, "
						+ "[Project3].[diagnosis_code_id] AS diagnosisCode, "
						+ "[Project3].[diagnosis_code_id2] AS secondaryDiagnosisCode, "
						+ "[Project3].[diagnosis_code_id3] AS tertiaryDiagnosisCode, "
						+ "case when [Project3].[reprndc_ind] = 'Y' then 'true' else 'false' end  AS representativeNdcIndicator, "
						+ "[Project3].[prn_reason] AS prnReason, "
						+ "[Project3].[formulary_id] AS formularyId, "
						+ "[Project3].[Formu_Override_code] AS formulaOverrideCode, "
						+ "[Project3].[Formu_Override_text] AS formulaOverrideText, "
						+ "[Project3].[original_rx_ndc] AS originalRxNdc, "
						+ "[Project3].[formu_status] AS formulaStatus, "
						+ "[Project3].[dur_audit_mask] AS auditMask, "
						+ "[Project3].[dur_audit_id] AS auditId, "
						+ "[Project3].[send_audit_id] AS sendAuditId, "
						+ "[Project3].[last_audit_type] AS lastAuditType, "
						+ "[Project3].[lot_number] AS lotNumber, "
						+ "Case when [Project3].[expiration_date] = '' then null else [Project3].[expiration_date] end AS expirationDate, "
						+ "Case when [Project3].[prn_ind] = 'Y' then 'true' else 'false' end AS isPrn, "
						+ "[Project3].[renew_parent_id] AS renewParentId, "
						+ "[Project3].[ng_med_rx_unit_code] AS rxUnitCode, "
						+ "[Project3].[old_uniq_id] AS oldId, "
						+ "[Project3].[rx_renew_note] AS rxRenewNote, "
						+ "[Project3].[poeostrid] AS fdbDosageId, "
						+ "[Project3].[refill_limit] AS refillLimit, "
						+ "Case when [Project3].[refill_limit_date]= '' then null else [Project3].[refill_limit_date] end AS refillLimitDate, "
						+ "[Project3].[prescribed_else_location] AS prescribedElsewhereLocation, "
						+ "Case when [Project3].[prior_auth_ind] = 'Y' then 'true' else 'false' end  AS priorAuthorizationIndicator, "
						+ "[Project3].[prior_auth_id] AS priorAuthorizationId, "
						+ "case when [Project3].[prior_auth_date] = '' then null else [Project3].[prior_auth_date] end AS priorAuthorizationDate, "
						+ "case when [Project3].[privacy_ind] = 'Y' then 'true' else 'false' end AS privacyIndicator, "
						+ "[Project3].[refills_left] AS refillsRemaining, "
						+ "[Project3].[custom_dosage_order_id] AS customDosageId, "
						+ "case when [Project3].[auto_calc_ind] = 'Y' then 'true' else 'false' end AS isAutoCalculated, "
						+ "[Project3].[pbm_id] AS pbmId, "
						+ "[Project3].[formu_data] AS formularyDetailData, "
						+ "[Project3].[formu_summ_data] AS formularySummaryData, "
						+ "[Project3].[source_product_id] AS sourceProductId,  "
						+ "[Project3].[supervising_provider_id] AS supervisingProviderId,  "
						+ "Case when [Project3].[ineffective_med_ind] = 'Y' then 'true' else 'false' end AS ineffectiveMedicationIndicator, "
						+ "[Project3].[ped_order_id] AS pedOrderId, "
						+ "[Project3].[e_coupon_notes] AS eCouponNote, "
						+ "[Project3].[e_coupon_url] AS eCouponUrl, "
						+ "CAST( [Project3].[create_timestamp1] AS datetime2) AS auditTimestamp, "
						+ "case when [Project3].[disable_erx_ind] = 'Y' then 'true' else 'false' end AS isErxDisabledByState, "
						+ "case when [Project3].[disable_fax_ind] = 'Y' then 'true' else 'false' end AS isFaxDisabledByState, "
						+ "case when (CASE WHEN ([Project3].[medid] IS NULL) THEN CAST(NULL AS varchar(1)) WHEN ([Project3].[medid] < cast(0 as decimal(18))) THEN [Project3].[delete_ind] ELSE [Project3].[hidden_flag] END) = 'Y' then 'true' else 'false' end AS isHidden, "
						+ "case when (CASE WHEN (N'0' = [Project3].[med_type_ind]) THEN cast(1 as bit) WHEN ( NOT ((N'0' = [Project3].[med_type_ind]) AND ([Project3].[med_type_ind] IS NOT NULL))) THEN cast(0 as bit) END) = '1' then 'true' else 'false' end  AS isSupply, "
						+ "CASE WHEN (( NOT (([Project3].[evd_fdb_vocab_id] IS NULL) AND ([Project3].[evd_fdb_vocab_type_id] IS NULL) AND ([Project3].[evd_ext_vocab_id] IS NULL) AND ([Project3].[evd_ext_vocab_type_id] IS NULL))) AND ([Project3].[evd_ext_vocab_id] IS NOT NULL)) THEN [Project3].[evd_ext_vocab_id] WHEN (( NOT (([Limit2].[evd_fdb_vocab_id] IS NULL) AND ([Limit2].[evd_fdb_vocab_type_id] IS NULL) AND ([Limit2].[evd_ext_vocab_id] IS NULL) AND ([Limit2].[evd_ext_vocab_type_id] IS NULL))) AND ([Limit2].[evd_ext_vocab_id] IS NOT NULL)) THEN [Limit2].[evd_ext_vocab_id] END AS rxNormCode,"
						+ "case  when [Project3].[date_stopped] = '' then  'Active' when (cast([Project3].[start_date] as datetime) > CONVERT (date, SYSDATETIME())) then 'Pending' when (cast([Project3].[date_stopped] as datetime) = CONVERT (date, SYSDATETIME())) then 'Inactive' when cast([Project3].[date_stopped] as datetime) < CONVERT (date, SYSDATETIME())  then 'Inactive'  end as status "
						+ "FROM "
						+ "( SELECT "
						+ "[Project1].[hicl_seqno] AS [hicl_seqno], "
						+ "[Project1].[brand_name] AS [brand_name], "
						+ "[Project1].[generic_name] AS [generic_name], "
						+ "[Project1].[dose] AS [dose], "
						+ "[Project1].[available_ind] AS [available_ind], "
						+ "[Project1].[route_desc] AS [route_desc], "
						+ "[Project1].[dose_form_desc] AS [dose_form_desc], "
						+ "[Project1].[gcn] AS [gcn], "
						+ "[Project1].[dea_id] AS [dea_id], "
						+ "[Project1].[delete_ind] AS [delete_ind], "
						+ "[Project1].[uniq_id] AS [uniq_id], "
						+ "[Project1].[created_by] AS [created_by], "
						+ "[Project1].[create_timestamp] AS [create_timestamp], "
						+ "[Project1].[create_timestamp_tz] AS [create_timestamp_tz], "
						+ "[Project1].[modified_by] AS [modified_by], "
						+ "[Project1].[modify_timestamp] AS [modify_timestamp], "
						+ "[Project1].[modify_timestamp_tz] AS [modify_timestamp_tz], "
						+ "[Project1].[person_id] AS [person_id], "
						+ "[Project1].[enc_id] AS [enc_id], "
						+ "[Project1].[enterprise_id] AS [enterprise_id], "
						+ "[Project1].[practice_id] AS [practice_id], "
						+ "[Project1].[location_id] AS [location_id], "
						+ "[Project1].[provider_id] AS [provider_id], "
						+ "[Project1].[medication_name] AS [medication_name], "
						+ "[Project1].[ndc_id] AS [ndc_id], "
						+ "[Project1].[gcn_seqno] AS [gcn_seqno], "
						+ "[Project1].[start_date] AS [start_date], "
						+ "[Project1].[date_stopped] AS [date_stopped], "
						+ "[Project1].[sig_codes] AS [sig_codes], "
						+ "[Project1].[rx_quanity] AS [rx_quanity], "
						+ "[Project1].[rx_refills] AS [rx_refills], "
						+ "[Project1].[generic_ok_ind] AS [generic_ok_ind], "
						+ "[Project1].[samples_ind] AS [samples_ind], "
						+ "[Project1].[dispense_as_writtn_ind] AS [dispense_as_writtn_ind], "
						+ "[Project1].[org_refills] AS [org_refills], "
						+ "[Project1].[date_last_refilled] AS [date_last_refilled], "
						+ "[Project1].[sig_desc] AS [sig_desc], "
						+ "[Project1].[prescribed_else_ind] AS [prescribed_else_ind], "
						+ "[Project1].[rx_units] AS [rx_units], "
						+ "[Project1].[rx_comment] AS [rx_comment], "
						+ "[Project1].[rx_special_instruction] AS [rx_special_instruction], "
						+ "[Project1].[print_spanish_ind] AS [print_spanish_ind], "
						+ "[Project1].[selected_generic_ind] AS [selected_generic_ind], "
						+ "[Project1].[diagnosis_code_id] AS [diagnosis_code_id], "
						+ "[Project1].[diagnosis_code_id2] AS [diagnosis_code_id2], "
						+ "[Project1].[diagnosis_code_id3] AS [diagnosis_code_id3], "
						+ "[Project1].[reprndc_ind] AS [reprndc_ind], "
						+ "[Project1].[prn_ind] AS [prn_ind], "
						+ "[Project1].[prn_reason] AS [prn_reason], "
						+ "[Project1].[formulary_id] AS [formulary_id], "
						+ "[Project1].[Formu_Override_code] AS [Formu_Override_code], "
						+ "[Project1].[Formu_Override_text] AS [Formu_Override_text], "
						+ "[Project1].[original_rx_ndc] AS [original_rx_ndc], "
						+ "[Project1].[formu_status] AS [formu_status], "
						+ "[Project1].[dur_audit_mask] AS [dur_audit_mask], "
						+ "[Project1].[dur_audit_id] AS [dur_audit_id], "
						+ "[Project1].[send_audit_id] AS [send_audit_id], "
						+ "[Project1].[last_audit_type] AS [last_audit_type], "
						+ "[Project1].[lot_number] AS [lot_number], "
						+ "[Project1].[expiration_date] AS [expiration_date], "
						+ "[Project1].[renew_parent_id] AS [renew_parent_id], "
						+ "[Project1].[ng_med_rx_unit_code] AS [ng_med_rx_unit_code], "
						+ "[Project1].[old_uniq_id] AS [old_uniq_id], "
						+ "[Project1].[rx_renew_note] AS [rx_renew_note], "
						+ "[Project1].[poeostrid] AS [poeostrid], "
						+ "[Project1].[refill_limit] AS [refill_limit], "
						+ "[Project1].[refill_limit_date] AS [refill_limit_date], "
						+ "[Project1].[prescribed_else_location] AS [prescribed_else_location], "
						+ "[Project1].[prior_auth_ind] AS [prior_auth_ind], "
						+ "[Project1].[prior_auth_id] AS [prior_auth_id], "
						+ "[Project1].[prior_auth_date] AS [prior_auth_date], "
						+ "[Project1].[privacy_ind] AS [privacy_ind], "
						+ "[Project1].[refills_left] AS [refills_left], "
						+ "[Project1].[custom_dosage_order_id] AS [custom_dosage_order_id], "
						+ "[Project1].[auto_calc_ind] AS [auto_calc_ind], "
						+ "[Project1].[pbm_id] AS [pbm_id], "
						+ "[Project1].[formu_data] AS [formu_data], "
						+ "[Project1].[formu_summ_data] AS [formu_summ_data], "
						+ "[Project1].[source_product_id] AS [source_product_id], "
						+ "[Project1].[medid] AS [medid], "
						+ "[Project1].[supervising_provider_id] AS [supervising_provider_id], "
						+ "[Project1].[ineffective_med_ind] AS [ineffective_med_ind], "
						+ "[Project1].[ped_order_id] AS [ped_order_id], "
						+ "[Project1].[e_coupon_notes] AS [e_coupon_notes], "
						+ "[Project1].[e_coupon_url] AS [e_coupon_url], "
						+ "[Project1].[enc_timestamp] AS [enc_timestamp], "
						+ "[Project1].[enc_timestamp_tz] AS [enc_timestamp_tz], "
						+ "[Project1].[rendering_provider_id] AS [rendering_provider_id], "
						+ "[Project1].[enterprise_name] AS [enterprise_name], "
						+ "[Project1].[practice_name] AS [practice_name], "
						+ "[Limit1].[evd_fdb_vocab_id] AS [evd_fdb_vocab_id], "
						+ "[Limit1].[evd_fdb_vocab_type_id] AS [evd_fdb_vocab_type_id], "
						+ "[Limit1].[evd_ext_vocab_id] AS [evd_ext_vocab_id], "
						+ "[Limit1].[evd_ext_vocab_type_id] AS [evd_ext_vocab_type_id], "
						+ "[Project1].[start_date1] AS [start_date1], "
						+ "[Project1].[create_timestamp1] AS [create_timestamp1], "
						+ "[Project1].[state_dea_id] AS [state_dea_id], "
						+ "[Project1].[disable_erx_ind] AS [disable_erx_ind], "
						+ "[Project1].[disable_fax_ind] AS [disable_fax_ind], "
						+ "[Project1].[tm_alt_med_name_desc] AS [tm_alt_med_name_desc], "
						+ "[Project1].[tm_alt_medid_desc] AS [tm_alt_medid_desc], "
						+ "[Project1].[hidden_flag] AS [hidden_flag], "
						+ "[Project1].[med_type_ind] AS [med_type_ind] "
						+ "FROM "
						+ "(SELECT "
						+ "[Extent1].[hicl_seqno] AS [hicl_seqno], "
						+ "[Extent1].[brand_name] AS [brand_name], "
						+ "[Extent1].[generic_name] AS [generic_name], "
						+ "[Extent1].[dose] AS [dose], "
						+ "[Extent1].[available_ind] AS [available_ind], "
						+ "[Extent1].[route_desc] AS [route_desc],  "
						+ "[Extent1].[dose_form_desc] AS [dose_form_desc], "
						+ "[Extent1].[gcn] AS [gcn],  "
						+ "[Extent1].[dea_id] AS [dea_id],  "
						+ "[Extent1].[delete_ind] AS [delete_ind],  "
						+ "[Extent2].[uniq_id] AS [uniq_id],  "
						+ "[Extent2].[created_by] AS [created_by],  "
						+ "[Extent2].[create_timestamp] AS [create_timestamp],  "
						+ "[Extent2].[create_timestamp_tz] AS [create_timestamp_tz],  "
						+ "[Extent2].[modified_by] AS [modified_by],  "
						+ "[Extent2].[modify_timestamp] AS [modify_timestamp],  "
						+ "[Extent2].[modify_timestamp_tz] AS [modify_timestamp_tz],  "
						+ "[Extent2].[person_id] AS [person_id], "
						+ "[Extent2].[enc_id] AS [enc_id], "
						+ "[Extent2].[enterprise_id] AS [enterprise_id], "
						+ "[Extent2].[practice_id] AS [practice_id], "
						+ "[Extent2].[location_id] AS [location_id], "
						+ "[Extent2].[provider_id] AS [provider_id], "
						+ "[Extent2].[medication_name] AS [medication_name], "
						+ "[Extent2].[ndc_id] AS [ndc_id], "
						+ "[Extent2].[gcn_seqno] AS [gcn_seqno], "
						+ "[Extent2].[start_date] AS [start_date], "
						+ "[Extent2].[date_stopped] AS [date_stopped], "
						+ "[Extent2].[sig_codes] AS [sig_codes], "
						+ "[Extent2].[rx_quanity] AS [rx_quanity], "
						+ "[Extent2].[rx_refills] AS [rx_refills], "
						+ "[Extent2].[generic_ok_ind] AS [generic_ok_ind], "
						+ "[Extent2].[samples_ind] AS [samples_ind], "
						+ "[Extent2].[dispense_as_writtn_ind] AS [dispense_as_writtn_ind], "
						+ "[Extent2].[org_refills] AS [org_refills], "
						+ "[Extent2].[date_last_refilled] AS [date_last_refilled], "
						+ "[Extent2].[sig_desc] AS [sig_desc], "
						+ "[Extent2].[prescribed_else_ind] AS [prescribed_else_ind], "
						+ "[Extent2].[rx_units] AS [rx_units], "
						+ "[Extent2].[rx_comment] AS [rx_comment], "
						+ "[Extent2].[rx_special_instruction] AS [rx_special_instruction], "
						+ "[Extent2].[print_spanish_ind] AS [print_spanish_ind], "
						+ "[Extent2].[selected_generic_ind] AS [selected_generic_ind],  "
						+ "[Extent2].[diagnosis_code_id] AS [diagnosis_code_id],  "
						+ "[Extent2].[diagnosis_code_id2] AS [diagnosis_code_id2], "
						+ "[Extent2].[diagnosis_code_id3] AS [diagnosis_code_id3], "
						+ "[Extent2].[reprndc_ind] AS [reprndc_ind], "
						+ "[Extent2].[prn_ind] AS [prn_ind], "
						+ "[Extent2].[prn_reason] AS [prn_reason], "
						+ "[Extent2].[formulary_id] AS [formulary_id], "
						+ "[Extent2].[Formu_Override_code] AS [Formu_Override_code], "
						+ "[Extent2].[Formu_Override_text] AS [Formu_Override_text], "
						+ "[Extent2].[original_rx_ndc] AS [original_rx_ndc], "
						+ "[Extent2].[formu_status] AS [formu_status], "
						+ "[Extent2].[dur_audit_mask] AS [dur_audit_mask], "
						+ "[Extent2].[dur_audit_id] AS [dur_audit_id], "
						+ "[Extent2].[send_audit_id] AS [send_audit_id], "
						+ "[Extent2].[last_audit_type] AS [last_audit_type], "
						+ "[Extent2].[lot_number] AS [lot_number], "
						+ "[Extent2].[expiration_date] AS [expiration_date], "
						+ "[Extent2].[renew_parent_id] AS [renew_parent_id], "
						+ "[Extent2].[ng_med_rx_unit_code] AS [ng_med_rx_unit_code], "
						+ "[Extent2].[old_uniq_id] AS [old_uniq_id], "
						+ "[Extent2].[rx_renew_note] AS [rx_renew_note], "
						+ "[Extent2].[poeostrid] AS [poeostrid], "
						+ "[Extent2].[refill_limit] AS [refill_limit], "
						+ "[Extent2].[refill_limit_date] AS [refill_limit_date], "
						+ "[Extent2].[prescribed_else_location] AS [prescribed_else_location], "
						+ "[Extent2].[prior_auth_ind] AS [prior_auth_ind], "
						+ "[Extent2].[prior_auth_id] AS [prior_auth_id], "
						+ "[Extent2].[prior_auth_date] AS [prior_auth_date], "
						+ "[Extent2].[privacy_ind] AS [privacy_ind], "
						+ "[Extent2].[refills_left] AS [refills_left], "
						+ "[Extent2].[custom_dosage_order_id] AS [custom_dosage_order_id], "
						+ "[Extent2].[auto_calc_ind] AS [auto_calc_ind], "
						+ "[Extent2].[pbm_id] AS [pbm_id], "
						+ "[Extent2].[formu_data] AS [formu_data], "
						+ "[Extent2].[formu_summ_data] AS [formu_summ_data], "
						+ "[Extent2].[source_product_id] AS [source_product_id], "
						+ "[Extent2].[medid] AS [medid], "
						+ "[Extent2].[supervising_provider_id] AS [supervising_provider_id], "
						+ "[Extent2].[ineffective_med_ind] AS [ineffective_med_ind], "
						+ "[Extent2].[ped_order_id] AS [ped_order_id], "
						+ "[Extent2].[e_coupon_notes] AS [e_coupon_notes], "
						+ "[Extent2].[e_coupon_url] AS [e_coupon_url], "
						+ "[Extent3].[enc_timestamp] AS [enc_timestamp], "
						+ "[Extent3].[enc_timestamp_tz] AS [enc_timestamp_tz], "
						+ "[Extent3].[rendering_provider_id] AS [rendering_provider_id], "
						+ "[Extent4].[enterprise_name] AS [enterprise_name], "
						+ "[Extent5].[practice_name] AS [practice_name], "
						+ "[Extent6].[start_date] AS [start_date1], "
						+ "[Extent7].[create_timestamp] AS [create_timestamp1], "
						+ "[Extent8].[state_dea_id] AS [state_dea_id], "
						+ "[Extent8].[disable_erx_ind] AS [disable_erx_ind], "
						+ "[Extent8].[disable_fax_ind] AS [disable_fax_ind], "
						+ "[Extent9].[tm_alt_med_name_desc] AS [tm_alt_med_name_desc], "
						+ "[Extent9].[tm_alt_medid_desc] AS [tm_alt_medid_desc], "
						+ "[Extent10].[hidden_flag] AS [hidden_flag],  "
						+ "[Extent12].[med_type_ind] AS [med_type_ind] "
						+ "FROM "
						+ "[dbo].[fdb_medication] AS [Extent1] "
						+ "INNER JOIN [dbo].[patient_medication] AS [Extent2] ON [Extent1].[ndc_id] = [Extent2].[ndc_id] "
						+ "INNER JOIN [dbo].[patient_encounter] AS [Extent3] ON [Extent2].[enc_id] = [Extent3].[enc_id] "
						+ "INNER JOIN [dbo].[enterprise] AS [Extent4] ON [Extent2].[enterprise_id] = [Extent4].[enterprise_id] "
						+ "INNER JOIN [dbo].[practice] AS [Extent5] ON [Extent2].[practice_id] = [Extent5].[practice_id] "
						+ "LEFT OUTER JOIN [dbo].[patient_medication] AS [Extent6] ON ([Extent2].[renew_parent_id] = [Extent6].[uniq_id]) AND (1 = (CASE WHEN ([Extent6].[renew_parent_id] = [Extent6].[uniq_id]) THEN cast(1 as bit) WHEN ( NOT (([Extent6].[renew_parent_id] = [Extent6].[uniq_id]) AND ((CASE WHEN ([Extent6].[renew_parent_id] IS NULL) THEN cast(1 as bit) ELSE cast(0 as bit) END) = 0))) THEN cast(0 as bit) END)) "
						+ "LEFT OUTER JOIN [dbo].[prescription_audit] AS [Extent7] ON [Extent2].[send_audit_id] = [Extent7].[uniq_id] "
						+ "LEFT OUTER JOIN [dbo].[med_state_dea_reg_mstr] AS [Extent8] ON [Extent2].[medid] = [Extent8].[med_id] "
						+ "LEFT OUTER JOIN [dbo].[fdb_tall_man_name_mstr] AS [Extent9] ON [Extent2].[medid] = [Extent9].[medid] "
						+ "LEFT OUTER JOIN [dbo].[medication_hidden] AS [Extent10] ON ([Extent2].[medid] = [Extent10].[medid]) AND ('00001' = [Extent10].[enterprise_id]) AND (N'-1' = [Extent10].[practice_id]) AND (-1 = [Extent10].[user_id]) "
						+ "LEFT OUTER JOIN [dbo].[ng_med_tables_mstr] AS [Extent11] ON [Extent2].[medid] = [Extent11].[medid] "
						+ "LEFT OUTER JOIN [dbo].[formu_med_mstr] AS [Extent12] ON [Extent11].[routed_med_id] = [Extent12].[routed_med_id] ) AS [Project1] "
						+ "OUTER APPLY  (SELECT TOP (1) [Project2].[evd_fdb_vocab_id] AS [evd_fdb_vocab_id], [Project2].[evd_fdb_vocab_type_id] AS [evd_fdb_vocab_type_id], [Project2].[evd_ext_vocab_id] AS [evd_ext_vocab_id], [Project2].[evd_ext_vocab_type_id] AS [evd_ext_vocab_type_id] "
						+ "FROM ( SELECT  "
						+ "CASE WHEN (cast(504 as decimal(18)) = [Extent13].[evd_ext_vocab_type_id]) THEN 0 WHEN (cast(502 as decimal(18)) = [Extent13].[evd_ext_vocab_type_id]) THEN 1 WHEN (cast(503 as decimal(18)) = [Extent13].[evd_ext_vocab_type_id]) THEN 2 WHEN (cast(501 as decimal(18)) = [Extent13].[evd_ext_vocab_type_id]) THEN 3 ELSE 4 END AS [C1],  "
						+ "[Extent13].[evd_fdb_vocab_id] AS [evd_fdb_vocab_id],  "
						+ "[Extent13].[evd_fdb_vocab_type_id] AS [evd_fdb_vocab_type_id], "
						+ "[Extent13].[evd_ext_vocab_id] AS [evd_ext_vocab_id], "
						+ "[Extent13].[evd_ext_vocab_type_id] AS [evd_ext_vocab_type_id] "
						+ "FROM [dbo].[revdel0] AS [Extent13] "
						+ "WHERE ([Extent13].[evd_ext_vocab_type_id] IN (cast(504 as decimal(18)), cast(502 as decimal(18)), cast(503 as decimal(18)), cast(501 as decimal(18)), cast(505 as decimal(18)))) AND (cast(100 as decimal(18)) = [Extent13].[evd_fdb_vocab_type_id]) AND (cast(4 as decimal(18)) = [Extent13].[evd_link_type_id]) AND ([Extent13].[evd_fdb_vocab_id] = [Project1].[ndc_id]) "
						+ ")  AS [Project2] "
						+ "ORDER BY [Project2].[C1] ASC ) AS [Limit1] ) AS [Project3] "
						+ "OUTER APPLY  (SELECT TOP (1) [Extent14].[evd_fdb_vocab_id] AS [evd_fdb_vocab_id], [Extent14].[evd_fdb_vocab_type_id] AS [evd_fdb_vocab_type_id], [Extent14].[evd_ext_vocab_id] AS [evd_ext_vocab_id], [Extent14].[evd_ext_vocab_type_id] AS [evd_ext_vocab_type_id] "
						+ "FROM [dbo].[revdel0] AS [Extent14] "
						+ "WHERE (cast(3 as decimal(18)) = [Extent14].[evd_fdb_vocab_type_id]) AND (cast(5 as decimal(18)) = [Extent14].[evd_link_type_id]) AND ([Project3].[medid] IS NOT NULL) AND ([Extent14].[evd_fdb_vocab_id] = (CASE WHEN ([Project3].[medid] IS NULL) THEN N'' ELSE  CAST( [Project3].[medid] AS nvarchar(max)) END)) ) AS [Limit2] "
						+ "WHERE ([Project3].[person_id] = '"+parameters.get("personId")+"') AND (([Project3].[enc_id] = '"+parameters.get("encounterId")+"') OR (([Project3].[enc_id] IS NULL) "
								+ "AND ('"+parameters.get("encounterId")+"' IS NULL))) AND ([Project3].[enterprise_id] = '" + System.getProperty("LoggedInEnterpriseId") + "') AND ([Project3].[practice_id] = '" + System.getProperty("LoggedInPracticeId") + "') "
										 +    "AND ([Project3].[uniq_id] IN (cast('"+parameters.get("Med1")+"' as uniqueidentifier), cast('"+parameters.get("Med2")+"' as uniqueidentifier)))";
							//	+ 	 "AND ([Project3].[uniq_id] IN (cast('"+parameters.get("Med1")+"', '"+parameters.get("Med2")+"')))";
							//	+ "AND ([Project3].[uniq_id] IN (cast('"+parameters.get("medicationId")+"'  as uniqueidentifier))) ";
						
						

						
				
				String[] jsonMetaData = {"id;GUID","fdbName;Normal","medicationName;Normal","genericName;Normal","brandName;Normal","dose;Normal","doseForm;Normal","ndcId;Normal",
						"gcn;Normal","route;Normal","hiclSequenceNumber;Normal","gcnSequenceNumber;Normal","deaId;Normal","medicationId;Normal","isAvailable;Boolean","createdBy;Normal",
						"createTimestamp;Date_YYYYMMDD HH:MM:SS","createTimestampTimezone;Normal","modifiedBy;Normal","modifyTimestamp;Date_YYYYMMDD HH:MM:SS","modifyTimestampTimezone;Normal",
						"personId;GUID","encounterId;GUID","encounterProviderId;GUID","encounterTimestamp;Date_YYYYMMDD HH:MM:SS","encounterTimestampTimezone;Normal",
						"providerId;GUID","locationId;GUID","practiceId;Normal","practiceName;Normal","enterpriseId;Normal","enterpriseName;Normal","startDate;Date_YYYYMMDD",
						"originalStartDate;Date_YYYYMMDD","stopDate;Date_YYYYMMDD","sigCodes;Normal","rxQuantity;Normal","rxRefills;Normal","isGenericAllowed;Boolean","isSample;Boolean",
						"dispenseAsWritten;Boolean","orgRefills;Normal","lastRefillDate;Date_YYYYMMDD","sigDescription;Normal","isPrescribedElsewhere;Boolean","rxUnits;Normal",
						"rxComment;Normal","rxSpecialInstruction;Normal","printSpanishIndicator;Boolean","isGenericSelected;Boolean","diagnosisCode;Normal",
						"secondaryDiagnosisCode;Normal","tertiaryDiagnosisCode;Normal","representativeNdcIndicator;Boolean","prnReason;Normal","formularyId;Normal",
						"formulaOverrideCode;Normal","formulaOverrideText;Normal","originalRxNdc;Normal","formulaStatus;Normal","auditMask;Normal","auditId;GUID",
						"sendAuditId;GUID","lastAuditType","lotNumber;Normal","expirationDate;Date_YYYYMMDD HH:MM:SS","isPrn;Boolean","renewParentId;GUID","rxUnitCode;Normal",
						"oldId;Normal","rxRenewNote;Normal","fdbDosageId;Normal","refillLimit;Normal","refillLimitDate;Normal","prescribedElsewhereLocation;Normal",
						"priorAuthorizationIndicator;Boolean","priorAuthorizationId;Normal","priorAuthorizationDate;Normal","privacyIndicator;Boolean",
						"refillsRemaining;Normal","customDosageId;GUID","isAutoCalculated;Boolean","pbmId","formularyDetailData;Normal","formularySummaryData;Normal",
						"sourceProductId","supervisingProviderId;GUID","ineffectiveMedicationIndicator;Boolean","pedOrderId;Normal","eCouponNote;Normal",
						"eCouponUrl;Normal","auditTimestamp;Date_YYYYMMDD HH:MM:SS","isErxDisabledByState;Boolean","isFaxDisabledByState;Boolean","isHidden;Boolean","isSupply;Boolean","rxNormCode;Normal","status;Normal"};
				
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..id", jsonList);
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
