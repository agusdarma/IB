package id.co.emobile.samba.web.mapper;

import id.co.emobile.samba.web.entity.PasswordHistory;

public interface PassHistoryMapper {
	
	public PasswordHistory  findPassHistoryByUserId(String userId);
	
	public void createPassHistory (PasswordHistory passwordHistory);
	
	public void updatePassHistory (PasswordHistory passwordHistory);
}
