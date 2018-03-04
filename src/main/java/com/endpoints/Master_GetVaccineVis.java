package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Master_GetVaccineVis 
{

	public static Boolean master_GetVaccineVis_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "select cvx_Code as cvxCode,vaccine_vis_id as id,  vis_desc as visDescription, "
						+ "vis_publish_date as visPublishDate, vis_path as visPath, vis_documentType as visDocumentType  "
						+ "from imm_vaccine_vis where cvx_code = '"
			         +   parameters.get("cvxCode")+"'";
					
				
				//da.event_text whoReviewed,da.create_timestamp as whenReviewed   --------"whoReviewed": null,"whenReviewed": null,

				
				String[] jsonMetaData = {"cvxCode;Normal","id;GUID","visDescription;Normal","visPublishDate;Date_YYYYMMDD HH:MM:SS","visPath;Normal","visDocumentType;Normal"};
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..cvxCode", jsonList);
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
