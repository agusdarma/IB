package id.co.emobile.samba.web.data;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class MmbsResponseVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private String hostRc;
	private String hostRef;
	private String hostMessage;
	private String mmbsRef;
	public String getHostRc() {
		return hostRc;
	}
	public void setHostRc(String hostRc) {
		this.hostRc = hostRc;
	}
	public String getHostRef() {
		return hostRef;
	}
	public void setHostRef(String hostRef) {
		this.hostRef = hostRef;
	}
	public String getHostMessage() {
		return hostMessage;
	}
	public void setHostMessage(String hostMessage) {
		this.hostMessage = hostMessage;
	}
	public String getMmbsRef() {
		return mmbsRef;
	}
	public void setMmbsRef(String mmbsRef) {
		this.mmbsRef = mmbsRef;
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	
}
