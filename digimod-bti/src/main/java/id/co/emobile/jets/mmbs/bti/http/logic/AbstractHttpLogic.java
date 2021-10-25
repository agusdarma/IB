package id.co.emobile.jets.mmbs.bti.http.logic;

import java.io.IOException;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.co.emobile.jets.mmbs.bti.BtiHttpDelegateAgent;
import id.co.emobile.jets.mmbs.bti.http.HttpMsg;
import id.co.emobile.jets.mmbs.bti.http.data.AbstractResponseVO;
import id.co.emobile.samba.web.data.ResultCode;
import id.co.emobile.samba.web.data.TransactionTO;
import id.co.emobile.samba.web.service.JetsException;

public abstract class AbstractHttpLogic {

protected abstract Logger getLogger();
	
	protected abstract HttpMsg buildHttp(TransactionTO task) throws JetsException;
	
	protected abstract void processHostRef(TransactionTO task, HttpMsg rspHttpMsg);
	
	protected abstract void processSuccessResponse(TransactionTO task,
			HttpMsg rspHttpMsg) throws JetsException;
	
	protected ObjectMapper mapper = new ObjectMapper();
	
	protected boolean reversalEnabled = false;

	public void solve(BtiHttpDelegateAgent agent, TransactionTO task) 
			throws JetsException, IOException {
		HttpMsg httpMsg= buildHttp(task);
//		getLogger().debug("http Msg ---------------- " + httpMsg.toString());
		HttpMsg rspHttpMsg = agent.processToHost(this, httpMsg);
//		getLogger().debug("RESPON HOST ---------------- " + rspHttpMsg.getResponse());
//		getLogger().debug("status code ---------------- " + rspHttpMsg.getStatusCode());
		processResponse(task, rspHttpMsg);
	}
	
	protected void processResponse(TransactionTO task, HttpMsg rspHttpMsg)
			throws JetsException {
		if (rspHttpMsg == null) {
			getLogger().warn("[#{}] No HTTP Response from HOST",
					task.getMsgLogNo());
			throw new JetsException("No Response from HOST", 
					ResultCode.BTI_NO_RESPONSE_FROM_HOST);
		}
		
		processHostRef(task, rspHttpMsg);
		if (rspHttpMsg.getStatusCode() == HttpStatus.SC_OK || 
				rspHttpMsg.getStatusCode() == HttpStatus.SC_ACCEPTED) {
//			getLogger().debug("status code 2 ---------------- " + rspHttpMsg.getStatusCode());
			String hostRc = processRcHost(rspHttpMsg);
//			getLogger().debug("host rc ---------------- " + hostRc);
			if("00".equals(hostRc)){
				processSuccessResponse(task, rspHttpMsg);	
			}else{
				task.setResultCode(hostRc);
//				getLogger().debug("task2 ---------------- " + task);
//				getLogger().debug("result code task2 " + task.getResultCode());
				processFailedResponse(task, rspHttpMsg);
			}			
		} else {
			task.setResultCode(Integer.toString(rspHttpMsg.getStatusCode()));
//			getLogger().debug("task ---------------- " + task);
//			getLogger().debug("result code task " + task.getResultCode());
			processFailedResponse(task, rspHttpMsg);
		}
	}
	
	protected String processRcHost(HttpMsg rspHttpMsg){
		getLogger().debug("Response Host New " + rspHttpMsg.getResponse());
		String resultHost = "99999";
		try {
			AbstractResponseVO resp = mapper.readValue(rspHttpMsg.getResponse(), AbstractResponseVO.class);
			getLogger().debug("AbstractResponseVO new " + resp);
			if("00".equals(resp.getrCode())){
//				return resultHost = "00";
				resultHost = "00";
			}else{
				resultHost = resp.getrCode();
			}
		} catch (JsonParseException e) {
			getLogger().debug("JsonParseException new " + e);
		} catch (JsonMappingException e) {		
			getLogger().debug("JsonMappingException new " + e);
		} catch (IOException e) {
			getLogger().debug("IOException new " + e);
		}
		return resultHost;
	}
	
	protected void processFailedResponse(TransactionTO task, HttpMsg rspHttpMsg)
			throws JetsException {
		//nothing todo
		
		
		
	}

	public boolean isReversalEnabled() {
		return reversalEnabled;
	}

	public void setReversalEnabled(boolean reversalEnabled) {
		this.reversalEnabled = reversalEnabled;
	}

}
