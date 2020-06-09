package com.kuangji.paopao.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kuangji.paopao.service.BookingTimeService;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.util.ServiceResultUtils;

/**
 * Author 金威正
 * Date  2020-02-27
 */
@RestController
@RequestMapping(value = "/v1/bookingTime")
public class BookingTimeController {
    @Autowired
    private BookingTimeService bookingTimeService;

    //客户端 获取预约时间
    @GetMapping(value = {"/list/{consultantId}/{date}"})
    public Object list(
    		@PathVariable("consultantId") int consultantId,
    		@PathVariable("date") String date) {
        Map<String, Object> map = bookingTimeService.listWorkBookingTime(consultantId, date);
        return ServiceResultUtils.success(map);
    }

    
    //咨询师端 获取预约时间
    @GetMapping(value = {"/list/{date}"})
    public Object list(@PathVariable("date") String date,HttpServletRequest request) {
		String token = request.getHeader("authorization").replace("Bearer ", ""); 
		Integer userId=JwtUtils.getUserId(token);
        Map<String, Object> map = bookingTimeService.listConsultantWorkBookingTime(userId, date);
        return ServiceResultUtils.success(map);
    }

    
}
