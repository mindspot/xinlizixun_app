package com.kuangji.paopao.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.base.Pager;
import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.constant.CommonConstant;
import com.kuangji.paopao.dto.SupervisorOrderDTO;
import com.kuangji.paopao.dto.param.SupervisorOrderParam;
import com.kuangji.paopao.enums.ConsultantWorOrderEnum;
import com.kuangji.paopao.enums.ConsultantWorkEnum;
import com.kuangji.paopao.enums.OrderCodeEnum;
import com.kuangji.paopao.enums.OrderStatusEnum;
import com.kuangji.paopao.enums.PayStatusEnum;
import com.kuangji.paopao.enums.PointTypeEnum;
import com.kuangji.paopao.mapper.CommonMapper;
import com.kuangji.paopao.mapper.ConsultantSupervisorOrderDetailsMapper;
import com.kuangji.paopao.mapper.ConsultantSupervisorOrderDiagnosisUpdateMapper;
import com.kuangji.paopao.mapper.ConsultantSupervisorOrderMapper;
import com.kuangji.paopao.mapper.MallTradeOrderMapper;
import com.kuangji.paopao.mapper.MemberCaseMapper;
import com.kuangji.paopao.mapper.UserMapper;
import com.kuangji.paopao.model.Common;
import com.kuangji.paopao.model.ConsultantSupervisorOrder;
import com.kuangji.paopao.model.ConsultantSupervisorOrderDetails;
import com.kuangji.paopao.model.ConsultantSupervisorOrderDiagnosisUpdate;
import com.kuangji.paopao.model.MemberCase;
import com.kuangji.paopao.model.User;
import com.kuangji.paopao.redis.AsyncTask;
import com.kuangji.paopao.service.ConsultantSupervisorOrderService;
import com.kuangji.paopao.service.PointHistoryService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;
import com.kuangji.paopao.util.DateUtils;
import com.kuangji.paopao.util.ImageUtils;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.util.PropertiesFileUtil;
import com.kuangji.paopao.util.SerialNumberGenUtils;
import com.kuangji.paopao.vo.CommonVO;
import com.kuangji.paopao.vo.ConsultantMallTradeOrderDetailsVO;
import com.kuangji.paopao.vo.ConsultantOrderDiagnosisVO;
import com.kuangji.paopao.vo.ConsultantSupervisorOrderDetailsVO;
import com.kuangji.paopao.vo.ConsultantSupervisorOrderVO;
import com.kuangji.paopao.vo.ConsultationWorkSupervisorMallTradeOrderVO;
import com.kuangji.paopao.vo.MemberCaseVO;
import com.kuangji.paopao.vo.SupervisorOrderDetailsInfoVO;
import com.kuangji.paopao.vo.SupervisorOrderDetailsVO;
import com.kuangji.paopao.vo.SupervisorOrderVO;

import tk.mybatis.mapper.entity.Example;

@Service
public class ConsultantSupervisorOrderServiceImpl extends BaseServiceImpl<ConsultantSupervisorOrder, Integer>
		implements ConsultantSupervisorOrderService {

	@Autowired
	private ConsultantSupervisorOrderMapper consultantSupervisorOrderMapper;

	@Autowired
	private ConsultantSupervisorOrderDetailsMapper consultantSupervisorOrderDetailsMapper;

	@Autowired
	private MallTradeOrderMapper mallTradeOrderMapper;

	@Autowired
	private UserMapper userMapper;

	private static String imageUrl = PropertiesFileUtil.getInstance().get("image_url");

	@Autowired
	private CommonMapper commonMapper;

	@Autowired
	private MemberCaseMapper memberCaseMapper;
	
	@Autowired
	private PointHistoryService pointHistoryService;

	@Autowired
	private ConsultantSupervisorOrderDiagnosisUpdateMapper consultantSupervisorOrderDiagnosisUpdateMapper;

	private static final Logger LOG = LoggerFactory.getLogger(ConsultantSupervisorOrderServiceImpl.class);

	@Autowired
	private AsyncTask asyncTask;

	// 进行中的订单
	private static Integer[] HAVE_IN_HAND = { OrderStatusEnum.CREATE_SUCC.code, OrderStatusEnum.SEND_SUCC.code };
	// 已经完成
	private static Integer[] END = { OrderStatusEnum.REFUND.code, OrderStatusEnum.CANCEL_BUYER.code,
			OrderStatusEnum.TRADE_SUCC.code };

	@Transactional(rollbackFor = Exception.class)
	@Override
	public int insertSupervisorOrder(SupervisorOrderDTO supervisorOrderDTO, String token) {
		// TODO Auto-generated method stub
		List<String> list = supervisorOrderDTO.getOrderNos();
		Integer supervisorId = supervisorOrderDTO.getConsultantId();

		Integer consultantId = JwtUtils.getUserId(token);

		if (supervisorId.intValue() == consultantId.intValue()) {
			throw new ServiceException(ResultCodeEnum.ERROR_ORDER_BAD_FROM);
		}
		List<ConsultationWorkSupervisorMallTradeOrderVO> orders = mallTradeOrderMapper
				.listMallTradeOrderByOrderNos(list, consultantId);
		if (orders == null || (orders.size() != list.size())) {
			throw new ServiceException(ResultCodeEnum.ERROR_ORDER_FAIL_VUID);
		}

		int amount = orders.stream().mapToInt(ConsultationWorkSupervisorMallTradeOrderVO::getOrderAmount).sum();
		if (amount < 0) {
			throw new ServiceException(ResultCodeEnum.ERROR_PAY_INVALID_FEE);
		}
		int proportion = supervisorOrderDTO.getProportion();
		BigDecimal bigDecimal = new BigDecimal(proportion);
		int scale = 2;// 保留2位小数
		BigDecimal bigDecimalDivide = bigDecimal.divide(new BigDecimal(100), scale, BigDecimal.ROUND_HALF_UP);
		int sumAmount = bigDecimalDivide.multiply(new BigDecimal(amount)).intValue();
		User user = userMapper.getUserforUpdate(consultantId);
		int userAmount = user.getAccount();
		LOG.info("督导订单总金额  " + sumAmount + "  当前账户总金额  " + userAmount);
		if (userAmount < sumAmount) {
			throw new ServiceException(ResultCodeEnum.ERROR_PAY_NO_ACCOUNT);
		}
		// 减去订单金额
		if(userMapper.updateAccount(consultantId, -sumAmount)<1){
			throw new ServiceException(ResultCodeEnum.ERROR_PAY_NO_ACCOUNT);
		}
		ConsultantSupervisorOrder consultantSupervisorOrder = new ConsultantSupervisorOrder();
		String suOrderNo = SerialNumberGenUtils.genSupervisorOrderNo();
		consultantSupervisorOrder.setOrderNo(suOrderNo);
		consultantSupervisorOrder.setGoodsAmount(amount);
		consultantSupervisorOrder.setOrderAmount(sumAmount);
		consultantSupervisorOrder.setProportion(proportion);
		consultantSupervisorOrder.setPayStatus(PayStatusEnum.PAY_SUCC.code);
		consultantSupervisorOrder.setConsultantId(consultantId);
		consultantSupervisorOrder.setSupervisorId(supervisorId);
		consultantSupervisorOrderMapper.insertSelective(consultantSupervisorOrder);
		List<ConsultantSupervisorOrderDetails> details = new ArrayList<ConsultantSupervisorOrderDetails>();
		for (ConsultationWorkSupervisorMallTradeOrderVO vo : orders) {
			ConsultantSupervisorOrderDetails orderDetails = new ConsultantSupervisorOrderDetails();
			orderDetails.setSupervisorOrderNo(suOrderNo);
			orderDetails.setMallTradOrderNo(vo.getOrderNo());
			details.add(orderDetails);
		}
		int result = consultantSupervisorOrderDetailsMapper.insertBatch(details);
		//记录督导单金额变动
		pointHistoryService.superPointHistory(suOrderNo, PointTypeEnum.SUPERVISION_PAYMENT.getType(), userAmount);
		asyncTask.supervisorsOrder(suOrderNo, consultantId, supervisorId);
		return result;
	}

	@Override
	public BaseMapper<ConsultantSupervisorOrder> getMapper() {
		
		return consultantSupervisorOrderMapper;
	}

	@Override
	public int supervisorOrderConfirm(String orderNo, Integer supervisorId) {
		Example example = new Example(ConsultantSupervisorOrder.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("orderNo", orderNo);
		criteria.andEqualTo("supervisorId", supervisorId);
		criteria.andEqualTo("orderStatus", OrderStatusEnum.CREATE_SUCC.code);
		ConsultantSupervisorOrder consultantSupervisorOrder = consultantSupervisorOrderMapper
				.selectOneByExample(example);
		if (consultantSupervisorOrder != null) {
			int result = consultantSupervisorOrderMapper.supervisorOrderConfirm(orderNo, supervisorId);
			asyncTask.sendSupervisorConfirmServiceMsg(orderNo);
			return result;
		}
		throw new ServiceException(ResultCodeEnum.ERROR_ORDER_CONFIRM);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public int supervisorOrderEnd(String orderNo, Integer supervisorId) {
		Example example = new Example(ConsultantSupervisorOrder.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("orderNo", orderNo);
		criteria.andEqualTo("supervisorId", supervisorId);
		ConsultantSupervisorOrder consultantSupervisorOrder = consultantSupervisorOrderMapper
				.selectOneByExample(example);

		if (consultantSupervisorOrder == null) {
			throw new ServiceException(ResultCodeEnum.ERROR_ORDER_BAD_FROM);
		}

		if (consultantSupervisorOrder.getOrderStatus() == OrderStatusEnum.REFUND.code
				|| consultantSupervisorOrder.getOrderStatus() == OrderStatusEnum.CANCEL_BUYER.code) {
			throw new ServiceException(ResultCodeEnum.ERROR_ORDER_CONFIRM);
		}

		if (consultantSupervisorOrder.getOrderStatus() == OrderStatusEnum.TRADE_SUCC.code) {
			throw new ServiceException(ResultCodeEnum.ERROR_ORDER_END);
		}

		if (consultantSupervisorOrderMapper.supervisorOrderEnd(orderNo, supervisorId) < 1) {
			throw new ServiceException(ResultCodeEnum.ERROR_ORDER_CONFIRM);
		}
		User user=userMapper.getUserforUpdate(supervisorId);
		Integer orderAmount = consultantSupervisorOrder.getOrderAmount();
		if (userMapper.updateAccount(supervisorId, orderAmount) < 1) {
			throw new ServiceException(ResultCodeEnum.ERROR_PAY_FAIL);
		}
		pointHistoryService.endSuperPointHistory(orderNo, PointTypeEnum.SUPERVISION_SETTLE.getType(), user.getAccount());
		asyncTask.sendSupervisorOrderEndlMsg(orderNo);
		return 1;
	}

	// 督导师拒绝订单
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int supervisorOrderRefuse(String orderNo, Integer supervisorId) {

		Example example = new Example(ConsultantSupervisorOrder.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("orderNo", orderNo);
		criteria.andEqualTo("supervisorId", supervisorId);
		criteria.andEqualTo("orderStatus", OrderStatusEnum.CREATE_SUCC.code);
		ConsultantSupervisorOrder consultantSupervisorOrder = consultantSupervisorOrderMapper
				.selectOneByExample(example);
		if (consultantSupervisorOrder == null) {
			throw new ServiceException(ResultCodeEnum.ERROR_ORDER_CONFIRM);

		}
		if (consultantSupervisorOrderMapper.supervisorOrderRefuse(orderNo, supervisorId) < 1) {
			throw new ServiceException(ResultCodeEnum.ERROR_ORDER_CONFIRM);
		}

		int orderAmount = consultantSupervisorOrder.getOrderAmount();
		int consultantId = consultantSupervisorOrder.getConsultantId();
		User user=userMapper.getUserforUpdate(consultantId);
		userMapper.updateAccount(consultantId, orderAmount);
		//督导师拒绝订单 记录金额变动
		pointHistoryService.refundSuperPointHistory(orderNo, PointTypeEnum.SUPERVISION_REFUND.getType(), user.getAccount());
		asyncTask.sendSupervisorOrderRefuselMsg(orderNo);
		return 1;
	}

	// 咨询师取消 督导订单
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int supervisorOrderCancel(String orderNo, Integer consultantId) {
		Example example = new Example(ConsultantSupervisorOrder.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("orderNo", orderNo);
		criteria.andEqualTo("consultantId", consultantId);
		ConsultantSupervisorOrder consultantSupervisorOrder = consultantSupervisorOrderMapper
				.selectOneByExample(example);
		if (consultantSupervisorOrder == null) {
			throw new ServiceException(ResultCodeEnum.ERROR_ORDER_BAD_FROM);

		}
		if (consultantSupervisorOrder.getOrderStatus() == OrderStatusEnum.TRADE_SUCC.code) {
			throw new ServiceException(ResultCodeEnum.ERROR_ORDER_END);
		}

		if (consultantSupervisorOrder.getOrderStatus() == OrderStatusEnum.REFUND.code
				|| consultantSupervisorOrder.getOrderStatus() == OrderStatusEnum.CANCEL_BUYER.code) {
			throw new ServiceException(ResultCodeEnum.ERROR_ORDER_CONFIRM);
		}

		if (consultantSupervisorOrderMapper.supervisorOrderCancel(orderNo, consultantId) < 1) {
			throw new ServiceException(ResultCodeEnum.ERROR_ORDER_NOT_EXIST);
		}
		int orderAmount = consultantSupervisorOrder.getOrderAmount();
		User user=userMapper.getUserforUpdate(consultantId);
		userMapper.updateAccount(consultantId, orderAmount);
		//用户取消督导订单
		pointHistoryService.refundSuperPointHistory(orderNo, PointTypeEnum.SUPERVISION_REFUND.getType(), user.getAccount());
		asyncTask.sendConsultantSupervisorOrderCancelMsg(orderNo);
		return 1;
	}

	@Override
	public Pager<List<SupervisorOrderVO>> listOrderBysupervisorId(Integer supervisorId, Integer pageNum, Integer type) {

		Map<String, Object> map = new HashMap<String, Object>();
		Pager<List<SupervisorOrderVO>> pager = new Pager<List<SupervisorOrderVO>>();
		if (type == ConsultantWorOrderEnum.HAVE_IN_HAND.getValue()) {
			pager.setRecordTotal(
					this.consultantSupervisorOrderMapper.countOrderBySupervisorId(supervisorId, HAVE_IN_HAND));
			map.put(CommonConstant.TYPES, HAVE_IN_HAND);
		}
		if (type == ConsultantWorOrderEnum.END.getValue()) {
			pager.setRecordTotal(this.consultantSupervisorOrderMapper.countOrderBySupervisorId(supervisorId, END));
			map.put(CommonConstant.TYPES, END);
		}
		pager.setCurrentPage(pageNum);
		pager.setPageSize(13);
		map.put(CommonConstant.PAGE_NUM, pager.getCurrentPage() * pager.getPageSize() - pager.getPageSize());
		map.put(CommonConstant.PAGE_SIZE, pager.getPageSize());
		map.put(CommonConstant.SUPERVISORID, supervisorId);

		List<SupervisorOrderVO> list = this.consultantSupervisorOrderMapper.listOrderBySupervisorId(map);

		for (SupervisorOrderVO c : list) {
			List<ConsultantSupervisorOrderDetailsVO> vos = c.getListConsultantSupervisorOrderDetailsVO();
			List<String> orderNos = new ArrayList<>();
			Set<String> set = new LinkedHashSet<String>();
			if (vos != null && vos.size() > 0) {
				for (ConsultantSupervisorOrderDetailsVO details : vos) {
					orderNos.add(details.getMallTradOrderNo());
				}
				set = this.listLavels(orderNos);
			}
			c.setLabelVOs(set);
			c.setHeadImg(ImageUtils.getImgagePath(imageUrl, c.getHeadImg()));
			c.setOrderCode(OrderCodeEnum.supervisorOrderCode(c.getOrderStatus()));
		}

		pager.setList(list);
		this.updateReadBySupervisorId(supervisorId);
		return pager;
	}

	//
	@Override
	public Pager<List<SupervisorOrderVO>> listOrderByConsultantId(Integer consultantId, Integer pageNum, Integer type) {

		Map<String, Object> map = new HashMap<String, Object>();
		Pager<List<SupervisorOrderVO>> pager = new Pager<List<SupervisorOrderVO>>();
		if (type == ConsultantWorOrderEnum.HAVE_IN_HAND.getValue()) {
			pager.setRecordTotal(
					this.consultantSupervisorOrderMapper.countOrderByConsultantId(consultantId, HAVE_IN_HAND));
			map.put(CommonConstant.TYPES, HAVE_IN_HAND);
		}
		if (type == ConsultantWorOrderEnum.END.getValue()) {
			pager.setRecordTotal(this.consultantSupervisorOrderMapper.countOrderByConsultantId(consultantId, END));
			map.put(CommonConstant.TYPES, END);
		}
		pager.setCurrentPage(pageNum);
		pager.setPageSize(13);
		map.put(CommonConstant.PAGE_NUM, pager.getCurrentPage() * pager.getPageSize() - pager.getPageSize());
		map.put(CommonConstant.PAGE_SIZE, pager.getPageSize());
		map.put(CommonConstant.CONSULTANTID, consultantId);
		List<SupervisorOrderVO> list = this.consultantSupervisorOrderMapper.listOrderByConsultantId(map);
		for (SupervisorOrderVO c : list) {
			List<ConsultantSupervisorOrderDetailsVO> vos = c.getListConsultantSupervisorOrderDetailsVO();
			List<String> orderNos = new ArrayList<>();
			Set<String> set = new LinkedHashSet<String>();
			if (vos != null && vos.size() > 0) {
				for (ConsultantSupervisorOrderDetailsVO details : vos) {
					orderNos.add(details.getMallTradOrderNo());
				}
				set = this.listLavels(orderNos);
			}
			c.setLabelVOs(set);
			c.setHeadImg(ImageUtils.getImgagePath(imageUrl, c.getHeadImg()));
			c.setOrderCode(OrderCodeEnum.supervisorOrderCode(c.getOrderStatus()));
		}
		pager.setList(list);
		this.updateReadByConsultantId(consultantId);
		return pager;
	}

	@Override
	public SupervisorOrderDetailsInfoVO getLaunchSupervisorOrderDetailsVO(String orderNo) {
		
		this.updateRead(orderNo);
		
		SupervisorOrderDetailsVO vo = this.consultantSupervisorOrderMapper.getLaunchSupervisorOrderDetailsVO(orderNo);
		if (vo == null) {
			throw new ServiceException(ResultCodeEnum.ERROR_ORDER_NOT_EXIST);
		}
		vo.setGoodsName(ConsultantWorkEnum.SET_MEAL.getValue());
		List<ConsultantSupervisorOrderDetailsVO> list = vo.getListConsultantSupervisorOrderDetailsVO();

		List<String> orderNos = new ArrayList<>();
		Set<String> set = new LinkedHashSet<String>();
		List<Map<String, Object>> listMap = new ArrayList<>();
		if (list != null && list.size() > 0) {
			for (ConsultantSupervisorOrderDetailsVO details : list) {
				orderNos.add(details.getMallTradOrderNo());
				Map<String, Object> map = this.getConsultantMallTradeOrderWork(details.getConsultantId(),
						details.getMallTradOrderNo());
				if (map != null) {
					listMap.add(map);
				}

			}
			set = this.listLavels(orderNos);
			vo.setSupervisorOrderNum(list.size());
		}
		vo.setHeadImg(ImageUtils.getImgagePath(imageUrl, vo.getHeadImg()));
		vo.setLabels(set);
		vo.setListSupervisorOrderDetails(listMap);
		vo.setOrderCode(OrderCodeEnum.supervisorOrderCode(vo.getOrderStatus()));
		vo.setOrderTime(DateUtils.StringDateTime(vo.getCreateTime()));

		SupervisorOrderDetailsInfoVO infoVO = new SupervisorOrderDetailsInfoVO();
		BeanUtils.copyProperties(vo, infoVO);

		List<Map<String, Object>> maps = vo.getListSupervisorOrderDetails();

		if (maps != null && !maps.isEmpty()) {
			for (Map<String, Object> map : maps) {
				if (map != null) {
					Object object = map.get("orderInfo");
					if (object != null) {
						ConsultantMallTradeOrderDetailsVO mallTradeOrderVO = (ConsultantMallTradeOrderDetailsVO) object;
						map.put("orderInfo", mallTradeOrderVO.getOrderTime());
					}
				}

			}
		}
		return infoVO;
	}

	@Override
	public SupervisorOrderDetailsInfoVO getSupervisorOrderDetailsVO(String orderNo) {
	
		SupervisorOrderDetailsVO vo = this.consultantSupervisorOrderMapper.getSupervisorOrderDetailsVO(orderNo);

		if (vo == null) {
			throw new ServiceException(ResultCodeEnum.ERROR_ORDER_NOT_EXIST);
		}
		vo.setGoodsName(ConsultantWorkEnum.SET_MEAL.getValue());
		List<ConsultantSupervisorOrderDetailsVO> list = vo.getListConsultantSupervisorOrderDetailsVO();

		List<String> orderNos = new ArrayList<>();
		Set<String> set = new LinkedHashSet<String>();
		List<Map<String, Object>> listMap = new ArrayList<>();
		if (list != null && list.size() > 0) {
			for (ConsultantSupervisorOrderDetailsVO details : list) {
				orderNos.add(details.getMallTradOrderNo());
				Map<String, Object> map = this.getConsultantMallTradeOrderWork(details.getConsultantId(),
						details.getMallTradOrderNo());
				if (map != null) {
					listMap.add(map);
				}
			}
			set = this.listLavels(orderNos);
			vo.setSupervisorOrderNum(list.size());
		}
		vo.setHeadImg(ImageUtils.getImgagePath(imageUrl, vo.getHeadImg()));
		vo.setLabels(set);
		vo.setListSupervisorOrderDetails(listMap);
		vo.setOrderCode(OrderCodeEnum.supervisorOrderCode(vo.getOrderStatus()));
		vo.setOrderTime(DateUtils.StringDateTime(vo.getCreateTime()));

		SupervisorOrderDetailsInfoVO infoVO = new SupervisorOrderDetailsInfoVO();
		BeanUtils.copyProperties(vo, infoVO);

		List<Map<String, Object>> maps = vo.getListSupervisorOrderDetails();

		if (maps != null && !maps.isEmpty()) {
			for (Map<String, Object> map : maps) {
				if (map != null) {
					Object object = map.get("orderInfo");
					if (object != null) {
						ConsultantMallTradeOrderDetailsVO mallTradeOrderVO = (ConsultantMallTradeOrderDetailsVO) object;
						map.put("orderInfo", mallTradeOrderVO.getOrderTime());
					}
				}

			}
		}
		return infoVO;
	}

	protected Set<String> listLavels(List<String> orderNos) {
		Set<String> set = new LinkedHashSet<String>();

		List<String> arr = mallTradeOrderMapper.listConsultantTypeByOrderNos(orderNos);

		List<Integer> ids = new ArrayList<>();
		for (String string : arr) {
			if (StringUtils.isBlank(string)) {
				continue;
			}
			JSONArray jsonArray = JSONObject.parseArray(string);
			for (int i = 0; i < jsonArray.size(); i++) {
				ids.add((Integer) jsonArray.get(i));
			}
		}
		Integer[] idsArr = new Integer[ids.size()];
		if (idsArr.length > 0) {
			List<Common> commons = commonMapper.listCommon(ids.toArray(idsArr));

			for (Common c : commons) {
				set.add(c.getVal());
			}
		}
		return set;
	}

	protected Map<String, Object> getConsultantMallTradeOrderWork(Integer consultantId, String orderNo) {
		Map<String, Object> map = new HashMap<String, Object>();

		ConsultantMallTradeOrderDetailsVO mallTradeOrderVO = mallTradeOrderMapper
				.getConsultantMallTradeOrderDetailsVO(consultantId, orderNo);
		if (mallTradeOrderVO == null) {
			return null;
		}

		mallTradeOrderVO.setHeadImg(ImageUtils.getImgagePath(imageUrl, mallTradeOrderVO.getHeadImg()));
		int code = OrderCodeEnum.mallOrderCode(mallTradeOrderVO.getPayStatus(), mallTradeOrderVO.getOrderStatus());
		mallTradeOrderVO.setOrderCode(code);
		JSONObject jsonObject = JSONObject.parseObject(mallTradeOrderVO.getExt());
		String consultantWorkDate = jsonObject.getString("consultantWorkDate");
		String consultantWorkTime = jsonObject.getString("consultantWorkTime");
		String ext = consultantWorkDate + " " + consultantWorkTime;
		mallTradeOrderVO.setExt(ext);
		map.put(CommonConstant.ORDER_INFO, mallTradeOrderVO);

		Integer id = mallTradeOrderVO.getMemberCaseId();
		MemberCaseVO memberCaseVO = new MemberCaseVO();
		MemberCase memberCase = memberCaseMapper.get(id);
		BeanUtils.copyProperties(memberCase, memberCaseVO);
		String types = memberCase.getConsultantType();
		if (StringUtils.isNotBlank(types)) {
			JSONArray jsonArray = JSONObject.parseArray(types);
			Integer[] ids = new Integer[jsonArray.size()];
			for (int i = 0; i < jsonArray.size(); i++) {
				ids[i] = (Integer) jsonArray.get(i);
			}

			List<Common> commons = commonMapper.listCommon(ids);
			List<CommonVO> lVos = this.listTypes(commons);
			memberCaseVO.setCommonVOs(lVos);
		}
		User user = userMapper.get(consultantId);
		memberCaseVO.setConsultatName(user.getRealName());
		Example example = new Example(ConsultantSupervisorOrderDiagnosisUpdate.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("orderNo", mallTradeOrderVO.getOrderNo());
		// 病例修改时间
		ConsultantSupervisorOrderDiagnosisUpdate update = consultantSupervisorOrderDiagnosisUpdateMapper
				.selectOneByExample(example);
		// 获取所有的病例 图片文字
		List<ConsultantOrderDiagnosisVO> diagnosisVOs = mallTradeOrderMapper.listConsultantMallTradeDiagnosis(orderNo,
				consultantId);
		// 分离出图片
		List<ConsultantOrderDiagnosisVO> imgs = diagnosisVOs.stream().filter(c -> c.getContentType() == 1)
				.collect(Collectors.toList());
		for (ConsultantOrderDiagnosisVO c : imgs) {
			c.setContent(ImageUtils.getImgagePath(imageUrl, c.getContent()) + CommonConstant.IMG_SIZE);
			c.setImgeSize(CommonConstant.IMG_SIZE);
		}
		memberCaseVO.setConsultantOrderDiagnosisVOs(imgs);

		// 分离出文字
		long count = diagnosisVOs.stream().filter(c -> c.getContentType() == 0).count();
		if (count != 0) {
			ConsultantOrderDiagnosisVO vo = diagnosisVOs.stream().filter(c -> c.getContentType() == 0).findFirst()
					.get();
			vo.setImgeSize("");
			memberCaseVO.setConsultantOrderDiagnosisVO(vo);
		}

		map.put(CommonConstant.MEMBER_CASE, memberCaseVO);
		if (update == null) {
			map.put(CommonConstant.ISDIAGNOSIS, false);
			return map;
		}
		map.put(CommonConstant.ISDIAGNOSIS, true);
		String strDateFormat = "yyyy-MM-dd";
		String date = DateUtils.formatDate(update.getDiagnosisTime(), strDateFormat);
		memberCaseVO.setConsultationTime(date);

		return map;
	}

	@Override
	public List<ConsultantSupervisorOrderVO> listOrder(String token, Integer consultantId) {
		Integer supervisorId = JwtUtils.getUserId(token);
		List<ConsultantSupervisorOrderVO> list = this.consultantSupervisorOrderMapper.listOrder(consultantId,
				supervisorId);

		for (ConsultantSupervisorOrderVO vo : list) {
			vo.setHeadImg(ImageUtils.getImgagePath(imageUrl, vo.getHeadImg()));
			int code = OrderCodeEnum.mallOrderCode(vo.getPayStatus(), vo.getOrderStatus());
			vo.setOrderCode(code);

			List<ConsultantSupervisorOrderDetails> details = this.listSupervisorOrderDetails(vo.getOrderNo());
			List<String> stringList = new ArrayList<>();
			for (ConsultantSupervisorOrderDetails orderDetails : details) {
				stringList.add(orderDetails.getMallTradOrderNo());
			}

			List<String> tList = mallTradeOrderMapper.listConsultantTypeByOrderNos(stringList);
			for (String types : tList) {
				if (types != null) {
					if (StringUtils.isNotBlank(types)) {
						JSONArray jsonArray = JSONObject.parseArray(types);
						Integer[] ids = new Integer[jsonArray.size()];
						for (int i = 0; i < jsonArray.size(); i++) {
							ids[i] = (Integer) jsonArray.get(i);
						}
						List<Common> commons = commonMapper.listCommon(ids);
						List<CommonVO> lVos = this.listTypes(commons);

						Set<String> labels = new LinkedHashSet<String>();
						for (CommonVO c : lVos) {
							labels.add(c.getVal());
						}
						vo.setLabelVOs(labels);
					}
				}
			}
		}

		return list;
	}

	@Override
	public List<com.kuangji.paopao.admin.vo.SupervisorOrderVO> listSupervisorOrder(SupervisorOrderParam param) {
		PageHelper.startPage(param.getPageNo(), param.getPageSize());
		List<com.kuangji.paopao.admin.vo.SupervisorOrderVO> list = this.consultantSupervisorOrderMapper
				.listSupervisorOrder(param);
		return list;
	}

	@Override
	public int unReadOrderNumByConsultantId(Integer userId) {
		Example example = new Example(ConsultantSupervisorOrder.class);
		example.createCriteria().andEqualTo("consultantId", userId).andEqualTo("isRead", 0);
		;
		int unReadNum = consultantSupervisorOrderMapper.selectCountByExample(example);
		return unReadNum;
	}

	@Override
	public int unReadOrderNumBySupervisorId(Integer userId) {

		Example example = new Example(ConsultantSupervisorOrder.class);
		example.createCriteria().andEqualTo("supervisorId", userId).andEqualTo("isRead", 0);
		int unReadNum = consultantSupervisorOrderMapper.selectCountByExample(example);
		return unReadNum;
	}

	@Override
	public int updateReadByConsultantId(Integer userId) {
		Example example = new Example(ConsultantSupervisorOrder.class);
		example.createCriteria().andEqualTo("consultantId", userId).andEqualTo("isRead", 0);
		ConsultantSupervisorOrder consultantSupervisorOrder = new ConsultantSupervisorOrder();
		consultantSupervisorOrder.setIsRead(1);
		return consultantSupervisorOrderMapper.updateByExampleSelective(consultantSupervisorOrder, example);
	}

	@Override
	public int updateReadBySupervisorId(Integer userId) {

		Example example = new Example(ConsultantSupervisorOrder.class);
		example.createCriteria().andEqualTo("supervisorId", userId).andEqualTo("isRead", 0);
		ConsultantSupervisorOrder consultantSupervisorOrder = new ConsultantSupervisorOrder();
		consultantSupervisorOrder.setIsRead(1);
		return consultantSupervisorOrderMapper.updateByExampleSelective(consultantSupervisorOrder, example);
	}

	protected List<CommonVO> listTypes(List<Common> commons) {

		List<CommonVO> lVos = new ArrayList<CommonVO>();
		for (int i = 0; i < commons.size(); i++) {
			CommonVO commonVO = new CommonVO();
			commonVO.setVal(commons.get(i).getVal());
			lVos.add(commonVO);
		}
		return lVos;

	}

	protected List<ConsultantSupervisorOrderDetails> listSupervisorOrderDetails(String orderNo) {
		Example example = new Example(ConsultantSupervisorOrderDetails.class);
		example.createCriteria().andEqualTo("supervisorOrderNo", orderNo);
		List<ConsultantSupervisorOrderDetails> details = consultantSupervisorOrderDetailsMapper
				.selectByExample(example);
		return details;
	}

	@Override
	public int updateRead(String orderNo) {
		Example example = new Example(ConsultantSupervisorOrder.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("orderNo", orderNo);
		ConsultantSupervisorOrder consultantSupervisorOrder=consultantSupervisorOrderMapper.selectOneByExample(example);
		ConsultantSupervisorOrder order=new ConsultantSupervisorOrder();
		order.setId(consultantSupervisorOrder.getId());
		order.setIsRead(1);
		return consultantSupervisorOrderMapper.updateByPrimaryKeySelective(order);
	}
}
