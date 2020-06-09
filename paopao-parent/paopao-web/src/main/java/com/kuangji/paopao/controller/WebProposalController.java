package com.kuangji.paopao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.kuangji.paopao.admin.vo.ProposalVO;
import com.kuangji.paopao.dto.param.ProposalParam;
import com.kuangji.paopao.service.ProposalService;
import com.kuangji.paopao.util.ServiceResultUtils;

/**
 * Author 金威正
 * Date  2020-03-12
 */
@RestController
public class WebProposalController {
	
    @Autowired
    private ProposalService proposalService;


    @PostMapping(value = "/web/proposal/list")
    public Object list(@RequestBody ProposalParam param)  {
    	List<ProposalVO> list=proposalService.listProposal(param);
		return ServiceResultUtils.success(new PageInfo<>(list)) ;
    }
    
    @PostMapping(value = "/web/proposal/delete")
    public Object delete(Integer id)  {
    	int result=proposalService.deleteById(id);
		return ServiceResultUtils.success(result>0) ;
    }
}
