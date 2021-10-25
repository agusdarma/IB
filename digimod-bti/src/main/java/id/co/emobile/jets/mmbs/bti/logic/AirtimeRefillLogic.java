package id.co.emobile.jets.mmbs.bti.logic;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import id.co.emobile.jets.mmbs.bti.BtiIsoDelegateAgent;
import id.co.emobile.jets.mmbs.bti.iso.HostIsoMsgFactory;
import id.co.emobile.jets.mmbs.bti.iso.IsoMsg;
import id.co.emobile.samba.web.data.ResultCode;
import id.co.emobile.samba.web.data.StateConstant;
import id.co.emobile.samba.web.data.TermConstant;
import id.co.emobile.samba.web.data.TransactionTO;
import id.co.emobile.samba.web.service.JetsException;

public class AirtimeRefillLogic extends HostBtiLogic {
	private static final Logger LOG = LoggerFactory.getLogger(AirtimeRefillLogic.class);
	
	/*
	 * map telco code to prepaidInfo
	 */
//	private Map<String, PrepaidInfo> prepaidInfos;
//
//	private Set<String> prefixSet;
//
//	@Autowired
//	private AppsMessageService messageService;
//	
//	@Autowired
//	private TelcoMatrix telcoMatrix;
	
	@Override
	protected Logger getLogger() {
		return LOG;
	}
	
	@Override
	public void solve(BtiIsoDelegateAgent agent, TransactionTO task) 
			throws JetsException, IOException {
			super.solve(agent, task);
//		if (task.getState() == StateConstant.AIRTIME_REFILL_INQ) {
//			String srac = task.getTerm(TermConstant.SRAC);
//			if (srac.length() < 3) {
//				task.setTerm(TermConstant.ACC_INDEX, srac);
//				srac = getDefaultAccount(agent, task);
//				task.setTerm(TermConstant.SRAC, srac);
//			} else {
//				task.setResultCode(ResultCode.SUCCESS_CODE);
//			}
//		}
//		else {
//			super.solve(agent, task);
//		}
	}

	@Override
	protected IsoMsg buildIso(TransactionTO task) throws JetsException {
		if(StateConstant.FUND_TRANSFER_INQUIRY == task.getState()){
			// nothing to do here...
			return null;
		}
		else if(StateConstant.FUND_TRANSFER_SETTLEMENT == task.getState()){
			return HostIsoMsgFactory.createAirTimeSettle(this, task);
		} else {
			LOG.error("Unknown state : " + task.getTermAsInteger(TermConstant.ISO_TYPE));
			throw new JetsException("Unknown state!", ResultCode.BTI_UNKNOWN_STATE);
		}
//		String buyerPhoneNo = formatBit61Phone(task.getTerm(TermConstant.BUYER_PHONE));
//		task.setTerm(TermConstant.PRODUCT_NAME, telcoMatrix.getProductName(buyerPhoneNo));
//		task.setTerm(TermConstant.BUYER_PHONE, buyerPhoneNo);
//		PrepaidInfo prepaidInfo = getPrepaidInfo(task.getTerm(TermConstant.PRODUCT_CODE));
//		return HostIsoMsgFactory.createPrepaidRefill(this, task, prepaidInfo);
	}
	
	@Override
	protected void processSuccessResponse(TransactionTO task, IsoMsg rspIsoMsg) throws JetsException {
		try {
			if (task.getState() == StateConstant.AIRTIME_REFILL_INQ) {
				
			}
			else if (task.getState() == StateConstant.AIRTIME_REFILL_SETTLE) {
				try {
					String bit61 = rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA);
					task.setTerm(TermConstant.VOUCHER_CODE, bit61.substring(70, bit61.length()));
				} catch (Exception e) {
					LOG.error("Unable to parse bit 61: " + e);
					throw new JetsException(ResultCode.BTI_ERROR_PARSE_ISO);
				}
			}
//			String bit61 = rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA);
//			task.setTerm(JetsConstant.BIT126, bit61);
//			String bit62 = rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA_2);
//			task.setTerm(JetsConstant.BIT126, bit62);
			
//			String bit126 = rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA_3);
//			task.setTerm(TermConstant.VOUCHER_CODE, bit126.substring(74, 99).trim());
//			task.setTerm(TermConstant.PHONE_PREFIX, bit61.substring(40, 44));
//			task.setTerm(JetsConstant.BIT126, bit126);
//			MandiriPrepaidBit126 bit126 = new MandiriPrepaidBit126();
//			bit126.parse(rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA_3));
//			task.setTerm(JetsConstant.VOUCHER_CODE, StringUtils.trim(bit126.getBillCustName()));
//			task.setTerm(JetsConstant.VOUCHER_CODE, bit126.getBillVoucher());
			
//			String hostRef = task.getTerm(TermConstant.HREF);
//			String refNo = hostRef.substring(9, 13) + hostRef.substring(15, 20);
//			task.setTerm(TermConstant.HOST_REF_NO, refNo);
		}
		catch(Exception e) {
//			logger.debug("respon Msg: " + rspIsoMsg);
//			System.out.println("respon Msg: " + rspIsoMsg);
			throw new JetsException("Error process success response: " + e, ResultCode.BTI_ERROR_PARSE_ISO);
		}
	}

//	private String getDefaultAccount(TransactionTO task) throws JetsException, IOException {
//		IsoMsg isoMsg = MandiriIsoMsgFactory.createList8Account(this, task);
//		IsoMsg rspIsoMsg = processToHost(isoMsg);
//		if (isSuccess(rspIsoMsg)) {
//			task.setResultCode(ResultCode.SUCCESS_CODE);
//			String bit61 = rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA);
//			String srac = "";
//			if (bit61.length() > 19){
//				srac = bit61.substring(0, 19);
//			} else {
//				srac = bit61.substring(0, 13);
//			}
//			return srac.trim();
//		}
//		else {
//			throw new JetsException("Get default account failed!",
//					getResponseCode(rspIsoMsg));
//		}
//	}

//	private String formatBit61Phone(String phone) throws JetsException {
//		if (phone == null) {
////			logger.debug("phone: " + phone);
////			System.out.println("phone: " + phone);
//			throw new JetsException("Buyer phone no is null!" , ResultCode.BTI_ERROR_COMPOSE_ISO);
//		}
//		if (phone.startsWith("+")) {
//			phone = phone.substring(1);
//		}
//		if (phone.startsWith("62")) {
//			phone = 0 + phone.substring(2);
//		}
//		String prefix = phone.substring(0, 3);
//		if (prefixSet.contains(prefix)) {
//			phone = "0" + phone;
//		}
//		return phone;
//	}
//	
//	private PrepaidInfo getPrepaidInfo(String telco) throws JetsException {
//		PrepaidInfo prepaidInfo = prepaidInfos.get(telco);
//		if (prepaidInfo == null) {
//			LOG.warn("Prepaid info for " + telco + " is not found!");
//	//		logger.debug("telco: " + telco);
//	//		System.out.println("telco: " + telco);
//			throw new JetsException("Prepaid info for " + telco + " is not found!", ResultCode.BTI_ERROR_COMPOSE_ISO);
//		}
//		return prepaidInfos.get(telco);
//	}
//
//	public void setPrepaidInfos(Map<String, String> infos) {
//		this.prepaidInfos = new HashMap<String, PrepaidInfo>();
//		Iterator<String> iter = infos.keySet().iterator();
//		while (iter.hasNext()) {
//			String code = iter.next();
//			PrepaidInfo prepaidInfo = new PrepaidInfo(infos.get(code));
////			logger.debug("prepaidInfo: " + prepaidInfo);
////			System.out.println("prepaidInfo: " + prepaidInfo);
//			this.prepaidInfos.put(code, prepaidInfo);
//		}
//		//logInfo("Prepaid Info: " + prepaidInfos);
//	}
//
//	/*
//	 * phone no start with: 021, 022, 024, 031, 061, will be added with 0
//	 */
//	public void setPrefixSet(Set<String> prefixSet) {
//		this.prefixSet = prefixSet;
//	}
}
