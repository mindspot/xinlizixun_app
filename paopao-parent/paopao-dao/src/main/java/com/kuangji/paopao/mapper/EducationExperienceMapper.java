package com.kuangji.paopao.mapper;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.model.consultant.EducationExperience;

import java.util.List;

/**
* Created by Mybatis Generator 2020/04/05
*/
public interface EducationExperienceMapper extends BaseMapper<EducationExperience> {
    List<EducationExperience> listEducationExperience(Integer consultantId);

}