package com.kuangji.paopao.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.constant.CommonConstant;
import com.kuangji.paopao.enums.LoginEnum;

public class JwtUtils {
    private static final String SECRET = "com.kaungji.secret";
    private static final String ISSUER = "user";

    /**
     * 生成token
     *
     * @param claims
     * @return
     */
    public static String sign(Map<String, Object> map) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTCreator.Builder builder = JWT.create()
                    .withIssuer(ISSUER);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
    			String mapKey = entry.getKey();
    			String mapValue = String.valueOf(entry.getValue());
    			builder.withClaim(mapKey, mapValue);
    			builder.withClaim("date", new Date());
    		}
            return builder.sign(algorithm);
        } catch (IllegalArgumentException e) {
  			throw new ServiceException(ResultCodeEnum.ERROR_CREATE_TOKEN);

        }
	
    }

    /**
     * 验证jwt，并返回数据
     */
    public static Map<String, Object> verifyToken(String token) {
    	
        Algorithm algorithm=null;
        Map<String, Claim> map = null;
        try {
            algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
            DecodedJWT jwt = verifier.verify(token);
            map = jwt.getClaims();
        } catch (Exception e) {
  			throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_TOKEN);

        }
        Map<String, Object> resultMap = new HashMap<>(map.size());
       
        map.forEach((k, v) -> resultMap.put(k, v.asString()));
   
        return resultMap;
    }
    
    
  	
  	
  	//解析客户的userId
  	public static Integer  getCustomerId(String token) {
  		Map<String, Object> map=verifyToken(token);
  		int userType=Integer.valueOf((String) map.get(CommonConstant.USER_TYPE_K));
  		if (userType!=LoginEnum.LOGIN_USER_TYPE.getIndex()) {

  			throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_USER_TYPE);
  		}
  			return   Integer.valueOf((String) map.get(CommonConstant.USER_ID)); 
      }
  	
	
  	//解析咨询师id
  	public static Integer  getConsultantId(String token) {
  		Map<String, Object> map=verifyToken(token);
  		Integer userType=Integer.valueOf((String) map.get(CommonConstant.USER_TYPE_K));
  	
  		if (userType!=LoginEnum.LOGIN_CONSULTANT_TYPE.getIndex()) {
  			
  			throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_USER_TYPE);
  		}
  			return   Integer.valueOf((String) map.get(CommonConstant.CONSULTANT_ID));
      }
  	
  	//不考虑用户身份 直接返回id
  	public static Integer  getUserId(String token) {
  		Map<String, Object> map=verifyToken(token);
  		
  		return   Integer.valueOf((String) map.get(CommonConstant.USER_ID)); 
      }
}