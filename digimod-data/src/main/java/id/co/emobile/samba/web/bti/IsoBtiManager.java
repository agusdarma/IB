package id.co.emobile.samba.web.bti;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import id.co.emobile.samba.web.data.ResultCode;
import id.co.emobile.samba.web.data.TransactionTO;
import id.co.emobile.samba.web.iso.helper.NetworkInfoConstant;
import id.co.emobile.samba.web.iso.logic.IsoLogicFactory;
import id.co.emobile.samba.web.iso.logic.NetworkManagementLogic;
import id.co.emobile.samba.web.netty.BasePipelineFactory;
import id.co.emobile.samba.web.netty.NewTcpClient;
import id.co.emobile.samba.web.service.AppsTimeService;
import id.co.emobile.samba.web.service.JetsException;
import id.co.emobile.samba.web.service.SettingService;

public class IsoBtiManager {
	private static final Logger LOG = LoggerFactory.getLogger(IsoBtiManager.class);
	
	@Autowired
	private SettingService settingService;
	
	@Autowired
	private AppsTimeService timeService;
	
	private List<String> listConfig;
	private IsoLogicFactory logicFactory;
	private int socketTimeout;
	private BasePipelineFactory pipelineFactory;
	
	private List<BtiIsoDelegateAgent> listAgent;
	private AtomicInteger agentIdx = new AtomicInteger(0);
	
	private NetworkManagementLogic networkLogic;
	
//	@Autowired
//	private HandleIsoRequestOtherChannel handleIsoReqOtherChannel;
	
	public void startManager() {
		listAgent = new ArrayList<BtiIsoDelegateAgent>();
		for (String config: listConfig) {
			// id, ip, port
			String[] parts = config.split(",");
			String id = parts[0];
			int amount = Integer.parseInt(parts[1]);
			String hostIp = parts[2];
			int hostPort = Integer.parseInt(parts[3]);
			
			for (int i=0; i < amount; i++) {
				BtiIsoDelegateAgent agent = new BtiIsoDelegateAgent();
				agent.setId(id + "_" + (i + 1));
				agent.setLogicFactory(logicFactory);
				agent.setSettingService(settingService);
				agent.setNetworkLogic(networkLogic);
//				agent.setHandleIsoReqOtherChannel(handleIsoReqOtherChannel);
				
				NewTcpClient tcpClient = new NewTcpClient();
				tcpClient.setHost(hostIp);
				tcpClient.setPort(hostPort);
				tcpClient.setPipelineFactory(pipelineFactory);
				tcpClient.setSocketTimeout(socketTimeout);
				tcpClient.setTcpNotifier(agent);
				
				tcpClient.connect();
				if (tcpClient.isConnected()) {
					agent.sendNetworkManagement(NetworkInfoConstant.SIGN_ON);
				}
				
				listAgent.add(agent);
			}  // end for -> looping worker
		}  // end for -> looping config
		LOG.info("BTI Manager has been initialized with {} agent(s)", listAgent.size());		
	}
	
//	public void stopManager() {
//		for (BtiIsoDelegateAgent agent: listAgent) {
//			agent.getTcpClient().shutdown();
//		}
//		LOG.info("BTI Manager has been stopped");		
//	}
	public void stopManager() {
		for (BtiIsoDelegateAgent agent: listAgent) {
			agent.sendNetworkManagement(NetworkInfoConstant.SIGN_OFF);
			try {
				Thread.sleep(100);
			} catch (InterruptedException ie) {}
			
			agent.getTcpClient().shutdown();
		}
		LOG.info("BTI Manager has been stopped");		
	}
	
//	public void processDistribution(DistributionHeader header, DistributionDetail detail) {
//		LOG.debug("sendTransactionToHost: {}", detail);
//		try {
//			// create TransactionTO here from header and detail
//			
//		} catch (Exception e) {
//			LOG.warn("Exception sendTransactionToHost for " + detail, e);
//			detail.setHostRc(ResultCode.ERR_UNKNOWN);
//			detail.setHostMessage("Error processing data, error: " + e.getMessage());
//		}
//	}
	
	public TransactionTO processDistribution(TransactionTO task) {
		LOG.debug("sendTransactionToHost: {}", task);
		try {
			process(task);
		} catch (Exception e) {
			LOG.warn("Exception sendTransactionToHost for " + task, e);
//			detail.setHostRc(ResultCode.ERR_UNKNOWN);
//			detail.setHostMessage("Error processing data, error: " + e.getMessage());
		}
		return task;
	}
	
//	@Override
	public void process(TransactionTO task) {
		LOG.info("[#{}] BTI Process: {}", task.getMsgLogNo(), task);
		try {
			task.setBtiArrivedTime(timeService.getCurrentTimemillis());
			int timeout = settingService.getSettingAsInt(SettingService.SETTING_TRANSACTION_TIMEOUT);
			int count=0;
			boolean isProcessed = false;
			while (count < 10) {
				long startTime = timeService.getCurrentTimemillis();
				long processTime = startTime - task.getReceivedTime();
				if (processTime > timeout) {
					// transaction is timeout
					LOG.warn("[#{}] Process Time {}ms, exceed timeout {}ms",
							task.getMsgLogNo(), processTime, timeout);
					throw new JetsException("Transaction timeout in BTI", 
							ResultCode.BTI_TRANSACTION_EXPIRED);
				}  // end if timeout
				if (tryToAssignTask(task)) {
					isProcessed = true;
					break;
				}
				if (count % 2 == 0) {
					// wait for 200ms after 2 check
					try {
						Thread.sleep(200);
						Thread.yield();
					} catch (InterruptedException e) {}
				}
			}  // end while -> looping get btiRunnableWorker
			if (!isProcessed) {
				// transaction is not processed
				LOG.warn("[#{}] No BTI Agent is available after {} check",
						task.getMsgLogNo(), count);
				throw new JetsException("No BTI Agent is available", 
						ResultCode.BTI_TRANSACTION_EXPIRED);
			}  // end if not processed
		} catch (JetsException je) {
			task.setResultCode(je.getResultCode());
			task.setSysMessage(je.getMessage());
		} catch (Exception e) {
			LOG.warn("[#" + task.getMsgLogNo() + "] Exception: " + task, e);
			task.setResultCode(ResultCode.BTI_UNKNOWN_ERROR);
			task.setSysMessage(e.toString());
		} finally {
			long delta = timeService.getCurrentTimemillis() - task.getBtiArrivedTime();
			task.setBtiProcess((int) delta);
			LOG.info("[#{}] Finish BTI Process: {} in {}ms", 
					task.getMsgLogNo(), task, delta);
		}
	}
	
	private boolean tryToAssignTask(TransactionTO task) throws JetsException {
		int idx = agentIdx.get();
		if (idx >= 2000000000) {
			synchronized (this) {
				// reset workerIdx if it reached beyond 2M
				if (idx >= 2000000000) {
					agentIdx.set(0);
				}
			}
		}
		
		int localIdx = agentIdx.getAndIncrement() % listAgent.size();
		BtiIsoDelegateAgent agent = listAgent.get(localIdx);
		return agent.doTask(task);
	}
	
	public void checkConnection() {
		for (BtiIsoDelegateAgent agent: listAgent) {
			agent.sendNetworkManagement(NetworkInfoConstant.ECHO_TEST);
		}
	}
	
	/**
	 * set config for RunnableWorker with format id, amount, ip, port
	 * @param listConfig
	 */
	public void setListConfig(List<String> listConfig) {
		this.listConfig = listConfig;
	}

	public void setLogicFactory(IsoLogicFactory logicFactory) {
		this.logicFactory = logicFactory;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public void setPipelineFactory(BasePipelineFactory pipelineFactory) {
		this.pipelineFactory = pipelineFactory;
	}
	
	public void setNetworkLogic(NetworkManagementLogic networkLogic) {
		this.networkLogic = networkLogic;
	}
}
