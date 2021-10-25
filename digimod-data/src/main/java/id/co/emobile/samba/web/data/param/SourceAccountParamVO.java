package id.co.emobile.samba.web.data.param;

public class SourceAccountParamVO extends ParamPagingVO {
	private static final long serialVersionUID = 1L; 
	
	@Override
	protected String getPrimaryKey() {
		return "id";
	}
	
	private String phoneNo;
	private String sracNumber;
	private String sracName;
	
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getSracNumber() {
		return sracNumber;
	}
	public void setSracNumber(String sracNumber) {
		this.sracNumber = sracNumber;
	}
	public String getSracName() {
		return sracName;
	}
	public void setSracName(String sracName) {
		this.sracName = sracName;
	}

}
