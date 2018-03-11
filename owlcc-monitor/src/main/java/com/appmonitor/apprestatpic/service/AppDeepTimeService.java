package com.appmonitor.apprestatpic.service;

import com.appmonitor.apprestatpic.dao.AppMonitorInfoDao1;
import com.appmonitor.apprestatpic.po.VisitDeepResponesTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xyds on 2017/6/17.
 */
@Service(value = "appDeepTimeService")
@Transactional
public class AppDeepTimeService{
    @Autowired
    AppMonitorInfoDao1 appdao;

    public void insertData(List<VisitDeepResponesTime> visitDeepResponesTimesList) {
        appdao.save("com.appmonitor.apprestatpic.insertAppMonitorInfo",visitDeepResponesTimesList);
    }

    public void insertPicToBinary(String appId,String appName, String binary) {
        Map<String, Object> resultMap =  resultMap = new HashMap<String, Object>();
        resultMap.put("appId",appId);
        resultMap.put("appName",appName);
        resultMap.put("binary",binary);
        appdao.save("com.appmonitor.apprestatpic.insertPicBinary",resultMap);
    }
}
