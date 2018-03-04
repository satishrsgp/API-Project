package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Users_GetFavoriteDiagnoses
{

	/*
	 * ********************************************************************************
	 * Class Name						:Users_GetFavoriteDiagnoses
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

	public static Boolean users_GetFavoriteDiagnoses_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		//System.out.println(strResponse);
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{ 
				String strQuery = "select distinct "
						+" dfx.diagnosis_code_id as icdCode,ds.term as clinicalDescription,ds.icd_description as billingDescription,ds.hcc_id as hccId,ds.nghcc_id as ngHccId, "
						+ " ds.hcc_description as hccDescription,ds.comm_coefficient as hccCommunityCoefficientds,hcc_inst_coefficient as hccInstitutionalCoefficient,ds.rxhcc_id as rxHccId,ds.ngrxhcc_id as ngRxHccId, "
						+ " ds.rxhcc_description as rxHccDescription,ds.rxhcc_inst_coefficient as rxHccInstitutionalCoefficient,ds.LowIncome_under65 as rxHccCoefficientLowIncomeUnder65,ds.LowIncome_over65 as rxHccCoefficientLowIncomeOver65, "
						+ " ds.under65 as rxHccCoefficientNonLowIncomeUnder65,ds.over65 as rxHccCoefficientNonLowIncomeOver65"
						+" from diag_search as ds "
						+ "join diagnosis_favorite_xref as dfx on dfx.diagnosis_code_id = ds.icd "
				
						+ "where dfx.diag_favorite_id='"+parameters.get("groupId")+"' and dfx.created_by='"+parameters.get("userId")+"' and ds.display_ind = 'Y' ";
						
				String[] jsonMetaData = {"icdCode","clinicalDescription","billingDescription","hccId", "ngHccId","hccDescription","hccCommunityCoefficient","hccInstitutionalCoefficient", "rxHccId","ngRxHccId","rxHccDescription","rxHccInstitutionalCoefficient","rxHccCoefficientLowIncomeUnder65", "rxHccCoefficientLowIncomeOver65", "rxHccCoefficientNonLowIncomeUnder65", "rxHccCoefficientNonLowIncomeOver65"};

				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..icdCode", jsonList);
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
