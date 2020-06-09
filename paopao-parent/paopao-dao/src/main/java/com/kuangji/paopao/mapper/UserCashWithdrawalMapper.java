package com.kuangji.paopao.mapper;

import java.util.List;

import com.kuangji.paopao.admin.vo.UserCashWithdrawalVO;
import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.dto.param.UserCashWithdrawalParam;
import com.kuangji.paopao.model.UserCashWithdrawal;

/**
* Created by Mybatis Generator 2020/03/25
*/
public interface UserCashWithdrawalMapper extends BaseMapper<UserCashWithdrawal> {
	
	
	List<UserCashWithdrawalVO> listUserCashWithdrawalVO(UserCashWithdrawalParam userCashWithdrawalParam);
}