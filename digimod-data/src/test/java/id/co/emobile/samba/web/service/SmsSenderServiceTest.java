package id.co.emobile.samba.web.service;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import id.co.emobile.samba.web.entity.DistributionDetail;
import id.co.emobile.samba.web.utils.CipherUtils;
import id.co.emobile.samba.web.utils.CommonUtil;

public class SmsSenderServiceTest {
	private static final Logger LOG = LoggerFactory.getLogger(SmsSenderServiceTest.class);
	
	@Test
	public void testValidateDataCheck() {
//		DistributionDetail detail = new DistributionDetail();
//		detail.setSysLogNo("191021A000004"); //"190802A000006");
//		detail.setDetailId(4);
//		detail.setPhoneNo("081341006693"); // 08124242828");
//		detail.setAccountNo("0010107113036"); //0010205056326");
//		detail.setMoneyValue(5980000);
//		String dataCheck = detail.getSysLogNo() + "_" + detail.getDetailId() + "_" + detail.getPhoneNo() 
//			+ "_" + detail.getAccountNo() + "_" + detail.getMoneyValue();
//		String check = CommonUtil.toHexString(CipherUtils.hashSHA256(dataCheck));
//		//OLD VALUE: A15DDF1E716EAC1C4DF6326E5E61E37A8D5F5E76FBD66857D0E630E765E6F33D
//		//OLD VALUE: A7912101BF4FB4F4A091332B8B434D77A046B8A5082214B68067A2E9BA19F96A
//		//OLD VALUE: 66CCB9165982897B026A56E26C21AA57B8F3838F7981530032A053BE42DF8E45
//		LOG.debug("Check: " + check);
		double lot = Double.parseDouble("0.05"); // 0.01
		double commissionDollar= new Double(4);//first way.
		double commissionFinal = commissionDollar/100;
		double lotFinal = lot/0.01;
		double total = lotFinal * commissionFinal;
		String totalCommission = Double.toString(total);
		LOG.debug("total: " + totalCommission + " $");
	}
}
