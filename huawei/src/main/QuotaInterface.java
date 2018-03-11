package main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import dao.ClientDao;
import service.InterfaceService;

public class QuotaInterface extends TimerTask {
	
	private Map param;
	private String[] interfaceUrls; 
	private String[] quotaCodes; 
	private String authUser; 
	private String authKey;
	
	public QuotaInterface(Map param) {
		super();
		this.param = param;
	}
	
	InterfaceService service = new InterfaceService();
	ClientDao dao = new ClientDao();
	
	@Override
	public void run() {
		System.out.println("获取指标数据 开始（getQuotaValue start） --> "+ new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒")
				.format(new Date()) + "==============");
		if (null == param || param.isEmpty()) {
			return;
		}
		if (param.containsKey("interfaceUrls")) {
			interfaceUrls = (String[]) param.get("interfaceUrls");
		}
		if (param.containsKey("quotaCodes")) {
			quotaCodes = (String[]) param.get("quotaCodes");
		}
		if (param.containsKey("authUser")) {
			authUser = param.get("authUser").toString();
		}
		if (param.containsKey("authKey")) {
			authKey = param.get("authKey").toString();
		}
		
		getQuotaValue();
	}

	private void getQuotaValue() {
		//获取添加时间
		String createTime = service.parse5MinuteTime();
		List<String> quotaAndUnitList = this.getQuotaAndUnitList();
		String quotaStr = this.getQuotaStr();
		//循环服务器列表
		for (int i = 0; i < interfaceUrls.length; i++) {
			String ip = interfaceUrls[i];
			String parentIp = ip.split(":")[0];
			//1、登录及获取站点列表
			List<String> sites = null;
			String authToken = service.login(ip,authUser,authKey);
			if(null != authToken && !authToken.equals("")) {
				sites = service.getSites(ip, authToken);
			}
			if (null == sites || sites.size() <= 0) {
				continue;
			}
			//2、按站点获取数据
			for (int j = 0; j < sites.size(); j++) {
				String site = sites.get(j);
				//3、获取站点下的资源数据
				List<Map<String,String>> resList = dao.ListResourceBySite(parentIp, site);
				if (null == resList || resList.size() <= 0) {
					continue;
				}
				//4、获取指标数据
				List<Map<String, String>> quotaValueList = service.listSiteAllQuotaValue(ip, authToken, site, resList, quotaCodes);
				if (null == quotaValueList || quotaValueList.size() <= 0) {
					continue;
				}
				//5、添加表huawei_quota_value数据
				dao.insertBatchQuotaValue(quotaAndUnitList, quotaStr, quotaValueList, createTime);
			}
			
		}
		
		System.out.println("获取指标数据 结束（getQuotaValue end） --> "+ new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒")
				.format(new Date()) + "==============");
	}
	
	/**
	 * 指标封装为字符串
	 * @Title: getQuotaStr
	 * @return String
	 * @throws
	 */
	private String getQuotaStr() {
		StringBuilder sBuilder = new StringBuilder();
		for (int i = 0; i < quotaCodes.length; i++) {
			sBuilder.append(quotaCodes[i] + ",");
			sBuilder.append(quotaCodes[i] + "_unit,");
		}
		String sb = sBuilder.toString();
		return sb.substring(0, sb.length()-1);
	}
	
	/**
	 * 封装指标单位到集合中
	 * @Title: getQuotaAndUnitList
	 * @return List<String>
	 * @throws
	 */
	private List<String> getQuotaAndUnitList(){
		List<String> quotas = new ArrayList<>();
		for (int i = 0; i < quotaCodes.length; i++) {
			quotas.add(quotaCodes[i]);
			quotas.add(quotaCodes[i]+"_unit");
		}
		return quotas;
	}
}
