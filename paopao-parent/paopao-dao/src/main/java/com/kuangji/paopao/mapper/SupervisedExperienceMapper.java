package com.kuangji.paopao.mapper;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.model.consultant.SupervisedExperience;

import java.util.List;

/**
* Created by Mybatis Generator 2020/04/05
*/
public interface SupervisedExperienceMapper extends BaseMapper<SupervisedExperience> {
    List<SupervisedExperience> listSupervisedExperience(Integer id);
}