package com.testscripts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.google.common.base.Objects;
import com.utils.API_Request_RetObj;
import com.utils.ComputePayLoadDetails;
import com.utils.Compute_Paramters;
import com.utils.ExcelReaderWriter;
import com.utils.GeneralUtils;
import com.utils.JsonListArray;
import com.utils.Log;
import com.utils.RequestParameters;
import com.utils.RequestResponseHandler;
import com.utils.TestResults;
import com.utils.TestsEvaluation;
import com.utils.ValidateResults;

public class Chart_AddTelephoneCall_TC1 extends BaseTest {

	SoftAssert softAssert = new SoftAssert();
	
	@Test
	public void chart_AddTelephoneCall_TC1()
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
				
				String strCountQuery = "select signoff_id from lab_order_signoff";
				HashMap<String, Object> mapPostDetails = ComputePayLoadDetails.computePayLoadMap(parameters, strCountQuery, strSimpleClassName);
				mapRequestParrameters.put("strPayLoad", mapPostDetails.get("PayLoad"));
				
				//Sending the Request to get the Response
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParrameters);

				hashMapResult = TestsEvaluation.mapperExecuteTests(hashMapResult, objHandler, strSimpleClassName, strTestDataSheetName, softAssert, parameters);

				Object[] testexecutionResult = hashMapResult.get(String.valueOf(k));
				if(Objects.equal(testexecutionResult[testexecutionResult.length-1].toString().toLowerCase(), "pass") && objHandler.getIntResponseCode() == 201)
				{
					Map<String, Object> mapRequestParrameters1 = RequestParameters.generateRequestParamsHashMap("GET", "", "");
					mapRequestParrameters1.put("strURI", Compute_Paramters.formulateFinalURI(strSimpleClassName, parameters));
					mapRequestParrameters1.put("strQueryString", "");					

					//Sending the Request to get the Response
					objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParrameters1);
					
					//hashMapResult = TestsEvaluation.mapperExecuteTests(hashMapResult, objHandler, strSimpleClassName,strTestDataSheetName, softAssert, parameters);

					//Object[] testexecutionResult1 = hashMapResult.get(String.valueOf(k));
					
					if(objHandler.getIntResponseCode() == 200)
					{
						//we are evaluating the chart person details here
						String strResponse = objHandler.getStrResponse();
						String[] jsonMetaData = {"contactType;Normal","time;Normal","spokeWithPatient;Boolean","name;Normal","relationship;Normal",
								"urgency;Normal","isPreferredContact;Boolean","taskPriority;Normal","date;Normal","phoneOther;Normal","phoneOtherExt;Normal","takenByUserName;Normal",
								"taskedToUserName;Normal","type;Normal","isAction1;Boolean","actions1;Normal","isAction2;Boolean","actions2;Normal","isAction3;Boolean",
								"actions3;Normal","isAction4;Boolean","actions4;Normal","isAction5;Boolean","actions5;Normal","isAction6;Boolean","actions6;Normal",
								"selectedPharmacy;Normal","contactTypeHeld;Normal","relationshipHeld;Normal","initialUserName;Normal","prePharmacy;Normal","isNotificationRequired;Boolean",
								"notification;Normal","returnCall;Normal","extension;Normal","email;Normal","medication;Normal","medication2;Normal","medication3;Normal","medication4;Normal",
								"medication5;Normal","medication6;Normal","medicationStrength1;Normal","medicationStrength2;Normal","medicationStrength3;Normal",
								"medicationStrength4;Normal","medicationStrength5;Normal","medicationStrength6;Normal","medicationComments1;Normal","medicationComments2;Normal",
								"medicationComments3;Normal","medicationComments4;Normal","medicationComments5;Normal","medicationComments6;Normal",
								"isMedicationNotApproved;Boolean","isMedicationNotApproved2;Boolean","isMedicationNotApproved3;Boolean",
								"isMedicationNotApproved4;Boolean","isMedicationNotApproved5;Boolean","isMedicationNotApproved6;Boolean",
								"isMedicationErx;Boolean","isMedicationErx2;Boolean","isMedicationErx3;Boolean","isMedicationErx4;Boolean",
								"isMedicationErx5;Boolean","isMedicationErx6;Boolean","isMedicationPrinted;Boolean","isMedicationPrinted2;Boolean",
								"isMedicationPrinted3;Boolean","isMedicationPrinted4;Boolean","isMedicationPrinted5;Boolean","isMedicationPrinted6;Boolean",
								"isMedicationApproved;Boolean","isMedicationApproved2;Boolean","isMedicationApproved3;Boolean","isMedicationApproved4;Boolean",
								"isMedicationApproved5;Boolean","isMedicationApproved6;Boolean","isAppointmentScheduled;Boolean","isAppointmentScheduled2;Boolean",
								"isAppointmentScheduled3;Boolean","isAppointmentScheduled4;Boolean","isAppointmentScheduled5;Boolean","isAppointmentScheduled6;Boolean",
								"isMedicationCalled;Boolean","isMedicationCalled2;Boolean","isMedicationCalled3;Boolean","isMedicationCalled4;Boolean","isMedicationCalled5;Boolean",
								"isMedicationCalled6;Boolean","isMedicationFaxed;Boolean","isMedicationFaxed2;Boolean","isMedicationFaxed3;Boolean","isMedicationFaxed4;Boolean",
								"isMedicationFaxed5;Boolean","isMedicationFaxed6;Boolean","sigDescription1;Normal","sigDescription2;Normal","sigDescription3;Normal","sigDescription4;Normal","sigDescription5;Normal",
								"sigDescription6;Normal","pharmacy1;Normal","pharmacyPhone1;Normal","pharmacyFax1;Normal","pharmacy2;Normal","pharmacyPhone2;Normal","pharmacyFax2;Normal","isScheduledAppointment;Boolean",
								"appointmentWhen;Normal","isReferral;Boolean","sendRef;Normal","isNewMed;Boolean","newMed;Normal","isAdjustedMed;Boolean","adjustedMed;Normal","sendResults;Boolean","testResults;Normal",
								"isCounselPt;Boolean","counselPt;Normal","isOtherAction;Boolean","otherAction;Normal","isComAppointment;Boolean","isComReferral;Boolean","isComNewMed;Boolean",
								"isComAdjust;Boolean","isComSendRes;Boolean","isComCounsel;Boolean","isComOther;Boolean","isAction7;Boolean","actions7;Normal","isComAct1;Boolean",
								"isComAct2;Boolean","isComAct3;Boolean","isComAct4;Boolean","isComAct5;Boolean","isComAct6;Boolean","isComAct7;Boolean","assignedUserIds;Normal",
								"assignedGroupIds;Normal","isMedicationManagement;Boolean","medicationManagementStatus;Normal","isMedicalQuestion;Boolean","medicalQuestionStatus;Normal",
								"isOutgoing;Boolean","outgoingStatus;Normal","isOther;Boolean","otherStatus;Normal"};
						
						ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, "contactType");
						Log.info("JSON LIST is :\n" + jsonList);
						
						ArrayList<Object> DBList = new ArrayList<>();
						
						DBList.add(parameters.get("contactType"));
						DBList.add(parameters.get("time"));
						DBList.add(parameters.get("spokeWithPatient").toString());
						DBList.add(parameters.get("name"));
						DBList.add(parameters.get("relationship"));
						DBList.add(parameters.get("urgency"));
						DBList.add(parameters.get("isPreferredContact").toString());
						DBList.add(parameters.get("taskPriority"));
						DBList.add(parameters.get("date"));
						DBList.add(parameters.get("phoneOther"));
						DBList.add(parameters.get("phoneOtherExt"));
						DBList.add(Objects.equal(parameters.get("takenByUserName"), "") ? null : (parameters.get("takenByUserName").toString()));
						DBList.add(Objects.equal(parameters.get("taskedToUserName"), "null") ? null : (parameters.get("taskedToUserName").toString()));
						DBList.add(parameters.get("type"));
						DBList.add(parameters.get("isAction1").toString());
						DBList.add(Objects.equal(parameters.get("actions1"), "") ? null : (parameters.get("actions1").toString()));
						DBList.add(parameters.get("isAction2").toString());
						DBList.add(Objects.equal(parameters.get("action2"), "") ? null : (parameters.get("action2").toString()));
						DBList.add(parameters.get("isAction3").toString());
						DBList.add(Objects.equal(parameters.get("actions3"), "") ? null : (parameters.get("actions3").toString()));
						DBList.add(parameters.get("isAction4").toString());
						DBList.add(Objects.equal(parameters.get("actions4"), "") ? null : (parameters.get("actions4").toString()));
						DBList.add(parameters.get("isAction5").toString());
						DBList.add(Objects.equal(parameters.get("actions5"), "") ? null : (parameters.get("actions5").toString()));
						DBList.add(parameters.get("isAction6").toString());
						DBList.add(Objects.equal(parameters.get("actions6"), "") ? null : (parameters.get("actions6").toString()));
						DBList.add(Objects.equal(parameters.get("selectedPharmacy"), "null") ? null : (parameters.get("selectedPharmacy").toString()));
						DBList.add(Objects.equal(parameters.get("contactTypeHeld"), "null") ? null : (parameters.get("contactTypeHeld").toString()));
						DBList.add(Objects.equal(parameters.get("relationshipHeld"), "null") ? null : (parameters.get("relationshipHeld").toString()));
						DBList.add(Objects.equal(parameters.get("initialUserName"), "null") ? null : (parameters.get("initialUserName").toString()));
						DBList.add(Objects.equal(parameters.get("prePharmacy"), "null") ? null : (parameters.get("prePharmacy").toString()));
						DBList.add(parameters.get("isNotificationRequired").toString());
						DBList.add(Objects.equal(parameters.get("notification"), "null") ? null : (parameters.get("notification").toString()));
						DBList.add(Objects.equal(parameters.get("returnCall"), "null") ? null : (parameters.get("returnCall").toString()));
						DBList.add(Objects.equal(parameters.get("extension"), "null") ? null : (parameters.get("extension").toString()));
						DBList.add(Objects.equal(parameters.get("email"), "null") ? null : (parameters.get("email").toString()));
						DBList.add(Objects.equal(parameters.get("medication"), "null") ? null : (parameters.get("medication").toString()));
						DBList.add(Objects.equal(parameters.get("medication2"), "null") ? null : (parameters.get("medication2").toString()));
						DBList.add(Objects.equal(parameters.get("medication3"), "null") ? null : (parameters.get("medication3").toString()));
						DBList.add(Objects.equal(parameters.get("medication4"), "null") ? null : (parameters.get("medication4").toString()));
						DBList.add(Objects.equal(parameters.get("medication5"), "null") ? null : (parameters.get("medication5").toString()));
						DBList.add(Objects.equal(parameters.get("medication6"), "null") ? null : (parameters.get("medication6").toString()));
						DBList.add(Objects.equal(parameters.get("medicationStrength1"), "null") ? null : (parameters.get("medicationStrength1").toString()));
						DBList.add(Objects.equal(parameters.get("medicationStrength2"), "null") ? null : (parameters.get("medicationStrength2").toString()));
						DBList.add(Objects.equal(parameters.get("medicationStrength3"), "null") ? null : (parameters.get("medicationStrength3").toString()));
						DBList.add(Objects.equal(parameters.get("medicationStrength4"), "null") ? null : (parameters.get("medicationStrength4").toString()));
						DBList.add(Objects.equal(parameters.get("medicationStrength5"), "null") ? null : (parameters.get("medicationStrength5").toString()));
						DBList.add(Objects.equal(parameters.get("medicationStrength6"), "null") ? null : (parameters.get("medicationStrength6").toString()));
						DBList.add(Objects.equal(parameters.get("medicationComments1"), "null") ? null : (parameters.get("medicationComments1").toString()));
						DBList.add(Objects.equal(parameters.get("medicationComments2"), "null") ? null : (parameters.get("medicationComments2").toString()));
						DBList.add(Objects.equal(parameters.get("medicationComments3"), "null") ? null : (parameters.get("medicationComments3").toString()));
						DBList.add(Objects.equal(parameters.get("medicationComments4"), "null") ? null : (parameters.get("medicationComments4").toString()));
						DBList.add(Objects.equal(parameters.get("medicationComments5"), "null") ? null : (parameters.get("medicationComments5").toString()));
						DBList.add(Objects.equal(parameters.get("medicationComments6"), "null") ? null : (parameters.get("medicationComments6").toString()));
						DBList.add(parameters.get("isMedicationNotApproved").toString());
						DBList.add(parameters.get("isMedicationNotApproved2").toString());
						DBList.add(parameters.get("isMedicationNotApproved3").toString());
						DBList.add(parameters.get("isMedicationNotApproved4").toString());
						DBList.add(parameters.get("isMedicationNotApproved5").toString());
						DBList.add(parameters.get("isMedicationNotApproved6").toString());
						DBList.add(parameters.get("isMedicationErx").toString());
						DBList.add(parameters.get("isMedicationErx2").toString());
						DBList.add(parameters.get("isMedicationErx3").toString());
						DBList.add(parameters.get("isMedicationErx4").toString());
						DBList.add(parameters.get("isMedicationErx5").toString());
						DBList.add(parameters.get("isMedicationErx6").toString());
						DBList.add(parameters.get("isMedicationPrinted").toString());
						DBList.add(parameters.get("isMedicationPrinted2").toString());
						DBList.add(parameters.get("isMedicationPrinted3").toString());
						DBList.add(parameters.get("isMedicationPrinted4").toString());
						DBList.add(parameters.get("isMedicationPrinted5").toString());
						DBList.add(parameters.get("isMedicationPrinted6").toString());
						DBList.add(parameters.get("isMedicationApproved").toString());
						DBList.add(parameters.get("isMedicationApproved2").toString());
						DBList.add(parameters.get("isMedicationApproved3").toString());
						DBList.add(parameters.get("isMedicationApproved4").toString());
						DBList.add(parameters.get("isMedicationApproved5").toString());
						DBList.add(parameters.get("isMedicationApproved6").toString());
						DBList.add(parameters.get("isAppointmentScheduled").toString());
						DBList.add(parameters.get("isAppointmentScheduled2").toString());
						DBList.add(parameters.get("isAppointmentScheduled3").toString());
						DBList.add(parameters.get("isAppointmentScheduled4").toString());
						DBList.add(parameters.get("isAppointmentScheduled5").toString());
						DBList.add(parameters.get("isAppointmentScheduled6").toString());
						DBList.add(parameters.get("isMedicationCalled").toString());
						DBList.add(parameters.get("isMedicationCalled2").toString());
						DBList.add(parameters.get("isMedicationCalled3").toString());
						DBList.add(parameters.get("isMedicationCalled4").toString());
						DBList.add(parameters.get("isMedicationCalled5").toString());
						DBList.add(parameters.get("isMedicationCalled6").toString());
						DBList.add(parameters.get("isMedicationFaxed").toString());
						DBList.add(parameters.get("isMedicationFaxed2").toString());
						DBList.add(parameters.get("isMedicationFaxed3").toString());
						DBList.add(parameters.get("isMedicationFaxed4").toString());
						DBList.add(parameters.get("isMedicationFaxed5").toString());
						DBList.add(parameters.get("isMedicationFaxed6").toString());
						DBList.add(Objects.equal(parameters.get("sigDescription1"), "null") ? null : (parameters.get("sigDescription1").toString()));
						DBList.add(Objects.equal(parameters.get("sigDescription2"), "null") ? null : (parameters.get("sigDescription2").toString()));
						DBList.add(Objects.equal(parameters.get("sigDescription3"), "null") ? null : (parameters.get("sigDescription3").toString()));
						DBList.add(Objects.equal(parameters.get("sigDescription4"), "null") ? null : (parameters.get("sigDescription4").toString()));
						DBList.add(Objects.equal(parameters.get("sigDescription5"), "null") ? null : (parameters.get("sigDescription5").toString()));
						DBList.add(Objects.equal(parameters.get("sigDescription6"), "null") ? null : (parameters.get("sigDescription6").toString()));
						DBList.add(Objects.equal(parameters.get("pharmacy1"), "null") ? null : (parameters.get("pharmacy1").toString()));
						DBList.add(Objects.equal(parameters.get("pharmacyPhone1"), "null") ? null : (parameters.get("pharmacyPhone1").toString()));
						DBList.add(Objects.equal(parameters.get("pharmacyFax1"), "null") ? null : (parameters.get("pharmacyFax1").toString()));
						DBList.add(Objects.equal(parameters.get("pharmacy2"), "null") ? null : (parameters.get("pharmacy2").toString()));
						DBList.add(Objects.equal(parameters.get("pharmacyPhone2"), "null") ? null : (parameters.get("pharmacyPhone2").toString()));
						DBList.add(Objects.equal(parameters.get("pharmacyFax2"), "null") ? null : (parameters.get("pharmacyFax2").toString()));
						DBList.add(parameters.get("isScheduledAppointment"));
						DBList.add(Objects.equal(parameters.get("appointmentWhen"), "") ? null : (parameters.get("appointmentWhen").toString()));
						DBList.add(parameters.get("isReferral").toString());
						DBList.add(Objects.equal(parameters.get("sendRef"), "null") ? null : (parameters.get("sendRef").toString()));
						DBList.add(parameters.get("isNewMed").toString());
						DBList.add(Objects.equal(parameters.get("newMed"), "null") ? null : (parameters.get("newMed").toString()));
						DBList.add(parameters.get("isAdjustedMed").toString());
						DBList.add(Objects.equal(parameters.get("adjustedMed"), "null") ? null : (parameters.get("adjustedMed").toString()));
						DBList.add(parameters.get("SendResults").toString());
						DBList.add(Objects.equal(parameters.get("TestResults"), "null") ? null : (parameters.get("TestResults").toString()));
						DBList.add(parameters.get("isCounselPt").toString());
						DBList.add(Objects.equal(parameters.get("counselPt"), "null") ? null : (parameters.get("counselPt").toString()));
						DBList.add(parameters.get("isOtherAction").toString());
						DBList.add(Objects.equal(parameters.get("otherAction"), "null") ? null : (parameters.get("otherAction").toString()));
						DBList.add(parameters.get("isComAppointment").toString());
						DBList.add(parameters.get("isComReferral").toString());
						DBList.add(parameters.get("isComNewMed").toString());
						DBList.add(parameters.get("isComAdjust").toString());
						DBList.add(parameters.get("isComSendRes").toString());
						DBList.add(parameters.get("isComCounsel").toString());
						DBList.add(parameters.get("isComOther").toString());
						DBList.add(parameters.get("isAction7").toString());
						DBList.add(Objects.equal(parameters.get("actions7"), "null") ? null : (parameters.get("actions7").toString()));
						DBList.add(parameters.get("isComAct1").toString());
						DBList.add(parameters.get("isComAct2").toString());
						DBList.add(parameters.get("isComAct3").toString());
						DBList.add(parameters.get("isComAct4").toString());
						DBList.add(parameters.get("isComAct5").toString());
						DBList.add(parameters.get("isComAct6").toString());
						DBList.add(parameters.get("isComAct7").toString());
						DBList.add(Objects.equal(parameters.get("assignedUserIds"), "null") ? null : (parameters.get("assignedUserIds").toString()));
						DBList.add(Objects.equal(parameters.get("assignedGroupIds"), "null") ? null : (parameters.get("assignedGroupIds").toString()));
						DBList.add(parameters.get("isMedicationManagement").toString());
						DBList.add(Objects.equal(parameters.get("medicationManagementStatus"), "null") ? null : (parameters.get("medicationManagementStatus").toString()));
						DBList.add(Objects.equal(parameters.get("isMedicalQuestion"), "null") ? null : (parameters.get("isMedicalQuestion").toString()));
						DBList.add(Objects.equal(parameters.get("medicalQuestionStatus"), "null") ? null : (parameters.get("medicalQuestionStatus").toString()));
						DBList.add(Objects.equal(parameters.get("isOutgoing"), "null") ? null : (parameters.get("isOutgoing").toString()));
						DBList.add(Objects.equal(parameters.get("outgoingStatus"), "null") ? null : (parameters.get("outgoingStatus").toString()));
						DBList.add(Objects.equal(parameters.get("isOther"), "null") ? null : (parameters.get("isOther").toString()));
						DBList.add(Objects.equal(parameters.get("otherStatus"), "null") ? null : (parameters.get("otherStatus").toString()));
						boolTestDataStatus = ValidateResults.resultValidation(jsonList, DBList);
					}
					else
					{
						boolTestDataStatus = false;
					}

					
				}
				
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
