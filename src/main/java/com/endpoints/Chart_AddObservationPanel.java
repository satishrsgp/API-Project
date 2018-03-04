
package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import org.testng.asserts.SoftAssert;
import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_AddObservationPanel
{
	public static Boolean chart_AddObservationPanel(HashMap<String, String> parameters,String strResponse, HashMap<String, Object> mapPostDetails, SoftAssert softAssert, String strQuery)
	{
		Boolean status=false;
		try
		{
			int countAfterPOST = DatabaseConnection.returnNumberOfRows(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery));
			if(countAfterPOST - ((int)mapPostDetails.get("countBeforePOST")) == 1)
			{
				String strQuery1 = "select ngn_order_num,order_test_id,coll_date_time,order_result_stat,obr_comment from lab_results_obr_p "
						+ "where person_id='"+parameters.get("personId")+"' order by create_timestamp desc";
				
				ArrayList<Object> jsonList = new ArrayList<Object>();
				
                jsonList.add(parameters.get("OrderId").toString());
                jsonList.add(parameters.get("OrderedTestId").toString());
                jsonList.add(parameters.get("CollectionDateTime"));
                jsonList.add(parameters.get("Status").toString());
                jsonList.add(parameters.get("PanelComment").toString());
                
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