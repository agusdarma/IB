package id.co.emobile.samba.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import id.co.emobile.samba.web.data.param.ReportParamVO;
import id.co.emobile.samba.web.entity.DistributionHeader;

public interface DistributionHeaderMapper {
	public int createDistributionHeader(DistributionHeader header);
	public int updateDistributionHeader(DistributionHeader header);
	
	public DistributionHeader findDistributionHeaderBySysLogNo(String sysLogNo);
	public List<DistributionHeader> findDistributionHeaderByFileData(String fileData);
	public List<DistributionHeader> findDistributionHeaderByFileAssignment(String fileAssignment);
	public List<DistributionHeader> findDistributionHeaderByGroupAndStatus(
			@Param("groupId") int groupId, @Param("status") int status);
	public List<DistributionHeader> findDistributionHeaderAll(ReportParamVO paramVO);
	public List<DistributionHeader> findDistributionHeaderByGroup(ReportParamVO paramVO);
}
