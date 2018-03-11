package com.inspur.sp.web.BaseInfrastructure.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.inspur.AESEncrypter;
import com.inspur.sp.web.BaseInfrastructure.Service.HostService;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by xingwentao on 2017/7/25.
 */

/***
 * 计算资源
 */
@RestController
@RequestMapping("/baseInfrastructures")
public class HostController {
    @Autowired
    private HostService hostService;
    private final org.slf4j.Logger logs = LoggerFactory.getLogger(HostController.class);
    public String getMsg(){
        Map msgMap = new HashMap<>();
        msgMap.put("statusCode","401");
        msgMap.put("statusMsg","认证失败");
        JSONObject json = new JSONObject(msgMap);
        return json.toString();
    }
    /**
     * 宿主机总的接口
     * @param data
     * @return
     */
    @RequestMapping(value = "SaveAllHostCldData",method = RequestMethod.POST)
    public String SaveAllHostCldData(@RequestBody String data){//{"skey":"980c0e8b306781dc01d733ec8df8ecbf","vcpuTotal":"2000","vcpuAllocation":"1500","vcpuNoAllocation":"500","memoryTotal":"3000","memoryAllocation":"2000","memoryNoAllocation":"1000","timestamp":"20170101"}
        JSONObject obj = JSON.parseObject(data);
        Map msgMap = new HashMap<>();
        try {
            String skey = "",uuid = "";
            long increaseid = 0;
            try {
                skey = AESEncrypter.getInstance().decryptAsString(obj.getString("skey"));
                List<String> list = new ArrayList<String>();
                list = hostService.getCompanyList();
                if (!list.contains(skey)) return getMsg();
                uuid = UUID.randomUUID().toString();
            }catch (Exception e){
                return getMsg();
            }
            String id = "LTSZJ-" + UUID.randomUUID().hashCode();
            String df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            increaseid = hostService.SaveAuditLogs(uuid,"union_host_datasource",skey,1,df);//获取自增的id值
            obj.put("id",id);
            obj.put("date",df);
            obj.put("cam",skey);
            obj.put("increaseid",increaseid);
            hostService.saveAllHostCldData(obj);
            hostService.UpdateAuditLog(uuid);//更新插入状态数据
            msgMap.put("statusCode","200");
            msgMap.put("statusMsg","成功");
            JSONObject json = new JSONObject(msgMap);
            return json.toString();
        }catch (Exception e){
            msgMap.put("statusCode","400");
            msgMap.put("statusMsg","失败");
            JSONObject json = new JSONObject(msgMap);
            return json.toString();
        }
    }
    /**
     * 块存储
     * @param data
     * @return
     */
    private static  final Logger LOGGER = Logger.getLogger(HostController.class);
    @RequestMapping(value = "SaveBlockStorageData",method = RequestMethod.POST)
    public String SaveBlockStorageData(@RequestBody String data){//{"skey":"980c0e8b306781dc01d733ec8df8ecbf","storageTotal":"2000","storageUsed":"1500","storageRemain":"500","storageOutput":"3000","storageInput":"2000","timestamp":"20170101"}
        JSONObject obj = JSON.parseObject(data);
        Map msgMap = new HashMap<>();
        try {
            String skey = "",uuid = "";
            long increaseid = 0;
            try {
                AESEncrypter a = AESEncrypter.getInstance();
                skey = a.decryptAsString(obj.getString("skey"));
                List<String> list = new ArrayList<String>();
                list = hostService.getCompanyList();
                if (!list.contains(skey)) return getMsg();
                uuid = UUID.randomUUID().toString();
            }catch (Exception e){
                return getMsg();
            }
            String id = "LTSZJ-" + UUID.randomUUID().hashCode();
            String df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            increaseid = hostService.SaveAuditLogs(uuid,"union_blockstorage",skey,1,df);
            obj.put("id",id);
            obj.put("date",df);
            obj.put("cam",skey);
            obj.put("increaseid",increaseid);
            hostService.saveBlockStorageData(obj);
            hostService.UpdateAuditLog(uuid);//更新插入状态数据
            msgMap.put("statusCode","200");
            msgMap.put("statusMsg","成功");
            JSONObject json = new JSONObject(msgMap);
            return json.toString();
        }catch (Exception e){
            msgMap.put("statusCode","400");
            msgMap.put("errorMsg","失败");
            JSONObject json = new JSONObject(msgMap);
            return json.toString();
        }
    }

    /**
     * 提供应用云平台虚拟机资源数据批量推送服务
     * @param data
     * @return
     */
    @RequestMapping(value = "SaveHostVirtualData",method = RequestMethod.POST)
    public String SaveHostVirtualData(@RequestBody String data){
        //{"skey":"980c0e8b306781dc01d733ec8df8ecbf","targetObject":[{"cpuAllocation":[{"uuid":"123","hostname":"a","ip":"10.10.10.10","coreNum":20,"mainFrequency":"2.4Ghz"},{"uuid":"456","hostname":"a","ip":"10.10.10.10","coreNum":20,"mainFrequency":"2.4Ghz"}],"memorydiskDetail":[{"uuid":"321","hostname":"a","ip":"10.10.10.10","memoryAllocation":200,"memoryNoUsed":100,"diskAllocation":10000},{"uuid":"654","hostname":"a","ip":"10.10.10.10","memoryAllocation":200,"memoryNoUsed":100,"diskAllocation":10000}]
        //},{"cpuAllocation":[{"uuid":"1234","hostname":"a","ip":"10.10.10.10","coreNum":20,"mainFrequency":"2.4Ghz"},{"uuid":"4567","hostname":"a","ip":"10.10.10.10","coreNum":20,"mainFrequency":"2.4Ghz"}],"memorydiskDetail":[{"uuid":"4321","hostname":"a","ip":"10.10.10.10","memoryAllocation":200,"memoryNoUsed":100,"diskAllocation":10000},{"uuid":"7654","hostname":"a","ip":"10.10.10.10","memoryAllocation":200,"memoryNoUsed":100,"diskAllocation":10000}]}],"timestamp":"20170621"}
        JSONObject obj = JSON.parseObject(data);
        Map msgMap = new HashMap<>();
        try {
            String skey = "",uuid = "";
            long increaseid = 0;
            try {
                skey = AESEncrypter.getInstance().decryptAsString(obj.getString("skey"));
                List<String> list = new ArrayList<String>();
                list = hostService.getCompanyList();
                if (!list.contains(skey)) return getMsg();
                uuid = UUID.randomUUID().toString();
            }catch (Exception e){
                return getMsg();
            }
            String df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            JSONArray targetObject = obj.getJSONArray("targetObject");
            String timestamp = obj.getString("timestamp");
            increaseid = hostService.SaveAuditLogs(uuid,"union_virtualhost_datasource",skey,targetObject.size(),df);
            for(int i=0;i<targetObject.size();i++){
                JSONObject temp = (JSONObject) targetObject.get(i);
                JSONArray cpuAllocationList = temp.getJSONArray("cpuAllocation");
                for(int j=0;j<cpuAllocationList.size();j++){
                    JSONObject cpuAllocation = (JSONObject) cpuAllocationList.get(j);
                    String id = "XNJZY-" + UUID.randomUUID().hashCode();
                    cpuAllocation.put("id",id);
                    cpuAllocation.put("date",df);
                    cpuAllocation.put("cam",skey);
                    cpuAllocation.put("timestamp",timestamp);
                    cpuAllocation.put("increaseid",increaseid);
                    hostService.SaveHostVirtualDataCpuAllocation(cpuAllocation);
                }
                JSONArray memorydiskDetailList = temp.getJSONArray("memorydiskDetail");
                for(int j=0;j<memorydiskDetailList.size();j++){
                    JSONObject memorydiskDetail = (JSONObject) memorydiskDetailList.get(j);
                    String id = "XNJZY-" + UUID.randomUUID().hashCode();
                    memorydiskDetail.put("id",id);
                    memorydiskDetail.put("date",df);
                    memorydiskDetail.put("cam",skey);
                    memorydiskDetail.put("timestamp",timestamp);
                    memorydiskDetail.put("increaseid",increaseid);
                    hostService.SaveHostVirtualDataMemorydiskDetail(memorydiskDetail);
                }
            }
            hostService.UpdateAuditLog(uuid);//更新插入状态数据
            msgMap.put("statusCode","200");
            msgMap.put("statusMsg","成功");
            JSONObject json = new JSONObject(msgMap);
            return json.toString();
        }catch (Exception e){
            msgMap.put("statusCode","400");
            msgMap.put("errorMsg","失败");
            JSONObject json = new JSONObject(msgMap);
            return json.toString();
        }
    }
    /**
     * 提供云平台物理机资源基础数据批量推送服务。包括单个应用物理机的资源配置情况
     * @param data
     * @return
     */
    @RequestMapping(value = "SavePhysicalCldData",method = RequestMethod.POST)
    public String SavePhysicalCldData(@RequestBody String data){
        //{"skey":"980c0e8b306781dc01d733ec8df8ecbf","targetObject":{"total":20,"coreTotal":1000,"cpuDetail":[{"ip":"10.10.10.10","cpuNum":20,"coreNum":1000,"mainFrequency":"2.4Ghz"},{"ip":"20.20.20.20","cpuNum":40,"coreNum":2000,"mainFrequency":"2.4Ghz"}],"coreAllocation":800,"coreNoAllocation":200},"timestamp":"20170621"}
        JSONObject obj = JSON.parseObject(data);
        JSONObject result = new JSONObject();
        Map msgMap = new HashMap<>();
        try {
            String skey = "",uuid = "";
            long increaseid = 0;
            try {
                skey = AESEncrypter.getInstance().decryptAsString(obj.getString("skey"));
                List<String> list = new ArrayList<String>();
                list = hostService.getCompanyList();
                if (!list.contains(skey)) return getMsg();
                uuid = UUID.randomUUID().toString();
            }catch (Exception e){
                return getMsg();
            }
            String df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            result.put("date",df);
            result.put("cam",skey);
            result.put("timestamp",obj.getString("timestamp"));
            JSONObject targetObject = obj.getJSONObject("targetObject");
            result.put("total",targetObject.getInteger("total"));
            result.put("coreTotal",targetObject.getInteger("coreTotal"));
            result.put("coreAllocation",targetObject.getInteger("coreAllocation"));
            result.put("coreNoAllocation",targetObject.getInteger("coreNoAllocation"));
            JSONArray cpuDetail = targetObject.getJSONArray("cpuDetail");
            increaseid = hostService.SaveAuditLogs(uuid,"union_phsical_cld",skey,cpuDetail.size(),df);
            for(int i=0;i<cpuDetail.size();i++){
                String id = "WLJZY-" + UUID.randomUUID().hashCode();
                result.put("id",id);
                JSONObject temp = (JSONObject) cpuDetail.get(i);
                result.put("uuid",temp.getString("uuid"));
                result.put("ip",temp.getString("ip"));
                result.put("cpuNum",temp.getInteger("cpuNum"));
                result.put("coreNum",temp.getInteger("coreNum"));
                result.put("mainFrequency",temp.getString("mainFrequency"));
                result.put("increaseid",increaseid);
                hostService.SavePhysicalCldData(result);
            }
            hostService.UpdateAuditLog(uuid);//更新插入状态数据
            msgMap.put("statusCode","200");
            msgMap.put("statusMsg","成功");
            JSONObject json = new JSONObject(msgMap);
            return json.toString();
        }catch (Exception e){
            msgMap.put("statusCode","400");
            msgMap.put("errorMsg","失败");
            JSONObject json = new JSONObject(msgMap);
            return json.toString();
        }
    }
    /**
     * 提供云平台存储资源数据批量推送服务。存储种类包括：SAN
     * @param data
     * @return
     */
    @RequestMapping(value = "SaveHostStorageData",method = RequestMethod.POST)
    public String SaveHostStorageData(@RequestBody String data){
        //{"skey":"980c0e8b306781dc01d733ec8df8ecbf","storageWholeIn":2000,"storageWholeOut":1000,"targetObject":[{"storageName":"Inspurxxx","storageTotal":2500,"storageUsed":2000,"storageRemain":300,"storageDatail":[
        //{"lun":"00:01:01","lunSize":400,"lunUsed":400},{"lun":"10:01:01","lunSize":400,"lunUsed":400}],"storageOutput":500,"storageInput":200},{"storageName":"Unionxxx","storageTotal":1500,"storageUsed":1000,"storageRemain":500,
        // "storageDatail":[{"lun":"00:01:01","lunSize":200,"lunUsed":100},{"lun":"10:01:01","lunSize":400,"lunUsed":400}],"storageOutput":500,"storageInput":200}],"timestamp":"20170621"}
        JSONObject obj = JSON.parseObject(data);
        Map msgMap = new HashMap<>();
        try {
            String skey = "",uuid = "";
            long increaseid = 0;
            try {
                skey = AESEncrypter.getInstance().decryptAsString(obj.getString("skey"));
                List<String> list = new ArrayList<String>();
                list = hostService.getCompanyList();
                if (!list.contains(skey)) return getMsg();
                uuid = UUID.randomUUID().toString();
            }catch (Exception e){
                return getMsg();
            }
            String df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            JSONArray targetObject = obj.getJSONArray("targetObject");
            String storageWholeIn = obj.getString("storageWholeIn");
            String storageWholeOut = obj.getString("storageWholeOut");
            String timestamp = obj.getString("timestamp");
            increaseid = hostService.SaveAuditLogs(uuid,"union_nasstorage",skey,targetObject.size(),df);
            for(int i=0;i<targetObject.size();i++){
                JSONObject temp = (JSONObject) targetObject.get(i);
                JSONArray storageDatail = temp.getJSONArray("storageDatail");
                for(int j=0;j<storageDatail.size();j++){
                    Map<String,Object> result = new HashedMap();
                    String id = "CCZY-" + UUID.randomUUID().hashCode();
                    JSONObject item = (JSONObject) storageDatail.get(j);
                    result.put("lun",item.getString("lun"));
                    result.put("lunSize",item.getInteger("lunSize"));
                    result.put("lunUsed",item.getInteger("lunUsed"));
                    result.put("storageWholeIn",storageWholeIn);
                    result.put("storageWholeOut",storageWholeOut);
                    result.put("storageName",temp.getString("storageName"));
                    result.put("storageTotal",temp.getString("storageTotal"));
                    result.put("storageUsed",temp.getString("storageUsed"));
                    result.put("storageRemain",temp.getString("storageRemain"));
                    result.put("storageOutput",temp.getString("storageOutput"));
                    result.put("storageInput",temp.getString("storageInput"));
                    result.put("id",id);
                    result.put("date",df);
                    result.put("cam",skey);
                    result.put("timestamp",timestamp);
                    result.put("increaseid",increaseid);
                    hostService.SaveHostStorageData(result);
                }
            }
            hostService.UpdateAuditLog(uuid);//更新插入状态数据
            msgMap.put("statusCode","200");
            msgMap.put("statusMsg","成功");
            JSONObject json = new JSONObject(msgMap);
            return json.toString();
        }catch (Exception e){
            msgMap.put("statusCode","400");
            msgMap.put("errorMsg","失败");
            JSONObject json = new JSONObject(msgMap);
            return json.toString();
        }
    }
    /**
     * 提供云平台存储资源数据批量推送服务。存储   NAS
     * @param data
     * @return
     */
    @RequestMapping(value = "SaveNasStorageData",method = RequestMethod.POST)
    public String SaveNasStorageData(@RequestBody String data){
        //{"skey":"980c0e8b306781dc01d733ec8df8ecbf","storageWholeIn":2000,"storageWholeOut":1000,"targetObject":[{"storageName":"Dellxxx","storageTotal":1500,"storageTotalUsed":1000,"storageRemain":500,
        //"storageDatail":[{"ip":"10.10.10.10","storageAll":300,"storageUsed":200,"storageNoUsed":100},{"ip":"20.10.10.10","storageAll":300,"storageUsed":200,"storageNoUsed":100}],"storageOutput":500,"storageInput":200},{"storageName":"wiinter","storageTotal":1500,
        //"storageTotalUsed":1000,"storageRemain":500,"storageDatail":[{"ip":"10.10.10.10","storageAll":300,"storageUsed":200,"storageNoUsed":100},{"ip":"20.10.10.10","storageAll":300,"storageUsed":200,"storageNoUsed":100}],"storageOutput":500,"storageInput":200}],"timestamp":"20170621"}
        JSONObject obj = JSON.parseObject(data);
        Map msgMap = new HashMap<>();
        try {
            String skey = "",uuid="";
            long increaseid = 0;
            try {
                skey = AESEncrypter.getInstance().decryptAsString(obj.getString("skey"));
                List<String> list = new ArrayList<String>();
                list = hostService.getCompanyList();
                if (!list.contains(skey)) return getMsg();
                uuid = UUID.randomUUID().toString();
            }catch (Exception e){
                return getMsg();
            }
            String df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            JSONArray targetObject = obj.getJSONArray("targetObject");
            String storageWholeIn = obj.getString("storageWholeIn");
            String storageWholeOut = obj.getString("storageWholeOut");
            String timestamp = obj.getString("timestamp");
            increaseid = hostService.SaveAuditLogs(uuid,"union_nasstorage",skey,targetObject.size(),df);
            for(int i=0;i<targetObject.size();i++){
                JSONObject temp = (JSONObject) targetObject.get(i);
                Map<String,Object> result = new HashedMap();
                result.put("storageWholeIn",storageWholeIn);
                result.put("storageWholeOut",storageWholeOut);
                result.put("storageName",temp.getString("storageName"));
                result.put("storageTotal",temp.getString("storageTotal"));
                result.put("storageTotalUsed",temp.getString("storageTotalUsed"));
                result.put("storageRemain",temp.getString("storageRemain"));
                result.put("storageOutput",temp.getString("storageOutput"));
                result.put("storageInput",temp.getString("storageOutput"));
                JSONArray storageDatail = temp.getJSONArray("storageDatail");
                for(int j=0;j<storageDatail.size();j++){
//                    Map<String,Object> result = new HashedMap();
                    String id = "CCZY-" + UUID.randomUUID().hashCode();
                    JSONObject item = (JSONObject) storageDatail.get(j);
                    result.put("ip",item.getString("ip"));
                    result.put("storageAll",item.getString("storageAll"));
                    result.put("storageUsed",item.getString("storageUsed"));
                    result.put("storageNoUsed",item.getString("storageNoUsed"));

                    result.put("id",id);
                    result.put("date",df);
                    result.put("cam",skey);
                    result.put("timestamp",timestamp);
                    result.put("increaseid",increaseid);
                    hostService.SaveNasStorageData(result);
                }
            }
            hostService.UpdateAuditLog(uuid);//更新插入状态数据
            msgMap.put("statusCode","200");
            msgMap.put("statusMsg","成功");
            JSONObject json = new JSONObject(msgMap);
            return json.toString();
        }catch (Exception e){
            msgMap.put("statusCode","400");
            msgMap.put("errorMsg","失败");
            JSONObject json = new JSONObject(msgMap);
            return json.toString();
        }
    }
    /**
     * 提供云平台物理机宽带利用率数据批量推送服务
     * @param data
     * @return
     */
    @RequestMapping(value = "SaveHostBandWidthData",method = RequestMethod.POST)
    public String SaveHostBandWidthData(@RequestBody String data){
        //{"skey": "980c0e8b306781dc01d733ec8df8ecbf","targetObject": [{"bandwidthTotal": 200,"bandwidthDetail": [{"ip": "10.10.10.10","bandwidth": 20,"Detail": [{"netwrokCard": "eth0","bandwidthUsage": 20},{"netwrokCard": "eth1","bandwidthUsage": 30
        //}]},{"ip": "20.10.10.10","bandwidth": 20,"Detail": [{"netwrokCard": "eth0","bandwidthUsage": 20},{"netwrokCard": "eth1","bandwidthUsage": 30}]}]}],"timestamp": "20160908124521"}
        JSONObject obj = JSON.parseObject(data);
        Map msgMap = new HashMap<>();
        try {
            String skey = "",uuid = "";
            long increaseid = 0;
            try {
                skey = AESEncrypter.getInstance().decryptAsString(obj.getString("skey"));
                List<String> list = new ArrayList<String>();
                list = hostService.getCompanyList();
                if (!list.contains(skey)) return getMsg();
                uuid = UUID.randomUUID().toString();

            }catch (Exception e){
                return getMsg();
            }
            String df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            JSONArray targetObject = obj.getJSONArray("targetObject");
            String timestamp = obj.getString("timestamp");
            increaseid = hostService.SaveAuditLogs(uuid,"union_hostbandwidth",skey,targetObject.size(),df);
            for(int i=0;i<targetObject.size();i++){
                JSONObject temp = (JSONObject) targetObject.get(i);
                String bandwidthTotal = temp.getString("bandwidthTotal");
                JSONArray bandwidthDetail = temp.getJSONArray("bandwidthDetail");
                for(int j=0;j<bandwidthDetail.size();j++){
                    Map<String,Object> result = new HashedMap();
                    JSONObject item = (JSONObject) bandwidthDetail.get(j);
                    result.put("bandwidthTotal",bandwidthTotal);
                    result.put("ip",item.getString("ip"));
                    result.put("bandwidth",item.getString("bandwidth"));
                    JSONArray detailList = item.getJSONArray("Detail");
                    for(int k=0;k<detailList.size();k++){
                        String id = "KDLYL-" + UUID.randomUUID().hashCode();
                        JSONObject o = (JSONObject) detailList.get(k);
                        result.put("netwrokCard",o.getString("netwrokCard"));
                        result.put("bandwidthUsage",o.getString("bandwidthUsage"));
                        result.put("id",id);
                        result.put("date",df);
                        result.put("cam",skey);
                        result.put("timestamp",timestamp);
                        result.put("increaseid",increaseid);
                        hostService.SaveHostBandWidthData(result);
                    }
                }
            }
            hostService.UpdateAuditLog(uuid);//更新插入状态数据
            msgMap.put("statusCode","200");
            msgMap.put("statusMsg","成功");
            JSONObject json = new JSONObject(msgMap);
            return json.toString();
        }catch (Exception e){
            msgMap.put("statusCode","400");
            msgMap.put("errorMsg","失败");
            JSONObject json = new JSONObject(msgMap);
            return json.toString();
        }
    }
    /**
     * 提供云平台虚拟机宽带利用率数据批量推送服务
     * @param data
     * @return
     */
    @RequestMapping(value = "SaveHostVirtualBWData",method = RequestMethod.POST)
    public String SaveHostVirtualBWData(@RequestBody String data){
        //{"skey":"980c0e8b306781dc01d733ec8df8ecbf","targetObject":[{"bandwidthDetail":[{"ip":"10.10.10.10","bandwidth":20,"bandwidthUsage":30},{"ip":"20.10.10.10","bandwidth":30,"bandwidthUsage":40}]}],"timestamp":"20160908124521"}
        JSONObject obj = JSON.parseObject(data);
        JSONObject result = new JSONObject();
        Map msgMap = new HashMap<>();
        String uuid = "";
        long increaseid = 0;
        try {
            String skey = "";
            try {
                skey = AESEncrypter.getInstance().decryptAsString(obj.getString("skey"));
                List<String> list = new ArrayList<String>();
                list = hostService.getCompanyList();
                if (!list.contains(skey)) return getMsg();
                uuid = UUID.randomUUID().toString();

            }catch (Exception e){
                return getMsg();
            }
            String df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            result.put("date",df);
            result.put("cam",skey);
            result.put("timestamp",obj.getString("timestamp"));
            JSONArray targetObject = obj.getJSONArray("targetObject");
            increaseid = hostService.SaveAuditLogs(uuid,"union_hostvirtualbwdata",skey,targetObject.size(),df);
            for(int i=0;i<targetObject.size();i++){
                JSONObject temp = (JSONObject) targetObject.get(i);
                JSONArray arr = temp.getJSONArray("bandwidthDetail");
                for(int j=0;j<arr.size();j++) {
                    JSONObject object = arr.getJSONObject(j);
                    String id = "XNJDK-" + UUID.randomUUID().hashCode();
                    result.put("id", id);
                    result.put("ip", object.getString("ip"));
                    result.put("bandwidth", object.getInteger("bandwidth"));
                    result.put("bandwidthUsage", object.getInteger("bandwidthUsage"));
                    result.put("increaseid",increaseid);
                    hostService.SaveHostVirtualBWData(result);
                }
            }
            hostService.UpdateAuditLog(uuid);//更新插入状态数据
            msgMap.put("statusCode","200");
            msgMap.put("statusMsg","成功");
            JSONObject json = new JSONObject(msgMap);
            return json.toString();
        }catch (Exception e){
            msgMap.put("statusCode","400");
            msgMap.put("errorMsg","失败");
            JSONObject json = new JSONObject(msgMap);
            return json.toString();
        }
    }

    /**
     * 提供通用网络设备资源的数据批量推送服务
     * @param data
     * @return
     */
    @RequestMapping(value = "SaveNetWorkData",method = RequestMethod.POST)
    public String SaveNetWorkData(@RequestBody String data){
        //[{"skey":"980c0e8b306781dc01d733ec8df8ecbf","deviceIp": "172.1.120.110","cpuUsage":"50","cpuMaxUsage":"60","totalMemory":"500000","usedMemory":"11150","memoryUsage":"100050","memoryMaxUsage":"1150",
        //"netWork":[{"interfaceName": "interface1","loseBag": "1","netportStatus": "1","bandWidth":"10","bandwidthUsage":"15","readNetFlow":"2","writeNetFlow":"2","errorBag":"1","timestamp":"2017-07-29 12:12:12","pingStatus":"1"},{"interfaceName": "interface2",
        //"loseBag": "2","netportStatus": "1","bandWidth":"10","bandwidthUsage":"12","readNetFlow":"2","writeNetFlow":"2","errorBag":"1","timestamp":"2017-07-29 12:12:12","pingStatus":"1"}]}]
        logs.warn("union_network ->\n");
        logs.warn(data);
        logs.warn("union_network ->\n");
        Map msgMap = new HashMap<>();
        try {
            String uuid = "";
            long increaseid = 0;
//            JSONArray arrList = JSON.parseArray(data);
            JSONObject datas = JSON.parseObject(data);
            String skey = "";
            try {
                skey = AESEncrypter.getInstance().decryptAsString(datas.getString("skey"));
                List<String> list = new ArrayList<String>();
                list = hostService.getCompanyList();
                if (!list.contains(skey)) return getMsg();
                uuid = UUID.randomUUID().toString();

            }catch (Exception e){
                return getMsg();
            }
            JSONArray arrList = JSON.parseArray(datas.getString("switch"));
            String df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            increaseid = hostService.SaveAuditLogs(uuid, "union_network", skey,arrList.size(),df);
            for(int p=0;p<arrList.size();p++) {
                JSONObject obj = arrList.getJSONObject(p);
                JSONObject result = new JSONObject();
                result.put("name",obj.getString("name"));
                result.put("uuid",obj.getString("uuid"));
                result.put("deviceIp",obj.getString("deviceIp"));
                result.put("cpuUsage",obj.getString("cpuUsage"));
                result.put("cpuMaxUsage",obj.getString("cpuMaxUsage"));
                result.put("totalMemory",obj.getString("totalMemory"));
                result.put("usedMemory",obj.getString("usedMemory"));
                result.put("memoryUsage",obj.getString("memoryUsage"));
                result.put("memoryMaxUsage",obj.getString("memoryMaxUsage"));
                result.put("bandWidth",obj.getString("bandWidth"));
                result.put("loseBag",obj.getString("loseBag"));
                result.put("bandWidthUsageUp",obj.getString("bandWidthUsageUp"));
                result.put("bandWidthUsageDown",obj.getString("bandWidthUsageDown"));
//                result.put("readNetFlow",obj.getString("readNetFlow"));
//                result.put("writeNetFlow",obj.getString("writeNetFlow"));
                result.put("errorBag",obj.getString("errorBag"));
                result.put("timestamp",obj.getString("timestamp"));
                result.put("date", df);
                result.put("increaseid",increaseid);
                hostService.SaveNetWorkSwitch(result);//保存交换机数据
                logs.warn("SaveNetWorkData() -> union_network_switch -> "+arrList.size());
                JSONArray targetObject = obj.getJSONArray("port");
                hostService.SaveAuditLogs(uuid, "union_network_port", skey, targetObject.size(),df);
                for (int i = 0; i < targetObject.size(); i++) {
                    JSONObject temp = (JSONObject) targetObject.get(i);
                    JSONObject item = new JSONObject();
                    item.put("interfaceName",temp.getString("interfaceName"));
                    item.put("uuid",temp.getString("uuid"));
                    item.put("puuid",obj.getString("uuid"));
                    item.put("loseBag",temp.getString("loseBag"));
                    item.put("netportStatus",temp.getString("netportStatus"));
                    item.put("bandWidth",temp.getString("bandWidth"));
                    item.put("bandWidthUsageUp",temp.getString("bandWidthUsageUp"));
                    item.put("bandWidthUsageDown",temp.getString("bandWidthUsageDown"));
                    item.put("readNetFlow",temp.getString("readNetFlow"));
                    item.put("writeNetFlow",temp.getString("writeNetFlow"));
                    item.put("errorBag",temp.getString("errorBag"));
                    item.put("timestamp",obj.getString("timestamp"));
                    item.put("pingStatus",temp.getString("pingStatus"));
                    item.put("date",df);
//                    String id = "XNJDK-" + UUID.randomUUID().hashCode();
//                    item.put("id", id);
                    item.put("increaseid",increaseid);
                    hostService.SaveNetWorkPort(item);
                }
                logs.warn("SaveNetWorkData() -> union_network -> "+targetObject.size());
            }
            hostService.UpdateAuditLog(uuid);//更新插入状态数据
            msgMap.put("statusCode", "200");
            msgMap.put("statusMsg", "成功");
            JSONObject json = new JSONObject(msgMap);
            return json.toString();

        }catch (Exception e) {
            msgMap.put("statusCode", "400");
            msgMap.put("errorMsg", "失败");
            JSONObject json = new JSONObject(msgMap);
            return json.toString();
        }
    }
    /**
     * 提供通用基础设施资源数据的批量推送服务
     * @param data
     * @return
     */
    @RequestMapping(value = "SaveHostData",method = RequestMethod.POST)
    public String SaveHostData(@RequestBody String data) {
        //{"skey":"980c0e8b306781dc01d733ec8df8ecbf","targetObject":[{"uuid":"xxxx","ip":"10.10.10.1","cpuUsage":20,"totalMemory":50,"maxCpuUsage":70,"usedMemory":40,"memoryUsage":20,"maxMemoryUsage":65,
        //        "totalDiskSize":50000,"nousedMemory":"11","nousedDiskSize":"40000","usedDiskSize":10000,"diskInput":200,"diskOutput":300,"timestamp":"2017-07-31 12:12:12","pingstatus":1,"flag":"1"},{"uuid":"1xxx","ip":"10.10.10.1","cpuUsage":20,"totalMemory":50,"maxCpuUsage":70,"usedMemory":40,"memoryUsage":20,"maxMemoryUsage":65,"totalDiskSize":50000,
        //    "nousedMemory":"11","nousedDiskSize":"40000","usedDiskSize":10000,"diskInput":200,"diskOutput":300,"timestamp":"2017-07-31 12:12:12","pingstatus":1,"flag":"1"}]}
        JSONObject obj = JSON.parseObject(data);
        JSONObject result = new JSONObject();
        Map msgMap = new HashMap<>();
        String uuid  = "";
        long increaseid = 0;
        try {
            String skey = "";
            try {
                skey = AESEncrypter.getInstance().decryptAsString(obj.getString("skey"));
                List<String> list = new ArrayList<String>();
                list = hostService.getCompanyList();
                if (!list.contains(skey)) return getMsg();
                uuid = UUID.randomUUID().toString();
            }catch (Exception e){
                return getMsg();
            }
            String df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            result.put("date", df);
            result.put("cam", skey);
            JSONArray targetObject = obj.getJSONArray("targetObject");
            increaseid = hostService.SaveAuditLogs(uuid,"union_host_data",skey,targetObject.size(),df);
            for (int i = 0; i < targetObject.size(); i++) {
                JSONObject temp = (JSONObject) targetObject.get(i);
                String id = "XNJDK-" + UUID.randomUUID().hashCode();
                result.put("id", id);
                result.put("ip", temp.getString("ip"));
                result.put("uuid", temp.getString("uuid"));
                result.put("cpuUsage", temp.getString("cpuUsage"));
                result.put("totalMemory", temp.getString("totalMemory"));
                result.put("maxCpuUsage", temp.getString("maxCpuUsage"));
                result.put("usedMemory", temp.getString("usedMemory"));
                result.put("memoryUsage", temp.getString("memoryUsage"));
                result.put("totalDiskSize", temp.getString("totalDiskSize"));
                result.put("maxMemoryUsage", temp.getString("maxMemoryUsage"));
                result.put("usedDiskSize", temp.getString("usedDiskSize"));
                result.put("diskInput", temp.getString("diskInputByte"));
                result.put("diskOutput", temp.getString("diskOutputByte"));
                result.put("pingstatus", temp.getString("pingstatus"));
                result.put("flag",temp.getString("flag"));
                result.put("nousedMemory",temp.getString("nousedMemory"));
                result.put("nousedDiskSize",temp.getString("nousedDiskSize"));
                result.put("timestamp", temp.getString("timestamp"));
                result.put("increaseid",increaseid);
//               首信云专用字段hostname
//                result.put("hostname",temp.getString("hostname"));
                if("2".equals(temp.getString("flag")) && (temp.getString("uuid").length() <=0 || temp.getString("uuid") == null))
                    continue;
                hostService.SaveHostData(result);
            }
            hostService.UpdateAuditLog(uuid);//更新插入状态数据
            msgMap.put("statusCode", "200");
            msgMap.put("statusMsg", "成功");
            JSONObject json = new JSONObject(msgMap);
            return json.toString();
        } catch (Exception e) {
            msgMap.put("statusCode", "400");
            msgMap.put("errorMsg", "失败");
            JSONObject json = new JSONObject(msgMap);
            return json.toString();
        }
    }


}
