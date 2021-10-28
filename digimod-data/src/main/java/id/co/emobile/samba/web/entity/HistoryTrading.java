package id.co.emobile.samba.web.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class HistoryTrading implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private String symbol;
	private String openTime;
	private String closeTime;
	private String action;
	private String sizeLot;
	private String openPrice;
	private String closePrice;
	private String profit;
	private String myfxbookId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getOpenTime() {
		return openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	public String getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getSizeLot() {
		return sizeLot;
	}

	public void setSizeLot(String sizeLot) {
		this.sizeLot = sizeLot;
	}

	public String getOpenPrice() {
		return openPrice;
	}

	public void setOpenPrice(String openPrice) {
		this.openPrice = openPrice;
	}

	public String getClosePrice() {
		return closePrice;
	}

	public void setClosePrice(String closePrice) {
		this.closePrice = closePrice;
	}

	public String getProfit() {
		return profit;
	}

	public void setProfit(String profit) {
		this.profit = profit;
	}

	public String getMyfxbookId() {
		return myfxbookId;
	}

	public void setMyfxbookId(String myfxbookId) {
		this.myfxbookId = myfxbookId;
	}
}
