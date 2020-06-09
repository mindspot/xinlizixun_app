package com.kuangji.paopao.api.impl;

import com.kuangji.paopao.api.CouponApi;
import com.kuangji.paopao.model.consultant.Coupon;
import com.kuangji.paopao.service.CouponService;
import com.kuangji.paopao.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CouponApiImpl implements CouponApi {

    @Autowired
    CouponService couponService;

    @Override
    public List<Coupon> listMyCoupon(String token) {
        int userId = JwtUtils.getUserId(token);
        return couponService.listMyCoupon(userId);
    }

    @Override
    public List<Coupon> listMatchedCoupon(String token, int useAmount) {
        int userId = JwtUtils.getUserId(token);
        return couponService.listMatchedCoupon(userId, useAmount);
    }

    @Override
    public Coupon redeemCoupon(String redeemCode, Integer userId) {
        return couponService.redeemCoupon(redeemCode, userId);
    }
}
