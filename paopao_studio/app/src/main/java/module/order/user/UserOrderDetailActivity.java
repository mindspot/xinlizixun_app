package module.order.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import common.repository.http.entity.order.user.MemberCaseInfoBean;
import common.repository.http.entity.order.user.UserOrderDetailInfoBean;
import common.repository.http.entity.order.user.UserOrderDetailResponseBean;
import common.repository.http.param.order.ConfirmUserOrderRequestBean;
import common.repository.http.param.order.UserOrderDetailRequestBean;
import de.greenrobot.event.EventBus;
import im.IMHolder;
import im.chat.ChatActivity;
import module.app.MyApplication;
import module.main.MainActivity;
import module.order.InputCaseActivity;
import ui.title.ToolBarTitleView;
import util.MathOperationUtil;

public class UserOrderDetailActivity extends BaseActivity {

    @BindView(R.id.user_order_detail_activity_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.user_order_detail_photo)
    ImageView photo;
    @BindView(R.id.user_order_detail_username)
    TextView username;
    @BindView(R.id.user_order_detail_service_type)
    TextView serviceType;
    @BindView(R.id.user_order_detail_service_time)
    TextView serviceTime;
    @BindView(R.id.user_order_detail_order_status)
    TextView orderStatus;
    @BindView(R.id.user_order_detail_order_num)
    TextView orderNum;
    @BindView(R.id.user_order_detail_start_time)
    TextView startTime;
    @BindView(R.id.user_order_detail_subscribe_time)
    TextView subscribeTime;
    @BindView(R.id.user_order_detail_all_money)
    TextView allMoney;
    @BindView(R.id.user_order_detail_coupon_money)
    TextView couponMoney;
    @BindView(R.id.user_order_detail_money)
    TextView money;
    @BindView(R.id.user_order_detail_arrows)
    ImageView arrows;
    @BindView(R.id.user_order_detail_detail_layout)
    LinearLayout detailLayout;
    @BindView(R.id.user_order_detail_time)
    TextView detailTime;
    @BindView(R.id.user_order_detail_userinfo)
    TextView userinfo;
    @BindView(R.id.user_order_detail_yes_or_no)
    TextView yesOrNo;
    @BindView(R.id.user_order_detail_question)
    TextView question;
    @BindView(R.id.user_order_detail_describe)
    TextView describe;
    @BindView(R.id.user_order_detail_counselor_time)
    TextView counselorTime;
    @BindView(R.id.user_order_detail_counselor_username)
    TextView counselorUsername;
    @BindView(R.id.user_order_detail_counselor_describe)
    TextView counselorDescribe;
    @BindView(R.id.user_order_detail_one_image)
    ImageView oneImage;
    @BindView(R.id.user_order_detail_wo_image)
    ImageView twoImage;
    @BindView(R.id.user_order_detail_hree_image)
    ImageView threeImage;
    @BindView(R.id.user_order_detail_image_layout)
    LinearLayout imageLayout;
    @BindView(R.id.user_order_detail_case_layout)
    LinearLayout caseDetailLayout;
    @BindView(R.id.user_order_detail_btn)
    TextView btn;
    @BindView(R.id.user_order_detail_bianji_btn)
    TextView bianjiBtn;
    @BindView(R.id.user_order_detail_chat)
    TextView chatBtn;

    private String mOrderNo;

    private UserOrderDetailResponseBean mData;

    private boolean isShow = false;

    private boolean isChat = true;

    public static void newIntent(Context context, String orderNo) {
        newIntent(context, orderNo, true);
    }

    public static void newIntent(Context context, String orderNo, boolean isChat) {
        Intent intent = new Intent(context, UserOrderDetailActivity.class);
        intent.putExtra("orderNo", orderNo);
        intent.putExtra("isChat", isChat);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_user_order_detail;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (getIntent() != null) {
            mOrderNo = getIntent().getStringExtra("orderNo");
            isChat = getIntent().getBooleanExtra("isChat", isChat);
        }
        caseDetailLayout.setVisibility(View.GONE);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    public void getData() {
        MyApplication.loadingDefault(activity());
        UserOrderDetailRequestBean bean = new UserOrderDetailRequestBean();
        bean.setOrderNo(mOrderNo);
        HttpApi.app().getUserOrderDetail(this, bean, new HttpCallback<UserOrderDetailResponseBean>() {
            @Override
            public void onSuccess(int code, String message, UserOrderDetailResponseBean data) {
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
        UserOrderDetailInfoBean orderInfo = mData.getOrderInfo();
        GlideImageLoader.loadGlideImageCustomCorner(this, orderInfo.getHeadImg(), photo, 23);
        username.setText(orderInfo.getRealName());
        serviceType.setText(orderInfo.getGoodsClassName());
        serviceTime.setText(orderInfo.getOrderTime());
        btn.setVisibility(View.VISIBLE);
        bianjiBtn.setVisibility(View.GONE);
        switch (orderInfo.getOrderCode()) {
            case 1://未支付
                orderStatus.setText("未支付");
                orderStatus.setTextColor(Color.parseColor("#999999"));
                btn.setVisibility(View.GONE);
                break;
            case 2://未支付
                orderStatus.setText("已取消");
                orderStatus.setTextColor(Color.parseColor("#999999"));
                btn.setVisibility(View.GONE);
                break;
            case 3://已支付
                orderStatus.setText("已支付");
                orderStatus.setTextColor(Color.parseColor("#9DDCAF"));
                btn.setText("确认收到订单");
                break;
            case 4://已完成
                orderStatus.setText("已完结");
                orderStatus.setTextColor(Color.parseColor("#9DDCAF"));
                btn.setText("找督导师");
                bianjiBtn.setVisibility(View.VISIBLE);
                break;
            case 5://已确定
                orderStatus.setText("已确定");
                orderStatus.setTextColor(Color.parseColor("#666666"));
                btn.setText("编辑个案");
                break;
            default:
                btn.setVisibility(View.GONE);
        }
        orderNum.setText(orderInfo.getOrderNo());
        startTime.setText(orderInfo.getOrderTime());
        subscribeTime.setText(orderInfo.getExt());
        allMoney.setText("¥" + MathOperationUtil.divStr(orderInfo.getOrderAmount(), 100));
        couponMoney.setText(orderInfo.getDiscountAmount() == 0 ? "无" : "¥" + MathOperationUtil.divStr(orderInfo.getDiscountAmount(), 100));
        money.setText("¥" + MathOperationUtil.divStr(MathOperationUtil.sub(orderInfo.getOrderAmount(), orderInfo.getDiscountAmount()), 100));

        MemberCaseInfoBean memberCase = mData.getMemberCase();
        if (memberCase == null) {
            return;
        }
        detailTime.setText(orderInfo.getOrderTime().split(" ")[0]);
        userinfo.setText("姓名：" + memberCase.getOperName() + "  性别：" + (memberCase.getSex() == 0 ? "男" : "女") + "  年龄：" + memberCase.getAge());
        yesOrNo.setText("是否接受过咨询：" + (memberCase.isConsultant() ? "是" : "否"));
        String questionStr = "咨询问题：";
        for (MemberCaseInfoBean.CommonItemBean itemBean : memberCase.getCommonVOs()) {
            questionStr += itemBean.getVal() + ",";
        }
        if (questionStr.indexOf(",") != -1) {
            questionStr = questionStr.substring(0, questionStr.lastIndexOf(","));
        }
        question.setText(questionStr);
        describe.setText("详情描述：" + memberCase.getDetailedDescription());

        imageLayout.setVisibility(View.VISIBLE);
        oneImage.setVisibility(View.GONE);
        twoImage.setVisibility(View.GONE);
        threeImage.setVisibility(View.GONE);
        counselorTime.setText("");
        counselorUsername.setText("");
        counselorDescribe.setText("");

        if (!mData.isDiagnosis()) {
            return;
        }
        counselorTime.setText(memberCase.getConsultationTime());
        counselorUsername.setText("咨询师：" + memberCase.getConsultatName());
        counselorDescribe.setText(memberCase.getConsultantOrderDiagnosisVO().getContent());
        if (memberCase.getConsultantOrderDiagnosisVOs().isEmpty()) {
            imageLayout.setVisibility(View.GONE);
            return;
        }

        if (memberCase.getConsultantOrderDiagnosisVOs().size() >= 1) {
            oneImage.setVisibility(View.VISIBLE);
            MemberCaseInfoBean.ConsultantOrderDiagnosisBean bean = memberCase.getConsultantOrderDiagnosisVOs().get(0);
            GlideImageLoader.loadImageView(this, bean.getContent(), oneImage);
            GlideImageLoader.preloadImage(this, bean.getContent().replace(bean.getImgeSize(), ""));
        }
        if (memberCase.getConsultantOrderDiagnosisVOs().size() >= 2) {
            twoImage.setVisibility(View.VISIBLE);
            MemberCaseInfoBean.ConsultantOrderDiagnosisBean bean = memberCase.getConsultantOrderDiagnosisVOs().get(1);
            GlideImageLoader.loadImageView(this, bean.getContent(), twoImage);
            GlideImageLoader.preloadImage(this, bean.getContent().replace(bean.getImgeSize(), ""));
        }
        if (memberCase.getConsultantOrderDiagnosisVOs().size() >= 3) {
            threeImage.setVisibility(View.VISIBLE);
            MemberCaseInfoBean.ConsultantOrderDiagnosisBean bean = memberCase.getConsultantOrderDiagnosisVOs().get(2);
            GlideImageLoader.loadImageView(this, bean.getContent(), threeImage);
            GlideImageLoader.preloadImage(this, bean.getContent().replace(bean.getImgeSize(), ""));
        }
    }

    @OnClick({R.id.user_order_detail_chat, R.id.user_order_detail_detail_layout,
            R.id.user_order_detail_btn, R.id.user_order_detail_bianji_btn,
            R.id.user_order_detail_one_image, R.id.user_order_detail_wo_image,
            R.id.user_order_detail_hree_image})
    public void onViewClicked(View view) {
        if (isDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.user_order_detail_chat:
                if (mData != null) {
                    ChatActivity.newIntent(context(), mData.getOrderInfo().getEasemobId(),
                            mData.getOrderInfo().getRealName(), mData.getOrderInfo().getHeadImg());
                }
                break;
            case R.id.user_order_detail_detail_layout:
                isShow = !isShow;
                caseDetailLayout.setVisibility(isShow ? View.VISIBLE : View.GONE);
                arrows.setImageResource(isShow ? R.mipmap.common_up_arrows_icon : R.mipmap.common_down_arrows_icon);
                break;
            case R.id.user_order_detail_btn:
                switch (mData.getOrderInfo().getOrderCode()) {
                    case 3://已支付
                        confirmOrder();
                        break;
                    case 4://已完成
                        Intent intent = new Intent(activity(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        EventBus.getDefault().post(new TabEvent(TabEvent.TYPE_CHANGE_TAB, MainActivity.TAB_INDEX_DUDAO));
                        break;
                    case 5://已确定
                        InputCaseActivity.newIntent(this, mOrderNo, mData.getMemberCase());
                        break;
                    default:
                        btn.setVisibility(View.GONE);
                }
                break;
            case R.id.user_order_detail_bianji_btn:
                InputCaseActivity.newIntent(this, mOrderNo, mData.getMemberCase());
                break;
            case R.id.user_order_detail_one_image:
                showBigImage(0);
                break;
            case R.id.user_order_detail_wo_image:
                showBigImage(1);
                break;
            case R.id.user_order_detail_hree_image:
                showBigImage(2);
                break;
        }
    }

    public void showBigImage(int index) {
        MemberCaseInfoBean memberCase = mData.getMemberCase();
        if (memberCase.getConsultantOrderDiagnosisVOs().isEmpty()) {
            return;
        }
        if (memberCase.getConsultantOrderDiagnosisVOs().size() >= index) {
            MemberCaseInfoBean.ConsultantOrderDiagnosisBean bean = memberCase.getConsultantOrderDiagnosisVOs().get(index);
            IMHolder.getInstance().showBigImage(this, null, bean.getContent().replace(bean.getImgeSize(), ""));
        }
    }

    public void confirmOrder() {
        MyApplication.loadingDefault(activity());
        ConfirmUserOrderRequestBean bean = new ConfirmUserOrderRequestBean();
        bean.setOrderNo(mOrderNo);
        HttpApi.app().confirmUserOrder(this, bean, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                MyApplication.hideLoading();
                showToast(message);
                getData();
            }

            @Override
            public void onFailed(HttpError error) {
                MyApplication.hideLoading();
                showToast(error.getErrMessage());
            }
        });
    }

}
