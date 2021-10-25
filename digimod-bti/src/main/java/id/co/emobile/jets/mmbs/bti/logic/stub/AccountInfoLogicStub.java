//package id.co.emobile.jets.mmbs.bti.logic.stub;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.emobile.jets.mmbs.lib.data.TermConstant;
//import com.emobile.jets.mmbs.lib.data.TransactionTO;
//import com.emobile.jets.mmbs.lib.data.WebAccountResponseVO;
//import com.emobile.jets.mmbs.lib.service.JetsException;
//import com.emobile.jets.mmbs.lib.util.ResultCode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import id.co.emobile.jets.mmbs.bti.BtiIsoDelegateAgent;
//import id.co.emobile.jets.mmbs.bti.iso.IsoMsg;
//import id.co.emobile.jets.mmbs.bti.logic.HostBtiLogic;
//
//public class AccountInfoLogicStub extends HostBtiLogic {
//	private static final Logger LOG = LoggerFactory.getLogger(AccountInfoLogicStub.class);
//
//	private int delay = 0;
//	
//	private ObjectMapper mapper;
//	
//	@Override
//	public void solve(BtiIsoDelegateAgent agent, TransactionTO task) 
//			throws JetsException, IOException {
//		if (delay > 0) {
//			try {
//				Thread.sleep(delay);
//			} catch (InterruptedException e) {
//				LOG.error("Sleep deprivation!");
//			}
//		}
//		processSuccessResponse(task, null);
//	}
//
//	@Override
//	protected Logger getLogger() {
//		return LOG;
//	}
//
//	@Override
//	protected void processSuccessResponse(TransactionTO task, IsoMsg rspIsoMsg)
//			throws JetsException {
//		task.setTerm(TermConstant.CURR, "IDR");
//		task.setResultCode(ResultCode.SUCCESS_CODE);
//		
//		List<WebAccountResponseVO> accList = new ArrayList<WebAccountResponseVO>();
//		
//		WebAccountResponseVO accResponseVO = new WebAccountResponseVO();
//		
//		// acc option: 0=Option card no, 1=Option acc no
//		if (task.getTermAsInteger(TermConstant.ACC_OPTION) == 1) {
//			accResponseVO = new WebAccountResponseVO();
//			accResponseVO.setAccountNo(task.getTerm(TermConstant.CARD_NUMBER));
////			accResponseVO.setAccountType("10");	
////			accResponseVO.setAccIndex(1);
//			accResponseVO.setRemarks("Willy");
//			
//			accList.add(accResponseVO);
//		} else {
//			accResponseVO = new WebAccountResponseVO();
//			accResponseVO.setAccountNo("1230001289080");
////			accResponseVO.setAccountType("10");	
////			accResponseVO.setAccIndex(1);
//			accResponseVO.setRemarks("Willy A");
//			
//			accList.add(accResponseVO);
//
//			accResponseVO = new WebAccountResponseVO();
//			accResponseVO.setAccountNo("1250005279870");
////			accResponseVO.setAccountType("20");	
////			accResponseVO.setAccIndex(2);
//			accResponseVO.setRemarks("Willy B");
//			
//			accList.add(accResponseVO);
//
//			accResponseVO = new WebAccountResponseVO();
//			accResponseVO.setAccountNo("1250000980931");
////			accResponseVO.setAccountType("10");	
////			accResponseVO.setAccIndex(3);
//			accResponseVO.setRemarks("Willy C");
//			
//			accList.add(accResponseVO);
//		}
//		
//		mapper = new ObjectMapper();
//		
//		try {
//			task.setTerm(TermConstant.ACC_LIST, mapper.writeValueAsString(accList));
//		} catch (IOException e) {
//			LOG.error("Error Creating account list: ", e);
//		}
//		
//		task.setTerm(TermConstant.ACC_NUMBER, task.getTerm(TermConstant.CARD_NUMBER));
//		task.setTerm(TermConstant.ACC_TYPE, "10");
//		task.setTerm(TermConstant.CARD_TYPE, "");
//		task.setTerm(TermConstant.ACCOUNT_NAME, "Willy");
//	}
//
//	@Override
//	protected IsoMsg buildIso(TransactionTO task) throws JetsException {
//		return null;
//	}
//
//	public void setDelay(int delay) {
//		this.delay = delay;
//	}
//}
