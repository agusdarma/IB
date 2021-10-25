package id.co.emobile.jets.mmbs.bti.helper;

import id.co.emobile.samba.web.data.ResultCode;
import id.co.emobile.samba.web.service.JetsException;

/*
 * Only support for Foreign to Local
 * effective rate = echannelBuyRate - (echannelBuyRate*spread/100)
 * fromAmount = user input
 * toAmount = user input * effective rate
 * chargesIdr = pass by mte
 * chargesValas = chargeIdr / (effectiveRate)
 * bit 4 = toAmount
 * echannelFrom = effectiveRate
 * 
 */
public class RemittanceMandiriRateMatrix extends MandiriRateMatrix {

	private double spread;

	private double effectiveRate;

	private double localCharges;

	public RemittanceMandiriRateMatrix(MandiriRate rate, String strAmount, String strSpread,
			String strLocalCharges) throws JetsException {
		super(rate, new MandiriRate(), strAmount);
		try {
			this.spread = Double.parseDouble(strSpread);
			this.localCharges = Double.parseDouble(strLocalCharges);
			checkType();
			calculateEffectiveRate();
		}
		catch (NumberFormatException e) {
			throw new JetsException("Unable to parse spread " + strSpread + ": " + e.toString(),
					ResultCode.BTI_ERROR_COMPOSE_ISO);
		}
	}

	public RemittanceMandiriRateMatrix(MandiriRate rate, double amount, double spread, double localCharges)
			throws JetsException {
		super(rate, new MandiriRate(), amount);
		this.spread = spread;
		this.localCharges = localCharges;
		checkType();
		calculateEffectiveRate();
	}

	/*
	 * effective rate = echannelBuyRate - (echannelBuyRate*spread/100)
	 */
	private void calculateEffectiveRate() {
		double eChannelBuyRate = super.getEChannelFromRate();
		this.effectiveRate = eChannelBuyRate - (eChannelBuyRate * spread / 100);
	}

	/**
	 * only support for foreign to local currency
	 * @throws JetsException
	 */
	private void checkType() throws JetsException {
		if (!(type == Type.ForeignToLocal)) {
			throw new JetsException("Must be foreign to local fund transfer", ResultCode.BTI_UNSUPPORTED_TRX);
		}
	}

	@Override
	public double getBit4() {
		return getToAmount();
	}

	@Override
	public double getCharges() {
		return localCharges / effectiveRate;
	}

	@Override
	public String getChargesAccount() {
		return "TK";
	}

	@Override
	public double getChargesEChannelRate() {
		return effectiveRate;
	}

	@Override
	public double getEChannelFromRate() {
		return effectiveRate;
	}

	@Override
	public double getFromAmount() {
		return amount;
	}

	@Override
	public double getLocalCharges() {
		return localCharges;
	}

	@Override
	public double getToAmount() {
		return amount * effectiveRate;
	}

	public double getEffectiveRate() {
		return effectiveRate;
	}

}
