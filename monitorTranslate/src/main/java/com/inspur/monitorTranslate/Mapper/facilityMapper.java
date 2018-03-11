package com.inspur.monitorTranslate.Mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by xingwentao on 2017/12/7.
 */
@Mapper
public interface facilityMapper {
    public Long getLastId(String sql);
    public Long getPingStatusMaxId(String sql);
    public void updateOldId(String sql);
    public String getInitSource(String type);
    public String getInitMx(String type);
}
