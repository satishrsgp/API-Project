package com.endpoints;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.utils.API_Request_RetObj;
import com.utils.DatabaseConnection;
import com.utils.ExcelReaderWriter;
import com.utils.FinalAssertions;
import com.utils.Log;
import com.utils.RequestParameters;
import com.utils.RequestResponseHandler;
import com.utils.TestResults;
import com.utils.ValidateResults;

/*
 * ********************************************************************
 * Class Name						: Person_DocumentId_GET
 * Created By						: Anil Kumar Veluru
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: personId and documentId
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							: Route will fetch details of a document based on the personId and documentId
 * ******************************************************************
 */

public class Person_DocumentId_GET extends BaseTest_obsl
{
	SoftAssert softAssert = new SoftAssert();

	@SuppressWarnings("null")
	@Test
	public void person_DocumentId_GET()
	{
		String strClassName = getClass().getSimpleName();
		System.out.println("Running test case " + strClassName+ " with Thread Id:- " + Thread.currentThread().getId());
		Log.info("Running test case " + strClassName+ " with Thread Id:- " + Thread.currentThread().getId());
		boolean boolTestcaseStatus = true;
		
		//Creating a new HashMap to store all the Request and Response Details
		HashMap<String, Object[]> hashMapResult = TestResults.generateHashMapResults();
		
		//Preparing the Map with all the Request details
		Map<String, Object> mapRequestParrameters = RequestParameters.generateRequestParamsHashMap("GET", "", "");

		RequestResponseHandler objHandler = new RequestResponseHandler();
		try
		{
			//Iterating through the Test DataSheet to run the testscript
			for(int k=1; k<=ExcelReaderWriter.getRowCount( BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName); k++)
			{
				String strPersonId = ExcelReaderWriter.getCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 0);
				if (strPersonId.equalsIgnoreCase("null"))
					strPersonId="";
				String strDocumentId = ExcelReaderWriter.getCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 1);
				if (strDocumentId.equalsIgnoreCase("null"))
					strDocumentId="";

				mapRequestParrameters.put("strURI", BaseTest_obsl.mapURLRepo.get(strClassName).replace("personId", strPersonId).replace("documentId", strDocumentId));
				
				//Sending the Request to get the Response
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParrameters);

				if (objHandler != null)
				{
					String strResponse = objHandler.getStrResponse();
					ValidateResults.validateStatusCode(objHandler, ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2));
					if(objHandler.getStrRequestStatus().equalsIgnoreCase("Pass"))
					{
						if (!strResponse.isEmpty())
						{
							if (!(strResponse.contains("is not valid for Guid")||strResponse.contains("Date parameter is in an incorrect format.")))
							{
								//StringBuilder strSQLQuery= new StringBuilder();
								String strSQLQuery = "SELECT * from (SELECT pd.document_id  AS id, pd.document_desc AS description, 1  AS itemtype, 'EHRDocument' AS itemtypedescription, "
										+ "pd.create_timestamp    AS createtimestamp, pd.create_timestamp_tz AS createtimestamptimezone, pd.modify_timestamp AS modifytimestamp, "
										+ "pd.modify_timestamp_tz AS modifytimestamptimezone, pd.enc_id AS encounterid, e.enc_timestamp AS encounterTimestamp, d.doc_type_id AS docTypeId "
										+ "FROM   patient_documents pd "
										+ "INNER JOIN patient_encounter e ON pd.enc_id = e.enc_id "
										+ "LEFT OUTER JOIN document d ON pd.document_id = d.document_id "
										+ "WHERE  pd.person_id='" + strPersonId + "' and pd.document_id='" + strDocumentId + "' "
										+ "UNION "
										+ "SELECT i.image_id  AS id, i.image_desc AS description, 2 AS itemtype, 'EHRImage' AS itemtypedescription, "
										+ "i.create_timestamp    AS createtimestamp, i.create_timestamp_tz AS createtimestamptimezone, i.modify_timestamp    AS modifytimestamp, "
										+ "i.modify_timestamp_tz AS modifytimestamptimezone, i.enc_id AS encounterid, e.enc_timestamp AS encounterTimestamp, d.doc_type_id AS docTypeId "
										+ "FROM   patient_images i "
										+ "INNER JOIN patient_encounter e ON i.enc_id = e.enc_id "
										+ "LEFT OUTER JOIN document d ON i.image_id = d.document_id "
										+ "WHERE  i.person_id='" + strPersonId + "' and i.image_id='" + strDocumentId + "' "
										+ "UNION "
										+ "SELECT  pii.document_id AS id, dtm.description AS description, 0  AS itemtype, 'ICS' AS itemtypedescription, "
										+ "pii.create_timestamp    AS createtimestamp, pii.create_timestamp_tz AS createtimestamptimezone, pii.modify_timestamp    AS modifytimestamp, "
										+ "pii.modify_timestamp_tz AS modifytimestamptimezone, pii.enc_id  AS encounterid, e.enc_timestamp AS encounterTimestamp, d.doc_type_id AS docTypeId "
										+ "FROM patient_ics_images pii "
										+ "INNER JOIN patient_encounter e ON pii.enc_id = e.enc_id "
										+ "INNER JOIN document d ON         pii.document_id=d.document_id  "
										+ "INNER JOIN doc_type_mstr dtm ON         d.doc_type_id=dtm.doc_type_id "
										+ "WHERE pii.person_id='" + strPersonId + "' and pii.document_id='" + strDocumentId + "') " + "tbl";
								try
								{	
									String[] jsonMetaData = {"id","description","itemType","itemTypeDescription","createTimeStamp","createTimestampTimezone","modifyTimeStamp","modifyTimestampTimezone","encounterId","encounterTimestamp","docTypeId"};

									objHandler = FinalAssertions.assertResponseCount(jsonMetaData, objHandler, DatabaseConnection.statementExecution(connNGA, strSQLQuery), "id");
								} 
								catch (Exception e)
								{
									e.printStackTrace();
                                    log.info("Error in Test Class and the detail are : \n" + e.getMessage());
                                    softAssert.fail("Error in Test Script please look at logs");
                                    objHandler.setStrRequestStatus("Fail");
								}
								hashMapResult.put(String.valueOf(k), new Object[]{"person_id= " + strPersonId + " and document_id= " +strDocumentId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
							}
							else
							{
								Log.info("It's a Bad Request");
								Log.info(objHandler.getStrResponse());
								hashMapResult.put(String.valueOf(k), new Object[]{"person_id= " + strPersonId + " and document_id= " +strDocumentId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
							}
						}
						else
						{
							Log.info("It's a Bad Request");
							Log.info(objHandler.getStrResponse());
							hashMapResult.put(String.valueOf(k), new Object[]{"person_id= " + strPersonId + " and document_id= " +strDocumentId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						}
					}

					else if(! (objHandler.getIntResponseCode() == HttpStatus.SC_NOT_FOUND))
					{
						Log.info("We have recieved the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2));
						softAssert.fail("We have received the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2) + " ");
						Log.info(objHandler.getStrResponse());
						hashMapResult.put(String.valueOf(k), new Object[]{"person_id= " + strPersonId + " and document_id= " +strDocumentId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
						objHandler.setStrRequestStatus("Pass");
					}
					else
					{
						softAssert.fail("The Request URI: " + objHandler.getStrRequestURI() +  " could not be found on the Server ");
						hashMapResult.put(String.valueOf(k), new Object[]{"person_id= " + strPersonId + " and document_id= " +strDocumentId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"The requested resource could not be found", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						objHandler.setStrRequestStatus("Fail");
					}
				}
				else
				{
					boolTestcaseStatus = false;
					log.info("The Request could not be sent as the Request Type is not a valid Http Method");
					softAssert.fail("The Request could not be sent as the Request Type is not a valid Http Method ");
					hashMapResult.put(String.valueOf(k), new Object[]{"person_id= " + strPersonId + " and document_id= " +strDocumentId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"The requested resource could not be found", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
				}
				if (objHandler.getStrRequestStatus().contains("Fail"))
				{
					boolTestcaseStatus = false;
					softAssert.fail("Test case fails with data: "+"person_id= " + strPersonId + " and document_id= " +strDocumentId);
				}
				objHandler = null;
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