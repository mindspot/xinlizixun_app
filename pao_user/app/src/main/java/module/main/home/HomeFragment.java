package module.main.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.framework.http.bean.HttpError;
import com.framework.utils.ConvertUtil;
import com.lib.image.loader.glide.GlideImageLoader;
import com.paopao.paopaouser.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.stx.xhb.xbanner.XBanner;

import java.util.ArrayList;
import java.util.List;

import base.BaseFragment;
import butterknife.BindView;
import butterknife.OnClick;
import common.events.TabEvent;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.home.BannerItemBean;
import common.repository.http.entity.home.HomeResponseBean;
import common.repository.http.param.BaseRequestBean;
import common.webview.page.WebViewActivity;
import de.greenrobot.event.EventBus;
import module.app.MyApplication;
import module.main.MainActivity;
import ui.MyGridView;

/**
 * Created by hpzhan on 2020/2/17.
 */

public class HomeFragment extends BaseFragment {

    @BindView(R.id.fragment_home_banner)
    XBanner banner;
    @BindView(R.id.fragment_home_menu_gridview)
    MyGridView menuGridview;
    @BindView(R.id.fragment_home_test_layout)
    LinearLayout testLayout;
    @BindView(R.id.fragment_home_test_recycler)
    RecyclerView testRecycler;
    @BindView(R.id.fragment_home_article_listView)
    ListView articleListView;
    @BindView(R.id.fragment_home_refresh)
    SmartRefreshLayout refreshLayout;

    private MenuAdapter menuAdapter;

    List<String> images;

    private HomeResponseBean mData;

    private TestListAdapter mTestListAdapter;
    private ArtivlesAdapter mArtivlesAdapter;

    @Override
    public void onResume() {
        super.onResume();
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        refreshLayout.setEnableLoadMore(false);
    }

    @Override
    protected void loadData() {
        refreshLayout.autoRefresh();
    }

    @Override
    protected void initListener() {
        super.initListener();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getData();
            }
        });
        banner.setOnItemClickListener(new XBanner.OnItemClickListener() {
            @Override
            public void onItemClick(XBanner banner, Object model, View view, int position) {
                WebViewActivity.newIntent(HomeFragment.this, mData.getIndexImages().get(position).getLink(), "", "", true);
            }
        });
    }

    public void getData() {
        HttpApi.app().getHomeInfo(this, new BaseRequestBean(), new HttpCallback<HomeResponseBean>() {
            @Override
            public void onSuccess(int code, String message, HomeResponseBean data) {
                refreshLayout.finishRefresh(0);
                if (code == 0) {
                    mData = data;
                    setBanner();
                    setMenuView();
                    setTestRecycler();
                    setArtivles();
                    return;
                }
                showToast(message);
            }

            @Override
            public void onFailed(HttpError error) {
                refreshLayout.finishRefresh(0);
                showToast(error.getErrMessage());
            }
        });
    }

    public void setBanner() {
        if (images == null) {
            images = new ArrayList<>();
        }
        images.clear();
        for (BannerItemBean bannerItem : mData.getIndexImages()) {
            images.add(bannerItem.getVal());
        }
        // 为XBanner绑定数据
        banner.setData(images, null);
        // XBanner适配数据
        //加载广告图片
        banner.loadImage(new XBanner.XBannerAdapter() {
            @Override
            public void loadBanner(XBanner banner, Object model, View view, int position) {
                GlideImageLoader.loadImageCustomCorner(HomeFragment.this, images.get(position), (ImageView) view, 0);
            }
        });
    }

    public void setMenuView() {
        if (menuAdapter == null) {
            menuAdapter = new MenuAdapter(this);
        }
        menuAdapter.refresh(mData.getClassification());
        int count = menuAdapter.getCount();
        int cloumn = 0;
        int screenWidth = ConvertUtil.getScreenWidth(context());
        int cloumWidth;
        if (count <= 6) {
            cloumn = 3;
            cloumWidth = screenWidth / cloumn;
        } else {
            cloumn = (count % 2 == 0) ? count / 2 : count / 2 + 1;
            cloumWidth = screenWidth / 4;
        }
        if (cloumn > 4) {
            screenWidth = screenWidth - 120;
            cloumWidth = screenWidth / 4;
            screenWidth = cloumWidth * cloumn;
        }
        menuGridview.setAdapter(menuAdapter);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        menuGridview.setLayoutParams(params);
        menuGridview.setColumnWidth(cloumWidth);
        menuGridview.setNumColumns(cloumn);

        MyApplication.app.setmClassify(mData.getClassification());
    }

    public void setTestRecycler() {
        if (1 == 1) {
            testLayout.setVisibility(View.GONE);
            return;
        }
        if (mData.getTestQuestions() == null || mData.getTestQuestions().size() == 0) {
            testLayout.setVisibility(View.GONE);
            return;
        }
        testLayout.setVisibility(View.VISIBLE);
        if (mTestListAdapter == null) {
            mTestListAdapter = new TestListAdapter(this);
            //在此处修改布局排列方向
            LinearLayoutManager layoutManager = new LinearLayoutManager(context());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            testRecycler.setLayoutManager(layoutManager);
            testRecycler.setAdapter(mTestListAdapter);
        }
        mTestListAdapter.refresh(mData.getTestQuestions());
    }

    public void setArtivles() {
        if (mArtivlesAdapter == null) {
            mArtivlesAdapter = new ArtivlesAdapter(this);
            articleListView.setAdapter(mArtivlesAdapter);
        }
        mArtivlesAdapter.refresh(mData.getArticles());
    }

    @OnClick({R.id.fragment_home_test_more, R.id.fragment_home_article_more})
    public void onViewClicked(View view) {
        if (isDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.fragment_home_test_more:
                toWebViewActivity(mData.getTestMoreUrl());
                break;
            case R.id.fragment_home_article_more:
                EventBus.getDefault().post(new TabEvent(TabEvent.TYPE_CHANGE_TAB, MainActivity.TAB_INDEX_ARTICLE));
                break;
        }
    }
}
