package module.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.framework.http.bean.HttpError;
import com.framework.page.Page;
import com.framework.utils.StringUtil;
import com.paopao.paopaostudio.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.Arrays;
import java.util.List;

import base.BaseActivity;
import butterknife.BindView;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.order.UseCouncilorItemBean;
import common.repository.http.entity.order.UseCouncilorResponseBean;
import common.repository.http.entity.order.councilor.NowUserOrderDetailItemBean;
import common.repository.http.entity.order.user.UserOrderDetailInfoBean;
import common.repository.http.param.order.UseCouncilorListRequestBean;
import module.app.MyApplication;
import module.order.user.UserOrderDetailActivity;
import ui.CustomClickListener;
import ui.title.ToolBarTitleView;

public class UseCouncilorListActivity extends BaseActivity {

    @BindView(R.id.use_councilor_list_activity_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.use_councilor_list_activity_listView)
    ListView listView;
    @BindView(R.id.use_councilor_list_activity_refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.empty_image)
    ImageView empty;

    private UseCouncilorListAdapter mAdapter;

    private int page = 1;

    public static final int KEY_ACTIVITY_CODE = 2001;

    private boolean isOrderList = false;

    private String orderNos;

    private int listSize;

    private boolean hideChat = false;

    public static void newIntent(Page page, String orderNos) {
        Intent intent = new Intent(page.activity(), UseCouncilorListActivity.class);
        intent.putExtra("orderNos", orderNos);
        page.startActivityForResult(intent, KEY_ACTIVITY_CODE);
    }

    public static void newIntentOrderList(Page page) {
        Intent intent = new Intent(page.activity(), UseCouncilorListActivity.class);
        intent.putExtra("isOrderList", true);
        intent.putExtra("hideChat", true);
        page.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_use_councilor_list;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (getIntent() != null) {
            isOrderList = getIntent().getBooleanExtra("isOrderList", isOrderList);
            orderNos = getIntent().getStringExtra("orderNos");
            hideChat = getIntent().getBooleanExtra("hideChat", hideChat);
        }
        refresh.setEnableLoadMore(false);
        mAdapter = new UseCouncilorListAdapter(this);
        listView.setAdapter(mAdapter);
        if (isOrderList) {
            for (NowUserOrderDetailItemBean item : MyApplication.app.getCouncilorList()) {
                UserOrderDetailInfoBean userItem = item.getOrderInfo();
                UseCouncilorItemBean bean = new UseCouncilorItemBean();
                bean.setExt(userItem.getExt());
                bean.setGoodsClassName(userItem.getGoodsClassName());
                bean.setHeadImg(userItem.getHeadImg());
                bean.setOrderAmount(userItem.getOrderAmount());
                bean.setOrderCode(userItem.getOrderCode());
                bean.setOrderNo(userItem.getOrderNo());
                bean.setRealName(userItem.getRealName());
                mAdapter.addData(bean);
            }
        }
    }

    @Override
    public void initListener() {
        if (!isOrderList)
            refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(RefreshLayout refreshLayout) {
                    page++;
                    getData();
                }

                @Override
                public void onRefresh(RefreshLayout refreshLayout) {
                    page = 1;
                    listSize = 0;
                    getData();
                }
            });
        mAdapter.setOnItemListener(new UseCouncilorListAdapter.OnItemListener() {
            @Override
            public void OnClick(UseCouncilorItemBean data) {
                if (isOrderList) {
                    UserOrderDetailActivity.newIntent(UseCouncilorListActivity.this, data.getOrderNo(), !hideChat);
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("mData", data);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        toolbar.setLeftClickListener(new CustomClickListener() {
            @Override
            protected void onClick() {
                finish();
            }
        });
    }

    @Override
    public void loadData() {
        if (!isOrderList)
            refresh.autoRefresh();
    }

    public void getData() {
        UseCouncilorListRequestBean bean = new UseCouncilorListRequestBean();
        bean.setPageNum(page);
        HttpApi.app().getUseCouncilorList(this, bean, new HttpCallback<UseCouncilorResponseBean>() {
            @Override
            public void onSuccess(int code, String message, UseCouncilorResponseBean data) {
                empty.setVisibility(View.GONE);
                if (page == 1) {
                    refresh.finishRefresh();
                    if (data.getList().isEmpty()) {
                        mAdapter.clear();
                        refresh.setEnableLoadMore(false);
                        empty.setVisibility(View.VISIBLE);
                        listSize = 0;
                        return;
                    }
                    listSize = data.getList().size();
                    mAdapter.refresh(getShowList(data.getList()));
                } else {
                    listSize += data.getList().size();
                    refresh.finishLoadMore();
                    mAdapter.append(getShowList(data.getList()));
                }
                if (mAdapter.getCount() == 0) {
                    empty.setVisibility(View.VISIBLE);
                }
                refresh.setEnableLoadMore(data.getTotal() != listSize);
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

    public List<UseCouncilorItemBean> getShowList(List<UseCouncilorItemBean> list) {
        if (!StringUtil.isBlank(orderNos)) {
            List<String> orderList = Arrays.asList(orderNos.split(","));
            for (int i = 0; i < list.size(); i++) {
                if (orderList.contains(list.get(i).getOrderNo())) {
                    list.remove(i);
                    i--;
                }
            }
        }
        return list;
    }
}
