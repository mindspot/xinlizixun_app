package com.kuangji.paopao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.kuangji.paopao.admin.vo.UserCashWithdrawalVO;
import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.dto.param.UserCashWithdrawalParam;
import com.kuangji.paopao.enums.UserCashWithdrawalEnum;
import com.kuangji.paopao.service.UserCashWithdrawalService;
import com.kuangji.paopao.util.ServiceResultUtils;

/**
 * Author 金威正
 * Date  2020-02-16
 */
@RestController
public class WebUserCashWithdrawalController {
    @Autowired
    private UserCashWithdrawalService userCashWithdrawalService;
    //后台
    @PostMapping("/web/userCashWithdrawal/list")
    public Object listBanner(@RequestBody UserCashWithdrawalParam userCashWithdrawalParam) {
    	
    	List<UserCashWithdrawalVO> list=userCashWithdrawalService.listUserCashWithdrawalVO(userCashWithdrawalParam);
        return ServiceResultUtils.success(new PageInfo<>(list));
    }
    
    
    
    //后台
    @PostMapping("/web/userCashWithdrawal/updateCashType")
    public Object updateCashType(Integer id,Integer userId,Integer cashType) {
    	if (id==null||userId==null||cashType==null) {
			throw new ServiceException(ResultCodeEnum.ILLGEAL_PARAMTER);
		}
    	if (!UserCashWithdrawalEnum.isInclude(cashType)) {
    		throw new ServiceException(ResultCodeEnum.ILLGEAL_PARAMTER);
		}
    	if (cashType==UserCashWithdrawalEnum.TO_EXAMINE.getIndex()) {
    		  return ServiceResultUtils.failure(null);
		}
    	int result=userCashWithdrawalService.updateCashType(id, userId, cashType);
        return ServiceResultUtils.success(result>0);
    }
}
