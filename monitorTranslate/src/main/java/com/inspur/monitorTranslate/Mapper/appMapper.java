package com.inspur.monitorTranslate.Mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface appMapper {
    public Long getLastId();
    public Long getPingStatusMaxId();
    public Long getPingStatusMaxIdNet();
    public List<Map<String,Object>> getBeteenData(Map<String,Object> param);
    public void updateOldId(Long news);
    public void updateOldIdNet(Long news);
}
