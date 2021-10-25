package id.co.emobile.samba.web.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppsUserActivity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	private int id;
	private String phoneNo;
	private String appsType;
	private String activity;
	private Date createdOn;
	private String createdOnDisplay;
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
		createdOnDisplay = sdf.format(createdOn);
		this.createdOn = createdOn;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getAppsType() {
		return appsType;
	}
	public void setAppsType(String appsType) {
		this.appsType = appsType;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public String getCreatedOnDisplay() {
		return createdOnDisplay;
	}
	public void setCreatedOnDisplay(String createdOnDisplay) {
		this.createdOnDisplay = createdOnDisplay;
	}
	public int getRowNum() {
		return rowNum;
	}
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
}