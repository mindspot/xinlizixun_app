package com.kuangji.paopao.service.impl;

import com.github.pagehelper.PageHelper;
import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.dto.param.PromotionalActivitiesParam;
import com.kuangji.paopao.dto.result.ActivitiesResult;
import com.kuangji.paopao.mapper.PromotionalActivitiesMapper;
import com.kuangji.paopao.model.consultant.PromotionalActivities;
import com.kuangji.paopao.service.CouponTemplateService;
import com.kuangji.paopao.service.PromotionalActivitiesService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionalActivitiesServiceImpl extends BaseServiceImpl<PromotionalActivities, Integer> implements PromotionalActivitiesService {
    @Value("${platform.inner-activities-prefix-url}")
    private String INNER_ACTIVITIES_PREFIX_URL;
    @Autowired
    private PromotionalActivitiesMapper promotionalActivitiesMapper;
    @Autowired
    private CouponTemplateService couponTemplateService;

    @Override
    public BaseMapper<PromotionalActivities> getMapper() {
        return promotionalActivitiesMapper;
    }

    @Override
    public List<ActivitiesResult> listPromotionalActivities(PromotionalActivitiesParam param) {
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        return promotionalActivitiesMapper.listPromotionalActivities();
    }

    @Override
    public int addPromotionalActivities(PromotionalActivities promotionalActivities) {
        promotionalActivitiesMapper.insertSelective(promotionalActivities);
        promotionalActivities.setUrl(INNER_ACTIVITIES_PREFIX_URL + promotionalActivities.getId());
        if (promotionalActivities.getStatus() == 1) {
            couponTemplateService.updateStatusByIds(promotionalActivities.getTemplateIds(), 1);
        }
        return promotionalActivitiesMapper.updateByPrimaryKeySelective(promotionalActivities);
    }

    @Override
    public int updatePromotionalActivities(PromotionalActivities promotionalActivities) {
        if (promotionalActivities.getStatus() == 1) {
            couponTemplateService.updateStatusByIds(promotionalActivities.getTemplateIds(), 1);
        } else {
            couponTemplateService.updateStatusByIds(promotionalActivities.getTemplateIds(), 2);
        }
        return promotionalActivitiesMapper.updateByPrimaryKeySelective(promotionalActivities);
    }
}
