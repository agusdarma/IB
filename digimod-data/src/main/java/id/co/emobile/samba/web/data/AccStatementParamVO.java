package id.co.emobile.samba.web.data;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class AccStatementParamVO implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	private int accountId;
	private Date startDate;
	private Date endDate;
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
