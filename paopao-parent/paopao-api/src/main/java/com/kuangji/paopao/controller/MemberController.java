package com.kuangji.paopao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.kuangji.paopao.admin.vo.MemberVO;
import com.kuangji.paopao.dto.param.MemberParam;
import com.kuangji.paopao.service.UserService;
import com.kuangji.paopao.util.ServiceResultUtils;

@RestController
@RequestMapping(value = "/member")
public class MemberController {
    @Autowired
    private UserService userService;
    @PostMapping("/list")
    public Object listMember(@RequestBody MemberParam memberParam) {
        List<MemberVO> list=userService.listMemberVO(memberParam);
        return ServiceResultUtils.success(new PageInfo<>(list));
    }
    
    
}
