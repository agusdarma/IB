package id.co.emobile.samba.web.entity;

import java.io.File;
import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Bank implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	//show on 0 = all, 1 = android, 2 = ios
	//show default account 1 = yes, 2 = no
	
	private int id;				//id                int(10) auto_increment not null,
	private String bankCode;	//  bank_code         varchar(10) not null,
	private String bankName;	//  bank_name         varchar(50) null,
	private String bankLogo;	//  bank_logo         varchar(200) null,
	private String bankInitial;	//	bank_initial	  varchar(20) null
	private int showOn;			// show_on           int not null default 0,
	private int showDefAccount;	//  show_def_account  int not null default 1,
	private String ussdCode;	//  ussd_code         varchar(50) null,
	private String tpda;		// tpda              varchar(100) null,
	private int createdBy;		//  created_by        int not null,
	private Date createdOn;		//  created_on        datetime not null,
	private int updatedBy;		//  updated_by        int not null,
	private Date updatedOn;		//  updated_on        datetime not null,
	private String createdByDisplay;
	private String updatedByDisplay;
	private File imageLogo;
	private String bankUrl;
	private int versionStatus;
	private int showOrder;
	private int supportLongCall;
	private String showOnDisplay;
	
	private int rowNum;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankLogo() {
		return bankLogo;
	}
	public void setBankLogo(String bankLogo) {
		this.bankLogo = bankLogo;
	}
	public int getShowOn() {
		return showOn;
	}
	public void setShowOn(int showOn) {
		this.showOn = showOn;
	}
	public int getShowDefAccount() {
		return showDefAccount;
	}
	public void setShowDefAccount(int showDefAccount) {
		this.showDefAccount = showDefAccount;
	}
	public String getUssdCode() {
		return ussdCode;
	}
	public void setUssdCode(String ussdCode) {
		this.ussdCode = ussdCode;
	}
	public String getTpda() {
		return tpda;
	}
	public void setTpda(String tpda) {
		this.tpda = tpda;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public int getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
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
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public Date getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	public String getBankInitial() {
		return bankInitial;
	}
	public void setBankInitial(String bankInitial) {
		this.bankInitial = bankInitial;
	}
	public File getImageLogo() {
		return imageLogo;
	}
	public void setImageLogo(File imageLogo) {
		this.imageLogo = imageLogo;
	}
	public String getBankUrl() {
		return bankUrl;
	}
	public void setBankUrl(String bankUrl) {
		this.bankUrl = bankUrl;
	}
	public int getVersionStatus() {
		return versionStatus;
	}
	public void setVersionStatus(int versionStatus) {
		this.versionStatus = versionStatus;
	}
	public int getShowOrder() {
		return showOrder;
	}
	public void setShowOrder(int showOrder) {
		this.showOrder = showOrder;
	}
	public int getSupportLongCall() {
		return supportLongCall;
	}
	public void setSupportLongCall(int supportLongCall) {
		this.supportLongCall = supportLongCall;
	}
	public int getRowNum() {
		return rowNum;
	}
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
	public String getShowOnDisplay() {
		return showOnDisplay;
	}
	public void setShowOnDisplay(String showOnDisplay) {
		this.showOnDisplay = showOnDisplay;
	}
}