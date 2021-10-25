package id.co.emobile.samba.web.data.param;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class ReportParamVO extends ParamPagingVO{
	private static final long serialVersionUID = 1L; 
	
	private List<Integer> listGroupId;
	private Date inputReceivedStart;
	private Date inputReceivedEnd;
	private String initiatorCode;
	private String channelInput;
	private String messageInput;

	private Date outputReceivedStart;
	private Date outputReceivedEnd;
	private String channelOutput;

	private String transactionType;
	private String transactionCode;
	private String mobileNumber;
	private String mobileNumberDB;

	private int trxLogHId;

	private Date startDate;
	private Date endDate;
	
	private Date periodStart;
	private Date periodEnd;
	private Date trxDate;
	private String sysNumber;
	private String sracNo;
	
	private String adsName;
	private String adsLocation;
	
	private String operator;
	private String gatewayRef;
	private String transactionStatus;
	private String tpda;

	private int useLimit; //0 yes 1 no
	
	private double pricePrepaid;
	private double pricePostpaid;
	private double priceFailed;
	
	private int month;
	private int year;
	private String phoneType;
	private int bankId;
	private String phoneNo;
	private String appsType;
	private String trxCode;
	private String tableName;
	private String bankCode;
	private int trxGroup;
	private int userType;
	
	public String getMessageInput() {
		return messageInput;
	}

	public void setMessageInput(String messageInput) {
		this.messageInput = messageInput;
	}

	public int getTrxLogHId() {
		return trxLogHId;
	}

	public void setTrxLogHId(int trxLogHId) {
		this.trxLogHId = trxLogHId;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	@Override
	protected String getPrimaryKey() {
		return null;
	}

	public Date getInputReceivedStart() {
		return inputReceivedStart;
	}
	public void setInputReceivedStart(Date inputReceivedStart) {
		this.inputReceivedStart = inputReceivedStart;
	}
	public Date getInputReceivedEnd() {
		return inputReceivedEnd;
	}
	public void setInputReceivedEnd(Date inputReceivedEnd) {
		this.inputReceivedEnd = inputReceivedEnd;
	}

	public String getInitiatorCode() {
		return initiatorCode;
	}

	public void setInitiatorCode(String initiatorCode) {
		this.initiatorCode = initiatorCode;
	}

	public String getChannelInput() {
		return channelInput;
	}
	public void setChannelInput(String channelInput) {
		this.channelInput = channelInput;
	}

	public Date getOutputReceivedStart() {
		return outputReceivedStart;
	}

	public void setOutputReceivedStart(Date outputReceivedStart) {
		this.outputReceivedStart = outputReceivedStart;
	}

	public Date getOutputReceivedEnd() {
		return outputReceivedEnd;
	}

	public void setOutputReceivedEnd(Date outputReceivedEnd) {
		this.outputReceivedEnd = outputReceivedEnd;
	}

	public String getChannelOutput() {
		return channelOutput;
	}

	public void setChannelOutput(String channelOutput) {
		this.channelOutput = channelOutput;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public String getMobileNumberDB() {
		if(mobileNumber!=null){
			if(mobileNumber.startsWith("+62")||mobileNumber.startsWith("62")||mobileNumber.startsWith("+0"))
			{
				if(mobileNumber.startsWith("+62"))
				{
					mobileNumber=mobileNumber.substring(3, mobileNumber.length());
				}
				else
				{
					mobileNumber=mobileNumber.substring(1, mobileNumber.length());
				}
				mobileNumber="0" + mobileNumber;
			}
		}
		return mobileNumber;
	}

	public void setMobileNumberDB(String mobileNumberDB) {
		this.mobileNumberDB = mobileNumberDB;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	public Date getPeriodStart() {
		if(periodStart==null)
			periodStart = new Date();
		return periodStart;
	}

	public void setPeriodStart(Date periodStart) {
		if (periodStart == null)
		{
			this.periodStart = null;
		}
		else
		{
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			this.periodStart = cal.getTime();
		}
	}

	public Date getPeriodEnd() {
		if(periodEnd==null)
			periodEnd = new Date();
		return periodEnd;
	}

	public void setPeriodEnd(Date periodEnd) {
		if (periodEnd == null)
		{
			this.periodEnd = null;
		}
		else
		{
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			this.periodEnd = cal.getTime();
		}
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		if (startDate == null)
		{
			this.startDate = null;
		}
		else
		{
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			this.startDate = cal.getTime();
		}
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		if (endDate == null)
		{
			this.endDate = null;
		}
		else
		{
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 999);
			this.endDate = cal.getTime();
		}
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getGatewayRef() {
		return gatewayRef;
	}

	public void setGatewayRef(String gatewayRef) {
		this.gatewayRef = gatewayRef;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public String getTpda() {
		return tpda;
	}

	public void setTpda(String tpda) {
		this.tpda = tpda;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public int getUseLimit() {
		return useLimit;
	}

	public void setUseLimit(int useLimit) {
		this.useLimit = useLimit;
	}

	public double getPricePrepaid() {
		return pricePrepaid;
	}

	public void setPricePrepaid(double pricePrepaid) {
		this.pricePrepaid = pricePrepaid;
	}

	public double getPricePostpaid() {
		return pricePostpaid;
	}

	public void setPricePostpaid(double pricePostpaid) {
		this.pricePostpaid = pricePostpaid;
	}

	public double getPriceFailed() {
		return priceFailed;
	}

	public void setPriceFailed(double priceFailed) {
		this.priceFailed = priceFailed;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public String getAdsName() {
		return adsName;
	}

	public void setAdsName(String adsName) {
		this.adsName = adsName;
	}

	public String getAdsLocation() {
		return adsLocation;
	}

	public void setAdsLocation(String adsLocation) {
		this.adsLocation = adsLocation;
	}

	public int getBankId() {
		return bankId;
	}

	public void setBankId(int bankId) {
		this.bankId = bankId;
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
	
	public String getPhoneNoLike(){
		if(phoneNo!=null)
		{
			return "%"+phoneNo+"%";
		}
		return phoneNo;
	}

	public String getTrxCode() {
		return trxCode;
	}

	public void setTrxCode(String trxCode) {
		this.trxCode = trxCode;
	}

	public Date getTrxDate() {
		if(trxDate==null)
			trxDate = new Date();
		return trxDate;
	}

	public void setTrxDate(Date trxDate) {
		this.trxDate = trxDate;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getTrxGroup() {
		return trxGroup;
	}

	public void setTrxGroup(int trxGroup) {
		this.trxGroup = trxGroup;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public List<Integer> getListGroupId() {
		return listGroupId;
	}

	public void setListGroupId(List<Integer> listGroupId) {
		this.listGroupId = listGroupId;
	}

	public String getSysNumber() {
		return sysNumber;
	}

	public void setSysNumber(String sysNumber) {
		this.sysNumber = sysNumber;
	}
	
	public String getSracNo() {
		return sracNo;
	}

	public void setSracNo(String sracNo) {
		this.sracNo = sracNo;
	}
}
