package id.co.emobile.samba.web.job;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import id.co.emobile.samba.web.entity.HistoryTrading;
import id.co.emobile.samba.web.entity.MasterTradingAccount;
import id.co.emobile.samba.web.entity.UserData;
import id.co.emobile.samba.web.http.HttpTransmitterAgent;
import id.co.emobile.samba.web.mapper.HistoryTradingMapper;
import id.co.emobile.samba.web.mapper.UserDataMapper;
import id.co.emobile.samba.web.service.CalculateCommissionService;
import id.co.emobile.samba.web.service.MasterTradingAccountService;
import id.co.emobile.samba.web.service.UserDataService;
import id.co.emobile.samba.web.utils.CommonUtil;

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
	private UserDataMapper userDataMapper;

	@Autowired
	private CalculateCommissionService calculateCommissionService;

	@Autowired
	private MasterTradingAccountService masterTradingAccountService;

	@Autowired
	private HistoryTradingMapper historyTradingMapper;

	public void syncHistoryTransaction() {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		getLogger().info("Start sync history transaction");
		long time = System.currentTimeMillis();
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
//				getLogger().info("history : " + history.getHistory().size());
				insertUpdateHistory(history, tradingAccount);
			}

		}

		time = System.currentTimeMillis() - time;
		getLogger().info("Sync history transaction finished took " + time + " milliseconds");
		try {
			TimeUnit.SECONDS.sleep(5);
			MyFxBookLoginResponseVO logoutResponse = handleLogoutResponseMyFxBook(logout(loginResponse.getSession()));
			getLogger().info("logoutResponse " + logoutResponse);
		} catch (Exception e) {
			getLogger().error("Error exception logout " + e.toString());
		}

	}

	private void insertUpdateHistory(HistoryTradingRoot history, MasterTradingAccount tradingAccount) {
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.FLOOR);
		UserData userIb = userDataMapper.findUserDataByUserCode(tradingAccount.getIbUserCode());
		double clientCommissionDouble = 0;
		if (userIb.getTotalClientCommission() != null) {
			try {
				clientCommissionDouble = Double.parseDouble(userIb.getTotalClientCommission());
			} catch (Exception e) {

			}
		}

		for (int i = 0; i < history.getHistory().size(); i++) {

			String symbol = history.getHistory().get(i).getSymbol();
			String tempOpenTime = history.getHistory().get(i).getOpenTime();
			String tempCloseTime = history.getHistory().get(i).getCloseTime();
			Date openTime = null;
			Date closeTime = null;
			try {
				openTime = CommonUtil.strToDateTime(CommonUtil.formatTimeMyfxBook, tempOpenTime);
				closeTime = CommonUtil.strToDateTime(CommonUtil.formatTimeMyfxBook, tempCloseTime);
			} catch (Exception e) {
				getLogger().error("Error Date convert " + e.toString());
			}

			HistoryTrading historyTrading = historyTradingMapper.findHistoryTrading(symbol, openTime, closeTime,
					tradingAccount.getMyfxbookId());
			if (historyTrading == null) {
				if (history.getHistory().get(i).getAction().equalsIgnoreCase("Sell")
						|| history.getHistory().get(i).getAction().equalsIgnoreCase("Buy")) {
					historyTrading = new HistoryTrading();
					historyTrading.setSymbol(history.getHistory().get(i).getSymbol());
					historyTrading.setAction(history.getHistory().get(i).getAction());
					historyTrading.setClosePrice(history.getHistory().get(i).getClosePrice());
					try {
						Date closeTimeConverted = CommonUtil.strToDateTime(CommonUtil.formatTimeMyfxBook,
								history.getHistory().get(i).getCloseTime());
//						getLogger().info("Date " + closeTimeConverted);
						historyTrading.setCloseTime(closeTimeConverted);
						Date openTimeConverted = CommonUtil.strToDateTime(CommonUtil.formatTimeMyfxBook,
								history.getHistory().get(i).getOpenTime());
						historyTrading.setOpenTime(openTimeConverted);
					} catch (Exception e) {
						getLogger().error("Error Date convert " + e.toString());
					}

					historyTrading.setMyfxbookId(tradingAccount.getMyfxbookId());
					historyTrading.setOpenPrice(history.getHistory().get(i).getOpenPrice());
					historyTrading.setProfit(history.getHistory().get(i).getProfit());
					historyTrading.setSizeLot(history.getHistory().get(i).getSizing().getValue());

					// hitung komisi total
					historyTrading.setTotalCommission(
							calculateCommissionService.calculateTotalCommission(historyTrading, tradingAccount));
					// hitung komisi perusahaan/comapny
					historyTrading.setCompanyCommission(calculateCommissionService
							.calculateCompanyCommission(historyTrading.getTotalCommission(), tradingAccount));
					// hitung komisi client
					historyTrading.setClientCommission(calculateCommissionService
							.calculateClientCommission(historyTrading.getTotalCommission(), tradingAccount));

					double result = new Double(df.format(Double.parseDouble(historyTrading.getClientCommission())));
					clientCommissionDouble = clientCommissionDouble + result;
					historyTradingMapper.createHistoryTrading(historyTrading);
				}

			}

		}

		double result = new Double(df.format(clientCommissionDouble));
		userIb.setTotalClientCommission(Double.toString(result));
		getLogger().info("userIb updated " + userIb.getTotalClientCommission());
		userDataMapper.updateCommission(userIb);
		getLogger().info("insertUpdateHistory success with " + tradingAccount.getMyfxbookId());
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
//		getLogger().info("handleLoginResponseMyFxBook with data " + responseData.getMsgToUser());
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

	private MyFxBookLoginResponseVO handleLogoutResponseMyFxBook(ResponseData responseData) {
		getLogger().info("handleLogoutResponseMyFxBook with data " + responseData.getMsgToUser());
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
