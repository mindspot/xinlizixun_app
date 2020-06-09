package module.start;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.framework.http.bean.HttpError;
import com.framework.utils.StringUtil;
import com.paopao.paopaouser.BuildConfig;
import com.paopao.paopaouser.R;

import base.Base2Activity;
import base.UserCenter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.app.ServicePhoneResponseBean;
import common.repository.http.entity.app.ServiceUrlResponseBean;
import common.repository.share_preference.SPApi;
import module.app.MyApplication;
import module.main.MainActivity;
import module.user.LoginActivity;
import util.ServiceConfig;

public class StartActivity extends Base2Activity {

    @BindView(R.id.start_activity_inpuit_edit)
    EditText inpuitEdit;
    @BindView(R.id.start_activity_image)
    ImageView startActivityImage;

    private boolean isLoadPhone = false;
    private boolean isLoadUrl = false;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_start;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        UserCenter.instance().initUserInfo();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void loadData() {
        if (!BuildConfig.DEBUG) {
            getServicePhone();
            getServiceUrl();
            return;
        }
        startActivityImage.setVisibility(View.GONE);
        String url = SPApi.app().getConfigDeveloper();
        inpuitEdit.setText(url);
    }

    public void nextPage() {
        if (!isLoadPhone || !isLoadUrl) {
            return;
        }
        if (UserCenter.instance().getLoginStatus()) {
            MainActivity.newIntent(StartActivity.this);
        } else {
            LoginActivity.newIntent(StartActivity.this);
        }
        inpuitEdit.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 500);
    }

    @OnClick({R.id.start_activity_goto, R.id.start_activity_url_one, R.id.start_activity_url_three})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.start_activity_goto:
                if (StringUtil.isBlank(inpuitEdit.getText().toString())) {
                    showToast("访问地址无效！");
                    return;
                }
                setUrl(inpuitEdit.getText().toString());
                SPApi.app().setConfigDeveloper(inpuitEdit.getText().toString());
                break;
            case R.id.start_activity_url_one:
            case R.id.start_activity_url_three:
                setUrl(((TextView) view).getText().toString());
                break;
        }
    }

    public void setUrl(String url) {
        showToast(url);
        ServiceConfig.SERVICE_BASE_URL = url;
        getServicePhone();
        getServiceUrl();
    }

    public void getServicePhone() {
        HttpApi.app().getServicePhone(this, new HttpCallback<ServicePhoneResponseBean>() {
            @Override
            public void onSuccess(int code, String message, ServicePhoneResponseBean data) {
                MyApplication.app.setServicePhoneResponseBean(data);
                isLoadPhone = true;
                nextPage();
            }

            @Override
            public void onFailed(HttpError error) {
                isLoadPhone = true;
                nextPage();
            }
        });
    }

    public void getServiceUrl() {
        HttpApi.app().getServiceConfigUrl(this, new HttpCallback<ServiceUrlResponseBean>() {
            @Override
            public void onSuccess(int code, String message, ServiceUrlResponseBean data) {
                MyApplication.app.setServiceUrlResponseBean(data);
                isLoadUrl = true;
                nextPage();
            }

            @Override
            public void onFailed(HttpError error) {
                isLoadUrl = true;
                nextPage();
            }
        });
    }
}
