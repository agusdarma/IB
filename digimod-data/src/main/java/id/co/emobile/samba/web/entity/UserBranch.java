package id.co.emobile.samba.web.entity;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class UserBranch implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;  // level_id INT AUTO_INCREMENT NOT NULL,
	private String branchName;  //  level_name VARCHAR(32) NOT NULL,
	private String branchDesc;  //  level_desc VARCHAR(255) NOT NULL,
	private Date createdOn;  //  created_on DATETIME NOT NULL,
	private int createdBy;  //  created_by INT NOT NULL,
	private Date updatedOn;  //  updated_on DATETIME NOT NULL,
	private int updatedBy;  //  updated_by INT NOT NULL,
	
	private int rowNum;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	public int getRowNum() {
		return rowNum;
	}
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getBranchDesc() {
		return branchDesc;
	}
	public void setBranchDesc(String branchDesc) {
		this.branchDesc = branchDesc;
	}
}
