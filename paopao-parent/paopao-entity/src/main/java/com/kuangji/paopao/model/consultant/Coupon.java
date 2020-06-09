package com.kuangji.paopao.model.consultant;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
* Created by Mybatis Generator 2020/05/25
*/
@Table(name = "coupon")
@Data
public class Coupon implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 优惠券模板
     */
    @Column(name = "template_id")
    private Integer templateId;

    /**
     * 优惠券名称
     */
    private String name;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 有效期：开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Column(name = "term_start_date")
    private Date termStartDate;

    /**
     * 有效期：截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Column(name = "term_end_date")
    private Date termEndDate;

    @Column(name = "amount_limit")
    private Integer amountLimit;
    /**
     * 优惠券金额
     */
    private Integer amount;

    /**
     * 兑换码
     */
    @Column(name = "redeem_code")
    private String redeemCode;

    /**
     * 使用须知
     */
    @Column(name = "use_notice")
    private String useNotice;

    /**
     * 优惠券状态
     */
    private Integer status;

    /**
     * userId
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 创建人
     */
    @Column(name = "create_by")
    private Integer createBy;

    /**
     * 创建时间
     */
    @Column(name = "create_dt")
    private Date createDt;

    /**
     * 修改人
     */
    @Column(name = "modified_by")
    private Integer modifiedBy;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", templateId=").append(templateId);
        sb.append(", name=").append(name);
        sb.append(", type=").append(type);
        sb.append(", termStartDate=").append(termStartDate);
        sb.append(", termEndDate=").append(termEndDate);
        sb.append(", amount=").append(amount);
        sb.append(", redeemCode=").append(redeemCode);
        sb.append(", useNotice=").append(useNotice);
        sb.append(", status=").append(status);
        sb.append(", userId=").append(userId);
        sb.append(", createBy=").append(createBy);
        sb.append(", createDt=").append(createDt);
        sb.append(", modifiedBy=").append(modifiedBy);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}