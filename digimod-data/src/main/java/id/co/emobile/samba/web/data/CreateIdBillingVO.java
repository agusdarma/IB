package id.co.emobile.samba.web.data;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class CreateIdBillingVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private String rc;
	private String rcMessage;
	private String idBilling;
	private String namaWp;
	private String alamatWp;
	private String masaKadaluarsa;
	
	public String getRc() {
		return rc;
	}
	public void setRc(String rc) {
		this.rc = rc;
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	public String getIdBilling() {
		return idBilling;
	}
	public void setIdBilling(String idBilling) {
		this.idBilling = idBilling;
	}
	public String getNamaWp() {
		return namaWp;
	}
	public void setNamaWp(String namaWp) {
		this.namaWp = namaWp;
	}
	public String getAlamatWp() {
		return alamatWp;
	}
	public void setAlamatWp(String alamatWp) {
		this.alamatWp = alamatWp;
	}
	public String getMasaKadaluarsa() {
		return masaKadaluarsa;
	}
	public void setMasaKadaluarsa(String masaKadaluarsa) {
		this.masaKadaluarsa = masaKadaluarsa;
	}
	public String getRcMessage() {
		return rcMessage;
	}
	public void setRcMessage(String rcMessage) {
		this.rcMessage = rcMessage;
	}
	
}
