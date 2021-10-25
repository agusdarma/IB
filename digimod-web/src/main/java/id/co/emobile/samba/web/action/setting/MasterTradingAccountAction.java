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
import id.co.emobile.samba.web.data.param.ParamPagingVO;
import id.co.emobile.samba.web.data.param.UserGroupParamVO;
import id.co.emobile.samba.web.entity.UserGroup;
import id.co.emobile.samba.web.helper.WebModules;
import id.co.emobile.samba.web.interceptor.ModuleCheckable;
import id.co.emobile.samba.web.service.SambaWebException;
import id.co.emobile.samba.web.service.UserGroupService;

public class MasterTradingAccountAction extends BaseListAction implements ModuleCheckable {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(MasterTradingAccountAction.class);

	@Autowired
	private UserGroupService userGroupService;
	
	private WebResultVO wrv;

	private String message;
	private String json;
	private List<UserGroup> listUserGroup;
	private int groupId;
	private UserGroup userGroup;
	
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
			userGroup = userGroupService.findUserGroupById(groupId);
			return INPUT;
		} catch (Exception e) {
			handleException(e);
		}
		return SEARCH;
	}

	public String processInput(){
		getLogger().debug("processing: process input() " + userGroup);
		UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);
		Locale language=(Locale) session.get(WEB_LOCALE_KEY);
		try {				
			wrv = userGroupService.insertOrUpdateUserGroup(userGroup, loginVO, language);
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
	
	public List<UserGroup> getListUserGroup() {
		return listUserGroup;
	}

	public void setListUserGroup(List<UserGroup> listUserGroup) {
		this.listUserGroup = listUserGroup;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public UserGroup getUserGroup() {
		if (userGroup == null){
			Object o = session.get(WEB_CONTENT_KEY);
			if (o instanceof UserGroup)
				userGroup = (UserGroup) o;
			if (userGroup == null)
				userGroup = new UserGroup();
		}
		return userGroup;
	}

	public List<UserGroup> getListUserGroupAll() {
		UserGroupParamVO userGroupParamVO = new UserGroupParamVO();
		int size = userGroupService.countUserGroupByParam(userGroupParamVO);
		userGroupParamVO.setRowStart(1);
		userGroupParamVO.setRowEnd(100);		
		userGroupParamVO.setRowPerPage(size);
		userGroupParamVO.setSortVariable("ug.id");
		userGroupParamVO.setSortOrder(WebConstants.SORT_ORDER_ASC);
		
		List<UserGroup> listData = userGroupService.findUserGroupByParam(userGroupParamVO);
		LOG.debug("ListData size: {}", listData.size());
		return listData;
	}

	/**************************************   ESSENTIAL FOR SEARCH  *******************************************/
	private String resultSearchJson;

	public void makeTableContent()
	{
		prepareParamVO(new UserGroupParamVO(), WEB_PARAM_KEY + WebModules.MODULE_APPS_SETTING_USER_GROUP,
				"ug.id", WebConstants.SORT_ORDER_ASC);
		String[] arrayHeader={getText("l.recordNo"), getText("l.groupName"), getText("l.groupDesc")};
		String[] arrayBody={"rowNum", "groupName", "groupDesc"};
		String[] arrayDbVariable={"", "ug.group_name", "ug.group_desc"};
		List<LinkTableVO> listLinkTable=new ArrayList<LinkTableVO>();
		listLinkTable.add(new LinkTableVO("UserGroup!detail.web", "groupName", new String[]{"groupId"}, new String[]{"id"}));

		UserGroupParamVO userGroupParamVO = (UserGroupParamVO) paramVO;
		int totalRow = userGroupService.countUserGroupByParam(userGroupParamVO);
		listUserGroup = userGroupService.findUserGroupByParam(userGroupParamVO);
		Locale language =  (Locale) session.get(WEB_LOCALE_KEY);
		try {
			String bodyContent = objectMapper.writeValueAsString(listUserGroup);
			resultSearchJson=webSearchResultService.composeSearchResult(getText("l.listUserGroup"), arrayHeader, arrayBody,
					arrayDbVariable, bodyContent, getCurrentPage(),
					totalRow, listLinkTable, language, listUserGroup.size(), paramVO);
		} catch (Exception e) {
			LOG.warn("Exception when serializing " + listUserGroup, e);
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

	/**************************************   ESSENTIAL FOR SEARCH  *******************************************/
}
