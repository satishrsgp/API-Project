
package com.endpoints;

import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;
import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;

public class Chart_SendErx
{
	public static Boolean chart_SendErx(HashMap<String, String> parameters,String strResponse, HashMap<String, Object> mapPostDetails, SoftAssert softAssert, String strQuery)
	{
		Boolean status=false;
		try
		{
			int countAfterPOST = DatabaseConnection.returnNumberOfRows(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery));
			if(countAfterPOST - ((int)mapPostDetails.get("countBeforePOST")) == 1)
			{
				String strQuery1 = "select top 1 response_type  as requestType from erx_message_history " 
                        + "where person_id = '"+parameters.get("personId")+"' and medication_id ='"+parameters.get("medicationId")+"' order by create_timestamp desc ";
				
				Thread.sleep(50000);
				
				Object responseType = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strQuery1, "requestType");
				
				if(Objects.equals(responseType.toString(),"STATUS"))
				{
                
				status = true;
				}
				else
				{
				status=false;
				Log.info("Send eRx is failed,please check DB for details");
				
				}
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