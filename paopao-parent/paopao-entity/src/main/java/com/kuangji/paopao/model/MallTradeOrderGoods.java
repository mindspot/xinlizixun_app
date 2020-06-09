package com.kuangji.paopao.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
* Created by Mybatis Generator 2020/03/14
*/
@Table(name = "mall_trade_order_goods")
@Data
public class MallTradeOrderGoods implements Serializable {
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
     * 店铺ID
     */
    @Column(name = "shop_id")
    private Integer shopId;

    /**
     * 商品ID
     */
    @Column(name = "goods_id")
    private Integer goodsId;

    /**
     * 商品类型：
     */
    @Column(name = "goods_class")
    private Integer goodsClass;

    /**
     * 商品名称
     */
    @Column(name = "goods_name")
    private String goodsName;

    /**
     * 商品属性字符串
     */
    @Column(name = "goods_attrs")
    private String goodsAttrs;

    /**
     * 商品主图
     */
    @Column(name = "goods_img")
    private String goodsImg;

    /**
     * 售价/标准价，单位：分
     */
    @Column(name = "sell_price")
    private Integer sellPrice;

    /**
     * 成本价/OTA价/进价，单位：分
     */
    @Column(name = "cost_price")
    private Integer costPrice;

    /**
     * 会员最终购买价格，单位：分
     */
    @Column(name = "buy_price")
    private Integer buyPrice;

    /**
     * 购买数量
     */
    @Column(name = "buy_count")
    private Integer buyCount;

    /**
     * 购买总金额，单位：分
     */
    @Column(name = "buy_amount")
    private Integer buyAmount;

    /**
     * 折扣率
     */
    @Column(name = "discount_rate")
    private BigDecimal discountRate;

    /**
     * 折扣金额，单位：分
     */
    @Column(name = "discount_amount")
    private Integer discountAmount;

    /**
     * 优惠劵id
     */
    @Column(name = "coupon_id")
    private Integer couponId;

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
    private Integer isDelete=0;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", orderNo=").append(orderNo);
        sb.append(", shopId=").append(shopId);
        sb.append(", goodsId=").append(goodsId);
        sb.append(", goodsClass=").append(goodsClass);
        sb.append(", goodsName=").append(goodsName);
        sb.append(", goodsAttrs=").append(goodsAttrs);
        sb.append(", goodsImg=").append(goodsImg);
        sb.append(", sellPrice=").append(sellPrice);
        sb.append(", costPrice=").append(costPrice);
        sb.append(", buyPrice=").append(buyPrice);
        sb.append(", buyCount=").append(buyCount);
        sb.append(", buyAmount=").append(buyAmount);
        sb.append(", discountRate=").append(discountRate);
        sb.append(", discountAmount=").append(discountAmount);
        sb.append(", couponId=").append(couponId);
        sb.append(", insertTime=").append(insertTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}