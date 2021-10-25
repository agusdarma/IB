//package id.co.emobile.samba.web.service;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import id.co.emobile.samba.web.data.SourceAccountVO;
//import id.co.emobile.samba.web.data.UserDataLoginVO;
//import id.co.emobile.samba.web.data.WebConstants;
//import id.co.emobile.samba.web.data.WebResultVO;
//import id.co.emobile.samba.web.data.param.SourceAccountParamVO;
//import id.co.emobile.samba.web.entity.Lookup;
//import id.co.emobile.samba.web.entity.SourceAccount;
//import id.co.emobile.samba.web.entity.UserGroup;
//import id.co.emobile.samba.web.mapper.SourceAccountMapper;
//import id.co.emobile.samba.web.utils.CipherUtils;
//import id.co.emobile.samba.web.utils.CommonUtil;
//
//public class SourceAccountService {
//	private static final Logger LOG = LoggerFactory.getLogger(SourceAccountService.class);
//
//	private ObjectMapper objectMapper = new ObjectMapper();
//	private List<SourceAccountVO> listAccount = new ArrayList<>();
//	
//	@Autowired
//	private SourceAccountMapper sourceAccountMapper;
//	
//	@Autowired
//	private LookupService lookupService;
//	
//	@Autowired
//	private AppsTimeService timeService;
//	
//	@Autowired
//	private BizMessageService messageService;
//	
//	@Autowired
//	private UserActivityService userActivityService;
//	
//	@Autowired
//	private UserGroupService userGroupService;
//	
//	@Autowired
//	private SmsSenderService smsSenderService;
//	
//	public void init() {
//		reload();
//		LOG.debug("SourceAccountService started with {} listAccount", listAccount.size());
//	}
//	
//	public void shutdown() {
//		LOG.debug("SourceAccountService stopped");
//	}
//	
//	public void reload() {
//		listAccount.clear();
//		List<SourceAccount> list = sourceAccountMapper.findSourceAccountAll();
//		for (SourceAccount acc: list) {
//			String data = acc.getSracData();
//			try {
//				String key = acc.getSracKey() + "-" + acc.getPhoneNo() + "-" + acc.getCreatedBy();
//				String plain = CipherUtils.decryptAESPKCS7(data, key);
//				SourceAccountVO vo = objectMapper.readValue(plain, SourceAccountVO.class);
//				vo.setId(acc.getId());
//				Lookup l = lookupService.findLookupByCatValue(LookupService.CAT_USER_STATUS, "" + vo.getSracStatus());
//				vo.setSracStatusDisplay(l.getLookupDesc());
//				UserGroup userGroup = userGroupService.findUserGroupById(vo.getGroupId());
//				if (userGroup == null)
//					vo.setGroupNameDisplay("");
//				else
//					vo.setGroupNameDisplay(userGroup.getGroupName());
////				LOG.debug("Source Account is loaded with id {}", vo.getId());
//				listAccount.add(vo);
//			} catch (Exception e) {
//				LOG.warn("Exception when reading " + data, e);
//			}
//		}
//		LOG.debug("list SourceAccount is reloaded with {} data", listAccount.size());
//	}
//
//	@Transactional(rollbackFor=Exception.class)
//	public WebResultVO insertOrUpdateSourceAccount(SourceAccountVO sourceAccount, UserDataLoginVO loginVO,
//			Locale language) throws SambaWebException {
//		LOG.debug("insertOrUpdateSourceAccount {}", sourceAccount);
//		try {
//			// validate data
//			if (StringUtils.isEmpty(sourceAccount.getPhoneNo()) ) {
//				LOG.warn("Missing phoneNo");
//				throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,new String[] {messageService.getMessageFor("l.phoneNo", language)});
//			}
//			if (StringUtils.isEmpty(sourceAccount.getSracName()) ) {
//				LOG.warn("Missing sracName");
//				throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,new String[] {messageService.getMessageFor("l.sracName", language)});
//			}
//			if (StringUtils.isEmpty(sourceAccount.getSracNumber()) ) {
//				LOG.warn("Missing sracNumber");
//				throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,new String[] {messageService.getMessageFor("l.sracNumber", language)});
//			}
//			if (StringUtils.isEmpty(sourceAccount.getSracPin()) ) {
//				LOG.warn("Missing sracPin");
//				throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,new String[] {messageService.getMessageFor("l.sracPin", language)});
//			}
//			String phoneNo = CommonUtil.formatPhoneLocal(sourceAccount.getPhoneNo());
//			sourceAccount.setPhoneNo(phoneNo);
//			
//			WebResultVO wrv = new WebResultVO();
//			Date now = timeService.getCurrentTime();
//			sourceAccount.setUpdatedBy(loginVO.getId());
//			sourceAccount.setUpdatedOn(now);
//			if (sourceAccount.getId() == 0 && StringUtils.isEmpty(sourceAccount.getSracKey())) {
//				// new source account
//				String sracKey = CommonUtil.generateRandomAlphanum(32);
//				sourceAccount.setSracKey(sracKey);
//				sourceAccount.setCreatedBy(loginVO.getId());
//				sourceAccount.setCreatedOn(now);
//			}  else if (StringUtils.isEmpty(sourceAccount.getSracKey())) {
//				LOG.warn("Missing sracKey");
//				throw new SambaWebException(SambaWebException.NE_DATA_NOT_FOUND );
//			}
//			SourceAccount acc = new SourceAccount();
//			acc.setId(sourceAccount.getId());
//			acc.setPhoneNo(sourceAccount.getPhoneNo());
//			String key = sourceAccount.getSracKey() + "-" + sourceAccount.getPhoneNo() + "-" + sourceAccount.getCreatedBy();
//			String content = objectMapper.writeValueAsString(sourceAccount);
//			String enc = CipherUtils.encryptAESPKCS7(content, key);
//			acc.setSracData(enc);
//			acc.setSracKey(sourceAccount.getSracKey());
//			acc.setCreatedBy(sourceAccount.getCreatedBy());
//			acc.setCreatedOn(sourceAccount.getCreatedOn());
//			acc.setUpdatedBy(sourceAccount.getUpdatedBy());
//			acc.setUpdatedOn(sourceAccount.getUpdatedOn());
////			String actType; 
//			if (acc.getId() == 0) {
//				int created = sourceAccountMapper.createSourceAccount(acc);
//				LOG.debug("Created {} {}", created, acc);
////				actType = WebConstants.ACT_TYPE_INSERT;
//				
//				String desc = "Create Source Account " + sourceAccount.getSracName() + ":" + sourceAccount.getPhoneNo();
//				userActivityService.createUserActivityCreateData(loginVO, 
//						UserActivityService.MODULE_MANAGE_SOURCE_ACC, desc, UserActivityService.TABLE_SOURCE_ACCOUNT, 
//						acc.getId(), sourceAccount);
//				
//				wrv.setRc(WebConstants.RESULT_SUCCESS);
//				wrv.setMessage(messageService.getMessageFor("rm.generalMessage",
//						new String[] {messageService.getMessageFor("l.sourceAccount", language),
//						messageService.getMessageFor("l.created", language)}, language));
//				wrv.setType(WebConstants.TYPE_INSERT);
//			} else {
//				SourceAccountVO original = findSourceAccountById(acc.getId());
//				
//				int updated = sourceAccountMapper.updateSourceAccount(acc);
//				LOG.debug("Updated {} {}", updated, acc);
////				actType = WebConstants.ACT_TYPE_UPDATE;
//				
//				String desc = "Update Source Account " + sourceAccount.getSracName() + ":" + sourceAccount.getPhoneNo();
//				userActivityService.createUserActivityUpdateData(loginVO, 
//						UserActivityService.MODULE_MANAGE_SOURCE_ACC, desc, UserActivityService.TABLE_SOURCE_ACCOUNT, 
//						acc.getId(), sourceAccount, original);
//				
//				wrv.setRc(WebConstants.RESULT_SUCCESS);
//				wrv.setMessage(messageService.getMessageFor("rm.generalMessage",
//						new String[] {messageService.getMessageFor("l.sourceAccount", language),
//						messageService.getMessageFor("l.updated", language)}, language));
//				wrv.setType(WebConstants.TYPE_UPDATE);
//				wrv.setPath(WebConstants.PATH_UPDATE_SOURCE_ACCOUNT);
//			}
////			try 
////			{			
////				Collection<String> excludes = new ArrayList<String>();
////				SourceAccount original = sourceAccountMapper.findSourceAccountById(acc.getId());
////				excludes.add("sracData"); // no need to compare, too much data
////				excludes.add("createdOn");
////				excludes.add("createdBy");
////				excludes.add("updatedOn");
////				excludes.add("updatedBy");
////				userActivityService.generateHistoryActivity(excludes, original, acc, 
////						loginVO.getId(), loginVO.getUserCode(), loginVO.getUserName(),
////						WebConstants.ACT_MODULE_SOURCE_ACCOUNT, 
////						actType, WebConstants.ACT_TABLE_SOURCE_ACCOUNT, 
////						loginVO.getId(), loginVO.getIpAddress());
////			} 
////			catch (Exception e) 
////			{
////				LOG.warn("Unable to Create History Activity: " + e.getMessage());
////			}
//
//			return wrv;
//		} catch (SambaWebException swe) {
//			throw swe;
//		} catch (Exception e) {
//			LOG.warn("Exception insertOrUpdateSourceAccount for " + sourceAccount, e);
//			throw new SambaWebException(SambaWebException.NE_UNKNOWN_ERROR);
//		}
//	}
//	
//	public List<SourceAccountVO> getListSourceAccountByGroup(int groupId) {
//		return getListSourceAccountByGroup(groupId, true);
//	}
//	
//	public List<SourceAccountVO> getListSourceAccountByGroup(int groupId, boolean checkBalance) {
//		List<SourceAccountVO> listSelected = new ArrayList<>();
//		for (SourceAccountVO vo: listAccount) {
////			LOG.debug("*******Checking groupId {} with vo {}", groupId, vo);
//			if (vo.getSracStatus() == WebConstants.STATUS_ACTIVE) {
//				if (vo.getGroupId() == groupId) {
//					if (checkBalance)
//						smsSenderService.checkBalanceForAccount(vo);
//					listSelected.add(vo);
//				}
//			} // active
//		} // looping account
//		LOG.debug("******List SourceAccountVO {}", listSelected);
//		return listSelected;
//	}
//	
//	public SourceAccountVO findSourceAccountById (int sracId) {
//		for (SourceAccountVO vo: listAccount) {
//			if (vo.getId() == sracId) return vo;
//		}
//		return null;
//	}
//	
//	public String findChallengePinForAccount(int sracId, int idx1, int idx2) {
//		LOG.debug("findChallengePinForAccount for {}, with idx {} {}", sracId, idx1, idx2);
//		SourceAccountVO vo = findSourceAccountById(sracId);
//		if (vo == null) {
//			LOG.warn("No SourceAccount is defined for id {}", sracId);
//			return null;
//		}
//		if (vo.getSracStatus() != WebConstants.STATUS_ACTIVE) {
//			LOG.warn("SourceAccount {} is not active", vo);
//			return null;	
//		}
//		String pin = vo.getSracPin();
//		if (idx1 > pin.length() || idx2 > pin.length()) {
//			LOG.warn("Invalid index {} {} for Length Pin {}", idx1, idx2, pin.length());
//			return null;
//		}
//		String pin1 = pin.substring(idx1 - 1, idx1);
//		String pin2 = pin.substring(idx2 - 1, idx2);
//		return pin1 + pin2;
//	}
//	
//	public List<SourceAccountVO> findSourceAccountByParam(SourceAccountParamVO paramVO) {
//		List<SourceAccountVO> listSelected = new ArrayList<>();
//		int rowNum = 1;
//		for (SourceAccountVO vo: listAccount) {
//			boolean found = true;
//			if (StringUtils.isNotEmpty(paramVO.getPhoneNo()) && !vo.getPhoneNo().contains(paramVO.getPhoneNo()))
//				found = false;
//			if (StringUtils.isNotEmpty(paramVO.getSracName()) && !vo.getSracName().contains(paramVO.getSracName()))
//				found = false;
//			if (StringUtils.isNotEmpty(paramVO.getSracNumber()) && !vo.getSracName().contains(paramVO.getSracNumber()))
//				found = false;
//			if (found) {
//				vo.setRowNum(rowNum++);
//				listSelected.add(vo);
//			}
//		}
//		return listSelected;
//	}
//
//	public int countSourceAccountByParam(SourceAccountParamVO paramVO) {
//		List<SourceAccountVO> listSelected = findSourceAccountByParam(paramVO);
//		return listSelected.size();
//	}
//}
