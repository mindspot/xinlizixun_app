package com.kuangji.paopao.service;

import java.util.List;

import com.kuangji.paopao.dto.UpdateImgeDTO;
import com.kuangji.paopao.dto.param.ConsultantParam;
import com.kuangji.paopao.dto.result.ConsultantInfo;
import com.kuangji.paopao.model.consultant.Consultant;
import com.kuangji.paopao.model.consultant.ConsultantRate;
import com.kuangji.paopao.service.base.BaseService;
import com.kuangji.paopao.vo.ConsultantUpdateVO;
import com.kuangji.paopao.vo.consulant.ConsultantResult;

/**
 * Author 金威正 Date 2020-02-17
 */
public interface ConsultantService extends BaseService<Consultant, Integer>{

	ConsultantUpdateVO getConsultantUpdateVO(Integer consultantId);
	
	
	int updateConsultantVO(Integer consultantId,ConsultantUpdateVO consultantUpdateVO);
	
	
	int consultantUpdateStatus (Integer id);
	
	int updateSupervisor(Integer id,String invitationCode);
	
	int updateCancelSupervisor(Integer id);
	
	int update(Integer id,String invitationCode,Integer sort);

	ConsultantInfo getConsultantInfo(Integer consultantId);

    List<ConsultantResult> listConsultantResult(ConsultantParam consultantParam);

    int changeSupervisorList(String consultantIds, String invitationCode);

	List<ConsultantResult> listConsultantVO(ConsultantParam consultantParam);

    int updateConsultantRate(ConsultantResult consultantResult);

	ConsultantRate getConsultantRate(Integer consultantUserId, Integer type);

    List<String> listSupervisorCode();

    int  updateImage(UpdateImgeDTO dto);
    
    int  delImage(UpdateImgeDTO dto);
}
