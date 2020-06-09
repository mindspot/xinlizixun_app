package common.repository.http.entity.center;

import java.util.List;

import common.repository.http.param.BaseResponseBean;

/**
 * Created by hpzhan on 2020/2/24.
 */
public class MyCenterResponseBean extends BaseResponseBean {
    private boolean stopBooking;
    private ConsultantBean consultants;
    private List<String> work;
    private List<String> contention;
    private List<String> classification;

    public boolean isStopBooking() {
        return stopBooking;
    }

    public void setStopBooking(boolean stopBooking) {
        this.stopBooking = stopBooking;
    }

    public ConsultantBean getConsultants() {
        return consultants;
    }

    public void setConsultants(ConsultantBean consultants) {
        this.consultants = consultants;
    }

    public List<String> getWork() {
        return work;
    }

    public void setWork(List<String> work) {
        this.work = work;
    }

    public List<String> getContention() {
        return contention;
    }

    public void setContention(List<String> contention) {
        this.contention = contention;
    }

    public List<String> getClassification() {
        return classification;
    }

    public void setClassification(List<String> classification) {
        this.classification = classification;
    }

    public static class ConsultantBean {
        private String username;
        private String realName;
        private String headImg;
        private int sex;
        private String sendWord;
        private String content;
        private int account;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getSendWord() {
            return sendWord;
        }

        public void setSendWord(String sendWord) {
            this.sendWord = sendWord;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getAccount() {
            return account;
        }

        public void setAccount(int account) {
            this.account = account;
        }
    }
}
