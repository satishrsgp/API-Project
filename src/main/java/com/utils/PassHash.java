package com.utils;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PassHash
{
	public static String Hash(String userName, String password) throws NoSuchAlgorithmException
	{
		userName = userName.toLowerCase();
		String passwordToHash = userName + "nghash" + password;
		return Hash(passwordToHash);
	}
	
	public static String Hash(String passwordToHash) throws NoSuchAlgorithmException
	{
		byte[] bytesToHash = passwordToHash.getBytes(StandardCharsets.US_ASCII);

		MessageDigest hasher = MessageDigest.getInstance("SHA-1");
		hasher.reset();    
		hasher.update(bytesToHash);
		byte[] hashedBytes = hasher.digest();

		String returnValue = "";
		for (byte hashedByte : hashedBytes) {
			String stringOfByte;
			if(hashedByte == 126)
				stringOfByte = "0A";
			else
				stringOfByte = String.format("%x", hashedByte);

			returnValue += stringOfByte;
		}
		//System.out.println("PassHash Is:"+returnValue.toUpperCase());
		return returnValue.toUpperCase();
	}
}
