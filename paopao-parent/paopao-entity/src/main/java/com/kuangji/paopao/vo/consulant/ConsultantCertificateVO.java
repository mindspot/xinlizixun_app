package com.kuangji.paopao.vo.consulant;

import com.kuangji.paopao.model.consultant.*;
import lombok.Data;

import java.util.List;

@Data
public class ConsultantCertificateVO {
    private List<EducationExperience> educationExperienceList;
    private List<Certification> certificationList;
    private List<TrainingExperience> trainingExperienceList;
    private List<SupervisedExperience> supervisedExperienceList;
    private List<PublishMaterial> publishBookList;
    private List<PublishMaterial> publishPaperList;
}
