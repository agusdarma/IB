package id.co.emobile.samba.web.data;

import id.co.emobile.samba.web.entity.SummaryDailyTrx;

import java.util.List;

public class SummaryDailyTransactionVO{

	private List<SummaryDailyTrx> listSummary;
	private String message;

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<SummaryDailyTrx> getListSummary() {
		return listSummary;
	}
	public void setListSummary(List<SummaryDailyTrx> listSummary) {
		this.listSummary = listSummary;
	}
}
