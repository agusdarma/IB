//package id.co.emobile.jets.mmbs.bti.helper;
//
//import java.util.SortedSet;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.emobile.jets.mmbs.lib.data.FundFlowVO;
//import com.emobile.jets.mmbs.lib.service.JetsException;
//import com.emobile.jets.mmbs.lib.util.ResultCode;
//
//import id.co.emobile.jets.mmbs.bti.CommonUtils;
//
//public class MultiCreditBit61 {
//	private static final Logger LOG = LoggerFactory.getLogger(MultiCreditBit61.class);
//	
//	private String fromAccount;
//	private String toAccount;
//	private String ibtFromRate;
//	private String ibtToRate;
//	private String echannelFromRate;
//	private String echannelToRate;
//	private String chargesIbtRate;
//	private String chargesEchannelRate;
//	private String fromCurrencyCode;
//	private String toCurrencyCode;
//	private String fromAmount;
//	private String toAmount;
//	private String remark;
//	private String chargesFromAccountNo;
//	private String chargesFromAccountType;
//	private String chargesAmountIdr;
//	private String chargesAmountCurr;
//	private SortedSet<FundFlowVO> fundFlows;
//	
//	public void parse(String bit61) throws Exception {
//		try {
//			if (LOG.isDebugEnabled()) {
//				LOG.debug("Parse bit61: " + bit61);
//			}
//			fromAccount = bit61.substring(0, 20);				// AN 20
//			toAccount = bit61.substring(20, 40);				// AN 20
//			ibtFromRate = bit61.substring(40, 53);				// N  13
//			ibtToRate = bit61.substring(53, 66);				// N  13
//			echannelFromRate = bit61.substring(66, 79);			// N  13
//			echannelToRate = bit61.substring(79, 92);			// N  13
//			chargesIbtRate = bit61.substring(92, 105);			// N  13
//			chargesEchannelRate = bit61.substring(105, 118);	// N  13
//			fromCurrencyCode = bit61.substring(118, 121);		// AN 3
//			toCurrencyCode = bit61.substring(121, 124);			// AN 3
//			fromAmount = bit61.substring(124, 141);				// N  17
//			toAmount = bit61.substring(141, 158);				// N  17
//			remark = bit61.substring(158, 198);					// AN 40
//			chargesFromAccountNo = bit61.substring(198, 218);	// AN 20
//			chargesFromAccountType = bit61.substring(218, 220);	// AN 2
//			chargesAmountIdr = bit61.substring(220, 237);		// N  17
//			chargesAmountCurr = bit61.substring(237, 254);		// N  17
//		} catch(Exception e) {
//			LOG.error("Exception in parsing bit 61: " + e);
//			throw e;
//		}
//	}
//	
//	public String getFromAccount() {
//		return fromAccount;
//	}
//	public void setFromAccount(String fromAccount) {
//		this.fromAccount = fromAccount;
//	}
//	public String getToAccount() {
//		return toAccount;
//	}
//	public void setToAccount(String toAccount) {
//		this.toAccount = toAccount;
//	}
//	public String getIbtFromRate() {
//		return ibtFromRate;
//	}
//	public void setIbtFromRate(String ibtFromRate) {
//		this.ibtFromRate = ibtFromRate;
//	}
//	public String getIbtToRate() {
//		return ibtToRate;
//	}
//	public void setIbtToRate(String ibtToRate) {
//		this.ibtToRate = ibtToRate;
//	}
//	public String getEchannelFromRate() {
//		return echannelFromRate;
//	}
//	public void setEchannelFromRate(String echannelFromRate) {
//		this.echannelFromRate = echannelFromRate;
//	}
//	public String getEchannelToRate() {
//		return echannelToRate;
//	}
//	public void setEchannelToRate(String echannelToRate) {
//		this.echannelToRate = echannelToRate;
//	}
//	public String getChargesIbtRate() {
//		return chargesIbtRate;
//	}
//	public void setChargesIbtRate(String chargesIbtRate) {
//		this.chargesIbtRate = chargesIbtRate;
//	}
//	public String getChargesEchannelRate() {
//		return chargesEchannelRate;
//	}
//	public void setChargesEchannelRate(String chargesEchannelRate) {
//		this.chargesEchannelRate = chargesEchannelRate;
//	}
//	public String getFromCurrencyCode() {
//		return fromCurrencyCode;
//	}
//	public void setFromCurrencyCode(String fromCurrencyCode) {
//		this.fromCurrencyCode = fromCurrencyCode;
//	}
//	public String getToCurrencyCode() {
//		return toCurrencyCode;
//	}
//	public void setToCurrencyCode(String toCurrencyCode) {
//		this.toCurrencyCode = toCurrencyCode;
//	}
//	public String getFromAmount() {
//		return fromAmount;
//	}
//	public void setFromAmount(String fromAmount) {
//		this.fromAmount = fromAmount;
//	}
//	public String getToAmount() {
//		return toAmount;
//	}
//	public void setToAmount(String toAmount) {
//		this.toAmount = toAmount;
//	}
//	public String getRemark() {
//		return remark;
//	}
//	public void setRemark(String remark) {
//		this.remark = remark;
//	}
//	public String getChargesFromAccountNo() {
//		return chargesFromAccountNo;
//	}
//	public void setChargesFromAccountNo(String chargesFromAccountNo) {
//		this.chargesFromAccountNo = chargesFromAccountNo;
//	}
//	public String getChargesFromAccountType() {
//		return chargesFromAccountType;
//	}
//	public void setChargesFromAccountType(String chargesFromAccountType) {
//		this.chargesFromAccountType = chargesFromAccountType;
//	}
//	public String getChargesAmountIdr() {
//		return chargesAmountIdr;
//	}
//	public void setChargesAmountIdr(String chargesAmountIdr) {
//		this.chargesAmountIdr = chargesAmountIdr;
//	}
//	public String getChargesAmountCurr() {
//		return chargesAmountCurr;
//	}
//	public void setChargesAmountCurr(String chargesAmountCurr) {
//		this.chargesAmountCurr = chargesAmountCurr;
//	}
//	public SortedSet<FundFlowVO> getFundFlows() {
//		return fundFlows;
//	}
//	public void setFundFlows(SortedSet<FundFlowVO> fundFlows) {
//		this.fundFlows = fundFlows;
//	}
//	
//	public String toBit61Str() throws Exception {
//		String bit61Str = "";
//
//		if (fundFlows.size() < 1 || fundFlows.size() > 5) {
//			LOG.error("");
//			throw new JetsException(ResultCode.BTI_ERROR_PARSE_ISO);
//		}
//				
//		FundFlowVO[] fundFlowVOArr = (FundFlowVO[])fundFlows.toArray(new FundFlowVO[0]);
//		
//		String toAccountNoFlow = addToAccountNoFlow(fundFlowVOArr);
//		String toAmountFlow = addToAmountFlow(fundFlowVOArr);
//		
//		bit61Str = fromAccount +
//		toAccount +
//		ibtFromRate +
//		ibtToRate +
//		echannelFromRate +
//		echannelToRate +
//		chargesIbtRate +
//		chargesEchannelRate +
//		fromCurrencyCode +
//		toCurrencyCode +
//		fromAmount +
//		toAmount +
//		remark +
//		chargesFromAccountNo +
//		chargesFromAccountType +
//		chargesAmountIdr +
//		chargesAmountCurr +
//		toAccountNoFlow +
//		toAmountFlow;
//		
//		return bit61Str;
//	}
//	
//	private String addToAccountNoFlow(FundFlowVO[] fundFlowVOArr) {
//		String result = "";
//		
//		for (int i=0;i<5;i++) {
//			if (i == 0) {
//				setToAccount(CommonUtils.strpad(fundFlowVOArr[i].getAcctTo(), 20, ' ', CommonUtils.PAD_LEFT));
//			} else if (i <= fundFlows.size()) {
//				result = result + CommonUtils.strpad(fundFlowVOArr[i].getAcctTo(), 20, ' ', CommonUtils.PAD_LEFT);
//			} else {
//				result = result + CommonUtils.padright(" ", 20, ' ');
//			}
//		}
//		
////		for (FundFlowVO ff : fundFlows) {
////			result = result + MandiriUtil.strpad(ff.getAcctTo(), 20, ' ', MandiriUtil.PAD_LEFT);
////		}
//		return result;
//	}
//	
//	private String addToAmountFlow(FundFlowVO[] fundFlowVOArr) throws Exception {
//		String result = "";
//
//		for (int i=0;i<5;i++) {
//			if (i == 0) {
//				setToAmount(CommonUtils.formatCurrency("" + fundFlowVOArr[i].getAmount(), 15, 2));
//			} else if (i <= fundFlows.size()) {
//				String amnt = CommonUtils.formatCurrency("" + fundFlowVOArr[i].getAmount(), 15, 2);
//				result = result + amnt + amnt;
//			} else {
//				result = result + CommonUtils.formatCurrency("0", 15, 2);
//			}
//		}
//		
////		for (FundFlowVO ff : fundFlows) {
////			String isoAmount = MandiriUtil.formatCurrency("" + ff.getAmount(), 15, 2);
////			result = result + isoAmount + isoAmount;
////		}
//		return result;
//	}
//	
//	public String toString() {
//		StringBuffer sb = new StringBuffer();
//		sb.append("fromAccount:");
//		sb.append(fromAccount);
//		sb.append(",toAccount:");
//		sb.append(toAccount);
//		sb.append(",ibtFromRate:");
//		sb.append(ibtFromRate);
//		sb.append(",ibtToRate:");
//		sb.append(ibtToRate);
//		sb.append(",echannelFromRate:");
//		sb.append(echannelFromRate);
//		sb.append(",echannelToRate:");
//		sb.append(echannelToRate);
//		sb.append(",chargesIbtRate:");
//		sb.append(chargesIbtRate);
//		sb.append(",chargesEchannelRate:");
//		sb.append(chargesEchannelRate);
//		sb.append(",fromCurrencyCode:");
//		sb.append(fromCurrencyCode);
//		sb.append(",toCurrencyCode:");
//		sb.append(toCurrencyCode);
//		sb.append(",fromAmount:");
//		sb.append(fromAmount);
//		sb.append(",toAmount:");
//		sb.append(toAmount);
//		sb.append(",remark:");
//		sb.append(remark);
//		sb.append(",chargesFromAccountNo:");
//		sb.append(chargesFromAccountNo);
//		sb.append(",chargesFromAccountType:");
//		sb.append(chargesFromAccountType);
//		sb.append(",chargesAmountIdr:");
//		sb.append(chargesAmountIdr);
//		sb.append(",chargesAmountCurr:");
//		sb.append(chargesAmountCurr);
//		sb.append(toAccountNoStr());
//		//TODO
//		try {
//			sb.append(toAmountStr());
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//		return sb.toString();
//	}
//
//	private StringBuffer toAccountNoStr() {
//		StringBuffer sb = new StringBuffer();
//		int cnt = 0;
//		for (FundFlowVO ff : fundFlows) {
//			cnt++;
//			sb.append(",chargesToAccountNo" + cnt + ":");
//			sb.append(CommonUtils.strpad(ff.getAcctTo(), 20, ' ', CommonUtils.PAD_LEFT));
//		}
//		return sb;
//	}
//	
//	private StringBuffer toAmountStr() throws Exception {
//		StringBuffer sb = new StringBuffer();
//		int cnt = 0;
//		for (FundFlowVO ff : fundFlows) {
//			cnt++;
//			String isoAmount = CommonUtils.formatCurrency("" + ff.getAmount(), 15, 2);
//			sb.append(",chargesToAmountIdr" + cnt + ":");
//			sb.append(isoAmount);
//			sb.append(",chargesToAmountCurr" + cnt + ":");
//			sb.append(isoAmount);
//		}
//		return sb;
//	}
//	
//}
