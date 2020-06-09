package module.order;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.http.bean.HttpError;
import com.lib.image.loader.glide.GlideImageLoader;
import com.paopao.paopaostudio.R;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import common.events.TabEvent;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.order.councilor.CouncilorOrderDetailResponseBean;
import common.repository.http.param.order.AcceptCouncilorOrderRequestBean;
import common.repository.http.param.order.CancelCouncilorOrderRequestBean;
import common.repository.http.param.order.CouncilorOrderDetailRequestBean;
import common.repository.http.param.order.FinshCouncilorOrderRequestBean;
import common.repository.http.param.order.RefuseCouncilorOrderRequestBean;
import de.greenrobot.event.EventBus;
import im.chat.ChatActivity;
import module.app.MyApplication;
import module.main.MainActivity;
import ui.MyListView;
import ui.title.ToolBarTitleView;
import util.MathOperationUtil;

public class CouncilorOrderDetailActivity extends BaseActivity {

    @BindView(R.id.councilor_order_detail_activity_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.councilor_order_detail_photo)
    ImageView photo;
    @BindView(R.id.councilor_order_detail_username)
    TextView username;
    @BindView(R.id.councilor_order_detail_service_type)
    TextView serviceType;
    @BindView(R.id.councilor_order_detail_service_time)
    TextView serviceTime;
    @BindView(R.id.councilor_order_detail_order_status)
    TextView orderStatus;
    @BindView(R.id.councilor_order_detail_order_num)
    TextView orderNum;
    @BindView(R.id.councilor_order_detail_order_case_num)
    TextView caseNum;
    @BindView(R.id.councilor_order_detail_order_money_ratio)
    TextView moneyRatio;
    @BindView(R.id.councilor_order_detail_councilor_way)
    TextView councilorWay;
    @BindView(R.id.councilor_order_detail_councilor_type)
    TextView councilorType;
    @BindView(R.id.councilor_order_detail_all_money)
    TextView allMoney;
    @BindView(R.id.councilor_order_detail_money)
    TextView detailMoney;
    @BindView(R.id.councilor_order_detail_councilor_listview)
    MyListView councilorListview;
    @BindView(R.id.councilor_order_detail_cancle_btn)
    TextView cancleBtn;
    @BindView(R.id.councilor_order_detail_refuse_btn)
    TextView refuseBtn;
    @BindView(R.id.councilor_order_detail_accept_btn)
    TextView acceptBtn;
    @BindView(R.id.councilor_order_detail_btn_layout)
    LinearLayout btnLayout;
    @BindView(R.id.councilor_order_detail_chat)
    TextView chatBtn;

    private String mOrderNum;
    private int mType = 1;//1=我发起的督导订单 2=我收到的督导订单

    private OrderCaseListAdapter mAdapter;

    private CouncilorOrderDetailResponseBean mData;

    private String mUserName;
    private String mHeadImg;

    private boolean isChat = true;

    public static void newIntent(Context context, String orderNo, int type) {
        newIntent(context, orderNo, type, true);
    }

    public static void newIntent(Context context, String orderNo, int type, boolean isChat) {
        Intent intent = new Intent(context, CouncilorOrderDetailActivity.class);
        intent.putExtra("orderNo", orderNo);
        intent.putExtra("type", type);
        intent.putExtra("isChat", isChat);
        context.startActivity(intent);
    }

    public static void newIntent(Context context, String orderNo, int type, String username, String headimg, boolean isChat) {
        Intent intent = new Intent(context, CouncilorOrderDetailActivity.class);
        intent.putExtra("orderNo", orderNo);
        intent.putExtra("type", type);
        intent.putExtra("username", username);
        intent.putExtra("headimg", headimg);
        intent.putExtra("isChat", isChat);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_councilor_order_detail;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (getIntent() != null) {
            mOrderNum = getIntent().getStringExtra("orderNo");
            mType = getIntent().getIntExtra("type", mType);
            mUserName = getIntent().getStringExtra("username");
            mHeadImg = getIntent().getStringExtra("headimg");
            isChat = getIntent().getBooleanExtra("isChat", isChat);
        }
        mAdapter = new OrderCaseListAdapter(this);
        councilorListview.setAdapter(mAdapter);
        chatBtn.setVisibility(isChat ? View.VISIBLE : View.GONE);
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
        CouncilorOrderDetailRequestBean bean = new CouncilorOrderDetailRequestBean();
        bean.setOrderNo(mOrderNum);
        MyApplication.loadingDefault(activity());
        HttpApi.app().getCouncilorOrderDetail(this, mType, bean, new HttpCallback<CouncilorOrderDetailResponseBean>() {
            @Override
            public void onSuccess(int code, String message, CouncilorOrderDetailResponseBean data) {
                MyApplication.hideLoading();
                mData = data;
                setView();
            }

            @Override
            public void onFailed(HttpError error) {
                MyApplication.hideLoading();
                showToast(error.getErrMessage());
            }
        });
    }

    public void setView() {
        if (mData == null) {
            return;
        }
        if (mType == 1) {
            GlideImageLoader.loadGlideImageCustomCorner(context(), mData.getHeadImg(), photo, 23);
            username.setText(mData.getRealName());
        } else if (mType == 2 && mData.getListSupervisorOrderDetails() != null) {
            GlideImageLoader.loadGlideImageCustomCorner(context(), mData.getHeadImg(), photo, 23);
            username.setText(mData.getRealName());
        }
        serviceType.setText(mData.getGoodsName());
        serviceTime.setText(mData.getOrderTime());

        btnLayout.setVisibility(View.GONE);
        cancleBtn.setVisibility(View.GONE);
        refuseBtn.setVisibility(View.GONE);
        acceptBtn.setVisibility(View.GONE);

        switch (mData.getOrderCode()) {
            case 1://已申请
                orderStatus.setText("已申请");
                orderStatus.setTextColor(Color.parseColor("#333333"));
                btnLayout.setVisibility(View.VISIBLE);
                if (mType == 1) {
                    cancleBtn.setVisibility(View.VISIBLE);
                    cancleBtn.setText("取消订单");
                } else {
                    refuseBtn.setVisibility(View.VISIBLE);
                    acceptBtn.setVisibility(View.VISIBLE);
                }
                break;
            case 2://已取消
                orderStatus.setText("已取消");
                orderStatus.setTextColor(Color.parseColor("#999999"));
                break;
            case 4://已督导
                orderStatus.setText("已督导");
                orderStatus.setTextColor(Color.parseColor("#9DDCAF"));
                if (mType == 1) {
                    btnLayout.setVisibility(View.VISIBLE);
                    cancleBtn.setVisibility(View.VISIBLE);
                    cancleBtn.setText("编辑本次督导意见");
                }
                break;
            case 5://已接单
                orderStatus.setText("已接单");
                orderStatus.setTextColor(Color.parseColor("#9DDCAF"));
                btnLayout.setVisibility(View.VISIBLE);
                cancleBtn.setVisibility(View.VISIBLE);
                if (mType == 1) {
                    cancleBtn.setText("取消订单");
                } else {
                    cancleBtn.setText("督导完毕");
                }
                break;
            case 11://已拒绝
                orderStatus.setText("已拒绝");
                orderStatus.setTextColor(Color.parseColor("#333333"));
                if (mType == 1) {
                    cancleBtn.setText("选择其他督导师");
                    btnLayout.setVisibility(View.VISIBLE);
                    cancleBtn.setVisibility(View.VISIBLE);
                }
                break;
        }

        orderNum.setText(mData.getOrderNo());
        caseNum.setText(mData.getSupervisorOrderNum() + "");
        moneyRatio.setText(mData.getProportion() + "%");
        councilorWay.setText(mData.getGoodsName());
        councilorType.setText(TextUtils.join(",", mData.getLabels()));
        allMoney.setText("¥" + MathOperationUtil.divStr(mData.getGoodsAmount(), 100));
        detailMoney.setText("¥" + MathOperationUtil.divStr(mData.getOrderAmount(), 100));
        mAdapter.refresh(mData.getListSupervisorOrderDetails());
        try {
            for (CouncilorOrderDetailResponseBean.SupervisorOrderDetailBean item : mData.getListSupervisorOrderDetails()) {
                CouncilorOrderDetailResponseBean.MemberCaseBean memberCaseBean = item.getMemberCase();
                for (CouncilorOrderDetailResponseBean.ConsultantOrderDiagnosisBean imgItem : memberCaseBean.getConsultantOrderDiagnosisVOs()) {
                    GlideImageLoader.preloadImage(this, imgItem.getContent().replace(imgItem.getImgeSize(), ""));
                }
            }
        } catch (Exception ex) {
        }
    }

    @OnClick({R.id.councilor_order_detail_cancle_btn, R.id.councilor_order_detail_refuse_btn,
            R.id.councilor_order_detail_accept_btn, R.id.councilor_order_detail_chat})
    public void onViewClicked(View view) {
        if (isDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.councilor_order_detail_cancle_btn:
                switch (mData.getOrderCode()) {
                    case 1://已申请
                        if (mType == 1) {//取消订单
                            cancelOrder();
                        }
                        break;
                    case 4://已督导
                        if (mType == 1) {//编辑督导意见
                            InputCaseActivity.newIntent(this, mOrderNum);
                        }
                        break;
                    case 5://已接单
                        if (mType == 1) {//取消订单
                            cancelOrder();
                        } else {//督导完毕
                            finshOrder();
                        }
                        break;
                    case 11://已拒绝
                        if (mType == 1) {//选择其他督导师
                            EventBus.getDefault().post(new TabEvent(TabEvent.TYPE_CHANGE_TAB, MainActivity.TAB_INDEX_DUDAO));
                            Intent intent = new Intent(activity(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                        }
                        break;
                }
                break;
            case R.id.councilor_order_detail_refuse_btn://拒单
                refuseOrder();
                break;
            case R.id.councilor_order_detail_accept_btn://接单
                acceptOrder();
                break;
            case R.id.councilor_order_detail_chat:
                if (mData != null) {
                    ChatActivity.newIntent(context(), mData.getEasemobId(), mData.getRealName(), mData.getHeadImg());
                }
                break;
        }
    }

    /**
     * 取消订单
     */
    public void cancelOrder() {
        MyApplication.loadingDefault(activity());
        CancelCouncilorOrderRequestBean bean = new CancelCouncilorOrderRequestBean();
        bean.setOrderNo(mOrderNum);
        HttpApi.app().cancelCouncilorOrder(this, bean, callback);
    }

    /**
     * 完成订单
     */
    public void finshOrder() {
        MyApplication.loadingDefault(activity());
        FinshCouncilorOrderRequestBean bean = new FinshCouncilorOrderRequestBean();
        bean.setOrderNo(mOrderNum);
        HttpApi.app().finishCouncilorOrder(this, bean, callback);
    }

    /**
     * 拒绝订单
     */
    public void refuseOrder() {
        MyApplication.loadingDefault(activity());
        RefuseCouncilorOrderRequestBean bean = new RefuseCouncilorOrderRequestBean();
        bean.setOrderNo(mOrderNum);
        bean.setType(0);
        HttpApi.app().refuseCouncilorOrder(this, bean, callback);
    }

    /**
     * 接受订单
     */
    public void acceptOrder() {
        MyApplication.loadingDefault(activity());
        AcceptCouncilorOrderRequestBean bean = new AcceptCouncilorOrderRequestBean();
        bean.setOrderNo(mOrderNum);
        bean.setType(1);
        HttpApi.app().acceptCouncilorOrder(this, bean, callback);
    }

    private HttpCallback<String> callback = new HttpCallback<String>() {
        @Override
        public void onSuccess(int code, String message, String data) {
            MyApplication.hideLoading();
            showToast(message);
            loadData();
        }

        @Override
        public void onFailed(HttpError error) {
            MyApplication.hideLoading();
            showToast(error.getErrMessage());
        }
    };
}
