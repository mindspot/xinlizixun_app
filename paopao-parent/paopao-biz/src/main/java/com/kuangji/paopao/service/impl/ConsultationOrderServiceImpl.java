package com.kuangji.paopao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.mapper.ConsultationOrderMapper;
import com.kuangji.paopao.mapper.ConsultationSetMealOrderMapper;
import com.kuangji.paopao.model.ConsultationOrder;
import com.kuangji.paopao.service.ConsultationOrderService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.vo.SetMealVO;

/**
 * Author 金威正
 * Date  2020-02-21
 */
@Service
public class ConsultationOrderServiceImpl  extends BaseServiceImpl<ConsultationOrder, Integer> implements ConsultationOrderService  {
    @Autowired
    private ConsultationOrderMapper consultationOrderMapper;
    
    
    @Autowired
    private ConsultationSetMealOrderMapper consultationSetMealOrderMapper;
    
  

	@Override
	public List<SetMealVO> listSetMeal(String token) {
		// TODO Auto-generated method stub
		int userId=JwtUtils.getUserId(token);
		return consultationSetMealOrderMapper.listSetMeal(userId);
	}


	@Override
	public BaseMapper<ConsultationOrder> getMapper() {
		// TODO Auto-generated method stub
		return consultationOrderMapper;
	}

}
