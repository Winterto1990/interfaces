package com.inspur.monitorTranslate.Controller;

import com.alibaba.fastjson.JSON;
import com.inspur.monitorTranslate.Mapper.facilityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xingwentao on 2017/12/7.
 */
@RestController
public class facilityController {
    private final Logger logs = LoggerFactory.getLogger(facilityController.class);
    @Autowired
    private facilityMapper facility;
    @Value("${spring.socket.ip}")
    private  String ip;
    @Value("${spring.socket.port}")
    private int port;
    @Value("${jdbcurl}")
    private String jdbcurl;
    @Value("${jdbcclass}")
    private String jdbcclass;
    @Value("${id_name}")
    private String id_name;
    @Value("${sqlinit}")
    private String sqlinit;
    @Value("${get_last_id}")
    private String get_last_id;
    @Value("${get_ping_status_max_id}")
    private String get_ping_status_max_id;
    @Value("${colume_name}")
    private String colume_name;
    @Value("${colume_value}")
    private String colume_value;
    @Value("${update_old_id}")
    private String update_old_id;
    @Value("${datasource_ini}")
    private String datasource_ini;
    @Value("${mxini}")
    private String mxini;
    @Value("${datasource_type}")
    private String datasourceType;
    @Scheduled(cron = "0 0/1 * * * *")    //每1分钟调用一次
    public void facility() {
//        if(equ_schedule_open.equals("1")) return;
        Socket s = null;
        try {
            Long oldId = facility.getLastId(get_last_id);//上一次拉取的自增id
            Long maxId = facility.getPingStatusMaxId(get_ping_status_max_id);//目前表中最大的自增id值
            String tast = update_old_id+maxId+" where  " + colume_name + "='" + colume_value + "'";
            facility.updateOldId(update_old_id+maxId+" where  " + colume_name + "='" + colume_value + "'");
            Map<String,Object> temp = new HashMap();
            temp.put("password","root");
            temp.put("jdbcurl",jdbcurl);
            temp.put("type","sql");
            temp.put("class",jdbcclass);
            temp.put("username","root");
            temp.put("sql",sqlinit +
                    " and " + id_name + " > " + oldId + " and "+ id_name +" <= " + maxId);
            String result = JSON.toJSONString(temp);
            logs.warn(result);
            logs.warn("start push.");
            s = new Socket(ip,port);
            DataOutputStream ds = new DataOutputStream(s.getOutputStream());
            ds.write(result.getBytes());
            ds.flush();
            DataInputStream dis = new DataInputStream(new BufferedInputStream(s.getInputStream()));
            System.out.println(dis.toString());
            byte[] d = new byte[1024];
            s.getInputStream().read(d);
            String ss = new String(d);
            logs.warn(ss);
            logs.warn("end push.");
        } catch (Exception e) {
            logs.warn(e.getMessage());
        }finally {
            if(s != null) try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Scheduled(fixedRate = 10 * 60 * 1000)  //
    public void initSource(){
        initSource(datasourceType);
        initMx(datasourceType);
    }
    public void initSource(String datasourceType){
        Socket s = null;
        String init="";
        try {
            init = facility.getInitSource(datasourceType);
            logs.warn("START.");
            logs.warn("初始化开始.");
            s = new Socket(ip, port);
            DataOutputStream ds = new DataOutputStream(s.getOutputStream());
            ds.write(init.getBytes("utf-8"));
            ds.flush();
//            DataInputStream dis = new DataInputStream(new BufferedInputStream(s.getInputStream()));
            BufferedReader dis = new BufferedReader(new InputStreamReader(s.getInputStream(),"utf-8"));
            System.out.println(dis.toString());
            byte[] d = new byte[1024];
            s.getInputStream().read(d);
            String ss = new String(d);
            logs.warn(ss);
            logs.warn("初始化结束.");
        }catch (Exception e){
            logs.warn(e.getMessage());
        }finally {
            if(s != null) try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void initMx(String datasourceType){
        Socket s = null;
        try {
            String init = facility.getInitMx(datasourceType);
            logs.warn("START.");
            logs.warn("初始化开始.");
            s = new Socket(ip, port);
            DataOutputStream ds = new DataOutputStream(s.getOutputStream());
            ds.write(init.getBytes("utf-8"));
            ds.flush();
//            DataInputStream dis = new DataInputStream(new BufferedInputStream(s.getInputStream()));
            BufferedReader dis = new BufferedReader(new InputStreamReader(s.getInputStream(),"utf-8"));
            System.out.println(dis.toString());
            byte[] d = new byte[1024];
            s.getInputStream().read(d);
            String ss = new String(d);
            logs.warn(ss);
            logs.warn("初始化结束.");
        }catch (Exception e){
            logs.warn(e.getMessage());
        }finally {
            if(s != null) try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
