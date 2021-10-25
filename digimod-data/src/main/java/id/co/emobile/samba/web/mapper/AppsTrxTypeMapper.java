package id.co.emobile.samba.web.mapper;

import java.util.List;

import id.co.emobile.samba.web.data.param.AppsTrxTypeParamVO;
import id.co.emobile.samba.web.entity.AppsTrxType;

public interface AppsTrxTypeMapper {
	public int createAppsTrxType(AppsTrxType appsTrxType);
	public int updateAppsTrxType(AppsTrxType appsTrxType);

	public AppsTrxType findAppsTrxTypeByTrxName(String trxName);
	public AppsTrxType findAppsTrxTypeById(int id);
	public List<AppsTrxType> findAllAppsTrxType();
	public List<AppsTrxType> findAppsTrxTypeByParam(AppsTrxTypeParamVO paramVO);
	public int countAppsTrxTypeByParam(AppsTrxTypeParamVO paramVO);
	
}
