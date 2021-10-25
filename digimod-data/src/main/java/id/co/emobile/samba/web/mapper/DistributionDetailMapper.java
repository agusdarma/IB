package id.co.emobile.samba.web.mapper;

import java.util.List;

import id.co.emobile.samba.web.entity.DistributionDetail;

public interface DistributionDetailMapper {
	public int createDistributionDetail(DistributionDetail detail);
	public int updateDistributionDetail(DistributionDetail detail);
	
	public List<DistributionDetail> findDistributionDetailBySysLogNo(String sysLogNo);
}
