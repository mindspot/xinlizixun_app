package com.kuangji.paopao.service.consultant.impl;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.mapper.EducationExperienceMapper;
import com.kuangji.paopao.model.consultant.EducationExperience;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;
import com.kuangji.paopao.service.consultant.EducationExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EducationExperienceServiceImpl extends BaseServiceImpl<EducationExperience, Integer> implements EducationExperienceService {
    @Autowired
    private EducationExperienceMapper educationExperienceMapper;
    @Override
    public BaseMapper<EducationExperience> getMapper() {
        return educationExperienceMapper;
    }
}
