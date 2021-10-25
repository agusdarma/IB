package id.co.emobile.samba.web.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import id.co.emobile.samba.web.entity.ServerPerformance;

public interface ServerPerformanceMapper {

	public void insertServerPerformance(ServerPerformance serverPerformance);
	public int findTotalTrx(@Param("tableName") String tableName, @Param("createdOn") Date createdOn);
}
