package id.co.emobile.samba.web.entity;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class DistributionHeader implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String trxCode;
	/*
	 * listJenis.put("1", "Transfer Sesama"); listJenis.put("2",
	 * "Transfer Antar Bank"); listJenis.put("3", "Pembayaran Pajak");
	 */
	private String sysLogNo;  // syslogno VARCHAR(14) NOT NULL,
	private int groupId;
	private String groupName;
	private String fileData;  // file_data VARCHAR(50) NOT NULL,
	private String fileAssignment;  // file_assignment VARCHAR(50) NOT NULL,
	private int sourceAccountId;  // source_account_id INT NOT NULL,
	private String phoneNo;  // phone_no VARCHAR(50) NOT NULL,
	private String sracNumber;  // srac_number VARCHAR(50) NOT NULL,
	private String sracName;  
	private String makerRemarks;  // maker_remarks VARCHAR(1000) NOT NULL,
	private String checkerRemarks; // checker_remarks VARCHAR(1000) NOT NULL,
	private String callbackRemarks; // checker_remarks VARCHAR(1000) NOT NULL,
	private String approvalRemarks;  //approval_remarks VARCHAR(1000) NOT NULL,
	private int status;   // status INT NOT NULL,
	private int uploadedAmount;  // uploaded_amount INT,
	private double uploadedValue;  // uploaded_value DECIMAL(12,2),
	private int uploadedBy;  // uploaded_by INT NOT NULL,
	private Date uploadedOn;  // uploaded_on DATETIME,
	private int checkedBy;  // checked_by INT NOT NULL,
	private Date checkedOn;  // checked_on DATETIME,
	private int callbackBy;  // checked_by INT NOT NULL,
	private Date callbackOn;  // checked_on DATETIME,
	private int approvedBy;  // approved_by INT NOT NULL,
	private Date approvedOn;  // approved_on DATETIME,
	private int processSuccess; // process_success INT,
	private int processFailed;  // process_failed INT,
	private double processValue;  // process_value DECIMAL(12,2),
	private Date processStarted;  // process_started DATETIME,
	private Date processFinished;  // process_finished DATETIME,
	
	// for display
	private String statusDisplay;
	private String uploadedUserCode;
	private String uploadedUserName;
	private String checkedUserCode;
	private String checkedUserName;
	private String callbackUserCode;
	private String callbackUserName;
	private String approvedUserCode;
	private String approvedUserName;
	
	private String bit48;
	
	// for temporary data
	private String otp;
	
	public String getSysLogNo() {
		return sysLogNo;
	}
	public void setSysLogNo(String sysLogNo) {
		this.sysLogNo = sysLogNo;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
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
	public int getSourceAccountId() {
		return sourceAccountId;
	}
	public void setSourceAccountId(int sourceAccountId) {
		this.sourceAccountId = sourceAccountId;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getSracNumber() {
		return sracNumber;
	}
	public void setSracNumber(String sracNumber) {
		this.sracNumber = sracNumber;
	}
	public String getMakerRemarks() {
		return makerRemarks;
	}
	public void setMakerRemarks(String makerRemarks) {
		this.makerRemarks = makerRemarks;
	}
	public String getCheckerRemarks() {
		return checkerRemarks;
	}
	public void setCheckerRemarks(String checkerRemarks) {
		this.checkerRemarks = checkerRemarks;
	}
	public String getCallbackRemarks() {
		return callbackRemarks;
	}
	public void setCallbackRemarks(String callbackRemarks) {
		this.callbackRemarks = callbackRemarks;
	}
	public String getApprovalRemarks() {
		return approvalRemarks;
	}
	public void setApprovalRemarks(String approvalRemarks) {
		this.approvalRemarks = approvalRemarks;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getUploadedAmount() {
		return uploadedAmount;
	}
	public void setUploadedAmount(int uploadedAmount) {
		this.uploadedAmount = uploadedAmount;
	}
	public double getUploadedValue() {
		return uploadedValue;
	}
	public void setUploadedValue(double uploadedValue) {
		this.uploadedValue = uploadedValue;
	}
	public int getUploadedBy() {
		return uploadedBy;
	}
	public void setUploadedBy(int uploadedBy) {
		this.uploadedBy = uploadedBy;
	}
	public Date getUploadedOn() {
		return uploadedOn;
	}
	public void setUploadedOn(Date uploadedOn) {
		this.uploadedOn = uploadedOn;
	}
	public int getCheckedBy() {
		return checkedBy;
	}
	public void setCheckedBy(int checkedBy) {
		this.checkedBy = checkedBy;
	}
	public Date getCheckedOn() {
		return checkedOn;
	}
	public void setCheckedOn(Date checkedOn) {
		this.checkedOn = checkedOn;
	}
	public int getCallbackBy() {
		return callbackBy;
	}
	public void setCallbackBy(int callbackBy) {
		this.callbackBy = callbackBy;
	}
	public Date getCallbackOn() {
		return callbackOn;
	}
	public void setCallbackOn(Date callbackOn) {
		this.callbackOn = callbackOn;
	}
	public int getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(int approvedBy) {
		this.approvedBy = approvedBy;
	}
	public Date getApprovedOn() {
		return approvedOn;
	}
	public void setApprovedOn(Date approvedOn) {
		this.approvedOn = approvedOn;
	}
	public int getProcessSuccess() {
		return processSuccess;
	}
	public void setProcessSuccess(int processSuccess) {
		this.processSuccess = processSuccess;
	}
	public int getProcessFailed() {
		return processFailed;
	}
	public void setProcessFailed(int processFailed) {
		this.processFailed = processFailed;
	}
	public double getProcessValue() {
		return processValue;
	}
	public void setProcessValue(double processValue) {
		this.processValue = processValue;
	}
	public Date getProcessStarted() {
		return processStarted;
	}
	public void setProcessStarted(Date processStarted) {
		this.processStarted = processStarted;
	}
	public Date getProcessFinished() {
		return processFinished;
	}
	public void setProcessFinished(Date processFinished) {
		this.processFinished = processFinished;
	}
	public String getStatusDisplay() {
		return statusDisplay;
	}
	public void setStatusDisplay(String statusDisplay) {
		this.statusDisplay = statusDisplay;
	}
	public String getUploadedUserCode() {
		return uploadedUserCode;
	}
	public void setUploadedUserCode(String uploadedUserCode) {
		this.uploadedUserCode = uploadedUserCode;
	}
	public String getUploadedUserName() {
		return uploadedUserName;
	}
	public void setUploadedUserName(String uploadedUserName) {
		this.uploadedUserName = uploadedUserName;
	}
	public String getCheckedUserCode() {
		return checkedUserCode;
	}
	public void setCheckedUserCode(String checkedUserCode) {
		this.checkedUserCode = checkedUserCode;
	}
	public String getCheckedUserName() {
		return checkedUserName;
	}
	public void setCheckedUserName(String checkedUserName) {
		this.checkedUserName = checkedUserName;
	}
	public String getCallbackUserCode() {
		return callbackUserCode;
	}
	public void setCallbackUserCode(String callbackUserCode) {
		this.callbackUserCode = callbackUserCode;
	}
	public String getCallbackUserName() {
		return callbackUserName;
	}
	public void setCallbackUserName(String callbackUserName) {
		this.callbackUserName = callbackUserName;
	}
	public String getApprovedUserCode() {
		return approvedUserCode;
	}
	public void setApprovedUserCode(String approvedUserCode) {
		this.approvedUserCode = approvedUserCode;
	}
	public String getApprovedUserName() {
		return approvedUserName;
	}
	public void setApprovedUserName(String approvedUserName) {
		this.approvedUserName = approvedUserName;
	}
	
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	
	public double getFailedValue() {
		return uploadedValue - processValue;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	public String getSracName() {
		return sracName;
	}
	public void setSracName(String sracName) {
		this.sracName = sracName;
	}
	public String getTrxCode() {
		return trxCode;
	}
	public void setTrxCode(String trxCode) {
		this.trxCode = trxCode;
	}
	public String getBit48() {
		return bit48;
	}
	public void setBit48(String bit48) {
		this.bit48 = bit48;
	}
	
	
	
	
	
}
