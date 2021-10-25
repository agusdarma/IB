//package id.co.emobile.samba.web.action.operational;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//import java.net.URLConnection;
//
//import org.apache.commons.io.FilenameUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import id.co.emobile.samba.web.action.BaseAction;
//import id.co.emobile.samba.web.data.UserDataLoginVO;
//import id.co.emobile.samba.web.helper.WebModules;
//import id.co.emobile.samba.web.service.DistMoneyService;
//
//public class DownloadAction extends BaseAction {
//	private static final long serialVersionUID = 1L;
//	private static final Logger LOG = LoggerFactory.getLogger(DownloadAction.class);
//	
//	@Autowired
//	private DistMoneyService distMoneyService;
//	
//	private String f;
//	private InputStream fileInputStream;
//	
//	public String getF() {
//		return f;
//	}
//	public void setF(String f) {
//		this.f = f;
//	}
//	public InputStream getFileInputStream() {
//		return fileInputStream;
//	}
//	
//	@Override
//	public String execute() {
//		// validate modules first, as this class not implement ModuleCheckable
//		UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);
//		if (loginVO == null) {
//			LOG.warn("No user is logged on");
//			return "failed";
//		}
//		boolean allowed = false;
//		int[] allowedMenu = {
//			WebModules.MODULE_APPS_OPERATIONAL_DIST_APPROVAL,
//			WebModules.MODULE_APPS_OPERATIONAL_DIST_CHECKER,
//			WebModules.MODULE_APPS_OPERATIONAL_DIST_MAKER,
//			WebModules.MODULE_APPS_OPERATIONAL_DIST_CALLBACK,
//			WebModules.MODULE_REPORT_DIST_MONEY,			
//		};
//		for (int menu: allowedMenu) {
//			if (loginVO.getLevelVO().isMenuAllowed(menu)) {
//				allowed = true;
//				break;
//			}
//		}
//		if (!allowed) {
//			LOG.warn("User {} does not have permission to view the file {}", loginVO, f);
//			return "failed";
//		}
//		
//		File file = distMoneyService.getFileFromName(f);
//		if (file != null) {
//			try {
//				fileInputStream = new FileInputStream(file);
//			} catch (FileNotFoundException e) {
//				LOG.warn("Unable to fetch file " + f, e);
//			}
//		}
//		if (fileInputStream == null) {
//			LOG.warn("Unable to find file {}", f);
//			return "failed";
//		}
//		return SUCCESS;
//	}
//	public String getDownloadContentType() {
//		LOG.debug("downloadContentType: {}", f);
//		if (StringUtils.isEmpty(f)) return "";
//		String mimeType = URLConnection.guessContentTypeFromName(f);
//		return mimeType;
//	}
//	public String getDownloadContentDisposition() {
//		LOG.debug("downloadContentDisposition: {}", f);
//		String name = FilenameUtils.getName(f);
//		if (name != null && name.startsWith("19") && name.indexOf("_") <= 8)
//			name = name.substring(7);
////		return "attachment;filename=\"" + name + "\"";
//		return "inline;filename=\"" + name + "\"";
//	}
//	
//	@Override
//	protected Logger getLogger() {
//		return LOG;
//	}
//
//}
