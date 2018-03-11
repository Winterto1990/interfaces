package com.inspur.userBehavior.dao;

import com.inspur.userBehavior.data.AppCodeInfoBean;
import com.inspur.userBehavior.data.IndexValueBean;

import java.util.List;

/**
 * Created by xutongnian on 2017/6/4.
 */

public interface UserBehaviorDao {
    //获取应用信息
    public List<AppCodeInfoBean> queryAppInfo();
    //插入数据
    public void insertBehaviorValueDay(List<IndexValueBean> listIndexValue);
    public void insertBehaviorValueHourOfDay(List<IndexValueBean> listIndexValue);
    public void insertBehaviorValueCountry(List<IndexValueBean> listIndexValue);
    public void insertBehaviorValueProvince(List<IndexValueBean> listIndexValue);
    public void insertBehaviorValueCity(List<IndexValueBean> listIndexValue);
    public void insertBehaviorValuePageUrl(List<IndexValueBean> listIndexValue);
}
