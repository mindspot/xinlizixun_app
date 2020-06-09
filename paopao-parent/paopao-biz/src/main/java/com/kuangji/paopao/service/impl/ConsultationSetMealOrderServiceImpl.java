package com.kuangji.paopao.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.constant.CommonConstant;
import com.kuangji.paopao.enums.MallGoodsClassEnum;
import com.kuangji.paopao.mapper.ConsultationSetMealOrderMapper;
import com.kuangji.paopao.mapper.MallGoodsMapper;
import com.kuangji.paopao.mapper.MallTradeOrderGoodsMapper;
import com.kuangji.paopao.mapper.UserMapper;
import com.kuangji.paopao.model.ConsultationSetMealOrder;
import com.kuangji.paopao.model.MallGoods;
import com.kuangji.paopao.model.MallTradeOrder;
import com.kuangji.paopao.model.MallTradeOrderGoods;
import com.kuangji.paopao.mq.factory.MallOrderFactory;
import com.kuangji.paopao.redis.AsyncTask;
import com.kuangji.paopao.redis.send.SendService;
import com.kuangji.paopao.schedule.delayqueue.DelayQueueManager;
import com.kuangji.paopao.service.ConsultationSetMealOrderService;
import com.kuangji.paopao.service.MallTradeOrderService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.vo.MQTradeOrderVO;
import com.kuangji.paopao.vo.SetMealVO;

import tk.mybatis.mapper.entity.Example;

/**
 * Author 金威正
 * Date  2020-03-10
 */
@Service
public class ConsultationSetMealOrderServiceImpl extends BaseServiceImpl<ConsultationSetMealOrder, Integer> implements ConsultationSetMealOrderService {
    @Autowired
    private ConsultationSetMealOrderMapper consultationSetMealOrderMapper;
    
    @Autowired
    private   MallTradeOrderGoodsMapper mallTradeOrderGoodsMapper;

	@Autowired
	AsyncTask asyncTask;
	
	@Autowired
	SendService  sendService;
	
	@Autowired
	DelayQueueManager  delayQueueManager;
	
	@Autowired
	UserMapper  userMapper;

	@Autowired
	RedissonClient redissonClient;
	
	
	@Autowired
	MallTradeOrderService mallTradeOrderService;
	
	@Autowired
	MallGoodsMapper  mallGoodsMapper;
	
	//private static final int twenty_four_hours=24*60*60*1000;

	 static final int twenty_four_hours=2*60*1000;
    

	@Override
	public ConsultationSetMealOrder getSetMealConsultationOrder(Integer shopId, Integer goodsId, Integer userId) {
		
		return consultationSetMealOrderMapper.getSetMealConsultationOrder(shopId, goodsId, userId);
	}

	@Autowired
	MallOrderFactory mallOrderFactory;
	
	@Override
	public List<SetMealVO> listSetMeal(String  token) {
		
		Integer  userId=JwtUtils.getUserId(token);
		return consultationSetMealOrderMapper.listSetMeal(userId);
	}

	@Override
	public List<ConsultationSetMealOrder> listSetMealConsultationOrder(Integer shopId, Integer userId) {
		
		return consultationSetMealOrderMapper.listSetMealConsultationOrder(shopId, userId);
	}

	

	@Override
	public BaseMapper<ConsultationSetMealOrder> getMapper() {
		
		return consultationSetMealOrderMapper;
	}

	@Override
	public PageInfo<SetMealVO> listConsultantSetMeal(Integer consultantId,Integer pageNum) {
		
		PageHelper.startPage(pageNum, 13);
		List<SetMealVO> list=consultationSetMealOrderMapper.listConsultantSetMeal(consultantId);
		PageInfo<SetMealVO> pageInfo = new PageInfo<>(list);
		return pageInfo ;
	}

	@Override
	public boolean isPaySetMeal(Integer shopId, Integer userId) {
		List<ConsultationSetMealOrder> list=this.listSetMealConsultationOrder(shopId, userId);
		
		return list!=null&&!list.isEmpty();
	}

	@Override
	public boolean insertSetMealOrder(String orderNo) {
		
		MQTradeOrderVO mo = this.mallOrderFactory.create(orderNo);
		if (mo == null) {
			return false;
		}
		String ext = mo.getExt();
		JSONObject	jsonObject = JSONObject.parseObject(ext);
		String date = jsonObject.getString(CommonConstant.CONSULTANT_WORK_DATE);
		String time = jsonObject.getString(CommonConstant.CONSULTANT_WORK_TIME);
		StringBuffer stringBuffer = new StringBuffer(date);
		stringBuffer.append(" ").append(time);
		ConsultationSetMealOrder setMealOrder = new ConsultationSetMealOrder();
		setMealOrder.setOrderNo(orderNo);
		setMealOrder.setConsultationTime(stringBuffer.toString());
		setMealOrder.setShopId(mo.getShopId());
		setMealOrder.setUserId(mo.getBuyerId());
		setMealOrder.setConsultationNumber((mo.getBuyCount()-1));
		setMealOrder.setGoodsId(mo.getGoodsId());
		setMealOrder.setBuyDate(new Date());
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, mo.getExpires());
		setMealOrder.setTermEndDate(cal.getTime());
		if (this.consultationSetMealOrderMapper.insertSelective(setMealOrder) < 1) {
			throw new ServiceException(ResultCodeEnum.FAIL);
		}
		
		return true;
	}

	@Override
	public boolean checkSetMealOrder(String orderNo) {
		Example example =new Example(MallTradeOrderGoods.class);
		example.createCriteria().andEqualTo("orderNo", orderNo);
		MallTradeOrderGoods mallTradeOrderGoods=mallTradeOrderGoodsMapper.selectOneByExample(example);
		MallGoods goods=mallGoodsMapper.selectByPrimaryKey(mallTradeOrderGoods.getGoodsId());
		if (goods.getGoodsClass()==MallGoodsClassEnum.SET_MEAL.getValue()) {
			this.insertSetMealOrder(orderNo);
			return true;
		}
		return false;
	}

	@Override
	public boolean updateAddResidualTimes(String orderNo) {
		
		MallTradeOrder mallTradeOrder= mallTradeOrderService.findOne(new MallTradeOrder(orderNo));
		
		Integer shopId=mallTradeOrder.getShopId();
		Integer userId=mallTradeOrder.getBuyerId();
		Example example =new Example(ConsultationSetMealOrder.class);
		example.createCriteria()
				.andEqualTo("userId", userId)
				.andEqualTo("shopId", shopId);
		List<ConsultationSetMealOrder>  list=consultationSetMealOrderMapper.selectByExample(example);
		for (ConsultationSetMealOrder consultationSetMealOrder : list) {
			int result=consultationSetMealOrderMapper.updateAddResidualTimes(consultationSetMealOrder.getOrderNo());
			if (result>0) {
				return true;
			}
		}
		return false;
	}
	
  
    
}
