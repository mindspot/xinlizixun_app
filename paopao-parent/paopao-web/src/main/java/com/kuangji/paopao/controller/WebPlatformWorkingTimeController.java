package com.kuangji.paopao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.kuangji.paopao.dto.param.PlatformWorkingTimeParam;
import com.kuangji.paopao.model.PlatformWorkingTime;
import com.kuangji.paopao.service.PlatformWorkingTimeService;
import com.kuangji.paopao.util.ServiceResultUtils;

/**
 * Author 金威正
 * Date  2020-02-27
 */
@RestController
public class WebPlatformWorkingTimeController {
    @Autowired
    private PlatformWorkingTimeService platformWorkingTimeService;


    @PostMapping(value = {"/web/platformWorkingTime/list"})
    public Object list(@RequestBody PlatformWorkingTimeParam param) {
 
        List<PlatformWorkingTime> list =platformWorkingTimeService.listWorking(param);
 
        return ServiceResultUtils.success(new PageInfo<PlatformWorkingTime>(list));
    }

    
    @PostMapping(value = {"/web/platformWorkingTime/update"})
    public Object list(@RequestBody PlatformWorkingTime platformWorkingTime) {
 
        int result =platformWorkingTimeService.updateByPrimaryKeySelective(platformWorkingTime);
 
        return ServiceResultUtils.success(result>0);
    }
}
