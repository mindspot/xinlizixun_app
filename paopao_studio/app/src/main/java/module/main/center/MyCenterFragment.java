package module.main.center;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.framework.http.bean.HttpError;
import com.framework.utils.ConvertUtil;
import com.framework.utils.StringUtil;
import com.hyphenate.easeui.sqlite.UserTabUtil;
import com.lib.image.loader.glide.GlideImageLoader;
import com.paopao.paopaostudio.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import base.BaseFragment;
import base.UserCenter;
import butterknife.BindView;
import butterknife.OnClick;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.app.LabelItemBean;
import common.repository.http.entity.center.MyCenterResponseBean;
import common.repository.http.param.time.SetAllDayRestRequestBean;
import common.repository.share_preference.SPApi;
import im.IMHolder;
import module.app.MyApplication;
import module.balance.BalanceActivity;
import module.dialog.TipDialog;
import module.main.center.adapter.ServiceLabelAdapter;
import module.user.time.WorkPlanActivity;
import ui.MyListView;
import ui.title.ToolBarTitleView;
import util.MathOperationUtil;

/**
 * Created by hpzhan on 2020/2/18.
 */

public class MyCenterFragment extends BaseFragment {
    @BindView(R.id.fragment_mycenter_username)
    TextView username;
    @BindView(R.id.fragment_mycenter_user_signature)
    TextView userSignature;
    @BindView(R.id.fragment_mycenter_user_photo)
    ImageView userPhoto;
    @BindView(R.id.fragment_mycenter_user_yue_text)
    TextView yueText;
    @BindView(R.id.fragment_mycenter_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.fragment_mycenter_subscribe_switch)
    ImageView subscribeSwitch;
    @BindView(R.id.fragment_mycenter_listview)
    MyListView listview;
    @BindView(R.id.fragment_mycenter_refresh_layout)
    SmartRefreshLayout refreshLayout;

    private boolean isSwitch = false;

    private ServiceLabelAdapter mAdapter;

    private MyCenterResponseBean mData;

    private TipDialog tipDialog;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_mycenter;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        refreshLayout.setEnableLoadMore(false);
        mAdapter = new ServiceLabelAdapter(this);
        listview.setAdapter(mAdapter);

        tipDialog = new TipDialog(this);
        tipDialog.setTitle("泡泡工作室");
        tipDialog.setContent("开关显示“绿色”时，为“开启”状态；开启时，当日所有剩余的“未预约”时间段，不能继续被预约；已预约的时间段，仍需正常接单");
        tipDialog.setConfirm("确定");
    }

    @Override
    protected void loadData() {
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
    }

    public void getData() {
        HttpApi.app().getMyCenterInfo(this, new HttpCallback<MyCenterResponseBean>() {
            @Override
            public void onSuccess(int code, String message, MyCenterResponseBean data) {
                refreshLayout.finishRefresh();
                mData = data;
                setView();
            }

            @Override
            public void onFailed(HttpError error) {
                refreshLayout.finishRefresh();
                showToast(error.getErrMessage());
            }
        });
    }

    public void setView() {
        if (mData == null) {
            return;
        }
        SPApi.user().setLoginRealName(mData.getConsultants().getRealName());
        SPApi.user().setUserPhoto(mData.getConsultants().getHeadImg());
        SPApi.user().setSendWork(mData.getConsultants().getSendWord());
        username.setText(SPApi.user().getLoginRealName());
        userSignature.setText(SPApi.user().getSendWork());
        GlideImageLoader.loadImageCustomCorner(this, SPApi.user().getUserPhoto(), userPhoto, ConvertUtil.dip2px(context(), 23));
        IMHolder.getInstance().setUser(SPApi.user().getUserImAccount(), username.getText().toString(), SPApi.user().getUserPhoto());
        UserTabUtil.uploadUser(SPApi.user().getUserImAccount(), username.getText().toString(), SPApi.user().getUserPhoto(), true);
        mAdapter.clear();
        if (mData.getClassification() != null && !mData.getClassification().isEmpty()) {
            LabelItemBean itemBean = new LabelItemBean();
            itemBean.setTitle("专业领域");
            itemBean.setList(mData.getClassification());
            mAdapter.addData(itemBean);
        }
        if (mData.getContention() != null && !mData.getContention().isEmpty()) {
            LabelItemBean itemBean = new LabelItemBean();
            itemBean.setTitle("针对人群");
            itemBean.setList(mData.getContention());
            mAdapter.addData(itemBean);
        }
        if (mData.getWork() != null && !mData.getWork().isEmpty()) {
            LabelItemBean itemBean = new LabelItemBean();
            itemBean.setTitle("服务类型");
            itemBean.setList(mData.getWork());
            mAdapter.addData(itemBean);
        }
        yueText.setText("¥" + MathOperationUtil.divStr(mData.getConsultants().getAccount(), 100));
        isSwitch = mData.isStopBooking();
        subscribeSwitch.setImageResource(isSwitch ? R.mipmap.common_switch_open : R.mipmap.common_switch_close);
    }

    @OnClick({R.id.fragment_mycenter_user_layout, R.id.fragment_mycenter_yue_layout,
            R.id.fragment_mycenter_work_layout, R.id.fragment_mycenter_service_layout,
            R.id.fragment_mycenter_shoucang_layout, R.id.fragment_mycenter_jianyi_layout,
            R.id.fragment_mycenter_gongsi_layout, R.id.fragment_mycenter_lianxi_layout,
            R.id.fragment_mycenter_shezhi_layout,
            R.id.fragment_mycenter_subscribe_tip, R.id.fragment_mycenter_subscribe_switch,
            R.id.fragment_mycenter_visitor_layout, R.id.fragment_mycenter_client_layout})
    public void onViewClicked(View view) {
        if (isDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.fragment_mycenter_user_layout:
                UserInfoActivity.newIntent(this);
                break;
            case R.id.fragment_mycenter_yue_layout:
                BalanceActivity.newIntent(this);
                break;
            case R.id.fragment_mycenter_work_layout:
                WorkPlanActivity.newIntent(this);
                break;
            case R.id.fragment_mycenter_service_layout:
                String url = MyApplication.app.getServiceUrlResponseBean().getServiceSetInUrl().getVal();
                if (StringUtil.isBlank(url)) {
                    return;
                }
                url = url + UserCenter.instance().getUserInfo().getToken();
                toWebViewActivity(url);
                break;
            case R.id.fragment_mycenter_shoucang_layout:
                CollectActivity.newIntent(this);
                break;
            case R.id.fragment_mycenter_jianyi_layout:
                FeedbackActivity.newIntent(this);
                break;
            case R.id.fragment_mycenter_gongsi_layout:
                AboutActivity.newIntent(this);
                break;
            case R.id.fragment_mycenter_lianxi_layout:
                ServicePhoneActivity.newIntent(this);
                break;
            case R.id.fragment_mycenter_shezhi_layout:
                SettingActivity.newIntent(this);
                break;
            case R.id.fragment_mycenter_subscribe_tip:
                tipDialog.showMyDialog();
                break;
            case R.id.fragment_mycenter_subscribe_switch:
                switchPlan();
                break;
            case R.id.fragment_mycenter_visitor_layout:
                VisitorRecordActivity.newIntent(this);
                break;
            case R.id.fragment_mycenter_client_layout:
                break;
        }
    }

    public void switchPlan() {
        MyApplication.loadingDefault(activity());
        SetAllDayRestRequestBean restRequestBean = new SetAllDayRestRequestBean();
        restRequestBean.setTimeType(!isSwitch ? 2 : 0);
        HttpApi.app().setAllDayRest(this, restRequestBean, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                MyApplication.hideLoading();
                showToast(message);
                isSwitch = !isSwitch;
                subscribeSwitch.setImageResource(isSwitch ? R.mipmap.common_switch_open : R.mipmap.common_switch_close);
            }

            @Override
            public void onFailed(HttpError error) {
                MyApplication.hideLoading();
                showToast(error.getErrMessage());
            }
        });
    }

    @Override
    public void onShow() {
        super.onShow();
        getData();
    }
}
