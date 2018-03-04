package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Users_GetFavoriteProceduresWithOutLocation {


	public static Boolean Users_GetFavoriteProceduresWithOutLocation_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				
				String strQuery = "SELECT  DISTINCT Top 25  "
						+ "SIM.cpt4_code_id as code, "
						+ "SIM.description as description, "
						+ "SIM.service_item_id as serviceItemId, "
						+ "case when SIM.delete_ind = 'N' then 'false' else 'true' end as isDeleted "
						+ "FROM patient_procedure PP "
						+ "INNER JOIN patient_encounter PE ON PE.enc_id = PP.enc_id  "
						+ "INNER JOIN service_item_mstr SIM ON SIM.service_item_id = PP.service_item_id and SIM.service_item_lib_id = PP.service_item_lib_id "
						+ "WHERE PP.create_timestamp > GETDATE() - 180  "
						+ "AND SIM.eff_date < FORMAT(getdate(), 'yyyyMMdd') "
						+ "AND SIM.exp_date > FORMAT(getdate(), 'yyyyMMdd') "
						+ "AND SIM.service_item_lib_id = '"+parameters.get("ServiceItemLibID")+"'"
						+ "AND PP.created_by = '"+parameters.get("userId")+"' "
						+ "AND SIM.delete_ind = 'N' "
						+ "ORDER BY SIM.cpt4_code_id ASC, SIM.description ASC, isDeleted ASC,SIM.service_item_id ASC ";
				
				
				String[] jsonMetaData = {"code","description","serviceItemId","isDeleted"};
						
				
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$.."+strUniqueJsonToken, jsonList);
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
