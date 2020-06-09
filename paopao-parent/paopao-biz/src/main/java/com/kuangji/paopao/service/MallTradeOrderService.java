package com.kuangji.paopao.service;

import java.util.List;

import com.kuangji.paopao.admin.vo.SetMealInfoVO;
import com.kuangji.paopao.dto.param.MallTradeOrderParam;
import com.kuangji.paopao.model.MallTradeOrder;
import com.kuangji.paopao.service.base.BaseService;
import com.kuangji.paopao.vo.ConsultantMallTradeOrderDetailsVO;
import com.kuangji.paopao.vo.ConsultantMallTradeOrderVO;
import com.kuangji.paopao.vo.ConsultantOrderDiagnosisVO;
import com.kuangji.paopao.vo.ConsultantWorkMemberMallTradeOrderVO;
import com.kuangji.paopao.vo.MallTradeOrderVO;
import com.kuangji.paopao.vo.ReadOrderNumVO;

/**
 * Author 金威正 Date 2020-02-20
 */
public interface MallTradeOrderService extends BaseService<MallTradeOrder, Integer> {

	/**
	 * 根据用户id 查询自己的咨询订单 Author 金威正 Date 2020-02-20
	 */
	List<MallTradeOrderVO> listConsultantMallTradeOrder(Integer userId);
	
	
	/**
	 * 咨询师端 客户订单
	 */
	List<ConsultantWorkMemberMallTradeOrderVO> listConsultantWorkMemberMallTradeOrder(Integer consultantId);

	/**
	 * 咨询师端 正在进行时的 订单
	 */
	List<ConsultantMallTradeOrderVO> listConsultantMallTradeOrderWork(Integer consultantId);

	/**
	 * 咨询师端 完成订单
	 */
	List<ConsultantMallTradeOrderVO> listConsultantMallTradeOrderWorkEnd(Integer consultantId);

	/**
	 * 咨询师端 获取订单信息
	 */
	ConsultantMallTradeOrderDetailsVO getConsultantMallTradeOrderDetailsVO(Integer consultantId, String orderNo);
	
	
	List<ConsultantMallTradeOrderDetailsVO> listConsultantMallTradeOrderDetailsVO(Integer consultantId,Integer memberId);

	MallTradeOrder generateOrder(MallTradeOrder mallTradeOrder);

	/**
	 * 根据用户id 咨询师id 访问 服务类型 确定时间 验证通话 Author 金威正 Date 2020-02-20
	 */
	void memberConversation(String userEasemobId, String consultantEasemobId, Integer serviceType);
	
	/**
	 * 咨询师
	 * 根据用户id 咨询师id 访问 服务类型 确定时间 验证通话 Author 金威正 Date 2020-02-20
	 */
	void consultantConversation(Integer userId, String consultantEasemobId, Integer serviceType);
	

	/**
	 * 根据用户id 把所有的订单消息改为已读 Author 金威正 Date 2020-02-21
	 */
	int updateOrderRead(Integer consultantId);

	/**
	 * 根据用户id 咨询师 确认订单 Author 金威正 Date 2020-02-21
	 */
	int updateOrderConfirm(Integer consultantId, String orderNo);

	/**
	 * 取出对订单的评价 Author 金威正 Date 2020-02-21
	 */
	List<ConsultantOrderDiagnosisVO> listConsultantMallTradeDiagnosis( String orderNo,Integer consultantId);
	

	MallTradeOrder getMallTradeOrderByOrder(String orderNo);
	
	
	/**
	 * 咨询订单 Author 金威正 Date 2020-02-20
	 */
	List<com.kuangji.paopao.admin.vo.MallTradeOrderVO> listConsultantMallTradeOrderByGoodsClass(MallTradeOrderParam mallTradeOrderParam);


	SetMealInfoVO getSetMealInfo(Integer id);
	
	
	com.kuangji.paopao.admin.vo.OrderInfoVO getOrderInfo(Integer id);

	Boolean checkIfFirstOrder(MallTradeOrder mallTradeOrder);

	
	//是否  有取消订单 取消订单不能超过2个  true 为不操作2个
	boolean   cancerOrderBuyUserId(Integer userId);
	
	
	//是不是存在未支付订单 true 为不存在
	boolean   notPayOrderBuyUserId(Integer userId);
	
	//检查是不是可以下单    true 为可以下单
	boolean  checkPayOrderBuyUserId(Integer userId);
	
	//根据下单时间  返回booking表 唯一unk
	
	String getBookingUnKey (String  orderNo);

	//用户取消订单
	int  cancerOrderBuyUserId(String orderNo);
	
	

	//当前订单修改为 已经预约状态
	String getNotOrderPayStatusBookingUnKey(String orderNo);
	
	//查询状态是不是可以预定
	boolean isBooking(Integer userId,String date, String start);
	
	
	//查看普通订单可读 已读数量
	int unReadOrderNum(Integer userId);

	
	//查看订单可读 已读数量
	ReadOrderNumVO unReadOrderNumVO(Integer userId);
	
	//取消订单 解锁 发送取消消息
	int canceOrderUnlock(Integer userId,String orderNo,int payStatus,int orderStatus,int  canceStatus,String content);
	
	//完结订单
	int closeOrder(String orderNo);
	
	List<MallTradeOrder> listNeedCloseOrder();
	
	boolean checkOverBooktimeOrder(MallTradeOrder mallTradeOrder);
	
	int overBooktimeOrder(MallTradeOrder mallTradeOrder);

	//筛选出需要取消的订单
	List<MallTradeOrder> listNeedCancelOrder(); 
	
	//筛选出需要关闭的订单
	boolean checkCanceBooktimeOrder(MallTradeOrder mallTradeOrder);
	
	//取消预定
	int canceBooktimeOrder(MallTradeOrder mallTradeOrder);
	
	//24点跑卖家取消订单
	int canceOrderUnlock(MallTradeOrder mallTradeOrder);

	
	//当前订单修改为 已读状态
	int unReadOrderNum(String orderNo);

	
}


