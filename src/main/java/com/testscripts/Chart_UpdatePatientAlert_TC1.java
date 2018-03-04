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
import com.utils.GeneralUtils;
import com.utils.Log;
import com.utils.RequestParameters;
import com.utils.RequestResponseHandler;
import com.utils.TestResults;
import com.utils.TestsEvaluation;


/*
 * ********************************************************************
 * Class Name						: Chart_UpdatePatientAlert
 * Created By						: 
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: personId
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							: update the description to  a single alert message based on  alert_id and person_id 
 * ******************************************************************
 */

public class Chart_UpdatePatientAlert_TC1 extends BaseTest
{
	SoftAssert softAssert = new SoftAssert();

	@Test
	public void chart_UpdatePatientAlert_TC1()
	{
		boolean boolTestcaseStatus = true;
		String strClassName = getClass().getSimpleName();
		String strSimpleClassName = Log.intializeLoggin(strClassName);
		String strTestDataSheetName = GeneralUtils.sheetName(strSimpleClassName)+"_TC1";
		/*String strSimpleClassName = strClassName.substring(0, strClassName.length()-4);
		System.out.println("Running test case " + strClassName+ " with Thread Id:- " + Thread.currentThread().getId());
		Log.info("Running test case " + strClassName+ " with Thread Id:- " + Thread.currentThread().getId());*/

		//Creating a new HashMap to store all the Request and Response Details
		HashMap<String, Object[]> hashMapResult = TestResults.generateHashMapResults();

		//Preparing the Map with all the Request details
		Map<String, Object> mapRequestParrameters = RequestParameters.generateRequestParamsHashMap("PUT", "", "");
		RequestResponseHandler objHandler = new RequestResponseHandler();

		try
		{
			//Iterating through the Test DataSheet to run the testscript
			for(int k=1; k<=ExcelReaderWriter.getRowCount( BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strClassName); k++)
			{
				HashMap<String, String> parameters = Compute_Paramters.mapperCompute_Parameters(k, strClassName);
				mapRequestParrameters.put("strURI", Compute_Paramters.formulateFinalURI(strClassName, parameters));

				String payLoad = "{\"Comment\":\""+parameters.get("String_Update") +"\""
				+",\"IsFlagged\":\"true"+"\""
				+",\"ShouldBeAcknowledgedBy\":\"Provider"+"\""	
				+",\"SnoozeHours\":\"1"
				+"\"}"
				;
				mapRequestParrameters.put("strPayLoad", payLoad);
				String comment_Update="select comment from patient_alerts_ehr where person_id = '"+parameters.get("personId")+"' and alert_id ='"+parameters.get("alertId")+"'";;		
				String message ="";
				

					
					
				//Sending the Request to get the Response
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParrameters);
				hashMapResult = TestsEvaluation.mapperExecuteTests(hashMapResult, objHandler, strSimpleClassName, strTestDataSheetName, softAssert, parameters);
				
				
				Object[] testexecutionResult = hashMapResult.get(String.valueOf(k));
				if(Objects.equal(testexecutionResult[testexecutionResult.length-1].toString().toLowerCase(), "fail"))
				{
					boolTestcaseStatus = false;
					log.info("Check with the Alert/connection : " + parameters.get("alertId"));
				}
				
				if(ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2).contains("201")){
					 message = DatabaseConnection.fetchColumnAsObject(connNGA, comment_Update, "comment").toString();
						boolTestcaseStatus = message.contains(parameters.get("String_Update"));
						log.info("\n------- Comment added to the Alert_id " +parameters.get("alertId") +" : "+parameters.get("String_Update"));
				}
			
				objHandler = TestsEvaluation.updateStatus(objHandler, softAssert, boolTestcaseStatus, parameters);
				hashMapResult = TestsEvaluation.updateHashMap(hashMapResult, k, boolTestcaseStatus);
				//objHandler = null;
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
