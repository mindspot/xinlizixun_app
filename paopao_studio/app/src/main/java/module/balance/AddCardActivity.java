package module.balance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.framework.http.bean.HttpError;
import com.framework.page.Page;
import com.framework.utils.StringUtil;
import com.paopao.paopaostudio.R;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.balance.CardItemBean;
import common.repository.http.param.balance.SaveCardRequestBean;
import module.app.MyApplication;
import ui.title.ToolBarTitleView;

public class AddCardActivity extends BaseActivity {

    @BindView(R.id.add_card_activity_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.add_card_activity_account)
    EditText account;
    @BindView(R.id.add_card_activity_username)
    EditText username;

    private CardItemBean mCard;

    public static void newIntent(Page page) {
        page.startActivity(new Intent(page.activity(), AddCardActivity.class));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_add_card;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mCard = MyApplication.app.getCardItemBean();
        if (mCard != null) {
            username.setText(mCard.getRealName());
            username.setSelection(username.getText().length());
            account.setText(mCard.getAlipayAccount());
            account.setSelection(account.getText().length());
        }
    }

    @Override
    public void initListener() {
        toolbar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void loadData() {

    }

    @OnClick(R.id.add_card_activity_confirm)
    public void onViewClicked() {
        if (isDoubleClick()) {
            return;
        }
        if (StringUtil.isBlank(account.getText().toString()) || StringUtil.isBlank(username.getText().toString())) {
            showToast("请将信息填写完整");
            return;
        }
        MyApplication.loadingDefault(activity());
        SaveCardRequestBean bean = new SaveCardRequestBean();
        bean.setAlipayAccount(account.getText().toString());
        bean.setRealName(username.getText().toString());
        bean.setType(mCard == null ? 0 : 1);
        HttpApi.app().saveUserCard(this, bean, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                showToast(message);
                MyApplication.hideLoading();
                if (MyApplication.app.getCardItemBean() == null) {
                    MyApplication.app.setCardItemBean(new CardItemBean());
                }
                MyApplication.app.getCardItemBean().setAlipayAccount(account.getText().toString());
                MyApplication.app.getCardItemBean().setRealName(username.getText().toString());
                finish();
            }

            @Override
            public void onFailed(HttpError error) {
                showToast(error.getErrMessage());
                MyApplication.hideLoading();
            }
        });
    }
}
