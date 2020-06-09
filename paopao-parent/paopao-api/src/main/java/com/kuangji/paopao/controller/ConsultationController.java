package com.kuangji.paopao.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kuangji.paopao.api.ConsultationApi;
import com.kuangji.paopao.base.Pager;
import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.dto.ConsultationDTO;
import com.kuangji.paopao.enums.CommonEnum;
import com.kuangji.paopao.model.Visitor;
import com.kuangji.paopao.schedule.AsyncTaskJob;
import com.kuangji.paopao.service.SettledInService;
import com.kuangji.paopao.service.UserService;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.util.ServiceResultUtils;
import com.kuangji.paopao.vo.ConsultantVO;

/**
 * Author 金威正
 * Date  2020-02-16
 * 咨询页面
 */
@RestController
public class ConsultationController {
    @Autowired
    private ConsultationApi consultationApi;
    @Autowired
    private UserService userService;

  
    private SettledInService settledInService;
    
    @Autowired
    private  AsyncTaskJob asyncTaskJob;
    //客户端 咨询师列表
    @PostMapping("/v1/consultation/list")
    public Object list(@RequestBody  ConsultationDTO consultationDTO) {
    	int pageNum=consultationDTO.getPageNum();
    	if (pageNum<=0) {
            return ServiceResultUtils.failure("-1", "跳转页码最小为1");

		}
		Pager<List<ConsultantVO>> pageInfo=userService.pageListConsultant(pageNum,consultationDTO);
	
        return ServiceResultUtils.success(pageInfo);
    }
    
    
    
    //获取入驻平标签
    @GetMapping("/consultation/label")
    public Object label(HttpServletRequest request) {
		String token = request.getHeader("authorization").replace("Bearer ", "");
    
    	Map<String, Object> labels=consultationApi.listConsultationLabel(token);
		
        return ServiceResultUtils.success(labels);
    }
    
    
    //获取入驻平台咨询师详细信息
    @GetMapping("/v1/consultation/get/{id}")
    public Object get(@PathVariable("id") int id,HttpServletRequest request) {
		String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer userId=JwtUtils.getUserId(token);
		
		//异步
		Visitor visitor=new Visitor();
    	visitor.setShopId(id);
    	visitor.setUserId(userId);
    	asyncTaskJob.insertVisitor(visitor);
    	
    	Map<String, Object> aboutConsultantVOs= consultationApi.getConsultation(userId,id,CommonEnum.SERVICE_INSTRUCTIONS.getVal());
        return ServiceResultUtils.success(aboutConsultantVOs);
    }

    @PostMapping(value = "/v1/settledInService/deleteUserImg")
    public Object deleteUserImg(Integer id,HttpServletRequest request) {
    	if (id==null) {
    	    return ServiceResultUtils.failure(ResultCodeEnum.FAIL);
		}
    	String token = request.getHeader("authorization").replace("Bearer ", "");
    	Integer userId=JwtUtils.getUserId(token);
		settledInService.deleteUserImg(id, userId);
        return ServiceResultUtils.success(null);
        
    }

}
