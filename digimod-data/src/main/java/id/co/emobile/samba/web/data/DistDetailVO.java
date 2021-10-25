package id.co.emobile.samba.web.data;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import id.co.emobile.samba.web.utils.CommonUtil;

public class DistDetailVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private int index;
	private String accNumber;
	private String accName;
	private String bankCode;
	private String tx;
	private double amount;
	private String phoneNo;
	private boolean checkSuccess;
	private String validationDisplay;
	
	private String npwp;
	private String npwpView;
	private String npwpPenyetor;
	private String npwpPenyetorView;
	private String nama;
	private String alamat;
	private String jenisPajak;
	private String jenisSetoran;
	private Date startDatePbb;
	private Date endDatePbb;
	private String tahunPajak;
	private double jumlahSetor;
	private String uraianSsp;
	private String noSk;
	private String noSkView;
	private String kodeKkpSsp;
	private String nop;
	private String nopView;
	private String kotaWp;
	private String nik;
	
	private String idBilling;
	private String masaAktifBilling;
	private Date masaAktifBillingView;
	private boolean isNonNpwp;
	
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getAccNumber() {
		return accNumber;
	}
	public void setAccNumber(String accNumber) {
		this.accNumber = accNumber;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getTx() {
		return tx;
	}
	public void setTx(String tx) {
		this.tx = tx;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public boolean isCheckSuccess() {
		return checkSuccess;
	}
	public void setCheckSuccess(boolean checkSuccess) {
		this.checkSuccess = checkSuccess;
	}
	public String getValidationDisplay() {
		return validationDisplay;
	}
	public void setValidationDisplay(String validationDisplay) {
		this.validationDisplay = validationDisplay;
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getNpwp() {
		return npwp;
	}
	public void setNpwp(String npwp) {
		this.npwp = npwp;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public String getAlamat() {
		return alamat;
	}
	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}
	public String getJenisPajak() {
		return jenisPajak;
	}
	public void setJenisPajak(String jenisPajak) {
		this.jenisPajak = jenisPajak;
	}
	public String getJenisSetoran() {
		return jenisSetoran;
	}
	public void setJenisSetoran(String jenisSetoran) {
		this.jenisSetoran = jenisSetoran;
	}
	public Date getStartDatePbb() {
		return startDatePbb;
	}
	public void setStartDatePbb(Date startDatePbb) {
		this.startDatePbb = startDatePbb;
	}
	public Date getEndDatePbb() {
		return endDatePbb;
	}
	public void setEndDatePbb(Date endDatePbb) {
		this.endDatePbb = endDatePbb;
	}
	public String getTahunPajak() {
		return tahunPajak;
	}
	public void setTahunPajak(String tahunPajak) {
		this.tahunPajak = tahunPajak;
	}
	public double getJumlahSetor() {
		return jumlahSetor;
	}
	public void setJumlahSetor(double jumlahSetor) {
		this.jumlahSetor = jumlahSetor;
	}
	public String getIdBilling() {
		return idBilling;
	}
	public void setIdBilling(String idBilling) {
		this.idBilling = idBilling;
	}
	public String getMasaAktifBilling() {
		return masaAktifBilling;
	}
	public void setMasaAktifBilling(String masaAktifBilling) {
		this.masaAktifBilling = masaAktifBilling;
	}
	public String getNpwpPenyetor() {
		return npwpPenyetor;
	}
	public void setNpwpPenyetor(String npwpPenyetor) {
		this.npwpPenyetor = npwpPenyetor;
	}
	public String getUraianSsp() {
		return uraianSsp;
	}
	public void setUraianSsp(String uraianSsp) {
		this.uraianSsp = uraianSsp;
	}
	public String getNoSk() {
		return noSk;
	}
	public void setNoSk(String noSk) {
		this.noSk = noSk;
	}
	public String getKodeKkpSsp() {
		return kodeKkpSsp;
	}
	public void setKodeKkpSsp(String kodeKkpSsp) {
		this.kodeKkpSsp = kodeKkpSsp;
	}
	public String getNop() {
		return nop;
	}
	public void setNop(String nop) {
		this.nop = nop;
	}
	public String getKotaWp() {
		return kotaWp;
	}
	public void setKotaWp(String kotaWp) {
		this.kotaWp = kotaWp;
	}
	public boolean isNonNpwp() {
		return isNonNpwp;
	}
	public void setNonNpwp(boolean isNonNpwp) {
		this.isNonNpwp = isNonNpwp;
	}
	public String getNik() {
		return nik;
	}
	public void setNik(String nik) {
		this.nik = nik;
	}
	public String getNpwpView() {
		npwpView = getNpwp();
		npwpView = CommonUtil.convertPlainToNpwpFormatToString(npwpView);
//		npwpView = npwpView.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{1})(\\d{3})(\\d{3})", "$1.$2.$3.$4-$5.$6");
		return npwpView;
	}
	public String getNpwpPenyetorView() {
		npwpPenyetorView = getNpwpPenyetor();
		npwpPenyetorView = CommonUtil.convertPlainToNpwpFormatToString(npwpPenyetorView);
//		npwpPenyetorView = npwpPenyetorView.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{1})(\\d{3})(\\d{3})", "$1.$2.$3.$4-$5.$6");
		return npwpPenyetorView;
	}
	public String getNopView() {
		nopView = getNop();
		nopView = CommonUtil.convertPlainToNopFormatToString(nopView);
		return nopView;
	}
	public String getNoSkView() {
		noSkView = getNoSk();
		noSkView = CommonUtil.convertPlainToNoSkFormatToString(noSkView);
		return noSkView;
	}
	public Date getMasaAktifBillingView() {
//		20210813162328
		String pattern = "yyyyMMddHHmmss";
		try {
			masaAktifBillingView = CommonUtil.strToDateTime(pattern, getMasaAktifBilling()) ;	
		} catch (Exception e) {
			masaAktifBillingView = new Date();
		}
		return masaAktifBillingView;
	}
	
	

}
