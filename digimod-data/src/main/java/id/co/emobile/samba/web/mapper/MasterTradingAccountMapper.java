package id.co.emobile.samba.web.mapper;

import java.util.List;

import id.co.emobile.samba.web.data.param.MasterTradingAccountParamVO;
import id.co.emobile.samba.web.entity.MasterTradingAccount;

public interface MasterTradingAccountMapper {
	public int createMasterTradingAccount(MasterTradingAccount masterTradingAccount);

	public int updateMasterTradingAccount(MasterTradingAccount masterTradingAccount);

	public MasterTradingAccount findMasterTradingAccountById(int id);

	public List<MasterTradingAccount> findAllMasterTradingAccount();

	public List<MasterTradingAccount> findMasterTradingAccountByParam(MasterTradingAccountParamVO paramVO);

	public int countMasterTradingAccountByParam(MasterTradingAccountParamVO paramVO);

}
