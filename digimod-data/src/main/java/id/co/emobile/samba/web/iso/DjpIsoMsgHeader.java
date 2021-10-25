package id.co.emobile.samba.web.iso;

public interface DjpIsoMsgHeader {
	public static final String UNKNOWN_MSG_TYPE = "0000";
	public static final String AUTHORIZATION_REQUEST = "0100";
	public static final String AUTHORIZATION_RESPONSE = "0110";
	public static final String FINANCIAL_TRANSACTION_REQUEST = "0200";
	public static final String FINANCIAL_TRANSACTION_RESPONSE = "0210";
	public static final String ACQUIRER_REVERSAL_REQUEST = "0400";
	public static final String ACQUIRER_REVERSAL_RESPONSE = "0410";
	public static final String ACQUIRER_REVERSAL_ADVICE = "0420";
	public static final String ACQUIRER_REVERSAL_ADVICE_SAMSAT = "0421";
	public static final String NETWORK_MANAGEMENT_REQUEST = "0800";
	public static final String NETWORK_MANAGEMENT_RESPONSE = "0810";
	
	public int getLength();
	
	public int parseHeader(String rawMessage);
		
	public String getMsgType();
}
