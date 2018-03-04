package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Persons_GetFormularyCopay 
{

	public static Boolean Persons_GetFormularyCopay_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "select distinct    "
						+ "nrcs.out_of_pocket_range_start as outOfPocketRangeStart,nrcs.out_of_pocket_range_end	as outOfPocketRangeEnd,nrcds.flat_copay_amount as flatCopayAmount, "
						+ "nrcds.percent_copay_rate as percentCopayRate,nrcds.first_copay_term as firstCopayTerm,nrcds.maximum_copay as maximumCopay, "
						+ "nrcds.minimum_copay as minimumCopay,nrcds.days_supply_per_copay as daysSupplyPerCopay,nrcds.copay_tier as copayTier, nrcds.maximum_copay_tier as maximumCopayTier, "
						+ "nrcds.pharmacy_type as pharmacyType,nrcds.source_id as sourceId , nrcds.copay_id as copayId "
						+ "from ng_rxh_copay_drug_specific as nrcds inner join patient_eligibility as pe on nrcds.source_id = pe.pbm_id "
						+ "inner join ng_rxh_copay_summary as nrcs on nrcs.copay_id = nrcds.copay_id "
					
						+ "	where  pe.person_id='"+parameters.get("personId")+"' and  nrcds.medid = '"+parameters.get("medicationId")+"' and nrcds.copay_id = '"+parameters.get("copayId")+"'  and nrcds.source_id ='"+parameters.get("pbmId1")+"'";

				
				
				
				String[] jsonMetaData = {"outOfPocketRangeStart","outOfPocketRangeEnd","flatCopayAmount","percentCopayRate","firstCopayTerm","maximumCopay","minimumCopay","daysSupplyPerCopay","copayTier",
						"maximumCopayTier","pharmacyType","sourceId","copayId"
				
				};
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..sourceId", jsonList);
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
