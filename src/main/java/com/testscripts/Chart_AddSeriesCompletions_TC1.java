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
 * Class Name						: Chart_AddSeriesCompletions_TC1
 * Created By						: 
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: personId
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							:  
 * ******************************************************************
 */

public class Chart_AddSeriesCompletions_TC1 extends BaseTest
{
	SoftAssert softAssert = new SoftAssert();

	@Test
	public void chart_AddSeriesCompletions_TC1()
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
		Map<String, Object> mapRequestParrameters = RequestParameters.generateRequestParamsHashMap("POST", "", "");

		RequestResponseHandler objHandler = new RequestResponseHandler();

		try
		{
			//Iterating through the Test DataSheet to run the testscript
			for(int k=1; k<=ExcelReaderWriter.getRowCount( BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strClassName); k++)
			{
				HashMap<String, String> parameters = Compute_Paramters.mapperCompute_Parameters(k, strClassName);
				mapRequestParrameters.put("strURI", Compute_Paramters.formulateFinalURI(strClassName, parameters));
				String comment_Update = null;
				String payLoad = "{\"SeriesName\":\""+parameters.get("SeriesName") +"\""
				+",\"IsComplete\":\"true"+"\""
				+",\"Comment\":\""+parameters.get("String_Update")+"\""	
				+"}"
				;
				String message="";
				mapRequestParrameters.put("strPayLoad", payLoad);
				comment_Update = "select  * from  imm_series_complete  where person_id = '"+parameters.get("personId") +"' and comment='"+parameters.get("String_Update")+"'";

				//Sending the Request to get the Response
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParrameters);
				hashMapResult = TestsEvaluation.mapperExecuteTests(hashMapResult, objHandler, strSimpleClassName, strTestDataSheetName, softAssert, parameters);

				
				Object[] testexecutionResult = hashMapResult.get(String.valueOf(k));
				if(Objects.equal(testexecutionResult[testexecutionResult.length-1].toString().toLowerCase(), "fail"))
				{
					boolTestcaseStatus=false;
				}
				if(objHandler.getIntResponseCode()==201){
					message = DatabaseConnection.fetchColumnAsObject(connNGA, comment_Update, "comment").toString();
					boolTestcaseStatus = message.contains(parameters.get("String_Update"));
					log.info("\n------- Comment added to the person_id " +parameters.get("SeriesName") +" and  : "+parameters.get("SeriesName"));
				}
				

				hashMapResult = TestsEvaluation.updateHashMap(hashMapResult, k, boolTestcaseStatus);
				objHandler = TestsEvaluation.updateStatus(objHandler, softAssert, boolTestcaseStatus, parameters);
			
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
