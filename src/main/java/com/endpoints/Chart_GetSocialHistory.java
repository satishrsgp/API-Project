package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetSocialHistory 
{
	public static Boolean chart_GetSocialHistory_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				
				String strQuery = "Select "
						+ "SH.create_timestamp as createTimestamp, SH.create_timestamp_tz as createTimestampTimezone, SH.created_by as createdBy, SH.modify_timestamp as modifyTimestamp, "
						+ "SH.modify_timestamp_tz as modifyTimestampTimezone, SH.modified_by as modifiedBy, SH.enterprise_id as enterpriseId, SH.practice_id as practiceId, "
						+ "SH.person_id as personId, SH.enc_id as encounterId, SH.language_at_home as homeLanguage, SH.birthplace as birthCountry, SH.opt_right_left_hnd as handDominance, " 
						+ "SH.education_level as educationLevel, SH.txt_bh_degree as educationDegree, SH.txt_bh_degree_country as educationDegreeCountry, SH.hazard1 as occupationalHazard1, "
						+ "SH.hazard2 as occupationalHazard2, "
						//+ "SH.opt_military_experience as hasMilitaryExperience, "
						+ "case when SH.opt_military_experience = '1' then 'true' else 'false'  end as hasMilitaryExperience, "
						+ "SH.txt_military_type as militaryType, SH.txt_military_branch as militaryBranch, "
						+ "SH.txt_military_years_served as militaryYearsServed, SH.txt_military_discharge_type as militaryDischargeType, SH.opt_military_stat_overseas as militaryStationedOverseas, "
						+ "SH.txt_military_overseas_where as militaryStationedOverseasLocation, SH.opt_military_noise_exp as militaryNoiseExposure, SH.opt_military_biohazard_exp as militaryBiohazardExposure, "
						+ "SH.previous_widowed as previousWidowed, SH.prev_widow_times as previousWidowedTimes, "
						+ "Case SH.previous_divorce "
						+ "when 'N' then 'false' "
						+ "when 'Y' then 'true' "
						+ "else SH.previous_divorce end as previousDivorce, "
						+ "SH.prev_divorce_times as previousDivorceTimes, SH.children as hasChildren, SH.number_sons as sonsCount, SH.number_daughter as daughtersCount, SH.txt_support_netwk_comments as supportNetworkComments, "
						+ "SH.txt_suuport_network as supportNetwork, SH.txt_primary_res_comments as primaryResidenceComments, " 
						+ "case SH.tobacco_yes  " 
						+ "when 'N' then 'No' "
						+ "when 'Y' then 'Yes' "
						+ "when 'R' then 'Formerly' "
						+ "else cast(ISNULL(SH.tobacco_yes,'NotSet') as varchar(15))  "
						+ "end  as  tobaccoUsage, "
						+ "SH.tobacco_type as tobaccoType, SH.pack_per_day as tobaccoUnitsPerDay, SH.txt_unit as tobaccoUnit, SH.Yrs_Smoked as tobaccoYearsSmoked, SH.packyears as tobaccoPackYears, "
						+ "SH.opt_current_past as tobaccoCurrentOrPast, SH.tobaccoQuit as tobaccoPreviousCessationAttempt, SH.tobacco_date_stop as tobaccoYearQuit, SH.txt_prd_tobac_free as tobaccoLongestFreePeriod, "
						+ "SH.txt_reason_relapse as tobaccoRelapseReason, SH.passiveSmoke as passiveSmokeExposure, SH.txt_concat as tobaccoCessationMethods, "
						+ "Case "
						+ "WHEN SH.alcohol_yes IS NULL THEN 'NotSet'"
						+ "WHEN SH.alcohol_yes = 'Y' THEN 'Yes' "
						+ "WHEN SH.alcohol_yes = 'N' THEN 'No' "
						+ "WHEN sh.alcohol_yes = 'R' THEN 'Formerly' "
						+ "end as alcoholUsage, "
						+ "SH.caffeine_yes as caffeineUsage, SH.caffeine_type as caffeineType, SH.caffeine_type2 as caffeineType2, SH.caffeine_per_day as caffeinePerDay, SH.activity_level as activityLevel, "
						+ "SH.helath_club_mbr as healthClubMember, SH.exercise_freq as exerciseFrequency, SH.exerciseHrsWk as exerciseHoursWeek, SH.exercise_type as exerciseType, SH.exerciseType2 as exercisetype2, "
						+ "SH.exerciseType3 as exercisetype3, SH.hobby1 as hobby1, SH.hobby2 as hobby2, SH.hobby3 as hobby3, SH.diet_hx1 as dietHistory1, SH.diet_hx2 as dietHistory2, SH.diet_hx3 as dietHistory3, "
						+ "SH.diet_hx4 as dietHistory4, SH.diet_hx5 as dietHistory5, SH.animals as petAnimals, SH.birds as petBirds, SH.dogs as petDogs, SH.rodents as petRodents, SH.reptiles as petReptiles, SH.other as petOther, "
						+ "Case SH.txt_pets_doc_txt  "
						+ "when '' then '' "
						+ "else SH.txt_pets_doc_txt  "
						+ "end  as pets, "
						+ "SH.animals_clean as cleansAnimals, SH.opt_sleep as changesInSleepPatterns, SH.opt_religious_affillation as religiousAffiliation, SH.opt_practice_religion as practiceReligion, SH.opt_spiritual_beliefs as spiritualBeliefs, "
						+ "SH.opt_importance_spirituality as importanceOfReligionSpirituality, SH.smoke_detectors as smokeDetectorsInHome, SH.co_detectors as carbonMonoxideDetectorsInHome, "
						+ "case SH.radon "
						+ "when 'T' then 'Treated' "
						+ "when 'U' then 'Untested' "
						+ "when 'N' then 'No' "
						+ "else ISNULL(SH.radon,'1')  "
						+ "end as radonInHome, "
						+ "SH.homeHeating as homeHeating, "
						+ "case "
						+ "WHEN SH.seat_belts IS NULL THEN 'NotSet'  "
						+ "when SH.seat_belts = 'N' then 'No' "
						+ "when SH.seat_belts = 'Y' then 'Yes' "
						+ "end  as seatBeltUsage, "
						+ "case SH.firearms_yes "
						+ "when 'N' then 'false' "
						+ "when 'Y' then 'true' "
						+ "else SH.firearms_yes "
						+ "end as fireArmsAtHome, SH.transfusion_yes as agreesToTransfusion, SH.travelState as travelOutOfState, SH.txt_travel_out_state as travelOutOfStateLocation, SH.travelCountry as travelOutOfCountry, "
						+ "SH.txt_travel_out_counrty as travelOutOfCounrtyLocation, SH.txt_travel_rgn as travelOutOfCountryRegion, "
						+ "case SH.chk_travelexposure "
						+ "when 0 then 'false' "
						+ "when 2 then 'true' "
						+ "when 1 then 'true' "
						+ "else str(SH.chk_travelexposure) "
						+ "end as travelExposure, "
						+ "SH.txt_travel_exposure as travelExposureDetail, "
						+ "SH.dt_directives_reviewed as advanceDirectivesReviewDate, SH.ADR_none as advanceDirectivesNone, SH.ADR_refused as advanceDirectivesRefused, SH.ADR_DNR as advanceDirectivesDnr, "
						+ "SH.ADR_livingwill as advanceDirectivesLivingWill, SH.ADR_life_support as advanceDirectivesDoNotPlaceOnLifeSupport, SH.ADR_Attorney as advanceDirectivesDurablePowerOfAttorney, "
						+ "SH.ADR_hcproxy as advanceDirectivesHcProxy, SH.proxy_name as advanceDirectivesHcProxyName, SH.ADR_incremented as advanceDirectivesIncremented, SH.ADR_sum_discussed as advanceDirectivesDiscussionsCount, "
						+ "SH.comments as comments, SH.etoh_date_stopped as alcoholYearQuit, SH.alcoholType as alcoholType, SH.etohfrequency as alcoholFrequency, SH.last_alcoholic_bev as alcoholLastDrink, "
						+ "SH.amount_per_day as alcoholAmount, "
						+ "CASE WHEN SH.smoke_status IS NULL or upper(ltrim(rtrim(smoke_status))) not in ('CURRENT EVERY DAY SMOKER', 'CURRENT SOME DAY SMOKER', 'SMOKER, CURRENT STATUS UNKNOWN', 'UNKNOWN IF EVER SMOKED', 'FORMER SMOKER', 'NEVER SMOKER', 'HEAVY TOBACCO SMOKER', 'LIGHT TOBACCO SMOKER') THEN 'Unknown if ever smoked' ELSE smoke_status END as smokingStatus, "
						+ "CASE WHEN SH.smoke_status_code IS NULL or ltrim(rtrim(smoke_status_code)) not in ('449868002', '428041000124106', '77176002', '266927001', '8517006', '266919005', '428071000124103', '428061000124105') THEN '266927001' ELSE smoke_status_code END as smokingStatusCode, "
						+ "P.marital_status as maritalStatus, P.religion as religion "
						+ "From Social_Hx_ SH,person P "
						+ "Where SH.person_id = P.person_id and "
						+ "SH.person_id = '"+parameters.get("personId")+"' "
						+ "order by SH.create_timestamp desc, SH.create_timestamp_tz desc, SH.created_by desc, SH.modify_timestamp desc, SH.modify_timestamp_tz desc, SH.modified_by desc, SH.enterprise_id desc, "
						+ "SH.practice_id desc, SH.person_id desc, SH.enc_id desc, SH.language_at_home desc, SH.birthplace desc, SH.opt_right_left_hnd desc, SH.education_level desc, SH.txt_bh_degree desc, "
						+ "SH.txt_bh_degree_country desc, SH.hazard1 desc, SH.hazard2 desc, SH.opt_military_experience desc, SH.txt_military_type desc, SH.txt_military_branch desc, SH.txt_military_years_served desc, "
						+ "SH.txt_military_discharge_type desc, SH.opt_military_stat_overseas desc, SH.txt_military_overseas_where desc, SH.opt_military_noise_exp desc, SH.opt_military_biohazard_exp desc, "
						+ "SH.previous_widowed desc, SH.prev_widow_times desc, SH.previous_divorce desc, SH.prev_divorce_times desc, SH.children desc, SH.number_sons desc, SH.number_daughter desc, "
						+ "SH.txt_support_netwk_comments desc, SH.txt_suuport_network desc, SH.txt_primary_res_comments desc, SH.tobacco_yes desc, SH.tobacco_type desc, SH.pack_per_day desc, "
						+ "SH.txt_unit desc, SH.Yrs_Smoked desc, SH.packyears desc, SH.opt_current_past desc, SH.tobaccoQuit desc, SH.tobacco_date_stop desc, SH.txt_prd_tobac_free desc, "
						+ "SH.txt_reason_relapse desc, SH.passiveSmoke desc, SH.txt_concat desc, SH.alcohol_yes desc, SH.caffeine_yes desc, SH.caffeine_type desc, SH.caffeine_type2 desc, "
						+ "SH.caffeine_per_day desc, SH.activity_level desc, SH.helath_club_mbr desc, SH.exercise_freq desc, SH.exerciseHrsWk desc, SH.exercise_type desc, SH.exerciseType2 desc, "
						+ "SH.exerciseType3 desc, SH.hobby1 desc, SH.hobby2 desc, SH.hobby3 desc, SH.diet_hx1 desc, SH.diet_hx2 desc, SH.diet_hx3 desc, SH.diet_hx4 desc, SH.diet_hx5 desc, "
						+ "SH.animals desc, SH.birds desc, SH.dogs desc, SH.rodents desc, SH.reptiles desc, SH.other desc, SH.txt_pets_doc_txt desc, SH.animals_clean desc, SH.opt_sleep desc, "
						+ "SH.opt_religious_affillation desc, SH.opt_practice_religion desc, SH.opt_spiritual_beliefs desc, SH.opt_importance_spirituality desc, SH.smoke_detectors desc, "
						+ "SH.co_detectors desc, SH.radon desc, SH.homeHeating desc, SH.seat_belts desc, SH.firearms_yes desc, SH.transfusion_yes desc, SH.travelState desc, SH.txt_travel_out_state desc, "
						+ "SH.travelCountry desc, SH.txt_travel_out_counrty desc, SH.txt_travel_rgn desc, SH.chk_travelexposure desc, SH.txt_travel_exposure desc, SH.dt_directives_reviewed desc, "
						+ "SH.ADR_none desc, SH.ADR_refused desc, SH.ADR_DNR desc, SH.ADR_livingwill desc, SH.ADR_life_support desc, SH.ADR_Attorney desc, SH.ADR_hcproxy desc, SH.proxy_name desc, "
						+ "SH.ADR_incremented desc, SH.ADR_sum_discussed desc, SH.comments desc, SH.etoh_date_stopped desc, SH.alcoholType desc, SH.etohfrequency desc, SH.last_alcoholic_bev desc, "
						+ "SH.amount_per_day desc, SH.smoke_status desc, SH.smoke_status_code desc, P.marital_status desc, P.religion desc";

						
				
				String[] jsonMetaData = {"createTimestamp;Date_YYYYMMDD HH:MM:SS","createTimestampTimezone","createdBy","modifyTimestamp;Date_YYYYMMDD HH:MM:SS","modifyTimestampTimezone",
						"modifiedBy","enterpriseId","practiceId","personId","encounterId","homeLanguage","birthCountry","handDominance",
						"educationLevel","educationDegree","educationDegreeCountry","occupationalHazard1","occupationalHazard2",
						"hasMilitaryExperience","militaryType","militaryBranch","militaryYearsServed","militaryDischargeType",
						"militaryStationedOverseas","militaryStationedOverseasLocation","militaryNoiseExposure","militaryBiohazardExposure",
						"previousWidowed","previousWidowedTimes","previousDivorce","previousDivorceTimes","hasChildren","sonsCount",
						"daughtersCount","supportNetworkComments","supportNetwork","primaryResidenceComments","tobaccoUsage",
						"tobaccoType","tobaccoUnitsPerDay","tobaccoUnit","tobaccoYearsSmoked","tobaccoPackYears","tobaccoCurrentOrPast",
						"tobaccoPreviousCessationAttempt","tobaccoYearQuit","tobaccoLongestFreePeriod","tobaccoRelapseReason",
						"passiveSmokeExposure","tobaccoCessationMethods","alcoholUsage","caffeineUsage","caffeineType","caffeineType2",
						"caffeinePerDay","activityLevel","healthClubMember","exerciseFrequency","exerciseHoursWeek","exerciseType",
						"exercisetype2","exercisetype3","hobby1","hobby2","hobby3","dietHistory1","dietHistory2","dietHistory3",
						"dietHistory4","dietHistory5","petAnimals","petBirds","petDogs","petRodents","petReptiles","petOther",
						"pets","cleansAnimals","changesInSleepPatterns","religiousAffiliation","practiceReligion","spiritualBeliefs","importanceOfReligionSpirituality",
						"smokeDetectorsInHome","carbonMonoxideDetectorsInHome","radonInHome","homeHeating","seatBeltUsage","fireArmsAtHome",
						"agreesToTransfusion","travelOutOfState","travelOutOfStateLocation","travelOutOfCountry","travelOutOfCounrtyLocation",
						"travelOutOfCountryRegion","travelExposure","travelExposureDetail","advanceDirectivesReviewDate","advanceDirectivesNone",
						"advanceDirectivesRefused","advanceDirectivesDnr","advanceDirectivesLivingWill","advanceDirectivesDoNotPlaceOnLifeSupport",
						"advanceDirectivesDurablePowerOfAttorney","advanceDirectivesHcProxy","advanceDirectivesHcProxyName","advanceDirectivesIncremented",
						"advanceDirectivesDiscussionsCount","comments","alcoholYearQuit","alcoholType","alcoholFrequency","alcoholLastDrink",
						"alcoholAmount","smokingStatus","smokingStatusCode","maritalStatus","religion"};
				
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..createTimestamp", jsonList);
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
