package com.passenger.financial.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.*;

public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static final CloseableHttpClient httpClient = HttpClients.createDefault();
    /**
     * http请求格式
     */
    private static final String HTTP_CHARSET = "utf-8";
    /**
     * 参数分割符
     */
    private static final String[] PARAMETER_SEPARATOR = {"?", "=", "&"};
    /**
     * XML的Content-Type
     */
    private static final String XML_CONTENT_TYPE = "text/xml; charset=UTF-8";
    /**
     * Json的Content-Type
     */
    private static final String JSON_CONTENT_TYPE = "application/json";
    /**
     * 连接超时时间
     */
    private static final int CONNECT_TIME_OUT = 30000;
    /**
     * 读取超时时间
     */
    private static final int READ_TIME_OUT = 30000;
    /**
     * 设置默认配置(连接超时时间)
     */
    private static RequestConfig getDefaultConfigure(){
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(CONNECT_TIME_OUT).setConnectTimeout(CONNECT_TIME_OUT)
                .setSocketTimeout(READ_TIME_OUT).build();
        return requestConfig;
    }

    /**
     * 发送get请求
     * @param url
     * @return
     */
    public static String get(String url){
        return get(url, null);
    }

    /**
     * 发送get请求(带参数)
     * @param url
     * @param param
     * @return
     */
    public static String get(String url, Map<String,Object> param){
        logger.debug("get url:{},param:{}",url,param);
        String result = null;
        CloseableHttpResponse response = null;
        RequestConfig requestConfig = getDefaultConfigure();
        try {
            url = url + stitchParameters(param);
            logger.info("request wechat token url :{}",url);
            HttpGet httpGet = new HttpGet(url);
            httpGet.setConfig(requestConfig);
            response = httpClient.execute(httpGet);
            result = EntityUtils.toString(response.getEntity(),HTTP_CHARSET);
            logger.debug("result:{}",result);
        } catch (Exception e) {
            logger.error("do get error:",e);
        } finally {
            //关闭资源
            closeResource(response);
        }
        return result;
    }

    /**
     * 发送post请求(不带参数)
     * @param url
     * @return
     */
    public static String post(String url){
        return post(url, null);
    }

    public static String postHttp(String url,String body) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(10000)
                .setConnectionRequestTimeout(10000)
                .setSocketTimeout(10000)
                .build();
        httpPost.setConfig(requestConfig);
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");//表示客户端发送给服务器端的数据格式
        httpPost.setHeader("Accept", "*/*");//这样也ok,只不过服务端返回的数据不一定为json
        httpPost.setHeader("Accept", "application/json");                    //表示服务端接口要返回给客户端的数据格式，
        StringEntity entity = new StringEntity(body, ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = httpclient.execute(httpPost);
        String result = EntityUtils.toString(response.getEntity(),HTTP_CHARSET);
        logger.info("send message wechat return result: {}",result);
        return result;
    }


    /**
     * 发送post请求(带参数)
     * @param url 请求的URL
     * @param param 请求的参数
     * @return
     */
    public static String post(String url, Map<String,Object> param){
        logger.debug("post url:{},param:{}", url, param);
        CloseableHttpResponse response = null;
        String result = null;
        RequestConfig requestConfig = getDefaultConfigure();
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            List<NameValuePair> params = conversionParameter(param);
            httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP_CHARSET));
            response = httpClient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity(),HTTP_CHARSET);
            logger.info("result:{}",result);
        } catch (IOException e) {
            logger.error("do post error:{}",e);
        } finally {
            //关闭资源
            closeResource(response);
        }
        return result;
    }

    /**
     * 发送post请求,提交格式为json
     * @param url
     * @param json
     * @return
     */
    public static String postJson(String url, String json){
        logger.debug("post json url:{},json:{}",url, json);
        return postByType(url,JSON_CONTENT_TYPE,json);
    }

    /**
     * 发送post请求,提交格式为xml
     * @param url
     * @param xml
     * @return
     */
    public static String postXml(String url, String xml){
        logger.debug("post xml url:{},xml:{}",url, xml);
        return postByType(url,XML_CONTENT_TYPE,xml);
    }

    /**
     * 按照类型发送post请求
     * @param url 请求地址
     * @param postType 请求类型
     * @param content
     * @return
     */
    private static String postByType(String url, String postType, String content){
        logger.debug("post by type url:{},postType:{},content:{}",url, postType, content);
        CloseableHttpResponse response = null;
        String result = null;
        RequestConfig requestConfig = getDefaultConfigure();
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            StringEntity entity = new StringEntity(content,HTTP_CHARSET);
            httpPost.setEntity(entity);
            httpPost.setHeader(HTTP.CONTENT_TYPE, JSON_CONTENT_TYPE);
            response = httpClient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity());
            logger.debug("post by type result:{}",result);
        } catch (IOException e) {
            logger.error("do post by type error:{}",e);
        } finally {
            //关闭资源
            closeResource(response);
        }
        return result;
    }
    /**
     * 关闭资源方法
     * @param response
     */
    private static void closeResource(CloseableHttpResponse response){
        if (null == response){
            return;
        }
        try {
            response.close();
        } catch (IOException e) {
            logger.error("response close error:{}",e);
        }
    }
    /**
     * 参数格式转换
     * @param param
     * @return
     */
    private static List<NameValuePair> conversionParameter(Map<String, Object> param){
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        if (null == param){
            return list;
        }
        Set<String> keys = param.keySet();
        Iterator<String> iterator = keys.iterator();
        try {
            while(iterator.hasNext()){
                String key = iterator.next();
                Object value = param.get(key);
                ObjectMapper mapper = new ObjectMapper();
                String newValue = mapper.writeValueAsString(value);
                NameValuePair nameValuePair = new BasicNameValuePair(key,newValue);
                list.add(nameValuePair);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * get方法拼接参数使用
     * @param param 参数
     * @return
     */
    private static String stitchParameters( Map<String,Object> param){
        String result = "";
        if (null == param){
            return result;
        }
        try {
            StringBuilder builder = new StringBuilder();
            Set<String> keys = param.keySet();
            Iterator<String> iterator = keys.iterator();
            builder.append(PARAMETER_SEPARATOR[0]);
            while(iterator.hasNext()){
                String key = iterator.next();
                Object value = param.get(key);
                ObjectMapper mapper = new ObjectMapper();
                String newValue = mapper.writeValueAsString(value);
                newValue = URLEncoder.encode(newValue,HTTP_CHARSET);
                builder.append(key).append(PARAMETER_SEPARATOR[1]).append(newValue).append(PARAMETER_SEPARATOR[2]);
            }
            result = builder.toString();
            result = result.substring(0,result.length() - 1);
        } catch (JsonProcessingException e) {
            logger.error("json error:{}",e);
        } catch (UnsupportedEncodingException e2){
            logger.error("urlEncode error:{}",e2);
        }
        return result;
    }

    public static String getHttp(String url, Map<String, String> params) {
        String responseStr = null;
        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        if (params != null) {
            Iterator<Map.Entry<String, String>> iter = params.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                String key = entry.getKey();
                String value = entry.getValue();
                BasicNameValuePair basicNameValuePair = new BasicNameValuePair(key, value);
                nameValuePairList.add(basicNameValuePair);
            }
        }

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setRetryHandler(new DefaultHttpRequestRetryHandler(5, false))
                .setDefaultRequestConfig(RequestConfig.custom().setConnectionRequestTimeout(3 * 1000)
                        .setSocketTimeout(5 * 1000).setConnectTimeout(3 * 1000).build())
                .build();

        HttpGet httpGet = null;
        try {

            URI uri = new URIBuilder(url).setParameters(nameValuePairList).build();
            httpGet = new HttpGet(uri);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200){
                responseStr = EntityUtils.toString(response.getEntity());
            }

        }catch (Exception ex){

            logger.error("HttpClient Execute error", ex);

        }finally {

            if (httpGet != null){
                httpGet.releaseConnection();
            }
            HttpClientUtils.closeQuietly(httpClient);
        }
        return responseStr;
    }

    public static void main(String[] args) {
    }
}
