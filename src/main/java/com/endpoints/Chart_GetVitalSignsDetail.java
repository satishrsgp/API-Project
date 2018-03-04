package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

/*
 * ********************************************************************
 * Class Name						: Chart_GetVitalSignsDetail 
 * Created By						: Seema Jaffary
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: 
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							:
 * ******************************************************************
 */
public class Chart_GetVitalSignsDetail {

	
	public static Boolean chart_GetVitalSignsDetail_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				
				String strQuery = "SELECT vs.seq_no AS id, vs.person_id AS personId, vs.enterprise_id AS enterpriseId, "
						+ "vs.practice_id AS practiceId, vs.enc_id AS encounterId, vs.BMI_calc AS bmi, vs.bp_display AS bloodPressure, "
						+ "vs.bp_systolic AS bloodPressureSystolic, vs.bp_diastolic AS bloodPressureDiastolic, "
						+ "vs.bp_method AS bloodPressureMethod, vs.BP_target_side AS bloodPressureSide, vs.BP_target_site AS bloodPressureBodySite, "
						+ "vs.bp_target_site_cd AS bloodPressureSite, vs.bp_body_position_2 AS bloodPressureBodyPosition, "
						+ "CASE WHEN vs.bp_cuff_size1 = '0' THEN 'false' ELSE 'true' END AS isBloodPressureCuffSizePediatric, "
						+ "CASE WHEN vs.bp_cuff_size2 = '0' THEN 'false' ELSE 'true' END AS isBloodPressureCuffSizeAdult, "
						+ "CASE WHEN vs.bp_cuff_size3 = '0' THEN 'false' ELSE 'true' END AS isBloodPressureCuffSizeLarge, "
						+ "CASE WHEN vs.bp_cuff_size4 = '0' THEN 'false' ELSE 'true' END AS isBloodPressureCuffSizeThigh, "
						+ "CASE WHEN vs.chk_os_cons_bp = '1' THEN 'true' ELSE 'false' END AS isBloodPressureCaptured, "
						+ "vs.measured_by AS measuredBy, vs.vitalSignsDate AS vitalSignsDate, vs.vitalSignsTime AS vitalSignsTime, "
						+ "vs.FiO2 AS fractionOfInspiredOxygen, vs.pulse_pattern_2 AS pulsePattern, vs.pulse_rate AS pulseRate, "
						+ "vs.sp_o2_dtl AS pulseOximeterOxygenSaturationRest, vs.SpO2TargetSite AS pulseOximeterOxygenSaturationRestSite, "
						+ "CASE WHEN vs.atxt_growth_charts = '1' THEN 'true' WHEN vs.atxt_growth_charts = '0' THEN 'false' ELSE NULL END AS isGrowthChartCaptured, "
						+ "CASE WHEN vs.atxt_HAQ_popop = '1' THEN 'true' WHEN vs.atxt_HAQ_popop = '2' THEN 'false' ELSE NULL END AS isHealthAssessmentQuestionnaireCaptured, "
						+ "CASE WHEN vs.atxt_vs_exclude = '1' THEN 'true' ELSE 'false' END AS isExclusionCaptured, "
						+ "vs.bmi_percent AS bmiPercent, vs.body_surface_area AS bodySurfaceArea, "
						+ "CASE WHEN vs.chk_os_cons_hc = 'y' THEN 'true' ELSE 'false' END AS isHipCircumferenceCaptured, "
						+ "CASE WHEN vs.chk_os_cons_hh = 'y' THEN 'true' ELSE 'false' END AS isHeadCircumferenceCaptured, "
						+ "CASE WHEN vs.chk_os_cons_ht = 'y' THEN 'true' ELSE 'false' END AS isHeightCaptured, "
						+ "CASE WHEN vs.chk_os_cons_nc = 'y' THEN 'true' ELSE 'false' END AS isNeckCircumferenceCaptured, "
						+ "CASE WHEN vs.chk_os_cons_T = 'y' THEN 'true' ELSE 'false' END AS isTemperatureCaptured, "
						+ "CASE WHEN vs.chk_os_cons_wc = 'y' THEN 'true' ELSE 'false' END AS isWaistCircumferenceCaptured, "
						+ "CASE WHEN vs.chk_os_cons_wt = 'y' THEN 'true' ELSE 'false' END AS isWeightCaptured, "
						+ "CASE WHEN vs.chk_Refused = 0 THEN 'false' ELSE 'true' END AS hasPatientRefused, "
						+ "CASE WHEN vs.chk_unobtainable = 0 THEN 'false' ELSE 'true' END AS isUnobtainable, vs.comments AS comments, "
						+ "vs.head_circ_cm AS headCircumferenceCentimeters, vs.head_circ_in AS headCircumferenceInches, "
						+ "vs.head_circ_percent AS headCircumferencePercent, vs.height_cm AS heightCentimeters, vs.height_date AS heightDate, "
						+ "vs.height_ft AS heightFeet, vs.height_in AS heightInches, vs.height_m AS heightMeters, vs.height_method AS heightMethod, "
						+ "vs.height_percent AS heightPercent, vs.hip_circ_cm AS hipCircumferenceCentimeters, "
						+ "vs.hip_circ_in AS hipCircumferenceInches, vs.ht_body_position AS heightBodyPosition, "
						+ "vs.neck_circ_cm AS neckCircumferenceCentimeters, vs.neck_circ_in AS neckCircumferenceInches, "
						+ "vs.opt_O2_sat AS pulseOximeterOxygenSaturationSource, vs.painScore_LOINC AS painScoreLoinc, "
						+ "vs.painScoreDisplay AS painScoreDisplay, vs.peakFlow AS peakFlow, vs.peakFlowMethod AS peakFlowMethod, "
						+ "vs.peakFlowTiming AS peakFlowTiming, vs.respiration_rate AS respirationRate, "
						+ "vs.spO2Timing AS pulseOximeterOxygenSaturationMeasured, vs.temp_deg_C AS temperatureDegreesCelcius, "
						+ "vs.temp_deg_F AS temperatureDegreesFahrenheit, vs.temp_targt_site_cd AS temperatureSite, "
						+ "vs.txt_02LPM AS fractionOfInspiredOxygenLitresPerMinute, vs.txt_current_age AS currentAge, "
						+ "vs.txt_Fio2_room_air AS fractionOfInspiredOxygenRoomAir, vs.txt_ox_lpm AS pulseOximeterOxygenSaturationRestLitresPerMinute, "
						+ "vs.txt_pain_method AS painMethod, vs.txt_pulse_ox_amb AS pulseOximeterOxygenSaturationAmbulatory, "
						+ "vs.txt_reason_refused AS reasonRefused, vs.txt_reason_unobtainable AS unobtainableReason, "
						+ "vs.txt_refused AS patientRefusedVitals, vs.txt_unobtainable AS unobtainableVitals, "
						+ "vs.waist_circ_cm AS waistCircumferenceCentimeters, vs.waist_circ_in AS waistCircumferenceInches, "
						+ "vs.waist_hip_ratio AS waistHipRatio, vs.weight_context AS weightContext, vs.weight_kg AS weightKilograms, "
						+ "vs.weight_lb AS weightPounds, vs.weight_percent AS weightPercent, vs.bp_cuff_size AS bloodPressureCuffSize, "
						+ "CASE WHEN vs.atxt_ortho = '1' THEN 'true' WHEN vs.atxt_ortho = '0' THEN 'false' ELSE NULL END AS areOrthostaticVitalSignsCaptured, "
						+ "h.opt_menopausal_stage AS menopausalStage, rg.LMP AS lmp, bh.gest_weeks AS gestationalWeeks, vs.ht_cm_graph AS heightCentimetersGraph, "
						+ "vs.ht_in_graph AS heightInchesGraph, vs.wt_kg_graph AS weightKilogramsGraph, "
						+ "vs.wt_lb_graph AS weightPoundsGraph FROM vital_signs_ AS vs "
						+ "LEFT JOIN histories_ AS h ON vs.person_id = h.person_id AND vs.enc_id = h.enc_id "
						+ "LEFT JOIN ros_gyn_ AS rg ON vs.person_id = rg.person_id AND vs.enc_id = rg.enc_id "
						+ "LEFT JOIN birth_hx_ AS bh ON vs.person_id = bh.person_id AND vs.enc_id = bh.enc_id "
						+ "WHERE vs.person_id = '"+parameters.get("personId")+"' AND vs.enc_id = '"+parameters.get("encounterId")+"' "
						+ "AND vs.seq_no = '"+parameters.get("vitalsId")+"'";
						
										
				String[] jsonMetaData = {"id","personId","enterpriseId","practiceId","encounterId","bmi", "bloodPressure",
						"bloodPressureSystolic","bloodPressureDiastolic","bloodPressureMethod","bloodPressureSide","bloodPressureBodySite",
						"bloodPressureSite","bloodPressureBodyPosition", "isBloodPressureCuffSizePediatric", "isBloodPressureCuffSizeAdult",
						"isBloodPressureCuffSizeLarge","isBloodPressureCuffSizeThigh","isBloodPressureCaptured","measuredBy","vitalSignsDate;Date_YYYYMMDD HH:MM:SS",
						"vitalSignsTime", "fractionOfInspiredOxygen", "pulsePattern", "pulseRate", "pulseOximeterOxygenSaturationRest", 
						"pulseOximeterOxygenSaturationRestSite", "isGrowthChartCaptured", "isHealthAssessmentQuestionnaireCaptured", 
						"isExclusionCaptured", "bmiPercent", "bodySurfaceArea", "isHipCircumferenceCaptured", "isHeadCircumferenceCaptured", 
						"isHeightCaptured", "isNeckCircumferenceCaptured", "isTemperatureCaptured", "isWaistCircumferenceCaptured", 
						"isWeightCaptured", "hasPatientRefused", "isUnobtainable", "comments", "headCircumferenceCentimeters",
						"headCircumferenceInches", "headCircumferencePercent", "heightCentimeters", "heightDate;Date_YYYYMMDD HH:MM:SS", "heightFeet", "heightInches", 
						"heightMeters", "heightMethod", "heightPercent", "hipCircumferenceCentimeters", "hipCircumferenceInches", 
						"heightBodyPosition", "neckCircumferenceCentimeters", "neckCircumferenceInches", "pulseOximeterOxygenSaturationSource", 
						"painScoreLoinc", "painScoreDisplay", "peakFlow", "peakFlowMethod", "peakFlowTiming", "respirationRate", 
						"pulseOximeterOxygenSaturationMeasured", "temperatureDegreesCelcius", "temperatureDegreesFahrenheit", "temperatureSite", 
						"fractionOfInspiredOxygenLitresPerMinute", "currentAge", "fractionOfInspiredOxygenRoomAir", "pulseOximeterOxygenSaturationRestLitresPerMinute", 
						"painMethod", "pulseOximeterOxygenSaturationAmbulatory", "reasonRefused", "unobtainableReason", "patientRefusedVitals", 
						"unobtainableVitals", "waistCircumferenceCentimeters", "waistCircumferenceInches", "waistHipRatio", "weightContext", 
						"weightKilograms", "weightPounds", "weightPercent", "bloodPressureCuffSize", "areOrthostaticVitalSignsCaptured", 
						"menopausalStage", "lmp", "gestationalWeeks", "heightCentimetersGraph", "heightInchesGraph", "weightKilogramsGraph", 
						"weightPoundsGraph"};
				
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
