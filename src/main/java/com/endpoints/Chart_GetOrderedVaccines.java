package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetOrderedVaccines {


	public static Boolean Chart_GetOrderedVaccines_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				
				String strQuery = "Select "
						+ "IOV.order_vaccine_id as id,  IMM.person_id as personId, IMM.enterprise_id as enterpriseId,  IMM.practice_id as practiceId,  IOV.order_num as orderId, "
						+ "IOV.cvx_code as cvxCode, IOV.vaccine_desc as description, IOV.cpt4_code as cptCode,  IOV.seq_nbr as sequenceNumber,  IOV.vaccine_status as status, "
						+ "IOV.lot_num as lotNumber,  IOV.administer_year as administeredYear,  IOV.administer_month as administeredMonth,  IOV.administer_day as administeredDay, " 
						+ "IOV.administer_time as administeredTimestamp, IOV.administer_by as administeredByUserId,  "
						+ "case when IOV.administer_by is null then null  else concat(UM.last_name,', ',UM.first_name) end as administeredByName, "
						+ "case when IOV.vfc_ind = 'N' then 'false' else 'true' end  as isVfc,  "
						+ "IOV.vfc_code as vfcCode,  IOV.funding_src_code as fundingSourceCode   "
						+ "from imm_nor IMM Inner join imm_order_vaccines IOV on IOV.order_num = IMM.order_num  "
						+ "LEFT OUTER JOIN user_mstr UM on IOV.administer_by = UM.user_id "
						+ "Where IMM.person_id = '"+parameters.get("personId")+"' and IMM.order_num = '"+parameters.get("orderId")+"'";
						
				String[] jsonMetaData = {"id;GUID","personId;GUID","enterpriseId;Normal","practiceId;Normal","orderId;GUID","cvxCode;Normal","description;Normal","cptCode;Normal",
						"sequenceNumber;Normal","status;Normal","lotNumber;Normal","administeredYear;Normal","administeredMonth;Normal","administeredDay;Normal","administeredTimestamp;Date_YYYYMMDD HH:MM:SS",
						"administeredByUserId;Normal","administeredByName;Normal","isVfc;Boolean","vfcCode;Normal","fundingSourceCode;Normal"};
				
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
