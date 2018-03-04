package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Persons_GetDemographics 
{
	public static Boolean persons_GetDemographics_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "select	UPPER(CAST(p.person_id AS CHAR(36))) as id,p.first_name as firstName,p.last_name as lastName,p.middle_name as middleName,p.prefix as prefix,p.suffix as suffix,p.address_line_1 as addressLine1, "
	     				+"p.address_line_2 as addressLine2,p.city as city,p.state as state,p.zip as zip,p.home_phone as homePhone,p.sec_home_phone as secondaryHomePhone,p.day_phone as dayPhone,p.alt_phone as alternatePhone, "
	     				+"p.cell_phone as cellPhone,p.email_address as email,p.sex as sex,p.date_of_birth as dateOfBirth,p.ssn as socialSecurityNumber,p.primarycare_prov_name as primaryCareProviderFullName, "
	     				+"pm.first_name as primaryCareProviderFirstName,pm.last_name as primaryCareProviderLastName,pm.middle_name as primaryCareProviderMiddleInitial,p.marital_status as maritalStatus,"
	     				+" case when p.expired_ind='Y' then 'true' else 'false' end as isExpired "
	     				+"from person p left outer join provider_mstr pm on (p.primarycare_prov_id=pm.provider_id) "
	     				+"where p.person_id='"+parameters.get("personId")+"'"  ;
				
				
				
				String[] jsonMetaData = {"id","firstName","lastName","middleName","prefix","suffix","addressLine1","addressLine2","city","state","zip","homePhone","secondaryHomePhone",
						"dayPhone","alternatePhone","cellPhone","email","sex","dateOfBirth","socialSecurityNumber","primaryCareProviderFullName","primaryCareProviderFirstName","primaryCareProviderLastName",
						"primaryCareProviderMiddleInitial","maritalStatus","isExpired"};
				
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
