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
import common.events.PayResultEvent;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.order.OrderListItemBean;
import common.repository.http.entity.order.OrderListResponseBean;
import common.repository.http.entity.user.UserInfoResponseBean;
import de.greenrobot.event.EventBus;
import module.dialog.SelectPayDialog;
import module.main.center.adapter.OrderInfoAdapter;
import ui.title.ToolBarTitleView;

public class OrderInfoActivity extends BaseActivity {

    @BindView(R.id.order_info_activity_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.order_info_activity_listview)
    ListView listview;
    @BindView(R.id.order_info_activity_refresh)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.empty_image)
    ImageView empty;

    private int page = 1;

    private OrderInfoAdapter mAdapter;

    SelectPayDialog payDialog;

    private int mAccount;

    public static void newIntent(Page page) {
        Intent intent = new Intent(page.activity(), OrderInfoActivity.class);
        page.startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAccount();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_order_info;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        refreshLayout.setEnableLoadMore(false);
        mAdapter = new OrderInfoAdapter(this);
        listview.setAdapter(mAdapter);
        payDialog = new SelectPayDialog(this);
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
        mAdapter.setItemClickListener(new OrderInfoAdapter.OnItemClickListener() {
            @Override
            public void OnClick(OrderListItemBean data) {
                payDialog.setAccount(mAccount);
                payDialog.setData(data);
            }
        });
    }

    @Override
    public void loadData() {
        refreshLayout.autoRefresh();
    }

    public void getData() {
        HttpApi.app().getOrderList(this, page, new HttpCallback<OrderListResponseBean>() {
            @Override
            public void onSuccess(int code, String message, OrderListResponseBean data) {
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

    public void onEventMainThread(PayResultEvent event) {
        switch (event.getType()) {
            case 0:
                showToast("支付成功");
                page = 1;
                refreshLayout.autoRefresh();
                getAccount();
                break;
            case -1:
                showToast("支付失败");
                break;
            case -2:
                showToast("取消支付");
                break;
        }
    }

    public void getAccount() {
        HttpApi.app().getUserInfo(this, new HttpCallback<UserInfoResponseBean>() {
            @Override
            public void onSuccess(int code, String message, UserInfoResponseBean data) {
                if (data != null) {
                    mAccount = data.getAccount();
                    return;
                }
                mAccount = 0;
            }

            @Override
            public void onFailed(HttpError error) {
            }
        });
    }
}
