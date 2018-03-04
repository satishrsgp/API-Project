package com.endpoints;

import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;

public class Master_GetVaccineVisDocument 
{

	public static Boolean Master_GetVaccineVisDocument_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "select * from imm_vaccine_vis WHERE cvx_code = '"+parameters.get("cvxCode")+"' and vaccine_vis_id = '"+parameters.get("visId")+"'";
				
				//providerName ,"whoReviewed": null,"whenReviewed": null,
				

				String vis_path = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strQuery, "vis_path").toString();
				
				try
				{	

					if(vis_path.contains(".pdf")|| !vis_path.isEmpty())
					{
						Log.info("vis_path is valid which has a path : " + parameters.get(""));
						status=true;
					}
					else
					{
						softAssert.fail("Error in Test Script please look at logs");
					}
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
