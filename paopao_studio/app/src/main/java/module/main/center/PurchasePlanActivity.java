package module.main.center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.framework.http.bean.HttpError;
import com.framework.page.Page;
import com.paopao.paopaostudio.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import base.BaseActivity;
import butterknife.BindView;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.order.SetMealResponseBean;
import common.repository.http.param.user.PurchasePlanRequestBean;
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
    private int page = 1;
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
                page++;
                getData();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                page = 1;
                getData();
            }
        });
    }

    @Override
    public void loadData() {
        refreshLayout.autoRefresh();
    }

    public void getData() {
        PurchasePlanRequestBean bean = new PurchasePlanRequestBean();
        bean.setPageNum(page);
        HttpApi.app().getSetMealList(this, bean, new HttpCallback<SetMealResponseBean>() {
            @Override
            public void onSuccess(int code, String message, SetMealResponseBean data) {
                empty.setVisibility(View.GONE);
                if (page == 1) {
                    refreshLayout.finishRefresh();
                    mAdapter.refresh(data.getList());
                    if (data.getList().isEmpty()) {
                        empty.setVisibility(View.VISIBLE);
                    }
                } else {
                    refreshLayout.finishLoadMore();
                    mAdapter.append(data.getList());
                }
                refreshLayout.setEnableLoadMore(mAdapter.getCount() != data.getTotal());
            }

            @Override
            public void onFailed(HttpError error) {
                if (page == 1) {
                    refreshLayout.finishRefresh();
                } else {
                    refreshLayout.finishLoadMore();
                }
                showToast(error.getErrMessage());
                page--;
                if (page < 1) {
                    page = 1;
                }
            }
        });
    }
}
