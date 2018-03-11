package com.appmonitor.appstatemonitor.action;

import com.appmonitor.appstatemonitor.po.ConnResult;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by Jason on 12/01/2017.
 */
public class ConnTask implements Callable<ConnResult> {

    private String itmAppId;
    private String itmAppName;
    private String itmAppIp;
    private int itmAppPort;
    private String pingFlag;
    private String urlStr;
    private Long ipErrTime;
    private Long telnetErrTime;
    private Long accessErrTime;

    public ConnTask(String itmAppId, String itmAppName, String itmAppIp, int itmAppPort, String urlStr,String pingFlag) {
        this.itmAppId = itmAppId;
        this.itmAppName = itmAppName;
        this.itmAppIp = itmAppIp;
        this.itmAppPort = itmAppPort;
        this.urlStr = urlStr;
        this.pingFlag = pingFlag;
    }


    public Long getIpErrTime() {
        return ipErrTime;
    }

    public void setIpErrTime(Long ipErrTime) {
        this.ipErrTime = ipErrTime;
    }

    public Long getTelnetErrTime() {
        return telnetErrTime;
    }

    public void setTelnetErrTime(Long telnetErrTime) {
        this.telnetErrTime = telnetErrTime;
    }

    public Long getAccessErrTime() {
        return accessErrTime;
    }

    public void setAccessErrTime(Long accessErrTime) {
        this.accessErrTime = accessErrTime;
    }

    public ConnResult call() throws Exception {
        boolean telnetResult = false;
        boolean accessResult = false;
        boolean ipResult = false;
        Map access = new HashMap();
        String responsetime = "0";
        String statusCode = "404";//接收判断链接返回的状态
        //ping 0206
//        if("1".equals(pingFlag) && !pingFlag.equals(null)){
//            String resultCode = Ping.ping(itmAppIp);
//            ipResult = resultCode == "001" ? true : false;
//            if (!ipResult) {
//                this.ipErrTime = System.currentTimeMillis();
//            }
//        }else {
//            ipResult = true;
//        }
        //telnet  0126
        if (itmAppIp != null && itmAppPort != 0) {
            telnetResult = ConnectionUtil.portTest(itmAppIp, itmAppPort);
            if (!telnetResult) {
                this.telnetErrTime = System.currentTimeMillis() + 1000;
            }
        } else {    //ip 或 port 不完整
            telnetResult = false;
            this.telnetErrTime = System.currentTimeMillis() + 1000;
        }
        //访问url   0126
        access = ConnectionUtil.accessTest(urlStr);
        accessResult = (Boolean) access.get("isValid");

        responsetime = access.get("responstime").toString();
        statusCode = access.get("statusCode").toString();
        if (!accessResult) {
            this.accessErrTime = System.currentTimeMillis() + 2000;
        }

        ConnResult result = new ConnResult(itmAppId, itmAppIp, itmAppName, urlStr,
                ipResult, telnetResult, accessResult,
                responsetime, statusCode,
                ipErrTime, telnetErrTime, accessErrTime);
        return result;
    }


}
