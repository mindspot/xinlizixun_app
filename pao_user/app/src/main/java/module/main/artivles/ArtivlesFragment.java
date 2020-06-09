package module.main.artivles;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.framework.http.bean.HttpError;
import com.paopao.paopaouser.R;

import java.util.List;

import base.BaseFragment;
import butterknife.BindView;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.artivles.ArtivlesResponseBean;
import common.repository.http.entity.home.MenuItemBean;
import common.repository.http.param.artivles.ArtivlesRequestBean;
import ui.title.ToolBarTitleView;
import ui.viewpager.SuperViewPager;

/**
 * Created by hpzhan on 2020/2/18.
 */

public class ArtivlesFragment extends BaseFragment {
    @BindView(R.id.fragment_artivles_title)
    ToolBarTitleView title;
    @BindView(R.id.fragment_artivles_tabLayout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.fragment_artivles_viewPager)
    SuperViewPager viewPager;
    private List<MenuItemBean> mClassify;

    private boolean isSuccess = false;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_artivles;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
    }

    @Override
    protected void loadData() {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void getData() {
        if (isSuccess) {
            return;
        }
        ArtivlesRequestBean bean = new ArtivlesRequestBean();
        bean.setPageNum(1);
        bean.setCommId(0);
        HttpApi.app().getArtivlesInfo(this, bean, new HttpCallback<ArtivlesResponseBean>() {
            @Override
            public void onSuccess(int code, String message, ArtivlesResponseBean data) {
                if (code == 0) {
                    if (mClassify != null) {
                        mClassify.clear();
                    }
                    mClassify = data.getClassification();
                    if (mClassify != null && mClassify.size() >= 0) {
                        MenuItemBean menuItemBean = new MenuItemBean();
                        menuItemBean.setId(0);
                        menuItemBean.setVal("推荐");
                        mClassify.add(0, menuItemBean);
                    }
                    if (mClassify.size() > 0) {
                        isSuccess = true;
                        MyPagerAdapter mAdapter = new MyPagerAdapter(getFragmentManager());
                        viewPager.setAdapter(mAdapter);
                        tabLayout.setViewPager(viewPager);
                        viewPager.setCurrentItem(0,false);
                        viewPager.setOffscreenPageLimit(1);
                        tabLayout.setCurrentTab(0);
                        tabLayout.notifyDataSetChanged();
                    }
                    return;
                }
                showToast(message);
            }

            @Override
            public void onFailed(HttpError error) {
                showToast(error.getErrMessage());
            }
        });
    }

    class MyPagerAdapter extends FragmentStatePagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mClassify.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mClassify.get(position).getVal();
        }

        @Override
        public Fragment getItem(int position) {
            return new ArtivlesItemFragment(mClassify.get(position).getId());
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isSuccess) {
            getData();
        }
    }
}
