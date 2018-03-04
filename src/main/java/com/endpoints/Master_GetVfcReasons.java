package com.endpoints;

import java.util.ArrayList;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Master_GetVfcReasons
{
	public static Boolean master_GetVfcReasons_Test(String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strQuery =null;
			if(strResponse.contains("$skip=25"))
			{
				strQuery= "select top 25 external_system_id as externalSystemId,vfc_code as vfcCode,vfc_desc as vfcDescription,vfc_coding_system as vfcCodingSystem,case when delete_ind='N' then 'false' else 'true' end as isDeleted from imm_registry_vfc_xref where delete_ind = 'N' "
						+"order by external_system_id,delete_ind,vfc_code,vfc_coding_system,vfc_desc";
			}
			else
			{
				strQuery= "select external_system_id as externalSystemId,vfc_code as vfcCode,vfc_desc as vfcDescription,vfc_coding_system as vfcCodingSystem,case when delete_ind='N' then 'false' else 'true' end as isDeleted from imm_registry_vfc_xref where delete_ind = 'N' "
						+"order by external_system_id,delete_ind,vfc_code,vfc_coding_system,vfc_desc";
			}

			String[] jsonMetaData = {"externalSystemId","vfcCode","vfcDescription","vfcCodingSystem","isDeleted"};
			try
			{	
				ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
				Log.info("JSON LIST is :\n" + jsonList);
				status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..vfcCode", jsonList);
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
		return status;
	}
}