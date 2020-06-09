package module.main.center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.framework.http.bean.HttpError;
import com.framework.page.Page;
import com.paopao.paopaouser.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import base.BaseActivity;
import butterknife.BindView;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.counsel.CounselListResponseBean;
import module.main.common.ExpertAdapter;
import ui.title.ToolBarTitleView;

public class CollectActivity extends BaseActivity {

    @BindView(R.id.collect_activity_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.collect_activity_listview)
    ListView listview;
    @BindView(R.id.collect_activity_refresh)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.empty_image)
    ImageView empty;

    private ExpertAdapter mAdapter;

    private int page = 1;

    public static void newIntent(Page page) {
        Intent intent = new Intent(page.activity(), CollectActivity.class);
        page.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_collect;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        refreshLayout.setEnableLoadMore(false);
        mAdapter = new ExpertAdapter(this);
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
        HttpApi.app().getCollectList(this, page, new HttpCallback<CounselListResponseBean>() {
            @Override
            public void onSuccess(int code, String message, CounselListResponseBean data) {
                empty.setVisibility(View.GONE);
                if (page == 1) {
                    refreshLayout.finishRefresh();
                    if (data.getList().isEmpty()) {
                        mAdapter.clear();
                        refreshLayout.setEnableLoadMore(false);
                        empty.setVisibility(View.VISIBLE);
                        return;
                    }
                    mAdapter.refresh(data.getList());
                } else {
                    refreshLayout.finishLoadMore();
                    mAdapter.append(data.getList());
                }
                refreshLayout.setEnableLoadMore(data.getRecordTotal() != mAdapter.getCount());
            }

            @Override
            public void onFailed(HttpError error) {
                if (page == 1) {
                    refreshLayout.finishRefresh();
                } else {
                    refreshLayout.finishLoadMore();
                }
                page--;
                if (page < 1) {
                    page = 1;
                }
                showToast(error.getErrMessage());
            }
        });
    }

}
