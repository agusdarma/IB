package id.co.emobile.jets.mmbs.bti.helper;

import id.co.emobile.samba.web.data.ResultCode;
import id.co.emobile.samba.web.service.JetsException;

/*
 * See Mandiri ISO specification section A.5.3 for details
 */
public class MandiriRateMatrix {

	protected MandiriRate rate1;

	protected MandiriRate rate2;

	protected double amount;

	protected Type type;
	
	public MandiriRateMatrix(MandiriRate rate1, MandiriRate rate2, String strAmount) throws JetsException {
		try {
			this.amount = Double.parseDouble(strAmount);
			this.rate1 = rate1;
			this.rate2 = rate2;
			defineType();
		}
		catch (NumberFormatException e) {
			throw new JetsException("Unable to parse amount " + strAmount + ": " + e.toString(),
					ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public MandiriRateMatrix(MandiriRate rate1, MandiriRate rate2, double amount) {
		this.rate1 = rate1;
		this.rate2 = rate2;
		this.amount = amount;
		defineType();
	}

	protected void defineType() {
		if (rate1.getCurr().equals(rate2.getCurr())) {
			if (rate1.isLocal()) {
				type = Type.LocalToLocal;
			}
			else {
				type = Type.ForeignToForeign;
			}
		}
		else {
			if (rate1.isLocal() && !rate2.isLocal()) {
				type = Type.LocalToForeign;
			}
			else if (!rate1.isLocal() && rate2.isLocal()) {
				type = Type.ForeignToLocal;
			}
			else {
				type = Type.ForeignAToForeignB;
			}
		}
	}

	public double getBit4() {
		switch (type) {
		case LocalToLocal:
			return amount;
		case LocalToForeign:
			return amount * rate1.getEchannelSell();
		case ForeignToLocal:
			return amount;
		case ForeignAToForeignB:
			return amount * rate2.getEchannelSell();
		case ForeignToForeign:
			return amount * rate1.getEchannelBuy();
		default:
			return 0;
		}
	}

	public double getIBTFromRate() {
		switch (type) {
		case LocalToLocal:
		case LocalToForeign:
		case ForeignToLocal:
		case ForeignAToForeignB:
			return rate1.getIbtBuy();
		case ForeignToForeign:
			return rate1.getBookRate();
		default:
			return 0;
		}
	}

	public double getIBTToRate() {
		switch (type) {
		case LocalToLocal:
		case LocalToForeign:
		case ForeignToLocal:
		case ForeignAToForeignB:
			return rate2.getIbtSell();
		case ForeignToForeign:
			return rate1.getBookRate();
		default:
			return 0;
		}
	}

	public double getEChannelFromRate() {
		switch (type) {
		case LocalToLocal:
		case LocalToForeign:
		case ForeignToLocal:
		case ForeignAToForeignB:
			return rate1.getEchannelBuy();
		case ForeignToForeign:
			return rate1.getBookRate();
		default:
			return 0;
		}
	}

	public double getEChannelToRate() {
		switch (type) {
		case LocalToLocal:
		case LocalToForeign:
		case ForeignToLocal:
		case ForeignAToForeignB:
			return rate2.getEchannelSell();
		case ForeignToForeign:
			return rate1.getBookRate();
		default:
			return 0;
		}
	}

	public double getChargesIBTRate() {
		switch (type) {
		case LocalToLocal:
		case LocalToForeign:
		case ForeignToLocal:
		case ForeignAToForeignB:
		case ForeignToForeign:
			return rate1.getIbtBuy();
		default:
			return 0;
		}
	}

	public double getChargesEChannelRate() {
		switch (type) {
		case LocalToLocal:
		case LocalToForeign:
		case ForeignToLocal:
		case ForeignAToForeignB:
		case ForeignToForeign:
			return rate1.getEchannelBuy();
		default:
			return 0;
		}
	}

	public double getFromAmount() {
		switch (type) {
		case LocalToLocal:
			return amount;
		case LocalToForeign:
			return amount * rate1.getEchannelSell();
		case ForeignToLocal:
			return amount / rate1.getEchannelBuy();
		case ForeignAToForeignB:
			return amount * rate2.getEchannelSell() / rate1.getEchannelBuy();
		case ForeignToForeign:
			return rate1.getBookRate();
		default:
			return 0;
		}
	}

	public double getToAmount() {
		return amount;
	}

	public String getFromCurr() {
		return rate1.getCurr();
	}

	public String getToCurr() {
		return rate2.getCurr();
	}

	/*
	 * charges always blank character
	 */
	public String getChargesAccount() {
		return " ";
	}

	/*
	 * charges always 0
	 */
	public double getLocalCharges() {
		return 0;
	}

	/*
	 * return charges according to debit currency
	 */
	public double getCharges() {
		return 0;
	}
	
	protected enum Type {
		LocalToLocal, LocalToForeign, ForeignToLocal, ForeignAToForeignB, ForeignToForeign;
	}

}
