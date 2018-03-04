package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetHealthConcernVitals {

	public static Boolean Chart_GetHealthConcernVitals_Test(HashMap<String, String> parameters, String strResponse,
			String strUniqueJsonToken, SoftAssert softAssert) {
		Boolean status = false;
		if (!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid")) {
			try {
				
				
				String strQuery="SELECT  CASE WHEN vs.bp_systolic IS NULL OR vs.bp_diastolic IS NULL THEN NULL ELSE CONCAT ('Blood Pressure - ',vs.bp_systolic,'/',vs.bp_diastolic,' mm/Hg') END AS"
						+ " bloodPressureHealthConcern, 'VitalsBloodPressure' AS bloodPressureCategory, vs.bp_systolic AS bloodPressureSystolicValue, vs.bp_diastolic AS"
						+ " bloodPressureDiastolicValue, 'mm/Hg' AS bloodPressureUnit, CASE WHEN vs.temp_deg_C IS NULL THEN NULL ELSE CONCAT ('Body Temperature - ',vs.temp_deg_C,' C') END AS"
						+ " bodyTemperatureHealthConcern, 'VitalsBodyTemperature' AS bodyTemperatureCategory, vs.temp_deg_C AS"
						+ " bodyTemperatureValue, 'C' AS bodyTemperatureUnit, CASE WHEN vs.head_circ_cm IS NULL THEN NULL ELSE CONCAT ('Head Circumference - ',vs.head_circ_cm,' cm') END AS"
						+ " headCircumferenceHealthConcern, 'VitalsHeadCircumference' AS headCircumferenceCategory, vs.head_circ_cm AS headCircumferenceValue, 'cm' AS"
						+ " headCircumferenceUnit, CASE WHEN vs.pulse_rate IS NULL THEN NULL ELSE CONCAT ('Heart Rate - ',pulse_rate,'/min') END AS heartRateHealthConcern, 'VitalsHeartRate' AS"
						+ " heartRateCategory, vs.pulse_rate AS heartRateValue, 'per min' AS heartRateUnit, CASE WHEN vs.height_cm IS NULL THEN NULL ELSE CONCAT ('Height - ',height_cm,' cm') END AS"
						+ " heightHealthConcern, 'VitalsHeight' AS heightCategory, vs.height_cm AS heightValue, 'cm' AS"
						+ " heightUnit, CASE WHEN vs.weight_kg IS NULL THEN NULL ELSE CONCAT ('Weight - ',weight_kg,' kg') END AS weightHealthConcern, 'VitalsWeight' AS"
						+ " weightCategory, vs.weight_kg AS weightValue, 'kg' AS weightUnit, CASE WHEN vs.BMI_calc IS NULL THEN NULL ELSE CONCAT ('BMI - ',BMI_calc) END AS"
						+ " bmiHealthConcern, 'VitalsBmi' AS bmiCategory, vs.BMI_calc AS bmiValue, CASE WHEN vs.sp_o2_dtl IS NULL THEN NULL ELSE CONCAT ('O2 % BldC Oximetry - ',sp_o2_dtl,'%') END AS"
						+ " o2BldCOximetryHealthConcern, 'VitalsO2BldCOximetry' AS o2BldCOximetryCategory, vs.sp_o2_dtl AS o2BldCOximetryValue, 'percent' AS"
						+ " o2BldCOximetryUnit, CASE WHEN vs.respiration_rate IS NULL THEN NULL ELSE CONCAT ('Respiratory Rate - ',respiration_rate,'/min') END AS"
						+ " respiratoryRateHealthConcern, 'VitalsRespiratoryRate' AS respiratoryRateCategory, vs.respiration_rate AS respiratoryRateValue, 'per min' AS"
						+ " respiratoryRateUnit,vs.seq_no AS id, vs.enc_id AS encounterId, vs.enterprise_id AS enterpriseId, vs.practice_id AS practiceId, vs.person_id AS"
						+ " personId, pe.enc_timestamp AS encounterTimestamp "
						+ "FROM vital_signs_ AS vs INNER JOIN patient_encounter AS pe "
						+ "ON vs.enc_id = pe.enc_id AND vs.person_id = '"+parameters.get("personId")+"' AND vs.enterprise_id = '"+System.getProperty("LoggedInEnterpriseId")+"' AND vs.practice_id = '"+System.getProperty("LoggedInPracticeId")+"'";

				// providerName ,"whoReviewed": null,"whenReviewed": null,

				String[] jsonMetaData = { "bloodPressureHealthConcern","bloodPressureCategory","bloodPressureSystolicValue","bloodPressureDiastolicValue","bloodPressureUnit",
						"bodyTemperatureHealthConcern","bodyTemperatureCategory","bodyTemperatureValue","bodyTemperatureUnit","headCircumferenceHealthConcern","headCircumferenceCategory",
						"headCircumferenceValue","headCircumferenceUnit","heartRateHealthConcern","heartRateCategory","heartRateValue","heartRateUnit","heightHealthConcern",
						"heightCategory","heightValue","heightUnit","weightHealthConcern","weightCategory","weightValue","weightUnit","bmiHealthConcern","bmiCategory",
						"bmiValue","o2BldCOximetryHealthConcern","o2BldCOximetryCategory","o2BldCOximetryValue","o2BldCOximetryUnit","respiratoryRateHealthConcern",
						"respiratoryRateCategory","respiratoryRateValue","respiratoryRateUnit","id","encounterId","enterpriseId","practiceId","personId","encounterTimestamp;Date_YYYYMMDD HH:MM:SS"};
				try {
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse,
							strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(
							DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse,
							"$.." + strUniqueJsonToken, jsonList);
				} catch (Exception e) {
					e.printStackTrace();
					Log.info("Error in Test Class and the detail are : \n" + e.getMessage());
					softAssert.fail("Error in Test Script please look at logs");
					// objHandler.setStrRequestStatus("Fail");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			status = true;
		}
		return status;
	}
}

