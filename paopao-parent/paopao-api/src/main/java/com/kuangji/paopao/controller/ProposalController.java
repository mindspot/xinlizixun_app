package com.kuangji.paopao.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kuangji.paopao.model.Proposal;
import com.kuangji.paopao.service.ProposalService;
import com.kuangji.paopao.util.ServiceResultUtils;

/**
 * Author 金威正
 * Date  2020-03-12
 */
@RestController
@RequestMapping(value = "/v1/proposal")
public class ProposalController {
    @Autowired
    private ProposalService proposalService;


    @PostMapping(value = "/insert")
    public Object insert(HttpServletRequest request,Proposal proposal)  {
		String token = request.getHeader("authorization").replace("Bearer ", "");
        if (proposalService.insert(token,proposal) > 0) {
            return ServiceResultUtils.success(null);
        } else {
            return ServiceResultUtils.failure("-1");
        }
    }

  
  

}
