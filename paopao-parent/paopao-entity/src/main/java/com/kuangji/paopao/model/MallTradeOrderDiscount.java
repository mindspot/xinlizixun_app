package com.kuangji.paopao.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
* Created by Mybatis Generator 2020/03/14
*/
@Table(name = "mall_trade_order_discount")
@Data
public class MallTradeOrderDiscount implements Serializable {
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
     * 未优惠之前的价格，单位：分
     */
    @Column(name = "un_discount_amount")
    private Integer unDiscountAmount;

    /**
     * 删除标识：0=未删除，1=已删除
     */
    @Column(name = "is_delete")
    private Integer isDelete=0;

    /**
     * 优惠金额，单位：分
     */
    @Column(name = "discount_amount")
    private Integer discountAmount;

    /**
     * 折扣率，相对商品总额
     */
    @Column(name = "discount_rate")
    private BigDecimal discountRate;

    /**
     * 优惠方式：1=积分，2=会员折扣
     */
    @Column(name = "discount_type")
    private Integer discountType;

    /**
     * 优惠说明
     */
    @Column(name = "type_name")
    private String typeName;

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

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", orderNo=").append(orderNo);
        sb.append(", unDiscountAmount=").append(unDiscountAmount);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", discountAmount=").append(discountAmount);
        sb.append(", discountRate=").append(discountRate);
        sb.append(", discountType=").append(discountType);
        sb.append(", typeName=").append(typeName);
        sb.append(", insertTime=").append(insertTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}