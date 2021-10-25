package id.co.emobile.jets.mmbs.bti.http.logic;

import java.util.Map;

import id.co.emobile.samba.web.data.ResultCode;
import id.co.emobile.samba.web.service.JetsException;

public class HttpLogicFactory {
	
	private Map<String, AbstractHttpLogic> logicMap;
	
	public AbstractHttpLogic getLogic(String trxCode) throws JetsException {
		AbstractHttpLogic logic = logicMap.get(trxCode);
		if (logic == null) {
			throw new JetsException("Unsupported trx: " + trxCode, 
					ResultCode.BTI_UNSUPPORTED_TRX);
		}
		return logic;
	}
	
	public void setLogicMap(Map<String, AbstractHttpLogic> logicMap) {
		this.logicMap = logicMap;
	}

}
