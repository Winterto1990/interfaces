package com.inspur.hksso_union_hy.Mapper;

import org.apache.ibatis.annotations.Mapper;
import com.inspur.hksso_union_hy.Bean.deviceBean;
import java.util.List;
import java.util.Map;

/**
 * Created by xingwentao on 2017/10/12.
 */
@Mapper
public interface videoMsgMapper {
    List<String> selectCompany();
    public void SaveAuditLog(Map<String, Object> data);
    public void saveDevice(deviceBean deviceBeans);
    public void savePosition(Map<String, Object> result);
    public void saveQualityDiagnosis(Map<String, Object> result);
}
