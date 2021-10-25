package id.co.emobile.jets.mmbs.bti.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MandiriPrepaidBit126 {
	private static final Logger LOG = LoggerFactory.getLogger(MandiriPrepaidBit126.class);

	private static final int TOKENHEADER = 0;

	private static final int UTILCODE = 1;

	private static final int BILLPHONENO = 2;

	private static final int BILLVOUCHER = 3;

	private static final int BILLCUSTNO = 4;

	private static final int BILLCUSTNAME = 5;

	private static final int NPWP = 6;

	private static final int KANDATELNO = 7;

	private static final int BILLSTATUSCODE = 8;

	private static final int BILLREFNUM1 = 9;

	private static final int BILLREFNUM2 = 10;

	private static final int BILLREFNUM3 = 11;

	private static final int BILLAMOUNT1 = 12;

	private static final int BILLAMOUNT2 = 13;

	private static final int BILLAMOUNT3 = 14;

	private static final int INDOSATDATA = 15;

	private String[] data = new String[16];

	public void parse(String bit126) throws Exception {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Parse prepaid bit126: " + bit126);
			}
			int lastPos = 0;
			assign(bit126, TOKENHEADER, lastPos, 22);
			assign(bit126, UTILCODE, lastPos, 4);
			assign(bit126, BILLPHONENO, lastPos, 19);
			assign(bit126, BILLVOUCHER, lastPos, 18);
			assign(bit126, BILLCUSTNO, lastPos, 11);
			assign(bit126, BILLCUSTNAME, lastPos, 25);
			assign(bit126, NPWP, lastPos, 15);
			assign(bit126, KANDATELNO, lastPos, 4);
			assign(bit126, BILLSTATUSCODE, lastPos, 1);
			assign(bit126, BILLREFNUM1, lastPos, 11);
			assign(bit126, BILLREFNUM2, lastPos, 11);
			assign(bit126, BILLREFNUM3, lastPos, 11);
			assign(bit126, BILLAMOUNT1, lastPos, 12);
			assign(bit126, BILLAMOUNT2, lastPos, 12);
			assign(bit126, BILLAMOUNT3, lastPos, 12);
			// no need to parse indosat data, it's empty
		}
		catch (Exception e) {
			LOG.error("Error parsing prepaid bit126: " + e);
			throw e;
		}
	}

	/*
	 * return last position
	 */
	private int assign(String bit126, int index, int startPos, int length) {
		int lastPos = startPos + length;
		data[index] = bit126.substring(startPos, lastPos);
		return lastPos;
	}

	public String toBit126Str() {
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < data.length; i++) {
			if (data[i] != null) {
				sb.append(data[i]);
			}
		}
		return buildTokenHeader(sb.toString().length()) + sb.toString();
	}

	private String buildTokenHeader(int length) {
		StringBuilder sb = new StringBuilder("& 0000200");
		sb.append(length + 22).append("! Q100").append(length).append(" ");
		return sb.toString();
	}

	public String getTokenHeader() {
		return data[TOKENHEADER];
	}

	public String getUtilCode() {
		return data[UTILCODE];
	}

	public void setUtilCode(String utilCode) {
		data[UTILCODE] = utilCode;
	}

	public String getBillPhoneNo() {
		return data[BILLPHONENO];
	}

	public void setBillPhoneNo(String billPhoneNo) {
		data[BILLPHONENO] = billPhoneNo;
	}

	public String getBillVoucher() {
		return data[BILLVOUCHER];
	}

	public void setBillVoucher(String billVoucher) {
		data[BILLVOUCHER] = billVoucher;
	}

	public String getBillCustNo() {
		return data[BILLCUSTNO];
	}

	public void setBillCustNo(String billCustNo) {
		data[BILLCUSTNO] = billCustNo;
	}

	public String getBillCustName() {
		return data[BILLCUSTNAME];
	}

	public void setBillCustName(String billCustName) {
		data[BILLCUSTNAME] = billCustName;
	}

	public String getNpwp() {
		return data[NPWP];
	}

	public void setNpwp(String npwp) {
		data[NPWP] = npwp;
	}

	public String getKandatelNo() {
		return data[KANDATELNO];
	}

	public void setKandatelNo(String kandatelNo) {
		data[KANDATELNO] = kandatelNo;
	}

	public String getBillStatusCode() {
		return data[BILLSTATUSCODE];
	}

	public void setBillStatusCode(String billStatusCode) {
		data[BILLSTATUSCODE] = billStatusCode;
	}

	public String getBillRefNum1() {
		return data[BILLREFNUM1];
	}

	public void setBillRefNum1(String billRefNum1) {
		data[BILLREFNUM1] = billRefNum1;
	}

	public String getBillRefNum2() {
		return data[BILLREFNUM2];
	}

	public void setBillRefNum2(String billRefNum2) {
		data[BILLREFNUM2] = billRefNum2;
	}

	public String getBillRefNum3() {
		return data[BILLREFNUM3];
	}

	public void setBillRefNum3(String billRefNum3) {
		data[BILLREFNUM3] = billRefNum3;
	}

	public String getBillAmount1() {
		return data[BILLAMOUNT1];
	}

	public void setBillAmount1(String billAmount1) {
		data[BILLAMOUNT1] = billAmount1;
	}

	public String getBillAmount2() {
		return data[BILLAMOUNT2];
	}

	public void setBillAmount2(String billAmount2) {
		data[BILLAMOUNT2] = billAmount2;
	}

	public String getBillAmount3() {
		return data[BILLAMOUNT3];
	}

	public void setBillAmount3(String billAmount3) {
		data[BILLAMOUNT3] = billAmount3;
	}

	public String getIndosatData() {
		return data[INDOSATDATA];
	}

	public void setIndosatData(String indosatData) {
		data[INDOSATDATA] = indosatData;
	}
}
