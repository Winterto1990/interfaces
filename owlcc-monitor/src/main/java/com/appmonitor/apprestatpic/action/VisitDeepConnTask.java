package com.appmonitor.apprestatpic.action;

import com.appmonitor.apprestatpic.po.VisitDeepResponesTime;
import com.appmonitor.utils.ConnectionUtil;
import com.appmonitor.utils.RenderTimeByUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by Jason on 12/01/2017.
 */
public class VisitDeepConnTask implements Callable<VisitDeepResponesTime> {

//    private String[] AppUrlArray = null;
    private String FirstUrl = null;
    private String AppName = null;
    private String AppId = null;
//    private int UrlNum = 0;//配置应用链接个数
//    private double AvgTime = 0.00;//平均响应时间
//    private String VisitResponesUrl = new String();
    private long VisitResponesTimePre ;
//    private long VisitTotalTime = 0;

    private String modelName = null;


    public VisitDeepConnTask(String appId,String appName, Long visitDeepTimePre,String firstPageUrl,String modelName) {
//        AppUrlArray = appUrlArray;
        this.AppId = appId;
        this.AppName = appName;
        this.VisitResponesTimePre = visitDeepTimePre;
        this.FirstUrl = firstPageUrl;
        this.modelName = modelName;

    }

    public VisitDeepResponesTime call() throws Exception {
        boolean accessResult = false;
        Map access = new HashMap();
        String responsetime = "600000";
        String renderTime = "0";
//        Long unionTime = 0L;//响应时间与渲染时间和
//        List<VisitDeepResponesTime> results = new ArrayList<VisitDeepResponesTime>();
//        for (int i = 2; i < AppUrlArray.length; i++) {
//            AppId = AppUrlArray[0];
//            AppName = AppUrlArray[1];
//            FirstUrl = AppUrlArray[2];  // 获取模块名称和url的集合  首页:https://www.baidu.com/;登:https://www.baidu.com/
//            UrlNum = AppUrlArray.length-2;
//            //访问url
//            System.out.println("access --- " + AppUrlArray[i]);
//            //每个URL以#连接
//            VisitResponesUrl = VisitResponesUrl + AppUrlArray[i] + "#";
//            access = ConnectionUtil.accessTest(AppUrlArray[i]);
//            accessResult = (Boolean) access.get("isValid");
//            //访问成功记录时间
//            if (accessResult) {
//                responsetime = access.get("responstime").toString();
//                renderTime = RenderTimeByUrl.getRenderTime(AppId, AppName, AppUrlArray[i]);//渲染时间
//                ///////////////////调用渲染返回时间//////
//            } else {
//                //访问不成功时，再进行三次的访问，成功一次即终止
//                System.out.println(AppUrlArray[i] + "访问不成功，正在进行再次尝试！");
//                for (int j = 0; j < 3; j++) {
//                    access = ConnectionUtil.accessTest(AppUrlArray[i]);
//                    accessResult = (Boolean) access.get("isValid");
//                    //访问成功记录时间
//                    if (accessResult) {
//                        responsetime = access.get("responstime").toString();
//                        renderTime = RenderTimeByUrl.getRenderTime(AppId, AppName, AppUrlArray[i]);//渲染时间
//                        ///////////////////调用渲染返回时间//////
//                        break;
//                    } else {
//                        System.out.println(AppUrlArray[i] + "访问不成功！");
//                    }
//                }
//            }
//            unionTime = Long.valueOf(responsetime) + Long.valueOf(renderTime);
//            //每个URL返回时间以#连接
//            VisitResponesTimePre = VisitResponesTimePre + unionTime + "#";
//            VisitTotalTime = VisitTotalTime + Long.valueOf(responsetime) + Long.valueOf(renderTime);
//            AvgTime = (double) VisitTotalTime/UrlNum;
//        }
//        AppId = AppUrlArray[0];
//        AppName = AppUrlArray[1];
//        FirstUrl = AppUrlArray[2];  // 获取模块名称和url的集合  首页:https://www.baidu.com/;登:https://www.baidu.com/
//        UrlNum = AppUrlArray.length-2;
        //访问url
        System.out.println("access --- " + FirstUrl);
//        String[] arrayUrls = FirstUrl.split(";");  //对应的名称:URL
//        for(String url_name: arrayUrls) {
//            String[] nameUrl = url_name.split(","); // 对应着名称 + URL
//            modelName = nameUrl[0];
//            FirstUrl = nameUrl[1];
            //每个URL以#连接
//        VisitResponesUrl = VisitResponesUrl + AppUrlArray[2] + "#";
            access = ConnectionUtil.accessTest(FirstUrl);
            accessResult = (Boolean) access.get("isValid");
            //访问成功记录时间
            if (accessResult) {
                responsetime = access.get("responstime").toString();
                renderTime = RenderTimeByUrl.getRenderTime(AppId, AppName, FirstUrl);//渲染时间
                ///////////////////调用渲染返回时间//////
            } else {
                //访问不成功时，再进行三次的访问，成功一次即终止
                System.out.println(FirstUrl + "访问不成功，正在进行再次尝试！");
                for (int j = 0; j < 3; j++) {
                    access = ConnectionUtil.accessTest(FirstUrl);
                    accessResult = (Boolean) access.get("isValid");
                    //访问成功记录时间
                    if (accessResult) {
                        responsetime = access.get("responstime").toString();
                        renderTime = RenderTimeByUrl.getRenderTime(AppId, AppName, FirstUrl);//渲染时间
                        ///////////////////调用渲染返回时间//////
                        break;
                    } else {
                        System.out.println(FirstUrl + "访问不成功！");
                    }
                }
            }

//        unionTime = Long.valueOf(responsetime) + Long.valueOf(renderTime);
        //每个URL返回时间以#连接
//        VisitResponesTimePre = VisitResponesTimePre + unionTime + "#";
//        VisitTotalTime = VisitTotalTime + Long.valueOf(responsetime) + Long.valueOf(renderTime);
//        AvgTime = (double) VisitTotalTime/UrlNum;
        VisitResponesTimePre = Long.valueOf(responsetime) + Long.valueOf(renderTime);
        VisitDeepResponesTime result = new VisitDeepResponesTime(AppId, AppName,VisitResponesTimePre, FirstUrl,modelName);
        return result;
    }

}
