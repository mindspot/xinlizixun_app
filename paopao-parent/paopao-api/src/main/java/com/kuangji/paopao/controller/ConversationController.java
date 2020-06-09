package com.kuangji.paopao.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kuangji.paopao.service.MallTradeOrderService;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.util.ServiceResultUtils;

/**
 * 验证通话
 * Author 金威正
 * Date  2020-02-22
 */
@RestController
public class ConversationController {
    @Autowired
    private MallTradeOrderService mallTradeOrderService;


    
    //客户端 验证客户与发起咨询师服务
    @PostMapping(value = {"/v1/conversation"})
    public Object conversation(HttpServletRequest request,
    		 @RequestParam("userEasemobId")String userEasemobId,
    		 @RequestParam("consultantEasemobId")String consultantEasemobId,
    		 @RequestParam("serviceType")Integer serviceType
    		 ) {
   
    	mallTradeOrderService.memberConversation(userEasemobId, consultantEasemobId, serviceType);
		
    	return ServiceResultUtils.success(null);

    }
    
    
    //咨询师端验证 咨询师跟客户发起的服务
    @PostMapping(value = {"/v1/consultationWork/conversation"})
    public Object consultationWorkConversation(HttpServletRequest request,
    		 @RequestParam("userEasemobId")String userEasemobId,
    		 @RequestParam("serviceType")Integer serviceType
    		 ) {
    	String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer userId=JwtUtils.getUserId(token);
    	mallTradeOrderService.consultantConversation(userId, userEasemobId, serviceType);
    	return ServiceResultUtils.success(null);

    }
    
    
   
}