package id.co.emobile.jets.mmbs.bti.helper;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import id.co.emobile.samba.web.data.ResultCode;
import id.co.emobile.samba.web.service.JetsException;

public class MandiriRate {
	private static final Logger LOG = LoggerFactory.getLogger(MandiriRate.class);

	private final static String LOCAL_CURR = "IDR";
	
	private String curr;

	private double ibtBuy = 1;

	private double ibtSell = 1;

	private double echannelBuy = 1;

	private double echannelSell = 1;

	private double bookRate = 1;
	
	public MandiriRate() {
		this.curr = LOCAL_CURR;
	}

	/*
	 * param, bit62 from ForexRate ISO
	 */
	public MandiriRate(String curr, String bit62) throws JetsException {
		this.curr = curr;
		try {
			parse(bit62);
		}
		catch (Exception e) {
			throw new JetsException(e.toString(), ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	/*
	 * rate format, eg: 9.250,15
	 */
	private void parse(String bit62) throws Exception {
		try {
			if (bit62 == null) {
				return;
			}
			this.echannelBuy = convertToDouble(bit62.substring(0, 13).trim());
			this.echannelSell = convertToDouble(bit62.substring(13, 26).trim());
			this.ibtBuy = convertToDouble(bit62.substring(26, 39).trim());
			this.ibtSell = convertToDouble(bit62.substring(39, 52).trim());
			this.bookRate = convertToDouble(bit62.substring(52, 65).trim());
		}
		catch (Exception e) {
			throw new Exception("Unable to parse bit62: " + bit62);
		}
	}

	private double convertToDouble(String str) throws ParseException {
		//German and Indonesia has the same number format
		Double dbl;
		try {
			dbl = (Double)NumberFormat.getInstance(Locale.GERMAN).parse(str);
			return dbl.doubleValue();
		}
		catch (ParseException e) {
			LOG.error("Unable to convert: " + str + " to double");
			throw e;
		}
	}
	
	public boolean isLocal() {
		return LOCAL_CURR.equals(curr);
	}

	public String getCurr() {
		return curr;
	}

	public void setCurr(String curr) {
		this.curr = curr;
	}

	public double getIbtBuy() {
		return ibtBuy;
	}

	public void setIbtBuy(double ibtBuy) {
		this.ibtBuy = ibtBuy;
	}

	public double getIbtSell() {
		return ibtSell;
	}

	public void setIbtSell(double ibtSell) {
		this.ibtSell = ibtSell;
	}

	public double getEchannelBuy() {
		return echannelBuy;
	}

	public void setEchannelBuy(double echannelBuy) {
		this.echannelBuy = echannelBuy;
	}

	public double getEchannelSell() {
		return echannelSell;
	}

	public void setEchannelSell(double echannelSell) {
		this.echannelSell = echannelSell;
	}

	public double getBookRate() {
		return bookRate;
	}

	public void setBookRate(double bookRate) {
		this.bookRate = bookRate;
	}

}
