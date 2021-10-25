//package id.co.emobile.jets.mmbs.bti;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.emobile.jets.mmbs.lib.logic.MmbsModuleHandler;
//
//import id.co.emobile.jets.mmbs.bti.http.logic.HttpLogicFactory;
//import id.co.emobile.samba.web.data.ResultCode;
//import id.co.emobile.samba.web.data.TransactionTO;
//import id.co.emobile.samba.web.service.AppsTimeService;
//import id.co.emobile.samba.web.service.JetsException;
//import id.co.emobile.samba.web.service.SettingService;
//
//public class HttpBtiManager implements MmbsModuleHandler {
//	private static final Logger LOG = LoggerFactory.getLogger(HttpBtiManager.class);
//
//	private String httpMethod = "GET";
//	private int maxRetry = 3;
//	private int timeout = 30000;
//	
//	@Autowired
//	private SettingService settingService;
//	
//	@Autowired
//	private AppsTimeService timeService;
//	
//	private List<String> listConfig;
//	private HttpLogicFactory logicFactory;
//	
//	private List<BtiHttpDelegateAgent> listAgent;
//	private AtomicInteger agentIdx = new AtomicInteger(0);
//	
//	public void startManager() {
//		listAgent = new ArrayList<BtiHttpDelegateAgent>();
//		for (String config: listConfig) {
//			// id, amount, url
//			String[] parts = config.split(",");
//			String id = parts[0];
//			int amount = Integer.parseInt(parts[1]);
//			String hostUrl = parts[2];
//			
//			for (int i=0; i < amount; i++) {
//				BtiHttpDelegateAgent agent = new BtiHttpDelegateAgent();
//				agent.setId(id + "_" + (i + 1));
//				agent.setMaxRetry(maxRetry);
//				agent.setTimeout(timeout);
//				agent.setLogicFactory(logicFactory);
//				agent.setHttpMethod(httpMethod);
//				agent.setHostUrl(hostUrl);
//				
//				agent.start();
//				
//				listAgent.add(agent);
//			}
//		}
//	}
//	
//	public void stopManager() {
//		for (BtiHttpDelegateAgent agent : listAgent) {
//			agent.stop();
//		}
//	}
//	
//	@Override
//	public void process(TransactionTO task) {
//		LOG.info("[#{}] BTI Process: {}", task.getMsgLogNo(), task);
//		try {
//			task.setBtiArrivedTime(timeService.getCurrentTimemillis());
//			int timeout = settingService.getSettingDefaultAsInt(SettingService.SETTING_TRANSACTION_TIMEOUT);
//			int count=0;
//			boolean isProcessed = false;
//			while (count < 10) {
//				long startTime = timeService.getCurrentTimemillis();
//				long processTime = startTime - task.getReceivedTime();
//				if (processTime > timeout) {
//					// transaction is timeout
//					LOG.warn("[#{}] Process Time {}ms, exceed timeout {}ms",
//							task.getMsgLogNo(), processTime, timeout);
//					throw new JetsException("Transaction timeout in BTI", 
//							ResultCode.BTI_TRANSACTION_EXPIRED);
//				}  // end if timeout
//				if (tryToAssignTask(task)) {
//					isProcessed = true;
//					break;
//				}
//				if (count % 2 == 0) {
//					// wait for 200ms after 2 check
//					try {
//						Thread.sleep(200);
//						Thread.yield();
//					} catch (InterruptedException e) {}
//				}
//			}  // end while -> looping get btiRunnableWorker
//			if (!isProcessed) {
//				// transaction is not processed
//				LOG.warn("[#{}] No BTI Agent is available after {} check",
//						task.getMsgLogNo(), count);
//				throw new JetsException("No BTI Agent is available", 
//						ResultCode.BTI_TRANSACTION_EXPIRED);
//			}  // end if not processed
//		} catch (JetsException je) {
//			task.setResultCode(je.getResultCode());
//			task.setSysMessage(je.getMessage());
//		} catch (Exception e) {
//			LOG.warn("[#" + task.getMsgLogNo() + "] Exception: " + task, e);
//			task.setResultCode(ResultCode.BTI_UNKNOWN_ERROR);
//			task.setSysMessage(e.toString());
//		} finally {
//			long delta = timeService.getCurrentTimemillis() - task.getBtiArrivedTime();
//			task.setBtiProcess((int) delta);
//			LOG.info("[#{}] Finish BTI Process: {} in {}ms", 
//					task.getMsgLogNo(), task, delta);
//		}
//	}
//
//	private boolean tryToAssignTask(TransactionTO task) throws JetsException {
//		int idx = agentIdx.get();
//		if (idx >= 2000000000) {
//			synchronized (this) {
//				// reset workerIdx if it reached beyond 2M
//				if (idx >= 2000000000) {
//					agentIdx.set(0);
//				}
//			}
//		}
//		
//		int localIdx = agentIdx.getAndIncrement() % listAgent.size();
//		BtiHttpDelegateAgent agent = listAgent.get(localIdx);
//		return agent.doTask(task);
//	}
//	
//	public void setLogicFactory(HttpLogicFactory logicFactory) {
//		this.logicFactory = logicFactory;
//	}
//
//	public void setListConfig(List<String> listConfig) {
//		this.listConfig = listConfig;
//	}
//
//	public void setMaxRetry(int maxRetry) {
//		this.maxRetry = maxRetry;
//	}
//
//	public void setTimeout(int timeout) {
//		this.timeout = timeout;
//	}
//
//	public void setHttpMethod(String httpMethod) {
//		this.httpMethod = httpMethod;
//	}
//}
