package com.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

public class API_GET 
{
	public static HttpGet httpGet;
	public static CloseableHttpResponse httpResponse;
	public static Header[] httpHeaders;
	public static String strfinalSignature;

	public static String sendGetRequest(CloseableHttpClient httpClient, String strURI, String strQueryString)
	{
		try 
		{
			httpGet = new HttpGet(strURI);

			String strPayLoad  = "";
			System.setProperty("RequestURI", httpGet.getURI().toString());

			strfinalSignature = FinalSignature.ComputeSignature(FinalSignature.GetSigningString(System.getProperty("UserName"), GeneralUtils.getXNGDate() , "GET", FinalSignature.CanonicalURI(strURI), FinalSignature.CanonicalQueryString(strQueryString), FinalSignature.ComputeMD5(strPayLoad.getBytes())), System.getProperty("SessionToken"));

			httpGet.addHeader("Content-Type", "application/json");
			String strAuthHeader = "NEXTGEN-AMB-API Credential=" + System.getProperty("UserName") + ", Signature=" + strfinalSignature;
			httpGet.addHeader("Authorization", strAuthHeader);
			httpGet.addHeader("X-NG-Date", GeneralUtils.getXNGDate());

			httpHeaders = httpGet.getAllHeaders();
			System.setProperty("RequestHeaders", httpHeaders.toString());

			String requestHeaders = "";

			for (int i = 0; i < httpHeaders.length; i++)
			{
				requestHeaders = requestHeaders + httpHeaders[i].toString() + "\n";
			}
			System.setProperty("RequestHeaders", requestHeaders);

			httpResponse = httpClient.execute(httpGet);
			httpHeaders = httpResponse.getAllHeaders();

			String line = null;
			StringBuffer strResponseBuffer = new StringBuffer();
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				//ExcelReaderWriter.setCellValue()
				System.setProperty("Result", "Pass");

				String strStatus = "Get Request is fine with status code of "+httpResponse.getStatusLine().getStatusCode()+" "+httpResponse.getStatusLine().getReasonPhrase();

				BufferedReader bReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
				while((line = bReader.readLine())!= null)
				{
					strResponseBuffer .append(line);
				}

				Log.printArtifacts(strStatus, strURI, "GET", "", httpGet.getAllHeaders(), strPayLoad, httpResponse.getAllHeaders(), strResponseBuffer.toString());
				//JsonPa
				//List<String> strAllergyDescription = JsonPath.read(strResponseBuffer.toString(), "$..RxnDesc");

				//System.out.println(strAllergyDescription.get(0).toString());
				return GeneralUtils.prettyprintJsonResponse(strResponseBuffer.toString());
			}
			else if(httpResponse.getStatusLine().getStatusCode() == 400)
			{
				System.setProperty("Result", "Pass");
				String strStatus = httpResponse.getStatusLine().getStatusCode()+" "+httpResponse.getStatusLine().getReasonPhrase();
				Log.printArtifactsForBadRequest(strStatus, strURI, "GET", "", httpGet.getAllHeaders(), strPayLoad);
				return "";
			}
			else
			{
				System.setProperty("Result", "Fail with status code of "+httpResponse.getStatusLine().getStatusCode());
				String strStatus = "Response is: "+httpResponse.getStatusLine().getStatusCode()+" "+httpResponse.getStatusLine().getReasonPhrase();

				BufferedReader bReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
				while((line = bReader.readLine())!= null)
				{
					strResponseBuffer.append(line);
				}
				Log.printArtifacts(strStatus, strURI, "GET", "", httpGet.getAllHeaders(), strPayLoad, httpResponse.getAllHeaders(), strResponseBuffer.toString());
				//throw new LoginFailed("Get Allergy Route Failed");
				return GeneralUtils.prettyprintJsonResponse(strResponseBuffer.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
}
