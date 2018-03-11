package com.shouxin.alerm.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shouxin.alerm.Datasource.TargetDataSource;
import com.shouxin.alerm.Mapper.translateMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("translateService")
@EnableAutoConfiguration
public class translateService {
    @Autowired
    private translateMapper mapper;
    @Autowired
    private sxService sxServices;
    private final org.slf4j.Logger logs = LoggerFactory.getLogger(translateService.class);
    /***
     * 通过subUuid获取hardware_resources表中的uuid
     * @param subUuid
     * @return
     */
    @TargetDataSource(name="ds1")
    public String getUuid(String subUuid){
        try{
        Map<String,Object> result = mapper.getResourceIdHw(subUuid);
            return result.get("uuid").toString();
            }
        catch (Exception e) {
            return "-1";
        }
    }
    @TargetDataSource(name="ds1")
    public Map<String,Object> getHWalermLevel(String title){
        return mapper.getHWalermLevel(title);
    }
    @TargetDataSource(name="ds2")
    public List<String> getWBZWhwxnhAlermData(){
        List<String> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        String maxid = mapper.getWBZWmaxId();
        String oldid = mapper.getWBZWoldId();
        map.put("old",oldid);
        map.put("new",maxid);
        list = mapper.getWBZWhwAlermData(map);
        mapper.updateLastMaxId(map);
        return list;
    }
    @TargetDataSource(name="ds3")
    public List<String> getYDZWhwxnhAlermData(){
        List<String> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        String maxid = mapper.getWBZWmaxId();
        String oldid = mapper.getWBZWoldId();
        map.put("old",oldid);
        map.put("new",maxid);
        list = mapper.getWBZWhwAlermData(map);
        mapper.updateLastMaxId(map);
        return list;
    }

    /***
     * 获取首信告警数据返回jsonarray
     * @return
     */
    public JSONArray getSXalermDatas(){
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            Long min = Long.parseLong(mapper.sxFromId("sx_task_id").get("value").toString());
            Map<String, Object> t = mapper.sxMaxId();
            logs.warn("查询首信告警表的最大值为" + t.get("maxs").toString());
            Long max = Long.parseLong(t.get("maxs").toString());
            t.put("min",min);t.put("max",max);
            result = mapper.getSXalermDatas(t);
            logs.warn("查询首信告警表的数量为" + result.size());
            JSONArray array = new JSONArray();
            for (int i = 0; i < result.size(); i++) {
                logs.warn("首信告警计数器" + i);
                Map<String, Object> temp = new HashMap<>();
                temp = result.get(i);
                JSONObject object = new JSONObject();
                Date day=new Date();
//                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                String subUuid = "";
//            如果告警类型为100则是告警生成 NEW 如果是101则是告警解除+
                if ("100".equals(temp.get("ALERT_STATUS"))) {
                    object.put("alm_type", "NEW");
                    object.put("sub_uuid", "SXPINGALM"+subUuid+temp.get("ITEM_ID").toString());
                    logs.warn("首信告警***生成***" + "SXALM"+subUuid+temp.get("ITEM_ID").toString());
                }
                if ("101".equals(temp.get("ALERT_STATUS"))) {
                    object.put("alm_type", "CLR");
                    try {
//                        object.put("sub_uuid", "SXPINGALM"+subUuid+mapper.getCreatUuid(temp.get("ITEM_ID").toString()).get("ID").toString());
                        object.put("sub_uuid", "SXPINGALM"+subUuid+temp.get("ITEM_ID").toString());
                        logs.warn("首信告警***清除***" + "SXPINGALM"+subUuid+temp.get("ITEM_ID").toString());
                    }catch (Exception e ){continue;}
                }
                object.put("appid", "");
                object.put("appname", "");
                logs.warn("PUUID    是     " + temp.get("PUUID").toString());
                String hardResourceUuid = sxServices.getHardwareResourceUuid(temp.get("PUUID").toString());
                object.put("pid", hardResourceUuid);
                object.put("pname", "");
                object.put("resourceid", hardResourceUuid);
                object.put("resourcename", "");
                object.put("alarm_text", temp.get("ALERT_DESC"));
                object.put("alarm_title", temp.get("ALERT_DESC"));
                object.put("alarm_content", temp.get("ALERT_DESC"));
                object.put("metricid", "");
                object.put("metricname", "");
                object.put("metricvalue", "");
                String[] timeMetric = temp.get("DATE_TIME").toString().split("\\.");
                object.put("metrictime", timeMetric[0]);
                object.put("active_time", temp.get("OPRATE_TIME"));
//                object.put("level", temp.get("ALERT_LEVEL"));
                object.put("level", "1");
                array.add(object);
            }
            logs.warn("首信告警更新告警最大值开始");
            mapper.updateSxMax(max);//更新首信告警进度表中的最大值
            logs.warn("首信告警更新告警最大值结束");
            return array;
        }catch (Exception e){
            logs.warn("首信告警出现问题，！！！！！！！！！");
            return null;
        }
    }


}
