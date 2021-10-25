package id.co.emobile.samba.web.iso.logic;

import java.util.Map;

import id.co.emobile.samba.web.data.ResultCode;
import id.co.emobile.samba.web.service.JetsException;

public class DjpIsoLogicFactory {
	
	private Map<String, DjpAbstractIsoLogic> logicMap;
	
	public DjpAbstractIsoLogic getLogic(String trxCode) throws JetsException {
		DjpAbstractIsoLogic logic = logicMap.get(trxCode);
		if (logic == null) {
			throw new JetsException("Unsupported trx: " + trxCode, 
					ResultCode.BTI_UNSUPPORTED_TRX);
		}
		return logic;
	}
	
	public void setLogicMap(Map<String, DjpAbstractIsoLogic> logicMap) {
		this.logicMap = logicMap;
	}

}
