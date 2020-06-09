package com.kuangji.paopao.order.business.cust.consulting;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.enums.OrderTypeEnum;
import com.kuangji.paopao.mapper.MemberCaseMapper;
import com.kuangji.paopao.model.MallTradeOrder;
import com.kuangji.paopao.model.MallTradeOrderGoods;
import com.kuangji.paopao.order.business.cust.def.DefaultCustOrderServiceImpl;
import com.kuangji.paopao.order.vo.BaseOrderFormVO;
import com.kuangji.paopao.order.vo.BaseOrderFormVO.BuyGoodsFormVO;
import com.kuangji.paopao.schedule.delayqueue.DelayQueueManager;
import com.kuangji.paopao.service.BookingTimeService;
import com.kuangji.paopao.service.MallTradeOrderService;

//套餐 购买下单
@Service("consultingSetMealService")
public class ConsultingSetMealServiceImpl extends DefaultCustOrderServiceImpl {


	@Autowired
	MallTradeOrderService mallTradeOrderService;
	
	@Autowired
	MemberCaseMapper memberCaseMapper;
	
	@Autowired
	BookingTimeService bookingTimeService;
	
	
	@Autowired
	DelayQueueManager delayQueueManager;
	

	@Override
	protected void onCancelSuccEvent(MallTradeOrder order) {
		

	}
	

	@Override
	protected void onConfirmAcceSuccEvent(MallTradeOrder order) {
		

	}

	@Override
	protected int getOrderType() {
		// TODO Auto-generated method stub
		return OrderTypeEnum.SET_MEAL.code;
	}

	


	


	/**
	 * 创建订单中的商品信息<br>
	 * 商品金额采用数据库中的商品金额
	 * 
	 * @param
	 */
	protected List<MallTradeOrderGoods> genOrderGoods(BaseOrderFormVO fvo) {
		List<MallTradeOrderGoods> orderGoodsList = null;
		if (fvo.getBuyGoods() != null && !fvo.getBuyGoods().isEmpty()) {
			orderGoodsList = new LinkedList<MallTradeOrderGoods>();
			// 默认商品金额
			for (BuyGoodsFormVO vo : fvo.getBuyGoods()) {
				MallTradeOrderGoods ogs = this.mallTradeOrderGoodsMapper.getBuyGoods(vo);
				// 验证商品信息
				if (ogs == null) {
					throw new ServiceException(ResultCodeEnum.ERROR_TRADE_ORDER_DOWN_GOODS);
				}
				if (ogs.getGoodsClass() != OrderTypeEnum.SET_MEAL.code) {
					throw new ServiceException(ResultCodeEnum.ERROR_ORDER_BAD_TYPE);

				}
				orderGoodsList.add(ogs);
			}
		}
		return orderGoodsList;
	}





}
