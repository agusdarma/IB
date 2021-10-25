package id.co.emobile.samba.web.data;
/**
 *  Historical Changes:
 *  *  1.3.3 - 2020-03-18, Agus
 *  - Added menu Upload Data Other
 *  
 *  1.3.2 - 2020-03-12, Hariyanto
 *  - fix resultCode in TrxCollegaService.checkStatementForAccount
 *  
 *  1.3.1 - 2020-03-05, Hariyanto
 *  - add module check acc statement
 *  
 *  1.3.0 - 2020-03-03, Hariyanto
 *  - remove lib gson, update lib Jackson
 *  - remove folder RAW in folder JavaScript
 *  - add module user activity report
 *  
 *  1.2.0 - 2020-02-28, Hariyanto
 *  - refactor user_activity, drop and create table user_activity
 *  
 *  1.1.1 - 2020-02-28, Hariyanto
 *  - disable button sendOTP and button process if detail is failed checked
 *  
 *  1.1.0 - 2020-02-17, Hariyanto
 *  - add support for sending transaction to Collega
 *  
 *  1.0.7 - 2020-01-22, Hariyanto
 *  - add validation for srac status in DistMoneyService.verifyDistApproval
 *  
 *  1.0.6 - 2019-08-06, Hariyanto
 *  - add check for duplicate number in upload file
 *  - b: add check for txType and totalDebet in DistMoneyService.processDistMaker
 *  
 *  1.0.5 - 2019-08-02, Hariyanto
 *  - add check for account no and phoneNo in DistMoneyService.processDistMaker
 *  
 *  1.0.4 - 2019-07-11, Hariyanto
 *  - add filter for reporting based on group
 *  
 *  1.0.3 - 2019-07-03, Hariyanto
 *  - add group approval
 *  
 *  1.0.2 - 2019-07-01, Hariyanto
 *  - add balanceRc in SourceAccountVO
 *  
 *  1.0.1 - 2019-06-28, Hariyanto
 *  - add PDFBox to check for valid PDF File in DistMoneyService
 *  - add filter source account active in SourceAccountService
 *  - add secure cookie in web.xml
 *  - remove form in login.jsp
 *  
 * 1.0.0 - 2019-05-22, Hariyanto
 * - initial release
 * - a: add level callback
 */

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VersionData {
	private static Logger LOG = LoggerFactory.getLogger(VersionData.class);
	
	private String version;
	private String appsName;
	private String buildDate;
	
	public VersionData() {
		appsName = "NabungDividen - Introducing Broker Web";
		version = "1.3.3";
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy, HH:mm");
		
		String buildNumber = "";
		Calendar cal = Calendar.getInstance();
		cal.set(2020, Calendar.MARCH, 12, 17, 20);
		Date appsBuild = new Date();
		try {
			Enumeration<URL> resources = getClass().getClassLoader()
				.getResources("META-INF/MANIFEST.MF");
			while (resources.hasMoreElements()) {
				Manifest manifest = new Manifest(resources.nextElement().openStream());
				Attributes attrib = manifest.getMainAttributes();
				String vendorId = attrib.getValue("Implementation-Vendor-Id");
				if ("id.co.emobile".equals(vendorId)) {
					buildNumber = attrib.getValue("Implementation-Build");
					String timestamp = attrib.getValue("Implementation-Timestamp");
					if (timestamp != null && !timestamp.equals("")) {
						long t = Long.parseLong(timestamp);
						appsBuild = new Date(t);
					}
					break;
				}
			}
		} catch (IOException E) {
			// handle
		}
		if (!"".equals(buildNumber))
			version = version + " rev." + buildNumber;
		this.buildDate = sdf.format(appsBuild);
	}
	
	public void printInfo() throws Exception {
		LOG.info("{} v.{}, Built on {}", appsName, version, buildDate );
	}
	
	public String getVersion() {
		return version;
	}
	
	public String getAppsName() {
		return appsName;
	}
	
	public String getBuildDate() {
		return buildDate;
	}


}
