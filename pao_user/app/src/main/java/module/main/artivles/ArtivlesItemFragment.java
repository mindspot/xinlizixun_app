package module.main.artivles;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.framework.http.bean.HttpError;
import com.paopao.paopaouser.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import base.BaseFragment;
import butterknife.BindView;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.artivles.ArtivlesResponseBean;
import common.repository.http.param.artivles.ArtivlesRequestBean;

/**
 * Created by hpzhan on 2020/2/18.
 */

public class ArtivlesItemFragment extends BaseFragment {


    @BindView(R.id.fragment_artivles_item_listview)
    ShimmerRecyclerView listview;
    @BindView(R.id.fragment_artivles_item_refresh)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.empty_image)
    ImageView empty;

    private int id;

    private int page = 1;

    private ArtivlesAdapter artivlesAdapter;

    public ArtivlesItemFragment() {
    }

    @SuppressLint("ValidFragment")
    public ArtivlesItemFragment(int id) {
        this.id = id;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_artivles_item_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        refreshLayout.setEnableLoadMore(false);

        //在此处修改布局排列方向
        LinearLayoutManager layoutManager = new LinearLayoutManager(context());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listview.setLayoutManager(layoutManager);

        artivlesAdapter = new ArtivlesAdapter(this);
        listview.setAdapter(artivlesAdapter);
        listview.showShimmerAdapter();
    }

    @Override
    protected void loadData() {
        getData();
    }

    @Override
    protected void initListener() {
        super.initListener();
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

    public void getData() {
        ArtivlesRequestBean bean = new ArtivlesRequestBean();
        bean.setPageNum(page);
        bean.setCommId(id);
        HttpApi.app().getArtivlesInfo(this, bean, new HttpCallback<ArtivlesResponseBean>() {
            @Override
            public void onSuccess(int code, String message, ArtivlesResponseBean data) {
                empty.setVisibility(View.GONE);
                if (page == 1) {
                    listview.hideShimmerAdapter();
                    refreshLayout.finishRefresh();
                    if (data.getPageInfoArticle().getList().isEmpty()) {
                        artivlesAdapter.clear();
                        refreshLayout.setEnableLoadMore(false);
                        empty.setVisibility(View.VISIBLE);
                        return;
                    }
                    artivlesAdapter.refresh(data.getPageInfoArticle().getList());
                } else {
                    refreshLayout.finishLoadMore();
                    artivlesAdapter.append(data.getPageInfoArticle().getList());
                }
                refreshLayout.setEnableLoadMore(data.getPageInfoArticle().getTotal() != artivlesAdapter.getDatas().size());
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindDrawables(listview);
    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }
}
