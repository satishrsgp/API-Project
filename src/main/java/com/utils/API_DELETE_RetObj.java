package com.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.CloseableHttpClient;

public class API_DELETE_RetObj 
{
	public static RequestResponseHandler sendDeleteRequest(CloseableHttpClient httpClient, String strURI, String strQueryString)
	{
		HttpDelete httpDelete;
		CloseableHttpResponse httpResponse;
		Header[] httpHeaders;
		//StringEntity entity;
		String strfinalSignature;
		
		RequestResponseHandler objDTO = new RequestResponseHandler();
		try
		{
			// Creating the PUT Request that needs to be executed
			httpDelete = new HttpDelete(strURI);
			objDTO.setStrRequestURI(httpDelete.getURI().toString());

			//Add the Request Headers
			httpDelete.addHeader("Content-Type", "application/json");
			
			String strPayLoad  = "";
			
			//There will be no Pay load for Delete and Get Requests
			/*// The Pay load creation part
			entity = new StringEntity(strPayLoad, ContentType.APPLICATION_JSON);

			//Setting the Request body with the Pay Load
			httpPut.setEntity(entity);*/

			//Computing the final signature
			strfinalSignature = FinalSignature.ComputeSignature(FinalSignature.GetSigningString(System.getProperty("UserName"), GeneralUtils.getXNGDate() , "DELETE", FinalSignature.CanonicalURI(strURI), FinalSignature.CanonicalQueryString(strQueryString), FinalSignature.ComputeMD5(strPayLoad.getBytes())), System.getProperty("SessionToken"));
			httpDelete.addHeader("Content-Type", "application/json");
			String strAuthHeader = "NEXTGEN-AMB-API Credential=" + System.getProperty("UserName") + ", Signature=" + strfinalSignature;
			httpDelete.addHeader("Authorization", strAuthHeader);
			httpDelete.addHeader("X-NG-Date", GeneralUtils.getXNGDate());

			//Reading all the Request Headers
			httpHeaders = httpDelete.getAllHeaders();
			String requestHeaders = "";
			
			for (int i = 0; i < httpHeaders.length; i++)
			{
				requestHeaders = requestHeaders + httpHeaders[i].toString() + "\n";
			}
			objDTO.setStrRequestHeaders(requestHeaders);

			//Execute the Request
			httpResponse = httpClient.execute(httpDelete);

			//Read all the Response Headers
			httpHeaders = httpResponse.getAllHeaders();
			String responseHeaders = "";
			
			for (int i = 0; i < httpHeaders.length; i++)
			{
				responseHeaders = responseHeaders + httpHeaders[i].toString() + "\n";
			}
			objDTO.setStrResponseHeaders(responseHeaders);

			String line = null;
			StringBuffer strResponseBuffer = new StringBuffer();
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK || httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT)
			{
				objDTO.setStrRequestStatus("Pass");
				objDTO.setIntResponseCode(httpResponse.getStatusLine().getStatusCode());
				String strStatus = "DELETE request processed successfully with status code of "+httpResponse.getStatusLine().getStatusCode()+" "+httpResponse.getStatusLine().getReasonPhrase();
				
				if(!(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_NO_CONTENT))
				{
					BufferedReader bReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
					while((line = bReader.readLine())!= null)
					{
						strResponseBuffer .append(line);
					}
					objDTO.setStrResponse(strResponseBuffer.toString());
				}
				else
					objDTO.setStrResponse("For DELETE there will be no JSON Response");
				Log.printArtifacts(strStatus, strURI, "PUT", "", httpDelete.getAllHeaders(), strPayLoad, httpResponse.getAllHeaders(), strResponseBuffer.toString());
			}
			else
			{
				objDTO.setStrRequestStatus("Fail");
				objDTO.setIntResponseCode(httpResponse.getStatusLine().getStatusCode());
				String strStatus = "PUT request failed with a response code of: "+httpResponse.getStatusLine().getStatusCode()+" "+httpResponse.getStatusLine().getReasonPhrase();
				BufferedReader bReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
				while((line = bReader.readLine())!= null)
				{
					strResponseBuffer.append(line);
				}
				objDTO.setStrResponse(strResponseBuffer.toString());
				Log.printArtifacts(strStatus, strURI, "DELETE", "", httpDelete.getAllHeaders(), strPayLoad, httpResponse.getAllHeaders(), strResponseBuffer.toString());
			}
			return objDTO;
		}catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

}
