package com.kuangji.paopao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.model.User;
import com.kuangji.paopao.service.MemaberService;
import com.kuangji.paopao.util.ServiceResultUtils;
import com.kuangji.paopao.util.VerificationUtils;

/**
 * Author 金威正 Date 2020-02-17
 */
@RestController
public class WebMemberController {
	@Autowired
	private MemaberService memaberService;

	//通过
	@PostMapping(value = "/web/member/add")
	public Object addMember(@RequestBody User user) {
		String userName=user.getUserName();
		boolean bm = VerificationUtils.isMobilePhone(userName);
		if (!bm) {
            throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_PHONE);
      	}
		int result=memaberService.addMemaber(userName);
		return ServiceResultUtils.success(result>0);
	}
	 
   
}
