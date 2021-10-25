package id.co.emobile.samba.web.entity;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class UserActivity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id; // id INT AUTO_INCREMENT NOT NULL,
	private int userDataId; // user_data_id int NOT NULL,
	private String userCode; // user_code VARCHAR(16) NOT NULL,
	private String userName; // user_name VARCHAR(32) NOT NULL,
	private String action; // action Varchar(50) NULL,
	private String moduleName; // module_name Varchar(50) NULL,
	private String changedAttribute; // changed_attribute Text NULL,
	private String description; //description Varchar(200) NULL,
	private String targetTable;  // target_table Varchar(50) NULL,
	private String targetId;  // target_id Varchar(50) NULL,
	private String ipAddress; // ip_address Varchar(50) DEFAULT '0.0.0.0',
	private Date createdOn;  //created_on Datetime NOT NULL,
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserDataId() {
		return userDataId;
	}
	public void setUserDataId(int userDataId) {
		this.userDataId = userDataId;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getChangedAttribute() {
		return changedAttribute;
	}
	public void setChangedAttribute(String changedAttribute) {
		this.changedAttribute = changedAttribute;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTargetTable() {
		return targetTable;
	}
	public void setTargetTable(String targetTable) {
		this.targetTable = targetTable;
	}
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	
}
