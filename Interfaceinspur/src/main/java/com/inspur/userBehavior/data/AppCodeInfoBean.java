package com.inspur.userBehavior.data;

import java.io.Serializable;

/**
 * Created by xutongnian on 2017/5/27.
 * 根据表获取应用的code和国双提供的请求代码
 */
public class AppCodeInfoBean implements Serializable {
    private String AppCode;             //应用编码
    private int AppGuoShuangCode;   //国双提供的应用编码

    public String getAppCode() {
        return AppCode;
    }

    public void setAppCode(String appCode) {
        AppCode = appCode;
    }

    public int getAppGuoShuangCode() {
        return AppGuoShuangCode;
    }

    public void setAppGuoShuangCode(int appGuoShuangCode) {
        AppGuoShuangCode = appGuoShuangCode;
    }

    @Override
    public String toString() {
        return "AppCodeInfoBean{" +
                "AppCode='" + AppCode + '\'' +
                ", AppGuoShuangCode=" + AppGuoShuangCode +
                '}';
    }
}
