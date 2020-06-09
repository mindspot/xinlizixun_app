package com.kuangji.paopao.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kuangji.paopao.dto.UserUpdateDTO;
import com.kuangji.paopao.model.User;
import com.kuangji.paopao.service.UserService;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.util.ServiceResultUtils;
import com.kuangji.paopao.vo.UserUpdateVO;

/**
 * Author 金威正 Date 2020-02-17
 */
@RestController
public class UserController {
	@Autowired
	private UserService userService;
	
	//获取修改页面资料
	@GetMapping(value = "/v1/user/update")
	public Object getUpdateUser(HttpServletRequest request) {
		String token = request.getHeader("authorization").replace("Bearer ", "");
		UserUpdateVO updateVO = userService.getUserUpdateVO(token);
		if (updateVO==null) {
			return ServiceResultUtils.failure("-1","无该账户信息");
		}
		return ServiceResultUtils.success(updateVO);
		
	}
	
	//修改 修改页面参数
	@PostMapping(value = "/v1/user/update")
	public Object updateUser(UserUpdateDTO userUpdateDTO, HttpServletRequest request) {
		String token = request.getHeader("authorization").replace("Bearer ", "");
		UserUpdateDTO dto =userService.userUpdateVO(token, userUpdateDTO);
		return ServiceResultUtils.success(dto);
	}

	//客户端获取 账户余额
	@GetMapping(value = "/v1/user/account")
	public Object getAccount(HttpServletRequest request) {
			String token = request.getHeader("authorization").replace("Bearer ", "");
			Integer consultantId=JwtUtils.getUserId(token);
	    	User user=userService.getUserforUpdate(consultantId);
	    	if (user==null) {
	    		return ServiceResultUtils.failure("-1","无该账户信息");
			}
	    	Integer account=user.getAccount();
			return ServiceResultUtils.success(account);
	}
	
	//查看环信用户 在不在线
	@GetMapping(value = "/v1/user/status")
	public Object getAccount(HttpServletRequest request,String easemobId) {
		String status=userService.getStatus(easemobId);
		return ServiceResultUtils.success(status);
	}
	
}
