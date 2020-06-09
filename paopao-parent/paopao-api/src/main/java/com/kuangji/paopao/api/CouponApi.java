package com.kuangji.paopao.api;

import com.kuangji.paopao.model.consultant.Coupon;

import java.util.List;

public interface CouponApi {

    /**
     * 全部优惠卷
     * Author 金威正
     * Date  2020-02-22
     */
    List<Coupon> listMyCoupon(String token);


    Coupon redeemCoupon(String redeemCode, Integer userId);

    List<Coupon> listMatchedCoupon(String token, int useAmount);

}
