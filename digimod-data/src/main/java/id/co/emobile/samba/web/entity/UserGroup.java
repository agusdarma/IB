package id.co.emobile.samba.web.entity;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class UserGroup implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;  // level_id INT AUTO_INCREMENT NOT NULL,
	private String groupName;  //  level_name VARCHAR(32) NOT NULL,
	private String groupDesc;  //  level_desc VARCHAR(255) NOT NULL,
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
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName == null? null: groupName .trim();
	}
	public String getGroupDesc() {
		return groupDesc;
	}
	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc == null? null: groupDesc.trim();
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
}
