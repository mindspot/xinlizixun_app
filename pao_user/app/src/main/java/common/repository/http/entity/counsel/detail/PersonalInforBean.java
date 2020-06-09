package common.repository.http.entity.counsel.detail;

import java.io.Serializable;

/**
 * Created by hpzhan on 2020/2/23.
 */

public class PersonalInforBean implements Serializable {
    private int id;

    private String realName;

    private String headImg;

    private String provName;

    private int provCode;

    private String cityName;

    private int cityCode;

    private String areaName;

    private int areaCode;

    private String addrDetail;

    private String sendWord;

    private String easemobId;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRealName() {
        return this.realName;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getHeadImg() {
        return this.headImg;
    }

    public void setProvName(String provName) {
        this.provName = provName;
    }

    public String getProvName() {
        return this.provName;
    }

    public void setProvCode(int provCode) {
        this.provCode = provCode;
    }

    public int getProvCode() {
        return this.provCode;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return this.cityName;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getCityCode() {
        return this.cityCode;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaName() {
        return this.areaName;
    }

    public void setAreaCode(int areaCode) {
        this.areaCode = areaCode;
    }

    public int getAreaCode() {
        return this.areaCode;
    }

    public void setAddrDetail(String addrDetail) {
        this.addrDetail = addrDetail;
    }

    public String getAddrDetail() {
        return this.addrDetail;
    }

    public void setSendWord(String sendWord) {
        this.sendWord = sendWord;
    }

    public String getSendWord() {
        return this.sendWord;
    }

    public String getEasemobId() {
        return easemobId;
    }

    public void setEasemobId(String easemobId) {
        this.easemobId = easemobId;
    }
}
