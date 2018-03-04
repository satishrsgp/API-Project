package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetVaccineComponentLotNumbers 
{
	public static Boolean chart_GetVaccineComponentLotNumbers_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				
				String strQuery = "select "
						+ "IVCL.vaccine_lot_id as id, "
						+ "IVCL.order_vaccine_id as vaccineId, "						
						+ "IVCL.component_name as componentName,  " 
						+ "IVCL.lot_nbr as lotNumber, "
						+ "IVCL.diluent_lot_nbr as diluentLotNumber, "
						+ "IMN.order_num as orderId, "
						+ "IMN.person_id as personId "
						+ "FROM imm_nor AS IMN "
						+ "INNER JOIN imm_order_vaccines AS IOV ON IMN.order_num = IOV.order_num "
						+ "INNER JOIN imm_vaccine_comp_lot AS IVCL ON IOV.order_vaccine_id = IVCL.order_vaccine_id "						
						+ "WHERE IVCL.order_vaccine_id = '"+parameters.get("VaccineId")+"' AND IMN.person_id = '"+parameters.get("personId")+"' AND IMN.order_num = '"+parameters.get("orderId")+"'";
				
				String[] jsonMetaData = {"id","vaccineId","componentName","lotNumber","diluentLotNumber","orderId","personId"};
				
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
