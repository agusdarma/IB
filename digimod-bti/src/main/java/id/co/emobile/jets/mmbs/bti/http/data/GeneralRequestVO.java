package id.co.emobile.jets.mmbs.bti.http.data;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class GeneralRequestVO {

	private String jsonRequest;

	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}


	public String getJsonRequest() {
		return jsonRequest;
	}


	public void setJsonRequest(String jsonRequest) {
		this.jsonRequest = jsonRequest;
	}

}
