package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_AddLabOrderTrackingComment
{

	public static Boolean Chart_AddLabOrderTrackingComment_Test(HashMap<String, String> parameters, HashMap<String, Object> mapPostDetails, SoftAssert softAssert, String strQuery)
	{
		Boolean status=false;
		try
		{
			int countAfterPOST = DatabaseConnection.returnNumberOfRows(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery));
			if(countAfterPOST - ((int)mapPostDetails.get("countBeforePOST")) == 1)
			{
				String strQuery1 = "select top 1 signoff_comment from lab_order_signoff where order_id='" + parameters.get("orderId") + "' order by create_timestamp desc";
				ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
				ArrayList<Object> listTestData  = new ArrayList<>();
				for(Map.Entry<String, Object> entry1 : mapPostDetails.entrySet())
				{
					for(Map.Entry<String, String> entry2 : parameters.entrySet())
					{
						if(Objects.equals(entry2.getKey(), entry1.getKey()))
							listTestData.add(entry1.getValue());
					}	
				}
				status = ValidateResults.resultValidation(listTestData, listDatabase);
			}
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