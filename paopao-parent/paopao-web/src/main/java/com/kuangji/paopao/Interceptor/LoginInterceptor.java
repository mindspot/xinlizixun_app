package com.kuangji.paopao.Interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.alibaba.fastjson.JSON;
import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.constant.CommonConstant;
import com.kuangji.paopao.enums.LoginEnum;
import com.kuangji.paopao.enums.UserTypeEnum;
import com.kuangji.paopao.model.User;
import com.kuangji.paopao.redis.util.RedisUtils;
import com.kuangji.paopao.service.UserService;
import com.kuangji.paopao.util.ImageUtils;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.util.PropertiesFileUtil;
import com.kuangji.paopao.util.ServiceResultUtils;


/*
 * 拦截器验证token
 * 
 * */
@Component
public class LoginInterceptor implements HandlerInterceptor {

	@Autowired
	private RedisUtils redisUtils;

	  
    @Autowired
    private UserService userService;
	
	private static final Logger LOG = LoggerFactory.getLogger(LoginInterceptor.class);

    //环信 用户 标志
    public static final String member = PropertiesFileUtil.getInstance().get("easemob_member");

   // 环信 用户 标志
    public static final String consultant = PropertiesFileUtil.getInstance().get("easemob_consultant");
    
    
    private static String imageUrl = PropertiesFileUtil.getInstance().get("image_url");
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object chain) throws Exception {
		 

	final HttpServletResponse res = (HttpServletResponse) response;
	res.setContentType("application/json; charset=utf-8");  
	res.setCharacterEncoding("UTF-8");
	
	String token = request.getHeader("authorization");
	if ("OPTIONS".equals(request.getMethod())) {
		response.setStatus(HttpServletResponse.SC_OK);
	} else {
		if (token == null) {
	
			res.getWriter().write(JSON.toJSONString(ServiceResultUtils.failure(String.valueOf(ResultCodeEnum.ERROR_LOGIN_NOT_NULL_TOKEN.getCode()),ResultCodeEnum.ERROR_LOGIN_NOT_NULL_TOKEN.getMsg())));
//
			return false;
		}
		token=token.replace("Bearer ", "");
		LOG.info("当前token  "+token);
//
		Map<String, Object> userData = JwtUtils.verifyToken(token);
		LOG.info("当前token的 userData "+userData);
		if (userData == null) {
			res.getWriter().write(JSON.toJSONString(ServiceResultUtils.failure(String.valueOf(ResultCodeEnum.ERROR_LOGIN_TOKEN.getCode()),ResultCodeEnum.ERROR_LOGIN_TOKEN.getMsg())));
			return false;
		}
		Integer userId=JwtUtils.getUserId(token);

		String key=CommonConstant.PAO_PAO_APP_TOKEN+userId;
		String redisToken=(String) redisUtils.get(key);
		
		if (redisToken==null) {
			Map<String, String> map=this.getToken(userId,res);
			if (map.containsKey("err")) {
				res.getWriter().write(map.get("err"));
				return false;
			}
			res.getWriter().write(JSON.toJSONString(ServiceResultUtils.failure(
					String.valueOf(ResultCodeEnum.ERROR_LOGIN_BE_OVERDUE_TOKEN.getCode()),
					ResultCodeEnum.ERROR_LOGIN_BE_OVERDUE_TOKEN.getMsg(),
					map.get("success")
					))
				);
			return false;
		}
		LOG.info("redistoken "+redisToken);
		LOG.info("验证结果 "+redisToken.equals(token));
		if (!redisToken.equals(token)) {
			res.getWriter().write(JSON.toJSONString(ServiceResultUtils.failure(String.valueOf(ResultCodeEnum.ERROR_LOGIN_OUT_TOKEN.getCode()),ResultCodeEnum.ERROR_LOGIN_OUT_TOKEN.getMsg())));
			return false;
		}
	}
	
		return true;
	}
	
	
  
    protected Map<String, String> getToken(Integer userId,HttpServletResponse res) throws IOException {
    	Map<String, String> msgMap=new HashMap<>();
    	
		User user=userService.findById(userId);
		if (user==null) {
			msgMap.put("err", JSON.toJSONString(ServiceResultUtils.failure(String.valueOf(ResultCodeEnum.ERROR_LOGIN_NO_USER.getCode()),ResultCodeEnum.ERROR_LOGIN_NO_USER.getMsg())));
			return  msgMap;
          
		}	
		LOG.info("当前用户状态 "+user.getStatus());
		 Map<String, Object> map = new HashMap<>();

		 if (user.getStatus() == LoginEnum.LOGIN_TO_EXAMINE.getIndex()) {
			 msgMap.put("err",  JSON.toJSONString(ServiceResultUtils.failure(String.valueOf(ResultCodeEnum.ERROR_LOGIN_TO_EXAMINE.getCode()),ResultCodeEnum.ERROR_LOGIN_TO_EXAMINE.getMsg())));
			 return msgMap;
         }
         if (user.getStatus() == LoginEnum.LOGIN_RESTRICTED_ENTRY.getIndex()) {
       
			 msgMap.put("err",  JSON.toJSONString(ServiceResultUtils.failure(String.valueOf(ResultCodeEnum.ERROR_LOGIN_RESTRICTED_ENTRY.getCode()),ResultCodeEnum.ERROR_LOGIN_RESTRICTED_ENTRY.getMsg())));
        	
			 return msgMap;
         }
         Integer type= user.getType();
         if (type==UserTypeEnum.MEMBER.getType()) {
        	 
             map.put(CommonConstant.EASEMOBID, member + user.getId());
             map.put(CommonConstant.USER_TYPE_K, UserTypeEnum.MEMBER.getType());
         }
         
         if (type==UserTypeEnum.CONSULTANT.getType()) {
        	 
             map.put(CommonConstant.EASEMOBID, consultant + user.getId());
             map.put(CommonConstant.USER_TYPE_K, UserTypeEnum.CONSULTANT.getType());
         }
         
         map.put(CommonConstant.USER_ID, user.getId());
         map.put(CommonConstant.USER_HEAD_IMG, ImageUtils.getImgagePath(imageUrl, user.getHeadImg()));
         map.put(CommonConstant.REAL_NAME, user.getRealName());
         map.put(CommonConstant.PERSONAL_SIGNATURE, "");
         map.put(CommonConstant.ACCOUNT, user.getAccount());
         String newToken = JwtUtils.sign(map);
         redisUtils.set(CommonConstant.PAO_PAO_APP_TOKEN+userId, newToken, CommonConstant.LOGIN_TIME);
         msgMap.put("success", newToken);
         return msgMap;
    }

    
}