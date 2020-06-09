package common.repository.http.param.order;

/**
 * Created by hpzhan on 2020/2/25.
 */

public class BuyGoodItemBean {
    private String shopId;
    private String goodsId;
    private String goodsClass;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsClass() {
        return goodsClass;
    }

    public void setGoodsClass(String goodsClass) {
        this.goodsClass = goodsClass;
    }
}
