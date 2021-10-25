package id.co.emobile.samba.web.data;

import java.util.Date;

public class SourceAccountVO {
	private int id; // id INT AUTO_INCREMENT NOT NULL,
	private String phoneNo;  // srac_data VARCHAR(1000) NOT NULL,
	private String sracNumber;
	private String sracName;
	private String sracPin;
	private int groupId;
	private int sracStatus;
	private String sracKey;  // srac_key VARCHAR(50) NOT NULL,
	private Date createdOn;  // created_on DATETIME NOT NULL,
	private int createdBy;  // created_by INT NOT NULL,
	private Date updatedOn;  // updated_on DATETIME NOT NULL,
	private int updatedBy; // updated_by INT NOT NULL,
	
	private double sracBalance;
	private long lastChecked;
	private String balanceRc;
	
	// for display
	private int rowNum;
	private String sracStatusDisplay;
	private String groupNameDisplay;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
	public String getSracPin() {
		return sracPin;
	}
	public void setSracPin(String sracPin) {
		this.sracPin = sracPin;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public int getSracStatus() {
		return sracStatus;
	}
	public void setSracStatus(int sracStatus) {
		this.sracStatus = sracStatus;
	}
	public String getSracKey() {
		return sracKey;
	}
	public void setSracKey(String sracKey) {
		this.sracKey = sracKey;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public Date getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	public int getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	public double getSracBalance() {
		return sracBalance;
	}
	public void setSracBalance(double sracBalance) {
		this.sracBalance = sracBalance;
	}
	public long getLastChecked() {
		return lastChecked;
	}
	public void setLastChecked(long lastChecked) {
		this.lastChecked = lastChecked;
	}
	public String getBalanceRc() {
		return balanceRc;
	}
	public void setBalanceRc(String balanceRc) {
		this.balanceRc = balanceRc;
	}
	
	public int getRowNum() {
		return rowNum;
	}
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
	public String getSracStatusDisplay() {
		return sracStatusDisplay;
	}
	public void setSracStatusDisplay(String sracStatusDisplay) {
		this.sracStatusDisplay = sracStatusDisplay;
	}
	public String getGroupNameDisplay() {
		return groupNameDisplay;
	}
	public void setGroupNameDisplay(String groupNameDisplay) {
		this.groupNameDisplay = groupNameDisplay;
	}
	
	@Override
	public String toString() {
		return "SourceAccountVO@" + Integer.toHexString(hashCode())
			+ "[id=" + id 
			+ ", phoneNo=" + phoneNo 
			+ ", sracNumber=" + sracNumber 
			+ ", sracName=" + sracName 
			+ ", groupId=" + groupId
			+ ", sracPin=******" //+ sracPin 
			+ ", sracStatus=" + sracStatus 
			+ ", sracKey=" + sracKey
			+ ", sracBalance=" + sracBalance
			+ ", lastChecked=" + lastChecked
			+ ", balanceRc=" + balanceRc
			+ ", createdOn=" + createdOn 
			+ ", createdBy=" + createdBy 
			+ ", updatedOn=" + updatedOn 
			+ ", updatedBy=" + updatedBy 
			+ ", rowNum=" + rowNum 
			+ ", sracStatusDisplay=" + sracStatusDisplay 
			+ ", groupNameDisplay=" + groupNameDisplay 
			+ "]";
	}
	
	
	
}
