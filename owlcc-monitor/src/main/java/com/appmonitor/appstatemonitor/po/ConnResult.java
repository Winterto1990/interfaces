package com.appmonitor.appstatemonitor.po;

/**
 * Created by Jason on 12/01/2017.
 */
public class ConnResult {
    private String itmAppId;
    private String ipAddress;
    private String itmAppName;
    private String urlStr;
    private boolean ipResult;
    private boolean telnetResult;
    private boolean accessResult;
    private String statusCode;
    private String responseTime;
    private Long ipErrTime;
    private Long telnetErrTime;
    private Long accessErrTime;

    public ConnResult(String itmAppId, String ipAddress, String itmAppName, String urlStr, boolean ipResult, boolean telnetResult, boolean accessResult, String responseTime, String statusCode, Long ipErrTime, Long telnetErrTime, Long accessErrTime) {
        this.itmAppId = itmAppId;
        this.ipAddress = ipAddress;
        this.itmAppName = itmAppName;
        this.urlStr = urlStr;
        this.ipResult = ipResult;
        this.telnetResult = telnetResult;
        this.accessResult = accessResult;
        this.responseTime = responseTime;
        this.ipErrTime = ipErrTime;
        this.telnetErrTime = telnetErrTime;
        this.accessErrTime = accessErrTime;
        this.statusCode = statusCode;
    }

    public String getItmAppId() {
        return itmAppId;
    }

    public void setItmAppId(String itmAppId) {
        this.itmAppId = itmAppId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getItmAppName() {
        return itmAppName;
    }

    public void setItmAppName(String itmAppName) {
        this.itmAppName = itmAppName;
    }

    public boolean isIpResult() {
        return ipResult;
    }

    public void setIpResult(boolean ipResult) {
        this.ipResult = ipResult;
    }

    public boolean isTelnetResult() {
        return telnetResult;
    }

    public void setTelnetResult(boolean telnetResult) {
        this.telnetResult = telnetResult;
    }

    public boolean isAccessResult() {
        return accessResult;
    }

    public void setAccessResult(boolean accessResult) {
        this.accessResult = accessResult;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public Long getIpErrTime() {
        return ipErrTime;
    }

    public void setIpErrTime(Long ipErrTime) {
        this.ipErrTime = ipErrTime;
    }

    public Long getTelnetErrTime() {
        return telnetErrTime;
    }

    public void setTelnetErrTime(Long telnetErrTime) {
        this.telnetErrTime = telnetErrTime;
    }

    public Long getAccessErrTime() {
        return accessErrTime;
    }

    public void setAccessErrTime(Long accessErrTime) {
        this.accessErrTime = accessErrTime;
    }

    public String getUrlStr() {
        return urlStr;
    }

    public void setUrlStr(String urlStr) {
        this.urlStr = urlStr;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "ConnResult{" +
                "itmAppId='" + itmAppId + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", itmAppName='" + itmAppName + '\'' +
                ", urlStr='" + urlStr + '\'' +
                ", ipResult=" + ipResult +
                ", telnetResult=" + telnetResult +
                ", accessResult=" + accessResult +
                ", statusCode='" + statusCode + '\'' +
                ", responseTime='" + responseTime + '\'' +
                ", ipErrTime=" + ipErrTime +
                ", telnetErrTime=" + telnetErrTime +
                ", accessErrTime=" + accessErrTime +
                '}';
    }
}
