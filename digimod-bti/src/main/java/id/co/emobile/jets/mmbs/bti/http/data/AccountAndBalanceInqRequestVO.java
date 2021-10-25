package id.co.emobile.jets.mmbs.bti.http.data;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountAndBalanceInqRequestVO {

	private String authKey;
	private String reqId;
	private String txDate;
	private String txHour;
	private String userGtw;
	private String channelId;
	private String accnbr;
	//private String countTrx;
	private int flgSaldo;
	private int flgNsb;
	
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public String getAuthKey() {
		return authKey;
	}

	@JsonProperty("authKey")
	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public String getReqId() {
		return reqId;
	}

	@JsonProperty("reqId")
	public void setReqId(String reqId) {
		this.reqId = reqId;
	}

	public String getTxDate() {
		return txDate;
	}

	@JsonProperty("txDate")
	public void setTxDate(String txDate) {
		this.txDate = txDate;
	}

	public String getTxHour() {
		return txHour;
	}

	@JsonProperty("txHour")
	public void setTxHour(String txHour) {
		this.txHour = txHour;
	}

	public String getUserGtw() {
		return userGtw;
	}

	@JsonProperty("userGtw")
	public void setUserGtw(String userGtw) {
		this.userGtw = userGtw;
	}

	public String getChannelId() {
		return channelId;
	}

	@JsonProperty("channelId")
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getAccnbr() {
		return accnbr;
	}

	@JsonProperty("accnbr")
	public void setAccnbr(String accnbr) {
		this.accnbr = accnbr;
	}

	public int getFlgSaldo() {
		return flgSaldo;
	}

	@JsonProperty("flgSaldo")
	public void setFlgSaldo(int flgSaldo) {
		this.flgSaldo = flgSaldo;
	}

	public int getFlgNsb() {
		return flgNsb;
	}

	@JsonProperty("flgNsb")
	public void setFlgNsb(int flgNsb) {
		this.flgNsb = flgNsb;
	}

//	public String getCountTrx() {
//		return countTrx;
//	}
//
//	@JsonProperty("countTrx")
//	public void setCountTrx(String countTrx) {
//		this.countTrx = countTrx;
//	}
}
