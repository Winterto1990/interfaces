package com.inspur.collect.Controller;

import com.inspur.collect.Service.collectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class collectController {
    private final Logger logs = LoggerFactory.getLogger(collectController.class);
    @Autowired
    private collectService collect;

    @Value("${direction}")
    private  String direction;
    @Value("${collect_tableName_one}")
    private  String collectTableNameOne;
    @Value("${collect_tableName_two}")
    private  String collectTableNameTwo;
    @Value("${specialTableName}")
    private  String specialTableName;
    @Value("${column}")
    private  String column;

    @Scheduled(cron = "0 0/1 * * * *")    //每5分钟调用一次
    public void collect(){
        if(StringUtils.isEmpty(column)){
            return;
        }

        if (!StringUtils.isEmpty(specialTableName)){
            specialTableName = specialTableName.trim();
        }
        if (!StringUtils.isEmpty(collectTableNameOne)){
            collectTableNameOne = collectTableNameOne.trim();
        }
        if (!StringUtils.isEmpty(collectTableNameTwo)){
            collectTableNameTwo = collectTableNameTwo.trim();
        }

        String[] specialTables = specialTableName.split(",");

        Map<String,String[]> columnsMap = new HashMap<>();
        List<Map> paramsList = new ArrayList<>();
        String[] tableColumns = column.split("&");
        for (int i = 0; i < tableColumns.length; i++) {
            String tableValue = tableColumns[i];
            String[] tableInfo = tableValue.split("\\|");
            if (null != tableInfo && tableInfo.length > 1) {
                String tableColumnStr = tableInfo[0];
                String tableName = tableInfo[1];
                String[] columns = tableColumnStr.split(",");
                String collectTableName = collectTableNameOne;
                for (int j = 0; j < specialTables.length; j++) {
                    if (tableName.equals(specialTables[j])){
                        collectTableName = collectTableNameTwo;
                        break;
                    }
                }
                Map tableMap = new HashMap();
                tableMap.put("tableColumnStr",tableColumnStr);
                tableMap.put("tableName",tableName);
                tableMap.put("collectTableName",collectTableName);
                paramsList.add(tableMap);
                columnsMap.put(tableName,columns);
            }
        }

        if("0".equals(direction))  //采集原始表到光闸表
        {
            List<Map<String,String>> result = collect.dataTransferUp(paramsList);
            log(result);
        }
        if("1".equals(direction))  //光闸表到采集表
        {
            List<String> collectTableNames = new ArrayList<>();
            collectTableNames.add(collectTableNameOne);
            collectTableNames.add(collectTableNameTwo);
            List<Map<String,String>> result = collect.dataTransferDown(columnsMap,collectTableNames);
            log(result);
        }
    }

    private void log(List<Map<String,String>> result){
        if (null == result){
            logs.warn("function 'dataTransferUp' no param " +
                    new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒").format(new Date()));
        }else{
            for (int i = 0; i < result.size(); i++) {
                Map<String,String> map = result.get(i);
                if (null != map.get("success")){
                    logs.info(map.get("success") +
                            new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒").format(new Date()));
                    continue;
                }
                if (null != map.get("error")){
                    logs.warn(map.get("error") +
                            new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒").format(new Date()));
                    continue;
                }
            }
        }
    }
}
