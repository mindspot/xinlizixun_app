package com.kuangji.paopao.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.model.User;
import com.kuangji.paopao.redis.send.SendService;
import com.kuangji.paopao.service.UserService;
import com.kuangji.paopao.util.ServiceResultUtils;

/**
 * 用户登入
 * Author 金威正 Date 2020-02-17
 */
@RestController
public class MemberLoginController {
	@Autowired
	private UserService userService;

	@Autowired
	private SendService sendService;
	
	@PostMapping(value = "/user/phoneLogin")
	public Object phoneLogin(
			@RequestParam(defaultValue = "") String phone,
			@RequestParam(defaultValue = "")  String code,
			@RequestParam(defaultValue = "")  String spreadUrl
			) {
		if (StringUtils.isBlank(phone)) {
			throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_EMP_USER_NAME);
		}
		Map<String, Object> map = userService.phoneLogin(phone, code,spreadUrl);

		return ServiceResultUtils.success(map);
	}

	@PostMapping(value = "/user/pwdLogin")
	public Object login(@RequestParam(defaultValue = "") String phone,  String pwd) {

		Map<String, Object> map = userService.Login(phone, pwd);

		return ServiceResultUtils.success(map);
	}

	@GetMapping(value = "/user/info")
	public Object getUserInfo(@RequestParam(defaultValue = "") String token) {
		User user=userService.getUserInfo(token);
		return ServiceResultUtils.success(user);
	}

	//获取短信验证
	@PostMapping(value = "/get/code")
	public Object getCode(@RequestParam(defaultValue = "") String phone) {
		String repo=sendService.sendMsg(phone);
		if (repo.length()>2) {
			return ServiceResultUtils.success(null);
		}
		return ServiceResultUtils.failure(ResultCodeEnum.ERROR_LOGIN_CODE);
	}
	
	
	//获取修改密码时候的短信验证
	@PostMapping(value = "/v1/get/code")
	public Object updateGetCode(HttpServletRequest request) {
		String token = request.getHeader("authorization").replace("Bearer ", "");

		String repo=sendService.updatePwdSendMsg(token);
		if (repo.length()>2) {
			return ServiceResultUtils.success(null);
		}
		return ServiceResultUtils.failure(ResultCodeEnum.ERROR_LOGIN_CODE);
	}
	
	//修改 密码
	@PostMapping(value = "/v1/user/updatePwd")
	public Object updatePwd(
			HttpServletRequest request,
			@RequestParam(defaultValue = "") String pwd, 
			@RequestParam(defaultValue = "") String  code) {
		String token = request.getHeader("authorization").replace("Bearer ", "");

		int size = userService.updatePwd(token, pwd, code);
		if (size < 1) {
			return ServiceResultUtils.failure(ResultCodeEnum.FAIL);
		}
		return ServiceResultUtils.success(null);
	}
}
