package common.repository.http.entity.coupon;

/**
 * Created by hpzhan on 2020/2/23.
 */

public class CouponItemBean {
    private int id;
    private int goodsId;
    private String termEndDate;
    private String useNotice;
    private int deduction;
    private String goodsName;
    private boolean beOverdue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getTermEndDate() {
        return termEndDate;
    }

    public void setTermEndDate(String termEndDate) {
        this.termEndDate = termEndDate;
    }

    public String getUseNotice() {
        return useNotice;
    }

    public void setUseNotice(String useNotice) {
        this.useNotice = useNotice;
    }

    public int getDeduction() {
        return deduction;
    }

    public void setDeduction(int deduction) {
        this.deduction = deduction;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public boolean isBeOverdue() {
        return beOverdue;
    }

    public void setBeOverdue(boolean beOverdue) {
        this.beOverdue = beOverdue;
    }
}
