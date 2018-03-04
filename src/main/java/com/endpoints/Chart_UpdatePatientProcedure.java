package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import org.testng.asserts.SoftAssert;
import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_UpdatePatientProcedure
{
	public static Boolean chart_UpdatePatientProcedure_Test(HashMap<String, String> parameters, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strSQLQuery = "select datediff(second,create_timestamp,modify_timestamp) as diff,modified_by as mb "
					+ "from patient_procedure where person_id='"+parameters.get("personId")+"' and  enc_id= '"+parameters.get("encounterId")+"' and uniq_id='"+parameters.get("procedureId")+"'";
			Object objIntDateDiff = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "diff");
			Object objIntModifiedBy = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "mb");
			if(Integer.valueOf(objIntDateDiff.toString()) > 0)
			{
				if(Objects.equals(objIntModifiedBy.toString(), System.getProperty("LoggedInUserId")))
				{
					String strQuery1 =  " SELECT top 1 service_item_group_name as serviceItemGroupName, "			   
							+ "service_item_group_seq_num as serviceItemGroupSequenceNumber,"
							+  " service_item_id as serviceItemId, "
							+ " service_item_desc as serviceItemDescription, "
							+ " cpt4_code_id as cpt4Code, "
							+ " service_date as serviceDate, "
							+ " provider_id as providerId, " 
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
							+ " teeth as teeth "					
							+ " from patient_procedure  "
							+ "  WHERE person_id = '"+parameters.get("personId")+"' and  enc_id= '"+parameters.get("encounterId")+"' and uniq_id= '"+parameters.get("procedureId")+"' order by create_timestamp desc";
					//ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
					ArrayList<Object> listTestData  = new ArrayList<>();
					listTestData.add(Objects.equals(parameters.get("ServiceItemGroupName"), null) ? null : (parameters.get("ServiceItemGroupName").toString()));		
					//	listTestData.add(parameters.get("serviceItemGroupName").toString());
						listTestData.add(Objects.equals(parameters.get("ServiceItemGroupSequenceNumber"), "") ? null : (parameters.get("ServiceItemGroupSequenceNumber").toString()));
					//listTestData.add(parameters.get("ServiceItemGroupSequenceNumber"), null) ? null : (parameters.get("ServiceItemGroupSequenceNumber")));
						listTestData.add(Objects.equals(parameters.get("ServiceItemId"), null) ? null : (parameters.get("ServiceItemId").toString()));	
						listTestData.add(Objects.equals(parameters.get("ServiceItemDescription"), null) ? null : (parameters.get("ServiceItemDescription").toString()));	
						listTestData.add(Objects.equals(parameters.get("Cpt4Code"), null) ? null : (parameters.get("Cpt4Code").toString()));	
						//listTestData.add(parameters.get("RecallPlanId").toString());
						listTestData.add(Objects.equals(parameters.get("ServiceDate"), null) ? null : (parameters.get("ServiceDate").toString().split("T")[0]).replace("-", ""));
						listTestData.add(Objects.equals(parameters.get("ProviderId"), null) ? null : (parameters.get("ProviderId").toString()));	
						listTestData.add(Objects.equals(parameters.get("ReferringProviderId"), null) ? null : (parameters.get("ReferringProviderId").toString()));	
						listTestData.add(Objects.equals(parameters.get("ReferringProviderName"), null) ? null : (parameters.get("ReferringProviderName").toString()));	
						listTestData.add(Objects.equals(parameters.get("AssistingProviderId"), null) ? null : (parameters.get("AssistingProviderId").toString()));	
						listTestData.add(Objects.equals(parameters.get("DateResolved"), null) ? null : (parameters.get("DateResolved").toString().split("T")[0]).replace("-", ""));
						listTestData.add(Objects.equals(parameters.get("ModifierId1"), null) ? null : (parameters.get("ModifierId1").toString()));	
						listTestData.add(Objects.equals(parameters.get("ModifierId2"), null) ? null : (parameters.get("ModifierId2").toString()));	
						listTestData.add(Objects.equals(parameters.get("ModifierId3"), null) ? null : (parameters.get("ModifierId3").toString()));
						listTestData.add(Objects.equals(parameters.get("ModifierId4"), null) ? null : (parameters.get("ModifierId4").toString()));	
						listTestData.add(Objects.equals(parameters.get("DiagnosisCodeId1"), null) ? null : (parameters.get("DiagnosisCodeId1").toString()));	
						listTestData.add(Objects.equals(parameters.get("DiagnosisCodeLibraryId1"), "") ? null : (parameters.get("DiagnosisCodeLibraryId1").toString()));
						listTestData.add(Objects.equals(parameters.get("DiagnosisCodeId2"), null) ? null : (parameters.get("DiagnosisCodeId2").toString()));	
						listTestData.add(Objects.equals(parameters.get("DiagnosisCodeLibraryId2"), "") ? null : (parameters.get("DiagnosisCodeLibraryId2").toString()));	
						listTestData.add(Objects.equals(parameters.get("DiagnosisCodeId3"), null) ? null : (parameters.get("DiagnosisCodeId3").toString()));
						listTestData.add(Objects.equals(parameters.get("DiagnosisCodeLibraryId3"), "") ? null : (parameters.get("DiagnosisCodeLibraryId3").toString()));	
						listTestData.add(Objects.equals(parameters.get("DiagnosisCodeId4"), null) ? null : (parameters.get("DiagnosisCodeId4").toString()));	
						listTestData.add(Objects.equals(parameters.get("DiagnosisCodeLibraryId4"), "") ? null : (parameters.get("DiagnosisCodeLibraryId4").toString()));
						listTestData.add(Objects.equals(parameters.get("PlaceOfService"), null) ? null : (parameters.get("PlaceOfService").toString()));	
						listTestData.add(Objects.equals(parameters.get("HasAcceptedAssignment"), "false") ? "N" : "Y");
						listTestData.add(parameters.get("Units").concat(".00"));
						
						//listTestData.add(parameters.get("Units"));
						listTestData.add(Objects.equals(parameters.get("LocationId"), null) ? null : (parameters.get("LocationId").toString()));	
						listTestData.add(Objects.equals(parameters.get("PayerId"), null) ? null : (parameters.get("PayerId").toString()));
						listTestData.add(parameters.get("Amount").concat(".00"));
						listTestData.add(Objects.equals(parameters.get("IsBillingSuppressed"), "false") ? "N" : "Y");
						listTestData.add(Objects.equals(parameters.get("Tooth"), null) ? null : (parameters.get("Tooth").toString()));	
						listTestData.add(Objects.equals(parameters.get("Surface"), null) ? null : (parameters.get("Surface").toString()));	
						listTestData.add(Objects.equals(parameters.get("Quadrant"), null) ? null : (parameters.get("Quadrant").toString()));
						listTestData.add(Objects.equals(parameters.get("Note"), null) ? null : (parameters.get("Note").toString()));	
						listTestData.add(Objects.equals(parameters.get("StartTime"), null) ? null : (parameters.get("StartTime").toString()));	
						listTestData.add(Objects.equals(parameters.get("StopTime"), null) ? null : (parameters.get("StopTime").toString()));
						listTestData.add(Objects.equals(parameters.get("TotalTime"), null) ? null : (parameters.get("TotalTime").toString()));
					
						listTestData.add(parameters.get("BaseUnit"));
						listTestData.add(Objects.equals(parameters.get("AlternateCode"), null) ? null : (parameters.get("AlternateCode").toString()));	
						listTestData.add(Objects.equals(parameters.get("IsAnesthesiaBilling"), "false") ? "N" : "Y");
						listTestData.add(Objects.equals(parameters.get("DiagnosisCodeId5"), null) ? null : (parameters.get("DiagnosisCodeId5").toString()));	
						listTestData.add(Objects.equals(parameters.get("DiagnosisCodeLibraryId5"), "") ? null : (parameters.get("DiagnosisCodeLibraryId5").toString()));
						listTestData.add(Objects.equals(parameters.get("DiagnosisCodeId6"), null) ? null : (parameters.get("DiagnosisCodeId6").toString()));	
						listTestData.add(Objects.equals(parameters.get("DiagnosisCodeLibraryId6"), "") ? null : (parameters.get("DiagnosisCodeLibraryId6").toString()));	
						listTestData.add(Objects.equals(parameters.get("DiagnosisCodeId7"),null) ? null : (parameters.get("DiagnosisCodeId7").toString()));
						listTestData.add(Objects.equals(parameters.get("DiagnosisCodeLibraryId7"), "") ? null : (parameters.get("DiagnosisCodeLibraryId7").toString()));	
						listTestData.add(Objects.equals(parameters.get("DiagnosisCodeId8"), null) ? null : (parameters.get("DiagnosisCodeId8").toString()));	
						listTestData.add(Objects.equals(parameters.get("DiagnosisCodeLibraryId8"), "") ? null : (parameters.get("DiagnosisCodeLibraryId8").toString()));
						listTestData.add(Objects.equals(parameters.get("SourceProductId"), null) ? null : (parameters.get("SourceProductId").toString()));
						listTestData.add(Objects.equals(parameters.get("IsRxOnFile"), "false") ? "N" : "Y");
						listTestData.add(Objects.equals(parameters.get("NationalDrugCode"), null) ? null : (parameters.get("NationalDrugCode").toString()));
						listTestData.add(Objects.equals(parameters.get("IsBehavioralBilling"), "false") ? "N" : "Y");
						listTestData.add(Objects.equals(parameters.get("DiagnosisCodeId9"), null) ? null : (parameters.get("DiagnosisCodeId9").toString()));	
						listTestData.add(Objects.equals(parameters.get("DiagnosisCodeLibraryId9"), "") ? null : (parameters.get("DiagnosisCodeLibraryId9").toString()));
						listTestData.add(Objects.equals(parameters.get("DiagnosisCodeId10"), null) ? null : (parameters.get("DiagnosisCodeId10").toString()));	
						listTestData.add(Objects.equals(parameters.get("DiagnosisCodeLibraryId10"), "") ? null : (parameters.get("DiagnosisCodeLibraryId10").toString()));	
						listTestData.add(Objects.equals(parameters.get("DiagnosisCodeId11"), null) ? null : (parameters.get("DiagnosisCodeId11").toString()));
						listTestData.add(Objects.equals(parameters.get("DiagnosisCodeLibraryId11"), "") ? null : (parameters.get("DiagnosisCodeLibraryId11").toString()));	
						listTestData.add(Objects.equals(parameters.get("DiagnosisCodeId12"), null) ? null : (parameters.get("DiagnosisCodeId12").toString()));	
						listTestData.add(Objects.equals(parameters.get("DiagnosisCodeLibraryId12"), "") ? null : (parameters.get("DiagnosisCodeLibraryId12").toString()));
						
						listTestData.add(Objects.equals(parameters.get("SnomedConceptId"), null) ? null : (parameters.get("SnomedConceptId").toString()));
						listTestData.add(Objects.equals(parameters.get("SnomedConceptId2"), null) ? null : (parameters.get("SnomedConceptId2").toString()));	
						listTestData.add(Objects.equals(parameters.get("SnomedConceptId3"), null) ? null : (parameters.get("SnomedConceptId3").toString()));	
						listTestData.add(Objects.equals(parameters.get("SnomedConceptId4"), null) ? null : (parameters.get("SnomedConceptId4").toString()));
						listTestData.add(Objects.equals(parameters.get("IsDental"), "false") ? "N" : "Y");
						listTestData.add(Objects.equals(parameters.get("IsSupernumerary"), "false") ? "N" : "Y");
						listTestData.add(parameters.get("SurfaceDescriptor"));
						listTestData.add(Objects.equals(parameters.get("IsDefective"), "false") ? "N" : "Y");
						//listTestData.add(Objects.equals(parameters.get("NotApplicableDate"), null) ? null : (parameters.get("NotApplicableDate").toString().split("T")[0]).replace("-", ""));
						listTestData.add(Objects.equals(parameters.get("NotApplicableDate"), null) ? null : (parameters.get("NotApplicableDate").toString().replace("T", " ").replace("Z", "")));
						listTestData.add(Objects.equals(parameters.get("ApprovalDate"), null) ? null : (parameters.get("ApprovalDate").toString().replace("T", " ").replace("Z", "")));
						//listTestData.add(Objects.equals(parameters.get("ApprovalDate"), null) ? null : (parameters.get("ApprovalDate").toString().split("T")[0]).replace("-", ""));
				
			
					  listTestData.add(Objects.equals(parameters.get("MedicalDirectorId"), null) ? null : (parameters.get("MedicalDirectorId").toString()));	
					  listTestData.add(Objects.equals(parameters.get("AsaCrosswalkLibraryId"), null) ? null : (parameters.get("AsaCrosswalkLibraryId").toString()));	
						
					  listTestData.add(Objects.equals(parameters.get("SurgicalProcedureCodeId"), null) ? null : (parameters.get("SurgicalProcedureCodeId").toString()));	
					  listTestData.add(Objects.equals(parameters.get("SurgicalProcedureDescription"), null) ? null : (parameters.get("SurgicalProcedureDescription").toString()));	
					  listTestData.add(Objects.equals(parameters.get("BasisOfMeasure"), null) ? null : (parameters.get("BasisOfMeasure").toString()));	
					  listTestData.add(Objects.equals(parameters.get("NationalDrugUnits"), null) ? null : (parameters.get("NationalDrugUnits").toString()));	
						
					  listTestData.add(Objects.equals(parameters.get("Teeth"), "") ? null : (parameters.get("Teeth").toString()));	
					Log.info("Parameters:"+parameters);
					Log.info("TestData:\n"+listTestData);
					//Log.info("DBData:\n"+listDatabase);
					//status = ValidateResults.resultValidation(listTestData, listDatabase);
					status = ValidateResults.resultsvalidation(listTestData, DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
				}
				else
				{
					Log.error("Procedure note with problemId="+parameters.get("procedureId")+", was not updated correctly-modified_by is not updated with logged in user's credentials.");
					Log.error("Expected:"+System.getProperty("LoggedInUserId")+", Actual:"+objIntModifiedBy.toString());
					softAssert.fail("Problem note with problemId="+parameters.get("problemId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				}
			}
			else
			{
				Log.error("Procedure with problemId="+parameters.get("procedureId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				softAssert.fail("Problem note with problemId="+parameters.get("procedureId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}


}
