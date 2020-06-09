package module.main.counsel.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.http.bean.HttpError;
import com.framework.page.Page;
import com.framework.utils.ConvertUtil;
import com.framework.utils.StringUtil;
import com.lib.image.loader.glide.GlideImageLoader;
import com.paopao.paopaostudio.R;

import java.util.ArrayList;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import common.events.OrderBuyEvent;
import common.repository.bean.IntroduceItemBean;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.counsel.detail.AboutConsultantBean;
import common.repository.http.entity.counsel.detail.CounselDetailResponseBean;
import common.repository.http.entity.counsel.detail.PersonalInforBean;
import common.repository.http.param.BaseResponseBean;
import common.repository.http.param.counsel.CollectRequestBean;
import de.greenrobot.event.EventBus;
import im.IMHolder;
import module.app.MyApplication;
import module.order.CouncilorOrderActivity;
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
    @BindView(R.id.counsel_detail_activity_recycler)
    RecyclerView listview;
    @BindView(R.id.counsel_detail_activity_collect_img)
    ImageView collectImg;
    @BindView(R.id.counsel_detail_activity_root)
    LinearLayout counselDetailActivityRoot;

    private int mId;

    private IntroduceAdapter mAdapter;

    private CounselDetailResponseBean mData;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

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
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listview.setLayoutManager(layoutManager);

        if (getIntent() != null) {
            mId = getIntent().getIntExtra("id", 0);
        }
        mAdapter = new IntroduceAdapter(this);
        listview.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
        toolbar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
        bean.setTitle("个人介绍：");
        bean.setType(1);
        bean.setContent(mData.getAboutConsultant().getPersonalIntroduction());
        mAdapter.addData(bean);

        IntroduceItemBean bean1 = new IntroduceItemBean();
        bean1.setTitle("个人资质：");
        bean1.setType(2);
        bean1.setQualifications(mData.getAboutConsultant().getQualifications());
        mAdapter.addData(bean1);

        IntroduceItemBean bean2 = new IntroduceItemBean();
        bean2.setTitle("擅长方向：");
        for (AboutConsultantBean.BeGoodAtBean item : mData.getAboutConsultant().getBeGoodAt()) {
            if (bean2.getLabels() == null) {
                bean2.setLabels(new ArrayList<>());
            }
            bean2.getLabels().add(item.getLabelVal());
        }
        bean2.setType(3);
        mAdapter.addData(bean2);

        IntroduceItemBean bean3 = new IntroduceItemBean();
        bean3.setTitle("服务须知：");
        bean3.setType(1);
        bean3.setContent(mData.getAboutConsultant().getServiceInstructions().getContent().replace("\\n","\n"));
        mAdapter.addData(bean3);
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

    @OnClick({R.id.counsel_detail_activity_collect_img, R.id.counsel_detail_activity_chat_btn,
            R.id.counsel_detail_activity_yuyue_btn})
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
            case R.id.counsel_detail_activity_yuyue_btn:
                if (mData == null || mData.getPersonalInformation() == null) {
                    return;
                }
                MyApplication.app.setPersonalInforBean(mData.getPersonalInformation());
                CouncilorOrderActivity.newIntent(CounselDetailActivity.this, mData.getPersonalInformation());
                break;
        }
    }

    public void onEventMainThread(OrderBuyEvent event) {
        finish();
    }
}
