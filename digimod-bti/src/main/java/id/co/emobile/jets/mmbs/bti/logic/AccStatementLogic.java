package id.co.emobile.jets.mmbs.bti.logic;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import id.co.emobile.jets.mmbs.bti.BtiIsoDelegateAgent;
import id.co.emobile.jets.mmbs.bti.iso.HostIsoMsgFactory;
import id.co.emobile.jets.mmbs.bti.iso.IsoMsg;
import id.co.emobile.samba.web.data.ResultCode;
import id.co.emobile.samba.web.data.TermConstant;
import id.co.emobile.samba.web.data.TransactionTO;
import id.co.emobile.samba.web.service.JetsException;

public class AccStatementLogic extends HostBtiLogic {
	private static final Logger LOG = LoggerFactory.getLogger(AccStatementLogic.class);

//	@Autowired
//	private AppsMessageService messageService;
	
	@Override
	protected Logger getLogger() {
		return LOG;
	}
	
	@Override
	public void solve(BtiIsoDelegateAgent agent, TransactionTO task) 
			throws JetsException, IOException {
//		if (task.getState() == StateConstant.ACC_BAL_INQ) {
//			String srac = task.getTerm(TermConstant.SRAC);
//			if (srac.length() < 3) {
//				task.setTerm(TermConstant.ACC_INDEX, srac);
//				srac = getDefaultAccount(agent, task);
//				task.setTerm(TermConstant.SRAC, srac);
//			} else {
//				task.setResultCode(ResultCode.SUCCESS_CODE);
//			}
//		}
//		else {
			super.solve(agent, task);
//		}
	}

	@Override
	protected IsoMsg buildIso(TransactionTO task) throws JetsException {
		return HostIsoMsgFactory.createAccStatement(this, task);
	}
	
//	public static void main(String[] args) {
//		String a = "0082311839938001020519638420150930-270,00-C*20150831-279,00-C*20150731-280,00-C*20150630-270,00-C*20150529-279,00-C*";
//		String contentData = a.substring(26, a.length());
//		StringBuffer sb = new StringBuffer();		
//		for (String retval: contentData.split("[*]", 5)){
//				retval = retval.replaceAll("[*]", "");
//				String date = retval.substring(0, 8);
//				String year= date.substring(0,4);
//				String month= date.substring(4,6);
//				String day= date.substring(6,8);
//				String d = day+'/'+month+'/'+year+retval.substring(8, retval.length());				
//				sb.append(d).append("\n");
//	      }
//        System.out.println(sb.toString());
//	}
//	061 : 0082311839938001020201722014/08/2016     2,000,000.00 K .SETORAN T*14/08/2016       250,000.00 K .SETORAN T*14/08/2016       150,000.00 K .SETORAN T*14/08/2016       100,000.00 K .SETORAN T*14/08/2016       200,000.00 K .SETORAN T
//	061 : 0082311839938002020502183001/09/2016        50,000.00 K (ATMB)TRFC*31/08/2016            93.00 K BNG TAB 8
	@Override
	protected void processSuccessResponse(TransactionTO task, IsoMsg rspIsoMsg) throws JetsException {
		String bit61 = rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA);
		if(bit61.length() > 26){
			String contentData = bit61.substring(26, bit61.length());
			StringBuffer trxHistory = new StringBuffer();		
//			for (String retval: contentData.split("[*]", 5)){
//					retval = retval.replaceAll("[*]", "");
//					String date = retval.substring(0, 8);
//					String year= date.substring(0,4);
//					String month= date.substring(4,6);
//					String day= date.substring(6,8);
//					String d = day+'/'+month+'/'+year+retval.substring(8, retval.length());				
//					trxHistory.append(d).append("\n");
//		    }	
			for (String retval: contentData.split("[*]", 5)){
				retval = retval.replaceAll("[*]", "");
				int lastIdx = retval.indexOf("K");
				if(lastIdx>0){
					retval = retval.substring(0,lastIdx+1);
				}else{
					lastIdx = retval.indexOf("D");
					if(lastIdx>0){
						retval = retval.substring(0,lastIdx+1);
					}
				}							
				trxHistory.append(retval).append("\n");
			}
			task.setTerm(TermConstant.TRX_HISTORY, trxHistory.toString());
		}
		else{
			task.setTerm(TermConstant.TRX_HISTORY, "Maaf, mutasi terakhir anda kosong");
		}
		//System.out.print("Response : " + rspIsoMsg);
//		String bit62 = rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA_2);
//		LOG.debug("[#" + task.getMsgLogNo() + "] Response bit62: " + bit62);
//		
//		//if no transaction, bit62, start with word "Mutasi"
//		if (!bit62.startsWith("Mutasi")) {
//			StringBuilder sb = new StringBuilder();
//				//format: dd/mm/yy(8) D/C(1) Amount(18)
//			while (bit62.length() > 27) {
//				String amntStr = bit62.substring(9, 27).trim();
//				Number amnt = CommonUtil.parseMoneyIndo(amntStr.substring(3));
//
//				sb.append("\n")//CR will be converted to Carriage return
//				.append(bit62.substring(0,8)) //date
//				.append(" ")
//				.append(amntStr.substring(0,3)) //Rp.
//				.append(CommonUtil.formatMoneyIndo(Double.valueOf(amnt.toString()), 2)) //999.999,99
//				.append(" ")
//				.append(bit62.substring(8,9)); //D/C;
//				bit62 = bit62.substring(27);
//			}
//			task.setTerm(TermConstant.TRX_HISTORY, sb.toString());
//		} else {
//			task.setTerm(TermConstant.TRX_HISTORY, bit62.substring(0, bit62.length() - 2).trim());
//		}
//		// added for MTE
//		String trxHistoryMsg = task.getTerm(TermConstant.TRX_HISTORY);
//		trxHistoryMsg = trxHistoryMsg.trim().replaceAll("\n", "\\\\x0d");
//		task.setMessageOutput(trxHistoryMsg);
	}
	
//	public static void main(String[] args) 
//	{
//		String m = "00081210743731170204006929110/07/2015  5,000,000.00 D, 10/07/2015 100,000,000.00 K, 10/07/2015  70,000.00 D";
//		String contentData =  m.substring(27, m.length());
//		System.out.println(m.substring(0, 27));
//		System.out.println(printMatches(contentData, "(\\W|^)(D|K)(\\W|$)"));
//	}
	
	private static String printMatches(String text, String regex) {
		int x = 0;
		int idx = 0;
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(text);
	    // Check all occurrences
	    StringBuilder sb = new StringBuilder();
	    while (matcher.find()) {
	    	int flag = matcher.end();
	    	if(idx == text.length())
    			break;
//  		  	if(text.substring(idx, flag).contains(","))
//  		  		flag-=1; //buat hapus comma
	    	if(x == 0){
	    		  sb.append(text.substring(0, flag)).append("\n"); // flag di cetak sampe sblm tanda koma
	    		  idx+= text.substring(0, matcher.end()).length(); // di count sampe comma
	    	}
	    	else{
	    		sb.append(text.substring(idx, flag).trim()).append("\n");
	    		idx+= text.substring(idx, matcher.end()).length();
	    	}
//	        System.out.print("Start index: " + matcher.start());
//	        System.out.print(" End index: " + matcher.end());
//	        System.out.println(" Found: " + matcher.group());
	        x++;
	    }
	    return sb.toString();
	}

	@Override
	protected void processFailedResponse(TransactionTO task, IsoMsg rspIsoMsg)
			throws JetsException {
		String bit39 = rspIsoMsg.getItem(IsoMsg.RESPONSE_CODE);
		if(ResultCode.MUTASI_EMPTY_RC.equals(bit39)){
			task.setTerm(TermConstant.TRX_HISTORY, "Maaf, mutasi terakhir anda kosong");
		}
	}

	

}
