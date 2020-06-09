package com.kuangji.paopao.service.consultant.impl;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.mapper.TrainingExperienceMapper;
import com.kuangji.paopao.model.consultant.TrainingExperience;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;
import com.kuangji.paopao.service.consultant.TrainingExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingExperienceServiceImpl extends BaseServiceImpl<TrainingExperience, Integer> implements TrainingExperienceService {
    @Autowired
    private TrainingExperienceMapper trainingExperienceMapper;
    @Override
    public BaseMapper<TrainingExperience> getMapper() {
        return trainingExperienceMapper;
    }
}
