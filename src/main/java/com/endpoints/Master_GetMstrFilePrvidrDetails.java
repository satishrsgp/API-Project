package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Master_GetMstrFilePrvidrDetails
{

	public static Boolean master_GetMstrFilePrvidrDetails_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "SELECT pm.provider_id as id, ppm.enterprise_id as enterpriseId, ppm.practice_id as practiceId, "
						+ "pm.first_name as firstName, pm.last_name as lastName, pm.middle_name as middleName, "
						+ "pm.address_line_1 as providerAddressLine1, pm.address_line_2 as providerAddressLine2, "
						+ "pm.city as providerCity, pm.state as providerState, pm.zip as providerZip, pm.phone as providerPhone, "
						+ "pm.phone_ext as providerPhoneExtension, pm.fax as providerFax, pm.degree as degree, ppm.dea_nbr as deaNumber, "
						+ "pm.email_address as email, pm.national_provider_id as nationalProviderId, pm.other_lic_id as otherLicenseId, "
						+ "pm.other_lic_desc as otherLicenseDescription, ppm.lic_nbr as stateLicenseNumber, pm.description as description, "
						+ "CASE pm.delete_ind WHEN 'n' THEN 'false' END AS isDeleted, pm.note as note, pm.ssn as ssn, "
						+ "pm.salutory_name as salutoryName, pm.hosp_affiliations as hospitalAffiliations, pm.mobile_phone as mobilePhone, "
						+ "pm.home_addr_line_1 as homeAddressLine1, pm.home_addr_line_2 as homeAddressLine2, pm.home_city as homeCity, "
						+ "pm.home_state as homeState, pm.home_zip as homeZip, pm.home_phone as homePhone, pm.home_fax as homeFax, "
						+ "pm.fax_extras as faxExtras, pm.default_tax_id as defaultTaxId, "
						+ "CASE ppm.enable_paq_ind WHEN 'Y' THEN 'true' else 'false' END AS isPaqEnabled "
						+ "FROM provider_mstr pm INNER JOIN provider_practice_mstr ppm ON ppm.provider_id = pm.provider_id "
						+ "WHERE ppm.provider_id = '"+parameters.get("providerId")+"' ";
				
				String[] jsonMetaData = {"id","enterpriseId","practiceId","firstName","lastName","middleName","providerAddressLine1",
				"providerAddressLine2","providerCity","providerState"	,"providerZip","providerPhone","providerPhoneExtension","providerFax", 
				"degree", "deaNumber", "email", "nationalProviderId", "otherLicenseId", "otherLicenseDescription", "stateLicenseNumber", 
				"description", "isDeleted", "note", "ssn", "salutoryName", "hospitalAffiliations", "mobilePhone", "homeAddressLine1", "homeAddressLine2", 
				"homeCity", "homeState", "homeZip", "homePhone", "homeFax", "faxExtras", "defaultTaxId", "isPaqEnabled"};
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..id", jsonList);
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
