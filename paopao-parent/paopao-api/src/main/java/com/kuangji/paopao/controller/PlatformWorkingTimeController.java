package com.kuangji.paopao.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kuangji.paopao.service.PlatformWorkingTimeService;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.util.ServiceResultUtils;
import com.kuangji.paopao.vo.BookingTimeVO;

/**
 * Author 金威正
 * Date  2020-02-27
 */
@RestController
public class PlatformWorkingTimeController {
    @Autowired
    private PlatformWorkingTimeService platformWorkingTimeService;

    @GetMapping(value = {"/v1/platformWorkingTime/list"})
    public Object list(HttpServletRequest request) {
		String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer userId=JwtUtils.getUserId(token);
        List<BookingTimeVO> bookingTimeVOs =platformWorkingTimeService.listWorkingUserTime(userId);
        return ServiceResultUtils.success(bookingTimeVOs);
    }
   
}
