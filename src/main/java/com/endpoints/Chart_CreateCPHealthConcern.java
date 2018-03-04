
package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;
import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_CreateCPHealthConcern
{
	public static Boolean chart_CreateCPHealthConcern(HashMap<String, String> parameters,String strResponse, HashMap<String, Object> mapPostDetails, SoftAssert softAssert, String strQuery)
	{
		Boolean status=false;
		try
		{
			int countAfterPOST = DatabaseConnection.returnNumberOfRows(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery));
			if(countAfterPOST - ((int)mapPostDetails.get("countBeforePOST")) == 1)
			{
				String strQuery1 = "select top 1 comments,health_concern,identified_date,pointer_value,code_description,code_system,code_value,status from health_concern_ext_ "
						+ "where person_id='"+parameters.get("personId")+"' and encounterID='"+parameters.get("encounterId")+"' order by create_timestamp desc";
				
				ArrayList<Object> jsonList = new ArrayList<Object>();
				
				jsonList.add(parameters.get("category").toString());
                jsonList.add(parameters.get("comments"));
                jsonList.add(parameters.get("healthConcern"));
                jsonList.add(Objects.equals(parameters.get("identifiedDate"), null) ? null : (parameters.get("identifiedDate").toString().split("T")[0]).replace("-", ""));
                jsonList.add(parameters.get("categoryId").toString());
                jsonList.add(parameters.get("codeDescription").toString());
                jsonList.add(parameters.get("codeSystem"));
                jsonList.add(parameters.get("codeValue").toString());
                jsonList.add(parameters.get("status").toString());
                jsonList.add(parameters.get("otherCategory").toString());
                
                
				status = ValidateResults.resultsvalidation(jsonList, DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
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