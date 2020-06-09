package com.kuangji.paopao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.dto.param.SupervisorOrderParam;
import com.kuangji.paopao.model.ConsultantSupervisorOrder;
import com.kuangji.paopao.vo.ConsultantSupervisorOrderVO;
import com.kuangji.paopao.vo.SupervisorOrderDetailsVO;
import com.kuangji.paopao.vo.SupervisorOrderVO;


/**
* Created by Mybatis Generator 2020/03/24
*/
public interface ConsultantSupervisorOrderMapper extends BaseMapper<ConsultantSupervisorOrder> {
	
	
	//督导订单确认
	int supervisorOrderConfirm(@Param("orderNo")String orderNo,@Param("supervisorId") Integer supervisorId);
	
	
	//督导完结订单
	int supervisorOrderEnd(@Param("orderNo")String orderNo,@Param("supervisorId") Integer supervisorId);
	
	
	//督导拒绝订单
	
	int supervisorOrderRefuse(@Param("orderNo")String orderNo,@Param("supervisorId") Integer supervisorId);
	
	//咨询师取消订单
	
	int supervisorOrderCancel(@Param("orderNo")String orderNo,@Param("consultantId") Integer consultantId);
	
	//我发起的督导订单 统计用于分页
	int countOrderByConsultantId(@Param("consultantId") Integer consultantId,@Param("types") Integer[] arr);
	
	List<SupervisorOrderVO> listOrderByConsultantId(Map<String, Object> map);
	
	//我接受的督导订单 数量 用于分页
	int countOrderBySupervisorId(@Param("supervisorId") Integer supervisorId,@Param("types") Integer[] arr);
	//我接受的督导订单
	List<SupervisorOrderVO> listOrderBySupervisorId(Map<String, Object> map);
	
	//创建时间的督导订单
	SupervisorOrderDetailsVO getSupervisorOrderDetailsVO(@Param("orderNo")String orderNo);
	
	
	SupervisorOrderDetailsVO getLaunchSupervisorOrderDetailsVO(@Param("orderNo")String orderNo);

	
	
	//我跟当前咨询师 已经完成的督导订单列表
	List<ConsultantSupervisorOrderVO> listOrder(@Param("consultantId")Integer consultantId,@Param("supervisorId") Integer supervisorId);
	
	//后台查询督导订单
	List<com.kuangji.paopao.admin.vo.SupervisorOrderVO> listSupervisorOrder(SupervisorOrderParam supervisorOrderParam);
	
	
}