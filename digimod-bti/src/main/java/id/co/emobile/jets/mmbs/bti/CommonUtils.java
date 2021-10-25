package id.co.emobile.jets.mmbs.bti;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import id.co.emobile.samba.web.utils.CommonUtil;

public class CommonUtils {
	private static final Logger LOG = LoggerFactory.getLogger(CommonUtils.class);

	public static final char PAD_LEFT = 'L';

	public static final char PAD_RIGHT = 'R';

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
	 * Added right pad to a string with a given char e.g: padright("foo", 5, ' ')
	 * --> " foo"
	 */
	public static String padright(String s, int len, char c) {
		StringBuffer d = new StringBuffer(s);
		while (d.length() < len)
			d.append(c);
		return d.toString();
	}

	/*
	 * Added right or left pad to a string with a given char e.g:
	 * padright("foo", 7, ' ', right) --> "foo "
	 */
	public static String strpad(String s, int len, char c, char t) {
		String result = s;
		try {
			if (t == 'L') {
				result = CommonUtil.padLeft(s, c, len);
			}
			else if (t == 'R') {
				result = padright(s, len, c);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/*
	 * Same with getBalance
	 */
	public static String getAmount(String bit62) {
		// iso amount in cents
		try {
			String isoAmount = bit62.substring(0, 17);
			String flag = bit62.substring(17, 18);

			double amount = Double.valueOf(isoAmount).doubleValue();
			amount = amount / 100;
			if (flag.equals("D"))
				amount = amount * -1;
			return formatAmount(amount);
		}
		catch (Exception e) {
			LOG.error("Unable to get amount from iso: " + bit62);
		}
		return "0";
	}

	/*
	 * Bit62 account balance: YYMMDD(6)+Rate(7)+Amount1(17)+D/C(1)+Amount2(17)+D/C(1)
	 */
	public static String getBit62Balance(String bit62) {
		// iso amount in cents
		try {
//			if (bit62.length() != 31) {
//				logger.error("Invalid ISO amount: " + bit62);
//				return "0";
//			}
			if (bit62.length() < 31) {
				LOG.error("Invalid ISO amount: " + bit62);
				return "0";
			}
			String bit62Amount = bit62.substring(13, 31);
			return getBalance(bit62Amount);
		}
		catch (Exception e) {
			LOG.error("Unable to get amount from iso: " + bit62);
		}
		return "0";
	}
	
	public static String getBalance(String amtAndFlag) throws Exception {
		if (amtAndFlag == null ||amtAndFlag.length() < 13) {
			throw new Exception("Not valid balance string" + amtAndFlag);
		}
		String isoAmount = amtAndFlag.substring(1);
		String flag = amtAndFlag.substring(0, 1);
		double amount = Double.valueOf(isoAmount).doubleValue();
		amount = amount / 100;
		if (flag.equals("D"))
			amount = amount * -1;
		return formatAmount(amount);
	}
	
	/*
	 * convert 00000000002500050 --> 25000.50
	 */
	public static String getFormattedAmount(String isoAmount) {
		double amount = Double.valueOf(isoAmount).doubleValue();
		amount = amount / 100;
		return formatAmount(amount);
	}

	/*
	 * convert to Indonesian currency: 00000000002500050 --> 25.000,50
	 */
	public static String getFormattedAmountIndo(String isoAmount, int digits) {
		double amount = Double.valueOf(isoAmount).doubleValue();
		amount = amount / 100;
		return CommonUtil.formatMoneyIndo(amount, digits);
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

	/*
	 * convert from amount to Rate Amount (13 digit, last 7 digit is the
	 * decimal) eg: 9750.25 --> 0097502500000
	 */
	public static String getISORateAmount(String amount) throws Exception {
		try {
			double isoAmount = Double.valueOf(amount).doubleValue();
			return getISORateAmount(isoAmount);
		}
		catch (Exception e) {
			LOG.error("Unable to convert amount to isoAmount: " + amount);
			throw e;
		}
	}

	/*
	 * convert from amount to Rate Amount (13 digit, last 7 digit is the
	 * decimal) eg: 9750.25 --> 0097502500000
	 */
	public static String getISORateAmount(double amount) throws Exception {
		try {
			return CommonUtils.formatCurrency(amount, 6, 7);
		}
		catch (Exception e) {
			LOG.error("Exception in getISORateAmount: " + e);
			throw e;
		}
	}
	
	public static String formatAmount(double amount) {
		return String.format("%.2f", amount);
	}

}
