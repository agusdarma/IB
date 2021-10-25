package id.co.emobile.samba.web.data;

import id.co.emobile.samba.web.utils.DateUtils;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class UserDataVO {
	
	private int pk; //alias U1PK
	private String userID; //alias U1UID
	private String password; //alias U1PSW
	private String confirmPassword;
	private String userName; //alias U1UNAM
	private int invalidAccessCount; //alias U1IACN
	private String jobTitle; //alias U1RANK
	private String remark; //alias U1REMK
	private String userLevel; //alias U1ULEV
	private int userStatus; //alias U1USTA
	private String userStatus2; //alias U1USTA dari MXGUIT
	private String statusDate; //alias U1SDAT 
	private String passwordChangedOn; //alias U1PWCH
	private String createOn; //U1ENDT
	private String createBy; //U1ENBY
	private String lastUpdatedOn; //U1UPDT
	private String lastUpdatedBy; //U1UPBY
	private String securityRemark; //U1SREM
	private String departmentCode; //U1DEP
	private String branchCode; //U1UBRA
	private String lastConnect; //U1LCON 
	private String lastAccessCount; //U1LACN
	private String authDate; //U1AUDT 
	private String authBy; //U1AUBY
	private Date dateLastConnect;
	private Date dateLastUpdatedOn;
	private Date dateStatusDate;
	
	
	public String getAuthDate() {
		return authDate;
	}
	public void setAuthDate(String authDate) {
		this.authDate = authDate;
	}
	public String getAuthBy() {
		return authBy;
	}
	public void setAuthBy(String authBy) {
		this.authBy = authBy;
	}
	public int getPk() {
		return pk;
	}
	public void setPk(int pk) {
		this.pk = pk;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public String getUserName() {
		return userName.trim();
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getInvalidAccessCount() {
		return invalidAccessCount;
	}
	public void setInvalidAccessCount(int invalidAccessCount) {
		this.invalidAccessCount = invalidAccessCount;
	}
	public String getJobTitle() {
		return jobTitle.trim();
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getRemark() {
		return remark.trim();
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}

	public String getStatusDate() {
		return statusDate;
	}
	public void setStatusDate(String statusDate) {
		this.statusDate = statusDate;
	}
	public String getPasswordChangedOn() {
		return passwordChangedOn;
	}
	public void setPasswordChangedOn(String passwordChangedOn) {
		this.passwordChangedOn = passwordChangedOn;
	}
	public String getCreateOn() {
		return createOn;
	}
	public void setCreateOn(String createOn) {
		this.createOn = createOn;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getLastUpdatedOn() {
		return lastUpdatedOn;
	}
	public void setLastUpdatedOn(String lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public String getSecurityRemark() {
		return securityRemark;
	}
	public void setSecurityRemark(String securityRemark) {
		this.securityRemark = securityRemark;
	}
	public String getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getLastConnect() {
		return lastConnect;
	}
	public void setLastConnect(String lastConnect) {
		this.lastConnect = lastConnect;
	}
	public String getLastAccessCount() {
		return lastAccessCount;
	}
	public void setLastAccessCount(String lastAccessCount) {
		this.lastAccessCount = lastAccessCount;
	}
	public Date getDateLastConnect() {
		if(!StringUtils.isEmpty(lastConnect)){
			dateLastConnect = DateUtils.StrToDateTime("yyyyMMddHHmmss", lastConnect);
		}
		else
		{
			dateLastConnect = new Date();
		}
		return dateLastConnect;
	}


	public Date getDateLastUpdatedOn() {
		if(!StringUtils.isEmpty(lastUpdatedOn)){
			dateLastUpdatedOn = DateUtils.StrToDateTime("yyyyMMddHHmmss", lastUpdatedOn);
		}
		else
		{
			dateLastUpdatedOn = new Date();
		}
		return dateLastUpdatedOn;
	}

	public String getUserStatus2() {
		return userStatus2;
	}
	public void setUserStatus2(String userStatus2) {
		this.userStatus2 = userStatus2;
	}
	public int getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}
	public void setDateStatusDate(Date dateStatusDate) {
		this.dateStatusDate = dateStatusDate;
	}
	public Date getDateStatusDate() {
		return dateStatusDate;
	}

	
}
