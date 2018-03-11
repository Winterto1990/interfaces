package service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import util.HttpsHelper;

public class InterfaceService {
	
	/**
	 * 登录HUAWEI FusionCompute，返回header：X-Auth-Token
	 * @Title: login
	 * @param ip 
	 * @param authUser
	 * @param authKey
	 * @return String authToken
	 * @throws
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String login(String ip,String authUser,String authKey) {
		String url = "https://"+ip+"/service/session";
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Accept", "application/json;version=1.2;charset=UTF-8");
		headerMap.put("Content-Type", "application/json; charset=UTF-8");
		headerMap.put("Accept-Language", "zh_CN");
		headerMap.put("X-Auth-User", authUser);
		headerMap.put("X-Auth-Key", authKey);
		headerMap.put("X-Auth-UserType", "2");
		headerMap.put("X-Auth-AuthType", "0");
		Map resultMap = HttpsHelper.doPost(url, headerMap, "");
		if(null != resultMap) {
			Map<String, String> resultHeaderMap = (Map<String, String>) resultMap.get("headers");
			if(resultHeaderMap.containsKey("X-Auth-Token")) {
				return resultHeaderMap.get("X-Auth-Token");
			}
		}
		return null;
	}
	
	/**
	 * 获取站点列表
	 * @Title: getSites
	 * @param ip
	 * @param authToken
	 * @param
	 * @return List<String>
	 * @throws
	 */
	@SuppressWarnings("rawtypes")
	public List<String> getSites(String ip,String authToken){
		List<String> list =new ArrayList<>();
		String url = "https://"+ip+"/service/sites";
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Accept", "application/json;version=1.2;charset=UTF-8");
		headerMap.put("X-Auth-Token", authToken);
		Map resultMap = HttpsHelper.doGet(url, headerMap);
		if(null != resultMap) {
			String resultJson = resultMap.get("result").toString();
			JSONObject jsonObj = JSONObject.parseObject(resultJson);
			JSONArray resultAry = jsonObj.getJSONArray("sites");
			if (null == resultAry || resultAry.size() <= 0) {
				return null;
			}
			for (int i = 0; i < resultAry.size(); i++) {
				JSONObject data = (JSONObject) resultAry.get(i);
				if(null != data.getString("uri")) {
					list.add(data.getString("uri"));
				}
			}
		}
		return list;
	}
	
	/**
	 * 获取所有站点资源信息，包括宿主机和虚拟机
	 * @Title: listAllResourceInfo
	 * @param ip
	 * @param authToken
	 * @param sites
	 * @return List<Map<String,String>>
	 * @throws
	 */
	public List<Map<String, String>> listAllResourceInfo(
			String ip,String authToken,List<String> sites) {
		if (null == sites || sites.size() <= 0) {
			return null;
		}
		List<Map<String, String>> resourceList = new ArrayList<>();
		for (int i = 0; i < sites.size(); i++) {
			String site = sites.get(i);
			Map<String, String> headerMap = new HashMap<>();
			headerMap.put("Accept", "application/json;version=1.2;charset=UTF-8");
			headerMap.put("X-Auth-Token", authToken);
			List<Map<String, String>> hostList = listResourceInfo(ip,site,headerMap,"hosts");
			if (null != hostList && hostList.size() > 0) {
				resourceList.addAll(hostList);
			}
			List<Map<String, String>> vmList = listResourceInfo(ip,site,headerMap,"vms");
			if (null != vmList && vmList.size() > 0) {
				resourceList.addAll(vmList);
			}
		}
		return resourceList;
	}
	
	/**
	 * 查询资源数据
	 * @Title: listResourceInfo
	 * @param ip
	 * @param site
	 * @param headerMap
	 * @param type
	 * @return List<Map<String,String>>
	 * @throws
	 */
	@SuppressWarnings("rawtypes")
	private List<Map<String, String>> listResourceInfo(String ip,String site, Map<String, String> headerMap,String type) {
        List<Map<String, String>> resultList = new ArrayList<>();
        String url = "";
		if (type.equals("vms")) {
			url = "https://" + ip + site + "/vms?detail=2";
		} else if(type.equals("hosts")) {
			url = "https://" + ip + site + "/hosts";
		}
		Map jsonMap = HttpsHelper.doGet(url, headerMap);
		if(null == jsonMap || jsonMap.size() <= 0) {
			return null;
		}
		String resultJson = jsonMap.get("result").toString();
		JSONObject jsonObj = JSONObject.parseObject(resultJson);
		//JSONArray resultAry = jsonObj.getJSONArray(type);
        int total = jsonObj.getInteger("total");
        int tempNumber = 0;
        //接口限制每次调用100条记录，如果总记录大于100条，则循环调用
        while (tempNumber <= total){
            List<Map<String, String>> resultTempList = parseResource(jsonObj,ip,site,type);
            resultList.addAll(resultTempList);
            tempNumber += 100;
            if (total > 100){
                String newUrl = url + "&offset="+tempNumber+"&limit=100";
                jsonMap = HttpsHelper.doGet(newUrl, headerMap);
                if(null == jsonMap || jsonMap.size() <= 0) {
                    break;
                }
                resultJson = jsonMap.get("result").toString();
                jsonObj = JSONObject.parseObject(resultJson);
            }
        }
		return resultList;
	}


	private List<Map<String, String>> parseResource(JSONObject jsonObj,String ip,String site,String type){
        List<Map<String, String>> resultList = new ArrayList<>();
        JSONArray resultAry = JSONArray.parseArray(jsonObj.getString(type));
        for (int i = 0; i < resultAry.size(); i++) {
            Map<String, String> resultMap = new HashMap<>();
            JSONObject data = (JSONObject) resultAry.get(i);
            if(null!=data.getString("name") && !data.getString("name").equals("")) {
                resultMap.put("name", data.getString("name"));
            }
            if(null!=data.getString("ip") && !data.getString("ip").equals("")) {
                resultMap.put("ip", data.getString("ip"));
            }
            if(null!=data.getString("urn") && !data.getString("urn").equals("")) {
                resultMap.put("urn", data.getString("urn"));
            }
            if(null!=data.getString("description") && !data.getString("description").equals("")) {
                resultMap.put("description", data.getString("description"));
            }
            if(null!=data.getString("status") && !data.getString("status").equals("")) {
                resultMap.put("status", data.getString("status"));
            }
            resultMap.put("type", type);
            resultMap.put("parentIp", ip.split(":")[0]);
            resultMap.put("site", site);
            if (type.equals("vms")){ //虚拟机
                if(null!=data.getString("hostUrn") && !data.getString("hostUrn").equals("")) {
                    resultMap.put("hostUrn", data.getString("hostUrn"));
                }
                String ips = "";
                JSONObject vmConfig = data.getJSONObject("vmConfig");
                JSONArray nics = vmConfig.getJSONArray("nics");
                if (null != nics && nics.size()>0){
                    for (int j = 0; j < nics.size(); j++) {
                        ips += nics.getJSONObject(j).getString("ip") + ",";
                    }
                }
                if(null != ips && !ips.equals("")){
                    resultMap.put("ip", ips.substring(0,ips.length()-1));
                }
            }
            resultList.add(resultMap);
        }
        return resultList;
    }

	/**
	 * 查询一个站点下所有指标信息
	 * @Title: listSiteAllQuotaValue
	 * @param ip
	 * @param authToken
	 * @param resourceList
	 * @param quotas
	 * @return List<Map<String,String>>
	 * @throws
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String, String>> listSiteAllQuotaValue(
			String ip,String authToken,String site,
			List<Map<String,String>> resourceList,String[] quotas){
		List<Map<String, String>> resultList = new ArrayList<>();
		if (null == resourceList || resourceList.size() <= 0) {
			return null;
		}
		if (null == quotas || quotas.length <= 0) {
			return null;
		}
		String url = "https://" + ip + site + "/monitors/objectmetric-realtimedata";
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Accept", "application/json;version=1.2;charset=UTF-8");
		headerMap.put("Content-Type", "application/json");
		headerMap.put("X-Auth-Token", authToken);
		List<Map> httpDataList = new ArrayList<>();
		for (int i = 0; i < resourceList.size(); i++) {
			Map map = new HashMap();
			Map<String,String> resource = resourceList.get(i);
			map.put("urn", resource.get("urn"));
			map.put("metricId", quotas);
			httpDataList.add(map);
		}
		Map jsonMap = HttpsHelper.doPost(url, headerMap, JSON.toJSONString(httpDataList));
		
		if(null == jsonMap) {
			return null;
		}
		String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String resultJson = jsonMap.get("result").toString();
		JSONObject jsonObj = JSONObject.parseObject(resultJson);
        JSONArray resultAry = JSONArray.parseArray(jsonObj.getString("items"));
		for (int i = 0; i < resultAry.size(); i++) {
			JSONObject item = (JSONObject) resultAry.get(i);
            JSONArray jsonArray = JSONArray.parseArray(item.getString("value"));
			Map<String, String> dataMap = new HashMap<>();
			dataMap.put("urn", item.getString("urn"));
            dataMap.put("quota_date", nowTime);
			for (int j = 0; j < resourceList.size(); j++) {
				Map<String,String> resource = resourceList.get(i);
				if (item.getString("urn").equals(resource.get("urn"))) {
					dataMap.put("code", resource.get("code"));
					dataMap.put("ip", resource.get("ip"));
                    break;
				}
			}
			for (int j = 0; j < quotas.length; j++) {
				String quota = quotas[j];
				boolean flag = false;
				for (int k = 0; k < jsonArray.size(); k++) {
					JSONObject data = (JSONObject) jsonArray.get(j);
					if (quota.equals(data.getString("metricId"))) {
						if ("disk_info".equals(data.getString("metricId"))) {
							String diskInfoJson = data.getString("metricValue");
							JSONArray diskInfoArray = JSONArray.parseArray(diskInfoJson);
							if (null == diskInfoArray) {
								break;
							}
							long diskInfo = 0L;
							for (int l = 0; l < diskInfoArray.size(); l++) {
								JSONObject diskInfoObj = (JSONObject) diskInfoArray.get(l);
								if (null != diskInfoObj.getLong("total")){
									diskInfo += diskInfoObj.getLong("total");
								}
							}
							dataMap.put(quota, Long.toString(diskInfo));
							dataMap.put(quota+"_unit", "MB");
						}else{
							dataMap.put(quota, data.getString("metricValue"));
							dataMap.put(quota+"_unit", data.getString("unit"));
						}
						flag = true;
						break;
					}
				}
				if (!flag) {
					dataMap.put(quota, null);
					dataMap.put(quota+"_unit", null);
				}
			}
			resultList.add(dataMap);
		}
		return resultList;
	}

	/**
	 * 获取一个站点下最新告警信息
	 */
	public List<Map<String, String>> listAlarmInfo(
			String ip,String authToken,List<String> sites){
		if (null == sites || sites.size() <= 0) {
			return null;
		}
		List<Map<String, String>> alarmList = new ArrayList<>();
		for (int i = 0; i < sites.size(); i++) {
			String site = sites.get(i);
			Map<String, String> headerMap = new HashMap<>();
			headerMap.put("Accept", "application/json;version=1.2;charset=UTF-8");
			headerMap.put("Content-Type", "application/json; charset=UTF-8");
			headerMap.put("X-Auth-Token", authToken);
			String url = "https://" + ip + site + "/alarms/activeAlarms";
			Map<String, Integer> httpDataMap = new HashMap<>();
			Map jsonMap = HttpsHelper.doPost(url, headerMap, JSON.toJSONString(httpDataMap));
			if(null == jsonMap || jsonMap.size() <= 0) {
				continue;
			}
			String resultJson = jsonMap.get("result").toString();
			JSONObject jsonObj = JSONObject.parseObject(resultJson);
			int total = jsonObj.getInteger("total");
			int tempNumber = 0;
			//接口限制每次调用100条记录，如果总记录大于100条，则循环调用
			while (tempNumber <= total) {
				List<Map<String, String>> resultTempList = parseAlarm(jsonObj, ip, site);
				alarmList.addAll(resultTempList);
				tempNumber += 100;
				if (total > 100) {
					httpDataMap.put("offset", tempNumber);
					httpDataMap.put("limit", 100);
					jsonMap = HttpsHelper.doPost(url, headerMap, JSON.toJSONString(httpDataMap));
					if (null == jsonMap || jsonMap.size() <= 0) {
						break;
					}
					resultJson = jsonMap.get("result").toString();
					jsonObj = JSONObject.parseObject(resultJson);
				}
			}
		}
		return alarmList;
	}

	private List<Map<String, String>> parseAlarm(
			JSONObject jsonObj, String ip, String site) {
		List<Map<String, String>> resultList = new ArrayList<>();
		JSONArray resultAry = jsonObj.getJSONArray("items");
		for (int i = 0; i < resultAry.size(); i++) {
			Map<String, String> resultMap = new HashMap<>();
			JSONObject data = (JSONObject) resultAry.get(i);
			//有清除告警的标识，则跳过此条记录
			if(null!=data.getString("iClearType") && !data.getString("iClearType").equals("")  && !data.getString("iClearType").equals("-")) {
				continue;
			}
			if(null!=data.getString("objectUrn") && !data.getString("objectUrn").equals("")) {
				resultMap.put("urn", data.getString("objectUrn"));
			}
			if(null!=data.getString("urnByName") && !data.getString("urnByName").equals("")) {
				resultMap.put("name", data.getString("urnByName"));
			}
			if(null!=data.getString("iSerialNo") && !data.getString("iSerialNo").equals("")) {
				resultMap.put("iSerialNo", data.getString("iSerialNo"));
			}
			if(null!=data.getString("svAlarmID") && !data.getString("svAlarmID").equals("")) {
				resultMap.put("svAlarmID", data.getString("svAlarmID"));
			}
			if(null!=data.getString("svMoc") && !data.getString("svMoc").equals("")) {
				resultMap.put("svMoc", data.getString("svMoc"));
			}
			if(null!=data.getString("svAlarmName") && !data.getString("svAlarmName").equals("")) {
				resultMap.put("svAlarmName", data.getString("svAlarmName"));
			}
			if(null!=data.getString("iAlarmCategory") && !data.getString("iAlarmCategory").equals("")) {
				resultMap.put("iAlarmCategory", data.getString("iAlarmCategory"));
			}
			if(null!=data.getString("iAlarmLevel") && !data.getString("iAlarmLevel").equals("")) {
				resultMap.put("iAlarmLevel", data.getString("iAlarmLevel"));
			}
			if(null!=data.getString("svAlarmCause") && !data.getString("svAlarmCause").equals("")) {
				resultMap.put("svAlarmCause", data.getString("svAlarmCause"));
			}
			if(null!=data.getString("svAdditionalInfo") && !data.getString("svAdditionalInfo").equals("")) {
				resultMap.put("svAdditionalInfo", data.getString("svAdditionalInfo"));
			}
			if(null!=data.getString("dtOccurTime") && !data.getString("dtOccurTime").equals("")) {
				if (data.getString("dtOccurTime").equals("-")){
					resultMap.put("dtOccurTime",data.getString("dtOccurTime"));
				}else{
					Date dtOccurTime = new Date(data.getLong("dtOccurTime"));
					resultMap.put("dtOccurTime",
							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dtOccurTime));
				}
			}
			if(null!=data.getString("dtUpdateTime") && !data.getString("dtUpdateTime").equals("")) {
				if (data.getString("dtUpdateTime").equals("-")){
					resultMap.put("dtUpdateTime",data.getString("dtUpdateTime"));
				}else{
					Date dtUpdateTime = new Date(data.getLong("dtUpdateTime"));
					resultMap.put("dtUpdateTime",
							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dtUpdateTime));
				}
			}
			resultMap.put("parentIp", ip.split(":")[0]);
			resultMap.put("site", site);
			resultList.add(resultMap);
		}
		return resultList;
	}

	/**
	 * 获取向下取整5分钟的当前时间数据
	 * @Title: parse5MinuteTime
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String parse5MinuteTime() {
		LocalDateTime date = LocalDateTime.now();
		int minute = date.getMinute();
		int newMinute = (minute/5)*5;
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = date.atZone(zone).toInstant();
		return new SimpleDateFormat("yyyy-MM-dd HH:"+newMinute+":00").format(Date.from(instant));
	}
}
