//package id.co.emobile.jets.mmbs.bti.logic;
//
//import java.io.IOException;
//import java.util.Map;
//import java.util.Set;
//
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
//import id.co.emobile.jets.mmbs.bti.CommonUtils;
//import id.co.emobile.jets.mmbs.bti.helper.MandiriUbpPLNPrepaid;
//import id.co.emobile.jets.mmbs.bti.iso.HostIsoMsgFactory;
//import id.co.emobile.jets.mmbs.bti.iso.IsoMsg;
//
//public class UnifiedPaymentLogic extends HostBtiLogic {
//	private static final Logger LOG = LoggerFactory.getLogger(UnifiedPaymentLogic.class);
//	
//	@Autowired
//	private AccountBalanceLogic accountBalanceInquiryLogic;
//	
//	//Mapping for rc
//	//e.g.: 30300 - 56, 17
//	private Map<String, Set<String>> rcMap;
//	
//	//Mapping for code to be added to rc
//	//e.g.: 30300 - PP (result: PP.56, PP.17)
//	//		30301 - PA
//	private Map<String, String> rcCodeMap;
//
//	@Override
//	protected Logger getLogger() {
//		return LOG;
//	}
//	
//	@Override
//	public void solve(BtiIsoDelegateAgent agent, TransactionTO task) 
//			throws JetsException, IOException {
//		//State : GetDefaultAccount, Inquiry, Settlement
//		int state = task.getState();
//		if (state == StateConstant.BILL_PAYMENT_GET_DEF_ACC_PIN) {
//			accountBalanceInquiryLogic.solve(agent, task);
//		} else if (state == StateConstant.BILL_PAYMENT_GET_AMOUNT) {
//			solveDefaultAccountInquiry(agent, task);
//			solveInquiry(agent, task);
//		} else if (state == StateConstant.BILL_PAYMENT_INQ) {
//			getSelectedBill(task);
//		} else if (state == StateConstant.BILL_PAYMENT_SETTLE) {
////			super.solve(task);
//			solveSettlement(agent, task);
//		} else {
//			LOG.error("Unknown state: " + state);
//			throw new JetsException("Unknown Unified Payment state: " + state, ResultCode.BTI_UNKNOWN_STATE);
//		}
//	}
//
//	private void solveInquiry(BtiIsoDelegateAgent agent, TransactionTO task) throws JetsException, IOException {
//		IsoMsg isoMsg = null;
//		if (task.getTermAsBoolean(TermConstant.IS_SOA_UBP)) {
//			isoMsg = HostIsoMsgFactory.createNewUnifiedBillInquiry(this, task, LOG);
//		} else {
//			isoMsg = HostIsoMsgFactory.createUnifiedBillInquiry(this, task, 
//					task.getPhoneNo(), task.getTerm(TermConstant.PIN), LOG);
//		}
//		IsoMsg rspIsoMsg = processToHost(agent, isoMsg);
//		LOG.info("Response Iso : " + rspIsoMsg);
//		if (isSuccess(rspIsoMsg)) {
//			String bit124 = rspIsoMsg.getItem(IsoMsg.INFORMATION_DATA);
//			String bref2 = bit124.substring(60, 79);
//			if (JetsConstant.BILLER_PLN_PREPAID.equals(task.getTermAsString(TermConstant.PAYEE_CODE)) ||
//					JetsConstant.BILLER_PLN_PREPAID_ADVICE.equals(task.getTermAsString(TermConstant.PAYEE_CODE))) {
//				String noMeter = bit124.substring(482, 507).trim(); // nomor meter
//				String name = bit124.substring(552, 577).trim(); // nama
//				task.setTerm(TermConstant.METER_ID, noMeter);
//				task.setTerm(TermConstant.CUSTOMER_NAME, name);
//				if (JetsConstant.BILLER_PLN_PREPAID_ADVICE.equals(task.getTermAsString(TermConstant.PAYEE_CODE))) {
//					task.setTerm(TermConstant.AMOUNT, 1);
//				} else {
//					String daya = bit124.substring(587, 598).trim(); // tariff/daya
//					task.setTerm(TermConstant.DAYA, daya);
//				}
//				task.setState(StateConstant.BILL_PAYMENT_INQ);
//			} else {
////				String name = bit124.substring(185, 205);
////				String period = bit124.substring(219, 224);
//				String charges = CommonUtils.getFormattedAmount(bit124.substring(40, 50));
//				int paymentType = Integer.parseInt(bit124.substring(101, 102));
//				String period = bit124.substring(587, 592);
//				String name = bit124.substring(507, 537).trim();
//				String amount = CommonUtils.getFormattedAmount(bit124.substring(673, 685));
//				task.setTerm(TermConstant.PERIOD, period);
//				task.setTerm(TermConstant.CUSTOMER_NAME, name);
//				task.setTerm(TermConstant.AMOUNT, amount);
//				task.setTerm(TermConstant.CHARGES, charges);
//				task.setTerm(TermConstant.PAYMENT_TYPE, paymentType);
//				if (paymentType == JetsConstant.UBP_PAYMENT_TYPE_ALL) {
//					task.setTrxValue(Double.parseDouble(amount));
//					task.setState(StateConstant.BILL_PAYMENT_INQ);
//					task.setTerm(TermConstant.BILLING_FLAG_MAP, "FFFFFF");
//				} else {
//					//parsing bit 124
//					//save list of bills for user to choose from
//					//int billAmount = Integer.parseInt(bit124.substring(452, 453));
//					int optionAmount = (bit124.length() - 647) / 69;
//					LOG.debug("Number of options: " + optionAmount);
//					String listDetail = "";
//					for (int i=0; i<optionAmount; i++) {
//						if (i > 0) listDetail = listDetail + "\n";
//						listDetail = listDetail + (i + 1) + ". " + bit124.substring(648 + (i * 69), 673 + (i * 69)).trim() +
//								" Rp." + CommonUtils.getFormattedAmountIndo(bit124.substring(673 + (i * 69), 685 + (i * 69)), 2);
//					}
//					if (paymentType == JetsConstant.UBP_PAYMENT_TYPE_STANDARD && optionAmount == 4) {
//						String option1 = bit124.substring(648 + (1 * 69), 673 + (1 * 69)).trim();
//						String option2 = bit124.substring(648 + (2 * 69), 673 + (2 * 69)).trim();
//						String option3 = bit124.substring(648 + (3 * 69), 673 + (3 * 69)).trim();
//						
//						listDetail = listDetail + "\n5. " + option1 + " + " + option2;
//						listDetail = listDetail + "\n6. " + option1 + " + " + option3;
//						listDetail = listDetail + "\n7. " + option2 + " + " + option3;
//					}
//					task.setTerm(TermConstant.LIST_AMNT, listDetail);
//				}
//			}
//			task.setTerm(TermConstant.BREF, bref2);
//			task.setTerm(TermConstant.BIT61, rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA));
//			task.setTerm(TermConstant.BIT124, bit124);
//		} else {
//			throw new JetsException(getResponseCode(rspIsoMsg, 
//					task.getTermAsString(TermConstant.PAYEE_CODE)));
//		}
//		task.setResultCode(getResponseCode(rspIsoMsg));
//		task.setTerm(TermConstant.UBP_INFO_MSG, 
//				rspIsoMsg.getItem(IsoMsg.INFORMATION_DATA).substring(152, 452).trim());
//	}
//	
//	private void solveSettlement(BtiIsoDelegateAgent agent, TransactionTO task) throws JetsException, IOException {
//		IsoMsg isoMsg = null;
//		isoMsg = HostIsoMsgFactory.createUnifiedBillSettlement(this, task, 
//				task.getPhoneNo(), task.getTerm(TermConstant.PIN), LOG);
//		IsoMsg rspIsoMsg = processToHost(agent, isoMsg);
//		LOG.info("Response Iso : " + rspIsoMsg);
//		if (isSuccess(rspIsoMsg)) {
//			//String amount = task.getTermAsString(TermConstant.AMOUNT);
//			String bref = task.getTermAsString(TermConstant.BANK_REF);
//			String bit61 = rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA);
//			task.setTerm(TermConstant.BIT61, bit61);
//			//task.setTerm(TermConstant.AMOUNT, amount);
//			task.setTerm(TermConstant.BREF, bref);
//			if (JetsConstant.BILLER_PLN_PREPAID.equals(task.getTermAsString(TermConstant.PAYEE_CODE)) ||
//					JetsConstant.BILLER_PLN_PREPAID_ADVICE.equals(task.getTermAsString(TermConstant.PAYEE_CODE))) {
//				String bit124 = rspIsoMsg.getItem(IsoMsg.INFORMATION_DATA);
//				MandiriUbpPLNPrepaid mandiriUbpPLNPrepaid = parseUbpPLNPrepaid(task, bit124);
//				task.setTerm(TermConstant.BILLER_REF, mandiriUbpPLNPrepaid.getPlnRefNo()); //no ref PLN
//				task.setTerm(TermConstant.JUMLAH_KWH, mandiriUbpPLNPrepaid.getJumlahKwh()); //jumlah kWh
//				task.setTerm(TermConstant.TOKEN_NUMBER, mandiriUbpPLNPrepaid.getTokenNumber()); //token number
//				if (task.getTermAsBoolean(TermConstant.NEED_ADVICE)) {
//					task.setTerm(TermConstant.PLN_ADVICE_ID, mandiriUbpPLNPrepaid.getAdviceId()); //no ref advice
//				}
//			}
//		} else {
//			throw new JetsException(getResponseCode(rspIsoMsg, task.getTermAsString(TermConstant.PAYEE_CODE)));
//		}
//		task.setResultCode(getResponseCode(rspIsoMsg));
//		task.setTerm(TermConstant.UBP_INFO_MSG, rspIsoMsg.getItem(IsoMsg.INFORMATION_DATA).substring(80, 430).trim());
//		
//		processHostRef(task, rspIsoMsg);
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
//	}
//
//	@Override
//	protected IsoMsg buildIso(TransactionTO task) throws JetsException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//	private void getSelectedBill(TransactionTO task) throws JetsException {
//		int index = task.getTermAsInteger(TermConstant.AMNT_INDEX);
//		int paymentType = task.getTermAsInteger(TermConstant.PAYMENT_TYPE);
//		String bit124 = task.getTerm(TermConstant.BIT124);
//		//int billAmount = Integer.parseInt(bit124.substring(452, 453));
//		int optionAmount = (bit124.length() - 647) / 69;
//		
//		int cnt = optionAmount; // contains the real number of options
//		boolean hasCombination = false;
//		if (paymentType == JetsConstant.UBP_PAYMENT_TYPE_STANDARD && optionAmount == 4) {
//			cnt = 7;
//			hasCombination = true;
//		}
//		if (index > cnt) {
//			throw new JetsException("Invalid bill index: " + index + ", max index: " + cnt, 
//					ResultCode.BTI_INVALID_BILL_INDEX);
//		}
//		
//		double amount = 0.0;
//		String selectedOption = "";
//		if (index <= optionAmount) {
//			selectedOption = bit124.substring(648 + ((index - 1) * 69), 673 + ((index - 1) * 69)).trim();
//			String amountStr = bit124.substring(673 + ((index - 1) * 69), 685 + ((index - 1) * 69));
//			amount = Double.valueOf(amountStr).doubleValue();
//			amount = amount / 100;
//		} else {
//			String optionName1 = bit124.substring(648 + (1 * 69), 673 + (1 * 69)).trim();
//			String optionName2 = bit124.substring(648 + (2 * 69), 673 + (2 * 69)).trim();
//			String optionName3 = bit124.substring(648 + (3 * 69), 673 + (3 * 69)).trim();
//			
//			String optionAmnt1 = bit124.substring(673 + (1 * 69), 685 + (1 * 69)).trim();
//			String optionAmnt2 = bit124.substring(673 + (2 * 69), 685 + (2 * 69)).trim();
//			String optionAmnt3 = bit124.substring(673 + (3 * 69), 685 + (3 * 69)).trim();
//			
//			if (index == 5) {
//				selectedOption = optionName1 + " + " + optionName2;
//				amount = Double.valueOf(optionAmnt1).doubleValue() + Double.valueOf(optionAmnt2).doubleValue();
//			}
//			if (index == 6) {
//				selectedOption = optionName1 + " + " + optionName3;
//				amount = Double.valueOf(optionAmnt1).doubleValue() + Double.valueOf(optionAmnt3).doubleValue();
//			}
//			if (index == 7) {
//				selectedOption = optionName2 + " + " + optionName3;
//				amount = Double.valueOf(optionAmnt2).doubleValue() + Double.valueOf(optionAmnt3).doubleValue();
//			}
//			amount = amount / 100;
//		}
//		String billingFlagMap = getBillingFlagMap(index, paymentType, hasCombination);
//		task.setTerm(TermConstant.BILLING_FLAG_MAP, billingFlagMap);
//		task.setTerm(TermConstant.AMOUNT, amount);
//		task.setTrxValue(amount);
//		task.setTerm(TermConstant.SELECTED_OPTION, selectedOption);
//		task.setResultCode(ResultCode.SUCCESS_CODE);
//	}
//	
//	private MandiriUbpPLNPrepaid parseUbpPLNPrepaid(TransactionTO task, String bit124) {
//		//example: Transaksi Berhasil 53523B3E5AA5A9D1C2EA INDAH PUTRI 14234567895 Rp. 145,000.00 kWh.  981.40 Token4352 0272 1911 6712 5912 12/07/13 17:08
//		MandiriUbpPLNPrepaid result = new MandiriUbpPLNPrepaid();
//		if (bit124.length() < 430) {
//			return result;
//		}
//		String userMsg = bit124.substring(80, 430).toUpperCase();
//		
//		int startPos = userMsg.indexOf("LAKUKAN ADV");
//		if (startPos > 0) {
//			task.setTerm(TermConstant.NEED_ADVICE, true);
//			
//			//Adv Purchase no. ref 0331259488
//			int advicePos = userMsg.indexOf("NO. REF");
//			if (advicePos > 0) {
//				result.setAdviceId(userMsg.substring(advicePos + 8, advicePos + 18));
//			}
//		} else {
//			task.setTerm(TermConstant.NEED_ADVICE, false);
//			userMsg = userMsg.substring(startPos + 9);
//			result.setPlnRefNo(userMsg.substring(0, userMsg.indexOf(" ")));
//			
//		}
//		
//		//kWh.  981.40 Token6657 5751 7950 3842 5948
//		int kwhPos = userMsg.indexOf("KWH");
//		int tokenPos = userMsg.indexOf("TOKEN");
//		if (kwhPos > 0) {
//			if (".".equals(userMsg.substring(kwhPos + 3, kwhPos + 4))) {
//				kwhPos = kwhPos + 1;
//			}
//			result.setJumlahKwh(userMsg.substring(kwhPos + 3, tokenPos - 1).trim());
//		}
//		if (tokenPos > 0) {
//			int endPos = tokenPos + 5 + 25; //userMsg.indexOf(" ", tokenPos + 6);
//			result.setTokenNumber(userMsg.substring(tokenPos + 5, endPos).trim());
//		}
//		return result;
//	}
//	
//	protected String getResponseCode(IsoMsg rspIsoMsg, String payeeCode) {
//		String responseCode = rspIsoMsg.getItem(IsoMsg.RESPONSE_CODE);
//		if (HOST_SUCCESS_CODE.equals(responseCode)) {
//			return ResultCode.SUCCESS_CODE;
//		} else {
//			if (rcMap.containsKey(payeeCode)) {
//				if (rcMap.get(payeeCode).contains(responseCode)) {
//					responseCode = CommonUtil.truncate(rcCodeMap.get(payeeCode), 3) + "." + responseCode;
//				}
//			}
//			return responseCode;
//		}
//	}
//
//	private String getBillingFlagMap(int index, int paymentType, boolean hasCombination) {
//		int[] paymentMap = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
//		
//		if (index == 1) {
//			if (paymentType == JetsConstant.UBP_PAYMENT_TYPE_SINGLE)
//				paymentMap[0] = 1;
//			else
//				return "FFFFFF";
//		} else if (index == 5 && hasCombination) {
//			paymentMap[0] = 1;
//			paymentMap[1] = 1;
//		} else if (index == 6) {
//			paymentMap[0] = 1;
//			paymentMap[2] = 1;
//		} else if (index == 7) {
//			paymentMap[1] = 1;
//			paymentMap[2] = 1;
//		} else if (index > 7) {
//			return "FFFFFF";
//		} else {
//			paymentMap[index - 2] = 1;
//		}
//		
//		String bin = "";
//		for (int i=0; i<8; i++)
//			bin += paymentMap[i];
//		String hex = CommonUtil.binToHex(bin.substring(0, 4)).toUpperCase();
//		
//		return CommonUtil.padRight(hex, '0', 6);
//	}
//	
//	public void setRcMap(Map<String, Set<String>> rcMap) {
//		this.rcMap = rcMap;
//	}
//
//	public void setRcCodeMap(Map<String, String> rcCodeMap) {
//		this.rcCodeMap = rcCodeMap;
//	}
//
//	@Override
//	protected void processSuccessResponse(TransactionTO task, IsoMsg rspIsoMsg)
//			throws JetsException {
//		try {
//			String hostRef = task.getTerm(TermConstant.HREF);
//			String refNo = hostRef.substring(9, 13) + hostRef.substring(15, 20);
//			//TODO: need check, REF_NO already used as verPin 
//			task.setTerm(TermConstant.HOST_REF_NO, refNo);
//		} catch (Exception e) {
//			throw new JetsException(ResultCode.BTI_ERROR_PARSE_ISO);
//		}
////		super.processSuccessResponse(task, rspIsoMsg);
//	}
//	
//}
