package com.inspur.hksso_union_hy.Service;

import com.inspur.hksso_union_hy.Mapper.dataColdStorage;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by xingwentao on 2017/9/6.
 */
@Service("appTaskService")
@Transactional
public class appTaskService {
    @Autowired
    public dataColdStorage dataColdStorage;
    public void saveColdStorage(Map<String,Object> data){
        dataColdStorage.saveColdStorage(data);
    }
    public Map<String,Object> getAuditLogs(String uuid,String interfaceName,String creator){
        Map<String,Object> m = new HashedMap();
        m.put("uuid", uuid);
        m.put("method",""+interfaceName);
        m.put("hostname","172");
        m.put("operator",creator);
        m.put("operate_time",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return m;
    }
    public long SaveAuditLogs(String uuid,String interfaceName,String creator){
        dataColdStorage.SaveAuditLogs(getAuditLogs(uuid,interfaceName,creator));
        return dataColdStorage.getLogId(uuid);
    }
    public void UpdateAuditLog(String uuid){
        dataColdStorage.UpdateAuditLog(uuid);
    }

    public void saveLog(String ID,String df){
        dataColdStorage.saveLog(ID,df);
    }
}
