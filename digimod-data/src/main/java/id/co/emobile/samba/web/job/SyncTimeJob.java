package id.co.emobile.samba.web.job;

import id.co.emobile.samba.web.service.AppsTimeService;

import org.springframework.beans.factory.annotation.Autowired;

public class SyncTimeJob {
	
	@Autowired
	private AppsTimeService timeService;
	
	public void syncTime() {
		timeService.updateCurrentTime();
	}
}
