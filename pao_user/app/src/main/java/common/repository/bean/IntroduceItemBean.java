package common.repository.bean;

import java.util.List;

import common.repository.http.entity.counsel.detail.AboutConsultantBean;

/**
 * Created by hpzhan on 2020/2/23.
 */

public class IntroduceItemBean {
    private String title;
    private String content;
    private int type;
    private List<String> labels;
    private List<AboutConsultantBean.QualificationItemBean> qualifications;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<AboutConsultantBean.QualificationItemBean> getQualifications() {
        return qualifications;
    }

    public void setQualifications(List<AboutConsultantBean.QualificationItemBean> qualifications) {
        this.qualifications = qualifications;
    }
}
