package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;
/*
 * ********************************************************************
 * Class Name						: Chart_GetWastedVaccine
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
public class Chart_GetWastedVaccine
{

	public static Boolean chart_GetWastedVaccine_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "select im.vaccine_waste_id as id, im.order_vaccine_id AS vaccineId, im.cvx_code as cvxCode, "
						+ "im.vaccine_desc as vaccineDescription, im.lot_num as lotNumber, im.cpt4_code as cptCode, im.mvx_code as mvxCode, "
						+ "im.manufacturer_name as manufacturerName, im.manufacturer_nbr as manufacturerNumber, im.strength as strength, "
						+ "im.units as units, im.units_code as unitsCode, im.dose as dose, im.route as route, im.route_code as routeCode, "
						+ "im.side as side, im.side_code as sideCode, im.site as site, im.site_code as siteCode, im.brand_name as brandName, "
						+ "im.purchase_type as purchaseType, im.wasted_reason as wastedReason, im.wasted_date as wastedDate, "
						+ "nor.order_num as orderId, nor.person_id as personId from imm_vaccine_waste as im "
						+ "inner join imm_nor as nor on im.person_id=nor.person_id "
						+ "where im.person_id='" + parameters.get("personId") + "'"
						+ "AND nor.order_num='" + parameters.get("orderId") + "'"
						+ "AND im.order_vaccine_id='" + parameters.get("vaccineId") + "'"
						+ "AND im.vaccine_waste_id='" + parameters.get("wastedVaccineId") + "'";
				
				String[] jsonMetaData = {"id","vaccineId","cvxCode","vaccineDescription", "lotNumber","cptCode","mvxCode", "manufacturerName",
						"manufacturerNumber","strength","units","unitsCode","dose", "route", "routeCode", "side", "sideCode", "site",
						"siteCode","brandName","purchaseType","wastedReason", "wastedDate;Date_YYYYMMDD HH:MM:SS", "orderId", "personId"};
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$.."+strUniqueJsonToken, jsonList);
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
