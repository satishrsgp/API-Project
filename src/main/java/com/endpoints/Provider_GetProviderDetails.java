/**
 * API 1.x route_Abhishek
 */

package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Provider_GetProviderDetails {

	public static Boolean provider_GetProviderDetails(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery="select pm.provider_id,ppm.enterprise_id,ppm.practice_id,pm.first_name,pm.last_name,pm.middle_name,pm.address_line_1,pm.address_line_2,pm.city,pm.state,pm.zip,pm.phone,pm.phone_ext,pm.fax,pm.degree, "
										+ "ppm.dea_nbr,pm.email_address,pm.national_provider_id,pm.other_lic_id,pm.other_lic_desc,ppm.lic_nbr,pm.description, "
										+ "case pm.delete_ind when 'n' then 'false' end as delete_ind, "
										+ " pm.note,pm.ssn,pm.salutory_name,pm.hosp_affiliations,pm.mobile_phone,pm.home_addr_line_1,pm.home_addr_line_2, "
										+ "pm.home_city,pm.home_state,pm.home_zip,pm.home_phone,pm.home_fax,pm.fax_extras,pm.default_tax_id,case ppm.enable_paq_ind  when 'Y' then 'true' else 'false' end as enable_paq_ind  "
										+ "from provider_mstr pm inner join provider_practice_mstr ppm on ppm.provider_id=pm.provider_id where ppm.provider_id='"+parameters.get("providerId")+"'";

				String[] jsonMetaData = {"id","enterpriseId","practiceId","firstName","lastName","middleName","providerAddressLine1","providerAddressLine2",
						"providerCity","providerState","providerZip","providerPhone","providerPhoneExtension","providerFax","degree","deaNumber","email","nationalProviderId","otherLicenseId","otherLicenseDescription","stateLicenseNumber",
						"description","isDeleted","note","ssn","salutoryName","hospitalAffiliations","mobilePhone","homeAddressLine1","homeAddressLine2","homeCity","homeState","homeZip","homePhone","homeFax","faxExtras","defaultTaxId","isPaqEnabled"};

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
