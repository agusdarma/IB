package id.co.emobile.samba.web.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class DistMakerVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String trxCode;
	/*
	 * listJenis.put("1", "Transfer Sesama"); listJenis.put("2",
	 * "Transfer Antar Bank"); listJenis.put("3", "Pembayaran Pajak");
	 */
	
	private String sysLogNo;
	private int sracId;
	private String sracNo;
	private String sracName;
	private String remarks;
	private String otp;
	private String fileData;
	private String fileAssignment;
	private List<DistDetailVO> listDetail = new ArrayList<>();
	
	public String getSysLogNo() {
		return sysLogNo;
	}
	public void setSysLogNo(String sysLogNo) {
		this.sysLogNo = sysLogNo;
	}
	public int getSracId() {
		return sracId;
	}
	public void setSracId(int sracId) {
		this.sracId = sracId;
	}
	public String getSracNo() {
		return sracNo;
	}
	public void setSracNo(String sracNo) {
		this.sracNo = sracNo;
	}
	public String getSracName() {
		return sracName;
	}
	public void setSracName(String sracName) {
		this.sracName = sracName;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public String getFileData() {
		return fileData;
	}
	public void setFileData(String fileData) {
		this.fileData = fileData;
	}
	public String getFileAssignment() {
		return fileAssignment;
	}
	public void setFileAssignment(String fileAssignment) {
		this.fileAssignment = fileAssignment;
	}
	public List<DistDetailVO> getListDetail() {
		return listDetail;
	}
	public double getTotalAmount() {
		double total = 0;
		for (DistDetailVO detail: listDetail)
			total += detail.getAmount();
		return total;
	}
	public boolean isDetailCheckSuccess() {
		for (DistDetailVO detail: listDetail)
			if (!detail.isCheckSuccess()) return false;
		return true;
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	public String getTrxCode() {
		return trxCode;
	}
	public void setTrxCode(String trxCode) {
		this.trxCode = trxCode;
	}
	
		
}
