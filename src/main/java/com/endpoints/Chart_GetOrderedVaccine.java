package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetOrderedVaccine 
{



	public static Boolean chart_GetOrderedVaccine_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				
				String strQuery = "select top(1) "
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
						+ "IOV.created_by as createdBy,IOV.create_timestamp as createTimestamp,IOV.create_timestamp_tz as createTimestampTimezone,IOV.modified_by as modifiedByUserId, "
						+ "case when (IOV.modified_by IS NULL) then NULL else Concat(UM3.last_name,', ',UM3.first_name)end as modifiedByName,IOV.modify_timestamp as modifyTimestamp, "
						+ "IOV.modify_timestamp_tz as modifyTimestampTimezone,Case when IOV.override_invalid_dose_ind = 'N' then 'false' else 'true' end as overrideInvalidDose "
						+ "FROM imm_nor AS IMN "
						+ "INNER JOIN imm_order_vaccines AS IOV ON IMN.order_num = IOV.order_num "
						+ "INNER JOIN vw_fdb_all_vaccines ALLV ON IOV.cvx_code = ALLV.cvx  AND IOV.cpt4_code = ALLV.cpt  "
						+ "INNER JOIN revdcvx0 AS RECVXO on RECVXO.evd_cvx_cd = IOV.cvx_code "
						+ "INNER JOIN revdcpt0 AS RECPTO ON RECVXO.evd_cvx_cd = RECPTO.evd_cvx_cd "
						+ "LEFT OUTER JOIN ng_vaccines_xref AS NGX ON RECVXO.evd_cvx_cd = NGX.cvx AND RECPTO.evd_cpt_cd = NGX.cpt "
						+ "LEFT OUTER JOIN user_mstr AS UM1 ON IOV.administer_by = UM1.user_id "
						+ "LEFT OUTER JOIN user_mstr AS UM2 ON IOV.sign_off_person = UM2.user_id "
						+ "LEFT OUTER JOIN user_mstr AS UM3 ON IMN.modified_by = UM3.user_id "
						+ "WHERE IMN.person_id = '"+parameters.get("personId")+"' AND IMN.order_num = '"+parameters.get("orderId")+"' AND IOV.order_vaccine_id = '"+parameters.get("VaccineId")+"'";
				
				String[] jsonMetaData = {"id;GUID","personId;GUID","enterpriseId;Normal","practiceId;Normal","orderId;GUID","orderStatus;Normal","cvxCode;Normal","description;Normal",
						"recordSourceCode;Normal","recordSourceName;Normal","administerCptCode;Normal","cptCode;Normal","sequenceNumber;Normal","status;Normal","comment;Normal",
						"brandName;Normal","siteCode;Normal","site;Normal","sideCode;Normal","side;Normal","routeCode;Normal","route;Normal","unitsCode;Normal","units;Normal","lotNumber;Normal","expirationDate;Date_YYYYMMDD HH:MM:SS",
						"dose;Normal","mvxCode;Normal","manufacturerNumber;Normal","manufacturerName;Normal","notAdministeredReason;Normal","notAdministeredCode;Normal",
						"purchaseType;Normal","strength;Normal","administeredYear;Normal","administeredMonth;Normal","administeredDay;Normal","administeredTimestamp;Normal",
						"administeredByUserId;Normal","administeredByName;Normal","auditId","chargeId","adminChargeId","isException;Boolean","isCounselled;Boolean",
						"counselledCode;Normal","counselledUnit;Normal","counselChargeId;Normal","consentFrom;Normal","consentGivenTo;Normal","ndcId;Normal","isError;Boolean",
						"snomedImmunity;Normal","isVfc;Boolean","vfcCode;Normal","vfcDate;Normal","vfcDateTimezone;Normal","fundingSourceCode;Normal","simCode;Normal","signOffUserId;Normal",
						"signOffName;Normal","signOffDate;Normal","signOffDateTimezone;Normal","signoffComment;Normal","displayName;Normal","hideOnChart;Boolean","createdBy;Normal",
						"createTimestamp;Date_YYYYMMDD HH:MM:SS","createTimestampTimezone","modifiedByUserId;Normal","modifiedByName;Normal","modifyTimestamp;Date_YYYYMMDD HH:MM:SS","modifyTimestampTimezone",
						"overrideInvalidDose;Boolean"};
				
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
