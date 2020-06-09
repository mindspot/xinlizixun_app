package com.kuangji.paopao.vo;

import lombok.Data;
/**
 * 优惠劵
 * Author 金威正
 * Date  2020-02-22
 */
@Data
public class CouponVO {
	private long id;
	private long goodsId;
    private int termType;
    private int termDays;
    private String termEndDate;
    private String useNotice;
    private int useAmount;
    private int deduction;
    private String goodsName;
    private int sellPrice;
    private boolean  BeOverdue;
}