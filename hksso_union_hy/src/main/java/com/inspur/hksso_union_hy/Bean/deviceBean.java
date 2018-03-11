package com.inspur.hksso_union_hy.Bean;

import java.io.Serializable;

/**
 * Created by xingwentao on 2017/10/12.
 */
public class deviceBean implements Serializable {
    public deviceBean(){}
    private String deviceTypeCode;
    private String deviceTypeCodeName;
    private String manufacture;
    private String manufactureName;
    private String model;
    private String modelName;
    private String businessType;
    private String businessTypeName;
    private String installSite;
    private String installSiteName;
    private String installSitePath;
    private String siteCode;
    private String ip;
    private String port;
    private String status;
    private String orgCode;
    private String orgCodeName;
    private String updateStatusOn;
    private String code;
    private String volume;
    private String volumeUnit;
    private String volumeUnitName;
    private String usageVolume;
    private String name;
    private String types;
    private String deviceType;
    private String operateTime;

    public void setDeviceTypeCode(String deviceTypeCode) {
        this.deviceTypeCode = deviceTypeCode;
    }

    public void setUsageVolume(String usageVolume) {
        this.usageVolume = usageVolume;
    }

    public void setDeviceTypeCodeName(String deviceTypeCodeName) {
        this.deviceTypeCodeName = deviceTypeCodeName;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public void setManufactureName(String manufactureName) {
        this.manufactureName = manufactureName;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }

    public void setInstallSite(String installSite) {
        this.installSite = installSite;
    }

    public void setInstallSiteName(String installSiteName) {
        this.installSiteName = installSiteName;
    }

    public void setInstallSitePath(String installSitePath) {
        this.installSitePath = installSitePath;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public void setOrgCodeName(String orgCodeName) {
        this.orgCodeName = orgCodeName;
    }

    public void setUpdateStatusOn(String updateStatusOn) {
        this.updateStatusOn = updateStatusOn;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public void setVolumeUnit(String volumeUnit) {
        this.volumeUnit = volumeUnit;
    }

    public void setVolumeUnitName(String volumeUnitName) {
        this.volumeUnitName = volumeUnitName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }
}
