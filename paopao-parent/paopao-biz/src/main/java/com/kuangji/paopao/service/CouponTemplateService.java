package com.kuangji.paopao.service;

import com.kuangji.paopao.dto.param.CouponParam;
import com.kuangji.paopao.dto.param.CouponTemplateParam;
import com.kuangji.paopao.model.consultant.Coupon;
import com.kuangji.paopao.model.consultant.CouponTemplate;
import com.kuangji.paopao.service.base.BaseService;

import java.util.List;

public interface CouponTemplateService extends BaseService<CouponTemplate, Integer> {
    List<CouponTemplate> listCouponTemplate(CouponParam couponParam);

    List<CouponTemplate> queryCouponTemplate(CouponTemplateParam couponTemplateParam);

    Coupon collectCoupon(Integer templateId, Integer userId);

    Boolean genRedeemCoupon(Integer templateId,Integer userId);

    int addCouponTemplate(CouponTemplate couponTemplate);

    int updateStatusByIds(String ids, Integer status);

    Coupon getCouponTemplate(Integer id);
}
