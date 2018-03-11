package com.inspur.huayu.cocall.Mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface commonMap {
    public List<String> selectCompany();
    public Map<String,Object> getSearchId(String id);
    public List<Map> getProperties(String id);
    public void SaveAuditLogs(Map<String,Object> data);
    public long getLogId(String uuid);
    public void saveData(String sql);
    public void UpdateAuditLog(String uuid);
    public List<String> getCode();
}
