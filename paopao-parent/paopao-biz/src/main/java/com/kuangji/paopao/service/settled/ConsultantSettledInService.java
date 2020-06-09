package com.kuangji.paopao.service.settled;

import java.util.List;
import java.util.Map;

import com.kuangji.paopao.dto.WorkDTO;
import com.kuangji.paopao.dto.result.CommonResult;
import com.kuangji.paopao.model.Common;
import com.kuangji.paopao.model.consultant.Certification;
import com.kuangji.paopao.model.consultant.EducationExperience;
import com.kuangji.paopao.model.consultant.PublishMaterial;
import com.kuangji.paopao.model.consultant.SupervisedExperience;
import com.kuangji.paopao.model.consultant.TrainingExperience;
import com.kuangji.paopao.vo.PlatformWorkingTimeVO;
import com.kuangji.paopao.vo.SettledInConsultantVO;
import com.kuangji.paopao.vo.consulant.ConsultantCertificateVO;

public interface ConsultantSettledInService  {
    Map<String, Object> settledInLogin(String userName, String code);

    /**
     * 咨询师基本信息填写
     */
    void insertConsultantDTO(SettledInConsultantVO dto, Integer userId);

    int insertConsultantCertificateVO(ConsultantCertificateVO vo, Integer consultantId);

    SettledInConsultantVO listConsultantBaseInfo(String token);

    List<PublishMaterial> listPublishMaterial(Integer consultantId,Integer materialType);

    int addPublishMaterial(PublishMaterial publishMaterial, Integer consultantId);

    int deletePublishMaterial(Integer publishMaterialId);

    List<EducationExperience> listEducationExperience(Integer consultantId);

    int addEducationExperience(EducationExperience educationExperience);

    int deleteEducationExperience(Integer educationExperienceId);

    List<TrainingExperience> listTrainingExperience(Integer consultantId);

    int addTrainingExperience(TrainingExperience trainingExperience);

    int deleteTrainingExperience(Integer trainingExperienceId);

    List<SupervisedExperience> listSupervisedExperience(Integer consultantId);

    int addSupervisedExperience(SupervisedExperience supervisedExperience);

    int deleteSupervisedExperience(Integer supervisedExperienceId);

    List<Certification> listCertification(Integer consultantId);

    int addCertification(Certification certification);

    int deleteCertification(Integer certificationId);

    List<Common> listCommonOption(Integer type);

    CommonResult listCommonOption(Integer userId,Integer type);

    List<PlatformWorkingTimeVO> listPlatformWorkingTime(Integer userId);

    List<WorkDTO> listWorkingTime(Integer userId);

}
