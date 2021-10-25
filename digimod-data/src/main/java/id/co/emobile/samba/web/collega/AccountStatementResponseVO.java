package id.co.emobile.samba.web.collega;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountStatementResponseVO {

	private String message;
	private int statusId;
	private String rCode;
	private String saldoAwal;
	private String saldoAkhir;
	
	private List<Map<String, String>> result;
	
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

	public List<Map<String, String>> getResult() {
		return result;
	}

	public void setResult(List<Map<String, String>> result) {
		this.result = result;
	}
	
	public String getSaldoAkhir() {
		return saldoAkhir;
	}
	@JsonProperty("saldoAkhir")
	public void setSaldoAkhir(String saldoAkhir) {
		this.saldoAkhir = saldoAkhir;
	}

	public String getSaldoAwal() {
		return saldoAwal;
	}
	@JsonProperty("saldoAwal")
	public void setSaldoAwal(String saldoAwal) {
		this.saldoAwal = saldoAwal;
	}

	
}
