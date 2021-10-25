package id.co.emobile.jets.mmbs.bti.http.data;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeneralHeaderResponseVO {

	private String message;
	private int statusId;
	private String rCode;
	
	private GeneralResultResponseVO result;
	
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

	public GeneralResultResponseVO getResult() {
		return result;
	}

	@JsonProperty("result")
	public void setResult(GeneralResultResponseVO result) {
		this.result = result;
	}
	
}
