package id.co.emobile.jets.mmbs.bti.http.data;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeneralResultResponseVO {

	private String noHp;
	private String fullName;
	private String cifId;
	
	
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}



	public String getNoHp() {
		return noHp;
	}



	@JsonProperty("NOHP")
	public void setNoHp(String noHp) {
		this.noHp = noHp;
	}



	public String getFullName() {
		return fullName;
	}


	@JsonProperty("FULLNM")
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}



	public String getCifId() {
		return cifId;
	}


	@JsonProperty("CIFID")
	public void setCifId(String cifId) {
		this.cifId = cifId;
	}

	

	
}
