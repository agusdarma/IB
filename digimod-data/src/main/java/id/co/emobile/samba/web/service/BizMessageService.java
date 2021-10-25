package id.co.emobile.samba.web.service;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

@Service
public class BizMessageService {
	private static final Logger LOGGER = LoggerFactory.getLogger(BizMessageService.class);
	
	@Autowired
	private MessageSource messageSource;
	
	public String getMessageFor(String key) {
		String value = messageSource.getMessage(key, null, null);
		if (value == null) {
			LOGGER.warn("Unable to find message for: {}. Using defaults", key);
			return key;
		} else
			return value;
	}
	
	public String getErrorMessageFor(int rc) {
		return getMessageFor("rc." + rc);
	}
	
	public String getMessageFor(String key, Locale language) {
		String value = messageSource.getMessage(key, null, language);
		if (value == null) {
			LOGGER.warn("Unable to find message for: {}. Using defaults", key);
			return key;
		} else
			return value;
	}
	
	public String getMessageFor(String key, String[] args, Locale language) {
		String value = messageSource.getMessage(key, args, language);
		if (value == null) {
			LOGGER.warn("Unable to find message for: {}. Using defaults", key);
			return key;
		} else
			return value;
	}
		
	public String getSysMessageFrom(SambaWebException jbe) {
		String message = null;
		try {
		if (jbe.hasInfo())
			message = messageSource.getMessage("rc." + jbe.getErrorCode(), jbe.getInfo(), null);
		else
			message = messageSource.getMessage("rc." + jbe.getErrorCode(), null, null);
		} catch (NoSuchMessageException nsme) {
			LOGGER.warn("Unable to find rc." + jbe.getErrorCode() + ": " + nsme.getMessage());
		}
		if (message == null)
			message = "No Message is defined for: rc." + jbe.getErrorCode();
		return message;
	}
	
}
