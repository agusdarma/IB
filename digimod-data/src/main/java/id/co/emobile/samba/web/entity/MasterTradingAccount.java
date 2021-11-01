package id.co.emobile.samba.web.entity;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class MasterTradingAccount implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private String tradingAccountNo;
	private String myfxbookId;	
	private String tradingServer;
	private String name;
	private String passwordTrading;
	private String passwordInvestor;
	private String ibUserCode;
	private String ibUserName;
	private int status;
	private String userStatusDisplay;
	private Date createdOn;
	private int createdBy;
	private Date updatedOn;
	private int updatedBy;
	private int rowNum;
	private String pctSharingProfit;
	private int commissionInDollar;

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

	public String getTradingAccountNo() {
		return tradingAccountNo;
	}

	public void setTradingAccountNo(String tradingAccountNo) {
		this.tradingAccountNo = tradingAccountNo;
	}

	public String getTradingServer() {
		return tradingServer;
	}

	public void setTradingServer(String tradingServer) {
		this.tradingServer = tradingServer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPasswordTrading() {
		return passwordTrading;
	}

	public void setPasswordTrading(String passwordTrading) {
		this.passwordTrading = passwordTrading;
	}

	public String getPasswordInvestor() {
		return passwordInvestor;
	}

	public void setPasswordInvestor(String passwordInvestor) {
		this.passwordInvestor = passwordInvestor;
	}

	public String getIbUserCode() {
		return ibUserCode;
	}

	public void setIbUserCode(String ibUserCode) {
		this.ibUserCode = ibUserCode;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getIbUserName() {
		return ibUserName;
	}

	public void setIbUserName(String ibUserName) {
		this.ibUserName = ibUserName;
	}

	public String getUserStatusDisplay() {
		return userStatusDisplay;
	}

	public void setUserStatusDisplay(String userStatusDisplay) {
		this.userStatusDisplay = userStatusDisplay;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public String getPctSharingProfit() {
		return pctSharingProfit;
	}

	public void setPctSharingProfit(String pctSharingProfit) {
		this.pctSharingProfit = pctSharingProfit;
	}

	public String getMyfxbookId() {
		return myfxbookId;
	}

	public void setMyfxbookId(String myfxbookId) {
		this.myfxbookId = myfxbookId;
	}

	public int getCommissionInDollar() {
		return commissionInDollar;
	}

	public void setCommissionInDollar(int commissionInDollar) {
		this.commissionInDollar = commissionInDollar;
	}
}
