package com.kuangji.paopao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.dto.param.MallTradeOrderParam;
import com.kuangji.paopao.model.MallTradeOrder;
import com.kuangji.paopao.vo.ConsultantMallTradeOrderDetailsVO;
import com.kuangji.paopao.vo.ConsultantMallTradeOrderVO;
import com.kuangji.paopao.vo.ConsultantOrderDiagnosisVO;
import com.kuangji.paopao.vo.ConsultantWorkMemberMallTradeOrderVO;
import com.kuangji.paopao.vo.ConsultationWorkSupervisorMallTradeOrderVO;
import com.kuangji.paopao.vo.MQTradeOrderVO;
import com.kuangji.paopao.vo.MallTradeOrderVO;

/**
 * Created by Mybatis Generator 2020/03/14
 */
public interface MallTradeOrderMapper extends BaseMapper<MallTradeOrder> {

	/**
	 * 根据orderNo获取数据 Author 金威正 Date 2020-02-20
	 */
	MQTradeOrderVO getMallTradeOrderByOrderNo(String orderNo);

	/**
	 * 根据用户id 查询自己的咨询订单 Author 金威正 Date 2020-02-20
	 */
	List<MallTradeOrderVO> listConsultantMallTradeOrder(Integer userId);

	/**
	 * 咨询师端 正在进行 订单 Author 金威正 Date 2020-02-20
	 */

	List<ConsultantMallTradeOrderVO> listConsultantMallTradeOrderWork(Integer consultantId);

	/**
	 * 咨询师端 已结结束的订单 Author 金威正 Date 2020-02-20
	 */

	List<ConsultantMallTradeOrderVO> listConsultantMallTradeOrderEnd(Integer consultantId);

	/**
	 * 咨询师端 根据shipId 查询所有订单 Author 金威正 Date 2020-02-20
	 */
	List<ConsultantWorkMemberMallTradeOrderVO> listConsultantWorkMemberMallTradeOrder(Integer consultantId);

	/**
	 * 咨询师端 获取订单详情 Author 金威正 Date 2020-02-20
	 */
	ConsultantMallTradeOrderDetailsVO getConsultantMallTradeOrderDetailsVO(@Param("consultantId") Integer consultantId,
			@Param("orderNo") String orderNo);

	/**
	 * 咨询师端 获取多个订单详情 Author 金威正 Date 2020-02-20
	 */
	List<ConsultantMallTradeOrderDetailsVO> listConsultantMallTradeOrderDetailsVO(@Param("consultantId") Integer consultantId,@Param("memberId") Integer memberId);

	/**
	 * 订单号 支付成功 但是业务订单 没有成功 Author 金威正 Date 2020-02-20
	 */
	int updateMallTradeOrderWrong(@Param("orderNo") String orderNo);

	/**
	 * 套餐支付成功 Author 金威正 Date 2020-02-20
	 */
	int updateSetMealPaySuccess(@Param("orderNo") String orderNo);

	/**
	 * 根据orderNo获取数据 Author 金威正 Date 2020-02-20
	 */
	MQTradeOrderVO getMallTradeOrderByOrderNo(@Param("userId") Integer userId, @Param("shopId") Integer shopId);

	/**
	 * 根据用户id 把所有的订单消息改为已读 Author 金威正 Date 2020-02-21
	 */
	int updateOrderRead(@Param("consultantId") Integer consultantId);

	/**
	 * 根据用户id 咨询师 确认订单 Author 金威正 Date 2020-02-21
	 */
	int updateOrderConfirm(@Param("consultantId") Integer consultantId, @Param("orderNo") String orderNo);

	/**
	 * 根据用户id 咨询师 确认订单 Author 金威正
	 */
	List<ConsultantOrderDiagnosisVO> listConsultantMallTradeDiagnosis(@Param("orderNo") String orderNo,@Param("consultantId") Integer consultantId);
	
	
	/**
	 * 根据多个订单号查询
	 */
	List<ConsultationWorkSupervisorMallTradeOrderVO> listMallTradeOrderByOrderNos(@Param("orderNos") List<String> orderNos,@Param("shopId") Integer shopId);
	
	
	List<String>  listConsultantTypeByOrderNos(@Param("orderNos") List<String> orderNos);
	
	//没有支付的 直接修改为已经取消
	int updateOrderCancel(@Param("orderNo") String orderNo,@Param("ext")String ext);
	
	//支付成功订单
	int updateOrderPayStatus(MallTradeOrder mallTradeOrder);

	
	/**
	 * 根据orderNo获取数据 Author 金威正 Date 2020-02-20
	 */
	MallTradeOrder getMallTradeOrderByOrder (String orderNo);

	
	/**
	 * 咨询订单 Author 金威正 Date 2020-02-20
	 */
	List<com.kuangji.paopao.admin.vo.MallTradeOrderVO> listConsultantMallTradeOrderByGoodsClass(MallTradeOrderParam mallTradeOrderParam);
	
	com.kuangji.paopao.admin.vo.SetMealInfoVO getSetMealInfo(Integer id);
	
	com.kuangji.paopao.admin.vo.OrderInfoVO getOrderInfo(Integer id);
	
	
	int getCountOrderEndByUserId(Integer userId);
	
}