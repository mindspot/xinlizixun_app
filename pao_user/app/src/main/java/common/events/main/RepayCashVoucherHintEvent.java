package common.events.main;

/**
 * 还款后，优惠券提示
 *
 * @author Administrator
 * @date 2018/3/28
 */
public class RepayCashVoucherHintEvent {

    /**
     * 是否需要弹出优惠劵提示框
     */
    public static final int TYPE_SHOW_HINT_VOUCHER_DIALOG = 1;
    /**
     * 切换用户后，重置请求状态
     */
    public static final int TYPE_RESET_CASH_VOUCHER_DIALOG_SHOW_STATUS = 2;


    private final int type;

    public RepayCashVoucherHintEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
