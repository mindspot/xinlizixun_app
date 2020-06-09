package com.kuangji.paopao.mapper;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.model.consultant.Consultant;
import com.kuangji.paopao.model.consultant.ConsultantRate;

/**
* Created by Mybatis Generator 2020/04/21
*/
public interface ConsultantRateMapper extends BaseMapper<ConsultantRate> {
    ConsultantRate getFirstConsultantRate(Integer consultantId);
    ConsultantRate getSecondConsultantRate(Integer consultantId);
}