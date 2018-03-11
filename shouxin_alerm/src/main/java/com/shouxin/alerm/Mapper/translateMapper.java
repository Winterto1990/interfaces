package com.shouxin.alerm.Mapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
public interface translateMapper {
    public Map<String,Object>  getResourceIdHw(String subUuid);
    public Map<String,Object>  getResourceIdSx(String subUuid);
    public Map<String,Object>  sxMaxId();
    public Map<String,Object>  sxFromId(String sxTaskId);
    public void updateSxMax(Long max);
    public Map<String,Object> getCreatUuid(String itemId);
    public List<Map<String,Object>> getSXalermDatas(Map<String,Object> map);
    public Map<String,Object> getHWalermLevel(String title);
    public List<String> getWBZWhwAlermData(Map<String,Object> map);
    public String getWBZWmaxId();
    public String getWBZWoldId();
    public void updateLastMaxId(Map<String,Object> map);
}
