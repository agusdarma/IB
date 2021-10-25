//
//package id.co.emobile.jets.mmbs.bti.logic;
//
//import java.io.IOException;
//import java.util.Set;
//
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.emobile.jets.mmbs.lib.data.JetsConstant;
//import com.emobile.jets.mmbs.lib.data.StateConstant;
//import com.emobile.jets.mmbs.lib.data.TermConstant;
//import com.emobile.jets.mmbs.lib.data.TransactionTO;
//import com.emobile.jets.mmbs.lib.service.JetsException;
//import com.emobile.jets.mmbs.lib.util.CommonUtil;
//import com.emobile.jets.mmbs.lib.util.ResultCode;
//
//import id.co.emobile.jets.mmbs.bti.BtiIsoDelegateAgent;
//import id.co.emobile.jets.mmbs.bti.iso.HostIsoMsgFactory;
//import id.co.emobile.jets.mmbs.bti.iso.IsoMsg;
//
//public class NewPaymentLogic extends HostBtiLogic {
//	private static final Logger LOG = LoggerFactory.getLogger(NewPaymentLogic.class);
//	
//	private Set<String> prefixSet;
//	
//	@Autowired
//	private AccountBalanceLogic accountBalanceInquiryLogic;
//
//	@Override
//	protected Logger getLogger() {
//		return LOG;
//	}
//	
//	@Override
//	public void solve(BtiIsoDelegateAgent agent, TransactionTO task) 
//			throws JetsException, IOException {
//		// State : GetDefaultAccount, Inquiry, Settlement
//		int state = task.getState();
//		if (state == StateConstant.BILL_PAYMENT_GET_AMOUNT) {
//			accountBalanceInquiryLogic.solve(agent, task);
//		} else if (state == StateConstant.BILL_PAYMENT_INQ) {
//			solveDefaultAccountInquiry(agent, task);
//			solveInquiry(agent, task);
//		} else if (state == StateConstant.BILL_PAYMENT_SETTLE) {
//			super.solve(agent, task);
//		} else {
//			LOG.error("Unknown state: " + state);
//			throw new JetsException("Unknown New Payment state: " + state, ResultCode.BTI_UNKNOWN_STATE);
//		}
//	}
//
//	private void solveDefaultAccountInquiry(BtiIsoDelegateAgent agent, TransactionTO task) throws JetsException, IOException {
//		// Check for default account if empty
//		String srac = task.getTerm(TermConstant.SRAC);
//		if (srac.length() < 3) {
//			task.setTerm(TermConstant.ACC_INDEX, srac);
//			srac = getDefaultAccount(agent, task);
//			task.setTerm(TermConstant.SRAC, srac);
//		}
////		if (StringUtils.isEmpty(task.getTermAsString(TermConstant.SRAC))) {
////			getDefaultAccountLogic.solve(agent, task);
////			task.setTerm(TermConstant.SRAC, task.getTerm(TermConstant.DEFAULT_ACCOUNT));
////			task.setTerm(TermConstant.IS_GET_DEF_ACCOUNT, true);
////		} else {
////			task.setTerm(TermConstant.DEFAULT_ACCOUNT, task.getTerm(TermConstant.SRAC));
////		}
//	}
//
//	private void solveInquiry(BtiIsoDelegateAgent agent, TransactionTO task) throws JetsException, IOException {
//		IsoMsg isoMsg = null;
//		String payeeCode = task.getTerm(TermConstant.PAYEE_CODE);
//		String billAcctNo = task.getTerm(TermConstant.BILL_KEY1);
//		if (JetsConstant.BILLER_CODE_PSTN.equals(payeeCode) ||
//				JetsConstant.BILLER_CODE_FLEXI_POSTPAID.equals(payeeCode)) {
//			billAcctNo = formatBillPhoneNo(billAcctNo);
//		}
//		else if (JetsConstant.BILLER_CODE_SPEEDY.equals(payeeCode)){
//			billAcctNo = CommonUtil.zeroPadLeft(billAcctNo, 13);
//		}
//		// fill term BillAcctNo with value sent to host
//		task.setTerm(TermConstant.BILL_ACCT_NO, billAcctNo);
//		
//		isoMsg = HostIsoMsgFactory.createBillInquirySulteng(this, task, billAcctNo);
//		IsoMsg rspIsoMsg = processToHost(agent, isoMsg);
//		String bit4Temp = rspIsoMsg.getItem(IsoMsg.TRANSACTION_AMT);
//		int amount = Integer.parseInt(bit4Temp);
//		LOG.debug("Check amount first: {}", amount);
//		String responseCode = rspIsoMsg.getItem(IsoMsg.RESPONSE_CODE);
//		LOG.debug("Bit 39: {}", responseCode);
//		if(amount == 0 && HOST_SUCCESS_CODE.equals(responseCode)){
//			rspIsoMsg.setItem(IsoMsg.RESPONSE_CODE, ResultCode.JETS_INVALID_AMOUNT);
//		}
//		if (isSuccess(rspIsoMsg)) {
//			// PLN and airline
//			// bit61 format: BookingCode<20>+AreaCode<4>+RRNO<22>
//			String bit61 = rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA);
////			task.setTerm(TermConstant.BREF, bit61.substring(24));
//			task.setTerm(TermConstant.BIT61, rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA));
//			if (JetsConstant.BILLER_CODE_PSTN.equals(payeeCode) || // PSTN
//					JetsConstant.BILLER_CODE_SPEEDY.equals(payeeCode) ||
//					JetsConstant.BILLER_CODE_FLEXI_POSTPAID.equals(payeeCode)) {
//				String divreCode = bit61.substring(40, 42);
//				String datelCode = bit61.substring(42, 46);
//				Integer billCount = 1;
//				try {
//					billCount = Integer.parseInt(bit61.substring(46, 47));
//				} catch (NumberFormatException e) {
//					billCount = 0;
//				}
//				LOG.debug("Number of Bills: {}", billCount);
//				String billRef1 = "";
//				double billAmount1 = 0;
//				String billRef2 = "";
//				double billAmount2 = 0;
//				String billRef3 = "";
//				double billAmount3 = 0;
//				if (billCount > 0) {
//					billRef1 = CommonUtil.trim(bit61.substring(47, 58));
//					billAmount1 = Double.parseDouble(bit61.substring(58, 70));
//				}
//				if (billCount > 1) {
//					billRef2 = CommonUtil.trim(bit61.substring(70, 81));
//					billAmount2 = Double.parseDouble(bit61.substring(81, 93));
//				}
//				if (billCount > 2) {
//					billRef3 = CommonUtil.trim(bit61.substring(93, 104));
//					billAmount3 = Double.parseDouble(bit61.substring(104, 116));
//				}
//				String customerName = CommonUtil.trim(bit61.substring(47+(23*billCount),47+30+(23*billCount)));
//				String npwpNo = CommonUtil.trim(bit61.substring(47+30+(23*billCount)));
////				String customerName = CommonUtil.trim(bit61.substring(116, 146));
////				String npwpNo = CommonUtil.trim(bit61.substring(146, 161));
//				
//				double totalAmount = billAmount1 + billAmount2 + billAmount3;
//				String billRef = "";
//				task.setTerm(TermConstant.DIVRE_CODE, divreCode);
//				task.setTerm(TermConstant.DATEL_CODE, datelCode);
//				task.setTerm(TermConstant.BILL_COUNT, billCount);
//				task.setTerm(TermConstant.BILL_REF_1, billRef1);
//				task.setTerm(TermConstant.BILL_REF_2, billRef2);
//				task.setTerm(TermConstant.BILL_REF_3, billRef3);
//				LOG.debug("bill 1: {}", billRef1);
//				LOG.debug("bill 2: {}", billRef2);
//				LOG.debug("bill 3: {}", billRef3);
//				if(!StringUtils.isEmpty(billRef1)){
//					billRef = billRef+billRef1;
//					billRef = billRef+",";
//				}
//				if(!StringUtils.isEmpty(billRef2)){
//					billRef = billRef+billRef2;
//					billRef = billRef+",";
//				}
//				if(!StringUtils.isEmpty(billRef3)){
//					billRef = billRef+billRef3;
//					billRef = billRef+",";
//				}
//				billRef = billRef.substring(0, billRef.length()-1);
//				task.setTerm(TermConstant.BILL_REF, billRef);
//				task.setTerm(TermConstant.CUSTOMER_NAME, customerName);
//				task.setTerm(TermConstant.NPWP_NO, npwpNo);
//				task.setTerm(TermConstant.AMOUNT, totalAmount);
//			} else if (JetsConstant.BILLER_CODE_HALO.equals(payeeCode)) { // Kartu HALO
//				String productCode = bit61.substring(40, 46);
//				String billCount = bit61.substring(46, 47);
//				String billRef = CommonUtil.trim(bit61.substring(47, 58));
//				double billAmount = Double.parseDouble(bit61.substring(58, 70));
//				String customerName = CommonUtil.trim(bit61.substring(70));
//				
//				task.setTerm(TermConstant.PRODUCT_CODE, productCode);
//				task.setTerm(TermConstant.BILL_COUNT, billCount);
//				task.setTerm(TermConstant.BILL_REF_1, billRef);
//				task.setTerm(TermConstant.CUSTOMER_NAME, customerName);
//				task.setTerm(TermConstant.AMOUNT, billAmount);
//			} else if (JetsConstant.BILLER_CODE_BPJS.equals(payeeCode)) { // Kartu HALO
//				String jmlBulan = bit61.substring(43, 45);
//				String jmlAnggotaKeluarga = bit61.substring(60, 63);
//				String customerName = CommonUtil.trim(bit61.substring(114, 164));	
//				String noRef = CommonUtil.trim(bit61.substring(bit61.length()-32, bit61.length()));
//				int temp = Integer.parseInt(jmlAnggotaKeluarga);				
//				int temp2 = Integer.parseInt(jmlBulan);
//				task.setTerm(TermConstant.JUMLAH_ANGGOTA_KELUARGA, Integer.toString(temp));
//				task.setTerm(TermConstant.BILL_COUNT, Integer.toString(temp2));
//				task.setTerm(TermConstant.BILL_REF, noRef);
//				task.setTerm(TermConstant.CUSTOMER_NAME, customerName);
//				task.setTerm(TermConstant.AMOUNT, rspIsoMsg.getItem(IsoMsg.TRANSACTION_AMT));
//				LOG.debug("noRef: {}", noRef);
//				LOG.debug("jmlAnggotaKeluarga: {}", temp);
//				LOG.debug("jmlBulan: {}", temp2);
//				LOG.debug("customerName: {}", customerName);
//			} else if (JetsConstant.BILLER_CODE_XL_POSTPAID.equals(payeeCode)) { // XL_POSTPAID
//				String productCode = bit61.substring(40, 46);
//				String billCount = bit61.substring(46, 47);
////				String billRef = CommonUtil.trim(bit61.substring(47, 58));
//				String custNo = bit61.substring(47, 63);
//				String bit4 = rspIsoMsg.getItem(IsoMsg.TRANSACTION_AMT);
////				double billAmount = Double.parseDouble(bit61.substring(63, 75));
//				double billAmount = Double.parseDouble(bit4);
//				String customerName = CommonUtil.trim(bit61.substring(75,105));
//				String dueDate = CommonUtil.trim(bit61.substring(105,113));
//				
//				task.setTerm(TermConstant.PRODUCT_CODE, productCode);
//				task.setTerm(TermConstant.BILL_COUNT, billCount);
////				task.setTerm(TermConstant.BILL_REF_1, billRef);
//				task.setTerm(TermConstant.DUE_DATE, dueDate);
//				task.setTerm(TermConstant.CUSTOMER_NAME, customerName);
//				task.setTerm(TermConstant.CUSTOMER_NO, custNo);
//				task.setTerm(TermConstant.AMOUNT, billAmount);
//			} else if (JetsConstant.BILLER_CODE_INDOVISION.equals(payeeCode)) { // Indovision
//				String customerName = CommonUtil.trim(bit61.substring(40, 70));
//				String period = CommonUtil.trim(bit61.substring(70, 87));
//				double billAmount = Double.parseDouble(bit61.substring(87, 99));
//				
//				task.setTerm(TermConstant.CUSTOMER_NAME, customerName);
//				task.setTerm(TermConstant.PERIOD, period);
//				task.setTerm(TermConstant.AMOUNT, billAmount);
//			} 
////			else if (JetsConstant.BILLER_CODE_AORA_TV.equals(payeeCode)) { // AORA TV
////				String customerID = bit61.substring(27, 40);
////				String aoraTvCode = bit61.substring(40, 46);
////				String totalBill = bit61.substring(46, 47);
////				String packageName = bit61.substring(47, 58);
////				String billAmount = bit61.substring(58, 70);
////				String customerName = bit61.substring(70, 100);
////				String billingCode = bit61.substring(100, bit61.length());
////				
////				task.setTerm(TermConstant.CUSTOMER_NAME, customerName.trim());
////				task.setTerm(TermConstant.AMOUNT, billAmount);
////				task.setTerm(TermConstant.CUSTOMER_NO, customerID.trim());
////				task.setTerm(TermConstant.PACKAGE_NAME, packageName.trim());
////				task.setTerm(TermConstant.BILLING_CODE, billingCode.trim());
//////				String productCode = bit61.substring(40, 46);
//////				String billCount = bit61.substring(46, 47);
//////				String productName = CommonUtil.trim(bit61.substring(47, 58));
//////				double billAmount = Double.parseDouble(bit61.substring(58, 70));
//////				String customerName = CommonUtil.trim(bit61.substring(70, 100));
//////				String billRef = CommonUtil.trim(bit61.substring(100));
////				
//////				task.setTerm(TermConstant.PRODUCT_CODE, productCode);
//////				task.setTerm(TermConstant.BILL_COUNT, billCount);
//////				task.setTerm(TermConstant.PRODUCT_NAME, productName);
//////				task.setTerm(TermConstant.BILL_REF_1, billRef);
//////				task.setTerm(TermConstant.CUSTOMER_NAME, customerName);
//////				task.setTerm(TermConstant.AMOUNT, billAmount);
////			} 
////			else if(JetsConstant.BILLER_CODE_PDAM_TIRTANADI.equals(payeeCode) || 
////					JetsConstant.BILLER_CODE_PDAM_TIRTABULIAN.equals(payeeCode)){
////				String customerID = bit61.substring(27, 42);
////				String customerName = bit61.substring(42, 72);
////				String customerAddress = bit61.substring(72, 102);
////				String groupRate = bit61.substring(102, 107);
////				String billReportCount = bit61.substring(107, 109);
////				String billInfo = bit61.substring(109, 110);
////				String billPeriod = bit61.substring(110, 116);
////				String billAcc = bit61.substring(116, 126);
////				String billAmount = bit61.substring(126, 138);
////				String penalti = bit61.substring(138, 146);
////				String kubikasi = bit61.substring(146, 163);
////				
////				// amount + penalti
////				Double amountBill = Double.valueOf(billAmount).doubleValue();
////				Double amountPenalti = Double.valueOf(penalti).doubleValue();
////				String totalAmount = "";
////				try {
////					totalAmount = CommonUtil.zeroPadLeft(String.format("%.0f", amountBill + amountPenalti), 12); //CommonUtils.getISOAmount(amountBill + amountPenalti, 12);
////				} catch (Exception e) {
////					throw (JetsException) e;
////				} 
////				
////				task.setTerm(TermConstant.CUSTOMER_NAME, customerName);
////				task.setTerm(TermConstant.CUSTOMER_ADDRESS, customerAddress);
////				task.setTerm(TermConstant.AMOUNT, totalAmount);
////				task.setTerm(TermConstant.AMOUNT_DISPLAY, (amountBill + amountPenalti));
////				task.setTerm(TermConstant.PERIOD, billPeriod);
////			}
////			else if(JetsConstant.BILLER_CODE_ESAMSAT.equals(payeeCode)){
////				String trxID = bit61.substring(27, 47);
////				String kodeBayar = bit61.substring(47, 63);
////				String noPolisi = bit61.substring(63, 73);
////				String namaPemilikKendaraan = bit61.substring(73, 123);
////				String jenisKendaraan = bit61.substring(123, 173);
////				String merekKendaraan = bit61.substring(173, 193);
////				String modelKendaraan = bit61.substring(193, 243);
////				String warnaKendaraan = bit61.substring(243, 253);
////				String tahunBuat = bit61.substring(253, 257);
////				String nomorChasis = bit61.substring(257, 277);
////				String tanggalPajak = bit61.substring(277, 287);
////				String pkb_pok = bit61.substring(287, 298);
////				String pkb_den = bit61.substring(298, 309);
////				String swd_pok = bit61.substring(309, 320);
////				String swd_den = bit61.substring(320, 331);
////				String totalTagihan = bit61.substring(331, 342);
////				String kodeDati = bit61.substring(342, 344);
////				String noMesin = bit61.substring(344, 369);
////				String noReferensi = bit61.substring(369, 379);
////				String prog_pok = bit61.substring(379, 390);
////				String prog_den = bit61.substring(390, 401);
////				String beaStnk = bit61.substring(401, 412);
////				String alamat = bit61.substring(412, 462);
////				String spasi = bit61.substring(462, 463);
////				String spasi2 = bit61.substring(463, 493);					
////				
////				task.setTerm(TermConstant.CUSTOMER_NAME, namaPemilikKendaraan);
////				task.setTerm(TermConstant.CUSTOMER_ADDRESS, alamat);
////				task.setTerm(TermConstant.AMOUNT, totalTagihan);
////				task.setTerm(TermConstant.LICENSE_NO, noPolisi);
////				task.setTerm(TermConstant.TAX_PERIOD, tanggalPajak);
////			}
////			else if(JetsConstant.BILLER_CODE_TAX_AND_RETRIBUTION.equals(payeeCode)){
////				String sts = bit61.substring(27, 48);
////				String nopOrNpwpOrNpwr = bit61.substring(48, 73);
////				String nama = bit61.substring(73, 103);
////				String alamat = bit61.substring(103, 148);
////				String mataAnggaran = bit61.substring(148, 157);
////				String uraian = bit61.substring(157, 207);
////				String masaPajak = bit61.substring(207, 217);
////				String skpOrSkr = bit61.substring(217, 237);
////				String jmlPokok = bit61.substring(237, 249);
////				String jmlDenda = bit61.substring(249, 261);
////				
////				Double amountBill = Double.valueOf(jmlPokok).doubleValue();
////				Double amountPenalti = Double.valueOf(jmlDenda).doubleValue();
////				String totalAmount = "";
////				try {
////					totalAmount = CommonUtils.getISOAmount(amountBill + amountPenalti, 12);
////				} catch (Exception e) {
////					throw (JetsException) e;
////				} 
////				
////				task.setTerm(TermConstant.CUSTOMER_NAME, nama);
////				task.setTerm(TermConstant.CUSTOMER_ADDRESS, alamat);
////				task.setTerm(TermConstant.AMOUNT, (amountBill+amountPenalti));
////			}
//			else {
//				LOG.error("Biller with code {} is not supported!", payeeCode);
//				throw new JetsException("Biller code not supported: " + payeeCode, 
//						ResultCode.JETS_UNDEFINED_BILLER);
//			}
//			//System.out.println("Send To DSP: " + isoMsg);
//		} else {
//			throw new JetsException(getResponseCode(rspIsoMsg));
//		}
//		task.setResultCode(getResponseCode(rspIsoMsg));
//		LOG.debug("Response Transaction TO: {}", task);
//	}
//
//	@Override
//	protected IsoMsg buildIso(TransactionTO task) throws JetsException {
//		String bit61 = task.getTerm(TermConstant.BIT61);
//		//String bit61Sett = bit61;
//		//if (JetsConstant.BILLER_CODE_PSTN.equals(task.getTermAsString(TermConstant.PAYEE_CODE))) { // PSTN
//		//	int billCount = task.getTermAsInteger(TermConstant.BILL_COUNT);
//		//	bit61Sett = bit61.substring(0, 47+(23*billCount));
//		//}
//		IsoMsg isoMsg = null;
//		if (task.getTermAsBoolean(TermConstant.REVERSAL)) {
//			isoMsg = HostIsoMsgFactory.createBillReversalSulteng(this, task, bit61);
//		} else {
//			isoMsg = HostIsoMsgFactory.createBillSettlementSulteng(this, task, bit61);
//			
//			//save for reversal
//			task.setTerm(TermConstant.BIT7, isoMsg.getItem(IsoMsg.TIMESTAMP));
//			task.setTerm(TermConstant.BIT11, isoMsg.getItem(IsoMsg.SYS_TRACE_NO));
//			task.setTerm(TermConstant.BIT12, isoMsg.getItem(IsoMsg.TIME_LOCAL));
//			task.setTerm(TermConstant.BIT32, isoMsg.getItem(IsoMsg.ACQUIRING_INSTITUTION_ID));
//			task.setTerm(TermConstant.BIT37, isoMsg.getItem(IsoMsg.RETRIEVAL_REF_NO));
//		}
//		return isoMsg;
//	}
//
//	@Override
//	protected void processSuccessResponse(TransactionTO task, IsoMsg rspIsoMsg) throws JetsException {
//		// State : GetDefaultAccount, Inquiry, Settlement
//		int state = task.getState();
//		String payeeCode = task.getTerm(TermConstant.PAYEE_CODE);
//		if (state == StateConstant.BILL_PAYMENT_SETTLE) {
//			// fee dikali dengan bill count(utk trx speedy(0004) & pstn (0005))
//			if(JetsConstant.BILLER_CODE_PSTN.equals(payeeCode) || // PSTN
//					JetsConstant.BILLER_CODE_SPEEDY.equals(payeeCode)){
//				int billCount = task.getTermAsInteger(TermConstant.BILL_COUNT);
//				double fee = task.getTermAsDouble(TermConstant.FEE_TRX);
////				LOG.debug("Total Fee: " + billCount * fee);
////				task.setTerm(TermConstant.FEE_TRX, billCount * fee);
//			}
////			else if(JetsConstant.BILLER_CODE_ESAMSAT.equals(payeeCode)){
////				String bit61 = rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA);
////				task.setTerm(TermConstant.REF_NO, bit61.substring(36));
////			}
////			else if(JetsConstant.BILLER_CODE_PDAM_TIRTANADI.equals(payeeCode) || JetsConstant.BILLER_CODE_PDAM_TIRTABULIAN.equals(payeeCode)){
////				String bit61 = rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA);
////				task.setTerm(TermConstant.HREF, bit61.substring(163, 178));
////			}
//		}
//		String bit61 = rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA);
//		String bit62 = rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA_2);
//		LOG.debug("bit61: " + bit61);
//		LOG.debug("bit62: " + bit62);
////		if (JetsConstant.BILLER_CODE_AORA_TV.equals(payeeCode) && !task.getTermAsBoolean(TermConstant.REVERSAL)) { // AORA TV
////			String billRef2 = bit61.substring(47, 58);
////			task.setTerm(TermConstant.BILL_REF_2, billRef2);
////		}
////		String hostRef = task.getTerm(TermConstant.HREF);
////		String refNo = hostRef.substring(9, 13) + hostRef.substring(15, 20);
////		//TODO: need check, REF_NO already used as verPin 
////		task.setTerm(TermConstant.HOST_REF_NO, refNo);
////		if (StringUtils.isNotEmpty(bit61))
////			task.setTerm(TermConstant.BIT61, bit61);
//	}
//	
//	private String formatBillPhoneNo(String billAcctNo) {
//		String result = "";
//		String suffix = "";
//		String prefix = billAcctNo.substring(0, 3);
//		if (prefixSet.contains(prefix)) {
//			suffix = billAcctNo.substring(3);
//		} else {
//			prefix = billAcctNo.substring(0, 4);
//			suffix = billAcctNo.substring(4);
//		}
//		result = CommonUtil.zeroPadLeft(prefix, 4) + CommonUtil.zeroPadLeft(suffix, 9);
//		return result;
//	}
//
//	public void setPrefixSet(Set<String> prefixSet) {
//		this.prefixSet = prefixSet;
//	}
//	
//}
