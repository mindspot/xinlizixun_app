package common.repository.http.entity.counsel;

import java.util.List;

import common.repository.http.param.BaseResponseBean;

/**
 * Created by hpzhan on 2020/2/22.
 */

public class CounselHeaderResponseBean extends BaseResponseBean {
    private List<QuestionItemBean> consultationQuestion;
    private List<SortItemBean> classification;
    private List<TimeItemBean> consultationTime;
    private ComprehensiveBean comprehensive;

    public List<QuestionItemBean> getConsultationQuestion() {
        return consultationQuestion;
    }

    public void setConsultationQuestion(List<QuestionItemBean> consultationQuestion) {
        this.consultationQuestion = consultationQuestion;
    }

    public List<SortItemBean> getClassification() {
        return classification;
    }

    public void setClassification(List<SortItemBean> classification) {
        this.classification = classification;
    }

    public List<TimeItemBean> getConsultationTime() {
        return consultationTime;
    }

    public void setConsultationTime(List<TimeItemBean> consultationTime) {
        this.consultationTime = consultationTime;
    }

    public ComprehensiveBean getComprehensive() {
        return comprehensive;
    }

    public void setComprehensive(ComprehensiveBean comprehensive) {
        this.comprehensive = comprehensive;
    }
}
