package com.kuangji.paopao.mq.listener.order;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.constant.CommonConstant;
import com.kuangji.paopao.enums.MallGoodsClassEnum;
import com.kuangji.paopao.model.ConsultantScheduleStatus;
import com.kuangji.paopao.mq.message.MessageBean;
import com.kuangji.paopao.mq.message.MessageBean.MQEvent;
import com.kuangji.paopao.service.CouponService;
import com.kuangji.paopao.vo.MQTradeOrderVO;

/**
 * @author Administrator 套餐购买
 */
@Service("consultationSetMealOrderMessageListener")
public class ConsultationSetMealOrderMessageListener extends AbstractMessageListener {
	@Autowired
	private CouponService couponService;
	@Transactional
	@Override
	public void onMessage(String message) {
		try {
			// 解析消息
			MessageBean msg = this.parseMessage(message);
			if (msg.getEvent() == MQEvent.PAY_SUCC.code) {
				//
				Map<String, Object> data = msg.getData();
				String orderMap = data.get("order").toString();
				JSONObject jsonObject = JSONObject.parseObject(orderMap);
				int orderType = (int) jsonObject.get("orderType");
				if (orderType == MallGoodsClassEnum.SET_MEAL.getValue()) {
					String orderNo = (String) jsonObject.get("orderNo");
					
					MQTradeOrderVO mo = this.mallOrderFactory.create(orderNo);
					if (mo == null) {
						return;
					}
					this.upateOrder(jsonObject);
					Integer couponId = mo.getCouponId();
					String ext = mo.getExt();
					jsonObject = JSONObject.parseObject(ext);
					String date = jsonObject.getString(CommonConstant.CONSULTANT_WORK_DATE);
					String time = jsonObject.getString(CommonConstant.CONSULTANT_WORK_TIME);
					StringBuffer stringBuffer = new StringBuffer(date);
					stringBuffer.append(" ").append(time);
					
					try {
						ConsultantScheduleStatus userScheduleStatus = new ConsultantScheduleStatus();
						userScheduleStatus.setConsultantId(mo.getShopId());
						userScheduleStatus.setScheduleDate(date);
						String[] strings = time.split("-");
						userScheduleStatus.setScheduleStartTime(strings[0]);
						userScheduleStatus.setScheduleEndTime(strings[1]);
					
						this.checkConsultantScheduleStatus(userScheduleStatus);

						this.updateOrderCancel(mo);
						// 发给咨询师 确认订单
					asyncTask.sendOrder(orderNo, mo.getBuyerId(), mo.getShopId(),stringBuffer.toString(),mo.getSellPointText());
					this.sendStartOrder(orderNo,date,strings[0]);
					} catch (Exception e) {
						mallTradeOrderMapper.updateMallTradeOrderWrong(orderNo);

						if (e instanceof ServiceException) {
							ServiceException serviceException = (ServiceException) e;
							throw serviceException;
						}
						throw e;
					}
					

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return;
	}

	@Override
	public boolean exitOrder(String orderNo) {
		// TODO Auto-generated method stub

		return true;
	}
	
}