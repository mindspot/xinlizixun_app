package common.repository.http.entity.order;

import java.util.List;

import common.repository.http.entity.home.MenuItemBean;
import common.repository.http.param.BaseResponseBean;

/**
 * Created by hpzhan on 2020/2/27.
 */

public class OrderUserInfoResponseBean extends BaseResponseBean {
    private List<MenuItemBean> classification;
    private List<LabelItemBean> consulteds;
    private List<LabelItemBean> consultationSex;
    private LastUserInfoBean lastCase;

    public List<MenuItemBean> getClassification() {
        return classification;
    }

    public void setClassification(List<MenuItemBean> classification) {
        this.classification = classification;
    }

    public List<LabelItemBean> getConsulteds() {
        return consulteds;
    }

    public void setConsulteds(List<LabelItemBean> consulteds) {
        this.consulteds = consulteds;
    }

    public List<LabelItemBean> getConsultationSex() {
        return consultationSex;
    }

    public void setConsultationSex(List<LabelItemBean> consultationSex) {
        this.consultationSex = consultationSex;
    }

    public LastUserInfoBean getLastCase() {
        return lastCase;
    }

    public void setLastCase(LastUserInfoBean lastCase) {
        this.lastCase = lastCase;
    }
}
