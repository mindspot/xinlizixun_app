package com.kuangji.paopao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kuangji.paopao.api.IndexApi;
import com.kuangji.paopao.util.ServiceResultUtils;

/**
 * Author 金威正
 * Date  2020-02-16
 */
@RestController
public class IndexController {
    @Autowired
    private IndexApi indexApi;
    
 

    //获取入驻平台的标签
    @GetMapping("/v1/index")
    public Object listLabel() {
          
        return ServiceResultUtils.success(indexApi.index());
    }

    

    
}
