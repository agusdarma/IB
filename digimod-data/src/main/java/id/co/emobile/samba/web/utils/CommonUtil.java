package id.co.emobile.samba.web.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonUtil {
	private static final Logger LOG = LoggerFactory.getLogger(CommonUtil.class);
	private static SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //("dd-MMM-yyyy HH:mm:ss");
	private static SimpleDateFormat sdfMonthOnly = new SimpleDateFormat("MM"); 
	private static NumberFormat nfNoDecimal = new DecimalFormat("#,##0");
	private static char[] ALPHANUM = "ABCDEFGHJKLMNPQRSTUVWXYZ0123456789".toCharArray();
	private static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	private static final Pattern PATTERN_DATETIME = 
			Pattern.compile("([0-9]{1,2}/[0-9]{1,2}/[0-9]{2,4})|([0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2})");
	
	public static String formatTimeMyfxBook = "MM/dd/yyyy HH:mm";
	
	private static final Random r = new Random();
	
	public static String convertNumericFormatToString(String ori) {
		String result = "";
		result = ori.replaceAll(",", "");
		LOG.debug("ori :" + ori );
		result = result.replaceAll("\\.", "");
//		LOG.debug("result2 :" + result );
		result = result.replaceAll("-", "");
//		LOG.debug("result3 :" + result );
		result = result.replaceAll("/", "");
		LOG.debug("result4 :" + result );
		return result;
	}
	
	public static String convertPlainToNpwpFormatToString(String ori) {
		LOG.debug("convertPlainToNpwpFormatToString :" + ori );
		if(StringUtils.isNotEmpty(ori)) {
			ori = ori.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{1})(\\d{3})(\\d{3})", "$1.$2.$3.$4-$5.$6");	
		}		
		return ori;
	}
	
	public static String convertPlainToNopFormatToString(String ori) {
		LOG.debug("convertPlainToNopFormatToString :" + ori );
		if(StringUtils.isNotEmpty(ori)) {
			ori = ori.replaceAll("(\\d{2})(\\d{2})(\\d{3})(\\d{3})(\\d{3})(\\d{4})(\\d{1})", "$1.$2.$3.$4.$5-$6.$7");	
		}		
		return ori;
	}
	
	public static String convertPlainToNoSkFormatToString(String ori) {
		LOG.debug("convertPlainToNoSkFormatToString :" + ori );
		if(StringUtils.isNotEmpty(ori)) {
			ori = ori.replaceAll("(\\d{5})(\\d{3})(\\d{2})(\\d{3})(\\d{2})", "$1/$2/$3/$4/$5");	
		}		
		return ori;
	}
	
	public static String terbilang(BigDecimal value) {
		value = value.setScale(0, BigDecimal.ROUND_HALF_EVEN);
		String strValue = value.toString();

		int lenValue = strValue.length();
		int x = 0;
		int y = 0;
		int z;
		String bil1 = "";
		String bil2 = "";
		String urai = "";
		while (x < lenValue) {
			x = x + 1;
			int strTot = Integer.valueOf(strValue.substring(x - 1, x));
			y = y + strTot;
			z = lenValue - x + 1;
			switch (strTot) {
			case 1:
				if (z == 1 || z == 7 || z == 10 || z == 13) {
					bil1 = "Satu ";
				} else if (z == 4) {
					if (x == 1) {
						bil1 = "Se"; 
					} else {
						bil1 = "Satu ";
					}
				} else if (z == 2 || z == 5 || z == 8 || z == 11 || z == 14) {
					x = x + 1;
					int newStrTot = Integer.valueOf(strValue.substring(x - 1, x));
					z = lenValue - x + 1;
					bil2 = "";
					switch (newStrTot) {
					case 0:
						bil1 = "Sepuluh ";
						break;
					case 1:
						bil1 = "Sebelas ";
						break;
					case 2:
						bil1 = "Dua belas ";
						break;
					case 3:
						bil1 = "Tiga belas ";
						break;
					case 4:
						bil1 = "Empat belas ";
						break;
					case 5:
						bil1 = "Lima belas ";
						break;
					case 6:
						bil1 = "Enam belas ";
						break;
					case 7:
						bil1 = "Tujuh belas ";
						break;
					case 8:
						bil1 = "Delapan belas ";
						break;
					case 9:
						bil1 = "Sembilan belas ";
						break;
					default:
						break;
					}
				} else {
					bil1 = "Se";
				}
				break;
			case 2:
				bil1 = "Dua ";
				break;
			case 3:
				bil1 = "Tiga ";
				break;
			case 4:
				bil1 = "Empat ";
				break;
			case 5:
				bil1 = "Lima ";
				break;
			case 6:
				bil1 = "Enam ";
				break;
			case 7:
				bil1 = "Tujuh ";
				break;
			case 8:
				bil1 = "Delapan ";
				break;
			case 9:
				bil1 = "Sembilan ";
				break;
			default:
				bil1 = "";
				break;
			}
			
			if (strTot > 0) {
				if (z == 2 || z == 5 || z == 8 || z == 11 || z == 14) {
				   bil2 = "Puluh ";
				} else if (z == 3 || z == 6 || z == 9 || z == 12 || z == 15) {
				   bil2 = "Ratus ";
				} else {
				   bil2 = "";
				}
			} else {
				bil2 = "";
			}
			
			if (y > 0) {
				switch (z) {
				case 4:
					bil2 = bil2 + "Ribu ";
					y = 0;
					break;
				case 7:
					bil2 = bil2 + "Juta ";
					y = 0;
					break;
				case 10:
					bil2 = bil2 + "Milyar ";
					y = 0;
					break;
				case 13:
					bil2 = bil2 + "Trilyun ";
					y = 0;
					break;
				default:
					break;
				}
			}
			
			if (bil1.equals("Se")) {
				String pre = bil2.substring(0, 1);
				urai = urai + bil1 + bil2.replace(pre, pre.toLowerCase());
			} else {
				urai = urai + bil1 + bil2;
			}
		}
		
		return urai;
	}
	
	public static boolean checkImageExtension(String extension){
		if(extension.equalsIgnoreCase("jpg")||extension.equalsIgnoreCase("jpeg")||
				extension.equalsIgnoreCase("png")||extension.equalsIgnoreCase("gif"))
		{
			return true;
		}
		return false;
	}
	
	public static String trim(String str) {
		if (str != null) {
			return str.trim();
		}
		return str;
	}

	public static String trimAll(String str) {
		if (str != null) {
			str = trim(str).replaceAll("\\s+", " ");
			return str;
		}
		return str;
	}

	/*
	 * Convert from HexByte to HexString
	 */
	public static String toHexString(byte[] data) {
		return new String(Hex.encode(data)).toUpperCase();
	}

	/*
	 * http://www.exampledepot.com/egs/java.util/Bits2Array.html Returns a
	 * bitset containing the values in bytes. The byte-ordering of bytes must be
	 * big-endian which means the most significant bit is in element 0
	 */
	public static BitSet fromByteArray(byte[] bytes) {
		BitSet bits = new BitSet();
		for (int i = 0; i < bytes.length * 8; i++) {
			if ((bytes[bytes.length - i / 8 - 1] & (1 << (i % 8))) > 0) {
				bits.set(i);
			}
		}
		return bits;
	}

//	public static void main(String[] args) {
//		String x = "6E9EE33C921E0F7C1906E4BF7F57E192CA7318DCE2AF505BB57D4C1BE8FED3B821B10A0E235A1C5E0085B32CD9685476638E020F8444E115EA2666935E23E8B967D4BD930E54377300AB14B58855687FC77C8132E6D360B0844DDBCDA201012A";
//		String b = "testing";
//		String g = toHexString(b.getBytes());
//		System.out.println(g);
//		System.out.println(toHexsString(g.getBytes()));
//	}
//	
//	public static String toHexsString(byte[] data) {
//		return new String(Hex.decode(data)).toUpperCase();
//	}
	
	/*
	 * http://www.exampledepot.com/egs/java.util/Bits2Array.html Returns a byte
	 * array of at least length 1. The most significant bit in the result is
	 * guaranteed not to be a 1 (since BitSet does not support sign extension).
	 * The byte-ordering of the result is big-endian which means the most
	 * significant bit is in element 0. The bit at index 0 of the bit set is
	 * assumed to be the least significant bit.
	 */
	public static byte[] toByteArray(BitSet bits) {
		// byte[] bytes = new byte[bits.length()/8+1];
		// WARNING !!! set fixed to 8 byte, only to be used for ISOMsg
		byte[] bytes = new byte[8];
		for (int i = 0; i < bits.length(); i++) {
			if (bits.get(i)) {
				bytes[bytes.length - i / 8 - 1] |= 1 << (i % 8);
			}
		}
		return bytes;
	}
	
	public static String toHexString(BitSet bits) {
		return toHexString(toByteArray(bits));
	}

	public static BitSet toBitSet(String hexStr) {
		return fromByteArray(toHexByte(hexStr));
	}
	
	/*
	 * Convert from HexString to HexByte
	 */
	public static byte[] toHexByte(String input) {
		return Hex.decode(input);
	}
	
	public static String toBase64(byte[] data) {
		return new String(Base64.encode(data));
	}
	
	public static String hexToAscii(String hex){       
        if (hex.length()%2 != 0){
        	LOG.warn("requires EVEN number of chars");
        	return null;
        }
        StringBuilder sb = new StringBuilder();               
        //Convert Hex 0232343536AB into two characters stream.
        for( int i=0; i < hex.length()-1; i+=2 ){
            String output = hex.substring(i, (i + 2));
            int decimal = Integer.parseInt(output, 16);                 
            sb.append((char)decimal);             
        }           
        return sb.toString();
	}
	
	public static String binToHex(String bin) {
	    String hex = Long.toHexString(Long.parseLong(bin, 2));
	    return String.format("%" + (int)(Math.ceil(bin.length() / 4.0)) + "s", hex).replace(' ', '0');
	}
	
	public static String repeat(String str, int count) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; i++) {
			sb.append(str);
		}
		return sb.toString();
	}
	
	public static String padRight(String str, char c, int length) {
		if (str == null) str = "";
		StringBuilder sb = new StringBuilder(str);
		while (sb.length() < length)
			sb.append(c);
		return sb.toString();
	}
	
	public static String padRight(String str, int length) {
		return padRight(str, ' ', length);
	}

	public static String padLeft(String str, char c, int length) {
		if (str == null) str = "";
		str = str.trim();
		StringBuilder sb = new StringBuilder();
		if (str.length() >= length) {
			return str;
		}
		int fill = length - str.length();
		while (fill-- > 0)
			sb.append(c);
		sb.append(str);
		return sb.toString();
	}
	
	public static String padCenter(String str, char c, int length) {
		if (str == null) str = "";
		str = str.trim();
		StringBuilder sb = new StringBuilder();
		if (str.length() >= length) {
			return str;
		}
		
		int lengthLeft = (length - str.length())/2;
		int lengthRight = (length - str.length())/2;
		while (lengthLeft -- > 0)
			sb.append(c);
		sb.append(str);
		while (lengthRight -- >0)
			sb.append(c);
		return sb.toString();
	}
	
	public static String zeroPadLeft(String str, int length) {
		return padLeft(str, '0', length);
	}
	
	public static String truncate(String str, int length) {
		if (str == null)
			return str;
		if (str.length() <= length) {
			return str;
		}
		else {
			return str.substring(0, length);
		}
	}
	
	/*
	 * The string length return is strict to the length,
	 * it will cut the string if necessary.
	 * This function is important in composing ISO which require an exact length.
	 * return pad right if string < length
	 */
	public static String cutOrPad(String str, int length) {
		if (str == null) {
			return repeat(" ", length);
		}
		if (str.length() < length) {
			return padRight(str, length);
		}
		else if (str.length() > length) {
			return truncate(str, length);
		}
		return str;
	}
	
	/*
	 * return the last x chars from right
	 */
	public static String rightSubstring(String str, int length) {
		if (str.length() <= length) {
			return str;
		}
		return str.substring(str.length() - length);
	}
	
	/**
	 * Parse QueryString from HTTP Get Method, e.g: ?query=1&param1=value1
	 * 
	 * @param query
	 *            Input
	 * @param params
	 *            Output Properties that hold the parameter that has been parsed
	 */
	public static void parseQueryString(String query, Properties params) {
		LOG.debug("Query :" + query +", params :  " + params);
		StringTokenizer st = new StringTokenizer(query, "?&=", true);
		String previous = null;
		while (st.hasMoreTokens()) {
			String current = st.nextToken();
			if ("?".equals(current) || "&".equals(current)) {
				// ignore
			} else if ("=".equals(current)) {
				String value = "";
				if (st.hasMoreTokens())
					value = st.nextToken();
				if ("&".equals(value))
					value = ""; // ignore &
				params.setProperty(previous, value);
			} else {
				previous = current;
			}
		}
	}

	private static long multiplier(int len) {
		long result = 1;
		for (int i = 1; i < len; i++) {
			result = result * 10;
		}
		return result;
	}

	/*
	 * Convert double value to a left-right padded string e.g:
	 * formatCurrency(9750.55, 6, 7) --> "0097505500000"
	 */
	public static String formatCurrency(double d, int lenLeft, int lenRight) throws Exception {
		d = Math.round(d * multiplier(lenRight));
		String r = Long.toString((long) d);
		r = CommonUtil.zeroPadLeft(r, lenLeft + lenRight);
		return r;
	}

	/*
	 * Convert String value to a left-right padded string e.g:
	 * formatCurrency("123456", 15, 2) --> "00000000012345600"
	 */
	public static String formatCurrency(String s, int lenLeft, int lenRight) throws Exception {
		double d = Double.parseDouble(s);
		String r = formatCurrency(d, lenLeft, lenRight);
		return r;
	}
	
	/**
	 * format phone number to International / ISO format: +628xxxx
	 * @param phoneNo
	 * @return
	 */
	public static String formatPhoneIntl(String phoneNo) {
		if (phoneNo == null)
			return phoneNo;
		phoneNo = phoneNo.trim();
		// TODO: this always assume that country is Indonesian, it must be
		// refactored to support other country
		if (phoneNo.startsWith("0")) // for 021900xxx, 0812xxxx
			phoneNo = "+62" + phoneNo.substring(1);
		else if (phoneNo.startsWith("+0")) // for +021900xxx
			phoneNo = "+62" + phoneNo.substring(2);
		else if (!phoneNo.startsWith("+")) // for 6221xxxx
			phoneNo = "+" + phoneNo;
		return phoneNo;
	}
	
	/**
	 * format phone number to Local: 08xxxx
	 * @param phoneNo
	 * @return
	 */
	public static String formatPhoneLocal(String phoneNo) {
		if (phoneNo == null)
			return phoneNo;
		if (phoneNo.startsWith("+")) {
			phoneNo = phoneNo.substring(1);
		}
		if (phoneNo.startsWith("62")) {
			phoneNo = 0 + phoneNo.substring(2);
		}
		if (!phoneNo.startsWith("0")) {
			phoneNo = 0 + phoneNo;
		}
		return phoneNo;
	}
	
	/*
	 * refer to: http://www.javapractices.com/topic/TopicAction.do?Id=62
	 */
	public static int random(int range) {
		return random(0, range);
	}

	public static int random(int start, int end) {
		long range = (long) end - (long) start + 1;
		long fraction = (long) (range * r.nextDouble());
		int randomNumber = (int) (fraction + start);
		return randomNumber;
	}
	
	public static String displayDateTime(Date dateTime) {
		return sdfDateTime.format(dateTime);
	}
	public static String displayMonthOnly(Date dateTime) {
		return sdfMonthOnly.format(dateTime);
	}
	
	public static Date strToDateTime(String pattern, String input) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		return df.parse(input);
	}
	
	// This function returns start hour of today
	// e.g: 2010-01-01 12:00:00.000
	public static Date getEarlyDate(Date date) {
		Calendar calFr = Calendar.getInstance();
		calFr.setTime(date);

		Date dateFr = calFr.getTime();
		
		calFr.set(Calendar.HOUR, 0);
		calFr.set(Calendar.MINUTE, 0);
		calFr.set(Calendar.SECOND, 0);
		calFr.set(Calendar.MILLISECOND, 0);
		calFr.set(Calendar.AM_PM, Calendar.AM);

		dateFr = calFr.getTime();
		
		return dateFr;
	}

	// This function returns start hour of today
	// e.g: 2010-01-01 23:59:59.999
	public static Date getLaterDate(Date date) {
		Calendar calFr = Calendar.getInstance();
		calFr.setTime(date);
		Calendar calTo = Calendar.getInstance();
		calTo.set(calFr.get(Calendar.YEAR), calFr.get(Calendar.MONTH), calFr.get(Calendar.DATE));

		calTo.set(Calendar.HOUR, 11);
		calTo.set(Calendar.MINUTE, 59);
		calTo.set(Calendar.SECOND, 59);
		calFr.set(Calendar.MILLISECOND, 999);
		calFr.set(Calendar.AM_PM, Calendar.PM);
		
		Date dateTo = calTo.getTime();
		
		return dateTo;
	}
	
	public static String displayNumberNoDecimal(double number) {
		return nfNoDecimal.format(number);
	}
	
	public static byte[] intToByteArray(int value) {
	    return new byte[] {
	    		(byte)(value >>> 24),
	    		(byte)(value >>> 16),
	    		(byte)(value >>> 8),
	    		(byte)value};
	}
	
	public static int byteArrayToInt(byte[] b) {
		return   b[3] & 0xFF |
				(b[2] & 0xFF) << 8 |
				(b[1] & 0xFF) << 16 |
				(b[0] & 0xFF) << 24;
	}
	
	// convert money into indonesian format
	// e.g: 7,500.00 --> "7.500" (digits 0)
	public static String formatMoneyIndo(double money, int digits){
		Locale lokal = new Locale("id","ID"); // language code and country code from ISO 639 googling
		NumberFormat nf = NumberFormat.getInstance(lokal);
		nf.setMaximumFractionDigits(digits); // for max digit after comma e.g Rp7.000,00
		nf.setMinimumFractionDigits(digits);
		return nf.format(money);
	}
	
	// convert integer into indonesian format
	// e.g: 7,500.00 --> "7.500" (digits 0)
	public static String formatIntegerIndo(int money, int digits){
		Locale lokal = new Locale("id","ID"); // language code and country code from ISO 639 googling
		NumberFormat nf = NumberFormat.getInstance(lokal);
		nf.setMaximumFractionDigits(digits); // for max digit after comma e.g Rp7.000,00
		nf.setMinimumFractionDigits(digits);
		return nf.format(money);
	}
	
	// convert indonesian money format into double
	// e.g: "7.500" --> 7,500
	public static Number parseMoneyIndo(String money) {
		try {
			Locale lokal = new Locale("id","ID");
			NumberFormat nf = NumberFormat.getInstance(lokal);
			return nf.parse(money);
		} catch (ParseException e) {
			return 0;
		}
	}
	
	/*
	 * list file in directory
	 */
	public static String[] listDir(String path) {
		File file = new File(path);

		File files[];

		files = file.listFiles();
		Arrays.sort(files);

		String[] list = new String[files.length];
		for (int i = 0, n = files.length; i < n; i++) {
			list[i] = files[i].toString();
		}
		return list;
	}

	/*
	 * Compress files into a single zip file
	 */
	public static void createZipFile(Set<String> inputFiles, String outputFile)
			throws IOException {
		byte[] buf = new byte[1024];
		try {
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					outputFile));
			Iterator<String> it = inputFiles.iterator();
			while (it.hasNext()) {
				String file = it.next();

				FileInputStream in = new FileInputStream(file);
				out.putNextEntry(new ZipEntry(extractFileName(file)));

				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}

				out.closeEntry();
				in.close();
			}
			out.close();
		} catch (IOException e) {
			throw e;
		}
	}

	public static String extractFileName(String path) {
		String result = "";
		int pos = path.lastIndexOf(".");
		if (pos > 0) {
			if (path.indexOf("/") >= 0) {
				result = path.substring(path.lastIndexOf("/") + 1);
			} else {
				result = path.substring(path.lastIndexOf("\\") + 1);
			}
		}
		return result;
	}

	public static boolean compareEqual(String str1, String str2) {
		if (str1 == null) str1 = "";
		if (str2 == null) str2 = "";
		
		str1 = str1.trim();
		str2 = str2.trim();
		
		return str1.equalsIgnoreCase(str2);
	}

	public static String cleanString(String str) {
		return str.replaceAll("[^a-zA-Z0-9 \\s\\.\\,]", " ").trim();
	}

	public static String formatDestroy(String input) {		
		SimpleDateFormat df = new SimpleDateFormat("yyMMd");
		String temp = df.format(new Date());	
		String tanggal = temp.substring(4);
		String bulan = temp.substring(2,4);
		String tahun = temp.substring(1, 2);
		if (Integer.parseInt(bulan) > 9) {
			if (bulan.equals("10")) {
				bulan = "A";
			} else {
				if (bulan.equals("11")) {
					bulan = "B";
				} else {
					if (bulan.equals("12"))
						bulan = "C";
				}
			}
		} 
		return tahun + bulan + tanggal + "#" + input;
	}
	
	public static String generateRandomPin(int pinLength) {
		StringBuilder sb = new StringBuilder(pinLength);
		for (int i = 0; i < pinLength; i++)
			sb.append("" + r.nextInt(10));
		return sb.toString();
	}
	
	public static String generateRandomAlphanum(int length) {
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int idx = r.nextInt(ALPHANUM.length);
			sb.append(ALPHANUM[idx]);
		}
		return sb.toString();
	}
	
	public static HashMap<Integer, String> getListMonthUtil()
	{
		HashMap<Integer, String> hm = new LinkedHashMap<Integer, String>();
		hm.put(1,"January");
		hm.put(2,"February");
		hm.put(3,"March");
		hm.put(4,"April");
		hm.put(5,"May");
		hm.put(6,"June");
		hm.put(7,"July");
		hm.put(8,"August");
		hm.put(9,"September");
		hm.put(10,"October");
		hm.put(11,"November");
		hm.put(12, "December");
		return hm;
	}
	
	public static List<Integer> listDateDynamic(int month, int year)
	{
		int maxDate = 31;
		if(month == 4 || month == 6 || month == 9 || month == 11) maxDate = 30;
		if(month == 2)
		{
			if(year%4==0)
				maxDate=29;
			else
				maxDate=28;
		}
		
		List<Integer> listDate = new ArrayList<Integer>();
		for (int i = 1; i <= maxDate; i++) {
			listDate.add(i);
		}
		return listDate;
	}
	
	public static HashMap<Integer, Integer> getListDate()
	{
		
		HashMap<Integer, Integer> hm = new LinkedHashMap<Integer, Integer>();
		for (int i = 1; i <= 31; i++) {
			hm.put(i, i);
		}
		return hm;
	}

	public static String htmlPurifier(String field)
	{
		PolicyFactory purifier = new HtmlPolicyBuilder().toFactory();
		field = purifier.sanitize(field);
		field = StringEscapeUtils.unescapeHtml4(field);		
		return field; 
	}
	
	public static String tokenGenerator()
	{
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public static Locale localeFinder(String language){
		if(language.equals("Bahasa Indonesia"))
		{
			return new Locale("id", "ID");
		}
		else
		{
			return new Locale("en", "US");
		}
	}
	
	// This function returns next day from what you insert
	public static Date getNextDay(Date date) {
		Calendar calFr = Calendar.getInstance();
		calFr.setTime(date);

		Date dateFr = calFr.getTime();
		
		calFr.add(Calendar.DATE, 1);
		dateFr = calFr.getTime();
		
		return dateFr;
	}
	
	public static String maskNumberForMessage(String message) {
		if (StringUtils.isEmpty(message)) return message;
		
		Map<String, String> mapReplaced = new HashMap<String, String>();
		Matcher mDatetime = PATTERN_DATETIME.matcher(message);
		while (mDatetime.find()) {
			int count = mDatetime.groupCount();
			for (int i = 0; i < count; i++){
				if (mDatetime.group(i) != null)
					mapReplaced.put(mDatetime.group(i), "__#" + ALPHABET[i]);
			}
		}
		// temp replace date time with symbol
		Iterator<Map.Entry<String, String>> iter = mapReplaced.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, String> entry = iter.next();
			message = message.replaceAll(entry.getKey(), entry.getValue());
		}
		
		message = message.replaceAll("[0-9]{1}", "*");
		message = message.replaceAll("[*,.]{2,20}\\*", "***");
		
		// change back symbol to original date time
		iter = mapReplaced.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, String> entry = iter.next();
			message = message.replaceAll(entry.getValue(), entry.getKey());
		}
		
		return message;
	}
}
