package com.kuangji.paopao.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kuangji.paopao.enums.CommonEnum;
import com.kuangji.paopao.model.Common;
import com.kuangji.paopao.service.CommonService;
import com.kuangji.paopao.util.ServiceResultUtils;
import com.kuangji.paopao.vo.UrlVO;

/**
 * Author 金威正
 * Date  2020-02-16
 */
@RestController
public class CommonController {
    @Autowired
    private CommonService commonService;
    
    //获取入驻平台的标签
    @GetMapping("/v1/common/listLabel")
    public Object listLabel(HttpServletRequest request) {
    	String token = request.getHeader("authorization").replace("Bearer ", "");
    	Map<String, Object> object=commonService.listIndexCommon(token);
    	
        return ServiceResultUtils.success(object);
    }
    
    
    //客服电话列表
    @GetMapping("/common/listCustomeService")
    public Object customeService() {
        	
    	return ServiceResultUtils.success(commonService.getCustomeServiceVO());
    }
    
 	//公司介绍
    @GetMapping("/v1/common/companyIntroduction")
    public Object companyIntroduction() {
        	
    	Common common=commonService.getCommonByType(CommonEnum.COMPANY_INTRODUCTION.getVal());
    
    	return ServiceResultUtils.success(common);
    }
    
	//service_url
    @GetMapping("/common/serviceUrl")
    public Object serviceUrl() {
        
    	List<Integer> types =new ArrayList<Integer>();
    	types.add(CommonEnum.SET_IN_SERVICE_URL.getVal());
    	types.add(CommonEnum.SERVICE_SETTINGS_URL.getVal());
    	List<Common> commons=commonService.listCommon(types);
    	UrlVO urlVO =new UrlVO();
    	Common setInUrl =commons.stream().filter(c->c.getType()==CommonEnum.SET_IN_SERVICE_URL.getVal()).findFirst().get();
    	urlVO.setSetInUrl(setInUrl);
    	Common  serviceSetInUrl=commons.stream().filter(c->c.getType()==CommonEnum.SERVICE_SETTINGS_URL.getVal()).findFirst().get();
    	urlVO.setServiceSetInUrl(serviceSetInUrl);
    	return ServiceResultUtils.success(urlVO);
    }
    
}
