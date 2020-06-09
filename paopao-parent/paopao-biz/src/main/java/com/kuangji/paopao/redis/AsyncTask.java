package com.kuangji.paopao.redis;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.enums.MallGoodsClassEnum;
import com.kuangji.paopao.mapper.ConsultantSupervisorOrderMapper;
import com.kuangji.paopao.mapper.MallTradeOrderMapper;
import com.kuangji.paopao.mapper.UserMapper;
import com.kuangji.paopao.model.ConsultantSupervisorOrder;
import com.kuangji.paopao.model.MallTradeOrder;
import com.kuangji.paopao.model.User;
import com.kuangji.paopao.util.DateUtils;
import com.kuangji.paopao.util.ImageUtils;
import com.kuangji.paopao.util.PropertiesFileUtil;
import com.kuangji.paopao.vo.ExtGroupVO;
import com.kuangji.paopao.vo.ExtStopMsgVO;
import com.kuangji.paopao.vo.ExtVO;

@Component
public class AsyncTask {
	@Autowired
	private Easemob easemob;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private ConsultantSupervisorOrderMapper supervisorOrderMapper;

	// 环信 用户 标志
	public static final String member = PropertiesFileUtil.getInstance().get("easemob_member");

	// 环信 咨询师标志easemob_consultant
	public static final String consultant = PropertiesFileUtil.getInstance().get("easemob_consultant");

	@Autowired
	private MallTradeOrderMapper mallTradeOrderMapper;

	private static String imageUrl = PropertiesFileUtil.getInstance().get("image_url");

	private static String defaultSysHeadImage = PropertiesFileUtil.getInstance().get("default_sys_head_image");

	private static String PAY_SUCCESS = "已支付";

	private static String CONFIRM_ORDER = "已确认";

	private static String END_ORDER = "已完成";

	private static String CANCEl_ORDER = "已取消";

	private static String REFUSR_ORDER = "已拒绝";

	private static final Logger LOG = LoggerFactory.getLogger(AsyncTask.class);

	private static final String STOP_MAS = "您好，本次服务时间已到，为不占用其他用户的服务时间，系统已经关闭音视频功能，敬请谅解，谢谢。";
	
	private static final String TEMP_STOP_MAS = "督导完毕后，请至订单详情界面，点击“督导完毕”按钮，结束订单，结算金额。";
	
	private static final String CONSULTANT_STOP_MAS = "您好，本次服务时间已到，系统已经关闭音视频功能。若需要延长服务时间，您可继续主动发起音/视频。";

	// 普通订单支付后发送消息
	@Async
	public void sendOrder(String orderNo, Integer from, Integer user, String ext, String sellPointText) {

		this.sendUpdateOrderMsg(orderNo, PAY_SUCCESS);

		Calendar date = Calendar.getInstance();
		String str = String.valueOf(date.get(Calendar.YEAR) + "-");
		ext = ext.replace(str, "");
		ExtVO extVO = this.createExtVO(orderNo, from, ext);
		extVO.setMsgType("1");
		extVO.setOrderType(0);
		extVO.setServiceType(sellPointText);
		easemob.sendOrderMsg(member + from, consultant + user, extVO);
	}

	// 新订单消息
	@Async
	public void sendUpdateOrderMsg(String orderNo, String connent) {
		MallTradeOrder mallTradeOrder = mallTradeOrderMapper.selectOne(new MallTradeOrder(orderNo));
		Integer buyId = mallTradeOrder.getBuyerId();
		User user = userMapper.selectByPrimaryKey(buyId);
		user.setHeadImg(user.getHeadImg() + user.getHeadImgSize());
		Integer shopId = mallTradeOrder.getShopId();
		User user2 = userMapper.selectByPrimaryKey(shopId);
		user2.setHeadImg(user2.getHeadImg() + user2.getHeadImgSize());
		Date orderTime = mallTradeOrder.getOrderTime();
		String orderTimeStr = DateUtils.formatDate(orderTime, "yyyy-MM-dd HH:mm:ss");
		easemob.msgConfirmOrder(user.getEasemobId(), user2.getEasemobId(), orderNo, user.getRealName(),
				imageUrl + user.getHeadImg(), connent, orderTimeStr, null, null);
		easemob.msgConfirmOrder(user2.getEasemobId(), user.getEasemobId(), orderNo, user2.getRealName(),
				imageUrl + user2.getHeadImg(), connent, orderTimeStr, null, null);
	}

	// 督导订单已支付发送消息
	@Async
	public void supervisorsOrder(String orderNo, Integer from, Integer userId) {

		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
		String ext = sdf.format(new Date());
		ExtVO extVO = this.createExtVO(orderNo, from, ext);
		// 1督导订单
		extVO.setMsgType("1");
		extVO.setOrderType(1);
		easemob.sendOrderMsg(consultant + from, consultant + userId, extVO);

		ConsultantSupervisorOrder consultantSupervisorOrder = supervisorOrderMapper
				.selectOne(new ConsultantSupervisorOrder(orderNo));
		Integer buyId = consultantSupervisorOrder.getConsultantId();
		User user = userMapper.selectByPrimaryKey(buyId);
		user.setHeadImg(user.getHeadImg() + user.getHeadImgSize());

		Integer shopId = consultantSupervisorOrder.getSupervisorId();
		User user2 = userMapper.selectByPrimaryKey(shopId);
		user2.setHeadImg(user2.getHeadImg() + user2.getHeadImgSize());
		Date orderTime = consultantSupervisorOrder.getInsertTime();
		String orderTimeStr = DateUtils.formatDate(orderTime, "yyyy-MM-dd HH:mm:ss");
		// 咨询师 发送 方 user 接受方督导师 user2
		easemob.msgConfirmOrder(user2.getEasemobId(), user.getEasemobId(), orderNo, user2.getRealName(),
				imageUrl + user2.getHeadImg(), PAY_SUCCESS, orderTimeStr, null, null);
		easemob.msgConfirmOrder(user.getEasemobId(), user2.getEasemobId(), orderNo, user.getRealName(),
				imageUrl + user.getHeadImg(), PAY_SUCCESS, orderTimeStr, null, null);
	}

	// 服务停止
	@Async
	public void sendStopServiceMsg(String orderNo, int type) {

		if (type == MallGoodsClassEnum.CONSULTANT_SERVICE.getValue()) {
			MallTradeOrder mallTradeOrder = mallTradeOrderMapper.selectOne(new MallTradeOrder(orderNo));
			Integer buyId = mallTradeOrder.getBuyerId();
			User user = userMapper.selectByPrimaryKey(buyId);
			String memoberId = user.getEasemobId();

			Integer shopId = mallTradeOrder.getShopId();
			User user2 = userMapper.selectByPrimaryKey(shopId);
			String consultanId = user2.getEasemobId();
			easemob.sendStopServiceMsg(memoberId, consultanId, this.getExtStopMsgVO(user, orderNo), CONSULTANT_STOP_MAS);
			easemob.sendStopServiceMsg(consultanId, memoberId, getExtStopMsgVO(user2, orderNo), STOP_MAS);
			return;
		}

		ConsultantSupervisorOrder mallTradeOrder = supervisorOrderMapper
				.selectOne(new ConsultantSupervisorOrder(orderNo));
		Integer buyId = mallTradeOrder.getConsultantId();
		User user = userMapper.selectByPrimaryKey(buyId);
		String memoberId = user.getEasemobId();

		Integer shopId = mallTradeOrder.getSupervisorId();
		User user2 = userMapper.selectByPrimaryKey(shopId);

		String consultanId = user2.getEasemobId();
		easemob.sendStopServiceMsg(memoberId, consultanId, getExtStopMsgVO(user, orderNo), TEMP_STOP_MAS);
		// easemob.sendStopServiceMsg(consultanId,memoberId,getExtStopMsgVO(user2,
		// orderNo),TEMP_STOP_MAS);

	}


	protected ExtStopMsgVO getExtStopMsgVO(User user, String orderNo) {
		ExtStopMsgVO extStopMsgVO = new ExtStopMsgVO();
		extStopMsgVO.setNickname(user.getRealName());
		user.setHeadImg(user.getHeadImg() + user.getHeadImgSize());
		extStopMsgVO.setUserphoto(ImageUtils.getImgagePath(imageUrl, user.getHeadImg()));
		// 立即停止服务
		extStopMsgVO.setServiceType("1");
		return extStopMsgVO;
	}

	// 服务停止前发送提示消息
	@Async
	public void sendBeforeStopServiceMsg(String orderNo) {

		MallTradeOrder mallTradeOrder = mallTradeOrderMapper.selectOne(new MallTradeOrder(orderNo));
		Integer buyId = mallTradeOrder.getBuyerId();
		Integer shopId = mallTradeOrder.getShopId();
		String userId = member + buyId;
		User buyer = userMapper.selectByPrimaryKey(buyId);
		buyer.setHeadImg(buyer.getHeadImg() + buyer.getHeadImgSize());
		String consultanId = consultant + shopId;
		User consultant = userMapper.selectByPrimaryKey(shopId);
		consultant.setHeadImg(consultant.getHeadImg() + consultant.getHeadImgSize());
		easemob.sendBeforeStopServiceMsg(userId, consultanId, buyer.getRealName(), imageUrl + buyer.getHeadImg());
		easemob.sendBeforeStopServiceMsg(consultanId, userId, consultant.getRealName(),
				imageUrl + consultant.getHeadImg());
	}

	
	@Async
	public void consultantSendStopServiceMsg(String orderNo) {
		MallTradeOrder mallTradeOrder = mallTradeOrderMapper.selectOne(new MallTradeOrder(orderNo));
		Integer buyId = mallTradeOrder.getBuyerId();
		User user = userMapper.selectByPrimaryKey(buyId);
		String memoberId = user.getEasemobId();
		Integer shopId = mallTradeOrder.getShopId();
		User user2 = userMapper.selectByPrimaryKey(shopId);
		String consultanId = user2.getEasemobId();
		easemob.sendStopServiceMsg(memoberId, consultanId, this.getExtStopMsgVO(user, orderNo), CONSULTANT_STOP_MAS);
		easemob.sendStopServiceMsg(consultanId, memoberId, getExtStopMsgVO(user2, orderNo), STOP_MAS);
		return;
	}
	// 多人发送消息
	@Async
	public void sendGroupMsg(Set<String> users, String mgs, ExtGroupVO extGroupVO) {
		users = new HashSet<String>();
		users.add("pu35");
		users.add("pu97");
		users.add("pu96");
		users.add("pu99");

		/* easemob.sendGroupServiceMsg(users, mgs, extGroupVO); */
	}

	// 点击 订单确认 消息
	@Async
	public void sendOrderConfirmServiceMsg(String orderNo) {

		MallTradeOrder mallTradeOrder = mallTradeOrderMapper.selectOne(new MallTradeOrder(orderNo));
		Integer buyId = mallTradeOrder.getBuyerId();
		Integer shopId = mallTradeOrder.getShopId();

		User user = userMapper.selectByPrimaryKey(shopId);
		String memberUserId = member + buyId;
		String consultantUserId = consultant + shopId;

		LOG.info("咨询师给用户发送消息" + consultantUserId + " -> " + memberUserId);
		user.setHeadImg(user.getHeadImg() + user.getHeadImgSize());
		easemob.msgConfirmOrder(consultantUserId, memberUserId, orderNo, user.getRealName(),
				imageUrl + user.getHeadImg(), CONFIRM_ORDER, null, null, "1");

		User user2 = userMapper.selectByPrimaryKey(buyId);
		LOG.info("用户发送咨询师消息" + memberUserId + " -> " + consultantUserId);
		user2.setHeadImg(user2.getHeadImg());
		easemob.msgConfirmOrder(memberUserId, consultantUserId, orderNo, user2.getRealName(),
				imageUrl + user2.getHeadImg(), CONFIRM_ORDER, null, null, "1");

	}

	// 督导订单确认
	@Async
	public void sendSupervisorConfirmServiceMsg(String orderNo) {

		ConsultantSupervisorOrder consultantSupervisorOrder = supervisorOrderMapper
				.selectOne(new ConsultantSupervisorOrder(orderNo));
		Integer consultantId = consultantSupervisorOrder.getConsultantId();

		Integer supervisorId = consultantSupervisorOrder.getSupervisorId();

		User user = userMapper.selectByPrimaryKey(supervisorId);
		user.setHeadImg(user.getHeadImg() + user.getHeadImgSize());
		String orderTime = consultantSupervisorOrder.getPayTime();
		User user2 = userMapper.selectByPrimaryKey(consultantId);
		user2.setHeadImg(user2.getHeadImg() + user2.getHeadImgSize());
		easemob.msgConfirmOrder(user2.getEasemobId(), user.getEasemobId(), orderNo, user2.getRealName(),
				imageUrl + user2.getHeadImg(), CONFIRM_ORDER, orderTime, null, null);
		easemob.msgConfirmOrder(user.getEasemobId(), user2.getEasemobId(), orderNo, user.getRealName(),
				imageUrl + user.getHeadImg(), CONFIRM_ORDER, orderTime, null, null);
	}

	// 咨询师给督导师取消订单消息
	@Async
	public void sendConsultantSupervisorOrderCancelMsg(String orderNo) {

		ConsultantSupervisorOrder consultantSupervisorOrder = supervisorOrderMapper
				.selectOne(new ConsultantSupervisorOrder(orderNo));
		Integer supervisorId = consultantSupervisorOrder.getSupervisorId();
		Integer consultantId = consultantSupervisorOrder.getConsultantId();
		User user = userMapper.selectByPrimaryKey(consultantId);
		user.setHeadImg(user.getHeadImg() + user.getHeadImgSize());
		String orderTime = consultantSupervisorOrder.getPayTime();
		orderTime = DateUtils.StringDate(orderTime);
		User user2 = userMapper.selectByPrimaryKey(supervisorId);
		user2.setHeadImg(user2.getHeadImg() + user2.getHeadImgSize());
		easemob.msgConfirmOrder(user.getEasemobId(), user2.getEasemobId(), orderNo, user.getRealName(),
				imageUrl + user.getHeadImg(), CANCEl_ORDER, orderTime, null, null);
		easemob.msgConfirmOrder(user2.getEasemobId(), user.getEasemobId(), orderNo, user2.getRealName(),
				imageUrl + user2.getHeadImg(), CANCEl_ORDER, orderTime, null, null);

	}

	// 督导师拒绝订单 给咨询师拒绝订单消息
	@Async
	public void sendSupervisorOrderRefuselMsg(String orderNo) {

		ConsultantSupervisorOrder consultantSupervisorOrder = supervisorOrderMapper
				.selectOne(new ConsultantSupervisorOrder(orderNo));
		Integer supervisorId = consultantSupervisorOrder.getSupervisorId();
		Integer consultantId = consultantSupervisorOrder.getConsultantId();
		User user = userMapper.selectByPrimaryKey(supervisorId);
		user.setHeadImg(user.getHeadImg() + user.getHeadImgSize());
		String orderTime = consultantSupervisorOrder.getPayTime();
		orderTime = DateUtils.StringDate(orderTime);
		User user2 = userMapper.selectByPrimaryKey(consultantId);
		user2.setHeadImg(user2.getHeadImg() + user2.getHeadImgSize());
		easemob.msgConfirmOrder(user.getEasemobId(), user2.getEasemobId(), orderNo, user.getRealName(),
				imageUrl + user.getHeadImg(), REFUSR_ORDER, "", null, null);
		easemob.msgConfirmOrder(user2.getEasemobId(), user.getEasemobId(), orderNo, user2.getRealName(),
				imageUrl + user2.getHeadImg(), REFUSR_ORDER, "", null, null);

	}

	// 督导师督导完毕订单
	@Async
	public void sendSupervisorOrderEndlMsg(String orderNo) {

		ConsultantSupervisorOrder consultantSupervisorOrder = supervisorOrderMapper
				.selectOne(new ConsultantSupervisorOrder(orderNo));
		Integer supervisorId = consultantSupervisorOrder.getSupervisorId();

		Integer consultantId = consultantSupervisorOrder.getConsultantId();

		User user = userMapper.selectByPrimaryKey(supervisorId);
		user.setHeadImg(user.getHeadImg() + user.getHeadImgSize());

		String orderTime = consultantSupervisorOrder.getPayTime();
		orderTime = DateUtils.StringDate(orderTime);
		User user2 = userMapper.selectByPrimaryKey(consultantId);
		user2.setHeadImg(user2.getHeadImg() + user2.getHeadImgSize());
		
		easemob.msgConfirmOrder(user2.getEasemobId(), user.getEasemobId(), orderNo, user2.getRealName(),
				imageUrl + user2.getHeadImg(), END_ORDER, "", null, null);
		easemob.msgConfirmOrder(user.getEasemobId(), user2.getEasemobId(), orderNo, user.getRealName(),
				imageUrl + user.getHeadImg(), END_ORDER, "", null, null);
	}

	protected ExtVO createExtVO(String orderNo, Integer id) {
		ExtVO extVO = userMapper.getExtVO(id);
		if (extVO == null) {
			throw new ServiceException(ResultCodeEnum.ERROR_USER_NULL);
		}
		extVO.setServiceOrderid(orderNo);
		extVO.setUserphoto(ImageUtils.getImgagePath(imageUrl, extVO.getUserphoto()));
		extVO.setNickname(extVO.getNickname());
		return extVO;
	}

	protected ExtVO createExtVO(String orderNo, Integer id, String ext) {
		ExtVO extVO = userMapper.getExtVO(id);
		if (extVO == null) {
			throw new ServiceException(ResultCodeEnum.ERROR_USER_NULL);
		}
		extVO.setServiceOrderid(orderNo);
		extVO.setUserphoto(ImageUtils.getImgagePath(imageUrl, extVO.getUserphoto()));
		extVO.setNickname(extVO.getNickname());
		extVO.setServiceTime(ext);
		return extVO;
	}

	// 发送登入消息
	@Async
	public void sendLogin(String easemobId, String content) {

		easemob.sendLogin(easemobId, content, imageUrl + defaultSysHeadImage);
	}

}
