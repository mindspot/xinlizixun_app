package com.kuangji.paopao.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.kuangji.paopao.api.ConsultationApi;
import com.kuangji.paopao.api.MallTradeOrderApi;
import com.kuangji.paopao.base.Pager;
import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.constant.CommonConstant;
import com.kuangji.paopao.dto.ConsultantOrderDiagnosisDTO;
import com.kuangji.paopao.dto.ConsultationDTO;
import com.kuangji.paopao.dto.SupervisorOrderDTO;
import com.kuangji.paopao.dto.param.VisitorParam;
import com.kuangji.paopao.enums.CommonEnum;
import com.kuangji.paopao.enums.ConsultantWorOrderEnum;
import com.kuangji.paopao.enums.ConsultedEnum;
import com.kuangji.paopao.enums.SexEnum;
import com.kuangji.paopao.enums.UserCardEnum;
import com.kuangji.paopao.model.Common;
import com.kuangji.paopao.model.ConsultantSupervisorOrder;
import com.kuangji.paopao.model.ConsultantSupervisorOrderDiagnosis;
import com.kuangji.paopao.model.User;
import com.kuangji.paopao.model.UserCard;
import com.kuangji.paopao.model.UserCashWithdrawal;
import com.kuangji.paopao.redis.send.SendService;
import com.kuangji.paopao.service.CommonService;
import com.kuangji.paopao.service.ConsultantOrderDiagnosisService;
import com.kuangji.paopao.service.ConsultantService;
import com.kuangji.paopao.service.ConsultantSupervisorOrderDiagnosisService;
import com.kuangji.paopao.service.ConsultantSupervisorOrderService;
import com.kuangji.paopao.service.ConsultationSetMealOrderService;
import com.kuangji.paopao.service.MallTradeOrderService;
import com.kuangji.paopao.service.UserCardService;
import com.kuangji.paopao.service.UserCashWithdrawalService;
import com.kuangji.paopao.service.UserService;
import com.kuangji.paopao.service.VisitorService;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.util.PropertiesFileUtil;
import com.kuangji.paopao.util.ServiceResultUtils;
import com.kuangji.paopao.vo.ConsultantMallTradeOrderVO;
import com.kuangji.paopao.vo.ConsultantSupervisorOrderVO;
import com.kuangji.paopao.vo.ConsultantUpdateVO;
import com.kuangji.paopao.vo.ConsultantVO;
import com.kuangji.paopao.vo.ConsultantWorkMemberMallTradeOrderVO;
import com.kuangji.paopao.vo.ReadOrderNumVO;
import com.kuangji.paopao.vo.SetMealVO;
import com.kuangji.paopao.vo.SupervisorOrderDetailsInfoVO;
import com.kuangji.paopao.vo.SupervisorOrderVO;
import com.kuangji.paopao.vo.UserCardVO;

/**
 * Author 金威正 Date 2020-02-16 咨询工作平台
 */
@RestController
public class ConsultantWorkController {

	@Autowired
	private MallTradeOrderApi mallTradeOrderApi;

	@Autowired
	private MallTradeOrderService mallTradeOrderService;

	@Autowired
	private ConsultationSetMealOrderService consultationSetMealOrderService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private ConsultantOrderDiagnosisService consultantOrderDiagnosisService;

	@Autowired
	private ConsultationApi consultationApi;

	@Autowired
	private ConsultantSupervisorOrderService consultantSupervisorOrderService;

	@Autowired
	private ConsultantSupervisorOrderDiagnosisService consultantSupervisorOrderDiagnosisService;

	@Autowired
	private ConsultantService consultantService;

	@Autowired
	private UserCardService userCardService;

	@Autowired
	private UserCashWithdrawalService userCashWithdrawalService;

	@Autowired
	private SendService sendService;
	
	@Autowired
	private CommonService commonService;
	

	@Autowired
	private VisitorService  visitorService;

	// 环信 用户 标志
	public static final String member = PropertiesFileUtil.getInstance().get("easemob_member");

	// 环信 咨询师
	public static final String consultant = PropertiesFileUtil.getInstance().get("easemob_consultant");

	private static final Logger LOG = LoggerFactory.getLogger(ConsultantWorkController.class);
	
	
	//咨询师端登入
	@PostMapping(value = "/consultant/phoneLogin")
	public Object phoneLogin(@RequestParam(defaultValue = "") String phone,
			@RequestParam(defaultValue = "") String code) {
		if (StringUtils.isBlank(phone)) {
			throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_EMP_USER_NAME);
		}
		Map<String, Object> map = userService.consultantPhoneLoginVO(phone, code);

		return ServiceResultUtils.success(map);
	}


	// 获取未修改订单的数量
	@PostMapping("/v1/consultantWork/orderUnReadNum")
	public Object orderReadNum(HttpServletRequest request) {
		String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer userId = JwtUtils.getUserId(token);
		ReadOrderNumVO vo = mallTradeOrderService.unReadOrderNumVO(userId);
		return ServiceResultUtils.success(vo);
	}


	// 正在进行中的订单 0进行中 1已完结
	@PostMapping("/v1/consultantWork/order")
	public Object consultantWorOrder(Integer pageNum, int type, HttpServletRequest request) {
		if (pageNum == null || pageNum < 1) {
			return ServiceResultUtils.success(null);
		}
		String token = request.getHeader("authorization").replace("Bearer ", "");
		if (type == ConsultantWorOrderEnum.HAVE_IN_HAND.getValue()) {
			PageInfo<ConsultantMallTradeOrderVO> pageList = mallTradeOrderApi.pageListConsultantMallTradeOrderWork(token, pageNum);
			return ServiceResultUtils.success(pageList);
		}
		PageInfo<ConsultantMallTradeOrderVO> pageList = mallTradeOrderApi.listConsultantMallTradeOrderWorkEnd(token,pageNum);
		return ServiceResultUtils.success(pageList);

	}

	// 咨询师端订单详情
	@PostMapping("/v1/consultantWork/orderDetails")
	public Object consultantWorOrderDetails(String orderNo, HttpServletRequest request) {
		if (StringUtils.isBlank(orderNo)) {
			return ServiceResultUtils.success(null);
		}
		String token = request.getHeader("authorization").replace("Bearer ", "");
		Map<String, Object> map = mallTradeOrderApi.getConsultantMallTradeOrderWork(token, orderNo);
		return ServiceResultUtils.success(map);
	}

	// 咨询师端 套餐情况
	@PostMapping("/v1/consultantWork/setMeal")
	public Object consultantWorkSetMeal(HttpServletRequest request, Integer pageNum) {
		if (pageNum == null || pageNum < 1) {
			return ServiceResultUtils.success(null);
		}
		String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer consultantId = JwtUtils.getUserId(token);
		PageInfo<SetMealVO> setMealVOs = consultationSetMealOrderService.listConsultantSetMeal(consultantId, pageNum);
		return ServiceResultUtils.success(setMealVOs);
	}

	// 咨询师端 确认订单
	@PostMapping("/v1/consultantWork/confirm")
	public Object confirm(HttpServletRequest request, String orderNo) {
		if (StringUtils.isBlank(orderNo)) {
			return ServiceResultUtils.failure(null);
		}
		String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer consultantId = JwtUtils.getUserId(token);
		
		if (mallTradeOrderService.updateOrderConfirm(consultantId, orderNo) < 1) {
			return ServiceResultUtils.failure(null);
		}
	
		return ServiceResultUtils.success(null);
	}

	// 咨询师端 修改订单详情
	@PostMapping("/v1/consultantOrderDiagnosis/update")
	public Object consultantOrderDiagnosisUpdate(@RequestBody ConsultantOrderDiagnosisDTO dto) {

		if (StringUtils.isBlank(dto.getOrderNo())) {
			return ServiceResultUtils.failure(null);
		}
		consultantOrderDiagnosisService.updateMemberCaseDiagnosis(dto);
		return ServiceResultUtils.success(null);
	}

	// 咨询师端 督导师列表
	@PostMapping("/v1/consultantWork/consultantList")
	public Object consultantList(HttpServletRequest request, @RequestBody ConsultationDTO consultationDTO) {
		String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer consultantId = JwtUtils.getUserId(token);
		if (consultationDTO.getPageNum() < 1) {
			return ServiceResultUtils.success(null);
		}
		consultationDTO.setConsultantId(consultantId);
		Pager<List<ConsultantVO>> pager = userService.pageWorkListConsultant(consultationDTO.getPageNum(),
				consultationDTO);

		return ServiceResultUtils.success(pager);
	}

	// 咨询师端 督导师 详细信息
	@GetMapping("/v1/consultationWork/get/{id}")
	public Object get(@PathVariable("id") int id, HttpServletRequest request) {
		String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer consultatId = JwtUtils.getUserId(token);
		if (id == consultatId.intValue()) {
			return ServiceResultUtils.success(null);
		}
		
		Map<String, Object> aboutConsultantVOs = consultationApi.getConsultation(consultatId, id,CommonEnum.SUPERVISOR_SERVICE_NOTICE_TYPE.getVal());
		aboutConsultantVOs.remove("workingMode");
		aboutConsultantVOs.remove("paySetMeal");
		return ServiceResultUtils.success(aboutConsultantVOs);
	}

	// 可督导列表
	@PostMapping("/v1/consultationWork/order/list")
	public Object listConsultationWorkOrder(HttpServletRequest request, Integer pageNum) {
		if (pageNum == null || pageNum < 1) {
			return ServiceResultUtils.success(null);
		}
		String token = request.getHeader("authorization").replace("Bearer ", "");

		PageInfo<ConsultantWorkMemberMallTradeOrderVO> lVos = mallTradeOrderApi.pageListConsultantWorkMemberMallTradeOrder(token, pageNum);
		return ServiceResultUtils.success(lVos);
	}

	// 咨询师端 根据用户环信id 解析出该用户 在该咨询师已 确认的订单
	@PostMapping("/v1/consultationWork/easemobs/order/list")
	public Object listConsultationWorkOrderEasemobs(HttpServletRequest request, String easemobsId) {

		if (StringUtils.isBlank(easemobsId)) {
			return ServiceResultUtils.success(null);
		}
		String token = request.getHeader("authorization").replace("Bearer ", "");
		
		Map<String, Object> orderMap=new HashMap<String, Object>(2);
		//1代码 客户与咨询师订单 
		orderMap.put(CommonConstant.IS_CONSULTANT,1);
		// 解析 客户对咨询师咨询订单详情
		if (easemobsId.startsWith(member)) {
			Integer memberId = 0;
			try {
				memberId = Integer.valueOf(easemobsId.replace(member, ""));
			} catch (Exception e) {
				throw new ServiceException(ResultCodeEnum.FAIL);
			}
			List<Map<String, Object>> listConsultantMallTradeOrderWork = mallTradeOrderApi.listConsultantMallTradeOrderWork(token, memberId);
			orderMap.put(CommonConstant.EASEMOB_ORDER_LIST, listConsultantMallTradeOrderWork);
			return ServiceResultUtils.success(orderMap);
		}
		
		Integer consultantId = 0;
		try {
			consultantId = Integer.valueOf(easemobsId.replace(consultant, ""));
		} catch (Exception e) {
			throw new ServiceException(ResultCodeEnum.FAIL);
		}
		//0代码 咨询师与督导订单 
		orderMap.put(CommonConstant.IS_CONSULTANT,0);
		
		List<ConsultantSupervisorOrderVO> listOrder = consultantSupervisorOrderService.listOrder(token, consultantId);
		orderMap.put(CommonConstant.EASEMOB_ORDER_LIST, listOrder);
		return ServiceResultUtils.success(orderMap);
	}

	// 下督导订单
	@PostMapping("/v1/consultationWork/dealUnifiedSupervisorOrder")
	public Object dealUnifiedSupervisorOrder(HttpServletRequest request,
			@RequestBody SupervisorOrderDTO supervisorOrderDTO) {

		String token = request.getHeader("authorization").replace("Bearer ", "");
		if (supervisorOrderDTO.getConsultantId() == null) {
			return ServiceResultUtils.failure("-1", "督导id不能为空");
		}

		List<String> orderNos = supervisorOrderDTO.getOrderNos();
		if (orderNos == null || orderNos.size() > 3) {
			return ServiceResultUtils.failure("-1", "可督导案例不能多于3个");

		}
		Integer proportion = supervisorOrderDTO.getProportion();
		if (proportion == null || proportion >= 100) {
			return ServiceResultUtils.failure("-1", "抽佣错误");

		}
		consultantSupervisorOrderService.insertSupervisorOrder(supervisorOrderDTO, token);
		return ServiceResultUtils.success(null);

	}

	// 咨询师端 督导师确认订单
	@PostMapping("/v1/consultationWork/supervisorOrder/confirm")
	public Object supervisorOrder(HttpServletRequest request, String orderNo, Integer type) {
		if (type == null) {
			return ServiceResultUtils.failure(null);
		}
		String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer supervisorId = JwtUtils.getUserId(token);
		// 1 接受订单
		if (type == ConsultedEnum.CONSULTED_NO.getValue()) {
			if (consultantSupervisorOrderService.supervisorOrderConfirm(orderNo, supervisorId) > 0) {
				return ServiceResultUtils.success(null);
			}
		}
		// 拒绝订单
		if (type == ConsultedEnum.CONSULTED_YES.getValue()) {
			if (consultantSupervisorOrderService.supervisorOrderRefuse(orderNo, supervisorId) > 0) {
				return ServiceResultUtils.success(null);
			}
		}
		return ServiceResultUtils.failure(null);
	}

	// 咨询师端 督导师完成订单
	@PostMapping("/v1/consultationWork/supervisorOrder/end")
	public Object supervisorOrderEnd(HttpServletRequest request, String orderNo) {

		if (StringUtils.isBlank(orderNo)) {
			return ServiceResultUtils.failure("-1", "订单号不能为空");
		}
		String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer supervisorId = JwtUtils.getUserId(token);
		if (consultantSupervisorOrderService.supervisorOrderEnd(orderNo, supervisorId) > 0) {
			return ServiceResultUtils.success(null);
		}
		return ServiceResultUtils.failure(null);
	}

	// 咨询师端 咨询师取消督导订单
	@PostMapping("/v1/consultationWork/supervisorOrder/cancel")
	public Object supervisorOrderCancel(HttpServletRequest request, String orderNo) {

		if (StringUtils.isBlank(orderNo)) {
			return ServiceResultUtils.failure("-1", "订单号不能为空");
		}
		String token = request.getHeader("authorization").replace("Bearer ", "");

		Integer consultantId = JwtUtils.getUserId(token);

		if (consultantSupervisorOrderService.supervisorOrderCancel(orderNo, consultantId) > 0) {
			return ServiceResultUtils.success(null);
		}
		return ServiceResultUtils.failure(null);
	}

	// 咨询师端 咨询师发起的督导师订单
	@PostMapping("/v1/consultationWork/consultant/supervisorOrder")
	public Object consultantSupervisorOrder(HttpServletRequest request, Integer pageNum, Integer type) {
		if (pageNum == null || pageNum < 1) {
			return ServiceResultUtils.success(null);
		}
		if (type == null) {
			return ServiceResultUtils.success(null);
		}

		String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer consultantId = JwtUtils.getUserId(token);
		Pager<List<SupervisorOrderVO>> pager = consultantSupervisorOrderService.listOrderByConsultantId(consultantId,pageNum, type);
		return ServiceResultUtils.success(pager);
	}

	// 咨询师端 我收到督导的订单
	@PostMapping("/v1/consultationWork/supervisor/supervisorOrder")
	public Object supervisorOrderSupervisorOrder(HttpServletRequest request, Integer pageNum, Integer type) {
		if (pageNum == null || pageNum < 1) {
			return ServiceResultUtils.success(null);
		}
		if (type == null) {
			return ServiceResultUtils.success(null);
		}
		String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer supervisorId = JwtUtils.getUserId(token);
		Pager<List<SupervisorOrderVO>> pager = consultantSupervisorOrderService.listOrderBysupervisorId(supervisorId,pageNum, type);
		return ServiceResultUtils.success(pager);
	}

	// 我收到的督导师 订单详情
	@PostMapping("/v1/consultationWork/supervisor/receivedDetails")
	public Object receivedSupervisorOrderDetails(HttpServletRequest request, String orderNo) {
		if (StringUtils.isBlank(orderNo)) {
			return ServiceResultUtils.failure("-1", "订单号不能为空");
		}
		SupervisorOrderDetailsInfoVO vo = consultantSupervisorOrderService.getLaunchSupervisorOrderDetailsVO(orderNo);


		return ServiceResultUtils.success(vo);
	}

	// 我发起的督导师 订单详情
	@PostMapping("/v1/consultationWork/supervisor/launchDetails")
	public Object launchSupervisorOrderDetails(HttpServletRequest request, String orderNo) {
		if (StringUtils.isBlank(orderNo)) {
			return ServiceResultUtils.failure("-1", "订单号不能为空");
		}

		SupervisorOrderDetailsInfoVO vo = consultantSupervisorOrderService.getSupervisorOrderDetailsVO(orderNo);

		return ServiceResultUtils.success(vo);
	}

	// 咨询师师 对督导服务的评价
	@PostMapping("/v1/consultationWork/supervisor/diagnosis")
	public Object supervisorDiagnosis(HttpServletRequest request, String orderNo, String content) {
		String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer consultantId = JwtUtils.getUserId(token);
		if (StringUtils.isBlank(orderNo)) {
			return ServiceResultUtils.failure("-1", "订单号不能为空");
		}

		if (StringUtils.isBlank(content)) {
			return ServiceResultUtils.failure("-1", "内容不能为空");
		}
		ConsultantSupervisorOrder order=new ConsultantSupervisorOrder();
		order.setConsultantId(consultantId);
		order.setOrderNo(orderNo);
		ConsultantSupervisorOrder consultantSupervisorOrder = consultantSupervisorOrderService.findOne(order);
		if (consultantSupervisorOrder == null) {
			return ServiceResultUtils.failure("-1", "没有此订单");
		}
		ConsultantSupervisorOrderDiagnosis diagnosis = consultantSupervisorOrderDiagnosisService
				.findOne(new ConsultantSupervisorOrderDiagnosis(orderNo));
		if (diagnosis == null) {
			diagnosis = new ConsultantSupervisorOrderDiagnosis();
			diagnosis.setContent(content);
			diagnosis.setOrderNo(orderNo);
			consultantSupervisorOrderDiagnosisService.add(diagnosis);
			return ServiceResultUtils.success(null);
		}
		diagnosis.setContent(content);
		diagnosis.setOrderNo(orderNo);
		return ServiceResultUtils.success(null);
	}

	// 督导师 我的页面
	@PostMapping("/v1/consultationWork/consultant/get")
	public Object consultant(HttpServletRequest request) {
		String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer consultantId = JwtUtils.getUserId(token);
		ConsultantUpdateVO vo = consultantService.getConsultantUpdateVO(consultantId);
		return ServiceResultUtils.success(vo);
	}

	// 督导师 我的页面 修改
	@PostMapping("/v1/consultationWork/consultant/update")
	public Object consultantUpdate(HttpServletRequest request, ConsultantUpdateVO consultantUpdateVO) {
		String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer consultantId = JwtUtils.getUserId(token);
		Integer sex = consultantUpdateVO.getSex();
		if (sex != null) {
			if (!SexEnum.isInclude(sex)) {
				return ServiceResultUtils.failure(null);
			}
		
		}
		consultantService.updateConsultantVO(consultantId, consultantUpdateVO);
		return ServiceResultUtils.success(null);
	}

	// 督导师 绑卡
	@PostMapping("/v1/consultationWork/card/insert")
	public Object cardInsert(HttpServletRequest request, String alipayAccount, String realName, Integer type) {
		String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer consultantId = JwtUtils.getUserId(token);
		if (type == null) {
			return ServiceResultUtils.failure("-1", "请选择新增还是修改绑定卡");
		}
		if (!UserCardEnum.isInclude(type)) {
			return ServiceResultUtils.failure("-1", "选择方式出错");
		}
		if (StringUtils.isBlank(alipayAccount)) {
			return ServiceResultUtils.failure("-1", "支付宝账户不能为空");
		}
		if (StringUtils.isBlank(realName)) {
			return ServiceResultUtils.failure("-1", "姓名不能为空");
		}

		UserCard userCard = userCardService.findOne(new UserCard(consultantId, 0));
		if (type == UserCardEnum.INSERT.getIndex()) {
			if (userCard != null) {
				return ServiceResultUtils.failure("-1", "已经绑定过支付宝,无法新增");
			}
			userCardService.add(new UserCard(consultantId, realName, alipayAccount));
			return ServiceResultUtils.success(null);
		}
		if (type == UserCardEnum.UPDATE.getIndex()) {
			if (userCard == null) {
				return ServiceResultUtils.failure("-1", "没有绑定过支付宝,无法修改");
			}
			userCardService.updateByPrimaryKeySelective(
					(new UserCard(userCard.getId(), consultantId, realName, alipayAccount)));
			return ServiceResultUtils.success(null);
		}
		return ServiceResultUtils.failure("-9", "操作失败");
	}

	// 督导师 获取吧绑卡列表
	@PostMapping("/v1/consultationWork/card/list")
	public Object cardList(HttpServletRequest request) {
		String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer consultantId = JwtUtils.getUserId(token);
		List<UserCardVO> userCardVOs = userCashWithdrawalService.cardList(consultantId);
		return ServiceResultUtils.success(userCardVOs);

	}

	// 督导师 提现
	@PostMapping("/v1/consultationWork/cashWithdrawal/insert")
	public Object cashWithdrawal(HttpServletRequest request, Integer amount, String code) {

		if (amount == null || amount.intValue() <= 0) {
			return ServiceResultUtils.failure("-1", "提现金额错误");
		}

		String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer consultantId = JwtUtils.getUserId(token);
		userCashWithdrawalService.cashWithdrawal(consultantId, amount, code);
		return ServiceResultUtils.success(null);
	}

	// 督导师 提现短信码
	@PostMapping("/v1/consultationWork/cashWithdrawal/getCode")
	public Object cashWithdrawalGetCode(HttpServletRequest request) {

		String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer consultantId = JwtUtils.getUserId(token);
		User user = userService.findById(consultantId);
		String phone = user.getUserName();
		sendService.sendCashWithdrawalMsg(phone);
		return ServiceResultUtils.success(null);
	}

	// 督导师 提现列表
	@PostMapping("/v1/consultationWork/cashWithdrawal/list")
	public Object cashWithdrawal(HttpServletRequest request, Integer pageNum) {
		if (pageNum == null || pageNum < 1) {
			return ServiceResultUtils.success(null);
		}
		String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer consultantId = JwtUtils.getUserId(token);
		List<UserCashWithdrawal> list = userCashWithdrawalService.pagelist(consultantId, pageNum);
		return ServiceResultUtils.success(list);
	}

	// 获取用户提现额度
	@PostMapping("/v1/consultationWork/user/account")
	public Object account(HttpServletRequest request) {
		Map<String, Object> map=new HashMap<>();
		String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer consultantId = JwtUtils.getUserId(token);
		User user = userService.getUserforUpdate(consultantId);
		Integer account = user.getAccount();
		Common common=commonService.getCommonByType(CommonEnum.CASH_WITHDRAWAL.getVal());
		map.put(CommonConstant.ACCOUNT, account);
		map.put(CommonConstant.CASH_WITHDRAWAL_REMARKS, "");
		if (common!=null) {
			map.put(CommonConstant.CASH_WITHDRAWAL_REMARKS, common.getVal());
		}
		return ServiceResultUtils.success(map);
	}

	// 咨询师端 咨询师个人信息
	@PostMapping("/v1/consultationWork/getConsultant")
	public Object getConsultant(HttpServletRequest request) {

		String token = request.getHeader("authorization").replace("Bearer ", "");
		Map<String, Object> map = consultationApi.getConsultant(token);
		return ServiceResultUtils.success(map);
	}
	//咨询师端登入获取短信验证
	@PostMapping(value = "/consultantWork/get/code")
	public Object getCode(@RequestParam(defaultValue = "") String phone) {
		
		String repo=sendService.sendConsultationWorkMsg(phone);
		if (repo.length()>2) {
			return ServiceResultUtils.success(null);
		}
		return ServiceResultUtils.failure(ResultCodeEnum.ERROR_LOGIN_CODE);
	}
	
	//咨询师端入驻获取短信验证
	@PostMapping(value = "/settledInLogin/get/code")
	public Object settledInLoginGetCode(@RequestParam(defaultValue = "") String phone) {
		String repo=sendService.sendsettledInLoginMsg(phone);
		if (repo.length()>2) {
			return ServiceResultUtils.success(null);
		}
		return ServiceResultUtils.failure(ResultCodeEnum.ERROR_LOGIN_CODE);
	}
	
	
	//咨询师端 访客记录表
	@GetMapping(value = "/v1/visitor/list")
	public Object visitorList(HttpServletRequest request,VisitorParam param) {
		String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer shopId=JwtUtils.getUserId(token);
		param.setShopId(shopId);
		return ServiceResultUtils.success(visitorService.listVisitorVO(param));
	}
}
