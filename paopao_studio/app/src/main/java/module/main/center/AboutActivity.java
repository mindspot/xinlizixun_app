package module.main.center;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.framework.http.bean.HttpError;
import com.framework.page.Page;
import com.framework.utils.StringUtil;
import com.paopao.paopaostudio.R;

import base.BaseActivity;
import butterknife.BindView;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import module.app.MyApplication;
import ui.title.ToolBarTitleView;

public class AboutActivity  extends BaseActivity {

    @BindView(R.id.about_activity_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.about_activity_title)
    TextView title;
    @BindView(R.id.about_activity_content)
    TextView content;
    @BindView(R.id.about_activity_footer)
    TextView footer;

    public static void newIntent(Page page) {
        Intent intent = new Intent(page.activity(), AboutActivity.class);
        page.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_about;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
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
        MyApplication.loadingDefault(activity());
        HttpApi.app().getCompanyInfo(this, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                MyApplication.hideLoading();
                if (StringUtil.isBlank(data)) {
                    showToast(message);
                    return;
                }
                JSONObject jsonObject = JSONObject.parseObject(data);
                title.setText(jsonObject.getString("clazz"));
                content.setText(jsonObject.getString("val"));
                footer.setText(jsonObject.getString("remarks"));
            }

            @Override
            public void onFailed(HttpError error) {
                MyApplication.hideLoading();
                showToast(error.getErrMessage());
            }
        });
    }
}
