package id.co.emobile.samba.web.data;

public class WebResultVO {

	private int rc; //0 success sisanya reject
	private String message;
	private int type; //0 insert sisanya update
	private String path;
	private String json1;

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getRc() {
		return rc;
	}
	public void setRc(int rc) {
		this.rc = rc;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getJson1() {
		return json1;
	}
	public void setJson1(String json1) {
		this.json1 = json1;
	}
}
