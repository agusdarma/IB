package id.co.emobile.samba.web.data;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class CheckAccountVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private String rcDisplay;
	private String rc; 
	private String accountNo;
	private String accountName;
	private String fullName;
	private int accountStatus;
	private String accountType;
	public String getRc() {
		return rc;
	}
	public void setRc(String rc) {
		this.rc = rc;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public int getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(int accountStatus) {
		this.accountStatus = accountStatus;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getAccountStatusDisplay() {
		String s = WebConstants.MAP_ACC_STATUS.get(accountStatus);
		if (StringUtils.isEmpty(s)) return "N/A";
		else return s;
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	public String getRcDisplay() {
		return rcDisplay;
	}
	public void setRcDisplay(String rcDisplay) {
		this.rcDisplay = rcDisplay;
	}
	
}
