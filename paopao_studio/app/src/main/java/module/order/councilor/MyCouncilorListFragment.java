package module.order.councilor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.framework.http.bean.HttpError;
import com.paopao.paopaostudio.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import base.BaseFragment;
import butterknife.BindView;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.order.councilor.CouncilorResponseBean;
import common.repository.http.param.councilor.CouncilorRequestBean;

public class MyCouncilorListFragment extends BaseFragment {

    public static final int TYPE_ORDER_UNFINSH = 0;
    public static final int TYPE_ORDER_FINSH = 1;
    @BindView(R.id.my_councilor_list_listView)
    ListView listView;
    @BindView(R.id.my_councilor_list_refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.empty_image)
    ImageView empty;
    private int mType;

    private CouncilorListAdapter mAdapter;

    private int page = 1;

    public MyCouncilorListFragment() {
    }

    @SuppressLint("ValidFragment")
    public MyCouncilorListFragment(int type) {
        this.mType = type;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_my_councilor_list_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mAdapter = new CouncilorListAdapter(this, 1);
        listView.setAdapter(mAdapter);
        refresh.setEnableLoadMore(false);
    }

    @Override
    protected void initListener() {
        super.initListener();
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
    protected void loadData() {
        getData();
    }

    public void getData() {
        CouncilorRequestBean bean = new CouncilorRequestBean();
        bean.setPageNum(page);
        bean.setType(mType);
        HttpApi.app().getMyCouncilorList(this, bean, new HttpCallback<CouncilorResponseBean>() {
            @Override
            public void onSuccess(int code, String message, CouncilorResponseBean data) {
                empty.setVisibility(View.GONE);
                if (page == 1) {
                    refresh.finishRefresh();
                    if (data.getList().isEmpty()) {
                        mAdapter.clear();
                        refresh.setEnableLoadMore(false);
                        empty.setVisibility(View.VISIBLE);
                        return;
                    }
                    mAdapter.refresh(data.getList());
                } else {
                    refresh.finishLoadMore();
                    mAdapter.append(data.getList());
                }
                refresh.setEnableLoadMore(data.getRecordTotal() != mAdapter.getCount());
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
