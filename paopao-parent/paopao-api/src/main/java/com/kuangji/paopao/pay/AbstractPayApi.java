package com.kuangji.paopao.pay;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.kuangji.paopao.api.impl.OrderApiImpl;
import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.mapper.ConsultationSetMealOrderMapper;
import com.kuangji.paopao.order.business.cust.ICustOrderService;
import com.kuangji.paopao.order.dto.ConsultantServiceDTO;
import com.kuangji.paopao.order.vo.BaseOrderFormVO.BuyGoodsFormVO;
import com.kuangji.paopao.service.ConsultantScheduleStatusService;
import com.kuangji.paopao.service.ConsultationOrderService;
import com.kuangji.paopao.service.MallTradeOrderGoodsService;
import com.kuangji.paopao.service.MallTradeOrderService;
import com.kuangji.paopao.service.MemberCaseService;
import com.kuangji.paopao.util.JwtUtils;

@Component
public abstract class AbstractPayApi implements PayApi {

	@Autowired
	protected MallTradeOrderGoodsService mallTradeOrderGoodsService;
	
	@Autowired
	protected ConsultationOrderService consultationOrderService;
	
	@Autowired
	protected MallTradeOrderService mallTradeOrderService;
	
	@Autowired
	protected MemberCaseService userCaseService;
	
	@Autowired
	protected ConsultantScheduleStatusService userScheduleStatusService;
	
	@Autowired
	@Qualifier("consultingOrderPayService")
	protected ICustOrderService iCustOrderService;

	
	@Autowired
	protected ConsultationSetMealOrderMapper consultationSetMealOrderMapper;
	@Autowired 
	OrderApiImpl orderApiImpl;
	
	@Override
	public void paySetMeal(String token,ConsultantServiceDTO consultantServiceDTO){
		

		Integer buyerId = JwtUtils.getUserId(token);
		if (consultantServiceDTO.getConsultantType()==null||consultantServiceDTO.getConsultantType().length<1) {
			throw new ServiceException(-1, "咨询问题不能为空");
		}
		if (consultantServiceDTO.getBuyGoods()==null
				||consultantServiceDTO.getBuyGoods().isEmpty()||consultantServiceDTO.getBuyGoods().size()!=1) {
			throw new ServiceException(-1, "购买的服务出错");
		}
		
		String date = consultantServiceDTO.getConsultantWorkDate();
		if (StringUtils.isBlank(date)) {
			throw new ServiceException(-1, "预订日期不能为空");
		}
		String time = consultantServiceDTO.getConsultantWorkTime();

		if (StringUtils.isBlank(time)) {
			throw new ServiceException(-1, "预订详细时间不能为空");
		}
		// 分割
		String[] strings = time.split("-");
		if (strings.length != 2) {
			throw new ServiceException(ResultCodeEnum.ILLGEAL_PARAMTER);
		}
		String start = strings[0];
		if (StringUtils.isBlank(start)) {
			throw new ServiceException(ResultCodeEnum.ILLGEAL_PARAMTER);
		}
		String end = strings[1];
		if (StringUtils.isBlank(end)) {
			throw new ServiceException(ResultCodeEnum.ILLGEAL_PARAMTER);

		}

		// 工作方式 视频 语音 视频/语音
		List<BuyGoodsFormVO> buyGoodsFormVOs = consultantServiceDTO.getBuyGoods();
		if (!buyGoodsFormVOs.isEmpty() && buyGoodsFormVOs.size() == 1) {
			BuyGoodsFormVO buyGoodsFormVO = buyGoodsFormVOs.get(0);
			Integer shopId = buyGoodsFormVO.getShopId();
			consultantServiceDTO.setBuyerId(buyerId);
			orderApiImpl.checkBuyOrder(shopId, buyerId, date, time);
			iCustOrderService.dealUnifiedOrder(consultantServiceDTO);
		}
	}
	

}