package module.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.http.bean.HttpError;
import com.framework.page.Page;
import com.framework.utils.StringUtil;
import com.hyphenate.easeui.sqlite.UserTabUtil;
import com.paopao.paopaouser.R;

import base.Base2Activity;
import base.UserCenter;
import butterknife.BindView;
import butterknife.OnClick;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.user.UserInfoBean;
import common.repository.http.param.app.GetCodeRequestBean;
import common.repository.http.param.app.LoginRequestBean;
import common.repository.share_preference.SPApi;
import module.app.MyApplication;
import module.dialog.AgreementDialog;
import module.dialog.ServiceDialog;
import module.main.MainActivity;
import util.CountDownUtil;

public class LoginActivity extends Base2Activity {

    @BindView(R.id.login_activity_phone_edittext)
    EditText phoneEdittext;
    @BindView(R.id.login_activity_phone_bg_layout)
    LinearLayout phoneBgLayout;
    @BindView(R.id.login_activity_code_edittext)
    EditText codeEdittext;
    @BindView(R.id.login_activity_code_bg_layout)
    LinearLayout codeBgLayout;
    @BindView(R.id.login_activity_getcode_btn)
    TextView getCodeBtn;

    private ServiceDialog serviceDialog;

    public static void newIntent(Page page) {
        page.startActivity(new Intent(page.activity(), LoginActivity.class));
    }

    @Override
    protected int getContentViewId() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return R.layout.activity_login;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        serviceDialog = new ServiceDialog(this);
    }

    @Override
    public void initListener() {
        phoneEdittext.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    phoneBgLayout.setBackgroundResource(R.drawable.login_input_background_select_share);
                } else {
                    // 此处为失去焦点时的处理内容
                    phoneBgLayout.setBackgroundResource(R.drawable.login_input_background_unselect_share);
                }
            }
        });
        codeEdittext.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    codeBgLayout.setBackgroundResource(R.drawable.login_input_background_select_share);
                } else {
                    // 此处为失去焦点时的处理内容
                    codeBgLayout.setBackgroundResource(R.drawable.login_input_background_unselect_share);
                }
            }
        });
    }

    @Override
    public void loadData() {

    }

    @OnClick({
            R.id.login_activity_service_btn, R.id.login_activity_help_btn,
            R.id.login_activity_getcode_btn, R.id.login_activity_login_btn,
            R.id.login_activity_ruzhu_btn, R.id.login_activity_reg_agreement,
            R.id.login_activity_pri_agreement})
    public void onViewClicked(View view) {
        if (isDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.login_activity_service_btn:
                serviceDialog.showMyDialog();
                break;
            case R.id.login_activity_help_btn:
                toWebViewActivity(MyApplication.app.getServiceUrlResponseBean().getSetInUrl().getVal());
                break;
            case R.id.login_activity_getcode_btn:
                gotoAuthCode();
                break;
            case R.id.login_activity_login_btn:
                gotoLogin();
                break;
            case R.id.login_activity_ruzhu_btn:
                break;
            case R.id.login_activity_reg_agreement:
                toWebViewActivity("file:///android_asset/register_agreement.htm");
                break;
            case R.id.login_activity_pri_agreement:
                toWebViewActivity("file:///android_asset/privacy_agreement.htm");
                break;
        }
    }

    public void gotoAuthCode() {
        String mobile = phoneEdittext.getText().toString();
        if (!StringUtil.isMobileNO(mobile)) {
            showToast("请输入正确的手机号");
            return;
        }
        MyApplication.loadingDefault(activity());
        GetCodeRequestBean bean = new GetCodeRequestBean();
        bean.setPhone(mobile);
        HttpApi.app().getCode(this, bean, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                MyApplication.hideLoading();
                CountDownUtil.startCountDown(getCodeBtn);
            }

            @Override
            public void onFailed(HttpError error) {
                showToast(error.getErrMessage());
                MyApplication.hideLoading();
            }
        });
    }

    public void gotoLogin() {
        String mobile = phoneEdittext.getText().toString();
        if (!StringUtil.isMobileNO(mobile)) {
            showToast("请输入正确的手机号");
            return;
        }
        String code = codeEdittext.getText().toString();
        if (StringUtil.isBlank(code)) {
            showToast("请输入验证码");
            return;
        }
        MyApplication.loadingDefault(activity());
        LoginRequestBean bean = new LoginRequestBean();
        bean.setPhone(mobile);
        bean.setCode(code);
        HttpApi.app().gotoLogin(this, bean, new HttpCallback<UserInfoBean>() {
            @Override
            public void onSuccess(int code, String message, UserInfoBean data) {
                MyApplication.hideLoading();
                UserTabUtil.uploadUser(data.getEasemobId(), data.getRealName(), data.getHeadImg(), true);
                SPApi.user().setLoginRealName(data.getRealName());
                SPApi.user().setUserPhoto(data.getHeadImg());
                SPApi.user().setUserPhone(phoneEdittext.getText().toString());
                data.setPhone(phoneEdittext.getText().toString());
                UserCenter.instance().saveUserInfo(LoginActivity.this, data);
                Intent intent = new Intent(activity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(HttpError error) {
                showToast(error.getErrMessage());
                MyApplication.hideLoading();
            }
        });
    }

    AgreementDialog dialog;

    @Override
    protected void onResume() {
        super.onResume();
        if (dialog == null) {
            dialog = new AgreementDialog(this);
        }
        dialog.showMyDialog();
    }
}
