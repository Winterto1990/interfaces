package com.inspur.sp.web.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * Created by xingwentao on 2017/9/6.
 */
@Mapper
public interface dataColdStorage {
    public void saveColdStorage(Map<String, Object> data);
    public void saveLog(String ID,String df);
    public void SaveAuditLogs(Map<String,Object> data);
    public long getLogId(String uuid);
    public void UpdateAuditLog(String uuid);
}
