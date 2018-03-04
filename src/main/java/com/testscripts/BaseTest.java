package com.testscripts;

import java.sql.Connection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.testng.SkipException;
import org.testng.annotations.*;

import com.config.Configs;
import com.jayway.jsonpath.JsonPath;
import com.utils.API_Request_RetObj;
import com.utils.DatabaseConnection;
import com.utils.ExcelReaderWriter;
import com.utils.GeneralUtils;
import com.utils.HashPassword;
import com.utils.Log;
import com.utils.RequestResponseHandler;
import com.utils.TestLogHelper;

public class BaseTest
{
	public static CloseableHttpClient httpClient;
	public CloseableHttpResponse httpResponse;
	public static String strFolderPath;
	public static String strDetailedTestLogs;
	public static Connection connNGA;
	public static Connection connCLOUD;
	public static Connection connNGAAudit;

	
	public static boolean strUserIdStatus = true, strUserDefaultsStatus = true, strPasswordHashStatus = true;

	public static Map<String,String> mapExecutionConfigs;// Map holding all configuration setup required for the execution
	public static Map<String,String> mapJSONSchema;// Map holding all JSON Schema file names
	public static Map<String, String> mapURLRepo;// Map that contains all URL's for each end point

	public static Map<String, Map<String, Object>> mapClientConfiguration; // Map of Maps which stores all the settings of User, Practice
	public static Map<String,Object> mapUserSettings=new HashMap<String, Object>(); // Map that has the User Settings
	public static Map<String,Object> mapPracticeSettings=new HashMap<String, Object>(); // Map that has the Practice Settings
	public static Map<String,Object> mapSysInfoSettings = new HashMap<String,Object>();// Map that has the System Info Settings
	public static Map<String, Object> mapEMRDetailOverrideInd = new HashMap<String,Object>();
	public static Logger log = LoggerFactory.getLogger(BaseTest.class);
	public static String strPrePasswordHash = null;
	
	public BaseTest()
	{
		httpClient = HttpClients.createDefault();
	}

	@BeforeSuite
	public void createDetailedTestLog ()
	{
		System.out.println("BeforeSuite executed now");

		mapExecutionConfigs = GeneralUtils.readPropsToMap("./src/main/java/Resources/Configs.properties", "=");
		mapJSONSchema = GeneralUtils.readPropsToMap("./src/main/java/Resources/JSONschema.properties", "=");
		System.out.println("Configs map is:\n"+mapExecutionConfigs);
		System.out.println("JSON Schema map is:\n"+mapJSONSchema);
		mapURLRepo = GeneralUtils.readPropsToMap(mapExecutionConfigs.get("RouteRepositoryPath"), "=");

		TestLogHelper.startTestCaseLogging("BaseTest");
		Log.beginTestCase("BaseTest");
		//Added new on 16-102016
		//Setting UserName and Password globally
		System.setProperty("LoggedInUserName", ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), "User_GetLoginDefaults", 1, 0));
		System.setProperty("LoggedInPassword", ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), "User_GetLoginDefaults", 1, 1));

		strPrePasswordHash = System.getProperty("LoggedInUserName").toLowerCase() + "nghash" + System.getProperty("LoggedInPassword");
		strDetailedTestLogs = null;
		strFolderPath = null;
		strFolderPath = GeneralUtils.createFolder();
		strDetailedTestLogs = ExcelReaderWriter.createDetailedTestLogs(strFolderPath);

		String strNGADBServerIP = ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), "NGADatabaseDetails", 1, 0);
		String strNGADBInstance = ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), "NGADatabaseDetails", 1, 1);
		String strNGADBName = ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), "NGADatabaseDetails", 1, 2);
		String strNGADBUserName = ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), "NGADatabaseDetails", 1, 3);
		String strNGADBPassword = ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), "NGADatabaseDetails", 1, 4);
		connNGA = DatabaseConnection.makeDBConnection(strNGADBServerIP, strNGADBInstance, strNGADBName, strNGADBUserName, strNGADBPassword);

		String strCLOUDDBServerIP = ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), "CLOUDDatabaseDetails", 1, 0);
		String strCLOUDDBInstance = ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), "CLOUDDatabaseDetails", 1, 1);
		String strCLOUDDBName = ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), "CLOUDDatabaseDetails", 1, 2);
		String strCLOUDDBUserName = ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), "CLOUDDatabaseDetails", 1, 3);
		String strCLOUDDBPassword = ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), "CLOUDDatabaseDetails", 1, 4);
		connCLOUD = DatabaseConnection.makeDBConnection(strCLOUDDBServerIP, strCLOUDDBInstance, strCLOUDDBName, strCLOUDDBUserName, strCLOUDDBPassword);

		String strNGAAuditDBServerIP = ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), "NGAuditDatabaseDetails", 1, 0);
		String strNGAAuditDBInstance = ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), "NGAuditDatabaseDetails", 1, 1);
		String strNGAAuditDBName = ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), "NGAuditDatabaseDetails", 1, 2);
		String strNGAAuditDBUserName = ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), "NGAuditDatabaseDetails", 1, 3);
		String strNGAAuditDBPassword = ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), "NGAuditDatabaseDetails", 1, 4);
		connNGAAudit = DatabaseConnection.makeDBConnection(strNGAAuditDBServerIP, strNGAAuditDBInstance, strNGAAuditDBName, strNGAAuditDBUserName, strNGAAuditDBPassword);
		
		
		httpClient = HttpClients.createDefault();

		try
		{
			HashMap<String, Object[]> hashMapResult = new HashMap<>();
			hashMapResult.put("0", new Object[] {"Parameters","RequestURI", "RequestHeaders","ResponseCode","ResponseHeaders", "Response", "Status"});

			//Find out the logged in user's id
			Object obj1 = DatabaseConnection.fetchColumnAsObject(connNGA, "select user_id from user_mstr where login_id = '" +ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), "User_GetLoginDefaults", 1, 0) + "' and delete_ind='N'", "user_id");

			if(!Objects.equals(obj1, null))
			{
				System.setProperty("LoggedInUserId", obj1.toString());
				strUserIdStatus = true;
				String strPrePasswordHash = System.getProperty("LoggedInUserName").toLowerCase() + "nghash" + System.getProperty("LoggedInPassword");
				String strPasswordHash = HashPassword.sha1(strPrePasswordHash);
				Object obj2 = DatabaseConnection.fetchColumnAsObject(connNGA, "select password from user_mstr where user_id = '" +System.getProperty("LoggedInUserId") + "' and delete_ind='N'", "password");
				if(!Objects.equals(obj2, null))
				{
					if(Objects.equals(obj2, strPasswordHash))
						strPasswordHashStatus = true;
					else
						strPasswordHashStatus  = false;
				}
				else
					strPasswordHashStatus  = false;

			}
			else
				strUserIdStatus = false;


			//To find login defaults
			if(strUserIdStatus && strPasswordHashStatus)
			{
				//Send an API request to fetch the login defaults
				RequestResponseHandler objHandler = new RequestResponseHandler();
				Map<String, Object> mapRequestParameters = new HashMap<String, Object>();
				mapRequestParameters.put("strRequestType", "GET");
				mapRequestParameters.put("strPayLoad", "");
				mapRequestParameters.put("strQueryString", "");
				mapRequestParameters.put("strURI", BaseTest.mapURLRepo.get("User_LoginDefaults_GET_By_Id").toString().replace("userId", System.getProperty("LoggedInUserId")));

				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParameters);
				if(objHandler.getIntResponseCode() == 200)
				{
					System.setProperty("LoggedInEnterpriseId", (JsonPath.read(objHandler.getStrResponse(), "$.enterpriseId") == null) ? null : JsonPath.read(objHandler.getStrResponse(), "$.enterpriseId").toString());
					System.setProperty("LoggedInPracticeId", (JsonPath.read(objHandler.getStrResponse(), "$.practiceId") == null) ? null : JsonPath.read(objHandler.getStrResponse(), "$.practiceId").toString());
					System.setProperty("LoggedInLocationId", (JsonPath.read(objHandler.getStrResponse(), "$.locationId") == null) ? null : JsonPath.read(objHandler.getStrResponse(), "$.locationId").toString().toUpperCase());
					System.setProperty("LoggedInProviderId", (JsonPath.read(objHandler.getStrResponse(), "$.providerId") == null) ? null : JsonPath.read(objHandler.getStrResponse(), "$.providerId").toString().toUpperCase());
					System.setProperty("LoggedInTimeZone", (JsonPath.read(objHandler.getStrResponse(), "$.timeZone") == null) ? null : JsonPath.read(objHandler.getStrResponse(), "$.timeZone").toString());
					strUserDefaultsStatus = true;
				}
				else
					strUserDefaultsStatus = false;

				System.out.println("LoggedInEnterpriseId:"+System.getProperty("LoggedInEnterpriseId"));
				System.out.println("LoggedInPracticeId:"+System.getProperty("LoggedInPracticeId"));
				System.out.println("LoggedInLocationId:"+System.getProperty("LoggedInLocationId"));
				System.out.println("LoggedInProviderId:"+System.getProperty("LoggedInProviderId"));
				System.out.println("LoggedInTimeZone:"+System.getProperty("LoggedInTimeZone"));

				Log.info("LoggedInEnterpriseId:"+System.getProperty("LoggedInEnterpriseId"));
				Log.info("LoggedInPracticeId:"+System.getProperty("LoggedInPracticeId"));
				Log.info("LoggedInLocationId:"+System.getProperty("LoggedInLocationId"));
				Log.info("LoggedInProviderId:"+System.getProperty("LoggedInProviderId"));
				Log.info("LoggedInTimeZone:"+System.getProperty("LoggedInTimeZone"));


				mapClientConfiguration = Configs.ClientConfiguration(connNGA);
				Set<String> settingsKeySet = new HashSet<String>();
				if(!mapClientConfiguration.isEmpty())
				{
					settingsKeySet = mapClientConfiguration.keySet();
					Iterator<String> iter = settingsKeySet.iterator();

					while(iter.hasNext())
					{
						//System.out.println(iter.next());
						String keyName= (String) iter.next();
						if(keyName.equalsIgnoreCase("UserSettingsList"))
						{
							mapUserSettings.putAll(mapClientConfiguration.get(keyName));
						}
						else if(keyName.equalsIgnoreCase("PracticeSettingsList"))
						{
							mapPracticeSettings.putAll(mapClientConfiguration.get(keyName));
						}
						else if(keyName.equalsIgnoreCase("SysInfoSettingsList"))
						{
							mapSysInfoSettings.putAll(mapClientConfiguration.get(keyName));
						}
						else if(keyName.equalsIgnoreCase("EMRDetailOverrideInd"))
						{
							mapEMRDetailOverrideInd.putAll(mapClientConfiguration.get(keyName));
						}
					}
				}

				System.out.println("mapUserSettings:"+mapUserSettings);
				System.out.println("mapPracticeSettings:"+mapPracticeSettings);
				System.out.println("mapSysInfoSettings"+mapSysInfoSettings);
				System.out.println("mapEMRDetailOverrideInd"+mapEMRDetailOverrideInd);
			}
			else
				System.out.println("User Credentials doesn't exist in NGA Database, unable to fetch the user defaults. Kindly check credentials");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		TestLogHelper.stopTestLogging();
	}


	@BeforeClass
	public void preCondition ()
	{
		httpClient = HttpClients.createDefault();
		//start logging to test specific log file
		TestLogHelper.startTestCaseLogging(getTestId());
	}

	@AfterClass
	public void testCleanUp()
	{
		//System.out.println("In after class of base test:"+getTestId());
		//stop test logging to test specific file
		TestLogHelper.stopTestLogging();
	}

	@BeforeMethod
	public void beforeTest()
	{
		if(!strUserIdStatus)
		{
			System.out.println("User Credentials doesn't exist in NGA Database, unable to fetch the user defaults. Kindly check credentials");
			Log.error("User Credentials doesn't exist in NGA Database, unable to fetch the user defaults. Kindly check credentials");
			throw new SkipException("User Credentials doesn't exist in NGA Database, unable to fetch the user defaults. Kindly check credentials");
		}
		else if (!strPasswordHashStatus)
		{
			System.out.println("PasswordHash doesn't exist in NGA Database, unable to fetch the user defaults. Kindly check PASSWORD");
			Log.error("PasswordHash doesn't exist in NGA Database, unable to fetch the user defaults. Kindly check PASSWORD");
			throw new SkipException("PasswordHash doesn't exist in NGA Database, unable to fetch the user defaults. Kindly check PASSWORD");
		}
		else if (!strUserDefaultsStatus)
		{
			System.out.println("Issue with Get_UserLoginDefaults.");
			Log.error("Issue with Get_UserLoginDefaults.");
			throw new SkipException("Issue with Get_UserLoginDefaults.");
		}
	}

	@AfterSuite
	public void tearDown()
	{
		DatabaseConnection.closeDBConnection(connNGA);
		System.out.println("Detailed Test Log File is present at " + strDetailedTestLogs);
		Log.endTestCase(getTestId());
		TestLogHelper.stopTestLogging();
	}

	public static void removeLoggerForBaseTest()
	{
		MDC.remove("testname");
	}

	public String getTestId() 
	{
		return this.getClass().getSimpleName();
	}
}