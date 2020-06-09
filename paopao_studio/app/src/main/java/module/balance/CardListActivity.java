package module.balance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.framework.page.Page;
import com.paopao.paopaostudio.R;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import common.repository.http.entity.balance.CardItemBean;
import module.app.MyApplication;
import ui.title.ToolBarTitleView;

public class CardListActivity extends BaseActivity {

    @BindView(R.id.card_list_activity_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.card_list_activity_username)
    TextView username;
    @BindView(R.id.card_list_activity_account)
    TextView account;

    private CardItemBean mCard;

    public static void newIntent(Page page) {
        Intent intent = new Intent(page.activity(), CardListActivity.class);
        page.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_card_list;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCard = MyApplication.app.getCardItemBean();
        if (mCard == null) {
            finish();
            return;
        }
        username.setText(mCard.getRealName());
        account.setText(mCard.getAlipayAccount());
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

    @OnClick(R.id.card_list_activity_root)
    public void onViewClicked() {
        if (isDoubleClick()) {
            return;
        }
        AddCardActivity.newIntent(CardListActivity.this);
    }
}
