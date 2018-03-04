package com.endpoints;

import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;

public class Chart_AddNewLabOrder
{
	
	public static Boolean Chart_AddNewLabOrder_Test(HashMap<String, String> parameters, String strResponse,HashMap<String, Object> mapPostDetails, SoftAssert softAssert, String strQuery)
	{
		Boolean status=false;
		try
		{
			int countAfterPOST = DatabaseConnection.returnNumberOfRows(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery));
			if(Math.abs(countAfterPOST - ((int)mapPostDetails.get("countBeforePOST")))  == 1)
			{
				status = true;
				/*
				
				Boolean Liststatus = true;
				Object obj = JsonPath.read(strResponse, "$..id");
				ArrayList<Object> jsonList = new ArrayList<>();

				if(obj instanceof JSONArray)
				{
					for(int i = 0; i< ((JSONArray)obj).size(); i++)
					{
						
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].testLocation").toString().toUpperCase());
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].personId").toString().toUpperCase());
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].encounterId").toString().toUpperCase());
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].orderingProvider").toString().toUpperCase());
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].testDescription"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].clinicalInformation"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].generatedBy"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].billingType"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].orderPriority"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].orderControl"));
						
						
					
					}
				}
				
				String[] ParameterData = {"TestLocationId","personId","encounterId","OrderingProvider","TestCodeText",
						"ClinicalInfo","GeneratedBy1","BillingType","orderPriority","orderControl"};
				
				for(int i = 0;i<=ParameterData.length-1;i++)
				{
					if(jsonList.contains(parameters.get(ParameterData[i])))
					{
						Liststatus = Liststatus && true;
					}
					else
					{
						Liststatus = Liststatus && false;
					}
					
				}	
				
				status = Liststatus;
			*/}
			else
			{
				Log.info("The Difference between records count before and after post is more than one, please reexcute once more");
				softAssert.fail("The Difference between records count before and after post is more than one. Please reexecute once more");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}


}
