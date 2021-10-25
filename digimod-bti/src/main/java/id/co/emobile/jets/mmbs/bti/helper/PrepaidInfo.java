package id.co.emobile.jets.mmbs.bti.helper;

import java.util.StringTokenizer;

public class PrepaidInfo {

	private String processCode;
	
	private String utilCode; 

	private boolean formatted;
	
	private String billVoucher;
	
	private boolean indosatData;
	
	private String cardName;
	

	public PrepaidInfo(String info) {
		parse(info);
	}
	
	private void parse(String info) {
		StringTokenizer st = new StringTokenizer(info, ";");
		processCode = st.nextToken();
		utilCode = st.nextToken();
		formatted = Boolean.valueOf(st.nextToken());
		billVoucher = st.nextToken();
		indosatData = Boolean.valueOf(st.nextToken());
		cardName = st.nextToken();
	}

	public String getProcessCode() {
		return processCode;
	}

	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}

	public String getUtilCode() {
		return utilCode;
	}

	public void setUtilCode(String utilCode) {
		this.utilCode = utilCode;
	}

	public boolean isFormatted() {
		return formatted;
	}

	public void setFormatted(boolean formatted) {
		this.formatted = formatted;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public boolean isIndosatData() {
		return indosatData;
	}

	public void setIndosatData(boolean indosatData) {
		this.indosatData = indosatData;
	}

	public String getBillVoucher() {
		return billVoucher;
	}

	public void setBillVoucher(String billVoucher) {
		this.billVoucher = billVoucher;
	}

}
