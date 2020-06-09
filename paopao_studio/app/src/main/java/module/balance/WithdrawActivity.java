package module.balance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.framework.http.bean.HttpError;
import com.framework.page.Page;
import com.framework.utils.StringUtil;
import com.paopao.paopaostudio.R;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.param.balance.WithdrawRequestBean;
import module.app.MyApplication;
import ui.CustomClickListener;
import ui.title.ToolBarTitleView;
import util.CountDownUtil;
import util.MathOperationUtil;

public class WithdrawActivity extends BaseActivity {

    @BindView(R.id.withdraw_activity_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.withdraw_activity_input_money)
    EditText inputMoney;
    @BindView(R.id.withdraw_activity_yue_text)
    TextView yuEText;
    @BindView(R.id.withdraw_activity_code)
    EditText code;
    @BindView(R.id.withdraw_activity_getcode)
    TextView getcode;
    @BindView(R.id.withdraw_activity_tip)
    TextView tip;

    private int mMoney;
    private String mTip;

    public static void newIntent(Page page, int money, String tip) {
        Intent intent = new Intent(page.activity(), WithdrawActivity.class);
        intent.putExtra("mMoney", money);
        intent.putExtra("mTip", tip);
        page.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_withdraw;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (getIntent() != null) {
            mMoney = getIntent().getIntExtra("mMoney", mMoney);
            mTip = getIntent().getStringExtra("mTip");
        }
        yuEText.setText("可用余额 " + MathOperationUtil.divStr(mMoney, 100) + " 元");
        tip.setText(mTip);
    }

    @Override
    public void initListener() {
        toolbar.setLeftClickListener(new CustomClickListener() {
            @Override
            protected void onClick() {
                finish();
            }
        });
    }

    @Override
    public void loadData() {

    }

    @OnClick({R.id.withdraw_activity_record, R.id.withdraw_activity_allwithdraw,
            R.id.withdraw_activity_getcode, R.id.withdraw_activity_confirm,
            R.id.withdraw_activity_clear})
    public void onViewClicked(View view) {
        if (isDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.withdraw_activity_record:
                BalanceRecordActivity.newIntent(this);
                break;
            case R.id.withdraw_activity_allwithdraw:
                inputMoney.setText(MathOperationUtil.divStr(mMoney, 100));
                inputMoney.setSelection(inputMoney.getText().length());
                break;
            case R.id.withdraw_activity_getcode:
                if (!MyApplication.app.getCardItemBean().isCashWithdrawal()) {
                    showToast("当前存在进行中的提现申请，需要审核通过后，才能继续提现；咨询师可在提现记录列表查看进度信息。");
                    return;
                }
                getCode();
                break;
            case R.id.withdraw_activity_confirm:
                withdraw();
                break;
            case R.id.withdraw_activity_clear:
                inputMoney.setText("");
                break;
        }
    }

    public void getCode() {
        MyApplication.loadingDefault(activity());
        HttpApi.app().getWithdrawCode(this, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                MyApplication.hideLoading();
                showToast(message);
                CountDownUtil.startCountDown(getcode);
            }

            @Override
            public void onFailed(HttpError error) {
                MyApplication.hideLoading();
                showToast(error.getErrMessage());
            }
        });
    }

    public void withdraw() {
        int money = 0;
        try {
            money = Integer.parseInt(inputMoney.getText().toString()) * 100;
        } catch (Exception ex) {
        }
        if (money == 0 || money > mMoney) {
            showToast("金额输入有误！");
            return;
        }
        if (MyApplication.app.getCardItemBean() == null) {
            showToast("系统异常，请稍后重试");
            return;
        }
        if (!MyApplication.app.getCardItemBean().isCashWithdrawal()) {
            showToast("当前存在进行中的提现申请，需要审核通过后，才能继续提现；咨询师可在提现记录列表查看进度信息。");
            return;
        }
        if (StringUtil.isBlank(code.getText().toString())) {
            showToast("请输入验证码！");
            return;
        }
        MyApplication.loadingDefault(activity());
        WithdrawRequestBean bean = new WithdrawRequestBean();
        bean.setAmount(money + "");
        bean.setCode(code.getText().toString());
        HttpApi.app().withdraw(this, bean, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                MyApplication.hideLoading();
                showToast(message);
                BalanceRecordActivity.newIntent(WithdrawActivity.this);
                finish();
            }

            @Override
            public void onFailed(HttpError error) {
                MyApplication.hideLoading();
                showToast(error.getErrMessage());
            }
        });
    }
}
