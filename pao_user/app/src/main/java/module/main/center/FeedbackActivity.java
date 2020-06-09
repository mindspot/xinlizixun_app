package module.main.center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.framework.http.bean.HttpError;
import com.framework.page.Page;
import com.framework.utils.StringUtil;
import com.paopao.paopaouser.R;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.param.user.FeedBackRequestBean;
import module.app.MyApplication;
import ui.title.ToolBarTitleView;

public class FeedbackActivity extends BaseActivity {

    @BindView(R.id.feedback_activity_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.feedback_activity_edittext)
    EditText edittext;

    public static void newIntent(Page page) {
        Intent intent = new Intent(page.activity(), FeedbackActivity.class);
        page.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_feedback;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

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

    @OnClick(R.id.feedback_activity_btn)
    public void onViewClicked() {
        if(isDoubleClick()){
            return;
        }
        uploadFeedBack();
    }

    public void uploadFeedBack() {
        String content = edittext.getText().toString();
        if (StringUtil.isBlank(content)) {
            showToast("请输入反馈内容");
            return;
        }
        MyApplication.loadingDefault(activity());
        FeedBackRequestBean bean = new FeedBackRequestBean();
        bean.setContent(content);
        HttpApi.app().uploadFeedback(this, bean, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                MyApplication.hideLoading();
                showToast("反馈成功");
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
