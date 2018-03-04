package com.endpoints;

/*
 * ********************************************************************
 * Class Name						: Chart_GetPatientDevice
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
import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetPatientDevice
{
	public static Boolean chart_GetPatientDevice_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			String strQuery = "SELECT pid.implantable_device_id AS id, pid.person_id AS personId, pid.device_identifier AS deviceIdentifier, "
					+ "pid.universal_device_identifier AS universalDeviceIdentifier, pid.snomed_CT_code AS snoMedCtCode, "
					+ "pid.snomed_ct_description AS snoMedCtDescription, pid.lot_batch AS lotOrBatchNumber, pid.serial_number AS serialNumber, "
					+ "pid.manufacturing_date AS manufacturingDate, pid.expiration_date AS expirationDate, "
					+ "CASE WHEN pid.delete_ind = 'N' THEN 'false' ELSE 'true' END AS isDeleted, pid.assigning_authority_oid as assigningAuthorityOid, "
					+ "pid.assigning_authority as assigningAuthority FROM patient_implantable_device pid "
					+ "WHERE pid.person_id = '"+parameters.get("personId")+"' AND "
					+ "pid.implantable_device_id = '"+parameters.get("deviceId")+"' ";

			String[] jsonMetaData = {"id","personId","deviceIdentifier","universalDeviceIdentifier","snoMedCtCode","snoMedCtDescription","lotOrBatchNumber","serialNumber","manufacturingDate","expirationDate",
					"isDeleted", "assigningAuthorityOid", "assigningAuthority"};

			try
			{
				ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
				Log.info("JSON LIST is :\n" +jsonList);
				status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..id", jsonList);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				Log.info("Error in Test Class and the detail are : \n" + e.getMessage());
				softAssert.fail("Error in Test Script please look at logs");
			}
		}
		else
		{
			status = true;
		}
		return status;
	}
}