package com.appmonitor.apprestatpic.po;

/**
 * Created by xyds on 2017/6/17.
 */
public class VisitDeepResponesTime {
    private String appName;                 //应用名称
    private String AppId;                 //应用id
//    private String VisitDeepUrl;           //url串
    private Long VisitDeepTimePre;       //每一个url的时间
//    private Long VisitDeepTotalTime;    ///总时间
    private String FirstPageUrl;
//    private int UrlNum;//配置应用链接个数
//    private double AvgTime;//平均响应时间
    private String modelName; // 模块名称

    public VisitDeepResponesTime(String AppId,String appName, Long visitDeepTimePre,String firstPageUrl,String modelname) {
        this.AppId = AppId;
        this.appName = appName;
        this.VisitDeepTimePre = visitDeepTimePre;
//        this.VisitDeepTotalTime = visitDeepTotalTime;
        this.FirstPageUrl = firstPageUrl;
        this.modelName = modelname;
//        this.UrlNum = UrlNum;
//        this.AvgTime = AvgTime;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppId() {
        return AppId;
    }

    public void setAppId(String appId) {
        AppId = appId;
    }


    public Long getVisitDeepTimePre() {
        return VisitDeepTimePre;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public void setVisitDeepTimePre(Long visitDeepTimePre) {
        VisitDeepTimePre = visitDeepTimePre;
    }


    public String getFirstPageUrl() {
        return FirstPageUrl;
    }

    public void setFirstPageUrl(String firstPageUrl) {
        FirstPageUrl = firstPageUrl;
    }

    @Override
    public String toString() {
        return "VisitDeepResponesTime{" +
                "appName='" + appName + '\'' +
                ", AppId='" + AppId + '\'' +
                ", VisitDeepTimePre='" + VisitDeepTimePre + '\'' +
                ", FirstPageUrl='" + FirstPageUrl + '\'' +
                '}';
    }
}
