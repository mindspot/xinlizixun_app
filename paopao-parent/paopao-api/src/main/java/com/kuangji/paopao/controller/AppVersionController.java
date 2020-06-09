package com.kuangji.paopao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kuangji.paopao.model.AppVersion;
import com.kuangji.paopao.service.AppVersionService;
import com.kuangji.paopao.util.ServiceResultUtils;

/**
 * Author 金威正
 * Date  2020-02-17
 */
@RestController
public class AppVersionController {
    @Autowired
    private AppVersionService appVersionService;
   
    //获取app版本
    @GetMapping(value = {"/appVersion"})
    public Object list(String bundleId) {
  
    	AppVersion appVersion=appVersionService.getNewestApp(bundleId);
        return ServiceResultUtils.success(appVersion);
    }
    
   
}
