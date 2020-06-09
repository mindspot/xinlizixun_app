package com.kuangji.paopao.service;

import java.util.List;

import com.kuangji.paopao.base.Pager;
import com.kuangji.paopao.dto.SupervisorOrderDTO;
import com.kuangji.paopao.dto.param.SupervisorOrderParam;
import com.kuangji.paopao.model.ConsultantSupervisorOrder;
import com.kuangji.paopao.service.base.BaseService;
import com.kuangji.paopao.vo.ConsultantSupervisorOrderVO;
import com.kuangji.paopao.vo.SupervisorOrderDetailsInfoVO;
import com.kuangji.paopao.vo.SupervisorOrderVO;

/**
 * Created by Mybatis Generator 2020/03/24
 */
public interface ConsultantSupervisorOrderService extends BaseService<ConsultantSupervisorOrder, Integer> {

	// 下督导订单
	int insertSupervisorOrder(SupervisorOrderDTO supervisorOrderDTO, String token);

	// 督导订单订单确认
	int supervisorOrderConfirm(String orderNo, Integer supervisorId);

	// 督导订单订单确认
	int supervisorOrderEnd(String orderNo, Integer supervisorId);

	// 督导拒绝订单
	int supervisorOrderRefuse(String orderNo, Integer supervisorId);

	// 咨询师 取消订单
	int supervisorOrderCancel(String orderNo, Integer consultantId);

	// 我发起的督导师订单
	Pager<List<SupervisorOrderVO>> listOrderByConsultantId(Integer consultantId, Integer pageNum, Integer type);

	// 我接受督导师订单
	Pager<List<SupervisorOrderVO>> listOrderBysupervisorId(Integer supervisorId, Integer pageNum, Integer type);

	// 根据咨询师 跟督导师 查出之间的订单 并且是完结状态
	List<ConsultantSupervisorOrderVO> listOrder(String token, Integer supervisorId);

	// 咨询师订单状态
	SupervisorOrderDetailsInfoVO getLaunchSupervisorOrderDetailsVO(String orderNo);

	SupervisorOrderDetailsInfoVO getSupervisorOrderDetailsVO(String orderNo);

	// 后台查看督导订单
	List<com.kuangji.paopao.admin.vo.SupervisorOrderVO> listSupervisorOrder(SupervisorOrderParam supervisorOrderParam);

	// 我发起的督导订单消息未读数量
	int unReadOrderNumByConsultantId(Integer userId);

	// 我收到的督导订单消息未读数量
	int unReadOrderNumBySupervisorId(Integer userId);

	// 修改发起的督导订单为已读状态
	int updateReadByConsultantId(Integer userId);

	// 修改收到的督导订单为已读状态
	int updateReadBySupervisorId(Integer userId);
	
	//修改 督导订单详情 改为已读
	int updateRead(String orderNo);

	
	
	
}