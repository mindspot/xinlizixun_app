package common.repository.http.entity.balance;

/**
 * @version V 1.0 xxxxxxxx
 * @Title: pao_studio
 * @Package common.repository.http.entity.balance
 * @Description: $todo$
 * @author: L-BackPacker
 * @date: 2020.03.27 下午 5:27
 * @verdescript 版本号 修改时间  修改人 修改的概要说明
 * @Copyright: 2020
 */
public class RecardItemBean {
    private String cashTime;
    private int amount;
    private int cardType;

    public String getCashTime() {
        return cashTime;
    }

    public void setCashTime(String cashTime) {
        this.cashTime = cashTime;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }
}
