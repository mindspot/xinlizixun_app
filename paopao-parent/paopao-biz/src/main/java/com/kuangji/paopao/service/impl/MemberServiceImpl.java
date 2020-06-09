package com.kuangji.paopao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.enums.LoginEnum;
import com.kuangji.paopao.enums.UserTypeEnum;
import com.kuangji.paopao.mapper.MemberMapper;
import com.kuangji.paopao.mapper.UserMapper;
import com.kuangji.paopao.model.Member;
import com.kuangji.paopao.model.User;
import com.kuangji.paopao.redis.util.RedisUtils;
import com.kuangji.paopao.service.MemaberService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;

import tk.mybatis.mapper.entity.Example;

@Service
public class MemberServiceImpl extends BaseServiceImpl<Member, Integer> implements   MemaberService{

	@Autowired
	private  MemberMapper memberMapper;
	
	@Autowired
	private  UserMapper userMapper;
	
	@Autowired
	private  RedisUtils redisUtils;
	
	private  static final String  paoPao="泡泡";
	
	@Override
	public BaseMapper<Member> getMapper() {
		// TODO Auto-generated method stub
		return memberMapper;
	}
	@Transactional(rollbackFor=Exception.class)
	@Override
	public int addMemaber(String userName) {

		Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userName", userName);
        criteria.andEqualTo("type", UserTypeEnum.MEMBER.getType());
        User user = userMapper.selectOneByExample(example);
        if (user==null) {
			user=new User();
        	user.setUserName(userName);
        	user.setRealName(paoPao+redisUtils.generateId());
        	user.setStatus(LoginEnum.LOGIN_NORMAL.getIndex());
        	user.setType(UserTypeEnum.MEMBER.getType());
    		user.setAccount(0);
    		userMapper.insertSelective(user);
    		Member member =new Member();
    		member.setUserId(user.getId());
    		return memberMapper.insertSelective(member);  		
		}
		return 0;
	}

}
