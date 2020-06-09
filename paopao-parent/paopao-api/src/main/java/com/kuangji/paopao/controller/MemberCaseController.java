package com.kuangji.paopao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kuangji.paopao.model.MemberCase;
import com.kuangji.paopao.service.MemberCaseService;
import com.kuangji.paopao.util.ServiceResultUtils;

/**
 * Author 金威正
 * Date  2020-02-23
 */
@RestController
@RequestMapping(value = "/v1/userCase")
public class MemberCaseController {
    @Autowired
    private MemberCaseService userCaseService;

    

    @PostMapping(value = {"/get/{userId}"})
    public Object get(@PathVariable("userId") int userId) {
    
        MemberCase userCase = userCaseService.getLastMemberCase(userId);
        
        return ServiceResultUtils.success(userCase);
    }

    @PostMapping(value = "/insert")
    public Object insert(MemberCase userCase) {
        if (userCaseService.insert(userCase) > 0) {
            return ServiceResultUtils.success(null);
        } else {
            return ServiceResultUtils.failure("-1");
        }
    }

  
    @PostMapping(value = "/update")
    public Object update(MemberCase userCase) {
        if (userCaseService.updateByPrimaryKeySelective(userCase) > 0) {
          return ServiceResultUtils.success(null);
        } else {
            return ServiceResultUtils.failure("-1");
        }
    }

    
}
