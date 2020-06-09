package com.kuangji.paopao.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.kuangji.paopao.dto.param.SupervisorOrderParam;
import com.kuangji.paopao.service.ConsultantSupervisorOrderService;
import com.kuangji.paopao.service.UserService;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.util.ServiceResultUtils;

/**
 * Author 金威正 Date 2020-02-20
 */
@RestController
public class WebConsultantSupervisorOrderController {

	
	@Autowired
	private ConsultantSupervisorOrderService supervisorOrderService;
	
	@Autowired
	private UserService userService;

	// 后台查看
	@PostMapping("/web/order/supervisorOrderList")
	public Object adminListOrderList(@RequestBody SupervisorOrderParam param,HttpServletRequest request) {

		String token = request.getHeader("authorization").replace("Bearer ", "");
    	Integer userId=JwtUtils.getUserId(token);
    	if (!userService.isAdmin(userId)) {
    		param.setUserIds(userService.userIds(userId));
		}
	
		List<com.kuangji.paopao.admin.vo.SupervisorOrderVO> list=supervisorOrderService.listSupervisorOrder(param);

		return ServiceResultUtils.success(new PageInfo<>(list));
	}

}
