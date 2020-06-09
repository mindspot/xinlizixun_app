package com.kuangji.paopao.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.vo.ExtVO;

public class HttpUtils {
	private static final Logger LOG = LoggerFactory.getLogger(HttpUtils.class);
	private static String defaulUserHeadImage= PropertiesFileUtil.getInstance().get("default_user_head_image");

	
	
	@SuppressWarnings("null")
	public static String post(String url,Map<String, String> request) {
		
		if (request==null&&request.isEmpty()) {
			
			throw new ServiceException(ResultCodeEnum.FAIL);
		}
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		
		// 创建Post请求 
		HttpPost httpPost = new HttpPost(url);
		
		httpPost.addHeader("Content-Type","application/json");
		httpPost.addHeader("Accept","application/json");
		String token=request.get("Authorization");
		httpPost.addHeader("Authorization",token);
		
        // 响应模型
		CloseableHttpResponse response = null;
		try {	
			StringEntity requestEntity = new StringEntity(JSONObject.toJSONString(request).replace("\\", ""),"utf-8");
			httpPost.setEntity(requestEntity);

			// 由客户端执行(发送)
			response = httpClient.execute(httpPost);
			
			// 从响应模型中获取响应实体
			HttpEntity responseEntity = response.getEntity();
			LOG.info("环信发送消息  "+response.getStatusLine().getStatusCode());
			if (responseEntity != null&&response.getStatusLine().getStatusCode()==200) {
				return EntityUtils.toString(responseEntity);
			}
			throw  new  ServiceException(ResultCodeEnum.ERROR_MESSAGE_ERR);
		} catch (Exception e) {
			throw  new  ServiceException(ResultCodeEnum.ERROR_MESSAGE_ERR);
		} finally {
			try {
				// 释放资源
				if (httpClient != null) {
					httpClient.close();
				}
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	
	@SuppressWarnings("null")
	public static String msg(String url,String token,Map<String, Object> request) {

		if (request==null&&request.isEmpty()) {
			
			throw new ServiceException(ResultCodeEnum.FAIL);
		}
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		
		// 创建Post请求 
		HttpPost httpPost = new HttpPost(url);
		
		httpPost.addHeader("Content-Type","application/json");
		httpPost.addHeader("Accept","application/json");
		httpPost.addHeader("Authorization",token);
		
        // 响应模型
		CloseableHttpResponse response = null;
		try {	
			
		
			String msg=JSONObject.toJSONString(request);
	
			LOG.info(msg);
			
			StringEntity requestEntity = new StringEntity(msg,"utf-8");
		
			
			httpPost.setEntity(requestEntity);

			// 由客户端执行(发送)
			response = httpClient.execute(httpPost);
		
			// 从响应模型中获取响应实体
			HttpEntity responseEntity = response.getEntity();
			LOG.info(JSONObject.toJSONString(responseEntity));
			
			LOG.info("环信状态为: "+response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode()==401) {
				
				return String.valueOf(401);
			}
			int code=response.getStatusLine().getStatusCode();
			if (responseEntity != null&&code==200) {
				return EntityUtils.toString(responseEntity);
			}
			if (responseEntity != null&&code==400) {
				return String.valueOf(code);
			}
			
			throw  new  ServiceException(ResultCodeEnum.ERROR_MESSAGE_ERR);
		} catch (Exception e) {
			
			throw  new  ServiceException(ResultCodeEnum.ERROR_MESSAGE_ERR);
		} finally {
			try {
				// 释放资源
				if (httpClient != null) {
					httpClient.close();
				}
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	
	
	//下订单之后发送订单消息
	public static String msgOrder(String url,String token,String form ,String user,ExtVO extVO) {
		 Map<String, Object> map =new HashMap<String, Object>();
			
			map.put("target_type",  "users");
			JSONArray jsonArray=new JSONArray();
			jsonArray.add(user);
			map.put("target", jsonArray );
		
			Map<String, String>msg=new HashMap<>(4);
			msg.put("type", "txt");
			msg.put("msg", "订单确认");
			
			map.put("msg", msg);
			map.put("from",  form);
			map.put("ext", JSONObject.toJSON(extVO));
		
		   return HttpUtils.msg(url, token, map);
	}
	
		//发送订单确定消息
	public static String msgConfirmOrder(String url,
				String token,
				String from,
				String user,
				String orderNo,
				String consultantName,
				String headImg,
				String status,
				String orderTime,
				String userType,
				String msgType) {
			 Map<String, Object> map =new HashMap<String, Object>();
				if (StringUtils.isBlank(headImg)) {
					headImg=defaulUserHeadImage;
				}
				map.put("target_type",  "users");
				JSONArray jsonArray=new JSONArray();
				jsonArray.add(user);
				map.put("target", jsonArray );
				Map<String, String>msg=new HashMap<>(4);
				msg.put("type", "txt");
				if (StringUtils.isBlank(userType)) {
					userType="咨询师";
				}else{
					userType="用户";
				}
				StringBuffer buffer=new StringBuffer("订单：")
						.append(orderNo)
						.append("\n")
						.append(status);
				msg.put("msg", buffer.toString());
				map.put("msg", msg);
				map.put("from", from);
				ExtVO extVO =new ExtVO();
				extVO.setUserphoto(headImg);
				extVO.setNickname(consultantName);
				if (StringUtils.isNoneBlank(orderTime)) {
					extVO.setServiceTime(orderTime);	
				}
				if (StringUtils.isBlank(msgType)) {
					extVO.setMsgType(msgType);
				}
				extVO.setServiceOrderid(orderNo);
				extVO.setServiceTime(orderTime);
				map.put("ext",JSONObject.toJSON(extVO));
				LOG.info("这是我发送的值 map    "+map.toString());
			   return HttpUtils.msg(url, token, map);
		}

	@SuppressWarnings("null")
	public static String get(String url,String token) {
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		// 创建Post请求 
		HttpGet httpGet = new HttpGet(url);
		
		httpGet.addHeader("Content-Type","application/json");
		httpGet.addHeader("Accept","application/json");
		httpGet.addHeader("Authorization",token);
		
        // 响应模型
		CloseableHttpResponse response = null;
		try {	
			
			// 由客户端执行(发送)
			response = httpClient.execute(httpGet);
			
			// 从响应模型中获取响应实体
			HttpEntity responseEntity = response.getEntity();
			LOG.info("请求环信内容环信   url "+url+" token "+token);
			LOG.info("环信发送消息  "+response.getStatusLine().getStatusCode());
			if (responseEntity != null&&response.getStatusLine().getStatusCode()==200) {
				return EntityUtils.toString(responseEntity);
			}
			throw  new  ServiceException(ResultCodeEnum.ERROR_MESSAGE_ERR);
		} catch (Exception e) {
			throw  new  ServiceException(ResultCodeEnum.ERROR_MESSAGE_ERR);
		} finally {
			try {
				// 释放资源
				if (httpClient != null) {
					httpClient.close();
				}
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
