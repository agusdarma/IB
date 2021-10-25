//package id.co.emobile.jets.mmbs.bti.logic;
//
//import java.io.IOException;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.emobile.jets.mmbs.lib.entity.AccountHistory;
//import com.emobile.jets.mmbs.lib.entity.CifHistory;
//import com.emobile.jets.mmbs.lib.mapper.CifHistoryMapper;
//
//import id.co.emobile.jets.mmbs.bti.BtiIsoDelegateAgent;
//import id.co.emobile.jets.mmbs.bti.iso.HostIsoMsgFactory;
//import id.co.emobile.jets.mmbs.bti.iso.IsoMsg;
//import id.co.emobile.samba.web.data.JetsConstant;
//import id.co.emobile.samba.web.data.ResultCode;
//import id.co.emobile.samba.web.data.StateConstant;
//import id.co.emobile.samba.web.data.TermConstant;
//import id.co.emobile.samba.web.data.TransactionTO;
//import id.co.emobile.samba.web.service.JetsException;
//
//public class AuthSmsNotifLogic extends HostBtiLogic {
//	private static final Logger LOG = LoggerFactory.getLogger(AuthSmsNotifLogic.class);
//	
//	@Autowired
//	private CifHistoryMapper cifHistoryMapper;
//	
//	@Override
//	protected Logger getLogger() {
//		return LOG;
//	}
//	
//	@Override
//	public void solve(BtiIsoDelegateAgent agent, TransactionTO task) 
//			throws JetsException, IOException {
//		IsoMsg isoMsg = null;
//		LOG.debug("AuthSmsNotifLogic Solve(): " + task);
//		LOG.debug("state solve: " + task.getState());
//		int state = task.getState();
//		String responseCode = task.getResultCode(); // set rc from default if activityBlastSms (regis or unregis) return response from host
//		if (StateConstant.STATE_CIF_REG_AUTH == state) {
//			
//			CifHistory cifHistory = cifHistoryMapper.findCifPendingAuthByCifHistoryId(task.getTermAsInteger(TermConstant.CIF_HISTORY_ID));
//			LOG.debug("cifHistory: " + cifHistory);
//			if(cifHistory != null){
//				for (AccountHistory accountHistory : cifHistory.getListAccount()) {
//					LOG.debug("accountHistory: " + accountHistory);
//					task.setTerm(TermConstant.SRAC, accountHistory.getAccountNo());
//					task.setTerm(TermConstant.CARD_NUMBER, accountHistory.getCardNo());
//					
//					if(JetsConstant.ACTION_REGIS.equals(accountHistory.getActivityBlastSms())){
//						//register
//						isoMsg = HostIsoMsgFactory.createRegisSmsNotif(this, task);
//						IsoMsg rspIsoMsg = processToHost(agent, isoMsg);
//						responseCode = rspIsoMsg.getItem(IsoMsg.RESPONSE_CODE);
//						if (HOST_SUCCESS_CODE.equals(responseCode)) {
//							responseCode = ResultCode.SUCCESS_CODE;
//							getLogger().debug("Register Account [{}] SMS Notif Success [{}]", task.getTermAsString(TermConstant.SRAC), task);
//							task.addListSuccessRegBlastSms(task.getTermAsString(TermConstant.SRAC));
//						} 
//						else{
//							getLogger().debug("Register Account [{}] SMS Notif Failed [{}]", task.getTermAsString(TermConstant.SRAC), task);
////							throw new JetsException(getResponseCode(rspIsoMsg));
//						}
//					}
//					else if(JetsConstant.ACTION_UNREGIS.equals(accountHistory.getActivityBlastSms())){
//						//unregister
//						isoMsg = HostIsoMsgFactory.createUnregisSmsNotif(this, task);
//						IsoMsg rspIsoMsg = processToHost(agent, isoMsg);
//						responseCode = rspIsoMsg.getItem(IsoMsg.RESPONSE_CODE);
//						if (HOST_SUCCESS_CODE.equals(responseCode)) {
//							responseCode = ResultCode.SUCCESS_CODE;
//							getLogger().debug("Unregister Account [{}] SMS Notif Success [{}]", task.getTermAsString(TermConstant.SRAC), task);
//							task.addListSuccessUnregBlastSms(task.getTermAsString(TermConstant.SRAC));
//						} 
//						else{
//							getLogger().debug("Unregister Account [{}] SMS Notif Failed [{}]", task.getTermAsString(TermConstant.SRAC), task);
////							throw new JetsException(getResponseCode(rspIsoMsg));
//						}
//					}
//					
//				}//end for
//				task.setResultCode(responseCode);
//			}//end cifHistory!=null
//			else{
//				LOG.error("Cif History Not Found For Auth Sms Notif id: " + task.getTermAsInteger(TermConstant.CIF_HISTORY_ID));
//				throw new JetsException("Cif History Not Found For Auth Sms Notif id: " + task.getTermAsInteger(TermConstant.CIF_HISTORY_ID), ResultCode.BTI_UNKNOWN_STATE);
//			}
//		} 
//		else {
//			LOG.error("Unknown state: " + state);
//			throw new JetsException("Unknown Registration Sms Notif state: " + state, ResultCode.BTI_UNKNOWN_STATE);
//		}
//	}
//	
////	@Override
////	public void solve(BtiIsoDelegateAgent agent, TransactionTO task) 
////			throws JetsException, IOException {
////		IsoMsg isoMsg = null;
////		LOG.debug("AuthSmsNotifLogic Solve(): "+ task);
////		int state = task.getState();
////		String responseCode = task.getResultCode(); // set rc from default if activityBlastSms (regis or unregis) return response from host
////		if (StateConstant.STATE_CIF_REG_AUTH == state) {
////			
////			CifHistory cifHistory = cifHistoryMapper.findCifPendingAuthByCifHistoryId(task.getTermAsInteger(TermConstant.CIF_HISTORY_ID));
////			LOG.debug("AuthSmsNotifLogic Solve() cifHistory: "+ cifHistory);
////			if(cifHistory != null){
////				for (AccountHistory accountHistory : cifHistory.getListAccount()) {
////					task.setTerm(TermConstant.SRAC, accountHistory.getAccountNo());
////					task.setTerm(TermConstant.CARD_NUMBER, accountHistory.getCardNo());
////					LOG.debug("AuthSmsNotifLogic Solve() accountHistory: "+ accountHistory);
////					if(JetsConstant.ACTION_REGIS.equals(accountHistory.getActivityBlastSms())){
////						//register
////						isoMsg = HostIsoMsgFactory.createRegisSmsNotif(this, task);
////						IsoMsg rspIsoMsg = processToHost(agent, isoMsg);
////						responseCode = rspIsoMsg.getItem(IsoMsg.RESPONSE_CODE);
////						if (HOST_SUCCESS_CODE.equals(responseCode)) {
////							responseCode = ResultCode.SUCCESS_CODE;
////							getLogger().debug("Register Account [{}] SMS Notif Success [{}]", task.getTermAsString(TermConstant.SRAC), task);
////							task.addListSuccessRegBlastSms(task.getTermAsString(TermConstant.SRAC));
////						} 
////						else{
////							getLogger().debug("Register Account [{}] SMS Notif Failed [{}]", task.getTermAsString(TermConstant.SRAC), task);
//////							throw new JetsException(getResponseCode(rspIsoMsg));
////						}
////					}
////					else if(JetsConstant.ACTION_UNREGIS.equals(accountHistory.getActivityBlastSms()) && accountHistory.getStatusHostBlastSms() == JetsConstant.STATUS_ALREADY_REGISTER){
////						//unregister
////						isoMsg = HostIsoMsgFactory.createUnregisSmsNotif(this, task);
////						IsoMsg rspIsoMsg = processToHost(agent, isoMsg);
////						responseCode = rspIsoMsg.getItem(IsoMsg.RESPONSE_CODE);
////						if (HOST_SUCCESS_CODE.equals(responseCode)) {
////							responseCode = ResultCode.SUCCESS_CODE;
////							getLogger().debug("Unregister Account [{}] SMS Notif Success [{}]", task.getTermAsString(TermConstant.SRAC), task);
////							task.addListSuccessUnregBlastSms(task.getTermAsString(TermConstant.SRAC));
////						} 
////						else{
////							getLogger().debug("Unregister Account [{}] SMS Notif Failed [{}]", task.getTermAsString(TermConstant.SRAC), task);
//////							throw new JetsException(getResponseCode(rspIsoMsg));
////						}
////					}
////					
////				}//end for
////				task.setResultCode(responseCode);
////			}//end cifHistory!=null
////			else{
////				LOG.error("Cif History Not Found For Auth Sms Notif id: " + task.getTermAsInteger(TermConstant.CIF_HISTORY_ID));
////				throw new JetsException("Cif History Not Found For Auth Sms Notif id: " + task.getTermAsInteger(TermConstant.CIF_HISTORY_ID), ResultCode.BTI_UNKNOWN_STATE);
////			}
////		} 
////		else {
////			LOG.error("Unknown state: " + state);
////			throw new JetsException("Unknown Registration Sms Notif state: " + state, ResultCode.BTI_UNKNOWN_STATE);
////		}
////	}
//
//	@Override
//	protected IsoMsg buildIso(TransactionTO task) throws JetsException {
//		// 0: terminate, 1:register - notif sms
////		if(task.getTermAsInteger(TermConstant.FLAG_SMS_NOTIF) == 0)
////			return HostIsoMsgFactory.createUnregisSmsNotif(this, task);
////		else
////			return HostIsoMsgFactory.createRegisSmsNotif(this, task);
//		return null;
//	}
//
//	@Override
//	protected void processSuccessResponse(TransactionTO task, IsoMsg rspIsoMsg) throws JetsException {
//		
//	}
//
//}
