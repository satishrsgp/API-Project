package com.utils;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.collections.map.MultiValueMap;

public class FinalSignature {

	public static String CanonicalURI(String uri)
	{
		if (uri == null /*&& uri.isEmpty()*/)
		{
			return "/";
		}

		java.net.URI url = null;
		try 
		{
			url = new java.net.URI(uri);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		// to get the absolute path of the Request URL
		String canonicalURI = url.getPath().replace("{", "%7B")
				.replace("}", "%7D")
				.replace("!", "%21")
				.replace("@", "%40")
				.replace("#", "%23")
				.replace("$", "%24")
				.replace("^", "%5E")
				.replace("*", "%2A")
				.replace("(", "%28")
				.replace(")", "%29")
				//.replace("-", "%2D")
				.replace("+", "%20")
				.replace(",", "%2C");
		//String canonicalURI = url.getPath().replace("{", "").replace("}", "");
		System.out.println("Canonical URI:"+canonicalURI);
		//canonicalURI;

		if (canonicalURI.startsWith("/"))
			return canonicalURI;
		else
			return "/" + canonicalURI;
	}

	public static String CanonicalQueryString(String queryParams)
	{
		try
		{
			if(queryParams.length() <= 0)
				//if (queryParams == null && queryParams.isEmpty())
			{
				//System.out.println("");
				return "";

			}
			//queryParams = queryParams.Remove(0, 1);
			else
			{
				queryParams = queryParams.substring(1, queryParams.length());
				//Make Microsoft RFC 2396 compliant
				queryParams = queryParams.replace("%27", "'");
				queryParams = queryParams.replace(":", "%3A");
				queryParams = queryParams.replace("@", "%40");
				//queryParams = queryParams.replace("$", "%24");
				queryParams = queryParams.replace(",", "%2C");
				queryParams = queryParams.replace("+", "%20");

				//Map<String, String> queryParameters = new HashMap<String, String>();
				MultiValueMap map = new MultiValueMap();

				String[] pairs = queryParams.split("&");
				String[] parts;

				for (String pair : pairs)
				{
					parts = pair.split("=");
					/*String key = parts[0].trim().replace("=", "");
					String value = parts[1];
					queryParameters.put(key, value);*/

					if(parts.length > 1)
					{
						String key = parts[0].trim().replace("=", "");
						String value = parts[1];
						//queryParameters.put(key, value);
						map.put(key, value);
					}
					else
					{
						String key = parts[0];
						String value = "";
						//queryParameters.put(key, value);
						map.put(key, value);
					}
				}

				//sort the dictionary based on ASCII code
				//Map<String, String> treeMap = new TreeMap<String, String>(queryParameters);
				@SuppressWarnings("unchecked")
				Map<String, Object> treeMap = new TreeMap<String, Object>(map);

				//build the query string from sorted parameters
				StringBuilder sb_QueryString = new StringBuilder();
				Set<?> entrySet = treeMap.entrySet();
				Iterator<?> it = entrySet.iterator();
				while (it.hasNext()) 
				{
					@SuppressWarnings("rawtypes")
					Map.Entry mapEntry = (Map.Entry) it.next();
					List<?> list = (List<?>) map.get(mapEntry.getKey());
					for (int j = 0; j < list.size(); j++) 
					{
						if(sb_QueryString.length() > 0)
							sb_QueryString.append("&");
						sb_QueryString.append(mapEntry.getKey());
						sb_QueryString.append("=");
						if (list.get(j) != null && !list.get(j).toString().isEmpty())
						{
							sb_QueryString.append(list.get(j));
						}
					}
				}
				System.out.println("CanonicalQueryString:"+sb_QueryString.toString());
				return sb_QueryString.toString();
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}

	public static String ComputeMD5(byte[] content)
	{
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		byte[] digest = m.digest(content);
		//String hash = new BigInteger(1, digest).toString(16);
		StringBuffer sb = new StringBuffer();
		for (byte b : digest) {
			sb.append(String.format("%02X", b));
		}

		System.out.println("ComputeMD5:"+sb.toString().toUpperCase());
		return sb.toString().toUpperCase();
	}

	public static String GetSigningString(String userName, String timestamp, String requestMethod, String uri, String queryStringMD5Hash, String requestBodyMD5Hash)
	{

		StringBuilder buf = new StringBuilder();
		buf.append(userName);
		buf.append(timestamp);
		buf.append(requestMethod);
		buf.append(CanonicalURI(uri));
		buf.append(queryStringMD5Hash);
		buf.append(requestBodyMD5Hash);

		/*if (requestMethod == "GET" || requestMethod == "DELETE")
			buf.append(queryStringMD5Hash);
		if (requestMethod == "POST" || requestMethod == "PUT")
			buf.append(requestBodyMD5Hash);*/
		System.out.println("GetSigningString: "+buf.toString());
		return buf.toString();
	}

	public static String ComputeSignature(String StringtoSing, String keyString)
	{	
		String digest = null;
		try {
			SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), "HmacSHA256");
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(key);

			byte[] bytes = mac.doFinal(StringtoSing.getBytes("UTF-8"));

			StringBuffer hash = new StringBuffer();
			for (int i = 0; i < bytes.length; i++) {
				String hex = Integer.toHexString(0xFF & bytes[i]);
				if (hex.length() == 1) {
					hash.append('0');
				}
				hash.append(hex);
			}
			digest = hash.toString();
		}
		catch (UnsupportedEncodingException e) {
		}
		catch (InvalidKeyException e) {
		}
		catch (NoSuchAlgorithmException e) {
		}
		System.out.println("ComputeSignature"+digest.toUpperCase());
		return digest.toUpperCase();
	}

	public static void main(String[] args) throws NoSuchAlgorithmException 
	{	
		/*String strfinalSignature = FinalSignature.ComputeSignature(
				FinalSignature.GetSigningString(strUserName.toLowerCase(), 
						GeneralUtils.getXNGDate() , 
						"GET", 
						FinalSignature.CanonicalURI(strURI), 
						FinalSignature.ComputeMD5(FinalSignature.CanonicalQueryString("").getBytes()), 
						FinalSignature.ComputeMD5(strPayLoad.getBytes())), 
				HashPassword.sha1(strPrePasswordHash));*/

		/*		String strfinalSignature = FinalSignature.ComputeSignature(
				FinalSignature.GetSigningString(strUserName.toLowerCase(), 
						"2017-07-21T15:33:09.854Z" , 
						"GET", 
						FinalSignature.CanonicalURI("http://10.145.9.139:888/v2/api/users/0/login-defaults"), 
						FinalSignature.ComputeMD5("".getBytes()), 
						FinalSignature.ComputeMD5("".getBytes())), 
				HashPassword.sha1(strPrePasswordHash));

		System.out.println("Final:"+strfinalSignature);*/

		//System.out.println(FinalSignature.ComputeSignature("adminnew2016-10-17T02:28:39GET/api/persons/E293A341-FF40-486F-A97F-2E1F97006D36/chartD41D8CD98F00B204E9800998ECF8427ED41D8CD98F00B204E9800998ECF8427E", "10A61D8F1A71ACE6C4BFA1B5347B95D0261B9689"));
	}
}
