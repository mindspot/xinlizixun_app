package com.kuangji.paopao.api;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.kuangji.paopao.vo.ConsultantMallTradeOrderVO;
import com.kuangji.paopao.vo.ConsultantWorkMemberMallTradeOrderVO;
import com.kuangji.paopao.vo.MallTradeOrderVO;

public interface  MallTradeOrderApi {


	PageInfo<MallTradeOrderVO> pageListConsultantMallTradeOrder(String token,int  pageNum);

	
	PageInfo<ConsultantMallTradeOrderVO> pageListConsultantMallTradeOrderWork(String token,int  pageNum);
	
	
	PageInfo <ConsultantMallTradeOrderVO> listConsultantMallTradeOrderWorkEnd(String token,int pageNum);

	Map<String, Object> getConsultantMallTradeOrderWork(String token, String orderNo);
	
	
	List<Map<String, Object>>  listConsultantMallTradeOrderWork(String token,Integer memberId);

	//咨询师端 客户订单
	PageInfo<ConsultantWorkMemberMallTradeOrderVO> pageListConsultantWorkMemberMallTradeOrder(String token,int pageNum);

}
