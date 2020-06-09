package common.repository.http.entity.order;

/**
 * Created by hpzhan on 2020/3/9.
 */

public class SetMealItemBean {
    private int consultantId;
    private String userName;
    private String goodsName;
    private String consultationNumber;
    private String buyDate;
    private String termEndDate;

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getConsultationNumber() {
        return consultationNumber;
    }

    public void setConsultationNumber(String consultationNumber) {
        this.consultationNumber = consultationNumber;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }

    public String getTermEndDate() {
        return termEndDate;
    }

    public void setTermEndDate(String termEndDate) {
        this.termEndDate = termEndDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getConsultantId() {
        return consultantId;
    }

    public void setConsultantId(int consultantId) {
        this.consultantId = consultantId;
    }
}
