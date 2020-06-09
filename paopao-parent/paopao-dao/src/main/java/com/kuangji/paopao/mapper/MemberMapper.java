package com.kuangji.paopao.mapper;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.model.Member;

/**
* Created by Mybatis Generator 2020/03/22
*/
public interface MemberMapper extends BaseMapper<Member> {
    Integer getChannelUserId(Integer userId);
}