package id.co.emobile.samba.web.iso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import id.co.emobile.samba.web.utils.CommonUtil;

public class IsoUtil {
	private static final Logger LOG = LoggerFactory.getLogger(IsoUtil.class);

	private static long multiplier(int len) {
		long result = 1;
		for (int i = 1; i < len; i++) {
			result = result * 10;
		}
		return result;
	}
	
	/*
	 * convert from amount to isoAmount (last 2 digit is the decimal) eg:
	 * 14500.20 --> 1450020
	 */
	public static String getISOAmount(double amount, int len) throws Exception {
		try {
			amount = amount * 100;
			return CommonUtil.zeroPadLeft(String.format("%.0f", amount), len);
		}
		catch (Exception e) {
			LOG.error("Exception in getISOAmount: " + amount);
			throw e;
		}
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
	
	/*
	 * convert from amount to isoAmount (last 2 digit is the decimal) eg:
	 * 14500.20 --> 1450020
	 */
	public static String getISOAmount(String amount) throws Exception {
		try {
			double isoAmount = Double.valueOf(amount).doubleValue();
			return getISOAmount(isoAmount);
		}
		catch (Exception e) {
			LOG.error("Unable to convert amount to isoAmount: " + amount);
			throw e;
		}
	}
	
	/*
	 * convert from amount to isoAmount (last 2 digit is the decimal) eg:
	 * 14500.20 --> 1450020
	 */
	public static String getISOAmount(double amount) throws Exception {
		try {
			amount = amount * 100;
			return CommonUtil.zeroPadLeft(String.format("%.0f", amount), 17);
		}
		catch (Exception e) {
			LOG.error("Exception in getISOAmount: " + amount);
			throw e;
		}
	}
}
