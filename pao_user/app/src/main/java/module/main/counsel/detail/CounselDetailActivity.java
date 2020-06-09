package module.main.counsel.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.framework.http.bean.HttpError;
import com.framework.page.Page;
import com.framework.utils.ConvertUtil;
import com.framework.utils.StringUtil;
import com.lib.image.loader.glide.GlideImageLoader;
import com.paopao.paopaouser.R;

import java.util.ArrayList;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import common.events.OrderBuyEvent;
import common.repository.bean.IntroduceItemBean;
import common.repository.bean.TabEntity;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.counsel.detail.AboutConsultantBean;
import common.repository.http.entity.counsel.detail.CounselDetailResponseBean;
import common.repository.http.entity.counsel.detail.PersonalInforBean;
import common.repository.http.entity.counsel.detail.WorkInfoBean;
import common.repository.http.param.BaseResponseBean;
import common.repository.http.param.counsel.CollectRequestBean;
import de.greenrobot.event.EventBus;
import im.IMHolder;
import module.app.MyApplication;
import module.dialog.TipDialog;
import module.order.OrderTimeActivity;
import ui.CustomClickListener;
import ui.MyListView;
import ui.title.ToolBarTitleView;

public class CounselDetailActivity extends BaseActivity {

    @BindView(R.id.counsel_detail_activity_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.counsel_detail_activity_photo_img)
    ImageView photoImg;
    @BindView(R.id.counsel_detail_activity_name_text)
    TextView nameText;
    @BindView(R.id.counsel_detail_activity_address_text)
    TextView addressText;
    @BindView(R.id.counsel_detail_activity_jiyu_text)
    TextView jiyuText;
    @BindView(R.id.counsel_detail_activity_tabLayout)
    CommonTabLayout tabLayout;
    @BindView(R.id.counsel_detail_activity_recycler)
    RecyclerView listview;
    @BindView(R.id.counsel_detail_activity_collect_img)
    ImageView collectImg;
    @BindView(R.id.counsel_detail_activity_tlistview)
    MyListView tlistview;
    @BindView(R.id.counsel_detail_activity_root)
    LinearLayout counselDetailActivityRoot;
    @BindView(R.id.counsel_detail_activity_coordonator)
    CoordinatorLayout coordonator;
    @BindView(R.id.counsel_detail_activity_appbar)
    AppBarLayout appBar;

    private int mId;

    private IntroduceAdapter mAdapter;

    private CounselDetailResponseBean mData;

    private CounselServiceAdapter serviceAdapter;

    public static void newIntent(Page page, int id) {
        Intent intent = new Intent(page.activity(), CounselDetailActivity.class);
        intent.putExtra("id", id);
        page.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_counsel_detail;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        layoutManager = new LinearLayoutManager(this);
        listview.setLayoutManager(layoutManager);

        ArrayList<CustomTabEntity> mTabs = new ArrayList<>();
        mTabs.add(new TabEntity("个人简介"));
        mTabs.add(new TabEntity("个人资质"));
        mTabs.add(new TabEntity("服务须知"));
        tabLayout.setTabData(mTabs);
        if (getIntent() != null) {
            mId = getIntent().getIntExtra("id", 0);
        }
        mAdapter = new IntroduceAdapter(this);
        listview.setAdapter(mAdapter);

        serviceAdapter = new CounselServiceAdapter(this);
        tlistview.setAdapter(serviceAdapter);

        tipDialog = new TipDialog(this);
        tipDialog.setTitle("产品须知");
        tipDialog.setContent("咨询方式：音/视频，面对面；以咨询师展示的可选方式为准；\n" +
                "单次产品：咨询次数为1次，直接购买使用；\n" +
                "套餐产品：咨询次数为多次，购买套餐过程中默认使用1次；再次预约该咨询师的单次服务时，可直接使用套餐卡支付；可选的单次服务具体以咨询师展示的可选方式为准。套餐类产品的使用期限=次数*2*30（天）；例如购买一张“6次”的套餐卡，使用时间为6*2*30=360（天），及自购买之日起，360天之后，次数过期不可使用。");
        tipDialog.setConfirm("确定");
    }

    //判读是否是recyclerView主动引起的滑动，true- 是，false- 否，由tablayout引起的
    private boolean isRecyclerScroll;
    //记录上一次位置，防止在同一内容块里滑动 重复定位到tablayout
    private int lastPos;
    //用于recyclerView滑动到指定的位置
    private boolean canScroll;
    private int scrollToPosition;
    private LinearLayoutManager layoutManager;

    private TipDialog tipDialog;

    @Override
    public void initListener() {
        toolbar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setRightClickListener(new CustomClickListener() {
            @Override
            protected void onClick() {
                tipDialog.showMyDialog();
            }
        });
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int i) {
                isRecyclerScroll = false;
                moveToPosition(layoutManager, listview, i);
                appBar.setExpanded(false, true);
            }

            @Override
            public void onTabReselect(int i) {
                appBar.setExpanded(false, true);
            }
        });
        listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //当滑动由recyclerView触发时，isRecyclerScroll 置true
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    isRecyclerScroll = true;
                }
                return false;
            }
        });
        listview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isRecyclerScroll) {
                    //第一个可见的view的位置，即tablayou需定位的位置
                    int position = layoutManager.findFirstVisibleItemPosition();
                    if (lastPos != position) {
                        tabLayout.setCurrentTab(position);
                    }
                    lastPos = position;
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (canScroll) {
                    canScroll = false;
                    moveToPosition(layoutManager, recyclerView, scrollToPosition);
                }
            }
        });
        serviceAdapter.setOnItemClickListener(new CounselServiceAdapter.OnItemClickListener() {
            @Override
            public void onClick(WorkInfoBean data) {
                MyApplication.app.setWorkInfoBean(data);
                MyApplication.app.setUseTao(serviceAdapter.isTaoUse());
                MyApplication.app.setPersonalInforBean(mData.getPersonalInformation());
                OrderTimeActivity.newIntent(CounselDetailActivity.this);
            }
        });
    }

    public void moveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int position) {
        try {
            // 第一个可见的view的位置
            int firstItem = manager.findFirstVisibleItemPosition();
            // 最后一个可见的view的位置
            int lastItem = manager.findLastVisibleItemPosition();
            if (position <= firstItem) {
                // 如果跳转位置firstItem 之前(滑出屏幕的情况)，就smoothScrollToPosition可以直接跳转，
                mRecyclerView.smoothScrollToPosition(position);
            } else if (position <= lastItem) {
                // 跳转位置在firstItem 之后，lastItem 之间（显示在当前屏幕），smoothScrollBy来滑动到指定位置
                int top = mRecyclerView.getChildAt(position - firstItem).getTop();
                if (top == 0) {
                    mRecyclerView.smoothScrollToPosition(position);
                    scrollToPosition = position;
                    canScroll = true;
                    return;
                }
                mRecyclerView.smoothScrollBy(0, top);
            } else {
                // 如果要跳转的位置在lastItem 之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
                // 再通过onScrollStateChanged控制再次调用当前moveToPosition方法，执行上一个判断中的方法
                mRecyclerView.smoothScrollToPosition(position);
                scrollToPosition = position;
                canScroll = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        getData();
    }

    public void getData() {
        MyApplication.loadingDefault(activity());
        HttpApi.app().getCounselInfo(this, mId, new HttpCallback<CounselDetailResponseBean>() {
            @Override
            public void onSuccess(int code, String message, CounselDetailResponseBean data) {
                MyApplication.hideLoading();
                mData = data;
                setView();
            }

            @Override
            public void onFailed(HttpError error) {
                showToast(error.getErrMessage());
                MyApplication.hideLoading();
            }
        });
    }

    public void setView() {
        if (mData == null) {
            return;
        }
        GlideImageLoader.loadImageCustomCorner(this, mData.getPersonalInformation().getHeadImg(), photoImg, ConvertUtil.dip2px(context(), 35));
        nameText.setText(mData.getPersonalInformation().getRealName());
        String address = mData.getPersonalInformation().getProvName() + " " + mData.getPersonalInformation().getCityName();
        addressText.setText(StringUtil.isBlank(address) ? mData.getPersonalInformation().getAddrDetail() : address);
        jiyuText.setText(mData.getPersonalInformation().getSendWord());
        setCollectView();

        IntroduceItemBean bean = new IntroduceItemBean();
        bean.setTitle("个人简介：");
        bean.setType(1);
        bean.setContent(mData.getAboutConsultant().getPersonalIntroduction());
        mAdapter.addData(bean);

        IntroduceItemBean bean1 = new IntroduceItemBean();
        bean1.setTitle("个人资质：");
        bean1.setType(2);
        bean1.setQualifications(mData.getAboutConsultant().getQualifications());
        for (AboutConsultantBean.BeGoodAtBean item : mData.getAboutConsultant().getBeGoodAt()) {
            if (bean1.getLabels() == null) {
                bean1.setLabels(new ArrayList<>());
            }
            bean1.getLabels().add(item.getLabelVal());
        }
        mAdapter.addData(bean1);

        IntroduceItemBean bean3 = new IntroduceItemBean();
        bean3.setTitle("服务须知：");
        bean3.setType(3);
        bean3.setContent(mData.getAboutConsultant().getServiceInstructions().getContent().replace("\\n","\n"));
        mAdapter.addData(bean3);

        if (mData.getPaySetMeal() == 1) {
            serviceAdapter.setTaoUse(true);
        }
        serviceAdapter.refresh(mData.getWorkingMode());
    }

    public void insertCollent() {
        if (mData == null) {
            return;
        }
        MyApplication.loadingDefault(activity());
        CollectRequestBean bean = new CollectRequestBean();
        bean.setCollectionId(mId);
        HttpApi.app().insertCollect(this, bean, new HttpCallback<BaseResponseBean>() {
            @Override
            public void onSuccess(int code, String message, BaseResponseBean data) {
                MyApplication.hideLoading();
                showToast(message);
                mData.setCollected(!mData.isCollected());
                setCollectView();
            }

            @Override
            public void onFailed(HttpError error) {
                showToast(error.getErrMessage());
                MyApplication.hideLoading();
            }
        });
    }

    public void delCollent() {
        if (mData == null) {
            return;
        }
        MyApplication.loadingDefault(activity());
        HttpApi.app().delCollect(this, mId, new HttpCallback<BaseResponseBean>() {
            @Override
            public void onSuccess(int code, String message, BaseResponseBean data) {
                MyApplication.hideLoading();
                showToast(message);
                mData.setCollected(!mData.isCollected());
                setCollectView();
            }

            @Override
            public void onFailed(HttpError error) {
                showToast(error.getErrMessage());
                MyApplication.hideLoading();
            }
        });
    }

    public void setCollectView() {
        if (mData == null) {
            return;
        }
        collectImg.setImageResource(mData.isCollected() ?
                R.mipmap.mycenter_shoucang_icon :
                R.mipmap.counsel_detail_uncollection_icon);
    }

    @OnClick({R.id.counsel_detail_activity_collect_img, R.id.counsel_detail_activity_chat_btn})
    public void onViewClicked(View view) {
        if (isDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.counsel_detail_activity_collect_img:
                if (mData == null) {
                    return;
                }
                if (mData.isCollected()) {
                    delCollent();
                } else {
                    insertCollent();
                }
                break;
            case R.id.counsel_detail_activity_chat_btn:
                if (mData == null || mData.getPersonalInformation() == null) {
                    return;
                }
                PersonalInforBean bean = mData.getPersonalInformation();
                IMHolder.getInstance().gotoChat(context(), bean.getEasemobId(), bean.getRealName(), bean.getHeadImg());
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void onEventMainThread(OrderBuyEvent event) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
