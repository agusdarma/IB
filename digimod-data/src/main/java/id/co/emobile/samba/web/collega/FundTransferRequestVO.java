package id.co.emobile.samba.web.collega;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FundTransferRequestVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private String authKey;
	private String reqId;
	private String txDate;
	private String txHour;
	private String userGtw;
	private String channelId;
	private String corpId;
	private String prodId;
	private String date;
	private String date_rk;
	private String branchId;
	private String txCcy;
	private String nbrOfAcc;
	private String totalAmount;
	private String prosesId;
	private String userId;
	private String spvId;
	private String revSts;
	private String txType;
	private String refAcc;
	
	private List<Map<String, Object>> param;
	
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

	public String getCorpId() {
		return corpId;
	}

	@JsonProperty("corpId")
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getProdId() {
		return prodId;
	}

	@JsonProperty("prodId")
	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public String getDate() {
		return date;
	}

	@JsonProperty("date")
	public void setDate(String date) {
		this.date = date;
	}

	public String getDate_rk() {
		return date_rk;
	}

	@JsonProperty("date_rk")
	public void setDate_rk(String date_rk) {
		this.date_rk = date_rk;
	}

	public String getBranchId() {
		return branchId;
	}

	@JsonProperty("branchId")
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getTxCcy() {
		return txCcy;
	}

	@JsonProperty("txCcy")
	public void setTxCcy(String txCcy) {
		this.txCcy = txCcy;
	}

	public String getNbrOfAcc() {
		return nbrOfAcc;
	}

	@JsonProperty("nbrOfAcc")
	public void setNbrOfAcc(String nbrOfAcc) {
		this.nbrOfAcc = nbrOfAcc;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	@JsonProperty("totalAmount")
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getProsesId() {
		return prosesId;
	}

	@JsonProperty("prosesId")
	public void setProsesId(String prosesId) {
		this.prosesId = prosesId;
	}

	public String getUserId() {
		return userId;
	}

	@JsonProperty("userId")
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSpvId() {
		return spvId;
	}

	@JsonProperty("spvId")
	public void setSpvId(String spvId) {
		this.spvId = spvId;
	}

	public String getRevSts() {
		return revSts;
	}

	@JsonProperty("revSts")
	public void setRevSts(String revSts) {
		this.revSts = revSts;
	}

	public String getTxType() {
		return txType;
	}

	@JsonProperty("txType")
	public void setTxType(String txType) {
		this.txType = txType;
	}

	public String getRefAcc() {
		return refAcc;
	}

	@JsonProperty("refAcc")
	public void setRefAcc(String refAcc) {
		this.refAcc = refAcc;
	}

	public List<Map<String, Object>> getParam() {
		return param;
	}

	public void setParam(List<Map<String, Object>> param) {
		this.param = param;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
