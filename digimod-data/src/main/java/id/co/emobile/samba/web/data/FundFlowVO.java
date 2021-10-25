package id.co.emobile.samba.web.data;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class FundFlowVO implements Serializable, Comparable<FundFlowVO> {
	private static final long serialVersionUID = 1L;
	
	private int flowSeq;
	
	private String flowType; 

	private String acctFrom; 

	private String acctTo; 	 

	private double amount; 	 

	private double charges;  
	
	private String settType;

	public int getFlowSeq() {
		return flowSeq;
	}

	public void setFlowSeq(int flowSeq) {
		this.flowSeq = flowSeq;
	}

	public String getFlowType() {
		return flowType;
	}

	public void setFlowType(String flowType) {
		this.flowType = flowType;
	}

	public String getAcctFrom() {
		return acctFrom;
	}

	public void setAcctFrom(String acctFrom) {
		this.acctFrom = acctFrom;
	}

	public String getAcctTo() {
		return acctTo;
	}

	public void setAcctTo(String acctTo) {
		this.acctTo = acctTo;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getCharges() {
		return charges;
	}

	public void setCharges(double charges) {
		this.charges = charges;
	}

	public String getSettType() {
		return settType;
	}

	public void setSettType(String settType) {
		this.settType = settType;
	}

	public int compareTo(FundFlowVO o) {
		return  this.flowSeq - o.getFlowSeq(); 
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
