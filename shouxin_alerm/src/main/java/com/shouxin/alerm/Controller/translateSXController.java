package com.shouxin.alerm.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shouxin.alerm.Producer;
import com.shouxin.alerm.Service.translateService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@EnableAutoConfiguration
public class translateSXController {
    @Autowired
    private translateService ts;
    @Autowired
    private Producer producer;
    @Value("${server.ip}")
    private String serverIp;
    private final org.slf4j.Logger logs = LoggerFactory.getLogger(translateSXController.class);
    @Scheduled(cron = "0 0/2 * * * ?")
    public String getSXdata(){
        logs.warn("首信告警记录开始：");
        Map<String,Object> map = new HashMap();
        JSONArray list = new JSONArray();
        list = ts.getSXalermDatas();
        if(list == null) {logs.warn("首信告警查询列表为空");return null;}
        for(int i=0;i<list.size();i++){
            logs.warn("推送首信告警记录"+i);
            try {
                producer.create_norepeat(serverIp,"","PMALARM").send(JSON.toJSONString(list.get(i)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logs.warn("推送首信告警记录---结束---" + JSON.toJSONString(list));
        return "true";
    }

//    //外部专网的华为虚拟化平台
    @Scheduled(cron = "0 0/2 * * * ?")
    public void pushWBZWhwxnhAlerm(){
        logs.warn("开始读取外部专网华为虚拟化告警数据");
        Map<String,Object> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        list = ts.getWBZWhwxnhAlermData();
        for(int k=0;k<list.size();k++) {
            String content = list.get(k).toString();

            JSONArray array = JSON.parseArray(content);
//        try {
            String pid = "";
            logs.warn("外部专网---华为虚拟化平台告警 start ");
            logs.warn(content);
            if (array.size() == 0) return;
            for (int i = 0; i < array.size(); i++) {
                logs.warn(i + "  ");
                String subUuid = "";
                subUuid = "HWALM_WBZW" + subUuid + ((JSONObject) array.get(i)).getString("sub_uuid");
                System.out.println("WBZW-----get the sub_uuid is " + subUuid);
                ((JSONObject) array.get(i)).put("sub_uuid", subUuid);
                pid = ts.getUuid(((JSONObject) array.get(i)).getString("pid"));//pid用来找hardware_resource记录
                if ("-1".equals(pid)) continue;
                System.out.println("WBZW-----get the pid is " + pid);
                ((JSONObject) array.get(i)).put("pid", pid);
                ((JSONObject) array.get(i)).put("resourceid", pid);
                String levelType = ((JSONObject) array.get(i)).getString("alarm_title");
                Map<String, Object> temp = ts.getHWalermLevel(levelType);
                if (temp.get("counts").toString().equals("0")) continue;//如果数据库中没有对应的告警类型
                ((JSONObject) array.get(i)).put("level", temp.get("alarm_level").toString());
                System.out.println("推送外部专网内容开始****************" + JSON.toJSONString(array.get(i)) + "推送外部专网内容开始****************");
                logs.warn(JSON.toJSONString(levelType));
                try {
                    producer.create_norepeat(serverIp, "", "PMALARM").send(JSON.toJSONString(array.get(i)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                logs.warn(JSON.toJSONString(array.get(i)));
                logs.warn("外部专网-华为虚拟化平台告警 end ");
            }
        }
    }

//    //移动专网的华为虚拟化平台
    @Scheduled(cron = "0 0/2 * * * ?")
    public void pushYDZWhwxnhAlerm(){
        logs.warn("开始读取移动专网华为虚拟化告警数据");
        Map<String,Object> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        list = ts.getWBZWhwxnhAlermData();
        for(int k=0;k<list.size();k++) {
            String content = list.get(k).toString();

            JSONArray array = JSON.parseArray(content);
//        try {
            String pid = "";
            logs.warn("移动专网---华为虚拟化平台告警 start ");
            logs.warn(content);
            if (array.size() == 0) return;
            for (int i = 0; i < array.size(); i++) {
                logs.warn(i + "  ");
                String subUuid = "";
                subUuid = "HWALM_YDZW" + subUuid + ((JSONObject) array.get(i)).getString("sub_uuid");
                ((JSONObject) array.get(i)).put("sub_uuid", subUuid);
                pid = ts.getUuid(((JSONObject) array.get(i)).getString("pid"));//pid用来找hardware_resource记录
                if ("-1".equals(pid)) continue;
                ((JSONObject) array.get(i)).put("pid", pid);
                ((JSONObject) array.get(i)).put("resourceid", pid);
                String levelType = ((JSONObject) array.get(i)).getString("alarm_title");
                Map<String, Object> temp = ts.getHWalermLevel(levelType);
                if (temp.get("counts").toString().equals("0")) continue;//如果数据库中没有对应的告警类型
                ((JSONObject) array.get(i)).put("level", temp.get("alarm_level").toString());
                System.out.println(JSON.toJSONString(array.get(i)));
                logs.warn(JSON.toJSONString(levelType));
                try {
                    producer.create_norepeat(serverIp, "", "PMALARM").send(JSON.toJSONString(array.get(i)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                logs.warn(JSON.toJSONString(array.get(i)));
                logs.warn("移动专网-华为虚拟化平台告警 end ");
            }
        }
    }
}
