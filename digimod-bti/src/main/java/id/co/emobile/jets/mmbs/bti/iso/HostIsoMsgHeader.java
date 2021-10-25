package id.co.emobile.jets.mmbs.bti.iso;

public class HostIsoMsgHeader implements IsoMsgHeader {
	private String msgType;

	private int length = 4;
	
	public HostIsoMsgHeader() {
	}
	
	public HostIsoMsgHeader(String msgType) {
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
