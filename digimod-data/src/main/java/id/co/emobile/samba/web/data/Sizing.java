package id.co.emobile.samba.web.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Sizing {
	@JsonProperty("type")
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	String type;

	@JsonProperty("value")
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	String value;
}
