package common.repository.http.entity.counsel.detail;

import java.io.Serializable;

/**
 * Created by hpzhan on 2020/2/23.
 */

public class WorkInfoBean implements Serializable {
    private int id;
    private String goodsName;
    private Double sellPrice;
    private int goodsClass;
    private String mode;
    private int stock;
    private int residualTimes;
    private String shareText;
    private String orderNo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public int getGoodsClass() {
        return goodsClass;
    }

    public void setGoodsClass(int goodsClass) {
        this.goodsClass = goodsClass;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getResidualTimes() {
        return residualTimes;
    }

    public void setResidualTimes(int residualTimes) {
        this.residualTimes = residualTimes;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getShareText() {
        return shareText;
    }

    public void setShareText(String shareText) {
        this.shareText = shareText;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
