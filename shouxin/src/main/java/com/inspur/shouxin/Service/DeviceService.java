package com.inspur.shouxin.Service;

import com.inspur.shouxin.Interface.DeviceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service("deviceService")
@Transactional
public class DeviceService {
    @Autowired
    private DeviceInterface device;
    public Map<String,Object> getAuditLogs(String uuid, String interfaceName, String creator,int amout,String df){
        Map<String,Object> m = new HashMap<>();
        m.put("uuid", uuid);
        m.put("method",""+interfaceName);
        m.put("hostname","172");
        m.put("operator",creator);
        m.put("amount",amout);
        m.put("operate_time",df);
        return m;
    }
    public List<String> getCompanyList(){
        List<String> data = device.selectCompany();
        return data;
    }
    public long SaveAuditLogs(String uuid,String interfaceName,String creator,int amout,String df){
        device.SaveAuditLogs(getAuditLogs(uuid,interfaceName,creator,amout,df));
        ArrayList list = device.getLogId(uuid);
        return Long.parseLong(list.get(0).toString());
    }
    public void UpdateAuditLog(String uuid){
        device.UpdateAuditLog(uuid);
    }
    public void SaveNetWorkSwitch(Map<String,Object> data){
        device.SaveNetWorkSwitch(data);
        device.SaveNetWorkSwitchHistory(data);
    }
    public void SaveNetWorkPort(Map<String,Object> data){
        device.SaveNetWorkPort(data);
        device.SaveNetWorkPortHistory(data);
    }
    public void SaveHostData(Map<String,Object> data){
        device.SaveHostData(data);
        device.SaveHostDataHistory(data);
    }
    public void SaveAlertData(Map<String,Object> data){
        device.SaveAlertData(data);
    }
}
