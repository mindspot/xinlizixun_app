package com.kuangji.paopao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.mapper.AppVersionMapper;
import com.kuangji.paopao.model.AppVersion;
import com.kuangji.paopao.service.AppVersionService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;

import tk.mybatis.mapper.entity.Example;

/**
 * Author 金威正
 * Date  2020-02-17
 */
@Service
public class AppVersionServiceImpl extends BaseServiceImpl<AppVersion, Integer> implements AppVersionService {
	@Autowired
	private AppVersionMapper appVersionMapper;
	
	public AppVersion getNewestApp(String bundleId) {
		Example example =new Example(AppVersion.class);
		example.createCriteria()
		.andEqualTo("bundleId", bundleId)
		.andEqualTo("isDelete", 0);
		AppVersion appVersion=appVersionMapper.selectOneByExample(example);
		return appVersion;
	}

	@Override
	public BaseMapper<AppVersion> getMapper() {
		
		return appVersionMapper;
	}
    
}
