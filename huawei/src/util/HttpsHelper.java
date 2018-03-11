package util;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import client.SSLClient;

public class HttpsHelper {

    public static Map doHttpPost(String url,Map<String, String> headerMap, String postData) {
        Map resultMap = new HashMap();
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
            //建立连接
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(url);

//        	httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json; charset=UTF-8");
//        	httpPost.setHeader("Accept", "application/json; charset=UTF-8");
            //设置请求头
            if (null!=headerMap) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            //设置参数
            StringEntity entity = new StringEntity(postData, "UTF-8");
            httpPost.setEntity(entity);
            //获取响应信息
            HttpResponse response = httpClient.execute(httpPost);
            int rspCode = response.getStatusLine().getStatusCode();
            if (rspCode == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), "UTF-8");
                resultMap.put("result", result);
            }
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(httpPost != null) {
                httpPost.releaseConnection();
            }
            //关闭连接 ,释放资源
            httpClient.getConnectionManager().shutdown();
        }
        return null;
    }

        /**
         * 执行POST请求
         * @param url 请求路径
         * @param headerMap 请求头参数
         * @param postData 请求数据，JSON格式
         * @return  Map {"result":"","headers":{"",""}}
         */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map doPost(String url,Map<String, String> headerMap, String postData) {  
    	Map resultMap = new HashMap();
    	HttpClient httpClient = null;
    	HttpPost httpPost = null;  
        String result = null;  
        try {
        	//建立连接
        	httpClient = new SSLClient();  
            httpPost = new HttpPost(url);  
  
//        	httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json; charset=UTF-8");  
//        	httpPost.setHeader("Accept", "application/json; charset=UTF-8");
            //设置请求头
            if (null!=headerMap) {
            	for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            		httpPost.setHeader(entry.getKey(), entry.getValue());
				}
			}
        	//设置参数
            StringEntity entity = new StringEntity(postData, "UTF-8");  
            httpPost.setEntity(entity);  
            //获取响应信息
            HttpResponse response = httpClient.execute(httpPost);
            int rspCode = response.getStatusLine().getStatusCode();  
            if (rspCode == HttpStatus.SC_OK) {
            	result = EntityUtils.toString(response.getEntity(), "UTF-8");
            	//响应获取头信息
            	Header[] headers = response.getAllHeaders();
            	Map<String,String> resultHeaderMap = new HashMap<>();
            	for (int i = 0; i < headers.length; i++) {
					resultHeaderMap.put(headers[i].getName(), headers[i].getValue());
				}
            	resultMap.put("result", result);
            	resultMap.put("headers", resultHeaderMap);
			}
            return resultMap;  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if(httpPost != null) {  
            	httpPost.releaseConnection();  
            } 
            //关闭连接 ,释放资源  
            httpClient.getConnectionManager().shutdown();  
        }  
        return null;  
    }  
    
    /**
     * 执行GET请求 
     * @Title: doGet
     * @param url 请求路径
     * @param headerMap 请求头参数
     * @return Map {"result":"","headers":{"",""}}
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map doGet(String url,Map<String, String> headerMap) {  
    	Map resultMap = new HashMap();
    	String result = "";
        HttpGet httpGet = null;
        HttpClient client = null;
        try {  
        	// Get请求  
        	client = new SSLClient();  
        	httpGet = new HttpGet(url);
            //设置请求头
            if (null!=headerMap) {
            	for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            		httpGet.setHeader(entry.getKey(), entry.getValue());
				}
			}
            //httpGet.setHeader(HTTP.CONTENT_TYPE, "application/json; charset=UTF-8");  
            //httpGet.setHeader("Accept", "application/json; charset=UTF-8");  
            //List<NameValuePair> params = new ArrayList<NameValuePair>();
            //params.add(new BasicNameValuePair("msg", "{\"fileName\":\"英语.txt\",\"password\":\"123\",\"subType\":\"Storage\",\"time\":\"2018-01-11 04:29:28 235\",\"type\":\"res\",\"organ_id\":\"0\",\"username\":\"ftpUser\"}"));  
            // 设置参数  
            //httpGet.setURI(new URI(httpGet.getURI().toString() + "?"+ URLEncodedUtils.format(params, "UTF-8")));  
            // 发送请求  
            HttpResponse response = client.execute(httpGet);  
            // 获取返回数据  
            int rspCode = response.getStatusLine().getStatusCode();  
            
            if (rspCode == HttpStatus.SC_OK) {
            	result = EntityUtils.toString(response.getEntity(), "UTF-8");
            	//响应获取头信息
            	Header[] headers = response.getAllHeaders();
            	Map<String,String> resultHeaderMap = new HashMap<>();
            	for (int i = 0; i < headers.length; i++) {
					resultHeaderMap.put(headers[i].getName(), headers[i].getValue());
				}
            	resultMap.put("result", result);
            	resultMap.put("headers", resultHeaderMap);
			}  
            return resultMap; 
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if(httpGet != null) {  
            	httpGet.releaseConnection();  
            }  
            //关闭连接 ,释放资源  
            client.getConnectionManager().shutdown();  
        } 
        return null;  
    }
    
}
