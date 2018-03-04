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
 * Class Name						: Chart_DeleteOrderedVaccine_TC1
 * Created By						: Pavan.G
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: personId,orderId,vaccineId
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							: Deletes a patient's ordered vaccine record.
 * ******************************************************************
 */

public class Chart_DeleteOrderedVaccine_TC1 extends BaseTest
{
	SoftAssert softAssert = new SoftAssert();

	@Test
	public void chart_DeleteOrderedVaccine_TC1()
	{
		boolean boolTestcaseStatus = true;
		String strClassName = getClass().getSimpleName();
		String strSimpleClassName = Log.intializeLoggin(strClassName);
		String strTestDataSheetName = GeneralUtils.sheetName(strSimpleClassName)+"_TC1";
		
		
		//Creating a new HashMap to store all the Request and Response Details
		HashMap<String, Object[]> hashMapResult = TestResults.generateHashMapResults();

		//Preparing the Map with all the Request details
		Map<String, Object> mapRequestParrameters = RequestParameters.generateRequestParamsHashMap("DELETE", "", "");

		RequestResponseHandler objHandler = new RequestResponseHandler();

		try
		{
			//Iterating through the Test DataSheet to run the testscript
			for(int k=1; k<=ExcelReaderWriter.getRowCount( BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strClassName); k++)
			{
				HashMap<String, String> parameters = Compute_Paramters.mapperCompute_Parameters(k, strClassName);
				mapRequestParrameters.put("strURI", Compute_Paramters.formulateFinalURI(strClassName, parameters));
				mapRequestParrameters.put("strPayLoad", "");
				

				String row_count = "select count(*) as row_count from imm_order_vaccines where order_num='"+parameters.get("orderId") +"'";
				String before_row_count = DatabaseConnection.fetchColumnAsObject(connNGA, row_count, "row_count").toString();

				//Sending the Request to get the Response
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParrameters);
				hashMapResult = TestsEvaluation.mapperExecuteTests(hashMapResult, objHandler, strSimpleClassName, strTestDataSheetName, softAssert, parameters);
				String after_row_count = DatabaseConnection.fetchColumnAsObject(connNGA, row_count, "row_count").toString();
				
				System.out.println(hashMapResult.get(""));
				Object[] testexecutionResult = hashMapResult.get(String.valueOf(k));

				
				if(Objects.equal(testexecutionResult[testexecutionResult.length-1].toString().toLowerCase(), "pass"))
				{
					if(objHandler.getIntResponseCode()==200)
					{
						boolTestcaseStatus= before_row_count.equals("1") && after_row_count.equals("0"); 
					}
					}
					else 
					{
						boolTestcaseStatus =false;
					}
				
				

				log.info("\n------- patient's ordered vaccine record for the orderId: " +parameters.get("orderId"));
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
