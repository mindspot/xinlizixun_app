package common.repository.http.entity.order;

import java.util.List;

/**
 * Created by hpzhan on 2020/2/27.
 */

public class LastUserInfoBean {
    private int isConsultant;
    private String consultantType;
    private List<String> consultantTypes;
    private String detailedDescription;
    private int sex;
    private int age;
    private String operName;

    public int getIsConsultant() {
        return isConsultant;
    }

    public void setIsConsultant(int isConsultant) {
        this.isConsultant = isConsultant;
    }

    public String getConsultantType() {
        return consultantType;
    }

    public void setConsultantType(String consultantType) {
        this.consultantType = consultantType;
    }

    public List<String> getConsultantTypes() {
        return consultantTypes;
    }

    public void setConsultantTypes(List<String> consultantTypes) {
        this.consultantTypes = consultantTypes;
    }

    public String getDetailedDescription() {
        return detailedDescription;
    }

    public void setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName;
    }
}
