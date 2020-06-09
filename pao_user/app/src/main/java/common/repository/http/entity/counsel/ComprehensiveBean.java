package common.repository.http.entity.counsel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hpzhan on 2020/2/21.
 */

public class ComprehensiveBean implements Serializable {
    private int minPrice;
    private int maxPrice;
    private List<SexItemBean> consultationSex;
    private List<AgeItemBean> consultationAge;

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public List<SexItemBean> getConsultationSex() {
        return consultationSex;
    }

    public void setConsultationSex(List<SexItemBean> consultationSex) {
        this.consultationSex = consultationSex;
    }

    public List<AgeItemBean> getConsultationAge() {
        return consultationAge;
    }

    public void setConsultationAge(List<AgeItemBean> consultationAge) {
        this.consultationAge = consultationAge;
    }

    public static class SexItemBean {
        private int value;
        private String name;
        private boolean isCheck;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }
    }

    public static class AgeItemBean {
        private int value;
        private String name;
        private boolean isCheck;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }
    }
}
