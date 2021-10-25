package id.co.emobile.jets.mmbs.bti.helper;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MandiriDspHeaderHelper {
	private static final Logger LOG = LoggerFactory.getLogger(MandiriDspHeaderHelper.class);
	
	private String socketHeader;
	
	private byte[] footer1 = {(byte) 00, (byte) 00, (byte) 00, (byte) 00, (byte) 00, (byte) 00, (byte) 00, 
							  (byte) 00, (byte) 00};
	
	private String footer1Str;
	
	private String footer2Str;
	
	private String footer3Str;
	
	public MandiriDspHeaderHelper() {
		LOG.info("MandiriDspHeaderHelper initializing");
		try {
			InetAddress ip = InetAddress.getLocalHost();
			String ipAddress = ip.getHostAddress(); //"10.204.9.37";
			socketHeader = "*MOBI" + ipAddress;
			footer1 = padByteArray(21 - ipAddress.length());
			
			footer1Str = new String(footer1);
			footer2Str = new String(new byte[]{(byte) 32});
			footer3Str = new String(new byte[]{(byte) 89, (byte) 48});
		} catch (UnknownHostException e) {
			LOG.error("Unknown network!");
		}
	}
	
	public String addHeader(String msg) {
		LOG.debug("Add header with helper");
		String encMsg = convertToEbcdic(socketHeader + footer1Str) + 
				footer2Str + convertToEbcdic(footer3Str + msg);
		return encMsg;
	}
	
	private String convertToEbcdic(String msg) {
		try {
			return new String(msg.getBytes("cp037")); //cp1047
		} catch (UnsupportedEncodingException e) {
			LOG.error("Failed convert to EBCDIC");
			return msg;
		}
	}

	private byte[] padByteArray(int len) {
		byte[] b = new byte[len];
		for (int i=0; i<len; i++) {
			b[i] = (byte) 00;
		}
		return b;
	}
	
}
