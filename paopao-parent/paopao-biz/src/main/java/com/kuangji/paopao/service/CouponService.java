package com.kuangji.paopao.service;

import com.kuangji.paopao.dto.param.CouponParam;
import com.kuangji.paopao.dto.result.CouponResult;
import com.kuangji.paopao.model.consultant.Coupon;
import com.kuangji.paopao.service.base.BaseService;

import java.util.List;

/**
 * Author 金威正
 * Date  2020-02-22
 */
public interface CouponService extends BaseService<Coupon, Integer> {

    /**
     * 获取优惠劵商品信息
     * Author 金威正
     * Date  2020-02-22
     */
    List<Coupon> listMyCoupon(int userId);


    /**
     * 后台获取优惠劵商品信息
     * Author 金威正
     * Date  2020-02-22
     */
    List<CouponResult> listCoupon(CouponParam couponParam);

    Coupon redeemCoupon(Integer templateId, String redeemCode, Integer userId);

    Coupon redeemCoupon(String code, Integer userId);

    Integer getActualDiscountAmount(Integer couponId, Integer buyAmount);

    List<Coupon> listMatchedCoupon(int userId, int useAmount);

    Boolean redeemCodeExisted(Integer templateId, String redeemCode);

    boolean isCouponUsed(String token, Integer couponId);
}
