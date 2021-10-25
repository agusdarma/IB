package id.co.emobile.samba.web.service;

import id.co.emobile.samba.web.data.UserDataLoginVO;
import id.co.emobile.samba.web.data.WebConstants;
import id.co.emobile.samba.web.data.WebResultVO;
import id.co.emobile.samba.web.data.param.LookupDataParamVO;
import id.co.emobile.samba.web.entity.Lookup;
import id.co.emobile.samba.web.mapper.LookupMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LookupService {
	private static final Logger LOGGER = LoggerFactory.getLogger(LookupService.class);

	public static final int CAT_LOOKUP_LIBRARY					= 0;
	public static final int CAT_USER_STATUS						= 1;
	public static final int CAT_DIST_STATUS						= 2; // not displayed
	public static final int CAT_LEVEL_TYPE						= 3;
	public static final int CAT_PROCESS_STATUS					= 4;
	public static final int CAT_BANK_CODE						= 5;
	public static final int CAT_JENIS_PAJAK						= 6;
	public static final int CAT_JENIS_SETORAN					= 7;
	
//	public static final int CAT_BANK_SHOW_ON					= 2;
//	public static final int CAT_BANK_SHOW_DEF_ACC				= 3;
//	public static final int CAT_APPS_WORDING_CATEGORY			= 4;
//	public static final int CAT_APPS_TRX_GROUP					= 5;
//	public static final int CAT_VALIDATION_TYPE					= 6;
//	public static final int CAT_INPUT_TYPE						= 7;
//	public static final int CAT_ADS_LOCATION					= 8;
//	public static final int CAT_ADS_BANK_MODE					= 9;
//	public static final int CAT_PARAM_TYPE						= 10;	
//	public static final int CAT_ACTIVITY_TYPE					= 11;	
//	public static final int CAT_ACTIVITY_TYPE_APPLET			= 12;	
	
	@Autowired
	private LookupMapper lookupMapper;

	@Autowired
	private BizMessageService messageService;

	private List<Lookup> listLookup;

	private synchronized void loadLookup() {
		listLookup = lookupMapper.findLookupAll();
		if (listLookup == null)
			listLookup = new ArrayList<Lookup>();
		if (LOGGER.isInfoEnabled()) {
			StringBuilder sb = new StringBuilder(listLookup.size());
			sb.append("Lookup has been loaded [");
			int i = 0;
			for (Lookup lookup: listLookup) {
				sb.append(lookup.getLookupCat()).append(":");
				sb.append(lookup.getLookupValue()).append("=");
				sb.append(lookup.getLookupDesc());
				if (++i < listLookup.size())
					sb.append(",");
			}
			sb.append("]");
			LOGGER.info(sb.toString());
		}
	}

	public Map<String, String> findLookupForSelection(int lookupCat) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		List<Lookup> list = findLookupByCat(lookupCat);
		for (Lookup lookup: list) {
			map.put("" + lookup.getLookupValue(), lookup.getLookupDesc());
		}
		return map;
	}

	public List<Lookup> findLookupByCat(int lookupCat) {
		try {
			if (listLookup == null)
				loadLookup();
			List<Lookup> list = new LinkedList<Lookup>();
			for (Lookup lookup: listLookup) {
//				LOGGER.debug("--> {}", lookup);
				if (lookup.getLookupCat() == lookupCat)
					list.add(lookup);
			}
			return list;
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			return new ArrayList<Lookup>();
		}
	}

	public Lookup findLookupByCatValue(int lookupCat, String lookupValue) {
		try {
			if (listLookup == null)
				loadLookup();
			for (Lookup lookup: listLookup) {
				if (lookup.getLookupCat() == lookupCat && lookup.getLookupValue().equals(lookupValue))
					return lookup;
			}
			return null;
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			return null;
		}
	}

	public Lookup findLookupByCatDesc(int lookupCat, String lookupDesc) {
		try {
			if (lookupDesc == null) lookupDesc = "";
			if (listLookup == null)
				loadLookup();
			for (Lookup lookup: listLookup) {
				if (lookup.getLookupCat() == lookupCat && lookupDesc.equals(lookup.getLookupValue()) )
					return lookup;
			}
			return null;
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			return null;
		}
	}

	public List<Lookup> findAllTrxCode()
	{
		List<Lookup> listTrxCode = lookupMapper.findAllTrxCode();
		return listTrxCode;
	}

	public List<Lookup> findListTpda()
	{
		List<Lookup> listTpda = lookupMapper.findAllTpda();
		return listTpda;
	}

	public List<Lookup> findAllTrxCodeCW()
	{
		List<Lookup> listTrxCode = lookupMapper.findAllTrxCodeCW();
		return listTrxCode;
	}

	public List<Lookup> findAllTrxGroup()
	{
		List<Lookup> listTrxGroup = lookupMapper.findTrxGroup();
		return listTrxGroup;
	}

	public List<Lookup> findTrxCodeByTrxGroup(String trxGroup)
	{
		List<Lookup> listTrxCode = lookupMapper.findTrxCodeByTrxGroup(trxGroup);
		return listTrxCode;
	}

	public List<Lookup> findC81()
	{
		List<Lookup> listC81 = lookupMapper.findC81();
		return listC81;
	}

	public List<Lookup> findC81Edit()
	{
		List<Lookup> listC81 = lookupMapper.findC81Edit();
		return listC81;
	}

	public void updateLookup(Lookup lookupData)
	{
		try {
			lookupMapper.updateLookupData(lookupData);
			LOGGER.debug("Success Update Lookup with param :" + lookupData);
		} catch (Exception e) {
			LOGGER.warn("Fail Update Lookup with param" + lookupData);
		}
	}

	public void callReloadLookup()
	{
		loadLookup();
	}

	public List<Lookup> findLookupByParam(LookupDataParamVO paramVO) {
		try {
			List<Lookup> listLookup = lookupMapper.findLookupByParam(paramVO);
			return listLookup;
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			return null;
		}
	}

	public int countLookupByParam(LookupDataParamVO paramVO) {
		try {
			int count = lookupMapper.countLookupByParam(paramVO);
			return count;
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			return 0;
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public WebResultVO insertOrUpdateLookupData(Lookup lookup, UserDataLoginVO loginVO, Locale language, int flagKey) throws SambaWebException
	{
		WebResultVO wrv = new WebResultVO();
		Date now = new Date();
		lookup.setUpdatedBy(loginVO.getId());
		lookup.setUpdatedOn(now);
		if (StringUtils.isEmpty(lookup.getLookupValue())) {
			throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,new String[] {messageService.getMessageFor("l.lookupValue", language)});
		}
		if (StringUtils.isEmpty(lookup.getLookupDesc())) {
			throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,new String[] {messageService.getMessageFor("l.lookupDesc", language)});
		}
		String msg ="";
		if(flagKey==0)
		{
			lookupMapper.insertLookupData(lookup);
			msg=messageService.getMessageFor("l.created", language);
			wrv.setType(WebConstants.TYPE_INSERT);
		}
		else
		{
			lookupMapper.updateWholeLookupData(lookup);
			msg=messageService.getMessageFor("l.updated", language);
			wrv.setType(WebConstants.TYPE_UPDATE);
			wrv.setPath(WebConstants.PATH_UPDATE_LOOKUP_DATA);
		}
		wrv.setRc(WebConstants.RESULT_SUCCESS);
		wrv.setMessage(messageService.getMessageFor("rm.generalMessage",
				new String[] {messageService.getMessageFor("l.lookupData", language), msg}, language));

		return wrv;
	}
}
