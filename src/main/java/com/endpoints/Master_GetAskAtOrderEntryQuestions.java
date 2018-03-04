package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Master_GetAskAtOrderEntryQuestions 
{

	public static Boolean master_GetAskAtOrderEntryQuestions_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "select laqm.question_code as id, laqm.system_id as systemId, laqm.answer_code_id as answerCodeId, "
						+ "laqm.table_name as tableName, laqm.field_name as fieldName, ltax.labtest_key as testId, ltax.sequence_number as sequenceNumber, "
						+ "laqm.description as description, case when laqm.global_ind = 'N' then 'false' else 'true' end as isGlobal, "
						+ "case when laqm.need_answer_ind ='N' then 'false' else 'true' end as needAnswer, laqm.answer_type_mode as answerTypeMode, "
						+ "laqm.code_system as codeSystem, laqm.answer_units as answerUnits, laqm.data_type as dataType, laqm.lab_native_data_type as labNativeDataType, "
						+ "laqm.data_format as dataFormat, case when laqm.delete_ind ='N' then 'false' else 'true' end as isDeleted FROM lab_aoe_questions_mstr laqm "
						+ "INNER JOIN lab_test_aoe_xref ltax ON laqm.system_id = ltax.system_id AND laqm.question_code = ltax.aoe_question_code AND ltax.labtest_key ='"
						+parameters.get("testId")+"'";
				
				String[] jsonMetaData = {"id","systemId","answerCodeId","tableName","fieldName","testId",
				"sequenceNumber","description","isGlobal","needAnswer",	"answerTypeMode","codeSystem","answerUnits",
				"dataType","labNativeDataType","dataFormat","isDeleted"};
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..testId", jsonList);
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
