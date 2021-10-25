package id.co.emobile.jets.mmbs.bti.helper;

import java.util.StringTokenizer;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class BankInfo {
	
	private String rtgsCode;
	
	private String name;
	
	private String cnCode;
	
	private String cityCode;
	
	private String branchCode;
	
	/*
	 * Info format : rtgsCode;name;cnCode;cityCode;branchCode
	 */
	public BankInfo(String info) {
		parse(info);
	}

	private void parse(String info) {
		StringTokenizer st = new StringTokenizer(info,";");
		rtgsCode = st.nextToken();
		name = st.nextToken();
		cnCode = st.nextToken();
		cityCode = st.nextToken();
		branchCode = st.nextToken();
	}

	public String getRtgsCode() {
		return rtgsCode;
	}

	public void setRtgsCode(String rtgsCode) {
		this.rtgsCode = rtgsCode;
	}

	public String getCnCode() {
		return cnCode;
	}

	public void setCnCode(String cnCode) {
		this.cnCode = cnCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
