package com.endpoints;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.JsonPath;
import com.utils.API_Request_RetObj;
import com.utils.DatabaseConnection;
import com.utils.ExcelReaderWriter;
import com.utils.Log;
import com.utils.RequestResponseHandler;
import com.utils.ValidateResults;

import net.minidev.json.JSONArray;

public class Allergy_GET_Master extends BaseTest_obsl {
	
	/*
	 * ********************************************************************
	 * Class Name						: Allergy_GET_Master
	 * Created By						: Pavan
	 * Created Date						: 
	 * Modified Date					:
	 * Modified By						:
	 * Parameters						: 
	 * Test Case ID						:
	 * Test Case Author					: 
	 * Test Case Modified By			:
	 * Test Case Reviewed By			:
	 * Purpose							: Route will fetch the all allergies from master
	 *********************************************************************
	 */
	String strClassName = this.getClass().getSimpleName();
	SoftAssert softAssert = new SoftAssert();
	//public CloseableHttpClient httpClient;
	public CloseableHttpResponse httpResponse;
	@SuppressWarnings("null")
	@Test
	public void allergy_GET_Master()
	{

		boolean boolTestcaseStatus = true;
		HashMap<String, Object[]> hashMapResult = new HashMap<>();
		hashMapResult.put("0", new Object[] {"Parameters","RequestURI", "RequestHeaders","ResponseCode", "ResponseHeaders", "Response", "Status"});
		try
		{


			Map<String, Object> mapRequestParrameters = new HashMap<String, Object>();
			mapRequestParrameters.put("strRequestType", "GET");
			mapRequestParrameters.put("strPayLoad", "");
			String strURI = BaseTest_obsl.mapURLRepo.get(strClassName).toString();
			String strQueryString = strURI.contains("?") ? strURI.substring(strURI.indexOf("?"), strURI.length()) : "";
			mapRequestParrameters.put("strQueryString", strQueryString);

			RequestResponseHandler objHandler = new RequestResponseHandler();
			for(int i=1; i<=1; i++)
			{
				//String PersonID = ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, i, 0);
				//System.out.println(PersonID);

				//pa.audit_id,
				mapRequestParrameters.put("strURI", BaseTest_obsl.mapURLRepo.get(strClassName));
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParrameters);



				/*String strSQLQuery = "select drugAllergies.dam_concept_id_desc as description,convert(varchar(20),drugAllergies.dam_concept_id) as allergy_id , fdbAllergy.description as 	allergy_type "
						+" from rdamapm0 as drugAllergies "
						+" INNER JOIN fdb_allergy_type_mstr as fdbAllergy "
						+" ON drugAllergies.dam_concept_id_typ = fdbAllergy.allergy_type_id UNION"
						+" select allergyMstr.description,allergyMstr.allergy_id, fdbAllergy.description as allergy_type"
						+" from allergy_mstr as allergyMstr "
						+" INNER JOIN fdb_allergy_type_mstr as fdbAllergy "
						+" ON allergyMstr.allergy_type_id = fdbAllergy.allergy_type_id " 
						+" where (allergyMstr.allergy_type_id = 5)" ;*/



				String  strSQLQuery = "SELECT top 1000 Rtrim(Ltrim([Project4].[C4] )) AS [description],[Project4].[C2] AS [allergyId], [Project4].[C3] AS [allergyType] "
						+ "FROM (SELECT [Distinct1].[C1] AS [C1], [Distinct1].[C2] AS [C2], [Distinct1].[C3] AS [C3], [Distinct1].[C4] AS [C4],  "
						+ "[Distinct1].[C5] AS [C5] FROM (SELECT DISTINCT [UnionAll1].[C1] AS [C1], [UnionAll1].[C2] AS [C2],  "
						+ "[UnionAll1].[description] AS [C3], [UnionAll1].[dam_concept_id_desc] AS [C4], [UnionAll1].[allergy_type_id] AS [C5]  "
						+ "FROM (SELECT 1 AS [C1], CAST([Extent1].[dam_concept_id] AS NVARCHAR(MAX)) AS [C2], [Extent2].[description] AS [description],  "
						+ "[Extent1].[dam_concept_id_desc] AS [dam_concept_id_desc], [Extent2].[allergy_type_id] AS [allergy_type_id] FROM [dbo].[rdamapm0] "
						+ " AS [Extent1] INNER JOIN [dbo].[fdb_allergy_type_mstr] AS [Extent2] ON [Extent1].[dam_concept_id_typ] = CAST ([Extent2].[allergy_type_id]  "
						+ "AS DECIMAL (19,0)) UNION ALL SELECT 1 AS [C1], [Extent3].[allergy_id] AS [allergy_id], [Extent4].[description] AS [description],  "
						+ "[Extent3].[description] AS [description1], [Extent3].[allergy_type_id] AS [allergy_type_id] FROM [dbo].[allergy_mstr] AS [Extent3]  "
						+ "INNER JOIN [dbo].[fdb_allergy_type_mstr] AS [Extent4] ON [Extent3].[allergy_type_id] = [Extent4].[allergy_type_id]  "
						+ "WHERE [Extent3].[allergy_type_id] = 5) AS [UnionAll1]) AS [Distinct1]) AS [Project4] ORDER BY [Project4].[C2] ASC,  "
						+ "[Project4].[C3] ASC, [Project4].[C4] ASC";

				System.out.println(strSQLQuery);
				
				String $count =mapRequestParrameters.get("strURI").toString().replace(mapRequestParrameters.get("strURI").toString(), "http://10.145.16.65:888/api/master/allergies?$count=true");
				mapRequestParrameters.put("strQueryString", "?$count=true");

				mapRequestParrameters.put("strURI",$count);
				
				/*objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParrameters);
				String line = objHandler.getStrResponse().toString().trim();
				int  linecount =Integer.parseInt(line.substring(line.indexOf("count\":")+7, line.length()-1));*/
				//ResultSet rs =  DatabaseConnection.statementExecution(connNGA, strSQLQuery);
				if (objHandler!= null)
				{
					String strResponse = objHandler.getStrResponse();
					ValidateResults.validateStatusCode(objHandler , ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), getClass().getSimpleName(), i, 1));
					if(objHandler.getStrRequestStatus().equalsIgnoreCase("Pass"))
					{
						if (!strResponse.isEmpty())
						{
							if (!(strResponse.contains("The request is invalid")))
							{
								try
								{
									ArrayList<Object> jsonList = new ArrayList<>();
									Object obj = JsonPath.read(strResponse, "$..description");
									if (obj instanceof JSONArray)
									{
										for(int j = 0; j< ((JSONArray) obj).size(); j++)
										{
											jsonList.add((Object)JsonPath.read(strResponse, "$..description["+j+"]"));
											jsonList.add((Object)JsonPath.read(strResponse, "$..allergyId["+j+"]"));
											jsonList.add((Object)JsonPath.read(strResponse, "$..allergyType["+j+"]"));
										}
									}

									System.out.println("The API Call executed is : \n" + mapRequestParrameters.get("strURI") + "\n");
									System.out.println("The JSON Response in a List: \n" + jsonList + "\n");			
									ResultSet rs = DatabaseConnection.statementExecution(connNGA, strSQLQuery);
									int intNumberOfRows = DatabaseConnection.returnNumberOfRows(rs);
									//Check the number of rows and if it is same as the number of ID's in jsonList then only proceed to validation
									if( intNumberOfRows == ((JSONArray) obj).size())
									{
										ValidateResults.resultsvalidation(jsonList, rs, objHandler);
									}
									else
									{
										System.out.println("The JSON List returned does not match with the SQL Result Set. Please Review the API Call");
										System.out.println("The JSON List size is :" + ((JSONArray) obj).size() + " and the SQL Result Set Row Count is : " + intNumberOfRows);
										Log.info("The JSON List returned does not match with the SQL Result Set. Please Review the API Call");
										objHandler.setStrRequestStatus("Fail");
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								hashMapResult.put(String.valueOf(i), new Object[]{"",objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
							}
							else
							{
								Log.info("It's a Bad Request");
								Log.info(objHandler.getStrResponse());
								hashMapResult.put(String.valueOf(i), new Object[]{objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
							}
						}
						else
						{
							Log.info("It's a Bad Request");
							Log.info(objHandler.getStrResponse());
							hashMapResult.put(String.valueOf(i), new Object[]{objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						}
					}

					else if(!(objHandler.getIntResponseCode() == HttpStatus.SC_NOT_FOUND))
					{
						Log.info("We have recieved the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, i, 2));
						softAssert.fail("We have received the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, i, 2) + " ");
						Log.info(objHandler.getStrResponse());
						hashMapResult.put(String.valueOf(i), new Object[]{"No parameters",objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
						objHandler.setStrRequestStatus("Pass");
					}
					else
					{
						softAssert.fail("The Request URI: " + objHandler.getStrRequestURI() +  " could not be found on the Server ");
						hashMapResult.put(String.valueOf(i), new Object[]{"No parameters",objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"The requested resource could not be found", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						objHandler.setStrRequestStatus("Fail");
					}

				}
				else
				{
					boolTestcaseStatus = false;
					log.info("The Request could not be sent as the Request Type is not a valid Http Method");
					softAssert.fail("The Request could not be sent as the Request Type is not a valid Http Method ");
					hashMapResult.put(String.valueOf(i), new Object[]{"No parameters",objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"The requested resource could not be found", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
				}


				if (objHandler.getStrRequestStatus().contains("Fail"))
				{
					boolTestcaseStatus = false;
					softAssert.fail("The test case fails with the data : " + "No parameters");	
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		ExcelReaderWriter.writeDetailedTestLogs(BaseTest_obsl.strDetailedTestLogs, strClassName, hashMapResult);
		if (boolTestcaseStatus)
		{
			ExcelReaderWriter.setCellValue(BaseTest_obsl.mapExecutionConfigs.get("ExecutionControllerPath"), BaseTest_obsl.mapExecutionConfigs.get("RouteRepositorySheet"), ExcelReaderWriter.findRow(BaseTest_obsl.mapExecutionConfigs.get("ExecutionControllerPath"), BaseTest_obsl.mapExecutionConfigs.get("RouteRepositorySheet"), strClassName), 4, "Pass");
		}
		else
		{
			ExcelReaderWriter.setCellValue(BaseTest_obsl.mapExecutionConfigs.get("ExecutionControllerPath"), BaseTest_obsl.mapExecutionConfigs.get("RouteRepositorySheet"), ExcelReaderWriter.findRow(BaseTest_obsl.mapExecutionConfigs.get("ExecutionControllerPath"), BaseTest_obsl.mapExecutionConfigs.get("RouteRepositorySheet"), strClassName), 4, "Fail");
		}
		softAssert.assertAll();
	}
}
