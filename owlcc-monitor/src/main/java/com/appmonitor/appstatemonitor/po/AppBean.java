package com.appmonitor.appstatemonitor.po;

import java.io.Serializable;

/**
 * Created by xuds on 2017/7/19.
 */
public class AppBean implements Serializable {
    /**
     * 解决转json出错的问题
     */
    public AppBean() {
    }

    private String itmAppId;
    private String itmAppCode;
    private String itmAppName;
    private String itmAppDesc;
    private String itmAppDept;
    private String itmAppCourtcode;
    private String itmNetgroupId;
    private String itmAppcatagoryId;
    private String itmAppVendor;
    private String itmAppUptime;
    private String itmAppVersion;
    private String itmAppMaintenanceId;
    private String itmAppOrder;
    private String itmAppNetid;
    private String itmAppIconsrc;
    private String itmAppSystemurl;
    private String organId;
    private String isGlobal;
    private String itmAppPid;

    private String itmAppIp;//应用所在服务器IP
    private String itmAppPort;//应用所在服务器端口
    private String itmAppWebRoot;//应用访问路径根目录
    private String itmAppCatagoryName;//应用所属网系

    public String getItmAppId() {
        return itmAppId;
    }

    public void setItmAppId(String itmAppId) {
        this.itmAppId = itmAppId;
    }

    public String getItmAppCode() {
        return itmAppCode;
    }

    public void setItmAppCode(String itmAppCode) {
        this.itmAppCode = itmAppCode;
    }

    public String getItmAppName() {
        return itmAppName;
    }

    public void setItmAppName(String itmAppName) {
        this.itmAppName = itmAppName;
    }

    public String getItmAppDesc() {
        return itmAppDesc;
    }

    public void setItmAppDesc(String itmAppDesc) {
        this.itmAppDesc = itmAppDesc;
    }

    public String getItmAppDept() {
        return itmAppDept;
    }

    public void setItmAppDept(String itmAppDept) {
        this.itmAppDept = itmAppDept;
    }

    public String getItmAppCourtcode() {
        return itmAppCourtcode;
    }

    public void setItmAppCourtcode(String itmAppCourtcode) {
        this.itmAppCourtcode = itmAppCourtcode;
    }

    public String getItmNetgroupId() {
        return itmNetgroupId;
    }

    public void setItmNetgroupId(String itmNetgroupId) {
        this.itmNetgroupId = itmNetgroupId;
    }

    public String getItmAppcatagoryId() {
        return itmAppcatagoryId;
    }

    public void setItmAppcatagoryId(String itmAppcatagoryId) {
        this.itmAppcatagoryId = itmAppcatagoryId;
    }

    public String getItmAppVendor() {
        return itmAppVendor;
    }

    public void setItmAppVendor(String itmAppVendor) {
        this.itmAppVendor = itmAppVendor;
    }

    public String getItmAppUptime() {
        return itmAppUptime;
    }

    public void setItmAppUptime(String itmAppUptime) {
        this.itmAppUptime = itmAppUptime;
    }

    public String getItmAppVersion() {
        return itmAppVersion;
    }

    public void setItmAppVersion(String itmAppVersion) {
        this.itmAppVersion = itmAppVersion;
    }

    public String getItmAppMaintenanceId() {
        return itmAppMaintenanceId;
    }

    public void setItmAppMaintenanceId(String itmAppMaintenanceId) {
        this.itmAppMaintenanceId = itmAppMaintenanceId;
    }

    public String getItmAppOrder() {
        return itmAppOrder;
    }

    public void setItmAppOrder(String itmAppOrder) {
        this.itmAppOrder = itmAppOrder;
    }

    public String getItmAppNetid() {
        return itmAppNetid;
    }

    public void setItmAppNetid(String itmAppNetid) {
        this.itmAppNetid = itmAppNetid;
    }

    public String getItmAppIconsrc() {
        return itmAppIconsrc;
    }

    public void setItmAppIconsrc(String itmAppIconsrc) {
        this.itmAppIconsrc = itmAppIconsrc;
    }

    public String getItmAppSystemurl() {
        return itmAppSystemurl;
    }

    public void setItmAppSystemurl(String itmAppSystemurl) {
        this.itmAppSystemurl = itmAppSystemurl;
    }

    public String getOrganId() {
        return organId;
    }

    public void setOrganId(String organId) {
        this.organId = organId;
    }

    public String getIsGlobal() {
        return isGlobal;
    }

    public void setIsGlobal(String isGlobal) {
        this.isGlobal = isGlobal;
    }

    public String getItmAppPid() {
        return itmAppPid;
    }

    public void setItmAppPid(String itmAppPid) {
        this.itmAppPid = itmAppPid;
    }

    public String getItmAppIp() {
        return itmAppIp;
    }

    public void setItmAppIp(String itmAppIp) {
        this.itmAppIp = itmAppIp;
    }

    public String getItmAppPort() {
        return itmAppPort;
    }

    public void setItmAppPort(String itmAppPort) {
        this.itmAppPort = itmAppPort;
    }

    public String getItmAppWebRoot() {
        return itmAppWebRoot;
    }

    public void setItmAppWebRoot(String itmAppWebRoot) {
        this.itmAppWebRoot = itmAppWebRoot;
    }

    public String getItmAppCatagoryName() {
        return itmAppCatagoryName;
    }

    public void setItmAppCatagoryName(String itmAppCatagoryName) {
        this.itmAppCatagoryName = itmAppCatagoryName;
    }

    @Override
    public String toString() {
        return "App{" +
                "itmAppId='" + itmAppId + '\'' +
                ", itmAppCode='" + itmAppCode + '\'' +
                ", itmAppName='" + itmAppName + '\'' +
                ", itmAppDesc='" + itmAppDesc + '\'' +
                ", itmAppDept='" + itmAppDept + '\'' +
                ", itmAppCourtcode='" + itmAppCourtcode + '\'' +
                ", itmNetgroupId='" + itmNetgroupId + '\'' +
                ", itmAppcatagoryId='" + itmAppcatagoryId + '\'' +
                ", itmAppVendor='" + itmAppVendor + '\'' +
                ", itmAppUptime='" + itmAppUptime + '\'' +
                ", itmAppVersion='" + itmAppVersion + '\'' +
                ", itmAppMaintenanceId='" + itmAppMaintenanceId + '\'' +
                ", itmAppOrder='" + itmAppOrder + '\'' +
                ", itmAppNetid='" + itmAppNetid + '\'' +
                ", itmAppIconsrc='" + itmAppIconsrc + '\'' +
                ", itmAppSystemurl='" + itmAppSystemurl + '\'' +
                ", organId='" + organId + '\'' +
                ", isGlobal='" + isGlobal + '\'' +
                ", itmAppPid='" + itmAppPid + '\'' +
                ", itmAppIp='" + itmAppIp + '\'' +
                ", itmAppPort='" + itmAppPort + '\'' +
                ", itmAppWebRoot='" + itmAppWebRoot + '\'' +
                ", itmAppCatagoryName='" + itmAppCatagoryName + '\'' +
                '}';
    }
}
