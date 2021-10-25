package id.co.emobile.jets.mmbs.bti.http.data;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AbstractResponseVO {

	private String message;
	private int statusId;
	private String rCode;

	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public int getStatusId() {
		return statusId;
	}


	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}


	public String getrCode() {
		return rCode;
	}


	public void setrCode(String rCode) {
		this.rCode = rCode;
	}




	
}
