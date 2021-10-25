package id.co.emobile.samba.web.action.security;

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
import id.co.emobile.samba.web.data.param.ParamPagingVO;
import id.co.emobile.samba.web.data.param.UserDataParamVO;
import id.co.emobile.samba.web.entity.Lookup;
import id.co.emobile.samba.web.entity.UserBranch;
import id.co.emobile.samba.web.entity.UserData;
import id.co.emobile.samba.web.entity.UserGroup;
import id.co.emobile.samba.web.entity.UserLevel;
import id.co.emobile.samba.web.helper.WebModules;
import id.co.emobile.samba.web.interceptor.ModuleCheckable;
import id.co.emobile.samba.web.service.LookupService;
import id.co.emobile.samba.web.service.SambaWebException;
import id.co.emobile.samba.web.service.UserBranchService;
import id.co.emobile.samba.web.service.UserDataService;
import id.co.emobile.samba.web.service.UserGroupService;
import id.co.emobile.samba.web.service.UserLevelService;

public class UserDataAction extends BaseListAction implements ModuleCheckable {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(UserDataAction.class);

	@Autowired
	private UserLevelService userLevelService;

	@Autowired
	private UserDataService userDataService;

	@Autowired
	private LookupService lookupService;

	@Autowired
	private UserGroupService userGroupService;

	@Autowired
	private UserBranchService userBranchService;

	private UserData userData;
	private List<UserData> listUser;
	private WebResultVO wrv;
	private int userId;
	private String confirmPassword;
	private String message;
	private String json;

	@Override
	public String[] getSessionKeyToHandle() {
		return new String[] { WEB_CONTENT_KEY };
	}

	@Override
	protected Logger getLogger() {
		return LOG;
	}

	// SAVE & INITIALIZATION
	@Override
	public String execute() {
		setMessage(getFlashMessage());
		return SEARCH;
	}

	public String processSearch() {
		makeTableContent();
		return "searchJson";
	}

	public String gotoSearch() {
		return SEARCH;
	}

	public String processInput() {
		UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);
		Locale language = (Locale) session.get(WEB_LOCALE_KEY);
		try {
			LOG.info("processing user data with param : " + userData + ", Confirm Password:" + confirmPassword
					+ ", language: " + language);
			wrv = userDataService.insertOrUpdateUserData(userData, loginVO, confirmPassword, language);
			if (wrv.getType() == WebConstants.TYPE_UPDATE) {
				setFlashMessage(wrv.getMessage());
			}
		} catch (SambaWebException mwe) {
			wrv = handleJsonException(mwe);
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

	public String finish() {
		addActionMessage(message);
		return SEARCH;
	}

	public String gotoInput() {
		session.remove(WEB_CONTENT_KEY);
		return INPUT;
	}

	public String detail() {
		getLogger().info("Processing - > edit()");
		// called when user needs to edit, to display input form
		try {
			userData = userDataService.findUserById(userId);
			session.put(WEB_CONTENT_KEY, userData); // userId);
		} catch (Exception e) {
			handleException(e);
		}
		return INPUT;
	}

	// GET DROPDOWNLIST CONTENT
	public List<UserLevel> getListUserLevel() {
		List<UserLevel> listUserLevel = userLevelService.getAllUserLevel();
		return listUserLevel;
	}

	public List<UserGroup> getListUserGroup() {
		List<UserGroup> listUserGroup = new ArrayList<>();
		UserGroup none = new UserGroup();
		none.setId(0);
		none.setGroupName("None");
		none.setGroupName("None");
		listUserGroup.add(none);
		List<UserGroup> list = userGroupService.findUserGroupAll();
		listUserGroup.addAll(list);
		return listUserGroup;
	}

	public List<UserBranch> getListUserBranch() {
		List<UserBranch> listUserBranch = new ArrayList<>();
		List<UserBranch> list = userBranchService.findUserBranchAll();
		listUserBranch.addAll(list);
		return listUserBranch;
	}

	@Override
	public int getMenuId() {
		return WebModules.MODULE_SECURITY_MANAGE_USER;
	}

	public List<Lookup> getListUserStatus() {
		List<Lookup> listLookup = lookupService.findLookupByCat(LookupService.CAT_USER_STATUS);
		return listLookup;
	}

	// SETTER GETTER AREA
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public UserData getUserData() {
		if (userData == null) {
			Object o = session.get(WEB_CONTENT_KEY);
			if (o instanceof UserData)
				userData = (UserData) o;
			if (userData == null)
				userData = new UserData();
		}
		return userData;
	}

	public void setUserData(UserData userData) {
		this.userData = userData;
	}

	public List<UserData> getListUser() {
		if (listUser == null)
			listUser = new ArrayList<UserData>();
		return listUser;
	}

	public void setListUser(List<UserData> listUser) {
		this.listUser = listUser;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public InputStream getWrv() {
		return new ByteArrayInputStream(json.toString().getBytes());
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public List<UserData> getListUserAll() {
		UserDataParamVO userDataParamVO = new UserDataParamVO();
		userDataParamVO.setRowStart(1);
		userDataParamVO.setRowEnd(100);
		userDataParamVO.setRowPerPage(10000);
		userDataParamVO.setSortVariable("ud.id");
		userDataParamVO.setSortOrder(WebConstants.SORT_ORDER_ASC);
		userDataParamVO.setUserStatus(999);

		List<UserData> listData = userDataService.findUserByParam(userDataParamVO);
		LOG.debug("ListData size: {}", listData.size());
		return listData;
	}

	/**************************************
	 * ESSENTIAL FOR SEARCH
	 *******************************************/
	private String resultSearchJson;

	public void makeTableContent() {
		prepareParamVO(new UserDataParamVO(), WEB_PARAM_KEY + WebModules.MODULE_SECURITY_MANAGE_USER, "ud.id",
				WebConstants.SORT_ORDER_ASC);
		String[] arrayHeader = { getText("l.recordNo"), getText("l.userCode"), getText("l.userName"),
				getText("l.userLevel"), getText("l.userStatus") };
		String[] arrayBody = { "rowNum", "userCode", "userName", "userLevelDisplay", "userStatusDisplay" };
		String[] arrayDbVariable = { "", "ud.user_code", "ud.user_name", "ul.level_name", "l.lookup_desc" };
		List<LinkTableVO> listLinkTable = new ArrayList<LinkTableVO>();
		listLinkTable.add(
				new LinkTableVO("UserData!detail.web", "userCode", new String[] { "userId" }, new String[] { "id" }));

		UserDataParamVO userDataParamVO = (UserDataParamVO) paramVO;
		int totalRow = userDataService.countUserByParam(userDataParamVO);
		listUser = userDataService.findUserByParam(userDataParamVO);
		Locale language = (Locale) session.get(WEB_LOCALE_KEY);
		try {
			String bodyContent = objectMapper.writeValueAsString(listUser);
			resultSearchJson = webSearchResultService.composeSearchResult(getText("l.listUser"), arrayHeader, arrayBody,
					arrayDbVariable, bodyContent, getCurrentPage(), totalRow, listLinkTable, language, listUser.size(),
					paramVO);
		} catch (Exception e) {
			LOG.warn("Exception when serializing " + listUser, e);
		}

	}

	@Override
	public ParamPagingVO getParamVO() {
		if (paramVO == null)
			paramVO = new UserDataParamVO();
		return paramVO;
	}

	public InputStream getWsr() {
		return new ByteArrayInputStream(resultSearchJson.toString().getBytes());
	}

	/**************************************
	 * ESSENTIAL FOR SEARCH
	 *******************************************/

}
