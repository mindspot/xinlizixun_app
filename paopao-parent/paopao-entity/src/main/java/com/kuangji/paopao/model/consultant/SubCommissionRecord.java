package com.kuangji.paopao.model.consultant;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
* Created by Mybatis Generator 2020/04/21
*/
@Table(name = "sub_commission_record")
@Data
public class SubCommissionRecord implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "consultant_user_id")
    private Integer consultantUserId;

    @Column(name = "member_user_id")
    private Integer memberUserId;

    @Column(name = "total_amount")
    private Integer totalAmount;

    @Column(name = "platform_rate")
    private Integer platformRate;

    @Column(name = "platform_amount")
    private Integer platformAmount;

    @Column(name = "channel_rate")
    private Integer channelRate;

    @Column(name = "channel_amount")
    private Integer channelAmount;

    @Column(name = "consultant_rate")
    private Integer consultantRate;

    @Column(name = "consultant_amount")
    private Integer consultantAmount;

    @Column(name = "partner_rate")
    private Integer partnerRate;

    @Column(name = "partner_amount")
    private Integer partnerAmount;

    /**
     * 类型 1 首单 2 复单
     */
    private Integer type;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 描述
     */
    private String remark;

    /**
     * 创建时间
     */
    @Column(name = "create_dt")
    private Date createDt;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "SubCommissionRecord{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", consultantUserId=" + consultantUserId +
                ", memberUserId=" + memberUserId +
                ", totalAmount=" + totalAmount +
                ", platformRate=" + platformRate +
                ", platformAmount=" + platformAmount +
                ", channelRate=" + channelRate +
                ", channelAmount=" + channelAmount +
                ", consultantRate=" + consultantRate +
                ", consultantAmount=" + consultantAmount +
                ", partnerRate=" + partnerRate +
                ", partnerAmount=" + partnerAmount +
                ", type=" + type +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", createDt=" + createDt +
                '}';
    }
}