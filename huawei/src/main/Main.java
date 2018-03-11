package main;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import com.alibaba.fastjson.JSON;
import util.HttpsHelper;
import util.JdbcTemplate;

/**
 * 对接HUAWEI接口客户端
 */
public class Main {

    public static void main(String[] args) {
        //读取配置信息
        Map param = getParam();
        Timer timer = new Timer();
        ResourceInterface resourceInterface = new ResourceInterface(param);
        QuotaInterface quotaInterface = new QuotaInterface(param);
        AlarmInterface alarmInterface = new AlarmInterface(param);

        timer.schedule(resourceInterface,0,86400000); //资源任务每天执行一次
        timer.schedule(quotaInterface,0,300000); //指标任务5分钟执行一次
        timer.schedule(alarmInterface,0,300000); //告警任务5分钟执行一次
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static Map getParam() {
        Map resultParam = new HashMap<>();
        InputStream in= JdbcTemplate.class.getClassLoader().getResourceAsStream("param.properties");
        Properties pro = new Properties();
        try {
            pro.load(in);
            String interfaceUrls = pro.getProperty("interfaceUrls");
            String quotaCodes = pro.getProperty("quotaCodes");
            String authUser = pro.getProperty("authUser");
            String authKey = pro.getProperty("authKey");
            if (null != interfaceUrls && !interfaceUrls.equals("")) {
                resultParam.put("interfaceUrls", interfaceUrls.split(","));
            }
            if (null != quotaCodes && !quotaCodes.equals("")) {
                resultParam.put("quotaCodes", quotaCodes.split(","));
            }
            if (null != authUser && !authUser.equals("")) {
                resultParam.put("authUser", authUser);
            }
            if (null != authKey && !authKey.equals("")) {
                resultParam.put("authKey", authKey);
            }
            if (null != pro.getProperty("alarmUpUrl") && !pro.getProperty("alarmUpUrl").equals("")) {
                resultParam.put("alarmUpUrl", pro.getProperty("alarmUpUrl"));
            }
            if (null != pro.getProperty("alarmTypeScreen") && !pro.getProperty("alarmTypeScreen").equals("")) {
                resultParam.put("alarmTypeScreen", pro.getProperty("alarmTypeScreen").split(","));
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        return resultParam;
    }

}
