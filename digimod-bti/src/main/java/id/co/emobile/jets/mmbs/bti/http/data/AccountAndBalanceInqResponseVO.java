package id.co.emobile.jets.mmbs.bti.http.data;

import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountAndBalanceInqResponseVO {

	private String message;
	private int statusId;
	private String rCode;
	private String noTelp;
	private String idnBr;
	private String brtDt;
	private String mothrnm;
	private String addr;
	
	
	private String accountNumber;
	private String saldoAkhir;
	
	
	private Map<String, String> result;
	
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public String getMessage() {
		return message;
	}

	@JsonProperty("message")
	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatusId() {
		return statusId;
	}

	@JsonProperty("statusId")
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public String getrCode() {
		return rCode;
	}

	@JsonProperty("rCode")
	public void setrCode(String rCode) {
		this.rCode = rCode;
	}

	public Map<String, String> getResult() {
		return result;
	}

	@JsonProperty("result")
	public void setResult(Map<String, String> result) {
		this.result = result;
	}
	
	public String getNoTelp() {
		return noTelp;
	}

	@JsonProperty("NOTELP")
	public void setNoTelp(String noTelp) {
		this.noTelp = noTelp;
	}

	public String getIdnBr() {
		return idnBr;
	}

	@JsonProperty("IDNBR")
	public void setIdnBr(String idnBr) {
		this.idnBr = idnBr;
	}

	public String getBrtDt() {
		return brtDt;
	}

	@JsonProperty("BRTDT")
	public void setBrtDt(String brtDt) {
		this.brtDt = brtDt;
	}

	public String getMothrnm() {
		return mothrnm;
	}

	@JsonProperty("MOTHRNM")
	public void setMothrnm(String mothrnm) {
		this.mothrnm = mothrnm;
	}

	public String getAddr() {
		return addr;
	}

	@JsonProperty("ADDR")
	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	@JsonProperty("ACCNBR")
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getSaldoAkhir() {
		return saldoAkhir;
	}

	@JsonProperty("SALDO_AKHIR")
	public void setSaldoAkhir(String saldoAkhir) {
		this.saldoAkhir = saldoAkhir;
	}

	
}
