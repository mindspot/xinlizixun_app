package common.repository.http.entity.user;

import common.repository.http.param.BaseResponseBean;

public class UserInfoBean extends BaseResponseBean {
    private String phone;
    private String realName;
    private int userTypeK;
    private String headImg;
    private String userTypeV;
    private int userId;
    private String token;
    private String sendWord;
    private String easemobId;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getUserTypeK() {
        return userTypeK;
    }

    public void setUserTypeK(int userTypeK) {
        this.userTypeK = userTypeK;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getUserTypeV() {
        return userTypeV;
    }

    public void setUserTypeV(String userTypeV) {
        this.userTypeV = userTypeV;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSendWord() {
        return sendWord;
    }

    public void setSendWord(String sendWord) {
        this.sendWord = sendWord;
    }

    public String getEasemobId() {
        return easemobId;
    }

    public void setEasemobId(String easemobId) {
        this.easemobId = easemobId;
    }
}
