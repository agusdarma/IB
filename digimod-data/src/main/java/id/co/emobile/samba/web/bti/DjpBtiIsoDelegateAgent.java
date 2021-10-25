package id.co.emobile.samba.web.bti;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import id.co.emobile.samba.web.data.JetsConstant;
import id.co.emobile.samba.web.data.ResultCode;
import id.co.emobile.samba.web.data.StateConstant;
import id.co.emobile.samba.web.data.TermConstant;
import id.co.emobile.samba.web.data.TransactionTO;
import id.co.emobile.samba.web.iso.DjpIsoMsg;
import id.co.emobile.samba.web.iso.IsoMsg;
import id.co.emobile.samba.web.iso.IsoMsgHeader;
import id.co.emobile.samba.web.iso.SultengIsoMsgFactory;
import id.co.emobile.samba.web.iso.helper.NetworkInfoConstant;
import id.co.emobile.samba.web.iso.logic.DjpAbstractIsoLogic;
import id.co.emobile.samba.web.iso.logic.DjpIsoLogicFactory;
import id.co.emobile.samba.web.iso.logic.DjpNetworkManagementLogic;
import id.co.emobile.samba.web.netty.NewTcpClient;
import id.co.emobile.samba.web.netty.NewTcpNotifier;
import id.co.emobile.samba.web.service.JetsException;
import id.co.emobile.samba.web.service.SettingService;

public class DjpBtiIsoDelegateAgent implements NewTcpNotifier {
	private static final Logger LOG = LoggerFactory.getLogger(DjpBtiIsoDelegateAgent.class);
	private final static Logger TRX_LOGGER = LoggerFactory.getLogger("bti.transaction");
	
	private String id;
	private int maxRetry = 4;
	private DjpIsoLogicFactory logicFactory;
	
	private String responseRcv;  // used privately to store response data
	private AtomicBoolean inProcess = new AtomicBoolean(false);
	
	private NewTcpClient tcpClient;
	private SettingService settingService;
	private DjpNetworkManagementLogic networkLogic;
	
	private Map<String, Integer> mapMaxRetry;
	
//	@Autowired
//	private HandleIsoRequestOtherChannel handleIsoReqOtherChannel;
	
	public boolean sendNetworkManagement(String networkInfo) {
		LOG.debug("{}: sendNetworkManagement: {}", id, networkInfo);
		if (inProcess.get()) {
			LOG.warn("{}: No need send network request. A transaction already in process", id);
			return false;
		}
		if (!inProcess.compareAndSet(false, true) ) {
			LOG.warn("{}: Network request is not needed. A transaction already in process", id);
			return false;
		}
		if (networkLogic != null) {
			TransactionTO task = new TransactionTO();
			task.setTerm(TermConstant.ISO_SUB_TYPE, networkInfo);
			try {
				networkLogic.solve(this, task);
				if (NetworkInfoConstant.ECHO_TEST.equals(networkInfo) && 
						!ResultCode.SUCCESS_CODE.equals(task.getResultCode())) {
					// echo test failed, try to connect
					LOG.warn("{}: Echo test failed, try to reconnect", id);
					tcpClient.connect();
				}
			} catch (Exception e) {
				LOG.error(id + ": Exception in sendNetworkManagement", e);
			} finally {
				inProcess.set(false);
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param task
	 * @return true if task has been processed, false if task cannot be processed
	 */
	public boolean doTask(TransactionTO task) {
		if (inProcess.get()) {
			LOG.warn("{}: [#{}] Cannot process. Already in process", 
					id, task.getMsgLogNo());
			return false;
		}
		if (!inProcess.compareAndSet(false, true) ) {
			LOG.warn("{}: [#{}] Cannot process again. Already in process", 
					id, task.getMsgLogNo());
			return false;
		}
		
		long startTime = System.currentTimeMillis();
		DjpAbstractIsoLogic logic = null;
		try {
			logic = logicFactory.getLogic(task.getTrxCode());
			LOG.debug("{}: [#{}] <{}> Get Logic: {}", id, task.getMsgLogNo(),
					task.getTrxCode(), getLogicName(logic));
			logic.solve(this, task);
		}
		catch (JetsException je) {
			LOG.warn("{}: [#{}] JetsException [{}]: {}", id, task.getMsgLogNo(),
					je.getResultCode(), je.getMessage());
			handleException(task, je);
		}
		catch (IOException e) {
			LOG.warn(id + ": [#" + task.getMsgLogNo() + "] IOException: " + task, e);
			handleException(task, ResultCode.BTI_NETWORK_IO_ERROR, e.toString());
		}
		catch (Exception e) {
			LOG.error(id + ": [#" + task.getMsgLogNo() + "] Exception: " + task, e);
			handleException(task, ResultCode.BTI_UNKNOWN_ERROR, e.toString());
		}
		finally {
			inProcess.set(false);
			long elapseTime = System.currentTimeMillis() - startTime;
			
			getTrxLogger().info(
					String.format("%s(%s): %s. %d[ms]", id, getLogicName(logic), task
							.toLogFormat(), elapseTime));
		}
		task.setBtiRc(task.getResultCode());
		LOG.debug("logic.isReversalEnabled(): " + logic.isReversalEnabled());
		LOG.debug("task.getResultCode(): " + task.getResultCode());
//		if (logic.isReversalEnabled() && StateConstant.STATE_SETTLE == task.getState() &&
//				ResultCode.BTI_NO_RESPONSE_FROM_HOST.equals(task.getResultCode())) {
		boolean isReversal = checkIfNeedReversal(task, logic);
		LOG.debug("isReversal: " + isReversal);
		
		if (isReversal) {					
			task.setTerm(TermConstant.REVERSAL, true);
			int retry = 0;
			
			/*
			 * Custom max retry for payment eSamsat
			 */
			if (mapMaxRetry != null) {
				Integer x = null;
				if(task.isTermExists(TermConstant.PAYEE_CODE)){
					x = mapMaxRetry.get(task.getTerm(TermConstant.PAYEE_CODE));
				}
				if (x != null)
					maxRetry = x.intValue();
			}
			LOG.debug("Max Retry Reversal New: " + maxRetry);
	
			
			while (retry < maxRetry) {
				if(!tcpClient.isConnected()){
					try {
						int waitResponseTime = settingService.getSettingAsInt(
								SettingService.SETTING_BTI_TIMEOUT);
						Thread.sleep(waitResponseTime);
					} catch (InterruptedException ie) {}
				}
				try {
					retry++;
					// run only once reversal to switching for FUND_TRF_OTH
					if((JetsConstant.TRX_CODE_FUND_TRF_OTH).equals(task.getTrxCode()) && retry > 4){
						LOG.debug("Retry Reversal for FUND_TRF_OTH: " + retry);
						break;
					}
					logic.solve(this, task);
					if(ResultCode.SUCCESS_CODE.equals(task.getResultCode())||ResultCode.RC_94.equals(task.getResultCode()))
						break;
				}
				catch (JetsException je) {
					LOG.warn("{}: [#{}] JetsException [{}]: {}", id, task.getMsgLogNo(),
							je.getResultCode(), je.getMessage());
					handleException(task, je);
				} 
				catch (IOException e) {
					LOG.warn(id + ": [#" + task.getMsgLogNo() + "] IOException: " + task, e);
					handleException(task, ResultCode.BTI_NETWORK_IO_ERROR, e.toString());
				}
			}
			task.setResultCode("R" + task.getResultCode());
		}else {
			task.setTerm(TermConstant.REVERSAL, false);
		}
		return true;
	}
	
	private boolean checkIfNeedReversal(TransactionTO task, DjpAbstractIsoLogic logic) {
//		LOG.debug("list Rev RC: " + logic.getListReversalRc());
//		if (logic.getListReversalRc() == null || logic.getListReversalRc().size() == 0) {
//			return false;
//		}
//		for (String rc: logic.getListReversalRc()) {
//			LOG.debug("Looping ListRev RC: " + rc);
//			if (logic.isReversalEnabled() && rc.equals(task.getResultCode()) && StateConstant.STATE_SETTLE == task.getState())
//				return true;
//		}
		LOG.debug("list Rev RC: " + logic.getListReversalRc());
		LOG.debug("list Biller No: " + logic.getListBillerNo());
		if (logic.isReversalEnabled()){
			if (logic.getListReversalRc() == null || logic.getListReversalRc().size() == 0) {
				return false;
			}
			for (String rc: logic.getListReversalRc()) {
				LOG.debug("Looping ListRev RC: " + rc);
				if(logic.getListBillerNo() !=null){
					if(logic.getListBillerNo().size() > 0){
						for (String billerNo: logic.getListBillerNo()) {
							LOG.debug("Looping Biller No: " + billerNo);
							if (rc.equals(task.getResultCode()) && StateConstant.STATE_SETTLE == task.getState() 
									&& billerNo.equals(task.getTerm(TermConstant.PAYEE_CODE)))
								return true;
						}
					}else{
						LOG.debug("---- 2");
						if (rc.equals(task.getResultCode()) && StateConstant.STATE_SETTLE == task.getState())
						return true;
					}
				}else{
					LOG.debug("---- 1");
					if (rc.equals(task.getResultCode()) && StateConstant.STATE_SETTLE == task.getState())
					return true;
				}
												
			}
		}
		return false;
	}
	
	private void handleException(TransactionTO task, JetsException e) {
		handleException(task, e.getResultCode(), e.getMessage());
	}

	private void handleException(TransactionTO task, String resultCode, String excMsg) {
		task.setResultCode(resultCode);
		task.setSysMessage(excMsg);
	}
	
	private String getLogicName(DjpAbstractIsoLogic logic) {
		if(logic == null) {
			return null;
		}
		return logic.getClass().getSimpleName();
	}
	
	public Logger getTrxLogger() {
		return TRX_LOGGER;
	}
	
	/*
	 * try to send data max 3 times, if get IOException
	 */
	public DjpIsoMsg processToHost(DjpAbstractIsoLogic logic, String request, String seqNo) throws IOException, JetsException {
		int retry = 0;
		while (retry < maxRetry) {
			try {
				sendData(request);
				break;
			}
			catch (IOException e) {
				LOG.warn(id + ": [" + seqNo + "] Unable to send data: <" + request + ">", e);
				if (!tcpClient.isConnected())
					tcpClient.connect();
				retry++;
				if (retry >= maxRetry) {
					LOG.error(id + ": [" + seqNo + "] Send data reached max retry: " + maxRetry);
					throw e;
				}
			}
		}
		DjpIsoMsg isoMsg = waitForResponse(logic, seqNo);
		return isoMsg;
	}
	
	/*
	 * The retry mechanism only execute when the seqNo is not equal
	 * 
	 * There is no retry for IOException, JetsException - IOException will be handled by the parent. -
	 * JetsException will be handled by IsoLogic.
	 */
	private DjpIsoMsg waitForResponse(DjpAbstractIsoLogic logic, String seqNo) throws IOException, JetsException {
		LOG.debug("waitForResponse: " + logic);
		DjpIsoMsg isoMsg = null;
		int retry = 0;
		while (isoMsg == null && retry < maxRetry) {
			String response = receiveData();
			isoMsg = logic.analyzeResponse(id, response, seqNo);
			retry++;
		}
		if (isoMsg == null) {
			LOG.warn(id + ": No response from host for seqNo: " + seqNo);
			throw new JetsException("No response from host for seqNo: " + seqNo, 
					ResultCode.BTI_NO_RESPONSE_FROM_HOST);
		}
		return isoMsg;
	}
	
	public void sendData(String data) throws IOException {
		if (!tcpClient.isConnected()) {
			tcpClient.connect();
			
			if (tcpClient.isConnected()) {
				boolean p = inProcess.get();
				try {
					inProcess.set(false);
					sendNetworkManagement(NetworkInfoConstant.SIGN_ON);
				} finally {
					inProcess.set(p);
				}
			}
		}
		tcpClient.sendData(data);
	}
	
	public String receiveData() throws IOException {
		long startTime = System.currentTimeMillis();
		int waitTime = 100; // wait for 100ms //waitResponseTime / 10;
		int waitResponseTime = settingService.getSettingAsInt(SettingService.SETTING_BTI_TIMEOUT);
		while (System.currentTimeMillis() - startTime < waitResponseTime) {
			if (responseRcv != null) {
				synchronized (this) {
					String msg = responseRcv;
					responseRcv = null;
					if (msg != null)
						return msg;
				}
			}
			if (!tcpClient.isConnected())
				break;
			try {
				Thread.yield();
				Thread.sleep(waitTime);
				Thread.yield();
			} catch (InterruptedException ie) {}
		}  // end while wait
		return null;
	}
	
//	@Override
//	public void notifyDataReceived(Channel channel, String message) {
//		synchronized (this) {
//			responseRcv = message;
//		}
//	}
	@Override
	public void notifyDataReceived(Channel channel, String message) {
		// get ISO Message from HOST BII
		LOG.debug("Receive Data from " + channel.getRemoteAddress() + " [" + message + "]");
		try {
			IsoMsg isoMessage = SultengIsoMsgFactory.createHostIsoMsg().parse(message);
			if (IsoMsgHeader.NETWORK_MANAGEMENT_REQUEST.equals(isoMessage.getHeader())) {
				// ISO Network Request
				networkLogic.processNetworkRequest(this, isoMessage);
				return;
			} else if(IsoMsgHeader.FINANCIAL_TRANSACTION_REQUEST.equals(isoMessage.getHeader())){
				//HandleIsoRequestOtherChannel handleIsoReqOtherChannel = new HandleIsoRequestOtherChannel();
//				if (handleIsoReqOtherChannel != null)
//					handleIsoReqOtherChannel.handleDataReceivedOtherChannel(channel, message);
//				else 
//					LOG.debug("can not process, handle iso req is null, channel : "+channel+" message : "+message);
			}
		} catch (Exception e) {
			LOG.warn("error on notifyDataReceived [" + message + "]", e);
			return;
		}
		synchronized (this) {
			responseRcv = message;
		}
	}
	

	public void setLogicFactory(DjpIsoLogicFactory logicFactory) {
		this.logicFactory = logicFactory;
	}

	public NewTcpClient getTcpClient() {
		return tcpClient;
	}
	@Override
	public void setTcpClient(NewTcpClient tcpClient) {
		this.tcpClient = tcpClient;
	}

	public void setSettingService(SettingService settingService) {
		this.settingService = settingService;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public void setNetworkLogic(DjpNetworkManagementLogic networkLogic) {
		this.networkLogic = networkLogic;
	}
	
//	public void setHandleIsoReqOtherChannel(
//			HandleIsoRequestOtherChannel handleIsoReqOtherChannel) {
//		this.handleIsoReqOtherChannel = handleIsoReqOtherChannel;
//	}

	public Map<String, Integer> getMapMaxRetry() {
		return mapMaxRetry;
	}

	public void setMapMaxRetry(Map<String, Integer> mapMaxRetry) {
		this.mapMaxRetry = mapMaxRetry;
	}

}
