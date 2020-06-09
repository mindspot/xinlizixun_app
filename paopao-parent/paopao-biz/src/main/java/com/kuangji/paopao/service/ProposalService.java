package com.kuangji.paopao.service;

import java.util.List;

import com.kuangji.paopao.admin.vo.ProposalVO;
import com.kuangji.paopao.dto.param.ProposalParam;
import com.kuangji.paopao.model.Proposal;
import com.kuangji.paopao.service.base.BaseService;

/**
 * Author 金威正
 * Date  2020-03-12
 */
public interface ProposalService extends BaseService<Proposal, Integer> {

	 
	int insert(String token,Proposal proposal) ;
	 
	 
	List<ProposalVO> listProposal(ProposalParam param);
}
