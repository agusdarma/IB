package id.co.emobile.samba.web.mapper;

import id.co.emobile.samba.web.data.param.BankParamVO;
import id.co.emobile.samba.web.entity.Bank;

import java.util.List;

public interface BankMapper {
	public int createBank(Bank bank);
	public int updateBank(Bank bank);

	public Bank findBankByBankCode(String bankCode);
	public Bank findBankById(int id);
	public List<Bank> findAllBank();
	public List<Bank> findBankByParam(BankParamVO paramVO);
	public int countBankByParam(BankParamVO paramVO);
	public Bank findBankByShowOrder(int showOrder);
	
}
