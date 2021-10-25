package id.co.emobile.samba.web.data;


public class AppsTrxTypeVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private int id;	
	private String trxName;
	private String trxGroup;
	private String activityType;
	private String trxNameR;
	private String trxGroupR;
	private String activityTypeR;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTrxName() {
		return trxName;
	}
	public void setTrxName(String trxName) {
		this.trxName = trxName;
	}
	public String getTrxGroup() {
		return trxGroup;
	}
	public void setTrxGroup(String trxGroup) {
		this.trxGroup = trxGroup;
	}
	public String getActivityType() {
		return activityType;
	}
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
	public String getTrxNameR() {
		return trxNameR;
	}
	public void setTrxNameR(String trxNameR) {
		this.trxNameR = trxNameR;
	}
	public String getTrxGroupR() {
		return trxGroupR;
	}
	public void setTrxGroupR(String trxGroupR) {
		this.trxGroupR = trxGroupR;
	}
	public String getActivityTypeR() {
		return activityTypeR;
	}
	public void setActivityTypeR(String activityTypeR) {
		this.activityTypeR = activityTypeR;
	}
}
