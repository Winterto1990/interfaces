package com.inspur.sp.web.BaseInfrastructure.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.inspur.sp.web.BaseInfrastructure.Service.appTaskService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;


/**
 * Created by xingwentao on 2017/9/6.
 */
@Component
public class ColdDataStorage {
    @Autowired
    private appTaskService appTaskService;
    @Scheduled(fixedDelay=7*24*60*60*1000)
    public void qwert() {
//        String test = "{'message':'123','data':{'dataList':[{'bfyq' : '是','cslx':'太极','fyname' : '最高','gxpl' : '2'," +
//                "'gxplmc' : '超过一周未更新','jlscgxts' : '13','netname' : '互联网应用'," +
//                "'scqlgxsj' : '2016-07-14','sjzl' : '1.65','systemname' : '中国执行信息公开网','zbts' : '9,746,348','zrcs' : '系统研发处','zxgxsj' : '2017-08-14'}]}," +
//                "'hjpersontage' : '100.00%（应备56,已备56）','rksj':{'date' : '27','day' : '0','hours' : 7,'minutes' : '44','month' : '7','nanos' : '0'," +
//                "'seconds' : '26','time' : '1503791066000','timezoneOffset' : '-480','year' : '117'},'susccess':'true'}";

        String bodyParam = "startTime=&endTime=&sjlx=0&from=0&limit=117&flag=1&fyid=000000&sswy=0&yyxt=&zrcs=&gs=";
        URL url = null;
        String uuid  = "";
        long increaseid = 0;
        try {
            url = new URL("http://192.1.36.74:8181/drsp-ebsm-server/api/services/d0764d10cc77419eaa35ed55f8063abc");
//            url = new URL("http://127.0.0.1:3150/put");

            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
             httpConn.setRequestProperty("drsp_ticket", "cf21d249ad7c4eb2a8a2debdbee3da65");//ticket
            httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//类型为x-www-form-urlencoded

            httpConn.setDoOutput(true); //需要输出 GET方式的时候设置为false
            httpConn.setDoInput(true); //需要输入
            httpConn.setUseCaches(false); //不允许缓存
            httpConn.setRequestMethod("POST"); //设置POST方式连接

            httpConn.connect();

            //建立输入流，向写入body中指向的URL传入参数
            OutputStreamWriter ops = new OutputStreamWriter(httpConn.getOutputStream(), "UTF-8");
            if (StringUtils.isNotEmpty(bodyParam)) {
                ops.write(bodyParam);
            }
            ops.flush();
            IOUtils.closeQuietly(ops);
            //获得响应状态
            int resultCode = httpConn.getResponseCode();

            if (!httpConn.getHeaderField("X-Ca-Supporter-StatusCode").equals("200")) {
                //请求被平台拦截
                return;
            }
            BufferedReader responseReader = null;
            InputStreamReader input = null;
            try {
                input = new InputStreamReader(httpConn.getInputStream(), "UTF-8");
                responseReader = new BufferedReader(input);
                StringBuffer sb = new StringBuffer();
                String readLine = new String();
                while ((readLine = responseReader.readLine()) != null) {
                    sb.append(readLine).append("\n");
                }
                JSONObject result = JSONObject.parseObject(sb.toString());
                JSONObject datas = result.getJSONObject("data");
                String hjpersontage = datas.getString("hjpersontage");
                String title = datas.getString("title");
                JSONArray arr = datas.getJSONArray("dataList");
                uuid = UUID.randomUUID().toString();
                increaseid = appTaskService.SaveAuditLogs(uuid,"data_cold_storage","huayu");
                String df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                for(int i=0;i<arr.size();i++){
                    JSONObject o = (JSONObject) arr.get(i);
                    Map<String,Object> map = new HashedMap();
                    map.put("id", uuid);
                    map.put("bfyq",o.getString("bfyq"));
                    map.put("cslx",o.getString("cslx"));
                    map.put("fyname",o.getString("fyname"));
                    map.put("gxpl",o.getString("gxpl"));
                    map.put("gxplmc",o.getString("gxplmc"));
                    map.put("jlscgxts",o.getString("jlscgxts"));
                    map.put("netname",o.getString("netname"));
                    map.put("scqlgxsj",o.getString("scqlgxsj"));
                    map.put("sjzl",o.getString("sjzl"));
                    map.put("systemname",o.getString("systemname"));
                    map.put("zbts",o.getString("zbts"));
                    map.put("zrcs",o.getString("zrcs"));
                    map.put("zxgxsj",o.getString("zxgxsj"));
                    map.put("hjpersontage",hjpersontage);
                    map.put("title",title);
                    map.put("operatetime",df);
                    map.put("increaseid",increaseid);
                    appTaskService.saveColdStorage(map);
                }
                appTaskService.UpdateAuditLog(uuid);//更新插入状态数据
//                appTaskService.saveLog(uuid,df);
            } finally {
                IOUtils.closeQuietly(input);
                IOUtils.closeQuietly(responseReader);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
