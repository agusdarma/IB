package id.co.emobile.samba.web.mapper;

import java.util.List;

import id.co.emobile.samba.web.entity.SourceAccount;

public interface SourceAccountMapper {
	public int createSourceAccount(SourceAccount sourceAccount);
	public int updateSourceAccount(SourceAccount sourceAccount);
	
	public List<SourceAccount> findSourceAccountAll();
	public SourceAccount findSourceAccountById(int id);
}
