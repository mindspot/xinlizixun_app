package module.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.page.Page;
import com.framework.utils.ConvertUtil;
import com.framework.utils.ViewUtil;
import com.paopao.paopaouser.R;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import ui.title.ToolBarTitleView;
import util.CountDownUtil;

public class ChangePassCodeActivity extends BaseActivity {

    @BindView(R.id.change_pass_code_activity_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.change_pass_code_activity_phone_text)
    TextView phoneText;
    @BindView(R.id.change_pass_code_activity_code_one_text)
    TextView codeOneText;
    @BindView(R.id.change_pass_code_activity_code_two_text)
    TextView codeTwoText;
    @BindView(R.id.change_pass_code_activity_code_three_text)
    TextView codeThreeText;
    @BindView(R.id.change_pass_code_activity_code_four_text)
    TextView codeFourText;
    @BindView(R.id.change_pass_code_activity_getcode_btn)
    TextView getcodeBtn;
    @BindView(R.id.change_pass_code_activity_code_edittext)
    EditText codeEdittext;
    @BindView(R.id.change_pass_code_activity_code_layout)
    LinearLayout codeLayout;
    @BindView(R.id.change_pass_code_activity_pass_one_edit)
    EditText passOneEdit;
    @BindView(R.id.change_pass_code_activity_pass_one_look)
    ImageView passOneLook;
    @BindView(R.id.change_pass_code_activity_pass_two_edit)
    EditText passTwoEdit;
    @BindView(R.id.change_pass_code_activity_pass_two_look)
    ImageView passTwoLook;
    @BindView(R.id.change_pass_code_activity_pass_layout)
    LinearLayout passLayout;

    private String mPhone;

    private boolean isShowCode = true;

    private boolean isShowPassOne = false;
    private boolean isShowPassTwo = false;

    public static void newIntent(Page page, String phone) {
        Intent intent = new Intent(page.activity(), ChangePassCodeActivity.class);
        intent.putExtra("phone", phone);
        page.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_change_pass_code;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (getIntent() != null) {
            mPhone = getIntent().getStringExtra("phone");
            phoneText.setText("为了您的账号安全，我们已向您" + mPhone + "的手机\\n发送验证码");
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
        codeEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                codeOneText.setText("");
                codeTwoText.setText("");
                codeThreeText.setText("");
                codeFourText.setText("");
                codeOneText.setBackgroundResource(R.drawable.change_pass_input_code_background_share);
                codeTwoText.setBackgroundResource(R.drawable.change_pass_input_code_background_share);
                codeThreeText.setBackgroundResource(R.drawable.change_pass_input_code_background_share);
                codeFourText.setBackgroundResource(R.drawable.change_pass_input_code_background_share);
                switch (codeEdittext.getText().length()) {
                    case 0:
                        codeOneText.setBackgroundResource(R.drawable.change_pass_input_code_theme_background_share);
                        break;
                    case 1:
                        codeTwoText.setBackgroundResource(R.drawable.change_pass_input_code_theme_background_share);
                        break;
                    case 2:
                        codeThreeText.setBackgroundResource(R.drawable.change_pass_input_code_theme_background_share);
                        break;
                    case 3:
                        codeFourText.setBackgroundResource(R.drawable.change_pass_input_code_theme_background_share);
                        break;
                }
                if (codeEdittext.getText().length() == 0) {
                    return;
                }
                char[] ca = codeEdittext.getText().toString().trim().toCharArray();
                if (ca.length >= 1) {
                    codeOneText.setText(String.valueOf(ca[0]));
                }
                if (ca.length >= 2) {
                    codeTwoText.setText(String.valueOf(ca[1]));
                }
                if (ca.length >= 3) {
                    codeThreeText.setText(String.valueOf(ca[2]));
                }
                if (ca.length >= 4) {
                    codeFourText.setText(String.valueOf(ca[3]));
                }
            }
        });
    }

    @Override
    public void loadData() {

    }

    @OnClick({R.id.change_pass_code_activity_getcode_btn,
            R.id.change_pass_code_activity_pass_one_look,
            R.id.change_pass_code_activity_pass_two_look})
    public void onViewClicked(View view) {
        if(isDoubleClick()){
            return;
        }
        switch (view.getId()) {
            case R.id.change_pass_code_activity_getcode_btn:
                getCode();
                break;
            case R.id.change_pass_code_activity_pass_one_look:
                isShowPassOne = !isShowPassOne;
                passOneLook.setImageDrawable(isShowPassOne ?
                        ConvertUtil.getDrawable(context(), R.mipmap.common_look_password_open) :
                        ConvertUtil.getDrawable(context(), R.mipmap.common_look_password_close));
                ViewUtil.passShow(passOneEdit, isShowPassOne);
                break;
            case R.id.change_pass_code_activity_pass_two_look:
                isShowPassTwo = !isShowPassTwo;
                passTwoLook.setImageDrawable(isShowPassTwo ?
                        ConvertUtil.getDrawable(context(), R.mipmap.common_look_password_open) :
                        ConvertUtil.getDrawable(context(), R.mipmap.common_look_password_close));
                ViewUtil.passShow(passTwoEdit, isShowPassTwo);
                break;
        }
    }

    public void getCode() {
        CountDownUtil.startCountDown(getcodeBtn);
    }

    @OnClick(R.id.change_pass_code_activity_pass_one_look)
    public void onViewClicked() {
    }
}
