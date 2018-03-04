package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetMedicationDosageRange 
{

	public static Boolean chart_GetMedicationDosageRange_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "select pd.pdm_mnage as minimumAge,pd.gcn_seqno as gcnSequenceNumber,pd.pdm_mxage as maximumAge,pd.pdm_mnd as minimumQuantity, "
						+ "pd.pdm_mxd as maximumQuantity,pd.doseu_desc as doseQuantityDescription,pd.pdm_mnu as minimumUnits,pd.pdm_mxu as maximumUnits, "
						+ "pd.unitf_desc as drugFormDescription,pd.pdm_nted as notToExceedQuantity,pd.ntedu_desc as notToExceedQuantityDescription,pd.pdm_nteu as notToExceedUnits, "
						+ "pd.nteuf_desc as notToExceedDrugFormDescription "
						+ "from ped_dosing as pd inner join patient_medication  as pm "
						+ "on pd.gcn_seqno = pm.gcn_seqno  "
			
						+ "where pm.person_id ='"+parameters.get("personId")+"' and pd.gcn_seqno = '"+parameters.get("gcnSequenceNumber")+"' ";
						
				
				
				
				String[] jsonMetaData = {"minimumAge","gcnSequenceNumber","maximumAge","minimumQuantity","maximumQuantity","doseQuantityDescription","minimumUnits","maximumUnits",
						"drugFormDescription","notToExceedQuantity","notToExceedQuantityDescription","notToExceedUnits","notToExceedDrugFormDescription"
				};
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..minimumAge", jsonList);
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
