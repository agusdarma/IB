package id.co.emobile.samba.web.interceptor;

public interface ModuleStateAware {

	public String[] getSessionKeyToHandle();
	
	public String getModuleState();
	public void setModuleState(String moduleState);
}
