package module.main.center;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.framework.http.bean.HttpError;
import com.framework.page.Page;
import com.framework.utils.ConvertUtil;
import com.framework.utils.StringUtil;
import com.paopao.paopaouser.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.coupon.CouponItemBean;
import module.main.center.adapter.CouponInfoAdapter;
import ui.title.ToolBarTitleView;

public class CouponActivity extends BaseActivity {

    @BindView(R.id.coupon_info_activity_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.coupon_info_activity_listview)
    ListView listview;
    @BindView(R.id.coupon_info_activity_empty_layout)
    LinearLayout emptyLayout;
    @BindView(R.id.coupon_info_activity_refresh)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.coupon_info_activity_code)
    EditText codeEdit;
    @BindView(R.id.coupon_info_activity_code_btn)
    TextView codeBtn;

    private CouponInfoAdapter mAdapter;

    public static void newIntent(Page page) {
        Intent intent = new Intent(page.activity(), CouponActivity.class);
        page.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_coupon;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        refreshLayout.setEnableLoadMore(false);
        mAdapter = new CouponInfoAdapter(this);
        listview.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
        toolbar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                getData();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getData();
            }
        });
        codeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                codeBtn.setEnabled(s.toString().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void loadData() {
        refreshLayout.autoRefresh();
    }

    public void getData() {
        HttpApi.app().getCouponList(this, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                refreshLayout.finishRefresh();
                emptyLayout.setVisibility(View.GONE);
                if (StringUtil.isBlank(data)) {
                    emptyLayout.setVisibility(View.VISIBLE);
                    return;
                }
                List<CouponItemBean> list = ConvertUtil.toList(data, CouponItemBean.class);
                mAdapter.refresh(list);
                if (list == null || list.isEmpty()) {
                    emptyLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailed(HttpError error) {
                refreshLayout.finishRefresh();
                showToast(error.getErrMessage());
            }
        });
    }

    @OnClick(R.id.coupon_info_activity_code_btn)
    public void onViewClicked() {
        if (isDoubleClick()) {
            return;
        }
        if (StringUtil.isBlank(codeEdit.getText().toString())) {
            showToast("请输入兑换码！");
            return;
        }

    }

}
