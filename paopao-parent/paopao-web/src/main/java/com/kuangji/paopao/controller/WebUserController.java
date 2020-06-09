package com.kuangji.paopao.controller;

import com.github.pagehelper.PageInfo;
import com.kuangji.paopao.dto.param.UserParam;
import com.kuangji.paopao.model.User;
import com.kuangji.paopao.service.UserService;
import com.kuangji.paopao.util.ServiceResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/web/user")
public class WebUserController {
    @Autowired
    private UserService userService;
    //获取咨询师
    @PostMapping("/list")
    public Object listUser(@RequestBody UserParam userParam) {
        List<User> users=userService.listUser(userParam);
        return ServiceResultUtils.success(new PageInfo<>(users));
    }
    @PostMapping("/update-status")
    public Object updateUserStatus(Integer id,Integer status) {
        Integer result=userService.updateUserStatus(id,status);
        return ServiceResultUtils.success(result>0);
    }
}
