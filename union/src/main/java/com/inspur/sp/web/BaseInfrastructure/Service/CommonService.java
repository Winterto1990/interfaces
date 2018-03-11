package com.inspur.sp.web.BaseInfrastructure.Service;

import com.inspur.sp.web.mapper.CommonMapper;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by xingwentao on 2017/10/13.
 */
@Service("commonService")
@Transactional
public class CommonService{
    @Autowired
    private CommonMapper mapper;
    public List<String> getCompanyList(){
        List<String> data = mapper.selectCompany();
        return data;
    }
    /***
     * 查看对应的id是否在表中存在
     */
    public boolean getSearchId(String id){
        Map<String,Object> result = mapper.getSearchId(id);
        return (Long) result.get("count")>0;
    }

    /***
     * 根据INTERFACE_ID查找表interface_config中对应表的所有字段，且包含表名
     * @param id
     * @return 表名
     */
    public List<Map> getTableData(String id){
        List<Map> properties = mapper.getProperties(id);
        return properties;
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
        mapper.saveData(exeSql);
    }

    /***
     * 将数据插入操作写入数据表中，其中使用标志位表明该操作是否成功
     * @param port
     * @param info
     */

    public long SaveLogs(String uuid,String interfaceName,String creator){
        mapper.SaveAuditLogs(getAuditLogs(uuid,interfaceName,creator));
        return mapper.getLogId(uuid);
    }

    public Map<String,Object> getAuditLogs(String uuid,String interfaceName,String creator){
        Map<String,Object> m = new HashedMap();
        m.put("uuid", uuid);
        m.put("method",""+interfaceName);
        m.put("hostname","172");
        m.put("operator",creator);
        m.put("operate_time",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return m;
    }

    public void UpdateAuditLog(String uuid){
        mapper.UpdateAuditLog(uuid);
    }
}
