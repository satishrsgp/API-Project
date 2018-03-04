package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

/*
 * ********************************************************************
 * Class Name						: Chart_GetOrderEntryAnswers 
 * Created By						: Seema Jaffary
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: 
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							:
 * ******************************************************************
 */

public class Chart_GetOrderEntryAnswers
{

	public static Boolean Chart_GetOrderEntryAnswers_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "select l.lab_test_aoe_answer_id as id, nor.enterprise_id as enterpriseId, NOR.practice_id AS practiceId, "
						+ "nor.person_id as personId, l.order_num as orderId, l.order_test_id as orderedTestId, l.test_data_value as answer, "
						+ "l.aoe_question_code as questionCode, l.created_by as createdBy, l.create_timestamp as createTimestamp, "
						+ "l.modified_by as modifiedBy, l.modify_timestamp as modifyTimestamp "
						+ "from lab_test_aoe_answer as l "
						+ "inner join lab_nor nor on l.order_num=nor.order_num "
						+ "where nor.person_id='" + parameters.get("personId") + "'"
						+ "AND nor.order_num='" + parameters.get("orderId") + "'"
						+ "AND l.order_test_id='" + parameters.get("testId") + "'";
					
				
				String[] jsonMetaData = {"id","enterpriseId","practiceId","personId", "orderId","orderedTestId","answer", "questionCode",
						"createdBy","createTimestamp;Date_YYYYMMDD HH:MM:SS","modifiedBy","modifyTimestamp;Date_YYYYMMDD HH:MM:SS"};
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$.."+strUniqueJsonToken, jsonList);
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
