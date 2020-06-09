package com.kuangji.paopao.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kuangji.paopao.base.Pager;
import com.kuangji.paopao.service.UserService;
import com.kuangji.paopao.util.ServiceResultUtils;
import com.kuangji.paopao.vo.ConsultantVO;

/**
 * Author 金威正
 * Date  2020-02-16
 * 咨询搜索页面接口
 */
@RestController
@RequestMapping(value = "/v1/consultation")
public class SearchController {
 
    @Autowired
    private UserService userService;

 
    //搜索咨询师接口
    @GetMapping("/s")
    public Object listWd(@RequestParam Integer pageNum,@RequestParam String wd,HttpServletRequest request) {
  
    	if (pageNum<=0) {
            return ServiceResultUtils.failure("-1", "跳转页码最小为1");

		}
		String token = request.getHeader("authorization");

		Pager<List<ConsultantVO>> pageInfo=userService.pageListConsultantWd(pageNum,wd,token);

		
        return ServiceResultUtils.success(pageInfo);
    }
    
   
}
