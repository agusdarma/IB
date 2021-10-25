package id.co.emobile.samba.web.iso.logic;

import java.util.Map;

import id.co.emobile.samba.web.data.ResultCode;
import id.co.emobile.samba.web.service.JetsException;

public class SddIsoLogicFactory {
	
	private Map<String, SddAbstractIsoLogic> logicMap;
	
	public SddAbstractIsoLogic getLogic(String trxCode) throws JetsException {
		SddAbstractIsoLogic logic = logicMap.get(trxCode);
		if (logic == null) {
			throw new JetsException("Unsupported trx: " + trxCode, 
					ResultCode.BTI_UNSUPPORTED_TRX);
		}
		return logic;
	}
	
	public void setLogicMap(Map<String, SddAbstractIsoLogic> logicMap) {
		this.logicMap = logicMap;
	}

}
