package id.co.emobile.jets.mmbs.bti.http.data;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class GeneralResponseVO {

	private String jsonResponse;
	
	private String rc;

	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}


	public String getJsonResponse() {
		return jsonResponse;
	}


	public void setJsonResponse(String jsonResponse) {
		this.jsonResponse = jsonResponse;
	}


	public String getRc() {
		return rc;
	}


	public void setRc(String rc) {
		this.rc = rc;
	}


	
}
