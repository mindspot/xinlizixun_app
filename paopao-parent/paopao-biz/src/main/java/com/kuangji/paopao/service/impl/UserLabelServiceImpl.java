package com.kuangji.paopao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.mapper.UserLabelMapper;
import com.kuangji.paopao.model.UserLabel;
import com.kuangji.paopao.service.UserLabelService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;

/**
 * Author 金威正
 * Date  2020-02-18
 */
@Service
public class UserLabelServiceImpl extends BaseServiceImpl<UserLabel, Integer> implements  UserLabelService {
    @Autowired
    private UserLabelMapper userLabelMapper;

	@Override
	public BaseMapper<UserLabel> getMapper() {
		// TODO Auto-generated method stub
		return userLabelMapper;
	}
    
   
}
