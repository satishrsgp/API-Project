package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;

import com.utils.JsonListArray;
import com.utils.Log;
import com.utils.ValidateResults;

public class Master_GetRefillDenialReasons 
{

	
	public static Boolean master_GetRefillDenialReasonss_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		
		//System.out.println(strResponse);
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{ 
				String strQuery = "SELECT mstr_list_item_desc FROM mstr_lists WHERE mstr_list_type = 'erx_custom_denial_reason' and delete_ind<> 'Y'" ;	
				
				String[] jsonMetaData = {"id", "code","denialReason"};
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					ArrayList<Object> DBList = new ArrayList<>();
					Object[] ConstantData={1,"AA","Person unknown to prescriber",2,"AB","Person never a patient",3,"AC","Person no longer a patient",
							4,"AD","Refill requested too soon",5,"AE","Medication never prescribed",6,"AF","Patient must contact provider first",
							7,"AG","Refill not appropriate",8,"AP","Request already responded to by other means (phone or fax)",9,
							"AO","No attempt will be made to obtain Prior Authorization",10,"AN","Provider not associated with this practice",11,
							"AM","Patient needs appointment",12,"AL","Change to prescription not appropriate",13,"AK","Patient has not picked up prescription, returned to stock",
							14,"AJ","Patient has picked up partial fill of prescription",15,"AH","Patient has picked up prescription",16,"BA","Denial - New prescription to follow"};
					
					for(Object i : ConstantData) {
						DBList.add(i);
					}
					ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery));
					int n=17;
					for (Object temp : listDatabase) {
						
						String Code = "";
						DBList.add(n);
						DBList.add(Code);
						DBList.add(temp);
						n++;
					}
					Log.info("DB LIST is :\n" + DBList);
					
					status = ValidateResults.resultValidation(jsonList, DBList);
					
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
