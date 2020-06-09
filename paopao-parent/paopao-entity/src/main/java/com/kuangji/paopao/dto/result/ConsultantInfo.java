package com.kuangji.paopao.dto.result;

import com.kuangji.paopao.dto.WorkDTO;
import com.kuangji.paopao.model.Common;
import com.kuangji.paopao.model.User;
import com.kuangji.paopao.model.UserImage;
import com.kuangji.paopao.model.consultant.*;
import com.kuangji.paopao.vo.PlatformWorkingTimeVO;
import com.kuangji.paopao.vo.consulant.ConsultantResult;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class ConsultantInfo extends Consultant {
    private User user;
    private ConsultantRate firstConsultantRate;
    private ConsultantRate nextConsultantRate;
    private List<UserImage> cards;
    private List<EducationExperience> educationExperienceList;
    private List<Certification> certificationList;
    private List<TrainingExperience> trainingExperienceList;
    private List<SupervisedExperience> supervisedExperienceList;
    private List<PublishMaterial> publishBookList;
    private List<PublishMaterial> publishPaperList;
    private List<UserLabelInfo> targetPeopleList;
    private List<UserLabelInfo> expertiseAreaList;
    private List<PlatformWorkingTimeVO> platformWorkingTimeVOList;
    private List<WorkDTO>  workTimeList;
    private List<ConsultantResult> consultantList;
}
