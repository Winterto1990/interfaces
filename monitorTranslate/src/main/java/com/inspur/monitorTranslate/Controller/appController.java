package com.inspur.monitorTranslate.Controller;

import com.alibaba.fastjson.JSON;
import com.inspur.monitorTranslate.Mapper.appMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import org.apache.ibatis.type.TypeAliasRegistry;

@RestController
public class appController {
    private final Logger logs = LoggerFactory.getLogger(facilityController.class);
    @Autowired
    private appMapper facility;
//    @Value("${spring.socket.ip}")
    private  String ip;
//    @Value("${spring.socket.port}")
    private int port;
//    @Value("${jdbcurl}")
    private String jdbcurl;
//    @Value("${jdbcclass}")
    private String jdbcclass;
//    @Value("${sqlinit_app}")
    private String sqlinit_app;
//    @Value("${id_name_app}")
    private String id_name_app;
//    @Value("${app_schedule_open}")
    private String app_schedule_open;
//    @Scheduled(cron = "0 0/1 * * * *")    //每1分钟调用一次
    public void app() {
        if(app_schedule_open.equals("1")) return;
        Long oldId = facility.getLastId();//上一次拉取的自增id
        Long maxId = facility.getPingStatusMaxId();//目前表中最大的自增id值

        facility.updateOldId(maxId);
        Map<String,Object> temp = new HashMap();
        temp.put("password","root");
        temp.put("jdbcurl",jdbcurl);
        temp.put("type","sql");
        temp.put("class",jdbcclass);
        temp.put("username","root");
        temp.put("sql",sqlinit_app +
                " and " + id_name_app + " BETWEEN " + oldId + " and " + maxId);
//        List<Map<String,Object>> datas = new ArrayList<>();
//        datas = f.getDatas();
        String result = JSON.toJSONString(temp);
        logs.warn(result);
        Socket s = null;
        try {
            logs.warn("start push of facility.");
            s = new Socket(ip,port);
            DataOutputStream ds = new DataOutputStream(s.getOutputStream());
            ds.write(result.getBytes());
            ds.flush();
            DataInputStream dis = new DataInputStream(new BufferedInputStream(s.getInputStream()));
            System.out.println(dis.toString());
            byte[] d = new byte[1024];
            s.getInputStream().read(d);
            String ss = new String(d);
//            JSONObject res = JSON.parseObject(ss);
            logs.warn(ss);
            logs.warn("end push of facility.");
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
}
