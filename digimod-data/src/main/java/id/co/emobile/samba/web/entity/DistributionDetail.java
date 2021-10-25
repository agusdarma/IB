package id.co.emobile.samba.web.entity;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import id.co.emobile.samba.web.utils.CommonUtil;

public class DistributionDetail implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private String sysLogNo; // syslogno VARCHAR(14) NOT NULL,
	private int detailId;  // detail_id INT NOT NULL,
	private String phoneNo;  // phone_no VARCHAR(50) NOT NULL,
	private String accountNo;  // account_no VARCHAR(50) NOT NULL,
	private String accountName;
	private String bankCode;
	private double moneyValue;  // money_value DECIMAL(12,2) NOT NULL,
	private String hostRefNo;  // host_ref_no VARCHAR(20),
	private String hostRc;  // host_rc VARCHAR(10),
	private String hostMessage;
	private int processStatus;  // process_status INT NOT NULL,
	private Date processStarted;  // process_started DATETIME,
	private Date processFinished;  // process_finished DATETIME,
	private String processRemarks;
	private String dataCheck;
	
	private String npwp;
	private String npwpView;
	private String npwpPenyetor;
	private String npwpPenyetorView;
	private String nama;
	private String alamat;
	private String kotaWp;
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
	private String idBilling;
	private String masaAktifBilling;
	private Date masaAktifBillingView;
	private String bit48;
	private String ntpn;
	private String nop;
	private String nopView;
	private String nik;
	private String ntb;
	private String stan;
	private String tanggalBuku;
	private String kodeCabang;
	private String tglJamBayar;
	private boolean isNonNpwp;
	
	// for display
	private String processStatusDisplay;
	
	// for report
	private String terbilangDisplayReport;
	private String statusDisplayReport;
	
	public String getSysLogNo() {
		return sysLogNo;
	}
	public void setSysLogNo(String sysLogNo) {
		this.sysLogNo = sysLogNo;
	}
	public int getDetailId() {
		return detailId;
	}
	public void setDetailId(int detailId) {
		this.detailId = detailId;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public double getMoneyValue() {
		return moneyValue;
	}
	public void setMoneyValue(double moneyValue) {
		this.moneyValue = moneyValue;
	}
	public String getHostRefNo() {
		return hostRefNo;
	}
	public void setHostRefNo(String hostRefNo) {
		this.hostRefNo = hostRefNo;
	}
	public String getHostRc() {
		return hostRc;
	}
	public void setHostRc(String hostRc) {
		this.hostRc = hostRc;
	}
	public String getHostMessage() {
		return hostMessage;
	}
	public void setHostMessage(String hostMessage) {
		this.hostMessage = hostMessage;
	}
	public int getProcessStatus() {
		return processStatus;
	}
	public void setProcessStatus(int processStatus) {
		this.processStatus = processStatus;
	}
	public Date getProcessStarted() {
		return processStarted;
	}
	public void setProcessStarted(Date processStarted) {
		this.processStarted = processStarted;
	}
	public Date getProcessFinished() {
		return processFinished;
	}
	public void setProcessFinished(Date processFinished) {
		this.processFinished = processFinished;
	}
	public String getProcessRemarks() {
		return processRemarks;
	}
	public void setProcessRemarks(String processRemarks) {
		this.processRemarks = processRemarks;
	}
	public String getDataCheck() {
		return dataCheck;
	}
	public void setDataCheck(String dataCheck) {
		this.dataCheck = dataCheck;
	}
	
	public String getProcessStatusDisplay() {
		return processStatusDisplay;
	}
	public void setProcessStatusDisplay(String processStatusDisplay) {
		this.processStatusDisplay = processStatusDisplay;
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
	public String getBit48() {
		return bit48;
	}
	public void setBit48(String bit48) {
		this.bit48 = bit48;
	}
	public String getNtpn() {
		return ntpn;
	}
	public void setNtpn(String ntpn) {
		this.ntpn = ntpn;
	}
	public String getNop() {
		return nop;
	}
	public void setNop(String nop) {
		this.nop = nop;
	}
	public String getNtb() {
		return ntb;
	}
	public void setNtb(String ntb) {
		this.ntb = ntb;
	}
	public String getStan() {
		return stan;
	}
	public void setStan(String stan) {
		this.stan = stan;
	}
	public String getTanggalBuku() {
		return tanggalBuku;
	}
	public void setTanggalBuku(String tanggalBuku) {
		this.tanggalBuku = tanggalBuku;
	}
	public String getKodeCabang() {
		return kodeCabang;
	}
	public void setKodeCabang(String kodeCabang) {
		this.kodeCabang = kodeCabang;
	}
	public String getTglJamBayar() {
		return tglJamBayar;
	}
	public void setTglJamBayar(String tglJamBayar) {
		this.tglJamBayar = tglJamBayar;
	}
	public String getTerbilangDisplayReport() {
		return terbilangDisplayReport;
	}
	public void setTerbilangDisplayReport(String terbilangDisplayReport) {
		this.terbilangDisplayReport = terbilangDisplayReport;
	}
	public String getStatusDisplayReport() {
		return statusDisplayReport;
	}
	public void setStatusDisplayReport(String statusDisplayReport) {
		this.statusDisplayReport = statusDisplayReport;
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
	
	
}
