package com.testscripts;

import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.google.common.base.Objects;
import com.utils.API_Request_RetObj;
import com.utils.Compute_Paramters;
import com.utils.DatabaseConnection;
import com.utils.ExcelReaderWriter;
import com.utils.RequestParameters;
import com.utils.RequestResponseHandler;
import com.utils.TestResults;
import com.utils.TestsEvaluation;


/*
 * ********************************************************************
 * Class Name						: Chart_AddNewPatientAlert_TC1
 * Created By						: 
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: personId
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							: Adds a single alert message based on  alert_type_id 
 * ******************************************************************
 */

public class Master_AddNewAlertMessage_TC1 extends BaseTest
{
	SoftAssert softAssert = new SoftAssert();

	@Test
	public void master_AddNewAlertMessage_TC1()
	{
		boolean boolTestcaseStatus = true;
		String strClassName = getClass().getSimpleName();

	

		//Creating a new HashMap to store all the Request and Response Details
		HashMap<String, Object[]> hashMapResult = TestResults.generateHashMapResults();

		//Preparing the Map with all the Request details
		Map<String, Object> mapRequestParrameters = RequestParameters.generateRequestParamsHashMap("POST", "", "");

		RequestResponseHandler objHandler = new RequestResponseHandler();

		try
		{
			//Iterating through the Test DataSheet to run the testscript
			for(int k=1; k<=ExcelReaderWriter.getRowCount( BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strClassName); k++)
			{
				
				HashMap<String, String> parameters = Compute_Paramters.mapperCompute_Parameters(k, strClassName);
				mapRequestParrameters.put("strURI", Compute_Paramters.formulateFinalURI(strClassName, parameters));
				String row_Count_Query = "select count(*) as count from ehr_alerts_mstr where alerttype_id = '"+parameters.get("AlertTypeId")+"'";
				String before_rowcount="";
				String after_rowcount ="";
				String payLoad = "{\"AlertTypeId\":\""+parameters.get("AlertTypeId") +"\""
				+",\"Description\":\""+parameters.get("Message") +"\"}"
				;
				mapRequestParrameters.put("strPayLoad", payLoad);

				
				
				if(ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2).contains("201"))
					 before_rowcount = DatabaseConnection.fetchColumnAsObject(connNGA, row_Count_Query, "count").toString();

				//Sending the Request to get the Response
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParrameters);
				hashMapResult = TestsEvaluation.mapperExecuteTests(hashMapResult, objHandler, strClassName, strClassName, softAssert, parameters);

				
				Object[] testexecutionResult = hashMapResult.get(String.valueOf(k));

				if(Objects.equal(testexecutionResult[testexecutionResult.length-1].toString().toLowerCase(), "fail"))
				{
					boolTestcaseStatus=false;	
					 log.info("\n-------please check with AlertTypeId " +parameters.get("AlertTypeId"));
				}
				String strSQLQuery = "select top 1 description from ehr_alerts_mstr where alerttype_id = '"+parameters.get("AlertTypeId")+"' order by create_timestamp desc"; 
				if(ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2).contains("201"))
				{	
				 after_rowcount = DatabaseConnection.fetchColumnAsObject(connNGA, row_Count_Query, "count").toString();
				 String message = DatabaseConnection.fetchColumnAsObject(connNGA, strSQLQuery, "Description").toString();
			     boolTestcaseStatus = message.contains(parameters.get("Message")) && Integer.parseInt(after_rowcount)==(Integer.parseInt(before_rowcount)+1);
				 log.info("\n------- Message added to the AlertTypeId " +parameters.get("AlertTypeId") +" : "+parameters.get("Message"));
				}
				
				
			
				objHandler = TestsEvaluation.updateStatus(objHandler, softAssert, boolTestcaseStatus, parameters);
				hashMapResult = TestsEvaluation.updateHashMap(hashMapResult, k, boolTestcaseStatus);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		//Preparing the TestResults Excel Sheet and updating with all the Request and Response Details.
		TestResults.writeResults(strClassName, hashMapResult, boolTestcaseStatus);
		softAssert.assertAll();
	}
}
