package com.kuangji.paopao.service.impl;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.mapper.SendSmsMapper;
import com.kuangji.paopao.mapper.UserMapper;
import com.kuangji.paopao.model.SendSms;
import com.kuangji.paopao.model.User;
import com.kuangji.paopao.redis.send.SendService;
import com.kuangji.paopao.service.SendSmsService;
import com.kuangji.paopao.service.UserService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;
import com.kuangji.paopao.util.DateUtils;

import tk.mybatis.mapper.entity.Example;

@Service
public class SendSmsServiceImpl  extends BaseServiceImpl<SendSms, Integer> implements   SendSmsService{

	@Autowired
	private SendSmsMapper sendSmsMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private UserService userService;
	@Autowired
	private SendService sendService;

	
	private static final int  MINUTE=30*60*1000;
	
	
	
	private static final Logger LOG = LoggerFactory.getLogger(SendSmsServiceImpl.class);

	
	@Override
	public BaseMapper<SendSms> getMapper() {
	
		return sendSmsMapper;
	}

	@Override
	public int sendSms(Integer userId,String easemobShopId) {
		
		if (userService.isOnline(easemobShopId)) {
			throw new ServiceException(-1, "已提醒成功");
		}
		
		String dateStr=DateUtils.formatDate(new Date(), "yyyy-MM-dd");
	
		Integer sum=sendSmsMapper.sumSendSmsCount(userId, dateStr);
		
		//当前发送次数大于5不可发送
		if (sum>=5) {
			throw new ServiceException(-1, "当日已发送5次,不可继续发送");
		}
		
		//判断给当前咨询师发送次数
		Integer userConsultantId=Integer.valueOf(easemobShopId.replaceAll("[a-zA-Z]",""));
		Example example2 =new Example(SendSms.class);
		example2.setOrderByClause("id DESC limit 1");
		example2.createCriteria()
				.andEqualTo("userId",userId )
				.andEqualTo("userConsultantId",userConsultantId )
				.andEqualTo("sendDate",dateStr);
		SendSms sendSms2=sendSmsMapper.selectOneByExample(example2);
		//短信接收者 
		User user =new User();
		user.setEasemobId(easemobShopId);
		User u=userMapper.selectOne(user);
		//手机号码
		String userName=u.getUserName();
		
		//短信发送者
		User user2 =new User();
		user2.setId(userId);
		User u2=userMapper.selectOne(user2);
		
		if (sendSms2==null) {
			LOG.info(userId+"用户今天  第一次发送通知咨询师上线短信");
			sendSms2=new SendSms();
			sendSms2.setUserId(userId);
			sendSms2.setSendDate(dateStr);
			sendSms2.setSendTime(new Date());
			sendSms2.setUserConsultantId(userConsultantId);
			sendService.sendMsg(userName, "您的来访者："+u2.getRealName()+"，提醒您上线；请及时回复消息信息。");
			return sendSmsMapper.insertSelective(sendSms2);
		}
		//当前用户最后一次发送短信时间
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sendSms2.getSendTime());
		long sendMinute=calendar.getTimeInMillis();
		
		
		//当前时间
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(new Date());
		long nowMinute=calendar2.getTimeInMillis();
		
		//差值
		long subMinute=nowMinute-sendMinute;
		
		LOG.info(userId+" 非第一次发送短信,当前时间 "+nowMinute+",用户最后一次发送短信时间 "+sendMinute+",已经发送次数 "+sum+",时间差值"+subMinute);
		
		//并且 差值大于30分钟 不可发送
		if (subMinute<MINUTE) {
			throw new ServiceException(-1, "30分钟内不可重复发送");
			//发送信息	
		}
		
		LOG.info("发送咨询师手机号码 "+userName);
		
		//更新最后一次发送记录 并且次数+1
		SendSms sms=new SendSms();
		sms.setId(sendSms2.getId());
		sms.setSendTime(new Date());
		sms.setFrequency(sendSms2.getFrequency()+1);
		int result=sendSmsMapper.updateByPrimaryKeySelective(sms);
		if (result>0) {
			sendService.sendMsg(userName, "您的来访者："+u2.getRealName()+"，提醒您上线；请及时回复消息信息。");
		}
		return result;
	}

}
