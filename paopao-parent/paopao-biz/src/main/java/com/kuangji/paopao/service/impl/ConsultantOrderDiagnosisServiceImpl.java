package com.kuangji.paopao.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangji.paopao.admin.vo.OrderDiagnosisVO;
import com.kuangji.paopao.admin.vo.OrderDiagnosisVO.DiagnosisVO;
import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.dto.ConsultantOrderDiagnosisDTO;
import com.kuangji.paopao.mapper.ConsultantOrderDiagnosisMapper;
import com.kuangji.paopao.mapper.ConsultantSupervisorOrderDiagnosisUpdateMapper;
import com.kuangji.paopao.model.ConsultantOrderDiagnosis;
import com.kuangji.paopao.model.ConsultantSupervisorOrderDiagnosisUpdate;
import com.kuangji.paopao.service.ConsultantOrderDiagnosisService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;

@Service
public class ConsultantOrderDiagnosisServiceImpl  extends BaseServiceImpl<ConsultantOrderDiagnosis, Integer> implements ConsultantOrderDiagnosisService{

	@Autowired
	private ConsultantOrderDiagnosisMapper consultantOrderDiagnosisMapper;
	
	@Autowired
	private ConsultantSupervisorOrderDiagnosisUpdateMapper consultantSupervisorOrderDiagnosisUpdateMapper;
	
	@Override
	public BaseMapper<ConsultantOrderDiagnosis> getMapper() {
		// TODO Auto-generated method stub
		return consultantOrderDiagnosisMapper;
	}
	@Transactional(rollbackFor=Exception.class)
	@Override
	public void updateMemberCaseDiagnosis(ConsultantOrderDiagnosisDTO dto) {
		List<ConsultantOrderDiagnosis> list=dto.getList();
	
		if (list==null||list.isEmpty()) {

			throw new ServiceException(ResultCodeEnum.FAIL);
		}
		long counttxt=list.stream().filter(c->c.getContentType()==0).count();
		if (counttxt>0&&counttxt!=1) {
			throw new ServiceException(-1,"请填写文字");
		}
		
		long count=list.stream().filter(c->c.getContentType()==1).count();
		if (count>3) {
			throw new ServiceException(-1,"图片选择不能超过3");
		}
		String orderNo= dto.getOrderNo();
		
		consultantSupervisorOrderDiagnosisUpdateMapper.delete(new ConsultantSupervisorOrderDiagnosisUpdate(orderNo) );
		consultantSupervisorOrderDiagnosisUpdateMapper.insertSelective(new ConsultantSupervisorOrderDiagnosisUpdate(orderNo,new Date()));
		
		consultantOrderDiagnosisMapper.delete(new ConsultantOrderDiagnosis(orderNo));
		list.stream().forEach(c->c.setOrderNo(dto.getOrderNo()));
		consultantOrderDiagnosisMapper.insertBatch(list);
	}
	@Override
	public List<DiagnosisVO> getCaseDiagnosis(String orderNo) {
		
		OrderDiagnosisVO orderDiagnosisVO=consultantOrderDiagnosisMapper.getCaseDiagnosis(orderNo);
		if (orderDiagnosisVO!=null) {
			List<DiagnosisVO> diagnosisVOs=orderDiagnosisVO.getDiagnosisVOs();
			diagnosisVOs=diagnosisVOs.stream().filter(c->c.getContentType()==1).collect(Collectors.toList());
			return diagnosisVOs;
		}
		return null;
	}
	@Override
	public DiagnosisVO getCaseDiagnos(String orderNo) {
		OrderDiagnosisVO orderDiagnosisVO=consultantOrderDiagnosisMapper.getCaseDiagnosis(orderNo);
		if (orderDiagnosisVO!=null) {
			List<DiagnosisVO> diagnosisVOs=orderDiagnosisVO.getDiagnosisVOs();
			DiagnosisVO dVo=diagnosisVOs.stream().filter(c->c.getContentType()==0).findFirst().get();
			orderDiagnosisVO.setDiagnosisVO(dVo);
			return dVo;
		}
		return null;
	}

}
