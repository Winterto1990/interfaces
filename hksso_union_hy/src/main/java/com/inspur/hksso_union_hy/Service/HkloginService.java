package com.inspur.hksso_union_hy.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.inspur.hksso_union_hy.Bean.MenuBean;
import com.inspur.hksso_union_hy.Datasource.TargetDataSource;
import com.inspur.hksso_union_hy.Mapper.HkloginMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xingwentao on 2017/6/22.
 */
@Service("hkloginService")
@Transactional
public class HkloginService {
    @Autowired
    private HkloginMapper hkloginMapper;
    private List<List<Map<String,Object>>> totalResultList= new ArrayList<List<Map<String,Object>>>();
    /**
     * 海康查询组织机构
     * @param OrganID
     * @return
     */
    @TargetDataSource(name="ds1")
    public String queryOrganInfo(String OrganID,String lastTime){
        List<Map<String,Object>> organInfoList = hkloginMapper.getOrganInfoFirst(OrganID,lastTime);
        JSONArray jsonArray = new JSONArray();
        totalResultList.clear();
        try{
            getTotalOrganInfo(organInfoList,OrganID,lastTime);

            //将最终所查结果组装成JSON字符串
            if (!totalResultList.isEmpty()){
                for (int k=0;k<totalResultList.size();k++){

                    List<Map<String,Object>> organInfoListTemp = totalResultList.get(k);
                    for (int i=0;i<organInfoListTemp.size();i++){

                        JSONObject jsonObject = new JSONObject();

                        jsonObject.put("id",organInfoListTemp.get(i).get("ID"));
                        jsonObject.put("code",organInfoListTemp.get(i).get("CODE"));
                        jsonObject.put("name",organInfoListTemp.get(i).get("NAME"));
                        jsonObject.put("short_name",organInfoListTemp.get(i).get("SHORT_NAME"));
                        jsonObject.put("court_num",organInfoListTemp.get(i).get("COURT_NUM"));
                        jsonObject.put("parent_code",organInfoListTemp.get(i).get("PARENT_CODE"));
                        jsonObject.put("organ_type",organInfoListTemp.get(i).get("ORGAN_TYPE"));
                        jsonObject.put("court_type",organInfoListTemp.get(i).get("COURT_TYPE"));
                        jsonObject.put("org_location",organInfoListTemp.get(i).get("REGION_NAME"));
                        jsonObject.put("status",organInfoListTemp.get(i).get("STATUS"));
                        jsonObject.put("order",organInfoListTemp.get(i).get("SHORT_CODE"));

                        jsonArray.add(jsonObject);
                    }
                }

            }else{
                System.out.println("所查组织机构信息为空");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonArray.toString();
    }
    @TargetDataSource(name="ds1")
    public void getTotalOrganInfo(List<Map<String,Object>> eachlist,String organIDfirst,
            String lastTime){
        if (!eachlist.isEmpty()) {
            totalResultList.add(eachlist);
            for (int j = 0; j < eachlist.size(); j++) {
                String organIDtemp = eachlist.get(j).get("ID").toString();
                String organType = eachlist.get(j).get("ORGAN_TYPE").toString();
                //如果是基层法院就终止查下级
                if ((organType!=null)&&(!organType.equals("FYJB.3"))&&(organIDtemp!=null)&&(!organIDfirst.equals(organIDtemp))){
                    List<Map<String,Object>> organInfoListTemp = hkloginMapper.getOrganInfoSecond(organIDtemp,lastTime);
                    getTotalOrganInfo(organInfoListTemp,organIDtemp,lastTime);
                 }
            }
        }else{
            System.out.println("所查组织机构信息为空");
        }
    }
    @TargetDataSource(name="ds1")
    public List<MenuBean> getMenu(Map<String,Object> paramMap){
        return hkloginMapper.findMenu(paramMap);
    }
    @TargetDataSource(name="ds1")
    public Map<String,Object> queryUserInfo(String userAccount,String appCode){
        return hkloginMapper.getUserInfo(userAccount,appCode);
    }
    @TargetDataSource(name="ds1")
    public List<Map<String,Object> > queryRoleCodeVaule(String appCode){
        return hkloginMapper.getRoleCodeVaule(appCode);
    }

    @TargetDataSource(name="ds1")
    public Map<String,Object> getOrganInfoOne(String organID){
        return hkloginMapper.getOrganInfoOne(organID);
    }

}
