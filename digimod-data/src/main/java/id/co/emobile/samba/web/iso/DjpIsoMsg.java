package id.co.emobile.samba.web.iso;

import java.util.BitSet;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import id.co.emobile.samba.web.utils.CommonUtil;

public abstract class DjpIsoMsg {
	private static final Logger LOG = LoggerFactory.getLogger(DjpIsoMsg.class);

	protected static final int BITMAP_LENGTH 			= 16;

	public static final int PAN 						= 2;

	public static final int PROCESSING_CODE 			= 3;

	public static final int TRANSACTION_AMT 			= 4;

	public static final int SETTLEMENT_AMT 				= 5;

	public static final int TIMESTAMP 					= 7;

	public static final int SYS_TRACE_NO 				= 11;

	public static final int TIME_LOCAL 					= 12;

	public static final int DATE_LOCAL 					= 13;

	public static final int EXPIRATION_DATE 			= 14;

	public static final int SETTLEMENT_DATE 			= 15;

	public static final int MERCHANT_TYPE 				= 18;
	
	public static final int POINT_SERVICE_ENTRY 		= 22;

	public static final int NETWORK_INTL_ID 			= 24;
	
	public static final int POINT_SERVICE_CONDITION 	= 25;

	public static final int AUTH_ID_RSP_LEN 			= 27;

	public static final int ACQUIRING_INSTITUTION_ID 	= 32;

	public static final int FORWARDING_INSTITUTION_ID 	= 33;
	
	public static final int TRACK_2_DATA				= 35;

	public static final int RETRIEVAL_REF_NO 			= 37;

	public static final int AUTH_ID 					= 38;

	public static final int RESPONSE_CODE 				= 39;

	public static final int TERMINAL_ID 				= 41;

	public static final int CARD_ACCEPTOR_ID 			= 42;

	public static final int CARD_ACCEPTOR_NAME_LOCATION = 43;

	public static final int DATA_PRIVATE 				= 48;

	public static final int CURR_CODE 					= 49;

	public static final int PIN_BLOCK 					= 52;

	public static final int ADDITIONAL_AMOUNT 			= 54;

	public static final int ADVICE_REASON_CODE 			= 60;

	public static final int ADDITIONAL_DATA 			= 61;

	public static final int ADDITIONAL_DATA_2 			= 62;

	public static final int ADDITIONAL_DATA_4 			= 63;

	public static final int INSTITUTION_COUNTRY_CODE 	= 68;

	public static final int NETWORK_INFO_CODE 			= 70;

	public static final int ORIGINAL_DATA_ELEMENTS 		= 90;
	
	public static final int RESPONSE_INDICATOR 			= 93;
	
	public static final int REPLACEMENT_AMOUNTS 		= 95;
	
	public static final int RECEIVING_INSTITUTION_ID 	= 100;

	public static final int ACCOUNT_ID_1 				= 102;

	public static final int ACCOUNT_ID_2 				= 103;

	public static final int PRIVATE_DATA_1 				= 120;
	
	public static final int INFORMATION_DATA 			= 124;
	
	public static final int INFORMATION_DATA_2 			= 125;

	public static final int ADDITIONAL_DATA_3 			= 126;
	
	public static final int ADDITIONAL_DATA_6 			= 127;

	private DjpIsoMsgHeader header;

	private String rawMessage;

	protected BitSet primaryBitmap;

	protected BitSet secondaryBitmap;

	private int readPosition;

	protected abstract String getPrimaryBitmapLength(int index);

	protected abstract String getSecondaryBitmapLength(int index);

	protected static DjpIsoMsg createISOMsg(DjpIsoMsgHeader header) {
		// THIS FUNCTION HAS TO BE OVERRIDE !!!
		return null;
	}

	protected SortedMap<Integer, String> items;

	public DjpIsoMsg() {
		readPosition = 0;
		primaryBitmap = new BitSet();
		secondaryBitmap = new BitSet();
		items = new TreeMap<Integer, String>();
	}

	public DjpIsoMsg(DjpIsoMsgHeader header) {
		this.header = header;
		readPosition = 0;
		primaryBitmap = new BitSet();
		secondaryBitmap = new BitSet();
		items = new TreeMap<Integer, String>();
	}

	public DjpIsoMsg parse(String rawMessage) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("parse message: <" + rawMessage + ">");
			}
			this.rawMessage = rawMessage;
			readPosition = header.parseHeader(rawMessage);
			doParse(rawMessage);
		} catch (Exception e) {
			LOG.error("Unable to parse message <" + rawMessage + ">: " + e);
		}
		return this;
	}

	public String getMsgType() {
		return header.getMsgType();
	}

	public void setItem(int key, String value) {
		items.put(key, value);
		updateBitmap(key);
	}

	public void setItemWithLength(int key, String value, int lenDigit)
			throws Exception {
		setItem(key, CommonUtil.zeroPadLeft(String.valueOf(value.length()), lenDigit)
				+ value);
	}
	
	public void setItemWithLengthNew(int key, String value, int lenDigit)
			throws Exception {
		setItem(key, value);
	}

	public void setItemWithPad(int key, String value, int len) {
		setItem(key, CommonUtil.padRight(value, len));
	}

	public String getItem(int key) {
		String bitmapLength = getBitmapLength(key);
		if (bitmapLength.equals("L") || bitmapLength.equals("LL")
				|| bitmapLength.equals("LLL")) {
			String value = items.get(key);
			if (StringUtils.isEmpty(value))
				return value;
			else
				return value.substring(bitmapLength.length());
		} else {
			if (Integer.parseInt(bitmapLength) == items.get(key).length()) {
				return items.get(key);
			} else {
				LOG.warn("Value length {} not equal with Element length {}.", 
						items.get(key).length(), bitmapLength);
				return items.get(key);
			}
		}
	}

	protected String doParse(String rawMessage) throws Exception {
		readBitmaps();
		// start from 2, index 63 to indicate secondary bitmap
		// and has been read in readBitmaps
		for (int i = 2; i <= primaryBitmap.length(); i++) {
			if (primaryBitmap.get(64 - i)) {
				String value = readData(getPrimaryBitmapLength(i));
				setIsoItem(i, value);
				//items.put(i, readData(getPrimaryBitmapLength(i)));
			}
		}
		// secondary bitmap
		// for (int i=1; i<=secondaryBitmap.length(); i++) {
		// if (secondaryBitmap.get(64-i)) {
		// items.put(i + 64, readData(getSecondaryBitmapLength(i)));
		// }
		// }
		// int st = 64 - primaryBitmap.length() + 1;
		// for (int i=st; i <= 64; i++) {
		// if (primaryBitmap.get(64-i)) {
		// items.put(i, readData(getPrimaryBitmapLength(i)));
		// }
		// }
		// TODO to be reviewed
		int start = 128 - secondaryBitmap.length() + 1;
		for (int i = start; i <= 128; i++) {
			if (secondaryBitmap.get(128 - i)) {
				String value = readData(getSecondaryBitmapLength(i - 64));
				setIsoItem(i, value);
				//items.put(i, readData(getSecondaryBitmapLength(i - 64)));
			}
		}

		return null;
	}

	protected void updateBitmap(int bitSequence) {
		if (bitSequence < 65) {
			bitSequence = 64 - bitSequence;
			primaryBitmap.set(bitSequence);
		} else {
			bitSequence = 128 - bitSequence;
			secondaryBitmap.set(bitSequence);
		}
	}

	/*
	 * WARNING! This function will not return properly if the object was build
	 * from rawMessage
	 */
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(getHeader());
		result.append(getBitmapString().toUpperCase());
		for (String value : items.values()) {
			result.append(value);
		}
		return result.toString();
	}

	/*
	 * function is similar with toString, but bit 52 (pin) is masked to xxxxxx
	 */
	public String printIso() {
		StringBuilder result = new StringBuilder();
		result.append(getHeader());
		result.append(getBitmapString().toUpperCase());
		Iterator<Integer> iter = items.keySet().iterator();
		while (iter.hasNext()) {
			Integer key = iter.next();
			if (key == PIN_BLOCK) {
				int pinLength = items.get(key).trim().length();
				String maskedPin = CommonUtil.repeat("x", pinLength);
				// based on ISO spec, bit 52 will always have 16 byte length
				result.append(CommonUtil.padRight(maskedPin, 16));
			} else {
				result.append(items.get(key));
			}
		}
		return result.toString();
	}

	public void setHeader(DjpIsoMsgHeader header) {
		this.header = header;
	}

	public String getHeader() {
		return header.toString();
	}

	protected String getBitmapString() {
		if (secondaryBitmap.length() > 0) {
			updateBitmap(1);
			return CommonUtil.toHexString(primaryBitmap)
					+ CommonUtil.toHexString(secondaryBitmap);
		}
		return CommonUtil.toHexString(primaryBitmap);
	}

	protected String readData(String len) {
		if (len.startsWith("LLL")) {
			len = readData(3);
		} else if (len.startsWith("LL")) {
			len = readData(2);
		} else if (len.startsWith("L")) {
			len = readData(1);
		}
		int dataLen = Integer.parseInt(len);
		return readData(dataLen);
	}

	private String readData(int len) {
		int lastPosition = readPosition + len;
		String result = rawMessage.substring(readPosition, lastPosition);
		readPosition = lastPosition;
		return result;
	}

	public int getReadPosition() {
		return readPosition;
	}

	public void setReadPosition(int readPosition) {
		this.readPosition = readPosition;
	}

	protected void readBitmaps() {
		primaryBitmap = CommonUtil.toBitSet(readData(BITMAP_LENGTH));
		if (primaryBitmap.get(64 - 1)) {
			secondaryBitmap = CommonUtil.toBitSet(readData(BITMAP_LENGTH));
		}
	}

	protected String getBitmapLength(int index) {
		if (index <= 64) {
			return getPrimaryBitmapLength(index);
		} else {
			return getSecondaryBitmapLength(index - 64);
		}
	}

	public void setIsoItem(int key, String value) throws Exception {
		String bitmapLength = getBitmapLength(key);
		if (bitmapLength.equals("L") || bitmapLength.equals("LL")
				|| bitmapLength.equals("LLL")) {
			setItemWithLength(key, value, bitmapLength.length());
		} else {
			if (Integer.parseInt(bitmapLength) == value.length()) {
				setItem(key, value);
			} else {
				throw new Exception(
						"Value length not equal with Element length.");
			}
		}
	}

	public void reuseIsoElements() throws Exception {
		for (int i = 2; i <= primaryBitmap.length(); i++) {
			if (primaryBitmap.get(64 - i)) {
				String bitmapLength = getBitmapLength(i);
				String value = items.get(i);
				if (bitmapLength.equals("L") || bitmapLength.equals("LL")
						|| bitmapLength.equals("LLL")) {
					items.put(i, value.substring(bitmapLength.length()));
				}
			}
		}
		int start = 128 - secondaryBitmap.length() + 1;
		for (int i = start; i <= 128; i++) {
			if (secondaryBitmap.get(128 - i)) {
				String bitmapLength = getBitmapLength(i);
				String value = items.get(i);
				if (bitmapLength.equals("L") || bitmapLength.equals("LL")
						|| bitmapLength.equals("LLL")) {
					items.put(i, value.substring(bitmapLength.length()));
				}
			}
		}
	}

	public void setRawMessage(String rawMessage) {
		this.rawMessage = rawMessage;
	}

	public String getRawMessage() {
		return rawMessage;
	}

}
