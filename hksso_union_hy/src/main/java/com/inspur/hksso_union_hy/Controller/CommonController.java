package com.inspur.hksso_union_hy.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.inspur.AESEncrypter;
import com.inspur.hksso_union_hy.Service.CommonService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by xingwentao on 2017/10/13.
 */
@RestController
@RequestMapping("/commonInterfaces/")
public class CommonController {
    @Autowired
    public CommonService commonService;
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
            JSONArray datas = (JSONArray) obj.get("data");
            Map<String,Object> result = new HashedMap();
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

    @RequestMapping("devices")
    public String devices(@RequestBody String data) {
        JSONObject objs = JSON.parseObject(data);
        JSONObject obj = new JSONObject();
        Map msgMap = new HashMap<>();
        String interfaceId = "",uuid = "",skey = "";
        long increaseid = 0;
        try {
//            interfaceId = obj.getString("interfaceId");
            skey = AESEncrypter.getInstance().decryptAsString(objs.getString("skey"));
            List<String> list = new ArrayList<String>();
            list = commonService.getCompanyList();
            if (!list.contains(skey)) return getMsg("401","认证失败");
        } catch (Exception e){
            e.printStackTrace();
            return getMsg("401","认证失败");
        }
        try{
//            添加遍歷列表的函數
            JSONArray array = objs.getJSONArray("datas");
            for(int jp = 0;jp<array.size();jp++) {
                obj = (JSONObject) array.get(jp);
                interfaceId = obj.getString("interfaceId");
                boolean searchID = commonService.getSearchId(interfaceId);
                if (!searchID) return getMsg("404", "编码错误");
                List<Map> properties = commonService.getTableData(interfaceId);
                int lenProperties = properties.size();
                JSONArray datas = (JSONArray) obj.get("data");
                Map<String, Object> result = new HashedMap();
                int length = datas.size();
                String tableName = (String) properties.get(0).get("tableName");
                String mappingProperty = "";
                String targetProperty = "";
                uuid = UUID.randomUUID().toString();
                increaseid = commonService.SaveLogs(uuid, tableName, skey);
                String df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                for (int i = 0; i < length; i++) {
                    JSONObject tempObj = (JSONObject) datas.get(i);
                    for (int j = 0; j < lenProperties; j++) {
                        mappingProperty = (String) properties.get(j).get("mappingProperty");
                        targetProperty = (String) properties.get(j).get("targetProperty");
                        result.put(targetProperty, tempObj.getString(mappingProperty));
                    }
                    result.put("operateTime", df);//其中時間字段不是在配置表中的
                    result.put("task_id", increaseid);
                    commonService.saveData(result, tableName);
                }

            }//for 結束
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
