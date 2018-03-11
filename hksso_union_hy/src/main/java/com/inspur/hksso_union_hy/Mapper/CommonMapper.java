package com.inspur.hksso_union_hy.Mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by xingwentao on 2017/10/13.
 */
@Mapper
public interface CommonMapper {
    List<String> selectCompany();
    public void SaveAuditLog(Map<String, Object> data);
    public void SaveAuditLogs(Map<String, Object> data);
    public long getLogId(String uuid);
    public Map<String,Object> getSearchId(String id);
    public List<Map> getProperties(String id);
    public void saveData(String sql);
    public void UpdateAuditLog(String uuid);
}
