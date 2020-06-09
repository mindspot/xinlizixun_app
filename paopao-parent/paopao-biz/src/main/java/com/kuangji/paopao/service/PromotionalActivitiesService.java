package com.kuangji.paopao.service;

import com.kuangji.paopao.dto.param.PromotionalActivitiesParam;
import com.kuangji.paopao.dto.result.ActivitiesResult;
import com.kuangji.paopao.model.consultant.PromotionalActivities;
import com.kuangji.paopao.service.base.BaseService;

import java.util.List;

public interface PromotionalActivitiesService extends BaseService<PromotionalActivities, Integer> {
    List<ActivitiesResult> listPromotionalActivities(PromotionalActivitiesParam param);

    int addPromotionalActivities(PromotionalActivities promotionalActivities);

    int updatePromotionalActivities(PromotionalActivities promotionalActivities);
}
