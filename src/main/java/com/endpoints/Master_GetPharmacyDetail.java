package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Master_GetPharmacyDetail
{

	/*
	 * ********************************************************************************
	 * Class Name						:Master_GetPharmacyDetail
	 * Created By						: 
	 * Created Date						: 
	 * Modified Date					:
	 * Modified By						:
	 * Parameters						: 
	 * Test Case ID						:
	 * Test Case Author					: 
	 * Test Case Modified By			:
	 * Test Case Reviewed By			:
	 * Purpose							: Route will fetch the allergy chart details
	 ***********************************************************************************
	 */

	public static Boolean master_GetPharmacyDetail_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		//System.out.println(strResponse);
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{ 
				String strQuery = "select"
						+" UPPER(CAST(pm.pharmacy_id AS CHAR(36))) as pharmacyId,pm.name as name, pm.address_line_1 as addressLine1,pm.address_line_2 as addressLine2,pm.city as city,pm.state as state,pm.zip as zip,"
						+" pm.country_id as countryId,pm.phone as phone,pm.alternate_phone as alternatePhone,pm.alt_phone_desc as altPhoneDesc,pm.fax as fax,pm.chief_pharm_name as chiefPharmacyName,"
						+" case when pm.delivery_ind='Y' then 'true' else 'false' end as isDelivered,pm.hrs_operation,case when pm.rx_by_fax_ind='Y' then 'true' else 'false' end as isPrescribedByFax,"
						+" case when pm.dial_area_code_ind='Y' then 'true' else 'false' end as hasDialAreaCode,pm.fax_extras as faxExtras,pm.note as note,case when pm.delete_ind='Y' then 'true' else 'false' end as isDeleted,"
						+" pm.store_number as storeNumber,case when pm.active_erx_ind='Y' then 'true' else 'false' end as hasActivePrescription,case when pm.mail_order_ind='Y' then 'true' else 'false' end as hasMailOrder,"
						+" pm.rxhub_service_level as rxHubServiceLevel,pm.rxhub_enroll_date as rxHubEnrollDate,pm.rxhub_unenroll_date as rxHubUnenrollDate,"
						+" epx.ncpdp_id as ncpdpId,epm.service_level as serviceLevel,epm.surescripts_partner_account as surescriptsPartnerAccount,epm.surescripts_enroll_date as surescriptsEnrollDate,"
						+" epm.surescripts_unenroll_date as surescriptsUnenrollDate,epm.pharmacy_model as pharmacyType"
						+" from"
						+" pharmacy_mstr pm inner join erx_pharmacy_xref epx on pm.pharmacy_id = epx.pharmacy_id  inner join erx_pharmacy_mstr epm  on epm.ncpdp_id = epx.ncpdp_id"
						+" where pm.pharmacy_id='"+parameters.get("pharmacyId")+"'";
				
				String[] jsonMetaData = {"pharmacyId;GUID","name;Normal","addressLine1;Normal","addressLine2;Normal","city;Normal","state;Normal","zip;Normal","countryId","phone;Normal","alternatePhone;Normal","altPhoneDesc;Normal","fax;Normal","chiefPharmacyName;Normal","isDelivered;Boolean","hoursOfOperation;Normal","isPrescribedByFax;Boolean","hasDialAreaCode;Boolean","faxExtras;Normal","note;Normal","isDeleted;Boolean","storeNumber;Normal","hasActivePrescription;Boolean","hasMailOrder;Boolean","rxHubServiceLevel;Normal","rxHubEnrollDate","rxHubUnenrollDate","ncpdpId","serviceLevel","surescriptsPartnerAccount","surescriptsEnrollDate;Date_YYYYMMDD HH:MM:SS","surescriptsUnenrollDate;Date_YYYYMMDD HH:MM:SS", "pharmacyType"};

				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..pharmacyId", jsonList);
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
