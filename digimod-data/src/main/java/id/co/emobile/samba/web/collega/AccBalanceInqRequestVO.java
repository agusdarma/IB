package id.co.emobile.samba.web.collega;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class AccBalanceInqRequestVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

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
	public String getAuthKey() {
		return authKey;
	}
	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}
	public String getReqId() {
		return reqId;
	}
	public void setReqId(String reqId) {
		this.reqId = reqId;
	}
	public String getTxDate() {
		return txDate;
	}
	public void setTxDate(String txDate) {
		this.txDate = txDate;
	}
	public String getTxHour() {
		return txHour;
	}
	public void setTxHour(String txHour) {
		this.txHour = txHour;
	}
	public String getUserGtw() {
		return userGtw;
	}
	public void setUserGtw(String userGtw) {
		this.userGtw = userGtw;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getAccnbr() {
		return accnbr;
	}
	public void setAccnbr(String accnbr) {
		this.accnbr = accnbr;
	}
	public int getFlgSaldo() {
		return flgSaldo;
	}
	public void setFlgSaldo(int flgSaldo) {
		this.flgSaldo = flgSaldo;
	}
	public int getFlgNsb() {
		return flgNsb;
	}
	public void setFlgNsb(int flgNsb) {
		this.flgNsb = flgNsb;
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
