package com.kuangji.paopao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kuangji.paopao.model.AppVersion;
import com.kuangji.paopao.service.AppVersionService;
import com.kuangji.paopao.util.ServiceResultUtils;

/**
 * Author 金威正
 * Date  2020-02-17
 */
@RestController
public class WebAppVersionController {
    @Autowired
    private AppVersionService appVersionService;
   
    //后台获取app版本信息
    @GetMapping(value = {"/web/appVersion/list"})
    public Object list() {
  
    	List<AppVersion> appVersions=appVersionService.findAll();
        
        return ServiceResultUtils.success(appVersions);
    }
    
    
    //app后台修改
    @PostMapping(value = {"/web/appVersion/update"})
    public Object update(@RequestBody AppVersion appVersion) {
  
    	int result=appVersionService.updateByPrimaryKeySelective(appVersion);
        
        return ServiceResultUtils.success(result>0);
    }
   
}
