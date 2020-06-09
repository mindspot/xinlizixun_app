package common.repository.http;

/**
 * Created by Administrator on 2017/11/7.
 * <p>
 * 请求响应的code定义，以后所有的code都在该文件中定义，且添加好该code是在哪个接口中，作用是什么，并且下次定义不要和现在的重复了；
 */
public class HttpCode {
    /**
     * 申请借款(credit-loan/apply-loan)，交易密码错误
     * com.module.money.lend.confirm.LendConfirmLoanActivity
     */
    public final static int HTTP_CODE_PAY_PASS_ERROR = 3;
    /**
     * 申请借款(credit-loan/apply-loan)，页面过期自动刷新页面
     * com.module.money.lend.confirm.LendConfirmLoanActivity
     */
    public final static int HTTP_CODE_PAGE_EXPIRES = 4;

    /**
     * 申请借款(credit-loan/apply-loan) 对话框形式显示错误
     * com.module.money.lend.confirm.LendConfirmLoanActivity
     * 借款确认(credit-loan/get-confirm-loan) 对话框形式显示错误
     * com.module.money.lend.confirm.LendConfirmLoanActivity
     * com.module.money.lend.main_page.ApplyLoanViewHolder
     */
    public final static int HTTP_CODE_ALERT_SHOW = 5;

    /**
     * 申请借款(credit-loan/apply-loan) 对话框形式显示"去还款"信息
     * com.module.money.lend.confirm.LendConfirmLoanActivity
     * 借款确认(credit-loan/get-confirm-loan) 对话框形式显示"去还款"信息
     * com.module.money.lend.confirm.LendConfirmLoanActivity
     * com.module.money.lend.main_page.ApplyLoanViewHolder
     */
    public final static int HTTP_CODE_ALERT_REAPY = 6;
    /**
     * 申请借款(credit-loan/apply-loan)，身份证过期，提示用户重新认证
     * com.module.money.lend.LendListAdapter
     * 申请借款(credit-loan/apply-loan)，身份证过期，提示用户重新认证
     * com.module.money.lend.confirm.LendConfirmLoanActivity
     */
    public final static int HTTP_CODE_ALERT_TO_AUTH_PERSIONINFO = 1004;


    /**
     * 数据MD5加密回传
     */
    public final static int HTTP_CODE_MD5_INFO_UPLOAD = 100;
    /**
     * 这个手机号已经注册，直接登录
     * 获取验证码(credit-user/reg-get-code)
     */
    public final static int HTTP_CODE_PHONE_IS_REGISTER = 1000;
    /**
     * 这个手机号已经注册，直接登录,但是将手机号与link传入LoginActivity，并显示前程数据视图，
     * 从来没有用过
     * 获取验证码(credit-user/reg-get-code)
     */
    public final static int HTTP_CODE_PHONE_IS_REGISTER2 = 2000;
}
