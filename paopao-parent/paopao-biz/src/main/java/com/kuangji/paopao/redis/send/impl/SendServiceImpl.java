package com.kuangji.paopao.redis.send.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.constant.CommonConstant;
import com.kuangji.paopao.enums.ConsultantWorkEnum;
import com.kuangji.paopao.mapper.ConsultantMapper;
import com.kuangji.paopao.mapper.MallGoodsMapper;
import com.kuangji.paopao.mapper.MallTradeOrderGoodsMapper;
import com.kuangji.paopao.mapper.MallTradeOrderMapper;
import com.kuangji.paopao.mapper.UserMapper;
import com.kuangji.paopao.model.MallGoods;
import com.kuangji.paopao.model.MallTradeOrder;
import com.kuangji.paopao.model.MallTradeOrderGoods;
import com.kuangji.paopao.model.User;
import com.kuangji.paopao.model.consultant.Consultant;
import com.kuangji.paopao.redis.send.SendService;
import com.kuangji.paopao.redis.util.RedisUtils;
import com.kuangji.paopao.service.UserService;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.util.PropertiesFileUtil;
import com.kuangji.paopao.util.SendsmsUtils;
import com.kuangji.paopao.util.VerificationUtils;

import tk.mybatis.mapper.entity.Example;

@Service
public  class SendServiceImpl implements SendService {

	@Autowired
	private RedisUtils redisUtils;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private MallTradeOrderMapper mallTradeOrderMapper;
	
	@Autowired
	private MallTradeOrderGoodsMapper mallTradeOrderGoodsMapper;
	
	@Autowired
	private MallGoodsMapper mallGoodsMapper;
	
	@Autowired
	private ConsultantMapper consultantMapper;
	
	private static String postUrl = PropertiesFileUtil.getInstance().get("post_url");
	
	private static String uid = PropertiesFileUtil.getInstance().get("uid");
	
	private static String pw = PropertiesFileUtil.getInstance().get("pw");

	private static String cashWithdawat = PropertiesFileUtil.getInstance().get("cash_withdawat");
	
	private static String paoPao="【泡泡心理】 ";
	
	private static String paoPaoWork="【泡泡心理 】";
	
	private static String  startOrderContent="您的订单将在10分钟之内开始。请授予APP音/视频权限，打开APP聊天窗，做好提前准备。您可在预约时间内，主动向对方发送音/视频。";
	
	private static String overtimeOrderContent="您预约咨询师，未在预约时间开始前确认您的订单。本次订单将自动取消，系统会在订单取消的同时退款至您的余额，您可自行提现或重新下单。";
	
	private static String newOrder="您有新的订单，用户“已付款”，请打开泡泡心理APP，及时“确认”订单。";
	

    private final Logger logger = LoggerFactory.getLogger(SendServiceImpl.class);

	
	@Override
	public String sendMsg(String phone) {
		// TODO Auto-generated method stub
		if (StringUtils.isBlank(phone)) {
			throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_EMP_USER_NAME);
		}
		boolean bm = VerificationUtils.isMobilePhone(phone);
		if (!bm) {
			throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_PHONE);
		}
		String key=CommonConstant.REDIS_PHONE+phone;
		if (redisUtils.get(key)!=null) {
			throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_CODE_EXISTS);
		}
		int code = (int) ((Math.random() * 9 + 1) * 1000); // 获取随机数
		String string =SendsmsUtils.send(postUrl,code, uid, pw, phone,paoPao);
		redisUtils.set(key, code, 60*1);
		return string;
	}

	
	

	@Override
	public String updatePwdSendMsg(String token) {
		Integer userId=JwtUtils.getUserId(token);
		User user=userService.get(userId);
		String phone=user.getUserName();
		String key=CommonConstant.REDIS_PHONE_UPDATE_PWD+phone;
		if (redisUtils.get(key)!=null) {
			throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_CODE_EXISTS);
		}
		int code = (int) ((Math.random() * 9 + 1) * 1000); // 获取随机数
		String string =SendsmsUtils.send(postUrl,code, uid, pw, phone,paoPao);
		redisUtils.set(key, code, 60*1);
		return string;
	}


	@Override
	public String sendCashWithdrawalMsg(String phone) {
	
		String key=cashWithdawat+":"+phone;
		if (redisUtils.get(key)!=null) {
			throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_CODE_EXISTS);
		}
		int code = (int) ((Math.random() * 9 + 1) * 1000); // 获取随机数
		String string =SendsmsUtils.send(postUrl,code, uid, pw, phone,paoPao);
		redisUtils.set(key, code, 60*1);
		return string;
	}


	@Override
	public String sendConsultationWorkMsg(String phone) {
	
		// TODO Auto-generated method stub
		if (StringUtils.isBlank(phone)) {
			throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_EMP_USER_NAME);
		}
		boolean bm = VerificationUtils.isMobilePhone(phone);
		if (!bm) {
			throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_PHONE);
		}
		String key=CommonConstant.REDIS_CONSULTANT_WORK_PHONE+phone;
		if (redisUtils.get(key)!=null) {
			throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_CODE_EXISTS);
		}
		User user=userMapper.consultantLogin(phone);
		if (user==null) {
			throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_NO_USER);
		}
		int code = (int) ((Math.random() * 9 + 1) * 1000); // 获取随机数
		String string =SendsmsUtils.send(postUrl,code, uid, pw, phone,paoPaoWork);
		redisUtils.set(key, code, 60*1);
		return string;
	}

	@Override
	public String sendsettledInLoginMsg(String phone) {
	
		// TODO Auto-generated method stub
		if (StringUtils.isBlank(phone)) {
			throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_EMP_USER_NAME);
		}
		boolean bm = VerificationUtils.isMobilePhone(phone);
		
		if (!bm) {
			throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_PHONE);
		}
		
		String key=CommonConstant.SETTLED_IN+phone;
		if (redisUtils.get(key)!=null) {
			throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_CODE_EXISTS);
		}
		int code = (int) ((Math.random() * 9 + 1) * 1000); // 获取随机数
		String string =SendsmsUtils.send(postUrl,code, uid, pw, phone,paoPaoWork);
		redisUtils.set(key, code, 60*1);
		return string;
	}



	@Async
	@Override
	public String sendStartOrder(String orderNo) {
		Example example =new Example(MallTradeOrder.class);
		example.createCriteria().andEqualTo("orderNo", orderNo);
		MallTradeOrder mallTradeOrder=mallTradeOrderMapper.selectOneByExample(example);
		Integer userId =mallTradeOrder.getBuyerId();
		User user=userMapper.selectByPrimaryKey(userId);
		SendsmsUtils.sendStartOrder(postUrl,uid,pw, user.getUserName(), paoPao,startOrderContent+"订单号："+orderNo+"。");
		Integer shopId=mallTradeOrder.getShopId();
		
		User consultant=userMapper.selectByPrimaryKey(shopId);
		SendsmsUtils.sendStartOrder(postUrl,uid,pw, consultant.getUserName(), paoPao,startOrderContent+"订单号："+orderNo+"。");
		return "OK";
	}
	
	@Async
	@Override
	public String sendConsultantOrder(String orderNo) {
		Example example =new Example(MallTradeOrder.class);
		example.createCriteria().andEqualTo("orderNo", orderNo);
		MallTradeOrder mallTradeOrder=mallTradeOrderMapper.selectOneByExample(example);
		Integer shopId=mallTradeOrder.getShopId();
		User consultant=userMapper.selectByPrimaryKey(shopId);
		SendsmsUtils.sendStartOrder(postUrl,uid,pw, consultant.getUserName(), paoPao,startOrderContent);
		return "OK";
	}
	
	
	@Async
	@Override
	public String sendConsultantDetermineOrder(String orderNo) {
		Example example =new Example(MallTradeOrder.class);
		example.createCriteria().andEqualTo("orderNo", orderNo);
		MallTradeOrder mallTradeOrder=mallTradeOrderMapper.selectOneByExample(example);
		Integer buyerId=mallTradeOrder.getBuyerId();
		User user=userMapper.selectByPrimaryKey(buyerId);
		String ext=mallTradeOrder.getExt();
		JSONObject jsonObject= JSONObject.parseObject(ext);
		String date = jsonObject.getString(CommonConstant.CONSULTANT_WORK_DATE);
		String time = jsonObject.getString(CommonConstant.CONSULTANT_WORK_TIME);
		StringBuffer stringBuffer = new StringBuffer(date);
		stringBuffer.append(" ").append(time);
		
		Example example2 =new Example(MallTradeOrderGoods.class);
		example2.createCriteria().andEqualTo("orderNo", orderNo);
		MallTradeOrderGoods mallTradeOrderGoods=mallTradeOrderGoodsMapper.selectOneByExample(example2);
		
		String  bookingOrderContent="您预约的咨询师订单“已确认”，预约时间："+stringBuffer.toString()+"；服务时间结束后订单将自动关闭，请准时赴约。订单号："+orderNo+"。";

		Integer goodsId=mallTradeOrderGoods.getGoodsId();
		MallGoods mallGoods=mallGoodsMapper.selectByPrimaryKey(goodsId);
		if (mallGoods.getSellPointText().equals(ConsultantWorkEnum.FACING_EACH_OTHER.getValue())) {
			Integer shopId=mallTradeOrder.getShopId();
			Example example3 =new Example(Consultant.class);
			example3.createCriteria().andEqualTo("userId",shopId );
			Consultant consultant=consultantMapper.selectOneByExample(example3);
			String faceAddress=consultant.getFaceAddress();
			if (StringUtils.isBlank(faceAddress)) {
				faceAddress=consultant.getAddrDetail();
			}
		     stringBuffer.append("。地址为：")
			 			 .append(consultant.getProvName())
			 			 .append(consultant.getCityName())
			 			 .append(consultant.getAreaName())
						 .append(faceAddress);
		 bookingOrderContent="您预约的咨询师订单“已确认”，预约时间："+stringBuffer.toString()+"；服务时间结束后订单将自动关闭，请准时赴约。订单号："+orderNo+"。";

		}
		
		SendsmsUtils.sendStartOrder(postUrl,uid,pw, user.getUserName(), paoPao,bookingOrderContent);
		return "OK";
	}



	@Override
	public String sendConsultantOvertimeOrder(String orderNo) {
		// TODO Auto-generated method stub
		Example example =new Example(MallTradeOrder.class);
		example.createCriteria().andEqualTo("orderNo", orderNo);
		MallTradeOrder mallTradeOrder=mallTradeOrderMapper.selectOneByExample(example);
		
		Integer buyerId=mallTradeOrder.getBuyerId();
		User user=userMapper.selectByPrimaryKey(buyerId);
		logger.info("开始  订单号为 ", orderNo+",手机号为 "+user.getUserName());
		SendsmsUtils.sendStartOrder(postUrl,uid,pw, user.getUserName(), paoPao,overtimeOrderContent+"订单号："+orderNo+"。");
		return "OK";
	}




	@Override
	public String sendNewOrder(String orderNo) {
		Example example =new Example(MallTradeOrder.class);
		example.createCriteria().andEqualTo("orderNo", orderNo);
		MallTradeOrder mallTradeOrder=mallTradeOrderMapper.selectOneByExample(example);
		Integer shopId=mallTradeOrder.getShopId();
		User user=userMapper.selectByPrimaryKey(shopId);
		SendsmsUtils.sendStartOrder(postUrl,uid,pw, user.getUserName(), paoPao,newOrder+"订单号："+orderNo+"。");
		return "OK";
	}




	@Override
	public String startimmediatelyOrderContent(String orderNo) {
		Example example =new Example(MallTradeOrder.class);
		example.createCriteria().andEqualTo("orderNo", orderNo);
		MallTradeOrder mallTradeOrder=mallTradeOrderMapper.selectOneByExample(example);
		Integer userId =mallTradeOrder.getBuyerId();
		User user=userMapper.selectByPrimaryKey(userId);
		SendsmsUtils.sendStartOrder(postUrl,uid,pw, user.getUserName(), paoPao,startOrderContent+"订单号："+orderNo+"。");
		Integer shopId=mallTradeOrder.getShopId();
		User consultant=userMapper.selectByPrimaryKey(shopId);
		SendsmsUtils.sendStartOrder(postUrl,uid,pw, consultant.getUserName(), paoPao,startOrderContent+"订单号："+orderNo+"。");
		return "OK";
	}




	@Override
	public String sendMsg(String phone, String content) {
		SendsmsUtils.sendStartOrder(postUrl,uid,pw, phone, paoPao,content);
		return "OK";
	}
}
