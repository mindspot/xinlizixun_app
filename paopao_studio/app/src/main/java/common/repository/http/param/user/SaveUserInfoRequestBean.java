package common.repository.http.param.user;

import common.repository.http.param.BaseRequestBean;

/**
 * Created by hpzhan on 2020/2/28.
 */

public class SaveUserInfoRequestBean extends BaseRequestBean {
    private String realName;
    private int sex;
    private String headImg;
    private String sendWord;
    private String content;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
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
}
