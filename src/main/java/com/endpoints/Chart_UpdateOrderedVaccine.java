package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_UpdateOrderedVaccine 
{
	public static Boolean chart_UpdateOrderedVaccine_Test(HashMap<String, String> parameters, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strSQLQuery = "Select top(1) datediff(second,IOV.create_timestamp,IOV.modify_timestamp) as diff,IOV.modified_by as mb "
					+ "from imm_nor AS IMN "
					+ "INNER JOIN imm_order_vaccines AS IOV ON IMN.order_num = IOV.order_num  "
					+ "INNER JOIN vw_fdb_all_vaccines ALLV ON IOV.cvx_code = ALLV.cvx  AND IOV.cpt4_code = ALLV.cpt  "
					+ "INNER JOIN revdcvx0 AS RECVXO on RECVXO.evd_cvx_cd = IOV.cvx_code "
					+ "INNER JOIN revdcpt0 AS RECPTO ON RECVXO.evd_cvx_cd = RECPTO.evd_cvx_cd  "
					+ "LEFT OUTER JOIN ng_vaccines_xref AS NGX ON RECVXO.evd_cvx_cd = NGX.cvx AND RECPTO.evd_cpt_cd = NGX.cpt "
					+ "LEFT OUTER JOIN user_mstr AS UM1 ON IOV.administer_by = UM1.user_id "
					+ "LEFT OUTER JOIN user_mstr AS UM2 ON IOV.sign_off_person = UM2.user_id  "
					+ "LEFT OUTER JOIN user_mstr AS UM3 ON IMN.modified_by = UM3.user_id  "
					+ "WHERE IMN.person_id = '"+parameters.get("personId")+"' AND "
					+ "IMN.order_num = '"+parameters.get("orderId")+"' AND  "
					+ "IOV.order_vaccine_id = '"+parameters.get("VaccineId")+"'  "
					+ "order by IOV.create_timestamp desc";
					
			Object objIntDateDiff = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "diff");
			Object objIntModifiedBy = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "mb");
			if(Integer.valueOf(objIntDateDiff.toString()) > 0)
			{
				if(Objects.equals(objIntModifiedBy.toString(), System.getProperty("LoggedInUserId")))
				{
					String strQuery1 = "select top(1) "
							+ "IOV.order_vaccine_id as id,IMN.person_id as personId,IMN.enterprise_id as enterpriseId,IMN.practice_id as practiceId,IOV.order_num as orderId, "
							+ "IMN.ngn_status as orderStatus,IOV.cvx_code as cvxCode,IOV.vaccine_desc as description,IOV.record_source as recordSourceCode, "
							+ "case IOV.record_source  " 
							+ "when '00' then 'New immunization record' "
							+ "when '01' then 'Historical information - source unspecified' "
							+ "when '02' then 'Historical information - from other provider' "
							+ "when '03' then 'Historical information - from parent''''s written record'"
							+ "when '04' then 'Historical information - from parent''''s recall' "
							+ "when '05' then 'Historical information - from other registry' "
							+ "when '06' then 'Historical information - from birth certificate' "
							+ "when '07' then 'Historical information - from school record' "
							+ "when '08' then 'Historical information - from public agency' "
							+ "end as recordSourceName, "
							+ "IOV.administer_cpt4_code as administerCptCode,IOV.cpt4_code as cptCode,IOV.seq_nbr as sequenceNumber,IOV.vaccine_status as status, "
							+ "IOV.vaccine_comment as comment,IOV.brand_name as brandName,IOV.site_code as siteCode,IOV.site as site,IOV.side_code as sideCode,IOV.side as side, "
							+ "IOV.route_code as routeCode,IOV.route as route,IOV.units_code as unitsCode,IOV.units as units,IOV.lot_num as lotNumber,IOV.expiration_date as expirationDate, "
							+ "IOV.dose as dose,IOV.mvx_code as mvxCode,IOV.manufacturer_nbr as manufacturerNumber,IOV.manufacturer_name as manufacturerName,IOV.not_administered_reason as notAdministeredReason, "
							+ "IOV.not_administered_code as notAdministeredCode,IOV.purchase_type as purchaseType,IOV.strength as strength,IOV.administer_year as administeredYear, "
							+ "IOV.administer_month as administeredMonth,IOV.administer_day as administeredDay,IOV.administer_time as administeredTimestamp,IOV.administer_by as administeredByUserId, "
							+ "case when (IOV.administer_by IS NULL) then NULL else concat(UM1.last_name,', ',UM1.first_name) end as administeredByName, "
							+ "IOV.audit_id as auditId,IOV.charge_id as chargeId,IOV.admin_charge_id as adminChargeId, "
							+ "case when IOV.exception_ind = 'N' then 'false' else 'true' end  as isException, "
							+ "case when IOV.counsel_ind = 'N' then 'false' else 'true' end  as isCounselled, "
							+ "IOV.counselled_code as counselledCode,IOV.counselled_unit as counselledUnit,IOV.councel_charge_id as counselChargeId,IOV.concent_from as consentFrom, "
							+ "IOV.concent_given_to as consentGivenTo,IOV.ndc_id as ndcId, "
							+ "case when IOV.error_ind = 'N' then 'false' else 'true' end as isError,IOV.snomed_immunity as snomedImmunity, "
							+ "case when IOV.vfc_ind = 'N' then 'false' else 'true' end  as isVfc,IOV.vfc_code as vfcCode, "
							+ "IOV.vfc_date as vfcDate,IOV.vfc_date_tz as vfcDateTimezone,IOV.funding_src_code as fundingSourceCode,IOV.sim_code as simCode,IMN.sign_off_person as signOffUserId, "
							+ "case when (IOV.sign_off_person is NULL) then NULL else Concat(UM2.last_name,', ',UM2.first_name) end as signOffName, "
							+ "IOV.sign_off_date as signOffDate,IOV.sign_off_date_tz as signOffDateTimezone,IOV.signoff_comment as signoffComment, "
							+ "case when ALLV.display_name != 'NULL'  then ALLV.display_name else ALLV.cvx_vaccine_name end as displayName, "
							+ "case when ALLV.hide_on_chart_ind = 'N' then 'false' else 'true'end  as hideOnChart, "
							/*+ "IOV.created_by as createdBy,IOV.create_timestamp as createTimestamp,IOV.create_timestamp_tz as createTimestampTimezone,IOV.modified_by as modifiedByUserId, "
							+ "case when (IOV.modified_by IS NULL) then NULL else Concat(UM3.last_name,', ',UM3.first_name)end as modifiedByName,IOV.modify_timestamp as modifyTimestamp, "
							+ "IOV.modify_timestamp_tz as modifyTimestampTimezone, "*/
							+ "Case when IOV.override_invalid_dose_ind = 'N' then 'false' else 'true' end as overrideInvalidDose "
							+ "FROM imm_nor AS IMN "
							+ "INNER JOIN imm_order_vaccines AS IOV ON IMN.order_num = IOV.order_num "
							+ "INNER JOIN vw_fdb_all_vaccines ALLV ON IOV.cvx_code = ALLV.cvx  AND IOV.cpt4_code = ALLV.cpt  "
							+ "INNER JOIN revdcvx0 AS RECVXO on RECVXO.evd_cvx_cd = IOV.cvx_code "
							+ "INNER JOIN revdcpt0 AS RECPTO ON RECVXO.evd_cvx_cd = RECPTO.evd_cvx_cd "
							+ "LEFT OUTER JOIN ng_vaccines_xref AS NGX ON RECVXO.evd_cvx_cd = NGX.cvx AND RECPTO.evd_cpt_cd = NGX.cpt "
							+ "LEFT OUTER JOIN user_mstr AS UM1 ON IOV.administer_by = UM1.user_id "
							+ "LEFT OUTER JOIN user_mstr AS UM2 ON IOV.sign_off_person = UM2.user_id "
							+ "LEFT OUTER JOIN user_mstr AS UM3 ON IMN.modified_by = UM3.user_id "
							+ "WHERE IMN.person_id = '"+parameters.get("personId")+"' AND IMN.order_num = '"+parameters.get("orderId")+"' AND IOV.order_vaccine_id = '"+parameters.get("VaccineId")+"'"
							+ "order by IOV.modify_timestamp desc";
					//ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
					ArrayList<Object> listTestData  = new ArrayList<>();
					listTestData.add(Objects.equals(parameters.get("VaccineId"), "") ? null: parameters.get("VaccineId").toString().toUpperCase());
					listTestData.add(Objects.equals(parameters.get("personId"), "") ? null: parameters.get("personId").toString().toUpperCase());
					listTestData.add(Objects.equals(parameters.get("enterpriseId"), "") ? null: parameters.get("enterpriseId"));
					listTestData.add(Objects.equals(parameters.get("practiceId"), "") ? null: parameters.get("practiceId"));
					listTestData.add(Objects.equals(parameters.get("orderId"), "") ? null: parameters.get("orderId").toString().toUpperCase());
					listTestData.add(Objects.equals(parameters.get("orderStatus"), "") ? null: parameters.get("orderStatus"));
					listTestData.add(Objects.equals(parameters.get("cvxCode"), "") ? null: parameters.get("cvxCode"));
					listTestData.add(Objects.equals(parameters.get("description"), "") ? null: parameters.get("description"));
					listTestData.add(Objects.equals(parameters.get("recordSourceCode"), "") ? null: parameters.get("recordSourceCode"));
					listTestData.add(Objects.equals(parameters.get("recordSourceName"), "") ? null: parameters.get("recordSourceName"));
					listTestData.add(Objects.equals(parameters.get("administerCptCode"), "") ? null: parameters.get("administerCptCode"));
					listTestData.add(Objects.equals(parameters.get("cptCode"), "") ? null: parameters.get("cptCode"));
					listTestData.add(Objects.equals(parameters.get("sequenceNumber"), "") ? null: parameters.get("sequenceNumber"));
					listTestData.add(Objects.equals(parameters.get("status"), "") ? null: parameters.get("status"));
					listTestData.add(Objects.equals(parameters.get("comment"), "") ? null: parameters.get("comment"));
					listTestData.add(Objects.equals(parameters.get("brandName"), "") ? null: parameters.get("brandName"));
					listTestData.add(Objects.equals(parameters.get("siteCode"), "") ? null: parameters.get("siteCode"));
					listTestData.add(Objects.equals(parameters.get("site"), "") ? null: parameters.get("site"));										
					listTestData.add(Objects.equals(parameters.get("sideCode"), "null") ? null: parameters.get("sideCode"));
					listTestData.add(Objects.equals(parameters.get("side"), "null") ? null: parameters.get("side"));
					listTestData.add(Objects.equals(parameters.get("routeCode"), "") ? null: parameters.get("routeCode"));
					listTestData.add(Objects.equals(parameters.get("route"), "") ? null: parameters.get("route"));
					listTestData.add(Objects.equals(parameters.get("unitsCode"), "") ? null: parameters.get("unitsCode"));
					listTestData.add(Objects.equals(parameters.get("units"), "") ? null: parameters.get("units"));
					listTestData.add(Objects.equals(parameters.get("lotNumber"), "") ? null: parameters.get("lotNumber"));
					listTestData.add(Objects.equals(parameters.get("expirationDate"), "") ? null : (parameters.get("expirationDate").toString().replace("T", " ")));
					listTestData.add(Objects.equals(parameters.get("dose"), "") ? null: parameters.get("dose").toString()+".000");
					listTestData.add(Objects.equals(parameters.get("mvxCode"), "") ? null: parameters.get("mvxCode"));
					listTestData.add(Objects.equals(parameters.get("manufacturerNumber"), "") ? null: parameters.get("manufacturerNumber"));
					listTestData.add(Objects.equals(parameters.get("manufacturerName"), "") ? null: parameters.get("manufacturerName"));
					listTestData.add(Objects.equals(parameters.get("notAdministeredReason"), "null") ? null: parameters.get("notAdministeredReason"));
					listTestData.add(Objects.equals(parameters.get("notAdministeredCode"), "null") ? null: parameters.get("notAdministeredCode"));
					listTestData.add(Objects.equals(parameters.get("purchaseType"), "null") ? null: parameters.get("purchaseType"));
					listTestData.add(Objects.equals(parameters.get("strength"), "") ? null: parameters.get("strength"));
					listTestData.add(Objects.equals(parameters.get("administeredYear"), "") ? null: parameters.get("administeredYear"));
					listTestData.add(Objects.equals(parameters.get("administeredMonth"), "") ? null: parameters.get("administeredMonth"));
					listTestData.add(Objects.equals(parameters.get("administeredDay"), "") ? null: parameters.get("administeredDay"));
					listTestData.add(Objects.equals(parameters.get("administeredTimestamp"), "") ? null: parameters.get("administeredTimestamp"));
					listTestData.add(Objects.equals(parameters.get("administeredByUserId"), "") ? null: parameters.get("administeredByUserId"));
					listTestData.add(Objects.equals(parameters.get("administeredByName"), "") ? null: parameters.get("administeredByName"));
					listTestData.add(Objects.equals(parameters.get("auditId"), "") ? null: parameters.get("auditId").toString().toUpperCase());
					listTestData.add(Objects.equals(parameters.get("chargeId"), "") ? null: parameters.get("chargeId").toString().toUpperCase());
					listTestData.add(Objects.equals(parameters.get("adminChargeId"), "") ? null: parameters.get("adminChargeId").toString().toUpperCase());
					listTestData.add(Objects.equals(parameters.get("isException"), "") ? null: parameters.get("isException"));
					listTestData.add(Objects.equals(parameters.get("isCounselled"), "") ? null: parameters.get("isCounselled"));
					listTestData.add(Objects.equals(parameters.get("counselledCode"), "null") ? null: parameters.get("counselledCode"));
					listTestData.add(Objects.equals(parameters.get("counselledUnit"), "") ? null: parameters.get("counselledUnit"));
					listTestData.add(Objects.equals(parameters.get("counselChargeId"), "") ? null: parameters.get("counselChargeId"));
					listTestData.add(Objects.equals(parameters.get("consentFrom"), "") ? null: parameters.get("consentFrom"));
					listTestData.add(Objects.equals(parameters.get("consentGivenTo"), "") ? null: parameters.get("consentGivenTo"));
					listTestData.add(Objects.equals(parameters.get("ndcId"), "") ? null: parameters.get("ndcId"));
					listTestData.add(Objects.equals(parameters.get("isError"), "") ? null: parameters.get("isError"));
					listTestData.add(Objects.equals(parameters.get("snomedImmunity"), "") ? null: parameters.get("snomedImmunity"));
					listTestData.add(Objects.equals(parameters.get("isVfc"), "") ? null: parameters.get("isVfc"));
					listTestData.add(Objects.equals(parameters.get("vfcCode"), "null") ? null: parameters.get("vfcCode"));
					listTestData.add(Objects.equals(parameters.get("vfcDate"), "null") ? null: parameters.get("vfcDate"));
					listTestData.add(Objects.equals(parameters.get("vfcDateTimezone"), "null") ? null: parameters.get("vfcDateTimezone"));
					listTestData.add(Objects.equals(parameters.get("fundingSourceCode"), "null") ? null: parameters.get("fundingSourceCode"));
					listTestData.add(Objects.equals(parameters.get("simCode"), "") ? null: parameters.get("simCode"));
					listTestData.add(Objects.equals(parameters.get("signOffUserId"), "null") ? null: parameters.get("signOffUserId"));
					listTestData.add(Objects.equals(parameters.get("signOffName"), "null") ? null: parameters.get("signOffName"));
					listTestData.add(Objects.equals(parameters.get("signOffDate"), "null") ? null: parameters.get("signOffDate"));
					listTestData.add(Objects.equals(parameters.get("signOffDateTimezone"), "null") ? null: parameters.get("signOffDateTimezone"));
					listTestData.add(Objects.equals(parameters.get("signoffComment"), "null") ? null: parameters.get("signoffComment"));
					listTestData.add(Objects.equals(parameters.get("displayName"), "") ? null: parameters.get("displayName"));
					listTestData.add(Objects.equals(parameters.get("hideOnChart"), "") ? null: parameters.get("hideOnChart"));
					listTestData.add(Objects.equals(parameters.get("overrideInvalidDose"), "") ? null: parameters.get("overrideInvalidDose"));
					/*listTestData.add(Objects.equals(parameters.get("createdBy"), "") ? null: parameters.get("createdBy"));
					listTestData.add(Objects.equals(parameters.get("createTimestamp"), "") ? null: parameters.get("createTimestamp"));
					listTestData.add(Objects.equals(parameters.get("createTimestampTimezone"), "") ? null: parameters.get("createTimestampTimezone"));
					listTestData.add(Objects.equals(parameters.get("modifiedByUserId"), "") ? null: parameters.get("modifiedByUserId"));
					listTestData.add(Objects.equals(parameters.get("modifiedByName"), "") ? null: parameters.get("modifiedByName"));
					listTestData.add(Objects.equals(parameters.get("modifyTimestamp"), "") ? null: parameters.get("modifyTimestamp"));
					listTestData.add(Objects.equals(parameters.get("modifyTimestampTimezone"), "") ? null: parameters.get("modifyTimestampTimezone"));
					*/
					Log.info("Parameters:"+parameters);
					Log.info("TestData:\n"+listTestData);
					//Log.info("DBData:\n"+listDatabase);
					status = ValidateResults.resultsvalidation(listTestData, DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
				}
				else
				{
					Log.error("Vaccine with VaccineId="+parameters.get("VaccineId")+", was not updated correctly-modified_by is not updated with logged in user's credentials.");
					Log.error("Expected:"+System.getProperty("LoggedInUserId")+", Actual:"+objIntModifiedBy.toString());
					softAssert.fail("Vaccine with VaccineId="+parameters.get("VaccineId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				}
			}
			else
			{
				Log.error("Vaccine with VaccineId="+parameters.get("VaccineId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				softAssert.fail("Vaccine with VaccineId="+parameters.get("VaccineId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}


}
