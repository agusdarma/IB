package id.co.emobile.samba.web.iso;

import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import id.co.emobile.samba.web.data.JetsConstant;
import id.co.emobile.samba.web.data.ResultCode;
import id.co.emobile.samba.web.data.TermConstant;
import id.co.emobile.samba.web.data.TransactionTO;
import id.co.emobile.samba.web.iso.helper.SddNetworkInfoConstant;
import id.co.emobile.samba.web.iso.logic.DjpHostBtiLogic;
import id.co.emobile.samba.web.iso.logic.HostBtiLogic;
import id.co.emobile.samba.web.iso.logic.SddHostBtiLogic;
import id.co.emobile.samba.web.service.JetsException;
import id.co.emobile.samba.web.utils.CommonUtil;

public class SultengIsoMsgFactory {
	private static final Logger logger = LoggerFactory.getLogger(SultengIsoMsgFactory.class);

	// for bit49, bit61 always use IDR
	protected static final String LOCAL_CURR_BIT49 = "360";

	private static final String MERCHANT_TYPE = "6019";
	private static final String MERCHANT_TYPE_ATMB = "6017";
	private static final String MERCHANT_TYPE_CREATE_ID_BILLING = "7012";
	private static final String MERCHANT_TYPE_SDD = "7012";
	private static final String MERCHANT_TYPE_ANTAR_BANK_KNT = "6014";

	public static IsoMsg createHostIsoMsg() {
		return new SultengIsoMsg(new SultengIsoMsgHeader());
	}

	public static IsoMsg createHostIsoMsg(String msgType) {
		return new SultengIsoMsg(new SultengIsoMsgHeader(msgType));
	}

	public static DjpIsoMsg createHostIsoMsgDjp() {
		return new DjpSultengIsoMsg(new DjpSultengIsoMsgHeader());
	}

	public static DjpIsoMsg createHostIsoMsgDjp(String msgType) {
		return new DjpSultengIsoMsg(new DjpSultengIsoMsgHeader(msgType));
	}

	public static SddIsoMsg createHostIsoMsgSdd() {
		return new SddSultengIsoMsg(new SddSultengIsoMsgHeader());
	}

	public static SddIsoMsg createHostIsoMsgSdd(String msgType) {
		return new SddSultengIsoMsg(new SddSultengIsoMsgHeader(msgType));
	}

	public static String formatPAN(String phoneNo) {
		Assert.notNull(phoneNo, "phoneNo is null!");
		if (phoneNo.startsWith("+")) {
			return phoneNo.substring(1);
		}
		return phoneNo;
	}

	public static IsoMsg commonIsoMsg(HostBtiLogic logic, TransactionTO task, IsoMsg isoMsg) throws JetsException {
		try {
//			isoMsg.setItemWithLength(IsoMsg.PAN, CommonUtil.padRight(task.getTermAsString(TermConstant.CARD_NUMBER), 19), 2); // bit2
			isoMsg.setItemWithLength(IsoMsg.PAN, task.getTermAsString(TermConstant.CARD_NUMBER), 2); // bit2
			isoMsg.setItem(IsoMsg.TIMESTAMP, ISODate.getDateTime(new Date(), TimeZone.getTimeZone("GMT"))); // bit7
//			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
			isoMsg.setItem(IsoMsg.SETTLEMENT_DATE, ISODate.getDate(CommonUtil.getNextDay(new Date()))); // bit15
			isoMsg.setItem(IsoMsg.MERCHANT_TYPE, MERCHANT_TYPE); // bit18
			isoMsg.setItemWithLength(IsoMsg.ACQUIRING_INSTITUTION_ID, logic.getAcquirerId(), 2); // bit32
//			isoMsg.setItem(IsoMsg.RETRIEVAL_REF_NO, logic.getNextSequenceRefNumber()); // bit37
			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
			isoMsg.setItem(IsoMsg.CARD_ACCEPTOR_NAME_LOCATION, "MOBILE BANKING BANK SULTENG      SLH IDN"); // bit43
			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static DjpIsoMsg commonIsoMsgDjp(DjpHostBtiLogic logic, TransactionTO task, DjpIsoMsg isoMsg)
			throws JetsException {
		try {
//			isoMsg.setItemWithLength(DjpIsoMsg.PAN, CommonUtil.padRight(task.getTermAsString(TermConstant.CARD_NUMBER), 19), 2); // bit2
//			isoMsg.setItemWithLength(DjpIsoMsg.PAN, task.getTermAsString(TermConstant.CARD_NUMBER), 2); // bit2
			isoMsg.setItemWithLength(DjpIsoMsg.PAN, "0000000000000000", 2); // bit2
			isoMsg.setItem(DjpIsoMsg.TIMESTAMP, ISODate.getDateTime(new Date(), TimeZone.getTimeZone("GMT"))); // bit7
//			isoMsg.setItem(DjpIsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(DjpIsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
			isoMsg.setItem(DjpIsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
			isoMsg.setItem(DjpIsoMsg.SETTLEMENT_DATE, ISODate.getDate(CommonUtil.getNextDay(new Date()))); // bit15
			isoMsg.setItem(DjpIsoMsg.MERCHANT_TYPE, MERCHANT_TYPE_CREATE_ID_BILLING); // bit18
//			isoMsg.setItemWithLength(DjpIsoMsg.ACQUIRING_INSTITUTION_ID, logic.getAcquirerId(), 2); // bit32
			isoMsg.setItemWithLength(DjpIsoMsg.ACQUIRING_INSTITUTION_ID, "0134", 2); // bit32

//			isoMsg.setItem(DjpIsoMsg.RETRIEVAL_REF_NO, logic.getNextSequenceRefNumber()); // bit37
//			isoMsg.setItemWithPad(DjpIsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
//			isoMsg.setItem(DjpIsoMsg.CARD_ACCEPTOR_NAME_LOCATION, "MOBILE BANKING BANK SULTENG      SLH IDN"); // bit43
			isoMsg.setItem(DjpIsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static SddIsoMsg commonIsoMsgSdd(SddHostBtiLogic logic, TransactionTO task, SddIsoMsg isoMsg)
			throws JetsException {
		try {
			isoMsg.setItemWithLength(SddIsoMsg.PAN, "0000000000000000", 2); // bit2
//			isoMsg.setItem(SddIsoMsg.PAN, "1111222233334444"); // bit2

			isoMsg.setItem(SddIsoMsg.TIMESTAMP, ISODate.getDateTime(new Date(), TimeZone.getTimeZone("GMT"))); // bit7
			isoMsg.setItem(SddIsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
			isoMsg.setItem(SddIsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
			isoMsg.setItem(SddIsoMsg.SETTLEMENT_DATE, ISODate.getDate(CommonUtil.getNextDay(new Date()))); // bit15
//			isoMsg.setItem(SddIsoMsg.ACQUIRING_INSTITUTION_ID, "123456789123"); // bit32
			isoMsg.setItemWithLength(SddIsoMsg.ACQUIRING_INSTITUTION_ID, "524134000990", 2); // bit32
//			isoMsg.setItem(SddIsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
			isoMsg.setItem(SddIsoMsg.CURR_CODE, "IDR"); // bit49
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createHostNetworkManagement(HostBtiLogic logic, String networkInfo) throws JetsException {
		try {
			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.NETWORK_MANAGEMENT_REQUEST);
			isoMsg.setItem(IsoMsg.TIMESTAMP, ISODate.getDateTime(new Date())); // bit7
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.NETWORK_INFO_CODE, networkInfo); // bit70
			return isoMsg;
		} catch (Exception e) {
			logger.warn("Exception for " + networkInfo, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg responseHostNetworkManagement(String sysTraceNo, String networkInfo) throws JetsException {
		try {
			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.NETWORK_MANAGEMENT_RESPONSE);
			isoMsg.setItem(IsoMsg.TIMESTAMP, ISODate.getDateTime(new Date())); // bit7
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, sysTraceNo); // bit11
			isoMsg.setItem(IsoMsg.RESPONSE_CODE, "00"); // bit39
			isoMsg.setItem(IsoMsg.NETWORK_INFO_CODE, networkInfo); // bit70
			return isoMsg;
		} catch (Exception e) {
			logger.warn("Exception for " + networkInfo, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static DjpIsoMsg createHostNetworkManagementDjp(DjpHostBtiLogic logic, String networkInfo)
			throws JetsException {
		try {
			DjpIsoMsg isoMsg = (DjpIsoMsg) SultengIsoMsgFactory
					.createHostIsoMsgDjp(IsoMsgHeader.NETWORK_MANAGEMENT_REQUEST);
			isoMsg.setItem(IsoMsg.TIMESTAMP, ISODate.getDateTime(new Date())); // bit7
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.NETWORK_INFO_CODE, networkInfo); // bit70
			return isoMsg;
		} catch (Exception e) {
			logger.warn("Exception for " + networkInfo, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static SddIsoMsg createHostNetworkManagementSdd(SddHostBtiLogic logic, String networkInfo,String bit47,String bit48)
			throws JetsException {
		try {
			logger.warn("createHostNetworkManagementSdd  network info: " + networkInfo);
			SddIsoMsg isoMsg = (SddIsoMsg) SultengIsoMsgFactory
					.createHostIsoMsgSdd(IsoMsgHeader.NETWORK_MANAGEMENT_REQUEST);
			isoMsg.setItem(IsoMsg.TIMESTAMP, ISODate.getDateTime(new Date())); // bit7
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.SETTLEMENT_DATE, ISODate.getDate(new Date())); // bit15
			if (SddNetworkInfoConstant.SIGN_ON.equals(networkInfo)) {
//				isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_5, "00627", 3); // bit47
				isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_5, bit47, 3); // bit47
//				isoMsg.setItemWithLength(IsoMsg.DATA_PRIVATE, "jDfmTl/nEFEIh2RsfsvluQ==", 3); // bit48	
//				isoMsg.setItemWithLength(IsoMsg.DATA_PRIVATE, "prlG55B+BsvfPFLJkBkMLUW5Zd8oUTTM", 3); // bit48
				isoMsg.setItemWithLength(IsoMsg.DATA_PRIVATE, bit48, 3); // bit48

			}
			isoMsg.setItemWithPad(IsoMsg.NETWORK_INFO_CODE, networkInfo, 3); // bit70
			return isoMsg;
		} catch (Exception e) {
			logger.warn("Exception for " + networkInfo, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg responseHostNetworkManagementSdd(String networkInfo, String sysTraceNo) throws JetsException {
		try {
			logger.warn("responseHostNetworkManagementSdd  network info: " + networkInfo);
			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.NETWORK_MANAGEMENT_RESPONSE);
			isoMsg.setItem(IsoMsg.TIMESTAMP, ISODate.getDateTime(new Date())); // bit7
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, sysTraceNo); // bit11
			isoMsg.setItem(IsoMsg.SETTLEMENT_DATE, ISODate.getDate(new Date())); // bit15
			isoMsg.setItem(IsoMsg.RESPONSE_CODE, "00"); // bit39
			isoMsg.setItemWithPad(IsoMsg.NETWORK_INFO_CODE, networkInfo, 3); // bit70
			return isoMsg;
		} catch (Exception e) {
			logger.warn("Exception for " + networkInfo, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createRegisOnline(HostBtiLogic logic, TransactionTO task) throws JetsException {
		try {
			String bit61 = CommonUtil.zeroPadLeft(task.getPhoneNo(), 13)
					+ CommonUtil.padRight(task.getTermAsString(TermConstant.SRAC), ' ', 14);

			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST); // bit
																														// 1
			isoMsg = commonIsoMsg(logic, task, isoMsg);
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "380000"); // bit3
			String amnt = IsoUtil.formatCurrency("0", 12, 0);
			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt); // bit4
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.RETRIEVAL_REF_NO, logic.getNextSequenceRefNumber()); // bit37
			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, task.getTermAsString(TermConstant.PIN), 16); // bit52
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, bit61, 3); // bit61
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_4, "01001", 3); // bit63
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static DjpIsoMsg createIdBillingDJP(DjpHostBtiLogic logic, TransactionTO task) throws JetsException {
		try {

			String bit48 = CommonUtil.padRight(task.getTermAsString(TermConstant.NPWP_SSP), ' ', 9)
//					 + CommonUtil.padRight(task.getTermAsString(TermConstant.KODE_KPP_SSP), ' ', 3)
//					 + CommonUtil.padRight(task.getTermAsString(TermConstant.KODE_CABANG), ' ', 3) 
					+ CommonUtil.padRight(task.getTermAsString(TermConstant.KODE_MAP), ' ', 6)
					+ CommonUtil.padRight(task.getTermAsString(TermConstant.KODE_JENIS_SETOR), ' ', 3)
					+ CommonUtil.padRight(task.getTermAsString(TermConstant.MASA_PAJAK_1), ' ', 2)
					+ CommonUtil.padRight(task.getTermAsString(TermConstant.MASA_PAJAK_2), ' ', 2)
					+ CommonUtil.padRight(task.getTermAsString(TermConstant.TAHUN_PAJAK), ' ', 4)
					+ CommonUtil.padRight(task.getTermAsString(TermConstant.NO_SK), ' ', 15)
					+ CommonUtil.padRight(task.getTermAsString(TermConstant.NPWP_PENYETOR), ' ', 15);
			logger.debug("createIdBillingDJP bit48 : " + bit48);
			/*
			 * 01-03: (3) panjang data (berisi 062). 04-12: (9) berisi *NPWP-SSP (contoh:
			 * 012345678) 13-15: (3) berisi *Kode KPP-SSP (contoh: 001) 16-18: (3) berisi
			 * *kode cabang WP-SSP (contoh: 000 untuk kantor pusat) 19-24: (6) kode MAP
			 * transaksi (contoh: 411121). 25-27: (3) kode jenis setoran (contoh : 110)
			 * 28-29: (2) Masa pajak 1. 30-31: (2) Masa Pajak 2. 32-35: (4) Tahun Pajak.
			 * 36-50: (15) Nomor SK. 51-65: (15) *NPWP Penyetor
			 */

			String bit62 = task.getTermAsString(TermConstant.URAIAN_SSP);
			logger.debug("createIdBillingDJP bit62 : " + bit62);
			String bit47 = task.getTermAsString(TermConstant.NOP);
			logger.debug("createIdBillingDJP bit47 : " + bit47);
			DjpIsoMsg isoMsg = (DjpIsoMsg) SultengIsoMsgFactory
					.createHostIsoMsgDjp(DjpIsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST); // bit 1
			isoMsg = commonIsoMsgDjp(logic, task, isoMsg);
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "400200"); // bit3

			logger.debug("NPWP_PENYETOR : " + task.getTermAsString(TermConstant.NPWP_PENYETOR));
			logger.debug("NPWP_SSP : " + task.getTermAsString(TermConstant.NPWP_SSP));
			logger.debug("IS_NON_NPWP : " + task.getTermAsString(TermConstant.IS_NON_NPWP));
			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, "0000000000000000", 16); // bit41

			if ("1".equals(task.getTerm(TermConstant.IS_NON_NPWP))) {
				isoMsg.setItem(IsoMsg.PROCESSING_CODE, "400201"); // bit3

				/*
				 * 004-033: (30) Nama WP. 034-083: (50) Alamat WP. 084-113: (30) Kota WP.
				 * 114-122: (9) berisi NPWP (contoh: 000000000) 123-125: (3) berisi Kode KPP
				 * (contoh: 001) 126-128: (3) berisi kode cabang WP (contoh: 000) 129-134: (6)
				 * kode MAP transaksi (contoh: 411121). 135-137: (3) kode jenis setoran (contoh
				 * : 110) 138-139: (2) Masa pajak 1. 140-141: (2) Masa Pajak 2. 142-145: (4)
				 * Tahun Pajak. 146-160: (15) Nomor SK. 161-175: (15) **NPWP Penyetor.
				 */

				bit48 = CommonUtil.padRight(task.getTermAsString(TermConstant.NAMA_WP), ' ', 30)
						+ CommonUtil.padRight(task.getTermAsString(TermConstant.ALAMAT_WP), ' ', 50)
						+ CommonUtil.padRight(task.getTermAsString(TermConstant.KOTA_WP), ' ', 30)
						+ CommonUtil.padRight(task.getTermAsString(TermConstant.NPWP_SSP), ' ', 15)
						+ CommonUtil.padRight(task.getTermAsString(TermConstant.KODE_MAP), ' ', 6)
						+ CommonUtil.padRight(task.getTermAsString(TermConstant.KODE_JENIS_SETOR), ' ', 3)
						+ CommonUtil.padRight(task.getTermAsString(TermConstant.MASA_PAJAK_1), ' ', 2)
						+ CommonUtil.padRight(task.getTermAsString(TermConstant.MASA_PAJAK_2), ' ', 2)
						+ CommonUtil.padRight(task.getTermAsString(TermConstant.TAHUN_PAJAK), ' ', 4)
						+ CommonUtil.padRight(task.getTermAsString(TermConstant.NO_SK), ' ', 15)
						+ CommonUtil.padRight(task.getTermAsString(TermConstant.NPWP_PENYETOR), ' ', 15);
				logger.debug("createIdBillingDJP bit48 NON NPWP : " + bit48);
				
				isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, task.getTermAsString(TermConstant.NIK), 16); // bit41
				logger.debug("createIdBillingDJP bit48 NON NPWP NIK : " + task.getTermAsString(TermConstant.NIK));

			}

			String amnt = IsoUtil.formatCurrency(task.getTermAsString(TermConstant.JUMLAH_SETOR), 12, 0);
			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt); // bit4
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.RETRIEVAL_REF_NO, logic.getNextSequenceRefNumber()); // bit37
			
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_5, bit47, 3); // bit47
			isoMsg.setItemWithLength(IsoMsg.DATA_PRIVATE, bit48, 3); // bit48
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_2, bit62, 3); // bit62
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_4, "000134", 3); // bit63
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static SddIsoMsg sddBillingInq(SddHostBtiLogic logic, TransactionTO task) throws JetsException {
		try {
			logger.debug("sddBillingInq UserCode : " + task.getTerm(TermConstant.LOGIN_USER));
			logger.debug("sddBillingInq BranchCode : " + task.getTerm(TermConstant.BRANCH_CODE));
			SddIsoMsg isoMsg = (SddIsoMsg) SultengIsoMsgFactory
					.createHostIsoMsgSdd(SddIsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST); // bit 1
			isoMsg = commonIsoMsgSdd(logic, task, isoMsg);
			isoMsg.setItem(SddIsoMsg.PROCESSING_CODE, "301100"); // bit3
//			String amnt = IsoUtil.formatCurrency(task.getTermAsString(TermConstant.JUMLAH_SETOR), 12, 0);
			String amnt = "000000000000";
			isoMsg.setItem(SddIsoMsg.TRANSACTION_AMT, amnt); // bit4
			isoMsg.setItem(SddIsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(SddIsoMsg.MERCHANT_TYPE, MERCHANT_TYPE_SDD); // bit18
			isoMsg.setItem(SddIsoMsg.RETRIEVAL_REF_NO, logic.getNextSequenceRefNumber()); // bit37
//			isoMsg.setItemWithPad(SddIsoMsg.TERMINAL_ID, "00095", 16); // bit41
			isoMsg.setItemWithPad(SddIsoMsg.TERMINAL_ID, task.getTerm(TermConstant.LOGIN_USER), 16); // bit41
			isoMsg.setItem(SddIsoMsg.CARD_ACCEPTOR_NAME_LOCATION, "Cabang Palu Barat                JKT IDN"); // bit43
//			isoMsg.setItemWithLength(SddIsoMsg.ADDITIONAL_DATA_5, task.getTerm(TermConstant.ID_BILLING),3); // bit47
			isoMsg.setItemWithLength(SddIsoMsg.ADDITIONAL_DATA_5, task.getTerm(TermConstant.ID_BILLING), 3); // bit47
//			isoMsg.setItem(SddIsoMsg.ADDITIONAL_DATA_5, "223816701479088"); // bit47
			isoMsg.setItemWithLength(SddIsoMsg.DATA_PRIVATE, task.getTerm(TermConstant.ID_BILLING), 3); // bit48
//			isoMsg.setItemWithLength(SddIsoMsg.ADDITIONAL_DATA_4, "000001", 3); // bit63
			isoMsg.setItemWithLength(SddIsoMsg.ADDITIONAL_DATA_4, CommonUtil.zeroPadLeft(task.getTerm(TermConstant.BRANCH_CODE), 6), 3); // bit63

			logger.debug("sddBillingInq bit7 : " + isoMsg.getItem(SddIsoMsg.TIMESTAMP));
			logger.debug("sddBillingInq bit12 : " + isoMsg.getItem(SddIsoMsg.TIME_LOCAL));

			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static SddIsoMsg sddBillingSett(SddHostBtiLogic logic, TransactionTO task) throws JetsException {
		try {

			String srac = CommonUtil.cutOrPad(task.getTerm(TermConstant.SRAC), 14);
			String temp = CommonUtil.cutOrPad("00142", 6);
//			String bit62 = srac + temp;
			String bit62 = srac + CommonUtil.cutOrPad(task.getTerm(TermConstant.LOGIN_USER),6);
			logger.debug("sddBillingSett bit62 : " + bit62);
			logger.debug("sddBillingSett UserCode : " + task.getTerm(TermConstant.LOGIN_USER));
			logger.debug("sddBillingSett BranchCode : " + task.getTerm(TermConstant.BRANCH_CODE));
			SddIsoMsg isoMsg = (SddIsoMsg) SultengIsoMsgFactory
					.createHostIsoMsgSdd(SddIsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST); // bit 1
			isoMsg = commonIsoMsgSdd(logic, task, isoMsg);
			isoMsg.setItemWithLength(SddIsoMsg.PAN, "0010101146061", 2); // bit2
			isoMsg.setItem(SddIsoMsg.PROCESSING_CODE, "500100"); // bit3
			String amnt = IsoUtil.formatCurrency(task.getTermAsString(TermConstant.JUMLAH_SETOR), 12, 0);
//			String amnt = "000000000000";
			isoMsg.setItem(SddIsoMsg.TRANSACTION_AMT, amnt); // bit4
			isoMsg.setItem(SddIsoMsg.TIMESTAMP, ISODate.getDateTime(new Date(), TimeZone.getTimeZone("GMT"))); // bit7
			isoMsg.setItem(SddIsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(SddIsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
			isoMsg.setItem(SddIsoMsg.MERCHANT_TYPE, MERCHANT_TYPE_SDD); // bit18
			isoMsg.setItem(SddIsoMsg.RETRIEVAL_REF_NO, logic.getNextSequenceRefNumber()); // bit37
//			isoMsg.setItemWithPad(SddIsoMsg.TERMINAL_ID, "00095", 16); // bit41
			isoMsg.setItemWithPad(SddIsoMsg.TERMINAL_ID, task.getTerm(TermConstant.LOGIN_USER), 16); // bit41
			isoMsg.setItem(SddIsoMsg.CARD_ACCEPTOR_NAME_LOCATION, "Cabang Palu Barat                JKT IDN"); // bit43
//			isoMsg.setItemWithLengthNew(IsoMsg.ADDITIONAL_DATA_5, "", 3); // bit47
			isoMsg.setItemWithLength(SddIsoMsg.ADDITIONAL_DATA_5, task.getTerm(TermConstant.ID_BILLING), 3); // bit47
//			isoMsg.setItemWithLength(IsoMsg.DATA_PRIVATE, bit48, 3); // bit48
			isoMsg.setItemWithLength(SddIsoMsg.DATA_PRIVATE, task.getTerm(TermConstant.BIT48), 3); // bit48

			isoMsg.setItemWithLength(SddIsoMsg.ADDITIONAL_DATA_2, bit62, 3); // bit62
//			isoMsg.setItemWithLength(SddIsoMsg.ADDITIONAL_DATA_4, "000001", 3); // bit63
			isoMsg.setItemWithLength(SddIsoMsg.ADDITIONAL_DATA_4, CommonUtil.zeroPadLeft(task.getTerm(TermConstant.BRANCH_CODE), 6), 3); // bit63

			logger.debug("sddBillingSett bit7 : " + isoMsg.getItem(SddIsoMsg.TIMESTAMP));
			logger.debug("sddBillingSett bit12 : " + isoMsg.getItem(SddIsoMsg.TIME_LOCAL));
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createRegisSmsNotif(HostBtiLogic logic, TransactionTO task) throws JetsException {
		try {
			String amountBlast = IsoUtil.formatCurrency(task.getTermAsDouble(TermConstant.AMOUNT_SMS_NOTIF), 15, 0);
			String bit61 = CommonUtil.zeroPadLeft(task.getPhoneNo(), 13)
					+ CommonUtil.padRight(task.getTermAsString(TermConstant.SRAC), ' ', 14) + amountBlast + amountBlast;

			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST); // bit
																														// 1
			isoMsg = commonIsoMsg(logic, task, isoMsg);
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "380000"); // bit3
			String amnt = IsoUtil.formatCurrency("0", 12, 0);
			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt); // bit4
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.RETRIEVAL_REF_NO, logic.getNextSequenceRefNumber()); // bit37
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, bit61, 3); // bit61
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_4, "01006", 3); // bit63
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createUnregisSmsNotif(HostBtiLogic logic, TransactionTO task) throws JetsException {
		try {
			String amountBlast = IsoUtil.formatCurrency(task.getTermAsDouble(TermConstant.AMOUNT_SMS_NOTIF), 15, 0);
			String bit61 = CommonUtil.zeroPadLeft(task.getPhoneNo(), 13)
					+ CommonUtil.padRight(task.getTermAsString(TermConstant.SRAC), ' ', 14) + amountBlast + amountBlast;

			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST); // bit
																														// 1
			isoMsg = commonIsoMsg(logic, task, isoMsg);
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "380000"); // bit3
			String amnt = IsoUtil.formatCurrency("0", 12, 0);
			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt); // bit4
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.RETRIEVAL_REF_NO, logic.getNextSequenceRefNumber()); // bit37
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, bit61, 3); // bit61
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_4, "01007", 3); // bit63
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createAccountBalanceSulteng(HostBtiLogic logic, TransactionTO task) throws JetsException {
		try {
			String bit61 = CommonUtil.zeroPadLeft(CommonUtil.formatPhoneLocal(task.getPhoneNo()), 13)
					+ CommonUtil.padRight(task.getTermAsString(TermConstant.SRAC), ' ', 14);

			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
			isoMsg = commonIsoMsg(logic, task, isoMsg);
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "380000"); // bit3
			String amnt = IsoUtil.formatCurrency("0", 12, 0);
			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt); // bit4
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.RETRIEVAL_REF_NO, logic.getNextSequenceRefNumber()); // bit37
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, bit61, 3); // bit61
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_4, "01002", 3); // bit63
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createInqAccSulteng(HostBtiLogic logic, TransactionTO task) throws JetsException {
		try {
			String bit61 = CommonUtil.zeroPadLeft(task.getPhoneNo(), 13)
					+ CommonUtil.padRight(task.getTermAsString(TermConstant.SRAC), ' ', 14);

			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST); // bit
																														// 1
			isoMsg = commonIsoMsg(logic, task, isoMsg);
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "380000"); // bit3
			String amnt = IsoUtil.formatCurrency("0", 12, 0);
			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt); // bit4
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.RETRIEVAL_REF_NO, logic.getNextSequenceRefNumber()); // bit37
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, bit61, 3); // bit61
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_4, "01001", 3); // bit63
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createFundTransferInquiry(HostBtiLogic logic, TransactionTO task) throws JetsException {
		try {
			String bit61 = CommonUtil.zeroPadLeft(CommonUtil.formatPhoneLocal(task.getPhoneNo()), 13)
					+ CommonUtil.padRight(task.getTermAsString(TermConstant.SRAC), ' ', 14) + CommonUtil.repeat(" ", 30)
					+ CommonUtil.padRight(task.getTermAsString(TermConstant.DSAC), ' ', 14)
					+ CommonUtil.repeat(" ", 30);

			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
			isoMsg = commonIsoMsg(logic, task, isoMsg);
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "380000"); // bit3
			String amnt = IsoUtil.formatCurrency(task.getTermAsString(TermConstant.AMOUNT), 12, 0);
			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt); // bit4
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.RETRIEVAL_REF_NO, logic.getNextSequenceRefNumber()); // bit37
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, bit61, 3); // bit61
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_4, "01005", 3); // bit63
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createFundTransferSettle(HostBtiLogic logic, TransactionTO task) throws JetsException {
		try {
			String bit61 = task.getTermAsString(TermConstant.BIT61);
			String x = bit61.substring(0, 27) + CommonUtil.padRight(task.getTermAsString(TermConstant.SRAC_NAME), 30)
					+ bit61.substring(57, bit61.length());

			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
			isoMsg = commonIsoMsg(logic, task, isoMsg);
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "500000"); // bit3
			String amnt = IsoUtil.formatCurrency(task.getTermAsString(TermConstant.AMOUNT), 12, 0);
			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt); // bit4
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.RETRIEVAL_REF_NO, logic.getNextSequenceRefNumber()); // bit37
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, x, 3); // bit61
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_4, "01005", 3); // bit63

			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createFundTransferReversal(HostBtiLogic logic, TransactionTO task) throws JetsException {
		try {
			String bit61 = task.getTermAsString(TermConstant.BIT61);
			String x = bit61.substring(0, 27) + CommonUtil.padRight(task.getTermAsString(TermConstant.SRAC_NAME), 30)
					+ bit61.substring(57, bit61.length());

			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.ACQUIRER_REVERSAL_ADVICE);
			isoMsg.setItemWithLength(IsoMsg.PAN, task.getTermAsString(TermConstant.CARD_NUMBER), 2); // bit2
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "500000"); // bit3
			String amnt = IsoUtil.formatCurrency(task.getTermAsString(TermConstant.AMOUNT), 12, 0);
			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt); // bit4
			isoMsg.setItem(IsoMsg.TIMESTAMP, ISODate.getDateTime(new Date())); // bit7
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, CommonUtil.cutOrPad(task.getTerm(TermConstant.BIT11), 6)); // bit11
			isoMsg.setItem(IsoMsg.TIME_LOCAL, CommonUtil.cutOrPad(task.getTerm(TermConstant.BIT12), 6)); // bit12
			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
			isoMsg.setItem(IsoMsg.SETTLEMENT_DATE, ISODate.getDate(new Date())); // bit15
			isoMsg.setItem(IsoMsg.MERCHANT_TYPE, MERCHANT_TYPE); // bit18
			isoMsg.setItemWithLength(IsoMsg.ACQUIRING_INSTITUTION_ID, logic.getAcquirerId(), 2); // bit32
			isoMsg.setItem(IsoMsg.RETRIEVAL_REF_NO, CommonUtil.cutOrPad(task.getTerm(TermConstant.BIT37), 12)); // bit37
			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
			isoMsg.setItem(IsoMsg.CARD_ACCEPTOR_NAME_LOCATION, "MOBILE BANKING BANK BENGKULU     SLH IDN"); // bit43
			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, x, 3); // bit61
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_4, "01005", 3); // bit63
			String bit90 = "0200" + CommonUtil.cutOrPad(task.getTerm(TermConstant.BIT11), 6)
					+ CommonUtil.cutOrPad(task.getTerm(TermConstant.BIT7), 10)
					+ CommonUtil.cutOrPad(CommonUtil.zeroPadLeft(task.getTerm(TermConstant.BIT32), 3), 11)
					+ CommonUtil.cutOrPad(CommonUtil.zeroPadLeft(task.getTerm(TermConstant.BIT32), 3), 11);
//					CommonUtil.cutOrPad(task.getTerm(TermConstant.BIT32), 11) +
//					CommonUtil.cutOrPad("", 11);
			isoMsg.setItem(IsoMsg.ORIGINAL_DATA_ELEMENTS, bit90); // bit90

			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createInterestInfo(HostBtiLogic logic, TransactionTO task, String processCode)
			throws JetsException {
		try {
			String bit61 = CommonUtil.zeroPadLeft(CommonUtil.formatPhoneLocal(task.getPhoneNo()), 13)
					+ CommonUtil.padRight(task.getTermAsString(TermConstant.SRAC), ' ', 14);

			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
			isoMsg = commonIsoMsg(logic, task, isoMsg);
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "380000"); // bit3
			String amnt = IsoUtil.formatCurrency("0", 12, 0);
			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt); // bit4
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, bit61, 3); // bit61
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_4, "01003", 3); // bit63
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createFundTransferOtherInquiry(HostBtiLogic logic, TransactionTO task) throws JetsException {
		try {
			String bit61 = CommonUtil.zeroPadLeft(CommonUtil.formatPhoneLocal(task.getPhoneNo()), 13)
					+ CommonUtil.padRight(task.getTermAsString(TermConstant.SRAC), ' ', 14) + CommonUtil.repeat(" ", 30)
					+ CommonUtil.padRight(task.getTermAsString(TermConstant.DSAC), ' ', 14)
					+ CommonUtil.repeat(" ", 30);

//			String bit48 = CommonUtil.repeat(" ", 30)  //nama nasabah penerima
//						 + CommonUtil.padLeft(task.getTermAsString(TermConstant.TRANSFER_TEXT), ' ', 16)//no referensi
//						 + CommonUtil.repeat(" ", 30); //nama nasabah pengirim
//			String bit48 = CommonUtil.repeat(" ", 30)  //nama nasabah penerima
//						 + CommonUtil.padRight(task.getTermAsString(TermConstant.TRANSFER_TEXT), ' ', 16)//no referensi
//						 + CommonUtil.repeat(" ", 30); //nama nasabah pengirim
			String bit48 = CommonUtil.padRight(task.getTermAsString(TermConstant.DSAC_NAME), ' ', 30) // nama nasabah
																										// penerima
					+ CommonUtil.padRight(task.getTermAsString(TermConstant.TRANSFER_TEXT), ' ', 16)// no referensi
					+ CommonUtil.padRight(task.getTermAsString(TermConstant.SRAC_NAME), ' ', 30); // nama nasabah
																									// pengirim

			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
			isoMsg = commonIsoMsg(logic, task, isoMsg);
			String bit2 = "606058";
//			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "391000"); // bit3
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "392000"); // bit3
			String amnt = IsoUtil.formatCurrency(task.getTermAsString(TermConstant.AMOUNT), 9, 3);
			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt); // bit4
			String bit7 = ISODate.getDateTime(new Date(), TimeZone.getTimeZone("GMT"));
			bit2 = bit2 + bit7;
			logger.debug("createFundTransferOtherInq bit2 : " + bit2);
			logger.debug("createFundTransferOtherInq bit7 : " + bit7);
			isoMsg.setItemWithLength(IsoMsg.PAN, bit2, 2); // bit2
			isoMsg.setItem(IsoMsg.TIMESTAMP, bit7); // bit7
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.SETTLEMENT_DATE, ISODate.getDate(CommonUtil.getNextDay(new Date()))); // bit15
			isoMsg.setItem(IsoMsg.MERCHANT_TYPE, MERCHANT_TYPE_ANTAR_BANK_KNT); // bit18
			isoMsg.setItem(IsoMsg.POINT_SERVICE_ENTRY, "000"); // bit22
			isoMsg.setItem(IsoMsg.RETRIEVAL_REF_NO, logic.getNextSequenceRefNumber()); // bit37
			String phoneNo = task.getPhoneNo();
			if (phoneNo.startsWith("+")) {
				phoneNo = phoneNo.substring(1);
			}
			String bit41 = logic.getTerminalId();
			logger.debug("createFundTransferOtherInq bit41 : " + bit41);
			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, bit41, 8); // bit41

			isoMsg.setItemWithPad(IsoMsg.CARD_ACCEPTOR_ID, "000000000000001", 15); // bit42
			isoMsg.setItem(IsoMsg.CARD_ACCEPTOR_NAME_LOCATION, CommonUtil.padRight(phoneNo, ' ', 33) + "SLH IDN"); // bit43
			isoMsg.setItemWithLength(IsoMsg.DATA_PRIVATE, bit48, 3); // bit48
			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, "1C1C1C1C1C1C1C1C", 16); // bit52
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, bit61, 3); // bit61
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_4, "02001", 3); // bit63
			isoMsg.setItemWithLength(IsoMsg.RECEIVING_INSTITUTION_ID, JetsConstant.BANK_CODE_SULTENG, 2);// bit100
			isoMsg.setItemWithLength(IsoMsg.ACCOUNT_ID_1, task.getTermAsString(TermConstant.SRAC), 2); // bit102
			isoMsg.setItemWithLength(IsoMsg.ACCOUNT_ID_2, task.getTermAsString(TermConstant.DSAC), 2); // bit103
			isoMsg.setItemWithLength(IsoMsg.INFORMATION_DATA_2, "2", 3); // bit125
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_6, task.getTermAsString(TermConstant.BANK_CODE), 3); // bit127
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createFundTransferOtherSettle(HostBtiLogic logic, TransactionTO task) throws JetsException {
		try {
			String bit61 = task.getTermAsString(TermConstant.BIT61);
//			String x = bit61.substring(0, 27) + CommonUtil.padRight(task.getTermAsString(TermConstant.SRAC_NAME), 30) + bit61.substring(57, bit61.length());

//			String bit48 = task.getTermAsString(TermConstant.BIT48).substring(0, 46) + CommonUtil.padRight(task.getTermAsString(TermConstant.SRAC_NAME), ' ', 30);
			String bit48 = task.getTermAsString(TermConstant.BIT48);
			logger.debug("createFundTransferOtherSettle bit48 : " + bit48);
			String dsacName = bit48.substring(0, 30);
			logger.debug("createFundTransferOtherSettle dsacName : " + dsacName);
			String trfText = bit48.substring(30, 46);
			logger.debug("createFundTransferOtherSettle trfText : " + trfText);
			String customBit48 = CommonUtil.padRight(dsacName, ' ', 30) // nama nasabah penerima
					+ CommonUtil.padRight(trfText, ' ', 16)// no referensi
					+ CommonUtil.padRight(task.getTermAsString(TermConstant.SRAC_NAME), 30); // nama nasabah pengirim
			logger.debug("createFundTransferOtherSettle customBit48 : " + customBit48);
			String bit2 = "606058";
			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
			isoMsg = commonIsoMsg(logic, task, isoMsg);

			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "402000"); // bit3
			String amnt = IsoUtil.formatCurrency(task.getTermAsString(TermConstant.AMOUNT), 9, 3);
			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt); // bit4
			String bit7 = ISODate.getDateTime(new Date(), TimeZone.getTimeZone("GMT"));
			bit2 = bit2 + bit7;
			isoMsg.setItemWithLength(IsoMsg.PAN, "6060580000000000", 2); // bit2
			isoMsg.setItem(IsoMsg.TIMESTAMP, ISODate.getDateTime(new Date(), TimeZone.getTimeZone("GMT"))); // bit7
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.SETTLEMENT_DATE, ISODate.getDate(CommonUtil.getNextDay(new Date()))); // bit15
			isoMsg.setItem(IsoMsg.MERCHANT_TYPE, MERCHANT_TYPE_ANTAR_BANK_KNT); // bit18
			isoMsg.setItem(IsoMsg.POINT_SERVICE_ENTRY, "000"); // bit22
			isoMsg.setItem(IsoMsg.RETRIEVAL_REF_NO, logic.getNextSequenceRefNumber()); // bit37
			String phoneNo = task.getPhoneNo();
			if (phoneNo.startsWith("+")) {
				phoneNo = phoneNo.substring(1);
			}
//			String bit41 = "IB"+logic.getTerminalId().substring(2, 8);
			String bit41 = logic.getTerminalId();
			logger.debug("createFundTransferOtherSettle bit2 : " + bit2);
			logger.debug("createFundTransferOtherSettle bit7 : " + bit7);
			logger.debug("createFundTransferOtherSettle bit41 : " + bit41);
			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, bit41, 8); // bit41
//			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
			isoMsg.setItemWithPad(IsoMsg.CARD_ACCEPTOR_ID, "000000000000001", 15); // bit42
			isoMsg.setItem(IsoMsg.CARD_ACCEPTOR_NAME_LOCATION, CommonUtil.padRight(phoneNo, ' ', 33) + "SLH IDN"); // bit43
			isoMsg.setItemWithLength(IsoMsg.DATA_PRIVATE, customBit48, 3); // bit48
			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, "1C1C1C1C1C1C1C1C", 16); // bit52
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, bit61, 3); // bit61
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_4, "02001", 3); // bit63
			isoMsg.setItemWithLength(IsoMsg.RECEIVING_INSTITUTION_ID, JetsConstant.BANK_CODE_SULTENG, 2);// bit100
			isoMsg.setItemWithLength(IsoMsg.ACCOUNT_ID_1, task.getTermAsString(TermConstant.SRAC), 2); // bit102
			isoMsg.setItemWithLength(IsoMsg.ACCOUNT_ID_2, task.getTermAsString(TermConstant.DSAC), 2); // bit103
			isoMsg.setItemWithLength(IsoMsg.INFORMATION_DATA_2, "2", 3); // bit125
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_6, task.getTermAsString(TermConstant.BANK_CODE), 3); // bit127
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createFundTransferOtherReversal(HostBtiLogic logic, TransactionTO task) throws JetsException {
		try {
			String bit61 = task.getTermAsString(TermConstant.BIT61);
			String x = bit61.substring(0, 27) + CommonUtil.padRight(task.getTermAsString(TermConstant.SRAC_NAME), 30)
					+ bit61.substring(57, bit61.length());

			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.ACQUIRER_REVERSAL_REQUEST);
//			isoMsg.setItemWithLength(IsoMsg.PAN, task.getTermAsString(TermConstant.CARD_NUMBER), 2); // bit2
			isoMsg.setItemWithLength(IsoMsg.PAN, "6060580000000000", 2); // bit2
//			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "401000"); // bit3
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "402000"); // bit3

//			String amnt = IsoUtil.formatCurrency(task.getTermAsString(TermConstant.AMOUNT), 12, 0);
			String amnt = IsoUtil.formatCurrency(task.getTermAsString(TermConstant.AMOUNT), 9, 3);
			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt); // bit4
			isoMsg.setItem(IsoMsg.TIMESTAMP, ISODate.getDateTime(new Date(), TimeZone.getTimeZone("GMT"))); // bit7
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, CommonUtil.cutOrPad(task.getTerm(TermConstant.BIT11), 6)); // bit11
			isoMsg.setItem(IsoMsg.TIME_LOCAL, CommonUtil.cutOrPad(task.getTerm(TermConstant.BIT12), 6)); // bit12
			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
//			isoMsg.setItem(IsoMsg.SETTLEMENT_DATE, ISODate.getDate(new Date())); // bit15
			isoMsg.setItem(IsoMsg.SETTLEMENT_DATE, ISODate.getDate(CommonUtil.getNextDay(new Date()))); // bit15
			isoMsg.setItem(IsoMsg.MERCHANT_TYPE, MERCHANT_TYPE_ATMB); // bit18
			isoMsg.setItemWithLength(IsoMsg.ACQUIRING_INSTITUTION_ID, logic.getAcquirerId(), 2); // bit32
			isoMsg.setItem(IsoMsg.RETRIEVAL_REF_NO, CommonUtil.cutOrPad(task.getTerm(TermConstant.BIT37), 12)); // bit37
			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
			isoMsg.setItemWithPad(IsoMsg.CARD_ACCEPTOR_ID, "000000000000001", 15); // bit42
			isoMsg.setItemWithLength(IsoMsg.DATA_PRIVATE, task.getTermAsString(TermConstant.BIT48), 3); // bit48
			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, x, 3); // bit61
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_4, "02001", 3); // bit63
			isoMsg.setItemWithLength(IsoMsg.RECEIVING_INSTITUTION_ID, JetsConstant.BANK_CODE_SULTENG, 2);// bit100
			isoMsg.setItemWithLength(IsoMsg.ACCOUNT_ID_1, task.getTermAsString(TermConstant.SRAC), 2); // bit102
			isoMsg.setItemWithLength(IsoMsg.ACCOUNT_ID_2, task.getTermAsString(TermConstant.DSAC), 2); // bit103
			isoMsg.setItemWithLength(IsoMsg.INFORMATION_DATA_2, "2", 3); // bit125
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_6, task.getTermAsString(TermConstant.BANK_CODE), 3); // bit127
			String bit90 = "0200" + CommonUtil.cutOrPad(task.getTerm(TermConstant.BIT11), 6)
					+ CommonUtil.cutOrPad(task.getTerm(TermConstant.BIT7), 10) +
//					CommonUtil.cutOrPad(CommonUtil.zeroPadLeft(task.getTerm(TermConstant.BIT32), 3), 11) +
					CommonUtil.zeroPadLeft(task.getTerm(TermConstant.BIT32), 11)
					+ CommonUtil.cutOrPad("00000000000", 11);
//					CommonUtil.cutOrPad(CommonUtil.zeroPadLeft(task.getTerm(TermConstant.BIT32), 3), 11);
//					CommonUtil.cutOrPad(task.getTerm(TermConstant.BIT32), 11) +
//					CommonUtil.cutOrPad("", 11);
			isoMsg.setItem(IsoMsg.ORIGINAL_DATA_ELEMENTS, bit90); // bit90
			isoMsg.setItem(IsoMsg.REPLACEMENT_AMOUNTS, CommonUtil.repeat("0", 42)); // bit95
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createAccStatement(HostBtiLogic logic, TransactionTO task) throws JetsException {
		try {
			String bit61 = CommonUtil.zeroPadLeft(CommonUtil.formatPhoneLocal(task.getPhoneNo()), 13)
					+ CommonUtil.padRight(task.getTermAsString(TermConstant.SRAC), ' ', 14);

			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
			isoMsg = commonIsoMsg(logic, task, isoMsg);
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "380000"); // bit3
			String amnt = IsoUtil.formatCurrency("0", 12, 0);
			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt); // bit4
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.RETRIEVAL_REF_NO, logic.getNextSequenceRefNumber()); // bit37
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, bit61, 3); // bit61
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_4, "01004", 3); // bit63
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createBillInquirySulteng(HostBtiLogic logic, TransactionTO task, String billAcctNo)
			throws JetsException {
		try {
			String bit61 = "";
			String bit37 = logic.getNextSequenceRefNumber();

//			if(JetsConstant.BILLER_CODE_PDAM_TIRTANADI.equals(task.getTermAsString(TermConstant.PAYEE_CODE)) || 
//					JetsConstant.BILLER_CODE_PDAM_TIRTABULIAN.equals(task.getTermAsString(TermConstant.PAYEE_CODE))){
//				bit61 = CommonUtil.zeroPadLeft(CommonUtil.formatPhoneLocal(task.getPhoneNo()), 13) 
//					     + CommonUtil.padRight(task.getTermAsString(TermConstant.SRAC), ' ', 14)
//					     + CommonUtil.padRight(billAcctNo, 15);
//			}
//			else if(JetsConstant.BILLER_CODE_ESAMSAT.equals(task.getTermAsString(TermConstant.PAYEE_CODE))){
////				bit61 = CommonUtil.padRight(billAcctNo, 16) 
////						 + CommonUtil.repeat(" ", 30)
////						 + CommonUtil.displayDateTimeESamsat(new Date())
////						 + "002" + CommonUtil.zeroPadLeft(bit37, 12) //digit tracenum
////						 + logic.getTerminalId();
//				bit61 = CommonUtil.zeroPadLeft(CommonUtil.formatPhoneLocal(task.getPhoneNo()), 13)
//						+ CommonUtil.padRight(task.getTermAsString(TermConstant.SRAC), ' ', 14)
//						+ CommonUtil.padRight(billAcctNo, 16) 
//						+ CommonUtil.repeat(" ", 30)
//						+ CommonUtil.displayDateTimeESamsat(new Date())
//						+ "002" + CommonUtil.zeroPadLeft(bit37, 12) //digit tracenum
//						+ logic.getTerminalId();
//			}
//			else if(JetsConstant.BILLER_CODE_TAX_AND_RETRIBUTION.equals(task.getTermAsString(TermConstant.PAYEE_CODE))){
//				bit61 =  CommonUtil.zeroPadLeft(CommonUtil.formatPhoneLocal(task.getPhoneNo()), 13) +
//						 CommonUtil.padRight(task.getTermAsString(TermConstant.SRAC), ' ', 14) +
//						 CommonUtil.padRight(billAcctNo, 21);
//			}
//			else{
			if (JetsConstant.BILLER_CODE_BPJS.equals(task.getTermAsString(TermConstant.PAYEE_CODE))) {
				bit61 = CommonUtil.zeroPadLeft(CommonUtil.formatPhoneLocal(task.getPhoneNo()), 13)
						+ CommonUtil.padRight(task.getTermAsString(TermConstant.SRAC), ' ', 14)
						+ CommonUtil.zeroPadLeft(billAcctNo, 16)
						+ CommonUtil.zeroPadLeft(task.getTermAsString(TermConstant.TYPE_VALUE), 2);
			} else {
				bit61 = CommonUtil.zeroPadLeft(CommonUtil.formatPhoneLocal(task.getPhoneNo()), 13)
						+ CommonUtil.padRight(task.getTermAsString(TermConstant.SRAC), ' ', 14)
						+ CommonUtil.zeroPadLeft(billAcctNo, 13);
			}

//			}

			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
			isoMsg = commonIsoMsg(logic, task, isoMsg);
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "380000"); // bit3
			String amnt = IsoUtil.formatCurrency("0", 12, 0);
			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt); // bit4
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.RETRIEVAL_REF_NO, bit37); // bit37
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, bit61, 3); // bit61
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_4, task.getTerm(TermConstant.BANK_REF), 3); // bit63
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createBillSettlementSulteng(HostBtiLogic logic, TransactionTO task, String bit61)
			throws JetsException {
		try {
			String bit11 = logic.getNextSequence();
			String bit37 = logic.getNextSequenceRefNumber();

//			if(JetsConstant.BILLER_CODE_ESAMSAT.equals(task.getTermAsString(TermConstant.PAYEE_CODE))){
//				bit61 =  bit61.substring(0, 27)
//						 + CommonUtil.repeat("0", 9) //fee admin
//						 + "0"
//						 + CommonUtil.padRight(task.getTermAsString(TermConstant.SRAC), 20)
//						 + CommonUtil.displayTrxDateESamsat(new Date())
//						 + task.getTermAsString(TermConstant.BILL_KEY1)
//						 + CommonUtil.displayDateTimeESamsat(new Date())
//						 + CommonUtil.cutOrPad(null, 10)
//						 + "002" + bit37
//						 + logic.getTerminalId()
//						 + "9920" + bit11
//						 + bit61.substring(27);
//			}
//			else if(JetsConstant.BILLER_CODE_TAX_AND_RETRIBUTION.equals(task.getTermAsString(TermConstant.PAYEE_CODE))){
//				bit61 =  CommonUtil.zeroPadLeft(CommonUtil.formatPhoneLocal(task.getPhoneNo()), 13) +
//						 CommonUtil.padRight(task.getTermAsString(TermConstant.SRAC), ' ', 14) +
//						 CommonUtil.padRight(task.getTermAsString(TermConstant.BILL_KEY1), 21) +
//						 CommonUtil.repeat("0", 16) +
//						 "992";
//			}

			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
			isoMsg = commonIsoMsg(logic, task, isoMsg);
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "500000"); // bit3
			String amnt = IsoUtil.formatCurrency(task.getTermAsString(TermConstant.AMOUNT), 12, 0);
			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt); // bit4
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, bit11); // bit11
			isoMsg.setItem(IsoMsg.RETRIEVAL_REF_NO, bit37); // bit37
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, bit61, 3); // bit61
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_4, task.getTerm(TermConstant.BANK_REF), 3); // bit63

			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createBillReversalSulteng(HostBtiLogic logic, TransactionTO task, String bit61)
			throws JetsException {
		try {

			IsoMsg isoMsg;
//			if(JetsConstant.BILLER_CODE_TAX_AND_RETRIBUTION.equals(task.getTermAsString(TermConstant.PAYEE_CODE))){
//				isoMsg = (IsoMsg) HostIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.ACQUIRER_REVERSAL_REQUEST);
//			}
//			else{
			/*
			 * for samsat, jika 0200 payment tdk dpt balesan dr host, maka engine generate
			 * reversal 0420 jika tdk dpt balesan selanjutnya kirim advice (0421) sebanyak
			 * 3x dlm x menit. balasan dr 0420 & 0421 berupa message 0430.
			 */
//				if(JetsConstant.BILLER_CODE_ESAMSAT.equals(task.getTermAsString(TermConstant.PAYEE_CODE))){
//
//					if(!task.isTermExists(TermConstant.ADVICE_REQ)){
//						isoMsg = (IsoMsg) HostIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.ACQUIRER_REVERSAL_ADVICE);
//						task.setTerm(TermConstant.ADVICE_REQ, true);
//					}
//					else{
//						isoMsg = (IsoMsg) HostIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.ACQUIRER_REVERSAL_ADVICE_SAMSAT);
//					}
//				}
//				else{
			isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.ACQUIRER_REVERSAL_ADVICE);
//				}
//			}
//			IsoMsg isoMsg = (IsoMsg) HostIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.ACQUIRER_REVERSAL_ADVICE);
			isoMsg.setItemWithLength(IsoMsg.PAN, task.getTermAsString(TermConstant.CARD_NUMBER), 2); // bit2
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "500000"); // bit3
			String amnt = IsoUtil.formatCurrency(task.getTermAsString(TermConstant.AMOUNT), 12, 0);
			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt); // bit4
			isoMsg.setItem(IsoMsg.TIMESTAMP, ISODate.getDateTime(new Date())); // bit7
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, CommonUtil.cutOrPad(task.getTerm(TermConstant.BIT11), 6)); // bit11
			isoMsg.setItem(IsoMsg.TIME_LOCAL, CommonUtil.cutOrPad(task.getTerm(TermConstant.BIT12), 6)); // bit12
			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
			isoMsg.setItem(IsoMsg.SETTLEMENT_DATE, ISODate.getDate(new Date())); // bit15
			isoMsg.setItem(IsoMsg.MERCHANT_TYPE, MERCHANT_TYPE); // bit18
			isoMsg.setItemWithLength(IsoMsg.ACQUIRING_INSTITUTION_ID, logic.getAcquirerId(), 2); // bit32
			isoMsg.setItem(IsoMsg.RETRIEVAL_REF_NO, CommonUtil.cutOrPad(task.getTerm(TermConstant.BIT37), 12)); // bit37
			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
			isoMsg.setItem(IsoMsg.CARD_ACCEPTOR_NAME_LOCATION, "MOBILE BANKING BANK SULTENG      SLH IDN"); // bit43
			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, bit61, 3); // bit61
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_4, task.getTerm(TermConstant.BANK_REF), 3); // bit63
			String bit90 = "0200" + CommonUtil.cutOrPad(task.getTerm(TermConstant.BIT11), 6)
					+ CommonUtil.cutOrPad(task.getTerm(TermConstant.BIT7), 10)
					+ CommonUtil.cutOrPad(CommonUtil.zeroPadLeft(task.getTerm(TermConstant.BIT32), 3), 11)
					+ CommonUtil.cutOrPad(CommonUtil.zeroPadLeft(task.getTerm(TermConstant.BIT32), 3), 11);
			isoMsg.setItem(IsoMsg.ORIGINAL_DATA_ELEMENTS, bit90); // bit90
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createAirTimeSettle(HostBtiLogic logic, TransactionTO task) throws JetsException {
		try {
			String bit61 = CommonUtil.zeroPadLeft(CommonUtil.formatPhoneLocal(task.getPhoneNo()), 13)
					+ CommonUtil.padRight(task.getTermAsString(TermConstant.SRAC), ' ', 14) + CommonUtil.zeroPadLeft(
							CommonUtil.formatPhoneLocal(task.getTermAsString(TermConstant.BUYER_PHONE)), 13);
//				     + "111111" 
//				     + "1"
//				     + CommonUtil.zeroPadLeft(task.getTermAsString(TermConstant.AMOUNT), 12);

			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
			isoMsg = commonIsoMsg(logic, task, isoMsg);
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "500000"); // bit3
			String amnt = IsoUtil.formatCurrency(task.getTermAsString(TermConstant.AMOUNT), 12, 0);
			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt); // bit4
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.RETRIEVAL_REF_NO, logic.getNextSequenceRefNumber()); // bit37
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, bit61, 3); // bit61
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_4, task.getTerm(TermConstant.INSTITUTION_CODE), 3); // bit63
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createActivation(HostBtiLogic logic, TransactionTO task) throws JetsException {
		try {
			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.NETWORK_MANAGEMENT_REQUEST);
			isoMsg.setItemWithLength(IsoMsg.PAN, formatPAN(task.getPhoneNo()), 2); // bit2
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "800001"); // bit3
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createNetwork(HostBtiLogic logic, TransactionTO task) throws JetsException {
		try {
			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.NETWORK_MANAGEMENT_REQUEST);
			isoMsg.setItemWithLength(IsoMsg.PAN, formatPAN(task.getPhoneNo()), 2); // bit2
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "800002"); // bit3
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, "XXXXXX", 16); // bit52
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createChangePin(HostBtiLogic logic, TransactionTO task) throws JetsException {
		try {
			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
			isoMsg.setItemWithLength(IsoMsg.PAN, formatPAN(task.getPhoneNo()), 2); // bit2
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "500000"); // bit3
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, task.getTerm(TermConstant.NEW_PIN), 16); // bit 52 new pin
			/*
			 * String bit61 = CommonUtil.cutOrPad(task.getTerm(TermConstant.NEW_PIN), 16) +
			 * CommonUtil.cutOrPad(task.getTerm(TermConstant.CONF_PIN), 16);
			 */
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, task.getTerm(TermConstant.PIN), 3); // bit61 old pin
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_4, "10000", 3); // bit63 fitur code
//			System.out.print("Send Iso : " + isoMsg);
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createAccountInfo(HostBtiLogic logic, TransactionTO task, String accountNo)
			throws JetsException {
		try {
			Assert.notNull(accountNo, "account no is null!");
			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
			isoMsg.setItemWithLength(IsoMsg.PAN, formatPAN(task.getPhoneNo()), 2); // bit2
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "120000"); // bit3
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, task.getTerm(TermConstant.PIN), 16); // bit52
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, CommonUtil.cutOrPad(accountNo, 20), 3); // bit61
//			System.out.print("Send Iso Account Info "+ isoMsg);
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createAccountBalance(HostBtiLogic logic, TransactionTO task) throws JetsException {
		return createAccountBalance(logic, task, task.getPhoneNo(), task.getTerm(TermConstant.PIN),
				task.getTerm(TermConstant.SRAC));
	}

	public static IsoMsg createAccountBalance(HostBtiLogic logic, TransactionTO task, String accountNo)
			throws JetsException {
		return createAccountBalance(logic, task, task.getPhoneNo(), task.getTerm(TermConstant.PIN), accountNo);
	}

	public static IsoMsg createAccountBalance(HostBtiLogic logic, TransactionTO task, String phoneNo, String pin,
			String accountNo) throws JetsException {
//	public static IsoMsg createAccountBalance(HostBtiLogic logic, TransactionTO task) throws JetsException {
		try {
//			Assert.notNull(accountNo, "account no is null!");
			String bit61 = CommonUtil.zeroPadLeft(task.getPhoneNo(), 13)
					+ CommonUtil.padRight(task.getTermAsString(TermConstant.SRAC), ' ', 14);
			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
			isoMsg.setItemWithLength(IsoMsg.PAN,
					CommonUtil.padRight(task.getTermAsString(TermConstant.CARD_NUMBER), 19), 2); // bit2
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "380000"); // bit3
			String amnt = IsoUtil.formatCurrency("0", 12, 0);
			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt); // bit4
			isoMsg.setItem(IsoMsg.TIMESTAMP, ISODate.getDateTime(new Date())); // bit7
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
			isoMsg.setItem(IsoMsg.SETTLEMENT_DATE, ISODate.getDate(new Date())); // bit15
			isoMsg.setItem(IsoMsg.MERCHANT_TYPE, "6017"); // bit18
			isoMsg.setItemWithLength(IsoMsg.ACQUIRING_INSTITUTION_ID, "12345678901", 2); // bit32
//			isoMsg.setItem(IsoMsg.TRACK_2_DATA, ""); // bit35
			isoMsg.setItem(IsoMsg.RETRIEVAL_REF_NO, "123456789012"); // bit37
			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
			isoMsg.setItem(IsoMsg.CARD_ACCEPTOR_NAME_LOCATION, CommonUtil.padRight("LOCATION", ' ', 40)); // bit43
			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, task.getTermAsString(TermConstant.PIN), 16); // bit52
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_AMOUNT, "0", 3); // bit54
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, bit61, 3); // bit61
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_4, "01002", 3); // bit63
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createCreditCardBalance(HostBtiLogic logic, TransactionTO task) throws JetsException {
		try {
			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
			isoMsg.setItemWithLength(IsoMsg.PAN, formatPAN(task.getPhoneNo()), 2); // bit2
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "140001"); // bit3
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, "XXXXXX", 16); // bit52
			// Dob: YYMMDD
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_2, task.getMandatoryTerm(TermConstant.DOB), 3); // bit62
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_3, CommonUtil.cutOrPad(task.getTerm(TermConstant.SRAC), 20),
					3); // bit126
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

//	public static IsoMsg createRefillPayment(HostBtiLogic logic, TransactionTO task, MandiriMultiCharges charges, boolean isReversal)
//			throws JetsException {
//		if (isReversal) {
//			return createMultiCredit(logic, task, charges, "650000", task.getTerm(TermConstant.BUYER_PHONE));
//		}
//		return createMultiCredit(logic, task, charges, "570000", task.getTerm(TermConstant.BUYER_PHONE));
//	}

//	private static IsoMsg createMultiCredit(HostBtiLogic logic,
//			TransactionTO task, MandiriMultiCharges charges,
//			String processCode, String remarkPrefix) throws JetsException {
//		return createMultiCredit(logic, task, charges, task.getPhoneNo(), task.getTerm(TermConstant.PIN), processCode, remarkPrefix);
//	}

//	private static IsoMsg createMultiCredit(HostBtiLogic logic,
//			TransactionTO task, MandiriMultiCharges charges, String phoneNo,
//			String pin, String processCode, String remarkPrefix)
//			throws JetsException {
//		try {
//			IsoMsg isoMsg = (IsoMsg) HostIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
//			isoMsg.setItemWithLength(IsoMsg.PAN, formatPAN(phoneNo), 2); // bit2
//			isoMsg.setItem(IsoMsg.PROCESSING_CODE, processCode); // bit3
//			String amnt = IsoUtil.formatCurrency(task.getTerm(TermConstant.AMOUNT), 15, 2);
//			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt);
//			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
//			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
//			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
//			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
//			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
//			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, pin, 16); // bit52
//			// remark: '<bit2>/<yymmdd><bit41><bit11>'
//			String remark = formatPAN(remarkPrefix) + "/"
//					+ ISODate.getANSIDate(new Date())
//					+ isoMsg.getItem(IsoMsg.TERMINAL_ID)
//					+ isoMsg.getItem(IsoMsg.SYS_TRACE_NO);
//			String bit61 = createMultiCreditBit61(remark, charges);
//			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, bit61, 3); // bit61
//			return isoMsg;
//		} catch (JetsException je) {
//			throw je;
//		} catch (Exception e) {
//			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
//			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
//		}
//	}

//	public static IsoMsg createMandiriMultiCredit(HostBtiLogic logic,
//			TransactionTO task, NewMandiriMultiCharges charges,
//			boolean isReversal) throws JetsException {
//		if (isReversal) {
//			return createMultiCreditNew(logic, task, charges, "650000", task.getTerm(TermConstant.BUYER_PHONE));
//		}
//		return createMultiCreditNew(logic, task, charges, "570000", task.getTerm(TermConstant.BUYER_PHONE));
//	}

//	private static IsoMsg createMultiCreditNew(HostBtiLogic logic,
//			TransactionTO task, NewMandiriMultiCharges charges,
//			String processCode, String remarkPrefix) throws JetsException {
//		return createMultiCreditNew(logic, task, charges, task.getPhoneNo(), task.getTerm(TermConstant.PIN), processCode, remarkPrefix);
//	}

//	private static IsoMsg createMultiCreditNew(HostBtiLogic logic,
//			TransactionTO task, NewMandiriMultiCharges charges, String phoneNo,
//			String pin, String processCode, String remarkPrefix)
//			throws JetsException {
//		try {
//			IsoMsg isoMsg = (IsoMsg) HostIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
//			isoMsg.setItemWithLength(IsoMsg.PAN, formatPAN(phoneNo), 2); // bit2
//			isoMsg.setItem(IsoMsg.PROCESSING_CODE, processCode); // bit3
//			String amnt = IsoUtil.formatCurrency(task.getTerm(TermConstant.AMOUNT), 15, 2);
//			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt);
//			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
//			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
//			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
//			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
//			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
//			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, pin, 16); // bit52
//			// remark: '<bit2>/<yymmdd><bit41><bit11>'
//			String remark = formatPAN(remarkPrefix) + "/"
//					+ ISODate.getANSIDate(new Date())
//					+ isoMsg.getItem(IsoMsg.TERMINAL_ID)
//					+ isoMsg.getItem(IsoMsg.SYS_TRACE_NO);
//			String bit61 = createMultiCreditBit61New(remark, charges);
//			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, bit61, 3); // bit61
//			return isoMsg;
//		} catch (JetsException je) {
//			throw je;
//		} catch (Exception e) {
//			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
//			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
//		}
//	}

//	private static String createMultiCreditBit61New(String remark,
//			NewMandiriMultiCharges charges) throws Exception {
//		MandiriRateMatrix rateMatrix = new MandiriRateMatrix(new MandiriRate(), new MandiriRate(), charges.getAmount());
//		StringBuilder bit61 = new StringBuilder();
//		bit61
//				.append(CommonUtil.cutOrPad(charges.getSrac(), 20))
//				// source account
//				.append(CommonUtil.cutOrPad(charges.getDsac(), 20))
//				// destination b
//				.append(CommonUtils.getISORateAmount(rateMatrix.getIBTFromRate()))
//				// IBT from rate
//				.append(CommonUtils.getISORateAmount(rateMatrix.getIBTToRate())) // IBT
//				// to
//				// rate
//				.append(CommonUtils.getISORateAmount(rateMatrix.getEChannelFromRate())) // eChannel from
//				// rate
//				.append(CommonUtils.getISORateAmount(rateMatrix.getEChannelToRate())) // eChannel to rate
//				.append(CommonUtils.getISORateAmount(rateMatrix.getChargesIBTRate())) // charges ibt rate
//				.append(CommonUtils.getISORateAmount(rateMatrix.getChargesEChannelRate())) // charges echannel
//				// rate
//				.append(rateMatrix.getFromCurr()) // from currency
//				.append(rateMatrix.getToCurr()) // to currency
//				.append(IsoUtil.formatCurrency(charges.getAmount(), 15, 2)) // from
//				// amount
//				.append(IsoUtil.formatCurrency(charges.getAmount(), 15, 2)) // to
//				// amount
//				.append(CommonUtil.cutOrPad(remark, 40)) // remarks
//				.append(CommonUtil.cutOrPad(charges.getChargeSrac(), 20)) // charges
//				// from
//				// account
//				.append(CommonUtil.repeat(" ", 2)) // account type
//				.append(IsoUtil.formatCurrency(charges.getTotalCharges(), 15, 2)) // charges amount in IDR
//				.append(IsoUtil.formatCurrency(charges.getTotalCharges(), 15, 2)) // charges amount in Debit
//				.append(CommonUtil.cutOrPad(charges.getToAccount(0), 20)) // charges
//				// account
//				// 1
//				.append(CommonUtil.cutOrPad(charges.getToAccount(1), 20)) // charges
//				// account
//				// 2
//				.append(CommonUtil.cutOrPad(charges.getToAccount(2), 20)) // charges
//				// account
//				// 3
//				.append(CommonUtil.cutOrPad(charges.getToAccount(3), 20)) // charges
//				// account
//				// 4
//				.append(IsoUtil.formatCurrency(charges.getToAmount(0), 15, 2)) // charges local amount 1
//				.append(IsoUtil.formatCurrency(charges.getToAmount(0), 15, 2)) // charges amount 1
//				.append(IsoUtil.formatCurrency(charges.getToAmount(1), 15, 2)) // charges local amount 2
//				.append(IsoUtil.formatCurrency(charges.getToAmount(1), 15, 2)) // charges amount 2
//				.append(IsoUtil.formatCurrency(charges.getToAmount(2), 15, 2)) // charges local amount 3
//				.append(IsoUtil.formatCurrency(charges.getToAmount(2), 15, 2)) // charges amount 3
//				.append(IsoUtil.formatCurrency(charges.getToAmount(3), 15, 2)) // charges local amount 4
//				.append(IsoUtil.formatCurrency(charges.getToAmount(3), 15, 2)); // charges amount 5
//		return bit61.toString();
//	}

	private static String createFundTransferATMBersamaBit61(TransactionTO task) throws Exception {
		StringBuilder bit61 = new StringBuilder();
		bit61.append(CommonUtil.cutOrPad(task.getTerm(TermConstant.SRAC), 20)) // source
				// account
				.append(CommonUtil.padRight(formatPAN(task.getPhoneNo()), 20)) // MSISDN
				.append("0000") // area Code
				.append(CommonUtil.cutOrPad(task.getTerm(TermConstant.DSAC), 20)) // destination account
				.append(CommonUtil.padRight(" ", 77)) // filler 77 chars
				.append("010") // point of service entry mode
				.append(CommonUtil.padRight(formatPAN(task.getPhoneNo()), 28) + "TSEL MDR ID "); //
		return bit61.toString();
	}

	private static String createFundTransferATMBersamaBit61New(TransactionTO task) throws Exception {
		StringBuilder bit61 = new StringBuilder();
		bit61.append(CommonUtil.cutOrPad(task.getTerm(TermConstant.SRAC), 20)) // source
				// account
				.append(CommonUtil.padRight(formatPAN(task.getPhoneNo()), 20)) // MSISDN
				.append("    ") // area Code
				.append(CommonUtil.cutOrPad(task.getTerm(TermConstant.DSAC), 20)) // destination account
				.append(CommonUtil.padRight(" ", 77)) // filler 77 chars
				.append("010") // point of service entry mode
				.append(CommonUtil.padRight(formatPAN(task.getPhoneNo()), 33) + "JKT IDN"); //
		return bit61.toString();
	}

	private static String createFundTransferLinkBit61(TransactionTO task) throws Exception {
		StringBuilder bit61 = new StringBuilder();
		bit61.append(CommonUtil.cutOrPad(task.getTerm(TermConstant.SRAC), 20)) // source account
				.append(CommonUtil.padRight(formatPAN(task.getPhoneNo()), 20)) // MSISDN
				.append("    ") // area Code
				.append(CommonUtil.cutOrPad(task.getTerm(TermConstant.DSAC), 20)) // destination account
				.append(CommonUtil.padRight(" ", 77)) // filler 77 chars
				.append("010") // point of service entry mode
				.append(CommonUtil.padRight("MANDIRI", 22) + CommonUtil.padRight("JAKARTA", 13) + "DKIID"); //
		return bit61.toString();
	}

	private static String createFundTransferPrimaBit61(TransactionTO task, String telco) throws Exception {
		StringBuilder bit61 = new StringBuilder();
		bit61.append(CommonUtil.cutOrPad(task.getTerm(TermConstant.SRAC), 20)) // source account
				.append(CommonUtil.padRight(formatPAN(task.getPhoneNo()), 20)) // MSISDN
				.append("    ") // area Code
				.append(CommonUtil.cutOrPad(task.getTerm(TermConstant.DSAC), 20)) // destination account
				.append(CommonUtil.padRight(" ", 77)) // filler 77 chars
				.append("010") // point of service entry mode
				.append(CommonUtil.padRight(formatPAN(task.getPhoneNo()) + " " + telco, 22)
						+ CommonUtil.padRight("JAKARTA", 13) + "   " + "ID")
//				.append(CommonUtil.padRight(formatPAN(task.getPhoneNo()) + " TSEL", 32) + " JKT" + " IDN")
				.append(CommonUtil.padRight(" ", 31)); // filler 77 chars
		return bit61.toString();
	}

	private static String createFundTransferATMBersamaBit126New(TransactionTO task) throws Exception {
		StringBuilder bit126 = new StringBuilder();
		String dsacName = "";
//		bit126.append(CommonUtil.cutOrPad("& 0000200348! Q100326", 22)) // Token Header
		if (!StringUtils.isEmpty(task.getTerm(TermConstant.DSAC_NAME)))
			dsacName = task.getTerm(TermConstant.DSAC_NAME);
		bit126.append(CommonUtil.cutOrPad("& 0000200168! Q900146", 22)) // Token Header
				// .append("3003") //util code, remove util code base on doc ver 1.5
				.append(CommonUtil.padRight(dsacName, 30)) // customer name
				.append(CommonUtil.padRight("", 16)) // customer ref no
//				.append(CommonUtil.cutOrPad(TermConstant.TRANSFER_TEXT, 16)) //customer ref no
				.append(CommonUtil.cutOrPad(task.getTerm(TermConstant.ACC_HOLDER_NAME), 30)) // issuer cust name
				.append(task.getTerm(TermConstant.BANK_CODE)) // dest bank code
				.append("008") // issuer bank code
				.append("MDR3") // from fiid
				.append("ATMB") // to fiid
				.append("2") // transfer indicator
				.append("2") // Debet or Credit Indicator
				.append(CommonUtil.cutOrPad(task.getTerm(TermConstant.DSAC), 19)) // destination account
				.append(CommonUtil.zeroPadLeft("0", 12)) // charges amount
				.append(CommonUtil.zeroPadLeft("0", 19)) // Charges account number with trailing spaces
				.append(CommonUtil.padRight(" ", 19)); // cif number blank
		return bit126.toString();
	}

	private static String createFundTransferLinkBit126(TransactionTO task) throws Exception {
		StringBuilder bit126 = new StringBuilder();
		bit126.append("& 0000200348! Q100326 ") // token header
				.append("3004") // util code
				.append(CommonUtil.padRight(" ", 30)) // dest customer name
				.append(CommonUtil.padRight(" ", 16)) // cust reference number
				.append(CommonUtil.padRight(task.getTerm(TermConstant.ACC_HOLDER_NAME), 30)) // issuer cust name
				.append(task.getTerm(TermConstant.BANK_CODE)) // dest bank code
				.append("002") // issuer bank code
				.append("MDR3") // from fiid
				.append("LINK") // to fiid
				.append("2") // transfer indicator
				.append("2") // D/C indicator
				.append(CommonUtil.padRight(TermConstant.DSAC, 19)) // to acc number
				.append(CommonUtil.padRight("0", '0', 12)) // charges amount
				.append(CommonUtil.padRight("0", '0', 19)) // charges account
				.append(CommonUtil.padRight(" ", 19)); // cif number
		return bit126.toString();
	}

	private static String createFundTransferPrimaBit126(TransactionTO task) throws Exception {
		StringBuilder bit126 = new StringBuilder();

		String dsacName = CommonUtil.padRight(task.getTerm(TermConstant.ACC_HOLDER_NAME), 40);
		bit126.append("& 0000200292! R100270 ") // token header
				.append(task.getTerm(TermConstant.BANK_CODE)) // destination bank code
				.append(CommonUtil.padRight("", 35)) // benef xfer1
				.append(CommonUtil.padRight("", 35)) // benef xfer2
				.append(dsacName.substring(0, 35)) // first 35 char of recipient's name
				.append(CommonUtil.padRight(dsacName.substring(35, 40), 35)) // last 5 char of recipient's name
				.append(CommonUtil.padRight(" ", 35)) // user reference
				.append(CommonUtil.padRight("", 35)) // desc 2
				.append(CommonUtil.padRight("", 35)) // desc 3
				.append("3") // indicator xfer acquirer
				.append(" ") // indicator xfer switcher
				.append("008") // issuer bank code
				.append(CommonUtil.padRight(" ", 17)); // filler
		return bit126.toString();
	}

	private static String createFundTransferATMBersamaBit126(TransactionTO task) throws Exception {
		StringBuilder bit126 = new StringBuilder();
		bit126
				// .append("3003") // util code, remove util code base on doc v1.5
				.append(CommonUtil.padRight(" ", 30)) // destination customer
				// name
				.append("1234512345123456") // customer reference number
				.append(CommonUtil.padRight(" ", 30)) // issuer customer name
				.append(task.getTerm(TermConstant.BANK_NO)) // destination bank
				// code
				.append("MDR") // issuer bank code
				.append("MDR3ATMB") // from/to FIID
				.append("2") // transfer indicator
				.append("2") // debet or credit indicator
				.append(CommonUtil.cutOrPad(task.getTerm(TermConstant.DSAC), 19)) // destination account
				.append(CommonUtil.zeroPadLeft("0", 12)) // charges amount
				.append(CommonUtil.zeroPadLeft("0", 19)) // charges account
				// number
				.append(CommonUtil.padRight(" ", 19)); // CIF number
		return bit126.toString();
	}

	public static IsoMsg createFundTransferInquiry(HostBtiLogic logic, TransactionTO task, String accountNo)
			throws JetsException {
		try {
			Assert.notNull(accountNo, "account no is null!");
			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
			isoMsg.setItemWithLength(IsoMsg.PAN, formatPAN(task.getPhoneNo()), 2); // bit2
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "690000"); // bit3
			String amnt = IsoUtil.formatCurrency(task.getTerm(TermConstant.AMOUNT), 15, 2);
			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt); // bit4
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, task.getTerm(TermConstant.PIN), 16); // bit52
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, createFundTransferATMBersamaBit61(task), 3); // bit61
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_3, createFundTransferATMBersamaBit126(task), 3); // bit126
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createForexInfo(HostBtiLogic logic, TransactionTO task) throws JetsException {
		return createForexInfo(logic, task, task.getTerm(TermConstant.CURR));
	}

	public static IsoMsg createForexInfo(HostBtiLogic logic, TransactionTO task, String curr) throws JetsException {
		try {
			IsoMsg isoMsg = createHostIsoMsg(IsoMsgHeader.AUTHORIZATION_REQUEST);
//			isoMsg.setItemWithLength(IsoMsg.PAN, formatPAN(phoneNo), 2); // bit2
			isoMsg.setItemWithLength(IsoMsg.PAN, CommonUtil.cutOrPad(formatPAN(task.getPhoneNo()), 16), 2); // bit2
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "030500"); // bit3
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, curr, 3); // bit61
//			System.out.print("Send Iso createForexRate "+ isoMsg);
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createCreditCardPayment(HostBtiLogic logic, TransactionTO task) throws JetsException {
		try {
			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
			isoMsg.setItemWithLength(IsoMsg.PAN, formatPAN(task.getPhoneNo()), 2); // bit2
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "340002"); // bit3
			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, IsoUtil.getISOAmount(task.getMandatoryTerm(TermConstant.AMOUNT)));// bit4
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, task.getTerm(TermConstant.PIN), 16); // bit52
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA,
					CommonUtil.cutOrPad(task.getMandatoryTerm(TermConstant.SRAC), 20), 3); // bit61
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_3, CommonUtil.cutOrPad(task.getTerm(TermConstant.DSAC), 20),
					3); // bit126
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createPhoneBillInquiry(HostBtiLogic logic, TransactionTO task) throws JetsException {
		return createPhoneBillInquiry(logic, task, task.getPhoneNo(), task.getTerm(TermConstant.PIN));
	}

	public static IsoMsg createPhoneBillInquiry(HostBtiLogic logic, TransactionTO task, String phoneNo, String pin)
			throws JetsException {
		try {
			// internal phone no and pin usage is specific to BP transaction
			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
			isoMsg.setItemWithLength(IsoMsg.PAN, formatPAN(phoneNo), 2); // bit2
			// isoMsg.setItem(ISOMsg.PROCESSING_CODE,
			// getBillInquiryProcessingCode(vo.getTerm(TermConstant.PAYEE_CODE))); //bit3
			if ("xxxx".equals(task.getTermAsString(TermConstant.BANK_REF)))
				task.setTerm(TermConstant.BANK_REF, "0001");
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "31" + task.getTerm(TermConstant.BANK_REF)); // bit3
//			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "310001"); // bit3
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, pin, 16); // bit52
			// bill acc no + biller code (0000)
			String bit61 = CommonUtil.cutOrPad(task.getTerm(TermConstant.BIT61), 20) + "0000"; // task.getTerm(TermConstant.BANK_REF);
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, bit61, 3); // bit61
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createPhoneBillSettlement(HostBtiLogic logic, TransactionTO task) throws JetsException {
		try {
			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
			isoMsg.setItemWithLength(IsoMsg.PAN, formatPAN(task.getPhoneNo()), 2); // bit2
			// isoMsg.setItem(ISOMsg.PROCESSING_CODE,
			// getBillPaymentProcessingCode(task.getTerm(TermConstant.PAYEE_CODE))); //bit3,
			// 32003 telkom
			String bankRef = task.getTerm(TermConstant.BANK_REF);
			if ("xxxx".equalsIgnoreCase(bankRef))
				bankRef = "0001";
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "32" + bankRef); // bit3
//			String amnt = MandiriUtil.getISOAmount(task.getMandatoryTerm(TermConstant.AMOUNT));
			String amnt = IsoUtil.getISOAmount(task.getTerm(TermConstant.AMOUNT));
			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt);// bit4
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, task.getTerm(TermConstant.PIN), 16); // bit52
			String bit61Inq = task.getMandatoryTerm(TermConstant.BIT61);
			// bit61:
			// SRAC<20>+MSISDN<20>+AreaCode<4>+DSAC<20>+RRNO<22>+BillerCode<4>+DebitAmnt<17>+CreditAmnt<17>+Commission<17>
			String bit61 = CommonUtil.cutOrPad(task.getTerm(TermConstant.SRAC), 20)
					// + CommonUtil.cutOrPad(bit61Inq.substring(4, 20), 20) //phone no without
					// prefix
					// + CommonUtil.cutOrPad(task.getTerm(TermConstant.BILL_KEY1), 20) //phone no
					// with prefix
					// + CommonUtil.cutOrPad(task.getTerm(TermConstant.AREA_CODE), 4) //area code
					+ bit61Inq.substring(0, 20) // phone no with prefix
					+ bit61Inq.substring(0, 4) // prefix
					+ CommonUtil.cutOrPad("", 20) /* task.getTerm(TermConstant.DSAC) */
					// + task.getTerm(TermConstant.BREF)
					+ CommonUtil.cutOrPad("", 22)
					// + task.getTerm(TermConstant.BANK_REF)
					+ "0000" + amnt + amnt + CommonUtil.zeroPadLeft("0", 17);
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, bit61, 3); // bit61
			String bit126 = task.getTerm(TermConstant.BIT126).substring(0, 9) + "188"
					+ task.getTerm(TermConstant.BIT126).substring(12, 18) + "166"
					+ task.getTerm(TermConstant.BIT126).substring(21, 188);
			// need to be updated for xl conditions
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_3, bit126, 3);// bit126
//			System.out.println("Send To DSP: " + isoMsg);
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createNewBillInquiry(HostBtiLogic logic, TransactionTO task) throws JetsException {
		return createNewBillInquiry(logic, task, task.getPhoneNo(), task.getTerm(TermConstant.PIN));
	}

	public static IsoMsg createNewBillInquiry(HostBtiLogic logic, TransactionTO task, String phoneNo, String pin)
			throws JetsException {
		try {
			// internal phone no and pin usage is specific to BP transaction
			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
			isoMsg.setItemWithLength(IsoMsg.PAN, formatPAN(phoneNo), 2); // bit2
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "350002"); // bit3
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, pin, 16); // bit52
			String bit61 = CommonUtil.cutOrPad(task.getTerm(TermConstant.BILL_KEY1), 20) + "0000"; // bill
			// acc no biller code(0000)
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, bit61, 3); // bit61
			String bit126 = CommonUtil.padRight("&", 22) + CommonUtil.cutOrPad(task.getTerm(TermConstant.BANK_REF), 4)
					+ task.getTermAsString(TermConstant.BILL_KEY2); // Tax year
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_3, bit126, 3); // bit126
//			System.out.println("Send To DSP: " + isoMsg);
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createNewBillSettlement(HostBtiLogic logic, TransactionTO task) throws JetsException {
		try {
			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
			isoMsg.setItemWithLength(IsoMsg.PAN, formatPAN(task.getPhoneNo()), 2); // bit2
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "360002"); // bit3
			String amnt = IsoUtil.getISOAmount(task.getMandatoryTerm(TermConstant.AMOUNT));
			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt);// bit4
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, task.getTerm(TermConstant.PIN), 16); // bit52
			// bit61, get from bill inquiry. Format:
			// BookingCode<20>+Blanks<4>+RRNO<22>
			// String bit61Inq = task.getMandatoryTerm(TermConstant.BIT61);
			// bit61:
			// BookingCode<20>+Blanks<4>+RRNO<22>+BillerCode<4>+DebitAccount<20>+DebitAmount<17>+
			// CreditAccount<20>+CreditAmount<17>+ChargesAccount<20>+ChargesAmount<17>+ChargesAccount2<20>+
			// ChargesAmount2<17>
			String bit61 =
//					bit61Inq +
					CommonUtil.cutOrPad(task.getTerm(TermConstant.BILL_KEY1), 20) + CommonUtil.cutOrPad("", 4)
							+ CommonUtil.cutOrPad(task.getTerm(TermConstant.BREF), 22) + "0000"
							+ CommonUtil.cutOrPad(task.getTerm(TermConstant.SRAC), 20) + amnt +
							// ISOUtil.zeropad("0", 17) +
							CommonUtil.padRight("", 20) +
							// amnt +
							CommonUtil.zeroPadLeft("0", 17) + CommonUtil.padRight("", 20)
							+ CommonUtil.zeroPadLeft("0", 17) + // charges
							CommonUtil.padRight("", 20) + CommonUtil.zeroPadLeft("0", 17); // charges2
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, bit61, 3); // bit61
			String bit126inq = task.getTerm(TermConstant.BIT126);
			String bit126 = bit126inq;
			if ("00004".equals(task.getTerm(TermConstant.PAYEE_CODE))
					|| "00041".equals(task.getTerm(TermConstant.PAYEE_CODE))) { // PAM
//				int counter = task.getTermAsInteger("%Counter%");
//				
//				bit126 = CommonUtil.cutOrPad(bit126, 247)
//						+ CommonUtil.cutOrPad("", 50)
//						+ bit126inq.substring(45  +  6*(counter - 1),  45 +  6*counter)
//						+ bit126inq.substring(122 + 11*(counter - 1), 122 + 11*(counter - 1) + 8)
//						+ bit126inq.substring(152 + 12*(counter - 1), 152 + 12*counter)
//						+ CommonUtil.cutOrPad("", 14);

				bit126 = CommonUtil.cutOrPad(bit126, 247) + CommonUtil.cutOrPad("", 50) + bit126inq.substring(45, 51)
						+ bit126inq.substring(122, 130) + bit126inq.substring(152, 164) + CommonUtil.cutOrPad("", 14);
			}
//			if ("00003".equals(task.getTerm(TermConstant.PAYEE_CODE))) {
//				bit126 = CommonUtil.cutOrPad(bit126inq, 164)
//						+ bit126inq.substring(211, 236)
//						+ bit126inq.substring(299, 324)
//						+ bit126inq.substring(387, 412);
//			}
			if ("00003".equals(task.getTerm(TermConstant.PAYEE_CODE))) { // PLN
				bit126 = "460" + bit126inq.substring(0, 61) + "1" + bit126inq.substring(62);
				isoMsg.setItem(IsoMsg.ADDITIONAL_DATA_3, bit126); // bit126

//				bit126 = bit126inq.substring(0, 61) + "1" + bit126inq.substring(62, 164) +
//						"00          00         0000          00         0000          00         00";
//				isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_3, bit126, 3); // bit126
			} else {
				isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_3, bit126, 3); // bit126
			}
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createOpenBillSettlement(HostBtiLogic logic, TransactionTO task) throws JetsException {
		try {
			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
			isoMsg.setItemWithLength(IsoMsg.PAN, formatPAN(task.getPhoneNo()), 2); // bit2
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "370002"); // bit3
			String amnt = IsoUtil.getISOAmount(task.getMandatoryTerm(TermConstant.AMOUNT));
			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt);// bit4
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, task.getTerm(TermConstant.PIN), 16); // bit52
			// bit61, get from bill inquiry. Format:
			// BookingCode<20>+Blanks<4>+RRNO<22>
//			String bit61Inq = task.getMandatoryTerm(TermConstant.BIT61);
			// bit61:
			// BookingCode<20>+Blanks<4>+RRNO<22>+BillerCode<4>+DebitAccount<20>+DebitAmount<17>+
			// CreditAccount<20>+CreditAmount<17>+ChargesAccount<20>+ChargesAmount<17>+ChargesAccount2<20>+
			// ChargesAmount2<17>
			String bit61 =
//				bit61Inq +
					CommonUtil.cutOrPad(task.getTerm(TermConstant.BILL_KEY1), 20) + CommonUtil.padRight("", 4)
							+ CommonUtil.padRight("", 22) + "0000"
							+ CommonUtil.cutOrPad(task.getTerm(TermConstant.SRAC), 20) + CommonUtil.zeroPadLeft("0", 17)
							+ CommonUtil.padRight("", 20) + CommonUtil.zeroPadLeft("0", 17)
							+ CommonUtil.padRight("", 20) + CommonUtil.zeroPadLeft("0", 17) + // charges
							CommonUtil.padRight("", 20) + CommonUtil.zeroPadLeft("0", 17); // charges2
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, bit61, 3); // bit61
//			MandiriPLNBit126 mpb = new MandiriPLNBit126();
//			mpb.parse(task.getMandatoryTerm(TermConstant.BIT126));
//			mpb.setPaidBill(1); // always pay the latest bill
//			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_3, mpb.toBit126Str(), 3);// bit126
//			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_3, createFundTransferATMBersamaBit126(task), 3); // bit126
//			String bit126inq = task.getTerm(TermConstant.BIT126);
//			String bit126= ISOUtil.strpad("&", 22) + CommonUtil.cutOrPad(task.getTerm(TermConstant.BANK_REF), 4); // token header
			String bit126 = CommonUtil.padRight("&", 22) + task.getTerm(TermConstant.BANK_REF); // token header
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_3, bit126, 3); // bit61
//			System.out.println("Send To DSP: " + isoMsg);
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

//	public static IsoMsg createFundTransfer(HostBtiLogic logic, TransactionTO task, MandiriRateMatrix rateMatrix)
//			throws JetsException {
//		try {
//			String processCode = null;
//			if (task.getTerm(TermConstant.TRANSFER_TEXT) == null) {
//				processCode = "230000";
//			} else {
//				processCode = "270000";
//			}
//			return createFundTransfer(logic, task, rateMatrix, processCode, createFundTransferBit61(task, rateMatrix));
//		} catch (JetsException je) {
//			throw je;
//		} catch (Exception e) {
//			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
//			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
//		}
//	}

//	public static IsoMsg createATMBersamaTransfer(HostBtiLogic logic, TransactionTO task, MandiriRateMatrix rateMatrix, BankInfo bankInfo)
//			throws JetsException {
//		try {
//			String bit61 = createFundTransferATMBersamaBit61(task);
//			String bit126 = createFundTransferATMBersamaBit126(task);
//			// bit61+bankCode<20>+bankName<20>+beneficaryName<40>
//			StringBuilder sb = new StringBuilder(bit61);
//			sb.append(bit126);
//			return createFundTransfer(logic, task, rateMatrix, "700000", sb.toString());
//		} catch (JetsException je) {
//			throw je;
//		} catch (Exception e) {
//			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
//			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
//		}
//	}

//	public static IsoMsg createLinkTransfer(HostBtiLogic logic, TransactionTO task, MandiriRateMatrix rateMatrix, BankInfo bankInfo)
//			throws JetsException {
//		try {
//			String bit61 = createFundTransferLinkBit61(task);
//			String bit126 = createFundTransferLinkBit126(task);
//			// bit61+bankCode<20>+bankName<20>+beneficaryName<40>
//			StringBuilder sb = new StringBuilder(bit61);
//			sb.append(bit126);
//			return createFundTransfer(logic, task, rateMatrix, "730000", sb.toString());
//		} catch (JetsException je) {
//			throw je;
//		} catch (Exception e) {
//			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
//			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
//		}
//	}

//	public static IsoMsg createRtgsTransfer(HostBtiLogic logic, TransactionTO task, MandiriRateMatrix rateMatrix, BankInfo bankInfo)
//			throws JetsException {
//		try {
//			String bit61 = createFundTransferBit61(task, rateMatrix);
//			// bit61+bankCode<20>+bankName<20>+beneficaryName<40>
//			StringBuilder sb = new StringBuilder(bit61);
//			sb.append(CommonUtil.cutOrPad(bankInfo.getRtgsCode(), 20))
//				.append(CommonUtil.cutOrPad(bankInfo.getName(), 20))
//				.append(CommonUtil.cutOrPad(task.getTerm(TermConstant.DSAC_NAME), 40));
//			return createFundTransfer(logic, task, rateMatrix, "510000", sb.toString());
//		} catch (JetsException je) {
//			throw je;
//		} catch (Exception e) {
//			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
//			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
//		}
//	}

//	public static IsoMsg createFundTransferAtmBersama(HostBtiLogic logic, TransactionTO task, MandiriRateMatrix rateMatrix, BankInfo bankInfo)
//			throws JetsException {
//		try {
//			String bit61 = createFundTransferBit61(task, rateMatrix);
//			// bit61+bankCode<20>+bankName<20>+beneficaryName<40>
//			StringBuilder sb = new StringBuilder(bit61);
//			sb.append(CommonUtil.cutOrPad(bankInfo.getRtgsCode(), 20))
//				.append(CommonUtil.cutOrPad(bankInfo.getName(), 20))
//				.append(CommonUtil.cutOrPad(task.getTerm(TermConstant.DSAC_NAME), 40));
//			return createFundTransfer(logic, task, rateMatrix, "510000", sb.toString());
//		} catch (JetsException je) {
//			throw je;
//		} catch (Exception e) {
//			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
//			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
//		}
//	}

//	public static IsoMsg createCnTransfer(HostBtiLogic logic, TransactionTO task, MandiriRateMatrix rateMatrix, BankInfo bankInfo)
//			throws JetsException {
//		try {
//			String bit61 = createFundTransferBit61(task, rateMatrix);
//			// bit61+beneficaryName<40>+bankCode<3>+CityCode<4>+BranchCode<7>
//			StringBuilder sb = new StringBuilder(bit61);
//			sb.append(CommonUtil.cutOrPad(task.getTerm(TermConstant.DSAC_NAME), 40))
//				.append(CommonUtil.cutOrPad(bankInfo.getCnCode(), 3))
//				.append(CommonUtil.cutOrPad(bankInfo.getCityCode(), 4))
//				.append(CommonUtil.zeroPadLeft(bankInfo.getBranchCode(), 7))
//				.append("Y") // citizen
////					// flag,
////					// always
////					// "Y"
//				.append(task.getMandatoryTerm(TermConstant.RESIDENCE));
//			return createFundTransfer(logic, task, rateMatrix, "520000", sb.toString());
//		} catch (JetsException je) {
//			throw je;
//		} catch (Exception e) {
//			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
//			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
//		}
//	}

//	public static IsoMsg createRemittanceTransfer(HostBtiLogic logic, TransactionTO task, RemittanceMandiriRateMatrix rateMatrix,
//			BankInfo bankInfo) throws JetsException {
//		try {
//			String bit61 = createFundTransferBit61(task, rateMatrix);
//			return createFundTransfer(logic, task, rateMatrix, "290000", bit61);
//		} catch (JetsException je) {
//			throw je;
//		} catch (Exception e) {
//			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
//			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
//		}
//	}

//	private static String createFundTransferBit61(TransactionTO task, MandiriRateMatrix rateMatrix) throws Exception {
//		// bit61=
//		// FromAccount<20>+ToAccount<20>+IBTFromRate<13>+IBTToRate<13>+EChannelFromRate<13>+
//		// EChannelToRate<13>+ChargesIBTRate<13>+ChargesEChannelRate<13>+FromCurr<3>+ToCurr<3>+
//		// FromAmount<17>+ToAmount<17>+Remarks<40>+ChargesAccount<20>+ChargesAmount<17>+ChargesAmount<17>
//		StringBuilder bit61 = new StringBuilder();
//		bit61.append(CommonUtil.cutOrPad(task.getTerm(TermConstant.SRAC), 20))
//				// source account
//				.append(CommonUtil.cutOrPad(task.getTerm(TermConstant.DSAC), 20))
//				// destination account
//				.append(CommonUtils.getISORateAmount(rateMatrix.getIBTFromRate()))
//				// IBT from rate
//				.append(CommonUtils.getISORateAmount(rateMatrix.getIBTToRate())) // IB
//				// to
//				// rate
//				.append(CommonUtils.getISORateAmount(rateMatrix.getEChannelFromRate())) // eChannel from
//				// rate
//				.append(CommonUtils.getISORateAmount(rateMatrix.getEChannelToRate())) // eChannel to rate
//				.append(CommonUtils.getISORateAmount(rateMatrix.getChargesIBTRate())) // charges ibt rate
//				.append(CommonUtils.getISORateAmount(rateMatrix.getChargesEChannelRate())) // charges echannel
//				// rate
//				.append(rateMatrix.getFromCurr()) // from currency
//				.append(rateMatrix.getToCurr()) // to currency
//				.append(IsoUtil.formatCurrency(rateMatrix.getFromAmount(), 15, 2)) // from amount
//				.append(IsoUtil.formatCurrency(rateMatrix.getToAmount(), 15, 2)) // to amount
//				.append(CommonUtil.cutOrPad(task.getTerm(TermConstant.TRANSFER_TEXT), 40)) // remarks
//				.append(CommonUtil.cutOrPad(rateMatrix.getChargesAccount(), 20)) // charges
//				// account
//				.append(IsoUtil.formatCurrency(rateMatrix.getLocalCharges(), 15, 2)) // charges
//				// amount
//				// in
//				// IDR
//				.append(IsoUtil.formatCurrency(rateMatrix.getCharges(), 15, 2)); // charges amount in Debit
//		return bit61.toString();
//	}

//	private static IsoMsg createFundTransfer(HostBtiLogic logic, TransactionTO task, MandiriRateMatrix rateMatrix,
//			String processCode, String bit61) throws JetsException {
//		try {
//			// iso fund transfer
//			IsoMsg isoMsg = (IsoMsg) HostIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
//			isoMsg.setItemWithLength(IsoMsg.PAN, CommonUtil.cutOrPad(formatPAN(task.getPhoneNo()), 19), 2); // bit2
//			isoMsg.setItem(IsoMsg.PROCESSING_CODE, processCode); // bit3
//			String amnt = IsoUtil.getISOAmount(task.getTerm(TermConstant.AMOUNT));
//			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt); // bit4
//			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
//			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
//			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
//			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
//			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
//			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, task.getTerm(TermConstant.PIN), 16); // bit52
//			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, bit61, 3); // bit61
////			System.out.println("Send Fund Transfer In House Iso : " + isoMsg);
//			return isoMsg;
//		} catch (JetsException je) {
//			throw je;
//		} catch (Exception e) {
//			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
//			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
//		}
//	}

	public static IsoMsg createFundTransferAtmBersamaNew(HostBtiLogic logic, TransactionTO task, String processCode,
			String bit61) throws JetsException {
		try {
			// iso fund transfer
			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
			isoMsg.setItemWithLength(IsoMsg.PAN, formatPAN(task.getPhoneNo()), 2); // bit2
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, processCode); // bit3
			String amnt = IsoUtil.getISOAmount(task.getTerm(TermConstant.AMOUNT));
			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt);// bit4
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, task.getTerm(TermConstant.PIN), 16); // bit52
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, createFundTransferATMBersamaBit61New(task), 3); // bit61
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_3, createFundTransferATMBersamaBit126New(task), 3); // bit126
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createFundTransferLink(HostBtiLogic logic, TransactionTO task, String processCode,
			String bit61) throws JetsException {
		try {
			// iso fund transfer
			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
			isoMsg.setItemWithLength(IsoMsg.PAN, formatPAN(task.getPhoneNo()), 2); // bit2
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, processCode); // bit3
			// String amnt = MandiriUtil.getISOAmount(rateMatrix.getBit4());
			String amnt = IsoUtil.getISOAmount(task.getTerm(TermConstant.AMOUNT));
			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt);// bit4
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, task.getTerm(TermConstant.PIN), 16); // bit52
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, createFundTransferLinkBit61(task), 3); // bit61
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_3, createFundTransferLinkBit126(task), 3); // bit126
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createFundTransferPrima(HostBtiLogic logic, TransactionTO task, String processCode,
			String bit61, String telco) throws JetsException {
		try {
			// iso fund transfer
			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
			isoMsg.setItemWithLength(IsoMsg.PAN, formatPAN(task.getPhoneNo()), 2); // bit2
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, processCode); // bit3
			// String amnt = MandiriUtil.getISOAmount(rateMatrix.getBit4());
			String amnt = IsoUtil.getISOAmount(task.getTerm(TermConstant.AMOUNT));
			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt);// bit4
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, task.getTerm(TermConstant.PIN), 16); // bit52
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, createFundTransferPrimaBit61(task, telco), 3); // bit61
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_3, createFundTransferPrimaBit126(task), 3); // bit126
//			System.out.println("Send Iso: " + isoMsg);
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

//	public static IsoMsg createMAtmPayment(HostBtiLogic logic,
//			TransactionTO task, boolean isReversal) throws JetsException {
//		return createMAtmPayment(logic, task, task.getPhoneNo(), task.getTerm(TermConstant.PIN), isReversal);
//	}

	/*
	 * remark: '<bit2>/<yymmdd><bit41><bit11>'
	 */
//	public static IsoMsg createMAtmPayment(HostBtiLogic logic, TransactionTO task, String phoneNo, String pin, boolean isReversal)
//			throws JetsException {
//		if (isReversal) {
//			return createMultiCredit(logic, task, phoneNo, pin, "610000", phoneNo);
//		}
//		return createMultiCredit(logic, task, phoneNo, pin, "530000", phoneNo);
//	}

//	public static IsoMsg createCashAdvance(HostBtiLogic logic, TransactionTO task, boolean isReversal)
//			throws JetsException {
//		return createCashAdvance(logic, task, task.getPhoneNo(), task.getTerm(TermConstant.PIN), isReversal);
//	}

	/*
	 * remark: '<bit2>/<yymmdd><bit41><bit11>'
	 */
//	public static IsoMsg createCashAdvance(HostBtiLogic logic, TransactionTO task, String phoneNo, String pin, boolean isReversal)
//			throws JetsException {
//		if (isReversal) {
//			return createMultiCredit(logic, task, phoneNo, pin, "620000", phoneNo);
//		}
//		return createMultiCredit(logic, task, phoneNo, pin, "540000", phoneNo);
//	}

	/*
	 * remark: 'buyerPhoneNo/<yymmdd><bit41><bit11>'
	 */
//	public static IsoMsg createRefillPayment(HostBtiLogic logic,
//			TransactionTO task, boolean isReversal) throws JetsException {
//		if (isReversal) {
//			return createMultiCredit(logic, task, "650000", task.getTerm(TermConstant.BUYER_PHONE));
//		}
//		return createMultiCredit(logic, task, "570000", task.getTerm(TermConstant.BUYER_PHONE));
//	}

//	private static IsoMsg createMultiCredit(HostBtiLogic logic,TransactionTO task, String processCode, String remarkPrefix)
//			throws JetsException {
//		return createMultiCredit(logic, task, task.getPhoneNo(), task.getTerm(TermConstant.PIN), processCode, remarkPrefix);
//	}

//	private static IsoMsg createMultiCredit(HostBtiLogic logic, TransactionTO task, String phoneNo, String pin, String processCode,
//			String remarkPrefix) throws JetsException {
//		try {
//			IsoMsg isoMsg = (IsoMsg) HostIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
//			isoMsg.setItemWithLength(IsoMsg.PAN, formatPAN(phoneNo), 2); // bit2
//			isoMsg.setItem(IsoMsg.PROCESSING_CODE, processCode); // bit3
//			String amnt = IsoUtil.formatCurrency(task.getTerm(TermConstant.AMOUNT), 15, 2);
//			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt);
//			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
//			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
//			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
//			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
//			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
//			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, pin, 16); // bit52
//			// remark: '<bit2>/<yymmdd><bit41><bit11>'
//			String remark = formatPAN(remarkPrefix) + "/"
//					+ ISODate.getANSIDate(new Date())
//					+ isoMsg.getItem(IsoMsg.TERMINAL_ID)
//					+ isoMsg.getItem(IsoMsg.SYS_TRACE_NO);
//			String bit61 = createMultiCreditBit61(remark,
//					new MandiriMultiCharges(task.getFundFlows()));
//			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, bit61, 3); // bit61
//			return isoMsg;
//		} catch (JetsException je) {
//			throw je;
//		} catch (Exception e) {
//			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
//			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
//		}
//	}

	/*
	 * Only support for local currency
	 */
//	private static String createMultiCreditBit61(String remark,
//			MandiriMultiCharges charges) throws Exception {
//		MandiriRateMatrix rateMatrix = new MandiriRateMatrix(new MandiriRate(), new MandiriRate(), 0);
//		StringBuilder bit61 = new StringBuilder();
//		bit61
//				.append(CommonUtil.cutOrPad(charges.getSrac(), 20))
//				// source account
//				.append(CommonUtil.cutOrPad(charges.getDsac(), 20))
//				// destination account
//				.append(CommonUtils.getISORateAmount(rateMatrix.getIBTFromRate()))
//				// IBT from rate
//				.append(CommonUtils.getISORateAmount(rateMatrix.getIBTToRate())) // IBT
//				// to
//				// rate
//				.append(CommonUtils.getISORateAmount(rateMatrix.getEChannelFromRate())) // eChannel from
//				// rate
//				.append(CommonUtils.getISORateAmount(rateMatrix.getEChannelToRate())) // eChannel to rate
//				.append(CommonUtils.getISORateAmount(rateMatrix.getChargesIBTRate())) // charges ibt rate
//				.append(CommonUtils.getISORateAmount(rateMatrix.getChargesEChannelRate())) // charges echannel
//				// rate
//				.append(rateMatrix.getFromCurr()) // from currency
//				.append(rateMatrix.getToCurr()) // to currency
//				.append(IsoUtil.formatCurrency(charges.getAmount(), 15, 2)) // from
//				// amount
//				.append(IsoUtil.formatCurrency(charges.getAmount(), 15, 2)) // to
//				// amount
//				.append(CommonUtil.cutOrPad(remark, 40)) // remarks
//				.append(CommonUtil.cutOrPad(charges.getChargeSrac(), 20)) // charges
//				// from
//				// account
//				.append(StringUtils.repeat(" ", 2)) // account type
//				.append(IsoUtil.formatCurrency(charges.getTotalCharges(), 15, 2)) // charges amount in IDR
//				.append(IsoUtil.formatCurrency(charges.getTotalCharges(), 15, 2)) // charges amount in Debit
//				.append(CommonUtil.cutOrPad(charges.getToAccount(0), 20)) // charges
//				// account
//				// 1
//				.append(CommonUtil.cutOrPad(charges.getToAccount(1), 20)) // charges
//				// account
//				// 2
//				.append(CommonUtil.cutOrPad(charges.getToAccount(2), 20)) // charges
//				// account
//				// 3
//				.append(CommonUtil.cutOrPad(charges.getToAccount(3), 20)) // charges
//				// account
//				// 4
//				.append(IsoUtil.formatCurrency(charges.getToAmount(0), 15, 2)) // charges local amount 1
//				.append(IsoUtil.formatCurrency(charges.getToAmount(0), 15, 2)) // charges amount 1
//				.append(IsoUtil.formatCurrency(charges.getToAmount(1), 15, 2)) // charges local amount 2
//				.append(IsoUtil.formatCurrency(charges.getToAmount(1), 15, 2)) // charges amount 2
//				.append(IsoUtil.formatCurrency(charges.getToAmount(2), 15, 2)) // charges local amount 3
//				.append(IsoUtil.formatCurrency(charges.getToAmount(2), 15, 2)) // charges amount 3
//				.append(IsoUtil.formatCurrency(charges.getToAmount(3), 15, 2)) // charges local amount 4
//				.append(IsoUtil.formatCurrency(charges.getToAmount(3), 15, 2)); // charges amount 5
//		return bit61.toString();
//	}

//	public static IsoMsg createPrepaidRefill(HostBtiLogic logic, TransactionTO task, PrepaidInfo prepaidInfo) throws JetsException {
//		try {
//			IsoMsg isoMsg = (IsoMsg) HostIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
//			isoMsg.setItemWithLength(IsoMsg.PAN, formatPAN(task.getPhoneNo()), 2); // bit2
//			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "32" + prepaidInfo.getProcessCode().toString()); // bit3
////			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "320000"); // bit3
//			String amnt = IsoUtil.getISOAmount(task.getTerm(TermConstant.AMOUNT));
//			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt); // bit4
//			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
//			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
//			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
//			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
//			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
//			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, task.getTerm(TermConstant.PIN), 16); // bit52
//			String buyerPhoneNo = task.getMandatoryTerm(TermConstant.BUYER_PHONE);
//			if (prepaidInfo.isFormatted()) {
//				// pad with 0 if phoneNo without prefix(4) is less than 9
//				String phoneNoWithoutPrefix = buyerPhoneNo.substring(4, buyerPhoneNo.length());
//				buyerPhoneNo = buyerPhoneNo.substring(0, 4) + CommonUtil.zeroPadLeft(phoneNoWithoutPrefix, 9);
//			}
//			// bit61:
//			StringBuilder bit61 = new StringBuilder();
//			bit61.append(CommonUtil.cutOrPad(task.getMandatoryTerm(TermConstant.SRAC), 20)) // source
//				// account
//				.append(CommonUtil.cutOrPad(buyerPhoneNo, 20)) // MSISDN
//				.append(buyerPhoneNo.substring(0, 4)) // area code
//				.append(StringUtils.repeat(" ", 20)) // to account
//				.append(StringUtils.repeat(" ", 22)) // rrno
//				.append(StringUtils.repeat("0", 4)) // biller code
//				.append(amnt) // debit amount
//				.append(amnt) // credit amount
//				.append(StringUtils.repeat("0", 17)); // no commission
//
//			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, bit61.toString(), 3); // bit61
//			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_3, createPrepaidBit126(buyerPhoneNo, task.getTerm(TermConstant.AMOUNT), prepaidInfo), 3);// bit126
//			
////			System.out.println("Request: " + isoMsg);
////			logger.debug("Request: " + isoMsg);
//			return isoMsg;
//		} catch (JetsException je) {
//			throw je;
//		} catch (Exception e) {
//			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
//			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
//		}
//	}

//	private static String createPrepaidBit126(String buyerPhoneNo, String amnt, PrepaidInfo prepaidInfo) throws Exception {
//		MandiriPrepaidBit126 bit126 = new MandiriPrepaidBit126();
//		bit126.setUtilCode(prepaidInfo.getUtilCode());
//		bit126.setBillPhoneNo(CommonUtil.cutOrPad(buyerPhoneNo, 19));
//		bit126.setBillVoucher(CommonUtil.cutOrPad(prepaidInfo.getBillVoucher(), 18));
//		bit126.setBillCustNo(StringUtils.repeat(" ", 11));
//		bit126.setBillCustName(StringUtils.repeat(" ", 25));
//		bit126.setNpwp(StringUtils.repeat(" ", 15));
//		bit126.setKandatelNo("0000");
//		bit126.setBillStatusCode(" ");
//		bit126.setBillRefNum1(StringUtils.repeat(" ", 11));
//		bit126.setBillRefNum2(StringUtils.repeat(" ", 11));
//		bit126.setBillRefNum3(StringUtils.repeat(" ", 11));
//		bit126.setBillAmount1(IsoUtil.formatCurrency(amnt, 10, 2));
//		bit126.setBillAmount2(StringUtils.repeat("0", 12));
//		bit126.setBillAmount3(StringUtils.repeat("0", 12));
//		if (prepaidInfo.isIndosatData()) {
//			bit126.setIndosatData(StringUtils.repeat(" ", 160));
//		}
//		return bit126.toBit126Str();
//	}

	public static IsoMsg createList8Account(HostBtiLogic logic, TransactionTO task) throws JetsException {
		return createList8Account(logic, task.getPhoneNo(), task);
	}

	public static DjpIsoMsg createList8AccountDjp(DjpHostBtiLogic logic, TransactionTO task) throws JetsException {
		return createList8AccountDjp(logic, task.getPhoneNo(), task);
	}

	public static SddIsoMsg createList8AccountSdd(SddHostBtiLogic logic, TransactionTO task) throws JetsException {
		return createList8AccountSdd(logic, task.getPhoneNo(), task);
	}

	public static IsoMsg createList8Account(HostBtiLogic logic, String phoneNo, TransactionTO task)
			throws JetsException {
		try {
			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
//			isoMsg.setItemWithLength(IsoMsg.PAN, CommonUtil.cutOrPad(formatPAN(phoneNo), 19), 2);
			isoMsg.setItemWithLength(IsoMsg.PAN, formatPAN(phoneNo), 2); // bit2
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "070000"); // bit3
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, task.getTerm(TermConstant.PIN), 16); // bit52
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, "", 3); // bit61
//			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, CommonUtil.cutOrPad(task.getTerm(TermConstant.SRAC), 20), 3); // bit61
//			System.out.println("Send To DSP: " + isoMsg);
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static DjpIsoMsg createList8AccountDjp(DjpHostBtiLogic logic, String phoneNo, TransactionTO task)
			throws JetsException {
		try {
			DjpIsoMsg isoMsg = (DjpIsoMsg) SultengIsoMsgFactory
					.createHostIsoMsgDjp(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
//			isoMsg.setItemWithLength(IsoMsg.PAN, CommonUtil.cutOrPad(formatPAN(phoneNo), 19), 2);
			isoMsg.setItemWithLength(IsoMsg.PAN, formatPAN(phoneNo), 2); // bit2
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "070000"); // bit3
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, task.getTerm(TermConstant.PIN), 16); // bit52
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, "", 3); // bit61
//			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, CommonUtil.cutOrPad(task.getTerm(TermConstant.SRAC), 20), 3); // bit61
//			System.out.println("Send To DSP: " + isoMsg);
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static SddIsoMsg createList8AccountSdd(SddHostBtiLogic logic, String phoneNo, TransactionTO task)
			throws JetsException {
		try {
			SddIsoMsg isoMsg = (SddIsoMsg) SultengIsoMsgFactory
					.createHostIsoMsgSdd(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
//			isoMsg.setItemWithLength(IsoMsg.PAN, CommonUtil.cutOrPad(formatPAN(phoneNo), 19), 2);
			isoMsg.setItemWithLength(IsoMsg.PAN, formatPAN(phoneNo), 2); // bit2
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "070000"); // bit3
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, task.getTerm(TermConstant.PIN), 16); // bit52
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, "", 3); // bit61
//			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, CommonUtil.cutOrPad(task.getTerm(TermConstant.SRAC), 20), 3); // bit61
//			System.out.println("Send To DSP: " + isoMsg);
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createCCBalance(HostBtiLogic logic, TransactionTO task) throws JetsException {
		return createCCBalance(logic, task, task.getTerm(TermConstant.SRAC), task.getTerm(TermConstant.DOB));
	}

	public static IsoMsg createCCBalance(HostBtiLogic logic, TransactionTO task, String accountNo, String dob)
			throws JetsException {
		try {
			Assert.notNull(accountNo, "account no is null!");

			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
			isoMsg.setItemWithLength(IsoMsg.PAN, formatPAN(task.getPhoneNo()), 2); // bit2
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "140001"); // bit3
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, task.getTerm(TermConstant.PIN), 16); // bit52
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_2, CommonUtil.cutOrPad(dob, 6), 3); // bit62
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_3, CommonUtil.cutOrPad(accountNo, 20), 3); // bit126
//			System.out.println("Send To DSP: " + isoMsg);
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public static IsoMsg createCCPaymentSettlement(HostBtiLogic logic, TransactionTO task) throws JetsException {
		try {
			IsoMsg isoMsg = (IsoMsg) SultengIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
			isoMsg.setItemWithLength(IsoMsg.PAN, formatPAN(task.getPhoneNo()), 2); // bit2
			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "340002"); // bit3
			String amnt = IsoUtil.getISOAmount(task.getMandatoryTerm(TermConstant.AMOUNT));
			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amnt);// bit4
			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, task.getTerm(TermConstant.PIN), 16); // bit52
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, task.getTerm(TermConstant.SRAC), 3); // bit61
			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_3, task.getTerm(TermConstant.BILL_KEY1), 3); // bit126
//			System.out.println("Send To DSP: " + isoMsg);
			return isoMsg;
		} catch (JetsException je) {
			throw je;
		} catch (Exception e) {
			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

//	public static IsoMsg createUnifiedBillInquiry(HostBtiLogic logic, TransactionTO task, String phoneNo, String pin,Logger logger)
//			throws JetsException {
//		try {
//			IsoMsg isoMsg = (IsoMsg) HostIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
//			isoMsg.setItemWithLength(IsoMsg.PAN, HostIsoMsgFactory.formatPAN(phoneNo), 2); // bit2
//			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "380000"); // bit3
//			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
//			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
//			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
//			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
//			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
//			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, pin, 16); // bit52
//			String bit61 = "UBP"
//					+ "6017"
//					+ CommonUtil.cutOrPad(task.getTermAsString(TermConstant.BANK_REF), 5);
//			if("88188".equals(task.getTermAsString(TermConstant.BANK_REF)) ) {
//				bit61 = bit61
//						+ CommonUtil.cutOrPad("01", 2)
//						+ "000000"
//						+ CommonUtil.padRight("0", '0', 20)
//						+ "I"
//						+ CommonUtil.cutOrPad(task.getTermAsString(TermConstant.BILL_KEY1), 20)
//						+ CommonUtil.cutOrPad(task.getTermAsString(TermConstant.AMOUNT), 20)
//						+ CommonUtil.cutOrPad("", 20)
//						+ CommonUtil.cutOrPad(task.getTermAsString(TermConstant.SRAC), 20)
//						+ CommonUtil.cutOrPad("", 20)
//						+ CommonUtil.cutOrPad("", 20)
//						+ CommonUtil.padRight("0", '0', 17)
//						+ CommonUtil.padRight("0", '0', 17)
//						+ CommonUtil.cutOrPad("", 20)
//						+ CommonUtil.cutOrPad("", 30)
//						+ CommonUtil.cutOrPad("", 20);
//			} else if (JetsConstant.BILLER_PLN_PREPAID.equals(task.getTermAsString(TermConstant.BANK_REF))) {
//				bit61 = bit61
//						+ CommonUtil.cutOrPad("00", 2)
//						+ "FFFFFF"
//						+ CommonUtil.cutOrPad(task.getTermAsString(TermConstant.BILL_KEY1), 20)
//						+ "I"
//						+ CommonUtil.cutOrPad(task.getTermAsString(TermConstant.BILL_KEY1), 20)
//						+ CommonUtil.cutOrPad(task.getTermAsString(TermConstant.AMOUNT), 20)
//						+ CommonUtil.cutOrPad("", 20)
//						+ CommonUtil.cutOrPad(task.getTermAsString(TermConstant.SRAC), 20)
//						+ CommonUtil.cutOrPad("", 20)
//						+ CommonUtil.cutOrPad(task.getTermAsString(TermConstant.BANK_REF), 20)
//						+ CommonUtil.padRight("0", '0', 10) + CommonUtil.cutOrPad("", 7)
//						+ CommonUtil.padRight("0", '0', 10) + CommonUtil.cutOrPad("", 7)
//						+ CommonUtil.cutOrPad("0", 20)
//						+ CommonUtil.cutOrPad("", 30)
//						+ CommonUtil.cutOrPad("", 20);
//			} else {
//				bit61 = bit61
//						+ CommonUtil.cutOrPad("00", 2)
//						+ "FFFFFF"
//						+ CommonUtil.cutOrPad(task.getTermAsString(TermConstant.BILL_KEY1), 20)
//						+ "I"
//						+ CommonUtil.cutOrPad(task.getTermAsString(TermConstant.BILL_KEY1), 20)
//						+ CommonUtil.cutOrPad("", 20)
//						+ CommonUtil.cutOrPad("", 20)
//						+ CommonUtil.cutOrPad(task.getTermAsString(TermConstant.SRAC), 20)
//						+ CommonUtil.cutOrPad("", 20)
//						+ CommonUtil.cutOrPad(task.getTermAsString(TermConstant.BANK_REF), 20)
//						+ CommonUtil.padRight("0", '0', 10) + CommonUtil.cutOrPad("", 7)
//						+ CommonUtil.padRight("0", '0', 10) + CommonUtil.cutOrPad("", 7)
//						+ CommonUtil.cutOrPad("0", 20)
//						+ CommonUtil.cutOrPad("", 30)
//						+ CommonUtil.cutOrPad("", 20);
//			}
//			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, bit61, 3); // bit61
//			
////			logger.debug("Request Iso : " + isoMsg);
////			System.out.print("Send Iso : " + isoMsg);
//			return isoMsg;
//		} catch (JetsException je) {
//			throw je;
//		} catch (Exception e) {
//			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
//			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
//		}
//	}

//	public static IsoMsg createUnifiedBillSettlement(HostBtiLogic logic, TransactionTO task, String phoneNo, String pin,Logger logger)
//			throws JetsException {
//		try {
//			IsoMsg isoMsg = (IsoMsg) HostIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
//			isoMsg.setItemWithLength(IsoMsg.PAN, HostIsoMsgFactory.formatPAN(phoneNo), 2); // bit2
//			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "390000"); // bit3
//			if (!StringUtils.isEmpty(task.getTermAsString(TermConstant.AMOUNT))) {
//				String amount = IsoUtil.getISOAmount(task.getTermAsString(TermConstant.AMOUNT));
//				isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amount); // bit4
//			}
//			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
//			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
//			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
//			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
//			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
//			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, pin, 16); // bit52
//			
//			String bit61Inq = task.getMandatoryTerm(TermConstant.BIT61);
//			String bit124 = task.getMandatoryTerm(TermConstant.BIT124);
//			String billingFlagMap = task.getTerm(TermConstant.BILLING_FLAG_MAP);
//			if (StringUtils.isEmpty(billingFlagMap))
//				billingFlagMap = "FFFFFF";
//			
//			String bit61 =
//					bit61Inq.substring(0,14)
//					+ billingFlagMap
//					+ bit61Inq.substring(20,40)
//					+ "P"
//					+ bit61Inq.substring(41,121)
//					+ CommonUtil.cutOrPad("", 20)
//					+ CommonUtil.cutOrPad(bit124.substring(60,79), 20) // comp acc no
//					+ CommonUtil.padLeft(bit124.substring(40,50), '0', 17) // comp charge amount
//					+ CommonUtil.padLeft(bit124.substring(50,60), '0', 17) // comp acc amount
//					+ CommonUtil.cutOrPad(bit124.substring(82,101), 20) // charge acc no
//					+ bit124.substring(507,537) // custname
//					+ bit124.substring(482,507); // custcode
//			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, bit61, 3); // bit61
//			
////			logger.debug("Request Iso : " + isoMsg);
////			System.out.print("Send Iso : " + isoMsg);
//			return isoMsg;
//		} catch (JetsException je) {
//			throw je;
//		} catch (Exception e) {
//			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
//			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
//		}
//	}

	// for SOA UBP, using bill key 1,2 & 3
//	public static IsoMsg createNewUnifiedBillInquiry(HostBtiLogic logic, TransactionTO task,Logger logger)
//			throws JetsException {
//		try {
//			IsoMsg isoMsg = (IsoMsg) HostIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
//			isoMsg.setItemWithLength(IsoMsg.PAN, HostIsoMsgFactory.formatPAN(task.getPhoneNo()), 2); // bit2
//			isoMsg.setItem(IsoMsg.PROCESSING_CODE, "380000"); // bit3
//			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
//			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
//			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
//			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
//			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
//			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, task.getTerm(TermConstant.PIN), 16); // bit52
//			String bit61 = "UBP"
//					+ "6017"
//					+ CommonUtil.cutOrPad(task.getTermAsString(TermConstant.BANK_REF), 5)
//					+ CommonUtil.cutOrPad("00", 2)
//					+ "000000"
//					+ CommonUtil.cutOrPad(task.getTermAsString(TermConstant.BILL_KEY1), 20)
//					+ "I"
//					+ CommonUtil.cutOrPad(task.getTermAsString(TermConstant.BILL_KEY1), 20)
//					+ CommonUtil.cutOrPad(task.getTermAsString(TermConstant.BILL_KEY2), 20)
//					+ CommonUtil.cutOrPad(task.getTermAsString(TermConstant.BILL_KEY3), 20)
//					+ CommonUtil.cutOrPad(task.getTermAsString(TermConstant.SRAC), 20)
//					+ CommonUtil.cutOrPad("", 20)
//					+ CommonUtil.cutOrPad("", 20)
//					+ CommonUtil.padRight("0", '0', 17)
//					+ CommonUtil.padRight("0", '0', 17)
//					+ CommonUtil.cutOrPad("", 20)
//					+ CommonUtil.cutOrPad("", 30)
//					+ CommonUtil.cutOrPad("", 20);
//			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, bit61, 3); // bit61
//			
//			return isoMsg;
//		} catch (JetsException je) {
//			throw je;
//		} catch (Exception e) {
//			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
//			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
//		}
//	}

//	public static IsoMsg createEcashActivation(HostBtiLogic logic, TransactionTO task, String phoneNo,
//				String pin, String processCode, String bankRef)
//			throws JetsException {
//		try {
//			IsoMsg isoMsg = (IsoMsg) HostIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
//			isoMsg.setItemWithLength(IsoMsg.PAN, HostIsoMsgFactory.formatPAN(phoneNo), 2); // bit2
//			isoMsg.setItem(IsoMsg.PROCESSING_CODE, processCode); // bit3
//			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, IsoUtil.getISOAmount("0")); // bit4
//			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
//			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
//			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
//			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
//			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
//			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, pin, 16); // bit52
//			String inqType = "I";
//			if (task.getState() == 0)
//				inqType = "P";
//			String bit61 = "UBP" + "6017" + CommonUtil.cutOrPad(bankRef, 5) + "01" +
//					"000000" +  //Billing code flag map
//					CommonUtil.padRight("0", '0', 20) + //Combination bill key 123
//					inqType + //Inquiry type, I = Inquiry, P = Payment
//					CommonUtil.cutOrPad(task.getTermAsString(TermConstant.BUYER_PHONE), 20) + //Bill Key 1
//					CommonUtil.cutOrPad(" ", 20) + //Bill Key 2
//					CommonUtil.cutOrPad(" ", 20) + //Bill Key 3
//					CommonUtil.cutOrPad(task.getTermAsString(TermConstant.SRAC), 20) + //Debiting cust acc no
//					CommonUtil.cutOrPad(HostIsoMsgFactory.formatPAN(phoneNo), 20) + //CIF
//					CommonUtil.cutOrPad(" ", 20) + //Company acc no
//					CommonUtil.cutOrPad(IsoUtil.getISOAmount("0"), 17) + //Cust charge amount
//					CommonUtil.cutOrPad(IsoUtil.getISOAmount("0"), 17) + //Company charge amount
//					CommonUtil.cutOrPad(" ", 20) + //Charge acc no
//					CommonUtil.cutOrPad(task.getTermAsString(TermConstant.CUSTOMER_NAME), 30) + //Cust name
//					CommonUtil.cutOrPad(" ", 20) + //Cust code
//					CommonUtil.cutOrPad("20", 2); //Account type
//			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, bit61, 3); // bit61
//			String bit126 = "& 0000200348! Q100326 " + "8001" +
//					CommonUtil.cutOrPad(task.getTermAsString(TermConstant.BUYER_PHONE), 19) + //Moku number
//					CommonUtil.cutOrPad(" ", 30 + 19 + 254); //Name + CIF + filler
//			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_3, bit126, 3); // bit126
//			
////			logger.debug("Request Iso : " + isoMsg);
//			return isoMsg;
//		} catch (JetsException je) {
//			throw je;
//		} catch (Exception e) {
//			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
//			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
//		}
//	}

//	public static IsoMsg createEcashTopup(HostBtiLogic logic, TransactionTO task, String phoneNo,
//			String pin, String processCode, String bankRef)
//					throws JetsException {
//		try {
//			IsoMsg isoMsg = (IsoMsg) HostIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST);
//			isoMsg.setItemWithLength(IsoMsg.PAN, HostIsoMsgFactory.formatPAN(phoneNo), 2); // bit2
//			isoMsg.setItem(IsoMsg.PROCESSING_CODE, processCode); // bit3
//			String amount = IsoUtil.getISOAmount(task.getTermAsString(TermConstant.AMOUNT));
//			isoMsg.setItem(IsoMsg.TRANSACTION_AMT, amount); // bit4
//			isoMsg.setItem(IsoMsg.SYS_TRACE_NO, logic.getNextSequence()); // bit11
//			isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
//			isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
//			isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, logic.getTerminalId(), 8); // bit41
//			isoMsg.setItem(IsoMsg.CURR_CODE, LOCAL_CURR_BIT49); // bit49
//			isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, pin, 16); // bit52
//			String inqType = "I";
//			if (task.getState() == StateConstant.AIRTIME_REFILL_SETTLE)
//				inqType = "P";
//			String bit61 = "UBP" + "6017" + CommonUtil.cutOrPad(bankRef, 5) + "01" +
//					"FFFFFF" +  //Billing code flag map
//					CommonUtil.padRight(task.getTermAsString(TermConstant.BUYER_PHONE), '0', 20) + //Combination bill key 123
//					inqType + //Inquiry type, I = Inquiry, P = Payment
//					CommonUtil.cutOrPad(task.getTermAsString(TermConstant.BUYER_PHONE), 20) + //Bill Key 1
//					CommonUtil.cutOrPad(task.getTermAsString(TermConstant.AMOUNT), 20) + //Bill Key 2
//					CommonUtil.cutOrPad(" ", 20) + //Bill Key 3
//					CommonUtil.cutOrPad(task.getTermAsString(TermConstant.SRAC), 20) + //Debiting cust acc no
//					CommonUtil.cutOrPad(HostIsoMsgFactory.formatPAN(phoneNo), 20) + //CIF
//					CommonUtil.cutOrPad(" ", 20) + //Company acc no
//					CommonUtil.cutOrPad(IsoUtil.getISOAmount("0"), 17) + //Cust charge amount
//					CommonUtil.cutOrPad(IsoUtil.getISOAmount("0"), 17) + //Company charge amount
//					CommonUtil.cutOrPad(" ", 20) + //Charge acc no
//					CommonUtil.cutOrPad(task.getTermAsString(TermConstant.CUSTOMER_NAME), 30) + //Cust name
//					CommonUtil.cutOrPad(" ", 20) + //Cust code
//					CommonUtil.cutOrPad("20", 2); //Account type
//			isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, bit61, 3); // bit61
//			String bit126 = task.getTermAsString(TermConstant.BIT126);
//			if ((task.getState() == StateConstant.AIRTIME_REFILL_SETTLE) &&
//					(!StringUtils.isEmpty(bit126))) {
//				isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_3, bit126, 3); // bit126
//			}
//
//			logger.debug("Request Iso : " + isoMsg);
//			return isoMsg;
//		} catch (JetsException je) {
//			throw je;
//		} catch (Exception e) {
//			logger.warn("[#" + task.getMsgLogNo() + "] Exception for " + task, e);
//			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
//		}
//	}

}
