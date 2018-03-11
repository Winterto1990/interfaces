package com.inspur.userBehavior.data;

import java.io.Serializable;

/**
 * Created by xutongnian on 2017/5/26.
 * 指标请求返回值解析
 */
public class IndexValueBean implements Serializable {

    private String AppCode;             //应用编码
    private String Dimensionality;          //维度
    private String Visits;                   //访问量
    private String Visitors;                 //独立访客数
    private String ApproxDistinctIPCount;  //独立IP数
    private String AvgLoadingDuration;     //平均加载时长
    private String PageRefreshRate;        // 平均页面刷新率
    private String BounceRate;              //跳出率
    private String AvgSessionPageViews;    //平均页面浏览量
    private String AvgPageViewDepth;       //平均页面访问深度
    private String AvgPageDuration;        //平均停留时间
    private String SessionMouseClickCount;//鼠标点击率

    private String SessionPageViews;       //会话页面浏览量

    public String getAppCode() {
        return AppCode;
    }

    public void setAppCode(String appCode) {
        AppCode = appCode;
    }

    public String getDimensionality() {
        return Dimensionality;
    }

    public void setDimensionality(String dimensionality) {
        Dimensionality = dimensionality;
    }

    public String getVisits() {
        return Visits;
    }

    public void setVisits(String visits) {
        Visits = visits;
    }

    public String getVisitors() {
        return Visitors;
    }

    public void setVisitors(String visitors) {
        Visitors = visitors;
    }

    public String getApproxDistinctIPCount() {
        return ApproxDistinctIPCount;
    }

    public void setApproxDistinctIPCount(String approxDistinctIPCount) {
        ApproxDistinctIPCount = approxDistinctIPCount;
    }

    public String getAvgLoadingDuration() {
        return AvgLoadingDuration;
    }

    public void setAvgLoadingDuration(String avgLoadingDuration) {
        AvgLoadingDuration = avgLoadingDuration;
    }

    public String getPageRefreshRate() {
        return PageRefreshRate;
    }

    public void setPageRefreshRate(String pageRefreshRate) {
        PageRefreshRate = pageRefreshRate;
    }

    public String getBounceRate() {
        return BounceRate;
    }

    public void setBounceRate(String bounceRate) {
        BounceRate = bounceRate;
    }

    public String getAvgSessionPageViews() {
        return AvgSessionPageViews;
    }

    public void setAvgSessionPageViews(String avgSessionPageViews) {
        AvgSessionPageViews = avgSessionPageViews;
    }

    public String getAvgPageViewDepth() {
        return AvgPageViewDepth;
    }

    public void setAvgPageViewDepth(String avgPageViewDepth) {
        AvgPageViewDepth = avgPageViewDepth;
    }

    public String getAvgPageDuration() {
        return AvgPageDuration;
    }

    public void setAvgPageDuration(String avgPageDuration) {
        AvgPageDuration = avgPageDuration;
    }

    public String getSessionMouseClickCount() {
        return SessionMouseClickCount;
    }

    public void setSessionMouseClickCount(String sessionMouseClickCount) {
        SessionMouseClickCount = sessionMouseClickCount;
    }

    public String getSessionPageViews() {
        return SessionPageViews;
    }

    public void setSessionPageViews(String sessionPageViews) {
        SessionPageViews = sessionPageViews;
    }

    @Override
    public String toString() {
        return "IndexValueBean{" +
                "AppCode='" + AppCode + '\'' +
                ", Dimensionality='" + Dimensionality + '\'' +
                ", Visits='" + Visits + '\'' +
                ", Visitors='" + Visitors + '\'' +
                ", ApproxDistinctIPCount='" + ApproxDistinctIPCount + '\'' +
                ", AvgLoadingDuration='" + AvgLoadingDuration + '\'' +
                ", PageRefreshRate='" + PageRefreshRate + '\'' +
                ", BounceRate='" + BounceRate + '\'' +
                ", AvgSessionPageViews='" + AvgSessionPageViews + '\'' +
                ", AvgPageViewDepth='" + AvgPageViewDepth + '\'' +
                ", AvgPageDuration='" + AvgPageDuration + '\'' +
                ", SessionMouseClickCount='" + SessionMouseClickCount + '\'' +
                ", SessionPageViews='" + SessionPageViews + '\'' +
                '}';
    }
}
