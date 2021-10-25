package id.co.emobile.samba.web.service;

import java.util.Date;

import id.co.emobile.samba.web.data.AccStatementVO;
import id.co.emobile.samba.web.data.CheckAccountVO;
import id.co.emobile.samba.web.data.SourceAccountVO;
import id.co.emobile.samba.web.entity.DistributionDetail;
import id.co.emobile.samba.web.entity.DistributionHeader;

public interface BaseTrxService {
	
	public String getServiceName();

	public CheckAccountVO checkAccountForNo(String accountNo);
	
	public void checkBalanceForAccount(SourceAccountVO sourceAccount);

	public AccStatementVO checkStatementForAccount(SourceAccountVO sourceAccount, Date startDate, Date endDate);
	
	public void sendTransactionToHost(DistributionHeader header, DistributionDetail detail);
	
	public void sendTransactionToHostPajak(DistributionHeader header, DistributionDetail detail);
	
}
