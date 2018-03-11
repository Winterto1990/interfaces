package com.inspur.userBehavior.service;

import com.inspur.userBehavior.data.AppCodeInfoBean;
import com.inspur.userBehavior.data.IndexValueBean;

import java.util.List;

/**
 * Created by xutongnian on 2017/5/27.
 */
public interface UserBehaviorInterface {
    public List<AppCodeInfoBean> queryAppInfo();
    public int insertUserBehaviorData(List<IndexValueBean> listUserBehaviorData, String Dimensionality);
}
