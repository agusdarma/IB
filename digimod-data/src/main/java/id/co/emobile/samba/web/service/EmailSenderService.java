package id.co.emobile.samba.web.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import id.co.emobile.samba.web.http.HttpTransmitterAgent;
import id.co.emobile.samba.web.utils.CommonUtil;

public class EmailSenderService {
	private static final Logger LOG = LoggerFactory.getLogger(EmailSenderService.class);

	private ExecutorService executor;
	private int agentCount = 20;

	@Autowired
	private SettingService settingService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private AppsTimeService timeService;

	@Autowired
	private UserDataService userDataService;

	private HttpTransmitterAgent transmitterAgent;

	public void init() {
		executor = Executors.newFixedThreadPool(agentCount);
		LOG.info("EmailSenderService is started with agentCount: {}", agentCount);
	}

	public void shutdown() {
		try {
			executor.shutdown();
			executor.awaitTermination(5, TimeUnit.SECONDS);
		} catch (Exception e) {
			LOG.warn("Exception in shutdown", e);
		}
		LOG.info("EmailSenderService is shutdown");
	}

	public void sendSms(String phoneNo, String message, String sysLogNo, String trxCode) {
		LOG.debug("sendSms to phoneNo {}, message {}, trxCode {}", phoneNo, message, trxCode);
		try {
			InternalSmsSender sender = new InternalSmsSender(phoneNo, message, sysLogNo, trxCode);
			executor.execute(sender);
		} catch (Exception e) {
			LOG.warn("Exception in sendSms to " + phoneNo, e);
		}
	}

	private class InternalSmsSender implements Runnable {
		final String phoneNo;
		final String message;
		final String sysLogNo;
		final String trxCode;

		InternalSmsSender(String phoneNo, String message, String sysLogNo, String trxCode) {
			this.phoneNo = phoneNo;
			this.message = message;
			this.sysLogNo = sysLogNo;
			this.trxCode = trxCode;
		}

		@Override
		public void run() {
			try {
				String serverUrl = settingService.getSettingAsString(SettingService.SETTING_SMS_GATEWAY_URL);
				String encKey = settingService.getSettingAsString(SettingService.SETTING_SMS_GATEWAY_ENC_KEY);
				if (StringUtils.isEmpty(serverUrl)) {
					LOG.warn("No serverUrl is defined. NOT SENDING SMS");
					return;
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
				String logno = sdf.format(new Date()) + CommonUtil.generateRandomPin(1);
				List<NameValuePair> listParam = new ArrayList<NameValuePair>();

				listParam.add(new BasicNameValuePair("p", phoneNo));
				listParam.add(new BasicNameValuePair("g", ""));
				listParam.add(new BasicNameValuePair("mmbsref", ""));
				listParam.add(new BasicNameValuePair("rc", "0"));
				listParam.add(new BasicNameValuePair("dsacname", ""));
				listParam.add(new BasicNameValuePair("trx", trxCode));
				listParam.add(new BasicNameValuePair("state", "99"));
				listParam.add(new BasicNameValuePair("syslogno", sysLogNo));
				listParam.add(new BasicNameValuePair("msglogno", logno));
				listParam.add(new BasicNameValuePair("c", "SMS"));
				String hexaMessage = CommonUtil.toHexString(message.getBytes());
				listParam.add(new BasicNameValuePair("m", hexaMessage));

				StringBuilder sb = new StringBuilder();
				for (NameValuePair nvp : listParam)
					sb.append(nvp.getName()).append("=").append(nvp.getValue()).append("&");
				LOG.debug("Param Message Send Notif To Gateway -> " + sb.toString());
//				String encMessage = encrypt(sb.toString(), encKey);

//				List<NameValuePair> params = new ArrayList<NameValuePair>();
//				params.add(new BasicNameValuePair("x", encMessage));

//				ResponseData responseData = transmitterAgent.sendGetMessage(serverUrl, params);
//				if (responseData.getResultCode() == 0) {
//					LOG.debug("OK, Response: {}", responseData.getMsgToUser());
//				} else {
//					LOG.warn("Error sending sms: {}", responseData);
//				}

			} catch (Exception e) {
				LOG.warn("Exception in sending SMS to " + phoneNo, e);
			}
		}

	} // InternalSmsSender

	public void setAgentCount(int agentCount) {
		this.agentCount = agentCount;
	}

	public void setTransmitterAgent(HttpTransmitterAgent transmitterAgent) {
		this.transmitterAgent = transmitterAgent;
	}

}
