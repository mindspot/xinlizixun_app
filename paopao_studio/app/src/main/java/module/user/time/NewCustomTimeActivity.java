package module.user.time;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.framework.http.bean.HttpError;
import com.framework.page.Page;
import com.framework.utils.ConvertUtil;
import com.paopao.paopaostudio.R;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.time.SystemPlanTimeItemBean;
import common.repository.http.param.BaseRequestBean;
import common.repository.http.param.time.SaveCustomTimeRequestBean;
import common.repository.http.param.time.UploadCustomTimeItemBean;
import module.app.MyApplication;
import ui.MyGridView;
import ui.title.ToolBarTitleView;

public class NewCustomTimeActivity extends BaseActivity {

    @BindView(R.id.custom_time_activity_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.work_plan_activity_gridview)
    MyGridView gridview;

    private CustomTimeAdapter mAdapter;

    public static final int KEY_ACTIVITY_CODE = 1001;

    public static void newIntent(Page page) {
        page.startActivityForResult(new Intent(page.activity(), NewCustomTimeActivity.class), KEY_ACTIVITY_CODE);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_new_custom_time;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mAdapter = new CustomTimeAdapter(this);
        gridview.setAdapter(mAdapter);
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
        HttpApi.app().getSystemPalnTime(this, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                MyApplication.hideLoading();
                List<SystemPlanTimeItemBean> list = ConvertUtil.toList(data, SystemPlanTimeItemBean.class);
                mAdapter.refresh(list);
            }

            @Override
            public void onFailed(HttpError error) {
                MyApplication.hideLoading();
                showToast(error.getErrMessage());
            }
        });
    }

    @OnClick(R.id.custom_time_activity_save_btn)
    public void onViewClicked() {
        if (isDoubleClick()) {
            return;
        }
        saveInfo();
    }

    public void saveInfo() {
        MyApplication.loadingDefault(activity());
        SaveCustomTimeRequestBean bean = new SaveCustomTimeRequestBean();
        bean.setBody(ConvertUtil.toJsonString(mAdapter.getDatas()));
        HttpApi.app().savePlanTime(this, bean, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                MyApplication.hideLoading();
                showToast(message);
                setResult(RESULT_OK);
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
