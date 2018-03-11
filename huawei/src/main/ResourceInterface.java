package main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import com.alibaba.fastjson.JSON;

import util.HttpsHelper;
import dao.ClientDao;
import service.InterfaceService;

public class ResourceInterface extends TimerTask{

	private Map param;
	private String[] interfaceUrls; 
	private String authUser; 
	private String authKey;
	
	public ResourceInterface(Map param) {
		super();
		this.param = param;
	}

	InterfaceService service = new InterfaceService();
	ClientDao dao = new ClientDao();
	
	@Override
	public void run() {
		System.out.println("获取资源数据 开始（getResources start） --> "+ new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒")
				.format(new Date()) + "==============");
		if (null == param || param.isEmpty()) {
			return;
		}
		if (param.containsKey("interfaceUrls")) {
			interfaceUrls = (String[]) param.get("interfaceUrls");
		}
		if (param.containsKey("authUser")) {
			authUser = param.get("authUser").toString();
		}
		if (param.containsKey("authKey")) {
			authKey = param.get("authKey").toString();
		}
		
		getResources();
	}

	private void getResources() {
		//循环服务器列表
		List<Map<String, String>> resourceList = new ArrayList<>();
		for (int i = 0; i < interfaceUrls.length; i++) {
			String ip = interfaceUrls[i];
			//1、登录及获取站点列表
			List<String> sites = null;
			String authToken = service.login(ip,authUser,authKey);
			if(null != authToken && !authToken.equals("")) {
				sites = service.getSites(ip, authToken);
			}
			//2、获取资源数据
			List<Map<String, String>> resList = service.listAllResourceInfo(ip, authToken, sites);
			if (null == resList || resList.size() <= 0) {
				continue;
			}
			//3、添加到一个集合
			resourceList.addAll(resList);
		}
		//4、清空资源表huawei_resource
		boolean deleteFlag = dao.deleteResources();
		//5、huawei_resource添加数据
		dao.insertBatchResources(resourceList);
		System.out.println("获取资源数据 结束（getResources end）--> "+ new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒")
				.format(new Date()) + "==============");
	}
	
}
