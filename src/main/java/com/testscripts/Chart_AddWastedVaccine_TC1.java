package com.testscripts;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;


import com.endpoints.Chart_AddWastedVaccine;
import com.utils.API_Request_RetObj;
import com.utils.ComputePayLoadDetails;
import com.utils.Compute_Paramters;
import com.utils.ExcelReaderWriter;
import com.utils.GeneralUtils;
import com.utils.Log;
import com.utils.RequestParameters;
import com.utils.RequestResponseHandler;
import com.utils.TestResults;
import com.utils.TestsEvaluation;

public class Chart_AddWastedVaccine_TC1 extends BaseTest
{


    SoftAssert softAssert = new SoftAssert();

    @Test
    public void chart_AddWastedVaccine_TC1()
    {

                    boolean boolTestcaseStatus = true, boolTestDataStatus = true;

                    String strClassName = getClass().getSimpleName();
                    String strSimpleClassName = Log.intializeLoggin(strClassName);
                    String strTestDataSheetName = GeneralUtils.sheetName(strSimpleClassName);

                    //Creating a new HashMap to store all the Request and Response Details
                    HashMap<String, Object[]> hashMapResult = TestResults.generateHashMapResults();

                    //Preparing the Map with all the Request details
                    Map<String, Object> mapRequestParrameters = RequestParameters.generateRequestParamsHashMap("POST", "", "");

                    RequestResponseHandler objHandler = new RequestResponseHandler();

                    try
                    {
                                    //Iterating through the Test DataSheet to run the testscript
                                    for(int k=1; k<=ExcelReaderWriter.getRowCount( BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strTestDataSheetName); k++)
                                    {

                                                    HashMap<String, String> parameters = Compute_Paramters.mapperCompute_Parameters(k, strTestDataSheetName);
                                                    mapRequestParrameters.put("strURI", Compute_Paramters.formulateFinalURI(strSimpleClassName, parameters));

//                                                    String strCountQuery = "select iov.cvx_code as CVxCode, "
//                                							+"iov.vaccine_desc as VaccineDescription, "
//                                							+"iov.lot_num as LotNumber, "
//                                							+"iov.cpt4_code as Cpt4Code, "
//                                							+"iov.manufacturer_name as ManufacturerName, "   
//                                							+"iov.manufacturer_nbr as ManufacturerNumber,  "
//                                							+ "iov.strength as Strength, "
//                                							+ "iov.units as Units, "
//                                							+ "iov.units_code as UnitsCode, "
//                                							+ "iov.dose as Dose, "
//                                							+ "iov.route as Route, "
//                                							+ "iov.route_code as RouteCode, "
//                                							+ "iov.site as Site, "
//                                							+ "iov.site_code as SiteCode, "
//                                							+ "iov.brand_name as BrandName, "
//                                							+ "ivw.wasted_reason as WastedReason, "
//                                							+  "ivw.wasted_date as WastedDate, "
//                                							+ "iov.units_code as BillingUnits, "
//                                							+ "iov.record_source as RecordSource "
//                                							+ "from imm_nor imn Inner join imm_order_vaccines iov on iov.order_num = imn.order_num inner join imm_vaccine_waste ivw on ivw.order_vaccine_id = iov.order_vaccine_id "
//                                							+ " WHERE imn.person_id = '"+parameters.get("personId")+"' and imn.enc_id= '"+parameters.get("encounterId")+"' and imn.order_num = '"+parameters.get("orderId")+"' and ivw.order_vaccine_id = '"+parameters.get("vaccineId")+"' order by ivw.create_timestamp desc";
                                                    
                                                    String strCountQuery = "select * from imm_nor imn Inner join imm_order_vaccines iov on iov.order_num = imn.order_num inner join imm_vaccine_waste ivw on ivw.order_vaccine_id = iov.order_vaccine_id "
                         							+ " WHERE imn.person_id = '"+parameters.get("personId")+"' and imn.enc_id= '"+parameters.get("encounterId")+"' and imn.order_num = '"+parameters.get("orderId")+"' and ivw.order_vaccine_id = '"+parameters.get("vaccineId")+"' order by ivw.create_timestamp desc";
                                                    HashMap<String, Object> mapPostDetails = ComputePayLoadDetails.computePayLoadMap(parameters, strCountQuery, strSimpleClassName);
                                                    mapRequestParrameters.put("strPayLoad", mapPostDetails.get("PayLoad"));

                                                    /*HashMap<String, String> parameters = Compute_Paramters.mapperCompute_Parameters(k, strTestDataSheetName);
                                                    String strPayload = "{\"Note\":\"" + parameters.get("noteBody") + "\"}";
                                                    mapRequestParrameters.put("strPayLoad", strPayload);
                                                    mapRequestParrameters.put("strURI", Compute_Paramters.formulateFinalURI(strSimpleClassName, parameters));*/

                                                    /*String strSQLQuery = "select count(pnx.note_id) as counts from patient_problems pp inner join problem_note_xref pnx on pp.uniq_id=pnx.problem_id where pp.person_id='"+parameters.get("personId")+"' and pp.uniq_id='"+parameters.get("problemId")+"' and pnx.delete_ind ='N'";
                                                    int beforeCounts = (int) DatabaseConnection.fetchColumnAsObject(connNGA,strSQLQuery,"counts");*/

                                                    //Sending the Request to get the Response
                                                    objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParrameters);

                                                    hashMapResult = TestsEvaluation.mapperExecuteTests(hashMapResult, objHandler, strSimpleClassName, strTestDataSheetName, softAssert, parameters);

                                                    Object[] testexecutionResult = hashMapResult.get(String.valueOf(k));
                                                    if(Objects.equals(testexecutionResult[testexecutionResult.length-1].toString().toLowerCase(), "pass") && objHandler.getIntResponseCode() == 201)
                                                    {
                                                                    String strResponse = objHandler.getStrResponse();
                                                                    boolTestDataStatus = Chart_AddWastedVaccine.chart_AddWastedVaccine_Test(parameters, strResponse, mapPostDetails, softAssert, strCountQuery);
                                                    }
                                                    else if(objHandler.getIntResponseCode() == 200)
                                                                    boolTestDataStatus = false; 

                                                    /*Object[] testexecutionResult = hashMapResult.get(String.valueOf(k));
                                                    if(Objects.equal(testexecutionResult[testexecutionResult.length-1].toString().toLowerCase(), "pass"))
                                                    {
                                                                    int afterCounts = (int) DatabaseConnection.fetchColumnAsObject(connNGA,strSQLQuery,"counts");
                                                                    if(afterCounts == (beforeCounts+1))
                                                                    {
                                                                                    boolTestDataStatus = Chart_AddNote.Chart_AddProblemNote_Test(parameters, softAssert);
                                                                    }
                                                                    else
                                                                    {
                                                                                    boolTestDataStatus = false;
                                                                    }
                                                    }
                                                    else
                                                                    boolTestDataStatus = false;*/

                                                    boolTestcaseStatus = boolTestcaseStatus && boolTestDataStatus;

                                                    //Update the pass/fail and update the hashMapResult
                                                    hashMapResult = TestsEvaluation.updateHashMap(hashMapResult, k, boolTestDataStatus);
                                                    objHandler = TestsEvaluation.updateStatus(objHandler, softAssert, boolTestDataStatus, parameters);
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


