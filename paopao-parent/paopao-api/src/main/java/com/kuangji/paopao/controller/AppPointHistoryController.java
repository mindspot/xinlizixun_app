package com.kuangji.paopao.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.kuangji.paopao.dto.param.PointHistoryParam;
import com.kuangji.paopao.service.PointHistoryService;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.util.ServiceResultUtils;
import com.kuangji.paopao.vo.PointHistoryVO;

@RestController
@RequestMapping("/v1/point-history")
public class AppPointHistoryController {
    @Autowired
    private PointHistoryService pointHistoryService;
    @GetMapping("/list")
    public Object listPointHistoryResult( HttpServletRequest request,PointHistoryParam pointHistoryParam) {
		String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer userId=JwtUtils.getUserId(token);
		pointHistoryParam.setUserId(userId);
		
		if (pointHistoryParam.getType()==1) {
			 List<PointHistoryVO> list=pointHistoryService.listWorkPointHistoryVO(pointHistoryParam);
		     return ServiceResultUtils.success(new PageInfo<>(list));
		}
        List<PointHistoryVO> list=pointHistoryService.listPointHistoryVO(pointHistoryParam);
        return ServiceResultUtils.success(new PageInfo<>(list));
    }
   
}
