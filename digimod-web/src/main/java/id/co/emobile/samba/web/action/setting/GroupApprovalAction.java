package id.co.emobile.samba.web.action.setting;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import id.co.emobile.samba.web.action.BaseListAction;
import id.co.emobile.samba.web.data.LinkTableVO;
import id.co.emobile.samba.web.data.ModulesAccessVO;
import id.co.emobile.samba.web.data.UserDataLoginVO;
import id.co.emobile.samba.web.data.UserLevelMenuVO;
import id.co.emobile.samba.web.data.WebConstants;
import id.co.emobile.samba.web.data.WebResultVO;
import id.co.emobile.samba.web.data.param.ParamPagingVO;
import id.co.emobile.samba.web.data.param.UserLevelParamVO;
import id.co.emobile.samba.web.entity.Lookup;
import id.co.emobile.samba.web.entity.UserData;
import id.co.emobile.samba.web.entity.UserGroup;
import id.co.emobile.samba.web.entity.UserLevel;
import id.co.emobile.samba.web.entity.UserMenu;
import id.co.emobile.samba.web.helper.WebModules;
import id.co.emobile.samba.web.interceptor.ModuleCheckable;
import id.co.emobile.samba.web.service.LookupService;
import id.co.emobile.samba.web.service.SambaWebException;
import id.co.emobile.samba.web.service.UserDataService;
import id.co.emobile.samba.web.service.UserGroupService;
import id.co.emobile.samba.web.service.UserLevelService;

public class GroupApprovalAction extends BaseListAction implements ModuleCheckable {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(GroupApprovalAction.class);

	@Autowired
	private UserLevelService userLevelService;

	@Autowired
	private UserDataService userDataService;
	
	@Autowired
	private UserGroupService userGroupService;
	
	@Autowired
	private LookupService lookupService;
	
	private WebResultVO wrv;
	
	private UserLevel userLevel;
	private List<UserLevel> listUserLevel;
	private List<Integer> modulesSelected;
	private List<ModulesAccessVO> modulesAccess;
	private String message;
	private String json;
	
	private String levelName;
	private int levelId;
	
	private List<Integer> selectedGroup;
	private int userId;
	private UserData userApproval;

	@Override
	protected Logger getLogger() {
		return LOG;
	}
	
	@Override
	public String[] getSessionKeyToHandle() {
		return new String[]{ WEB_CONTENT_KEY, WEB_CONTENT_KEY_2 };
	}

	// INITIATION AND PROCESS AREA
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
		getLogger().info("Processing - > edit(): {}", userId);
		// called when user needs to edit, to display input form
		try {
			userApproval = userDataService.findUserById(userId);
			if (userApproval == null) {
				getLogger().warn("Unable to find user approval with id: " + userId);
				throw new SambaWebException(SambaWebException.NE_DATA_NOT_FOUND);
			}
			UserLevel level = userLevelService.findLevelById(userApproval.getLevelId());
			if (level == null || level.getLevelType() != WebConstants.LEVEL_TYPE_APPROVAL) {
				getLogger().warn("User {} is not approval type", userApproval);
				throw new SambaWebException(SambaWebException.NE_DATA_NOT_FOUND);
			}
			List<UserGroup> listGroup = userGroupService.findUserGroupForApproval(userApproval.getId());
			session.put(WEB_CONTENT_KEY, userApproval);
			session.put(WEB_CONTENT_KEY_2, listGroup);
			return INPUT;
		} catch (SambaWebException swe) {
			handleException(swe);
		} catch (Exception e) {
			handleException(e);
		}
		return execute();
	}
	
	public String processInput(){
		Integer[] selected;
		if (selectedGroup == null) selected = new Integer[0];
		else selected = selectedGroup.toArray(new Integer[0]);
		getLogger().debug("processing: process input() {}, Selected: {}", getUserApproval(), Arrays.toString(selected));
		UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);	
		Locale language=(Locale) session.get(WEB_LOCALE_KEY);
		try {
			wrv = userGroupService.saveUserGroupForApproval(getUserApproval(), selectedGroup, loginVO, language);
			List<UserGroup> listGroup = userGroupService.findUserGroupForApproval(userApproval.getId());
			session.put(WEB_CONTENT_KEY_2, listGroup);
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
	
	public String processDelete(){
		getLogger().debug("processing: processDelete() ");
		int countUsedLevel = userDataService.countLevelUsedByName(userLevel.getLevelId());
		if(countUsedLevel==0){
			userLevelService.deleteByLevelId(userLevel.getLevelId());
			return SUCCESS;
		} else {
			addActionError(getText("err.userleveleditor.deletereject"));
			return LIST;
		}
	}
	
	public String finish(){
		this.addActionMessage(getText("pages.userleveleditor.successdelete"));
//		userLevel=new UserLevel();
		session.remove(WEB_CONTENT_KEY);
		session.remove(WEB_CONTENT_KEY_2);
		return SEARCH;
	}

	// END INITIATION AND PROCESS AREA

// VALIDATION AREA
	public boolean validateList(){
		LOG.debug("validate properties !");
		if(modulesSelected==null) {
			addActionError(getText("err.userleveleditor.nomenuselected"));
			return false;
		}
		return true;
	}
	
	public boolean validateInput(){
		LOG.debug("validate properties New User Level!");
		if(("").equals(userLevel.getLevelName())) {
			addActionError(getText("err.userleveleditor.misslevelname"));
			return false;
		}
		int countUser=userLevelService.countUserLevelValidate(userLevel.getLevelName());
		if(countUser!=0) {
			addActionError(getText("err.userleveleditor.duplicate"));
			return false;
		}
		if(modulesSelected==null) {
			addActionError(getText("err.userleveleditor.nomenuselected"));
			return false;
		}
		return true;
	}
	
// END OF VALIDATION AREA

	// SPAWN LEVEL AND CHECKBOX
	public boolean isModulesSelected(int moduleId) {
		if (modulesAccess != null && modulesAccess.size() > 0) {
			for (ModulesAccessVO i : modulesAccess)				
				if (i.getModulesSelected() == moduleId)
					return true;
		} else {
			userLevel = getUserLevel();
			if (userLevel == null)
				return false;
			for (UserMenu m : userLevel.getListMenu()) {
				if (m.getMenuId() == moduleId)
					return true;
			}
		}
		return false;
	}

	public boolean isFullAccess(int moduleId) {
		if (modulesAccess != null && modulesAccess.size() > 0) {
			for (ModulesAccessVO i : modulesAccess)				
				if (i.getModulesSelected() == moduleId)
				{
					if(i.getAccessLevel()==1)
						return true;
					else
						return false;
				}
		} else {
			userLevel = getUserLevel();
			if (userLevel == null)
				return false;
			for (UserMenu m : userLevel.getListMenu()) {
				if (m.getMenuId() == moduleId)
				{
					if(m.getAccessLevel()==1)
						return true;
					else
						return false;
				}
			}
		}
		return false;
	}
	public List<UserLevelMenuVO> getListModule() {
		return userLevelService.findModules();
	}
	// END SPAWN LEVEL AND CHECK BOX

	@Override
	public int getMenuId() {
		return WebModules.MODULE_SECURITY_MANAGE_LEVEL;
	}

	public UserLevel getUserLevel() {
		if (userLevel == null) {
			Object obj = session.get(WEB_CONTENT_KEY);
			if (obj != null && obj instanceof UserLevel)
				userLevel = (UserLevel) obj;
			else
				userLevel = new UserLevel();
		}
		return userLevel;
	}
	public void setUserLevel(UserLevel userLevel) {
		this.userLevel = userLevel;
	}
	public List<UserLevel> getListUserLevel() {
		return listUserLevel;
	}
	public void setListUserLevel(List<UserLevel> listUserLevel) {
		this.listUserLevel = listUserLevel;
	}
	public List<Integer> getModulesSelected() {
		return modulesSelected;
	}

	public void setModulesSelected(List<Integer> modulesSelected) {
		this.modulesSelected = modulesSelected;
	}

	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	public int getLevelId() {
		return levelId;
	}
	public void setLevelId(int levelId) {
		this.levelId = levelId;
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
	
	public List<ModulesAccessVO> getModulesAccess() {
		return modulesAccess;
	}

	public void setModulesAccess(List<ModulesAccessVO> modulesAccess) {
		this.modulesAccess = modulesAccess;
	}

	public List<UserLevel> getListUserAll() {
		UserLevelParamVO userLevelParamVO = new UserLevelParamVO();
		userLevelParamVO.setRowStart(1);
		userLevelParamVO.setRowEnd(100);
		userLevelParamVO.setRowPerPage(100);
		userLevelParamVO.setSortVariable("l.level_id");
		userLevelParamVO.setSortOrder(WebConstants.SORT_ORDER_ASC);
		
		listUserLevel = userLevelService.findUserLevelByParam(userLevelParamVO);
		LOG.debug("ListUserLevel size: {}", listUserLevel.size());
		return listUserLevel;
	}	
	
	public List<UserData> getListUserApproval() {
		return userDataService.findListUserApproval();
	}
	public List<UserGroup> getListUserGroup() {
		return userGroupService.findUserGroupAll();
	}
	
	public boolean isApprovalInGroup(int groupId) {
		Object o = session.get(WEB_CONTENT_KEY_2);
		if (o instanceof List) {
			@SuppressWarnings("unchecked")
			List<UserGroup> listGroup = (List<UserGroup>) o;
			boolean found = false;
			for (UserGroup group: listGroup) {
				if (group.getId() == groupId) {
					found = true;
					break;
				}
			}
			return found;
		}
		return false;
	}
	
	public List<Lookup> getListLevelType()
	{
		List<Lookup> listLookup = lookupService.findLookupByCat(LookupService.CAT_LEVEL_TYPE);
		return listLookup;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public UserData getUserApproval() {
		if (userApproval == null) {
			Object o = session.get(WEB_CONTENT_KEY);
			if (o instanceof UserData) {
				userApproval = (UserData) o;
			}
			if (userApproval == null)
				userApproval = new UserData();
		}
		return userApproval;
	}
	
	// for holding selected group input
	public List<Integer> getSelectedGroup() {
		return selectedGroup;
	}
	public void setSelectedGroup(List<Integer> selectedGroup) {
		this.selectedGroup = selectedGroup;
	}
	
	/**************************************   ESSENTIAL FOR SEARCH  *******************************************/	
	private String resultSearchJson;
	
	public void makeTableContent()
	{
		prepareParamVO(new UserLevelParamVO(), WEB_PARAM_KEY + WebModules.MODULE_SECURITY_MANAGE_LEVEL, 
				"l.level_id", WebConstants.SORT_ORDER_ASC);		
		String[] arrayHeader={getText("l.recordNo"), getText("l.levelName"), getText("l.levelDescription")};
		String[] arrayBody={"rowNum", "levelName", "levelDesc"};
		String[] arrayDbVariable={"", "l.level_name", "l.level_desc"};
		List<LinkTableVO> listLinkTable=new ArrayList<LinkTableVO>();
		listLinkTable.add(new LinkTableVO("UserLevel!detail.web", "levelName", new String[]{"levelId"}, new String[]{"levelId"}));
		
		UserLevelParamVO userLevelParamVO = (UserLevelParamVO) paramVO;
		int totalRow = userLevelService.countUserLevelByLevelParam(userLevelParamVO);
		listUserLevel = userLevelService.findUserLevelByParam(userLevelParamVO);
		Locale language =  (Locale) session.get(WEB_LOCALE_KEY);
		try {
			String bodyContent = objectMapper.writeValueAsString(listUserLevel);
			resultSearchJson=webSearchResultService.composeSearchResult(getText("l.listUserLevel"), arrayHeader, arrayBody, 
					arrayDbVariable, bodyContent, getCurrentPage(), 
					totalRow, listLinkTable, language, listUserLevel.size(), paramVO);
		} catch (Exception e) {
			LOG.warn("Exception when serializing " + listUserLevel, e);
		}
		
	}
	
	public ParamPagingVO getParamVO() {
		if (paramVO == null)
			paramVO = new UserLevelParamVO();
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
