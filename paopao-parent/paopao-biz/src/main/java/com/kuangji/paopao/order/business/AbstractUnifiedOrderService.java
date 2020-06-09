package com.kuangji.paopao.order.business;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.kuangji.paopao.model.consultant.Coupon;
import com.kuangji.paopao.service.CouponService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.constant.CommonConstant;
import com.kuangji.paopao.enums.OrderFromEnum;
import com.kuangji.paopao.enums.OrderStatusEnum;
import com.kuangji.paopao.enums.PayStatusEnum;
import com.kuangji.paopao.enums.PayTypeEnum;
import com.kuangji.paopao.mapper.CouponMapper;
import com.kuangji.paopao.mapper.MallTradeOrderDiscountMapper;
import com.kuangji.paopao.mapper.MallTradeOrderGoodsMapper;
import com.kuangji.paopao.mapper.MallTradeOrderMapper;
import com.kuangji.paopao.mapper.MemberCaseMapper;
import com.kuangji.paopao.model.MallTradeOrder;
import com.kuangji.paopao.model.MallTradeOrderDiscount;
import com.kuangji.paopao.model.MallTradeOrderGoods;
import com.kuangji.paopao.model.MemberCase;
import com.kuangji.paopao.order.dto.ConsultantServiceDTO;
import com.kuangji.paopao.order.vo.BaseOrderFormVO;
import com.kuangji.paopao.order.vo.BaseOrderFormVO.BuyGoodsFormVO;
import com.kuangji.paopao.order.vo.OrderAmountVO;
import com.kuangji.paopao.util.SerialNumberGenUtils;

/**
 * 统一下单抽象类
 * 
 * @ClassName: AbstractUnifiedOrderService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author: 金威正
 *
 */
public abstract class AbstractUnifiedOrderService {

	@Autowired
	protected MallTradeOrderGoodsMapper mallTradeOrderGoodsMapper;

	@Autowired
	protected MallTradeOrderMapper mallTradeOrderMapper;
	
	@Autowired
	protected MallTradeOrderDiscountMapper mallTradeOrderDiscountMapper;
	
    @Autowired
    protected CouponMapper couponMapper;

	@Autowired
	protected CouponService couponService;

	@Autowired
	protected DataSourceTransactionManager dataSourceTransactionManager;
	@Autowired
	protected TransactionDefinition transactionDefinition;
	

	@Autowired
	protected MemberCaseMapper memberCaseMapper;
	/**
	 * 创建订单
	 * 
	 * @param 表单参数对象
	 * @return: Order 返回创建成功的订单数据
	 */
	public MallTradeOrder dealCreateOrder(BaseOrderFormVO fvo) {
		// 校验下单
		fvo.setOrderType(this.getOrderType());
		ResultCodeEnum rc = this.validateUnifiedParams(fvo);
		if (rc != null) {
			throw new ServiceException(rc);
		}
		// 内存中创建订单明细/订单中的商品
		List<MallTradeOrderGoods> orderGoodsList = this.genOrderGoods(fvo);
		
		// 内存中创建订单主体信息
		MallTradeOrder mallTradeOrder = this.genOrder(fvo);
		//并且关联关系
		this.generateOrderGoodsList(fvo, orderGoodsList,mallTradeOrder);
		
		//计算订单金额等信息
		OrderAmountVO amount = this.calcOrderAmount(mallTradeOrder, orderGoodsList);
		TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
		try {
			if(amount.getDiscountItems()!=null && !amount.getDiscountItems().isEmpty()){
				this.mallTradeOrderDiscountMapper.insertBatch(amount.getDiscountItems());
			}
		
			// 订单明细入库
			this.mallTradeOrderGoodsMapper.insertBatch(orderGoodsList);
			// 订单入库
			mallTradeOrder.setOrderDetail(orderGoodsList.stream().map(p -> p.getGoodsName()).collect(Collectors.joining(",")));
			mallTradeOrder.setSellerRemarks(orderGoodsList.stream().map(p -> p.getGoodsAttrs()).collect(Collectors.joining(",")));

			ConsultantServiceDTO dto=(ConsultantServiceDTO) fvo;
			MemberCase userCase = new MemberCase();
			BeanUtils.copyProperties(dto, userCase);
			int userId=dto.getBuyerId().intValue();
			userCase.setUserId(userId);
			userCase.setConsultantType(JSONArray.toJSONString(dto.getConsultantType()));
			memberCaseMapper.insertSelective(userCase);
			mallTradeOrder.setMemberCaseId(userCase.getId());
		
			if (this.mallTradeOrderMapper.insertSelective(mallTradeOrder) != 1) {

				throw new ServiceException(rc);
			}
		} catch (Exception e) {
			dataSourceTransactionManager.rollback(transactionStatus);
			if (e instanceof ServiceException) {
				ServiceException serviceException=(ServiceException) e;
				throw serviceException;
			}
			throw e;
		}
		dataSourceTransactionManager.commit(transactionStatus);
		this.onUnifiedOrderSuccEvent(fvo, mallTradeOrder);
		return mallTradeOrder;
	}

	/**
	 * 校验下单参数<br>
	 * 默认以线上下单形式
	 * 
	 * @param fvo
	 *            表单对象
	 * @return: ResultCodeEnum
	 */
	protected ResultCodeEnum validateUnifiedParams(BaseOrderFormVO fvo) {
		// TODO Auto-generated method stub
		// 订单来源
		if (!OrderFromEnum.include(fvo.getOrderFrom())) {
			return ResultCodeEnum.ERROR_ORDER_BAD_FROM;
		}

		// 仅支持线上支付包 或者微信支付
		if (!PayTypeEnum.include(fvo.getPayType())) {
			return ResultCodeEnum.ERROR_ORDER_BAD_PAY_TYPE;
		}

		// 下单人信息
		if (StringUtils.isBlank(fvo.getOperName())) {
			return ResultCodeEnum.ERROR_ORDER_NO_OPER;
		}

		// 校验商品
		if (fvo.getBuyGoods() == null || fvo.getBuyGoods().isEmpty()) {
			return ResultCodeEnum.ERROR_TRADE_ORDER_NO_GOODS;
		}
		return null;
	}
	 
	/**
	 * 验证优惠劵 
	 * 内存管理 计算商品的购买价格 优惠 价格
	 * 生成内存总订单
	 * 
	 */
	protected  List<MallTradeOrderGoods> generateOrderGoodsList(BaseOrderFormVO fvo,List<MallTradeOrderGoods> list,MallTradeOrder mallTradeOrder){
		int  buyAmount=list.stream().mapToInt(MallTradeOrderGoods::getBuyAmount).sum();

		ConsultantServiceDTO dto = (ConsultantServiceDTO) fvo;
		Integer goodsCouponId= dto.getCouponId();
		if (goodsCouponId==null||goodsCouponId<=0) {
			list.stream().forEach(c->{
				c.setDiscountAmount(0);
				c.setOrderNo(mallTradeOrder.getOrderNo());
				c.setBuyAmount(c.getBuyPrice()-c.getDiscountAmount());
			});
			//总订单设置价格
			mallTradeOrder.setGoodsAmount(buyAmount);
			//得到优化价格
			int  discountAmount=list.stream().mapToInt(MallTradeOrderGoods::getDiscountAmount).sum();
			mallTradeOrder.setDiscountAmount(discountAmount);
			mallTradeOrder.setOrderAmount(mallTradeOrder.getGoodsAmount()-mallTradeOrder.getDiscountAmount());
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put(CommonConstant.CONSULTANT_WORK_DATE, dto.getConsultantWorkDate());
			map.put(CommonConstant.CONSULTANT_WORK_TIME, dto.getConsultantWorkTime());
			mallTradeOrder.setExt(JSON.toJSONString(map));
			return list;
		}
//		Coupon coupon = couponMapper.get(goodsCouponId);
//		if (coupon == null) {
//			throw new ServiceException(ResultCodeEnum.ERROR_COUPON_NO_GOODS);
//		}
//		Integer userAmout = coupon.getAmount();
//		if (buyAmount<userAmout) {
//			throw new ServiceException(ResultCodeEnum.ERROR_COUPON_NO_USE_GOODS);
//
//		}
		int deductionAmount = couponService.getActualDiscountAmount(goodsCouponId,buyAmount);
		list.stream().forEach(c->{
			c.setDiscountAmount(deductionAmount);
			c.setOrderNo(mallTradeOrder.getOrderNo());
			c.setBuyAmount(c.getBuyPrice()-c.getDiscountAmount());
			c.setCouponId(goodsCouponId);
		});
		//总订单设置价格
		mallTradeOrder.setGoodsAmount(buyAmount);
		//价格
		int  discountAmount=list.stream().mapToInt(MallTradeOrderGoods::getDiscountAmount).sum();
		mallTradeOrder.setDiscountAmount(discountAmount);
		mallTradeOrder.setOrderAmount(mallTradeOrder.getGoodsAmount()-mallTradeOrder.getDiscountAmount());
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put(CommonConstant.CONSULTANT_WORK_DATE, dto.getConsultantWorkDate());
		map.put(CommonConstant.CONSULTANT_WORK_TIME, dto.getConsultantWorkTime());
		mallTradeOrder.setExt(JSON.toJSONString(map));
		return list;	
	}
	 
	
	/**
	 * 创建主订单信息（内存中）<br>
	 * 默认以线上下单形式
	 * 
	 * @param fvo
	 *            下单参数
	 * @return: Order
	 */
	protected MallTradeOrder genOrder(BaseOrderFormVO fvo) {
		// TODO Auto-generated method stub
		MallTradeOrder mallTradeOrder = new MallTradeOrder();

		mallTradeOrder.setOrderNo(SerialNumberGenUtils.genOrderNo());

		// 设置订单类型：订单类型由具体的子实现类提供
		mallTradeOrder.setOrderType(this.getOrderType());
		mallTradeOrder.setPayType(fvo.getPayType());
		mallTradeOrder.setOrderFrom(fvo.getOrderFrom());
		mallTradeOrder.setOperName(fvo.getOperName());
		mallTradeOrder.setOrderTime(new Date());
		mallTradeOrder.setShopId(fvo.getBuyGoods().get(0).getShopId());
		mallTradeOrder.setBuyerId(fvo.getBuyerId());

		// 微信支付 或者支付宝支付 设置状态
		if (mallTradeOrder.getPayType() == PayTypeEnum.PAY_WX_MP.code
				|| mallTradeOrder.getPayType() == PayTypeEnum.PAY_ZFB_LINE.code) {
			mallTradeOrder.setPayStatus(PayStatusEnum.PAY_WAIT.code);
			mallTradeOrder.setPayTime(null);
			mallTradeOrder.setOrderStatus(OrderStatusEnum.CREATE_SUCC.code);
			mallTradeOrder.setOutTradeNo(UUID.randomUUID().toString().replace("-", ""));
		}

		mallTradeOrder.setIsInvoice(fvo.getIsInvoice());
		mallTradeOrder.setSendTime(null);
		mallTradeOrder.setCompleteTime(null);
		mallTradeOrder.setBuyerRemarks(fvo.getBuyerRemarks());
		mallTradeOrder.setTransactionId(null);
		
		return mallTradeOrder;
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
				
				orderGoodsList.add(ogs);
			}
		}
		return orderGoodsList;
	}

	/**
	 * 折扣的计算有逻辑顺序
	 * @param order		订单信息
	 * @return: List<OrderDiscount>      
	 */
	protected List<MallTradeOrderDiscount> calcOrderDiscount(MallTradeOrder order){
		// TODO Auto-generated method stub
		List<MallTradeOrderDiscount> discList = new LinkedList<MallTradeOrderDiscount>();
	
		MallTradeOrderDiscount mallTradeOrderDiscount=
				this.genOrderDiscRec(order.getOrderNo(), order.getGoodsAmount(),
						order.getDiscountAmount(), new BigDecimal(1));
		
		discList.add(mallTradeOrderDiscount);
		return discList;
	}
	
	/**
	 * 计算订单金额
	 * 
	 * @param fvo
	 *            统一下单参数
	 * @return: OrderAmountVO 返回订单金额对象信息
	 */
	public OrderAmountVO calcOrderAmount(BaseOrderFormVO fvo) {
		// 创建订单
		MallTradeOrder mallTradeOrder = this.genOrder(fvo);
		// 创建订单中的商品
		List<MallTradeOrderGoods> mallTradeOrderGoods = this.genOrderGoods(fvo);
		return this.calcOrderAmount(mallTradeOrder, mallTradeOrderGoods);
	}

	/**
	 * 计算主订单金额
	 * 
	 * @param
	 * @return: void
	 */
	protected OrderAmountVO calcOrderAmount(MallTradeOrder order, List<MallTradeOrderGoods> orderGoodsList) {

	
		//计算订单优惠金额信息
		List<MallTradeOrderDiscount> discItems = this.calcOrderDiscount(order);
		
		order.setDiscountAmount(discItems.stream()
				.collect(Collectors.summingInt(MallTradeOrderDiscount::getDiscountAmount)));
		
		//计算每个商品的折扣金额信息
		if(orderGoodsList.size() == 1){
			OrderAmountVO amount = new OrderAmountVO();
			amount.setGoodsAmount(order.getGoodsAmount());
			amount.setDiscountAmount(order.getDiscountAmount());
			amount.setOrderAmount(order.getOrderAmount());
			amount.setDiscountItems(discItems);
			return amount;
		}
		throw new ServiceException(ResultCodeEnum.ILLGEAL_PARAMTER);

	}

	/**
	 * 新增订单折扣记录
	 * @param order			订单信息
	 * @param type			折扣类型
	 */
	protected MallTradeOrderDiscount genOrderDiscRec(String orderNO, Integer unDiscountAmount, Integer discountAmount,
			BigDecimal discountRate) {
		// TODO Auto-generated method stub
		MallTradeOrderDiscount disc = new MallTradeOrderDiscount();
		disc.setOrderNo(orderNO);
		disc.setUnDiscountAmount(unDiscountAmount);
		disc.setDiscountAmount(discountAmount);
		disc.setDiscountRate(discountRate);
		disc.setDiscountType(0);
		disc.setTypeName("优惠劵");
		return disc;
	}
	
	/**
	 * 获取订单类型，参考：{@code OrderTypeEnum.java}
	 * 
	 * @return
	 */
	protected abstract int getOrderType();

	/**
	 * 下单成功事件：业务订单可以通过实现该方法完善自身业务的逻辑
	 * 
	 * @param order
	 *            订单信息
	 * @return: void
	 */
	protected abstract void onUnifiedOrderSuccEvent(BaseOrderFormVO fvo, final MallTradeOrder order);
	
	

	

}
