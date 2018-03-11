package com.inspur.userBehavior.cmd;

import com.inspur.userBehavior.data.AppCodeInfoBean;
import com.inspur.userBehavior.data.IndexValueBean;
import com.inspur.userBehavior.service.UserBehaviorInterface;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by xutongnian on 2017/5/25.
 */
@Component
public class UserBehaviorCmd {

     Logger logger = Logger.getLogger(UserBehaviorCmd.class);
    @Autowired
//    @Resource(name="userBehaviorService")
    private UserBehaviorInterface userBehaviorService;

    private static final String CHARSET="UTF-8";
    //维度信息
    private String[] strDimensionalityArray = {"Day","Country","Province","City","PageUrl","HourOfDay"};
    private String[] strColumsArray={
                "City",
                "Visits",
                "Visitors",
                "ApproxDistinctIPCount",
                "AvgLoadingDuration",
                "PageRefreshRate",
                "BounceRate",
                "AvgSessionPageViews",
                "AvgPageViewDepth",
                "AvgPageDuration",
                "SessionMouseClickCount"};
    private String[] strColumsArrayTop={
            "PageUrl",
            "Visits",
            "SessionPageViews",
            "AvgPageDuration",
            "AvgSessionPageViews",
            "BounceRate"
    };
    String[] strGroupBys = new String[1];
    long[] intArray = new long[2];
    /**
     * 开启定时任务
     * @throws
     */
//    @Scheduled(fixedDelay = 60000)
    @Scheduled(cron = "0 05 11 * * ?")
    public void execute() throws ExecutionException {

        //获取昨天日期，作为查询条件
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -1);// 今天-1天
        Date yesterday = calendar.getTime();

        intArray[0]=Long.valueOf(simpleDateFormat.format(yesterday));
        intArray[1]=Long.valueOf(simpleDateFormat.format(yesterday));

        List<IndexValueBean> resultList = new ArrayList<IndexValueBean>();
        //根据应用的编号请求用户行为分析数据
        List<AppCodeInfoBean> appInfoList = userBehaviorService.queryAppInfo();
        if (appInfoList.size()!=0){
            //应用遍历
            for (int m=0; m<appInfoList.size();m++){
                //维度遍历
                for (int n=0; n<strDimensionalityArray.length;n++){
                    String requestJson="";
                    strGroupBys[0] = strDimensionalityArray[n];//替换维度
                    if (strDimensionalityArray[n]=="HourOfDay"||strDimensionalityArray[n]=="Day"||strDimensionalityArray[n]=="Country"||strDimensionalityArray[n]=="PageUrl"){
                       if (strDimensionalityArray[n]=="PageUrl"){
                           strColumsArrayTop[0]=strDimensionalityArray[n];//替换维度
                           //组装请求JSON
                           requestJson = jsonBody(strColumsArrayTop,strGroupBys,intArray, appInfoList.get(m).getAppGuoShuangCode());
                       }else{
                           strColumsArray[0]=strDimensionalityArray[n];//替换维度
                           //组装请求JSON
                           requestJson = jsonBody(strColumsArray,strGroupBys,intArray, appInfoList.get(m).getAppGuoShuangCode());
                       }
                        System.out.println("请求的日期、国家、TOP为："+requestJson);
                    }else{
                        strColumsArray[0]=strDimensionalityArray[n];//替换维度
                        //组装请求JSON
                        requestJson = jsonBody(strColumsArray,strGroupBys,intArray, appInfoList.get(m).getAppGuoShuangCode());
                        System.out.println("请求的Json为："+requestJson);
                    }

                    resultList = analysisResult(requestJson,appInfoList.get(m).getAppCode(),strDimensionalityArray[n]);
                    System.out.println("返回结果："+resultList);
                    //调用数据库操作，存储请求回来的数据
                    if (!resultList.isEmpty()){
                        //存入数据库操作
                        userBehaviorService.insertUserBehaviorData(resultList,strDimensionalityArray[n]);
                        resultList.clear();
                    }else{
                        System.out.println("请求结果为空！");
                    }
                }
            }
        }else{
            System.out.println("未找到应用编号！");
        }
    }

    /**
     * 发送请求并处理返回结果
     * @param requestJson
     * @return
     */
    public  List<IndexValueBean>  analysisResult ( String requestJson, String appCode,String dimensionality){
        String pathURL = getURL();
        //发送请求
        String strResult = httpClientPost(pathURL,requestJson);
        List<IndexValueBean> resultList = new ArrayList<IndexValueBean>();
        //处理返回结果
        try {
            JSONObject resultJson = new JSONObject();
            resultJson = JSONObject.fromObject(strResult);
            JSONArray jsonArrayColumns = new JSONArray();
            jsonArrayColumns  =  (JSONArray)resultJson.get("headers");
            JSONArray jsonArrayValue = new JSONArray();
            //如果是一次只推送一条数据，则此处的大小应该是1，多条是>1
            jsonArrayValue = resultJson.getJSONArray("data");

            if ((jsonArrayValue!=null)&&(jsonArrayColumns!=null)){
                for (int j=0;j<jsonArrayValue.size();j++){
                    JSONArray jsonArrayValueLast = new JSONArray();
                    jsonArrayValueLast = jsonArrayValue.getJSONArray(j);
                    if (jsonArrayValueLast.size()==jsonArrayColumns.size()){
                        //创建存储bean
                        IndexValueBean indexNameAndValue = new IndexValueBean();
                        int i=0;
                        //判断是否是top10指标
                        if (dimensionality.equals(strDimensionalityArray[4])){
                            indexNameAndValue.setAppCode(appCode);
                            indexNameAndValue.setDimensionality(jsonArrayValueLast.getString(i++));
                            indexNameAndValue.setVisits(jsonArrayValueLast.getString(i++));
                            indexNameAndValue.setSessionPageViews(jsonArrayValueLast.getString(i++));
                            indexNameAndValue.setAvgPageDuration(jsonArrayValueLast.getString(i++));
                            indexNameAndValue.setAvgSessionPageViews(jsonArrayValueLast.getString(i++));
                            indexNameAndValue.setBounceRate(jsonArrayValueLast.getString(i++));
                        }else {
                            indexNameAndValue.setAppCode(appCode);
                            indexNameAndValue.setDimensionality(jsonArrayValueLast.getString(i++));
                            indexNameAndValue.setVisits(jsonArrayValueLast.getString(i++));
                            indexNameAndValue.setVisitors(jsonArrayValueLast.getString(i++));
                            indexNameAndValue.setApproxDistinctIPCount(jsonArrayValueLast.getString(i++));
                            indexNameAndValue.setAvgLoadingDuration(jsonArrayValueLast.getString(i++));
                            indexNameAndValue.setPageRefreshRate(jsonArrayValueLast.getString(i++));
                            indexNameAndValue.setBounceRate(jsonArrayValueLast.getString(i++));
                            indexNameAndValue.setAvgSessionPageViews(jsonArrayValueLast.getString(i++));
                            indexNameAndValue.setAvgPageViewDepth(jsonArrayValueLast.getString(i++));
                            indexNameAndValue.setAvgPageDuration(jsonArrayValueLast.getString(i++));
                            indexNameAndValue.setSessionMouseClickCount(jsonArrayValueLast.getString(i));
                        }

                        //存储一条数据
                        resultList.add(indexNameAndValue);
                    }else{
                        System.out.println("解析过程中指标名称与值数量无法对应！");
                    }
                }
            }else{
                System.out.println("返回的数据中名称或数值为空！");
            }
        }catch (Exception e){
            logger.debug(e.getMessage());
        }

        return resultList;
    }

    /**
     * 获取访问URL
     * @return
     */
    public String getURL(){
        String method = "POST";
        String path = "/api/v3/drilldown/query";
        String sharedSecret = "2f7372c6875d13f9399cd07d0250eab288f7454f4b94ddfbd263c29b55c633c6";

        //获取当前时间戳，将13位转为10位（国双文档里是10位）
        long timestamptemple = new Date().getTime();

        String timestamptemple_str = String.valueOf(timestamptemple);
        String timestamp_str=timestamptemple_str.substring(0,timestamptemple_str.length()-3);
        long timestamp = Long.valueOf(timestamp_str);

        String signature ="";
        try{
            signature = computeSignature(sharedSecret, method, path, timestamp);
        }catch (Exception e){
            logger.debug(e.getMessage());
        }
        //组装URL
        String pathURL="http://192.1.40.7/api/v3/drilldown/query?apikey=5927a3475b1b441390791d4e&sig=";
        pathURL=pathURL+signature+"&timestamp="+timestamp;
        System.out.println("访问URL:"+pathURL);
        return pathURL;
    }
    /**
     * 组装发送Json
     * @param strColumnsArray
     * @param strGroupBys
     * @param intArray
     * @param type
     * @return
     */
    public  String jsonBody (String[] strColumnsArray,String[] strGroupBys,long[] intArray,int type){
        JSONObject jsonObjectTotal = new JSONObject();
        JSONArray jsonArrayFilter = new JSONArray();

        ///Begin构建Columns和GroupBys////////////////////
        jsonObjectTotal.put("Columns",strColumnsArray);
        jsonObjectTotal.put("GroupBys",strGroupBys);
        ///end构建Columns和GroupBys////////////////////

        ////Begin构建Filter////////////////////
        JSONObject jsonObject4 = new JSONObject();
        jsonObject4.put("FilterItemType",1);
        jsonObject4.put("FilterOn","Day");
        jsonObject4.put("FilterType",6);
        jsonObject4.put("FilterValues",intArray);

        jsonArrayFilter.add(jsonObject4);
        if ((!strGroupBys[0].equals("HourOfDay"))&&(!strGroupBys[0].equals("Day"))&&(!strGroupBys[0].equals("Country"))&&(!strGroupBys[0].equals("PageUrl"))){
            String[] strFilterValuesArray={"中国"};

            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("filterValues",strFilterValuesArray);
            jsonObject1.put("filterOn","Country");
            jsonObject1.put("filterType",0);
            jsonObject1.put("filterItemType",1);
            JSONArray jsonArray1 = new JSONArray();
            jsonArray1.add(jsonObject1);


            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("filterItemType",0);
            jsonObject2.put("concatType",0);
            jsonObject2.put("children",jsonArray1);

            JSONArray jsonArray2 = new JSONArray();
            jsonArray2.add(jsonObject2);

            JSONObject jsonObject3 =new JSONObject();
            jsonObject3.put("filterItemType",0);
            jsonObject3.put("concatType",1);
            jsonObject3.put("children",jsonArray2);

            jsonArrayFilter.add(jsonObject3);
        }

        JSONObject jsonObjectFilter = new JSONObject();
        jsonObjectFilter.put("Children",jsonArrayFilter);
        ////end构建Filter////////////////////

        jsonObjectTotal.put("Filter",jsonObjectFilter);

        ///Begin构建Options//////////////////////
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Key","ProfileId");
        jsonObject.put("Value",type);

        JSONObject jsonObject5 = new JSONObject();
        jsonObject5.put("Key","Culture");
        jsonObject5.put("Value","cn");

        JSONArray jsonArray3 = new JSONArray();
        jsonArray3.add(jsonObject);
        jsonArray3.add(jsonObject5);

        JSONObject jsonObject6 = new JSONObject();
        jsonObject6.put("OptionPairs",jsonArray3);
        ///End构建Options//////////////////////
        jsonObjectTotal.put("Options",jsonObject6);

        ////Begin构建OrderBys/////////////
        JSONObject jsonObject7 = new JSONObject();
//        if ((strGroupBys[0].equals("HourOfDay"))||(strGroupBys[0].equals("Day"))){
//            jsonObject7.put("OrderColumn",strGroupBys[0]);
//        }else{
            jsonObject7.put("OrderColumn","Visits");
//        }
        jsonObject7.put("OrderDirection",1);

        JSONArray jsonArray4 = new JSONArray();
        jsonArray4.add(jsonObject7);
        ////End构建OrderBys/////////////

        jsonObjectTotal.put("OrderBys",jsonArray4);

        if (strGroupBys[0].equals("PageUrl")){
            JSONObject jsonObject8 = new JSONObject();
            jsonObject8.put("PageIndex",0);
            jsonObject8.put("PageSize",10);
            jsonObjectTotal.put("Paging",jsonObject8);
        }

        return jsonObjectTotal.toString();
    }
    /**
     *  httpPost请求
     * @param urlParam
     * @param jsonparam
     * @return
     */
    public static String httpClientPost(String urlParam, String jsonparam){
        StringBuffer resultBuffer = new StringBuffer();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(urlParam);
        httpPost.addHeader("Content-Type","application/json");
        int CONNECTION_TIMEOUT = 10000;
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(CONNECTION_TIMEOUT)
                .setConnectTimeout(CONNECTION_TIMEOUT)
                .setSocketTimeout(CONNECTION_TIMEOUT)
                .build();

        httpPost.setConfig(requestConfig);

        BufferedReader bufferedReader = null;
        try{
            if (jsonparam!=null){
                StringEntity stringEntity = new StringEntity(jsonparam, Charset.forName("UTF-8"));
               // UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);
                httpPost.setEntity(stringEntity);
            }
            HttpResponse response = httpClient.execute(httpPost);

            //读取服务器响应数据
            if (response.getStatusLine().getStatusCode() == 200){
                System.out.println("请求响应成功");
               // resultBuffer = new StringBuffer();
                bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),CHARSET));
                String temp=null;
                while ((temp = bufferedReader.readLine())!=null){
                    resultBuffer.append(temp);
                }
            }else{
                System.out.println("请求失败，状态码："+response.getStatusLine().getStatusCode());
            }
        }catch (Exception e){
            System.out.println("请求URL超时！");
            throw  new RuntimeException(e);
        }finally {
            try{
                httpClient.close();
                if (bufferedReader!=null){
                    bufferedReader.close();
                }
            }catch (IOException e){
                bufferedReader = null;
                throw new RuntimeException(e);
            }
        }
        return resultBuffer.toString();
    }

    /***
     * 产生sig认证字符串
     * @param sharedSecret
     * @param method
     * @param path
     * @param timestamp
     * @return
     * @throws Exception
     */
    private static final String computeSignature(String sharedSecret, String method, String path, long timestamp)
            throws Exception {
        String message = method + path + String.valueOf(timestamp);
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(message.getBytes());
        byte[] digest = messageDigest.digest();

        SecretKeySpec keySpec = new SecretKeySpec(sharedSecret.getBytes(), "HmacMD5");
        Mac mac = Mac.getInstance("HmacMD5");
        mac.init(keySpec);

        mac.update(digest);
        byte[] hash = mac.doFinal();
        String signature = bytesToHexString(hash);
        return signature;

    }

    private static final String bytesToHexString(byte[] bytes) {
        StringBuffer strBuffer = new StringBuffer(bytes.length * 2);
        String hex;
        for (int i = 0; i < bytes.length; i++) {
            hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() < 2)
                strBuffer.append(0);
            strBuffer.append(hex.toLowerCase());
        }
        return strBuffer.toString();
    }
}
