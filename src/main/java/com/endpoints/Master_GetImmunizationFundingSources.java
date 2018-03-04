package com.endpoints;

import java.util.ArrayList;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Master_GetImmunizationFundingSources
{
	public static Boolean master_GetImmunizationFundingSources_Test(String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery= null;
				if(strResponse.contains("$skip="))
				{
					strQuery = "select top 25 funding_src_code as fundingSourceCode,external_system_id as externalSystemId,funding_src_desc as fundingSourceDescription,coding_system as codingSystem, "
							+"case when delete_ind='N' then 'false' else 'true' end as isDeleted from imm_funding_src_xref where delete_ind = 'N' order by funding_src_desc,coding_system,external_system_id,funding_src_code,delete_ind";
				}
				else
				{
					strQuery = "select funding_src_code as fundingSourceCode,external_system_id as externalSystemId,funding_src_desc as fundingSourceDescription,coding_system as codingSystem, "
							+"case when delete_ind='N' then 'false' else 'true' end as isDeleted from imm_funding_src_xref where delete_ind = 'N' order by funding_src_desc,coding_system,external_system_id,funding_src_code,delete_ind";
				}
				String[] jsonMetaData = {"fundingSourceCode","externalSystemId","fundingSourceDescription","codingSystem","isDeleted"};

				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..fundingSourceCode", jsonList);
				} 
				catch (Exception e)
				{
					e.printStackTrace();
					Log.info("Error in Test Class and the detail are : \n" + e.getMessage());
					softAssert.fail("Error in Test Script please look at logs");
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