package id.co.emobile.samba.web.action.security;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import id.co.emobile.samba.web.action.BaseListAction;
import id.co.emobile.samba.web.data.UserDataLoginVO;
import id.co.emobile.samba.web.data.WebConstants;
import id.co.emobile.samba.web.data.WebResultVO;
import id.co.emobile.samba.web.data.param.BankParamVO;
import id.co.emobile.samba.web.data.param.MasterTradingAccountParamVO;
import id.co.emobile.samba.web.data.param.ParamPagingVO;
import id.co.emobile.samba.web.entity.MasterTradingAccount;
import id.co.emobile.samba.web.entity.UserData;
import id.co.emobile.samba.web.helper.WebModules;
import id.co.emobile.samba.web.interceptor.ModuleCheckable;
import id.co.emobile.samba.web.service.MasterTradingAccountService;
import id.co.emobile.samba.web.service.SambaWebException;
import id.co.emobile.samba.web.service.UserDataService;

public class MemberAccountAction extends BaseListAction implements ModuleCheckable {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(MemberAccountAction.class);

	@Autowired
	private MasterTradingAccountService masterTradingAccountService;

	@Autowired
	private UserDataService userDataService;

	private WebResultVO wrv;

	private String message;
	private String json;
	private List<MasterTradingAccount> listMasterTradingAccounts;
	private int masterTradingAccountId;
	private MasterTradingAccount masterTradingAccount;

	@Override
	protected Logger getLogger() {
		return LOG;
	}

	// INITIATION AND PROCESS AREA
	@Override
	public String execute() {
		setMessage(getFlashMessage());
		return SEARCH;
	}

	public String gotoInput() {
		return INPUT;
	}

	public String gotoSearch() {
		return SEARCH;
	}

//	public String processSearch() {
//		makeTableContent();
//		return "searchJson";
//	}

	public String detail() {
		getLogger().info("Processing - > edit()");
		// called when user needs to edit, to display input form
		try {
			masterTradingAccount = masterTradingAccountService.findMasterTradingAccountById(masterTradingAccountId);
			return INPUT;
		} catch (Exception e) {
			handleException(e);
		}
		return SEARCH;
	}

	public String processInput() {
		getLogger().debug("processing: process input() " + masterTradingAccount);
		UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);
		Locale language = (Locale) session.get(WEB_LOCALE_KEY);
		try {
			wrv = masterTradingAccountService.insertOrUpdateMasterTradingAccount(masterTradingAccount, loginVO,
					language);
			if (wrv.getType() == WebConstants.TYPE_UPDATE) {
				setFlashMessage(wrv.getMessage());
			}
		} catch (SambaWebException gwe) {
			wrv = handleJsonException(gwe);
		} catch (Exception e) {
			wrv = handleJsonException(e);
		}
		try {
			json = objectMapper.writeValueAsString(wrv);
		} catch (Exception e) {
			LOG.warn("Exception in serializing " + wrv, e);
		}
		return "inputJson";
	}

	@Override
	public int getMenuId() {
		return WebModules.MODULE_SECURITY_MANAGE_MEMBER;
	}

	// END SETTER GETTER AREA
	public InputStream getWrv() {
		return new ByteArrayInputStream(json.toString().getBytes());
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public void setWrv(WebResultVO wrv) {
		this.wrv = wrv;
	}

	public MasterTradingAccount getMasterTradingAccount() {
		if (masterTradingAccount == null) {
			Object o = session.get(WEB_CONTENT_KEY);
			if (o instanceof MasterTradingAccount)
				masterTradingAccount = (MasterTradingAccount) o;
			if (masterTradingAccount == null)
				masterTradingAccount = new MasterTradingAccount();
		}
		return masterTradingAccount;
	}

	public List<MasterTradingAccount> getListMasterTradingAccountAll() {
		UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);
		MasterTradingAccountParamVO masterTradingAccountParamVO = new MasterTradingAccountParamVO();
		masterTradingAccountParamVO.setIbUserCode(loginVO.getUserCode());
		int size = masterTradingAccountService.countMasterTradingAccountByParam(masterTradingAccountParamVO);
		masterTradingAccountParamVO.setRowStart(1);
		masterTradingAccountParamVO.setRowEnd(100);
		masterTradingAccountParamVO.setRowPerPage(size);
		masterTradingAccountParamVO.setSortVariable("mta.updated_on");
		masterTradingAccountParamVO.setSortOrder(WebConstants.SORT_ORDER_DESC);

		List<MasterTradingAccount> listData = masterTradingAccountService
				.findMasterTradingAccountByParam(masterTradingAccountParamVO);
		LOG.debug("ListData size: {}", listData.size());
		return listData;
	}

	public List<UserData> getListUserIb() {
		List<UserData> listUserIb = userDataService.getListUserIbActive();
		return listUserIb;
	}

	/**************************************
	 * ESSENTIAL FOR SEARCH
	 *******************************************/
	private String resultSearchJson;

	@Override
	public ParamPagingVO getParamVO() {
		if (paramVO == null)
			paramVO = new BankParamVO();
		return paramVO;
	}

	public InputStream getWsr() {
		return new ByteArrayInputStream(resultSearchJson.toString().getBytes());
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<MasterTradingAccount> getListMasterTradingAccounts() {
		return listMasterTradingAccounts;
	}

	public void setListMasterTradingAccounts(List<MasterTradingAccount> listMasterTradingAccounts) {
		this.listMasterTradingAccounts = listMasterTradingAccounts;
	}

	public int getMasterTradingAccountId() {
		return masterTradingAccountId;
	}

	public void setMasterTradingAccountId(int masterTradingAccountId) {
		this.masterTradingAccountId = masterTradingAccountId;
	}

	/**************************************
	 * ESSENTIAL FOR SEARCH
	 *******************************************/
}
