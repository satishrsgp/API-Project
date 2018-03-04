package com.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import com.testscripts.BaseTest;

public class API_POST
{

	public static HttpPost httpPost;
	public static CloseableHttpResponse httpResponse;
	public static Header[] httpHeaders;
	public static StringEntity entity;
	public static String strfinalSignature = null;

	public static String sendPostRequest(CloseableHttpClient httpClient, String strURI, String strPayLoad)
	{
		try
		{
			httpPost = new HttpPost(strURI);

			System.setProperty("RequestURI", httpPost.getURI().toString());

			String strUserName = ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), "Login", 1, 0);
			String strPassword = ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), "Login", 1, 1);
			String strPrePasswordHash = strUserName.toLowerCase() + "nghash" + strPassword;



			strfinalSignature = FinalSignature.ComputeSignature(
					FinalSignature.GetSigningString(strUserName.toLowerCase(), 
							GeneralUtils.getXNGDate() , 
							"POST", 
							FinalSignature.CanonicalURI(strURI), 
							FinalSignature.ComputeMD5(FinalSignature.CanonicalQueryString("").getBytes()), 
							FinalSignature.ComputeMD5(strPayLoad.getBytes())), 
					HashPassword.sha1(strPrePasswordHash));

			//httpPost.addHeader("Content-Type", "application/json");
			httpPost.addHeader("Accept", "*/*");
			String strAuthHeader = "NEXTGEN-AMB-API-V2 Credential=" + "AdminNew" + ", Signature=" + strfinalSignature;
			httpPost.addHeader("Authorization", strAuthHeader);
			httpPost.addHeader("X-NG-Date", GeneralUtils.getXNGDate());

			entity = new StringEntity(strPayLoad, ContentType.APPLICATION_JSON);
			httpPost.setEntity(entity);

			httpHeaders = httpPost.getAllHeaders();
			String requestHeaders = "";

			for (int i = 0; i < httpHeaders.length; i++)
			{
				requestHeaders = requestHeaders + httpHeaders[i].toString() + "\n";
			}
			System.setProperty("RequestHeaders", requestHeaders);

			httpResponse = httpClient.execute(httpPost);
			httpHeaders = httpResponse.getAllHeaders();

			String responseHeaders = "";

			for (int i = 0; i < httpHeaders.length; i++)
			{
				responseHeaders = responseHeaders + httpHeaders[i].toString() + "\n";
			}
			System.setProperty("ResponseHeaders", responseHeaders);

			String line = null;
			StringBuffer strResponseBuffer = new StringBuffer();
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				//ExcelReaderWriter.setCellValue()
				System.setProperty("Result", "Pass");
				System.setProperty("ResponseCode", String.valueOf(httpResponse.getStatusLine().getStatusCode()));

				String strStatus = "Post request was processed successfully with status code of "+httpResponse.getStatusLine().getStatusCode()+" "+httpResponse.getStatusLine().getReasonPhrase();

				BufferedReader bReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
				while((line = bReader.readLine())!= null)
				{
					strResponseBuffer .append(line);
				}

				Log.printArtifacts(strStatus, strURI, "POST", "", httpPost.getAllHeaders(), strPayLoad, httpResponse.getAllHeaders(), strResponseBuffer.toString());

				//List<String> strNextGenUserName = JsonPath.read(strResponseBuffer.toString(), "$..NextGenUserName");

				System.out.println("We have Authorized and the Logged in User is: " + strUserName + "\n");

			}
			else
			{
				System.setProperty("Result", "Fail with status code of "+httpResponse.getStatusLine().getStatusCode());
				String strStatus = "POST request failed with a response code of: "+httpResponse.getStatusLine().getStatusCode()+" "+httpResponse.getStatusLine().getReasonPhrase();
				System.setProperty("ResponseCode", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
				BufferedReader bReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
				while((line = bReader.readLine())!= null)
				{
					strResponseBuffer.append(line);
				}

				Log.printArtifacts(strStatus, strURI, "POST", "", httpPost.getAllHeaders(), strPayLoad, httpResponse.getAllHeaders(), strResponseBuffer.toString());
				//throw new LoginFailed("Login Failed");
			}

			return ((strResponseBuffer.toString()).isEmpty() || strResponseBuffer.toString() == null ? "No Response": strResponseBuffer.toString());
			//return GeneralUtils.prettyprintJsonResponse(strResponseBuffer.toString());
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
