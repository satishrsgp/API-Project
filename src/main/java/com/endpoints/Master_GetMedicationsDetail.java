/**
 * API 1.x route_Abhishek
 */

package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Master_GetMedicationsDetail {

	public static Boolean master_GetMedicationsDetail(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				
				String strQuery="SELECT distinct n.med_name_id, n.med_name, n.medid, n.med_strength, n.med_strength_uom,n.medid_desc, n.gcn_seqno, n.med_ref_dea_class_code,n.generic_medid,n.med_route_id, "
						+ "n.med_dosage_form_id,n.med_dosage_form_desc,n.med_strength_numeric,mr.med_route_desc,r.etc_id,fms.ndc_id,b.med_name,case m.hidden_flag when 'Y' then 'true' else 'false' end as hidden_flag "
						+ ", case n.GENERIC_MEDID WHEN 0 THEN 'true' ELSE 'false' END AS is_generic,h.hic3, h.hicl_seqno FROM fdb_med_single fms inner join fdb_med_name_search_mstr n ON n.medid = fms.medid "
						+ "Left Outer JOIN fdb_med_name_search_mstr b on n.generic_medid = b.medid --inner Join fdb_med_single fms ON b.medid = fms.medid Left Outer JOIN fdb_medication fm ON fm.medid = n.medid "
						+ "Left Outer JOIN RETCMED0 r ON r.medid=fm.medid Left Outer JOIN fdb_med_route_mstr mr ON mr.med_route_id = n.med_route_id Left Outer JOIN gcnseqnotbl h ON h.gcn_seqno = n.gcn_seqno "
						+ "Left Outer JOIN medication_hidden m ON fm.medid = m.medid WHERE fm.medid='"+parameters.get("medId")+"' and fm.delete_ind='N'";
				

				String[] jsonMetaData = {"brandId","medicationName","medicationId","medicationStrength","medicationStrengthUom","medicationIdDescription","gcnSequenceNumber","medicationRefDeaClassCode",
						"genericMedicationId","medicationRouteId","medicationDosageFormId","medicationDosageFormDescription","medicationStrengthNumeric","medicationRouteDescription","etcId",
						"ndcId","genericName","hic3","hiclSequenceNumber","isGeneric","isHidden"};

				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..brandId", jsonList);
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
