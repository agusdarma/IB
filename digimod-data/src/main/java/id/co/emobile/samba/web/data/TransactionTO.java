package id.co.emobile.samba.web.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionTO implements java.io.Serializable {
	private static final Logger LOG = LoggerFactory.getLogger(TransactionTO.class);
	private static final long serialVersionUID = 1L;
	private String originalMsgFromApplet;
	private long receivedTime;
	private long sentTime;
	private String sentStatus;
	private long btiArrivedTime;
	private int btiProcess;  // how long process in BTI
	private String btiRc;  // rc from BTI
	
	private long xtiArrivedTime;
	private int xtiProcess;  // how long process in XTI
	private String xtiRc;  // rc from XTI
	
	private String msgLogNo;
	private String sysLogNo;
	private String trxCode;
	
	private String phoneNo;
	private String messageInput;
	private String messageOutput;
	private String messageNotif;
	private String clientRef;
	
	private String smiInput;
	private String channelType;
	private String smiOutput;
	
	// value of transaction, usually just for financial trx
	// amount from term amount should be copied, amount from term amount is never modified..
	// it is from user input
	private double trxValue;  
	
	private boolean verification;
	private int state;
	private String resultCode;
	private String chargeCode;
	private String sysMessage;
	
	private boolean routeToBti;  // flag to route to BTI
	private boolean routeToXti;  // flag to route to XTI
	
	private Map<String, String> terms = new HashMap<String, String>();
	
	private Set<NotificationMsg> listNotifMsg = new HashSet<NotificationMsg>();
	
	private Map<String, Channel> mapChannelTcp = new HashMap<String, Channel>();
	
	private List<String> listSuccessRegSmsBlast = new ArrayList<String>();
	private List<String> listSuccessUnregSmsBlast = new ArrayList<String>();
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[")
			.append("originalMsgFromApplet=").append(originalMsgFromApplet).append(",")
			.append("msgLogNo=").append(msgLogNo).append(",")
			.append("sysLogNo=").append(sysLogNo).append(",")
			.append("trxCode=").append(trxCode).append(",");
		if (receivedTime > 0) {
			Date d = new Date(receivedTime);
			sb.append("receivedTime=").append(d.toString()).append(",");
		}
		if (sentTime > 0) {
			Date d = new Date(sentTime);
			sb.append("sentTime=").append(d.toString()).append(",");
			sb.append("sentStatus=").append(sentStatus).append(",");
		}
		if (btiArrivedTime > 0) {
			Date d = new Date(btiArrivedTime);
			sb.append("btiArrivedTime=").append(d.toString()).append(",");
			sb.append("btiProcess=").append(btiProcess).append(",");
			sb.append("btiRc=").append(btiRc).append(",");
		}
		// masked the message output for digit PIN
		String in = messageInput;
		if (StringUtils.isEmpty(in)) in = "";
		in = in.replaceAll(" [0-9]{2}$", " **");
		in = in.replaceAll(" [0-9]{2} ", " ** ");
		
		String out = messageOutput;
		if (StringUtils.isEmpty(out)) out = "";
		//out = out.replaceAll(" [0-9]{1} ", " * ");
		//out = out.replaceAll(" [0-9]{1}.", " *.");
		sb.append("phoneNo=").append(phoneNo).append(",")	
			.append("clientRef=").append(clientRef).append(",")	
			.append("messageInput=").append(in).append(",")	
			.append("messageOutput=").append(out).append(",")	
			.append("smiInput=").append(smiInput).append(",")	
			.append("smiOutput=").append(smiOutput).append(",")	
			.append("channelType=").append(channelType).append(",")	
			.append("trxValue=").append(trxValue).append(",")
			.append("verification=").append(verification).append(",")
			.append("state=").append(state).append(",")
			.append("resultCode=").append(resultCode).append(",")
			.append("sysMessage=").append(sysMessage).append(",")
			.append("chargeCode=").append(chargeCode).append(",");
		
		if (terms.size() > 0) {
			Iterator<String> iter = terms.keySet().iterator();
			sb.append("terms=[");
			while(iter.hasNext()) {
				String key = iter.next();
				if (TermConstant.PIN.equals(key) || 
						TermConstant.CONF_PIN.equals(key) || 
						TermConstant.NEW_PIN.equals(key) ||
						TermConstant.BUYER_PIN.equals(key) ||
						TermConstant.MERCHANT_PIN.equals(key) ||
						TermConstant.USER_INPUT.equals(key) ||
						TermConstant.DOB.equals(key))
					sb.append(key).append("=").append("***");
				else
					sb.append(key).append("=").append(terms.get(key));
				if (iter.hasNext())
					sb.append(",");
			} 
			sb.append("]");
		} else {
			sb.append("terms=[]");
		}
		sb.append("]");
		return sb.toString();
	}

	public String toLogFormat() {
		if (StringUtils.isEmpty(resultCode)) {
			return String.format("%s-#%s [%s: %s(%s)]", sysLogNo, msgLogNo, phoneNo, trxCode, state);
		} else {
			return String.format("%s-#%s [%s: %s(%s)] => %s", sysLogNo, msgLogNo, phoneNo, trxCode, state, resultCode);
		}
	}
	
	public long getReceivedTime() {
		return receivedTime;
	}
	public void setReceivedTime(long receivedTime) {
		this.receivedTime = receivedTime;
	}

	public long getSentTime() {
		return sentTime;
	}
	public void setSentTime(long sentTime) {
		this.sentTime = sentTime;
	}

	public String getSentStatus() {
		return sentStatus;
	}
	public void setSentStatus(String sentStatus) {
		this.sentStatus = sentStatus;
	}
	
	public long getBtiArrivedTime() {
		return btiArrivedTime;
	}
	public void setBtiArrivedTime(long btiArrivedTime) {
		this.btiArrivedTime = btiArrivedTime;
	}

	public int getBtiProcess() {
		return btiProcess;
	}
	public void setBtiProcess(int btiProcess) {
		this.btiProcess = btiProcess;
	}

	public String getBtiRc() {
		return btiRc;
	}
	public void setBtiRc(String btiRc) {
		this.btiRc = btiRc;
	}
	
	public String getMsgLogNo() {
		return msgLogNo;
	}
	public void setMsgLogNo(String msgLogNo) {
		this.msgLogNo = msgLogNo;
	}

	public String getSysLogNo() {
		return sysLogNo;
	}
	public void setSysLogNo(String sysLogNo) {
		this.sysLogNo = sysLogNo;
	}

	public String getTrxCode() {
		return trxCode;
	}
	public void setTrxCode(String trxCode) {
		this.trxCode = trxCode;
	}
	
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getMessageInput() {
		return messageInput;
	}
	public void setMessageInput(String messageInput) {
		this.messageInput = messageInput;
	}

	public String getMessageOutput() {
		return messageOutput;
	}
	public void setMessageOutput(String messageOutput) {
		this.messageOutput = messageOutput;
	}
	
	public String getMessageNotif() {
		return messageNotif;
	}
	public void setMessageNotif(String messageNotif) {
		this.messageNotif = messageNotif;
	}

	public String getClientRef() {
		return clientRef;
	}
	public void setClientRef(String clientRef) {
		this.clientRef = clientRef;
	}
	
	public String getSmiInput() {
		return smiInput;
	}
	public void setSmiInput(String smiInput) {
		this.smiInput = smiInput;
	}
	public String getChannelType() {
		return channelType;
	}
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
	public String getSmiOutput() {
		return smiOutput;
	}
	public void setSmiOutput(String smiOutput) {
		this.smiOutput = smiOutput;
	}
	
	public double getTrxValue() {
		return trxValue;
	}
	public void setTrxValue(double trxValue) {
		this.trxValue = trxValue;
	}
	
	public boolean isVerification() {
		return verification;
	}
	public void setVerification(boolean verification) {
		this.verification = verification;
	}
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}

	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	
	public String getChargeCode() {
		return chargeCode;
	}
	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}
	
	public String getSysMessage() {
		return sysMessage;
	}
	public void setSysMessage(String sysMessage) {
		this.sysMessage = sysMessage;
	}
	
	public boolean isRouteToBti() {
		return routeToBti;
	}
	public void setRouteToBti(boolean routeToBti) {
		this.routeToBti = routeToBti;
	}
	
	public Map<String, String> getTerms() {
		return terms;
	}
	public void setTerms(Map<String, String> terms) {
		this.terms = terms;
	}

	public boolean isTermExists(String key) {
		return terms.get(key) != null;
	}
	
	public String getTerm(String name) {
		return terms.get(name);
	}
	
	public String getMandatoryTerm(String name) throws Exception {
		String term = terms.get(name);
		if (term == null) {
			throw new Exception("Term: " + name + " not found");
		}
		return term;
	}
	
	/*
	 * return empty string if null
	 */
	public String getTermAsString(String name) {
		String s = getTerm(name);
		if (s == null) {
			return "";
		}
		return s;
	}
	public int getTermAsInteger(String name) {
		try {
			int result = Integer.parseInt(terms.get(name));
			return result;
		} catch (Exception e) {
			LOG.warn("[{}] Error getTermAsInteger [{}]: {}",
					msgLogNo, name, e.getMessage());
			return 0;
		}
	}
	public double getTermAsDouble(String name) {
		String s = getTerm(name);
		if (s == null) {
			return 0;
		}
		try {
			double result = Double.parseDouble(s);
			return result;
		} catch (Exception e) {
			LOG.warn("[{}] Error getTermAsDouble [{}]: {}",
					msgLogNo, name, e.getMessage());
			return 0;
		}
	}
	
	public boolean getTermAsBoolean(String name) {
		return Boolean.parseBoolean(terms.get(name));
	}
	
	public void setTerm(String name, String value) {
		if (value == null || JetsConstant.NULL_STRING_PARAM.equals(value)) {
			return;
		}
		terms.put(name, value);
	}
	public void setTerm(String name, int value) {
		terms.put(name, String.valueOf(value));
	}
	public void setTerm(String name, double value) {
		terms.put(name, String.valueOf(value));
	}
	public void setTerm(String name, boolean value) {
		terms.put(name, String.valueOf(value));
	}
	public String removeTerm(String name) {
		return terms.remove(name);
	}

	public void addNotificationMsg(NotificationMsg msg) {
		listNotifMsg.add(msg);
	}
	public Set<NotificationMsg> getListNotificationMsg() {
		return listNotifMsg;
	}
	
	/**
	 * This function won't be used
	 * declared here just to accomodate btiLogic -> MandiriMultiCreditLogic, MerchantInitiatedPaymentLogic
	 * @return
	 */
	public SortedSet<FundFlowVO> getFundFlows() {
		return null;
	}

	public Map<String, Channel> getMapChannelTcp() {
		return mapChannelTcp;
	}

	public void setMapChannelTcp(Map<String, Channel> mapChannelTcp) {
		this.mapChannelTcp = mapChannelTcp;
	}
	
	public void addListSuccessRegBlastSms(String accountNo){
		listSuccessRegSmsBlast.add(accountNo);
	}

	public void addListSuccessUnregBlastSms(String accountNo){
		listSuccessUnregSmsBlast.add(accountNo);
	}

	public List<String> getListSuccessRegSmsBlast() {
		return listSuccessRegSmsBlast;
	}

	public void setListSuccessRegSmsBlast(List<String> listSuccessRegSmsBlast) {
		this.listSuccessRegSmsBlast = listSuccessRegSmsBlast;
	}

	public List<String> getListSuccessUnregSmsBlast() {
		return listSuccessUnregSmsBlast;
	}

	public void setListSuccessUnregSmsBlast(List<String> listSuccessUnregSmsBlast) {
		this.listSuccessUnregSmsBlast = listSuccessUnregSmsBlast;
	}

	public String getOriginalMsgFromApplet() {
		return originalMsgFromApplet;
	}

	public void setOriginalMsgFromApplet(String originalMsgFromApplet) {
		this.originalMsgFromApplet = originalMsgFromApplet;
	}

	public long getXtiArrivedTime() {
		return xtiArrivedTime;
	}

	public void setXtiArrivedTime(long xtiArrivedTime) {
		this.xtiArrivedTime = xtiArrivedTime;
	}

	public int getXtiProcess() {
		return xtiProcess;
	}

	public void setXtiProcess(int xtiProcess) {
		this.xtiProcess = xtiProcess;
	}

	public String getXtiRc() {
		return xtiRc;
	}

	public void setXtiRc(String xtiRc) {
		this.xtiRc = xtiRc;
	}

	public boolean isRouteToXti() {
		return routeToXti;
	}

	public void setRouteToXti(boolean routeToXti) {
		this.routeToXti = routeToXti;
	}

	

	
}
