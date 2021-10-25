package id.co.emobile.samba.web.interceptor;

import id.co.emobile.samba.web.utils.CommonUtil;

import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class SanitizerInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(SanitizerInterceptor.class);

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Map<String, Object> parameters = ActionContext.getContext().getParameters();
		Iterator<String> iter = parameters.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			Object o = parameters.get(key);
			if (o instanceof String[]) {
				String[] values = (String[]) o;
				for (int i=0; i < values.length; i++) {
					values[i] = CommonUtil.htmlPurifier(values[i]);
				}
				parameters.put(key, values);
			}
		}
		return invocation.invoke();
	}

}
