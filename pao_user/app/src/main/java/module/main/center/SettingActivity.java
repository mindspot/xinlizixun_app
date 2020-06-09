package module.main.center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.framework.page.Page;
import com.framework.utils.StringUtil;
import com.paopao.paopaouser.R;

import base.BaseActivity;
import base.UserCenter;
import butterknife.BindView;
import butterknife.OnClick;
import common.repository.share_preference.SPApi;
import module.user.ChangePassCodeActivity;
import ui.title.ToolBarTitleView;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.setting_activity_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.setting_activity_account_text)
    TextView accountText;

    private String mPhone;

    public static void newIntent(Page page) {
        Intent intent = new Intent(page.activity(), SettingActivity.class);
        page.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (getIntent() != null) {
            mPhone = SPApi.user().getUserPhone();
            accountText.setText(StringUtil.changeMobile(mPhone));
        }
    }

    @Override
    public void initListener() {
        toolbar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void loadData() {

    }

    @OnClick({R.id.setting_activity_changepass_layout, R.id.setting_activity_switchaccount_layout, R.id.setting_activity_logout_btn})
    public void onViewClicked(View view) {
        if(isDoubleClick()){
            return;
        }
        switch (view.getId()) {
            case R.id.setting_activity_changepass_layout:
                ChangePassCodeActivity.newIntent(this, mPhone);
                break;
            case R.id.setting_activity_switchaccount_layout:
                break;
            case R.id.setting_activity_logout_btn:
                UserCenter.instance().logout(this);
                break;
        }
    }
}
