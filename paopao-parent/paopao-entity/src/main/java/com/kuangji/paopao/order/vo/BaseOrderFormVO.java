package com.kuangji.paopao.order.vo;

import java.util.List;

import com.kuangji.paopao.enums.PayTypeEnum;

import lombok.Data;

/**
 * 下单表单数据对象
 * @author Administrator
 */
public class BaseOrderFormVO implements java.io.Serializable{
	private static final long serialVersionUID = 7412399187770605025L;
	
	/** 下单人ID */
	private Integer buyerId ;
	
	/** 收货地址ID */
	private Integer receiveAddrId ;
	
	/** 下单人姓名 （必填）*/
	private String operName = "";
	
	/** 支付方式  （必填）*/
	private int payType=PayTypeEnum.PAY_ZFB_LINE.code;
	
	/** 订单来源（必填）*/
	private int orderFrom;
	
	/** 买家备注 */
	private String buyerRemarks = "";
	
	/** 是否开发票：0=不开，1=开 */
	private Integer isInvoice = 0;
	
	/** 订单类型（不需要下单传值） */
	private int orderType = 1;
	
	/** 购买商品的信息 */
	private List<BuyGoodsFormVO> buyGoods;

	public Integer getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Integer buyerId) {
		this.buyerId = buyerId;
	}

	public Integer getReceiveAddrId() {
		return receiveAddrId;
	}

	public void setReceiveAddrId(Integer receiveAddrId) {
		this.receiveAddrId = receiveAddrId;
	}

	public String getOperName() {
		return operName;
	}

	public void setOperName(String operName) {
		this.operName = operName;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public Integer getOrderFrom() {
		return orderFrom;
	}

	public void setOrderFrom(int orderFrom) {
		this.orderFrom = orderFrom;
	}

	public String getBuyerRemarks() {
		return buyerRemarks;
	}

	public void setBuyerRemarks(String buyerRemarks) {
		this.buyerRemarks = buyerRemarks;
	}

	public Integer getIsInvoice() {
		return isInvoice;
	}

	public void setIsInvoice(Integer isInvoice) {
		this.isInvoice = isInvoice;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public List<BuyGoodsFormVO> getBuyGoods() {
		return buyGoods;
	}

	public void setBuyGoods(List<BuyGoodsFormVO> buyGoods) {
		this.buyGoods = buyGoods;
	}

	/**
	 * 购买商品信息
	 * @author Administrator
	 */
	@Data
	public static class BuyGoodsFormVO{
		
		public BuyGoodsFormVO(){
			super();
		}
		
		/** 店铺ID（必填） */
		private Integer shopId ;

		/** 商品ID（必填） */
		private Integer goodsId ;
		
		/** 购买数量（必填） */
		private Integer buyCount = 1;
		
		/** 商品名称 */
		private String goodsName = "";
		
		/** 商品类型：1=咨询师服务商品*/
		private Integer goodsClass = 1;
		
		/** 商品主图 */
		private String goodsImg = "";

		/** 售价/标准价，单位：分 */
		private Integer sellPrice = 0;
		
		/** 会员最终购买价格，单位：分 */
		private Integer buyPrice = 0;
		
		/** 购买总金额，单位：分 */
		private Integer buyAmount = 0;

		private String sellPointText="";
		
	
	}
	
}
