package id.co.emobile.samba.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.co.emobile.samba.web.data.UserDataLoginVO;
import id.co.emobile.samba.web.data.WebConstants;
import id.co.emobile.samba.web.data.WebResultVO;
import id.co.emobile.samba.web.data.param.AppsTrxTypeParamVO;
import id.co.emobile.samba.web.entity.AppsTrxType;
import id.co.emobile.samba.web.mapper.AppsTrxTypeMapper;

@Service
public class AppsTrxTypeService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AppsTrxTypeService.class);

	@Autowired
	private AppsTimeService timeService;

	@Autowired
	private AppsTrxTypeMapper appsTrxTypeMapper;

	@Autowired
	private BizMessageService messageService;
	
	@Autowired
	private UserActivityService userActivityService;
	
	public List<AppsTrxType> findAllAppsTrxType()
	{
		try {
			List<AppsTrxType> listAppsTrxType = appsTrxTypeMapper.findAllAppsTrxType();
			return listAppsTrxType;
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			return new ArrayList<AppsTrxType>();
		}
	}
	
	public List<AppsTrxType> findAppsTrxTypeByParam(AppsTrxTypeParamVO paramVO) {
		try {
			List<AppsTrxType> listAppsTrxType = appsTrxTypeMapper.findAppsTrxTypeByParam(paramVO);
			return listAppsTrxType;
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			return new ArrayList<AppsTrxType>();
		}
	}

	public int countAppsTrxTypeByParam(AppsTrxTypeParamVO paramVO) {
		try {
			int count = appsTrxTypeMapper.countAppsTrxTypeByParam(paramVO);
			return count;
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			return 0;
		}
	}

	public AppsTrxType findAppsTrxTypeByTrxName(String trxName) {
		try {
			AppsTrxType appsTrxType = appsTrxTypeMapper.findAppsTrxTypeByTrxName(trxName);
			return appsTrxType;
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			return new AppsTrxType();
		}
	}

	public AppsTrxType findAppsTrxTypeById(int id) {
		try {
			AppsTrxType appsTrxType = appsTrxTypeMapper.findAppsTrxTypeById(id);
			return appsTrxType;
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			return new AppsTrxType();
		}
	}
	
	@Transactional(rollbackFor=Exception.class)
	public WebResultVO insertOrUpdateAppsTrxType(AppsTrxType appsTrxType, UserDataLoginVO loginVO, Locale language) throws SambaWebException {
		try {
			WebResultVO wrv = new WebResultVO();
			appsTrxType.setCreatedBy(loginVO.getId());
			appsTrxType.setCreatedOn(timeService.getCurrentTime());
			appsTrxType.setUpdatedBy(loginVO.getId());
			appsTrxType.setUpdatedOn(timeService.getCurrentTime());
			appsTrxType.setVersionStatus(WebConstants.VERSION_STATUS_CHANGED);
			if(StringUtils.isEmpty(appsTrxType.getTrxName())) {
				LOGGER.warn("transaction name is empty!");
				throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,new String[] {messageService.getMessageFor("l.trxName")});
			}
			if(appsTrxType.getId()==0)
			{
				AppsTrxType appsTrxTypecheck = appsTrxTypeMapper.findAppsTrxTypeByTrxName(appsTrxType.getTrxName());
				if(appsTrxTypecheck!=null)
				{
					LOGGER.warn("Transaction name already used ! Transaction name : {}", appsTrxType.getTrxName());
					throw new SambaWebException(SambaWebException.NE_DUPLICATE_DATA,new String[] {messageService.getMessageFor("l.trxName")});
				}
				int created = appsTrxTypeMapper.createAppsTrxType(appsTrxType);
				LOGGER.debug("Created {} {}", created, appsTrxType);
				
				/** SET TO USER ACTIVITY **/
//				try 
//				{			
//					Collection<String> excludes = new ArrayList<String>();
//					AppsTrxType original = new AppsTrxType();
//					excludes.add("createdOn");
//					excludes.add("createdBy");
//					excludes.add("updatedOn");
//					excludes.add("updatedBy");
//					userActivityService.generateHistoryActivity(excludes, original, appsTrxType, 
//							loginVO.getId(), loginVO.getUserCode(), loginVO.getUserName(), 
//							WebConstants.ACT_MODULE_APPS_TRX_TYPE, WebConstants.ACT_TYPE_INSERT, WebConstants.ACT_TABLE_APPS_TRX_TYPE, 
//							appsTrxType.getId(), loginVO.getIpAddress());
//				} 
//				catch (Exception e) 
//				{
//					LOGGER.warn("Unable to Create History Activity: " + e.getMessage());
//				}
				/** SET TO USER ACTIVITY **/
				
				wrv.setRc(WebConstants.RESULT_SUCCESS);
				wrv.setMessage(messageService.getMessageFor("rm.generalMessage",
						new String[] {messageService.getMessageFor("t.appsMenuTrxType", language),
						messageService.getMessageFor("l.created", language)}, language));
				wrv.setType(WebConstants.TYPE_INSERT);
				return wrv;
			}
			else
			{
				AppsTrxType original = appsTrxTypeMapper.findAppsTrxTypeById(appsTrxType.getId());
				int updated = appsTrxTypeMapper.updateAppsTrxType(appsTrxType);
				LOGGER.debug("Updated {} {}", updated, appsTrxType);
				
				/** SET TO USER ACTIVITY **/
//				try 
//				{			
//					Collection<String> excludes = new ArrayList<String>();
//					excludes.add("createdOn");
//					excludes.add("createdBy");
//					excludes.add("updatedOn");
//					excludes.add("updatedBy");
//					userActivityService.generateHistoryActivity(excludes, original, appsTrxType, 
//							loginVO.getId(), loginVO.getUserCode(), loginVO.getUserName(), 
//							WebConstants.ACT_MODULE_APPS_TRX_TYPE, WebConstants.ACT_TYPE_UPDATE, WebConstants.ACT_TABLE_APPS_TRX_TYPE, 
//							appsTrxType.getId(), loginVO.getIpAddress());
//				} 
//				catch (Exception e) 
//				{
//					LOGGER.warn("Unable to Create History Activity: " + e.getMessage());
//				}
				/** SET TO USER ACTIVITY **/
				
				wrv.setRc(WebConstants.RESULT_SUCCESS);
				wrv.setMessage(messageService.getMessageFor("rm.generalMessage",
						new String[] {messageService.getMessageFor("t.appsMenuTrxType", language),
						messageService.getMessageFor("l.updated", language)}, language));
				wrv.setType(WebConstants.TYPE_UPDATE);
				wrv.setPath(WebConstants.PATH_UPDATE_APPS_MENU_TRX_TYPE);
				return wrv;
			}
		} catch (SambaWebException gwe) {
			LOGGER.warn("Exception when saving apps trx type {} with errorCode", appsTrxType, gwe.getErrorCode());
			throw gwe;
		}
	}

}
