package com.inspur.userBehavior.service;

import com.inspur.userBehavior.dao.UserBehaviorDao;
import com.inspur.userBehavior.data.AppCodeInfoBean;
import com.inspur.userBehavior.data.IndexValueBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by xutongnian on 2017/5/27.
 */
@Service(value = "userBehaviorService")
@Transactional
public class UserBehaviorService implements UserBehaviorInterface{

    @Autowired
    UserBehaviorDao userBehaviorDao;
    /**
     *  查询应用对应的编号（国双提供的编号）
     * @return
     */
    public List<AppCodeInfoBean> queryAppInfo() {
        List<AppCodeInfoBean> AppCodeInfoList = userBehaviorDao.queryAppInfo();
        return AppCodeInfoList;
    }

    /**
     * 用户行为分析数据
     * @param listUserBehaviorData
     * @param Dimensionality
     * @return
     */

    public int insertUserBehaviorData(List<IndexValueBean> listUserBehaviorData, String Dimensionality) {
        if (Dimensionality.equals("Day")){
            userBehaviorDao.insertBehaviorValueDay(listUserBehaviorData);
        }
        if (Dimensionality.equals("HourOfDay")){
            userBehaviorDao.insertBehaviorValueHourOfDay(listUserBehaviorData);
        }
        if (Dimensionality.equals("Country")){
            userBehaviorDao.insertBehaviorValueCountry(listUserBehaviorData);
        }
        if (Dimensionality.equals("Province")){
            userBehaviorDao.insertBehaviorValueProvince(listUserBehaviorData);
        }
        if (Dimensionality.equals("City")){
            userBehaviorDao.insertBehaviorValueCity(listUserBehaviorData);
        }
        if (Dimensionality.equals("PageUrl")){
            userBehaviorDao.insertBehaviorValuePageUrl(listUserBehaviorData);
        }
        return 0;
    }
}
