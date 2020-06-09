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
* Created by Mybatis Generator 2020/03/24
*/
@Table(name = "consultant_supervisor_order")
@Data
public class ConsultantSupervisorOrder implements Serializable {
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
     * 支付状态：0=待支付，1=已支付
     */
    @Column(name = "pay_status")
    private Integer payStatus;

    /**
     * 支付时间
     */
    @Column(name = "pay_time")
    private String payTime;

    /**
     * 订单状态：-10=交易失败，-3=平台取消，-2=卖家取消，-1=买家取消，0=创建,1=支付完成，2=已发货，10=交易完成
     */
    @Column(name = "order_status")
    private Integer orderStatus;

    /**
     * 咨询师案例订单价格总合
     */
    @Column(name = "goods_amount")
    private Integer goodsAmount;

    /**
     * 订单金额，单位：分
     */
    @Column(name = "order_amount")
    private Integer orderAmount;

    /**
     * 比例 10 表示百分之10
     */
    private Integer proportion;

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
    
    @Column(name = "consultant_id")
    private  Integer consultantId;
   
    @Column(name = "supervisor_id")
    private  Integer supervisorId;
    
    @Column(name = "is_read")
    private  Integer isRead;
    
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", orderNo=").append(orderNo);
        sb.append(", payStatus=").append(payStatus);
        sb.append(", payTime=").append(payTime);
        sb.append(", orderStatus=").append(orderStatus);
        sb.append(", goodsAmount=").append(goodsAmount);
        sb.append(", orderAmount=").append(orderAmount);
        sb.append(", proportion=").append(proportion);
        sb.append(", insertTime=").append(insertTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", consultantId=").append(consultantId);
        sb.append(", supervisorId=").append(supervisorId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

	public ConsultantSupervisorOrder(String orderNo) {
		super();
		this.orderNo = orderNo;
	}
	
	
	public ConsultantSupervisorOrder() {
		
	}

	public ConsultantSupervisorOrder(String orderNo, Integer supervisorId) {
		super();
		this.orderNo = orderNo;
		this.supervisorId = supervisorId;
	}
    
    
}