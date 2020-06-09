package com.kuangji.paopao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.mapper.MemberCaseMapper;
import com.kuangji.paopao.model.MemberCase;
import com.kuangji.paopao.service.MemberCaseService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;
import com.kuangji.paopao.vo.MemberCaseVO;

/**
 * Author 金威正
 * Date  2020-02-23
 */
@Service
public  class MemberCaseServiceImpl  extends BaseServiceImpl<MemberCase, Integer> implements   MemberCaseService{
    @Autowired
    private MemberCaseMapper memberCaseMapper;

	@Override
	public MemberCase getLastMemberCase(Integer userId) {
		// TODO Auto-generated method stub
		return memberCaseMapper.getLastMemberCase(userId);
	}

	@Override
	public BaseMapper<MemberCase> getMapper() {
		// TODO Auto-generated method stub
		return memberCaseMapper;
	}

	

	@Override
	public List<MemberCaseVO> listMemberCaseVO(Integer mermberId) {
		// TODO Auto-generated method stub
		return memberCaseMapper.listMemberCaseVO(mermberId);
	}}
