package id.co.emobile.samba.web.service;

public class JetsException extends Exception{
	/**
	 * generated
	 */
	private static final long serialVersionUID = 1L;

	private String resultCode;
	
	public JetsException(String resultCode) {
		this.resultCode = resultCode;
	}
	
	public JetsException(String msg, String resultCode) {
		super(msg);
		this.resultCode =  resultCode;
	}
	
	public String getResultCode() {
		return resultCode;
	}
}
