package common.repository.http.entity.order.user;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hpzhan on 2020/2/24.
 */

public class MemberCaseInfoBean implements Serializable {
    private int id;
    private int userId;
    private String consultationTime;
    private String operName;
    private int sex;
    private String age;
    private boolean isConsultant;
    private List<CommonItemBean> commonVOs;
    private String detailedDescription;
    private String consultatName;

    private ConsultantOrderDiagnosisBean consultantOrderDiagnosisVO;
    private List<ConsultantOrderDiagnosisBean> consultantOrderDiagnosisVOs;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getConsultationTime() {
        return consultationTime;
    }

    public void setConsultationTime(String consultationTime) {
        this.consultationTime = consultationTime;
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public boolean isConsultant() {
        return isConsultant;
    }

    public void setConsultant(boolean consultant) {
        isConsultant = consultant;
    }

    public List<CommonItemBean> getCommonVOs() {
        return commonVOs;
    }

    public void setCommonVOs(List<CommonItemBean> commonVOs) {
        this.commonVOs = commonVOs;
    }

    public String getDetailedDescription() {
        return detailedDescription;
    }

    public void setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
    }

    public ConsultantOrderDiagnosisBean getConsultantOrderDiagnosisVO() {
        return consultantOrderDiagnosisVO;
    }

    public void setConsultantOrderDiagnosisVO(ConsultantOrderDiagnosisBean consultantOrderDiagnosisVO) {
        this.consultantOrderDiagnosisVO = consultantOrderDiagnosisVO;
    }

    public List<ConsultantOrderDiagnosisBean> getConsultantOrderDiagnosisVOs() {
        return consultantOrderDiagnosisVOs;
    }

    public void setConsultantOrderDiagnosisVOs(List<ConsultantOrderDiagnosisBean> consultantOrderDiagnosisVOs) {
        this.consultantOrderDiagnosisVOs = consultantOrderDiagnosisVOs;
    }

    public String getConsultatName() {
        return consultatName;
    }

    public void setConsultatName(String consultatName) {
        this.consultatName = consultatName;
    }

    public static class ConsultantOrderDiagnosisBean implements Serializable{
        private String content;
        private String realName;
        private String imgeSize;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getImgeSize() {
            return imgeSize;
        }

        public void setImgeSize(String imgeSize) {
            this.imgeSize = imgeSize;
        }
    }

    public static class CommonItemBean implements Serializable{
        private String val;

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }
    }
}
