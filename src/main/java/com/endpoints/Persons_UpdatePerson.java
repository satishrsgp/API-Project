package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.ExcelReaderWriter;
import com.utils.Log;
import com.utils.ValidateResults;

public class Persons_UpdatePerson 
{

	public static Boolean Persons_UpdatePerson_Test(HashMap<String, String> parameters, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strSQLQuery = "select datediff(second,create_timestamp,modify_timestamp) as diff,modified_by as mb  "
					+ "from person "
					+ "WHERE person_id = '"+parameters.get("personId")+"'";
					
			Object objIntDateDiff = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "diff");
			Object objIntModifiedBy = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "mb");
			if(Integer.valueOf(objIntDateDiff.toString()) > 0)
			{
				if(Objects.equals(objIntModifiedBy.toString(), System.getProperty("LoggedInUserId")))
				{
					String strQuery1 ="SELECT TOP 1 RTRIM(p.other_id_number), RTRIM(p.last_name), RTRIM(p.first_name), RTRIM(p.middle_name), RTRIM(p.prior_last_name)"
							+ ", RTRIM(p.address_line_1), RTRIM(p.address_line_2), RTRIM(p.city), RTRIM(p.state), RTRIM(p.zip), RTRIM(p.country_id), RTRIM(p.county_id)"
							+ ", RTRIM(p.home_phone), RTRIM(p.sec_address_line_1), RTRIM(p.sec_address_line_2), RTRIM(p.sec_city), RTRIM(p.sec_state), RTRIM(p.sec_zip)"
							+ ", RTRIM(p.sec_country_id), RTRIM(p.sec_county_id), RTRIM(p.sec_home_phone), RTRIM(p.day_phone), RTRIM(p.day_phone_ext), RTRIM(p.alt_phone)"
							+ ", RTRIM(p.alt_phone_desc), RTRIM(p.alt_phone_ext), RTRIM(FORMAT (CAST(p.date_of_birth AS datetime),'yyyy-MM-dd')), RTRIM(p.sex), RTRIM(p.ssn)"
							+ ", RTRIM(CASE WHEN p.marital_status = 'S' THEN 'Single' WHEN p.marital_status = 'M' THEN 'Married' END)"
							+ ", RTRIM(CASE WHEN p.expired_ind = 'Y' THEN 'true' ELSE 'false' END), RTRIM(p.expired_date)"
							+ ", RTRIM(CASE WHEN p.smoker_ind = 'Y' THEN 'true' ELSE 'false' END), RTRIM(CASE WHEN p.veteran_ind = 'Y' THEN 'true' ELSE 'false' END)"
							+ ", RTRIM(p.race_id), RTRIM(p.language_id), RTRIM(p.religion_id), RTRIM(p.church_id), RTRIM(p.student_status), RTRIM(p.primarycare_prov_id)"
							+ ", RTRIM(p.image_id), RTRIM(p.external_id), RTRIM(p.email_address), RTRIM(p.int_home_phone), RTRIM(p.int_work_phone), RTRIM(p.int_zip)"
							+ ", RTRIM(p.nickname), RTRIM(p.uds_homeless_status_id), RTRIM(p.uds_migrant_worker_status_id), RTRIM(p.uds_language_barrier_id)"
							+ ", RTRIM(p.uds_primary_med_coverage_id), RTRIM(p.contact_seq), RTRIM(p.home_phone_comment), RTRIM(p.day_phone_comment)"
							+ ", RTRIM(p.alt_phone_comment), RTRIM(p.sec_home_phone_comment), RTRIM(p.email_address_comment), RTRIM(p.contact_pref_id)"
							+ ", RTRIM(CASE WHEN p.contact_alert_ind = 'Y' THEN 'true' ELSE 'false' END)"
							+ ", RTRIM(CASE WHEN pec.enterprise_chart_ind = 'Y' THEN 'true' ELSE 'false' END), RTRIM(p.uds_pub_hsng_pri_care_id), RTRIM(p.uds_school_hlth_ctr_id)"
							+ ", RTRIM(CASE WHEN p.self_pay_ind = 'Y' THEN 'true' ELSE 'false' END), RTRIM(p.uds_tribal_affiliation_id), RTRIM(p.uds_blood_quantum_id)"
							+ ", RTRIM(p.uds_veteran_status), RTRIM(CASE WHEN p.uds_consent_to_treat_ind = 'Y' THEN 'true' ELSE 'false' END), RTRIM(p.mothers_maiden_name)"
							+ ", RTRIM(p.uds_ihs_elig_status_id), RTRIM(p.uds_tribal_class_id), RTRIM(p.uds_decendancy_id), RTRIM(p.uds_consent_to_treat_date)"
							+ ", RTRIM(p.community_code_id), RTRIM(p.ethnicity_id), RTRIM(p.primary_dental_prov_id), RTRIM(p.cell_phone), RTRIM(p.cell_phone_comment)"
							+ ", RTRIM(p.address_type_id), RTRIM(p.sec_address_type_id), RTRIM(p.prefix_id), RTRIM(p.suffix_id), RTRIM(p.cause_of_death_code)"
							+ ", RTRIM(p.cause_of_death), RTRIM(p.birth_mothers_lname), RTRIM(p.birth_mothers_fname), RTRIM(p.birth_mothers_mname), RTRIM(p.expired_time)"
							+ ", RTRIM(p.expired_time_tz), RTRIM(CASE WHEN p.email_ind = 'Y' THEN 'true' ELSE 'false' END)"
							+ ", RTRIM(CASE WHEN p.phone_ind = 'Y' THEN 'true' ELSE 'false' END), RTRIM(CASE WHEN p.portal_ind = 'Y' THEN 'true' ELSE 'false' END)"
							+ ", RTRIM(CASE WHEN p.sms_ind = 'Y' THEN 'true' ELSE 'false' END), RTRIM(CASE WHEN p.voice_ind = 'Y' THEN 'true' ELSE 'false' END)"
							+ ", RTRIM(CASE WHEN p.optout_ind = 'Y' THEN 'true' ELSE 'false' END), RTRIM(CASE WHEN p.notific_pref_ind = 'Y' THEN 'true' ELSE 'false' END)"
							+ ", RTRIM(p.risk_level),RTRIM(CASE WHEN p.exempt_frm_person_merge = 'Y' THEN 'true' ELSE 'false' END), RTRIM(p.sexual_orientation)"
							+ ", RTRIM(p.preferred_pronoun), RTRIM(CASE WHEN p.enable_home_phone_ind = 'Y' THEN 'true' ELSE 'false' END)"
							+ ", RTRIM(CASE WHEN p.enable_day_phone_ind = 'Y' THEN 'true' ELSE 'false' END)"
							+ ", RTRIM(CASE WHEN p.enable_alt_phone_ind = 'Y' THEN 'true' ELSE 'false' END)"
							+ ", RTRIM(CASE WHEN p.enable_sec_hm_phone_ind = 'Y' THEN 'true' ELSE 'false' END)"
							+ ", RTRIM(CASE WHEN p.enable_email_address_ind = 'Y' THEN 'true' ELSE 'false' END)"
							+ ", RTRIM(CASE WHEN p.enable_cell_phone_ind = 'Y' THEN 'true' ELSE 'false' END)"
							+ ", RTRIM(CASE WHEN p.enable_int_phone_ind = 'Y' THEN 'true' ELSE 'false' END)"
							+ ", RTRIM(pg.gender_identity_code), RTRIM(p.current_gender), RTRIM(p.previous_first_name), RTRIM(p.other_reason_so), RTRIM(pg.other_reason_gi)"
							+ ", RTRIM(ps.patient_status_id), RTRIM(ps.patient_status_rsn_chng_id), RTRIM(pu.ud_demo1_id), RTRIM(pu.ud_demo2_id), RTRIM(pu.ud_demo3_id)"
							+ ", RTRIM(pu.ud_demo4_id), RTRIM(pu.ud_demo5_id), RTRIM(pu.ud_demo6_id), RTRIM(pu.ud_demo7_id), RTRIM(pu.ud_demo8_id), RTRIM(pu.ud_demo9_id)"
							+ ", RTRIM(pu.ud_demo10_id), RTRIM(pu.ud_demo11_id), RTRIM(pu.ud_demo12_id), RTRIM(pu.ud_demo13_id), RTRIM(pu.ud_demo14_id) "
							+ "FROM person AS p LEFT JOIN person_gender_identity_xref AS pg ON p.person_id = pg.person_id LEFT JOIN patient_status AS ps "
							+ "ON pg.person_id = ps.person_id AND ps.practice_id = '0001' LEFT JOIN person_ud AS pu ON ps.person_id = pu.person_id "
							+ "LEFT JOIN patient_enterprise_chart AS pec ON pu.person_id = pec.person_id AND pec.enterprise_id = '00001' WHERE "
							+ " p.person_id = '"+parameters.get("personId")+"' AND p.enterprise_id='00001' AND p.practice_id='0001'";
							
					//ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
					ArrayList<Object> jsonList  = new ArrayList<>();
					for (int i = 0; i < ExcelReaderWriter.getColumnCount(
							BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), "Persons_AddPerson") - 2; i++) {
						String key = ExcelReaderWriter.getCellValue(
								BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), "Persons_AddPerson", 0, i);

						if (key.equals("UdsVeteranStatus") && parameters.get(key) == "null") {
							jsonList.add("U");
							continue;
						}

						if (parameters.get(key) == "null")
							jsonList.add(null);
						else
							jsonList.add(parameters.get(key));

					}
					Log.info("Parameters:"+parameters);
					Log.info("TestData:\n"+jsonList);
					//Log.info("DBData:\n"+listDatabase);
					status = ValidateResults.resultsvalidation(jsonList, DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
				}
				else
				{
					Log.error("Encounter with EncounterId="+parameters.get("encounterId")+", was not updated correctly-modified_by is not updated with logged in user's credentials.");
					Log.error("Expected:"+System.getProperty("LoggedInUserId")+", Actual:"+objIntModifiedBy.toString());
					softAssert.fail("Encounter with EncounterId="+parameters.get("encounterId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				}
			}
			else
			{
				Log.error("Encounter with EncounterId="+parameters.get("encounterId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				softAssert.fail("Encounter with EncounterId="+parameters.get("encounterId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}



}
