package com.inspur.sp.web.BaseInfrastructure.Service;

import com.inspur.sp.web.mapper.HostMapper;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by xingwentao on 2017/7/25.
 */
@Service("hostService")
@Transactional
public class HostService {
    @Autowired
    private HostMapper hostMapper;
    public Map<String,Object> getAuditLogs(String uuid,String interfaceName,String creator,int amout,String df){
        Map<String,Object> m = new HashedMap();
        m.put("uuid", uuid);
        m.put("method",""+interfaceName);
        m.put("hostname","172");
        m.put("operator",creator);
        m.put("amount",amout);
        m.put("operate_time",df);
        return m;
    }
    public long SaveAuditLogs(String uuid,String interfaceName,String creator,int amout,String df){
        hostMapper.SaveAuditLogs(getAuditLogs(uuid,interfaceName,creator,amout,df));
        ArrayList list = hostMapper.getLogId(uuid);
        return Long.parseLong(list.get(0).toString());
    }
    public void UpdateAuditLog(String uuid){
        hostMapper.UpdateAuditLog(uuid);
    }
    public void SaveNetWorkSwitch(Map<String,Object> data){
        hostMapper.SaveNetWorkSwitch(data);
    }
    public void SaveNetWorkPort(Map<String,Object> data){
        hostMapper.SaveNetWorkPort(data);
    }
    public void saveAllHostCldData(Map<String,Object> data){
        hostMapper.saveAllHostCldData(data);
    }
    public void saveBlockStorageData(Map<String,Object> data){
        hostMapper.saveBlockStorageData(data);
    }
    public List<String> getCompanyList(){
        List<String> data = hostMapper.selectCompany();
        return data;
    }
    public void SaveHostVirtualDataCpuAllocation(Map<String,Object> data){
        hostMapper.SaveHostVirtualDataCpuAllocation(data);
    }
    public void SaveHostVirtualDataMemorydiskDetail(Map<String,Object> data){
        hostMapper.SaveHostVirtualDataMemorydiskDetail(data);
    }
    public boolean checkInt(String data){
        try {
            int num = Integer.valueOf(data);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    public void SavePhysicalCldData(Map<String,Object> data){
        hostMapper.SavePhysicalCldData(data);
    }
    public void SaveHostStorageData(Map<String,Object> data){
        hostMapper.SaveHostStorageData(data);
    }
    public void SaveNasStorageData(Map<String,Object> data){
        hostMapper.SaveNasStorageData(data);
    }
    public void SaveHostBandWidthData(Map<String,Object> data){
        hostMapper.SaveHostBandWidthData(data);
    }
    public void SaveHostVirtualBWData(Map<String,Object> data){
        hostMapper.SaveHostVirtualBWData(data);
    }
    public void SaveNetWorkData(Map<String,Object> data){
        hostMapper.SaveNetWorkData(data);
    }
    public void SaveHostData(Map<String,Object> data){
        hostMapper.SaveHostData(data);
    }
    public void SaveAlertData(Map<String,Object> data){
        hostMapper.SaveAlertData(data);
    }
}
