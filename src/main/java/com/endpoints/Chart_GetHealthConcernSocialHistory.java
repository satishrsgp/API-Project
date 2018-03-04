package com.endpoints;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.JsonListArray;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_GetHealthConcernSocialHistory {
	
	public static Boolean Chart_GetHealthConcernSocialHistory_Test(HashMap<String, String> parameters, String strResponse,
			String strUniqueJsonToken, SoftAssert softAssert) {
		Boolean status = false;
		if (!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid")) {
			try {
				
				
				String strQuery="SELECT sh.exercise_type AS exerciseType, sh.exerciseType2 AS exerciseType2, sh.exerciseType3 AS exerciseType3, sh.exercise_freq AS exerciseFrequency"
						+ ", sh.exerciseHrsWK AS exerciseHoursWork, CASE WHEN sh.alcohol_yes IS NULL THEN 'NotSet' WHEN sh.alcohol_yes = 'Y' THEN 'Yes' WHEN sh.alcohol_yes = 'N' THEN"
						+ " 'No' WHEN sh.alcohol_yes = 'R' THEN 'Formerly' END AS alcoholUsage, sh.alcoholtype AS alcoholType, sh.etohfrequency AS alcoholFrequency, sh.amount_per_day "
						+ "AS amountPerDay, sh.last_alcoholic_bev AS lastAlcoholicBeverage, CASE WHEN sh.drugs IS NULL THEN 'NotSet' WHEN sh.drugs = 'Y' THEN 'Yes' WHEN"
						+ " sh.drugs = 'N' THEN 'No' WHEN sh.drugs = 'R' THEN 'Formerly' END AS drugs, sh.drugAge AS drugAge, sh.drug_type1 AS drugType1, sh.drug_freq1 AS"
						+ " drugFrequency1, sh.drugRoute AS drugRoute, sh.drug_type2 AS drugType2, sh.drug_freq2 AS drugFrequency2, sh.drugRoute2 AS drugRoute2, sh.drug_type3 AS"
						+ " drugType3, sh.drug_freq3 AS drugFrequency3, sh.drugRoute3 AS drugRoute3, CASE WHEN sh.drugTreatment IS NULL THEN 'NotSet' WHEN CAST(sh.drugTreatment AS"
						+ " VARCHAR(10)) = 'Y' THEN 'Yes' WHEN CAST(sh.drugTreatment AS VARCHAR(10)) = 'N' THEN 'No' WHEN CAST(sh.drugTreatment AS VARCHAR(10)) = 'R' THEN 'Formerly' "
						+ "END AS drugTreatment, sh.txt_drug_treattime AS drugTreatmentCount, CASE WHEN sh.drugMedAttention IS NULL THEN 'NotSet' WHEN CAST(sh.drugMedAttention AS"
						+ " VARCHAR(10)) = 'Y' THEN 'Yes' WHEN CAST(sh.drugMedAttention AS VARCHAR(10)) = 'N' THEN 'No' WHEN CAST(sh.drugMedAttention AS VARCHAR(10)) = 'R' THEN"
						+ " 'Formerly' END AS drugMedicalAttention, sh.drugAttentNumber AS drugMedicalAttentionCount, sh.education_level AS educationLevel, sh.txt_bh_degree AS"
						+ " degree, CASE WHEN sh.passiveSmoke IS NULL THEN 'NotSet' WHEN CAST(sh.passiveSmoke AS VARCHAR(10)) = 'Y' THEN 'Yes' WHEN CAST(sh.passiveSmoke AS"
						+ " VARCHAR(10)) = 'N' THEN 'No' WHEN CAST(sh.passiveSmoke AS VARCHAR(10)) = 'R' THEN 'Formerly' END AS passiveSmoker, sh.enc_id AS"
						+ " encounterId, sh.enterprise_id AS enterpriseId, sh.practice_id AS practiceId, sh.person_id AS personId, CAST(CONVERT(varchar,pe.enc_timestamp,120) AS"
						+ " VARCHAR(20)) AS encounterTimestamp, shtu.txt_tobacco_user_profile AS tobaccoUserProfile, shtu.txt_passive_tob_type AS"
						+ " passiveTobaccoType, shtu.txt_passive_ength_exposure AS passiveLengthExposure, shtu.txt_passive_level_exposure AS passiveLevelExposure, NULL AS"
						+ " employmentName, NULL AS employmentStatus, NULL AS employmentRestrictions, NULL AS occupation, NULL AS"
						+ " employmentRetireDate, '00000000-0000-0000-0000-000000000000' AS employmentId, 'SocialHistoryTobaccoUseAndExposure' AS"
						+ " tobaccoUseCategory, 'SocialHistoryExercise' AS exerciseCategory, 'SocialHistoryAlcoholIntake' AS alcoholIntakeCategory, 'SocialHistoryEmploymentDetails' AS"
						+ " employmentDetailCategory, 'SocialHistoryDrugMisuseBehavior' AS drugMisuseBehaviorCategory, 'SocialHistoryEducationalAchievement' AS"
						+ " educationalAchievementCategory FROM Social_Hx_ AS sh LEFT JOIN patient_encounter AS pe ON sh.enc_id = pe.enc_id LEFT JOIN soc_hx_tob_use_ AS"
						+ " shtu ON pe.enc_id = shtu.enc_id WHERE sh.person_id = '"+parameters.get("personId")+"' AND sh.enterprise_id = '"+System.getProperty("LoggedInEnterpriseId")+"' AND"
						+ " sh.practice_id = '"+System.getProperty("LoggedInPracticeId")+"' UNION ALL SELECT NULL AS exerciseType, NULL AS exerciseType2, NULL AS exerciseType3, NULL AS exerciseFrequency, NULL AS"
						+ " exerciseHoursWork, 'NotSet' AS alcoholUsage, NULL AS alcoholType, NULL AS alcoholFrequency, NULL AS amountPerDay, NULL AS lastAlcoholicBeverage, 'NotSet' AS"
						+ " drugs, NULL AS drugAge, NULL AS drugType1, NULL AS drugFrequency1, NULL AS drugRoute, NULL AS drugType2, NULL AS drugFrequency2, NULL AS drugRoute2, NULL AS"
						+ " drugType3, NULL AS drugFrequency3, NULL AS drugRoute3, 'NotSet' AS drugTreatment, NULL AS drugTreatmentCount, 'NotSet' AS"
						+ " drugMedicalAttention, NULL AS drugMedicalAttentionCount, NULL AS educationLevel, NULL AS degree, 'NotSet' AS"
						+ " passiveSmoker, '00000000-0000-0000-0000-000000000000' AS encounterId, enterprise_id AS enterpriseId, practice_id AS practiceId, person_id AS"
						+ " personId, CAST('0001-01-01 00:00:00' AS VARCHAR(20)) AS encounterTimestamp, NULL AS tobaccoUserProfile, NULL AS passiveTobaccoType, NULL AS"
						+ " passiveLengthExposure, NULL AS passiveLevelExposure, name AS employmentName, description AS employmentStatus, txt_restrictions AS"
						+ " employmentRestrictions, mstr_list_item_desc AS occupation, retire_date AS employmentRetireDate, seq_no AS"
						+ " employmentId, 'SocialHistoryTobaccoUseAndExposure' AS tobaccoUseCategory, 'SocialHistoryExercise' AS"
						+ " exerciseCategory, 'SocialHistoryAlcoholIntake' AS alcoholIntakeCategory, 'SocialHistoryEmploymentDetails' AS"
						+ " employmentDetailCategory, 'SocialHistoryDrugMisuseBehavior' AS drugMisuseBehaviorCategory, 'SocialHistoryEducationalAchievement' AS"
						+ " educationalAchievementCategory "
						+ "FROM employment_dmp_ WHERE person_id = '"+parameters.get("personId")+"' AND enterprise_id = '"+System.getProperty("LoggedInEnterpriseId")+"' AND practice_id = '"+System.getProperty("LoggedInPracticeId")+"' ORDER BY encounterTimestamp";

				// providerName ,"whoReviewed": null,"whenReviewed": null,

				String[] jsonMetaData = {"exerciseType","exerciseType2","exerciseType3","exerciseFrequency","exerciseHoursWork","alcoholUsage","alcoholType","alcoholFrequency","amountPerDay","lastAlcoholicBeverage","drugs","drugAge","drugType1",
						"drugFrequency1","drugRoute","drugType2","drugFrequency2","drugRoute2","drugType3","drugFrequency3","drugRoute3","drugTreatment","drugTreatmentCount","drugMedicalAttention","drugMedicalAttentionCount","educationLevel","degree","passiveSmoker","encounterId","enterpriseId","practiceId","personId",
						"encounterTimestamp;Date_YYYYMMDD HH:MM:SS","tobaccoUserProfile","passiveTobaccoType","passiveLengthExposure","passiveLevelExposure","employmentName","employmentStatus","employmentRestrictions","occupation","employmentRetireDate","employmentId","tobaccoUseHealthConcern","tobaccoUseCategory","exerciseHealthConcern","exerciseCategory","alcoholIntakeHealthConcern","alcoholIntakeCategory","employmentDetailHealthConcern","employmentDetailCategory","drugMisuseBehaviorHealthConcern","drugMisuseBehaviorCategory","educationalAchievementHealthConcern","educationalAchievementCategory"
						};
				try {
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse,
							strUniqueJsonToken);
					
					
					
					
					
					ArrayList<Object> dbList = new ArrayList<Object>(); ;
					ResultSet rs=DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery);
					//ResultSetMetaData rsmd=rs.getMetaData(); 
					
				//	dbList=DatabaseConnection.addResultSettoArrayList(rs);
					
					while(rs.next())
					{
						String tobaccoUseHealthConcern="",exerciseHealthConcern="",alcoholIntakeHealthConcern="",employmentDetailHealthConcern="",drugMisuseBehaviorHealthConcern="",educationalAchievementHealthConcern="";
						
						tobaccoUseHealthConcern+=append("Tobacco use status",rs.getString("tobaccoUserProfile"));
						if(rs.getString("passiveSmoker").equals("Yes"))
							tobaccoUseHealthConcern+=append("Passive smoke exposure","Yes");
						else if(rs.getString("passiveSmoker").equals("No"))
							tobaccoUseHealthConcern+=append("Passive smoke exposure","No/never");
						tobaccoUseHealthConcern+=append("Tobacco Type",rs.getString("passiveTobaccoType"));
						tobaccoUseHealthConcern+=append("Length of exposure Type",rs.getString("passiveLengthExposure"));
						tobaccoUseHealthConcern+=append("Length of exposure Type",rs.getString("passiveLengthExposure"));
						tobaccoUseHealthConcern+=append("Level of exposure",rs.getString("passiveLevelExposure"));
						
						
						exerciseHealthConcern+=append("Type of exercise",rs.getString("exerciseType"));
						exerciseHealthConcern+=append(rs.getString("exerciseType2"));
						exerciseHealthConcern+=append(rs.getString("exerciseType3"));
						exerciseHealthConcern+=append("Exercise frequency",rs.getString("exerciseFrequency"));
						exerciseHealthConcern+=append("Hours/Week",rs.getString("exerciseHoursWork"));
						
						
						if(rs.getString("alcoholUsage").equals("Yes"))
							alcoholIntakeHealthConcern+=append("Drinks alcohol","Y");
						else if(rs.getString("alcoholUsage").equals("No"))
							alcoholIntakeHealthConcern+=append("Drinks alcohol","N");
						alcoholIntakeHealthConcern+=append("Type of alcohol",rs.getString("alcoholType"));
						alcoholIntakeHealthConcern+=append("Frequency",rs.getString("alcoholFrequency"));
						alcoholIntakeHealthConcern+=append("Amount",rs.getString("amountPerDay"));
						alcoholIntakeHealthConcern+=append("Last drink",rs.getString("lastAlcoholicBeverage"));
						
						
						employmentDetailHealthConcern+=append("Employment",rs.getString("employmentName"));
						employmentDetailHealthConcern+=append("Employment status",rs.getString("employmentStatus"));
						employmentDetailHealthConcern+=append("Restrictions",rs.getString("employmentRestrictions"));
						employmentDetailHealthConcern+=append("Occupation",rs.getString("occupation"));
						employmentDetailHealthConcern+=append("Retired",rs.getString("employmentRetireDate"));
						
						
						if(!rs.getString("drugs").equals("NotSet"))
							drugMisuseBehaviorHealthConcern+=append("Use drugs",rs.getString("drugs"));
							if(rs.getString("drugAge")!=null)
								drugMisuseBehaviorHealthConcern+="Age started:"+rs.getString("drugAge")+" year(s), ";
							drugMisuseBehaviorHealthConcern+=append("Type",rs.getString("drugType1"));
							drugMisuseBehaviorHealthConcern+=append("Frequency",rs.getString("drugFrequency1"));
							drugMisuseBehaviorHealthConcern+=append("Route",rs.getString("drugRoute"));
							drugMisuseBehaviorHealthConcern+=append("Type",rs.getString("drugType2"));
							drugMisuseBehaviorHealthConcern+=append("Frequency",rs.getString("drugFrequency2"));
							drugMisuseBehaviorHealthConcern+=append("Route",rs.getString("drugRoute2"));
							drugMisuseBehaviorHealthConcern+=append("Type",rs.getString("drugType3"));
							drugMisuseBehaviorHealthConcern+=append("Frequency",rs.getString("drugFrequency3"));
							drugMisuseBehaviorHealthConcern+=append("Route",rs.getString("drugRoute3"));
							if(!rs.getString("drugTreatment").equals("NotSet"))
								drugMisuseBehaviorHealthConcern+=append("Sought treatment for drug abuse",rs.getString("drugTreatment"));
							drugMisuseBehaviorHealthConcern+=append("Number of times",rs.getString("drugTreatmentCount"));
							if(!rs.getString("drugMedicalAttention").equals("NotSet"))
								drugMisuseBehaviorHealthConcern+=append("Emergency medical attention required due to drug use",rs.getString("drugMedicalAttention"));
							drugMisuseBehaviorHealthConcern+=append("Number of times",rs.getString("drugMedicalAttentionCount"));

						
						
						educationalAchievementHealthConcern+=append("Education",rs.getString("educationLevel"));
						educationalAchievementHealthConcern+=append("Degree",rs.getString("degree"));


					

						
						for(int i=1;i<=43;i++)
							dbList.add(rs.getObject(i));
						
						if(tobaccoUseHealthConcern.equals(""))
							dbList.add(null);
						else
							dbList.add(tobaccoUseHealthConcern.substring(0, tobaccoUseHealthConcern.length()-2));
						
						dbList.add(rs.getString("tobaccoUseCategory"));
						
						
						
						if(exerciseHealthConcern.equals(""))
							dbList.add(null);
						else
							dbList.add(exerciseHealthConcern.substring(0, exerciseHealthConcern.length()-2));
						
						dbList.add(rs.getString("exerciseCategory"));
						
						
						
						if(alcoholIntakeHealthConcern.equals(""))
							dbList.add(null);
						else
							dbList.add(alcoholIntakeHealthConcern.substring(0, alcoholIntakeHealthConcern.length()-2));
						dbList.add(rs.getString("alcoholIntakeCategory"));
						
						
						
						if(employmentDetailHealthConcern.equals(""))
							dbList.add(null);
						else
							dbList.add(employmentDetailHealthConcern.substring(0, employmentDetailHealthConcern.length()-2));
						
						dbList.add(rs.getString("employmentDetailCategory"));
						
						
						
						
						if(drugMisuseBehaviorHealthConcern.equals(""))
							dbList.add(null);
						else
							dbList.add(drugMisuseBehaviorHealthConcern.substring(0, drugMisuseBehaviorHealthConcern.length()-2));
						
						dbList.add(rs.getString("drugMisuseBehaviorCategory"));
						
						
						
						if(educationalAchievementHealthConcern.equals(""))
							dbList.add(null);
						else
							dbList.add(educationalAchievementHealthConcern.substring(0, educationalAchievementHealthConcern.length()-2));
						dbList.add(rs.getString("educationalAchievementCategory"));
						
					
			
					}
			
					Log.info("JSON LIST is :\n" + jsonList);
					
			/*		status = FinalAssertions.assertResponseCount(
							DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse,
							"$.." + strUniqueJsonToken, jsonList);*/
					
					status =	ValidateResults.resultValidation(jsonList, dbList);
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
	
	public static String append(String str,String value)
	{
	if(value==null)
		return "";
	else
		return str+":"+value+", ";
	}
	public static String append(String value)
	{
	if(value==null)
		return "";
	else
		return value+", ";
	}
}
