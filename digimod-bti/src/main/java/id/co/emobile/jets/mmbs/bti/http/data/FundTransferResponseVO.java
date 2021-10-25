package id.co.emobile.jets.mmbs.bti.http.data;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FundTransferResponseVO {

	private String message;
	private int statusId;
	private String rCode;
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

	@JsonProperty("result")
	public void setResult(List<Map<String, String>> result) {
		this.result = result;
	}

	

	
}
