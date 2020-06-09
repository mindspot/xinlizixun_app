package module.order.councilor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.framework.page.Page;
import com.paopao.paopaostudio.R;

import java.util.ArrayList;

import base.BaseActivity;
import butterknife.BindView;
import ui.title.ToolBarTitleView;

public class UserCouncilorListActivity extends BaseActivity {
    @BindView(R.id.user_councilor_activity_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.user_councilor_activity_tabLayout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.user_councilor_activity_viewPager)
    ViewPager viewPager;
    private String[] titles = {"进行中", "已完成"};

    private ArrayList<Fragment> mFragments = new ArrayList<>();

    public static void newIntent(Page page) {
        page.startActivity(new Intent(page.activity(), UserCouncilorListActivity.class));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_user_councilor_list;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mFragments.add(new UserCouncilorListFragment(UserCouncilorListFragment.TYPE_ORDER_UNFINSH));
        mFragments.add(new UserCouncilorListFragment(UserCouncilorListFragment.TYPE_ORDER_FINSH));
        MyPagerAdapter mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        tabLayout.setViewPager(viewPager);
        viewPager.setCurrentItem(0);
        tabLayout.setCurrentTab(0);
        tabLayout.notifyDataSetChanged();
    }

    @Override
    public void initListener() {
        toolbar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void loadData() {

    }

    class MyPagerAdapter extends FragmentStatePagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
