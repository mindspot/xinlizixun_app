package com.kuangji.paopao.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kuangji.paopao.dto.UserScheduleStatusDTO;
import com.kuangji.paopao.service.ConsultantScheduleService;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.util.ServiceResultUtils;

/**
 * Author 金威正 Date 2020-02-27
 */
@RestController
@RequestMapping(value = "/v1/userSchedule")
public class ConsultantScheduleController {
	@Autowired
	private ConsultantScheduleService userScheduleService;

	@PostMapping(value = "/insert")
	public Object insert(@RequestBody UserScheduleStatusDTO userScheduleDTO,HttpServletRequest request) {
		

		String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer userId=JwtUtils.getUserId(token);
		userScheduleService.insertBatch(userId, userScheduleDTO);
		return ServiceResultUtils.success(null);
	}

}
