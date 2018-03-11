package com.inspur.sp.web.mapper;

import com.inspur.sp.web.domain.MenuBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by xingwentao on 2017/7/11.
 */
@Mapper
public interface HkloginMapper {
    public List<Map<String,Object>> getOrganInfoFirst(@Param("organID") String organID, @Param("lastTime") String lastTime);
    public List<Map<String,Object>> getOrganInfoSecond(@Param("organID") String organID, @Param("lastTime") String lastTime);

    public List<MenuBean> findMenu(Map<String, Object> paramMap);

    public Map<String,Object> getUserInfo(@Param("userAccount") String userAccount,@Param("appCode")String appCode);

    public Map<String,Object> getOrganInfoOne(@Param("organID") String organID);

    public List<Map<String,Object>> getRoleCodeVaule(@Param("appCode")String appCode);

//    public Map<String,Object> login(String ip);
//    public Map<String,Object> getUserInfo(String id);
//    public List<Map<String,Object>> getSubMenuInfo(String id);
//    public List<Map<String,Object>> getRecourceInfo(String id);
//    public Map<String,Object> getOrganInfo(String id);
//    public Map<String,Object> getOrganId(String id);
}
