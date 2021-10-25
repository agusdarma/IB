package id.co.emobile.jets.mmbs.bti;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.co.emobile.jets.mmbs.bti.http.HttpMsg;
import id.co.emobile.jets.mmbs.bti.http.data.AbstractResponseVO;
import id.co.emobile.jets.mmbs.bti.http.logic.AbstractHttpLogic;
import id.co.emobile.jets.mmbs.bti.http.logic.HttpLogicFactory;
import id.co.emobile.samba.web.data.ResultCode;
import id.co.emobile.samba.web.data.TermConstant;
import id.co.emobile.samba.web.data.TransactionTO;
import id.co.emobile.samba.web.service.JetsException;
import id.co.emobile.samba.web.service.SettingService;

public class BtiHttpDelegateAgent {
	private static final Logger LOG = LoggerFactory.getLogger(BtiHttpDelegateAgent.class);
	private final static Logger TRX_LOGGER = LoggerFactory.getLogger("bti.transaction");

	private String id;
	private int maxRetry = 3;
	private HttpLogicFactory logicFactory;
	
	private int timeout = 30000;
	private int maxConnection = 100;
	private String httpMethod;
	private String hostUrl;
	
	private AtomicBoolean inProcess = new AtomicBoolean(false);
	
	private HttpClient httpClient;
	private SettingService settingService;
	private PoolingHttpClientConnectionManager cm;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	public void start() {
		cm = new PoolingHttpClientConnectionManager();
	
		cm.setDefaultMaxPerRoute(maxConnection);
		cm.setMaxTotal(maxConnection);
	
		// Create socket configuration
	    SocketConfig socketConfig = SocketConfig.custom()
	        .setTcpNoDelay(true)
	        .setSoTimeout(timeout)
	        .build();
	    LOG.debug("Use Socket Config: " + socketConfig.toString());
	    cm.setDefaultSocketConfig(socketConfig);
	    
	    HttpClientBuilder httpClientBuilder = HttpClients.custom().
	    		setConnectionManager(cm);
    

	    httpClient = httpClientBuilder.build(); 
	
		LOG.info("[{}] BtiHttpDelegateAgent is started with timeout {}ms," +
				" MaxConnection: {}", id, timeout, maxConnection );
	}
	
	public void stop() {
		cm.shutdown();
		LOG.info("[{}] BtiHttpDelegateAgent has been stopped", id );
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
		AbstractHttpLogic logic = null;
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
//		LOG.debug("--------bti rc " + task.getBtiRc());
//		LOG.debug("--------xti rc " + task.getXtiRc());
//		LOG.debug("logic.isReversalEnabled(): " + logic.isReversalEnabled());
//		if (logic.isReversalEnabled() && !ResultCode.XTI_RESPONSE_SUCCESS.equals(task.getXtiRc())) {
//			//check Rc for reversal
//			
////			if(StringUtils.isNotEmpty(listRcReversal)){
////				String[] rcArray = listRcReversal.split(",");
////				for (String rc : rcArray) {
////					if(rc.equals(task.getXtiRc())){
////						LOG.debug("rc xti : "+task.getXtiRc()+ " is need to reversal");
////						task.setTerm(TermConstant.REVERSAL, true);
////						break;
////					}
////				}
////			}
//			
//			
//			task.setTerm(TermConstant.REVERSAL, true);
//			int retry = 0;
//			while (retry < maxRetry) {
//				try {
//					retry++;
//					logic.solve(this, task);
//					if (ResultCode.SUCCESS_CODE.equals(task.getResultCode()))
//						break;
//				}
//				catch (JetsException je) {
//					LOG.warn("{}: [#{}] JetsException [{}]: {}", id, task.getMsgLogNo(),
//							je.getResultCode(), je.getMessage());
//					handleException(task, je);
//				}
//				catch (IOException e) {
//					LOG.warn(id + ": [#" + task.getMsgLogNo() + "] IOException: " + task, e);
//					handleException(task, ResultCode.BTI_NETWORK_IO_ERROR, e.toString());
//				}				
//			}
//			task.setResultCode("R" + task.getResultCode());
//		} else {
//			task.setTerm(TermConstant.REVERSAL, false);
//		}
		
		if (task.getTermAsBoolean(TermConstant.REVERSAL)) {
			task.setResultCode("R" + task.getResultCode());
		}
		
		return true;
	}
	
	private boolean processRcHost(HttpMsg rspHttpMsg){
		LOG.warn("Response Host {}", rspHttpMsg.getResponse());
		boolean resultHost = false;
		try {
			AbstractResponseVO resp = mapper.readValue(rspHttpMsg.getResponse(), AbstractResponseVO.class);
			LOG.warn("AbstractResponseVO {}", resp);
			if("00".equals(resp.getrCode())){
				return resultHost = true;
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultHost;
	}
	
	public HttpMsg processToHost(AbstractHttpLogic logic, HttpMsg httpMsg) throws JetsException {
		HttpPost httpPost = null;
		HttpGet httpGet = null;
		try {
			LOG.debug("Send {} request to {}: {}", httpMethod, hostUrl, httpMsg.getEntity());
			if ("POST".equals(httpMethod) || "JSON".equals(httpMethod)) {
				httpPost = new HttpPost(hostUrl);
				
				httpPost.setEntity(new StringEntity(httpMsg.getEntity(), Consts.UTF_8));
				
				HttpResponse response = httpClient.execute(httpPost);
				int statusCode = response.getStatusLine().getStatusCode();
				httpMsg.setStatusCode(statusCode);
				
				if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_ACCEPTED) {
					String respString = EntityUtils.toString(response.getEntity());
	                LOG.debug("Response: {}", respString);
	                httpMsg.setResponse(respString);
				} else {
					LOG.warn("Invalid statusCode: {}", statusCode);
				}
			} else if ("GET".equals(httpMethod)) {
				URIBuilder builder = new URIBuilder(hostUrl);
				for (NameValuePair data: httpMsg.getParams()) {
					builder.addParameter(data.getName(), data.getValue());
				}
				
				httpGet = new HttpGet(builder.build());
				
				HttpResponse response = httpClient.execute(httpGet);
				int statusCode = response.getStatusLine().getStatusCode();
				httpMsg.setStatusCode(statusCode);
				
				if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_ACCEPTED) {
					String respString = EntityUtils.toString(response.getEntity());
	                LOG.debug("Response: {}", respString);
	                httpMsg.setResponse(respString);
				} else {
					LOG.warn("Invalid statusCode: {}", statusCode);
				}
			}
		} catch (Exception e) {
			if (httpPost != null)
				httpPost.abort();
			if (httpGet != null)
				httpGet.abort();
            LOG.warn("Unknown Error", e);
            
            throw new JetsException("Failed send HTTP", 
            		ResultCode.BTI_UNKNOWN_ERROR);
        } finally {
        	if (httpPost != null)
        		httpPost.releaseConnection();
        	if (httpGet != null)
        		httpGet.releaseConnection();
        } // end try catch
		return httpMsg;
	}
	
	private void handleException(TransactionTO task, JetsException e) {
		handleException(task, e.getResultCode(), e.getMessage());
	}

	private void handleException(TransactionTO task, String resultCode, String excMsg) {
		task.setResultCode(resultCode);
		task.setSysMessage(excMsg);
	}
	
	private String getLogicName(AbstractHttpLogic logic) {
		if (logic == null) {
			return null;
		}
		return logic.getClass().getSimpleName();
	}
	
	public Logger getTrxLogger() {
		return TRX_LOGGER;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMaxRetry(int maxRetry) {
		this.maxRetry = maxRetry;
	}

	public void setLogicFactory(HttpLogicFactory logicFactory) {
		this.logicFactory = logicFactory;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public void setHostUrl(String hostUrl) {
		this.hostUrl = hostUrl;
	}
	
}
