package com.inspur.sp.web.domain;

import java.io.Serializable;

/**
 * Created by xingwentao on 2017/6/28.
 */
public class NetDevice implements Serializable {
    public NetDevice(){}
    private String deviceIp;
    private String deviceId;
    private String organId;
    private String cpuUsage;
    private String cpuMaxUsage;
    private String totalMemory;
    private String usedMemory;
    private String memoryUsage;
    private String memoryMaxUsage;
    private String loseBag;
    private String netportStatus;
    private String interfaceName;
    private String bandWidth;
    private String netFlow;
    private String errorBag;
    private String writeDate;
    private String pingStatus;
    private String timestamp;

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getOrganId() {
        return organId;
    }

    public void setOrganId(String organId) {
        this.organId = organId;
    }

    public String getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(String cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public String getCpuMaxUsage() {
        return cpuMaxUsage;
    }

    public void setCpuMaxUsage(String cpuMaxUsage) {
        this.cpuMaxUsage = cpuMaxUsage;
    }

    public String getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(String totalMemory) {
        this.totalMemory = totalMemory;
    }

    public String getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(String usedMemory) {
        this.usedMemory = usedMemory;
    }

    public String getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(String memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public String getMemoryMaxUsage() {
        return memoryMaxUsage;
    }

    public void setMemoryMaxUsage(String memoryMaxUsage) {
        this.memoryMaxUsage = memoryMaxUsage;
    }

    public String getLoseBag() {
        return loseBag;
    }

    public void setLoseBag(String loseBag) {
        this.loseBag = loseBag;
    }

    public String getNetportStatus() {
        return netportStatus;
    }

    public void setNetportStatus(String netportStatus) {
        this.netportStatus = netportStatus;
    }

    public String getBandWidth() {
        return bandWidth;
    }

    public void setBandWidth(String bandWidth) {
        this.bandWidth = bandWidth;
    }

    public String getNetFlow() {
        return netFlow;
    }

    public void setNetFlow(String netFlow) {
        this.netFlow = netFlow;
    }

    public String getErrorBag() {
        return errorBag;
    }

    public void setErrorBag(String errorBag) {
        this.errorBag = errorBag;
    }

    public String getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(String writeDate) {
        this.writeDate = writeDate;
    }

    public String getPingStatus() {
        return pingStatus;
    }

    public void setPingStatus(String pingStatus) {
        this.pingStatus = pingStatus;
    }
}
