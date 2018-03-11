package com.appmonitor.appstatemonitor.action;

import com.appmonitor.appstatemonitor.po.ConnResult;
import com.appmonitor.appstatemonitor.po.AppBean;
import com.appmonitor.appstatemonitor.service.AppStateService;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by xuds on 2017/7/20.
 */
@EnableAutoConfiguration
@Component
public class AppMonitorTask {
    private static Logger logger = LoggerFactory.getLogger(AppMonitorTask.class);
    private final static String IP_STATUS_ERR = "001";
    private final static String TELNET_STATUS_ERR = "002";
    private final static String ACCESS_STATUS_ERR = "003";
    private final static String StatusCode = "004";//url  可访问
    public static final int POOL_SIZE = 5;
    private AppBean app;

    //网系编码
    @Value("${spring.netGroup.code}")
    private String netCode;
    //组织机构id
    @Value("${spring.organ.id}")
    private String organId;
    //是否启用ping
    @Value("${spring.ping.isFlag}")
    private String pingFlag;
    @Autowired
    private AppStateService appStateService;

    @Scheduled(fixedRate = 10 * 60 * 1000)  // 每10分钟执行一次
//    @Scheduled(cron = "0 0/10 * * * *")
    public void task() {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("start app task========" + df.format(System.currentTimeMillis()));

        long start = System.currentTimeMillis();
        //初始化缓存
        InputStream path = AppMonitorTask.class.getClassLoader().getResourceAsStream("ehcache/ehcache-app.xml");
        CacheManager cacheManager = CacheManager.newInstance(path);

        // 获取ehcache配置文件中的一个cache
        Cache mapCache = null;
        if (cacheManager.cacheExists("appCache")) {
            mapCache = cacheManager.getCache("appCache");
        } else {
            cacheManager.addCacheIfAbsent("appCache");
            mapCache = cacheManager.getCache("appCache");
        }

        //长String结果
//        StringBuffer sb = new StringBuffer();
        AppBean appbean = new AppBean();
        appbean.setItmAppNetid(netCode);
        appbean.setOrganId(organId);
        try {
            List<AppBean> appList = appStateService.getAllAppList(appbean);
            System.out.println("******************" + appList.toString());
            System.out.println("******************" + appList.size());
            if (appList != null) {
                //本次总查询时间戳
                long beginTime = System.currentTimeMillis();

                //创建线程池相关
                ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
                List<Callable<ConnResult>> taskList = new ArrayList<Callable<ConnResult>>(appList.size());

                for (int i = 0; i < appList.size(); i++) {
                    app = appList.get(i);
                    String ipResult = IP_STATUS_ERR; //001
                    String telnetResult = TELNET_STATUS_ERR; //002
                    String accessResult = ACCESS_STATUS_ERR; //003
                    String statusCode = StatusCode; //004
//                    RenderTimeByUrl.getRenderTime(app.getItmAppId(), app.getItmAppSystemurl().trim());
                    String urlStr = app.getItmAppSystemurl().trim();
                    System.out.println(i);
//                    if (urlStr != null && urlStr != "") {
                    if("无".equals(urlStr) || urlStr.equals(null)){System.out.println("没有url地址。");}
                    else{
                        try {
                            URL url = new URL(urlStr); //http://wenshu.court.gov.cn/
                            String itmAppIp = url.getHost(); //wenshu.court.gov.cn
                            int itmAppPort = url.getPort() == -1 ? 80 : url.getPort();

                            ConnTask connTask = new ConnTask(app.getItmAppId(), app.getItmAppName(), itmAppIp, itmAppPort, urlStr,pingFlag);
                            taskList.add(connTask);

                        } catch (MalformedURLException e) { //url不合法
                            ipResult = IP_STATUS_ERR;
                            telnetResult = TELNET_STATUS_ERR;
                            accessResult = ACCESS_STATUS_ERR;
                            //给前台结果
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("net_name", app.getItmAppCatagoryName());
                            jsonObject.put("app_name", app.getItmAppName());
                            jsonObject.put("ip_status", ipResult);
                            jsonObject.put("telnet_status", telnetResult);
                            jsonObject.put("access_status", accessResult);
                            jsonObject.put("respons_time", "0");
                            jsonObject.put("appUrl", urlStr);
                            jsonObject.put("statusCode", "005");//zanding
                            //组成仿json串
//                            sb.append(jsonObject.toJSONString()).append(";");

                            //写入数据库(未加入线程池的就可写入的错误信息)
                            writeIntoDB(app.getItmAppId(), beginTime);
                        }
                    }
//                    else {
                        //url为空 *** 将url为空的结果不放入数据库中 ***
//                        ipResult = IP_STATUS_ERR;
//                        telnetResult = TELNET_STATUS_ERR;
//                        accessResult = ACCESS_STATUS_ERR;
//                        //给前台结果
//                        JSONObject jsonObject = new JSONObject();
//                        jsonObject.put("net_name", app.getItmAppCatagoryName());
//                        jsonObject.put("app_name", app.getItmAppName());
//                        jsonObject.put("ip_status", ipResult);
//                        jsonObject.put("telnet_status", telnetResult);
//                        jsonObject.put("access_status", accessResult);
//                        jsonObject.put("respons_time", "0");
//                        jsonObject.put("appUrl", urlStr);
//                        jsonObject.put("statusCode", "005");
//                        //组成仿json串
//                        sb.append(jsonObject.toJSONString()).append(";");
//
//                        //写入数据库(未加入线程池的就可写入的错误信息)
//                        writeIntoDB(app.getItmAppId(), beginTime);
//                    }

                }   //for 循环结束点

                //执行线程池中任务
                List<ConnResult> resultList = new ArrayList<ConnResult>();
                List<Future<ConnResult>> futureList = pool.invokeAll(taskList);
                for (Future<ConnResult> future : futureList) {
                    ConnResult result = future.get();
                    resultList.add(result);
                }
                for (ConnResult result : resultList) {
                    String ipResult;
                    String telnetResult;
                    String accessResult;
                    ipResult = result.isIpResult() ? "000" : IP_STATUS_ERR;
                    telnetResult = result.isTelnetResult() ? "000" : TELNET_STATUS_ERR;
                    accessResult = result.isAccessResult() ? "000" : ACCESS_STATUS_ERR;

                    //给前台结果
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("net_name", app.getItmAppCatagoryName());
                    jsonObject.put("app_name", result.getItmAppName());
                    jsonObject.put("ip_status", ipResult);
                    jsonObject.put("telnet_status", telnetResult);
                    jsonObject.put("access_status", accessResult);
                    jsonObject.put("respons_time", result.getResponseTime());
                    jsonObject.put("appUrl", result.getUrlStr());
                    jsonObject.put("statusCode", result.getStatusCode());
                    //组成仿json串
//                    sb.append(jsonObject.toJSONString()).append(";");
                    //其中一个有错,就写入数据库
                    //if (!ipResult.equals("000") || !telnetResult.equals("000") || !accessResult.equals("000")) {
                    //将检测结果存储到数据库
//                    System.out.println("+++++++++++++" + result.toString());
                    writeResIntoDB(result, beginTime);
                    // }
                }
                System.out.println("end app task========" + df.format(System.currentTimeMillis()));
                pool.shutdownNow();
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
            logger.info("应用初始化-应用信息异常，调用补偿机制！" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Element element = mapCache.get("app");
//        String result = sb.toString();
//        if (element == null) {
//            Element element1 = new Element("app", result);
//            mapCache.put(element1);
//        } else {
//            // 删除缓存
//            mapCache.remove("app");
//            Element element1 = new Element("app", result);
//            mapCache.put(element1);
//        }

    }

    /**
     * 信息写入数据库(线程池任务)
     *
     * @param connResult
     * @param beginTime
     */
    private void writeResIntoDB(ConnResult connResult, Long beginTime) {
        //写入数据库记录-
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> resultMap;
        Long ipErrTime = connResult.getIpErrTime();
        Long telnetErrTime = connResult.getTelnetErrTime();
        Long accessErrTime = connResult.getAccessErrTime();
        //right
        if (accessErrTime == null) {
            resultMap = new HashMap<String, Object>();
            resultMap.put("begin_time", format.format(beginTime));
            resultMap.put("app_id", connResult.getItmAppId());
            resultMap.put("monitor_time", format.format(System.currentTimeMillis()));
            resultMap.put("response_time", connResult.getResponseTime());
            resultMap.put("monitor_status", StatusCode);
            resultMap.put("netCode", netCode);
            appStateService.insertResponsAppMonitor(resultMap);
            return;
        }
        if (ipErrTime != null && ipErrTime > 0) {
            resultMap = new HashMap<String, Object>();
            resultMap.put("begin_time", format.format(beginTime));
            resultMap.put("app_id", connResult.getItmAppId());
            resultMap.put("monitor_time", format.format(ipErrTime));
            resultMap.put("monitor_status", IP_STATUS_ERR);
            resultMap.put("netCode", netCode);
            appStateService.insertAppMonitor(resultMap);
            return;
        }
        //0126
//        if (telnetErrTime != null && telnetErrTime > 0) {
//            resultMap = new HashMap<String, Object>();
//            resultMap.put("begin_time", format.format(beginTime));
//            resultMap.put("app_id", connResult.getItmAppId());
//            resultMap.put("monitor_time", format.format(telnetErrTime));
//            resultMap.put("monitor_status", TELNET_STATUS_ERR);
//            resultMap.put("netCode", netCode);
//            appStateService.insertAppMonitor(resultMap);
//            return;
//        }
        //0126
//        if (accessErrTime != null && accessErrTime > 0) {
//            resultMap = new HashMap<String, Object>();
//            resultMap.put("begin_time", format.format(beginTime));
//            resultMap.put("app_id", connResult.getItmAppId());
//            resultMap.put("monitor_time", format.format(accessErrTime));
//            resultMap.put("monitor_status", ACCESS_STATUS_ERR);
//            resultMap.put("netCode", netCode);
//            appStateService.insertAppMonitor(resultMap);
//            return;
//        }

    }

    /**
     * 错误信息写入数据库(非线程池任务 e.g. URL为空,URL不合法)
     *
     * @param itmAppId
     * @param beginTime 应用url为空的时候认为正常 状态为004 响应时间为-1 ms
     */
    private void writeIntoDB(String itmAppId, Long beginTime) {
        //写入数据库记录
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> resultMap;
        resultMap = new HashMap<String, Object>();
        resultMap.put("begin_time", format.format(beginTime));
        resultMap.put("app_id", itmAppId);
        resultMap.put("monitor_time", format.format(System.currentTimeMillis()));
        resultMap.put("response_time", "-1");
        resultMap.put("monitor_status", "004");
        resultMap.put("netCode", netCode);
        appStateService.insertResponsAppMonitor(resultMap);
    }
}
