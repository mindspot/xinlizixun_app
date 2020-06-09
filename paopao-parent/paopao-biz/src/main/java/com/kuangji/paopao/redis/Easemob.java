package com.kuangji.paopao.redis;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kuangji.paopao.redis.util.RedisUtils;
import com.kuangji.paopao.util.HttpUtils;
import com.kuangji.paopao.util.PropertiesFileUtil;
import com.kuangji.paopao.vo.ExtStopMsgVO;
import com.kuangji.paopao.vo.ExtVO;

//环信
@Component
public class Easemob {

	@Autowired
	private RedisUtils redisUtils;

	private static String easemobtokenUrl = PropertiesFileUtil.getInstance().get("easemob_token_url");

	private static String easemobSendMsgUrl = PropertiesFileUtil.getInstance().get("easemob_send_msg_url");

	private static String easemobCreateUrl = PropertiesFileUtil.getInstance().get("easemob_create_url");

	private static String easemobStatusUrl = PropertiesFileUtil.getInstance().get("easemob_status_url");
	
	private static String client_id = PropertiesFileUtil.getInstance().get("client_id");
	
	private static String client_secret = PropertiesFileUtil.getInstance().get("client_secret");
	
	private static final String EASEMOB_KEY = "EASEMOB_KEY";

	private static final String BEFORE_STOP_MSG = "您好，本次服务时间剩余五分钟，为不占用其他用户的服务时间，系统将在五分钟后关闭音视频功能，敬请谅解，谢谢。";

	
	

	public String getEasemobToken() {
		synchronized (this) {
			String reslult = (String) redisUtils.get(EASEMOB_KEY);
			if (StringUtils.isNoneBlank(reslult)) {
				return reslult;
			}
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("grant_type", "client_credentials");
		map.put("client_id", client_id);
		map.put("client_secret", client_secret);
		String json = (String) HttpUtils.post(easemobtokenUrl, map);
		JSONObject jsonObject = JSONObject.parseObject(json);
		String token = (String) jsonObject.get("access_token");
		Integer expiresTime = (Integer) jsonObject.get("expires_in");
		redisUtils.set(EASEMOB_KEY, token, expiresTime - 5000);
		return token;
	}

	// 发送确认订单
	public String sendOrderMsg(String from, String user, ExtVO extVO) {
		String token = "Bearer " + getEasemobToken();

		String json = (String) HttpUtils.msgOrder(easemobSendMsgUrl, token, from, user, extVO);

		return json;
	}
	//用户登入消息
	public String sendLogin(String user,String content,String userphoto) {
			String token = "Bearer " + getEasemobToken();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("target_type", "users");
			JSONArray jsonArray = new JSONArray();
			jsonArray.add(user);
			map.put("target", jsonArray);
			Map<String, String> msg = new HashMap<>(4);
			msg.put("type", "txt");
			msg.put("msg", content);
			map.put("msg", msg);
			map.put("from", "系统消息");
			ExtStopMsgVO extStopMsgVO = new ExtStopMsgVO();
			extStopMsgVO.setNickname("");
			extStopMsgVO.setUserphoto(userphoto);
			map.put("ext", JSONObject.toJSON(extStopMsgVO));
			String json = (String) HttpUtils.msg(easemobSendMsgUrl, token, map);
			return json;
		}
	
	// 咨询服务服务停止
	public String sendStopServiceMsg(String user, String consultant, ExtStopMsgVO extStopMsgVO,String conent) {
		String token = "Bearer " + getEasemobToken();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("target_type", "users");
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(consultant);
		map.put("target", jsonArray);
		Map<String, String> msg = new HashMap<>(4);
		msg.put("type", "txt");
		msg.put("msg",conent);
		map.put("msg", msg);
		map.put("from", user);
		map.put("ext", JSONObject.toJSON(extStopMsgVO));
		String json = (String) HttpUtils.msg(easemobSendMsgUrl, token, map);
		return json;
	}

	// 服务停止前5分钟发送消息
	public String sendBeforeStopServiceMsg(String user, String consultant,String nickname,String userphoto) {
		String token = "Bearer " + getEasemobToken();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("target_type", "users");
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(consultant);
		map.put("target", jsonArray);
		Map<String, String> msg = new HashMap<>(4);
		msg.put("type", "txt");
		msg.put("msg", BEFORE_STOP_MSG);
		map.put("msg", msg);
		map.put("from", user);
		ExtStopMsgVO extStopMsgVO = new ExtStopMsgVO();
		extStopMsgVO.setCode("2");
		extStopMsgVO.setNickname(nickname);
		extStopMsgVO.setUserphoto(userphoto);
		extStopMsgVO.setContent(BEFORE_STOP_MSG);
		map.put("ext", JSONObject.toJSON(extStopMsgVO));
		String json = (String) HttpUtils.msg(easemobSendMsgUrl, token, map);
		return json;
	}

	// 创建用户
	public String createUser(String username, String password) {
		String token = "Bearer " + getEasemobToken();
		HashMap<String, Object> map = new HashMap<>();
		map.put("username", username);
		map.put("password", password);
		String json = (String) HttpUtils.msg(easemobCreateUrl, token, map);
		return json;
	}

	// 推送多人消息

	/*
	 * public String sendGroupServiceMsg(Set<String> users,String mgs,ExtGroupVO
	 * extGroupVO) { String token="Bearer "+getEasemobToken(); Map<String,
	 * Object> map =new HashMap<String, Object>(); map.put("target_type",
	 * "users"); JSONArray jsonArray=new JSONArray(); for (String user : users)
	 * { jsonArray.add(user); } map.put("target", jsonArray ); Map<String,
	 * String>msg=new HashMap<>(4); msg.put("type", "txt"); msg.put("msg",mgs);
	 * map.put("msg", msg); map.put("from", "系统消息"); map.put("ext",
	 * JSONObject.toJSON(extGroupVO)); String json=(String)
	 * HttpUtils.msg(easemobSendMsgUrl, token, map);
	 * 
	 * return json; }
	 */

	// 订单消息确认
	public String msgConfirmOrder(String from, String user, String orderNo, String consultantName,
			String defaulUserHeadImage, String status,String orderTime,String userType,String msgType) {
		String token = "Bearer " + getEasemobToken();
		String code = HttpUtils.msgConfirmOrder(easemobSendMsgUrl, token,from, user, orderNo, consultantName,
				defaulUserHeadImage, status,orderTime,userType,msgType);
	
		return code;
	}
	
	//获取用户 是否在线状态
	public String userStatus(String user){
		String getUserStatusUrl=easemobStatusUrl+user+"/status";
		String token = "Bearer " + getEasemobToken();
		String json=HttpUtils.get(getUserStatusUrl, token);
		return json;
	}

}
