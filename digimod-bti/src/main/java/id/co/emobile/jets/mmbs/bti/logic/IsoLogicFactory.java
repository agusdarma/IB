package id.co.emobile.jets.mmbs.bti.logic;

import java.util.Map;

import id.co.emobile.samba.web.data.ResultCode;
import id.co.emobile.samba.web.service.JetsException;

public class IsoLogicFactory {
	
	private Map<String, AbstractIsoLogic> logicMap;
	
	public AbstractIsoLogic getLogic(String trxCode) throws JetsException {
		AbstractIsoLogic logic = logicMap.get(trxCode);
		if (logic == null) {
			throw new JetsException("Unsupported trx: " + trxCode, 
					ResultCode.BTI_UNSUPPORTED_TRX);
		}
		return logic;
	}
	
	public void setLogicMap(Map<String, AbstractIsoLogic> logicMap) {
		this.logicMap = logicMap;
	}

}
