/**
 * 
 */
package com.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import com.utils.DatabaseConnection;
import com.utils.GeneralUtils;

/**
 * @author aveluru
 * This class will be holding all the configuration that will be used through the frame work
 */
public class Configs
{	
	public static  String strDetailedTestLog = "DetailedTestLog_" + GeneralUtils.getDateTime() + ".xlsx";
	
	public static Map<String, Map<String, Object>> ClientConfiguration (Connection conn)
	{
		Map<String, Map<String, Object>> mapClientConfiguration = new HashMap<String, Map<String, Object>>();
		Map<String, Object> mapUserSettings = new HashMap<String, Object>();
		Map<String, Object> mapPracticeSettings = new HashMap<String, Object>();
		Map<String, Object> mapSysInfoSettings = new HashMap<String, Object>();
 		
		//Query to check whether the TallMan casing is enabled for the logged in User
		String strSQLQueryUserSettings = "SELECT item_value FROM user_pref "
				+ "WHERE enterprise_id= '" + System.getProperty("LoggedInEnterpriseId") + "'and practice_id='" 
				+ System.getProperty("LoggedInPracticeId") + "'and user_id='" + System.getProperty("LoggedInUserId") 
				+ "' and item_name= 'MED_DISPLAY_TALL_NAME'";

		/*//Query to check whether the TallMan casing is enabled for the logged in practice
		String strSQLPracticeSettings = "SELECT enable_case_man_ind FROM practice_misc "
				+ "WHERE practice_id= '" + System.getProperty("LoggedInPracticeId") + "'";*/

		//Query to check whether the TallMan casing is enabled for the logged in practice
		String strSQLPracticeSettings = "SELECT display_tallman FROM practice_emr "
				+ "WHERE practice_id= '" + System.getProperty("LoggedInPracticeId") + "'";
		
		
		String strServerUTCOffset = "select case when timezones_enabled_ind = 'Y' then server_utc_offset else 0 end as server_utc_offset from sys_info";

		//Connection conn;
		ResultSet rsUserSetting = DatabaseConnection.statementExecution(conn, strSQLQueryUserSettings);
		ResultSet rsPracticeSetting = DatabaseConnection.statementExecution(conn, strSQLPracticeSettings);
		ResultSet rsSysInfoSetting = DatabaseConnection.statementExecution(conn, strServerUTCOffset);
		mapUserSettings = DatabaseConnection.addResultswithMetaData(rsUserSetting);
		mapPracticeSettings = DatabaseConnection.addResultswithMetaData(rsPracticeSetting);
		mapSysInfoSettings = DatabaseConnection.addResultswithMetaData(rsSysInfoSetting);
		
		mapClientConfiguration.put("UserSettingsList", mapUserSettings);
		mapClientConfiguration.put("PracticeSettingsList", mapPracticeSettings);
		mapClientConfiguration.put("SysInfoSettingsList", mapSysInfoSettings);

		return mapClientConfiguration;
	}
}
