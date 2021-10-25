//package id.co.emobile.samba.web.action.setting;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import id.co.emobile.samba.web.action.BaseListAction;
//import id.co.emobile.samba.web.data.LinkTableVO;
//import id.co.emobile.samba.web.data.SourceAccountVO;
//import id.co.emobile.samba.web.data.UserDataLoginVO;
//import id.co.emobile.samba.web.data.WebConstants;
//import id.co.emobile.samba.web.data.WebResultVO;
//import id.co.emobile.samba.web.data.param.ParamPagingVO;
//import id.co.emobile.samba.web.data.param.SourceAccountParamVO;
//import id.co.emobile.samba.web.entity.Lookup;
//import id.co.emobile.samba.web.entity.UserGroup;
//import id.co.emobile.samba.web.helper.WebModules;
//import id.co.emobile.samba.web.interceptor.ModuleCheckable;
//import id.co.emobile.samba.web.service.LookupService;
//import id.co.emobile.samba.web.service.SambaWebException;
//import id.co.emobile.samba.web.service.SourceAccountService;
//import id.co.emobile.samba.web.service.UserGroupService;
//
//public class SourceAccountAction extends BaseListAction implements ModuleCheckable {
//	private static final long serialVersionUID = 1L;
//	private static final Logger LOG = LoggerFactory.getLogger(SourceAccountAction.class);
//
//	@Autowired
//	private SourceAccountService sourceAccountService;
//
//	@Autowired
//	private LookupService lookupService;
//	
//	@Autowired
//	private UserGroupService userGroupService;
//	
//	private SourceAccountVO sourceAccount;
//	private List<SourceAccountVO> listSourceAccount;
//	private WebResultVO wrv;
//	private int sracId;
//	private String sracConfirmPin;
//	private String message;
//	private String json;
//
//	@Override
//	public String[] getSessionKeyToHandle() {
//		return new String[]{ WEB_CONTENT_KEY };
//	}
//
//	@Override
//	protected Logger getLogger() {
//		return LOG;
//	}
//
//	//SAVE & INITIALIZATION
//	@Override
//	public String execute() {
//		setMessage(getFlashMessage());
//		return SEARCH;
//	}
//
//	public String processSearch() {
//		makeTableContent();
//		return "searchJson";
//	}
//
//	public String gotoSearch(){
//		return SEARCH;
//	}
//
//	public String processInput(){
//		UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);
//		Locale language=(Locale) session.get(WEB_LOCALE_KEY);
//		try {
//			LOG.info("Processing: {}", sourceAccount);
//			if (sracConfirmPin == null || !sracConfirmPin.equals(sourceAccount.getSracPin()) ) {
//				LOG.warn("Different confirm PIN {} with PIN {}", sracConfirmPin, sourceAccount.getSracPin());
//				throw new SambaWebException(SambaWebException.NE_PASSWORD_DIFFERENT);
//			}
//			wrv = sourceAccountService.insertOrUpdateSourceAccount(sourceAccount, loginVO, language);
//			if(wrv.getType()==WebConstants.TYPE_UPDATE)
//			{
//				setFlashMessage(wrv.getMessage());
//			}
//			sourceAccountService.reload(); // reload data that has been saved / updated
//		} catch (SambaWebException mwe) {
//			wrv = handleJsonException(mwe);
//		} catch (Exception e) {
//			wrv = handleJsonException(e);
//		}
//		try {
//			json = objectMapper.writeValueAsString(wrv);
//		} catch (Exception e) {
//			LOG.warn("Exception in serializing " + wrv, e);
//		}
//		return "inputJson";
//	}
//
//	public String finish(){
//		addActionMessage(message);
//		return SEARCH;
//	}
//
//	public String gotoInput() {
//		session.remove(WEB_CONTENT_KEY);
//		return INPUT;
//	}
//
//	public String detail() {
//		getLogger().info("detail with id: {}", sracId);
//		// called when user needs to edit, to display input form
//		try {
//			sourceAccount = sourceAccountService.findSourceAccountById(sracId);
//			if (sourceAccount != null)  {
//				sracConfirmPin = sourceAccount.getSracPin();
//				session.put(WEB_CONTENT_KEY, sourceAccount);  // userId);
//			}
//		} catch (Exception e) {
//			handleException(e);
//		}
//		return INPUT;
//	}
//
//	@Override
//	public int getMenuId() {
//		return WebModules.MODULE_APPS_SETTING_SOURCE_ACCOUNT;
//	}
//	
//	public List<Lookup> getListStatus()
//	{
//		List<Lookup> listLookup = lookupService.findLookupByCat(LookupService.CAT_USER_STATUS);
//		return listLookup;
//	}
//
//	public List<UserGroup> getListUserGroup() {
//		List<UserGroup> listUserGroup = new ArrayList<>();
////		UserGroup none = new UserGroup();
////		none.setId(0);
////		none.setGroupName("None");
////		none.setGroupName("None");
////		listUserGroup.add(none);
//		List<UserGroup> list = userGroupService.findUserGroupAll();
//		listUserGroup.addAll(list);
//		return listUserGroup;
//	}
//
//	// SETTER GETTER AREA
//	public int getSracId() {
//		return sracId;
//	}
//	public void setSracId(int sracId) {
//		this.sracId = sracId;
//	}
//
//	public SourceAccountVO getSourceAccount() {
//		if(sourceAccount == null) {
//			Object o = session.get(WEB_CONTENT_KEY);
//			if (o instanceof SourceAccountVO)
//				sourceAccount = (SourceAccountVO) o;
//			if (sourceAccount == null)
//				sourceAccount = new SourceAccountVO();
//		}
//		return sourceAccount;
//	}
//
//	public List<SourceAccountVO> getListSourceAccount() {
//		if(listSourceAccount==null)
//			listSourceAccount = new ArrayList<SourceAccountVO>();
//		return listSourceAccount;
//	}
//
//
//	public String getMessage() {
//		return message;
//	}
//
//	public void setMessage(String message) {
//		this.message = message;
//	}
//
//	public InputStream getWrv() {
//		return new ByteArrayInputStream(json.toString().getBytes());
//	}
//
//	public String getJson() {
//		return json;
//	}
//
//	public void setJson(String json) {
//		this.json = json;
//	}
//	
//	public List<SourceAccountVO> getListSracAll() {
//		SourceAccountParamVO sracParamVO = new SourceAccountParamVO();
//		return sourceAccountService.findSourceAccountByParam(sracParamVO);
//	}
//
//	/**************************************   ESSENTIAL FOR SEARCH  *******************************************/
//	private String resultSearchJson;
//
//	public void makeTableContent()
//	{
//		prepareParamVO(new SourceAccountParamVO(), WEB_PARAM_KEY + WebModules.MODULE_APPS_SETTING_SOURCE_ACCOUNT,
//				"ud.id", WebConstants.SORT_ORDER_ASC);
//		String[] arrayHeader={getText("l.recordNo"), getText("l.phoneNo"), getText("l.sracNumber"), getText("l.sracName"), getText("l.status")};
//		String[] arrayBody={"rowNum", "phoneNo", "sracNumber", "sracName", "sracStatusDisplay"};
//		String[] arrayDbVariable={"", "phoneNo", "sracNumber", "sracName", "sracStatus"};
//		List<LinkTableVO> listLinkTable=new ArrayList<LinkTableVO>();
//		listLinkTable.add(new LinkTableVO("SourceAccount!detail.web", "phoneNo", new String[]{"sracId"}, new String[]{"id"}));
//
//		SourceAccountParamVO sracParamVO = (SourceAccountParamVO) paramVO;
//		int totalRow = sourceAccountService.countSourceAccountByParam(sracParamVO);
//		listSourceAccount = sourceAccountService.findSourceAccountByParam(sracParamVO);
//		Locale language =  (Locale) session.get(WEB_LOCALE_KEY);
//		try {
//			String bodyContent = objectMapper.writeValueAsString(listSourceAccount);
//			resultSearchJson = webSearchResultService.composeSearchResult(getText("l.listSourceAccount"), arrayHeader, arrayBody,
//					arrayDbVariable, bodyContent, getCurrentPage(),
//					totalRow, listLinkTable, language, listSourceAccount.size(), paramVO);
//		} catch (Exception e) {
//			LOG.warn("Exception when serializing " + listSourceAccount, e);
//		}
//		
//	}
//
//	@Override
//	public ParamPagingVO getParamVO() {
//		if (paramVO == null)
//			paramVO = new SourceAccountParamVO();
//		return paramVO;
//	}
//
//	public InputStream getWsr() {
//		return new ByteArrayInputStream(resultSearchJson.toString().getBytes());
//	}
//
//	public String getSracConfirmPin() {
//		return sracConfirmPin;
//	}
//
//	public void setSracConfirmPin(String sracConfirmPin) {
//		this.sracConfirmPin = sracConfirmPin;
//	}
//
//	/**************************************   ESSENTIAL FOR SEARCH  *******************************************/
//
//}
