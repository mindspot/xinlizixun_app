package module.main.center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.framework.http.bean.HttpError;
import com.framework.page.Page;
import com.framework.utils.ConvertUtil;
import com.paopao.paopaouser.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import base.BaseActivity;
import butterknife.BindView;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.order.SetMealItemBean;
import module.main.center.adapter.PurchasePlanAdapter;
import ui.title.ToolBarTitleView;

public class PurchasePlanActivity extends BaseActivity {

    @BindView(R.id.purchase_plan_activity_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.purchase_plan_activity_listview)
    ListView listview;
    @BindView(R.id.purchase_plan_activity_refresh)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.empty_image)
    ImageView empty;

    private PurchasePlanAdapter mAdapter;

    public static void newIntent(Page page) {
        Intent intent = new Intent(page.activity(), PurchasePlanActivity.class);
        page.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_purchase_plan;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        refreshLayout.setEnableLoadMore(false);
        mAdapter = new PurchasePlanAdapter(this);
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
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getData();
            }
        });
    }

    @Override
    public void loadData() {
        refreshLayout.autoRefresh();
    }

    public void getData() {
        HttpApi.app().getSetMealList(this, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                refreshLayout.finishRefresh();
                List<SetMealItemBean> list = ConvertUtil.toList(data, SetMealItemBean.class);
                mAdapter.refresh(list);
                empty.setVisibility(list == null || list.isEmpty() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onFailed(HttpError error) {
                refreshLayout.finishRefresh();
                showToast(error.getErrMessage());
            }
        });
    }
}
