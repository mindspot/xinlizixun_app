package common.repository.http.param.order;

import java.util.List;

import common.repository.http.param.BaseRequestBean;

/**
 * Created by hpzhan on 2020/2/25.
 */

public class ConsultantOrderRequestBean extends BaseRequestBean {
    private String consultantWorkDate;
    private String consultantWorkTime;
    private String isConsultant;
    private List<String> consultantType;
    private String detailedDescription;
    private String operName;
    private String sex;
    private String age;
    private String payType;
    private String couponId;
    private List<BuyGoodItemBean> buyGoods;
    private String goodsId;
    private String orderNo;

    public String getConsultantWorkDate() {
        return consultantWorkDate;
    }

    public void setConsultantWorkDate(String consultantWorkDate) {
        this.consultantWorkDate = consultantWorkDate;
    }

    public String getConsultantWorkTime() {
        return consultantWorkTime;
    }

    public void setConsultantWorkTime(String consultantWorkTime) {
        this.consultantWorkTime = consultantWorkTime;
    }

    public String getIsConsultant() {
        return isConsultant;
    }

    public void setIsConsultant(String isConsultant) {
        this.isConsultant = isConsultant;
    }

    public List<String> getConsultantType() {
        return consultantType;
    }

    public void setConsultantType(List<String> consultantType) {
        this.consultantType = consultantType;
    }

    public String getDetailedDescription() {
        return detailedDescription;
    }

    public void setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public List<BuyGoodItemBean> getBuyGoods() {
        return buyGoods;
    }

    public void setBuyGoods(List<BuyGoodItemBean> buyGoods) {
        this.buyGoods = buyGoods;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
