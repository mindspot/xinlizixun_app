package common.repository.http.param.counsel;

import java.util.List;

import common.repository.http.param.BaseRequestBean;

/**
 * Created by hpzhan on 2020/2/22.
 */

public class CounselListRequestBean extends BaseRequestBean {
    private String areaCode;
    private String areaName;
    private String cityCode;
    private String cityName;
    private List<Integer> classification;
    private List<Integer> consultationAge;
    private List<Integer> consultationTime;
    private int maxPrice;
    private int minPrice;
    private String provCode;
    private String provName;
    private List<Integer> sex;
    private int pageNum;

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<Integer> getClassification() {
        return classification;
    }

    public void setClassification(List<Integer> classification) {
        this.classification = classification;
    }

    public List<Integer> getConsultationAge() {
        return consultationAge;
    }

    public void setConsultationAge(List<Integer> consultationAge) {
        this.consultationAge = consultationAge;
    }

    public List<Integer> getConsultationTime() {
        return consultationTime;
    }

    public void setConsultationTime(List<Integer> consultationTime) {
        this.consultationTime = consultationTime;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public String getProvCode() {
        return provCode;
    }

    public void setProvCode(String provCode) {
        this.provCode = provCode;
    }

    public String getProvName() {
        return provName;
    }

    public void setProvName(String provName) {
        this.provName = provName;
    }

    public List<Integer> getSex() {
        return sex;
    }

    public void setSex(List<Integer> sex) {
        this.sex = sex;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}
