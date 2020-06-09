package com.kuangji.paopao.mapper;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.model.consultant.TrainingExperience;

import java.util.List;

/**
* Created by Mybatis Generator 2020/04/05
*/
public interface TrainingExperienceMapper extends BaseMapper<TrainingExperience> {
    List<TrainingExperience> listTrainingExperience(Integer consultantId);
}