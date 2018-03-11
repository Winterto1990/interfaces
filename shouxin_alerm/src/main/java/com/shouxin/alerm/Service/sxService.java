package com.shouxin.alerm.Service;


import com.shouxin.alerm.Datasource.TargetDataSource;
import com.shouxin.alerm.Mapper.translateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

/**
 * Created by xingwentao on 2018/2/3.
 */
@Service
@EnableAutoConfiguration
public class sxService {
    @Autowired
    private translateMapper mapper;
    //     查询221数据库中的hardware_resource_idrelation中的uuid
    @TargetDataSource(name="ds1")
    public  String getHardwareResourceUuid(String subUuid){
        return mapper.getResourceIdSx(subUuid).get("uuid").toString();
    }
}
