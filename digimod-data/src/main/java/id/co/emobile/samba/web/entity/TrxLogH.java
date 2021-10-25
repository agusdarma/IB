package id.co.emobile.samba.web.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TrxLogH implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	private String syslogno;
	private String phoneNo;
	private String bankCode;
	private String trxCode;
	private int trxGroup;
	private String clientRef;
	private String lastRc;
	private int lastState;
	private String srac;
	private double trxAmount;
	private String billerNo;
	private String billerAccNo;
	private String sysMessage;
	private Date receivedTime;
	private Date createdOn;
	private Date updatedOn;
	
	private String receivedTimeDisplay;
	private String bankName;
	
	private int rowNum;
	public String getSyslogno() {
		return syslogno;
	}
	public void setSyslogno(String syslogno) {
		this.syslogno = syslogno;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getTrxCode() {
		return trxCode;
	}
	public void setTrxCode(String trxCode) {
		this.trxCode = trxCode;
	}
	public int getTrxGroup() {
		return trxGroup;
	}
	public void setTrxGroup(int trxGroup) {
		this.trxGroup = trxGroup;
	}
	public String getClientRef() {
		return clientRef;
	}
	public void setClientRef(String clientRef) {
		this.clientRef = clientRef;
	}
	public String getLastRc() {
		return lastRc;
	}
	public void setLastRc(String lastRc) {
		this.lastRc = lastRc;
	}
	public int getLastState() {
		return lastState;
	}
	public void setLastState(int lastState) {
		this.lastState = lastState;
	}
	public String getSrac() {
		return srac;
	}
	public void setSrac(String srac) {
		this.srac = srac;
	}
	public double getTrxAmount() {
		return trxAmount;
	}
	public void setTrxAmount(double trxAmount) {
		this.trxAmount = trxAmount;
	}
	public String getBillerNo() {
		return billerNo;
	}
	public void setBillerNo(String billerNo) {
		this.billerNo = billerNo;
	}
	public String getBillerAccNo() {
		return billerAccNo;
	}
	public void setBillerAccNo(String billerAccNo) {
		this.billerAccNo = billerAccNo;
	}
	public String getSysMessage() {
		return sysMessage;
	}
	public void setSysMessage(String sysMessage) {
		this.sysMessage = sysMessage;
	}
	public Date getReceivedTime() {
		return receivedTime;
	}
	public void setReceivedTime(Date receivedTime) {
		receivedTimeDisplay = sdf.format(receivedTime);
		this.receivedTime = receivedTime;
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
	public String getReceivedTimeDisplay() {
		return receivedTimeDisplay;
	}
	public void setReceivedTimeDisplay(String receivedTimeDisplay) {
		this.receivedTimeDisplay = receivedTimeDisplay;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public int getRowNum() {
		return rowNum;
	}
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}	
}
