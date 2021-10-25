package id.co.emobile.samba.web.collega;

import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FundTransferResponseVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private String message;
	private int statusId;
	private String rCode;
	private Map<String, String> result;
	
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

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	
}
