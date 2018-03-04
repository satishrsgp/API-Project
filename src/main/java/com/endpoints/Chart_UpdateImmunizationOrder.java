package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_UpdateImmunizationOrder {


	public static Boolean chart_UpdateImmunizationOrder_Test(HashMap<String, String> parameters, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strSQLQuery = "select datediff(second,create_timestamp,modify_timestamp) as diff,modified_by as mb "
					+ "from imm_nor "
					+ " where person_id='"+parameters.get("personId")+"' "
					+ "and enc_id='" + parameters.get("encounterId") + "'"
					+ "and order_num = '" + parameters.get("orderId") + "' ";
					
			Object objIntDateDiff = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "diff");
			Object objIntModifiedBy = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "mb");
			
			if(Integer.valueOf(objIntDateDiff.toString()) > 0)
			{
				if(Objects.equals(objIntModifiedBy.toString(), System.getProperty("LoggedInUserId")))
				{
					String strQuery1 = "Select top 1 "
							+ "IMN.order_num as id, "
							+ "IMN.enterprise_id as enterpriseId, "
							+ "IMN.practice_id as practiceId, "
							+ "IMN.enc_id as encounterId, "
							+ "IMN.person_id as personId, "
							+ "concat( P.last_name ,', ', P.first_name ) as personName, "
							+ "IMN.ufo_num as userFriendlyOrderNumber, "
							+ "IMN.generated_by as generatedBy, "
							+ "IMN.location_id as locationId, "
							+ "LM.location_name as locationName, "
							+ "IMN.ordering_provider as orderingProviderId, "
							+ "PM.description as orderingProviderName, "
							+ "IMN.supervisor_provider as supervisorProviderId, "
							+ "IMN.ngn_status as status, "
							+ "IMN.vaccine_desc as vaccineDescription, "
							+ "IMN.registry_id as registryId, "
							+ "IMN.intrf_msg as interfaceMessage,"
							+ "Case when IMN.documents_ind = 'Y' then 'true' else 'false' end as hasDocuments, "
							+ "case when IMN.tracking_comments_ind = 'Y' then 'true' else 'false' end as hasTrackingComments, "
							+ "IMN.immunizations_desc as immunizationsDescription, "
							+ "IMN.order_comment as orderComment, "
							+ "case when IMN.verbal_order_ind = '1' then 'true' else 'false' end  as isVerbalOrder, "
							+ "IMN.allergy_review as allergiesReviewed, "
							+ "IMN.cancel_reason as cancelReason, "
							//+ "IMN.sign_off_date as signOffDate, "
							//+ "IMN.sign_off_date_tz as signOffDateTimezone, "
							+ "IMN.sign_off_person as signOffUserId, "
							+ "case when IMN.sign_off_person is null then null else concat( UM.last_name ,', ', UM.first_name ) end  as signOffUserName, "
							+ "Pat.privacy_level as privacyLevel, "
							+ "case when IMN.delete_ind = 'Y' then 'true' else 'false' end  as isDeleted "
							+ "FROM imm_nor IMN "
							+ "inner join person P on IMN.person_id = P.person_id "
							+ "left OUTER JOIN location_mstr LM on IMN.location_id = LM.location_id "
							+ "left OUTER JOIN provider_mstr PM on  IMN.ordering_provider = PM.provider_id "
							+ "left OUTER JOIN user_mstr UM on IMN.sign_off_person = UM.user_id "
							+ "left outer join  patient pat on IMN.person_id  = pat.person_id "
							+ " where IMN.person_id='"+parameters.get("personId")+"' "
							+ "and IMN.enc_id='" + parameters.get("encounterId") + "'"
							+ "a	nd IMN.order_num = '" + parameters.get("orderId") + "' "
							+ "Order by IMN.create_timestamp desc";
							
					Log.info("strQuery1:"+strQuery1);
					//ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
					ArrayList<Object> listTestData  = new ArrayList<>();
					listTestData.add(Objects.equals(parameters.get("orderId"), "") ? null: parameters.get("orderId").toString().toUpperCase());
					listTestData.add(Objects.equals(parameters.get("enterpriseId"), "") ? null : (parameters.get("enterpriseId").toString()));
					listTestData.add(Objects.equals(parameters.get("practiceId"), "") ? null: parameters.get("practiceId").toString());
					listTestData.add(Objects.equals(parameters.get("encounterId"), "") ? null: parameters.get("encounterId").toString().toUpperCase());
					listTestData.add(Objects.equals(parameters.get("personId"), "") ? null : (parameters.get("personId").toString().toUpperCase()));
					listTestData.add(Objects.equals(parameters.get("personName"), "") ? null: parameters.get("personName").toString());
					listTestData.add(Objects.equals(parameters.get("userFriendlyOrderNumber"), "") ? null: parameters.get("userFriendlyOrderNumber").toString());
					listTestData.add(Objects.equals(parameters.get("generatedBy"), "") ? null: parameters.get("generatedBy").toString());
					listTestData.add(Objects.equals(parameters.get("locationId"), "") ? null: parameters.get("locationId").toString().toUpperCase());
					listTestData.add(Objects.equals(parameters.get("locationName"), "") ? null: parameters.get("locationName").toString());
					listTestData.add(Objects.equals(parameters.get("orderingProviderId"), "") ? null: parameters.get("orderingProviderId").toString().toUpperCase());
					listTestData.add(Objects.equals(parameters.get("orderingProviderName"), "") ? null: parameters.get("orderingProviderName").toString());
					listTestData.add(Objects.equals(parameters.get("supervisorProviderId"), "null") ? null : (parameters.get("supervisorProviderId").toString().toUpperCase()));
					listTestData.add(Objects.equals(parameters.get("status"), "") ? null: parameters.get("status").toString());
					listTestData.add(Objects.equals(parameters.get("vaccineDescription"), "") ? null: parameters.get("vaccineDescription").toString());
					listTestData.add(Objects.equals(parameters.get("registryId"), "") ? null: parameters.get("registryId").toString());
					listTestData.add(Objects.equals(parameters.get("interfaceMessage"), "null") ? null: parameters.get("interfaceMessage").toString());
					listTestData.add(Objects.equals(parameters.get("hasDocuments"), "") ? null: parameters.get("hasDocuments").toString());
					listTestData.add(Objects.equals(parameters.get("hasTrackingComments"), "") ? null: parameters.get("hasTrackingComments").toString());
					listTestData.add(Objects.equals(parameters.get("immunizationsDescription"), "") ? null: parameters.get("immunizationsDescription").toString());
					listTestData.add(Objects.equals(parameters.get("orderComment"), "") ? "": parameters.get("orderComment").toString());
					listTestData.add(Objects.equals(parameters.get("isVerbalOrder"), "") ? null: parameters.get("isVerbalOrder").toString());
					String allergyReviewed = parameters.get("allergriesReviewed");
                   if(allergyReviewed.contains(","))
                   {
                          int i=0;
                          String allergyReviewed1[] = allergyReviewed.split(",");

                          for(int j=0;j<allergyReviewed1.length;j++)
                          {
                                 //allergyReviewed1[j]= "\"" +allergyReviewed1[j]+ "\"";
                                 
                                        if(allergyReviewed1[j].contains("egg"))
                                       i=i+1;
                                 else if((allergyReviewed1[j].trim().contains("latex")))
                                       i=i+4;
                                 else if((allergyReviewed1[j].trim().contains("gelatin")))
                                       i=i+8;
                                 else if((allergyReviewed1[j].trim().contains("neomycin")))
                                       i=i+2;
                          }
                          System.out.println(i);
                          listTestData.add(i);
                          //jsonList.add(allergyReviewed1);
                   }                   
                   else
                   {
                          int i=0;
                          String allergyReviewed1[]={allergyReviewed};
                          for(int j=0;j<allergyReviewed1.length;j++)
                          {
                                 if(allergyReviewed1[j].trim().contains("egg"))
                                       i=i+1;
                                 else if((allergyReviewed1[j].trim().contains("latex")))
                                       i=i+4;
                                 else if((allergyReviewed1[j].trim().contains("gelatin")))
                                       i=i+8;
                                 else if((allergyReviewed1[j].trim().contains("neomycin")))
                                       i=i+2;
                          }
                          System.out.println(i);
                          listTestData.add(i);
                   }

					//listTestData.add(Objects.equals(parameters.get("allergriesReviewed"), "") ? "": parameters.get("allergriesReviewed").toString());
					listTestData.add(Objects.equals(parameters.get("cancelReason"), "null") ? null: parameters.get("cancelReason").toString().split("\"")[1].split("\"")[0]);
					//listTestData.add(Objects.equals(parameters.get("signOffDate"), "null") ? null: parameters.get("signOffDate").toString());
					//listTestData.add(Objects.equals(parameters.get("signOffDateTimezone"), "null") ? null: parameters.get("signOffDateTimezone").toString());
					listTestData.add(Objects.equals(parameters.get("signOffUserId"), "null") ? null: parameters.get("signOffUserId").toString().split("\"")[1].split("\"")[0]);
					listTestData.add(Objects.equals(parameters.get("signOffUserName"), "null") ? null: parameters.get("signOffUserName").toString().split("\"")[1].split("\"")[0]);
					listTestData.add(Objects.equals(parameters.get("privacyLevel"), "1") ? null: parameters.get("privacyLevel").toString());
					listTestData.add(Objects.equals(parameters.get("isDeleted"), "") ? null: parameters.get("isDeleted").toString());
					
					Log.info("Parameters:"+parameters);
					Log.info("TestData:\n"+listTestData);
					//Log.info("DBData:\n"+listDatabase);
					status = ValidateResults.resultsvalidation(listTestData, DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
				}
				else
				{
					Log.error("Care team member for patient with person_id ="+parameters.get("personId")+", was not updated correctly-modified_by is not updated with logged in user's credentials.");
					Log.error("Expected:"+System.getProperty("LoggedInUserId")+", Actual:"+objIntModifiedBy.toString());
					softAssert.fail("seq_no for patient with person_id ="+parameters.get("personId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				}
			}
			else
			{
				Log.error("Care team member for patient with person_id ="+parameters.get("personId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				softAssert.fail("seq_no for patient with person_id ="+parameters.get("personId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}
}
