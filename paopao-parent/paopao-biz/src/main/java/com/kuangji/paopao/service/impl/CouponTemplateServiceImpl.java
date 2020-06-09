package com.kuangji.paopao.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.github.pagehelper.PageHelper;
import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.dto.param.CouponParam;
import com.kuangji.paopao.dto.param.CouponTemplateParam;
import com.kuangji.paopao.enums.CouponStatusEnum;
import com.kuangji.paopao.enums.CouponTypeEnum;
import com.kuangji.paopao.enums.CouponValidTypeEnum;
import com.kuangji.paopao.mapper.CouponTemplateMapper;
import com.kuangji.paopao.model.consultant.Coupon;
import com.kuangji.paopao.model.consultant.CouponTemplate;
import com.kuangji.paopao.service.CouponService;
import com.kuangji.paopao.service.CouponTemplateService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CouponTemplateServiceImpl extends BaseServiceImpl<CouponTemplate, Integer> implements CouponTemplateService {
    @Value("${platform.inner-coupon-prefix-url}")
    private String INNER_COUPON_PREFIX_URL;
    @Value("${platform.inner-redeem-prefix-url}")
    private String INNER_REDEEM_PREFIX_URL;
    @Autowired
    private CouponTemplateMapper couponTemplateMapper;
    @Autowired
    private CouponService couponService;

    @Override
    public BaseMapper<CouponTemplate> getMapper() {
        return couponTemplateMapper;
    }

    @Override
    public int addCouponTemplate(CouponTemplate couponTemplate) {
        couponTemplateMapper.insertSelective(couponTemplate);
        switch (CouponTypeEnum.getCouponTypeEnum(couponTemplate.getType())) {
            case REDEEM:
                couponTemplate.setUrl(INNER_REDEEM_PREFIX_URL + couponTemplate.getId());
                break;
            default:
                couponTemplate.setUrl(INNER_COUPON_PREFIX_URL + couponTemplate.getId());
                break;
        }
        return couponTemplateMapper.updateByPrimaryKeySelective(couponTemplate);
    }

    @Override
    public List<CouponTemplate> listCouponTemplate(CouponParam couponParam) {
        PageHelper.startPage(couponParam.getPageNo(), couponParam.getPageSize());
        return couponTemplateMapper.selectAll();
    }

    @Override
    public List<CouponTemplate> queryCouponTemplate(CouponTemplateParam couponTemplateParam) {
        return couponTemplateMapper.listCouponTemplate(couponTemplateParam);
    }

    @Override
    public int updateStatusByIds(String ids, Integer status) {
        return couponTemplateMapper.updateStatusByIds(ids, status);
    }

    @Override
    public Coupon collectCoupon(Integer templateId, Integer userId) {
        CouponTemplate couponTemplate = couponTemplateMapper.selectByPrimaryKey(templateId);
        if (couponTemplate.getStatus() != 1) {
            throw new ServiceException(ResultCodeEnum.COUPON_STATUS_ERROR);
        }
        if (couponTemplate.getTakeQty() + 1 > couponTemplate.getProvideQty()) {
            throw new ServiceException(ResultCodeEnum.COUPON_QTY_ERROR);
        }
        Coupon coupon = genCommonCoupon(couponTemplate, userId);
        if (coupon.getId() > 0) {
            couponTemplate.setTakeQty(couponTemplate.getTakeQty() + 1);
            couponTemplateMapper.updateByPrimaryKeySelective(couponTemplate);
        }
        return coupon;
    }

    @Override
    public Coupon getCouponTemplate(Integer id) {
        CouponTemplate couponTemplate = couponTemplateMapper.selectByPrimaryKey(id);
        Coupon coupon = new Coupon();
        coupon.setTemplateId(couponTemplate.getId());
        coupon.setType(couponTemplate.getType());
        coupon.setAmount(couponTemplate.getActualAmount());
        coupon.setAmountLimit(couponTemplate.getMaxAmount());
        coupon.setName(couponTemplate.getName());
        coupon.setUseNotice(couponTemplate.getRemark());
        if (CouponValidTypeEnum.FIXED_DAYS.type == couponTemplate.getValidType()) {
            coupon.setTermStartDate(DateUtil.date());
            coupon.setTermEndDate(DateUtil.offsetDay(DateUtil.date(), couponTemplate.getValidDays()));
        } else {
            coupon.setTermStartDate(couponTemplate.getValidStartTime());
            coupon.setTermEndDate(couponTemplate.getValidEndTime());
        }
        coupon.setStatus(CouponStatusEnum.WAIT_USE.status);
        return coupon;
    }

    private Coupon genCommonCoupon(CouponTemplate couponTemplate, Integer userId) {
        Coupon coupon = new Coupon();
        coupon.setTemplateId(couponTemplate.getId());
        coupon.setUserId(userId);
        coupon.setCreateBy(userId);
        coupon.setType(couponTemplate.getType());
        coupon.setAmount(couponTemplate.getActualAmount());
        coupon.setAmountLimit(couponTemplate.getMaxAmount());
        coupon.setName(couponTemplate.getName());
        coupon.setUseNotice(couponTemplate.getRemark());
        if (CouponValidTypeEnum.FIXED_DAYS.type == couponTemplate.getValidType()) {
            coupon.setTermStartDate(DateUtil.date());
            coupon.setTermEndDate(DateUtil.offsetDay(DateUtil.date(), couponTemplate.getValidDays()));
        } else {
            coupon.setTermStartDate(couponTemplate.getValidStartTime());
            coupon.setTermEndDate(couponTemplate.getValidEndTime());
        }
        coupon.setStatus(CouponStatusEnum.WAIT_USE.status);
        couponService.add(coupon);
        return coupon;
    }

    public Boolean genRedeemCoupon(Integer templateId, Integer userId) {
        CouponTemplate couponTemplate = couponTemplateMapper.selectByPrimaryKey(templateId);
        if(couponTemplate.getStatus()!=2) {
            throw new ServiceException(ResultCodeEnum.COUPON_SENDED);
        }
        Coupon coupon = new Coupon();
        coupon.setCreateBy(userId);
        coupon.setTemplateId(couponTemplate.getId());
        coupon.setType(couponTemplate.getType());
        coupon.setAmount(couponTemplate.getActualAmount());
        coupon.setName(couponTemplate.getName());
        coupon.setUseNotice(couponTemplate.getRemark());
        if (CouponValidTypeEnum.FIXED_DAYS.type == couponTemplate.getValidType()) {
            coupon.setTermStartDate(DateUtil.date());
            coupon.setTermEndDate(DateUtil.offsetDay(DateUtil.date(), couponTemplate.getValidDays()));
        } else {
            coupon.setTermStartDate(couponTemplate.getValidStartTime());
            coupon.setTermEndDate(couponTemplate.getValidEndTime());
        }
        coupon.setStatus(CouponStatusEnum.WAIT_COLLECT.status);
        for (int i = 0; i < couponTemplate.getProvideQty(); i++) {
            String redeemCode = RandomUtil.randomNumbers(12);
            while (couponService.redeemCodeExisted(couponTemplate.getId(), redeemCode)) {
                log.info(redeemCode);
                redeemCode = RandomUtil.randomNumbers(12);
            }
            coupon.setId(null);
            coupon.setRedeemCode(redeemCode);
            couponService.add(coupon);
        }
        couponTemplate.setStatus(1);
        return couponTemplateMapper.updateByPrimaryKeySelective(couponTemplate)>0;
    }
}