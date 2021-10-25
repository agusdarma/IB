package id.co.emobile.samba.web.entity;

import java.util.Date;


public class AppsTrxType implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	  
	// trx_name: e.g.: Balance Inq, Acc Statement
	private int id;
	private String trxName;
	private int trxGroup;
	private int createdBy;
	private Date createdOn;
	private int updatedBy;
	private Date updatedOn;
	private String activityType;
	private String createdByDisplay;
	private String updatedByDisplay;
	private String trxGroupDisplay;
	private int versionStatus;
	private int rowNum;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public int getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	public String getCreatedByDisplay() {
		return createdByDisplay;
	}
	public void setCreatedByDisplay(String createdByDisplay) {
		this.createdByDisplay = createdByDisplay;
	}
	public String getUpdatedByDisplay() {
		return updatedByDisplay;
	}
	public void setUpdatedByDisplay(String updatedByDisplay) {
		this.updatedByDisplay = updatedByDisplay;
	}
	public String getTrxName() {
		return trxName;
	}
	public void setTrxName(String trxName) {
		this.trxName = trxName;
	}
	public int getTrxGroup() {
		return trxGroup;
	}
	public void setTrxGroup(int trxGroup) {
		this.trxGroup = trxGroup;
	}
	public String getTrxGroupDisplay() {
		return trxGroupDisplay;
	}
	public void setTrxGroupDisplay(String trxGroupDisplay) {
		this.trxGroupDisplay = trxGroupDisplay;
	}
	public int getVersionStatus() {
		return versionStatus;
	}
	public void setVersionStatus(int versionStatus) {
		this.versionStatus = versionStatus;
	}
	public String getActivityType() {
		return activityType;
	}
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
	public int getRowNum() {
		return rowNum;
	}
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
}