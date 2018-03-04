package com.testscripts;

import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
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


public class Master_GetVaccineVisDocument_TC1 extends BaseTest
{

	SoftAssert softAssert = new SoftAssert();
	
	@Test
	public void master_GetVaccineVisDocument_TC1()
	{
		boolean boolTestcaseStatus = true, boolTestDataStatus = true;
		
		String strClassName = getClass().getSimpleName();
		String strSimpleClassName = Log.intializeLoggin(strClassName);
		String strTestDataSheetName = GeneralUtils.sheetName(strSimpleClassName);
		String vis_path = "";
		//Creating a new HashMap to store all the Request and Response Details
		HashMap<String, Object[]> hashMapResult = TestResults.generateHashMapResults();

		//Preparing the Map with all the Request details
		Map<String, Object> mapRequestParrameters = RequestParameters.generateRequestParamsHashMap("GET", "", "");

		RequestResponseHandler objHandler = new RequestResponseHandler();

		try
		{
			//Iterating through the Test DataSheet to run the testscript
			for(int k=1; k<=ExcelReaderWriter.getRowCount( BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strTestDataSheetName); k++)
			{	
				HashMap<String, String> parameters = Compute_Paramters.mapperCompute_Parameters(k, strTestDataSheetName);
				mapRequestParrameters.put("strURI", Compute_Paramters.formulateFinalURI(strSimpleClassName, parameters));

				//Sending the Request to get the Response
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParrameters);
				
				hashMapResult = TestsEvaluation.mapperExecuteTests(hashMapResult, objHandler, strSimpleClassName, strTestDataSheetName, softAssert, parameters);
				
                String strQuery = "select * from imm_vaccine_vis WHERE cvx_code = '"+parameters.get("cvxCode")+"' and vaccine_vis_id = '"+parameters.get("visId")+"'";
								
if(objHandler.getIntResponseCode()==200){
				 vis_path = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strQuery, "vis_path").toString();
}		
				try
				{	

					if(vis_path.contains(".pdf")|| !vis_path.isEmpty())
					{
						Log.info("vis_path is valid which has a path : " + vis_path);
						boolTestDataStatus=true;
					}
					else
					{
						boolTestDataStatus = false;
						softAssert.fail("Error in Test Script please look at logs");
					}
				
					boolTestcaseStatus = boolTestcaseStatus && boolTestDataStatus;

                //Update the pass/fail and update the hashMapResult
                hashMapResult = TestsEvaluation.updateHashMap(hashMapResult, k, boolTestDataStatus);
                objHandler = TestsEvaluation.updateStatus(objHandler, softAssert, boolTestDataStatus, parameters);
			}
				catch(Exception e)
				{
					e.printStackTrace();
				}
		}
	

		//Preparing the TestResults Excel Sheet and updating with all the Request and Response Details.
		TestResults.writeResults(strClassName, hashMapResult, boolTestcaseStatus);
		softAssert.assertAll();
	}
		catch(Exception e)
		{
			e.printStackTrace();
		}
}
}