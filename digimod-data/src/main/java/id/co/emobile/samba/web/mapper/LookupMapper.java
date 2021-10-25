package id.co.emobile.samba.web.mapper;

import id.co.emobile.samba.web.data.param.LookupDataParamVO;
import id.co.emobile.samba.web.entity.Lookup;

import java.util.List;

public interface LookupMapper {

	public List<Lookup> findLookupAll();

	public List<Lookup> findAllTrxCode();

	public List<Lookup> findAllTrxCodeCW();

	public List<Lookup> findTrxGroup();

	public List<Lookup> findTrxCodeByTrxGroup(String trxGroup);

	public List<Lookup> findAllTpda();

	public List<Lookup> findC81();

	public List<Lookup> findC81Edit();

	public void updateLookupData(Lookup lookupData);

	public void updateWholeLookupData(Lookup lookupData);

	public void insertLookupData(Lookup lookupData);

	public List<Lookup> findLookupByParam(LookupDataParamVO paramVO);

	public int countLookupByParam(LookupDataParamVO paramVO);
}
