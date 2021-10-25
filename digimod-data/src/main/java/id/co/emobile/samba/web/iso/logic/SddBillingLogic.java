package id.co.emobile.samba.web.iso.logic;

import java.io.IOException;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import id.co.emobile.samba.web.bti.SddBtiIsoDelegateAgent;
import id.co.emobile.samba.web.data.ResultCode;
import id.co.emobile.samba.web.data.StateConstant;
import id.co.emobile.samba.web.data.TermConstant;
import id.co.emobile.samba.web.data.TransactionTO;
import id.co.emobile.samba.web.iso.IsoMsg;
import id.co.emobile.samba.web.iso.SddIsoMsg;
import id.co.emobile.samba.web.iso.SultengIsoMsgFactory;
import id.co.emobile.samba.web.service.JetsException;

public class SddBillingLogic extends SddHostBtiLogic {
	private static final Logger LOG = LoggerFactory.getLogger(SddBillingLogic.class);

	@Override
	protected Logger getLogger() {
		return LOG;
	}
	
	@Override
	public void solve(SddBtiIsoDelegateAgent agent, TransactionTO task) 
			throws JetsException, IOException {
		LOG.debug("solve: " + task);
		super.solve(agent, task);

	}

	/*
	 * build IsoMsg for Fund Transfer settlement
	 * 
	 * @see com.emobile.common.iso.AbstractIsoLogic#buildIso(com.emobile.common.data.TransactionTO)
	 */
	@Override
	public SddIsoMsg buildIso(TransactionTO task) throws JetsException {
		if(StateConstant.FUND_TRANSFER_INQUIRY == task.getState()){
			return SultengIsoMsgFactory.sddBillingInq(this, task);
		}
		else if(StateConstant.FUND_TRANSFER_SETTLEMENT == task.getState()) {
			SddIsoMsg isoMsg = null;
//			if (task.getTermAsBoolean(TermConstant.REVERSAL)) {
//				isoMsg = SultengIsoMsgFactory.createList8AccountSdd(this, task);
//			} else {
				isoMsg = SultengIsoMsgFactory.sddBillingSett(this, task);
				LOG.debug("sddBillingSett ISO: " + isoMsg);
				
				//save for reversal
				task.setTerm(TermConstant.BIT7, isoMsg.getItem(IsoMsg.TIMESTAMP));
				task.setTerm(TermConstant.BIT11, isoMsg.getItem(IsoMsg.SYS_TRACE_NO));
				task.setTerm(TermConstant.BIT12, isoMsg.getItem(IsoMsg.TIME_LOCAL));
				task.setTerm(TermConstant.BIT32, isoMsg.getItem(IsoMsg.ACQUIRING_INSTITUTION_ID));
				task.setTerm(TermConstant.BIT37, isoMsg.getItem(IsoMsg.RETRIEVAL_REF_NO));
				task.setTerm(TermConstant.HOST_REF_BIT_37, isoMsg.getItem(IsoMsg.RETRIEVAL_REF_NO));
				task.setTerm(TermConstant.BIT48, isoMsg.getItem(IsoMsg.DATA_PRIVATE));
//			}
			return isoMsg;
		} else {
			LOG.error("Unknown state : " + task.getTermAsInteger(TermConstant.ISO_TYPE));
			throw new JetsException("Unknown state!", ResultCode.BTI_UNKNOWN_STATE);
		}
	}
	
	private String composeTanggalBuku(String tanggalBuku) {
		String newTanggalBuku = tanggalBuku;
		LOG.debug("tanggalBuku : " + tanggalBuku);
		try {
			Calendar now = Calendar.getInstance();
			int year = now.get(Calendar.YEAR);
			//MMdd
			String day = tanggalBuku.substring(2,4);
			String month = tanggalBuku.substring(0,2);
			String tahun = Integer.toString(year);
			LOG.debug("day : " + day);
			LOG.debug("month : " + month);
			LOG.debug("tahun : " + tahun);
			newTanggalBuku = day+month+tahun; // di format di action report
			LOG.debug("newTanggalBuku : " + newTanggalBuku);
		} catch (Exception e) {
			LOG.error("Exception composeTanggalBuku : " + e,e);
		}
		return newTanggalBuku;
	}
	
	private String composeTanggalJamBayar(String bit12,String bit13) {
		String newTanggalBayar = "";
		LOG.debug("bit12 : " + bit12);
		LOG.debug("bit13 : " + bit13);
		/*
		 * 012 : 160926 
		 * 013 : 1216
		 */
		try {
			Calendar now = Calendar.getInstance();
			int year = now.get(Calendar.YEAR);
			//MMdd
			String day = bit13.substring(2,4);
			String month = bit13.substring(0,2);
			String tahun = Integer.toString(year);
			String jam = bit12.substring(0,2);
			String menit = bit12.substring(2,4);
			String detik = bit12.substring(4,6);
			LOG.debug("day : " + day);
			LOG.debug("month : " + month);
			LOG.debug("tahun : " + tahun);
			LOG.debug("jam : " + jam);
			LOG.debug("menit : " + menit);
			LOG.debug("detik : " + detik);
			StringBuffer sb = new StringBuffer();
			sb.append(day);
			sb.append("/");
			sb.append(month);
			sb.append("/");
			sb.append(tahun);
			sb.append(" ");
			sb.append(jam);
			sb.append(":");
			sb.append(menit);
			sb.append(":");
			sb.append(detik);
			newTanggalBayar = sb.toString();	
			LOG.debug("newTanggalBayar : " + newTanggalBayar);
		} catch (Exception e) {
			LOG.error("Exception composeTanggalJamBayar : " + e,e);
		}
		return newTanggalBayar;
	}
	
	private String composeMasaPajak(String masaPajak) {
		String newMasaPajak = masaPajak;
		LOG.debug("masaPajak : " + masaPajak);
		try {
			String month = masaPajak.substring(2,4);
			String day = masaPajak.substring(0,2);
			String tahun = masaPajak.substring(4,8);
			LOG.debug("day : " + day);
			LOG.debug("month : " + month);
			LOG.debug("tahun : " + tahun);
			newMasaPajak = day+"/"+month+"/"+tahun;
			LOG.debug("newMasaPajak : " + newMasaPajak);
		} catch (Exception e) {
			LOG.error("Exception composeMasaPajak : " + e,e);
		}
		return newMasaPajak;
	}

	@Override
	protected void processSuccessResponse(TransactionTO task, SddIsoMsg rspIsoMsg)
			throws JetsException {
		try {
			if(StateConstant.FUND_TRANSFER_INQUIRY == task.getState()){
				String bit48 = rspIsoMsg.getItem(IsoMsg.DATA_PRIVATE);
				LOG.error(String.format("processSuccessResponse inq : %s", bit48));
				if(!StringUtils.isEmpty(bit48)){
					task.setTerm(TermConstant.BIT48, bit48);
					// 172342404075000BENNY DARMAWAN LAY                                JL POS UTR II NO 1 C RT  004 RW 001, PS BARU - KOT41112530006062020000771051107511
					int npwp = 15;
					int NamaWp = 50;
					int AlamatWp = 50;
					int Akun = 6;
					int KodeSetoran = 3;
					int MasaPajak = 8;
					int NoSk = 15;
					int NOP = 18;
					
					String npwpHasil = bit48.substring(0, npwp);
					String namaHasil = bit48.substring(npwp, NamaWp+ npwp);
					String alamatHasil = bit48.substring(NamaWp+ npwp,AlamatWp+ NamaWp+ npwp);
					String akunHasil = bit48.substring(AlamatWp+ NamaWp+ npwp , Akun + AlamatWp+ NamaWp+ npwp);
					String kodeSetoranHasil = bit48.substring(Akun + AlamatWp+ NamaWp+ npwp , KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp);
					String masaPajakHasil = bit48.substring(KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp , MasaPajak + KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp);
					String noSkHasil = bit48.substring(MasaPajak + KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp , NoSk + MasaPajak + KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp);
					String NOPHasil = bit48.substring(NoSk + MasaPajak + KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp , NOP + NoSk + MasaPajak + KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp);
					task.setTerm(TermConstant.NAMA_WP, namaHasil);
					task.setTerm(TermConstant.ALAMAT_WP, alamatHasil);
					task.setTerm(TermConstant.MASA_KADALUARSA_BILLING, masaPajakHasil);
					
				}			
			}else if(StateConstant.FUND_TRANSFER_SETTLEMENT == task.getState()) {
				String bit48 = rspIsoMsg.getItem(IsoMsg.DATA_PRIVATE);
				String bit12 = rspIsoMsg.getItem(IsoMsg.TIME_LOCAL);
				String bit13 = rspIsoMsg.getItem(IsoMsg.DATE_LOCAL);
				/*
				 * 012 : 160926 
				 * 013 : 1216
				 */
				
				String ntb = rspIsoMsg.getItem(IsoMsg.RETRIEVAL_REF_NO); // NTB
				String stan = rspIsoMsg.getItem(IsoMsg.SYS_TRACE_NO); //STAN
				String kodeCabang = rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA_4); //KodeCabang
//				String tanggalBuku = rspIsoMsg.getItem(IsoMsg.SETTLEMENT_DATE); //TanggalBuku
				String tanggalBuku = composeTanggalBuku(rspIsoMsg.getItem(IsoMsg.SETTLEMENT_DATE)); //TanggalBuku
				String tanggalBayar = composeTanggalJamBayar(bit12, bit13); //tanggalBayar
				
				
				LOG.info(String.format("processSuccessResponse sett : %s", bit48));
				if(!StringUtils.isEmpty(bit48)){
					task.setTerm(TermConstant.BIT48, bit48);
					// 172342404075000BENNY DARMAWAN LAY                                JL POS UTR II NO 1 C RT  004 RW 001, PS BARU - KOT41112530006062020000771051107511
					// 172342404075000WP OP SK                                          JL. INI ALAMATNYA - KOTA ADM. JAKARTA PUSAT       41112530011112020000771051107511                  
					int npwp = 15;
					int NamaWp = 50;
					int AlamatWp = 50;
					int Akun = 6;
					int KodeSetoran = 3;
					int MasaPajak = 8;
					int NoSk = 15;
					int NOP = 18;
					int ntpn = 16;
					String npwpHasil = bit48.substring(0, npwp);
					String namaHasil = bit48.substring(npwp, NamaWp+ npwp);
					String alamatHasil = bit48.substring(NamaWp+ npwp,AlamatWp+ NamaWp+ npwp);
					String akunHasil = bit48.substring(AlamatWp+ NamaWp+ npwp , Akun + AlamatWp+ NamaWp+ npwp);
					String kodeSetoranHasil = bit48.substring(Akun + AlamatWp+ NamaWp+ npwp , KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp);
					String masaPajakHasil = bit48.substring(KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp , MasaPajak + KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp);
					String noSkHasil = bit48.substring(MasaPajak + KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp , NoSk + MasaPajak + KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp);
					String NOPHasil = bit48.substring(NoSk + MasaPajak + KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp , NOP + NoSk + MasaPajak + KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp);
					String ntpnHasil = bit48.substring(NOP+ NoSk + MasaPajak + KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp , ntpn + NOP + NoSk + MasaPajak + KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp);
					task.setTerm(TermConstant.NOP, NOPHasil);					
					task.setTerm(TermConstant.NTPN, ntpnHasil);
					
					task.setTerm(TermConstant.NTB, ntb);
					task.setTerm(TermConstant.STAN, stan);
					task.setTerm(TermConstant.MASA_PAJAK, composeMasaPajak(masaPajakHasil));
					task.setTerm(TermConstant.ALAMAT_WP, alamatHasil);
					task.setTerm(TermConstant.TANGGAL_BUKU, tanggalBuku);
					task.setTerm(TermConstant.TANGGAL_JAM_BAYAR, tanggalBayar);
					task.setTerm(TermConstant.KODE_CABANG, kodeCabang);
					LOG.info("alamatHasil bit 48: " + alamatHasil);
				}			
			}
			
			
		} catch (Exception e) {
			LOG.error("Unable to parse bit 48: " + e , e);
			throw new JetsException(ResultCode.BTI_ERROR_PARSE_ISO);
		}
	}
	
	@Override
	protected void processFailedResponse(TransactionTO task, SddIsoMsg rspIsoMsg)
			throws JetsException {
		try {
			if(StateConstant.FUND_TRANSFER_INQUIRY == task.getState()){
				String bit48 = rspIsoMsg.getItem(IsoMsg.DATA_PRIVATE);
				LOG.error(String.format("processFailedResponse inq : %s", bit48));
				if(!StringUtils.isEmpty(bit48)){
					task.setTerm(TermConstant.BIT48, bit48);
					// 172342404075000BENNY DARMAWAN LAY                                JL POS UTR II NO 1 C RT  004 RW 001, PS BARU - KOT41112530006062020000771051107511
					int npwp = 15;
					int NamaWp = 50;
					int AlamatWp = 50;
					int Akun = 6;
					int KodeSetoran = 3;
					int MasaPajak = 8;
					int NoSk = 15;
					int NOP = 18;
					
					String npwpHasil = bit48.substring(0, npwp);
					String namaHasil = bit48.substring(npwp, NamaWp+ npwp);
					String alamatHasil = bit48.substring(NamaWp+ npwp,AlamatWp+ NamaWp+ npwp);
					String akunHasil = bit48.substring(AlamatWp+ NamaWp+ npwp , Akun + AlamatWp+ NamaWp+ npwp);
					String kodeSetoranHasil = bit48.substring(Akun + AlamatWp+ NamaWp+ npwp , KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp);
					String masaPajakHasil = bit48.substring(KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp , MasaPajak + KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp);
					String noSkHasil = bit48.substring(MasaPajak + KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp , NoSk + MasaPajak + KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp);
					String NOPHasil = bit48.substring(NoSk + MasaPajak + KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp , NOP + NoSk + MasaPajak + KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp);
					task.setTerm(TermConstant.NAMA_WP, namaHasil);
					task.setTerm(TermConstant.ALAMAT_WP, alamatHasil);
					task.setTerm(TermConstant.MASA_KADALUARSA_BILLING, masaPajakHasil);
					
				}			
			}else if(StateConstant.FUND_TRANSFER_SETTLEMENT == task.getState()) {
				String bit48 = rspIsoMsg.getItem(IsoMsg.DATA_PRIVATE);
				String bit12 = rspIsoMsg.getItem(IsoMsg.TIME_LOCAL);
				String bit13 = rspIsoMsg.getItem(IsoMsg.DATE_LOCAL);
				
				String ntb = rspIsoMsg.getItem(IsoMsg.RETRIEVAL_REF_NO); // NTB
				String stan = rspIsoMsg.getItem(IsoMsg.SYS_TRACE_NO); //STAN
				String kodeCabang = rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA_4); //KodeCabang
//				String tanggalBuku = rspIsoMsg.getItem(IsoMsg.SETTLEMENT_DATE); //TanggalBuku
				String tanggalBuku = composeTanggalBuku(rspIsoMsg.getItem(IsoMsg.SETTLEMENT_DATE)); //TanggalBuku
				String tanggalBayar = composeTanggalJamBayar(bit12, bit13); //tanggalBayar
				
				LOG.info(String.format("processFailedResponse sett : %s", bit48));
				if(!StringUtils.isEmpty(bit48)){
					task.setTerm(TermConstant.BIT48, bit48);
					// 172342404075000BENNY DARMAWAN LAY                                JL POS UTR II NO 1 C RT  004 RW 001, PS BARU - KOT41112530006062020000771051107511
					// 172342404075000WP OP SK                                          JL. INI ALAMATNYA - KOTA ADM. JAKARTA PUSAT       41112530011112020000771051107511                  
					int npwp = 15;
					int NamaWp = 50;
					int AlamatWp = 50;
					int Akun = 6;
					int KodeSetoran = 3;
					int MasaPajak = 8;
					int NoSk = 15;
					int NOP = 18;
					int ntpn = 16;
					String npwpHasil = bit48.substring(0, npwp);
					String namaHasil = bit48.substring(npwp, NamaWp+ npwp);
					String alamatHasil = bit48.substring(NamaWp+ npwp,AlamatWp+ NamaWp+ npwp);
					String akunHasil = bit48.substring(AlamatWp+ NamaWp+ npwp , Akun + AlamatWp+ NamaWp+ npwp);
					String kodeSetoranHasil = bit48.substring(Akun + AlamatWp+ NamaWp+ npwp , KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp);
					String masaPajakHasil = bit48.substring(KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp , MasaPajak + KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp);
					String noSkHasil = bit48.substring(MasaPajak + KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp , NoSk + MasaPajak + KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp);
					String NOPHasil = bit48.substring(NoSk + MasaPajak + KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp , NOP + NoSk + MasaPajak + KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp);
//					String ntpnHasil = bit48.substring(NOP+ NoSk + MasaPajak + KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp , ntpn + NOP + NoSk + MasaPajak + KodeSetoran + Akun + AlamatWp+ NamaWp+ npwp);
					task.setTerm(TermConstant.NOP, NOPHasil);					
//					task.setTerm(TermConstant.NTPN, ntpnHasil);
					
					task.setTerm(TermConstant.NTB, ntb);
					task.setTerm(TermConstant.STAN, stan);
					task.setTerm(TermConstant.MASA_PAJAK, composeMasaPajak(masaPajakHasil));
					task.setTerm(TermConstant.TANGGAL_BUKU, tanggalBuku);
					task.setTerm(TermConstant.TANGGAL_JAM_BAYAR, tanggalBayar);
					task.setTerm(TermConstant.KODE_CABANG, kodeCabang);
					task.setTerm(TermConstant.ALAMAT_WP, alamatHasil);
					LOG.info("NOPHasil bit 48: " + NOPHasil);
					LOG.info("ntb bit 48: " + ntb);
					LOG.info("alamatHasil bit 48: " + alamatHasil);
					LOG.info("tanggalBuku bit 48: " + tanggalBuku);
					LOG.info("kodeCabang bit 48: " + kodeCabang);
				}			
			}
			
			
		} catch (Exception e) {
			LOG.error("Unable to parse bit 48: " + e , e);
			throw new JetsException(ResultCode.BTI_ERROR_PARSE_ISO);
		}
	}


}
