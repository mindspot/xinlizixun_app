package module.order.user;

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
import common.repository.http.entity.order.UseCouncilorItemBean;
import common.repository.http.entity.order.UseCouncilorResponseBean;
import common.repository.http.param.order.UserOrderRequestBean;

public class UserOrderFragment extends BaseFragment {

    public static final int TYPE_ORDER_UNFINSH = 0;
    public static final int TYPE_ORDER_FINSH = 1;
    @BindView(R.id.user_order_fragment_listView)
    ListView listView;
    @BindView(R.id.user_order_fragment_refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.empty_image)
    ImageView empty;

    private int mType;

    private UserOrderListAdapter mAdapter;

    private int page = 1;

    public UserOrderFragment() {
    }

    @SuppressLint("ValidFragment")
    public UserOrderFragment(int type) {
        this.mType = type;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_user_order_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mAdapter = new UserOrderListAdapter(this);
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
        mAdapter.setOnItemListener(new UserOrderListAdapter.OnItemListener() {
            @Override
            public void OnClick(UseCouncilorItemBean data) {
                UserOrderDetailActivity.newIntent(context(), data.getOrderNo());
            }
        });
    }

    @Override
    protected void loadData() {
        getData();
    }

    public void getData() {
        UserOrderRequestBean bean = new UserOrderRequestBean();
        bean.setPageNum(page);
        bean.setType(mType);
        HttpApi.app().getUserOrderList(this, bean, new HttpCallback<UseCouncilorResponseBean>() {
            @Override
            public void onSuccess(int code, String message, UseCouncilorResponseBean data) {
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
                refresh.setEnableLoadMore(data.getTotal() != mAdapter.getCount());
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
