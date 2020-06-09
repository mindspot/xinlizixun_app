package module.main.center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.framework.http.bean.HttpError;
import com.framework.page.Page;
import com.framework.utils.ConvertUtil;
import com.paopao.paopaostudio.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import base.BaseActivity;
import butterknife.BindView;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.app.VisitorRecardItemBean;
import common.repository.http.entity.balance.RecardItemBean;
import common.repository.http.param.app.VisitorRecordRequestBean;
import module.main.center.adapter.VisitorRecordAdapter;
import ui.CustomClickListener;
import ui.title.ToolBarTitleView;

public class VisitorRecordActivity extends BaseActivity {
    @BindView(R.id.visitor_record_activity_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.visitor_record_activity_listView)
    ListView listView;
    @BindView(R.id.visitor_record_activity_refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.empty_image)
    ImageView empty;

    private VisitorRecordAdapter mAdapter;

    private int page = 1;
    private final int PAGE_DEFULT = 10;

    public static void newIntent(Page page) {
        page.startActivity(new Intent(page.activity(), VisitorRecordActivity.class));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_visitor_record;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mAdapter = new VisitorRecordAdapter(this);
        listView.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
        toolbar.setLeftClickListener(new CustomClickListener() {
            @Override
            protected void onClick() {
                finish();
            }
        });
        refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
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
        refresh.autoRefresh();
    }

    public void getData() {
        VisitorRecordRequestBean bean = new VisitorRecordRequestBean();
        bean.setPageNo(page);
        bean.setPageSize(PAGE_DEFULT);
        HttpApi.app().getVisitorRecord(this, bean, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                empty.setVisibility(View.GONE);
                List<VisitorRecardItemBean> list = ConvertUtil.toList(data, VisitorRecardItemBean.class);
                if (page == 1) {
                    refresh.finishRefresh();
                    if (list.isEmpty()) {
                        mAdapter.clear();
                        empty.setVisibility(View.VISIBLE);
                    } else {
                        mAdapter.refresh(list);
                    }
                } else {
                    refresh.finishLoadMore();
                    mAdapter.append(list);
                }
                refresh.setEnableLoadMore(list.isEmpty() ? false : true);
            }

            @Override
            public void onFailed(HttpError error) {
                if (page == 1) {
                    refresh.finishRefresh();
                } else {
                    refresh.finishLoadMore();
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
