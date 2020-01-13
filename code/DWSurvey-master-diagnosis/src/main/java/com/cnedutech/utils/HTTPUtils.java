package com.cnedutech.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;


public class HTTPUtils {

    private static Logger logger = LoggerFactory.getLogger(HTTPUtils.class);

    /**
     * post请求
     * @param url
     * @param json
     * @return
     */
    public static String postJosnContent(String url, String Json,Map<String,String> params) throws Exception {
        logger.error("请求接口参数：" + Json);
        PostMethod method = new PostMethod(url);
        method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");

        if(params!=null) {
        	for(java.util.Map.Entry<String, String> entry:params.entrySet()) {
        		method.setParameter(entry.getKey(), entry.getValue());
        	}
        }
        HttpClient httpClient = new HttpClient();
        try {
            RequestEntity entity = new StringRequestEntity(Json,"application/x-www-form-urlencoded","UTF-8");
            method.setRequestEntity(entity);
            httpClient.executeMethod(method);
            logger.error("请求接口路径url：" + method.getURI().toString());
            InputStream in = method.getResponseBodyAsStream();
            //下面将stream转换为String
            StringBuffer sb = new StringBuffer();
            InputStreamReader isr = new InputStreamReader(in, "UTF-8");
            char[] b = new char[4096];
            for(int n; (n = isr.read(b)) != -1;) {
                sb.append(new String(b, 0, n));
            }
            String returnStr = sb.toString();
            return returnStr;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            method.releaseConnection();
        }
    }
    
    public static JSONObject doPostForJson(String url, String jsonParams) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        JSONObject jsonObject = null;
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().
                setConnectTimeout(180 * 1000).setConnectionRequestTimeout(180 * 1000)
                .setSocketTimeout(180 * 1000).setRedirectsEnabled(true).build();
        httpPost.setConfig(requestConfig);
        httpPost.setHeader("Content-Type", "application/json");
        try {
            httpPost.setEntity(new StringEntity(jsonParams, ContentType.create("application/json", "utf-8")));
            System.out.println("request parameters" + EntityUtils.toString(httpPost.getEntity()));
            System.out.println("httpPost:" + httpPost);
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(response.getEntity());
                System.out.println("result:" + result);
                jsonObject = JSONObject.fromObject(result);
                return jsonObject;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return jsonObject;
        }
    }
}