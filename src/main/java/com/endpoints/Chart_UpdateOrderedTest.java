package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_UpdateOrderedTest
{
	public static Boolean chart_UpdateOrderedTest_Test(HashMap<String, String> parameters, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strSQLQuery = "select datediff(second,create_timestamp,modify_timestamp) as diff,modified_by as mb "
					+ "	from lab_order_tests where order_num='"+parameters.get("orderId")+"' and  order_test_id= '"+parameters.get("testId")+"'";
			Object objIntDateDiff = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "diff");
			Object objIntModifiedBy = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "mb");
			if(Integer.valueOf(objIntDateDiff.toString()) > 0)
			{
				if(Objects.equals(objIntModifiedBy.toString(), System.getProperty("LoggedInUserId")))
				{
					String strQuery1 =  " SELECT top 1 lot.order_test_id as id,lot.order_num as orderId,ln.enterprise_id as enterpriseId, "			   
							+ " ln.practice_id as practiceId,ln.person_id as personId,lot.test_code_id as testCodeId,lot.test_code_text as testCodeText,lot.test_code_system as testCodeSystem,"
							+  "lot.collection_time as collectionTime,lot.volume_quantity as volumeQuantity,lot.volume_units as volumeUnits,lot.spec_src_code as specSrcCode,lot.spec_src_desc as specSrcDesc, "
							+ "  	lot.spec_src_additives as specSrcAdditives,lot.scheduled_time as scheduledTime,lot.created_by as createdBy,lot.generated_by as generatedBy,lot.create_timestamp as createTimestamp, "
							+ " lot.modified_by as modifiedBy,lot.modify_timestamp as modifyTimestamp,lot.loinc_code as loincCode,lot.snomed_code as snomedCode,lot.spec_src_body_site as specSrcBodySite,lot.spec_src_site_modifier as specSrcSiteModifier, "		
							//+ "CASE WHEN series_complete_ind = 'false' THEN 'N' "
							//  + "ELSE 'Y' END AS isComplete, "
							+ "  	lot.spec_src_role as specSrcRole,lot.spec_src_storage as specSrcStorage,lot.spec_src_coll_method as specSrcCollectionMethod,lot.collector_id as collectorId, "		
							+ " 	lot.test_comment as testComment,lot.expected_rslt_date as expectedResultDate,lot.collection_time_tz as collectionTimezone,lot.scheduled_time_tz as scheduledTimezone, "
							+ "lot.create_timestamp_tz as createTimestampTimezone,lot.modify_timestamp_tz as modify_timestamp_tz,lot.charge_id as chargeId,lot.ordering_reason as orderingReason "
							+ " from lab_order_tests as lot inner join lab_nor as ln on lot.order_num = ln.order_num  "
							+ " where ln.person_id = '"+parameters.get("personId")+"'  and ln.order_num = '"+parameters.get("orderId")+"' and lot.order_test_id = '"+parameters.get("testId")+"'";
					//ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
					ArrayList<Object> listTestData  = new ArrayList<>();
					listTestData.add(Objects.equals(parameters.get("id"), null) ? null : (parameters.get("id").toString()));		
					listTestData.add(Objects.equals(parameters.get("orderId"), "") ? null : (parameters.get("orderId").toString()));
				
					listTestData.add(Objects.equals(parameters.get("enterpriseId"), null) ? null : (parameters.get("enterpriseId").toString()));	
					listTestData.add(Objects.equals(parameters.get("practiceId"), null) ? null : (parameters.get("practiceId").toString()));	
					listTestData.add(Objects.equals(parameters.get("personId"), "") ? null : (parameters.get("personId").toString()));

					listTestData.add(Objects.equals(parameters.get("seriesName"), null) ? null : (parameters.get("seriesName").toString()));	
					//listTestData.add(parameters.get("RecallPlanId").toString());
				//	listTestData.add(Objects.equals(parameters.get("isComplete"), "false") ? "N" : "Y");
					listTestData.add(Objects.equals(parameters.get("testCodeId"), null) ? null : (parameters.get("testCodeId").toString()));	
					listTestData.add(Objects.equals(parameters.get("testCodeText"), null) ? null : (parameters.get("testCodeText").toString()));	
					listTestData.add(Objects.equals(parameters.get("testCodeSystem"), null) ? null : (parameters.get("testCodeSystem").toString()));	
					listTestData.add(Objects.equals(parameters.get("collectionTime"), "") ? null : (parameters.get("collectionTime").toString()));
					listTestData.add(Objects.equals(parameters.get("volumeQuantity"), null) ? null : (parameters.get("volumeQuantity").toString()));	
					listTestData.add(Objects.equals(parameters.get("volumeUnits"), null) ? null : (parameters.get("volumeUnits").toString()));	
					listTestData.add(Objects.equals(parameters.get("specSrcCode"), null) ? null : (parameters.get("specSrcCode").toString()));	
					listTestData.add(Objects.equals(parameters.get("specSrcDesc"), "") ? null : (parameters.get("specSrcDesc").toString()));
					listTestData.add(Objects.equals(parameters.get("specSrcAdditives"), null) ? null : (parameters.get("specSrcAdditives").toString()));	
					listTestData.add(Objects.equals(parameters.get("scheduledTime"), null) ? null : (parameters.get("scheduledTime").toString()));	
					listTestData.add(Objects.equals(parameters.get("createdBy"), null) ? null : (parameters.get("createdBy").toString()));	
					listTestData.add(Objects.equals(parameters.get("generatedBy"), "") ? null : (parameters.get("generatedBy").toString()));
					listTestData.add(Objects.equals(parameters.get("createTimestamp"), null) ? null : (parameters.get("createTimestamp").toString()));	
					listTestData.add(Objects.equals(parameters.get("modifiedBy"), null) ? null : (parameters.get("modifiedBy").toString()));	
					listTestData.add(Objects.equals(parameters.get("modifyTimestamp"), null) ? null : (parameters.get("modifyTimestamp").toString()));	
					listTestData.add(Objects.equals(parameters.get("loincCode"), "") ? null : (parameters.get("loincCode").toString()));
					listTestData.add(Objects.equals(parameters.get("snomedCode"), "") ? null : (parameters.get("snomedCode").toString()));	
					listTestData.add(Objects.equals(parameters.get("specSrcBodySite"), null) ? null : (parameters.get("specSrcBodySite").toString()));	
					listTestData.add(Objects.equals(parameters.get("specSrcSiteModifier"), null) ? null : (parameters.get("specSrcSiteModifier").toString()));	
					listTestData.add(Objects.equals(parameters.get("specSrcRole"), "") ? null : (parameters.get("specSrcRole").toString()));
					listTestData.add(Objects.equals(parameters.get("specSrcStorage"), null) ? null : (parameters.get("specSrcStorage").toString()));	
					listTestData.add(Objects.equals(parameters.get("specSrcCollectionMethod"), null) ? null : (parameters.get("specSrcCollectionMethod").toString()));	
					listTestData.add(Objects.equals(parameters.get("collectorId"), null) ? null : (parameters.get("collectorId").toString()));	
					listTestData.add(Objects.equals(parameters.get("testComment"), "") ? null : (parameters.get("testComment").toString()));
					listTestData.add(Objects.equals(parameters.get("expectedResultDate"), "") ? null : (parameters.get("expectedResultDate").toString()));	
					listTestData.add(Objects.equals(parameters.get("collectionTimezone"), "") ? null : (parameters.get("collectionTimezone").toString()));
					listTestData.add(Objects.equals(parameters.get("scheduledTimezone"), null) ? null : (parameters.get("scheduledTimezone").toString()));	
					listTestData.add(Objects.equals(parameters.get("createTimestampTimezone"), null) ? null : (parameters.get("createTimestampTimezone").toString()));	
					listTestData.add(Objects.equals(parameters.get("modifyTimestampTimezone"), null) ? null : (parameters.get("modifyTimestampTimezone").toString()));	
					listTestData.add(Objects.equals(parameters.get("chargeId"), "") ? null : (parameters.get("chargeId").toString()));
					listTestData.add(Objects.equals(parameters.get("orderingReason"), "") ? null : (parameters.get("orderingReason").toString()));	
					Log.info("Parameters:"+parameters);
					Log.info("TestData:\n"+listTestData);
					//Log.info("DBData:\n"+listDatabase);
					//status = ValidateResults.resultValidation(listTestData, listDatabase);
					status = ValidateResults.resultsvalidation(listTestData, DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
				}
				else
				{
					Log.error("Order Test  with testId="+parameters.get("testId")+", was not updated correctly-modified_by is not updated with logged in user's credentials.");
					Log.error("Expected:"+System.getProperty("LoggedInUserId")+", Actual:"+objIntModifiedBy.toString());
					softAssert.fail("Order Test with testId="+parameters.get("testId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
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
