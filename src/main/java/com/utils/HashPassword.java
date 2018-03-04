package com.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashPassword 
{
	public static String sha1(String strPrePasswordHash) throws NoSuchAlgorithmException {
		/*MessageDigest mDigest = MessageDigest.getInstance("SHA1");
		byte[] result = mDigest.digest(strPrePasswordHash.getBytes());
		StringBuffer strPasswordHash = new StringBuffer();
		for (int i = 0; i < result.length; i++) {
			if(result[i)
				strPasswordHash.append(Integer.toString((result[i+1] & 0xff) + 0x100, 16).substring(1));
				//continue;
			strPasswordHash.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
		}
		for(byte b: result){
			for (int i = 0; i < hash.length; ++i)
	            hashWithoutPadding.push(hash[i][0] === "0" ? hash[i][1] : hash[i]);
			if()
			strPasswordHash.append(String.format("%02x", b & 0xff));
		}
			
		return strPasswordHash.toString();*/
		
		//Change made on 21th July 2017
		MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(strPrePasswordHash.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; ++i) {

        	sb.append(Integer.toHexString(result[i] & 0xFF));
        }
        return sb.toString().toUpperCase();
/*        StringBuffer sb = new StringBuffer();

            sb.append(Integer.toHexString(result[i] & 0xFF));
        }
        return sb.toString().toUpperCase();
		
		/*MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(strPrePasswordHash.getBytes());
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        
        String input = sb.toString().toUpperCase();
        char[] cArray = input.toCharArray();
        String[] cArray1 = new String[cArray.length/2];
        int x=0;
        for(int i=0; i< cArray.length; i++)
        {
        	if(i!=0 && i%2==0 )
        		x++;
        	
        	if(cArray1[x] == null )
        		cArray1[x]="";
        	cArray1[x] += cArray[i];
        	
        }
        StringBuffer sb1 = new StringBuffer();
        for(int i=0; i< cArray1.length;i++)
        {
        	char[] c = cArray1[i].toCharArray();
        	if(c[0] =='0')
        	{
        		sb1.append(c[1]);
        	}
        	else
        	{
        		sb1.append(c[0]);
        		sb1.append(c[1]);
        	}
        		
        }
        
        //System.out.println(sb1.toString());

//        return sb1.toString();

        return sb1.toString();*/
		

	}
	public static void main(String[] args) throws NoSuchAlgorithmException
	{
		//String str = sha1("adminnewnghashAdminNew");
		/*String str = sha1("bdowlingnghashbdowling1");
		System.out.println(str);*/
	}
}
