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
public class CardItemBean {
    private int id;
    private int userId;
    private String realName;
    private String alipayAccount;
    private boolean cashWithdrawal;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }

    public boolean isCashWithdrawal() {
        return cashWithdrawal;
    }

    public void setCashWithdrawal(boolean cashWithdrawal) {
        this.cashWithdrawal = cashWithdrawal;
    }
}
