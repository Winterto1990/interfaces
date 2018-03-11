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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@EnableAutoConfiguration
public class translateHWController {
    @Autowired
    private translateService ts;
    @Value("${server.ip}")
    private String serverIp;
    @Autowired
    private Producer producer;
    private final org.slf4j.Logger logs = LoggerFactory.getLogger(translateHWController.class);
    @RequestMapping(value =  "/hwAlermContent",method = RequestMethod.POST)
    public String hwAlermContent(@RequestBody String content){
//        [{"alm_type":"NEW","appid":"","appname":"","pid":"hard_resource_uuid",
// "pname":"h_r_name","resourceid":"pid","resourcename":"pname","alarm_text":"告警内容","alrm_title":"",
// "alarm_content":"","metricid":"","metricname":"","metricvalue":"","metrictime":"","active_time":"","level":"1"}]
        JSONArray array = JSON.parseArray(content);
//        try {
            String pid = "";
            logs.warn("数据专网---华为虚拟化平台告警 start ");
            logs.warn(content);
            if(array.size() == 0) return null;
            for (int i = 0; i < array.size(); i++) {
                logs.warn(i+"  ");
//                Date day=new Date();
//                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
//                subUuid = df.format(day);
                String subUuid = "";
                subUuid = "HWALM" + subUuid + ((JSONObject) array.get(i)).getString("sub_uuid");
                ((JSONObject) array.get(i)).put("sub_uuid", subUuid);
                pid = ts.getUuid(((JSONObject) array.get(i)).getString("pid"));//pid用来找hardware_resource记录
                if("-1".equals(pid)) continue;
                ((JSONObject) array.get(i)).put("pid", pid);
                ((JSONObject) array.get(i)).put("resourceid",pid);
                String levelType = ((JSONObject) array.get(i)).getString("alarm_title");
                Map<String,Object> temp = ts.getHWalermLevel(levelType);
                if(temp.get("counts").toString().equals("0")) continue;//如果数据库中没有对应的告警类型
                ((JSONObject) array.get(i)).put("level",temp.get("alarm_level").toString());
//                if(levelType.equals("主机光纤通道中断") || levelType.equals("主机关联的数据存储异常") || levelType.equals("VRM与NTP服务器心跳状态异常") || levelType.equals("主机网口状态异常") || levelType.equals("VRM主备间节点心跳故障") || levelType.equals("主机与VRM心跳异常") || levelType.equals("VRM主备节点数据库同步异常")
//                        || levelType.equals("虚拟机HA时，资源不足导致启动失败") || levelType.equals("数据存储异常") || levelType.equals("虚拟机发生蓝屏故障") || levelType.equals("主机与NTP服务器心跳状态异常") || levelType.equals("备节点数据库异常") || levelType.equals("CNA节点弹性计算业务进程异常")) ((JSONObject) array.get(i)).put("level","1");
//                if(levelType.equals("主机未配置NTP时钟源") || levelType.equals("VRM未配置NTP时钟源") || levelType.equals("VRM未配置远程管理数据备份") || levelType.equals("Remote Management Data Backup Is Not Configured for VRM")) ((JSONObject) array.get(i)).put("level","2");
//                if(levelType.equals("专享升级服务已经到期") || levelType.equals("主机CPU数超过许可90%") || levelType.equals("软件订阅与保障年费已经到期") || levelType.equals("License即将失效") || levelType.equals("主机管理域CPU占用率超过阈值")
//                        || levelType.equals("数据存储I/O响应时延超过阈值") || levelType.equals("主机虚拟化域CPU占用率超过阈值") || levelType.equals("主机CPU占用率超过阈值"))
//                    ((JSONObject) array.get(i)).put("level","3");
                System.out.println(JSON.toJSONString(array.get(i)));
                logs.warn(JSON.toJSONString(levelType));
                try {
//                    producer.create_norepeat(serverIp,"","PMALARM").send(JSON.toJSONString(array.get(i)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                logs.warn(JSON.toJSONString(array.get(i)));
                logs.warn("华为虚拟化平台告警 end ");
            }
            return getMsg("200","成功");
//        }
//        catch (Exception e){
//            return getMsg("500","失败");
//        }
    }
    public String getMsg(String code,String content){
        Map msgMap = new HashMap<>();
        msgMap.put("statusCode",code);
        msgMap.put("statusMsg",content);
        JSONObject json = new JSONObject(msgMap);
        return json.toString();
    }

}
