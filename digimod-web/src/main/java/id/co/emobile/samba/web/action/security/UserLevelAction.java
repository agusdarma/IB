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
import id.co.emobile.samba.web.data.ModulesAccessVO;
import id.co.emobile.samba.web.data.UserDataLoginVO;
import id.co.emobile.samba.web.data.UserLevelMenuVO;
import id.co.emobile.samba.web.data.WebConstants;
import id.co.emobile.samba.web.data.WebResultVO;
import id.co.emobile.samba.web.data.param.ParamPagingVO;
import id.co.emobile.samba.web.data.param.UserLevelParamVO;
import id.co.emobile.samba.web.entity.Lookup;
import id.co.emobile.samba.web.entity.UserLevel;
import id.co.emobile.samba.web.entity.UserMenu;
import id.co.emobile.samba.web.helper.WebModules;
import id.co.emobile.samba.web.interceptor.ModuleCheckable;
import id.co.emobile.samba.web.service.LookupService;
import id.co.emobile.samba.web.service.SambaWebException;
import id.co.emobile.samba.web.service.UserDataService;
import id.co.emobile.samba.web.service.UserLevelService;

public class UserLevelAction extends BaseListAction implements ModuleCheckable {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(UserLevelAction.class);

	@Autowired
	private UserLevelService userLevelService;

	@Autowired
	private UserDataService userDataService;
	
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

	@Override
	protected Logger getLogger() {
		return LOG;
	}
	
	@Override
	public String[] getSessionKeyToHandle() {
		return new String[]{ WEB_CONTENT_KEY };
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
		getLogger().info("Processing - > edit()");
		// called when user needs to edit, to display input form
		try {
			userLevel = userLevelService.findLevelById(levelId);
		} catch (Exception e) {
			handleException(e);
		}
		return INPUT;
	}
	
	public String processInput(){
		getLogger().debug("processing: process input() ");
		UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);	
		Locale language=(Locale) session.get(WEB_LOCALE_KEY);
		try {
			LOG.info("processing user level with param : " +userLevel+ ", module selected"+ modulesSelected + ", language" + language);
			wrv=userLevelService.saveLevelData(userLevel, modulesAccess, loginVO, language);
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
		userLevel=new UserLevel();
		session.remove(WEB_CONTENT_KEY);
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
	
	public List<Lookup> getListLevelType()
	{
		List<Lookup> listLookup = lookupService.findLookupByCat(LookupService.CAT_LEVEL_TYPE);
		return listLookup;
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
