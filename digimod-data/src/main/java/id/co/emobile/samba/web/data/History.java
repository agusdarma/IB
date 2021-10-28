package id.co.emobile.samba.web.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class History {
	@JsonProperty("openTime")
	public String getOpenTime() {
		return this.openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	String openTime;

	@JsonProperty("closeTime")
	public String getCloseTime() {
		return this.closeTime;
	}

	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}

	String closeTime;

	@JsonProperty("symbol")
	public String getSymbol() {
		return this.symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	String symbol;

	@JsonProperty("action")
	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	String action;

	@JsonProperty("sizing")
	public Sizing getSizing() {
		return this.sizing;
	}

	public void setSizing(Sizing sizing) {
		this.sizing = sizing;
	}

	Sizing sizing;

	@JsonProperty("openPrice")
	public String getOpenPrice() {
		return this.openPrice;
	}

	public void setOpenPrice(String openPrice) {
		this.openPrice = openPrice;
	}

	String openPrice;

	@JsonProperty("closePrice")
	public String getClosePrice() {
		return this.closePrice;
	}

	public void setClosePrice(String closePrice) {
		this.closePrice = closePrice;
	}

	String closePrice;

	@JsonProperty("tp")
	public double getTp() {
		return this.tp;
	}

	public void setTp(double tp) {
		this.tp = tp;
	}

	double tp;

	@JsonProperty("sl")
	public double getSl() {
		return this.sl;
	}

	public void setSl(double sl) {
		this.sl = sl;
	}

	double sl;

	@JsonProperty("pips")
	public double getPips() {
		return this.pips;
	}

	public void setPips(double pips) {
		this.pips = pips;
	}

	double pips;

	@JsonProperty("profit")
	public String getProfit() {
		return this.profit;
	}

	public void setProfit(String profit) {
		this.profit = profit;
	}

	String profit;

	@JsonProperty("comment")
	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	String comment;

	@JsonProperty("interest")
	public double getInterest() {
		return this.interest;
	}

	public void setInterest(double interest) {
		this.interest = interest;
	}

	double interest;

	@JsonProperty("commission")
	public double getCommission() {
		return this.commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
	}

	double commission;
}
