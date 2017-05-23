package com.carrie.lldiary.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	
	public static String md5Password(String password) {

		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] result = digest.digest(password.getBytes());
			StringBuffer buffer = new StringBuffer();
			
			for (byte b : result) {
				int number = b & 0xff;
				String str = Integer.toHexString(number);
				// System.out.println(str);
				if (str.length() == 1) {
					buffer.append("0");
				}
				buffer.append(str);
			}
			
			return buffer.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}

	}

	
	public static String getFileMd5(String path) throws Exception {
		File file = new File(path);
		MessageDigest digest = MessageDigest.getInstance("md5");
		FileInputStream inputStream = new FileInputStream(file);
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inputStream.read(buffer)) != -1) {
			digest.update(buffer, 0, len);
		}

		StringBuffer stringBuffer = new StringBuffer();
		int number = 0;
		String str = null;
		byte[] result = digest.digest();
		for (byte b : result) {
			number = b & 0xff;
			str = Integer.toHexString(number);
			
			if (str.length() == 1) {
				stringBuffer.append("0");
			}
			stringBuffer.append(str);
		}

		return stringBuffer.toString();
	}
	
	public static String md5(String paramString) {
		String returnStr;
		
		try {
			MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
			localMessageDigest.update(paramString.getBytes());
			returnStr = byteToHexString(localMessageDigest.digest());
			return returnStr;
		} catch (Exception e) {
			return paramString;
		}
	}
	
	/**
	 * 将制定byte数组转换成16进制字符串
	 * 
	 * @param b
	 * @return
	 */
	private static String byteToHexString(byte[] b) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			hexString.append(hex.toUpperCase());
		}
		return hexString.toString();
	}
}
