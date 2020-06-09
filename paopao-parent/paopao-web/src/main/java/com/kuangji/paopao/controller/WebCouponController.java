package com.kuangji.paopao.controller;

import com.github.pagehelper.PageInfo;
import com.kuangji.paopao.dto.param.CouponParam;
import com.kuangji.paopao.dto.result.CouponResult;
import com.kuangji.paopao.model.consultant.Coupon;
import com.kuangji.paopao.service.CouponService;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.util.ServiceResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Author 金威正
 * Date  2020-02-22
 */
@RestController
public class WebCouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping(value = {"/web/coupon/list"})
    public Object listCoupon(@RequestBody CouponParam couponParam) {
        List<CouponResult> list = couponService.listCoupon(couponParam);
        return ServiceResultUtils.success(new PageInfo<>(list));
    }

    @PostMapping(value = {"/web/coupon/add"})
    public Object addCoupon(@RequestBody Coupon coupon) {
        return ServiceResultUtils.success(couponService.add(coupon) > 0);
    }

    @PostMapping(value = {"/web/coupon/updateCoupon"})
    public Object updateCoupon(@RequestBody Coupon coupon) {
        return ServiceResultUtils.success(couponService.updateByPrimaryKeySelective(coupon) > 0);
    }

    @GetMapping(value = {"/web/coupon/redeem/{templateId}"})
    public Object redeemCoupon(HttpServletRequest request, @PathVariable("templateId") Integer templateId, @RequestParam String redeemCode) {
        String token = request.getHeader("authorization").replace("Bearer ", "");
        Integer userId = JwtUtils.getUserId(token);
        Coupon coupon = couponService.redeemCoupon(templateId, redeemCode, userId);
        return ServiceResultUtils.success(coupon);
    }

    @GetMapping(value = {"/web/coupon/redeem"})
    public Object redeemCoupon(HttpServletRequest request, @RequestParam String redeemCode) {
        String token = request.getHeader("authorization").replace("Bearer ", "");
        Integer userId = JwtUtils.getUserId(token);
        Coupon coupon = couponService.redeemCoupon(redeemCode, userId);
        return ServiceResultUtils.success(coupon);
    }
}
