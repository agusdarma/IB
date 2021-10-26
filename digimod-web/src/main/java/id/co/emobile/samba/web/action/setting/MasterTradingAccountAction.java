package id.co.emobile.samba.web.action.setting;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import id.co.emobile.samba.web.action.BaseListAction;
import id.co.emobile.samba.web.data.LinkTableVO;
import id.co.emobile.samba.web.data.UserDataLoginVO;
import id.co.emobile.samba.web.data.WebConstants;
import id.co.emobile.samba.web.data.WebResultVO;
import id.co.emobile.samba.web.data.param.BankParamVO;
import id.co.emobile.samba.web.data.param.MasterTradingAccountParamVO;
import id.co.emobile.samba.web.data.param.ParamPagingVO;
import id.co.emobile.samba.web.data.param.UserGroupParamVO;
import id.co.emobile.samba.web.entity.MasterTradingAccount;
import id.co.emobile.samba.web.entity.UserGroup;
import id.co.emobile.samba.web.helper.WebModules;
import id.co.emobile.samba.web.interceptor.ModuleCheckable;
import id.co.emobile.samba.web.service.MasterTradingAccountService;
import id.co.emobile.samba.web.service.SambaWebException;

public class MasterTradingAccountAction extends BaseListAction implements ModuleCheckable {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(MasterTradingAccountAction.class);

	@Autowired
	private MasterTradingAccountService masterTradingAccountService;
	
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

	public String processSearch() {
		makeTableContent();
		return "searchJson";
	}

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

	public String processInput(){
		getLogger().debug("processing: process input() " + masterTradingAccount);
		UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);
		Locale language=(Locale) session.get(WEB_LOCALE_KEY);
		try {				
			wrv = masterTradingAccountService.insertOrUpdateMasterTradingAccount(masterTradingAccount, loginVO, language);
			if(wrv.getType()==WebConstants.TYPE_UPDATE)
			{
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
		return WebModules.MODULE_MANAGE_TRADING_ACCOUNT;
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
		if (masterTradingAccount == null){
			Object o = session.get(WEB_CONTENT_KEY);
			if (o instanceof MasterTradingAccount)
				masterTradingAccount = (MasterTradingAccount) o;
			if (masterTradingAccount == null)
				masterTradingAccount = new MasterTradingAccount();
		}
		return masterTradingAccount;
	}

	public List<MasterTradingAccount> getListMasterTradingAccountAll() {
		MasterTradingAccountParamVO masterTradingAccountParamVO = new MasterTradingAccountParamVO();
		int size = masterTradingAccountService.countMasterTradingAccountByParam(masterTradingAccountParamVO);
		masterTradingAccountParamVO.setRowStart(1);
		masterTradingAccountParamVO.setRowEnd(100);		
		masterTradingAccountParamVO.setRowPerPage(size);
		masterTradingAccountParamVO.setSortVariable("mta.updated_on");
		masterTradingAccountParamVO.setSortOrder(WebConstants.SORT_ORDER_DESC);
		
		List<MasterTradingAccount> listData = masterTradingAccountService.findMasterTradingAccountByParam(masterTradingAccountParamVO);
		LOG.debug("ListData size: {}", listData.size());
		return listData;
	}

	/**************************************   ESSENTIAL FOR SEARCH  *******************************************/
	private String resultSearchJson;

	public void makeTableContent()
	{
		prepareParamVO(new MasterTradingAccountParamVO(), WEB_PARAM_KEY + WebModules.MODULE_MANAGE_TRADING_ACCOUNT,
				"ug.id", WebConstants.SORT_ORDER_ASC);
		String[] arrayHeader={getText("l.recordNo"), getText("l.groupName"), getText("l.groupDesc")};
		String[] arrayBody={"rowNum", "groupName", "groupDesc"};
		String[] arrayDbVariable={"", "ug.group_name", "ug.group_desc"};
		List<LinkTableVO> listLinkTable=new ArrayList<LinkTableVO>();
		listLinkTable.add(new LinkTableVO("MstTradeAccount!detail.web", "name", new String[]{"masterTradingAccountId"}, new String[]{"id"}));

		MasterTradingAccountParamVO masterTradingAccountParamVO = (MasterTradingAccountParamVO) paramVO;
		int totalRow = masterTradingAccountService.countMasterTradingAccountByParam(masterTradingAccountParamVO);
		listMasterTradingAccounts = masterTradingAccountService.findMasterTradingAccountByParam(masterTradingAccountParamVO);
		Locale language =  (Locale) session.get(WEB_LOCALE_KEY);
		try {
			String bodyContent = objectMapper.writeValueAsString(listMasterTradingAccounts);
			resultSearchJson=webSearchResultService.composeSearchResult(getText("l.listMasterTradingAccount"), arrayHeader, arrayBody,
					arrayDbVariable, bodyContent, getCurrentPage(),
					totalRow, listLinkTable, language, listMasterTradingAccounts.size(), paramVO);
		} catch (Exception e) {
			LOG.warn("Exception when serializing " + listMasterTradingAccounts, e);
		}
		
	}

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

	/**************************************   ESSENTIAL FOR SEARCH  *******************************************/
}
