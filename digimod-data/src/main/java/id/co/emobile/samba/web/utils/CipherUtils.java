package id.co.emobile.samba.web.utils;

import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.RIPEMD256Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class CipherUtils {
	private static final Logger LOG = LoggerFactory.getLogger(CipherUtils.class);
	private static final int MAX_LENGTH_3DES_KEY = 24;

	
	// https://www.grc.com/passwords.htm
	private static final String KEY_CACHE_VO = 
				"9A5E1352B2059D997385A996A7C6D036A67D5175A52A0FF3";	

	private static final int IV_SIZE = 16;
	
	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	public static byte[] hashSHA256(String input) {
		SHA256Digest digester = new SHA256Digest();
		byte[] resBuf = new byte[digester.getDigestSize()];
		byte[] resPass = input.getBytes();
		digester.update(resPass, 0, resPass.length);
		digester.doFinal(resBuf, 0);
		return resBuf;
	}
	
	/*
	 * input in ASCII String
	 */

	
	/*
	 * input in HexString
	 */
	
	public static String decryptDESede(String input, String key) {
		byte[] hashPassword = hashSHA256(key);
		byte[] hashPasswordx = new byte[MAX_LENGTH_3DES_KEY];
		System.arraycopy(hashPassword, 0, hashPasswordx, 0, MAX_LENGTH_3DES_KEY);
		
		return new String(decryptDESede(CommonUtil.toHexByte(input), hashPasswordx));
	}
	
	public static byte[] decryptDESede(byte[] input, byte[] key) {
		try {
			Cipher cipher = Cipher.getInstance("DESede/ECB/ZeroBytePadding", "BC");
			SecretKeySpec secretKey = new SecretKeySpec(key, "DESEDE");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return cipher.doFinal(input);
		} catch (Exception e) {
			LOG.error("Error decryptDESede. Input: " + Arrays.toString(input) + 
					". Password: " + Arrays.toString(key), e);
			return null;
		}
	}
	
	public static String decryptAESPKCS7(String input, String key) {
		byte[] hashPassword = hashSHA256(key);
		byte[] hashPasswordx = new byte[MAX_LENGTH_3DES_KEY];
		System.arraycopy(hashPassword, 0, hashPasswordx, 0, MAX_LENGTH_3DES_KEY);
		
		return new String(decryptAESPKCS7(CommonUtil.toHexByte(input), hashPasswordx));
	}
	
	public static byte[] decryptAESPKCS7(byte[] input, byte[] key) {
		try {
			// Extract IV.
	        byte[] iv = new byte[IV_SIZE];
	        System.arraycopy(input, 0, iv, 0, iv.length);
	        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
	        
	        int length = input.length - IV_SIZE;
	        byte[] inputNoIv = new byte[length];
	        System.arraycopy(input, IV_SIZE, inputNoIv, 0, length);
	        
			
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
			SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
			return cipher.doFinal(inputNoIv);
		} catch (Exception e) {
			LOG.error("Error decryptAESPKCS7. Input: " + Arrays.toString(input) + 
					". Password: " + Arrays.toString(key), e);
			return null;
		}
	}	
	
	/*
	 * Input in HexString 
	 */
	public static byte[] decryptDESedeWithPassword(String input, String password) {
		return decryptDESedeWithPassword(CommonUtil.toHexByte(input), password);
	}
	
	public static byte[] decryptDESedeWithPassword(byte[] input, String password) {
		try {
			byte[] hashPassword = hashRIPEMD256(password);
			byte[] hashPasswordx = new byte[MAX_LENGTH_3DES_KEY];
			System.arraycopy(hashPassword, 0, hashPasswordx, 0, MAX_LENGTH_3DES_KEY);
			return decryptDESede(input,hashPasswordx); 
		} catch (Exception e) {
			LOG.error("Failed to decrypt DES EDE with Password. Input: " + 
					", password: " + password +". "  + e);
			return null;
		}
	}
	
	public static String mixString(String data1, String data2) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < Math.max(data1.length(), data2.length()); i++) {
			if (i < data1.length())
				sb.append(data1.substring(i, i + 1));
			if (i < data2.length())
				sb.append(data2.substring(i, i + 1));
		}
		return sb.toString();
	}



	public static String passwordDigest(String password) {
		String mixedData = mixString(password, new StringBuilder(password)
				.reverse().toString());
		mixedData = mixedData.toLowerCase();
		String temp1 = new String(Base64.encode(mixedData.getBytes()));
		String temp2 = mixString(mixedData, temp1);

		MD5Digest digester = new MD5Digest();
		byte[] resBuf = new byte[digester.getDigestSize()];
		byte[] input = temp2.getBytes();
		digester.update(input, 0, input.length);
		digester.doFinal(resBuf, 0);

		String result = CommonUtil.toBase64(resBuf);

		return result;
	}

	public static String pinPassword(String str1, String str2) {
		Assert.notNull(str1, "str1 is null!");
		Assert.notNull(str2, "str2 is null!");
		String mixedData = mixString(str1, str2);
		MD5Digest digester = new MD5Digest();
		byte[] resBuf = new byte[digester.getDigestSize()];
		byte[] input = mixedData.getBytes();
		digester.update(input, 0, input.length);
		digester.doFinal(resBuf, 0);
		String result = CommonUtil.toBase64(resBuf);

		return result;
	}
	
	public static byte[] hashRIPEMD256(String input) {
		RIPEMD256Digest digester = new RIPEMD256Digest();
		byte[] resBuf = new byte[digester.getDigestSize()];
		byte[] resPass = input.getBytes();
		digester.update(resPass, 0, resPass.length);
		digester.doFinal(resBuf, 0);
		return resBuf;
	}
	
	public static byte[] encryptDESedeWithPassword(String input, String password) {
		return encryptDESedeWithPassword(input.getBytes(), password);
	}
	
	public static String encryptDESede(String input, String key) {
		byte[] hashPassword = hashSHA256(key);
		byte[] hashPasswordx = new byte[MAX_LENGTH_3DES_KEY];
		System.arraycopy(hashPassword, 0, hashPasswordx, 0, MAX_LENGTH_3DES_KEY);
		return CommonUtil.toHexString(encryptDESede(input.getBytes(), hashPasswordx) );
	}
	
	public static byte[] encryptDESede(byte[] input, byte[] key) {
		try {
			Cipher cipher = Cipher.getInstance("DESede/ECB/ZeroBytePadding", "BC");
			SecretKeySpec secretKey = new SecretKeySpec(key, "DES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return cipher.doFinal(input);
		} catch (Exception e) {
			LOG.error("Error encryptDESede. Input: " + Arrays.toString(input) + 
					". Password: " + Arrays.toString(key), e);
			return null;
		}
	}
	
	public static String encryptAESPKCS7(String input, String key) {
		byte[] hashPassword = hashSHA256(key);
		byte[] hashPasswordx = new byte[MAX_LENGTH_3DES_KEY];
		System.arraycopy(hashPassword, 0, hashPasswordx, 0, MAX_LENGTH_3DES_KEY);
		return CommonUtil.toHexString(encryptAESPKCS7(input.getBytes(), hashPasswordx) );
	}
	
	public static byte[] encryptAESPKCS7(byte[] input, byte[] key) {
		try {
			byte[] iv = new byte[IV_SIZE];
	        SecureRandom random = new SecureRandom();
	        random.nextBytes(iv);
			IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
			SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
//			cipher.init(Cipher.ENCRYPT_MODE, secretKey);

			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
			byte[] encrypted = cipher.doFinal(input);
//			LOG.debug("encrypt {}", Arrays.toString(encrypted));
			
			// Combine IV and encrypted part.
	        byte[] outputWithIv = new byte[IV_SIZE + encrypted.length];
	        System.arraycopy(iv, 0, outputWithIv, 0, IV_SIZE);
	        System.arraycopy(encrypted, 0, outputWithIv, IV_SIZE, encrypted.length);
	        
			return outputWithIv;
		} catch (Exception e) {
			LOG.error("Error encryptDESedePKCS7. Input: " + Arrays.toString(input) + 
					". Password: " + Arrays.toString(key), e);
			return null;
		}
	}
	
	public static byte[] encryptDESedeWithPassword(byte[] input, String password) {
		try {
			byte[] hashPassword = hashRIPEMD256(password);
			byte[] hashPasswordx = new byte[MAX_LENGTH_3DES_KEY];
			System.arraycopy(hashPassword, 0, hashPasswordx, 0, MAX_LENGTH_3DES_KEY);
			return encryptDESede(input,hashPasswordx); 
		} catch (Exception e) {
			LOG.error("Failed to encrypt DES EDE with Password. Input: " + 
					", password: " + password +". "  + e);
			return null;
		}
	}
	
}
