package com.inspur.hksso_union_hy.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.inspur.AESEncrypter;
import com.inspur.hksso_union_hy.util.ReturenCode;
import com.inspur.hksso_union_hy.Datasource.TargetDataSource;
import com.inspur.hksso_union_hy.Service.HkloginService;
import com.inspur.hksso_union_hy.Bean.MenuBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 海康视频模块单点登录
 */
@RestController
@RequestMapping("/hksso")
public class HkloginController {
    @Autowired
    private HkloginService hkloginService;

    //海康大屏调用路径
    @Value("${hk.lagerscreen.url}")
    String hklagerScreenUrl;
    @Value("${hk.user.account}")
    String hkuserAccount;
    @Value("${hk.organ.id}")
    String hkorganID;

    @Value("${hk.appCode}")
    String appCode;

    public static final String skey = "ZTZjZGVjZDcxNmMwMWQzZTIzOWE4ZjNkZ";

    ReturenCode returenCode = new ReturenCode();
    Logger logger  =  Logger.getLogger(HkloginController.class );

    /**
     * 海康大屏调用（暂时未用到）
     * @param request
     * @param response
     */
    @RequestMapping("/lagerScreen")
    public void lagerScreen(HttpServletRequest request, HttpServletResponse response){
        try{
            String AfterAEStohken = setJsonBody().toString();
            response.sendRedirect(hklagerScreenUrl+AfterAEStohken);
        }catch (Exception e){
            logger.debug(e.getMessage());
        }
    }


    /**
     * 获取组织机构信息
     *
     */
    @RequestMapping(value = "/getOrganInfo")
    public String queryOrganInfo(@RequestBody String data) {

        JSONObject requestParam = new JSONObject();
        JSONObject resultjsonObject = new JSONObject();
        String strOrganInfo = "";
        String afterEnAES = "";
        String resultTotal=null;

        try {
            JSONObject object2 =  (JSONObject)JSONObject.parse(data);
            String beforeDeAES=object2.getString("token");
            String afterDeAES  = AESEncrypter.getInstance().decryptAsString(beforeDeAES);
            requestParam = JSON.parseObject(afterDeAES);
            System.out.println("解密后：" + requestParam);
            String reskey = requestParam.getString("SKEY");
            String organID = requestParam.getString("ORGAN_ID");
            //第一调用的时候时没有同步时间这个值的
            String lastTime = requestParam.getString("LAST_TIME");

            if ((reskey!=null)&&(skey.equals(reskey))&&(organID!=null)) {
                strOrganInfo = hkloginService.queryOrganInfo(organID,lastTime);
            } else {
                System.out.println("校验不通过或所传参数缺省");
                returenCode.setStatusCode("401");
                returenCode.setMsg("SKEY验证失败或所传参数缺省");
                strOrganInfo = returenCode.toString();
            }
            System.out.println("组织机构信息："+strOrganInfo.toString());
            afterEnAES = AESEncrypter.getInstance().encrypt(strOrganInfo.toString());
//            System.out.println("组织机构信息解密："+AESEncrypter.getInstance().decryptAsString(afterEnAES));
            resultjsonObject.put("token",afterEnAES);
            resultTotal = new String(resultjsonObject.toString().getBytes(),"UTF-8");
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
        return resultTotal;
//        return  resultjsonObject.toString();
    }
    /**
     * 获取用户信息
     * @param data
     * @return
     */
    @RequestMapping(value = "/getBspInfo")
    @TargetDataSource(name="ds1")
    public String getBspInfo(@RequestBody String data){
        String afterEnAES = "";
        String beforeAES = "";
        String resultTotal=null;
        JSONObject resultjsonObjectTemp = new JSONObject();
        JSONObject resultjsonObjectLast = new JSONObject();
        JSONObject resultjsonObject = new JSONObject();
        try {
            JSONObject object2 =  (JSONObject)JSONObject.parse(data);
            String beforeDeAES=object2.getString("token");
            JSONObject object =  (JSONObject)JSONObject.parse(AESEncrypter.getInstance().decryptAsString(beforeDeAES));
            String reskey = object.getString("SKEY");
            String userId = object.getString("USER_ID");
            if((reskey!=null)&&(skey.equals(reskey))&&(userId!=null)){
                Map<String,Object> userInfoMap = hkloginService.queryUserInfo(userId,appCode);
                if (!userInfoMap.isEmpty()){
                    resultjsonObjectLast.put("userid",userInfoMap.get("userid").toString());
                    resultjsonObjectLast.put("username",userInfoMap.get("username").toString());
                    resultjsonObjectLast.put("appname",userInfoMap.get("appname").toString());
                    String str_role = userInfoMap.get("role").toString();
                    String[] roleArray = str_role.split(",");
//                    resultjsonObjectLast.put("role",roleArray);

                    String resultRoleCode="";
                    String[] resultRoleVauleArray=new String[1];

                    String roleCode = userInfoMap.get("ROLE_CODE").toString();
                    String[] roleCodelArray = roleCode.split(",");
                    List<Map<String,Object>>  roleCodeVauleList= hkloginService.queryRoleCodeVaule(appCode);
                    for (int k=0;k<roleCodelArray.length;k++){
                        for (int j=0;j<roleCodeVauleList.size();j++){
                            if (roleCodelArray[k].equals(roleCodeVauleList.get(j).get("ROLE_CODE"))){
                                resultRoleVauleArray[0]=roleArray[k];
                                resultRoleCode = roleCodelArray[k];
                                break;
                            }
                        }
                    }
                    resultjsonObjectLast.put("role",resultRoleVauleArray);


//                    String appCode = userInfoMap.get("APP_CODE").toString();

//                    String organIDone = userInfoMap.get("ORG_ID").toString();
                    String organIDone = userInfoMap.get("ORG_CODE").toString();

                    Map<String,Object> organ = hkloginService.getOrganInfoOne(organIDone);

                    resultjsonObjectLast.put("organ",organ);

                    String menuRsult = getUserPcAppResource(resultRoleCode,appCode);
                    resultjsonObjectLast.put("menu",JSONObject.parseArray(menuRsult));
                    beforeAES = resultjsonObjectLast.toString();
                }else{
                    logger.debug("所查用户信息不存在");
                }
            }else {
                System.out.println("校验不通过或所传参数缺省");
                returenCode.setStatusCode("401");
                returenCode.setMsg("SKEY验证失败或所传参数缺省");
                beforeAES = returenCode.toString();
            }
            System.out.println("用户信息："+beforeAES);
            afterEnAES = AESEncrypter.getInstance().encrypt(beforeAES);
            resultjsonObject.put("token",afterEnAES);
            resultTotal = new String(resultjsonObject.toString().getBytes(),"UTF-8");
        }catch (Exception e){
            logger.debug(e.getMessage());
            System.out.println("用户不存在");
//            e.printStackTrace();
        }
        return resultTotal;
//        return  resultjsonObject.toString();
    }

    /** 配合海康集成
     * 返回用户应用资源信息
     * @param
     * @param appCode
     * @return
     */
    public String getUserPcAppResource(String roleCode, String appCode){
        List<Object> param = new ArrayList<Object>();
        MenuBean mbean = new MenuBean();
        Map<String,Object> parameter =  new HashMap<String,Object>();
        parameter.put("roleCode",roleCode);
        parameter.put("appCode",appCode);//当前应用只有一个
        parameter.put("pid","#");
//        String sql = ServiceUtils.getUserAppResource(roleCode,appCode,param);
//        JSONObject json = new JSONObject();
        String result="";
        try{
//			List<Map<String,Object>> list = this.serviceDao.findSQL(sql,param.toArray());
            List<MenuBean> list = this.hkloginService.getMenu(parameter);
            for(int i=0;i<list.size();i++){
                MenuBean submenu =  (MenuBean) list.get(i);
                submenu.setSubMenu(getMenus(roleCode,appCode,submenu));
            }

            //成功标识
//            json.put("state",ServiceConstant.SYSTEM_SUCCESS);
            String jsonArray = JSONArray.toJSONString(list,true);
            JSONArray array = JSONArray.parseArray(jsonArray);
            result = array.toString();
//            json.put("menu", array);
        }catch(Exception e){
            //异常标识
//            json.put("state", ServiceConstant.SYSTEM_ERROR);
//            json.put("error", e.getMessage());

            result =e.getMessage();
        }
        return result;
    }


    private List getMenus(String roleCode,String appCode,MenuBean menu) throws Exception {
        List<MenuBean> menuList = new ArrayList<MenuBean>();
        Map<String,Object> parameter =  new HashMap<String,Object>();
        parameter.put("roleCode",roleCode);
        parameter.put("appCode",appCode);
        parameter.put("pid",menu.getId());
        menuList = this.hkloginService.getMenu(parameter);
        for(int i=0;i<menuList.size();i++){
            MenuBean functions = menuList.get(i);
            parameter.put("pid",functions.getId());
            List<MenuBean> nodeList = hkloginService.getMenu(parameter);
            functions.setSubMenu(nodeList);
            getMenus(roleCode,appCode,functions);
            menuList.set(i,functions);
        }
        return menuList;
    }


    public  String setJsonBody(){
        String beforeAES="";
        String afterEnAES="";
        JSONObject  jsonObject = new JSONObject();
        jsonObject.put("USER_ID",hkuserAccount);
        jsonObject.put("ORGAN_ID",hkorganID);
        jsonObject.put("SKEY","ZTZjZGVjZDcxNmMwMWQzZTIzOWE4ZjNkZ");
        beforeAES = jsonObject.toString();
        try{
            System.out.println("加密前："+beforeAES);
            afterEnAES= AESEncrypter.getInstance().encrypt(beforeAES);
            System.out.println("加密后："+beforeAES);
        }catch (Exception e){
            e.printStackTrace();
        }
        return afterEnAES;
    }
}