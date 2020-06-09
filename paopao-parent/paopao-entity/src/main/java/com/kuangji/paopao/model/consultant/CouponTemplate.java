package com.kuangji.paopao.model.consultant;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
* Created by Mybatis Generator 2020/05/26
*/
@Table(name = "coupon_template")
@Data
public class CouponTemplate implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 编号
     */
    private String no;

    /**
     * 优惠券名称
     */
    private String name;

    /**
     * 1 满减、2 立减、3 折扣券 4 优惠码
     */
    private Integer type;

    /**
     * 满多少金额
     */
    @Column(name = "max_amount")
    private Integer maxAmount;

    /**
     * 用券金额
     */
    @Column(name = "actual_amount")
    private Integer actualAmount;

    /**
     * 发放数量
     */
    @Column(name = "provide_qty")
    private Integer provideQty;

    /**
     * 已领取的优惠券数量
     */
    @Column(name = "take_qty")
    private Integer takeQty;

    /**
     * 已使用的优惠券数量
     */
    @Column(name = "used_qty")
    private Integer usedQty;

    /**
     * 时效:1绝对时效（领取后XXX-XXX时间段有效）  2相对时效（领取后N天有效）
     */
    @Column(name = "valid_type")
    private Integer validType;

    /**
     * 使用开始时间
     */
    @Column(name = "valid_start_time")
    private Date validStartTime;

    /**
     * 使用结束时间
     */
    @Column(name = "valid_end_time")
    private Date validEndTime;

    /**
     * 自领取之日起有效天数
     */
    @Column(name = "valid_days")
    private Integer validDays;

    /**
     * 每人限领
     */
    @Column(name = "limit_per_person")
    private Integer limitPerPerson;

    /**
     * 领取url
     */
    private String url;

    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    @Column(name = "create_by")
    private Long createBy;

    /**
     * 创建时间
     */
    @Column(name = "create_dt")
    private Date createDt;

    /**
     * 修改人
     */
    @Column(name = "modified_by")
    private Long modifiedBy;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", no=").append(no);
        sb.append(", name=").append(name);
        sb.append(", type=").append(type);
        sb.append(", maxAmount=").append(maxAmount);
        sb.append(", actualAmount=").append(actualAmount);
        sb.append(", provideQty=").append(provideQty);
        sb.append(", takeQty=").append(takeQty);
        sb.append(", usedQty=").append(usedQty);
        sb.append(", validType=").append(validType);
        sb.append(", validStartTime=").append(validStartTime);
        sb.append(", validEndTime=").append(validEndTime);
        sb.append(", validDays=").append(validDays);
        sb.append(", limitPerPerson=").append(limitPerPerson);
        sb.append(", url=").append(url);
        sb.append(", status=").append(status);
        sb.append(", remark=").append(remark);
        sb.append(", createBy=").append(createBy);
        sb.append(", createDt=").append(createDt);
        sb.append(", modifiedBy=").append(modifiedBy);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}