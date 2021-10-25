package id.co.emobile.samba.web.utils;

import java.security.Security;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bouncycastle.crypto.digests.RIPEMD256Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecureUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(SecureUtils.class);

	private static final String INTERNAL_PASSWORD_TOKEN = "DFF80BA1B01E1FCB960C3ACDDF63E386";
														   	
	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	public  static  boolean  regexPasswordChecker(int  minLength,String password) {
		String regex = "^.*(?=.{"  + minLength + ",})(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(password);
		return  matcher.matches();
	}
	
	private static String mixString(String data1, String data2) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < Math.max(data1.length(), data2.length()); i++) {
			if (i < data1.length())
				sb.append(data1.substring(i, i + 1));
			if (i < data2.length())
				sb.append(data2.substring(i, i + 1));
		}
		return sb.toString();
	}

	public static String passwordDigest(String userId, String password) {
		String upperUserId = userId.toUpperCase();
		String lowerPassword = password.toLowerCase();
		String mixedData ="";
		while (mixedData.length() < INTERNAL_PASSWORD_TOKEN.length()) {
			mixedData = mixString(mixedData, mixString(lowerPassword, upperUserId));
		}
		
		String temp1 = new String(Base64.encode(mixedData.getBytes()));
		String temp2 = mixString((new StringBuilder(INTERNAL_PASSWORD_TOKEN)).reverse().toString(), temp1);

		RIPEMD256Digest digester = new RIPEMD256Digest();
		byte[] resBuf = new byte[digester.getDigestSize()];
		byte[] input = temp2.getBytes();
		digester.update(input, 0, input.length);
		digester.doFinal(resBuf, 0);

		String result = new String(Hex.encode(resBuf));

		return result.toUpperCase();
		
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
	 * Redundant from jets-commons.Utils, declared here to remove dependency to jets-commons
	 * Convert from HexByte to HexString
	 */
	public static String toHexString(byte[] data) {
		return new String(Hex.encode(data)).toUpperCase();
	}

	/*
	 * Redundant from jets-commons.Utils, declared here to remove dependency to jets-commons
	 * Convert from HexString to HexByte
	 */
	public static byte[] toHexByte(String input) {
		return Hex.decode(input);
	}
	
	/*
	 * Redundant from jets-commons.Utils, declared here to remove dependency to jets-commons
	 */
	public static String toBase64(byte[] data) {
		return new String(Base64.encode(data));
	}
	
	/*
	 * Redundant from jets-commons.Utils, declared here to remove dependency to jets-commons
	 */
	public static byte[] padByteArray(String sIn, byte pad, int length) {
		byte[] bOut = sIn.getBytes();
		int i = length - (bOut.length % length);
		if (i < length) {
			byte[] bNew = new byte[bOut.length + i];
			System.arraycopy(bOut, 0, bNew, 0, bOut.length);
			for (int j = bOut.length; j < bNew.length; j++) {
				bNew[j] = pad;
			}
			bOut = bNew;
		}
		return bOut;
	}


}
