package id.co.emobile.samba.web.job;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.co.emobile.samba.web.data.HistoryTradingRoot;
import id.co.emobile.samba.web.data.MyFxBookLoginResponseVO;
import id.co.emobile.samba.web.data.ResponseData;
import id.co.emobile.samba.web.data.WebConstants;
import id.co.emobile.samba.web.data.param.MasterTradingAccountParamVO;
import id.co.emobile.samba.web.entity.MasterTradingAccount;
import id.co.emobile.samba.web.entity.UserData;
import id.co.emobile.samba.web.http.HttpTransmitterAgent;
import id.co.emobile.samba.web.service.MasterTradingAccountService;
import id.co.emobile.samba.web.service.UserDataService;

public class SyncHistoryTransactionJob {
	private static final Logger LOG = LoggerFactory.getLogger(SyncHistoryTransactionJob.class);

	private ObjectMapper objectMapper = new ObjectMapper();

	protected Logger getLogger() {
		return LOG;
	}

	@Autowired
	private HttpTransmitterAgent httpTransmitterAgent;

	@Autowired
	private UserDataService userDataService;

	@Autowired
	private MasterTradingAccountService masterTradingAccountService;

	public void syncHistoryTransaction() {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		getLogger().info("Start sync history transaction");
		MyFxBookLoginResponseVO loginResponse = handleLoginResponseMyFxBook(login());
		List<UserData> listIb = userDataService.getListUserIbActive();

		for (UserData userData : listIb) {
			getLogger().info("IB Root ->" + userData.getUserName());
			MasterTradingAccountParamVO masterTradingAccountParamVO = new MasterTradingAccountParamVO();
			masterTradingAccountParamVO.setIbUserCode(userData.getUserCode());
			int size = masterTradingAccountService.countMasterTradingAccountByParam(masterTradingAccountParamVO);
			masterTradingAccountParamVO.setRowStart(1);
			masterTradingAccountParamVO.setRowEnd(100);
			masterTradingAccountParamVO.setRowPerPage(size);
			masterTradingAccountParamVO.setSortVariable("mta.updated_on");
			masterTradingAccountParamVO.setSortOrder(WebConstants.SORT_ORDER_DESC);
			List<MasterTradingAccount> listData = masterTradingAccountService
					.findMasterTradingAccountByParam(masterTradingAccountParamVO);
			for (MasterTradingAccount tradingAccount : listData) {
				getLogger().info("                   -> Member Name : " + tradingAccount.getName());
				HistoryTradingRoot history = handleHistoryTradingByMyFxBookId(
						getHistoryTradingByMyFxBookId(tradingAccount.getMyfxbookId(), loginResponse.getSession()));
//				getLogger().info("history : " + history.toString());
			}

		}
		MyFxBookLoginResponseVO logoutResponse = handleLogoutResponseMyFxBook(logout(loginResponse.getSession()));
		getLogger().info("logoutResponse " + logoutResponse);
		getLogger().info("Sync history transaction finished.");
	}

	private ResponseData login() {
		ResponseData responseData = new ResponseData();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(WebConstants.HTTP_KEY_EMAIL, WebConstants.MYFXBOOK_EMAIL);
		params.put(WebConstants.HTTP_KEY_PASSWORD, WebConstants.MYFXBOOK_PASSWORD);
		getLogger().info("Initiated login.");
		responseData = httpTransmitterAgent.sendGetMessage(WebConstants.MYFXBOOK_URL_LOGIN, params);
		return responseData;
	}

	private ResponseData logout(String session) {
		ResponseData responseData = new ResponseData();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(WebConstants.HTTP_KEY_SESSION, session);
		getLogger().info("Initiated logout.");
		responseData = httpTransmitterAgent.sendGetMessage(WebConstants.MYFXBOOK_URL_LOGOUT, params);
		return responseData;
	}

	private ResponseData getHistoryTradingByMyFxBookId(String myFxBookId, String session) {
		ResponseData responseData = new ResponseData();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(WebConstants.HTTP_KEY_SESSION, session);
		params.put(WebConstants.HTTP_KEY_MYFXBOOKID, myFxBookId);
//		getLogger().info("Initiated getHistoryTradingByMyFxBookId.");
		responseData = httpTransmitterAgent.sendGetMessage(WebConstants.MYFXBOOK_URL_GET_HISTORY_TRADING, params);
		return responseData;
	}

	private MyFxBookLoginResponseVO handleLoginResponseMyFxBook(ResponseData responseData) {
		getLogger().info("handleLoginResponseMyFxBook with data " + responseData.getMsgToUser());
		MyFxBookLoginResponseVO myFxBookLoginResponseVO = new MyFxBookLoginResponseVO();
		try {
			myFxBookLoginResponseVO = objectMapper.readValue(responseData.getMsgToUser(),
					MyFxBookLoginResponseVO.class);
		} catch (Exception e) {
			getLogger().error("Error exception handleLoginResponseMyFxBook " + e.toString());
		}
		getLogger().info("MyFxBookLoginResponseVO -> " + myFxBookLoginResponseVO);
		return myFxBookLoginResponseVO;
	}

	private MyFxBookLoginResponseVO handleLogoutResponseMyFxBook(ResponseData responseData) {
//		getLogger().info("handleLogoutResponseMyFxBook with data " + responseData.getMsgToUser());
		MyFxBookLoginResponseVO myFxBookLoginResponseVO = new MyFxBookLoginResponseVO();
		try {
			myFxBookLoginResponseVO = objectMapper.readValue(responseData.getMsgToUser(),
					MyFxBookLoginResponseVO.class);
		} catch (Exception e) {
			getLogger().error("Error exception handleLoginResponseMyFxBook " + e.toString());
		}
//		getLogger().info("MyFxBookLoginResponseVO -> " + myFxBookLoginResponseVO);
		return myFxBookLoginResponseVO;
	}

	private HistoryTradingRoot handleHistoryTradingByMyFxBookId(ResponseData responseData) {
//		getLogger().info("handleHistoryTradingByMyFxBookId with data " + responseData.getMsgToUser());
		HistoryTradingRoot historyTradingRoot = new HistoryTradingRoot();
		try {
			historyTradingRoot = objectMapper.readValue(responseData.getMsgToUser(), HistoryTradingRoot.class);
		} catch (Exception e) {
			getLogger().error("Error exception handleLoginResponseMyFxBook " + e.toString());
		}
//		getLogger().info("HistoryTradingRoot -> " + historyTradingRoot);
		return historyTradingRoot;
	}
}
