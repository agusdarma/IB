package id.co.emobile.jets.mmbs.bti.http;

import java.util.List;

import org.apache.http.NameValuePair;

public class HttpMsg {

	private NameValuePair[] headers;
	
	private NameValuePair[] params;
	
	private String entity;
	
	private String response;
	
	private int statusCode;

	public NameValuePair[] getHeaders() {
		return headers;
	}

	public void setHeaders(List<NameValuePair> headers) {
		this.headers = headers.toArray(new NameValuePair[headers.size()]);
	}

	public NameValuePair[] getParams() {
		return params;
	}

	public void setParams(List<NameValuePair> params) {
		this.params = params.toArray(new NameValuePair[params.size()]);
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
}
