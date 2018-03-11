package com.appmonitor.apprestatpic.action;


import com.alibaba.fastjson.JSONObject;
import com.appmonitor.apprestatpic.po.VisitDeepJson;
import com.appmonitor.apprestatpic.po.VisitDeepResponesTime;
import com.appmonitor.apprestatpic.service.AppDeepTimeService;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by xuds on 2017/10/20.
 */
@EnableAutoConfiguration
@Component
public class appResptateTask {

    private final static String ACCESS_STATUS_NATURAL = "001";//正常
    private final static String ACCESS_STATUS_WARN = "002";   //告警
    private final static String ACCESS_STATUS_ERRO = "003";   //无法访问
    public static final int POOL_SIZE = 5;

    @Autowired
    private AppDeepTimeService appDeepTimeService;

//    @Scheduled(fixedRate = 15 * 60 * 1000)  //15分钟一次
    @Scheduled(cron = "0 0/15 * * * *")
    public void run() {
        System.out.println("start appDeep task 渲染时间 + ");
        long start = System.currentTimeMillis();
        // 添加数据到缓存中
        VisitDeepJson mapJson = new VisitDeepJson();
        JSONObject jsonAll = new JSONObject();

        //长String结果
        StringBuffer sb = new StringBuffer();
        List<VisitDeepResponesTime> resultList = new ArrayList<VisitDeepResponesTime>();
        try {
            //创建线程池相关
            ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
            List<Callable<VisitDeepResponesTime>> taskList = new ArrayList<Callable<VisitDeepResponesTime>>();
            ////读取配置文件中的应用URL列表
            try {
                String modelName = null;
                String FirstUrl = null;
                Properties properties = new Properties();
                InputStream inputStream = VisitDeepConnTask.class.getClassLoader().getResourceAsStream("config/appurl.properties");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                properties.load(bufferedReader);
                for (Object key : properties.keySet()) {
                    String strTempUrl = properties.get(key).toString();
                    String[] AppUrlArray = null;
                    AppUrlArray = strTempUrl.split("#");
                    if ((AppUrlArray != null) && (AppUrlArray.length != 0)) {
                        String[] arrayUrls = AppUrlArray[2].split(";");  //对应的名称:URL
                        for(String url_name: arrayUrls) {
                            String[] nameUrl = url_name.split(","); // 对应着名称 + URL
                            modelName = nameUrl[0];
                            if(nameUrl.length>1)
                            FirstUrl = nameUrl[1];
                            else continue;
                            //添加到list列表中
//                        VisitDeepConnTask connTask = new VisitDeepConnTask(AppUrlArray);
                            VisitDeepConnTask connTask = new VisitDeepConnTask(AppUrlArray[0],AppUrlArray[1],0L,FirstUrl,modelName);
                            taskList.add(connTask);
                        }
                    } else {
                        System.out.println("读取配置文件数据为空！");
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            //添加到线程池中
            List<Future<VisitDeepResponesTime>> futureList = pool.invokeAll(taskList);
            for (Future<VisitDeepResponesTime> future : futureList) {
                VisitDeepResponesTime result = future.get();
                resultList.add(result);  //***** ???
            }
//            for (VisitDeepResponesTime result : resultList) {
//                String appName;                      //应用名称
//                String VisitDeepUrl;           //url串
//                Long VisitDeepTimePre;       //每一个url的时间
//                Long VisitDeepTotalTime;             ///总时间
//                String FirstPageUrl;                 //首页url
//                String VisitDeepStatus;              //状态
//                Integer UrlNum;//配置应用链接个数
//                Double AvgTime;//平均响应时间

//                appName = result.getAppName();
//                VisitDeepUrl = result.getVisitDeepUrl();
//                VisitDeepTimePre = result.getVisitDeepTimePre();
//                VisitDeepTotalTime = result.getVisitDeepTotalTime();
//                FirstPageUrl = result.getFirstPageUrl();
//                UrlNum = result.getUrlNum();
//                AvgTime = result.getAvgTime();
                //总时间大于8秒小于100000的为告警
//                if (VisitDeepTimePre <= 8000) {
//                    VisitDeepStatus = ACCESS_STATUS_NATURAL;//正常
//                } else {
//                    if (VisitDeepTimePre >= 600000) {
//                        VisitDeepStatus = ACCESS_STATUS_ERRO;//错误
//                    } else {
//                        VisitDeepStatus = ACCESS_STATUS_WARN;//告警
//                    }
//                }
//               // 给前台结果
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("visit_Status", VisitDeepStatus);
//                jsonObject.put("app_name", appName);
//                jsonObject.put("visit_TotalTime", VisitDeepTotalTime.toString());
//                jsonObject.put("visit_TimePre", VisitDeepTimePre);
//                jsonObject.put("first_PageUrl", FirstPageUrl);
//                jsonObject.put("visit_Url", VisitDeepUrl);
//                jsonObject.put("UrlNum", UrlNum);
//                jsonObject.put("AvgTime", AvgTime);

                //组成仿json串，以符号;结尾
//                sb.append(jsonObject.toJSONString()).append(";");
//            }
            System.out.println("end appDeep task");
            //关闭所有的浏览器驱动 及浏览器
            Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
            Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
            pool.shutdownNow();

        } catch (NullPointerException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }

        //插入数据库
        try {
            appDeepTimeService.insertData(resultList);
            resultList.clear();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        try {
            Thread.sleep((int) Math.random() * 10);
        } catch (InterruptedException e) {
        }
    }

}
