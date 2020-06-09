package com.kuangji.paopao.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
* Created by Mybatis Generator 2020/03/14
*/
@Table(name = "mall_trade_order")
@Data
public class MallTradeOrder implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 订单编号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 订单类型
     */
    @Column(name = "order_type")
    private Integer orderType;

    /**
     * 下单方式：10=商家，20=平台客服
     */
    @Column(name = "order_from")
    private Integer orderFrom;

    @Column(name = "oper_name")
    private String operName;

    /**
     * 下单时间
     */
    @Column(name = "order_time")
    private Date orderTime;

    /**
     * 咨询师ID
     */
    @Column(name = "shop_id")
    private Integer shopId;

    /**
     * 下单人ID
     */
    @Column(name = "buyer_id")
    private Integer buyerId;

    /**
     * 支付方式：1=微信，2=支付宝
     */
    @Column(name = "pay_type")
    private Integer payType;

    /**
     * 支付状态：0=待支付，1=已支付
     */
    @Column(name = "pay_status")
    private Integer payStatus;

    /**
     * 支付时间
     */
    @Column(name = "pay_time")
    private Date payTime;

    /**
     * 订单状态：-10=交易失败，-3=平台取消，-2=卖家取消，-1=买家取消，0=创建,1=支付完成，2=已发货，10=交易完成
     */
    @Column(name = "order_status")
    private Integer orderStatus;

    /**
     * 评价状态：0=未评价，1=已评价
     */
    @Column(name = "comment_status")
    private Integer commentStatus;

    /**
     * 商品金额，单位：分
     */
    @Column(name = "goods_amount")
    private Integer goodsAmount;

    /**
     * 运费，单位：分
     */
    @Column(name = "logistics_amount")
    private Integer logisticsAmount;

    /**
     * 优惠金额，单位：分
     */
    @Column(name = "discount_amount")
    private Integer discountAmount;

    /**
     * 订单金额，单位：分
     */
    @Column(name = "order_amount")
    private Integer orderAmount;

    /**
     * 是否开发票：0=不开，1=开
     */
    @Column(name = "is_invoice")
    private Integer isInvoice;

    /**
     * 发货时间
     */
    @Column(name = "send_time")
    private Date sendTime;

    /**
     * 交易完成时间
     */
    @Column(name = "complete_time")
    private Date completeTime;

    /**
     * 买家备注
     */
    @Column(name = "buyer_remarks")
    private String buyerRemarks;

    /**
     * 商户交易单号，用于第三方支付
     */
    @Column(name = "out_trade_no")
    private String outTradeNo;

    /**
     * 第三方交易平台返回的支付订单号，用于平台查询对接
     */
    @Column(name = "transaction_id")
    private String transactionId;

    /**
     * 订单内容描述，一般用于支付
     */
    @Column(name = "order_detail")
    private String orderDetail;

    /**
     * 卖家备注
     */
    @Column(name = "seller_remarks")
    private String sellerRemarks;
    
    /**
     * 创建时间
     */
    @Column(name = "insert_time")
    private Date insertTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 删除标识：0=未删除，1=已删除
     */
    @Column(name = "is_delete")
    private Integer isDelete;
    /**
     * 是不是read 过这条订单消息
     */
    @Column(name = "is_read")
    private Integer isRead;
    
    @Column(name = "member_case_id")
    private Integer memberCaseId;
    
    /**
     * 业务扩展字段，根据实际业务存储
     */
    private String ext;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", orderNo=").append(orderNo);
        sb.append(", orderType=").append(orderType);
        sb.append(", orderFrom=").append(orderFrom);
        sb.append(", operName=").append(operName);
        sb.append(", orderTime=").append(orderTime);
        sb.append(", shopId=").append(shopId);
        sb.append(", buyerId=").append(buyerId);
        sb.append(", payType=").append(payType);
        sb.append(", payStatus=").append(payStatus);
        sb.append(", payTime=").append(payTime);
        sb.append(", orderStatus=").append(orderStatus);
        sb.append(", commentStatus=").append(commentStatus);
        sb.append(", goodsAmount=").append(goodsAmount);
        sb.append(", logisticsAmount=").append(logisticsAmount);
        sb.append(", discountAmount=").append(discountAmount);
        sb.append(", orderAmount=").append(orderAmount);
        sb.append(", isInvoice=").append(isInvoice);
        sb.append(", sendTime=").append(sendTime);
        sb.append(", completeTime=").append(completeTime);
        sb.append(", buyerRemarks=").append(buyerRemarks);
        sb.append(", outTradeNo=").append(outTradeNo);
        sb.append(", transactionId=").append(transactionId);
        sb.append(", orderDetail=").append(orderDetail);
        sb.append(", sellerRemarks=").append(sellerRemarks);
        sb.append(", insertTime=").append(insertTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", ext=").append(ext);
        sb.append(", isRead=").append(isRead);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

	public MallTradeOrder(String orderNo) {
		super();
		this.orderNo = orderNo;
	}
    
	
	
	public MallTradeOrder() {
		
	}

	public MallTradeOrder(String orderNo, Integer buyerId) {
		super();
		this.orderNo = orderNo;
		this.buyerId = buyerId;
	}

	public MallTradeOrder(String orderNo, Integer buyerId, Integer payStatus) {
		super();
		this.orderNo = orderNo;
		this.buyerId = buyerId;
		this.payStatus = payStatus;
	}

	public MallTradeOrder(Integer shopId, Integer isRead) {
		super();
		this.shopId = shopId;
		this.isRead = isRead;
	}
    
	
}