package common.repository.http.entity.counsel.detail;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hpzhan on 2020/2/23.
 */

public class AboutConsultantBean implements Serializable {
    private List<QualificationItemBean> qualifications;
    private ServiceInstructionBean serviceInstructions;
    private String personalIntroduction;
    private List<BeGoodAtBean> beGoodAt;

    public List<QualificationItemBean> getQualifications() {
        return qualifications;
    }

    public void setQualifications(List<QualificationItemBean> qualifications) {
        this.qualifications = qualifications;
    }

    public ServiceInstructionBean getServiceInstructions() {
        return serviceInstructions;
    }

    public void setServiceInstructions(ServiceInstructionBean serviceInstructions) {
        this.serviceInstructions = serviceInstructions;
    }

    public String getPersonalIntroduction() {
        return personalIntroduction;
    }

    public void setPersonalIntroduction(String personalIntroduction) {
        this.personalIntroduction = personalIntroduction;
    }

    public List<BeGoodAtBean> getBeGoodAt() {
        return beGoodAt;
    }

    public void setBeGoodAt(List<BeGoodAtBean> beGoodAt) {
        this.beGoodAt = beGoodAt;
    }

    public static class QualificationItemBean {
        private int id;
        private int qualifications;
        private String qualificationsName;
        private int qualificationsNumber;
        private int qualificationsYears;
        private String qualificationsImg;
        private int practitionersYears;
        private int caseNumber;
        private String imgUrl;
        private String imgUrlSize;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getQualifications() {
            return qualifications;
        }

        public void setQualifications(int qualifications) {
            this.qualifications = qualifications;
        }

        public int getQualificationsNumber() {
            return qualificationsNumber;
        }

        public void setQualificationsNumber(int qualificationsNumber) {
            this.qualificationsNumber = qualificationsNumber;
        }

        public int getQualificationsYears() {
            return qualificationsYears;
        }

        public void setQualificationsYears(int qualificationsYears) {
            this.qualificationsYears = qualificationsYears;
        }

        public String getQualificationsImg() {
            return qualificationsImg;
        }

        public void setQualificationsImg(String qualificationsImg) {
            this.qualificationsImg = qualificationsImg;
        }

        public int getPractitionersYears() {
            return practitionersYears;
        }

        public void setPractitionersYears(int practitionersYears) {
            this.practitionersYears = practitionersYears;
        }

        public int getCaseNumber() {
            return caseNumber;
        }

        public void setCaseNumber(int caseNumber) {
            this.caseNumber = caseNumber;
        }

        public String getQualificationsName() {
            return qualificationsName;
        }

        public void setQualificationsName(String qualificationsName) {
            this.qualificationsName = qualificationsName;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getImgUrlSize() {
            return imgUrlSize;
        }

        public void setImgUrlSize(String imgUrlSize) {
            this.imgUrlSize = imgUrlSize;
        }
    }

    public static class ServiceInstructionBean {
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public static class BeGoodAtBean {
        private int labelId;
        private String labelVal;
        private String userLabelType;

        public int getLabelId() {
            return labelId;
        }

        public void setLabelId(int labelId) {
            this.labelId = labelId;
        }

        public String getLabelVal() {
            return labelVal;
        }

        public void setLabelVal(String labelVal) {
            this.labelVal = labelVal;
        }

        public String getUserLabelType() {
            return userLabelType;
        }

        public void setUserLabelType(String userLabelType) {
            this.userLabelType = userLabelType;
        }
    }
}
