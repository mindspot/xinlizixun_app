package com.kuangji.paopao.mapper;

import java.util.List;

import com.kuangji.paopao.admin.vo.ProposalVO;
import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.dto.param.ProposalParam;
import com.kuangji.paopao.model.Proposal;

/**
* Created by Mybatis Generator 2020/03/14
*/
public interface ProposalMapper extends BaseMapper<Proposal> {
	
	List<ProposalVO> listProposal(ProposalParam param);
}