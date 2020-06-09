package com.kuangji.paopao.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kuangji.paopao.service.SendSmsService;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.util.ServiceResultUtils;

/**
 * Author 金威正 Date 2020-02-17
 */
@RestController
public class SendSmsController{
	@Autowired
	private SendSmsService sendSmsService;
	
	//短信通知上线
	@PostMapping(value = "/v1/sendSms")
	public Object getUpdateUser(HttpServletRequest request,String easemobId) {
		String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer userId=JwtUtils.getUserId(token);
		sendSmsService.sendSms(userId,easemobId);
		return ServiceResultUtils.success(null);	
	}
	
}
