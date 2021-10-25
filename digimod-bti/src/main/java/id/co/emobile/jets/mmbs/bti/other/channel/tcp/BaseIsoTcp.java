package id.co.emobile.jets.mmbs.bti.other.channel.tcp;

import org.apache.log4j.Logger;

public class BaseIsoTcp {
	protected final Logger logger = Logger.getLogger(getClass());

	protected String id;	

	public BaseIsoTcp() {
	}
	
	public BaseIsoTcp(String id) {
		this.id = id;
	}

	public void logError(String message) {
		logger.error(formatMessage(message));
	}
	
	public void logError(String message, Throwable t) {
		logger.error(formatMessage(message), t);
	}
	
	public void logWarn(String message) {
		logger.warn(formatMessage(message));
	}
	
	public void logInfo(String message) {
		if (logger.isInfoEnabled())
			logger.info(formatMessage(message));
	}

	public void logDebug(String message) {
		if (logger.isDebugEnabled())
			logger.debug(formatMessage(message));
	}

	protected String formatMessage(String message) {
		StringBuffer sb = new StringBuffer();
		sb.append(id);
		sb.append(": ");
		sb.append(message);
		return sb.toString();
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
