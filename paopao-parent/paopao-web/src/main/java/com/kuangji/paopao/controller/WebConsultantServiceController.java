package com.kuangji.paopao.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.kuangji.paopao.admin.vo.ConsultantServiceVO;
import com.kuangji.paopao.dto.param.ConsultantModelServiceParam;
import com.kuangji.paopao.model.ConsultantModelService;
import com.kuangji.paopao.service.ConsultantServiceService;
import com.kuangji.paopao.service.UserService;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.util.ServiceResultUtils;

@RestController
@RequestMapping("/web/consultantService")
public class WebConsultantServiceController {
	
    @Autowired
    private ConsultantServiceService consultantServiceService;
  
    @Autowired
    private UserService userService;
    
    @PostMapping("/list")
    public Object listUser(ConsultantModelServiceParam param,HttpServletRequest request) {
    	String token = request.getHeader("authorization").replace("Bearer ", "");
    	Integer userId=JwtUtils.getUserId(token);
    	if (!userService.isAdmin(userId)) {
    		param.setUserIds(userService.userIds(userId));
		}
        List<ConsultantServiceVO> list=consultantServiceService.listConsultantService(param);
        
        return ServiceResultUtils.success(new PageInfo<>(list));
    }
   
    @PostMapping("/info")
    public Object listConsultantService(Integer shopId) {
   
        List<ConsultantModelService> list=consultantServiceService.listConsultantService(shopId);
        return ServiceResultUtils.success(list);
    }
    
    @PostMapping("/add")
    public Object add(Integer shopId) {

    	int result=consultantServiceService.addConsultantService(shopId);
        return ServiceResultUtils.success(result>0);
    }
    
    @PostMapping("/delete")
    public Object delete(Integer shopId) {
    	int result=consultantServiceService.deleteConsultantService(shopId);
        return ServiceResultUtils.success(result>0);
    }
}
