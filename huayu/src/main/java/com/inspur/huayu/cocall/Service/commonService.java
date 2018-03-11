package com.inspur.huayu.cocall.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.inspur.huayu.cocall.Mapper.commonMap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("commonService")
public class commonService {
    @Autowired
    private commonMap map;
    public List<String> getCompanyList(){
        List<String> data = map.selectCompany();
        return data;
    }
    //获取法院编码列表
    public List<String> getCode(){
        return map.getCode();
    }
    /***
     * 查看对应的id是否在表中存在
     */
    public boolean getSearchId(String id){
        Map<String,Object> result = map.getSearchId(id);
        return (Long) result.get("count")>0;
    }
    /***
     * 根据INTERFACE_ID查找表interface_config中对应表的所有字段，且包含表名
     * @param id
     * @return 表名
     */
    public List<Map> getTableData(String id){
        List<Map> properties = map.getProperties(id);
        return properties;
    }
    /***
     * 将数据插入操作写入数据表中，其中使用标志位表明该操作是否成功
     * @param port
     * @param info
     */
    public long SaveLogs(String uuid,String interfaceName,String creator){
        map.SaveAuditLogs(getAuditLogs(uuid,interfaceName,creator));
        return map.getLogId(uuid);
    }
    public Map<String,Object> getAuditLogs(String uuid,String interfaceName,String creator){
        Map<String,Object> m = new HashMap<>();
        m.put("uuid", uuid);
        m.put("method",""+interfaceName);
        m.put("hostname","172");
        m.put("operator",creator);
        m.put("operate_time",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return m;
    }
    /***
     * 将controller中的map拼串插入到数据表中
     * @param data
     */
    public void saveData(Map<String,Object> data,String tableName){
        String exeSql = "insert into " + tableName + "(";
        int len = data.size(),k = 0;
        for(Map.Entry<String,Object> temp : data.entrySet()){
            if(k<len-1) exeSql += temp.getKey() + ",";
            else exeSql += temp.getKey() + ") values (";
            k ++;
        }
        k = 0;
        for(Map.Entry<String,Object> temp : data.entrySet()){
            if(k<len-1){
                if(temp.getValue() == null)
                    exeSql += temp.getValue() + ",";
                else exeSql += "'" + temp.getValue() + "',";
            }
            else{
                if(temp.getValue() == null)
                    exeSql += temp.getValue() + ")";
                else exeSql += "'" + temp.getValue() + "')";
            }
            k ++ ;
        }
        map.saveData(exeSql);
    }
    public void UpdateAuditLog(String uuid){
        map.UpdateAuditLog(uuid);
    }
}
