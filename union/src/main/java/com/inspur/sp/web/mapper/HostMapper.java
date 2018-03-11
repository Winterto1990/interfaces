package com.inspur.sp.web.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xingwentao on 2017/7/25.
 */
@Mapper
public interface HostMapper {
    void saveAllHostCldData(Map<String,Object> data);
    void saveBlockStorageData(Map<String,Object> data);
    List<String> selectCompany();
    public void SaveHostVirtualDataCpuAllocation(Map<String,Object> data);
    public void SaveHostVirtualDataMemorydiskDetail(Map<String,Object> data);
    public void SavePhysicalCldData(Map<String,Object> data);
    public void SaveHostStorageData(Map<String,Object> data);
    public void SaveNasStorageData(Map<String,Object> data);
    public void SaveHostBandWidthData(Map<String,Object> data);
    public void SaveHostVirtualBWData(Map<String,Object> data);
    public void SaveNetWorkData(Map<String,Object> data);
    public void SaveHostData(Map<String,Object> data);
    public void SaveAlertData(Map<String,Object> data);
    public void SaveAuditLogs(Map<String,Object> data);
    public ArrayList getLogId(String uuid);
    public void UpdateAuditLog(String uuid);
    public void SaveNetWorkSwitch(Map<String,Object> data);
    public void SaveNetWorkPort(Map<String,Object> data);
}
