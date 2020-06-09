package com.kuangji.paopao.service;

import com.kuangji.paopao.model.Member;
import com.kuangji.paopao.service.base.BaseService;

public interface MemaberService extends BaseService<Member, Integer> {

	//后台增加会员
	int addMemaber(String userName);
}
