package com.kuangji.paopao.mapper;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.dto.param.CouponParam;
import com.kuangji.paopao.dto.result.CouponResult;
import com.kuangji.paopao.model.consultant.Coupon;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Created by Mybatis Generator 2020/05/25
*/
public interface CouponMapper extends BaseMapper<Coupon> {
    Coupon getToCollectCoupon(@Param("templateId") Integer templateId, @Param("redeemCode") String redeemCode);

    List<CouponResult> listCoupon(CouponParam couponParam);
}