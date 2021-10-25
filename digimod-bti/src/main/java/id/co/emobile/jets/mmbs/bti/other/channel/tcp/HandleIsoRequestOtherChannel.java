//package id.co.emobile.jets.mmbs.bti.other.channel.tcp;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.jboss.netty.channel.Channel;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//
//import com.emobile.jets.mmbs.lib.data.JetsConstant;
//import com.emobile.jets.mmbs.lib.data.TermConstant;
//import com.emobile.jets.mmbs.lib.data.TransactionTO;
//import com.emobile.jets.mmbs.lib.logic.MmbsSystemAsyncUtil;
//import com.emobile.jets.mmbs.lib.logic.MmbsSystemFacade;
//import com.emobile.jets.mmbs.lib.service.AppsMessageService;
//import com.emobile.jets.mmbs.lib.service.AppsTimeService;
//import com.emobile.jets.mmbs.lib.service.JetsException;
//import com.emobile.jets.mmbs.lib.service.SequenceGeneratorService;
//import com.emobile.jets.mmbs.lib.service.SettingService;
//import com.emobile.jets.mmbs.lib.util.DatabaseAsyncUtil;
//import com.emobile.jets.mmbs.lib.util.DuplicateFilter;
//import com.emobile.jets.mmbs.lib.util.ResultCode;
//
//import id.co.emobile.jets.mmbs.bti.iso.HostIsoMsgFactory;
//import id.co.emobile.jets.mmbs.bti.iso.IsoMsg;
//import id.co.emobile.jets.mmbs.bti.iso.IsoMsgHeader;
//import id.co.emobile.jets.mmbs.bti.other.channel.iso.BaseIsoLogic;
//import id.co.emobile.jets.mmbs.bti.other.channel.iso.IsoLogicFactory;
//
//public class HandleIsoRequestOtherChannel extends BaseIsoTcp{
//	private static final Logger LOG = LoggerFactory.getLogger(HandleIsoRequestOtherChannel.class);
//	
//	@Autowired
//	private SettingService settingService;
//			
//	private IsoLogicFactory logicFactory;
//	
//	private DuplicateFilter duplicateFilter;
//
//    @Autowired
//    @Qualifier("msgSequenceGenerator")
//    protected SequenceGeneratorService seqGen;
//    
//    @Autowired
//	private AppsTimeService timeService;    
//    @Autowired
//	private AppsMessageService messageService;
//    
//	@Autowired
//	private DatabaseAsyncUtil databaseAsyncUtil;
//	
//    @Autowired
//	private MmbsSystemAsyncUtil mmbsSystemAsyncUtil;
//    
//    @Autowired
//    private MmbsSystemFacade mmbsSystemFacade;
//    
//    @Autowired
//    private HandleIsoResponseOtherChannel handleIsoResponseOtherChannel;
//    
//	public IsoMsg convertToIso(String data) throws JetsException {
//		IsoMsg isoMessage = HostIsoMsgFactory.createHostIsoMsg().parse(data);
//		if (isoMessage == null)
//			return null;
//		return isoMessage;
//	}
//		
//	public void handleDataReceivedOtherChannel(Channel channel, String message) {
//		long startTime = timeService.getCurrentTimemillis();
//		LOG.debug("Receive Data from " + channel.getRemoteAddress() + " [" + message + "]");
//		try {
//			IsoMsg isoRequest = convertToIso(message);
//			if (isoRequest != null){				
//				if (IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST.equals(isoRequest.getMsgType())) {
//					//check duplicate trx
//					if (checkDuplicate(isoRequest.getRawMessage())) {
//					    LOG.warn(getId() + ": Duplicate message [" + isoRequest.toString() + "] ignored");
//					    return ;
//					}
//					
//					String procCode = isoRequest.getItem(IsoMsg.ADDITIONAL_DATA_4);
//					BaseIsoLogic isoLogic = logicFactory.getLogic(procCode);
//					if (isoLogic == null) {
//						LOG.warn("Unknown Processing code: " + procCode);
//						return;
//					}
//					LOG.info("Get Logic: " + isoLogic);
//					
//					//create Transaction TO
//					TransactionTO task = new TransactionTO();					
//					task.setReceivedTime(startTime);
//					task.setChannelType(JetsConstant.CHANNEL_TYPE_ISO);
//					task.setSmiInput(getId());					
//					task.setTerm(TermConstant.ISO_REQUEST, isoRequest.getRawMessage());
//					//validate and set terms (set trxCode, clientRef)
//					isoLogic.composeTermIso(isoRequest, task);
//					
//					try {
//						String msgLogNo = seqGen.getNextSequence();
//						task.setMsgLogNo(msgLogNo);
//						logDebug(getId() + ": Rcv #" + task.getMsgLogNo() + " from " + task.getPhoneNo()
//								+ ": " + isoRequest.getRawMessage());
//						LOG.debug("[#{}] Started: {}", task.getMsgLogNo(), task);
//						task.setResultCode(ResultCode.SUCCESS_CODE);
//					} catch (JetsException je) {
//						task.setResultCode(je.getResultCode());
//						task.setSysMessage(je.getMessage());
//						messageService.assignErrorMessage(task, null);
//					}  // end try catch exception
//					catch (Exception e) {
//						LOG.warn("Exception", e);
//						task.setResultCode(ResultCode.JETS_UNKNOWN_ERROR);
//						task.setSysMessage("Error: " + e.getMessage());
//						messageService.assignErrorMessage(task, null);
//					} // end try catch exception			
//					
//					// save reference to channel		
//					Map<String, Channel> mapChannelTcp = new HashMap<String, Channel>();
//					mapChannelTcp.put(task.getMsgLogNo(), channel);
//					task.setMapChannelTcp(mapChannelTcp);
//					
//					mmbsSystemAsyncUtil.processTransaction(task);
//					
////					mmbsSystemFacade.processTransaction(task);
//					
//					handleIsoResponseOtherChannel.sendResponse(task);
//					
////					databaseAsyncUtil.logTransaction(task);
//				}				
//			} else {
//				LOG.warn("Iso Request is null");
//				return;
//			}
//		} catch (Exception e) {
//			LOG.error("Exception in handle Request(): ", e, e);
//		} 		
//	}
//	
//	private boolean checkDuplicate(String data) {
//		if (duplicateFilter == null) return false;
//		return duplicateFilter.isDuplicate(data);
//	}
//
//	public void setLogicFactory(IsoLogicFactory logicFactory) {
//		this.logicFactory = logicFactory;
//	}
//
//	public void setDuplicateFilter(DuplicateFilter duplicateFilter) {
//		this.duplicateFilter = duplicateFilter;
//	}
//}
