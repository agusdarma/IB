package id.co.emobile.samba.web.interceptor;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.interceptor.PreResultListener;

import id.co.emobile.samba.web.action.BaseAction;
import id.co.emobile.samba.web.data.UserDataLoginVO;
import id.co.emobile.samba.web.utils.CipherUtils;

public class ModuleStateInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = LoggerFactory.getLogger(ModuleStateInterceptor.class);

	private ObjectMapper objectMapper = new ObjectMapper();
	private TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Object o = invocation.getAction();
		if (! (o instanceof ModuleStateAware)) {
			return invocation.invoke();
		}
		final Map<String, Object> session = invocation.getInvocationContext().getSession();
		UserDataLoginVO userData = (UserDataLoginVO) session.get(BaseAction.LOGIN_KEY);
		if (userData == null) {
			return invocation.invoke();
		}
		final String encKey = userData.getSessionId();
		final ModuleStateAware action = (ModuleStateAware) o;
		final String[] sessionKeys = action.getSessionKeyToHandle();
		
		// retrieve data to set on session
		String encPrevState = action.getModuleState();
		if (StringUtils.isNotEmpty(encPrevState)) {
			String prevState = CipherUtils.decryptAESPKCS7(encPrevState, encKey);
//			LOG.debug("PrevState: {}", prevState);
			Map<String, Object> prevSession = objectMapper.readValue(prevState, typeRef);
			for (String sessionKey: sessionKeys) {
				Object prevObj = prevSession.remove(sessionKey);
				if (prevObj == null) continue;
				session.put(sessionKey, prevObj);
			}
		}
		
		invocation.addPreResultListener(new PreResultListener() {
			
			@Override
			public void beforeResult(ActionInvocation invocation, String resultCode) {
				Map<String, Object> listHandle = new HashMap<>();
				for (String sessionKey: sessionKeys) {
					Object obj = session.get(sessionKey);
					if (obj == null) continue;
					listHandle.put(sessionKey, obj);
				}
				if (listHandle.size() > 0) {
					try {
						String currentState = objectMapper.writeValueAsString(listHandle);
//						LOG.debug("CurrentState: {}", currentState);
						String encCurrentState = CipherUtils.encryptAESPKCS7(currentState, encKey);
						action.setModuleState(encCurrentState);
//						LOG.debug("Set moduleState: {}", encCurrentState);
					} catch (Exception e) {
						LOG.warn("Exception set currentState", e);
					}
				}
			}  // end beforeResult
		});
		return invocation.invoke();
	}

}
