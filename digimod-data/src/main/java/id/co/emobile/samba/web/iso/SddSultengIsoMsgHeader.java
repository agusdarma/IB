package id.co.emobile.samba.web.iso;

public class SddSultengIsoMsgHeader implements SddIsoMsgHeader {
	private String msgType;

	private int length = 4;
	
	public SddSultengIsoMsgHeader() {
	}
	
	public SddSultengIsoMsgHeader(String msgType) {
		this.msgType = msgType;
	}
	
	public int getLength() {
		return length;
	}

	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	
	public String toString() {
		return msgType;
	}

	public int parseHeader(String rawMessage) {
		msgType = rawMessage.substring(0, length);
		return length;
	}
}
