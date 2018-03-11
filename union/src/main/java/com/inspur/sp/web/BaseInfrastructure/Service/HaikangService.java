package com.inspur.sp.web.BaseInfrastructure.Service;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.inspur.sp.web.mapper.videoMsgMapper;
import com.inspur.sp.web.BaseInfrastructure.Bean.deviceBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by xingwentao on 2017/10/12.
 */
@Service("haikangService")
@Transactional
public class HaikangService {
    @Autowired
    private videoMsgMapper videoMsgMappers;
    public List<String> getCompanyList(){
        List<String> data = videoMsgMappers.selectCompany();
        return data;
    }

    public void saveDevice(deviceBean deviceBeans){
        videoMsgMappers.saveDevice(deviceBeans);
    }

    public void savePosition(Map<String,Object> result){
        videoMsgMappers.savePosition(result);
    }

    public void saveQualityDiagnosis(Map<String,Object> result){
        videoMsgMappers.saveQualityDiagnosis(result);
    }

    public void SaveAuditLog(String port,String info){
        videoMsgMappers.SaveAuditLog(getAuditLog(port,info));
    }
    public Map<String,Object> getAuditLog(String interfaceName, String desc){
        Map<String,Object> m = new HashedMap();
        m.put("id", UUID.randomUUID().toString());
        m.put("method","com.inspur.sp.web.Infrastructures."+interfaceName);
        m.put("description",desc);
        m.put("request_ip","");
        m.put("creator","haikang");
        m.put("remark","海康接口调用 数据存储");
        m.put("create_time",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return m;
    }
}
