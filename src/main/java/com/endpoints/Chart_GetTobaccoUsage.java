package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetTobaccoUsage 
{
	public static Boolean chart_GetTelephoneCalls_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "select tu.enc_id as encounterId, tu.person_id as personId, tu.seq_no as sequenceNumber, tu.create_timestamp as createTimestamp, "
						+ " tu.txt_max_tobacco_free_duration as txt_max_tobacco_free_duration, tu.txt_pack_years as packYears, "
						+ " tu.txt_passive_exposure as passiveExposure, tu.txt_relapse_reason as relapseReason, tu.txt_tobacco_type as tobaccoType, "
						+ " sht.txt_tobacco_type as socialHistoryTobaccoType, tu.txt_tobacco_use_status as tobaccoUseStatus, tu.txt_total_pack_years as totalPackYears,"
						+ " tu.txt_tried_to_quit as triedToQuit, tu.txt_units_per_day as unitsPerDay, tu.txt_year_quit as yearQuit, tu.txt_yrs_used as yearsUsed, "
						+ " tu.txt_age_started as ageStarted, tu.txt_age_stopped as ageStopped, tu.txt_smoking_status as smokingStatus, "
						+ " tu.txt_tobacco_status as tobaccoStatus, tu.txt_usage_per_day as usagePerDay, sht.create_timestamp as socialHistoryCreateTimestamp, "
						+ " sht.txt_tobacco_user_profile_code as tobaccoUserProfileCode, sht.txt_tobacco_user_profile as tobaccoUserProfile, "
						+ " sht.chk_use_daily_cigarette as useDailyCigarette, sht.txt_age_started_cigarette as ageStartedCigarette, "
						+ " sht.txt_age_stopped_cigarette as ageStoppedCigarette, sht.txt_usage_per_day_cigarette as usagePerDayCigarette, "
						+ " sht.chk_use_daily_cigarillo as useDailyCigarillo, sht.txt_age_started_cigarillo as ageStartedCigarillo, "
						+ " sht.txt_age_stopped_cigarillo as ageStoppedCigarillo, sht.txt_usage_per_day_cigarillo as usagePerDayCigarillo, "
						+ " sht.chk_use_daily_cigar as useDailyCigar, sht.txt_age_started_cigar as ageStartedCigar, sht.txt_age_stopped_cigar as ageStoppedCigar, "
						+ " sht.txt_usage_per_day_cigar as usagePerDayCigar, sht.chk_use_daily_pipe as useDailyPipe, sht.txt_age_started_pipe as ageStartedPipe, "
						+ " sht.txt_age_stopped_pipe as ageStoppedPipe, sht.txt_usage_per_day_pipe as usagePerDayPipe from tobacco_usage_ tu "
						+ " inner join soc_hx_tob_use_ sht on tu.person_id = sht.person_id where sht.person_id = '" + parameters.get("personId") + "'";
				
				String[] jsonMetaData = {"encounterId","personId","sequenceNumber","createTimestamp;Date_YYYYMMDD HH:MM:SS","maximumTobaccoFreeDuration","packYears","passiveExposure","relapseReason"
				,"tobaccoType","socialHistoryTobaccoType","tobaccoUseStatus","totalPackYears","triedToQuit","unitsPerDay","yearQuit","yearsUsed","ageStarted"
				,"ageStopped","smokingStatus","tobaccoStatus","usagePerDay","socialHistoryCreateTimestamp;Date_YYYYMMDD HH:MM:SS","tobaccoUserProfileCode","tobaccoUserProfile",
				"useDailyCigarette","ageStartedCigarette","ageStoppedCigarette","usagePerDayCigarette","useDailyCigarillo","ageStartedCigarillo","ageStoppedCigarillo"
				,"usagePerDayCigarillo","useDailyCigar","ageStartedCigar","ageStoppedCigar","usagePerDayCigar","useDailyPipe","ageStartedPipe","ageStoppedPipe",
				"usagePerDayPipe"
				};

				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..encounterId", jsonList);
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
