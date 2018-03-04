package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetPatientProcedure
{

	/*
	 * ********************************************************************************
	 * Class Name						:Chart_GetPatientProcedure
	 * Created By						: 
	 * Created Date						: 
	 * Modified Date					:
	 * Modified By						:
	 * Parameters						: 
	 * Test Case ID						:
	 * Test Case Author					: 
	 * Test Case Modified By			:
	 * Test Case Reviewed By			:
	 * Purpose							: Route will fetch the allergy chart details
	 ***********************************************************************************
	 */

	public static Boolean chart_GetPatientProcedure_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		//System.out.println(strResponse);
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{ 
				String strQuery = "SELECT uniq_id as id,"
						+ " enc_id as encounterId, "
						+ " person_id as personId, "
						+ " service_item_lib_id as serviceItemLibraryId," 
						+ " service_item_group_name as serviceItemGroupName, "   
						+ " service_item_group_seq_num as serviceItemGroupSequenceNumber, "
						+  " service_item_id as serviceItemId,"
						+ " service_item_desc as serviceItemDescription,"
						+ " cpt4_code_id as cpt4Code,"
						+ " service_date as serviceDate,"
						+ "provider_id as providerId, " 
						+ " referring_provider_id as referringProviderId, "
						+ " referring_provider_name as referringProviderName, "
						+ "assisting_provider_id as assistingProviderId, "
						+ " date_resolved as dateResolved, "
						+ "modifier_id_1 as modifierId1, "
						+ "modifier_id_2 as modifierId2, "
						+ " modifier_id_3 as modifierId3, "
						+ " modifier_id_4 as modifierId4, "
						+ " diagnosis_code_id_1 as diagnosisCodeId1, "
						+ " diagnosis_code_lib_id_1 as diagnosisCodeLibraryId1, "
						+ " diagnosis_code_id_2 as diagnosisCodeId2, "
						+ " diagnosis_code_lib_id_2 as diagnosisCodeLibraryId2,"
						+ " diagnosis_code_id_3 as diagnosisCodeId3, "
						+ " diagnosis_code_lib_id_3 as diagnosisCodeLibraryId3, "
						+ " diagnosis_code_id_4 as diagnosisCodeId4, "
						+ " diagnosis_code_lib_id_4 as diagnosisCodeLibraryId4,"
						+ " place_of_service as placeOfService, "
						+ " accept_assign_ind as hasAcceptedAssignment, "
						+ " units as units, "
						+ " location_id as locationId, "
						+ " payer_id as payerId, "
						+ " amount as amount, "
						+ " suppress_billing_ind as isBillingSuppressed, "
						+ " tooth as tooth, "
						+ " surface as surface, "
						+ " quadrant as quadrant, "
						+ " note as note, "
						+ " start_time as startTime, "
						+ " stop_time as stopTime, "
						+ " total_time as totalTime, "
						+ " base_unit as baseUnit, "
						+ " alt_code as alternateCode, "
						+ " anesthesia_billing_ind as isAnesthesiaBilling, "
						+ " diagnosis_code_id_5 as diagnosisCodeId5, "
						+ " diagnosis_code_lib_id_5 as diagnosisCodeLibraryId5, "
						+ " diagnosis_code_id_6 as diagnosisCodeId6, "
						+ " diagnosis_code_lib_id_6 as diagnosisCodeLibraryId6, "
						+ " diagnosis_code_id_7 as diagnosisCodeId7, "
						+ " diagnosis_code_lib_id_7 as diagnosisCodeLibraryId7, "
						+ " diagnosis_code_id_8 as diagnosisCodeId8, "
						+ " diagnosis_code_lib_id_8 as diagnosisCodeLibraryId8, "
						+ " source_product_id as sourceProductId, "
						+ " rx_on_file_ind as isRxOnFile, "
						+ " national_drug_code as nationalDrugCode, "
						+ " behavioral_billing_ind as isBehavioralBilling, "
						+ " diagnosis_code_id_9 as diagnosisCodeId9, "
						+ " diagnosis_code_lib_id_9 as diagnosisCodeLibraryId9, "
						+ " diagnosis_code_id_10 as diagnosisCodeId10, "
						+ " diagnosis_code_lib_id_10 as diagnosisCodeLibraryId10 , "
						+ " diagnosis_code_id_11 as diagnosisCodeId11, "
						+ " diagnosis_code_lib_id_11 as diagnosisCodeLibraryId11, "
						+ " diagnosis_code_id_12 as diagnosisCodeId12, "
						+ " diagnosis_code_lib_id_12 as diagnosisCodeLibraryId12, "
						+ " snomed_concept_id as snomedConceptId, "
						+ " snomed_concept_id2 as snomedConceptId2, "
						+ " snomed_concept_id3 as snomedConceptId3, "
						+ " snomed_concept_id4 as snomedConceptId4, "
						+ " dental_ind as isDental, "
						+ " supernumerary_ind as isSupernumerary, "
						+ " surface_descriptor as surfaceDescriptor, "
						+ " defective_ind as isDefective, "
						+ " not_applicable_date as notApplicableDate, "
						+ " approval_date as approvalDate, "
						+ " medical_director_id as medicalDirectorId, "
						+ " asa_crosswalk_lib_id as asaCrosswalkLibraryId, "
						+ " surgical_proc_code_id as surgicalProcedureCodeId, "
						+ " surgical_proc_desc as surgicalProcedureDescription, "
						+ " basis_of_measure as basisOfMeasure, "
						+ " national_drug_units as nationalDrugUnits, "
						+ " teeth as teeth,	"
						+ " enterprise_id as enterpriseId,"
						+ " practice_id as practiceId, "
						+ "created_by as createdBy, "
						+ " create_timestamp as createTimestamp, "
						+ " create_timestamp_tz as createTimestampTimezone, "
						+ " modified_by as modifiedBy,	"
						+ " modify_timestamp as modifyTimestamp, "
						+ " modify_timestamp_tz as modifyTimestampTimezone"
						+ " from patient_procedure  "
						+ " where person_id='"+parameters.get("personId")+"' and  enc_id= '"+parameters.get("encounterId")+"' and uniq_id ='"+parameters.get("procedureId")+"' ";

				
						
				
						
		
				//
				String[] jsonMetaData = {"id", "encounterId","personId","serviceItemLibraryId","serviceItemGroupName","serviceItemGroupSequenceNumber","serviceItemId","serviceItemDescription", "cpt4Code","serviceDate", "providerId" ,"referringProviderId", "referringProviderName	" ,"assistingProviderId", "dateResolved" , "modifierId1","modifierId2","modifierId3","modifierId4","diagnosisCodeId1", "diagnosisCodeLibraryId1", "diagnosisCodeId2" , "diagnosisCodeLibraryId2","diagnosisCodeId3","diagnosisCodeLibraryId3","diagnosisCodeId4","diagnosisCodeLibraryId4", "placeOfService", "hasAcceptedAssignment" ,"units","locationId","payerId", "amount", "isBillingSuppressed","tooth","surface","quadrant", "note", "startTime", "stopTime","totalTime","baseUnit","alternateCode", "isAnesthesiaBilling", "diagnosisCodeId5" , "diagnosisCodeLibraryId5", "diagnosisCodeId6", "diagnosisCodeLibraryId6","diagnosisCodeId7","diagnosisCodeLibraryId7","diagnosisCodeId8", "diagnosisCodeLibraryId8", "sourceProductId" ,"isRxOnFile","nationalDrugCode", "isBehavioralBilling", "diagnosisCodeId9" , "diagnosisCodeLibraryId9", "diagnosisCodeId10" ,"diagnosisCodeLibraryId10","diagnosisCodeId11", "diagnosisCodeLibraryId11", "diagnosisCodeId12" , "diagnosisCodeLibraryId12" ,"snomedConceptId","snomedConceptId2", "snomedConceptId3", "snomedConceptId4" ,"isDental","isSupernumerary", "surfaceDescriptor", "isDefective" , "notApplicableDate", "approvalDate" ,"medicalDirectorId","asaCrosswalkLibraryId", "surgicalProcedureCodeId", "surgicalProcedureDescription" , "basisOfMeasure" ,"nationalDrugUnits","teeth", "isDeleted", "enterpriseId" , "practiceId" , "createdBy" ,"createTimestamp","createTimestampTimezone", "modifiedBy", "modifyTimestamp","modifyTimestampTimezone"};

				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..id", jsonList);
					//objHandler = FinalAssertions.assertResponseCount(jsonMetaData, objHandler, DatabaseConnection.statementExecution(connNGA, strQuery), "id");
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
