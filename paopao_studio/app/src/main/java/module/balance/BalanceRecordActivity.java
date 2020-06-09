package module.balance;

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
import common.repository.http.entity.balance.RecardItemBean;
import common.repository.http.entity.balance.RecardResponseBean;
import common.repository.http.param.balance.BalanceRecordRequestBean;
import ui.title.ToolBarTitleView;

public class BalanceRecordActivity extends BaseActivity {

    @BindView(R.id.balance_record_activity_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.balance_record_activity_listView)
    ListView listView;
    @BindView(R.id.balance_record_activity_refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.empty_image)
    ImageView empty;

    private BalanceRecordAdapter mAdapter;

    private int page = 1;

    public static void newIntent(Page page) {
        page.startActivity(new Intent(page.activity(), BalanceRecordActivity.class));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_balance_record;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mAdapter = new BalanceRecordAdapter(this);
        listView.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
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
        toolbar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void loadData() {
        refresh.autoRefresh();
    }

    public void getData() {
        BalanceRecordRequestBean bean = new BalanceRecordRequestBean();
        bean.setPageNum(page);
        HttpApi.app().getBalanceRecord(this, bean, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                empty.setVisibility(View.GONE);
                List<RecardItemBean> list = ConvertUtil.toList(data, RecardItemBean.class);
                if (page == 1) {
                    refresh.finishRefresh();
                    if (list.isEmpty()) {
                        mAdapter.clear();
                        refresh.setEnableLoadMore(false);
                        empty.setVisibility(View.VISIBLE);
                        return;
                    }
                    mAdapter.refresh(list);
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
