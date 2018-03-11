package com.inspur.sp.web.BaseInfrastructure.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.inspur.AESEncrypter;
import com.inspur.sp.web.BaseInfrastructure.Service.HaikangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.inspur.sp.web.BaseInfrastructure.Bean.deviceBean;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by xingwentao on 2017/10/12.
 */
@RestController
@RequestMapping("/Infrastructures")
public class HaikangController {
    @Autowired
    private HaikangService haikangService;
    private deviceBean deviceBeans = new deviceBean();
    @RequestMapping("device")
    public String device(@RequestBody String data){
        JSONObject obj = JSON.parseObject(data);
        Map msgMap = new HashMap<>();
        try {
            String skey = AESEncrypter.getInstance().decryptAsString(obj.getString("skey"));
            List<String> list = new ArrayList<String>();
            list = haikangService.getCompanyList();
            if (!list.contains(skey)) return getMsg();
        } catch (Exception e) {
            e.printStackTrace();
            return getMsg();
        }
        try{
            int count = (int) obj.get("count");
            String df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            JSONArray array = (JSONArray) obj.get("data");
            for(int i=0;i<count;i++){
                JSONObject temp = (JSONObject) array.get(i);
                deviceBeans.setInstallSite(temp.getString("installSite"));
                deviceBeans.setCode(temp.getString("code"));
                deviceBeans.setVolumeUnitName(temp.getString("volumeUnitName"));
                deviceBeans.setVolumeUnit(temp.getString("volumeUnit"));
                deviceBeans.setDeviceTypeCode(temp.getString("deviceTypeCode"));
                deviceBeans.setInstallSiteName(temp.getString("installSiteName"));
                deviceBeans.setBusinessTypeName(temp.getString("businessTypeName"));
                deviceBeans.setOrgCode(temp.getString("orgCode"));
                deviceBeans.setModel(temp.getString("model"));
                deviceBeans.setOrgCodeName(temp.getString("orgCodeName"));
                deviceBeans.setTypes(temp.getString("types"));
                deviceBeans.setIp(temp.getString("ip"));
                deviceBeans.setUpdateStatusOn(temp.getString("updateStatusOn"));
                deviceBeans.setVolume(temp.getString("volume"));
                deviceBeans.setManufacture(temp.getString("manufacture"));
                deviceBeans.setModelName(temp.getString("modelName"));
                deviceBeans.setUsageVolume(temp.getString("usageVolume"));
                deviceBeans.setPort(temp.getString("port"));
                deviceBeans.setManufactureName(temp.getString("manufactureName"));
                deviceBeans.setInstallSitePath(temp.getString("installSitePath"));
                deviceBeans.setName(temp.getString("name"));
                deviceBeans.setDeviceTypeCodeName(temp.getString("deviceTypeCodeName"));
                deviceBeans.setBusinessType(temp.getString("businessType"));
                deviceBeans.setStatus(temp.getString("status"));
                deviceBeans.setOperateTime(df);
                haikangService.saveDevice(deviceBeans);
            }
            haikangService.SaveAuditLog("device()","保存设备数据信息");
            msgMap.put("statusCode","200");
            msgMap.put("statusMsg","成功");
            JSONObject json = new JSONObject(msgMap);
            return json.toString();
        }catch (Exception e ){
            msgMap.put("statusCode","400");
            msgMap.put("statusMsg","失败");
            JSONObject json = new JSONObject(msgMap);
            return json.toString();
        }
    }

    @RequestMapping("position")
    public String position(@RequestBody String data){
        JSONObject obj = JSON.parseObject(data);
        Map msgMap = new HashMap<>();
        Map<String,Object> result = new HashMap<>();
        try {
            String skey = AESEncrypter.getInstance().decryptAsString(obj.getString("skey"));
            List<String> list = new ArrayList<String>();
            list = haikangService.getCompanyList();
            if (!list.contains(skey)) return getMsg();
        } catch (Exception e) {
            e.printStackTrace();
            return getMsg();
        }
        try{
            int count = (int) obj.get("count");
            String df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            JSONArray array = (JSONArray) obj.get("data");
            for(int i=0;i<count;i++){
                JSONObject temp = (JSONObject) array.get(i);
                result.put("code",temp.getString("code"));
                result.put("orgCode",temp.getString("orgCode"));
                result.put("businessType",temp.getString("businessType"));
                result.put("resNameSuffix",temp.getString("resNameSuffix"));
                result.put("areaCode",temp.getString("areaCode"));
                result.put("onlineStatus",temp.getString("onlineStatus"));
                result.put("latestQualityId",temp.getString("latestQualityId"));
                result.put("updateStatusOn",temp.getString("updateStatusOn"));
                result.put("operateTime",df);
                haikangService.savePosition(result);
            }
            haikangService.SaveAuditLog("position()","保存点位属性数据信息");
            msgMap.put("statusCode","200");
            msgMap.put("statusMsg","成功");
            JSONObject json = new JSONObject(msgMap);
            return json.toString();
        }catch (Exception e ){
            msgMap.put("statusCode","400");
            msgMap.put("statusMsg","失败");
            JSONObject json = new JSONObject(msgMap);
            return json.toString();
        }
    }

    @RequestMapping("qualityDiagnosis")
    public String qualityDiagnosis(@RequestBody String data){
        JSONObject obj = JSON.parseObject(data);
        Map msgMap = new HashMap<>();
        Map<String,Object> result = new HashMap<>();
        try {
            String skey = AESEncrypter.getInstance().decryptAsString(obj.getString("skey"));
            List<String> list = new ArrayList<String>();
            list = haikangService.getCompanyList();
            if (!list.contains(skey)) return getMsg();
        } catch (Exception e) {
            e.printStackTrace();
            return getMsg();
        }
        try{
            int count = (int) obj.get("count");
            String df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            JSONArray array = (JSONArray) obj.get("data");
            for(int i=0;i<count;i++){
                JSONObject temp = (JSONObject) array.get(i);
                result.put("id",temp.getString("id"));
                result.put("videoCode",temp.getString("videoCode"));
                result.put("videoLoss",temp.getString("videoLoss"));
                result.put("blurredImage",temp.getString("blurredImage"));
                result.put("noiseInterference",temp.getString("noiseInterference"));
                result.put("stripeNoise",temp.getString("stripeNoise"));
                result.put("frameFreeze",temp.getString("frameFreeze"));
                result.put("videoTampering",temp.getString("videoTampering"));
                result.put("blackwhiteImage",temp.getString("blackwhiteImage"));
                result.put("darkImage",temp.getString("darkImage"));
                result.put("overbrightImage",temp.getString("overbrightImage"));
                result.put("colorCast",temp.getString("colorCast"));
                result.put("contrast",temp.getString("contrast"));
                result.put("scene",temp.getString("scene"));
                result.put("videoCover",temp.getString("videoCover"));
                result.put("videoDithering",temp.getString("videoDithering"));
                result.put("ptz",temp.getString("ptz"));
                result.put("createOn",temp.getString("createOn"));
                result.put("createUserId",temp.getString("createUserId"));
                result.put("imageUrl",temp.getString("imageUrl"));
                result.put("resultCode",temp.getString("resultCode"));
                result.put("audioLoss",temp.getString("audioLoss"));
                result.put("volumeBig",temp.getString("volumeBig"));
                result.put("volumeSmall",temp.getString("volumeSmall"));
                result.put("onlineStatus",temp.getString("onlineStatus"));
                result.put("width",temp.getString("width"));
                result.put("height",temp.getString("height"));
                result.put("operateTime",df);
                haikangService.saveQualityDiagnosis(result);
            }
            haikangService.SaveAuditLog("qualityDiagnosis()","保存质量诊断数据信息");
            msgMap.put("statusCode","200");
            msgMap.put("statusMsg","成功");
            JSONObject json = new JSONObject(msgMap);
            return json.toString();
        }catch (Exception e ){
            msgMap.put("statusCode","400");
            msgMap.put("statusMsg","失败");
            JSONObject json = new JSONObject(msgMap);
            return json.toString();
        }
    }

    public String getMsg(){
        Map msgMap = new HashMap<>();
        msgMap.put("statusCode","401");
        msgMap.put("statusMsg","认证失败");
        JSONObject json = new JSONObject(msgMap);
        return json.toString();
    }
}
