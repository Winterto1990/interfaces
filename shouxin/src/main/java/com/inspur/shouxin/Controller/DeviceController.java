package com.inspur.shouxin.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.inspur.AESEncrypter;
import com.inspur.shouxin.Service.DeviceService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/baseInfrastructures")
public class DeviceController {
    @Autowired
    private DeviceService device;
    private final Logger logs = LoggerFactory.getLogger(DeviceController.class);
    public String getMsg(){
        Map msgMap = new HashMap<>();
        msgMap.put("statusCode","401");
        msgMap.put("statusMsg","认证失败");
        JSONObject json = new JSONObject(msgMap);
        return json.toString();
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
        Map msgMap = new HashMap<>();
//        logs.warn("shouxin_network ->\n");
//        logs.warn(data);
        logs.warn("shouxin_network ->\n");
        try {
            String uuid = "";
            long increaseid = 0;
//            JSONArray arrList = JSON.parseArray(data);
            JSONObject datas = JSON.parseObject(data);
            String skey = "";
            try {
                skey = AESEncrypter.getInstance().decryptAsString(datas.getString("skey"));
                List<String> list = new ArrayList<String>();
                list = device.getCompanyList();
                if (!list.contains(skey)) return getMsg();
                uuid = UUID.randomUUID().toString();
            }catch (Exception e){
                return getMsg();
            }
            JSONArray arrList = JSON.parseArray(datas.getString("switch"));
            String df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            increaseid = device.SaveAuditLogs(uuid, "shouxin_network_switch", skey, arrList.size(),df);
            logs.warn("SaveNetWorkData() -> shouxin_network_switch -> "+arrList.size());
            for(int p=0;p<arrList.size();p++) {
                JSONObject obj = arrList.getJSONObject(p);
                JSONObject result = new JSONObject();
                result.put("name",obj.getString("name"));
                result.put("uuid",obj.getString("uuid"));
                result.put("deviceIp",obj.getString("deviceIp"));
                result.put("cpuUsage",obj.getString("cpuUsage"));
//                result.put("cpuMaxUsage",obj.getString("cpuMaxUsage"));
//                result.put("totalMemory",obj.getString("totalMemory"));
//                result.put("usedMemory",obj.getString("usedMemory"));
                result.put("memoryUsage",obj.getString("memoryUsage"));
//                result.put("memoryMaxUsage",obj.getString("memoryMaxUsage"));
                result.put("bandWidth",obj.getString("bandWidth"));//****
                result.put("lostBagNums",obj.getString("lostBagNums"));//***
                result.put("wrongBagNums",obj.getString("wrongBagNums"));//***
                result.put("bandWidthUsageUp",obj.getString("bandWidthUsageUp"));
                result.put("bandWidthUsageDown",obj.getString("bandWidthUsageDown"));
//                result.put("readNetFlow",obj.getString("readNetFlow"));
//                result.put("writeNetFlow",obj.getString("writeNetFlow"));
//                result.put("errorBag",obj.getString("errorBag"));
                result.put("timestamp",obj.getString("timestamp"));
                result.put("date", df);
                result.put("increaseid",increaseid);
                result.put("id",UUID.randomUUID().toString());
                device.SaveNetWorkSwitch(result);//保存交换机数据

                JSONArray targetObject = obj.getJSONArray("port");
                device.SaveAuditLogs(uuid, "shouxin_network_port", skey, targetObject.size(),df);
                logs.warn("SaveNetWorkData_Port() -> shouxin_network_port -> "+targetObject.size());
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
                    item.put("portTimeDelay",temp.getString("portTimeDelay"));
                    item.put("lostBagNums",temp.getString("lostBagNums"));
                    item.put("wrongBagNums",temp.getString("wrongBagNums"));
                    item.put("bagTransmit",temp.getString("bagTransmit"));
                    item.put("date",df);
                    item.put("id",UUID.randomUUID().toString());
//                    String id = "XNJDK-" + UUID.randomUUID().hashCode();
//                    item.put("id", id);
                    item.put("increaseid",increaseid);
                    device.SaveNetWorkPort(item);
                }
            }
            device.UpdateAuditLog(uuid);//更新插入状态数据
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
        logs.warn("shouxin_host_data ->\n");
        JSONObject result = new JSONObject();
        Map msgMap = new HashMap<>();
        String uuid  = "";
        long increaseid = 0;
        try {
            String skey = "";
            try {
                skey = AESEncrypter.getInstance().decryptAsString(obj.getString("skey"));
                List<String> list = new ArrayList<String>();
                list = device.getCompanyList();
                if (!list.contains(skey)) return getMsg();
                uuid = UUID.randomUUID().toString();

            }catch (Exception e){
                return getMsg();
            }
            String df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            result.put("date", df);
            result.put("cam", skey);
            JSONArray targetObject = obj.getJSONArray("targetObject");
            increaseid = device.SaveAuditLogs(uuid,"shouxin_host_data",skey,targetObject.size(),df);
            for (int i = 0; i < targetObject.size(); i++) {
                JSONObject temp = (JSONObject) targetObject.get(i);
                String id = UUID.randomUUID().toString();
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
                result.put("hostname",temp.getString("hostname"));
                device.SaveHostData(result);
            }
            logs.warn("SaveHostData() -> shouxin_host_data"+targetObject.size());
            device.UpdateAuditLog(uuid);//更新插入状态数据
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

    /***
     * 首信用，告警信息。包括，设备名称、设备ip、告警分级（警告、次要、主要、紧急）、告警描述、告警状态（新增、清除）、时间 、恢复时间
     * @param data
     * @return
     */
    @RequestMapping(value = "SaveAlertData",method = RequestMethod.POST)
    public String SaveAlertData(@RequestBody String data) {
        JSONObject obj = JSON.parseObject(data);
//        logs.warn(data);
        logs.warn("shouxin_alert ->\n");
        JSONObject result = new JSONObject();
        Map msgMap = new HashMap<>();
        String uuid  = "";
        long increaseid = 0;
        try {
            String skey = "";
            try {
                skey = AESEncrypter.getInstance().decryptAsString(obj.getString("skey"));
                List<String> list = new ArrayList<String>();
                list = device.getCompanyList();
                if (!list.contains(skey)) return getMsg();
                uuid = UUID.randomUUID().toString();
            }catch (Exception e){
                return getMsg();
            }
            String df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            result.put("date", df);
            result.put("cam", skey);
            JSONArray targetObject = obj.getJSONArray("targetObject");
            increaseid = device.SaveAuditLogs(uuid,"shouxin_alert",skey,targetObject.size(),df);
            for (int i = 0; i < targetObject.size(); i++) {
                JSONObject temp = (JSONObject) targetObject.get(i);
                String id =  UUID.randomUUID().toString();
                result.put("id", id);
                result.put("uuid",temp.getString("uuid"));
                result.put("puuid",temp.getString("puuid"));
                result.put("item_id",temp.getString("item_id"));
                result.put("ip", temp.getString("ip"));
                result.put("name", temp.getString("name"));
                result.put("level", temp.getString("level"));
                result.put("alert_status", temp.getString("alert_status"));
                result.put("alert_desc", temp.getString("alert_desc"));
                result.put("date_time",temp.getString("date_time"));
                result.put("recovery_time",temp.getString("recovery_time"));
                result.put("alert_type",temp.getString("alert_type"));
                result.put("increaseid",increaseid);
                device.SaveAlertData(result);
            }
            logs.warn("SaveAlertData() -> shouxin_alert"+targetObject.size());
            device.UpdateAuditLog(uuid);//更新插入状态数据
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
