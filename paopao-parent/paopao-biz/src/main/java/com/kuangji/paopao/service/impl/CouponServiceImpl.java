package com.kuangji.paopao.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.dto.param.CouponParam;
import com.kuangji.paopao.dto.result.CouponResult;
import com.kuangji.paopao.enums.CouponStatusEnum;
import com.kuangji.paopao.enums.CouponTypeEnum;
import com.kuangji.paopao.mapper.CouponMapper;
import com.kuangji.paopao.model.consultant.Coupon;
import com.kuangji.paopao.model.consultant.CouponTemplate;
import com.kuangji.paopao.service.CouponService;
import com.kuangji.paopao.service.CouponTemplateService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;
import com.kuangji.paopao.util.JwtUtils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * Author 金威正
 * Date  2020-02-22
 */
@Service
public class CouponServiceImpl extends BaseServiceImpl<Coupon, Integer> implements CouponService {
    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private CouponTemplateService couponTemplateService;

    @Override
    public BaseMapper<Coupon> getMapper() {
        return couponMapper;
    }

    @Override
    public List<Coupon> listMyCoupon(int userId) {
        Example example = new Example(Coupon.class);
        example.createCriteria().andEqualTo("userId", userId);
        return couponMapper.selectByExample(example);
    }

    @Override
    public List<Coupon> listMatchedCoupon(int userId, int useAmount) {
        Example example = new Example(Coupon.class);
        example.createCriteria().andEqualTo("userId", userId);
        example.createCriteria().andEqualTo("status", CouponStatusEnum.WAIT_USE.status);
        List<Coupon> couponList = couponMapper.selectByExample(example);
        List<Coupon> matchedList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(couponList)) {
            for (Coupon coupon : couponList) {
                if (genActualDiscountAmount(coupon, useAmount) > 0) {
                    matchedList.add(coupon);
                }
            }
        }
        return matchedList;
    }

    @Override
    public List<CouponResult> listCoupon(CouponParam couponParam) {
        PageHelper.startPage(couponParam.getPageNo(), couponParam.getPageSize());
        return couponMapper.listCoupon(couponParam);
    }

    @Override
    public Coupon redeemCoupon(Integer templateId, String redeemCode, Integer userId) {
        CouponTemplate couponTemplate = couponTemplateService.findById(templateId);
        if(couponTemplate.getStatus()!=1) {
            throw new ServiceException(ResultCodeEnum.COUPON_STATUS_ERROR);
        }
        Coupon coupon = couponMapper.getToCollectCoupon(templateId, redeemCode);
        if (coupon != null) {
            coupon.setUserId(userId);
            coupon.setStatus(CouponStatusEnum.WAIT_USE.status);
            coupon.setRedeemCode(redeemCode);
            couponMapper.updateByPrimaryKeySelective(coupon);
            couponTemplate.setTakeQty(couponTemplate.getTakeQty() + 1);
            couponTemplateService.updateByPrimaryKeySelective(couponTemplate);
        } else {
            throw new ServiceException(ResultCodeEnum.COUPON_REDEEM_CODE_ERROR);
        }
        return coupon;
    }

    @Override
    public Coupon redeemCoupon(String code, Integer userId) {
        String redeemCode = StrUtil.subSufByLength(code,12);
        String templateId = StrUtil.subWithLength(code,0,code.length()-12);
        return redeemCoupon(Integer.parseInt(templateId),redeemCode,userId);
    }

    @Override
    public Integer getActualDiscountAmount(Integer couponId, Integer buyAmount) {
        Coupon coupon = couponMapper.selectByPrimaryKey(couponId);
        return genActualDiscountAmount(coupon, buyAmount);
    }

    private Integer genActualDiscountAmount(Coupon coupon, Integer buyAmount) {
        Integer amount = 0;
        switch (CouponTypeEnum.getCouponTypeEnum(coupon.getType())) {
            case FULL_REDUCTION:
                if (buyAmount <= coupon.getAmountLimit()) {
                    amount = coupon.getAmountLimit();
                }
                break;
            case DISCOUNT:
                amount = (coupon.getAmount() * buyAmount) / 100;
                break;
            case REDUCTION:
                amount = coupon.getAmount();
                break;
            case REDEEM:
                amount = coupon.getAmount();
                break;
        }
        return amount;
    }

    @Override
    public Boolean redeemCodeExisted(Integer templateId, String redeemCode) {
        Example example = new Example(Coupon.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("templateId", templateId);
        criteria.andEqualTo("redeemCode", redeemCode);
        return couponMapper.selectCountByExample(example) > 0;
    }

    @Override
    public boolean isCouponUsed(String token, Integer couponId) {
        Integer userId = JwtUtils.getUserId(token);
        Coupon coupon=new Coupon();
        coupon.setUserId(userId);
        coupon.setId(couponId);
        coupon.setStatus(CouponStatusEnum.USED.status);
        return couponMapper.selectCount(coupon)>0;
    }
}
