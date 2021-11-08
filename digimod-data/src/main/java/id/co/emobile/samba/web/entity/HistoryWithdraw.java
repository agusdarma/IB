package id.co.emobile.samba.web.entity;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class HistoryWithdraw implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private String ibUserCode;
	private double amount;
	private int status;
	private Date dateWithdrawOn;

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIbUserCode() {
		return ibUserCode;
	}

	public void setIbUserCode(String ibUserCode) {
		this.ibUserCode = ibUserCode;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getDateWithdrawOn() {
		return dateWithdrawOn;
	}

	public void setDateWithdrawOn(Date dateWithdrawOn) {
		this.dateWithdrawOn = dateWithdrawOn;
	}
}
