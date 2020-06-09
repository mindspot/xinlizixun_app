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
@Table(name = "consultation_order")
@Data
public class ConsultationOrder implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 购买人ID
     */
    @Id
    @Column(name = "user_id")
    private Integer userId;

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
     * 删除标识：0=未删除，1=已删除
     */
    @Column(name = "is_delete")
    private Integer isDelete;

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
     * 预订状态 -1套餐支付类  0普通
     */
    private Integer status;



    /**
     * 继续使用的服务次数
     */
    @Column(name = "consultation_number")
    private Integer consultationNumber;

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

	public ConsultationOrder(String orderNo) {
		super();
		this.orderNo = orderNo;
	}

	public ConsultationOrder() {
		
	}

}