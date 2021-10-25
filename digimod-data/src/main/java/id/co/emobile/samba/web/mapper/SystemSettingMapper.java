package id.co.emobile.samba.web.mapper;

import id.co.emobile.samba.web.data.param.SystemSettingParamVO;
import id.co.emobile.samba.web.entity.SystemSetting;

import java.util.List;

public interface SystemSettingMapper {
	
	public List<SystemSetting> findSystemSettingAll();
	
	public void updateSystemSetting(SystemSetting systemSetting);
	
	public SystemSetting findSystemSettingById(int settingId);
	
	public List<SystemSetting> findSystemSettingByParam(SystemSettingParamVO systemParamVO);
	
	public int countSystemSettingByParam(SystemSettingParamVO systemParamVO);
		
	public SystemSetting findSystemSettingBySettingName(String settingName);
}
