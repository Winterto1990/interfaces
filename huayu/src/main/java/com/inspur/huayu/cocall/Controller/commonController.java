package com.inspur.huayu.cocall.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.inspur.AESEncrypter;
import com.inspur.huayu.cocall.Service.commonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("commonInterfaces")
public class commonController {
    @Autowired
    private commonService commonService;
    @RequestMapping("device")
    public String device(@RequestBody String data) {
        JSONObject obj = JSON.parseObject(data);
        Map msgMap = new HashMap<>();
        String interfaceId = "",uuid = "",skey = "";
        long increaseid = 0;
        try {
            interfaceId = obj.getString("interfaceId");
            skey = AESEncrypter.getInstance().decryptAsString(obj.getString("skey"));
            List<String> list = new ArrayList<String>();
            list = commonService.getCompanyList();
            if (!list.contains(skey)) return getMsg("401","认证失败");
        } catch (Exception e){
            e.printStackTrace();
            return getMsg("401","认证失败");
        }
        try{
            boolean searchID = commonService.getSearchId(interfaceId);
            if(!searchID) return getMsg("404","编码错误");
            List<Map> properties = commonService.getTableData(interfaceId);
            int lenProperties = properties.size();
            JSONObject datas = JSON.parseObject(obj.get("data").toString());
            Map<String,Object> result = new HashedMap();
            int length = datas.size();
            String tableName = (String) properties.get(0).get("tableName");
            String mappingProperty = "";
            String targetProperty = "";
            uuid = UUID.randomUUID().toString();
            increaseid = commonService.SaveLogs(uuid,tableName,skey);
            String df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//            for(int i=0;i<length;i++){
//                JSONObject tempObj = (JSONObject) datas.get(i);
                for(int j=0;j<lenProperties;j++){
                    mappingProperty = (String) properties.get(j).get("mappingProperty");
                    targetProperty = (String) properties.get(j).get("targetProperty");
                    result.put(targetProperty,datas.getString(mappingProperty));
                }
                result.put("operateTime",df);//其中時間字段不是在配置表中的
                result.put("task_id",increaseid);
                commonService.saveData(result,tableName);
//            }
            commonService.UpdateAuditLog(uuid);//更新插入状态数据
            return getMsg("200","成功");
        }catch (Exception e){
            return getMsg("400","保存失败");
        }

    }
    @RequestMapping("devices")
    public String devices(@RequestBody String data){
        System.out.println(data);
        JSONObject obj = JSON.parseObject(data);
        Map msgMap = new HashMap<>();
        String interfaceId = "",uuid = "",skey = "";
        long increaseid = 0;
        try {
            interfaceId = obj.getString("interfaceId");
            skey = AESEncrypter.getInstance().decryptAsString(obj.getString("skey"));
            List<String> list = new ArrayList<String>();
            list = commonService.getCompanyList();
            if (!list.contains(skey)) return getMsg("401","认证失败");
        } catch (Exception e){
            e.printStackTrace();
            return getMsg("401","认证失败");
        }
        try{
            boolean searchID = commonService.getSearchId(interfaceId);
            if(!searchID) return getMsg("404","编码错误");
            List<Map> properties = commonService.getTableData(interfaceId);
            int lenProperties = properties.size();
            JSONArray datas = (JSONArray) obj.get("data");
            Map<String,Object> result = new HashMap<>();
            int length = datas.size();
            String tableName = (String) properties.get(0).get("tableName");
            String mappingProperty = "";
            String targetProperty = "";
            uuid = UUID.randomUUID().toString();
            increaseid = commonService.SaveLogs(uuid,tableName,skey);
            String df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            for(int i=0;i<length;i++){
                JSONObject tempObj = (JSONObject) datas.get(i);
                for(int j=0;j<lenProperties;j++){
                    mappingProperty = (String) properties.get(j).get("mappingProperty");
                    targetProperty = (String) properties.get(j).get("targetProperty");
                    result.put(targetProperty,tempObj.getString(mappingProperty));
                }
                result.put("operateTime",df);//其中時間字段不是在配置表中的
                result.put("task_id",increaseid);
                commonService.saveData(result,tableName);
            }
            commonService.UpdateAuditLog(uuid);//更新插入状态数据
            return getMsg("200","成功");
        }catch (Exception e){
            return getMsg("400","保存失败");
        }
    }
    public String getMsg(String code,String msg){
        Map msgMap = new HashMap<>();
        msgMap.put("statusCode",code);
        msgMap.put("statusMsg",msg);
        JSONObject json = new JSONObject(msgMap);
        return json.toString();
    }

}
