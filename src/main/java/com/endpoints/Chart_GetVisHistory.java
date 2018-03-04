package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetVisHistory {

	
	public static Boolean chart_GetVisHistory_TC1(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				
				String strQuery = "SELECT "
						+ "IVVH.vaccine_vis_id as vaccineVisId,IVVH.order_vaccine_id as orderVaccineId,IMN.person_id as personId, "
						+ "IMN.order_num as orderId,convert(varchar(10),IVVH.vis_give_date,112) as visGiveDate,IVVH.vis_give_date_tz as visGiveDateTimeZone, "
						+ "convert(varchar(10),IVV.vis_publish_date,112) as visPublishDate,IVVH.given_vis_id as givenVisId,IVVH.vaccine_type as vaccineType,"
						+ "IVVH.vis_documentType as visDocumentType,IVV.created_by as createdBy,IVV.vis_desc as visDescription, "
						+ "IVV.language_id as languageId, "
						//+ "IVV.vis_path as visPath, "
						+ "concat(UM.last_name,', ',UM.first_name) as givenBy, "
						+ "ML.mstr_list_item_desc as languageName,convert(varchar(10),IVVH.vis_publish_date,112) as visHistoryPublishDate,IVV.vaccine_type as givenVisVaccineType " 
						+ "From imm_vaccine_vis_hist AS IVVH "
						+ "LEFT OUTER JOIN imm_vaccine_vis AS IVV ON IVVH.vaccine_vis_id = IVV.vaccine_vis_id "
						+ "LEFT OUTER JOIN user_mstr AS UM ON IVVH.created_by = UM.user_id "
						+ "LEFT OUTER JOIN mstr_lists AS ML ON IVV.language_id = ML.mstr_list_item_id "
						+ "INNER JOIN imm_order_vaccines AS IOV ON IVVH.order_vaccine_id = IOV.order_vaccine_id "
						+ "INNER JOIN imm_nor AS IMN ON IOV.order_num = IMN.order_num "
						+ "WHERE (IVVH.order_vaccine_id = '"+parameters.get("VaccineId")+"') AND ((ML.mstr_list_type = 'language') OR ((ML.mstr_list_type IS NULL) AND ('language' IS NULL))) "
						+ "AND (IMN.person_id = '"+parameters.get("personId")+"') "
						+ "AND (IMN.order_num = '"+parameters.get("orderId")+"')";
						
										
				String[] jsonMetaData = {"vaccineVisId","orderVaccineId","personId","orderId","visGiveDate","visGiveDateTimeZone",
						"visPublishDate","givenVisId","vaccineType","visDocumentType","createdBy","visDescription","languageId",
						//"visPath",
						"givenBy","languageName","visHistoryPublishDate","givenVisVaccineType"};
				
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..vaccineVisId", jsonList);
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
