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
import common.repository.http.entity.order.ConsumeRecordResponseBean;
import common.repository.http.param.order.ConsumeRecordRequestBean;
import module.main.center.adapter.ConsumeRecordInfoAdapter;
import ui.title.ToolBarTitleView;

public class ConsumeRecordActivity extends BaseActivity {

    @BindView(R.id.counsume_record_activity_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.empty_image)
    ImageView empty;
    @BindView(R.id.counsume_record_activity_listview)
    ListView listview;
    @BindView(R.id.counsume_record_activity_refresh)
    SmartRefreshLayout refreshLayout;

    private ConsumeRecordInfoAdapter mAdapter;

    private int page = 1;

    public static void newIntent(Page page) {
        page.startActivity(new Intent(page.activity(), ConsumeRecordActivity.class));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_consume_record;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        refreshLayout.setEnableLoadMore(false);
        mAdapter = new ConsumeRecordInfoAdapter(this);
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
        ConsumeRecordRequestBean bean = new ConsumeRecordRequestBean();
        bean.setPageNo(page);
        bean.setPageSize(10);
        bean.setType(1);
        HttpApi.app().getCounsumeRecordList(this, bean, new HttpCallback<ConsumeRecordResponseBean>() {
            @Override
            public void onSuccess(int code, String message, ConsumeRecordResponseBean data) {
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
                refreshLayout.setEnableLoadMore(data.getList().size() == 10);
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
