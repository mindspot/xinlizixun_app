package com.kuangji.paopao.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.dto.UserScheduleStatusDTO;
import com.kuangji.paopao.enums.BookingTimeEnum;
import com.kuangji.paopao.service.ConsultantScheduleStatusService;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.util.ServiceResultUtils;

/**
 * Author 金威正
 * Date  2020-02-28
 */
@RestController
@RequestMapping(value = "/v1/userScheduleStatus")
public class ConsultantScheduleStatusController {
    @Autowired
    private ConsultantScheduleStatusService userScheduleStatusService;

    
  
    //某个时间段休息 或者 恢复休息
    @PostMapping(value = "/insert")
    public Object insert(HttpServletRequest request,@RequestBody UserScheduleStatusDTO userScheduleStatusDTO) {
		String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer userId=JwtUtils.getUserId(token);
        if (userScheduleStatusService.insertConsultantScheduleStatus(userId,userScheduleStatusDTO)) {
          return ServiceResultUtils.success(null);
        } 
        
        return ServiceResultUtils.failure(String.valueOf(ResultCodeEnum.FAIL.getCode()),ResultCodeEnum.FAIL.getMsg());
        
    }

    //整天休息 或回复
    @PostMapping(value = "/insertAll")
    public Object insertAll(HttpServletRequest request,String date,Integer timeType) {
  
		String token = request.getHeader("authorization").replace("Bearer ", "");
		if (timeType==null) {
			return ServiceResultUtils.failure("-1","选择状态错");
		}
		if (timeType==BookingTimeEnum.BOOKING.getValue()) {
			return ServiceResultUtils.failure("-1","选择状态错");
		}
		
		if (!BookingTimeEnum.isInclude(timeType)) {
			return ServiceResultUtils.failure("-1","选择状态错");
		}
		Integer consultantId=JwtUtils.getUserId(token);
        if (userScheduleStatusService.insertConsultantScheduleStatus(consultantId,date,timeType)) {
          return ServiceResultUtils.success(null);
        } 
        
       return ServiceResultUtils.failure(String.valueOf(ResultCodeEnum.FAIL.getCode()),ResultCodeEnum.FAIL.getMsg());
        
    }
    
}
