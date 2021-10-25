package id.co.emobile.samba.web.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SummaryDailyTrx implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	
	private Date trxDate; //trx_date
	private String trxDateDisplay;
	private String bankCode;
	private String bankName;
	private String trxCode;
	private String trxGroup;
	private int amount;
	
	private String tableName;
	private int rowNum;
	
	public Date getTrxDate() {
		return trxDate;
	}
	public void setTrxDate(Date trxDate) {
		this.trxDate = trxDate;
		if (this.trxDate == null) trxDateDisplay = "";
		else trxDateDisplay = sdf.format(this.trxDate);
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getTrxDateDisplay() {
		return trxDateDisplay;
	}
		
	public String getTrxCode() {
		return trxCode;
	}
	public void setTrxCode(String trxCode) {
		this.trxCode = trxCode;
	}
	
	public String getTrxGroup() {
		return trxGroup;
	}
	public void setTrxGroup(String trxGroup) {
		this.trxGroup = trxGroup;
	}
	
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public void setTrxDateDisplay(String trxDateDisplay) {
		this.trxDateDisplay = trxDateDisplay;
	}
	@Override
	public String toString() {
		return "SummaryDailyTrx@" + Integer.toHexString(hashCode()) 
			+ "[trxDate=" + trxDate 
			+ ", trxDateDisplay=" + trxDateDisplay 
			+ ", bankName=" + bankName
			+ ", bankCode=" + bankCode 
			+ ", trxCode=" + trxCode 
			+ ", trxGroup=" + trxGroup 
			+ ", amount=" + amount
			+ "]";
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public int getRowNum() {
		return rowNum;
	}
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
	
}
