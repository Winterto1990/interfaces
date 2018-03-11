package main;

import com.alibaba.fastjson.JSON;
import com.sun.deploy.util.ArrayUtil;
import dao.ClientDao;
import service.InterfaceService;
import util.HttpsHelper;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by feng on 2018/1/25.
 */
public class AlarmInterface extends TimerTask {

    private Map param;
    private String[] interfaceUrls;
    private String[] alarmTypeScreen;
    private String authUser;
    private String authKey;
    private String alarmUpUrl;

    public AlarmInterface(Map param) {
        super();
        this.param = param;
    }

    InterfaceService service = new InterfaceService();
    ClientDao dao = new ClientDao();

    @Override
    public void run() {
        System.out.println("获取告警数据 开始（getAlarms start） --> "+ new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒")
                .format(new Date()) + "==============");
        if (null == param || param.isEmpty()) {
            return;
        }
        if (param.containsKey("interfaceUrls")) {
            interfaceUrls = (String[]) param.get("interfaceUrls");
        }
        if (param.containsKey("authUser")) {
            authUser = param.get("authUser").toString();
        }
        if (param.containsKey("authKey")) {
            authKey = param.get("authKey").toString();
        }
        if (param.containsKey("alarmUpUrl")) {
            alarmUpUrl = param.get("alarmUpUrl").toString();
        }
        if (param.containsKey("alarmTypeScreen")) {
            alarmTypeScreen = (String[]) param.get("alarmTypeScreen");
        }
        List<Map<String, String>> alarmList = getAlarms();
        sendAlarmData(alarmList);
    }

    /**
     * 获取华为接口告警数据
     * @return
     */
    private List<Map<String, String>> getAlarms() {
        List<Map<String, String>> alarmList = new ArrayList<>();
        //循环服务器列表
        for (int i = 0; i < interfaceUrls.length; i++) {
            String ip = interfaceUrls[i];
            //1、登录及获取站点列表
            List<String> sites = null;
            String authToken = service.login(ip,authUser,authKey);
            if(null != authToken && !authToken.equals("")) {
                sites = service.getSites(ip, authToken);
            }
            //2、获取告警数据
            List<Map<String, String>> resList = service.listAlarmInfo(ip,authToken,sites);
            if (null == resList || resList.size() <= 0) {
                continue;
            }
            //3、添加到一个集合
            alarmList.addAll(resList);
        }
        //4、huawei_alarm添加数据
        List<Map<String, String>> resultList = dao.insertBatchAlarm(alarmList);
        System.out.println("获取告警数据 结束（getAlarms end）--> "+ new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒")
                .format(new Date()) + "==============");
        return resultList;
    }

    /**
     * 发送告警数据
     * @param alarmList
     */
    private void sendAlarmData(List<Map<String, String>> alarmList) {
        if (alarmList == null || alarmList.size()<=0) {
            return;
        }
        System.out.println("发送告警数据 开始（sendAlarmData start） --> "+ new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒")
                .format(new Date()) + "==============");
        List<Map<String,String>> paramList = new ArrayList<>();
        List<Map<String,String>> lastAlarmList = dao.listLast2TimeAlarmTask();
        if (lastAlarmList == null || lastAlarmList.size() <= 0) {
            //首次产生告警记录
            paramList = putData(alarmList,"NEW");
        } else {
            //对比，1、新告警里有旧告警没有的记录，则是新增记录
            List<Map<String,String>> newList = compare(alarmList,lastAlarmList);
            newList = putData(newList,"NEW");
            // 2、旧告警里有新告警没有的记录，则是已清除的告警记录
            List<Map<String,String>> clrList = compare(lastAlarmList,alarmList);
            clrList = putData(clrList,"CLR");
            paramList.addAll(newList);
            paramList.addAll(clrList);
        }
        //发送数据
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", "application/json;charset=UTF-8");
        HttpsHelper.doHttpPost(alarmUpUrl,headerMap, JSON.toJSONString(paramList));
        System.out.println("发送告警数据 结束（sendAlarmData end） --> "+ new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒")
                .format(new Date()) + "==============");
    }

    private List<Map<String,String>> putData(
            List<Map<String, String>> alarmList,String type){
        List<Map<String,String>> paramList = new ArrayList<>();
        for (int i = 0; i < alarmList.size(); i++) {
            Map<String,String> alarm = alarmList.get(i);
            if (!arrayContains(alarmTypeScreen,alarm.get("svMoc"))){
                continue;
            }
            Map<String,String> temp = new HashMap<>();
            temp.put("alm_type",type);
            temp.put("appid","");
            temp.put("appname","");
            temp.put("resourceid","");
            temp.put("resourcename","");
            temp.put("pid",alarm.get("parentIp") + "_" + alarm.get("urn"));
            temp.put("sub_uuid",alarm.get("parentIp") + "_" + alarm.get("iSerialNo"));
            temp.put("pname",alarm.get("name"));
            temp.put("alarm_text",alarm.get("svAdditionalInfo"));
            temp.put("alarm_title",alarm.get("svAlarmName"));
            temp.put("alarm_content",alarm.get("svAdditionalInfo"));
            temp.put("metricid",alarm.get(""));
            temp.put("metricname",alarm.get(""));
            temp.put("metricvalue",alarm.get("svAdditionalInfo"));
            temp.put("metrictime",alarm.get("dtOccurTime"));
            temp.put("active_time",alarm.get("collectTime"));
            temp.put("level",alarm.get("levelNum"));
            paramList.add(temp);
        }
        return paramList;
    }

    private List<Map<String,String>> compare(List<Map<String,String>> listOne,
                                               List<Map<String,String>> listTwo){
        List<Map<String,String>> resultList = new ArrayList<>();
        for (int i = 0; i < listOne.size(); i++) {
            Map<String,String> mapOne = listOne.get(i);
            boolean flag = true;
            for (int j = 0; j < listTwo.size(); j++) {
                Map<String,String> mapTwo = listTwo.get(j);
                if (mapOne.get("parentIp").equals(mapTwo.get("parentIp")) &&
                        mapOne.get("iSerialNo").equals(mapTwo.get("iSerialNo"))) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                resultList.add(mapOne);
            }
        }
        return resultList;
    }

    public boolean arrayContains(String[] arr,String targetValue){
        for(String s:arr){
            if(s.equals(targetValue))
                return true;
        }
        return false;
    }
}
