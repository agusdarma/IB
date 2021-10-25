package id.co.emobile.samba.web.data;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class NotificationMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String originator;
	
	private String msgLogNo;

	private String phoneNo;
	
	private String message;  // in plain ascii, NOT HEXA
	
	private String chargeCode;
	
	private String smiOutput;
	
	private String clientRef;
	
	private String trxCode;
	
	private String sysLogNo;
	
	private String resultCode;

	public NotificationMsg(String sysLogNo, String msgLogNo, String phoneNo, String message, String clientRef) {
			this.msgLogNo = msgLogNo;
			this.sysLogNo = sysLogNo;
		this.phoneNo = phoneNo;
		this.message = message;
	}
	
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public String getMsgLogNo() {
		return msgLogNo;
	}
	public void setMsgLogNo(String msgLogNo) {
		this.msgLogNo = msgLogNo;
	}
	
	public String getChargeCode() {
		return chargeCode;
	}
	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}

	public String getSmiOutput() {
		return smiOutput;
	}
	public void setSmiOutput(String smiOutput) {
		this.smiOutput = smiOutput;
	}

	public String getOriginator() {
		return originator;
	}
	public void setOriginator(String originator) {
		this.originator = originator;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public String getClientRef() {
		return clientRef;
	}

	public void setClientRef(String clientRef) {
		this.clientRef = clientRef;
	}

	public String getTrxCode() {
		return trxCode;
	}

	public void setTrxCode(String trxCode) {
		this.trxCode = trxCode;
	}

	public String getSysLogNo() {
		return sysLogNo;
	}

	public void setSysLogNo(String sysLogNo) {
		this.sysLogNo = sysLogNo;
	}
	
	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
}
