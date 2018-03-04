package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_Person_OrderTest_GET_ById
{

	public static Boolean chart_Person_OrderTest_GET_ById_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "SELECT UPPER(CAST(lot.order_test_id AS CHAR(36))) AS id,UPPER(CAST(lot.order_num AS CHAR(36))) AS orderNumber,ln.enterprise_id AS enterpriseId,ln.practice_id AS practiceId,"
						+" UPPER(CAST(ln.person_id AS CHAR(36))) AS personId,lot.test_code_id AS testCodeId,lot.test_code_text AS testCodeText,lot.test_code_system AS testCodeSystem,lot.collection_time AS collectionTime,"
						+" lot.volume_quantity AS volumeQuantity,lot.volume_units AS volumeUnits,lot.spec_src_code AS specSrcCode,lot.spec_src_desc AS specSrcDesc,lot.spec_src_additives AS specSrcAdditives,"
						+" lot.scheduled_time AS scheduledTime,lot.created_by AS createdBy,lot.generated_by AS generatedBy,lot.create_timestamp AS createTimestamp,lot.modified_by AS modifiedBy,"
						+" lot.modify_timestamp AS modifyTimestamp,lot.loinc_code AS loincCode,lot.snomed_code AS snomedCode,lot.spec_src_body_site AS specSrcBodySite,lot.spec_src_site_modifier AS specSrcSiteModifier,"
						+" lot.spec_src_role AS specSrcRole,lot.spec_src_storage AS specSrcStorage,lot.spec_src_coll_method AS specSrcCollectionMethod,lot.collector_id AS collectorId,lot.test_comment AS testComment,"
						+" lot.expected_rslt_date AS expectedResultDate,lot.collection_time_tz AS collectionTimezone,lot.scheduled_time_tz AS scheduledTimezone,lot.create_timestamp_tz AS createTimestampTimezone,"
						+" lot.modify_timestamp_tz AS modifyTimestampTimezone,UPPER(CAST(lot.charge_id AS CHAR(36))) AS chargeId,lot.ordering_reason AS orderingReason,lot.cds_ref_num AS cdsReferenceNumber,"
						+" lot.cds_score AS cdsScore,lot.cds_date AS cdsDate"
						+" FROM  dbo.lab_order_tests AS lot INNER JOIN dbo.lab_nor AS ln ON lot.order_num = ln.order_num"
						+" WHERE ln.person_id = '"+parameters.get("personId")+"' AND lot.order_num = '"+parameters.get("orderId")+"' AND lot.order_test_id = '"+parameters.get("testId")+"'";
				
				String[] jsonMetaData = {"id","orderNumber","enterpriseId","practiceId","personId","testCodeId","testCodeText","testCodeSystem","collectionTime","volumeQuantity","volumeUnits","specSrcCode","specSrcDesc","specSrcAdditives","scheduledTime","createdBy","generatedBy","createTimestamp","modifiedBy","modifyTimestamp","loincCode","snomedCode","specSrcBodySite","specSrcSiteModifier","specSrcRole","specSrcStorage","specSrcCollectionMethod","collectorId","testComment","expectedResultDate","collectionTimezone","scheduledTimezone","createTimestampTimezone","modifyTimestampTimezone","chargeId","orderingReason","cdsReferenceNumber","cdsScore","cdsDate"};

				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..id", jsonList);
				} 
				catch (Exception e)
				{
					e.printStackTrace();
					Log.info("Error in Test Class and the detail are : \n" + e.getMessage());
					softAssert.fail("Error in Test Script please look at logs");
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