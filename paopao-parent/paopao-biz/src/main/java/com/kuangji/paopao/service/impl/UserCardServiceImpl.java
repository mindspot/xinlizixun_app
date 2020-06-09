package com.kuangji.paopao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.mapper.UserCardMapper;
import com.kuangji.paopao.model.UserCard;
import com.kuangji.paopao.service.UserCardService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;

/**
 * Author 金威正
 * Date  2020-02-17
 */
@Service
public class UserCardServiceImpl extends BaseServiceImpl<UserCard, Integer> implements UserCardService {
    @Autowired
    private UserCardMapper userCardMapper;


	@Override
	public BaseMapper<UserCard> getMapper() {
		// TODO Auto-generated method stub
		return userCardMapper;
	}


}
