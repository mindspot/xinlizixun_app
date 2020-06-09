package common.repository.http.entity.counsel.detail;

import java.util.List;

import common.repository.http.param.BaseResponseBean;

/**
 * Created by hpzhan on 2020/2/23.
 */

public class CounselDetailResponseBean extends BaseResponseBean {
    private boolean isCollected;//true已经收藏 false 待收藏
    private List<WorkInfoBean> workingMode;
    private PersonalInforBean personalInformation;
    private AboutConsultantBean aboutConsultant;
    private int paySetMeal;

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }

    public List<WorkInfoBean> getWorkingMode() {
        return workingMode;
    }

    public void setWorkingMode(List<WorkInfoBean> workingMode) {
        this.workingMode = workingMode;
    }

    public PersonalInforBean getPersonalInformation() {
        return personalInformation;
    }

    public void setPersonalInformation(PersonalInforBean personalInformation) {
        this.personalInformation = personalInformation;
    }

    public AboutConsultantBean getAboutConsultant() {
        return aboutConsultant;
    }

    public void setAboutConsultant(AboutConsultantBean aboutConsultant) {
        this.aboutConsultant = aboutConsultant;
    }

    public int getPaySetMeal() {
        return paySetMeal;
    }

    public void setPaySetMeal(int paySetMeal) {
        this.paySetMeal = paySetMeal;
    }
}
