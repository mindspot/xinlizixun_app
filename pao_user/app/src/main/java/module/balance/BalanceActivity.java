package module.balance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.framework.http.bean.HttpError;
import com.framework.page.Page;
import com.framework.utils.ConvertUtil;
import com.paopao.paopaouser.R;

import java.util.List;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.balance.CardItemBean;
import module.app.MyApplication;
import module.main.center.ConsumeRecordActivity;
import ui.CustomClickListener;
import ui.title.ToolBarTitleView;
import util.MathOperationUtil;

public class BalanceActivity extends BaseActivity {

    @BindView(R.id.order_fragment_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.balance_activity_money)
    TextView money;

    private int mMoney;

    private String mTip;

    public static void newIntent(Page page) {
        page.startActivity(new Intent(page.activity(), BalanceActivity.class));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_balance;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {
        toolbar.setLeftClickListener(new CustomClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void loadData() {
    }

    public void getData() {
        MyApplication.loadingDefault(activity());
        HttpApi.app().getUserBalance(this, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                MyApplication.hideLoading();
                try {
                    JSONObject jsonObject = JSONObject.parseObject(data);
                    mMoney = jsonObject.getIntValue("account");
                    mTip = jsonObject.getString("cashWithdrawalRemarks");
                } catch (Exception ex) {
                    mMoney = 0;
                }
                money.setText(MathOperationUtil.divStr(mMoney, 100));
            }

            @Override
            public void onFailed(HttpError error) {
                MyApplication.hideLoading();
                showToast(error.getErrMessage());
            }
        });
    }

    @OnClick({R.id.balance_activity_tixian_layout, R.id.balance_activity_addcard_layout,
            R.id.balance_activity_record_layout})
    public void onViewClicked(View view) {
        if (isDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.balance_activity_tixian_layout:
                getCardList(1);
                break;
            case R.id.balance_activity_addcard_layout:
                getCardList(2);
                break;
            case R.id.balance_activity_record_layout:
                ConsumeRecordActivity.newIntent(this);
                break;
        }
    }

    public void getCardList(int type) {
        MyApplication.loadingDefault(activity());
        HttpApi.app().getUserCardList(this, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                MyApplication.hideLoading();
                List<CardItemBean> list = ConvertUtil.toList(data, CardItemBean.class);
                if (list == null || list.isEmpty()) {
                    MyApplication.app.setCardItemBean(null);
                    AddCardActivity.newIntent(BalanceActivity.this);
                } else {
                    MyApplication.app.setCardItemBean(list.get(0));
                    if (type == 1) {
                        WithdrawActivity.newIntent(BalanceActivity.this, mMoney, mTip);
                    } else if (type == 2) {
                        CardListActivity.newIntent(BalanceActivity.this);
                    }
                }
            }

            @Override
            public void onFailed(HttpError error) {
                showToast(error.getErrMessage());
                MyApplication.hideLoading();
            }
        });
    }
}
