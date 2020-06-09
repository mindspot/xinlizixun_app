package com.kuangji.paopao.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kuangji.paopao.api.CouponApi;
import com.kuangji.paopao.model.consultant.Coupon;
import com.kuangji.paopao.service.CouponTemplateService;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.util.ServiceResultUtils;

/**
 * Author 金威正
 * Date  2020-02-22
 */
@RestController
public class CouponController {
    @Autowired
    private CouponApi couponApi;
    @Autowired
    private CouponTemplateService couponTemplateService;

    @GetMapping(value = {"/v1/coupon/list"})
    public Object list(HttpServletRequest request) {
        String token = request.getHeader("authorization").replace("Bearer ", "");
        List<Coupon> coupons = couponApi.listMyCoupon(token);
        return ServiceResultUtils.success(coupons);
    }

    //符合条件的优惠劵
    @GetMapping(value = {"/v1/coupon/list/{useAmount}"})
    public Object list(HttpServletRequest request, @PathVariable("useAmount") int useAmount) {
        String token = request.getHeader("authorization").replace("Bearer ", "");
        List<Coupon> coupons = couponApi.listMatchedCoupon(token, useAmount);
        return ServiceResultUtils.success(coupons);
    }

    @GetMapping(value = {"/v1/coupon/redeem"})
    public Object redeemCoupon(HttpServletRequest request, @RequestParam String redeemCode) {
        String token = request.getHeader("authorization").replace("Bearer ", "");
        Integer userId = JwtUtils.getUserId(token);
        Coupon coupon = couponApi.redeemCoupon(redeemCode, userId);
        return ServiceResultUtils.success(coupon);
    }

    @GetMapping(value = {"/v1/coupon/collect/{templateId}"})
    public Object collectCoupon(HttpServletRequest request, @PathVariable("templateId") Integer templateId) {
        String token = request.getHeader("authorization").replace("Bearer ", "");
        Integer userId = JwtUtils.getUserId(token);
        return ServiceResultUtils.success(couponTemplateService.collectCoupon(templateId, userId));
    }
}
