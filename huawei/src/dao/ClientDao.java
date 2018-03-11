package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.JdbcTemplate;

public class ClientDao extends JdbcTemplate {
	
	/**
	 * 获取站点下所有资源列表 
	 * @Title: ListResourceBySite
	 * @return List<Map<String,String>>
	 * @throws
	 */
	public List<Map<String,String>> ListResourceBySite(
			String ip,String site) {
		List<Map<String, String>> reslist = new ArrayList<>();
		String sql = "SELECT RESOURCE_CODE code,RESOURCE_NAME name,RESOURCE_URN urn,RESOURCE_IP ip,TYPE type,TERRACE_IP parentIp,SITE_URI siteUri FROM huawei_resource WHERE TERRACE_IP = ? AND SITE_URI = ?";
		String[] param = {ip,site};
		ResultSet rs = this.query(sql, param);
		try {
			while(rs.next()){
				Map<String, String> map = new HashMap<>();
				map.put("code", rs.getString(1));
				map.put("name", rs.getString(2));
				map.put("urn", rs.getString(3));
				map.put("ip", rs.getString(4));
				map.put("type", rs.getString(5));
				map.put("parentIp", rs.getString(6));
				map.put("siteUri", rs.getString(7));
				reslist.add(map);
			}
			return reslist;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			this.closeRes();
		}
		return null;
	}
	
	/**
	 * 删除资源表一个站点的数据
	 * @Title: deleteResources
	 * @return boolean
	 * @throws
	 */
	public boolean deleteResources() {
		boolean flag = false;
		String sql = "DELETE FROM huawei_resource";
		flag = this.updateDate(sql);
		if (flag) {
			this.myCommit();
		} else {
			this.myRollBack();
		}
		this.closeRes();
		return flag;
	}
	
	/**
	 * 添加一个站点的资源数据 
	 * @Title: insertBatchResources
	 * @param dataList
	 * @throws
	 */
	public void insertBatchResources(List<Map<String, String>> dataList) {
		if (null == dataList || dataList.size() <= 0) {
			return;
		}
		String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		List<String> sqls = new ArrayList<>();
		for (int i = 0; i < dataList.size(); i++) {
			Map<String, String> dataMap = dataList.get(i);
			String ip = dataMap.get("parentIp");
			String site = dataMap.get("site");
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO huawei_resource(RESOURCE_CODE,RESOURCE_URN,RESOURCE_NAME,RESOURCE_IP,RESOURCE_DESCRIPTION,STATUS,HOST_URN,TYPE,TERRACE_IP,SITE_URI,MANAGE_TIME) ");
			sql.append("VALUES(");
			if (null != dataMap.get("urn")) {
				sql.append("'" + ip + "_" + dataMap.get("urn") + "',");
				sql.append("'" + dataMap.get("urn") + "',");
			} else {
				continue;
			}
			sql.append(columnsDataIsNull(dataMap.get("name")));
			sql.append(columnsDataIsNull(dataMap.get("ip")));
			sql.append(columnsDataIsNull(dataMap.get("description")));
			sql.append(columnsDataIsNull(dataMap.get("status")));
			sql.append(columnsDataIsNull(dataMap.get("hostUrn")));
			sql.append("'" + dataMap.get("type") + "',");
			sql.append("'" + ip + "',");
			sql.append("'" + site + "',");
			sql.append("'" + nowTime + "'");

			sql.append(")");
			sqls.add(sql.toString());
		}
		this.executeBatch(sqls);
	}
	
	/**
	 * 根据时间删除指标数据
	 * @Title: deteleQuotaValueByTime
	 * @param time
	 * @return boolean
	 * @throws
	 */
	public boolean deteleQuotaValueByTime (String time) {
		boolean flag = false;
		String sql = "DELETE FROM huawei_quota_value WHERE COLLECT_TIME = ?";
		String[] param = {time};
		flag = this.updateDate(sql,param);
		if (flag) {
			this.myCommit();
		} else {
			this.myRollBack();
		}
		this.closeRes();
		return flag;
	}
	
	/**
	 * 插入指标数据
	 * @Title: insertBatchQuotaValue
	 * @param quotas
	 * @param columns
	 * @param createTime
	 * @param dataList
	 * @throws
	 */
	public void insertBatchQuotaValue(
			List<String> quotas,String columns,
			List<Map<String, String>> dataList,String createTime) {
		if (null == dataList || dataList.size() <= 0) {
			return;
		}
		List<String> sqls = new ArrayList<>();
		for (int i = 0; i < dataList.size(); i++) {
			Map<String, String> dataMap = dataList.get(i);
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO huawei_quota_value(RESOURCE_CODE,RESOURCE_URN,RESOURCE_IP,");
			sql.append(columns+",INSERT_TIME,COLLECT_TIME) ");
			sql.append("VALUES(");
			sql.append("'" + dataMap.get("code") + "',");
			sql.append("'" + dataMap.get("urn") + "',");
			sql.append("'" + dataMap.get("ip") + "',");
			for (int j = 0; j < quotas.size(); j++) {
				boolean falg = false;
				for (Map.Entry<String, String> entry : dataMap.entrySet()) {
					if (quotas.get(j).equals(entry.getKey())) {
						sql.append(columnsDataIsNull(entry.getValue()));
						falg = true;
						break;
					}
				}
				if (!falg) {
					sql.append(null + ",");
				}
			}
			sql.append("'" + dataMap.get("quota_date") + "',");
			sql.append("'" + createTime + "'");
			sql.append(")");
			sqls.add(sql.toString());
		}
		this.executeBatch(sqls);
	}

	/**
	 * 查询倒数第二次的告警记录
	 * @return
     */
	public List<Map<String,String>> listLast2TimeAlarmTask(){
		String sql = "SELECT RESOURCE_URN,RESOURCE_NAME,TERRACE_IP,SITE_URI,I_SERIAL_NO,SV_ALARM_ID,SV_MOC,SV_ALARM_NAME,I_ALARM_CATEGORY,I_ALARM_LEVEL,SV_ALARM_CAUSE,SV_ADDITIONAL_INFO,DT_OCCUR_TIME,DT_UPDATE_TIME,COLLECT_TIME FROM huawei_alarm WHERE COLLECT_TIME=(SELECT COLLECT_TIME FROM huawei_alarm GROUP BY COLLECT_TIME ORDER BY COLLECT_TIME DESC LIMIT 1,1)";
		return parseAlarmTask(sql);
	}

	/**
	 * 查询最后一批告警数据
	 * @return
     */
	public List<Map<String,String>> listLastTimeAlarmTask(){
		String sql = "SELECT RESOURCE_URN,RESOURCE_NAME,TERRACE_IP,SITE_URI,I_SERIAL_NO,SV_ALARM_ID,SV_MOC,SV_ALARM_NAME,I_ALARM_CATEGORY,I_ALARM_LEVEL,SV_ALARM_CAUSE,SV_ADDITIONAL_INFO,DT_OCCUR_TIME,DT_UPDATE_TIME,COLLECT_TIME FROM huawei_alarm WHERE COLLECT_TIME=(SELECT MAX(COLLECT_TIME) FROM huawei_alarm)";
		return parseAlarmTask(sql);
	}

	public List<Map<String,String>> parseAlarmTask(String sql){
		List<Map<String, String>> reslist = new ArrayList<>();
		ResultSet rs = this.query(sql);
		try {
			while(rs.next()){
				Map<String, String> map = new HashMap<>();
				map.put("urn", rs.getString(1));
				map.put("name", rs.getString(2));
				map.put("parentIp", rs.getString(3));
				map.put("site", rs.getString(4));
				map.put("iSerialNo", rs.getString(5));
				map.put("svAlarmID", rs.getString(6));
				map.put("svMoc", rs.getString(7));
				map.put("svAlarmName", rs.getString(8));
				map.put("iAlarmCategory", rs.getString(9));
				map.put("iAlarmLevel", rs.getString(10));
				map.put("svAlarmCause", rs.getString(11));
				map.put("svAdditionalInfo", rs.getString(12));
				map.put("dtOccurTime", rs.getString(13));
				map.put("dtUpdateTime", rs.getString(14));
				map.put("collectTime", rs.getString(15));
				if (map.get("iAlarmLevel") != null
						&& !"次要".equals(map.get("iAlarmLevel").trim())
						&& !"提示".equals(map.get("iAlarmLevel").trim())) {
					map.put("levelNum", "3");
				} else {
					map.put("levelNum", "2");
				}
				reslist.add(map);
			}
			return reslist;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			this.closeRes();
		}
		return null;
	}

	/**
	 * 添加告警数据
	 * @param dataList
	 * @return 数据列表
     */
	public List<Map<String, String>> insertBatchAlarm(List<Map<String, String>> dataList) {
		if (null == dataList || dataList.size() <= 0) {
			return null;
		}
		List<Map<String, String>> resultList = new ArrayList<>();
		String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		List<String> sqls = new ArrayList<>();
		for (int i = 0; i < dataList.size(); i++) {
			Map<String, String> dataMap = dataList.get(i);
			String ip = dataMap.get("parentIp");
			String site = dataMap.get("site");
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO huawei_alarm(RESOURCE_URN,RESOURCE_NAME,TERRACE_IP,SITE_URI,I_SERIAL_NO,SV_ALARM_ID,SV_MOC,SV_ALARM_NAME,I_ALARM_CATEGORY,I_ALARM_LEVEL,SV_ALARM_CAUSE,SV_ADDITIONAL_INFO,DT_OCCUR_TIME,DT_UPDATE_TIME,COLLECT_TIME) ");
			sql.append("VALUES(");
			if (null != dataMap.get("urn")) {
				sql.append("'" + dataMap.get("urn") + "',");
			} else {
				continue;
			}
			sql.append(columnsDataIsNull(dataMap.get("name")));
			sql.append("'" + ip + "',");
			sql.append("'" + site + "',");
			sql.append(columnsDataIsNull(dataMap.get("iSerialNo")));
			sql.append(columnsDataIsNull(dataMap.get("svAlarmID")));
			sql.append(columnsDataIsNull(dataMap.get("svMoc")));
			sql.append(columnsDataIsNull(dataMap.get("svAlarmName")));
			sql.append(columnsDataIsNull(dataMap.get("iAlarmCategory")));
			sql.append(columnsDataIsNull(dataMap.get("iAlarmLevel")));
			sql.append(columnsDataIsNull(dataMap.get("svAlarmCause")));
			sql.append(columnsDataIsNull(dataMap.get("svAdditionalInfo")));
			sql.append(columnsDataIsNull(dataMap.get("dtOccurTime")));
			sql.append(columnsDataIsNull(dataMap.get("dtUpdateTime")));
			sql.append("'" + nowTime + "'");
			sql.append(")");
			sqls.add(sql.toString());
			dataMap.put("collectTime",nowTime);
			if (dataMap.get("iAlarmLevel") != null
					&& !"次要".equals(dataMap.get("iAlarmLevel").trim())
					&& !"提示".equals(dataMap.get("iAlarmLevel").trim())) {
				dataMap.put("levelNum", "3");
			} else {
				dataMap.put("levelNum", "2");
			}
			resultList.add(dataMap);
		}
		this.executeBatch(sqls);
		return resultList;
	}

	private String columnsDataIsNull(Object obj){
		if (null != obj) {
			return "'" + obj + "',";
		} else {
			return null + ",";
		}
	}

}
