package id.co.emobile.samba.web.entity;

import java.util.Date;

public class SourceAccount implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private int id; // id INT AUTO_INCREMENT NOT NULL,
	private String phoneNo;
	private String sracData;  // srac_data VARCHAR(1000) NOT NULL,
	private String sracKey;  // srac_key VARCHAR(50) NOT NULL,
	private Date createdOn;  // created_on DATETIME NOT NULL,
	private int createdBy;  // created_by INT NOT NULL,
	private Date updatedOn;  // updated_on DATETIME NOT NULL,
	private int updatedBy; // updated_by INT NOT NULL,
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
	public String getSracData() {
		return sracData;
	}
	public void setSracData(String sracData) {
		this.sracData = sracData;
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
	@Override
	public String toString() {
		return "SourceAccount@" + Integer.toHexString(hashCode())
			+ "[id=" + id 
			+ ", phoneNo=" + phoneNo
			+ ", sracData=" + sracData 
			+ ", sracKey=" + sracKey 
			+ ", createdOn=" + createdOn 
			+ ", createdBy=" + createdBy 
			+ ", updatedOn=" + updatedOn 
			+ ", updatedBy=" + updatedBy
			+ "]";
	}
	
}
