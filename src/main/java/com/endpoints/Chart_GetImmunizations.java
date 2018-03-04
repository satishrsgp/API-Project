package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetImmunizations {


	public static Boolean chart_GetImmunizations_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				
				String strQuery = "Select "
						+ "IMV.order_vaccine_id as id,IMN.person_id as personId,IMN.enterprise_id as enterpriseId,IMN.practice_id as practiceId,IMV.order_num as orderId,IMN.ngn_status as orderStatus, "
						+ "IMV.cvx_code as cvxCode,IMV.vaccine_desc as description,IMV.record_source as recordSourceCode, "
						+ "case IMV.record_source " 
						+ "when '00' then 'New immunization record' "
						+ "when '01' then 'Historical information - source unspecified' "
						+ "when '02' then 'Historical information - from other provider' "
						+ "when '03' then 'Historical information - from parent''''s written record' "
						+ "when '04' then 'Historical information - from parent''''s recall' "
						+ "when '05' then 'Historical information - from other registry' "
						+ "when '06' then 'Historical information - from birth certificate' "
						+ "when '07' then 'Historical information - from school record' " 
						+ "when '08' then 'Historical information - from public agency' end as recordSourceName, " 
						+ "IMV.administer_cpt4_code as administerCptCode, "
						+ "IMV.cpt4_code as cptCode,IMV.seq_nbr as sequenceNumber,IMV.vaccine_status as status,IMV.vaccine_comment as comment,IMV.brand_name as brandName,IMV.site_code as siteCode,IMV.site as site, "
						+ "IMV.side_code as sideCode,IMV.side as side,IMV.route_code as routeCode,IMV.route as route,IMV.units_code as unitsCode,IMV.units as units,IMV.lot_num as lotNumber,IMV.expiration_date as expirationDate, "
						+ "IMV.dose as dose,IMV.mvx_code as mvxCode,IMV.manufacturer_nbr as manufacturerNumber,IMV.manufacturer_name as manufacturerName,IMV.not_administered_reason as notAdministeredReason, "
						+ "IMV.not_administered_code as notAdministeredCode,IMV.purchase_type as purchaseType,IMV.strength as strength,IMV.administer_year as administeredYear,IMV.administer_month as administeredMonth, "
						+ "IMV.administer_day as administeredDay,IMV.administer_time as administeredTimestamp,IMV.administer_by as administeredByUserId, "
						+ "case when IMV.administer_by is NULL then null else concat( UM2.last_name ,', ', UM2.first_name ) end as administeredByName, "
						+ "IMV.audit_id as auditId,IMV.charge_id as chargeId,IMV.admin_charge_id as adminChargeId,case when IMV.exception_ind = 'N' then 'false' else 'true' end as isException, "
						+ "case when IMV.counsel_ind = 'N' then 'false' else 'true' end as isCounselled,IMV.counselled_code as counselledCode, "
						+ "IMV.counselled_unit as counselledUnit,IMV.councel_charge_id as counselChargeId,IMV.concent_from as consentFrom,IMV.concent_given_to as consentGivenTo,IMV.ndc_id as ndcId, "
						+ "case when IMV.error_ind = 'N' then 'false' else 'true' end as isError, "
						+ "IMV.snomed_immunity as snomedImmunity, case when IMV.vfc_ind = 'N' then 'false' else 'true' end as isVfc,IMV.vfc_code asvfcCode, IMV.vfc_date as vfcDate,IMV.vfc_date_tz as vfcDateTimezone, IMV.funding_src_code as fundingSourceCode, "
						+ "IMV.sim_code as simCode,IMN.sign_off_person as signOffUserId, "
						+ "case when IMN.sign_off_person is null then null else concat( UM1.last_name ,', ', UM1.first_name ) end as signOffName, "
						+ "IMV.sign_off_date as signOffDate,IMV.sign_off_date_tz as signOffDateTimezone, "
						+ "IMV.signoff_comment as signoffComment, "
						//+ "case when ALLV.display_name != 'NULL'  then ALLV.display_name else ALLV.cvx_vaccine_name end as displayName, "
						//+ "case when ALLV.hide_on_chart_ind = 'N' then 'false' else 'true' end  as hideOnChart, "
						+ "IMV.created_by as createdBy,IMV.create_timestamp as createTimestamp, "
						+ "IMV.create_timestamp_tz as createTimestampTimezone,IMV.modified_by as modifiedByUserId, "
						+ "Case when IMV.modified_by is null  then null else Concat(UM3.last_name ,', ', UM3.first_name) end as modifiedByName, "
						+ "IMV.modify_timestamp as modifyTimestamp,IMV.modify_timestamp_tz as modifyTimestampTimezone, "
						+ "case when IMV.override_invalid_dose_ind = 'N' then 'false' else 'true' end as overrideInvalidDose "
						+ "From  imm_nor IMN "
						+ "INNER JOIN imm_order_vaccines IMV ON IMV.order_num = IMN.order_num  "
						+ "LEFT JOIN user_mstr AS UM2 ON IMV.administer_by = UM2.user_id  "
						+ "LEFT JOIN user_mstr AS UM1 ON IMN.sign_off_person = UM1.user_id  "
						+ "LEFT JOIN user_mstr AS UM3 ON IMV.modified_by = UM3.user_id  "
						+ "WHERE IMV.cvx_code IN "
						+ "( "
						+ "    SELECT DISTINCT V.cvx FROM revdcvx0 CX "
						+ "LEFT JOIN revdcpt0 CP ON CX.evd_cvx_cd = CP.evd_cvx_cd "
						+ "LEFT JOIN ng_vaccines_xref V ON V.cvx = CX.evd_cvx_cd  "
						+ "UNION "
						+ "SELECT DISTINCT V.cvx FROM revdcvx0 CX "
						+ "LEFT JOIN revdcpt0 CP ON CX.evd_cvx_cd = CP.evd_cvx_cd "
						+ "LEFT JOIN ng_vaccines_xref V ON V.cvx = CX.evd_cvx_cd AND V.cpt = CP.evd_cpt_cd  "
						+ ") "
						+ "AND IMN.person_id = '"+parameters.get("personId")+"' AND IMN.delete_ind = 'N'  "
						+ "Order by IMV.order_vaccine_id asc ";
						//+ "From imm_order_vaccines IMV INNER JOIN imm_nor IMN ON IMV.order_num = IMN.order_num "
						//+ "INNER JOIN vw_fdb_all_vaccines ALLV ON IMV.cvx_code = ALLV.cvx  AND IMV.cpt4_code = ALLV.cpt "
						//+ "LEFT OUTER JOIN user_mstr AS UM1 ON IMN.sign_off_person = UM1.user_id "
						//+ "LEFT OUTER JOIN user_mstr AS UM2 ON IMV.administer_by = UM2.user_id "
						//+ "LEFT OUTER JOIN user_mstr AS UM3 ON IMV.modified_by = UM3.user_id "
						//+ "Where IMN.person_id = '"+parameters.get("personId")+"'";

						
				
				String[] jsonMetaData = {"id;GUID","personId;GUID","enterpriseId;Normal","practiceId;Normal","orderId;GUID","orderStatus;Normal","cvxCode;Normal",
						"description;Normal","recordSourceCode;Normal","recordSourceName;Normal","administerCptCode;Normal","cptCode;Normal","sequenceNumber;Normal",
						"status;Normal","comment;Normal","brandName;Normal","siteCode;Normal","site;Normal","sideCode;Normal","side;Normal","routeCode;Normal",
						"route;Normal","unitsCode;Normal","units;Normal","lotNumber;Normal","expirationDate;Date_YYYYMMDD HH:MM:SS","dose;Normal","mvxCode;Normal",
						"manufacturerNumber;Normal","manufacturerName;Normal","notAdministeredReason;Normal","notAdministeredCode;Normal","purchaseType;Normal",
						"strength;Normal","administeredYear;Normal","administeredMonth;Normal","administeredDay;Normal","administeredTimestamp;Normal","administeredByUserId;Normal",
						"administeredByName;Normal","auditId","chargeId","adminChargeId","isException;Boolean","isCounselled;Boolean",
						"counselledCode;Normal","counselledUnit;Normal","counselChargeId;Normal","consentFrom;Normal","consentGivenTo;Normal","ndcId;Normal","isError;Boolean",
						"snomedImmunity;Normal","isVfc;Boolean","vfcCode;Normal","vfcDate;Date_YYYYMMDD HH:MM:SS","vfcDateTimezone","fundingSourceCode;Normal",
						"simCode;Normal","signOffUserId;Normal","signOffName;Normal","signOffDate;Date_YYYYMMDD HH:MM:SS","signOffDateTimezone","signoffComment;Normal",
						//"displayName;Normal",
						//"hideOnChart;Boolean",
						"createdBy;Normal","createTimestamp;Date_YYYYMMDD HH:MM:SS","createTimestampTimezone","modifiedByUserId;Normal",
						"modifiedByName;Normal","modifyTimestamp;Date_YYYYMMDD HH:MM:SS","modifyTimestampTimezone","overrideInvalidDose;Boolean"};
				
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
