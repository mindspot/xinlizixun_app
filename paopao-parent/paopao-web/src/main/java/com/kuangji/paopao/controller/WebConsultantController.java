package com.kuangji.paopao.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.dto.UpdateImgeDTO;
import com.kuangji.paopao.dto.param.ConsultantParam;
import com.kuangji.paopao.dto.result.ConsultantInfo;
import com.kuangji.paopao.model.consultant.Consultant;
import com.kuangji.paopao.service.ConsultantService;
import com.kuangji.paopao.service.UserService;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.util.ServiceResultUtils;
import com.kuangji.paopao.vo.consulant.ConsultantResult;

/**
 * Author 金威正 Date 2020-02-17
 */
@RestController
public class WebConsultantController {

    @Autowired
    private ConsultantService consultantService;

    @Autowired
    private UserService userService;
    
    //获取咨询师
    @PostMapping(value = "/web/consultant/list")
    public Object consultationAdminList(@RequestBody ConsultantParam param,HttpServletRequest request) {
    	String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer userId=JwtUtils.getUserId(token);
    	if (!userService.isAdmin(userId)) {
    		param.setUserIds(userService.userIds(userId));
		}
        List<ConsultantResult> users = consultantService.listConsultantVO(param);
        return ServiceResultUtils.success(new PageInfo<>(users));
    }
    //通过
    @PostMapping(value = "/web/consultant/update-result")
    public Object updateStatus(@RequestBody ConsultantResult consultantResult) {
        int result = consultantService.updateConsultantRate(consultantResult);
        return ServiceResultUtils.success(result > 0);
    }
    //通过
    @PostMapping(value = "/web/consultant/updateStatus")
    public Object updateStatus(@RequestParam(defaultValue = "0") Integer id, Integer status) {

        int result = consultantService.consultantUpdateStatus(id);
        return ServiceResultUtils.success(result > 0);
    }

    //指派分组
    @PostMapping(value = "/web/consultant/update")
    public Object consultantUpdate(@RequestBody Consultant consultant,HttpServletRequest request) {
    	String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer userId=JwtUtils.getUserId(token);
    	if (!userService.isAdmin(userId)) {
    		throw new ServiceException(-1, "权限不足");
		}
        int result = consultantService.updateByPrimaryKeySelective(consultant);
        return ServiceResultUtils.success(result > 0);
    }

    //咨询师详情
    @PostMapping(value = "/web/consultant/consultantInfo")
    public Object consultantInfo(@RequestParam(defaultValue = "0") Integer id) {
        ConsultantInfo consultant = consultantService.getConsultantInfo(id);
        return ServiceResultUtils.success(consultant);
    }

    //获取咨询师
    @PostMapping(value = "/web/consultant/list-result")
    public Object consultantResultList(@RequestBody ConsultantParam consultantParam) {
        List<ConsultantResult> consultantResults = consultantService.listConsultantResult(consultantParam);
        return ServiceResultUtils.success(consultantResults);
    }

    //获取咨询师
    @PostMapping(value = "/web/supervisor/change")
    public Object supervisorChange(String consultantIds,String invitationCode  ) {
        int result = consultantService.changeSupervisorList(consultantIds,invitationCode);
        return ServiceResultUtils.success(result>0);
    }
    @PostMapping(value = "/web/supervisor/code")
    public Object supervisorCodeList() {
        List<String> result = consultantService.listSupervisorCode();
        return ServiceResultUtils.success(result);
    }
    //通过
    @PostMapping(value = "/web/supervisor/set")
    public Object updateSupervisor(Integer id,String invitationCode,HttpServletRequest request) {
    	String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer userId=JwtUtils.getUserId(token);
    	if (!userService.isAdmin(userId)) {
    		throw new ServiceException(-1, "权限不足");
		}
        int result = consultantService.updateSupervisor(id,invitationCode);
        return ServiceResultUtils.success(result > 0);
    }

    //取消督导
    @PostMapping(value = "/web/supervisor/cancel")
    public Object updateCancelSupervisor(Integer id) {
        int result = consultantService.updateCancelSupervisor(id);
        return ServiceResultUtils.success(result > 0);
    }
    
    
  //取消督导
    @PostMapping(value = "/web/consultant/titel")
    public Object consultantTitel(@RequestBody Consultant consultant) {
    	Consultant c=new Consultant();
    	c.setId(consultant.getId());
    	c.setFaceAddress(consultant.getFaceAddress());
    	c.setTitle(consultant.getTitle());
        int result = consultantService.updateByPrimaryKeySelective(c);
        return ServiceResultUtils.success(result > 0);
    }
    
    //修改图图片
    @PostMapping(value = "/web/consultant/updateImage")
    public Object updateImage(@RequestBody UpdateImgeDTO dto) {
    	
        int result = consultantService.updateImage(dto);
        return ServiceResultUtils.success(result > 0);
    }
    
    //删除图片
    @PostMapping(value = "/web/consultant/delImage")
    public Object delImage(@RequestBody UpdateImgeDTO dto,HttpServletRequest request) {
    	String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer userId=JwtUtils.getUserId(token);
    	if (!userService.isAdmin(userId)) {
    		throw new ServiceException(-1, "权限不足");
		}
        int result = consultantService.delImage(dto);
        return ServiceResultUtils.success(result > 0);
    }
}
