package com.inspur.shouxin.Interface;

import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface DeviceInterface {
    public List<String> selectCompany();
    public void SaveNetWorkSwitch(Map<String,Object> data);
    public void SaveNetWorkSwitchHistory(Map<String,Object> data);
    public void SaveNetWorkPort(Map<String,Object> data);
    public void SaveNetWorkPortHistory(Map<String,Object> data);
    public void SaveHostData(Map<String,Object> data);
    public void SaveHostDataHistory(Map<String,Object> data);
    public void SaveAlertData(Map<String,Object> data);
    public void SaveAuditLogs(Map<String,Object> data);
    public ArrayList getLogId(String uuid);
    public void UpdateAuditLog(String uuid);
}
