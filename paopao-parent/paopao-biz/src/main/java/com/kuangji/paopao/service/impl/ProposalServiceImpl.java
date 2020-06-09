package com.kuangji.paopao.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuangji.paopao.admin.vo.ProposalVO;
import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.dto.param.ProposalParam;
import com.kuangji.paopao.mapper.ProposalMapper;
import com.kuangji.paopao.model.Proposal;
import com.kuangji.paopao.service.ProposalService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;
import com.kuangji.paopao.util.JwtUtils;

/**
 * Author 金威正
 * Date  2020-03-12
 */
@Service
public class ProposalServiceImpl extends BaseServiceImpl<Proposal, Integer> implements   ProposalService {
    @Autowired
    private ProposalMapper proposalMapper;
    
    /**
	 * 插入
	 * Author 金威正
	 * Date  2019-12-12
	 */
    
    @Override
    public int insert(String token,Proposal proposal) {
    	if (StringUtils.isBlank(proposal.getContent())) {
			throw new ServiceException(ResultCodeEnum.FAIL);
		}
    	int userId =JwtUtils.getUserId(token);
    	proposal.setUserId(userId);
        return proposalMapper.insertSelective(proposal);
    }

	@Override
	public BaseMapper<Proposal> getMapper() {
		// TODO Auto-generated method stub
		return proposalMapper;
	}

	@Override
	public List<ProposalVO> listProposal(ProposalParam param) {
		
		return proposalMapper.listProposal(param);
	}
  
}
