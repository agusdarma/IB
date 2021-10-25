package id.co.emobile.samba.web.data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MbankingUserVO {
	
	private String phoneNo;
	private Date trxDate;
	private String trxDateDisplay;
	private int rowNum;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public Date getTrxDate() {
		return trxDate;
	}
	public void setTrxDate(Date trxDate) {
		trxDateDisplay = sdf.format(trxDate);
		this.trxDate = trxDate;
	}
	public String getTrxDateDisplay() {
		return trxDateDisplay;
	}
	public void setTrxDateDisplay(String trxDateDisplay) {
		this.trxDateDisplay = trxDateDisplay;
	}
	public int getRowNum() {
		return rowNum;
	}
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

}
