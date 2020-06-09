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
@Table(name = "consultation_set_meal_order")
@Data
public class ConsultationSetMealOrder implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4796307790646589638L;

	/**
     * 主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 购买人ID
     */
    @Id
    @Column(name = "user_id")
    private Integer userId;

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
     * 店铺ID
     */
    @Column(name = "shop_id")
    private Integer shopId;

    /**
     * 商品id
     */
    @Column(name = "goods_id")
    private Integer goodsId;

    /**
     * 订单编号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 接单时间
     */
    @Column(name = "rece_time")
    private Date receTime;

    /**
     * 预约日期+时间
     */
    @Column(name = "consultation_time")
    private String consultationTime;

    /**
     * 预订状态 -1套餐支付类
     */
    @Column(name = "status")
    private Integer status;
    /**
     * 继续使用的服务次数
     */
    @Column(name = "consultation_number")
    private Integer consultationNumber;

    /**
     * 购买日期
     */
    @Column(name = "buy_date")
    private Date buyDate;

    /**
     * 过期
     */
    @Column(name = "term_end_date")
    private Date termEndDate;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", insertTime=").append(insertTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", shopId=").append(shopId);
        sb.append(", goodsId=").append(goodsId);
        sb.append(", orderNo=").append(orderNo);
        sb.append(", receTime=").append(receTime);
        sb.append(", consultationTime=").append(consultationTime);
        sb.append(", status=").append(status);
        sb.append(", consultationNumber=").append(consultationNumber);
        sb.append(", buyDate=").append(buyDate);
        sb.append(", termEndDate=").append(termEndDate);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}