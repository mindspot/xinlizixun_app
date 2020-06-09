package com.kuangji.paopao.mapper;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.dto.result.ActivitiesResult;
import com.kuangji.paopao.model.consultant.PromotionalActivities;

import java.util.List;

/**
* Created by Mybatis Generator 2020/05/26
*/
public interface PromotionalActivitiesMapper extends BaseMapper<PromotionalActivities> {
    List<ActivitiesResult> listPromotionalActivities();

}