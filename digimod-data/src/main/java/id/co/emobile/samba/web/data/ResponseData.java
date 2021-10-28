package id.co.emobile.samba.web.data;

import org.apache.commons.lang3.StringUtils;

import id.co.emobile.samba.web.utils.CommonUtil;

public class ResponseData implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private int resultCode;
	private String msgToUser;
	private String sysMessage;
	
	
	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	public String getMsgToUser() {
		return msgToUser;
	}
	public void setMsgToUser(String msgToUser) {
		this.msgToUser = msgToUser;
	}
	public String getSysMessage() {
		return sysMessage;
	}
	public void setSysMessage(String sysMessage) {
		if (StringUtils.isEmpty(sysMessage)) {
			this.sysMessage = "";
		} else {
			if (sysMessage.length() > 255)
				this.sysMessage = sysMessage.substring(0,253) + "..";
			else
				this.sysMessage = sysMessage;
		}  // end if 
	}

	@Override
	public String toString() {
		return "ResponseData@" + Integer.toHexString(hashCode()) 
				+ "[resultCode=" + resultCode 
				+ ", msgToUser=" + CommonUtil.maskNumberForMessage(msgToUser)
				+ ", sysMessage=" + sysMessage 				
				+ "]";
	}
	
}
