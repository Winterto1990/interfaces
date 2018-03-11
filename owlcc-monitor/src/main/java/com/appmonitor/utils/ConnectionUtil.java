package com.appmonitor.utils;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

public class ConnectionUtil {

    /**
     * 监控端口是否打开
     *
     * @param ip
     * @param port
     * @return
     */
    public static boolean portTest(String ip, int port) {
        boolean result = false;
        Socket socket = null;
        for (int i = 0; i < 2; i++) {
            try {
                socket = new Socket();
                InetSocketAddress socketAddress = new InetSocketAddress(ip, port);
                socket.connect(socketAddress, 2000);
                result = true;
                break;
            } catch (SocketTimeoutException e) {
                result = false;
                continue;
            } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println(" ----  无法连接" + ip + ":" + port);
                result = false;
                continue;
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return result;
    }

    /**
     * url链接是否可用延迟800mss
     *
     * @param url
     * @return
     */
    public static Map accessTest(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000)
                .build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        Map accessRes = new HashMap();
        boolean isValid = false;
        String accesStatus = "1";
        long responstime = 0;
        int statusCode = 404;

        for (int i = 0; i < 3; i++) {
            try {
                long nowDate = System.currentTimeMillis(); //Date.getTime() 获得毫秒型日期
                response = httpClient.execute(httpGet);
                statusCode = response.getStatusLine().getStatusCode();

                long endDate = System.currentTimeMillis();
                if (statusCode == 200) {
                    accesStatus = "2";
                    isValid = true;
                    responstime = endDate - nowDate;
                    accessRes.put("isValid", isValid);
                    accessRes.put("responstime", responstime);
                    accessRes.put("statusCode", statusCode);

                } else {
                    accessRes.put("isValid", isValid);
                    accessRes.put("responstime", responstime);
                    accessRes.put("statusCode", statusCode);
                }
                if (!isValid) {
                    continue;
                } else {
                    break;
                }
            } catch (Exception e) {
                accessRes.put("isValid", isValid);
                accessRes.put("responstime", responstime);
                accessRes.put("statusCode", statusCode);
                if (!isValid) {
                    continue;
                } else {
                    break;
                }
            } finally {
                if (response != null) {
                    try {
                        response.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
        return accessRes;
    }

}
