package com.appmonitor.appstatemonitor.service;

import com.appmonitor.appstatemonitor.po.AppBean;
import com.appmonitor.appstatemonitor.dao.AppStateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
 * 说明： 应用部署配置
 * 创建人：xtn
 * 创建时间：2016-10-09
 */
@Service("appStateService")
@Transactional
public class AppStateService {

    @Autowired
    private AppStateDao appStateDao;

    /**
     * @Author xds
     * @Date 2017/7/19 17:50
     * @Param 应用监控 根据网系与组织返回应用清单
     * @Function
     **/
    public List<AppBean> getAllAppList(AppBean appbean) {
        return appStateDao.selectList("com.inspur.backstage.appmonitor.selectAppInfoByNet", appbean);
    }

    public void insertAppMonitor(Map<String, Object> resultMap) {
        appStateDao.save("com.inspur.backstage.appmonitor.insertAppMonitor", resultMap);
    }

    public void insertResponsAppMonitor(Map<String, Object> resultMap) {
        appStateDao.save("com.inspur.backstage.appmonitor.insertResponsAppMonitor", resultMap);
    }
}



