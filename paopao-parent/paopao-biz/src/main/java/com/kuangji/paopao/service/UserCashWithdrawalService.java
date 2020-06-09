package com.kuangji.paopao.service;

import java.util.List;

import com.kuangji.paopao.admin.vo.UserCashWithdrawalVO;
import com.kuangji.paopao.dto.param.UserCashWithdrawalParam;
import com.kuangji.paopao.model.UserCashWithdrawal;
import com.kuangji.paopao.service.base.BaseService;
import com.kuangji.paopao.vo.UserCardVO;

/**
 * Author 金威正 Date 2020-02-17
 */
public interface UserCashWithdrawalService extends BaseService<UserCashWithdrawal, Integer>{

	
	int  cashWithdrawal(Integer consultantId,Integer amount,String code);

	List<UserCashWithdrawal> pagelist(Integer consultantId,Integer pageNum);
	
	
	List<UserCardVO> cardList(Integer consultantId);
	
	
	List<UserCashWithdrawalVO> listUserCashWithdrawalVO(UserCashWithdrawalParam userCashWithdrawalParam);
	
	int updateCashType(Integer id,Integer userId,Integer cashType);
}
