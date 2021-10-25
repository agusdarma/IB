package id.co.emobile.samba.web.mapper;

import id.co.emobile.samba.web.entity.Counter;

public interface CounterMapper {
	
	public Counter findCounterByAppName(String appName);
	
	public void createCounter(Counter counter);
	
	public int updateCounter(Counter counter);
	
}
