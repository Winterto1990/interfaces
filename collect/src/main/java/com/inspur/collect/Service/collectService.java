package com.inspur.collect.Service;

import com.alibaba.fastjson.JSON;
import com.inspur.collect.Mapper.collectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Service("collectService")
public class collectService {
    @Autowired
    private collectMapper mapper;

    public List<Map<String,String>> dataTransferUp(List<Map> paramsList) {
        List<Map<String,String>> resultList = new ArrayList<>();
        if (null == paramsList && paramsList.size() <= 0){
            return null;
        }
        String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        for (int i = 0; i < paramsList.size(); i++) {
            Map param = paramsList.get(i);
            String tableName = param.get("tableName").toString();
            //1、查询这张表的oldId，查询这张表最后一条记录lastId
            Map<String,Long> numberMap = getProcessNumber(tableName);
            //2、查询oldId和lastId两者之间的记录，处理成一条数据,插入到collect表中
            //返回插入结果
            Map<String,String> resultmap = getBaseTableInfo(param,numberMap,nowDate);
            resultList.add(resultmap);
        }
        return resultList;
    }

    public List<Map<String,String>> dataTransferDown(
            Map<String,String[]> columnsMap,List<String> collectTableNames) {
        List<Map<String,String>> resultList = new ArrayList<>();
        for (int i = 0; i < collectTableNames.size(); i++) {
            String collectTableName = collectTableNames.get(i);
            //1、查询每张采集表的oldId，lastid
            Map<String,Long> numberMap = getProcessNumber(collectTableName);
            //2、解析每张采集表的数据
            Map<String,String> resultmap = parseCollectTableInfo(columnsMap,numberMap,collectTableName);
            resultList.add(resultmap);
        }
        return resultList;
    }

    private Map<String,Long> getProcessNumber(String tableName) {
        Map<String,Long> numberMap = new HashMap<>();
        Map oldMap = mapper.getOldIdByTableName(tableName);
        Map lastMap = mapper.getLastIdByTableName(tableName);
        long oldId = 0L;
        long lastId = 0L;
        if (null != oldMap){
            oldId = (long) oldMap.get("oldId");
        }
        if (null == lastMap){ //表中没有数据
            return null;
        }
        lastId = (long) lastMap.get("id");
        if (oldId >= lastId){
            return null;
        }
        numberMap.put("oldId",oldId);
        numberMap.put("lastId",lastId);
        return numberMap;
    }

    private boolean updateOldId(String tableName,long lastId){
        boolean result = false;
        Map oldMap = mapper.getOldIdByTableName(tableName);
        Map paramMap = new HashMap<>();
        paramMap.put("tableName",tableName);
        paramMap.put("old",lastId);
        int number = 0;
        if (null == oldMap) { //没有改表的记录，插入记录
            number = mapper.insertOldIdByTableName(paramMap);
        } else { //更新记录
            number = mapper.updateOldIdByTableName(paramMap);
        }
        if(number > 0){
            result = true;
        }
        return result;
    }

    private Map<String,String> getBaseTableInfo(Map param,Map<String,Long> numberMap,String nowDate){
        Map<String,String> resultMap = new HashMap<>();
        String tableName = param.get("tableName").toString();
        if (null == numberMap){
            resultMap.put("error","原始表"+tableName+"没有新入数据");
            return resultMap;
        }
        param.put("start",numberMap.get("oldId"));
        param.put("end",numberMap.get("lastId"));
        List<Map> list = mapper.tableListByTableName(param);
        if(null == list && list.size() <= 0){
            resultMap.put("error","原始表"+tableName+"没有新入数据");
            return resultMap;
        }
        //处理数据，向采集表插入数据
        Map paramMap = new HashMap<>();
        List jsonList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            jsonList.add(list.get(i));
        }
        String json = JSON.toJSONString(jsonList);
        paramMap.put("sourceTableName",tableName);
        paramMap.put("content",json);
        paramMap.put("tableName",param.get("collectTableName"));
        paramMap.put("nowDate",nowDate);
        int number = mapper.insertCollectOne(paramMap);
        boolean flag = false;
        if (number > 0) {
            //更新采集记录表ping_getid的old
            flag = updateOldId(tableName,numberMap.get("lastId"));
        }else{
            resultMap.put("error","插入采集表"+param.get("collectTableName")+"失败!");
        }
        if(!flag){
            resultMap.put("error","更新记录表ping_getid失败！");
        }else{
            resultMap.put("success",tableName + " success");
        }
        return resultMap;
    }

    private Map<String,String> parseCollectTableInfo(
            Map<String,String[]> columnsMap,
            Map<String, Long> numberMap, String collectTableName) {
        Map<String,String> resultMap = new HashMap<>();
        if (null == numberMap){
            resultMap.put("error","采集表"+collectTableName+"没有新入数据");
            return resultMap;
        }
        Map param = new HashMap<>();
        param.put("tableName",collectTableName);
        param.put("start",numberMap.get("oldId"));
        param.put("end",numberMap.get("lastId"));
        List<Map> collectList = mapper.collectListByTableName(param);
        if(null == collectList && collectList.size() <= 0){
            resultMap.put("error","采集表"+collectTableName+"没有新入数据");
            return resultMap;
        }
        Map tempList = new HashMap<>();
        //一条一条解析采集表数据
        for (int j = 0; j < collectList.size(); j++) {
            Map collect = collectList.get(j);
            String tableName = collect.get("tableName").toString();
            //解析采集表的数据到原始表
            try{
                boolean parseResult = parseTableInfo(columnsMap,collect);
                if (parseResult) {
                    tempList.put(tableName,"success");
                }else{
                    tempList.put(tableName,"error");
                }
            }catch (Exception e){
                tempList.put(tableName,"error");
                continue;
            }
        }
        boolean flag = false;
        if (tempList.containsValue("success")){ //有成功插入原始表的记录
            //更新采集记录表ping_getid的old
            flag = updateOldId(collectTableName,numberMap.get("lastId"));
        }else{
            resultMap.put("error","插入原始表失败!明细："+JSON.toJSONString(tempList));
        }
        if(!flag){
            resultMap.put("error","更新记录表ping_getid失败！");
        }else{
            resultMap.put("success",collectTableName + "更新原始表结果："+JSON.toJSONString(tempList));
        }
        return resultMap;
    }

    private boolean parseTableInfo(Map<String,String[]> columnsMap,Map collect){
        String tableName = collect.get("tableName").toString();
        String content = collect.get("content").toString();
        //String operateTime = collect.get("operateTime").toString();
        List<Map> tableList = JSON.parseArray(content,Map.class);
        String[] columns = columnsMap.get(tableName);
        String tableColumnStr = "";
        for (int i = 0; i < columns.length; i++) {
            tableColumnStr += columns[i]+",";
        }
        List<List> dataList = new ArrayList<>();
        for (int i = 0; i < tableList.size(); i++) {
            Map map = tableList.get(i);
            //Map dataMap = new HashMap<>();
            List data = new ArrayList<>();
            for (int j = 0; j < columns.length; j++) {
                if (map.containsKey(columns[j])){
                    //dataMap.put(columns[j],map.get(columns[j]));
                    data.add("'" + map.get(columns[j]) + "' as " + columns[j]);
                }else{
                    //dataMap.put(columns[j],null);
                    data.add(null + " as " + columns[j]);
                }
            }
            dataList.add(data);
        }
        Map params = new HashMap<>();
        params.put("tableName",tableName);
        params.put("tableColumnStr",tableColumnStr.substring(0,tableColumnStr.length()-1));
        params.put("dataList",dataList);
        int result= mapper.insertTableInfoByTableName(params);
        if (result > 0){
            return true;
        } else {
            return false;
        }
    }
}
