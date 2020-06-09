package com.kuangji.paopao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.mapper.UserImageMapper;
import com.kuangji.paopao.model.UserImage;
import com.kuangji.paopao.service.UserImageService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;

/**
 * Author 金威正
 * Date  2020-02-20
 */
@Service
public class UserImageServiceImpl  extends BaseServiceImpl<UserImage, Integer> implements  UserImageService {
    @Autowired
    private UserImageMapper userImageMapper;

	@Override
	public BaseMapper<UserImage> getMapper() {
		// TODO Auto-generated method stub
		return userImageMapper;
	}
    
}
   
