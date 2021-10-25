package id.co.emobile.samba.web.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class AccStatementVO implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	private String statementRc;
	private String message;
	private double startBalance;
	private double endBalance;
	private List<AccStatementDetailVO> listDetail = new ArrayList<AccStatementDetailVO>();
	
	public String getStatementRc() {
		return statementRc;
	}
	public void setStatementRc(String statementRc) {
		this.statementRc = statementRc;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public double getStartBalance() {
		return startBalance;
	}
	public void setStartBalance(double startBalance) {
		this.startBalance = startBalance;
	}
	public double getEndBalance() {
		return endBalance;
	}
	public void setEndBalance(double endBalance) {
		this.endBalance = endBalance;
	}
	public List<AccStatementDetailVO> getListDetail() {
		return listDetail;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	
}
