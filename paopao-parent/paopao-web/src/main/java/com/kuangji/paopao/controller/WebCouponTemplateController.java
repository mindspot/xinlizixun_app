package com.kuangji.paopao.controller;

import com.github.pagehelper.PageInfo;
import com.kuangji.paopao.dto.param.CouponParam;
import com.kuangji.paopao.dto.param.CouponTemplateParam;
import com.kuangji.paopao.model.consultant.CouponTemplate;
import com.kuangji.paopao.service.CouponTemplateService;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.util.ServiceResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/web/coupon-template")
public class WebCouponTemplateController {
    @Autowired
    private CouponTemplateService couponTemplateService;

    @PostMapping(value = {"/list"})
    public Object listCoupon(@RequestBody CouponParam couponParam) {
        List<CouponTemplate> list = couponTemplateService.listCouponTemplate(couponParam);
        return ServiceResultUtils.success(new PageInfo<>(list));
    }

    @PostMapping(value = {"/add"})
    public Object addCoupon(@RequestBody CouponTemplate couponTemplate) {
        return ServiceResultUtils.success(couponTemplateService.addCouponTemplate(couponTemplate) > 0);
    }

    @PostMapping(value = {"/get/{id}"})
    public Object getCoupon(@PathVariable("id") Integer id) {
        return ServiceResultUtils.success(couponTemplateService.getCouponTemplate(id));
    }

    @PostMapping(value = {"/update"})
    public Object updateCoupon(@RequestBody CouponTemplate couponTemplate) {

        return ServiceResultUtils.success(couponTemplateService.updateByPrimaryKeySelective(couponTemplate) > 0);
    }

    @PostMapping(value = {"/query"})
    public Object queryCouponTemplate(@RequestBody CouponTemplateParam couponTemplateParam) {
        List<CouponTemplate> list = couponTemplateService.queryCouponTemplate(couponTemplateParam);
        return ServiceResultUtils.success(list);
    }

    @GetMapping(value = {"/collect/{templateId}"})
    public Object collectCoupon(HttpServletRequest request, @PathVariable("templateId") Integer templateId) {
        String token = request.getHeader("authorization").replace("Bearer ", "");
        Integer userId = JwtUtils.getUserId(token);
        return ServiceResultUtils.success(couponTemplateService.collectCoupon(templateId, userId));
    }

    @GetMapping(value = {"/generate"})
    public Object generateCoupon(HttpServletRequest request, @RequestParam("templateId") Integer templateId) {
        String token = request.getHeader("authorization").replace("Bearer ", "");
        Integer userId = JwtUtils.getUserId(token);
        return ServiceResultUtils.success(couponTemplateService.genRedeemCoupon(templateId, userId));
    }
}
